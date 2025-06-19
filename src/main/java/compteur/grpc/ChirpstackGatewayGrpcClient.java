package compteur.grpc;



import io.chirpstack.api.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;

import java.util.concurrent.TimeUnit;

public class ChirpstackGatewayGrpcClient {

    private final ManagedChannel channel;
    private final GatewayServiceGrpc.GatewayServiceBlockingStub gatewayStub;
    private final long timeoutSeconds;

    public ChirpstackGatewayGrpcClient(String host, int port, String apiToken, long timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;

        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .keepAliveTime(30, TimeUnit.SECONDS)
                .keepAliveTimeout(5, TimeUnit.SECONDS)
                .build();

        Metadata metadata = new Metadata();
        metadata.put(Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER),
                "Bearer " + apiToken);

        this.gatewayStub = GatewayServiceGrpc.newBlockingStub(channel)
                .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata));
    }

    public boolean createGateway(String gatewayId, String name, String description, String tenantId) {
        Gateway gateway = Gateway.newBuilder()
                .setGatewayId(gatewayId)
                .setName(name)
                .setDescription(description)
                .setTenantId(tenantId)  // <-- ajouté
                .build();

        CreateGatewayRequest request = CreateGatewayRequest.newBuilder()
                .setGateway(gateway)
                .build();

        try {
            gatewayStub.withDeadlineAfter(timeoutSeconds, TimeUnit.SECONDS).create(request);
            System.out.println(" Gateway created successfully: " + gatewayId);
            return true;
        } catch (Exception e) {
            System.err.println(" Failed to create gateway: " + e.getMessage());
            return false;
        }
    }


    public boolean deleteGateway(String gatewayId) {
        DeleteGatewayRequest request = DeleteGatewayRequest.newBuilder()
                .setGatewayId(gatewayId)
                .build();

        try {
            gatewayStub.withDeadlineAfter(timeoutSeconds, TimeUnit.SECONDS).delete(request);
            System.out.println(" Gateway deleted successfully: " + gatewayId);
            return true;
        } catch (Exception e) {
            System.err.println(" Failed to delete gateway: " + e.getMessage());
            return false;
        }
    }

    public Gateway getGateway(String gatewayId) {
        GetGatewayRequest request = GetGatewayRequest.newBuilder()
                .setGatewayId(gatewayId)
                .build();

        try {
            GetGatewayResponse response = gatewayStub.withDeadlineAfter(timeoutSeconds, TimeUnit.SECONDS)
                    .get(request);
            return response.getGateway();
        } catch (Exception e) {
            System.err.println(" Failed to get gateway: " + e.getMessage());
            return null;
        }
    }

    public boolean updateGateway(String gatewayId, String name, String description) {
        Gateway gateway = Gateway.newBuilder()
                .setGatewayId(gatewayId)
                .setName(name)
                .setDescription(description)
                .build(); // <-- ne pas mettre .setTenantId()

        UpdateGatewayRequest request = UpdateGatewayRequest.newBuilder()
                .setGateway(gateway)
                .build();

        try {
            gatewayStub.withDeadlineAfter(timeoutSeconds, TimeUnit.SECONDS).update(request);
            System.out.println("✅ Gateway updated successfully: " + gatewayId);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Failed to update gateway: " + e.getMessage());
            return false;
        }
    }


    public void shutdown() throws InterruptedException {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}

