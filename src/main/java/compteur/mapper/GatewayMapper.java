package compteur.mapper;

import compteur.dto.GatewayDTO;
import compteur.entite.Gateway;
import org.springframework.stereotype.Component;

@Component
public class GatewayMapper {

    public Gateway toEntity(GatewayDTO dto) {
        return Gateway.builder()
                .gatewayId(dto.getGatewayId())
                .name(dto.getName())
                .description(dto.getDescription())
                .tenantId(dto.getTenantId())  // <-- ajouté
                .build();
    }

    public GatewayDTO toDTO(Gateway entity) {
        return GatewayDTO.builder()
                .gatewayId(entity.getGatewayId())
                .name(entity.getName())
                .description(entity.getDescription())
                .tenantId(entity.getTenantId())  // <-- ajouté
                .build();
    }

}
