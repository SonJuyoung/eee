package ieetu.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

// TODO: 오류 코드 정의
@ControllerAdvice
@Controller
@Slf4j
public class CommonExceptionHandler {
    private static final String[] AJAX_KEY = {"x-requested-with", "ajax"};
    private static final String[] AJAX_VALUE = {"xmlhttprequest", "true"};

    private final static String RESOLVER_DEFAULT_ERROR_VIEW = "error";
//    @ExceptionHandler(value = {IllegalArgumentException.class})
//    @ResponseBody
//    public Object handleIllegalArgumentException(Exception e, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        if(isNotAjaxRequest(request)) {
//            response.sendRedirect("/err");
//            return RESOLVER_DEFAULT_ERROR_VIEW;
//        }
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("success", false);
//        result.put("message", e.getMessage());
//        return result;
//    }
//
//    @ExceptionHandler(value = {SecurityException.class})
//    @ResponseBody
//    public Object handleSecurityException(Exception e, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        if(isNotAjaxRequest(request)) {
//            response.sendRedirect("/err");
//            return RESOLVER_DEFAULT_ERROR_VIEW;
//        }
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("success", false);
//        result.put("message", "login" + e.getMessage());
//        return result;
//    }
//
//    @ExceptionHandler(value = {BindException.class})
//    @ResponseBody
//    public Object handleBindException(Exception e, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        if(isNotAjaxRequest(request)) {
//            response.sendRedirect("/err");
//            return RESOLVER_DEFAULT_ERROR_VIEW;
//        }
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("success", false);
//        result.put("message", Validator.getErrorMsg(bindingResult));
//        return result;
//    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws Exception {
        e.printStackTrace();
        if(isNotAjaxRequest(request)) {
            response.sendRedirect("/err");
            return RESOLVER_DEFAULT_ERROR_VIEW;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", e.getMessage());


        return result;
    }

    private boolean isNotAjaxRequest(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            if(Arrays.asList(AJAX_KEY).contains(key)) {
                String value = request.getHeader(key);
                if(Arrays.asList(AJAX_VALUE).contains(value)) {
                    return false;
                }
            }
        }
        return true;
    }

    // ================ EgovConfigWebDispatcherServlet.configureHandlerExceptionResolvers 설정 =============== //

//    @ExceptionHandler(value = {DataAccessException.class})
//    public String handleDataAccessException(Exception e) {
////        return "cmm/error/dataAccessFailure";
//        return RESOLVER_DEFAULT_ERROR_VIEW;
//    }
//
//    @ExceptionHandler(value = {TransactionException.class})
//    public String handleTransactionException(Exception e) {
////        return "cmm/error/transactionFailure";
//        return RESOLVER_DEFAULT_ERROR_VIEW;
//    }
//
//    @ExceptionHandler(value = EgovBizException.class)
//    public String handleEgovBizException(Exception e) {
////        return "cmm/error/egovError";
//        return RESOLVER_DEFAULT_ERROR_VIEW;
//    }
//
//    @ExceptionHandler(value = AccessDeniedException.class)
//    public String handleAccessDeniedException(Exception e) {
////        return "cmm/error/accessDenied";
//        return RESOLVER_DEFAULT_ERROR_VIEW;
//    }
}
