package song.sj.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import song.sj.dto.PageResponseDto;
import song.sj.dto.ReviewResponseDto;
import song.sj.service.query.ReviewQueryService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewQueryController {

    private final ReviewQueryService reviewQueryService;

    @GetMapping("/{shopId}/reviews")
    public ResponseEntity<PageResponseDto<ReviewResponseDto>> getShopReviews(
            @PathVariable("shopId") Long shopId,
            /*@PageableDefault(page = 0, size = 10*//*, sort = "reviewId", direction = Sort.Direction.DESC*//*)*/
            Pageable pageable) {
        log.info("요청받은 Pageable: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        return new ResponseEntity<>(reviewQueryService.getShopReviews(shopId, pageable), HttpStatus.OK);
    }
}
