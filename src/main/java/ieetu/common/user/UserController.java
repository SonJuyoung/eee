package ieetu.common.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String joinPost() {
        return "redirect:/board/list";
    }
}
