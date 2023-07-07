package org.carlspring.security.vertx.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;

/**
 * @author carlspring
 */
public class SecureCorsServer extends AbstractVerticle {

    @Override
    public void start() {
        // Create a router
        Router router = Router.router(vertx);

        // Configure CORS handling with allowed origins, headers, and methods
        CorsHandler corsHandler = CorsHandler.create()
                                             // 1) Use HTTPS
                                             // 2) Use an explicitly defined origin
                                             .addOrigin("https://example.com")
                                             // 3) Define allowed headers
                                             .allowedHeader(HttpHeaders.CONTENT_TYPE.toString())
                                             // 4) Define allowed methods
                                             .allowedMethod(HttpMethod.GET)
                                             .allowedMethod(HttpMethod.POST);

        // Mount the CORS handler
        router.route().handler(corsHandler);

        // Set up routes
        router.get("/api/data").handler(routingContext -> {
            // Handle the request and send response
            routingContext.response()
                          .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                          .end("{\"message\":\"Hello, CORS!\"}");
        });

        // Set up SSL
        HttpServerOptions options = new HttpServerOptions()
                                            // Set up SSL
                                            .setSsl(true)
                                            // Set up keystore
                                            .setKeyStoreOptions(new JksOptions().setPath("keystore.jks")
                                                                                .setPassword("keystore_password"));

        // Start the server
        vertx.createHttpServer(options)
             .requestHandler(router)
             .listen(8080, ar -> {
                 if (ar.succeeded()) {
                     System.out.println("Server started on port 8080");
                 } else {
                     System.err.println("Server failed to start: " + ar.cause());
                 }
             });
    }

}
