package song.sj.service.toEntity;

import song.sj.dto.SaveReviewDto;
import song.sj.entity.Review;

public class ToReviews {

    public static Review toReviewsEntity(SaveReviewDto dto) {

        return new Review(dto.getReviewTitle(), dto.getContent(), dto.getGrade());
    }
}
