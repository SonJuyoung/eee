package ieetu.common.securityConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()//개발단계에서 csrf 해제
                .authorizeRequests()
                .antMatchers("/join", "/login", "/js/**", "/css/**")//해당 주소 요청은
                .permitAll()//다 허용한다
                .anyRequest()//나머지 요청은
                .authenticated()//인증 해야됨
                .and()
                .formLogin()
                .loginPage("/login")//시큐리티 기본 로그인 페이지가 아닌 로그인페이지 설정
                .loginProcessingUrl("/login")//해당주소에서 로그인 처리
                .failureUrl("/err")//에러페이지
                .defaultSuccessUrl("/board/list")//로그인 성공 시 페이지
                .and()
                .logout()//시큐리티 기본 로그아웃(세션제거) /logout
                .logoutSuccessUrl("/login")//로그아웃 성공 시 페이지
                .invalidateHttpSession(true);//브라우저가 완전히 종료되면 로그인한 정보(세션) 삭제
    }
}