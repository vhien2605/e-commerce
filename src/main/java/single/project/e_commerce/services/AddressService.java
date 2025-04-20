package single.project.e_commerce.services;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import single.project.e_commerce.dto.response.AddressFilterResponseDTO;
import single.project.e_commerce.dto.response.AddressResponseDTO;
import single.project.e_commerce.dto.response.PageResponseDTO;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.mappers.AddressMapper;
import single.project.e_commerce.models.Address;
import single.project.e_commerce.models.User;
import single.project.e_commerce.repositories.AddressRepository;
import single.project.e_commerce.repositories.specifications.SpecificationBuilder;
import single.project.e_commerce.utils.commons.AppConst;
import single.project.e_commerce.utils.enums.ErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public AddressResponseDTO getAddressWithUsersByLocation(String name, String city, String country) {
        return addressMapper.toResponse(addressRepository.getAddressWithUsersAndRoles(name, city, country).
                orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXIST))
        );
    }

    public List<AddressResponseDTO> getAllAddress() {
        return addressRepository.findAll()
                .stream().map(addressMapper::toResponse)
                .toList();
    }

    public PageResponseDTO<?> getAllAddressByFilterSpecification(Pageable pageable, String[] address, String[] sortBy) {
        SpecificationBuilder<Address> builder = new SpecificationBuilder<>();
        Pattern pattern = Pattern.compile(AppConst.SEARCH_SPEC_OPERATOR);
        Pattern sortPattern = Pattern.compile(AppConst.SORT_BY);

        // build specification
        for (String s : address) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3),
                        matcher.group(4), matcher.group(5), matcher.group(6));
            }
        }
        Specification<Address> specification = builder.build();


        // build Sort
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

        // pageable instance after add Sort object
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                sort);

        Page<Address> addressPage = addressRepository.findAll(specification, sortedPageable);
        List<AddressFilterResponseDTO> result = addressPage.stream()
                .map(addressMapper::toResponse2)
                .toList();

        return PageResponseDTO.builder()
                .totalPage(addressPage.getTotalPages())
                .pageNo(sortedPageable.getPageNumber())
                .pageSize(sortedPageable.getPageSize())
                .data(result)
                .build();
    }

    public List<AddressFilterResponseDTO> getAllAddressBySpecificationNotPagination(String[] address, String[] sortBy) {
        SpecificationBuilder<Address> builder = new SpecificationBuilder<>();
        Pattern pattern = Pattern.compile(AppConst.SEARCH_SPEC_OPERATOR);
        Pattern sortPattern = Pattern.compile(AppConst.SORT_BY);
        for (String s : address) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3),
                        matcher.group(4), matcher.group(5), matcher.group(6));
            }
        }
        Specification<Address> specification = builder.build();

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

        return addressRepository.findAll(specification, sort).stream()
                .map(addressMapper::toResponse2)
                .toList();
    }
}
