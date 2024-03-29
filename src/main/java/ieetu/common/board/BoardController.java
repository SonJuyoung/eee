package ieetu.common.board;

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
import ieetu.common.user.ProfileImgService;
import org.apache.commons.io.FileUtils;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class BoardController {

    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private ProfileImgService profileImgService;

    @GetMapping("/list")
    public String board(Model model, Pageable pageable) {

        //로그인 유저 정보 없을 시 로그인 페이지로
        if (authenticationFacade.getLoginUserPk() < 1) {
            return "redirect:/login";
        }

        UserEntity entity = new UserEntity(authenticationFacade.getLoginUserPk());

        if (profileImgService.profileImgCall(entity) != null) {
            model.addAttribute("profileImg", profileImgService.profileImgCall(entity));
        }

        //공지사항 || 일반 게시물
        int noticeBoard = 1;
        int normalBoard = 0;

        model.addAttribute("list", boardRepository.findAllByOrderByIboardDesc(pageable));

        model.addAttribute("fixList", boardRepository.fixList(noticeBoard)); //공지사항 게시물
        model.addAttribute("fixListCount", boardRepository.fixList(noticeBoard).size()); //공지사항 게시물 갯수
        model.addAttribute("normalList", boardRepository.List(normalBoard, pageable)); //일반 게시물
        model.addAttribute("count", boardRepository.findAllByOrderByFixDescIboardDesc().size()); //일반 게시물 갯수
        model.addAttribute("user", authenticationFacade.getLoginUser()); //로그인 유저 정보

        return "/board/board";
    }

    //게시판 검색 시 페이지
    @GetMapping("/list/search")
    public String boardSearch(Model model, @RequestParam int category1, @RequestParam int category2, @RequestParam String searchTxt, @Nullable String startDate, @Nullable String endDate, Pageable pageable) {

        //로그인 유저 정보 없을 시 로그인 페이지로
        if (authenticationFacade.getLoginUserPk() < 1) {
            return "redirect:/login";
        }

        UserEntity entity = new UserEntity(authenticationFacade.getLoginUserPk());

        if (profileImgService.profileImgCall(entity) != null) {
            model.addAttribute("profileImg", profileImgService.profileImgCall(entity));
        }

        //공지사항 || 일반 게시물
        int noticeBoard = 1;
        int normalBoard = 0;

        model.addAttribute("user", authenticationFacade.getLoginUser()); //로그인 유저 정보
        model.addAttribute("list", boardRepository.findAllByOrderByIboardDesc(pageable));
        System.out.println("리스트 : " + boardRepository.findAll(pageable));
        model.addAttribute("fixList", boardRepository.fixList(noticeBoard)); //공지사항 게시물

        int fix = -1;
        String title = null;
        String ctnt = null;
        String writer = null;

        if (category1 == 1) {
            fix = 1;
        } else if (category1 == 2){
            fix = 0;
        }

        model.addAttribute("list", boardRepository.search(searchTxt, fix, ctnt, writer, title, startDate, endDate, pageable)); //검색 게시물
        model.addAttribute("count", boardRepository.search(searchTxt, fix, ctnt, writer, title, startDate, endDate).size()); //검색 게시물 갯수


        System.out.println("검색 된 것: " + model.getAttribute("list"));

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

        UserEntity userEntity = new UserEntity(authenticationFacade.getLoginUserPk());

        //dto를 통해 ajax로 받아온 데이터를 BoardEntity 객체에 set

        BoardEntity entity = BoardEntity.builder()
                .title(dto.getTitle())
                .ctnt(dto.getCtnt())
                .writer(dto.getWriter())
                .rdt(dto.getRdt())
                .fix(dto.getFix())
                .iuser(userEntity)
                .build();

        System.out.println(entity);

        return boardService.write(entity); //글 작성 로직은 service에서 처리
    }

    //글 수정 페이지
    @GetMapping("/mod")
    public String mod(Model model, @RequestParam int iboard) {

        BoardEntity boardEntity = new BoardEntity(iboard);

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

        UserEntity userEntity = new UserEntity(authenticationFacade.getLoginUserPk());

        //dto를 통해 ajax로 받아온 데이터를 BoardEntity 객체에 set

        BoardEntity entity = BoardEntity.builder()
                .iboard(dto.getIboard())
                .title(dto.getTitle())
                .ctnt(dto.getCtnt())
                .writer(dto.getWriter())
                .rdt(dto.getRdt())
                .fix(dto.getFix())
                .iuser(userEntity)
                .view(boardRepository.findByIboard(dto.getIboard()).getView())
                .build();

        System.out.println(entity);

        return boardService.write(entity); //글 작성 로직은 service에서 처리, jpa에서 id가 같은 튜플 insert할 때 자동으로 update 해줌
    }

    //글 상세 페이지 화면
    @GetMapping("/detail")
    public String detail(@RequestParam int iboard, Model model, HttpServletResponse response, HttpServletRequest request) {

        BoardEntity boardEntity = new BoardEntity(iboard);

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

        //쿠키 활용 조회수 증가
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + iboard + "]")) {
                boardService.viewUp(iboard, boardRepository.findByIboard(iboard).getView());
                oldCookie.setValue(oldCookie.getValue() + "_[" + iboard + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(oldCookie);
            }
        } else {
            boardService.viewUp(iboard, boardRepository.findByIboard(iboard).getView());
            Cookie newCookie = new Cookie("postView", "[" + iboard + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);
        }

        //이전, 다음 게시물

        int prev = 0;
        int next = 1;

        if (boardRepository.findPrevOrNext(prev, iboard).size() > 0) { //이전 게시물이 있으면 게시물 정보 불러옴
            int prevIboard = boardRepository.findPrevOrNext(prev, iboard).get(0).getIboard();
            System.out.println("이전 게시물 :" + prevIboard);
            model.addAttribute("prevIboard", prevIboard);
        } else { //이전 게시물 없으면 처음 게시물 표시
            model.addAttribute("prevIboard", "first");
        }

        if (boardRepository.findPrevOrNext(next, iboard).size() > 0) { //다음 게시물이 있으면 게시물 정보 불러옴
            int nextIboard = boardRepository.findPrevOrNext(next, iboard).get(0).getIboard();
            System.out.println("다음 게시물 :" + nextIboard);
            model.addAttribute("nextIboard", nextIboard);
        } else {
            model.addAttribute("nextIboard", "last");
        }

        //첨부 파일 있으면
        if (fileRepository.findAllByIboard(boardEntity).size() > 0) {
            System.out.println("첨부파일 : " + fileRepository.findAllByIboard(boardEntity));
            model.addAttribute("file", fileRepository.findAllByIboard(boardEntity));
        }

        System.out.println("리플 : " + replyRepository.findAllByIboardOrderByIreplyDesc(boardEntity));
        //댓글 있으면
        if (replyRepository.findAllByIboardOrderByIreplyDesc(boardEntity).size() > 0) {
            model.addAttribute("replies", replyRepository.findAllByIboardOrderByIreplyDesc(boardEntity));
        }

        return "/board/detail";
    }

    //글 삭제
    @GetMapping("/delete")
    public String delete(@RequestParam int iboard) throws IOException {

        //로그인 된 유저와 게시물의 유저 정보
        //url로 직접 접속해서 글 삭제 방지
        String sUser = authenticationFacade.getLoginUser().getName();
        String bUser = boardRepository.findByIboard(iboard).getWriter();

        if (sUser.equals(bUser)) {
            boardRepository.deleteByIboard(iboard);

            //DB에서 첨부파일명 가져와서 실제 경로에 파일 삭제

            BoardEntity entity = new BoardEntity(iboard);

            List<FileEntity> list = fileRepository.findAllByIboard(entity);

            for (FileEntity fileEntity : list) {
                System.out.println("삭제 파일 : " + fileEntity.getFileNm());
                File file = new File(fileEntity.getFileNm());
                if (file.exists()) {
                    if (file.delete()) {
                        System.out.println("파일삭제 성공");
                    } else {
                        System.out.println("파일삭제 실패");
                    }
                } else {
                    System.out.println("파일이 존재하지 않습니다.");
                }
            }
        }
        return "redirect:/board/list";
    }

    //파일 첨부
    @PostMapping("/uploadAjaxAction")
    @ResponseBody
    public List<FileDto> uploadAjaxPost(MultipartFile[] uploadFile) {

        System.out.println("update ajax post.................");

        List<FileDto> list = new ArrayList<>();
        String uploadFolder = "C:\\upload"; //업로드 폴더 경로 설정

        int iboard = 0;

        for (int i = 0; i < uploadFile.length; i++) {
            System.out.println("파일 이름 : " + uploadFile[i].getName());
            //각 파일의 iboard값 설정
            iboard = Integer.parseInt(uploadFile[i].getOriginalFilename().split("_")[1]);

            //등록일때 파일 업로드 경로, 다음 iboard 값
            //게시글 등록 후 파일이 생성되므로 최신 iboard값이 등록할 때 해당하는 iboard값이다
            if (iboard == 0) {
                iboard = boardRepository.findFirstByOrderByIboardDesc().getIboard();
            }

            System.out.println("파일 iboard : " + iboard);
            String uploadFolderPath = getFolder(iboard);
            // make folder ----------
            File uploadPath = new File(uploadFolder, getFolder(iboard));
            System.out.println("upload path : " + uploadPath);

            //기존 iboard에 해당하는 폴더가 있으면 삭제하고 다시 만듦, 파일 계속 축적되는 것 방지
            if (i == 0) {
                if (!uploadPath.exists()) {
                    uploadPath.mkdirs();
                } else {
                    String path = String.valueOf(uploadPath);
                    System.out.println("path : " + path);

                    try {
                        File dir = new File(path);
                        FileUtils.deleteDirectory(dir);

                        uploadPath.mkdirs();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            //파일이름 중복일 경우 대비해서 파일이름 랜덤으로 저장
            FileDto fileDto = new FileDto();

            String uploadFileName = uploadFile[i].getOriginalFilename();

            uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
            uploadFileName = uploadFileName.substring(0, uploadFileName.lastIndexOf("_"));
            System.out.println("only file name : " + uploadFileName);
            fileDto.setFileNm(uploadFileName);

            //랜덤 이름 생성
            UUID uuid = UUID.randomUUID();

            uploadFileName = uuid.toString() + "_" + uploadFileName;

            //File saveFile = new File(uploadFolder, uploadFileName);
            //경로에 파일 생성
            try {
                File saveFile = new File(uploadPath, uploadFileName);
                uploadFile[i].transferTo(saveFile);

                fileDto.setUuid(uuid.toString());
                fileDto.setUploadPath(uploadFolderPath);

                list.add(fileDto);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        BoardEntity boardEntity = new BoardEntity(iboard);

        System.out.println("게시물에 있는 파일 수 : " + fileRepository.findAllByIboard(boardEntity));
        //파일 첨부가 있는 수정일 때, DB에 기존 데이터 삭제 후 새로운 파일 첨부
        if (fileRepository.findAllByIboard(boardEntity).size() > 0) {
            fileRepository.deleteAllByIboard(boardEntity);
        }

        return list;
    }

    //오늘 날짜 + iboard의 경로를 문자열로 생성한다.
    private String getFolder(int iboard) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();

        String str = sdf.format(date) + "-" + iboard;

        return str.replace("-", File.separator);
    }

    //DB에 파일 데이터 저장
    @PostMapping("/fileSave")
    @ResponseBody
    public int fileSave(@RequestBody FileDto dto) {

        String uploadFolder = "C:\\upload";

        System.out.println("저장할 파일 이름 : " + dto.getFileNm());
        String fileNm = dto.getFileNm().substring(0, dto.getFileNm().lastIndexOf("."));
        String ext = dto.getFileNm().substring(dto.getFileNm().lastIndexOf("."));
        System.out.println("파일 이름 : " + fileNm);
        System.out.println("확장자 : " + ext);

        BoardEntity boardEntity = new BoardEntity(dto.getIboard());

        FileEntity entity = new FileEntity(boardEntity, uploadFolder + "\\" + dto.getUploadPath() + "\\" + dto.getUuid() + "_" + fileNm + ext);
//        entity.setIboard(boardEntity);
//        entity.setFileNm(uploadFolder + "\\" + dto.getUploadPath() + "\\" + dto.getUuid() + "_" + fileNm + ext);

        System.out.println("파일 데이터 : " + entity);

        fileRepository.save(entity);

        return 1;
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

        //첨부파일 삭제 시 경로에 있는 실제 파일 삭제
        File file = new File(dto.getFileNm());

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("파일 삭제 성공");
            } else {
                System.out.println("파일 삭제 실패");
            }
        } else {
            System.out.println("파일이 존재하지 않습니다다");
        }

        //DB에 file 데이터 삭제
        BoardEntity boardEntity = new BoardEntity(dto.getIboard());

        System.out.println("삭제 파일 이름 : " + dto.getFileNm());
        try {
            fileRepository.deleteByFileNmAndIboard(dto.getFileNm(), boardEntity);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @PostMapping("/reply")
    @ResponseBody
    public int reply(@RequestBody ReplyDto dto) {

        BoardEntity boardEntity = new BoardEntity(dto.getIboard());

        UserEntity userEntity = new UserEntity(authenticationFacade.getLoginUserPk());

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

