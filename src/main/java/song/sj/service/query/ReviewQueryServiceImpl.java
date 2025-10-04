package song.sj.service.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.sj.dto.Result;
import song.sj.dto.ReviewResponseDto;
import song.sj.dto.feign_dto.ReviewUsernameDto;
import song.sj.entity.Review;
import song.sj.repository.query.ReviewQueryRepository;
import song.sj.service.feign.MemberServiceFeignClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReviewQueryServiceImpl implements ReviewQueryService {

    private final ReviewQueryRepository reviewQueryRepository;
    private final MemberServiceFeignClient memberServiceFeignClient;
    
    @Override
    public Result<Page<ReviewResponseDto>> getShopReview(Long shopId, Pageable pageable) {

        Page<Review> shopReviews = reviewQueryRepository.getShopReviews(shopId, pageable);
        List<Long> memberIds = shopReviews.getContent().stream().map(Review::getMemberId).collect(Collectors.toList());

        List<ReviewUsernameDto> usernames = memberServiceFeignClient.getUsernameList(memberIds).getData();

        Map<Long, String> usernameMap = usernames.stream().collect(Collectors.toMap(
                ReviewUsernameDto::getMemberId,
                ReviewUsernameDto::getUsername
        ));

        Page<ReviewResponseDto> resDtos = shopReviews.map(review ->
            ReviewResponseDto.builder()
                .reviewId(review.getId())
                    .username(usernameMap.get(review.getMemberId()))
                .reviewTitle(review.getReviewTitle())
                .content(review.getContent())
                .grade(review.getGrade())
                .build());


        return new Result<>(resDtos.getSize(), resDtos);
    }
}
