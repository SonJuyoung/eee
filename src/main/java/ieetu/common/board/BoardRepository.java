package ieetu.common.board;

import ieetu.common.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
    List<BoardEntity> findAllByOrderByIboardDesc(Pageable pageable);
}
