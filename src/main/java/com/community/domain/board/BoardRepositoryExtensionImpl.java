package com.community.domain.board;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class BoardRepositoryExtensionImpl extends QuerydslRepositorySupport implements BoardRepositoryExtension {
    public BoardRepositoryExtensionImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> findByKeywordAndType(String type, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;

        JPQLQuery<Board> query = from(board).where(board.isReported.isFalse()
                        .and(board.boardTitle.contains(type))
                        .and(board.title.containsIgnoreCase(keyword)
                                .or(board.content.containsIgnoreCase(keyword))))
                .distinct();

        JPQLQuery<Board> boardJPQLQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Board> boardQueryResults = boardJPQLQuery.fetchResults();

        return new PageImpl<>(boardQueryResults.getResults(), pageable, boardQueryResults.getTotal());
    }
}
