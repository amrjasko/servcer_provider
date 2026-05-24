package com.serviceprovider.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * AES-256-CBC encryption service.
 *
 * Encrypted output format: Base64(IV [16 bytes] + CipherText)
 * Frontend must strip the first 16 bytes as IV, then decrypt the remainder.
 */
@Service
@Slf4j
public class EncryptionService {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int IV_LENGTH_BYTES = 16;
    private static final int KEY_LENGTH_BYTES = 32; // AES-256

    @Value("${application.encryption.secret-key}")
    private String configuredSecretKey;

    public String encrypt(String plainText) {
        try {
            SecretKeySpec keySpec = buildKeySpec();

            byte[] iv = new byte[IV_LENGTH_BYTES];
            new SecureRandom().nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            byte[] cipherBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            byte[] combined = new byte[IV_LENGTH_BYTES + cipherBytes.length];
            System.arraycopy(iv, 0, combined, 0, IV_LENGTH_BYTES);
            System.arraycopy(cipherBytes, 0, combined, IV_LENGTH_BYTES, cipherBytes.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            log.error("Encryption failed: {}", e.getMessage());
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public String decrypt(String encryptedBase64) {
        try {
            SecretKeySpec keySpec = buildKeySpec();

            byte[] combined = Base64.getDecoder().decode(encryptedBase64);

            byte[] iv = Arrays.copyOfRange(combined, 0, IV_LENGTH_BYTES);
            byte[] cipherBytes = Arrays.copyOfRange(combined, IV_LENGTH_BYTES, combined.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));

            byte[] decrypted = cipher.doFinal(cipherBytes);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Decryption failed: {}", e.getMessage());
            throw new RuntimeException("Decryption failed", e);
        }
    }

    private SecretKeySpec buildKeySpec() {
        byte[] rawKey = configuredSecretKey.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = new byte[KEY_LENGTH_BYTES];
        System.arraycopy(rawKey, 0, keyBytes, 0, Math.min(rawKey.length, KEY_LENGTH_BYTES));
        return new SecretKeySpec(keyBytes, "AES");
    }
}
