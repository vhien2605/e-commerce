package single.project.e_commerce.controllers;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import single.project.e_commerce.dto.request.ReviewRequestDTO;
import single.project.e_commerce.dto.request.ReviewUpdateRequestDTO;
import single.project.e_commerce.dto.response.ApiResponse;
import single.project.e_commerce.dto.response.ApiSuccessResponse;
import single.project.e_commerce.services.ReviewService;

@RestController
@RequestMapping("/api/review/")
@RequiredArgsConstructor
@Validated
public class ReviewController {

    private final ReviewService reviewService;

    @PreAuthorize("hasRole('CUSTOMER') OR hasRole('SELLER')")
    @PostMapping("/")
    public ApiResponse createReview(@RequestBody @Valid ReviewRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Create review successfully")
                .data(reviewService.create(dto))
                .build();
    }

    @PreAuthorize("hasRole('CUSTOMER') OR hasRole('SELLER')")
    @PatchMapping("/{id}")
    public ApiResponse updateReview(@Min(value = 1, message = "review id must greater than 0")
                                    @PathVariable("id") long reviewId
            , @RequestBody @Valid ReviewUpdateRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Update review successfully")
                .data(reviewService.update(reviewId, dto))
                .build();
    }

    @PreAuthorize("hasRole('CUSTOMER') OR hasRole('SELLER')")
    @GetMapping("/my-reviews")
    public ApiResponse readMyReviews() {
        return ApiSuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("read my reviews successfully")
                .data(reviewService.readMyReviews())
                .build();
    }
}
