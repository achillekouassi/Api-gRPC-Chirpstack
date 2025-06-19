package compteur.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GatewayDTO {
    private String gatewayId;
    private String name;
    private String description;
    private String tenantId;
}
