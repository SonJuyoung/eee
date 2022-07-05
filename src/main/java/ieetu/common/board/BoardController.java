package ieetu.common.board;

import ieetu.common.dto.BoardDto;
import ieetu.common.entity.BoardEntity;
import ieetu.common.entity.UserEntity;
import ieetu.common.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.awt.print.Pageable;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/list")
    public String board(Model model, @PageableDefault(size = 5) Pageable pageable) {

        model.addAttribute("list", boardRepository.findAllByOrderByIboardDesc(pageable));

        return "/board/board";
    }

    @GetMapping("/write")
    public String write(Model model, HttpSession hs) {
        String userNm = (String) hs.getAttribute("loginUser");

        model.addAttribute("loginUser", userNm);
        return "/board/write";
    }

    @PostMapping("/write")
    @ResponseBody
    public int writePost(@RequestBody BoardDto dto) {

        BoardEntity entity = new BoardEntity();

        entity.setTitle(dto.getTitle());
        entity.setCtnt(dto.getCtnt());
        entity.setWriter(dto.getWriter());
        entity.setRdt(dto.getRdt());
        entity.setFix(dto.getFix());

        System.out.println(entity);

        return boardService.write(entity);
    }
}
