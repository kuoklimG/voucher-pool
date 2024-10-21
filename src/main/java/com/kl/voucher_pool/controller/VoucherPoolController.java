package com.kl.voucher_pool.controller;

import com.kl.voucher_pool.model.VoucherCode;
import com.kl.voucher_pool.service.VoucherPoolService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherPoolController {

    private final VoucherPoolService voucherPoolService;

    public VoucherPoolController(VoucherPoolService voucherPoolService) {
        this.voucherPoolService = voucherPoolService;
    }

    private static final String ERROR_KEY = "error";

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateVoucher(
            @RequestParam String email,
            @RequestParam String specialOffer,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expirationDate) {
        try {
            VoucherCode voucherCode = voucherPoolService.generateVoucherCode(email, specialOffer, expirationDate);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("code", voucherCode.getCode()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(ERROR_KEY, e.getMessage()));
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateVoucher(
            @RequestParam String code,
            @RequestParam String email) {
        try {
            Map<String, Object> result = voucherPoolService.validateVoucherCode(code, email);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(ERROR_KEY, e.getMessage()));
        }
    }

    @GetMapping("/valid")
    public ResponseEntity<Map<String, Object>> getValidVouchers(@RequestParam String email) {
        try {
            List<String> validVouchers = voucherPoolService.getValidVoucherCodes(email);
            return ResponseEntity.ok(Map.of("vouchers", validVouchers));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(ERROR_KEY, e.getMessage()));
        }
    }
    
}
