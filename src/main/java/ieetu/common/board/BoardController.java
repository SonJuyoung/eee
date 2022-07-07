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
    public String board(Model model, @PageableDefault Pageable pageable, HttpSession hs) {

        if (hs.getAttribute("loginUser") == null) {
            return "redirect:/login";
        }

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

        if (hs.getAttribute("loginUser") == null) {
            return "redirect:/login";
        }

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

    @GetMapping("/mod")
    public String mod(Model model, HttpSession hs, @RequestParam int iboard) {

        String sUser = (String) hs.getAttribute("loginUser");
        String bUser = boardRepository.findByIboard(iboard).getWriter();

        String userNm = (String) hs.getAttribute("loginUser");

        model.addAttribute("loginUser", userNm);

        if (hs.getAttribute("loginUser") == null) {
            return "redirect:/login";
        } else if (sUser.equals(bUser)) {
            model.addAttribute("detail", boardRepository.findByIboard(iboard));
            return "/board/mod";
        } else {
            return "redirect:/board/list";
        }
    }

    @PostMapping("/mod")
    @ResponseBody
    public int modPost(@RequestBody BoardDto dto) {

        BoardEntity entity = new BoardEntity();

        entity.setIboard(dto.getIboard());
        entity.setTitle(dto.getTitle());
        entity.setCtnt(dto.getCtnt());
        entity.setWriter(dto.getWriter());
        entity.setRdt(dto.getRdt());
        entity.setFix(dto.getFix());

        System.out.println(entity);

        return boardService.write(entity);
    }

    @GetMapping("/detail")
    public String detail(@RequestParam int iboard, Model model, HttpSession hs) {

        if (boardRepository.findByIboard(iboard) == null) {
            return "redirect:/board/list";
        }

        model.addAttribute("detail", boardRepository.findByIboard(iboard));

        String sUser = (String) hs.getAttribute("loginUser");
        String bUser = boardRepository.findByIboard(iboard).getWriter();

        System.out.println("세션 유저: " + sUser);
        System.out.println("게시판 유저: " + bUser);

        if (sUser.equals(bUser)) {
            model.addAttribute("user", "same");
            System.out.println("아이디 같음");
        }

        //이전, 다음 게시물
        if (boardRepository.findPrev(iboard).size() != 0) {
            int prevIboard = boardRepository.findPrev(iboard).get(0).getIboard();
            System.out.println("이전 게시물 :" + prevIboard);
            model.addAttribute("prevIboard", prevIboard);
        } else {
            model.addAttribute("prevIboard", "first");
        }

        if (boardRepository.findNext(iboard).size() != 0) {
            int nextIboard = boardRepository.findNext(iboard).get(0).getIboard();
            System.out.println("다음 게시물 :" + nextIboard);
            model.addAttribute("nextIboard", nextIboard);
        } else {
            model.addAttribute("nextIboard", "last");
        }

        return "/board/detail";
    }
    @GetMapping("/delete")
    public String delete(@RequestParam int iboard, HttpSession hs) {

        String sUser = (String) hs.getAttribute("loginUser");
        String bUser = boardRepository.findByIboard(iboard).getWriter();

        if (sUser.equals(bUser)) {
            boardRepository.deleteByIboard(iboard);
        }
        return "redirect:/board/list";
    }


}
