package ieetu.common.config.etc;

import org.egovframe.rte.fdl.cmmn.exception.handler.ExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 공통 서비스 개발팀 이삼섭
 * @Class Name : EgovComExcepHndlr.java
 * @Description : 공통서비스의 exception 처리 클래스
 * @Modification Information
 * <p>
 * 수정일       수정자         수정내용
 * -------        -------     -------------------
 * 2009. 3. 13.     이삼섭
 * @see
 * @since 2009. 3. 13.
 */
public class EgovComExcepHndlr implements ExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EgovComExcepHndlr.class);

    /**
     * 발생된 Exception을 처리한다.
     */
    public void occur(Exception ex, String packageName) {
        LOGGER.debug("[HANDLER][PACKAGE]::: {}", packageName);
        LOGGER.debug("[HANDLER][Exception]:::", ex);
    }
}
