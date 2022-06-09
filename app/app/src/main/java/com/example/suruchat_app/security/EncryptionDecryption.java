package com.example.suruchat_app.security;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;

public class EncryptionDecryption {


    public static byte[] encryptionMethod(Key encryptionKey, String inputString) throws Exception
    {

        byte[] input = inputString.getBytes(StandardCharsets.UTF_8);

        byte[] ivBytes = new byte[] {
                0x00, 0x00, 0x00, 0x01, 0x04, 0x05, 0x06, 0x07,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01 };

        //initializing a new initialization vector
        IvParameterSpec ivSpec  = new IvParameterSpec(ivBytes);
        //what does this actually do?
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

        //encryption phase
        cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, ivSpec);
        //what is this doing?
        byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
        //what is this doing?
        int ctLength = cipher.update(input, 0, input.length, cipherText,0);

        //getting the cipher text length i assume?
        ctLength += cipher.doFinal (cipherText, ctLength );

        return cipherText;
    }
    public static String decryptionMethod(byte[] cipherText, Key decryptionKey) throws Exception
    {
        int ctLength = (cipherText.length);

        byte[] ivBytes = new byte[] {
                0x00, 0x00, 0x00, 0x01, 0x04, 0x05, 0x06, 0x07,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01 };

        //initializing a new initialization vector
        IvParameterSpec ivSpec  = new IvParameterSpec(ivBytes);
        //what does this actually do?
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

        //decryption phase
        cipher.init(Cipher.DECRYPT_MODE, decryptionKey, ivSpec);
        //storing the ciphertext in plaintext i'm assuming?
        byte[] plainText = new byte[cipher.getOutputSize(ctLength)];
        int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);
        //getting plaintextLength i think?
        ptLength= cipher.doFinal (plainText, ptLength);
        String plainString = "";
        for (int i =0;i<plainText.length;i++){
            int a = plainText[i];
            plainString += (char)a;
        }
        return plainString;
    }

    private static String digits = "0123456789abcdef";

    public static String toHex(byte[] data, int length)
    {
        StringBuffer buf = new StringBuffer();

        for (int i=0; i!= length; i++)
        {
            int v = data[i] & 0xff;

            buf.append(digits.charAt(v >>4));
            buf.append(digits.charAt(v & 0xf));
        }
        return buf.toString();

    }

    public static String toHex(byte[] data)
    {
        return toHex(data, data.length);
    }
}
