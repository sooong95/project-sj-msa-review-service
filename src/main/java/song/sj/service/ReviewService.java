package song.sj.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import song.sj.dto.SaveReviewDto;
import song.sj.dto.external_dto.ShopReviewCreatedEventDto;
import song.sj.dto.external_dto.ShopReviewDeletedEventDto;
import song.sj.entity.Review;
import song.sj.entity.ReviewImages;
import song.sj.repository.ReviewImageRepository;
import song.sj.repository.ReviewRepository;
import song.sj.service.feign.OrderServiceFeignClient;
import song.sj.service.image.ImageFile;
import song.sj.service.toEntity.ToReviews;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ImageFile imageFile;
    private final OrderServiceFeignClient orderServiceFeignClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void saveReview(Long memberId, Long shopId, SaveReviewDto dto, List<MultipartFile> files) throws AccessDeniedException {

        reviewAuthorizationVerification(shopId, dto.getOrderId());

        Review reviewEntity = ToReviews.toReviewsEntity(dto);
        Review review = reviewRepository.save(reviewEntity);

        kafkaTemplate.send("shop-review-topics", shopId.toString(), ShopReviewCreatedEventDto.builder()
                        .shopId(shopId)
                        .grade(dto.getGrade())
                .build());

        addReviewImages(files, review);
        review.addReview(memberId, shopId);

    }

    private void reviewAuthorizationVerification(Long shopId, Long orderId) throws AccessDeniedException {
        /*for (OrderShop orderShop : order.getOrderShopList()) {
            if (!orderShop.getShop().getId().equals(shopId)) {
                throw new AccessDeniedException("리뷰 권한이 없습니다.");
            }
        }*/

        if (orderServiceFeignClient.reviewAuthorizationVerification(shopId, orderId).getMessage().equals("null")) {
            throw new AccessDeniedException("리뷰 권한이 없습니다.");
        }
    }

    private void addReviewImages(List<MultipartFile> files, Review review) {

        try {
            for (MultipartFile file : files) {
                ReviewImages reviewImages = imageFile.serverFile(file, ReviewImages.class);
                reviewImageRepository.save(reviewImages);
                review.addReviewImages(reviewImages);
            }
        } catch (IOException e) {
            log.info("addReviewImages error={}", e.getMessage());
        }
    }

    /*public void updateReview(Long id, SaveReviewDto dto) {

        Review review = reviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다."));

        if (review.getGrade() != dto.getGrade()) {


            review.getShop().updateReview(review.getGrade(), dto.getGrade());
            review.changeGrade(dto.getGrade());
        }

        review.changeReviewTitle(dto.getReviewTitle());
        review.changeContent(dto.getContent());
    }*/

    public void addReviewImages(Long reviewId, List<MultipartFile> files) {

        addReviewImages(files, reviewRepository.findById(reviewId).orElseThrow());
    }

    public void deleteReviewImages(Long reviewImageId) {

        ReviewImages reviewImages = reviewImageRepository.findById(reviewImageId).orElseThrow(() ->
                new EntityNotFoundException("이미지를 찾을 수 없습니다."));

        Review review = reviewImages.getReview();
        review.deleteReviewImages(reviewImages);

        reviewImageRepository.delete(reviewImages);
    }

    public void deleteReview(Long memberId, Long reviewId, Long shopId) {

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 리뷰 입니다."));
        log.info("리뷰 내용={}", review.getReviewTitle());
        /*review.getReviewImagesList().forEach(image -> deleteReviewImages(image.getId()));*/

        if (!Objects.equals(review.getMemberId(), memberId)) {
            throw new RuntimeException("권한이 없습니다.");
        }
        kafkaTemplate.send("shop-review-topics", shopId.toString(), ShopReviewDeletedEventDto.builder()
                .shopId(shopId)
                .grade(review.getGrade())
                .build());
        review.getReviewImagesList().forEach(reviewImageRepository::delete);

        reviewRepository.delete(review);

        log.info("리뷰 확인={}", review.getReviewTitle());
    }
}
