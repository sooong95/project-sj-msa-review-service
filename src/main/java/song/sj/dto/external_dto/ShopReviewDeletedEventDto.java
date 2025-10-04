package song.sj.dto.external_dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopReviewDeletedEventDto {

    private Long shopId;
    private double grade;
}
