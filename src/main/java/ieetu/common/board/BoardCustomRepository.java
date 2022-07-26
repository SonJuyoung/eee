package ieetu.common.board;

import ieetu.common.entity.BoardEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardCustomRepository {

    List<BoardEntity> search(String search, int fix, String ctnt, String writer, String title, String startDate, String endDate, Pageable pageable);

    List<BoardEntity> search(String search, int fix, String ctnt, String writer, String title, String startDate, String endDate);

}
