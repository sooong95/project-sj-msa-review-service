package song.sj.service.async;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import song.sj.dto.Result;
import song.sj.dto.feign_dto.ReviewUsernameDto;
import song.sj.entity.Review;
import song.sj.service.feign.MemberServiceFeignClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewAsyncService {

    private final MemberServiceFeignClient memberServiceFeignClient;

    @Async("asyncExecutor")
    public CompletableFuture<List<ReviewUsernameDto>> getReviewUsernameDtosAsync(List<Review> reviews) {
        List<Long> memberIds = reviews.stream()
                .map(Review::getMemberId)
                .distinct()
                .collect(Collectors.toList());

        return memberServiceFeignClient.getUsernameListAsync(memberIds)
                .thenApply(Result::getData)
                .exceptionally(ex -> {
                    log.error("Feign 비동기 호출 실패", ex);
                    return List.of(); // 실패 시 빈 리스트 반환
                });
    }
}
