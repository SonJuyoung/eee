package ieetu.common.board;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ieetu.common.entity.BoardEntity;
import ieetu.common.entity.QBoardEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class BoardCustomRepositoryImpl implements BoardCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardEntity> search(String search, int fix, String ctnt, String writer, String title, String startDate, String endDate, Pageable pageable) {
        return queryFactory.selectFrom(QBoardEntity.boardEntity)
                .where(isFix(fix)
                        ,(
                                searchResult(search)
                        )
                        ,(isDate(startDate, endDate))
                )
                .orderBy(QBoardEntity.boardEntity.iboard.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<BoardEntity> search(String search, int fix, String ctnt, String writer, String title, String startDate, String endDate) {
        return queryFactory.selectFrom(QBoardEntity.boardEntity)
                .where(isFix(fix)
                        ,(
                                searchResult(search)
                        )
                        ,(isDate(startDate, endDate))
                )
                .orderBy(QBoardEntity.boardEntity.iboard.desc())
                .fetch();
    }

    private BooleanExpression isDate(String startDate, String endDate) {
        return (StringUtils.hasText(startDate) && StringUtils.hasText(endDate)) ? QBoardEntity.boardEntity.rdt.between(startDate, endDate) : null;
    }

    private BooleanExpression isFix(int fix) {
        if (fix == 1) {
            return QBoardEntity.boardEntity.fix.eq(1);
        } else if (fix == 0) {
            return QBoardEntity.boardEntity.fix.eq(0);
        } else {
            return null;
        }
    }

    private BooleanExpression searchResult(String search) {
        return QBoardEntity.boardEntity.writer.contains(search)
                .or(QBoardEntity.boardEntity.ctnt.contains(search))
                .or(QBoardEntity.boardEntity.title.contains(search));
    }
}