package com.nikolay.client.handler;

import com.nikolay.client.exception.ServerDataAccessException;
import java.io.IOException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * The type Custom response error handler.
 */
public class CustomResponseErrorHandler implements ResponseErrorHandler {

    private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

    public boolean hasError(ClientHttpResponse response) throws IOException {
        return errorHandler.hasError(response);
    }

    public void handleError(ClientHttpResponse response) throws IOException {
        throw new ServerDataAccessException(
                response.getStatusCode() + ": " + response.getStatusText() + ": " + response
                        .getBody());
    }
}
