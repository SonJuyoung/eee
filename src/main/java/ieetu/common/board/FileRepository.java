package ieetu.common.board;

import ieetu.common.entity.BoardEntity;
import ieetu.common.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Integer> {
    List<FileEntity> findAllByIboard(BoardEntity iboard); //해당 iboard에 있는 첨부파일
    void deleteAllByIboard(BoardEntity iboard); //해당 iboard에 첨부파일 데이터 삭제
    void deleteByFileNmAndIboard(String fileNm, BoardEntity iboard); //해당 iboard와 파일이름이 일치하면 삭제
}
