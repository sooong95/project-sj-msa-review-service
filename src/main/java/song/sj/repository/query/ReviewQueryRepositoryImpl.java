package song.sj.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import song.sj.entity.QReview;
import song.sj.entity.Review;

import java.util.List;

import static song.sj.entity.QReview.*;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepositoryImpl implements ReviewQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Review> getShopReviews(Long shopId, Pageable pageable) {

        List<Review> content = jpaQueryFactory
                .selectFrom(review)
                .where(review.shopId.eq(shopId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(review.count())
                .from(review)
                .where(review.shopId.eq(shopId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
