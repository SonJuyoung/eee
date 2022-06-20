package ieetu;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import ieetu.common.config.EgovWebApplicationInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@ServletComponentScan
@SpringBootApplication
@Import({EgovWebApplicationInitializer.class})
public class EgovBootApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        System.out.println("##### EgovBootApplication Start #####");

        SpringApplication springApplication = new SpringApplication(EgovBootApplication.class);
        //springApplication.setBannerMode(Banner.Mode.OFF);
        //springApplication.setLogStartupInfo(false);
        springApplication.run(args);

        System.out.println("##### EgovBootApplication End #####");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(EgovBootApplication.class);
    }

    @Bean
    Hibernate5Module hibernate5Module() {
        Hibernate5Module hibernate5Module = new Hibernate5Module(); // 강제로 지연로딩 해서 엔티티 정보를 가져오도록 설정 한다.
        hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
        return hibernate5Module;
    }
}
