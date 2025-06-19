package compteur.entite;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Gateway {
    @Id
    private String gatewayId;
    private String name;
    private String description;
    private String tenantId;
}
