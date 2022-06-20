package ieetu.common.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommonController {
    final static String RESOLVER_DEFAULT_ERROR_VIEW = "error";

    @RequestMapping("/err")
    public String error() {
        return RESOLVER_DEFAULT_ERROR_VIEW;
    }
}
