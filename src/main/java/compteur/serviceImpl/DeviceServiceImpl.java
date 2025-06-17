package compteur.serviceImpl;

import compteur.dto.DeviceDTO;
import compteur.entite.Device;
import compteur.grpc.ChirpstackGrpcClient;
import compteur.mapper.DeviceMapper;
import compteur.repository.DeviceRepository;
import compteur.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;
    private final ChirpstackGrpcClient grpcClient;

    @Override
    public void createDevice(DeviceDTO deviceDTO) {
        Device device = deviceMapper.toEntity(deviceDTO);
        String appId = convertToValidUuid(device.getApplicationId());
        String profileId = convertToValidUuid(device.getDeviceProfileId());

        deviceRepository.save(device);
        grpcClient.createDevice(device.getDevEui(), device.getName(), device.getDescription(), appId, profileId);
    }

    @Override
    public void updateDevice(DeviceDTO deviceDTO) {
        Device device = deviceMapper.toEntity(deviceDTO);
        String appId = convertToValidUuid(device.getApplicationId());
        String profileId = convertToValidUuid(device.getDeviceProfileId());

        deviceRepository.save(device);
        grpcClient.updateDevice(device.getDevEui(), device.getName(), device.getDescription(), appId, profileId);
    }

    @Override
    public void deleteDevice(String devEui) {
        deviceRepository.deleteById(devEui);
        grpcClient.deleteDevice(devEui);
    }

    @Override
    public DeviceDTO getDeviceById(String devEui) {
        Optional<Device> optional = deviceRepository.findById(devEui);
        return optional.map(deviceMapper::toDTO).orElse(null);
    }

    @Override
    public List<DeviceDTO> getAllDevices() {
        return deviceRepository.findAll().stream()
                .map(deviceMapper::toDTO)
                .collect(Collectors.toList());
    }

    private String convertToValidUuid(String input) {
        if (input.matches("^[0-9a-fA-F\\-]{36}$")) {
            return input;
        }
        return UUID.nameUUIDFromBytes(input.getBytes()).toString();
    }
}
