package ieetu.common.board;

import antlr.StringUtils;
import ieetu.common.board.reply.ReplyRepository;
import ieetu.common.board.reply.ReplyService;
import ieetu.common.dto.BoardDto;
import ieetu.common.dto.FileDto;
import ieetu.common.dto.ReplyDto;
import ieetu.common.entity.BoardEntity;
import ieetu.common.entity.FileEntity;
import ieetu.common.entity.ReplyEntity;
import ieetu.common.entity.UserEntity;
import ieetu.common.securityConfig.AuthenticationFacade;
import ieetu.common.user.ProfileImgRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.hsqldb.lib.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final AuthenticationFacade authenticationFacade;
    private final FileRepository fileRepository;
    private final ReplyService replyService;
    private final ReplyRepository replyRepository;
    private final ProfileImgRepository profileImgRepository;

    @GetMapping("/list")
    public String board(Model model, Pageable pageable) {

        UserEntity entity = new UserEntity();
        entity.changeIuser(authenticationFacade.getLoginUserPk());

        //로그인 유저 정보 없을 시 로그인 페이지로
        if (authenticationFacade.getLoginUserPk() < 1) {
            return "redirect:/login";
        }

        //프로필 이미지가 있을 때 이미지 불러오기
        if (boardService.callList() == 1) {
            model.addAttribute("profileImg", profileImgRepository.findByIuser(entity).getFileNm());
        }
        model.addAttribute("list", boardRepository.findAllByOrderByIboardDesc(pageable));
        System.out.println("리스트 : " + boardRepository.findAll(pageable));

        model.addAttribute("fixList", boardRepository.fixList()); //공지사항 게시물
        model.addAttribute("fixListCount", boardRepository.fixList().size()); //공지사항 게시물 갯수
        model.addAttribute("normalList", boardRepository.List(pageable)); //일반 게시물
        model.addAttribute("count", boardRepository.findAllByOrderByFixDescIboardDesc().size()); //일반 게시물 갯수
        model.addAttribute("user", authenticationFacade.getLoginUser()); //로그인 유저 정보

        return "/board/board";
    }

    //게시판 검색 시 페이지
    @GetMapping("/list/search")
    public String boardSearch(Model model, @RequestParam int category1, int category2, String searchTxt, @Nullable String startDate, @Nullable String endDate, Pageable pageable) {

        //로그인 유저 정보 없을 시 로그인 페이지로
        if (authenticationFacade.getLoginUserPk() < 1) {
            return "redirect:/login";
        }

        UserEntity entity = new UserEntity();
        entity.changeIuser(authenticationFacade.getLoginUserPk());

        if (profileImgRepository.findByIuser(entity) != null) {
            System.out.println("파일 : " + profileImgRepository.findByIuser(entity).getFileNm());
            model.addAttribute("profileImg", profileImgRepository.findByIuser(entity).getFileNm());
        }
        model.addAttribute("user", authenticationFacade.getLoginUser()); //로그인 유저 정보
        model.addAttribute("list", boardRepository.findAllByOrderByIboardDesc(pageable));
        System.out.println("리스트 : " + boardRepository.findAll(pageable));
        model.addAttribute("fixList", boardRepository.fixList()); //공지사항 게시물

        if (startDate == null || endDate == null) {
            if (category1 == 0) {
                if (category2 == 0) {
                    model.addAttribute("list", boardRepository.searchByAll(searchTxt, pageable)); //전체 검색 게시물
                    model.addAttribute("count", boardRepository.searchByAll(searchTxt).size()); //전체 검색 게시물 갯수
                } else if (category2 == 1) {
                    model.addAttribute("list", boardRepository.searchByTitle(searchTxt, pageable)); //제목 검색 게시물
                    model.addAttribute("count", boardRepository.searchByTitle(searchTxt).size()); //제목 검색 게시물 갯수
                } else if (category2 == 2) {
                    model.addAttribute("list", boardRepository.searchByCtnt(searchTxt, pageable)); //내용 검색 게시물
                    model.addAttribute("count", boardRepository.searchByCtnt(searchTxt).size()); //내용 검색 게시물 갯수
                } else if (category2 == 3) {
                    model.addAttribute("list", boardRepository.searchByWriter(searchTxt, pageable)); //작성자 검색 게시물
                    model.addAttribute("count", boardRepository.searchByWriter(searchTxt).size()); //작성자 검색 게시물 갯수
                } else if (category2 == 4) {
                    model.addAttribute("list", boardRepository.searchByTitleAndCtnt(searchTxt, pageable)); //제목+내용 검색 게시물
                    model.addAttribute("count", boardRepository.searchByTitleAndCtnt(searchTxt).size()); //제목+내용 검색 게시물 갯수
                } else if (category2 == 5) {
                    model.addAttribute("list", boardRepository.searchByTitleAndWriter(searchTxt, pageable)); //제목+작성자 검색 게시물
                    model.addAttribute("count", boardRepository.searchByTitleAndWriter(searchTxt).size()); //제목+작성자 검색 게시물 갯수
                } else if (category2 == 6) {
                    model.addAttribute("list", boardRepository.searchByCtntAndWriter(searchTxt, pageable)); //내용+작성자 검색 게시물
                    model.addAttribute("count", boardRepository.searchByCtntAndWriter(searchTxt).size()); //내용+작성자 검색 게시물 갯수
                }
            } else if (category1 == 1) {
                if (category2 == 0) {
                    model.addAttribute("list", boardRepository.searchByAllFix(searchTxt, pageable)); //공지사항 검색 게시물
                    model.addAttribute("count", boardRepository.searchByAllFix(searchTxt).size()); //공지사항 검색 게시물 갯수
                } else if (category2 == 1) {
                    model.addAttribute("list", boardRepository.searchByTitleFix(searchTxt, pageable)); //공지사항 제목 검색 게시물
                    model.addAttribute("count", boardRepository.searchByTitleFix(searchTxt).size()); //공지사항 제목 검색 게시물 갯수
                } else if (category2 == 2) {
                    model.addAttribute("list", boardRepository.searchByCtntFix(searchTxt, pageable)); //공지사항 내용 검색 게시물
                    model.addAttribute("count", boardRepository.searchByCtntFix(searchTxt).size()); //공지사항 내용 검색 게시물 갯수
                } else if (category2 == 3) {
                    model.addAttribute("list", boardRepository.searchByWriterFix(searchTxt, pageable)); //공지사항 작성자 검색 게시물
                    model.addAttribute("count", boardRepository.searchByWriterFix(searchTxt).size()); //공지사항 작성자 검색 게시물 갯수
                } else if (category2 == 4) {
                    model.addAttribute("list", boardRepository.searchByTitleAndCtntFix(searchTxt, pageable)); //공지사항 제목+내용 검색 게시물
                    model.addAttribute("count", boardRepository.searchByTitleAndCtntFix(searchTxt).size()); //공지사항 제목+내용 검색 게시물 갯수
                } else if (category2 == 5) {
                    model.addAttribute("list", boardRepository.searchByTitleAndWriterFix(searchTxt, pageable)); //공지사항 제목+작성자 검색 게시물
                    model.addAttribute("count", boardRepository.searchByTitleAndWriterFix(searchTxt).size()); //공지사항 제목+작성자 검색 게시물 갯수
                } else if (category2 == 6) {
                    model.addAttribute("list", boardRepository.searchByCtntAndWriterFix(searchTxt, pageable)); //공지사항 내용+작성자 검색 게시물
                    model.addAttribute("count", boardRepository.searchByCtntAndWriterFix(searchTxt).size()); //공지사항 내용+작성자 검색 게시물 갯수
                }
            } else {
                if (category2 == 0) {
                    model.addAttribute("list", boardRepository.searchByAllNormal(searchTxt, pageable)); //일반 검색 게시물
                    model.addAttribute("count", boardRepository.searchByAllNormal(searchTxt).size()); //일반 검색 게시물 갯수
                } else if (category2 == 1) {
                    model.addAttribute("list", boardRepository.searchByTitleNormal(searchTxt, pageable)); //일반 제목 검색 게시물
                    model.addAttribute("count", boardRepository.searchByTitleNormal(searchTxt).size()); //일반 제목 검색 게시물 갯수
                } else if (category2 == 2) {
                    model.addAttribute("list", boardRepository.searchByCtntNormal(searchTxt, pageable)); //일반 내용 검색 게시물
                    model.addAttribute("count", boardRepository.searchByCtntNormal(searchTxt).size()); //일반 내용 검색 게시물 갯수
                } else if (category2 == 3) {
                    model.addAttribute("list", boardRepository.searchByWriterNormal(searchTxt, pageable)); //일반 작성자 검색 게시물
                    model.addAttribute("count", boardRepository.searchByWriterNormal(searchTxt).size()); //일반 작성자 검색 게시물 갯수
                } else if (category2 == 4) {
                    model.addAttribute("list", boardRepository.searchByTitleAndCtntNormal(searchTxt, pageable)); //일반 제목+내용 검색 게시물
                    model.addAttribute("count", boardRepository.searchByTitleAndCtntNormal(searchTxt).size()); //일반 제목+내용 검색 게시물 갯수
                } else if (category2 == 5) {
                    model.addAttribute("list", boardRepository.searchByTitleAndWriterNormal(searchTxt, pageable)); //일반 제목+작성자 검색 게시물
                    model.addAttribute("count", boardRepository.searchByTitleAndWriterNormal(searchTxt).size()); //일반 제목+작성자 검색 게시물 갯수
                } else if (category2 == 6) {
                    model.addAttribute("list", boardRepository.searchByCtntAndWriterNormal(searchTxt, pageable)); //일반 내용+작성자 검색 게시물
                    model.addAttribute("count", boardRepository.searchByCtntAndWriterNormal(searchTxt).size()); //일반 내용+작성자 검색 게시물 갯수
                }
            }
        } else {
            if (category1 == 0) {
                if (category2 == 0) {
                    model.addAttribute("list", boardRepository.searchByAll(searchTxt, startDate, endDate, pageable)); //전체 날짜 검색 게시물
                    model.addAttribute("count", boardRepository.searchByAll(searchTxt, startDate, endDate).size()); //전체 날짜 검색 게시물 갯수
                } else if (category2 == 1) {
                    model.addAttribute("list", boardRepository.searchByTitle(searchTxt, startDate, endDate, pageable)); //제목 날짜 검색 게시물
                    model.addAttribute("count", boardRepository.searchByTitle(searchTxt, startDate, endDate).size()); //제목 날짜 검색 게시물 갯수
                } else if (category2 == 2) {
                    model.addAttribute("list", boardRepository.searchByCtnt(searchTxt, startDate, endDate, pageable)); //내용 날짜 검색 게시물
                    model.addAttribute("count", boardRepository.searchByCtnt(searchTxt, startDate, endDate).size()); //내용 날짜 검색 게시물 갯수
                } else if (category2 == 3) {
                    model.addAttribute("list", boardRepository.searchByWriter(searchTxt, startDate, endDate, pageable)); //작성자 날짜 검색 게시물
                    model.addAttribute("count", boardRepository.searchByWriter(searchTxt, startDate, endDate).size()); //작성자 날짜 검색 게시물 갯수
                } else if (category2 == 4) {
                    model.addAttribute("list", boardRepository.searchByTitleAndCtnt(searchTxt, startDate, endDate, pageable)); //제목+내용 날짜 검색 게시물
                    model.addAttribute("count", boardRepository.searchByTitleAndCtnt(searchTxt, startDate, endDate).size()); //제목+내용 날짜 검색 게시물 갯수
                } else if (category2 == 5) {
                    model.addAttribute("list", boardRepository.searchByTitleAndWriter(searchTxt, startDate, endDate, pageable)); //제목+작성자 날짜 검색 게시물
                    model.addAttribute("count", boardRepository.searchByTitleAndWriter(searchTxt, startDate, endDate).size()); //제목+작성자 날짜 검색 게시물 갯수
                } else if (category2 == 6) {
                    model.addAttribute("list", boardRepository.searchByCtntAndWriter(searchTxt, startDate, endDate, pageable)); //내용+작성자 날짜 검색 게시물
                    model.addAttribute("count", boardRepository.searchByCtntAndWriter(searchTxt, startDate, endDate).size()); //내용+작성자 날짜 검색 게시물 갯수
                }
            } else if (category1 == 1) {
                if (category2 == 0) {
                    model.addAttribute("list", boardRepository.searchByAllFix(searchTxt, startDate, endDate, pageable)); //공지사항 날짜 검색 게시물
                    model.addAttribute("count", boardRepository.searchByAllFix(searchTxt, startDate, endDate).size()); //공지사항 날짜 검색 게시물 갯수
                } else if (category2 == 1) {
                    model.addAttribute("list", boardRepository.searchByTitleFix(searchTxt, startDate, endDate, pageable)); //공지사항 제목 날짜 검색 게시물
                    model.addAttribute("count", boardRepository.searchByTitleFix(searchTxt, startDate, endDate).size()); //공지사항 제목 날짜 검색 게시물 갯수
                } else if (category2 == 2) {
                    model.addAttribute("list", boardRepository.searchByCtntFix(searchTxt, startDate, endDate, pageable)); //공지사항 내용 날짜 검색 게시물
                    model.addAttribute("count", boardRepository.searchByCtntFix(searchTxt, startDate, endDate).size()); //공지사항 내용 날짜 검색 게시물 갯수
                } else if (category2 == 3) {
                    model.addAttribute("list", boardRepository.searchByWriterFix(searchTxt, startDate, endDate, pageable)); //공지사항 작성자 날짜 검색 게시물
                    model.addAttribute("count", boardRepository.searchByWriterFix(searchTxt, startDate, endDate).size()); //공지사항 작성자 날짜 검색 게시물 갯수
                } else if (category2 == 4) {
                    model.addAttribute("list", boardRepository.searchByTitleAndCtntFix(searchTxt, startDate, endDate, pageable)); //공지사항 제목+내용 날짜 검색 게시물
                    model.addAttribute("count", boardRepository.searchByTitleAndCtntFix(searchTxt, startDate, endDate).size()); //공지사항 제목+내용 날짜 검색 게시물 갯수
                } else if (category2 == 5) {
                    model.addAttribute("list", boardRepository.searchByTitleAndWriterFix(searchTxt, startDate, endDate, pageable)); //공지사항 제목+작성자 날짜 검색 게시물
                    model.addAttribute("count", boardRepository.searchByTitleAndWriterFix(searchTxt, startDate, endDate).size()); //공지사항 제목+작성자 날짜 검색 게시물 갯수
                } else if (category2 == 6) {
                    model.addAttribute("list", boardRepository.searchByCtntAndWriterFix(searchTxt, startDate, endDate, pageable)); //공지사항 내용+작성자 날짜 검색 게시물
                    model.addAttribute("count", boardRepository.searchByCtntAndWriterFix(searchTxt, startDate, endDate).size()); //공지사항 내용+작성자 날짜 검색 게시물 갯수
                }
            } else {
                if (category2 == 0) {
                    model.addAttribute("list", boardRepository.searchByAllNormal(searchTxt, startDate, endDate, pageable)); //일반 날짜 검색 게시물
                    model.addAttribute("count", boardRepository.searchByAllNormal(searchTxt, startDate, endDate).size()); //일반 날짜 검색 게시물 갯수
                } else if (category2 == 1) {
                    model.addAttribute("list", boardRepository.searchByTitleNormal(searchTxt, startDate, endDate, pageable)); //일반 제목 날짜 검색 게시물
                    model.addAttribute("count", boardRepository.searchByTitleNormal(searchTxt, startDate, endDate).size()); //일반 제목 날짜 검색 게시물 갯수
                } else if (category2 == 2) {
                    model.addAttribute("list", boardRepository.searchByCtntNormal(searchTxt, startDate, endDate, pageable)); //일반 내용 날짜 검색 게시물
                    model.addAttribute("count", boardRepository.searchByCtntNormal(searchTxt, startDate, endDate).size()); //일반 내용 날짜 검색 게시물 갯수
                } else if (category2 == 3) {
                    model.addAttribute("list", boardRepository.searchByWriterNormal(searchTxt, startDate, endDate, pageable)); //일반 작성자 날짜 검색 게시물
                    model.addAttribute("count", boardRepository.searchByWriterNormal(searchTxt, startDate, endDate).size()); //일반 작성자 날짜 검색 게시물 갯수
                } else if (category2 == 4) {
                    model.addAttribute("list", boardRepository.searchByTitleAndCtntNormal(searchTxt, startDate, endDate, pageable)); //일반 제목+내용 날짜 검색 게시물
                    model.addAttribute("count", boardRepository.searchByTitleAndCtntNormal(searchTxt, startDate, endDate).size()); //일반 제목+내용 날짜 검색 게시물 갯수
                } else if (category2 == 5) {
                    model.addAttribute("list", boardRepository.searchByTitleAndWriterNormal(searchTxt, startDate, endDate, pageable)); //일반 제목+작성자 날짜 검색 게시물
                    model.addAttribute("count", boardRepository.searchByTitleAndWriterNormal(searchTxt, startDate, endDate).size()); //일반 제목+작성자 날짜 검색 게시물 갯수
                } else if (category2 == 6) {
                    model.addAttribute("list", boardRepository.searchByCtntAndWriterNormal(searchTxt, startDate, endDate, pageable)); //일반 내용+작성자 날짜 검색 게시물
                    model.addAttribute("count", boardRepository.searchByCtntAndWriterNormal(searchTxt, startDate, endDate).size()); //일반 내용+작성자 날짜 검색 게시물 갯수
                }

            }
        }

        System.out.println("검색 된 것: " + model.getAttribute("count"));

        return "/board/board";
    }

    //글 작성 페이지
    @GetMapping("/write")
    public String write(Model model) {

        //로그인 유저 정보 없을 시 로그인 페이지로
        if (authenticationFacade.getLoginUserPk() < 1) {
            return "redirect:/login";
        }

        //로그인된 유저 이름
        model.addAttribute("loginUser", authenticationFacade.getLoginUser());
        return "/board/write";
    }

    @PostMapping("/write")
    @ResponseBody
    public int writePost(@RequestBody BoardDto dto) {

        UserEntity userEntity = new UserEntity();
        userEntity.changeIuser(dto.getIuser());

        //입력값 검증
        if (StringUtil.isEmpty(dto.getTitle()) || StringUtil.isEmpty(dto.getCtnt()) || StringUtil.isEmpty(dto.getWriter())) {
            return 0;
        }

        BoardEntity entity = BoardEntity.builder()
                .title(dto.getTitle())
                .ctnt(dto.getCtnt())
                .writer(dto.getWriter())
                .rdt(dto.getRdt())
                .fix(dto.getFix())
                .iuser(userEntity)
                .build();

//        entity.setTitle(dto.getTitle());
//        entity.setCtnt(dto.getCtnt());
//        entity.setWriter(dto.getWriter());
//        entity.setRdt(dto.getRdt());
//        entity.setFix(dto.getFix());
//        entity.setIuser(userEntity);

        System.out.println(entity);

        return boardService.write(entity); //글 작성 로직은 service에서 처리
    }

    //글 수정 페이지
    @GetMapping("/mod")
    public String mod(Model model, @RequestParam int iboard) {

        BoardEntity boardEntity = new BoardEntity();
        boardEntity.changeIboard(iboard);

        //로그인 유저 정보 없을 시 로그인 페이지로
        if (authenticationFacade.getLoginUserPk() < 1) {
            return "redirect:/login";
        }

        //로그인 된 유저와 게시물의 유저 정보
        String sUser = authenticationFacade.getLoginUser().getName();
        String bUser = boardRepository.findByIboard(iboard).getWriter();

        model.addAttribute("loginUser", sUser);

        if (sUser.equals(bUser)) { //로그인 된 유저와 게시물의 유저 정보 비교, 같을 때
            model.addAttribute("detail", boardRepository.findByIboard(iboard)); //해당 게시물 정보 불러옴
            if (fileRepository.findAllByIboard(boardEntity).size() > 0) { //해당 게시물에 첨부파일 정보가 있을 경우 불러옴
                System.out.println("첨부파일 : " + fileRepository.findAllByIboard(boardEntity));
                model.addAttribute("file", fileRepository.findAllByIboard(boardEntity));
            }
            return "/board/mod";
        } else {
            return "redirect:/board/list";
        }
    }

    @PostMapping("/mod")
    @ResponseBody
    public int modPost(@RequestBody BoardDto dto) {

        UserEntity userEntity = new UserEntity();
        userEntity.changeIuser(authenticationFacade.getLoginUserPk());

        //입력값 검증
        if (StringUtil.isEmpty(dto.getTitle()) || StringUtil.isEmpty(dto.getCtnt()) || StringUtil.isEmpty(dto.getWriter())) {
            return 0;
        }

        //dto를 통해 ajax로 받아온 데이터를 BoardEntity 객체에 set

        BoardEntity entity = BoardEntity.builder()
                .iboard(dto.getIboard())
                .title(dto.getTitle())
                .ctnt(dto.getCtnt())
                .writer(dto.getWriter())
                .rdt(dto.getRdt())
                .fix(dto.getFix())
                .iuser(userEntity)
                .build();

//        entity.setIboard(dto.getIboard());
//        entity.setTitle(dto.getTitle());
//        entity.setCtnt(dto.getCtnt());
//        entity.setWriter(dto.getWriter());
//        entity.setRdt(dto.getRdt());
//        entity.setFix(dto.getFix());
//        entity.setIuser(userEntity);

        System.out.println(entity);

        return boardService.write(entity); //글 작성 로직은 service에서 처리, jpa에서 id가 같은 튜플 insert할 때 자동으로 update 해줌
    }

    //글 상세 페이지 화면
    @GetMapping("/detail")
    public String detail(@RequestParam int iboard, Model model) {

        BoardEntity boardEntity = new BoardEntity();
        boardEntity.changeIboard(iboard);

        //로그인 유저 정보 없을 시 로그인 페이지로
        if (authenticationFacade.getLoginUserPk() < 1) {
            return "redirect:/login";
        }

        //존재하지 않는 iboard 값을 가진 페이지로 접속 할 경우 글 목록 페이지로
        if (boardRepository.findByIboard(iboard) == null) {
            return "redirect:/board/list";
        }

        model.addAttribute("detail", boardRepository.findByIboard(iboard));

        //로그인 유저 정보
        model.addAttribute("loginUserPk", authenticationFacade.getLoginUserPk());

        //로그인 된 유저와 게시물의 유저 정보
        String sUser = authenticationFacade.getLoginUser().getName();
        String bUser = boardRepository.findByIboard(iboard).getWriter();

        System.out.println("세션 유저: " + sUser);
        System.out.println("게시판 유저: " + bUser);

        //로그인 된 유저와 게시물의 유저가 같을 시
        if (sUser.equals(bUser)) {
            model.addAttribute("user", "same");
            System.out.println("아이디 같음");
        }

        //이전, 다음 게시물
        if (boardService.prev(iboard) != 0) { //이전 게시물이 있으면 게시물 정보 불러옴
            int prevIboard = boardService.prev(iboard);
            model.addAttribute("prevIboard", prevIboard);
        } else { //이전 게시물 없으면 처음 게시물 표시
            model.addAttribute("prevIboard", "first");
        }

        if (boardService.next(iboard) != 0) { //다음 게시물이 있으면 게시물 정보 불러옴
            int nextIboard = boardService.next(iboard);
            model.addAttribute("nextIboard", nextIboard);
        } else {
            model.addAttribute("nextIboard", "last");
        }

        //첨부 파일 있으면
        if (boardService.file(boardEntity) != 0) {
            System.out.println("첨부파일 : " + fileRepository.findAllByIboard(boardEntity));
            model.addAttribute("file", fileRepository.findAllByIboard(boardEntity));
        }

        //댓글 있으면
        if (boardService.reply(boardEntity) != 0) {
            model.addAttribute("replies", replyRepository.findAllByIboardOrderByIreplyDesc(boardEntity));
        }

        return "/board/detail";
    }

    //글 삭제
    @GetMapping("/delete")
    public String delete(@RequestParam int iboard) throws IOException {

        if (boardService.delete(iboard) == 0) {
            return "redirect:/err";
        } else {
            return "redirect:/board/list";
        }
    }

    //파일 첨부
    @PostMapping("/uploadAjaxAction")
    @ResponseBody
    public List<FileDto> uploadAjaxPost(MultipartFile[] uploadFile) {

        if (boardService.uploadAjaxPost(uploadFile) != null) {
            return boardService.uploadAjaxPost(uploadFile);
        } else {
            return null;
        }
    }

    //DB에 파일 데이터 저장
    @PostMapping("/fileSave")
    @ResponseBody
    public int fileSave(@RequestBody FileDto dto) {

        if (boardService.fileSave(dto) == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    //첨부파일 다운로드
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(String fileName) {
        System.out.println("download file : " + fileName);
        Resource resource = new FileSystemResource(fileName);

        System.out.println("resource :" + resource);

        String resourceName = resource.getFilename();

        HttpHeaders headers = new HttpHeaders();

        try {
            headers.add("Content-Disposition", "attachment; filename=" + new String(resourceName.getBytes("UTF-8"),
                    "ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    }

    @PostMapping("/fileDelete")
    @ResponseBody
    public int fileDelete(@RequestBody FileDto dto) {

        if (boardService.fileDelte(dto) == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    @PostMapping("/reply")
    @ResponseBody
    public int reply(@RequestBody ReplyDto dto) {

        BoardEntity boardEntity = new BoardEntity();
        boardEntity.changeIboard(dto.getIboard());

        UserEntity userEntity = new UserEntity();
        userEntity.changeIuser(dto.getIuser());

        ReplyEntity entity = ReplyEntity.builder()
                .iboard(boardEntity)
                .name(authenticationFacade.getLoginUser().getName())
                .ctnt(dto.getCtnt())
                .iuser(userEntity)
                .build();

        if (replyService.replySave(entity) == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    @PostMapping("/reply/delete")
    @ResponseBody
    public int replyDelete(@RequestBody ReplyDto dto) {

        //로그인 유저와 댓글 유저가 같을 시 삭제
        int loginUserPk = authenticationFacade.getLoginUserPk();
        int replyUserPk = replyRepository.findByIreply(dto.getIreply()).getIuser().getIuser();

        if (loginUserPk == replyUserPk) {
            replyRepository.deleteByIreply(dto.getIreply());
            return 1;
        } else {
            return 0;
        }
    }
}

