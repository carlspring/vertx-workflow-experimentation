package org.carlspring.security.vertx.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.net.JksOptions;

/**
 * @author carlspring
 */
public class SecureHttpServer extends AbstractVerticle {

    @Override
    public void start() {
        HttpServerOptions options = new HttpServerOptions()
                                            // Set up SSL
                                            .setSsl(true)
                                            // Set up keystore
                                            .setKeyStoreOptions(new JksOptions().setPath("keystore.jks")
                                                                                .setPassword("keystore_password"));

        HttpServer server = vertx.createHttpServer(options);

        server.requestHandler(request -> {
            HttpServerResponse response = request.response();
            response.putHeader("Content-Type", "text/plain");
            response.end("Hello, World! This is a secure connection.");
        });

        server.listen(8443, "localhost", result -> {
            if (result.succeeded()) {
                System.out.println("Server started on port 8443 with SSL/TLS");
            } else {
                System.err.println("Server failed to start: " + result.cause());
            }
        });
    }

}
