package song.sj.dto.feign_dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewAuthorizationVerificationDto {

    private String message;
}
