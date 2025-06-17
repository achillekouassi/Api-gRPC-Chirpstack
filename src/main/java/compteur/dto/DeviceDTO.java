package compteur.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceDTO {
    private String devEui;
    private String name;
    private String applicationId;
    private String deviceProfileId;
    private String description;


}
