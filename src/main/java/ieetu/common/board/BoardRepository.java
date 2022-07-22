package ieetu.common.board;

import ieetu.common.entity.BoardEntity;
import ieetu.common.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
    List<BoardEntity> findAllByOrderByFixDescIboardDesc(); //공지사항 + 일반게시물 등록 날짜 내림차순 정렬

    List<BoardEntity> findAllByOrderByIboardDesc(Pageable pageable);

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

    @Query("SELECT b FROM BoardEntity as b WHERE (b.title like concat ('%',:search,'%')) or (b.writer like concat ('%',:search,'%')) or (b.ctnt like concat ('%',:search,'%')) order by b.iboard desc")
    List<BoardEntity> searchByAll(String search, Pageable pageable); //전체 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE ((b.title like concat ('%',:search,'%')) or (b.writer like concat ('%',:search,'%')) or (b.ctnt like concat ('%',:search,'%'))) and b.rdt between :startDate and :endDate order by b.iboard desc")
    List<BoardEntity> searchByAll(String search, String startDate, String endDate, Pageable pageable); //전체 날짜 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE ((b.title like concat ('%',:search,'%')) or (b.writer like concat ('%',:search,'%')) or (b.ctnt like concat ('%',:search,'%'))) and b.rdt between :startDate and :endDate order by b.iboard desc")
    List<BoardEntity> searchByAll(String search, String startDate, String endDate); //전체 날짜 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.title like concat ('%',:search,'%') or b.writer like concat ('%',:search,'%') or b.ctnt like concat ('%',:search,'%') order by b.iboard desc")
    List<BoardEntity> searchByAll(String search); //전체 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (b.title like concat ('%',:search,'%') or b.writer like concat ('%',:search,'%') or b.ctnt like concat ('%',:search,'%')) order by b.iboard desc")
    List<BoardEntity> searchByAllFix(String search, Pageable pageable); //공지사항 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (b.title like concat ('%',:search,'%') or b.writer like concat ('%',:search,'%') or b.ctnt like concat ('%',:search,'%')) and b.rdt between :startDate and :endDate order by b.iboard desc")
    List<BoardEntity> searchByAllFix(String search, String startDate, String endDate, Pageable pageable); //공지사항 검색 날짜 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (b.title like concat ('%',:search,'%') or b.writer like concat ('%',:search,'%') or b.ctnt like concat ('%',:search,'%')) order by b.iboard desc")
    List<BoardEntity> searchByAllFix(String search); //공지사항 검색 게시물 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (b.title like concat ('%',:search,'%') or b.writer like concat ('%',:search,'%') or b.ctnt like concat ('%',:search,'%')) and b.rdt between :startDate and :endDate order by b.iboard desc")
    List<BoardEntity> searchByAllFix(String search, String startDate, String endDate); //공지사항 검색 날짜 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (b.title like concat ('%',:search,'%') or b.writer like concat ('%',:search,'%') or b.ctnt like concat ('%',:search,'%')) order by b.iboard desc")
    List<BoardEntity> searchByAllNormal(String search, Pageable pageable); //일반 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (b.title like concat ('%',:search,'%') or b.writer like concat ('%',:search,'%') or b.ctnt like concat ('%',:search,'%')) and b.rdt between :startDate and :endDate order by b.iboard desc")
    List<BoardEntity> searchByAllNormal(String search, String startDate, String endDate, Pageable pageable); //일반 검색 날짜 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (b.title like concat ('%',:search,'%') or b.writer like concat ('%',:search,'%') or b.ctnt like concat ('%',:search,'%')) order by b.iboard desc")
    List<BoardEntity> searchByAllNormal(String search); //일반 검색 게시물 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (b.title like concat ('%',:search,'%') or b.writer like concat ('%',:search,'%') or b.ctnt like concat ('%',:search,'%')) and b.rdt between :startDate and :endDate order by b.iboard desc")
    List<BoardEntity> searchByAllNormal(String search, String startDate, String endDate); //일반 검색 날짜 게시물 정렬 갯수 파악

    @Query("SELECT b FROM BoardEntity as b WHERE b.title like concat ('%',:search,'%') order by b.iboard desc")
    List<BoardEntity> searchByTitle(String search, Pageable pageable); //제목 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE (b.title like concat ('%',:search,'%')) and b.rdt between :startDate and :endDate order by b.iboard desc")
    List<BoardEntity> searchByTitle(String search, String startDate, String endDate, Pageable pageable); //제목 날짜 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE (b.title like concat ('%',:search,'%')) and b.rdt between :startDate and :endDate order by b.iboard desc")
    List<BoardEntity> searchByTitle(String search, String startDate, String endDate); //제목 날짜 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.title like concat ('%',:search,'%') order by b.iboard desc")
    List<BoardEntity> searchByTitle(String search); //제목 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (b.title like concat ('%',:search,'%')) order by b.iboard desc")
    List<BoardEntity> searchByTitleFix(String search, Pageable pageable); //공지사항 제목 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (b.title like concat ('%',:search,'%') and b.rdt between :startDate and :endDate) order by b.iboard desc")
    List<BoardEntity> searchByTitleFix(String search, String startDate, String endDate, Pageable pageable); //공지사항 제목 검색 날짜 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (b.title like concat ('%',:search,'%') and b.rdt between :startDate and :endDate) order by b.iboard desc")
    List<BoardEntity> searchByTitleFix(String search, String startDate, String endDate); //공지사항 제목 검색 날짜 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (b.title like concat ('%',:search,'%')) order by b.iboard desc")
    List<BoardEntity> searchByTitleFix(String search); //공지사항 제목 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (b.title like concat ('%',:search,'%')) order by b.iboard desc")
    List<BoardEntity> searchByTitleNormal(String search, Pageable pageable); //일반 제목 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (b.title like concat ('%',:search,'%') and b.rdt between :startDate and :endDate) order by b.iboard desc")
    List<BoardEntity> searchByTitleNormal(String search, String startDate, String endDate, Pageable pageable); //일반 제목 검색 날짜 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (b.title like concat ('%',:search,'%') and b.rdt between :startDate and :endDate) order by b.iboard desc")
    List<BoardEntity> searchByTitleNormal(String search, String startDate, String endDate); //일반 제목 검색 날짜 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (b.title like concat ('%',:search,'%')) order by b.iboard desc")
    List<BoardEntity> searchByTitleNormal(String search); //일반 제목 검색 게시물 정렬 갯수 파악

    @Query("SELECT b FROM BoardEntity as b WHERE b.ctnt like concat ('%',:search,'%') order by b.iboard desc")
    List<BoardEntity> searchByCtnt(String search, Pageable pageable); //내용 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE (b.ctnt like concat ('%',:search,'%') and b.rdt between :startDate and :endDate) order by b.iboard desc")
    List<BoardEntity> searchByCtnt(String search, String startDate, String endDate, Pageable pageable); //내용 날짜 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE (b.ctnt like concat ('%',:search,'%') and b.rdt between :startDate and :endDate) order by b.iboard desc")
    List<BoardEntity> searchByCtnt(String search, String startDate, String endDate); //내용 날짜 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.ctnt like concat ('%',:search,'%') order by b.iboard desc")
    List<BoardEntity> searchByCtnt(String search); //내용 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (b.ctnt like concat ('%',:search,'%')) order by b.iboard desc")
    List<BoardEntity> searchByCtntFix(String search, Pageable pageable); //공지사항 내용 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (b.ctnt like concat ('%',:search,'%') and b.rdt between :startDate and :endDate ) order by b.iboard desc")
    List<BoardEntity> searchByCtntFix(String search, String startDate, String endDate, Pageable pageable); //공지사항 내용 날짜 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (b.ctnt like concat ('%',:search,'%') and b.rdt between :startDate and :endDate ) order by b.iboard desc")
    List<BoardEntity> searchByCtntFix(String search, String startDate, String endDate); //공지사항 내용 날짜 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (b.ctnt like concat ('%',:search,'%')) order by b.iboard desc")
    List<BoardEntity> searchByCtntFix(String search); //공지사항 내용 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (b.ctnt like concat ('%',:search,'%')) order by b.iboard desc")
    List<BoardEntity> searchByCtntNormal(String search, Pageable pageable); //일반 내용 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (b.ctnt like concat ('%',:search,'%') and b.rdt between :startDate and :endDate ) order by b.iboard desc")
    List<BoardEntity> searchByCtntNormal(String search, String startDate, String endDate, Pageable pageable); //일반 내용 날짜 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (b.ctnt like concat ('%',:search,'%') and b.rdt between :startDate and :endDate ) order by b.iboard desc")
    List<BoardEntity> searchByCtntNormal(String search, String startDate, String endDate); //일반 내용 날짜 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (b.ctnt like concat ('%',:search,'%')) order by b.iboard desc")
    List<BoardEntity> searchByCtntNormal(String search); //일반 내용 검색 게시물 정렬 갯수 파악

    @Query("SELECT b FROM BoardEntity as b WHERE b.writer like concat ('%',:search,'%') order by b.iboard desc")
    List<BoardEntity> searchByWriter(String search, Pageable pageable); //작성자 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE (b.writer like concat ('%',:search,'%')) and b.rdt between :startDate and :endDate order by b.iboard desc")
    List<BoardEntity> searchByWriter(String search, String startDate, String endDate, Pageable pageable); //작성자 날짜 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE (b.writer like concat ('%',:search,'%')) and b.rdt between :startDate and :endDate order by b.iboard desc")
    List<BoardEntity> searchByWriter(String search, String startDate, String endDate); //작성자 날짜 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.writer like concat ('%',:search,'%') order by b.iboard desc")
    List<BoardEntity> searchByWriter(String search); //작성자 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and b.writer like concat ('%',:search,'%') order by b.iboard desc")
    List<BoardEntity> searchByWriterFix(String search, Pageable pageable); //공지사항 작성자 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and ((b.writer like concat ('%',:search,'%')) and b.rdt between :startDate and :endDate) order by b.iboard desc")
    List<BoardEntity> searchByWriterFix(String search, String startDate, String endDate, Pageable pageable); //공지사항 작성자 날짜 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and ((b.writer like concat ('%',:search,'%')) and b.rdt between :startDate and :endDate) order by b.iboard desc")
    List<BoardEntity> searchByWriterFix(String search, String startDate, String endDate); //공지사항 작성자 날짜 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (b.writer like concat ('%',:search,'%')) order by b.iboard desc")
    List<BoardEntity> searchByWriterFix(String search); //공지사항 작성자 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and b.writer like concat ('%',:search,'%') order by b.iboard desc")
    List<BoardEntity> searchByWriterNormal(String search, Pageable pageable); //일반 작성자 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and ((b.writer like concat ('%',:search,'%')) and b.rdt between :startDate and :endDate) order by b.iboard desc")
    List<BoardEntity> searchByWriterNormal(String search, String startDate, String endDate, Pageable pageable); //일반 작성자 날짜 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and ((b.writer like concat ('%',:search,'%')) and b.rdt between :startDate and :endDate) order by b.iboard desc")
    List<BoardEntity> searchByWriterNormal(String search, String startDate, String endDate); //일반 작성자 날짜 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (b.writer like concat ('%',:search,'%')) order by b.iboard desc")
    List<BoardEntity> searchByWriterNormal(String search); //일반 작성자 검색 게시물 정렬 갯수 파악

    @Query("SELECT b FROM BoardEntity as b WHERE b.title like concat ('%',:search,'%') or (b.ctnt like concat ('%',:search,'%')) order by b.iboard desc")
    List<BoardEntity> searchByTitleAndCtnt(String search, Pageable pageable); //제목+내용 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE (b.title like concat ('%',:search,'%') or (b.ctnt like concat ('%',:search,'%'))) and b.rdt between :startDate and :endDate order by b.iboard desc")
    List<BoardEntity> searchByTitleAndCtnt(String search, String startDate, String endDate, Pageable pageable); //제목+내용 날짜 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE (b.title like concat ('%',:search,'%') or (b.ctnt like concat ('%',:search,'%'))) and b.rdt between :startDate and :endDate order by b.iboard desc")
    List<BoardEntity> searchByTitleAndCtnt(String search, String startDate, String endDate); //제목+내용 날짜 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.title like concat ('%',:search,'%') or (b.ctnt like concat ('%',:search,'%')) order by b.iboard desc")
    List<BoardEntity> searchByTitleAndCtnt(String search); //제목+내용 검색 게시물 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (b.title like concat ('%',:search,'%') or (b.ctnt like concat ('%',:search,'%'))) order by b.iboard desc")
    List<BoardEntity> searchByTitleAndCtntFix(String search, Pageable pageable); //공지사항 제목+내용 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (b.title like concat ('%',:search,'%') or (b.ctnt like concat ('%',:search,'%')) and b.rdt between :startDate and :endDate) order by b.iboard desc")
    List<BoardEntity> searchByTitleAndCtntFix(String search, String startDate, String endDate, Pageable pageable); //공지사항 제목+내용 날짜 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (b.title like concat ('%',:search,'%') or (b.ctnt like concat ('%',:search,'%')) and b.rdt between :startDate and :endDate) order by b.iboard desc")
    List<BoardEntity> searchByTitleAndCtntFix(String search, String startDate, String endDate); //공지사항 제목+내용 날짜 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (b.title like concat ('%',:search,'%') or (b.ctnt like concat ('%',:search,'%'))) order by b.iboard desc")
    List<BoardEntity> searchByTitleAndCtntFix(String search); //공지사항 제목+내용 검색 게시물 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (b.title like concat ('%',:search,'%') or (b.ctnt like concat ('%',:search,'%'))) order by b.iboard desc")
    List<BoardEntity> searchByTitleAndCtntNormal(String search, Pageable pageable); //일반 제목+내용 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (b.title like concat ('%',:search,'%') or (b.ctnt like concat ('%',:search,'%')) and b.rdt between :startDate and :endDate) order by b.iboard desc")
    List<BoardEntity> searchByTitleAndCtntNormal(String search, String startDate, String endDate, Pageable pageable); //일반 제목+내용 날짜 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (b.title like concat ('%',:search,'%') or (b.ctnt like concat ('%',:search,'%')) and b.rdt between :startDate and :endDate) order by b.iboard desc")
    List<BoardEntity> searchByTitleAndCtntNormal(String search, String startDate, String endDate); //일반 제목+내용 날짜 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (b.title like concat ('%',:search,'%') or (b.ctnt like concat ('%',:search,'%'))) order by b.iboard desc")
    List<BoardEntity> searchByTitleAndCtntNormal(String search); //일반 제목+내용 검색 게시물 갯수 파악

    @Query("SELECT b FROM BoardEntity as b WHERE b.title like concat ('%',:search,'%') or (b.writer like concat ('%',:search,'%')) order by b.iboard desc")
    List<BoardEntity> searchByTitleAndWriter(String search, Pageable pageable); //제목+작성자 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE (b.title like concat ('%',:search,'%') or (b.writer like concat ('%',:search,'%'))) and b.rdt between :startDate and :endDate order by b.iboard desc")
    List<BoardEntity> searchByTitleAndWriter(String search, String startDate, String endDate, Pageable pageable); //제목+작성자 날짜 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE (b.title like concat ('%',:search,'%') or (b.writer like concat ('%',:search,'%'))) and b.rdt between :startDate and :endDate order by b.iboard desc")
    List<BoardEntity> searchByTitleAndWriter(String search, String startDate, String endDate); //제목+작성자 날짜 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.title like concat ('%',:search,'%') or (b.writer like concat ('%',:search,'%')) order by b.iboard desc")
    List<BoardEntity> searchByTitleAndWriter(String search); //제목+작성자 검색 게시물 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (b.title like concat ('%',:search,'%') or (b.writer like concat ('%',:search,'%'))) order by b.iboard desc")
    List<BoardEntity> searchByTitleAndWriterFix(String search, Pageable pageable); //공지사항 제목+작성자 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (b.title like concat ('%',:search,'%') or (b.writer like concat ('%',:search,'%')) and b.rdt between :startDate and :endDate) order by b.iboard desc")
    List<BoardEntity> searchByTitleAndWriterFix(String search, String startDate, String endDate, Pageable pageable); //공지사항 제목+작성자 날짜 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (b.title like concat ('%',:search,'%') or (b.writer like concat ('%',:search,'%')) and b.rdt between :startDate and :endDate) order by b.iboard desc")
    List<BoardEntity> searchByTitleAndWriterFix(String search, String startDate, String endDate); //공지사항 제목+작성자 날짜 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (b.title like concat ('%',:search,'%') or (b.writer like concat ('%',:search,'%'))) order by b.iboard desc")
    List<BoardEntity> searchByTitleAndWriterFix(String search); //공지사항 제목+작성자 검색 게시물 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (b.title like concat ('%',:search,'%') or (b.writer like concat ('%',:search,'%'))) order by b.iboard desc")
    List<BoardEntity> searchByTitleAndWriterNormal(String search, Pageable pageable); //일반 제목+작성자 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (b.title like concat ('%',:search,'%') or (b.writer like concat ('%',:search,'%')) and b.rdt between :startDate and :endDate) order by b.iboard desc")
    List<BoardEntity> searchByTitleAndWriterNormal(String search, String startDate, String endDate, Pageable pageable); //일반 제목+작성자 날짜 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (b.title like concat ('%',:search,'%') or (b.writer like concat ('%',:search,'%')) and b.rdt between :startDate and :endDate) order by b.iboard desc")
    List<BoardEntity> searchByTitleAndWriterNormal(String search, String startDate, String endDate); //일반 제목+작성자 날짜 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (b.title like concat ('%',:search,'%') or (b.writer like concat ('%',:search,'%'))) order by b.iboard desc")
    List<BoardEntity> searchByTitleAndWriterNormal(String search); //일반 제목+작성자 검색 게시물 갯수 파악

    @Query("SELECT b FROM BoardEntity as b WHERE (b.ctnt like concat ('%',:search,'%')) or (b.writer like concat ('%',:search,'%')) order by b.iboard desc")
    List<BoardEntity> searchByCtntAndWriter(String search, Pageable pageable); //내용+작성자 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE ((b.ctnt like concat ('%',:search,'%')) or (b.writer like concat ('%',:search,'%'))) and b.rdt between :startDate and :endDate order by b.iboard desc")
    List<BoardEntity> searchByCtntAndWriter(String search, String startDate, String endDate, Pageable pageable); //내용+작성자 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE ((b.ctnt like concat ('%',:search,'%')) or (b.writer like concat ('%',:search,'%'))) and b.rdt between :startDate and :endDate order by b.iboard desc")
    List<BoardEntity> searchByCtntAndWriter(String search, String startDate, String endDate); //내용+작성자 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE (b.ctnt like concat ('%',:search,'%')) or (b.writer like concat ('%',:search,'%')) order by b.iboard desc")
    List<BoardEntity> searchByCtntAndWriter(String search); //내용+작성자 검색 게시물 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (b.ctnt like concat ('%',:search,'%') or (b.writer like concat ('%',:search,'%'))) order by b.iboard desc")
    List<BoardEntity> searchByCtntAndWriterFix(String search, Pageable pageable); //공지사항 내용+작성자 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (((b.ctnt like concat ('%',:search,'%')) or (b.writer like concat ('%',:search,'%'))) and b.rdt between :startDate and :endDate) order by b.iboard desc")
    List<BoardEntity> searchByCtntAndWriterFix(String search, String startDate, String endDate, Pageable pageable); //공지사항 내용+작성자 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (((b.ctnt like concat ('%',:search,'%')) or (b.writer like concat ('%',:search,'%'))) and b.rdt between :startDate and :endDate) order by b.iboard desc")
    List<BoardEntity> searchByCtntAndWriterFix(String search, String startDate, String endDate); //공지사항 내용+작성자 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 and (b.ctnt like concat ('%',:search,'%') or (b.writer like concat ('%',:search,'%'))) order by b.iboard desc")
    List<BoardEntity> searchByCtntAndWriterFix(String search); //공지사항 내용+작성자 검색 게시물 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (b.ctnt like concat ('%',:search,'%') or (b.writer like concat ('%',:search,'%'))) order by b.iboard desc")
    List<BoardEntity> searchByCtntAndWriterNormal(String search, Pageable pageable); //일반 내용+작성자 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (((b.ctnt like concat ('%',:search,'%')) or (b.writer like concat ('%',:search,'%'))) and b.rdt between :startDate and :endDate) order by b.iboard desc")
    List<BoardEntity> searchByCtntAndWriterNormal(String search, String startDate, String endDate, Pageable pageable); //일반 내용+작성자 검색 게시물 정렬
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (((b.ctnt like concat ('%',:search,'%')) or (b.writer like concat ('%',:search,'%'))) and b.rdt between :startDate and :endDate) order by b.iboard desc")
    List<BoardEntity> searchByCtntAndWriterNormal(String search, String startDate, String endDate); //일반 내용+작성자 검색 게시물 정렬 갯수 파악
    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and (b.ctnt like concat ('%',:search,'%') or (b.writer like concat ('%',:search,'%'))) order by b.iboard desc")
    List<BoardEntity> searchByCtntAndWriterNormal(String search); //일반 내용+작성자 검색 게시물 갯수 파악

    List<BoardEntity> findAllByIuserOrderByRdtDesc(UserEntity iuser);
    List<BoardEntity> findAllByIuserOrderByRdtDesc(UserEntity iuser, Pageable pageable);
}
