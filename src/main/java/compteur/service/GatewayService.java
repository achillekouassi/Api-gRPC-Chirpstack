package compteur.service;

import compteur.dto.GatewayDTO;

import java.util.List;

public interface GatewayService {
    void createGateway(GatewayDTO dto);
    void deleteGateway(String gatewayId);
    List<GatewayDTO> getAllGateways();
    GatewayDTO getGatewayById(String gatewayId);
    void updateGateway(String gatewayId, GatewayDTO dto);

}
