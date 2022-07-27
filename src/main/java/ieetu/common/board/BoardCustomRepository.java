package ieetu.common.board;

import com.querydsl.jpa.impl.JPAUpdateClause;
import ieetu.common.entity.BoardEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface BoardCustomRepository {

    List<BoardEntity> search(String search, int fix, String ctnt, String writer, String title, String startDate, String endDate, Pageable pageable);

    List<BoardEntity> search(String search, int fix, String ctnt, String writer, String title, String startDate, String endDate);

    List<BoardEntity> fixList(int fix); //공지사항 게시물 정렬

    List<BoardEntity> List(int fix, Pageable pageable); //일반 게시물 정렬

    List<BoardEntity> findPrevOrNext(int prevNext, int iboard); //이전 or 다음 게시물 정보

    @Modifying
    void viewUp(int iboard, int view);

}
