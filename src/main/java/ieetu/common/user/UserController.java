package ieetu.common.user;

import ieetu.common.dto.UserDto;
import ieetu.common.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login() {
        return "/login/login";
    }

    @GetMapping("/join")
    public String join() {
        return "/join/join";
    }

    @PostMapping("/join")
    @ResponseBody
    public int joinPost(@RequestBody UserDto dto) {
        UserEntity entity = new UserEntity();

        entity.setUid(dto.getId());
        entity.setUpw(dto.getPw());
        entity.setMail(dto.getMail());
        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());

        System.out.println(entity);

        userService.join(entity);

        return 1;
    }

    @PostMapping("/join/idchk")
    @ResponseBody
    public int idchk(@RequestBody String id) {

        return userService.idchk(id);
    }

    @PostMapping("/login")
    @ResponseBody
    public int loginPost(@RequestBody UserDto dto, HttpSession hs, Model model) {

        UserEntity entity = new UserEntity();

        entity = userRepository.findByUidAndUpw(dto.getId(),dto.getPw());

        if (userService.login(entity) == 1) {
            System.out.println(entity.getName());
            hs.setAttribute("loginUser", entity.getName());
            System.out.println(hs.getAttribute("loginUser"));
            return 1;
        } else {
            return 0;
        }
    }
}