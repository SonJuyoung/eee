package ieetu.common.board;

import ieetu.common.dto.BoardDto;
import ieetu.common.dto.FileDto;
import ieetu.common.entity.BoardEntity;
import ieetu.common.entity.FileEntity;
import ieetu.common.file.FileRepository;
import ieetu.common.securityConfig.AuthenticationFacade;
import ieetu.common.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;


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

    @GetMapping("/list")
    public String board(Model model, @PageableDefault Pageable pageable, HttpSession hs) {
//
        if (authenticationFacade.getLoginUserPk() < 1) {
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
    public String write(Model model) {

        if (authenticationFacade.getLoginUserPk() < 1) {
            return "redirect:/login";
        }

        model.addAttribute("loginUser", authenticationFacade.getLoginUser().getName());
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
    public String mod(Model model, @RequestParam int iboard) {

        String sUser = authenticationFacade.getLoginUser().getName();
        String bUser = boardRepository.findByIboard(iboard).getWriter();

        model.addAttribute("loginUser", sUser);

        if (authenticationFacade.getLoginUserPk() < 1) {
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
    public String detail(@RequestParam int iboard, Model model) {

        if (authenticationFacade.getLoginUserPk() < 1) {
            return "redirect:/login";
        }

        if (boardRepository.findByIboard(iboard) == null) {
            return "redirect:/board/list";
        }

        model.addAttribute("detail", boardRepository.findByIboard(iboard));

        String sUser = authenticationFacade.getLoginUser().getName();
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

        //첨부 파일 있으면
        if (fileRepository.findAllByIboard(iboard).size() > 0) {
            System.out.println("첨부파일 : " + fileRepository.findAllByIboard(iboard));
            model.addAttribute("file", fileRepository.findAllByIboard(iboard));
        }

        return "/board/detail";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam int iboard) {

        String sUser = authenticationFacade.getLoginUser().getName();
        String bUser = boardRepository.findByIboard(iboard).getWriter();

        if (sUser.equals(bUser)) {
            boardRepository.deleteByIboard(iboard);
        }
        return "redirect:/board/list";
    }

    @PostMapping("/uploadAjaxAction")
    @ResponseBody
    public List<FileDto> uploadAjaxPost(MultipartFile[] uploadFile) {
        System.out.println("update ajax post.................");

        List<FileDto> list = new ArrayList<>();
        String uploadFolder = "C:\\upload";

        String uploadFolderPath = getFolder();
        // make folder ----------
        File uploadPath = new File(uploadFolder, getFolder());
        System.out.println("upload path : " + uploadPath);

        if (uploadPath.exists() == false) {
            uploadPath.mkdirs();
        }

        for (MultipartFile multipartFile : uploadFile) {

            FileDto fileDto = new FileDto();

            String uploadFileName = multipartFile.getOriginalFilename();

            //IE has file path
            uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
            System.out.println("only file name : " + uploadFileName);
            fileDto.setFileNm(uploadFileName);

            UUID uuid = UUID.randomUUID();

            uploadFileName = uuid.toString() + "_" + uploadFileName;

            //File saveFile = new File(uploadFolder, uploadFileName);
            try {
                File saveFile = new File(uploadPath, uploadFileName);
                multipartFile.transferTo(saveFile);

                fileDto.setUuid(uuid.toString());
                fileDto.setUploadPath(uploadFolderPath);

                list.add(fileDto);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return list;
    }

    // 오늘 날짜의 경로를 문자열로 생성한다.
    private String getFolder() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();

        String str = sdf.format(date);

        return str.replace("-", File.separator);
    }

    @PostMapping("/fileSave")
    @ResponseBody
    public int fileSave(@RequestBody FileDto dto) {

        String uploadFolder = "C:\\upload";

        FileEntity entity = new FileEntity();

        String fileNm = dto.getFileNm().substring(0, dto.getFileNm().lastIndexOf("."));
        String ext = dto.getFileNm().substring(dto.getFileNm().lastIndexOf("."));
        System.out.println("파일 이름 : " + fileNm);
        System.out.println("확장자 : " + ext);

        entity.setIboard(dto.getIboard());
        entity.setFileNm(uploadFolder + "\\" + dto.getUploadPath() + "\\" + dto.getUuid() + "_" + fileNm + ext);

        System.out.println("파일 데이터 : " + entity);

        fileRepository.save(entity);

        return 1;
    }

    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(String fileName){
        System.out.println("download file : " + fileName);
        Resource resource = new FileSystemResource(fileName);

        System.out.println("resource :" + resource);

        String resourceName = resource.getFilename();

        HttpHeaders headers = new HttpHeaders();

        try {
            headers.add("Content-Disposition", "attachment; filename=" + new String(resourceName.getBytes("UTF-8"),
                    "ISO-8859-1"));
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    }
}
