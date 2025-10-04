package song.sj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveReviewDto {

    private Long orderId;
    private String reviewTitle;
    private String content;
    private double grade;
}
