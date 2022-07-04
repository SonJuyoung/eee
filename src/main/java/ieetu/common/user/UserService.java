package ieetu.common.user;

import ieetu.common.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public int join(UserEntity entity) {

        userRepository.save(entity);

        return 1;
    }
}
