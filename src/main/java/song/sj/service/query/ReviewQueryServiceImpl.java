package song.sj.service.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.sj.dto.PageResponseDto;
import song.sj.dto.ReviewResponseDto;
import song.sj.dto.feign_dto.ReviewUsernameDto;
import song.sj.entity.Review;
import song.sj.repository.query.ReviewQueryRepository;
import song.sj.service.feign.MemberServiceFeignClient;

import java.util.Arrays;
import java.util.HashMap;
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
    public PageResponseDto<ReviewResponseDto> getShopReviews(Long shopId, Pageable pageable) {

        Page<Review> shopReviews = getReviews(shopId, pageable);
        List<ReviewUsernameDto> usernames = getReviewUsernameDtos(shopReviews);
        log.info("통신 상태 확인하기 = {}", Arrays.toString(usernames.toArray()));

        Map<Long, String> finalUsernameMap = getLongStringUsernamesMap(usernames);
        Page<ReviewResponseDto> page = shopReviews.map(review ->
                ReviewResponseDto.builder()
                        .reviewId(review.getReviewId())
                        .username(finalUsernameMap.get(review.getMemberId()))
                        .reviewTitle(review.getReviewTitle())
                        .content(review.getContent())
                        .grade(review.getGrade())
                        .build());

        return PageResponseDto.<ReviewResponseDto>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    private static Map<Long, String> getLongStringUsernamesMap(List<ReviewUsernameDto> usernames) {
        Map<Long, String> usernameMap = Map.of();
        try {
            usernameMap = usernames.stream()
                    .filter(dto -> dto.getUsername() != null)
                    .collect(Collectors.toMap(
                            ReviewUsernameDto::getMemberId,
                            ReviewUsernameDto::getUsername,
                            (existing, replacement) -> existing
                    ));
        } catch (Exception e) {
            log.error("usernameMap 생성 중 오류 발생", e);
            usernames.forEach(u -> log.warn("usernameDto: {}", u));
        }

        return usernameMap;
    }

    private List<ReviewUsernameDto> getReviewUsernameDtos(Page<Review> shopReviews) {
        List<Long> memberIds = shopReviews.getContent().stream()
                .map(Review::getMemberId).distinct().collect(Collectors.toList());

        return memberServiceFeignClient.getUsernameList(memberIds).getData();
    }

    private Page<Review> getReviews(Long shopId, Pageable pageable) {
        return reviewQueryRepository.getShopReviews(shopId, pageable);
    }
}
