package song.sj.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponseDto {

    private Long reviewId;
    private Long memberId;
    private String username;
    private String reviewTitle;
    private String content;
    private double grade;
}
