package single.project.e_commerce.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import single.project.e_commerce.dto.request.ShipmentStatusRequestDTO;
import single.project.e_commerce.dto.response.ShipmentResponseDTO;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.mappers.ShipmentMapper;
import single.project.e_commerce.models.OrderDetail;
import single.project.e_commerce.models.Product;
import single.project.e_commerce.models.Shipment;
import single.project.e_commerce.repositories.ProductRepository;
import single.project.e_commerce.repositories.ShipmentRepository;
import single.project.e_commerce.utils.commons.GlobalMethod;
import single.project.e_commerce.utils.enums.ErrorCode;
import single.project.e_commerce.utils.enums.ShippingStatus;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentService {
    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;
    private final ProductRepository productRepository;

    public List<ShipmentResponseDTO> getShopShipment() {
        String username = GlobalMethod.extractUserFromContext();
        return shipmentRepository.getAllShipmentFromShopOwnerUsername(username)
                .stream().map(shipmentMapper::toResponse).toList();
    }

    public List<ShipmentResponseDTO> getUserShipment() {
        String username = GlobalMethod.extractUserFromContext();
        return shipmentRepository.getAllShipmentFromUserUsername(username)
                .stream().map(shipmentMapper::toResponse).toList();
    }

    @Transactional
    public ShipmentResponseDTO updateShipment(ShipmentStatusRequestDTO dto) {
        Long shipmentId = dto.getId();
        ShippingStatus shippingStatus = dto.getStatus();
        log.info("find shipment");
        Shipment shipment = shipmentRepository.findShipmentById(shipmentId)
                .orElseThrow(() -> new AppException(ErrorCode.SHIPMENT_NOT_EXIST));
        log.info("find shipment end");
        OrderDetail orderDetail = shipment.getOrderDetail();
        if (shippingStatus.equals(ShippingStatus.SHIPPED)) {
            shipment.setShippingStatus(ShippingStatus.SHIPPED);
            shipment.setDeliveredAt(new Date());
            shipmentRepository.save(shipment);
            return shipmentMapper.toResponse(shipment);
        }
        shipment.setShippingStatus(ShippingStatus.RETURNING);
        shipment.setDeliveredAt(new Date());
        // update quantity because shipment returning
        log.info("find product");
        Product product = productRepository.findProductByShipment(shipmentId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXIST));
        long newSoldQuantity = (product.getSoldQuantity() - orderDetail.getQuantity() >= 0) ? product.getSoldQuantity() - orderDetail.getQuantity() : 0;
        product.setSoldQuantity(newSoldQuantity);
        product.setRemainingQuantity(product.getRemainingQuantity() + orderDetail.getQuantity());

        shipmentRepository.save(shipment);
        productRepository.save(product);
        return shipmentMapper.toResponse(shipment);
    }
}
