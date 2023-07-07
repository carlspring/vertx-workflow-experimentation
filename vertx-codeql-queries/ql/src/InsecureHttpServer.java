package org.carlspring.security.vertx.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;

/**
 * @author carlspring
 */
public class InsecureHttpServer
        extends AbstractVerticle
{

    @Override
    public void start()
    {
        // Create an insecure HTTP server
        HttpServer server = vertx.createHttpServer();

        // Configure server settings
        server.requestHandler(request -> {
            request.response().end("Hello, World!");
        });

        // Start the server
        server.listen(8080);
    }

}

