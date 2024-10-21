package com.kl.voucher_pool;

import com.kl.voucher_pool.model.Recipient;
import com.kl.voucher_pool.model.SpecialOffer;
import com.kl.voucher_pool.model.VoucherCode;
import com.kl.voucher_pool.repository.RecipientRepository;
import com.kl.voucher_pool.repository.SpecialOfferRepository;
import com.kl.voucher_pool.repository.VoucherCodeRepository;
import com.kl.voucher_pool.service.VoucherPoolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class VoucherPoolServiceTest {

    @Mock
    private RecipientRepository recipientRepository;

    @Mock
    private SpecialOfferRepository specialOfferRepository;

    @Mock
    private VoucherCodeRepository voucherCodeRepository;

    @InjectMocks
    private VoucherPoolService voucherPoolService;

    private Recipient recipient;
    private SpecialOffer specialOffer;
    private VoucherCode voucherCode;

    @BeforeEach
    void setUp() {
        recipient = new Recipient("test@example.com", "Test User");
        recipient.setId("1");
        specialOffer = new SpecialOffer("Test Offer", 10.0);
        specialOffer.setId("1");
        voucherCode = new VoucherCode("TESTCODE", "1", "1", LocalDate.now().plusDays(30));
    }

    @Test
    void testGenerateVoucherCode_Success() {
        when(recipientRepository.findByEmail(anyString())).thenReturn(Optional.of(recipient));
        when(specialOfferRepository.findByName(anyString())).thenReturn(Optional.of(specialOffer));
        when(voucherCodeRepository.save(any(VoucherCode.class))).thenReturn(voucherCode);

        VoucherCode result = voucherPoolService.generateVoucherCode("test@example.com", "Test Offer", LocalDate.now().plusDays(30));

        assertNotNull(result);
        assertEquals("TESTCODE", result.getCode());
        verify(voucherCodeRepository, times(1)).save(any(VoucherCode.class));
    }

    @Test
    void testGenerateVoucherCode_RecipientNotFound() {
        when(recipientRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        String nonexistentEmail = "nonexistent@example.com";
        LocalDate expirationDate = LocalDate.now().plusDays(30);
        
        assertThrows(IllegalArgumentException.class, () -> {
            voucherPoolService.generateVoucherCode(nonexistentEmail, "Test Offer", expirationDate);
        });
    }

    @Test
    void testValidateVoucherCode_Success() {
        when(voucherCodeRepository.findByCode(anyString())).thenReturn(Optional.of(voucherCode));
        when(recipientRepository.findById(anyString())).thenReturn(Optional.of(recipient));
        when(specialOfferRepository.findById(anyString())).thenReturn(Optional.of(specialOffer));

        Map<String, Object> result = voucherPoolService.validateVoucherCode("TESTCODE", "test@example.com");

        assertNotNull(result);
        assertEquals(10.0, result.get("discount"));
        assertEquals("Test Offer", result.get("offerName"));
        verify(voucherCodeRepository, times(1)).save(any(VoucherCode.class));
    }

    @Test
    void testValidateVoucherCode_InvalidCode() {
        when(voucherCodeRepository.findByCode(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                voucherPoolService.validateVoucherCode("INVALIDCODE", "test@example.com"));
    }

    @Test
    void testValidateVoucherCode_ExpiredCode() {
        voucherCode.setExpirationDate(LocalDate.now().minusDays(1));
        when(voucherCodeRepository.findByCode(anyString())).thenReturn(Optional.of(voucherCode));
        when(recipientRepository.findById(anyString())).thenReturn(Optional.of(recipient));

        assertThrows(IllegalArgumentException.class, () ->
                voucherPoolService.validateVoucherCode("TESTCODE", "test@example.com"));
    }

    @Test
    void testGetValidVoucherCodes_Success() {
        when(recipientRepository.findByEmail(anyString())).thenReturn(Optional.of(recipient));
        when(voucherCodeRepository.findByRecipientIdAndExpirationDateAfterAndUsageDateIsNull(anyString(), any(LocalDate.class)))
                .thenReturn(List.of(voucherCode));
        when(specialOfferRepository.findById(anyString())).thenReturn(Optional.of(specialOffer));

        List<String> result = voucherPoolService.getValidVoucherCodes("test@example.com");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TESTCODE - Test Offer", result.get(0));
    }

    @Test
    void testGetValidVoucherCodes_RecipientNotFound() {
        when(recipientRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                voucherPoolService.getValidVoucherCodes("nonexistent@example.com"));
    }

    @Test
    void testUpdateSpecialOfferDiscount_Success() {
        when(specialOfferRepository.findById(anyString())).thenReturn(Optional.of(specialOffer));
        when(specialOfferRepository.save(any(SpecialOffer.class))).thenReturn(specialOffer);

        SpecialOffer result = voucherPoolService.updateSpecialOfferDiscount("1", 15.0);

        assertNotNull(result);
        assertEquals(15.0, result.getDiscountPercentage());
        verify(specialOfferRepository, times(1)).save(any(SpecialOffer.class));
    }

    @Test
    void testUpdateSpecialOfferDiscount_OfferNotFound() {
        when(specialOfferRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                voucherPoolService.updateSpecialOfferDiscount("nonexistent", 15.0));
    }
}
