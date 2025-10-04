package song.sj.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import song.sj.dto.Result;
import song.sj.dto.feign_dto.ReviewUsernameDto;

import java.util.List;

@FeignClient(name = "sj-member-service")
public interface MemberServiceFeignClient {

    @GetMapping("/api/member/usernames")
    Result<List<ReviewUsernameDto>> getUsernameList(@RequestParam("memberIds") List<Long> memberIds);
}
