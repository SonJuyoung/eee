package ieetu.common.board;

import ieetu.common.dto.BoardDto;
import ieetu.common.entity.BoardEntity;
import ieetu.common.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("/list")
    public String board(Model model, @PageableDefault Pageable pageable) {

        model.addAttribute("fixList", boardRepository.fixList());
        model.addAttribute("fixListCount", boardRepository.fixList().size());
        model.addAttribute("list", boardRepository.List(pageable));
        model.addAttribute("count", boardRepository.findAllByOrderByFixDescIboardDesc().size());

        return "/board/board";
    }

    @GetMapping("/list/search")
    public String boardSearch(Model model, @RequestParam int category, String searchTxt, Pageable pageable) {


        model.addAttribute("fixList", boardRepository.fixList());

        if (category == 0) {
            model.addAttribute("list", boardRepository.searchByAll(searchTxt, pageable));
            model.addAttribute("count", boardRepository.searchByAll(searchTxt).size());
        } else if (category == 1) {
            model.addAttribute("list", boardRepository.searchByTitle(searchTxt));
            model.addAttribute("count", boardRepository.searchByTitle(searchTxt).size());
        } else if (category == 2) {
            model.addAttribute("list", boardRepository.searchByCtnt(searchTxt, pageable));
            model.addAttribute("count", boardRepository.searchByCtnt(searchTxt).size());
        } else if (category == 3) {
            model.addAttribute("list", boardRepository.searchByWriter(searchTxt, pageable));
            model.addAttribute("count", boardRepository.searchByWriter(searchTxt).size());
        }
        System.out.println("검색 된 것: " + model.getAttribute("list"));

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
