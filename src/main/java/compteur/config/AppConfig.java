package compteur.config;

import compteur.grpc.ChirpstackGatewayGrpcClient;
import compteur.grpc.ChirpstackGrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class AppConfig {

    @Value("${chirpstack.grpc.host:localhost}")
    private String chirpstackHost;

    @Value("${chirpstack.grpc.port:9090}")
    private int chirpstackPort;

    @Value("${chirpstack.api.token}")
    private String apiToken;

    @Value("${chirpstack.tenant-id}")
    private String tenantId;

    @Bean
    public ChirpstackGrpcClient chirpstackGrpcClient() {
        return new ChirpstackGrpcClient(
                chirpstackHost,
                chirpstackPort,
                apiToken,
                30
        );
    }

    @Bean
    public ChirpstackGatewayGrpcClient chirpstackGatewayGrpcClient() {
        return new ChirpstackGatewayGrpcClient(
                chirpstackHost,
                chirpstackPort,
                apiToken,
                30
        );
    }
    public String getTenantId() {
        return tenantId;
    }
}
