package com.serviceprovider.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

/**
 * Standalone decryption tool for testing encrypted API responses.
 *
 * Usage: paste your encryptedData value into ENCRYPTED_VALUE and run main().
 *
 * Algorithm: AES-256-CBC / PKCS5Padding
 * Format:    Base64(IV[16 bytes] + ciphertext)
 */
public class DecryptionTool {

    private static final String SECRET_KEY = "MyS3cr3tEncryptionKey!2024PROD";
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int IV_LENGTH = 16;
    private static final int KEY_LENGTH = 32;

    // ---------------------------------------------------------------
    // Paste your encryptedData value here and run main()
    // ---------------------------------------------------------------
    private static final String ENCRYPTED_VALUE = "PASTE_YOUR_ENCRYPTED_VALUE_HERE";
    // ---------------------------------------------------------------

    public static void main(String[] args) {
        String valueToDecrypt = args.length > 0 ? args[0] : ENCRYPTED_VALUE;

        if (valueToDecrypt.equals("PASTE_YOUR_ENCRYPTED_VALUE_HERE") || valueToDecrypt.isBlank()) {
            System.err.println("ERROR: No encrypted value provided.");
            System.err.println("  Option 1 — edit ENCRYPTED_VALUE in this file and rerun.");
            System.err.println("  Option 2 — pass the value as a program argument.");
            return;
        }

        try {
            String decrypted = decrypt(valueToDecrypt, SECRET_KEY);
            System.out.println("=== Decrypted Result ===");
            System.out.println(decrypted);
        } catch (Exception e) {
            System.err.println("Decryption failed: " + e.getMessage());
            System.err.println("Possible causes:");
            System.err.println("  - Wrong secret key (check ENCRYPTION_SECRET_KEY env var)");
            System.err.println("  - Value was modified / incomplete");
            System.err.println("  - Value is not Base64-encoded");
        }
    }

    public static String decrypt(String encryptedBase64, String secretKey) throws Exception {
        byte[] rawKey = secretKey.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = new byte[KEY_LENGTH];
        System.arraycopy(rawKey, 0, keyBytes, 0, Math.min(rawKey.length, KEY_LENGTH));
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        byte[] combined = Base64.getDecoder().decode(encryptedBase64);
        byte[] iv = Arrays.copyOfRange(combined, 0, IV_LENGTH);
        byte[] cipherBytes = Arrays.copyOfRange(combined, IV_LENGTH, combined.length);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));

        byte[] decrypted = cipher.doFinal(cipherBytes);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
