package single.project.e_commerce.services;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import single.project.e_commerce.dto.response.AddressFilterResponseDTO;
import single.project.e_commerce.dto.response.AddressResponseDTO;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.mappers.AddressMapper;
import single.project.e_commerce.models.Address;
import single.project.e_commerce.repositories.AddressRepository;
import single.project.e_commerce.repositories.specifications.SpecificationBuilder;
import single.project.e_commerce.utils.commons.AppConst;
import single.project.e_commerce.utils.enums.ErrorCode;

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

    public List<AddressFilterResponseDTO> getAllAddressByFilterSpecification(Pageable pageable, String[] address) {
        SpecificationBuilder<Address> builder = new SpecificationBuilder<>();
        Pattern pattern = Pattern.compile(AppConst.SEARCH_SPEC_OPERATOR);
        for (String s : address) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3),
                        matcher.group(4), matcher.group(5), matcher.group(6));
            }
        }
        Specification<Address> specification = builder.build();
        return addressRepository.findAll(specification).stream()
                .map(addressMapper::toResponse2)
                .toList();
    }
}
