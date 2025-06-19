package compteur.serviceImpl;

import compteur.config.AppConfig;
import compteur.dto.GatewayDTO;
import compteur.entite.Gateway;
import compteur.grpc.ChirpstackGatewayGrpcClient;
import compteur.mapper.GatewayMapper;
import compteur.repository.GatewayRepository;
import compteur.service.GatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GatewayServiceImpl implements GatewayService {

    private final GatewayRepository gatewayRepository;
    private final GatewayMapper gatewayMapper;
    private final ChirpstackGatewayGrpcClient grpcGatewayClient;
    private final AppConfig appConfig;

    private static String generateEUI64() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[8];
        random.nextBytes(bytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    @Override
    public void createGateway(GatewayDTO dto) {
        Gateway gateway = gatewayMapper.toEntity(dto);

        // Toujours forcer un EUI64 pour tester
        gateway.setGatewayId(generateEUI64());

        gateway.setTenantId(appConfig.getTenantId());

        System.out.println("DEBUG: gatewayId length=" + gateway.getGatewayId().length() + ", value=" + gateway.getGatewayId());

        gatewayRepository.save(gateway);

        grpcGatewayClient.createGateway(
                gateway.getGatewayId(),
                gateway.getName(),
                gateway.getDescription(),
                gateway.getTenantId()
        );
    }


    @Override
    public void deleteGateway(String gatewayId) {
        gatewayRepository.deleteById(gatewayId);
        grpcGatewayClient.deleteGateway(gatewayId);
    }

    @Override
    public GatewayDTO getGatewayById(String gatewayId) {
        Optional<Gateway> optional = gatewayRepository.findById(gatewayId);
        return optional.map(gatewayMapper::toDTO).orElse(null);
    }

    @Override
    public List<GatewayDTO> getAllGateways() {
        return gatewayRepository.findAll()
                .stream()
                .map(gatewayMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateGateway(String gatewayId, GatewayDTO dto) {
        Optional<Gateway> optional = gatewayRepository.findById(gatewayId);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("Gateway not found: " + gatewayId);
        }

        Gateway gateway = optional.get();
        gateway.setName(dto.getName());
        gateway.setDescription(dto.getDescription());

        gateway.setTenantId(appConfig.getTenantId());

        gatewayRepository.save(gateway);

        grpcGatewayClient.updateGateway(
                gateway.getGatewayId(),
                gateway.getName(),
                gateway.getDescription()
        );
    }


}
