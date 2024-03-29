package ieetu.common.board;

import ieetu.common.entity.BoardEntity;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService extends EgovAbstractServiceImpl {

    @Autowired
    private BoardRepository boardRepository;

    //게시물 데이터 insert 후 iboard값 리턴
    public int write(BoardEntity entity) {
        try {
            boardRepository.save(entity);
            return boardRepository.findFirstByOrderByIboardDesc().getIboard();
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    }

    public void viewUp(int iboard, int view) {
        boardRepository.viewUp(iboard, view);
    }
}
