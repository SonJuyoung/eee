package ieetu.common.securityConfig;

import ieetu.common.securityConfig.auth.PrincipalDetailService;
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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 주소로 접근하면 권한 및 인증을 미리 체크
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalDetailService principalDetailService;
    @Autowired
    private final AuthenticationFailureHandler customFailureHandler;
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    //시큐리티가 로그인 과정에서 password를 가로챌때 어떤 해쉬로 암호화 했는지 확인
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailService).passwordEncoder(encoder());
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()//개발단계에서 csrf 해제
                .authorizeRequests()
                .antMatchers("/join/**", "/login", "/js/**", "/css/**", "/findId", "/findPw", "/changePw")//해당 주소 요청은
                .permitAll()//다 허용한다
                .anyRequest()//나머지 요청은
                .authenticated()//인증 해야됨
                .and()
                .formLogin()
                .loginPage("/login")//시큐리티 기본 로그인 페이지가 아닌 로그인페이지 설정
                .loginProcessingUrl("/login")//해당주소에서 로그인 처리
                .failureHandler(customFailureHandler)
                .defaultSuccessUrl("/board/list")//로그인 성공 시 페이지
                .and()
                .logout()//시큐리티 기본 로그아웃(세션제거) /logout
                .logoutSuccessUrl("/login")//로그아웃 성공 시 페이지
                .invalidateHttpSession(true);//브라우저가 완전히 종료되면 로그인한 정보(세션) 삭제
    }
}