package ieetu.common.board;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import ieetu.common.entity.BoardEntity;
import ieetu.common.entity.QBoardEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

import static ieetu.common.entity.QBoardEntity.*;

@RequiredArgsConstructor
public class BoardCustomRepositoryImpl implements BoardCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardEntity> search(String search, int fix, String ctnt, String writer, String title, String startDate, String endDate, Pageable pageable) {
        return queryFactory.selectFrom(boardEntity)
                .where(isFix(fix)
                        ,searchResult(search)
                        ,(isDate(startDate, endDate))
                )
                .orderBy(boardEntity.iboard.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<BoardEntity> search(String search, int fix, String ctnt, String writer, String title, String startDate, String endDate) {
        return queryFactory.selectFrom(boardEntity)
                .where(isFix(fix)
                        ,searchResult(search)
                        ,(isDate(startDate, endDate))
                )
                .orderBy(boardEntity.iboard.desc())
                .fetch();
    }

    @Override
    public List<BoardEntity> fixList(int fix) {
        return queryFactory.selectFrom(boardEntity)
                .where(isFix(fix))
                .orderBy(boardEntity.rdt.desc())
                .fetch();
    }

    @Override
    public List<BoardEntity> List(int fix, Pageable pageable) {
        return queryFactory.selectFrom(boardEntity)
                .where(isFix(fix))
                .orderBy(boardEntity.rdt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<BoardEntity> findPrevOrNext(int prevNext, int iboard) {
        return queryFactory.selectFrom(boardEntity)
                .where(prevOrNext(prevNext, iboard))
                .orderBy(descOrAsc(prevNext))
                .fetch();
    }

    @Override
    public void viewUp(int iboard, int view) {
        queryFactory.update(boardEntity)
                .set(boardEntity.view, view + 1)
                .where(boardEntity.iboard.eq(iboard))
                .execute();
    }

    private BooleanExpression isDate(String startDate, String endDate) {
        return (StringUtils.hasText(startDate) && StringUtils.hasText(endDate)) ? boardEntity.rdt.between(startDate, endDate) : null;
    }

    private BooleanExpression isFix(int fix) {
        if (fix == 1) {
            return boardEntity.fix.eq(1);
        } else if (fix == 0) {
            return boardEntity.fix.eq(0);
        } else {
            return null;
        }
    }

    private BooleanExpression searchResult(String search) {
        return boardEntity.writer.contains(search)
                .or(boardEntity.ctnt.contains(search))
                .or(boardEntity.title.contains(search));
    }

    private BooleanExpression prevOrNext(int prevNext, int iboard) {
        if (prevNext == 0) {
            return boardEntity.iboard.lt(iboard);
        } else if (prevNext == 1){
            return boardEntity.iboard.gt(iboard);
        } else {
            return null;
        }
    }

    private OrderSpecifier<Integer> descOrAsc(int prevNext) {
        if (prevNext == 0) {
            return boardEntity.iboard.desc();
        } else {
            return boardEntity.iboard.asc();
        }
    }
}