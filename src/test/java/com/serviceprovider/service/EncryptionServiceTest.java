package com.serviceprovider.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EncryptionService Unit Tests")
class EncryptionServiceTest {

    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        encryptionService = new EncryptionService();
        ReflectionTestUtils.setField(encryptionService, "configuredSecretKey", "TestSecretKey12345678901234567");
    }

    @Test
    @DisplayName("encrypt and decrypt round-trip returns original plaintext")
    void encryptDecrypt_RoundTrip_ReturnsOriginal() {
        String original = "{\"key\":\"value\",\"number\":42}";

        String encrypted = encryptionService.encrypt(original);
        String decrypted = encryptionService.decrypt(encrypted);

        assertThat(decrypted).isEqualTo(original);
    }

    @Test
    @DisplayName("encrypt produces different ciphertext each time (random IV)")
    void encrypt_ProducesUniqueOutput_EachCall() {
        String plainText = "same input";

        String first = encryptionService.encrypt(plainText);
        String second = encryptionService.encrypt(plainText);

        assertThat(first).isNotEqualTo(second);
    }

    @Test
    @DisplayName("encrypted output is valid Base64")
    void encrypt_ReturnsValidBase64() {
        String encrypted = encryptionService.encrypt("test data");

        assertThat(encrypted).matches("^[A-Za-z0-9+/=]+$");
    }
}
