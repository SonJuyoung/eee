package ieetu.common.utils;

import ieetu.common.crypto.SHA256;

import java.security.SecureRandom;

public class LoginUtils {

    /**
     * 비밀번호 문자열 암호화
     *
     * @param plainPwd(암호화할 문자열), salt(고유값)
     * @return String
     * @throws Exception
     */
    public static String makeEncryptPassword(String plainPwd, String salt) throws Exception {
        if (plainPwd == null || plainPwd.isEmpty())
            return plainPwd;

        String combinedPwd = getCombinedPassword(plainPwd, salt);

        byte[] pszMessage = combinedPwd.getBytes();
        int uPlainTextLen = pszMessage.length;
        byte[] pszDigest = new byte[32];

        SHA256.SHA256_Encrpyt(pszMessage, uPlainTextLen, pszDigest);

        return bytesToHex(pszDigest);
    }

    public static Boolean comparePassword(String encryptedPassword, String plainPassword, String salt) throws Exception {
        return encryptedPassword.equals(makeEncryptPassword(plainPassword, salt));
    }

    /**
     * 비밀번호 암호화 대상 문자열 생성
     *
     * @param plainPwd(암호화할 문자열), salt(고유값)
     * @return String
     * @throws Exception
     */
    public static String getCombinedPassword(String plainPwd, String salt) throws Exception {
        if (plainPwd == null || plainPwd.isEmpty() || salt == null || salt.isEmpty())
            return plainPwd;

        return salt + plainPwd + salt;
    }

    /**
     * 비밀번호 고유 문자열 생성
     *
     * @param
     * @return String salt(고유값)
     * @throws Exception
     */
    public static String getGeneratedSalt() throws Exception {
        byte[] saltBytes = new byte[16];

        SecureRandom secureRandom = SecureRandom.getInstance(("SHA1PRNG"));
        secureRandom.nextBytes(saltBytes);

        return bytesToHex(saltBytes);
    }

    /**
     * byte를 hex로 변경 함수
     *
     * @param bytes
     * @return String
     * @throws Exception
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();

        for (byte b : bytes) {
            result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }

        return result.toString();
    }
}
