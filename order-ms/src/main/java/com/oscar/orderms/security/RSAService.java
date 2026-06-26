package com.oscar.orderms.security;

import org.springframework.stereotype.Service;
import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Provides RSA encryption and decryption services.
 *
 * Sensitive payment information is encrypted before
 * being published to Kafka and decrypted by the
 * Payment Service.
 */
@Service
public class RSAService {

    private final RSAKeyLoader keyLoader;

    public RSAService(RSAKeyLoader keyLoader) {
        this.keyLoader = keyLoader;
    }

    public String encrypt(String value) {
        try {

            Cipher cipher = Cipher.getInstance("RSA");

            cipher.init(Cipher.ENCRYPT_MODE, keyLoader.getPublicKey());

            byte[] encrypted = cipher.doFinal(
                    value.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    public String decrypt(String value) {
        try {

            Cipher cipher = Cipher.getInstance("RSA");

            cipher.init(Cipher.DECRYPT_MODE, keyLoader.getPrivateKey());

            byte[] decrypted = cipher.doFinal(
                    Base64.getDecoder().decode(value));

            return new String(decrypted, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data", e);
        }
    }
}