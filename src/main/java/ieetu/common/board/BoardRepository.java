package ieetu.common.board;

import ieetu.common.entity.BoardEntity;
import ieetu.common.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer>, BoardCustomRepository {
    List<BoardEntity> findAllByOrderByFixDescIboardDesc(); //공지사항 + 일반게시물 등록 날짜 내림차순 정렬

    List<BoardEntity> findAllByOrderByIboardDesc(Pageable pageable);

    BoardEntity findFirstByOrderByIboardDesc(); //최신 게시물

    BoardEntity findByIboard(int iboard); //iboard로 게시물 찾기

//    @Query("SELECT b FROM BoardEntity as b WHERE b.iboard < :iboard order by b.iboard desc")
//    List<BoardEntity> findPrev(int iboard); //이전 게시물 정보
//
//    @Query("SELECT b FROM BoardEntity as b WHERE b.iboard > :iboard order by b.iboard asc")
//    List<BoardEntity> findNext(int iboard); //다음 게시물 정보

    void deleteByIboard(int iboard); //iboard로 게시물 삭제

//    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 order by b.iboard desc")
//    List<BoardEntity> fixList(); //공지사항 게시물 정렬

//    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 order by b.iboard desc")
//    List<BoardEntity> List(Pageable pageable); //일반 게시물 정렬

    List<BoardEntity> findAllByIuserOrderByRdtDesc(UserEntity iuser);
    List<BoardEntity> findAllByIuserOrderByRdtDesc(UserEntity iuser, Pageable pageable);

//    @Modifying
//    @Query("update BoardEntity b set b.view = b.view + 1 where b.iboard = :iboard")
//    void viewUp(int iboard);
}
