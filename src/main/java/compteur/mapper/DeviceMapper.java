package compteur.mapper;


import compteur.dto.DeviceDTO;
import compteur.entite.Device;
import org.springframework.stereotype.Component;

@Component
public class DeviceMapper {

    public Device toEntity(DeviceDTO dto) {
        return Device.builder()
                .devEui(dto.getDevEui())
                .name(dto.getName())
                .applicationId(dto.getApplicationId())
                .deviceProfileId(dto.getDeviceProfileId())
                .description(dto.getDescription())
                .build();
    }

    public DeviceDTO toDTO(Device device) {
        return DeviceDTO.builder()
                .devEui(device.getDevEui())
                .name(device.getName())
                .applicationId(device.getApplicationId())
                .deviceProfileId(device.getDeviceProfileId())
                .description(device.getDescription())
                .build();
    }
}

