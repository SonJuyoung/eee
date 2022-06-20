
package ieetu.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 파라미터 유효성 검증 관련 클래스를 정의한다
 *
 * @author A2TEC
 * @version 1.0
 * @see <pre>
 * << 개정이력(Modification Information) >>
 * 수정일      수정자           수정내용
 * -------    -------------    ----------------------
 * 2018. 6. 15.   A2TEC      최초생성
 *
 *
 * </pre>
 * @since 2018.06.15
 */

public class Validator {

    public static final String REQUIRED = "required";
    public static final String NUMBER = "number";
    public static final String LENGTH = "length";
    public static final String MIN_LENGTH = "minLength";
    public static final String MAX_LENGTH = "maxLength";
    public static final String TEL_NO = "telNo";
    public static final String PHONE_NO = "phoneNo";
    public static final String EMAIL = "email";
    public static final String BIZ_NO = "bizNo";
    public static final String IP_ADDRESS = "ipAddress";
    public static final String DATE = "date";
    public static final String DATE_TYPE = "dateType";
    public static final String TIME = "time";
    public static final String DATETIME = "dateTime";
    public static final String DAY_OF_WEEK = "dayOfWeek";
    public static final String YN = "yn";
    public static final String ID = "memberId";
    public static final String PWD = "memberPassword";
    public static final String NAME = "memberName";
    public static final String EMPTY_PARAM = "";
    public static final String LONG = "long";

    static private Map<String, Object> parse(String request) {
        if (StringUtils.isEmpty(request))
            return null;

        Map<String, Object> requestMap = new LinkedHashMap<String, Object>();
        String[] requests = request.split(";");
        for (int i = 0; i < requests.length; i++) {
            String item = requests[i];

            Map<String, String> attributeMap = new LinkedHashMap<String, String>();
            attributeMap.put(REQUIRED, EMPTY_PARAM);

            int nameEndIndex = item.indexOf(":");
            if (nameEndIndex == -1) {
                requestMap.put(item, attributeMap);
                continue;
            }

            String objName = item.substring(0, nameEndIndex).trim();
            String attributeText = item.substring(nameEndIndex + 1).trim();

            int openBracketIndex = attributeText.indexOf("{");
            int closeBracketIndex = attributeText.lastIndexOf("}");

            boolean errorSyntax = (openBracketIndex == -1) || (closeBracketIndex == -1);
            if (errorSyntax) {
                return null;
            }
            attributeText = attributeText.substring(openBracketIndex + 1, closeBracketIndex).trim();
            if (!attributeText.isEmpty()) {
                String[] attributes = attributeText.split("&");
                for (int j = 0; j < attributes.length; j++) {
                    String[] attributeInfo = attributes[j].split("=");
                    String key = attributeInfo[0].trim();
                    String value = (attributeInfo.length == 1) ? EMPTY_PARAM : attributeInfo[1].trim();
                    attributeMap.put(key, value);
                }
            }
            requestMap.put(objName, attributeMap);
        }
        return requestMap;
    }

    @SuppressWarnings("unchecked")
    static public String validate(HttpServletRequest request, String requestParam) {
        Map<String, Object> requestMap = parse(requestParam);
        if (requestMap == null || requestMap.isEmpty()) {
            return "error validator";
        }

        String invalidTargets = "";
        Iterator<String> targetIterator = requestMap.keySet().iterator();

        while (targetIterator.hasNext()) {
            String targetName = targetIterator.next();
            String targetValue = request.getParameter(targetName);

            String invalidTarget = "";
            boolean isValidTarget = true;

            Map<String, String> validationMap = (Map<String, String>) requestMap.get(targetName);
            Iterator<String> validationsIterator = validationMap.keySet().iterator();
            outer:
            while (validationsIterator.hasNext()) {
                String validationName = validationsIterator.next();
                String validationValue = validationMap.get(validationName);

                boolean isValidPart = true;

                switch (validationName) {
                    case REQUIRED:
                        isValidPart = FormatCheckUtils.checkNotEmpty(targetValue);
                        if (!isValidPart) {
                            invalidTarget = validationName;
                            isValidTarget = isValidPart;
                            break outer;
                        }
                        break;
                    case NUMBER:
                        isValidPart = FormatCheckUtils.checkNumber(targetValue);
                        break;
                    case LENGTH:
                        String[] length = validationValue.split(",");
                        int minLength = Integer.parseInt(length[0].trim());
                        int maxLength = Integer.parseInt(length[1].trim());
                        isValidPart = FormatCheckUtils.checkLength(targetValue, minLength, maxLength);
                        break;
                    case MIN_LENGTH:
                        isValidPart = FormatCheckUtils.checkMinLength(targetValue, Integer.parseInt(validationValue));
                        break;
                    case MAX_LENGTH:
                        isValidPart = FormatCheckUtils.checkMaxLength(targetValue, Integer.parseInt(validationValue));
                        break;
                    case TEL_NO:
                        isValidPart = FormatCheckUtils.checkFormatTell(targetValue);
                        break;
                    case PHONE_NO:
                        isValidPart = FormatCheckUtils.checkFormatCell(targetValue);
                        break;
                    case EMAIL:
                        isValidPart = FormatCheckUtils.checkFormatMail(targetValue);
                        break;
                    case BIZ_NO:
                        isValidPart = FormatCheckUtils.checkCompNumber(targetValue);
                        break;
                    case IP_ADDRESS:
                        isValidPart = FormatCheckUtils.checkIPAddress(targetValue);
                        break;
                    case DATE:
                        isValidPart = FormatCheckUtils.checkDate(targetValue);
                        break;
                    case DATE_TYPE:
                        isValidPart = FormatCheckUtils.checkDateType(targetValue);
                        break;
                    case TIME:
                        isValidPart = FormatCheckUtils.checkTime(targetValue);
                        break;
                    case DATETIME:
                        isValidPart = FormatCheckUtils.checkDateTime(targetValue);
                        break;
                    case DAY_OF_WEEK:
                        isValidPart = FormatCheckUtils.checkDayOfWeek(targetValue);
                        break;
                    case YN:
                        isValidPart = FormatCheckUtils.checkYn(targetValue);
                        break;
                    case ID:
                        isValidPart = FormatCheckUtils.checkMemberId(targetValue);
                        validationName = "format";
                        break;
                    case PWD:
                        isValidPart = FormatCheckUtils.checkMemberPassword(targetValue);
                        validationName = "format";
                        break;
                    case NAME:
                        isValidPart = FormatCheckUtils.checkMemberName(targetValue);
                        validationName = "format";
                        break;
                    case LONG:
                        isValidPart = FormatCheckUtils.checkLong(targetValue);
                        break;
                    default:
                        isValidPart = false;
                        validationName += " is not supported";
                        break;
                }

                if (!isValidPart) {
                    if (!invalidTarget.isEmpty()) {
                        invalidTarget += ",";
                    }
                    invalidTarget += validationName;
                    isValidTarget &= isValidPart;
                }
            }

            if (!isValidTarget) {
                if (!invalidTargets.isEmpty()) {
                    invalidTargets += ",";
                }
                invalidTargets += targetName + "(" + invalidTarget + ")";
            }
        }

        return invalidTargets;
    }

    static public String validate(Object defaultDto, String requestParam) throws Exception {
        Map<String, Object> requestMap = parse(requestParam);
        if (requestMap == null || requestMap.isEmpty()) {
            return "error validator";
        }

        String invalidTargets = "";
        Iterator<String> targetIterator = requestMap.keySet().iterator();

        while (targetIterator.hasNext()) {
            String targetName = targetIterator.next();
            Method method = defaultDto.getClass().getMethod("get" + upperCaseFirst(targetName));
            String targetValue = "";
            if (method.invoke(defaultDto) != null) {
                targetValue = method.invoke(defaultDto).toString();
            }

            String invalidTarget = "";
            boolean isValidTarget = true;

            Map<String, String> validationMap = (Map<String, String>) requestMap.get(targetName);
            Iterator<String> validationsIterator = validationMap.keySet().iterator();
            outer:
            while (validationsIterator.hasNext()) {
                String validationName = validationsIterator.next();
                String validationValue = validationMap.get(validationName);

                boolean isValidPart = true;

                switch (validationName) {
                    case REQUIRED:
                        isValidPart = FormatCheckUtils.checkNotEmpty(targetValue);
                        if (!isValidPart) {
                            invalidTarget = validationName;
                            isValidTarget = isValidPart;
                            break outer;
                        }
                        break;
                    case NUMBER:
                        isValidPart = FormatCheckUtils.checkNumber(targetValue);
                        break;
                    case LENGTH:
                        String[] length = validationValue.split(",");
                        int minLength = Integer.parseInt(length[0].trim());
                        int maxLength = Integer.parseInt(length[1].trim());
                        isValidPart = FormatCheckUtils.checkLength(targetValue, minLength, maxLength);
                        break;
                    case MIN_LENGTH:
                        isValidPart = FormatCheckUtils.checkMinLength(targetValue, Integer.parseInt(validationValue));
                        break;
                    case MAX_LENGTH:
                        isValidPart = FormatCheckUtils.checkMaxLength(targetValue, Integer.parseInt(validationValue));
                        break;
                    case TEL_NO:
                        isValidPart = FormatCheckUtils.checkFormatTell(targetValue);
                        break;
                    case PHONE_NO:
                        isValidPart = FormatCheckUtils.checkFormatCell(targetValue);
                        break;
                    case EMAIL:
                        isValidPart = FormatCheckUtils.checkFormatMail(targetValue);
                        break;
                    case BIZ_NO:
                        isValidPart = FormatCheckUtils.checkCompNumber(targetValue);
                        break;
                    case IP_ADDRESS:
                        isValidPart = FormatCheckUtils.checkIPAddress(targetValue);
                        break;
                    case DATE:
                        isValidPart = FormatCheckUtils.checkDate(targetValue);
                        break;
                    case DATE_TYPE:
                        isValidPart = FormatCheckUtils.checkDateType(targetValue);
                        break;
                    case TIME:
                        isValidPart = FormatCheckUtils.checkTime(targetValue);
                        break;
                    case DATETIME:
                        isValidPart = FormatCheckUtils.checkDateTime(targetValue);
                        break;
                    case DAY_OF_WEEK:
                        isValidPart = FormatCheckUtils.checkDayOfWeek(targetValue);
                        break;
                    case YN:
                        isValidPart = FormatCheckUtils.checkYn(targetValue);
                        break;
                    case ID:
                        isValidPart = FormatCheckUtils.checkMemberId(targetValue);
                        validationName = "format";
                        break;
                    case PWD:
                        isValidPart = FormatCheckUtils.checkMemberPassword(targetValue);
                        validationName = "format";
                        break;
                    case NAME:
                        isValidPart = FormatCheckUtils.checkMemberName(targetValue);
                        validationName = "format";
                        break;
                    case LONG:
                        isValidPart = FormatCheckUtils.checkLong(targetValue);
                        break;
                    default:
                        isValidPart = false;
                        validationName += " is not supported";
                        break;
                }

                if (!isValidPart) {
                    if (!invalidTarget.isEmpty()) {
                        invalidTarget += ",";
                    }
                    invalidTarget += validationName;
                    isValidTarget &= isValidPart;
                }
            }

            if (!isValidTarget) {
                if (!invalidTargets.isEmpty()) {
                    invalidTargets += ",";
                }
                invalidTargets += targetName + "(" + invalidTarget + ")";
            }
        }

        return invalidTargets;
    }

    public static String getErrorMsg(BindingResult errors) {
        if(!errors.hasErrors()) {
            return "";
        }

        StringBuilder invalidTargets = new StringBuilder();

        List<FieldError> fieldErrors = errors.getFieldErrors();
        for(int i=0; i< fieldErrors.size(); i++) {
            FieldError error = fieldErrors.get(i);
            invalidTargets.append(String.format("%s(%s)", error.getField(), error.getDefaultMessage()));
            if( i != fieldErrors.size() -1 ) {
                invalidTargets.append(",");
            }
        }

        return invalidTargets.toString();
    }

    public static String upperCaseFirst(String val) {
        char[] arr = val.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return new String(arr);
    }
}
