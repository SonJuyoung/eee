package ieetu.common.user;

import ieetu.common.dto.UserDto;
import ieetu.common.entity.UserEntity;
import ieetu.common.securityConfig.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    //기본 인덱스 주소로 접속 시 로그인 페이지로
    @GetMapping
    public String index() {
        return "redirect:/login";
    }

    //로그인 페이지
    @GetMapping("/login")
    public String login() {

        //로그인 한 유저인 경우 게시판으로 이동
        if (authenticationFacade.getLoginUserPk() > 0) {
            return "redirect:/board/list";
        }

        return "/login/login";
    }

    //회원가입 페이지
    @GetMapping("/join")
    public String join() {
        return "/join/join";
    }

    //회원가입
    @PostMapping("/join")
    @ResponseBody
    public int joinPost(@RequestBody UserDto dto) {

        userService.join(dto);

        return 1;
    }

    //아이디 중복 검사
    @PostMapping("/join/idchk")
    @ResponseBody
    public int idchk(@RequestBody String id) {

        return userService.idchk(id);
    }

    //아이디 찾기
    @PostMapping("/findId")
    @ResponseBody
    public String findId(@RequestBody UserDto dto) {

        if (userRepository.findByNameAndPhoneAndMail(dto.getName(), dto.getPhone(), dto.getMail()) != null) { //이름, 휴대폰번호, 메일으로 찾았을 때 아이디가 있는 경우
            System.out.println("아이디 : " + userRepository.findByNameAndPhoneAndMail(dto.getName(), dto.getPhone(), dto.getMail()));
            return userRepository.findByNameAndPhoneAndMail(dto.getName(), dto.getPhone(), dto.getMail()).getUid(); //아이디 리턴
        } else { //아이디가 없는 경우
            System.out.println("아이디 없음 : " + userRepository.findByNameAndPhoneAndMail(dto.getName(), dto.getPhone(), dto.getMail()));
            return "";
        }
    }

    //비밀번호 찾기
    @PostMapping("/findPw")
    @ResponseBody
    public int findPw(@RequestBody UserDto dto) {

        if (userRepository.findByUidAndNameAndPhoneAndMail(dto.getId(), dto.getName(), dto.getPhone(), dto.getMail()) != null) { //아이디, 이름, 휴대폰번호, 메일으로 찾았을 때 일치하는 정보가 있는 경우
            System.out.println("아이디 : " + userRepository.findByUidAndNameAndPhoneAndMail(dto.getId(), dto.getName(), dto.getPhone(), dto.getMail()));
            return 1;
        } else { //일치하는 정보가 없는 경우
            System.out.println("아이디 없음 : " + userRepository.findByUidAndNameAndPhoneAndMail(dto.getId(), dto.getName(), dto.getPhone(), dto.getMail()));
            return 0;
        }
    }

    //비밀번호 변경
    @PostMapping("/changePw")
    @ResponseBody
    public int changePw(@RequestBody UserDto dto) {

        return userService.pwChange(dto);

    }
//
//    @PostMapping("/login")
//    @ResponseBody
//    public int loginPost(@RequestBody UserDto dto, HttpSession hs) {
//
//        UserEntity entity = new UserEntity();
//
//        entity.setUid(dto.getId());
//        entity.setUpw(dto.getPw());
//        System.out.println("123123");
//        System.out.println(entity);
//
//
////        entity = userRepository.findByUidAndUpw(dto.getId(),dto.getPw());
////
////        if (userService.login(entity) == 1) {
////            System.out.println(entity.getName());
////            hs.setAttribute("loginUser", entity.getName());
////            System.out.println(hs.getAttribute("loginUser"));
////            return 1;
////        } else {
////            return 0;
////        }
//        if (userService.login(entity) != null) {
//            hs.setAttribute("loginUser", entity.getName());
//            return 1;
//        } else return 0;
//    }

//    @GetMapping("/logout")
//    public String logout(HttpSession hs) {
//        if (hs.getAttribute("loginUser") != null) {
//            hs.invalidate();
//            return "redirect:/login";
//        }
//        return "/login/login";
//    }
}