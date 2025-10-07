package song.sj.service.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import song.sj.dto.PageResponseDto;
import song.sj.dto.ReviewResponseDto;

public interface ReviewQueryService {

    PageResponseDto<ReviewResponseDto> getShopReviews(Long shopId, Pageable pageable);
}
