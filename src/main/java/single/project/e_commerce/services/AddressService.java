package single.project.e_commerce.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import single.project.e_commerce.dto.request.AddressRequestDTO;
import single.project.e_commerce.dto.response.AddressResponseDTO;
import single.project.e_commerce.exceptions.DataInvalidException;
import single.project.e_commerce.mappers.AddressMapper;
import single.project.e_commerce.repositories.AddressRepository;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public AddressResponseDTO getAddressWithUsersByLocation(AddressRequestDTO request) {
        return addressMapper.toResponse(addressRepository.getAddressWithUsersAndRoles(request.getName(),
                        request.getCity(),
                        request.getCountry()
                ).orElseThrow(() -> new DataInvalidException("Can't find any users with invalid address"))
        );
    }
}
