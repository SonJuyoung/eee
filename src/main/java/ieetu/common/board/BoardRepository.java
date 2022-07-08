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
    List<BoardEntity> findAllByOrderByFixDescIboardDesc();

    BoardEntity findFirstByOrderByIboardDesc();

    BoardEntity findByIboard(int iboard);

    @Query("SELECT b FROM BoardEntity as b WHERE b.iboard < :iboard order by b.iboard desc")
    List<BoardEntity> findPrev(int iboard);

    @Query("SELECT b FROM BoardEntity as b WHERE b.iboard > :iboard order by b.iboard asc")
    List<BoardEntity> findNext(int iboard);

    void deleteByIboard(int iboard);

    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 1 order by b.iboard desc")
    List<BoardEntity> fixList();

    @Query(value = "SELECT b FROM BoardEntity as b WHERE b.fix = 0 order by b.iboard desc")
    List<BoardEntity> List(Pageable pageable);

    @Query("SELECT b FROM BoardEntity as b WHERE (b.fix = 0 and b.title like concat ('%',:search,'%')) or (b.fix = 0 and b.writer like concat ('%',:search,'%')) or (b.fix = 0 and b.ctnt like concat ('%',:search,'%')) order by b.iboard desc")
    List<BoardEntity> searchByAll(String search, Pageable pageable);

    @Query("SELECT b FROM BoardEntity as b WHERE b.title like concat ('%',:search,'%') or b.writer like concat ('%',:search,'%') or b.ctnt like concat ('%',:search,'%') order by b.iboard desc")
    List<BoardEntity> searchByAll(String search);

    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and b.title like concat ('%',:search,'%') order by b.iboard desc")
    List<BoardEntity> searchByTitle(String search);

    @Query("SELECT b FROM BoardEntity as b WHERE b.title like concat ('%',:search,'%') order by b.iboard desc")
    List<BoardEntity> searchByTitle(String search, Pageable pageable);

    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and b.ctnt like concat ('%',:search,'%') order by b.iboard desc")
    List<BoardEntity> searchByCtnt(String search);

    @Query("SELECT b FROM BoardEntity as b WHERE b.ctnt like concat ('%',:search,'%') order by b.iboard desc")
    List<BoardEntity> searchByCtnt(String search, Pageable pageable);

    @Query("SELECT b FROM BoardEntity as b WHERE b.fix = 0 and b.writer like concat ('%',:search,'%') order by b.iboard desc")
    List<BoardEntity> searchByWriter(String search, Pageable pageable);

    @Query("SELECT b FROM BoardEntity as b WHERE b.writer like concat ('%',:search,'%') order by b.iboard desc")
    List<BoardEntity> searchByWriter(String search);
}
