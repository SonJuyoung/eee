package ieetu.common.utils;

import ieetu.common.config.etc.EgovMultiLoginPreventor;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommonFnc {
    final static String regexJsonNull = "(\"[a-zA-Z0-9_-]{0,}\"|\\\\\"[a-zA-Z0-9_-]{0,}\\\\\"):(\"\"|\\\\\"\\\\\")";
    final static String regexJsonAryNull = "(\"[a-zA-Z0-9_-]{0,}\"|\\\\\"[a-zA-Z0-9_-]{0,}\\\\\"):(\\[\\]|\\\\[\\\\])";
    final static String regexJsonNullVal = "(\"[a-zA-Z0-9_-]{0,}\"|\\\\\"[a-zA-Z0-9_-]{0,}\\\\\"):null";
    final static String regexfloat = "([a-zA-Z0-9_-]+):([0-9]+[\\.]+[0-9]+)";

    /*
     * [1-9]{1}[0-9]{3}([1]{1}[0-2]{1}|[0]{1}[0-9]{1})[0-3]{1}[0-9]{1}  == date (-)제거
     * [1-9]{1}[0-9]{3}([1]{1}[0-2]{1}|[0]{1}[0-9]{1})[0-3]{1}[0-9]{1}([2]{1}[0-3]{1}|[0-1]{1}[0-9]{1})[0-5]{1}[0-9]{1}[0-5]{1}[0-9]{1} == dateTime (-) 제거
     * */
    static String patternDate = "[1-9]{1}[0-9]{3}-([1]{1}[0-2]{1}|[0]{1}[0-9]{1})-[0-3]{1}[0-9]{1}";
    static String patternDateTime = "[1-9]{1}[0-9]{3}-([1]{1}[0-2]{1}|[0]{1}[0-9]{1})-[0-3]{1}[0-9]{1} ([2]{1}[0-3]{1}|[0-1]{1}[0-9]{1}):[0-5]{1}[0-9]{1}:[0-5]{1}[0-9]{1}";
    //static String patternTime = "([2]{1}[0-3]{1}|[0-1]{1}[0-9]{1}):[0-5]{1}[0-9]{1}:[0-5]{1}[0-9]{1}";
    static String patternTime = "([2]{1}[0-3]{1}|[0-1]{1}[0-9]{1}):[0-5]{1}[0-9]{1}";

    /**
     * 휴대폰
     */
    static String patternPhone = "01([0|1|6|7|8|9]?)-\\d{3,4}-\\d{4}";
    static String patternPhone_onlyNum = "01([0|1|6|7|8|9]?)\\d{7,8}";

    /**
     * 일반 전화
     */
    static String patternTel = "0\\d{1,2}-\\d{3,4}-\\d{4}";
    static String patternTel_onlyNum = "0\\d{8}|0\\d{9}|0\\d{10}";

    /**
     * 이메일
     */
    static String patternMail = "[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";

    /**
     * 위도 유효성
     */
    static String patternLatitude = "(^-?([0-8]?[0-9]|90)\\.[0-9]{6}$)";

    /**
     * 경도 유효성
     */
    static String patternLongitude = "(^-?(180(\\.[0-9]{6})?|((1[0-7]\\d)|([0-9]?\\d))(\\.[0-9]{6}))$)";

    final static Pattern patternJsonNull = Pattern.compile(regexJsonNull, Pattern.MULTILINE);
    final static Pattern patternJsonAryNull = Pattern.compile(regexJsonAryNull, Pattern.MULTILINE);
    final static Pattern patternJsonNullVal = Pattern.compile(regexJsonNullVal, Pattern.MULTILINE);
    final static Pattern patternfloatVal = Pattern.compile(regexfloat, Pattern.MULTILINE);
    final static Pattern patternBlank = Pattern.compile("\\([^\\(\\)]+\\)");

    @SuppressWarnings("serial")
    private static Map<String, int[]> numericParameter = new HashMap<String, int[]>() {{
        /** 연결상태 정보 */
        put("sys_id", new int[]{3, 0});
        put("cnnc_sttus", new int[]{1, 0});
    }};

    public static String requiredChecking(HttpServletRequest request, String mandatory) {
        String[] mandatoryString = mandatory.split(",");
        String str = "";
        for (int i = 0; i < mandatoryString.length; i++) {
            if (request.getParameter(mandatoryString[i]) == null
                    || request.getParameter(mandatoryString[i]).equals("")) {
                if (!str.equals(""))
                    str += ",";
                str += mandatoryString[i];
            }
        }

        return str;
    }

    /* JSON numeric 포맷 maxLength 확인 */
    public static String numericChk(JsonObject data) {
        String invalidParameter = "";
        final Matcher matchJsonNull = patternfloatVal.matcher(data.toString().replaceAll(" ", "").replaceAll("\"", "").replaceAll("\\\\", ""));

        while (matchJsonNull.find()) {
            if (numericParameter.containsKey(matchJsonNull.group(1).toString())) {
                String[] str = matchJsonNull.group(2).toString().split("\\.");
                int[] ary = numericParameter.get(matchJsonNull.group(1).toString());
                if (str[0].length() > ary[0] || str[1].length() > ary[1]) {
                    invalidParameter += matchJsonNull.group(1).toString();
                }
            }
        }
        return invalidParameter;
    }

    public static String requiredJsonChecking(JsonObject data, String mandatory) {
        String[] mandatoryString = mandatory.split(",");
        String str = "";

        if (data == null) {
            return str = mandatory;
        }

        final Matcher matchJsonNull = patternJsonNull.matcher(data.toString().replaceAll(" ", ""));
        final Matcher matcherJsonAryNull = patternJsonAryNull.matcher(data.toString().replaceAll(" ", ""));
        final Matcher matcherJsonNullVal = patternJsonNullVal.matcher(data.toString().replaceAll(" ", ""));

        /* JSON "" Check */
        while (matchJsonNull.find()) {
            String[] replaceStr = matchJsonNull.group(0).replaceAll("\"", "").replaceAll("\\\\", "").split(":");
            if (Arrays.asList(mandatoryString).contains(replaceStr[0])) {
                if (str != "") {
                    str += ",";
                }
                str += replaceStr[0];
            }
        }


        /* JSONArray [] Check */
        while (matcherJsonAryNull.find()) {
            String[] replaceStr = matcherJsonAryNull.group(0).replaceAll("\"", "").replaceAll("\\\\", "").split(":");
            if (Arrays.asList(mandatoryString).contains(replaceStr[0])) {
                if (str != "") {
                    str += ",";
                }
                str += replaceStr[0];
            }
        }

        /* JSON NULL Varidaitor Check (실제 NULL 값을 가진 변수) */
        while (matcherJsonNullVal.find()) {
            String[] replaceStr = matcherJsonNullVal.group(0).replaceAll("\"", "").replaceAll("\\\\", "").split(":");
            if (Arrays.asList(mandatoryString).contains(replaceStr[0])) {
                if (str != "") {
                    str += ",";
                }
                str += replaceStr[0];
            }
        }

        for (int i = 0; i < mandatoryString.length; i++) {
            if (jsonAllObjectGet(data, mandatoryString[i]) == null) {
                if (str != "") {
                    str += ",";
                }
                str += mandatoryString[i];
            }
        }

        String result = removeDuplicateString(str, ",");


        return result;
    }

    /* jsonObject 값 전체 中 get (ary,objec{object} 제외*/
    public static String jsonAllObjectGet(JsonObject data, String mandatory) {
        String regexJsonObject = "(" + mandatory + "|\\\"" + mandatory + "\\\"):([\\{]+\\D[^\\}]{0,}[\\}]|[\\\"a-zㄱ-ㅎ가-힣A-Z0-9_\\!\\[\\]\\/@#$%^&*:().?|<>-]+)";
        if (mandatory.contains("_tm") || mandatory.contains("_time")) {
            regexJsonObject = "(" + mandatory + "|\\\"" + mandatory + "\\\"):([\\{]+\\D[^\\}]{0,}[\\}]|[\\\"a-zㄱ-ㅎ\\s가-힣A-Z0-9_\\!\\[\\]\\/@#$%^&*:().?|<>-]+)";
        }

        Pattern patternJsonObject = Pattern.compile(regexJsonObject, Pattern.MULTILINE);

        Matcher matcherJsonObject = null;
        if (mandatory.contains("_tm") || mandatory.contains("_time")) {
            matcherJsonObject = patternJsonObject.matcher(data.toString().replaceAll("\"", "").replaceAll("\\\\", ""));
        } else {
            matcherJsonObject = patternJsonObject.matcher(data.toString().replaceAll(" ", "").replaceAll("\"", "").replaceAll("\\\\", ""));
        }

        if (matcherJsonObject.find()) {
            return matcherJsonObject.group(2).toString();
        }
        return null;
    }

    public static String getMethod(HttpServletRequest request) {
        String method = "GET";
        if (request.getMethod().equals("GET"))
            return method;

        method = request.getParameter("_method");
        if (method == null)
            method = "POST";
        return method;
    }

    public static void voInfo(Object obj) {
        Class<?> clazz = obj.getClass();
        Class<?> superClazz = clazz.getSuperclass();
        Field[] superFields = superClazz.getDeclaredFields();

        System.out.println("■■■■■■■■■■■■■■■■■■■■■■■ VO INFO Start ■■■■■■■■■■■■■■■■■■■■■■■");
        System.out.print(clazz.getName() + " => ");

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.print(field.getName() + "=" + getValue(field, obj) + ", ");
        }

        for (Field superField : superFields) {
            System.out.print(superField.getName() + "=" + getValue(superField, obj) + ", ");
        }
        System.out.println("");
        System.out.println("■■■■■■■■■■■■■■■■■■■■■■■ VO INFO End ■■■■■■■■■■■■■■■■■■■■■■■");
    }

    private static Object getValue(Field field, Object obj) {
        field.setAccessible(true);
        try {
            return field.get(obj);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            return "ERROR";
        }
    }

    public static String emptyCheckString(String str, HttpServletRequest request) {
        String returnVal = "";
        if (StringUtils.isNotEmpty(request.getParameter(str))) {
            returnVal = request.getParameter(str);
        }
        return returnVal;
    }

    public static int emptyCheckInt(String intVal, HttpServletRequest request) {
        int returnVal = 0;
        if (request.getParameter(intVal) != null && !request.getParameter(intVal).isEmpty()) {
            try {
                returnVal = Integer.parseInt(request.getParameter(intVal));
            } catch (Exception e) {
            }
        }
        return returnVal;
    }

    public static int emptyCheckIntRetVal(String intVal, HttpServletRequest request, int retVal) {
        int returnVal = retVal;
        if (request.getParameter(intVal) != null && !request.getParameter(intVal).isEmpty()) {
            try {
                returnVal = Integer.parseInt(request.getParameter(intVal));
            } catch (Exception e) {
            }
        }
        return returnVal;
    }

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static boolean checkReqParameter(HttpServletRequest request, String chkNm) {
        Map<String, String[]> paramMap = request.getParameterMap();
        return paramMap.containsKey(chkNm);
    }

    /* 구분자로 분리된 중복스트링을 제거하는 method */
    public static String removeDuplicateString(String str, String token) {
        List<String> list = Arrays.asList(str.split(","));


        String result = list.stream()
                .distinct() // 중복 제거
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(token, "", ""));

        return result;
    }

    /* JSON Type Null Validater Check
     *
     * type nuermic
     *      date
     *      datetime
     *      time
     *      timestamp
     *      String
     *      Integer
     * */
    public static Object JsonValidator(JsonObject json, String key, boolean nullAllow, String type, int maxLength) {
        if (json == null && nullAllow) {
            return false;
        } else if (json == null && !nullAllow) {
            throw new RuntimeException(key);
        } else if (json.containsKey(key)) {
            if (json.get(key) == null) {
                if (nullAllow) {
                    return null;
                } else {
                    throw new RuntimeException(key);
                }
            } else {
                //Size Validator Start
                boolean nuermicChk = false;
                if (type.equals("nuermic")) {
                    String[] chk = json.get(key).toString().split(".");
                    if (chk.length != 0) {
                        if (chk[0].length() > maxLength) {
                            nuermicChk = true;
                        }
                    }
                }

                if (json.get(key).toString().equals("")) {
                    if (type.equals("String")) {
                        return json.get(key);
                    } else {
                        return null;
                    }
                }

                if (json.get(key).toString().length() > maxLength && maxLength != 0 || nuermicChk) {
                    throw new RuntimeException(key);
                }
                //Size Validator End

                switch (type) {
                    case "nuermic":
                        if (!String.valueOf(json.get(key).toString()).matches("(^[.0-9]*$)")) {
                            throw new RuntimeException(key);
                        }
                        return json.get(key);
                    case "timestamp":
                    case "date":
                        if (json.get(key).toString().length() > 10 || !String.valueOf(json.get(key).toString()).matches(patternDate)) {
                            throw new RuntimeException(key);
                        }
                        return json.get(key);
                    case "datetime":
                        if (json.get(key).toString().length() > 19 || !String.valueOf(json.get(key).toString()).matches(patternDateTime)) {
                            throw new RuntimeException(key);
                        }
                        return json.get(key);
                    case "time":
                        //if (json.get(key).toString().length() > 8 || !String.valueOf(json.get(key).toString()).matches(patternTime)) {
                        //    throw new RuntimeException((String) MsgQue.getResultMode("invalid_parameter", key, null, null));
                        //}
                        if (json.get(key).toString().length() > maxLength || !String.valueOf(json.get(key).toString()).matches(patternTime)) {
                            throw new RuntimeException(key);
                        }
                        return json.get(key);
                    case "bit":
                        if (String.valueOf(json.get(key).toString()).matches(".*[^0-1].*")) {
                            throw new RuntimeException(key);
                        }
                        return json.get(key);
                    case "String":
                        switch (key) {
                            case "zeroClsYn":
                            case "fnsClsYn":
                            case "lnchYn":
                                if (!"Y".equals(json.get(key).toString()) && !"N".equals(json.get(key).toString())) {
                                    throw new RuntimeException(key);
                                }
                            default:
                                break;
                        }
                        return json.get(key);
                    case "telNo":
                        if (!String.valueOf(json.get(key).toString()).matches(patternPhone)
                                && !String.valueOf(json.get(key).toString()).matches(patternPhone_onlyNum)
                                && !String.valueOf(json.get(key).toString()).matches(patternTel)
                                && !String.valueOf(json.get(key).toString()).matches(patternTel_onlyNum)
                        ) {
                            throw new RuntimeException(key);
                        }
                        return json.get(key);
                    case "email":
                        if (!String.valueOf(json.get(key).toString()).matches(patternMail)) {
                            throw new RuntimeException(key);
                        }
                        return json.get(key);
                    case "Integer":
                        try {
                            int val = Integer.parseInt(json.get(key).toString());
                            switch (key) { // 키값으로 상세 값 벨리데이터
                                case "lastClsNum":
                                    if (val < 0 || val > 10) {
                                        throw new RuntimeException(key);
                                    }
                                    break;
                                default:
                                    break;
                            }

                            return val;
                        } catch (Exception e) {
                            throw new RuntimeException(key);
                        }
                }
                return json.get(key);

            }
        } else {
            if (!nullAllow) {
                throw new RuntimeException(key);
            } else {
                return false;
            }
        }
    }

    /* String TO integer <NullChk> */
    public static int getInteger(String no) {
        if (no != null) {
            return Integer.parseInt(no);
        } else {
            return 0;
        }
    }

    // base64 데이터에서 mime type으로 image 여부 확인
    public static boolean validImageFormat(String data) {
        System.out.println(">> data : " + data);
        if (data == null || data.isEmpty()) {
            return false;
        }

        String[] temp1Str = data.split(",");
        String[] temp2Str = temp1Str[0].split(":");
        String[] temp3Str = temp2Str[1].split("/");
        if (!temp3Str[0].equals("image")) {
            return false;
        }

        return true;
    }

    // base64 데이터에서 mime type으로 file format 추출
    public static String getFileFormat(String data) {
        if (data == null || data.isEmpty()) {
            return "";
        }

        String[] temp1Str = data.split(",");
        String[] temp2Str = temp1Str[0].split("/");
        String[] temp3Str = temp2Str[1].split(";");

        return temp3Str[0];
    }

    public static Boolean empty(ArrayList<String> setParameter, String str) {
        for (int i = 0; i < setParameter.size(); i++) {
            if (setParameter.get(i).equals(str)) {
                return true;
            }
        }

        return null;
    }

    public static class JsonValidatorAry {
        Object jsonVal;
        String parameterName;

        public JsonValidatorAry(Object jsonVal, String parameterName) {
            this.jsonVal = jsonVal;
            this.parameterName = parameterName;
        }

        public Object getJsonVal() {
            return jsonVal;
        }

        public void setJsonVal(Object jsonVal) {
            this.jsonVal = jsonVal;
        }

        public String getParameterName() {
            return parameterName;
        }

        public void setParameterName(String parameterName) {
            this.parameterName = parameterName;
        }
    }

    /**
     * 텍스트에 존재하는 괄호 내용 모두 추출 <p>
     *
     * @param text
     * @return
     */
    public static String findBracketTextByPattern(String text) {

        Matcher matcher = patternBlank.matcher(text);

        String pureText = text;
        String findText = new String();

        while (matcher.find()) {
            int startIndex = matcher.start();
            int endIndex = matcher.end();

            findText = pureText.substring(startIndex + 1, endIndex - 1);

            /** 추출된 괄호 데이터를 삽입  **/
            return findText;
        }

        return text;
    }

    /**
     * 텍스트 길이 수만큼 맞춰서 return
     *
     * @param in
     * @param size
     * @param padChar
     * @return
     */
    public static String pad(String in, int size, char padChar) {
        if (in.length() <= size) {
            char[] temp = new char[size];
            /* Llenado Array con el padChar*/
            for (int i = 0; i < size; i++) {
                temp[i] = padChar;
            }
            int posIniTemp = size - in.length();
            for (int i = 0; i < in.length(); i++) {
                temp[posIniTemp] = in.charAt(i);
                posIniTemp++;
            }
            return new String(temp);
        }
        return "";
    }

    public static int getSessionAttributeInt(String param, HttpSession session) {
        if (StringUtils.isEmpty(param)) {
            return 0;
        }

        String result = "";
        result = String.valueOf(session.getAttribute(param));
        if ("null".equals(result)) {
            return 0;
        }

        return Integer.parseInt(result);
    }

    public static String getSessionAttributeStr(String param, HttpSession session) {
        if (StringUtils.isEmpty(param)) {
            return "";
        }

        String result = "";

        result = String.valueOf(session.getAttribute(param));
        if ("null".equals(result)) {
            return "";
        }

        return result;
    }

    // escape 된 문자들(&,",',<,>,\,/,*,-)을 원복
    public static String unescapeStr(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        str = str.replaceAll("&amp;", "&");
        str = str.replaceAll("&apos;", "\'");
        str = str.replaceAll("&lsquo;", "\'");
        str = str.replaceAll("&rsquo;", "\'");
        str = str.replaceAll("&ldquo;", "\"");
        str = str.replaceAll("&rdquo;", "\"");
        str = str.replaceAll("&bsol;", "\\");
        str = str.replaceAll("&sol;", "/");
        str = str.replaceAll("&ast;", "*");
        str = str.replaceAll("&minus;", "-");

        // 아래 API에서 '(&apos)는 변환되지 않으므로 replaceAll로 변경
        return StringEscapeUtils.unescapeHtml3(str);
    }

    // XSS 필터링 공통 함수 - multipart form 전송인 경우 XSS 필터 함수를 거치지 않게 되어 별도 함수로 이를 처리토록 함.
    public static String strFilter(String str) {
        // 필터링할 문자열
        String filstr = "javascript,vbscript,expression,applet,blink,script,"
                + "embed,object,iframe,frame,frameset,ilayer,bgsound,base,details,eval,"
                + "innerHTML,charset,string,create,append,binding,alert,msgbox,refresh,confirm,"
                + "cookie,void,href,onabort,onactivae,onafterprint,onafterupdate,onbefore,onbeforeactivate,"
                + "onbeforecopy,onbeforecut,onbeforedeactivate,onbeforeeditfocus,onbeforepaste,onbeforeprint,"
                + "onbeforeunload,onbeforeupdate,onblur,onbounce,oncellchange,onchange,onclick,oncontextmenu,"
                + "oncontrolselect,oncopy,oncut,ondataavailable,ondatasetchanged,ondatasetcomplete,ondblclick,"
                + "ondeactivate,ondrag,ondragend,ondragenter,ondragleave,ondragover,ondragstart,ondrop,onerror,"
                + "onerrorupdate,onfilterchange,onfinish,onfocus,onfocusin,onfocusout,onhelp,onkeydown,onkeypress,"
                + "onkeyup,onlayoutcomplete,onload,onlosecapture,onmousedown,onmouseenter,onmouseleave,onmousemove,"
                + "onmouseout,onmouseover,onmouseup,onmousewheel,onmove,onmoveend,onmovestart,onpaste,onpropertychange,"
                + "onreadystatechange,onreset,onresize,onresizeend,onresizestart,onrowenter,onrowexit,onrowsdelete,"
                + "onrowsinserted,onscroll,onselect,onselectionchange,onselectstart,onstart,onstop,onsubmit,onunload,ontoggle,prompt";

        String convfilstr = "_javascript_,_vbscript_,_expression_,_applet_,_blink_,_script_,"
                + "_embed_,_object_,_iframe_,_frame_,_frameset_,_ilayer_,_bgsound_,_base_,_details_,_eval_,"
                + "_innerHTML_,_charset_,_string_,_create_,_append_,_binding_,_alert_,_msgbox_,_refresh_,_confirm_,"
                + "_cookie_,_void_,_href_,_onabort_,_onactivae_,_onafterprint_,_onafterupdate_,_onbefore_,_onbeforeactivate_,"
                + "_onbeforecopy_,_onbeforecut_,_onbeforedeactivate_,_onbeforeeditfocus_,_onbeforepaste_,_onbeforeprint_,"
                + "_onbeforeunload_,_onbeforeupdate_,_onblur_,_onbounce_,_oncellchange_,_onchange_,_onclick_,_oncontextmenu_,"
                + "_oncontrolselect_,_oncopy_,_oncut_,_ondataavailable_,_ondatasetchanged_,_ondatasetcomplete_,_ondblclick_,"
                + "_ondeactivate_,_ondrag_,_ondragend_,_ondragenter_,_ondragleave_,_ondragover_,_ondragstart_,_ondrop_,_onerror_,"
                + "_onerrorupdate_,_onfilterchange_,_onfinish_,_onfocus_,_onfocusin_,_onfocusout_,_onhelp_,_onkeydown_,_onkeypress_,"
                + "_onkeyup_,_onlayoutcomplete_,_onload_,_onlosecapture_,_onmousedown_,_onmouseenter_,_onmouseleave_,_onmousemove_,"
                + "_onmouseout_,_onmouseover_,_onmouseup_,_onmousewheel_,_onmove_,_onmoveend_,_onmovestart_,_onpaste_,_onpropertychange_,"
                + "_onreadystatechange_,_onreset_,_onresize_,_onresizeend_,_onresizestart_,_onrowenter_,_onrowexit_,_onrowsdelete_,"
                + "_onrowsinserted_,_onscroll_,_onselect_,_onselectionchange_,_onselectstart_,_onstart_,_onstop_,_onsubmit_,_onunload_,_ontoggle_,_prompt_";

        if (!convfilstr.equals("")) {
            convfilstr.replaceAll(" ", "");
            String[] convSt = convfilstr.split(",");
            String[] st = filstr.split(",");
            String[] ckst = str.split(" ");
//            for (int x = 0; x<convSt.length; x++) {
//                str = str.replaceAll(convSt[x], st[x]);
//            }
            for (int x = 0; x < convSt.length; x++) {
                for (int y = 0; y < ckst.length; y++) {
                    if (ckst[y].toLowerCase().contains(convSt[x])) {
                        ckst[y] = Pattern.compile(convSt[x], Pattern.MULTILINE | Pattern.CASE_INSENSITIVE).matcher(ckst[y]).replaceAll(st[x]);
                    }
                }
            }
            str = "";
            for (int z = 0; z < ckst.length; z++) {
                if (z == 0) {
                    str += ckst[z];
                } else {
                    str += " " + ckst[z];
                }
            }

        }

        if (!filstr.equals("")) {
            filstr.replaceAll(" ", "");
            String[] st = filstr.split(",");
            String[] ckst = str.split(" ");
//            for (int x = 0; x < st.length; x++) {
//                str = str.replaceAll(st[x], "_" + st[x] + "_");
//            }
            for (int x = 0; x < st.length; x++) {
                for (int y = 0; y < ckst.length; y++) {
                    if (ckst[y].toLowerCase().contains(st[x])) {
                        ckst[y] = Pattern.compile(st[x], Pattern.MULTILINE | Pattern.CASE_INSENSITIVE).matcher(ckst[y]).replaceAll("_" + st[x] + "_");
                    }
                }
            }
            str = "";
            for (int z = 0; z < ckst.length; z++) {
                if (z == 0) {
                    str += ckst[z];
                } else {
                    str += " " + ckst[z];
                }
            }

        }

        StringBuffer strBuff = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch (c) {
                case '<':
                    strBuff.append("&lt;");
                    break;
                case '>':
                    strBuff.append("&gt;");
                    break;
                case '&':
                    strBuff.append("&amp;");
                    break;
                case '"':
                    strBuff.append("&quot;");
                    break;
                case '\'':
                    strBuff.append("&apos;");
                    break;
                case '\\':
                    strBuff.append("&bsol;");
                    break;
                case '/':
                    strBuff.append("&sol;");
                    break;
                case '*':
                    strBuff.append("&ast;");
                    break;
                case '-':
                    strBuff.append("&minus;");
                    break;
                default:
                    strBuff.append(c);
                    break;
            }
        }

        str = strBuff.toString();

        return str;
    }

    /*
     * XSS 필터링된 텍스트 중 일부 사용 필요한 것에 대해 역필터링, flagStr에 따라 각각 설정 가능
     */
    public static String reConvFilter(String str, String flagStr) {

        if (str == null || str.isEmpty()) {
            return str;
        }

        String filStr = "";
        String reConvStr = "";

        switch (flagStr) {
            case "convHref":
                filStr = "_href_";
                reConvStr = "href";
                break;
            default:
                break;
        }

        if (!filStr.equals("")) {
            filStr.replaceAll(" ", "");
            String[] convSt = filStr.split(",");
            String[] st = reConvStr.split(",");
            for (int x = 0; x < convSt.length; x++) {
                str = str.replaceAll(convSt[x], st[x]);
            }
        }

        return str;
    }

    public static HttpSession getSessionVal(HttpServletRequest request, HttpSession session) {
        HttpSession resultSession = null;

        if (CommonFnc.getSessionAttributeInt("intteMbrSeq", session) == 0) {
            for (String key : EgovMultiLoginPreventor.loginUsers.keySet()) {
                if (EgovMultiLoginPreventor.loginUsers.get(key).getId().equals(request.getParameter("epSessionId"))) {
                    resultSession = EgovMultiLoginPreventor.loginUsers.get(key);
                }
            }
        } else {
            resultSession = session;
        }

        return resultSession;
    }

    /**
     * request 에 담긴 정보를 JSONObject string 형태로 반환한다.
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String getParams(HttpServletRequest request) throws IOException {
        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }


    /**
     * 바이트 체크한다. 기준보다 크면 false, 작거나 같으면 true
     *
     * @param txt          체크할 text
     * @param standardByte 기준 바이트 수
     * @return
     */
    public static boolean byteCheck(String txt, int standardByte) {
        if (StringUtils.isEmpty(txt)) {
            return true;
        }

        // 바이트 체크 (영문 1, 한글 2, 특문 1)
        int en = 0;
        int ko = 0;
        int etc = 0;

        char[] txtChar = txt.toCharArray();
        for (int j = 0; j < txtChar.length; j++) {
            if (txtChar[j] >= 'A' && txtChar[j] <= 'z') {
                en++;
            } else if (txtChar[j] >= '\uAC00' && txtChar[j] <= '\uD7A3') {
                ko++;
                ko++;
            } else {
                etc++;
            }
        }

        int txtByte = en + ko + etc;
        if (txtByte >= standardByte) {
            return false;
        }

        return true;
    }

    public static String convertNumberToString(int num) {
        String resultStr = String.valueOf(num);
        if (num <= 0) {
            return resultStr;
        }

        // A-Z, a-z 문자만 변환
        int value = 64 + num;
        if (value >= 91) {
            value += 6;
        }

        if (value > 122) {
            return resultStr;
        }

        return String.valueOf((char) value);
    }

}
