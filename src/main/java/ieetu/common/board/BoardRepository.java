package ieetu.common.board;

import ieetu.common.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
    List<BoardEntity> findAllByOrderByFixDescIboardDesc(); //공지사항 + 일반게시물 등록 날짜 내림차순 정렬

    BoardEntity findFirstByOrderByIboardDesc(); //최신 게시물

    BoardEntity findByIboard(int iboard); //iboard로 게시물 찾기

    @Query("SELECT b FROM BoardEntity as b WHERE b.iboard < :iboard order by b.iboard desc")
    List<BoardEntity> findPrev(int iboard); //이전 게시물 정보

    @Query("SELECT b FROM BoardEntity as b WHERE b.iboard > :iboard order by b.iboard asc")
    List<BoardEntity> findNext(int iboard); //다음 게시물 정보

    void deleteByIboard(int iboard); //iboard로 게시물 삭제

    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 order by b.iboard desc")
    List<BoardEntity> fixList(); //공지사항 게시물 정렬

    @Query(value = "SELECT b FROM BoardEntity as b WHERE b.fix = 0 order by b.iboard desc")
    List<BoardEntity> List(Pageable pageable); //일반 게시물 정렬

    @Query("SELECT b FROM BoardEntity as b WHERE (b.fix = 0 and b.title like concat ('%',:search,'%')) or (b.fix = 0 and b.writer like concat ('%',:search,'%')) or (b.fix = 0 and b.ctnt like concat ('%',:search,'%')) order by b.iboard desc")
    List<BoardEntity> searchByAll(String search, Pageable pageable); //전제 검색 게시물 정렬

    @Query("SELECT b FROM BoardEntity as b WHERE b.title like concat ('%',:search,'%') or b.writer like concat ('%',:search,'%') or b.ctnt like concat ('%',:search,'%') order by b.iboard desc")
    List<BoardEntity> searchByAll(String search); //전제 검색 게시물 정렬 갯수 파악

    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and b.title like concat ('%',:search,'%') order by b.iboard desc")
    List<BoardEntity> searchByTitle(String search, Pageable pageable); //제목 검색 게시물 정렬

    @Query("SELECT b FROM BoardEntity as b WHERE b.title like concat ('%',:search,'%') order by b.iboard desc")
    List<BoardEntity> searchByTitle(String search); //제목 검색 게시물 정렬 갯수 파악

    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and b.ctnt like concat ('%',:search,'%') order by b.iboard desc")
    List<BoardEntity> searchByCtnt(String search, Pageable pageable); //내용 검색 게시물 정렬

    @Query("SELECT b FROM BoardEntity as b WHERE b.ctnt like concat ('%',:search,'%') order by b.iboard desc")
    List<BoardEntity> searchByCtnt(String search); //내용 검색 게시물 정렬 갯수 파악

    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and b.writer like concat ('%',:search,'%') order by b.iboard desc")
    List<BoardEntity> searchByWriter(String search, Pageable pageable); //작성자 검색 게시물 정렬

    @Query("SELECT b FROM BoardEntity as b WHERE b.writer like concat ('%',:search,'%') order by b.iboard desc")
    List<BoardEntity> searchByWriter(String search); //작성자 검색 게시물 정렬 갯수 파악
}
