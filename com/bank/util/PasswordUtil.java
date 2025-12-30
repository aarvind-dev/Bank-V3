package com.bank.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Uses SHA-256 with a random Salt to protect user credentials.
 */
public class PasswordUtil {

    // Constants for configuration
    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;

    /**
     * Hashes a raw password with a generated salt.
     * Format: salt:hash
     */
    public static String hashPassword(String password) {
        try {
            // Generate a random salt
            byte[] salt = new byte[SALT_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);

            // Hash the password with the salt
            String hash = hashWithSalt(password, salt);

            // Encode salt to string
            String saltStr = Base64.getEncoder().encodeToString(salt);

            // Return format: salt:hash
            return saltStr + ":" + hash;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Verifies a plain text password against the stored hashed password.
     */
    public static boolean verifyPassword(String originalPassword, String storedPassword) {
        if (storedPassword == null || !storedPassword.contains(":")) {
            return false; // Invalid format
        }

        try {
            // Split stored password into salt and hash
            String[] parts = storedPassword.split(":");
            String saltStr = parts[0];
            String originalHash = parts[1];

            // Decode the salt
            byte[] salt = Base64.getDecoder().decode(saltStr);

            // Hash the input password with the extracted salt
            String newHash = hashWithSalt(originalPassword, salt);

            // Compare the new hash with the original hash
            return newHash.equals(originalHash);

        } catch (Exception e) {
            return false; // Verification failed
        }
    }

    /**
     * Helper method to perform the actual hashing
     */
    private static String hashWithSalt(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
        digest.reset();
        digest.update(salt);
        byte[] hash = digest.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }
}