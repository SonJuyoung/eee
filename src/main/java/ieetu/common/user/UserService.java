package ieetu.common.user;

import ieetu.common.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public int join(UserEntity entity) {

        userRepository.save(entity);

        return 1;
    }

    public int idchk(String id) {
        List list = new ArrayList<>();

        for (UserEntity entity : userRepository.findAll()) {
            list.add(entity.getUid());
        }
        System.out.println(list);
        System.out.println(id);
        if (list.contains(id)) {
            System.out.println("중복");
            return 1;
        } else {
            System.out.println("중복아님");
            return 0;
        }
    }

    public int login(UserEntity entity) {

        if (userRepository.findByUidAndUpw(entity.getUid(), entity.getUpw()) == null) {
            System.out.println("로그인 실패");
            return 0;
        } else {
            System.out.println("로그인 성공");
            return 1;
        }
    }
}
