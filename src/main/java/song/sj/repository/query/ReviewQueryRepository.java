package song.sj.repository.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import song.sj.entity.Review;


public interface ReviewQueryRepository {

    Page<Review> getShopReviews(Long shopId, Pageable pageable);
}
