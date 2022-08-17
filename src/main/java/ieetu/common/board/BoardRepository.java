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
    void deleteByIboard(int iboard); //iboard로 게시물 삭제
    List<BoardEntity> findAllByIuserOrderByRdtDesc(UserEntity iuser);
    List<BoardEntity> findAllByIuserOrderByRdtDesc(UserEntity iuser, Pageable pageable);

}
