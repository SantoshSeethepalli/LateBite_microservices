package com.backend.payment_service.controller;

import com.backend.payment_service.service.PaymentService;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;


@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @GetMapping("/generate-qr")
    public byte[] getQrByUpiIdAndAmount(@RequestParam String restaurantUpiId, @RequestParam BigDecimal amount) throws IOException, WriterException {

        return paymentService.generateQr(restaurantUpiId, amount);
    }
}
