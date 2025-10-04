package song.sj.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviews_id")
    private Long id;

    private String reviewTitle;
    private String content;
    private double grade = 0;

    private Long memberId;

    @OneToMany(mappedBy = "review")
    private List<ReviewImages> reviewImagesList = new ArrayList<>();

    private Long shopId;

    public Review(String reviewTitle, String content, double grade) {
        this.reviewTitle = reviewTitle;
        this.content = content;
        this.grade = grade;
    }

    public void addReview(Long memberId, Long shopId) {
        this.memberId = memberId;
        this.shopId = shopId;
    }

    public void addReviewImages(ReviewImages images) {
        if (Objects.nonNull(images)) {
            this.reviewImagesList.add(images);
            images.controllerReviewImages(this);
        }
    }

    public void deleteReviewImages(ReviewImages images) {
        if (Objects.nonNull(images)) {
            this.reviewImagesList.remove(images);
            images.controllerReviewImages(null);
        }
    }

    public void changeReviewTitle(String reviewTitle) {
        if (StringUtils.hasText(reviewTitle)) this.reviewTitle = reviewTitle;
    }

    public void changeContent(String content) {
        if (StringUtils.hasText(content)) this.content = content;
    }

    public void changeGrade(double grade) {
        if (grade > 0) this.grade = grade;
    }
}
