package ieetu.common.board.reply;

import ieetu.common.entity.BoardEntity;
import ieetu.common.entity.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<ReplyEntity, Integer> {

    List<ReplyEntity> findAllByIboardOrderByIreplyDesc(BoardEntity iboard);

    void deleteByIreply(int ireply);

    ReplyEntity findByIreply(int ireply);
}
