package song.sj.dto.external_dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ShopReviewCreatedEventDto {

    private Long shopId;
    private double grade;
}
