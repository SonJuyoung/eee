package ieetu.common.file.service;

import ieetu.common.file.entity.FileEntity;
import ieetu.common.file.vo.DeleteInfo;
import ieetu.common.file.vo.FileVO;
import ieetu.common.file.vo.UploadInfo;

public interface FileService {
    void uploadFiles(UploadInfo uploadInfo) throws Exception;
    void deleteFiles(DeleteInfo deleteInfo) throws Exception;
    FileEntity getFile(Long fileSeq) throws Exception;

    void uploadFilesWithMyBatis(UploadInfo uploadInfo) throws Exception;
    void deleteFilesWithMyBatis(DeleteInfo deleteInfo) throws Exception;
    FileVO getFileWithMyBatis(Long fileSeq) throws Exception;
}
