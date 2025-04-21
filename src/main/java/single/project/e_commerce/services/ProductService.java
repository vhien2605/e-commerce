package single.project.e_commerce.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import single.project.e_commerce.dto.request.CategoryRequestDTO;
import single.project.e_commerce.dto.request.ProductRequestDTO;
import single.project.e_commerce.dto.request.ProductUpdateRequestDTO;
import single.project.e_commerce.dto.request.QuantityRequestDTO;
import single.project.e_commerce.dto.response.PageResponseDTO;
import single.project.e_commerce.dto.response.ProductDetailResponseDTO;
import single.project.e_commerce.dto.response.ProductResponseDTO;
import single.project.e_commerce.dto.response.UserResponseDTO;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.mappers.CategoryMapper;
import single.project.e_commerce.mappers.ProductMapper;
import single.project.e_commerce.models.*;
import single.project.e_commerce.repositories.CategoryRepository;
import single.project.e_commerce.repositories.ProductRepository;
import single.project.e_commerce.repositories.ShopRepository;
import single.project.e_commerce.repositories.UserRepository;
import single.project.e_commerce.repositories.specifications.SpecificationBuilder;
import single.project.e_commerce.utils.commons.AppConst;
import single.project.e_commerce.utils.commons.GlobalMethod;
import single.project.e_commerce.utils.enums.ErrorCode;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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


    public List<ProductResponseDTO> getMyProducts() {
        String username = GlobalMethod.extractUserFromContext();
        List<Product> products = productRepository.findProductsByUsername(username);
        return products.stream().map(productMapper::toResponse).toList();
    }

    public ProductDetailResponseDTO productDetail(long id) {
        Product product = productRepository.findByIdWithReviews(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXIST));
        return productMapper.toResponse2(product);
    }

    public List<ProductResponseDTO> getAllProductsFilter(String[] product, String[] sortBy) {
        SpecificationBuilder<Product> builder = new SpecificationBuilder<>();
        Pattern pattern = Pattern.compile(AppConst.SEARCH_SPEC_OPERATOR);
        Pattern sortPattern = Pattern.compile(AppConst.SORT_BY);

        // build specification
        for (String s : product) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3),
                        matcher.group(4), matcher.group(5), matcher.group(6));
            }
        }

        // build sort instance
        List<Sort.Order> sortOrders = new ArrayList<>();
        for (String sb : sortBy) {
            Matcher sortMatcher = sortPattern.matcher(sb);
            if (sortMatcher.find()) {
                String field = sortMatcher.group(1);
                String value = sortMatcher.group(3);
                Sort.Direction direction = (value.equalsIgnoreCase("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
                sortOrders.add(new Sort.Order(direction, field));
            }
        }
        Sort sort = Sort.by(sortOrders);
        Specification<Product> specification = builder.build();
        List<Product> products = productRepository.findAll(specification, sort);
        return products.stream()
                .map(productMapper::toResponse)
                .toList();
    }

    public PageResponseDTO<?> getAllProductFilterPagination(Pageable pageable, String[] product, String[] sortBy) {
        SpecificationBuilder<Product> builder = new SpecificationBuilder<>();
        Pattern pattern = Pattern.compile(AppConst.SEARCH_SPEC_OPERATOR);
        Pattern sortPattern = Pattern.compile(AppConst.SORT_BY);

        // build specification
        for (String s : product) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3),
                        matcher.group(4), matcher.group(5), matcher.group(6));
            }
        }
        Specification<Product> specification = builder.build();

        // build sort properties
        List<Sort.Order> sortOrders = new ArrayList<>();
        for (String sb : sortBy) {
            Matcher sortMatcher = sortPattern.matcher(sb);
            if (sortMatcher.find()) {
                String field = sortMatcher.group(1);
                String value = sortMatcher.group(3);
                Sort.Direction direction = (value.equalsIgnoreCase("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
                sortOrders.add(new Sort.Order(direction, field));
            }
        }
        Sort sort = Sort.by(sortOrders);

        // add sort properties to pageable
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                sort);

        //get page response when not fetching collection yet
        Page<Product> products = productRepository.findAll(specification, sortedPageable);
        List<Long> orderedIds = products.stream().map(Product::getId).toList();
        // fetching collection in the second query
        List<Product> afterFetchedUsers = productRepository.findAllProductsWithId(orderedIds);
        Map<Long, Product> productByIdMap = afterFetchedUsers.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        List<ProductResponseDTO> result = orderedIds.stream()
                .map(productByIdMap::get)
                .map(productMapper::toResponse)
                .toList();
        return PageResponseDTO.builder()
                .data(result)
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(products.getTotalPages())
                .build();
    }
}
