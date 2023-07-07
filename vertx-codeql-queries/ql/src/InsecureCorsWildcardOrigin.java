package org.carlspring.security.vertx.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;

/**
 * @author carlspring
 */
public class InsecureCorsWildcardOrigin
        extends AbstractVerticle
{

    @Override
    public void start()
    {
        // Create a router
        Router router = Router.router(vertx);

        // Allow all origins, headers, and methods (insecure configuration)
        CorsHandler corsHandler = CorsHandler.create()
                                             .addOrigin("*")
                                             .allowedHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS.toString())
                                             .allowedMethod(HttpMethod.GET)
                                             .allowedMethod(HttpMethod.POST);

        // Mount the CORS handler
        router.route().handler(corsHandler);

        // Set up routes
        router.get("/api/data").handler(routingContext -> {
            // Handle the request and send response
            routingContext.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json").end(
                    "{\"message\":\"Hello, CORS!\"}");
        });

        // Start the server
        vertx.createHttpServer().requestHandler(router).listen(8080, ar -> {
            if (ar.succeeded())
            {
                System.out.println("Server started on port 8080");
            }
            else
            {
                System.err.println("Server failed to start: " + ar.cause());
            }
        });
    }

}
