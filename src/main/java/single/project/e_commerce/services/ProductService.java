package single.project.e_commerce.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import single.project.e_commerce.dto.request.CategoryRequestDTO;
import single.project.e_commerce.dto.request.ProductRequestDTO;
import single.project.e_commerce.dto.request.ProductUpdateRequestDTO;
import single.project.e_commerce.dto.request.QuantityRequestDTO;
import single.project.e_commerce.dto.response.ProductDetailResponseDTO;
import single.project.e_commerce.dto.response.ProductResponseDTO;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.mappers.CategoryMapper;
import single.project.e_commerce.mappers.ProductMapper;
import single.project.e_commerce.models.*;
import single.project.e_commerce.repositories.CategoryRepository;
import single.project.e_commerce.repositories.ProductRepository;
import single.project.e_commerce.repositories.ShopRepository;
import single.project.e_commerce.repositories.UserRepository;
import single.project.e_commerce.utils.commons.GlobalMethod;
import single.project.e_commerce.utils.enums.ErrorCode;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final FileService fileService;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ShopRepository shopRepository;

    private final String IMAGE_FOLDER = "product_images";
    private final UserRepository userRepository;


    /**
     * extract shop from user from context
     * this method only be called when users are "Seller"
     * , so shop must be already exist when user.getShop()
     *
     * @param dto  dto with creating information
     * @param file image file of product
     * @return product object after saving
     */


    public ProductResponseDTO save(ProductRequestDTO dto, MultipartFile file) {
        try {
            //mapper
            Product product = productMapper.toProduct(dto);

            // find user and extract shop
            String username = GlobalMethod.extractUserFromContext();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
            Shop shop = user.getShop();


            // if category is existed , use category in DB
            List<Category> existedCategories = categoryRepository.findAll();
            List<Category> uploadCategories = new ArrayList<>();
            for (Category uploadCat : product.getCategories()) {
                Optional<Category> matched = existedCategories.stream()
                        .filter(existed -> uploadCat.getName().equalsIgnoreCase(existed.getName()) &&
                                uploadCat.getDescription().equalsIgnoreCase(existed.getDescription()))
                        .findFirst();
                uploadCategories.add(matched.orElse(uploadCat));
            }
            product.setCategories(new HashSet<>(uploadCategories));

            // setter shop
            product.setShop(shop);

            // store file in cloudinary
            String fileUrl = fileService.uploadFile(file, IMAGE_FOLDER);
            product.setImageUrl(fileUrl);
            product.setSoldQuantity(0);

            //setter products
            shop.getProducts().add(product);

            return productMapper.toResponse(productRepository.save(product));
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_STORAGE_SERVICE_UNAVAILABLE);
        }
    }


    public ProductResponseDTO updateProduct(Long prodId, ProductUpdateRequestDTO dto, MultipartFile file) {
        //extract user
        log.info("-----------extract user---------");
        String username = GlobalMethod.extractUserFromContext();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        Product product = productRepository.findById(prodId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXIST));

        // if users aren't admin,they can't update products which
        // are not belongs to their shop
        if (user.getRoles().stream().noneMatch(role -> role.getName().equals("ADMIN"))) {
            if (!product.getShop().equals(user.getShop())) {
                throw new AppException(ErrorCode.ACCESS_DENIED);
            }
        }

        //normal fields setter
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setShortDesc(dto.getShortDesc());
        product.setLongDesc(dto.getLongDesc());
        product.setRemainingQuantity(dto.getRemainingQuantity());
        product.setSoldQuantity(dto.getSoldQuantity());


        log.info("-----------if category is existed , use category in DB---------");
        // if category is existed , use category in DB
        List<Category> existedCategories = categoryRepository.findAll();
        List<Category> uploadCategories = new ArrayList<>();
        for (CategoryRequestDTO uploadCat : dto.getCategories()) {
            Optional<Category> matched = existedCategories.stream()
                    .filter(existed -> uploadCat.getName().equalsIgnoreCase(existed.getName()) &&
                            uploadCat.getDescription().equalsIgnoreCase(existed.getDescription()))
                    .findFirst();
            uploadCategories.add(matched.orElse(categoryMapper.toCategory(uploadCat)));
        }
        product.setCategories(new HashSet<>(uploadCategories));

        try {
            log.info("-------------delete old file and save new file--------------");
            fileService.deleteFileByUrl(product.getImageUrl());
            String newFileUrl = fileService.uploadFile(file, IMAGE_FOLDER);
            product.setImageUrl(newFileUrl);
            return productMapper.toResponse(productRepository.save(product));
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_STORAGE_SERVICE_UNAVAILABLE);
        }
    }

    public ProductResponseDTO updateQuantity(Long id, QuantityRequestDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXIST));
        product.setSoldQuantity(dto.getSoldQuantity());
        product.setRemainingQuantity(dto.getRemainingQuantity());
        return productMapper.toResponse(productRepository.save(product));
    }


    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(productMapper::toResponse).toList();
    }
    
    public ProductDetailResponseDTO productDetail(long id) {
        Product product = productRepository.findByIdWithReviews(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXIST));
        return productMapper.toResponse2(product);
    }
}
