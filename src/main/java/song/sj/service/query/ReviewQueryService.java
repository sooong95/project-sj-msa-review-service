package song.sj.service.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import song.sj.dto.Result;
import song.sj.dto.ReviewResponseDto;

import java.util.List;

public interface ReviewQueryService {

    Result<Page<ReviewResponseDto>> getShopReview(Long shopId, Pageable pageable);
}
