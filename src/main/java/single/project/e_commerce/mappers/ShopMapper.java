package single.project.e_commerce.mappers;


import org.mapstruct.Mapper;
import single.project.e_commerce.dto.request.ShopRequestDTO;
import single.project.e_commerce.dto.response.RegisterShopResponseDTO;
import single.project.e_commerce.dto.response.ShopInformationResponseDTO;
import single.project.e_commerce.models.Shop;

@Mapper(componentModel = "spring")
public interface ShopMapper {
    Shop toShop(ShopRequestDTO dto);

    ShopInformationResponseDTO toResponse(Shop shop);

    RegisterShopResponseDTO toResponse2(Shop shop);
}
