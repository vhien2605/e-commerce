package single.project.e_commerce.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import single.project.e_commerce.dto.request.AddToCartRequestDTO;
import single.project.e_commerce.dto.request.CartItemUpdateRequestDTO;
import single.project.e_commerce.dto.request.CartUpdateRequestDTO;
import single.project.e_commerce.dto.request.RemoveCartItemsRequestDTO;
import single.project.e_commerce.dto.response.CartItemResponseDTO;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.mappers.CartDetailMapper;
import single.project.e_commerce.models.Cart;
import single.project.e_commerce.models.CartDetail;
import single.project.e_commerce.models.Product;
import single.project.e_commerce.repositories.CartDetailRepository;
import single.project.e_commerce.repositories.CartRepository;
import single.project.e_commerce.repositories.ProductRepository;
import single.project.e_commerce.utils.commons.GlobalMethod;
import single.project.e_commerce.utils.enums.ErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartDetailRepository cartDetailRepository;
    private final CartDetailMapper cartDetailMapper;

    public List<CartItemResponseDTO> getCartItems() {
        String username = GlobalMethod.extractUserFromContext();
        Cart cart = cartRepository.getCartByUserUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXIST));
        List<CartDetail> cartDetails = cartDetailRepository.getAllCartDetailsByCartId(cart.getId());
        return cartDetails.stream()
                .map(cartDetailMapper::toResponse)
                .toList();
    }


    public String updateCartItems(CartUpdateRequestDTO dto) {
        String username = GlobalMethod.extractUserFromContext();
        Cart cart = cartRepository.getCartByUserUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXIST));
        List<CartDetail> cartDetails = cartDetailRepository.getAllCartDetailsByCartId(cart.getId());
        Set<CartItemUpdateRequestDTO> updatedItemsDTO = dto.getUpdatedCartItems();
        List<CartDetail> updatedItems = new ArrayList<>();
        for (CartDetail cartDetail : cartDetails) {
            for (CartItemUpdateRequestDTO item : updatedItemsDTO) {
                if (cartDetail.getId() == item.getId()) {
                    if (item.getQuantity() <= 0 || item.getQuantity() > cartDetail.getProduct().getRemainingQuantity()) {
                        throw new AppException(ErrorCode.QUANTITY_INVALID);
                    }
                    cartDetail.setQuantity(item.getQuantity());
                    updatedItems.add(cartDetail);
                }
            }
        }
        cartDetailRepository.saveAll(updatedItems);
        return "updated cart item quantities successfully";
    }

    public String addToCart(AddToCartRequestDTO dto) {
        long productId = dto.getId();
        long quantity = dto.getQuantity();
        String username = GlobalMethod.extractUserFromContext();
        Cart cart = cartRepository.getCartByUserUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXIST));
        List<CartDetail> cartDetails = cartDetailRepository.getAllCartDetailsByCartId(cart.getId());
        for (CartDetail cartDetail : cartDetails) {
            // if item is already existed in cart, just increase quantity
            if (cartDetail.getProduct().getId() == productId) {
                long newQuantity = cartDetail.getQuantity() + quantity;
                // if new quantity passed product quantity, throw error
                if (newQuantity > cartDetail.getProduct().getRemainingQuantity()) {
                    throw new AppException(ErrorCode.QUANTITY_INVALID);
                }
                cartDetail.setQuantity(newQuantity);
                cartDetailRepository.save(cartDetail);
                return "Add products to cart successfully";
            }
        }

        // if item is not existed , create new
        Product product = productRepository.findProductById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXIST));
        if (quantity > product.getRemainingQuantity()) {
            throw new AppException(ErrorCode.QUANTITY_INVALID);
        }
        CartDetail cartDetail = CartDetail.builder()
                .product(product)
                .price(product.getPrice())
                .cart(cart)
                .quantity(quantity)
                .build();
        cartDetailRepository.save(cartDetail);
        return "Add products to cart successfully";
    }

    public int removeFromCart(RemoveCartItemsRequestDTO dto) {
        return cartDetailRepository.deleteByIdsInCart(new ArrayList<>(dto.getIds()));
    }
}
