package compteur.config;

import io.grpc.*;

public class AuthInterceptor implements ClientInterceptor {
    private final String apiToken;

    public AuthInterceptor(String apiToken) {
        this.apiToken = apiToken;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
                next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                headers.put(Metadata.Key.of("authorization",
                        Metadata.ASCII_STRING_MARSHALLER), "Bearer " + apiToken);
                super.start(responseListener, headers);
            }
        };
    }
}
