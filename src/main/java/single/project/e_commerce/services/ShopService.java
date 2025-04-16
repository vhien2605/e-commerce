package single.project.e_commerce.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import single.project.e_commerce.dto.response.ShopInformationResponseDTO;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.mappers.ShopMapper;
import single.project.e_commerce.repositories.ShopRepository;
import single.project.e_commerce.utils.commons.GlobalMethod;
import single.project.e_commerce.utils.enums.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopService {
    private final ShopRepository shopRepository;
    private final ShopMapper shopMapper;

    public List<ShopInformationResponseDTO> getAllShops() {
        return shopRepository.findAll().stream()
                .map(shopMapper::toResponse)
                .toList();
    }

    public ShopInformationResponseDTO getMyShop() {
        String userUsername = GlobalMethod.extractUserFromContext();
        return shopMapper.toResponse(shopRepository.findByUsername(userUsername)
                .orElseThrow(() -> new AppException(ErrorCode.SHOP_NOT_EXIST)));
    }

    public ShopInformationResponseDTO getShopInformationById(Long id) {
        log.info("---------------shop by shopId-----------");
        return shopMapper.toResponse(shopRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SHOP_NOT_EXIST)));
    }

    public ShopInformationResponseDTO getInformationByProductId(Long id) {
        log.info("---------------shop by productId-----------");
        return shopMapper.toResponse(shopRepository.findByProductId(id)
                .orElseThrow(() -> new AppException(ErrorCode.SHOP_NOT_EXIST)));
    }
}
