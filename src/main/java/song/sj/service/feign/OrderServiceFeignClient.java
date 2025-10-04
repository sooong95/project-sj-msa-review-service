package song.sj.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import song.sj.dto.feign_dto.ReviewAuthorizationVerificationDto;

@FeignClient(name = "sj-ordering-service")
public interface OrderServiceFeignClient {

    @GetMapping("/external/reviewAuthorizationVerification")
    ReviewAuthorizationVerificationDto reviewAuthorizationVerification(
            @RequestParam("shopId") Long shopId, @RequestParam("orderId") Long orderId);

}
