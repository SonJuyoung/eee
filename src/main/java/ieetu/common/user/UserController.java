package ieetu.common.user;

import ieetu.common.dto.UserDto;
import ieetu.common.entity.UserEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("")
public class UserController {

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
    public String joinPost(UserDto dto) {
        UserEntity entity = new UserEntity();

        entity.setId(dto.getId());
        entity.setPw(dto.getPw());
        entity.setMail(dto.getMail());
        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());

        return "redirect:/board/list";
    }
}
