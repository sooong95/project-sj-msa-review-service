package song.sj.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import song.sj.dto.SaveReviewDto;
import song.sj.service.ReviewService;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{shopId}")
    public ResponseEntity<String> saveReview(@RequestHeader("X-User-Id") Long memberId, @PathVariable("shopId") Long shopId, @ModelAttribute SaveReviewDto dto,
                                             @RequestParam("image") List<MultipartFile> files) throws AccessDeniedException {
        reviewService.saveReview(memberId, shopId, dto, files);
        return new ResponseEntity<>("리뷰 저장 성공!", HttpStatus.CREATED);
    }

    /*@PatchMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable("reviewId") Long reviewId, @RequestBody SaveReviewDto dto) {

        reviewService.updateReview(reviewId, dto);
        return new ResponseEntity<>("성공적으로 리뷰가 변경 되었습니다.", HttpStatus.OK);
    }*/

    @PostMapping("/{reviewId}/images")
    public ResponseEntity<String> addReviewImage(@PathVariable("reviewId") Long reviewId,
                                                 @RequestParam("image") List<MultipartFile> files) {
        reviewService.addReviewImages(reviewId, files);
        return new ResponseEntity<>("정상적으로 이미지가 등록 되었습니다.", HttpStatus.OK);
    }

    @DeleteMapping("/{reviewImageId}/images")
    public ResponseEntity<String> deleteImage(@PathVariable("reviewImageId") Long reviewImageId) {

        reviewService.deleteReviewImages(reviewImageId);
        return new ResponseEntity<>("정상적으로 이미지가 삭제 되었습니다.", HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@RequestHeader("X-User-Id") Long memberId,@PathVariable("reviewId") Long reviewId, @RequestParam("shopId") Long shopId) {

        log.info("리뷰 Id={},shop Id={}", reviewId, shopId);
        reviewService.deleteReview(memberId, reviewId, shopId);
        return new ResponseEntity<>("리뷰가 정상적으로 삭제 되었습니다.", HttpStatus.OK);
    }
}
