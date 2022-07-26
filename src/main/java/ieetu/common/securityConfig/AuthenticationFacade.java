package ieetu.common.securityConfig;

import ieetu.common.entity.UserEntity;
import ieetu.common.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {

    @Autowired
    private UserRepository userRepository;

    //로그인된 유저정보
    public UserEntity getLoginUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String Uid = auth.getName();

        if (userRepository.findByUid(Uid).isPresent()) {
            UserEntity data = userRepository.findByUid(Uid).get();
            if (data == null) {
                data = new UserEntity(0);
            }

            return data;
        }
        return null;
    }

    //로그인된 유저의 iuser값 리턴, 로그인상태 아니면 0 리턴
    public int getLoginUserPk() {
        int loginUserPk = 0;
        try {
            if (getLoginUser() != null) {
                loginUserPk = getLoginUser().getIuser();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return loginUserPk;
    }
}
