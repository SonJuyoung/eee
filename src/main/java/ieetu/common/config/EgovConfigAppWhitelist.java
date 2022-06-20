package ieetu.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : 윤주호
 * @version : 1.0
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일              수정자               수정내용
 *  -------------  ------------   ---------------------
 *   2021. 7. 20    윤주호               최초 생성
 * </pre>
 * @ClassName : EgovConfigAppWhitelist.java
 * @Description : whiteList 설정
 * @since : 2021. 7. 20
 */
@Configuration
public class EgovConfigAppWhitelist {

    @Bean
    public List<String> egovPageLinkWhitelist() {
        List<String> whiteList = new ArrayList<String>();
//		whiteList.add("main/inc/EgovIncHeader");
        return whiteList;
    }
}