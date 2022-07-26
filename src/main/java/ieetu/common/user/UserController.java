package ieetu.common.user;

import ieetu.common.board.BoardRepository;
import ieetu.common.dto.ProfileImgDto;
import ieetu.common.dto.UserDto;
import ieetu.common.entity.ProfileImgEntity;
import ieetu.common.entity.UserEntity;
import ieetu.common.securityConfig.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;
    private final ProfileImgService profileImgService;
    private final ProfileImgRepository profileImgRepository;
    private final BoardRepository boardRepository;

    //기본 인덱스 주소로 접속 시 로그인 페이지로
    @GetMapping
    public String index() {
        return "redirect:/login";
    }

    //로그인 페이지
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false)String error,
                        @RequestParam(value = "exception", required = false)String exception,
                        Model model) {

        //로그인 한 유저인 경우 게시판으로 이동
        if (authenticationFacade.getLoginUserPk() > 0) {
            return "redirect:/board/list";
        }

        System.out.println("에러 : " + error);
        System.out.println("예외 : " + exception);
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "user/login";
    }

    //회원가입 페이지
    @GetMapping("/join")
    public String join() {
        return "user/join";
    }

    //회원가입
    @PostMapping("/join")
    @ResponseBody // http요청 body를 자바 객체로 전달받을 수 있다.
    public int joinPost(@RequestBody UserDto dto) { // http요청의 본문(body)이 그대로 전달된다.

        if (userService.join(dto) == 0) {
            return 0;
        } else return 1;
    }

    //아이디 중복 검사
    @PostMapping("/join/idchk")
    @ResponseBody
    public int idchk(@RequestBody String id) {

        return userService.idchk(id);
    }

    //아이디 찾기
    @PostMapping("/findId")
    @ResponseBody
    public UserEntity findId(@RequestBody UserDto dto) {

        if (userRepository.findByNameAndPhoneAndMail(dto.getName(), dto.getPhone(), dto.getMail()) != null) { //이름, 휴대폰번호, 메일으로 찾았을 때 아이디가 있는 경우
            System.out.println("아이디 : " + userRepository.findByNameAndPhoneAndMail(dto.getName(), dto.getPhone(), dto.getMail()));
            System.out.println("아이디 : " + userRepository.findByNameAndPhoneAndMail(dto.getName(), dto.getPhone(), dto.getMail()).getUid());
            return userRepository.findByNameAndPhoneAndMail(dto.getName(), dto.getPhone(), dto.getMail()); //아이디 리턴
        } else { //아이디가 없는 경우
            System.out.println("아이디 없음 : " + userRepository.findByNameAndPhoneAndMail(dto.getName(), dto.getPhone(), dto.getMail()));
            return null;
        }
    }

    //비밀번호 찾기
    @PostMapping("/findPw")
    @ResponseBody
    public int findPw(@RequestBody UserDto dto) {

        if (userRepository.findByUidAndNameAndPhoneAndMail(dto.getId(), dto.getName(), dto.getPhone(), dto.getMail()) != null) { //아이디, 이름, 휴대폰번호, 메일으로 찾았을 때 일치하는 정보가 있는 경우
            System.out.println("아이디 : " + userRepository.findByUidAndNameAndPhoneAndMail(dto.getId(), dto.getName(), dto.getPhone(), dto.getMail()));
            return 1;
        } else { //일치하는 정보가 없는 경우
            System.out.println("아이디 없음 : " + userRepository.findByUidAndNameAndPhoneAndMail(dto.getId(), dto.getName(), dto.getPhone(), dto.getMail()));
            return 0;
        }
    }

    //비밀번호 변경
    @PostMapping("/changePw")
    @ResponseBody
    public int changePw(@RequestBody UserDto dto) {

        return userService.pwChange(dto);

    }

    //마이페이지
    @GetMapping("/mypage")
    public String myPage(Model model) throws MalformedURLException {

        UserEntity entity = new UserEntity();
        entity.changeIuser(authenticationFacade.getLoginUserPk());

        model.addAttribute("loginUser", authenticationFacade.getLoginUser());

        if (profileImgRepository.findByIuser(entity)!=null) {
            System.out.println("파일 : " + profileImgRepository.findByIuser(entity).getFileNm());
            model.addAttribute("profileImg", profileImgRepository.findByIuser(entity).getFileNm());
        }
        return "user/myPage";
    }

    //프로필 사진 db저장
    @PostMapping("/profileImg")
    @ResponseBody
    public int profileImg(@RequestBody ProfileImgDto dto) {

        UserEntity userEntity = new UserEntity();
        userEntity.changeIuser(dto.getIuser());

        ProfileImgEntity entity = ProfileImgEntity.builder()
                .iuser(userEntity)
                .fileNm(dto.getFileNm())
                .build();

//        entity.setIuser(userEntity);
//        entity.setFileNm(dto.getFileNm());

        //프로필 사진이 있으면 삭제
        if (profileImgRepository.findByIuser(userEntity) != null) {
            profileImgService.profileImgDel(userEntity);
        }
        return profileImgService.profileImgSave(entity);
    }

    //프로필 사진 업로드
    @PostMapping("/profileImgFile")
    @ResponseBody
    public String profileImgFile(MultipartFile uploadFile) {

        String profileImgFile = null;
        String uploadFolder = "C:\\Users\\이튜\\Desktop\\egovstudy\\egovstudy\\src\\main\\webapp\\images\\profileImg";

        int iuser = authenticationFacade.getLoginUserPk();

        String uploadFolderPath = uploadFolder + iuser;
        // make folder ----------
        File uploadPath = new File(uploadFolder, String.valueOf(iuser));

        //기존 iuser에 해당하는 폴더가 있으면 삭제하고 다시 만듦, 파일 계속 축적되는 것 방지
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

        //경로에 파일 생성
        try {
            File saveFile = new File(uploadPath, uploadFile.getOriginalFilename());
            uploadFile.transferTo(saveFile);

            profileImgFile = uploadFolder + "\\" + iuser +"\\"+ uploadFile.getOriginalFilename();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("파일 명 : " + profileImgFile);
        return profileImgFile;
    }

    @GetMapping("/myinfo")
    public String myInfo(Model model) {

        model.addAttribute("loginUser", authenticationFacade.getLoginUser());

        return "/user/myInfo";
    }

    @GetMapping("/myarticle")
    public String myArticle(Model model, Pageable pageable) {

        UserEntity entity = new UserEntity();
        entity.changeIuser(authenticationFacade.getLoginUserPk());

        if (profileImgRepository.findByIuser(entity)!=null) {
            System.out.println("파일 : " + profileImgRepository.findByIuser(entity).getFileNm());
            model.addAttribute("profileImg", profileImgRepository.findByIuser(entity).getFileNm());
        }

        model.addAttribute("articleList", boardRepository.findAllByIuserOrderByRdtDesc(entity, pageable)); //게시물
        model.addAttribute("count", boardRepository.findAllByIuserOrderByRdtDesc(entity).size()); //게시물 갯수
        model.addAttribute("user", authenticationFacade.getLoginUser()); //로그인 유저 정보

        return "/user/myArticle";
    }
}