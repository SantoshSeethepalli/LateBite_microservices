package com.backend.auth_services.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.apache.commons.codec.binary.Hex;

public class HashUtil {
    public static String sha256(String input) throws Exception {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] d = md.digest(input.getBytes(StandardCharsets.UTF_8));

        return Hex.encodeHexString(d);
    }
}