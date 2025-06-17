package compteur.grpc;

import io.chirpstack.api.*;
import io.chirpstack.api.DeviceServiceGrpc.DeviceServiceBlockingStub;
import io.grpc.*;
import io.grpc.stub.MetadataUtils;
import java.util.concurrent.TimeUnit;

public class ChirpstackGrpcClient {
    private final ManagedChannel channel;
    private final DeviceServiceBlockingStub blockingStub;
    private final long timeoutSeconds;

    public ChirpstackGrpcClient(String host, int port, String apiToken, long timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .keepAliveTime(30, TimeUnit.SECONDS)
                .keepAliveTimeout(5, TimeUnit.SECONDS)
                .build();

        Metadata metadata = new Metadata();
        metadata.put(Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER),
                "Bearer " + apiToken);

        this.blockingStub  = DeviceServiceGrpc.newBlockingStub(channel)
                .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata));

    }
    public void shutdown() throws InterruptedException {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    public boolean createDevice(String devEui, String name, String description,
                                String applicationId, String deviceProfileId) {
        Device device = Device.newBuilder()
                .setDevEui(devEui)
                .setName(name)
                .setDescription(description)
                .setApplicationId(applicationId)
                .setDeviceProfileId(deviceProfileId)
                .build();

        CreateDeviceRequest request = CreateDeviceRequest.newBuilder()
                .setDevice(device)
                .build();

        try {
            // 👇 Appliquer un deadline à chaque appel
            blockingStub .withDeadlineAfter(timeoutSeconds, TimeUnit.SECONDS)
                    .create(request);

            System.out.println("Device created successfully in ChirpStack: " + devEui);
            return true;
        } catch (StatusRuntimeException e) {
            System.err.println("Failed to create device: " + e.getStatus());
            System.err.println("Details: " + e.getTrailers());
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error creating device: " + e.getMessage());
            return false;
        }
    }

    public Device getDevice(String devEui) {
        GetDeviceRequest request = GetDeviceRequest.newBuilder()
                .setDevEui(devEui)
                .build();

        try {
            GetDeviceResponse response = blockingStub.get(request);
            Device device = response.getDevice();
            System.out.println("Retrieved device info for: " + devEui);
            return device;
        } catch (StatusRuntimeException e) {
            System.err.println("Failed to get device: " + e.getStatus());
            return null;
        }
    }

    public boolean deleteDevice(String devEui) {
        DeleteDeviceRequest request = DeleteDeviceRequest.newBuilder()
                .setDevEui(devEui)
                .build();

        try {
            blockingStub.delete(request);
            System.out.println("Device deleted successfully from ChirpStack: " + devEui);
            return true;
        } catch (StatusRuntimeException e) {
            System.err.println("Failed to delete device: " + e.getStatus());
            return false;
        }
    }

    public boolean updateDevice(String devEui, String name, String description,
                                String applicationId, String deviceProfileId) {
        Device device = Device.newBuilder()
                .setDevEui(devEui)
                .setName(name)
                .setDescription(description)
                .setApplicationId(applicationId)
                .setDeviceProfileId(deviceProfileId)
                .build();

        UpdateDeviceRequest request = UpdateDeviceRequest.newBuilder()
                .setDevice(device)
                .build();

        try {
            blockingStub.update(request);
            System.out.println("Device updated successfully: " + devEui);
            return true;
        } catch (StatusRuntimeException e) {
            System.err.println("Failed to update device: " + e.getStatus());
            return false;
        }
    }


}