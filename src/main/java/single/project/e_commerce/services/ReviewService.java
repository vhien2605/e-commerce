package single.project.e_commerce.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import single.project.e_commerce.dto.request.ReviewRequestDTO;
import single.project.e_commerce.dto.request.ReviewUpdateRequestDTO;
import single.project.e_commerce.dto.response.ReviewResponseDTO;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.mappers.ReviewMapper;
import single.project.e_commerce.models.Product;
import single.project.e_commerce.models.Review;
import single.project.e_commerce.models.User;
import single.project.e_commerce.repositories.ProductRepository;
import single.project.e_commerce.repositories.ReviewRepository;
import single.project.e_commerce.repositories.UserRepository;
import single.project.e_commerce.utils.commons.GlobalMethod;
import single.project.e_commerce.utils.enums.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReviewMapper reviewMapper;


    public ReviewResponseDTO create(ReviewRequestDTO dto) {
        String username = GlobalMethod.extractUserFromContext();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXIST));
        Review review = reviewMapper.toReview(dto);
        review.setUser(user);
        review.setProduct(product);
        return reviewMapper.toResponse(reviewRepository.save(review));
    }


    public ReviewResponseDTO update(ReviewUpdateRequestDTO dto) {
        Review review = reviewRepository.findById(dto.getReviewId())
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_EXIST));
        review.setTitle(dto.getTitle());
        review.setRate(dto.getRate());
        review.setDescription(dto.getDescription());
        return reviewMapper.toResponse(reviewRepository.save(review));
    }


    public List<ReviewResponseDTO> readMyReviews() {
        String username = GlobalMethod.extractUserFromContext();
        return reviewRepository.getByUsername(username).stream()
                .map(reviewMapper::toResponse)
                .toList();
    }

    public String deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
        return "deleted review";
    }
}
