/*
 *  File Name:    AES.java
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
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.bewsoftware.utils.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

/**
 * This class contains an as yet incomplete collection of methods for
 * encrypting and decrypting text.
 *
 * @implNote Work In Progress!!
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0.10
 * @version 1.0.10
 */
public class AES
{

    private AES()
    {
    }

    public static String encrypt(String algorithm, String input, SecretKey key, IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException
    {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    /**
     * Generate an Initialization Vector (IV).
     * <p>
     * IV is a pseudo-random value and has the same size as the block that is
     * encrypted.
     * <p>
     * Source:
     * <a href="https://www.baeldung.com/java-aes-encryption-decryption">
     * https://www.baeldung.com/java-aes-encryption-decryption</a>
     *
     * @implNote The block size used is: 16.
     *
     * @return IV
     */
    public static IvParameterSpec generateIv()
    {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
}
