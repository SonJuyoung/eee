package ieetu.common.file.service;

import ieetu.common.config.etc.EgovWebUtil;
import ieetu.common.file.dao.FileDAO;
import ieetu.common.file.entity.FileEntity;
import ieetu.common.file.repository.FileRepository;
import ieetu.common.file.vo.DeleteInfo;
import ieetu.common.file.vo.FileVO;
import ieetu.common.file.vo.UploadInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;

    private final FileDAO fileDAO;

    @Override
    public void uploadFiles(UploadInfo uploadInfo) throws Exception {
        if (!uploadInfo.isValid()) {
            throw new IllegalArgumentException();
        }
        String filePath = uploadInfo.getAbsoluteFilePath();
        File storedPath = new File(EgovWebUtil.filePathBlackList(filePath));
        if (!storedPath.exists() || storedPath.isFile()) {
            if (storedPath.mkdirs()) {
                log.debug("[file.mkdirs] saveFolder : Creation Success ");
            } else {
                throw new SecurityException("[file.mkdirs] Permission Denied");
            }
        }

        List<FileEntity> fileEntityList = new ArrayList<>();
        for (MultipartFile file : uploadInfo.getFiles()) {
            if (StringUtils.isEmpty(file.getOriginalFilename())) {
                continue;
            }

            // 확장자 제한
            String ext = FilenameUtils.getExtension(file.getOriginalFilename());
            if (!uploadInfo.getAllowFileType().isValid(ext)) {
                throw new IllegalArgumentException("파일 확장자 제한");
            }

            String storeFileNm = UUID.randomUUID().toString();

            file.transferTo(new File(EgovWebUtil.filePathBlackList(filePath + File.separator + storeFileNm)));

            FileEntity fileEntity = FileEntity.builder()
                    .refSeq(uploadInfo.getRefSeq())
                    .refType(uploadInfo.getRefType())
                    .originalFileNm(new String(file.getOriginalFilename().getBytes(StandardCharsets.UTF_8)))
                    .storeFileNm(storeFileNm)
                    .filePath(filePath)
                    .fileSize(file.getSize())
                    .downloadAuth(uploadInfo.getDownloadAuth())
                    .deleteAuth(uploadInfo.getDeleteAuth())
                    .build();
            fileEntityList.add(fileEntity);
        }

        fileRepository.saveAllAndFlush(fileEntityList);
    }

    @Override
    public void deleteFiles(DeleteInfo deleteInfo) throws Exception {
        if (!deleteInfo.isValid()) {
            throw new IllegalArgumentException();
        }

        List<FileEntity> deleteFileList = fileRepository.findBySeqIn(deleteInfo.getDeleteList());
        for (FileEntity file : deleteFileList) {

            try {
                Files.deleteIfExists(Paths.get(file.getAbsolutePath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // 파일을 모두 삭제한 후 DB 처리
        fileRepository.deleteAllById(deleteFileList.stream().map(FileEntity::getSeq).collect(Collectors.toList()));
    }

    @Override
    public FileEntity getFile(Long fileSeq) throws Exception {
        return fileRepository.findById(fileSeq).<IllegalArgumentException>orElseThrow(() -> {
            throw new IllegalArgumentException("file not exist");
        });
    }

    @Override
    public void uploadFilesWithMyBatis(UploadInfo uploadInfo) throws Exception {
        if (!uploadInfo.isValid()) {
            throw new IllegalArgumentException();
        }
        String filePath = uploadInfo.getAbsoluteFilePath();
        File storedPath = new File(EgovWebUtil.filePathBlackList(filePath));
        if (!storedPath.exists() || storedPath.isFile()) {
            if (storedPath.mkdirs()) {
                log.debug("[file.mkdirs] saveFolder : Creation Success ");
            }
        }

        List<FileVO> fileVOList = new ArrayList<>();
        for (MultipartFile file : uploadInfo.getFiles()) {
            if (StringUtils.isEmpty(file.getOriginalFilename())) {
                continue;
            }

            // 확장자 제한
            String ext = FilenameUtils.getExtension(file.getOriginalFilename());
            if (!uploadInfo.getAllowFileType().isValid(ext)) {
                throw new IllegalArgumentException("파일 확장자 제한");
            }

            String storeFileNm = UUID.randomUUID().toString();

            file.transferTo(new File(EgovWebUtil.filePathBlackList(filePath + File.separator + storeFileNm)));

            FileVO fileVO = new FileVO();

            fileVO.setRefSeq(uploadInfo.getRefSeq());
            fileVO.setRefType(uploadInfo.getRefType());
            fileVO.setOriginalFileNm(new String(file.getOriginalFilename().getBytes(StandardCharsets.UTF_8)));
            fileVO.setStoreFileNm(storeFileNm);
            fileVO.setFilePath(filePath);
            fileVO.setFileSize(file.getSize());
            fileVO.setDownloadAuth(uploadInfo.getDownloadAuth());
            fileVO.setDeleteAuth(uploadInfo.getDeleteAuth());
            fileVO.setCreatedBy(uploadInfo.getCreatedBy());
            fileVO.setCreatedDate(LocalDateTime.now());

            fileVOList.add(fileVO);
        }

        fileDAO.insertFileList(fileVOList);
    }

    @Override
    public void deleteFilesWithMyBatis(DeleteInfo deleteInfo) throws Exception {

    }

    @Override
    public FileVO getFileWithMyBatis(Long fileSeq) throws Exception {
        FileVO file = fileDAO.selectFile(fileSeq);
        if(file == null) {
            throw new IllegalArgumentException("File Not Exist");
        }
        return file;
    }
}
