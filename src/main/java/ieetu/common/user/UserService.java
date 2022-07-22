package ieetu.common.user;

import ieetu.common.dto.UserDto;
import ieetu.common.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.stringtemplate.v4.ST;

import javax.annotation.Resource;
import javax.persistence.Entity;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;

    @Transactional //jpa로 DB변경시 붙여줌. 원자성, 일관성, 격리성, 지속성 유지를 위해서
    public int join(UserDto dto) {

        //비밀번호 암호화
        String rawPassword = dto.getPw();
        String encPassword = encoder.encode(rawPassword);
//        String rawId = dto.getId();
//        String encId = encoder.encode(rawId);

        UserEntity entity = UserEntity.builder()
                .uid(dto.getId())
                .upw(encPassword)
                .mail(dto.getMail())
                .name(dto.getName())
                .phone(dto.getPhone())
                .postcode(dto.getPostcode())
                .address(dto.getAddress())
                .build();

//        entity.setUid(encId);
//        entity.setUid(dto.getId());
//        entity.setUpw(encPassword);
//        entity.setMail(dto.getMail());
//        entity.setName(dto.getName());
//        entity.setPhone(dto.getPhone());
//        entity.setPostcode(dto.getPostcode());
//        entity.setAddress(dto.getAddress());
        if (dto.getIuser() != 0) {
            entity.changeIuser(dto.getIuser());
        }

        try {
            userRepository.save(entity);

            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
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

    //시큐리티가 로그인 관리해주기 때문에 필요 없음
//    public UserEntity login(UserEntity entity) {
//
////        if (userRepository.findByUidAndUpw(entity.getUid(), entity.getUpw()) == null) {
////            System.out.println("로그인 실패");
////            return 0;
////        } else {
////            System.out.println("로그인 성공");
////            return 1;
////        }
//        return userRepository.findByUidAndUpw(entity.getUid(), entity.getUpw());
//    }

    //비밀번호 변경
    @Transactional //jpa로 DB변경시 붙여줌. 원자성, 일관성, 격리성, 지속성 유지를 위해서, 메서드가 포함하고 있는 작업 중에 하나라도 실패할 경우 전체 작업을 취소한다.
    public int pwChange(UserDto dto) {

        UserEntity entity = new UserEntity();

        //비밀번호 암호화
        String rawPassword = dto.getPw();
        String encPassword = encoder.encode(rawPassword);

        entity = userRepository.findByUid(dto.getId()).get();

        //비밀번호 변경 성공 시 1리턴 실패시 0리턴
        try {
            entity.changePw(encPassword);

//            userRepository.save(entity);

            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
