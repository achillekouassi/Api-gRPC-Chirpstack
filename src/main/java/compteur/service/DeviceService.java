package compteur.service;

import compteur.dto.DeviceDTO;
import java.util.List;

public interface DeviceService {
    void createDevice(DeviceDTO deviceDTO);
    void updateDevice(DeviceDTO deviceDTO);
    void deleteDevice(String devEui);
    DeviceDTO getDeviceById(String devEui);
    List<DeviceDTO> getAllDevices();

}
