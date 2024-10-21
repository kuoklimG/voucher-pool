package com.kl.voucher_pool;

import com.kl.voucher_pool.model.VoucherCode;
import com.kl.voucher_pool.service.VoucherPoolService;
import com.kl.voucher_pool.controller.VoucherPoolController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class VoucherPoolControllerTest {

    @Mock
    private VoucherPoolService voucherPoolService;

    @InjectMocks
    private VoucherPoolController voucherPoolController;

    private VoucherCode voucherCode;

    @BeforeEach
    void setUp() {
        voucherCode = new VoucherCode("TESTCODE", "1", "1", LocalDate.now().plusDays(30));
    }

    @Test
    void testGenerateVoucher_Success() {
        when(voucherPoolService.generateVoucherCode(anyString(), anyString(), any(LocalDate.class)))
                .thenReturn(voucherCode);

        ResponseEntity<Map<String, Object>> response = voucherPoolController.generateVoucher(
                "test@example.com", "Test Offer", LocalDate.now().plusDays(30));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("TESTCODE", response.getBody().get("code"));
    }

    @Test
    void testGenerateVoucher_Failure() {
        when(voucherPoolService.generateVoucherCode(anyString(), anyString(), any(LocalDate.class)))
                .thenThrow(new IllegalArgumentException("Invalid input"));

        ResponseEntity<Map<String, Object>> response = voucherPoolController.generateVoucher(
                "test@example.com", "Test Offer", LocalDate.now().plusDays(30));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid input", response.getBody().get("error"));
    }

    @Test
    void testValidateVoucher_Success() {
        Map<String, Object> validationResult = Map.of("discount", 10.0, "offerName", "Test Offer");
        when(voucherPoolService.validateVoucherCode(anyString(), anyString())).thenReturn(validationResult);

        ResponseEntity<Map<String, Object>> response = voucherPoolController.validateVoucher("TESTCODE", "test@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10.0, response.getBody().get("discount"));
        assertEquals("Test Offer", response.getBody().get("offerName"));
    }

    @Test
    void testValidateVoucher_Failure() {
        when(voucherPoolService.validateVoucherCode(anyString(), anyString()))
                .thenThrow(new IllegalArgumentException("Invalid voucher"));

        ResponseEntity<Map<String, Object>> response = voucherPoolController.validateVoucher("INVALIDCODE", "test@example.com");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid voucher", response.getBody().get("error"));
    }

    @Test
    void testGetValidVouchers_Success() {
        List<String> validVouchers = Arrays.asList("VOUCHER1", "VOUCHER2");
        when(voucherPoolService.getValidVoucherCodes(anyString())).thenReturn(validVouchers);

        ResponseEntity<Map<String, Object>> response = voucherPoolController.getValidVouchers("test@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("vouchers"));
        assertEquals(validVouchers, response.getBody().get("vouchers"));
    }

    @Test
    void testGetValidVouchers_Failure() {
        when(voucherPoolService.getValidVoucherCodes(anyString()))
                .thenThrow(new IllegalArgumentException("Invalid email"));

        ResponseEntity<Map<String, Object>> response = voucherPoolController.getValidVouchers("invalid@example.com");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid email", response.getBody().get("error"));
    }
}