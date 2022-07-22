package ieetu.common.board;

import ieetu.common.board.reply.ReplyRepository;
import ieetu.common.dto.FileDto;
import ieetu.common.entity.BoardEntity;
import ieetu.common.entity.FileEntity;
import ieetu.common.entity.UserEntity;
import ieetu.common.securityConfig.AuthenticationFacade;
import ieetu.common.user.ProfileImgRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final AuthenticationFacade authenticationFacade;
    private final ProfileImgRepository profileImgRepository;
    private final FileRepository fileRepository;
    private final ReplyRepository replyRepository;

    //게시물 불러오기
    public int callList() {

        UserEntity entity = new UserEntity();
        entity.changeIuser(authenticationFacade.getLoginUserPk());

        if (profileImgRepository.findByIuser(entity) != null) {//프로필 이미지가 있을 때
            return 1;
        }
        return 0;
    }

    //게시물 데이터 insert 후 iboard값 리턴
    public int write(BoardEntity entity) {
        try {
            boardRepository.save(entity);
            return boardRepository.findFirstByOrderByIboardDesc().getIboard();
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    }

    public int prev(int iboard) {
        if (boardRepository.findPrev(iboard).size() > 0) {
            return boardRepository.findPrev(iboard).get(0).getIboard();
        } else {
            return 0;
        }
    }

    public int next(int iboard) {
        if (boardRepository.findNext(iboard).size() > 0) {
            return boardRepository.findNext(iboard).get(0).getIboard();
        } else {
            return 0;
        }
    }

    public int file(BoardEntity entity) {
        if (fileRepository.findAllByIboard(entity).size() > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public int reply(BoardEntity entity) {
        if (replyRepository.findAllByIboardOrderByIreplyDesc(entity).size() > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public int delete(int iboard) {
        //로그인 된 유저와 게시물의 유저 정보
        //url로 직접 접속해서 글 삭제 방지
        String sUser = authenticationFacade.getLoginUser().getName();
        String bUser = boardRepository.findByIboard(iboard).getWriter();

        if (sUser.equals(bUser)) {
            boardRepository.deleteByIboard(iboard);

            //DB에서 첨부파일명 가져와서 실제 경로에 파일 삭제

            BoardEntity entity = new BoardEntity();
            entity.changeIboard(iboard);

            List<FileEntity> list = fileRepository.findAllByIboard(entity);

            for (FileEntity fileEntity : list) {
                System.out.println("삭제 파일 : " + fileEntity.getFileNm());
                File file = new File(fileEntity.getFileNm());
                if (file.exists()) {
                    if (file.delete()) {
                        System.out.println("파일삭제 성공");
                    } else {
                        System.out.println("파일삭제 실패");
                        return 0;
                    }
                } else {
                    System.out.println("파일이 존재하지 않습니다.");
                    return 0;
                }
            }
            return 1;
        } else  {
            return 2;
        }
    }

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

        BoardEntity boardEntity = new BoardEntity();
        boardEntity.changeIboard(iboard);

        System.out.println("게시물에 있는 파일 수 : " + fileRepository.findAllByIboard(boardEntity));
        //파일 첨부가 있는 수정일 때, DB에 기존 데이터 삭제 후 새로운 파일 첨부
        if (fileRepository.findAllByIboard(boardEntity).size() > 0) {
            fileRepository.deleteAllByIboard(boardEntity);
        }

        if (list.size() > 0) {
            return list;
        } else {
            return null;
        }
    }

    //오늘 날짜 + iboard의 경로를 문자열로 생성한다.
    private String getFolder(int iboard) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();

        String str = sdf.format(date) + "-" + iboard;

        return str.replace("-", File.separator);
    }

    public int fileSave(FileDto dto) {
        String uploadFolder = "C:\\upload";

        System.out.println("저장할 파일 이름 : " + dto.getFileNm());
        String fileNm = dto.getFileNm().substring(0, dto.getFileNm().lastIndexOf("."));
        String ext = dto.getFileNm().substring(dto.getFileNm().lastIndexOf("."));
        System.out.println("파일 이름 : " + fileNm);
        System.out.println("확장자 : " + ext);

        BoardEntity boardEntity = new BoardEntity();
        boardEntity.changeIboard(dto.getIboard());

        FileEntity entity = FileEntity.builder()
                .iboard(boardEntity)
                .fileNm(uploadFolder + "\\" + dto.getUploadPath() + "\\" + dto.getUuid() + "_" + fileNm + ext)
                .build();

        System.out.println("파일 데이터 : " + entity);

        try {
            fileRepository.save(entity);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int fileDelte(FileDto dto) {

        //첨부파일 삭제 시 경로에 있는 실제 파일 삭제
        File file = new File(dto.getFileNm());

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("파일 삭제 성공");
            } else {
                System.out.println("파일 삭제 실패");
                return 0;
            }
        } else {
            System.out.println("파일이 존재하지 않습니다다");
            return 0;
        }

        //DB에 file 데이터 삭제
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.changeIboard(dto.getIboard());

        System.out.println("삭제 파일 이름 : " + dto.getFileNm());
        try {
            fileRepository.deleteByFileNmAndIboard(dto.getFileNm(), boardEntity);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
