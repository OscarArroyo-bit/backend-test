package com.oscar.paymentms.security;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Loads the RSA public and private keys from the application resources.
 *
 * Keys are loaded once during application startup and reused
 * throughout the application lifecycle.
 */
@Component
public class RSAKeyLoader {

    private PublicKey publicKey;
    private PrivateKey privateKey;

    @PostConstruct
    public void init() throws Exception {

        byte[] publicBytes = new ClassPathResource("keys/public_key.der")
                .getInputStream()
                .readAllBytes();

        byte[] privateBytes = new ClassPathResource("keys/private_key.der")
                .getInputStream()
                .readAllBytes();

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        publicKey = keyFactory.generatePublic(
                new X509EncodedKeySpec(publicBytes));

        privateKey = keyFactory.generatePrivate(
                new PKCS8EncodedKeySpec(privateBytes));

        System.out.println("RSA Keys loaded successfully");
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}