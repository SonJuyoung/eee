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

    public UserEntity getLoginUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String Uid = auth.getName();

        if (userRepository.findByUid(Uid).isPresent()) {
            UserEntity data = userRepository.findByUid(Uid).get();
            if (data == null) {
                data.setIuser(0);
            }

            return data;
        }
        return null;
    }

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
