package single.project.e_commerce.mappers;


import org.mapstruct.Mapper;
import single.project.e_commerce.dto.request.CategoryRequestDTO;
import single.project.e_commerce.dto.response.CategoryResponseDTO;
import single.project.e_commerce.models.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryRequestDTO dto);

    CategoryResponseDTO toResponse(Category category);
}
