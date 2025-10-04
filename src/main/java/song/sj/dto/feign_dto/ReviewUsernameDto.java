package song.sj.dto.feign_dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReviewUsernameDto {

    private Long memberId;
    private String username;
}
