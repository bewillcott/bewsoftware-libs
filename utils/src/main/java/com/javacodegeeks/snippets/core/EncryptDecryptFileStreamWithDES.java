/*
 *  File Name:    EncryptDecryptFileStreamWithDES.java
 *  Project Name: bewsoftware-utils
 *
 *  Copyright (c) 2025 Bradley Willcott
 *
 *  bewsoftware-utils is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  bewsoftware-utils is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.javacodegeeks.snippets.core;

import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

/**
 * EncryptDecryptFileStreamWithDES class description.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0.0
 * @version 1.0.0
 */
public class EncryptDecryptFileStreamWithDES
{

    private static Cipher dcipher;

    private static Cipher ecipher;

    // 8-byte initialization vector
    private static final byte[] iv =
    {

        (byte) 0xB2, (byte) 0x12, (byte) 0xD5, (byte) 0xB2,
        (byte) 0x44, (byte) 0x21, (byte) 0xC3, (byte) 0xC3
    };

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public static void main(String[] args)
    {
        try
        {
            SecretKey key = KeyGenerator.getInstance("DES").generateKey();

            AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

            ecipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

            dcipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

            encrypt("cleartext.txt", "encrypted.dat");

            decrypt("encrypted.dat", "cleartext-reversed.txt");

        } catch (InvalidAlgorithmParameterException e)
        {
            System.out.println("Invalid Alogorithm Parameter:" + e.getMessage());
        } catch (NoSuchAlgorithmException e)
        {
            System.out.println("No Such Algorithm:" + e.getMessage());
        } catch (NoSuchPaddingException e)
        {
            System.out.println("No Such Padding:" + e.getMessage());
        } catch (InvalidKeyException e)
        {
            System.out.println("Invalid Key:" + e.getMessage());
        }
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private static void decrypt(final String inFilename, final String outFilename)
    {
        try (CipherInputStream cis = new CipherInputStream(new FileInputStream(inFilename), dcipher);
             OutputStream os = new FileOutputStream(outFilename))
        {

            byte[] buf = new byte[1024];

            // read in the decrypted bytes and write the clear text to out
            int numRead = 0;

            while ((numRead = cis.read(buf)) >= 0)
            {

                os.write(buf, 0, numRead);
            }
        } catch (IOException e)
        {
            System.out.println("I/O Error:" + e.getMessage());
        }
    }

    private static void encrypt(final String inFilename, final String outFilename)
//    private static void encrypt(InputStream is, OutputStream os)
    {
        try(OutputStream cos= new CipherOutputStream(new FileOutputStream(outFilename), ecipher);
                InputStream is=new FileInputStream(inFilename))
        {
            byte[] buf = new byte[1024];

            // read in the clear text and write to out to encrypt
            int numRead = 0;

            while ((numRead = is.read(buf)) >= 0)
            {
                // Write encypted data.
                cos.write(buf, 0, numRead);
            }
        } catch (IOException e)
        {
            System.out.println("I/O Error:" + e.getMessage());
        }
    }
}
