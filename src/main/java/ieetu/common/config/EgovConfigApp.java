package ieetu.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@Import({
        EgovConfigAppCommon.class,
        EgovConfigAppDatasource.class,
        EgovConfigAppMapper.class,
        EgovConfigAppTransaction.class,
        EgovConfigAppWhitelist.class
})
@PropertySources({
        @PropertySource("classpath:/egovframework/egovProps/globals.properties"),
        @PropertySource("classpath:/egovframework/egovProps/jdbc.properties")
}) //CAUTION: min JDK 8
public class EgovConfigApp {

}
