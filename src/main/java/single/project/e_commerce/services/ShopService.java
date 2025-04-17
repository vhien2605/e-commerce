package single.project.e_commerce.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import single.project.e_commerce.dto.request.ShopRequestDTO;
import single.project.e_commerce.dto.response.RegisterShopResponseDTO;
import single.project.e_commerce.dto.response.ShopInformationResponseDTO;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.mappers.ShopMapper;
import single.project.e_commerce.mappers.UserMapper;
import single.project.e_commerce.models.Role;
import single.project.e_commerce.models.Shop;
import single.project.e_commerce.models.User;
import single.project.e_commerce.repositories.RoleRepository;
import single.project.e_commerce.repositories.ShopRepository;
import single.project.e_commerce.repositories.UserRepository;
import single.project.e_commerce.utils.commons.GlobalMethod;
import single.project.e_commerce.utils.enums.ErrorCode;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopService {
    private final ShopRepository shopRepository;
    private final ShopMapper shopMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

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


    @Transactional
    public RegisterShopResponseDTO registerShop(ShopRequestDTO dto) {
        String username = GlobalMethod.extractUserFromContext();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        // check if SELLER role existed?
        Set<Role> roles = user.getRoles();
        if (roles.stream().anyMatch(role -> role.getName().equals("SELLER"))) {
            throw new AppException(ErrorCode.SELLER_REGISTERED);
        }

        //update user roles
        Role role = roleRepository.findByName("SELLER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        user.getRoles().add(role);

        //create shop
        Shop shop = shopMapper.toShop(dto);
        shop.setUser(user);

        userRepository.save(user);
        Shop updateShop = shopRepository.save(shop);
        return shopMapper.toResponse2(shop);
    }
}
