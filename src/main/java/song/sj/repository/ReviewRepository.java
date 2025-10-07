package song.sj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import song.sj.entity.Review;
import song.sj.repository.query.ReviewQueryRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {


}
