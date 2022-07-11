package ieetu.common.securityConfig.auth;

import ieetu.common.entity.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Optional;

// 스프링 시큐리티가 로그인 요청을 가로채서 로그인을 진행하고 완료가 되면 UserDetails 타입의 오브젝트를
// 스프링 시큐리티의 고유한 세션저장소에 저장을 해준다
@Getter
public class PrincipalDetail implements UserDetails {
    private UserEntity user; //컴포지션 (객체를 품고 있음)

    public PrincipalDetail(UserEntity user) {
        this.user = user; //
    }

    @Override
    public String getPassword() {
        return user.getUpw();
    }

    @Override
    public String getUsername() {
        return user.getUid();
    }

    //계정이 만료되지 않았는지 리턴(true:만료안됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠겨있지 않았는지 리턴(true:잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호가 만료되지 않았는지 리턴(true:만료 안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 활성화(사용가능)인지 리턴(true:활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }

    //계정 권한 목록 리턴(계정 권한 없으므로 null 리턴)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}