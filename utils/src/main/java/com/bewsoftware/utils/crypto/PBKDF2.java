/*
 *  File Name:    PBKDF2.java
 *  Project Name: bewsoftware-utils
 *
 *  Copyright (c) 2021 Bradley Willcott
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bewsoftware.utils.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Secure Password Storage v2.0.
 * <p>
 * PBKDF2 salted password hashing with HMAC and SHA512.
 * <p>
 * Source:
 * <a href="https://github.com/defuse/password-hashing/blob/master/PasswordStorage.java">
 * https://github.com/defuse/password-hashing/blob/master/PasswordStorage.java</a>
 * <p>
 * Downloaded: 07/10/2021.
 * <p>
 * Renamed and modified by Bradley Willcott
 *
 * @author Taylor Hornby @ defuse.ca
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since v1.0.10
 * @version v2.1.0
 */
public final class PBKDF2
{
    private static final String HASH_ALGORITHM = "sha512";// Was "sha1"

    private static final int HASH_ALGORITHM_INDEX = 0;

    // May be changed without breaking existing hashes.
    private static final int HASH_BYTE_SIZE = 512;// Was 18

    // These constants define the encoding and may not be changed.
    private static final int HASH_SECTIONS = 5;

    private static final int HASH_SIZE_INDEX = 2;

    private static final int ITERATION_INDEX = 1;

    // Used by the main() method in for loop test.
    private static final int NUM_OF_TEST_CYCLES = 10;// Was 100

    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA512";// Was "...SHA1"

    private static final int PBKDF2_INDEX = 4;

    // May be changed without breaking existing hashes.
    private static final int PBKDF2_ITERATIONS = 100000;// Was 64000

    // These constants may be changed without breaking existing hashes.
    private static final int SALT_BYTE_SIZE = HASH_BYTE_SIZE;// Was 24

    private static final int SALT_INDEX = 3;

    /**
     * This static helper class is not meant to be instantiated.
     */
    private PBKDF2()
    {
    }

    /**
     * Returns a salted PBKDF2 hash of the password.
     *
     * @param password the password to hash
     *
     * @return a salted PBKDF2 hash of the password
     *
     * @throws CannotPerformOperationException
     */
    public static String createHash(String password)
            throws CannotPerformOperationException
    {
        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTE_SIZE];
        random.nextBytes(salt);

        // Hash the password
        byte[] hash = pbkdf2(password.toCharArray(), salt, PBKDF2_ITERATIONS,
                HASH_BYTE_SIZE);
        int hashSize = hash.length;

        // format: algorithm:iterations:hashSize:salt:hash
        return HASH_ALGORITHM
                + ":"
                + PBKDF2_ITERATIONS
                + ":" + hashSize
                + ":"
                + toBase64(salt)
                + ":"
                + toBase64(hash);
    }

    /**
     * Generate an AES key with size of {@code n} (128, 192, and 256) bits.
     * <p>
     * Source:
     * <a href="https://www.baeldung.com/java-aes-encryption-decryption">
     * https://www.baeldung.com/java-aes-encryption-decryption</a>
     * <p>
     * Modified by Bradley Willcott (06/10/2021)
     *
     * @param n size of key to generate (in bits)
     *
     * @return generated secret key
     *
     * @throws NoSuchAlgorithmException
     */
    public static SecretKey generateKey(int n) throws NoSuchAlgorithmException
    {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        return keyGenerator.generateKey();
    }

    /**
     * Generate an AES key from a given password.
     * <p>
     * Source:
     * <a href="https://www.baeldung.com/java-aes-encryption-decryption">
     * https://www.baeldung.com/java-aes-encryption-decryption</a>
     * <p>
     * Modified by Bradley Willcott (06/10/2021)
     *
     * @param password to use
     * @param salt     to use
     *
     * @return generated secret key
     *
     * @throws CannotPerformOperationException
     */
    public static SecretKey getKeyFromPassword(String password, String salt)
            throws CannotPerformOperationException
    {
        byte[] passwordHash = pbkdf2(password.toCharArray(), salt.getBytes(),
                ITERATION_INDEX, HASH_BYTE_SIZE);
        return new SecretKeySpec(passwordHash, "AES");
    }

    /**
     * Tests the basic functionality of the PBKDF2 class
     *
     * @param args ignored
     */
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public static void main(String[] args)
    {
        try
        {
            // Print out 10 hashes
            for (int i = 0; i < 10; i++)
            {
                System.out.println(createHash("p\r\nassw0Rd!"));
            }

            // Test password validation
            boolean failure = false;
            System.out.println("Running tests...");
            for (int i = 0; i < NUM_OF_TEST_CYCLES; i++)
            {
                String password = "" + i;
                String hash = createHash(password);
                String secondHash = createHash(password);

                if (hash.equals(secondHash))
                {
                    System.out.println("FAILURE: TWO HASHES ARE EQUAL!");
                    failure = true;
                }

                String wrongPassword = "" + (i + 1);

                if (verifyPassword(wrongPassword, hash))
                {
                    System.out.println("FAILURE: WRONG PASSWORD ACCEPTED!");
                    failure = true;
                }

                if (!verifyPassword(password, hash))
                {
                    System.out.println("FAILURE: GOOD PASSWORD NOT ACCEPTED!");
                    failure = true;
                }
            }
            if (failure)
            {
                System.out.println("TESTS FAILED!");
            } else
            {
                System.out.println("TESTS PASSED!");
            }
        } catch (CannotPerformOperationException | InvalidHashException ex)
        {
            System.out.println("ERROR: " + ex);
        }
    }

    /**
     * Validates a password using a hash.
     *
     * @param password    the password to check
     * @param correctHash the hash of the valid password
     *
     * @return {@code true} if the password is correct, {@code false} if not
     *
     * @throws CannotPerformOperationException
     * @throws InvalidHashException
     */
    public static boolean verifyPassword(String password, String correctHash)
            throws CannotPerformOperationException, InvalidHashException
    {
        // Decode the hash into its parameters
        String[] params = correctHash.split(":");
        if (params.length != HASH_SECTIONS)
        {
            throw new InvalidHashException(
                    "Fields are missing from the password hash."
            );
        }

        // Currently, Java only supports SHA1.
        if (!params[HASH_ALGORITHM_INDEX].equals(HASH_ALGORITHM))
        {
            throw new CannotPerformOperationException(
                    "Unsupported hash type."
            );
        }

        int iterations = 0;
        try
        {
            iterations = Integer.parseInt(params[ITERATION_INDEX]);
        } catch (NumberFormatException ex)
        {
            throw new InvalidHashException(
                    "Could not parse the iteration count as an integer.",
                    ex
            );
        }

        if (iterations < 1)
        {
            throw new InvalidHashException(
                    "Invalid number of iterations. Must be >= 1."
            );
        }

        byte[] salt = null;
        try
        {
            salt = fromBase64(params[SALT_INDEX]);
        } catch (IllegalArgumentException ex)
        {
            throw new InvalidHashException(
                    "Base64 decoding of salt failed.",
                    ex
            );
        }

        byte[] hash = null;
        try
        {
            hash = fromBase64(params[PBKDF2_INDEX]);
        } catch (IllegalArgumentException ex)
        {
            throw new InvalidHashException(
                    "Base64 decoding of pbkdf2 output failed.",
                    ex
            );
        }

        int storedHashSize = 0;
        try
        {
            storedHashSize = Integer.parseInt(params[HASH_SIZE_INDEX]);
        } catch (NumberFormatException ex)
        {
            throw new InvalidHashException(
                    "Could not parse the hash size as an integer.",
                    ex
            );
        }

        if (storedHashSize != hash.length)
        {
            throw new InvalidHashException(
                    "Hash length doesn't match stored hash length."
            );
        }

        // Compute the hash of the provided password, using the same salt,
        // iteration count, and hash length
        byte[] testHash = pbkdf2(password.toCharArray(), salt, iterations, hash.length);
        // Compare the hashes in constant time. The password is correct if
        // both hashes match.
        return slowEquals(hash, testHash);
    }

    /**
     * Convert a string of Base64 characters into byte array.
     *
     * @param hex the Base64 text to convert
     *
     * @return the decoded text
     *
     * @throws IllegalArgumentException
     */
    private static byte[] fromBase64(String hex)
            throws IllegalArgumentException
    {
        return Base64.getDecoder().decode(hex);
    }

    /**
     * Computes the PBKDF2 hash of a password.
     *
     * @param password   the password to hash.
     * @param salt       the salt
     * @param iterations the iteration count (slowness factor)
     * @param bytes      the length of the hash to compute in bytes
     *
     * @return the PBDKF2 hash of the password
     *
     * @throws CannotPerformOperationException
     */
    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
            throws CannotPerformOperationException
    {
        try
        {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException ex)
        {
            throw new CannotPerformOperationException(
                    "Hash algorithm not supported.",
                    ex
            );
        } catch (InvalidKeySpecException ex)
        {
            throw new CannotPerformOperationException(
                    "Invalid key spec.",
                    ex
            );
        }
    }

    /**
     * Compares two byte arrays in length-constant time. This comparison method
     * is used so that password hashes cannot be extracted from an on-line
     * system using a timing attack and then attacked off-line.
     *
     * @param a the first byte array
     * @param b the second byte array
     *
     * @return true if both byte arrays are the same, false if not
     */
    private static boolean slowEquals(byte[] a, byte[] b)
    {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++)
        {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }

    /**
     * Converts a byte array into a string of Base64 characters.
     *
     * @param array the byte array to convert
     *
     * @return the string of Base64 characters
     */
    private static String toBase64(byte[] array)
    {
        return Base64.getEncoder().encodeToString(array);
    }

    /**
     * This exception is thrown when something is wrong with the platform
     * your code is running on, and for some reason it's not safe to verify a
     * password on it.
     */
    @SuppressWarnings(
            {
                "serial", "PublicInnerClass"
            })
    public static class CannotPerformOperationException extends Exception
    {
        public CannotPerformOperationException(String message)
        {
            super(message);
        }

        public CannotPerformOperationException(String message, Throwable source)
        {
            super(message, source);
        }
    }

    /**
     * The {@code correctHash} parameter you gave to the
     * {@link #verifyPassword(java.lang.String, java.lang.String) verifyPassword}
     * method was somehow corrupted. Note that some ways of corrupting a hash
     * are
     * impossible to detect, and their only symptom will be that
     * {@link #verifyPassword(java.lang.String, java.lang.String) verifyPassword}
     * will return {@code false} even though the correct
     * password was given. So {@code InvalidHashException} is not guaranteed to
     * be
     * thrown if a hash has been changed, but if it is thrown then you can be
     * sure that the hash <i>was</i> changed.
     */
    @SuppressWarnings(
            {
                "serial", "PublicInnerClass"
            })
    public static class InvalidHashException extends Exception
    {
        public InvalidHashException(String message)
        {
            super(message);
        }

        public InvalidHashException(String message, Throwable source)
        {
            super(message, source);
        }
    }
}
