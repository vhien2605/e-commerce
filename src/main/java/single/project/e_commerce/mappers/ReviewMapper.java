package single.project.e_commerce.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import single.project.e_commerce.dto.request.ReviewRequestDTO;
import single.project.e_commerce.dto.response.ReviewResponseDTO;
import single.project.e_commerce.models.Review;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "product.id", target = "productId")
    ReviewResponseDTO toResponse(Review review);

    Review toReview(ReviewRequestDTO dto);
}
