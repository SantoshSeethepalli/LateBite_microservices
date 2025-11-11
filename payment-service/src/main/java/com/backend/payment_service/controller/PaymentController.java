package com.backend.payment_service.controller;

import com.backend.payment_service.service.PaymentService;
import com.google.zxing.WriterException;
import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;


@RestController
@RequestMapping(value = "/api/payment", produces = MediaType.IMAGE_PNG_VALUE)
@ResponseStatus(HttpStatus.OK)
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @GetMapping("/generate-qr")
    public ResponseEntity<byte[]> getQrByUpiIdAndAmount(
                @RequestParam String restaurantUpiId,
                @RequestParam BigDecimal amount)
            throws IOException, WriterException {

        byte[] qrCode = paymentService.generateQr(restaurantUpiId, amount);

        return ResponseEntity.ok(qrCode);
    }
}
