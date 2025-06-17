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
public class Device {
    @Id
    private String devEui;
    private String name;
    private String applicationId;
    private String deviceProfileId;
    private String description;
    private String status; // ex: "ONLINE" ou "OFFLINE"
    private Long lastSeenAt;
}

