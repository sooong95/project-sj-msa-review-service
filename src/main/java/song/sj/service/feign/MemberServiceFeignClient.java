package song.sj.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import song.sj.dto.Result;
import song.sj.dto.feign_dto.ReviewUsernameDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@FeignClient(name = "sj-member-service")
public interface MemberServiceFeignClient {

    // Result<List<ReviewUsernameDto>> getUsernameList(@RequestBody List<Long> memberIds); 기존 동기 호출 방식

    // 비동기 호출 방식으로 변경
    @PostMapping("/api/member/usernames")
    CompletableFuture<Result<List<ReviewUsernameDto>>> getUsernameListAsync(@RequestBody List<Long> memberIds);
}
