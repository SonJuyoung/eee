package ieetu.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaskingUtils {
    public static String getNameMask(String name) {
        if (StringUtils.isEmpty(name)) {
            return "";
        }

        String maskStr = "";
        String nonMaskStr = "";
        int nameLen = name.length();

        // 이름이 3자 미만일 경우 마지막 글자를 마스킹
        if (nameLen < 3) {
            for (int i = 0; i < nameLen; i++) {
                if (i == nameLen - 1) {
                    maskStr += "*";
                } else {
                    maskStr += name.substring(i, i + 1);
                }
            }
            return maskStr;
        }

        int maskLen = Math.round(nameLen / 3);
        nonMaskStr = name.substring(0, nameLen - maskLen);
        maskStr = name.substring(nameLen - maskLen, nameLen);

        for (int i = 0; i < maskStr.length(); i++) {
            maskStr = maskStr.replace(maskStr.substring(i, i + 1), "*");
        }

        return nonMaskStr + maskStr;
    }

    public static String getIdMask(String id) {
        if (id == null || id.isEmpty()) {
            return "";
        }

        /* 아이디의 끝 3자리를 마스킹 처리함. 만약 아이디가 3자리 이하일 경우 모두 마스킹 처리함. */
        int idLen = id.length();
        String nonMaskStr = "";
        String maskStr = "";
        if (idLen <= 3) {
            char[] c = new char[idLen];
            Arrays.fill(c, '*');
            maskStr = id.replace(id, String.valueOf(c));
        } else {
            nonMaskStr = id.substring(0, idLen - 3);
            maskStr = id.substring(idLen - 3, idLen);
            maskStr = maskStr.replace(maskStr, "***");
        }

        return nonMaskStr + maskStr;
    }

    public static String getEmailMask(String email) {
        if (email == null || email.isEmpty()) {
            return "";
        }

        String regex = "\\b(\\S+)+@(\\S+.\\S+)";
        Matcher matcher = Pattern.compile(regex).matcher(email);

        String maskEmail = email;
        if (matcher.find()) {
            String id = matcher.group(1); // 마스킹 처리할 부분인 userId

            /*
             * userId의 길이를 기준으로 세글자 초과인 경우 뒤 세자리를 마스킹 처리하고,
             * 세글자인 경우 뒤 두글자만 마스킹,
             * 세글자 미만인 경우 모두 마스킹 처리
             */
            int length = id.length();
            if (length < 3) {
                char[] c = new char[length];
                Arrays.fill(c, '*');
                maskEmail = email.replace(id, String.valueOf(c));
            } else if (length == 3) {
                maskEmail = email.replaceAll("\\b(\\S+)[^@][^@]+@(\\S+)", "$1**@$2");
            } else {
                maskEmail = email.replaceAll("\\b(\\S+)[^@][^@][^@]+@(\\S+)", "$1***@$2");
            }
        }

        return maskEmail;
    }

    public static String getTelnoMask(String telno) {
        if (telno == null || telno.isEmpty()) {
            return "";
        }

        /*
         * 전화번호 2번째 자리는 뒤에 2자 **, ex) 1234 -> 12**
         * 전화번호 3번째 자리는 첫자만 *, ex) 1234 -> *234
         */
        String regex = "^(\\d{1,3})-?(\\d{1,2})\\d{2}-?\\d(\\d{3})$";

        Matcher matcher = Pattern.compile(regex).matcher(telno);
        String maskTelno = telno;
        if (matcher.find()) {
            maskTelno = matcher.group(1) + "-" + matcher.group(2) + "**-*" + matcher.group(3);
        }

        return maskTelno;
    }

    public static String getIpAddrMask(String ipAddr) {
        if (ipAddr == null || ipAddr.isEmpty()) {
            return "";
        }

        /* IP Address 숨김 처리 - ex) ***.123.***.123 */
        String regex = "^(\\d{1,3}).(\\d{1,3}).(\\d{1,3}).(\\d{1,3})$";

        Matcher matcher = Pattern.compile(regex).matcher(ipAddr);
        String maskIpAddr = ipAddr;
        if (matcher.find()) {
            maskIpAddr = "***." + matcher.group(2) + ".***." + matcher.group(4);
        }

        return maskIpAddr;
    }

    public static String getBiznoMask(String bizno) {
        if (bizno == null || bizno.isEmpty()) {
            return "";
        }

        /* 사업자번호 뒤 5자리 숨김 */
        String regex = "^(\\d{3})-(\\d{2})-(\\d{5})$";

        Matcher matcher = Pattern.compile(regex).matcher(bizno);
        String maskBizno = bizno;
        if (matcher.find()) {
            maskBizno = matcher.group(1) + "-" + matcher.group(2) + "-*****";
        }

        return maskBizno;
    }

    public static String getAddrMask(String addr) {
        if (addr == null || addr.isEmpty()) {
            return "";
        }

        /* 주소는 구주소인 경우 동까지, 신주소인 경우 길까지 표시 */
        String regex = "(([가-힣]+(\\d{1,5}|\\d{1,5}(,|.)\\d{1,5}|)+(읍|면|동|가|리))(^구|)((\\d{1,5}(~|-)\\d{1,5}|\\d{1,5})(가|리|)|))([ ](산(\\d{1,5}(~|-)\\d{1,5}|\\d{1,5}))|)|(([가-힣]|(\\d{1,5}(~|-)\\d{1,5})|\\d{1,5})+(로|길))";

        String[] addrStrArr = addr.split(" ");
        String str = "";
        String resultStr = "";
        int matchCnt = 0;

        for (int i = 0; i < addrStrArr.length; i++) {
            str += addrStrArr[i] + " ";

            if (addrStrArr[i].matches(regex)) {
                if (matchCnt == 0
                        || addrStrArr[i].substring(addrStrArr[i].length() - 1).equals("로")
                        || addrStrArr[i].substring(addrStrArr[i].length() - 1).equals("길")
                        || addrStrArr[i].substring(addrStrArr[i].length() - 1).equals("동")
                        || addrStrArr[i].substring(addrStrArr[i].length() - 1).equals("리")) {
                    resultStr = str;
                }

                matchCnt++;
            }
        }

        if (!resultStr.isEmpty() && matchCnt > 0) {
            resultStr += "***";
        } else {
            for (int i = 0; i < addrStrArr.length - 1; i++) {
                resultStr += addrStrArr[i] + " ";
            }
            resultStr += "***";
        }

        return resultStr;
    }
}
