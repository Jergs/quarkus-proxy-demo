package at.yehor.havryliuk.rest.proxy.demo.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class GeneralExceptionMapper implements ExceptionMapper<Throwable> {

    private static final String ERROR_PAGE_PATH = "/html/general_error.html";

    @Override
    public Response toResponse(Throwable exception) {
        log.error("Error has occurred", exception);

        InputStream errorPageStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(ERROR_PAGE_PATH);

        if (errorPageStream == null) {
            return buildResponse("Error page not found", "text/plain; charset=UTF-8");
        }

        String errorPage;
        try (Scanner scanner = new Scanner(errorPageStream, StandardCharsets.UTF_8)) {
            errorPage = scanner.useDelimiter("\\A").next();
        }

        return buildResponse(errorPage, "text/html; charset=UTF-8");
    }

    private Response buildResponse(Object entity, String contentType) {
        return Response.serverError()
                .entity(entity)
                .header("Content-Type", contentType)
                .build();
    }
}
