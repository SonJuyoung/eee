package ieetu.common.board;

import ieetu.common.entity.BoardEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    public int write(BoardEntity entity) {
        try {
            boardRepository.save(entity);
            return 1;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    }
}
