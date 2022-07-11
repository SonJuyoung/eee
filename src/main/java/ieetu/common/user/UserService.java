package ieetu.common.user;

import ieetu.common.dto.UserDto;
import ieetu.common.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public int join(UserDto dto) {

        UserEntity entity = new UserEntity();

        //아이디 비밀번호 암호화
        String rawPassword = dto.getPw();
        String encPassword = encoder.encode(rawPassword);
//        String rawId = dto.getId();
//        String encId = encoder.encode(rawId);

//        entity.setUid(encId);
        entity.setUid(dto.getId());
        entity.setUpw(encPassword);
        entity.setMail(dto.getMail());
        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());

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

    public UserEntity login(UserEntity entity) {

//        if (userRepository.findByUidAndUpw(entity.getUid(), entity.getUpw()) == null) {
//            System.out.println("로그인 실패");
//            return 0;
//        } else {
//            System.out.println("로그인 성공");
//            return 1;
//        }
        return userRepository.findByUidAndUpw(entity.getUid(), entity.getUpw());
    }
}
