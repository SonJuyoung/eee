package ieetu.common.file.dao;

import ieetu.common.dao.SqlComAbstractDAO;
import ieetu.common.file.dto.FileDto;
import ieetu.common.file.vo.FileVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FileDAOImpl extends SqlComAbstractDAO implements FileDAO{


    @Override
    public void insertFileList(List<FileVO> fileVOList) {
        insert("FileDAO.insertFileList", fileVOList);
    }

    @Override
    public List<FileDto> selectFileList(FileVO fileVO) {
        return selectList("FileDAO.selectFileList", fileVO);
    }

    @Override
    public FileVO selectFile(Long fileSeq) {
        return selectOne("FileDAO.selectFile", fileSeq);
    }
}
