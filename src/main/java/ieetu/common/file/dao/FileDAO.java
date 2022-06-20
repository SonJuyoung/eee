package ieetu.common.file.dao;

import ieetu.common.file.dto.FileDto;
import ieetu.common.file.vo.FileVO;

import java.util.List;

public interface FileDAO {
    void insertFileList(List<FileVO> fileVOList);

    List<FileDto> selectFileList(FileVO fileVO);

    FileVO selectFile(Long fileSeq);
}
