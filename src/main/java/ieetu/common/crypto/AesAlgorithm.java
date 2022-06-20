
package ieetu.common.crypto;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

public class AesAlgorithm {
    /* 0QPaGkPmPF7SafLn */
    public static byte[] ivBytes = {48, 81, 80, 97, 71, 107, 80, 109, 80, 70, 55, 83, 97, 102, 76, 110};

    public static String encrypt(String text, String key)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

        if (StringUtils.isEmpty(text)) {
            return text;
        }

        String encrypted = null;
        byte[] source = text.getBytes("UTF-8");
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");

        AlgorithmParameterSpec IVspec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, IVspec);
        int mod = source.length % 16;
        if (mod != 0) {
            text = String.format(text + "%" + (16 - mod) + "s", " ");
        }

        encrypted = byteArrayToHex_128(cipher.doFinal(text.getBytes("UTF-8")));

        return encrypted;
    }

    // key는 16 바이트로 구성 되어야 한다.
    public static String decrypt(String s, String key)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        if (s == null || s.length() == 0) {
            return s;
        }

        String decrypted = null;
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");

        AlgorithmParameterSpec IVspec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, IVspec);
        decrypted = new String(cipher.doFinal(hexToByteArray_128(s)), "UTF-8");

        return decrypted.trim();
    }

    // AES128
    private static byte[] hexToByteArray_128(String s) {
        if (s == null || s.length() == 0) {
            return null;
        }

        //16진수 문자열을 byte로 변환
        byte[] byteArray = new byte[s.length() / 2];
        for (int i = 0; i < byteArray.length; i++) {
            byteArray[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
        }

        return byteArray;
    }

    // AES128
    private static String byteArrayToHex_128(byte[] encrypted) {
        if (encrypted == null || encrypted.length == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer(encrypted.length * 2);
        String hexNumber;
        for (int x = 0; x < encrypted.length; x++) {
            hexNumber = "0" + Integer.toHexString(0xff & encrypted[x]);
            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }

        return sb.toString();
    }

    // AES256
    private static byte[] hexToByteArray_256(String s) {
        byte[] retValue = null;
        if (s != null && s.length() != 0) {
            retValue = new byte[s.length() / 2];
            for (int i = 0; i < retValue.length; i++) {
                retValue[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
            }
        }
        return retValue;
    }

    // AES256
    private static String byteArrayToHex_256(byte buf[]) {
        StringBuffer strbuf = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            strbuf.append(String.format("%02x", buf[i]));
        }

        return strbuf.toString();
    }

}
