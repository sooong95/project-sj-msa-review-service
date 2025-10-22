package song.sj.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import song.sj.dto.Result;
import song.sj.dto.feign_dto.ReviewUsernameDto;

import java.util.List;

@FeignClient(name = "sj-member-service")
public interface MemberServiceFeignClient {

    @PostMapping("/api/member/usernames")
    Result<List<ReviewUsernameDto>> getUsernameList(@RequestBody List<Long> memberIds);
}
