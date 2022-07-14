package ieetu.common.board.reply;

import ieetu.common.entity.ReplyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;

    public int replySave(ReplyEntity entity) {

        System.out.println("댓글 엔티티 : " + entity);
        try {
            replyRepository.save(entity);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
