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


    @GetMapping("/login")
    public String login() {

        if (authenticationFacade.getLoginUserPk() > 0) {
            return "redirect:/board/list";
        }

        return "/login/login";
    }

    @GetMapping("/join")
    public String join() {
        return "/join/join";
    }

    @PostMapping("/join")
    @ResponseBody
    public int joinPost(@RequestBody UserDto dto) {



//        System.out.println(entity);

        userService.join(dto);

        return 1;
    }

    @PostMapping("/join/idchk")
    @ResponseBody
    public int idchk(@RequestBody String id) {

        return userService.idchk(id);
    }

    @PostMapping("/findId")
    @ResponseBody
    public String findId(@RequestBody UserDto dto) {

        if (userRepository.findByNameAndPhoneAndMail(dto.getName(), dto.getPhone(), dto.getMail()) != null) {
            System.out.println("아이디 : " + userRepository.findByNameAndPhoneAndMail(dto.getName(), dto.getPhone(), dto.getMail()));
            return userRepository.findByNameAndPhoneAndMail(dto.getName(), dto.getPhone(), dto.getMail()).getUid();
        } else {
            System.out.println("아이디 없음 : " + userRepository.findByNameAndPhoneAndMail(dto.getName(), dto.getPhone(), dto.getMail()));
            return "empty";
        }
    }

    @PostMapping("/findPw")
    @ResponseBody
    public int findPw(@RequestBody UserDto dto) {

        if (userRepository.findByUidAndNameAndPhoneAndMail(dto.getId(), dto.getName(), dto.getPhone(), dto.getMail()) != null) {
            System.out.println("아이디 : " + userRepository.findByUidAndNameAndPhoneAndMail(dto.getId(), dto.getName(), dto.getPhone(), dto.getMail()));
            return 1;
        } else {
            System.out.println("아이디 없음 : " + userRepository.findByUidAndNameAndPhoneAndMail(dto.getId(), dto.getName(), dto.getPhone(), dto.getMail()));
            return 0;
        }
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