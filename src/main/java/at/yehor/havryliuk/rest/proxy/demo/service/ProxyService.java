package at.yehor.havryliuk.rest.proxy.demo.service;

import static at.yehor.havryliuk.rest.proxy.demo.config.Constants.CONTENT_TYPE;
import static at.yehor.havryliuk.rest.proxy.demo.config.Constants.HTML_CONTENT_TYPE;
import static at.yehor.havryliuk.rest.proxy.demo.config.Constants.JSON_CONTENT_TYPE;

import at.yehor.havryliuk.rest.proxy.demo.annotation.InvocationLog;
import at.yehor.havryliuk.rest.proxy.demo.config.HttpConfig;
import at.yehor.havryliuk.rest.proxy.demo.config.RemoteConfig;
import at.yehor.havryliuk.rest.proxy.demo.model.RemoteResponseWrapper;
import at.yehor.havryliuk.rest.proxy.demo.service.parser.HtmlParser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class ProxyService {

    private static final String HTTP_PROTOCOL_REGEX = "(http://)*(https://)*(w{3}[.])*";

    private final HttpConfig httpConfig;
    private final RemoteConfig remoteConfig;
    private final HtmlParser htmlParser;
    private final RemoteConnectorService remoteConnectorService;

    @InvocationLog
    public Response modifyOriginalContent(String resourcePath) throws IOException, URISyntaxException {
        RemoteResponseWrapper responseWrapper = remoteConnectorService.fetchRemoteByteArrayResponse(resourcePath);
        String contentType = responseWrapper.getContentType();
        byte[] contentBytes = responseWrapper.getContent();

        if (contentType.contains(HTML_CONTENT_TYPE) || contentType.contains(JSON_CONTENT_TYPE)) {
            String modifiedContent = modifyBaseContent(contentBytes, contentType);
            return buildResponse(modifiedContent, contentType);
        }

        return buildResponse(contentBytes, contentType);
    }

    private String modifyBaseContent(byte[] contentBytes, String contentType) {
        String content = new String(contentBytes, StandardCharsets.UTF_8);
        String modifiedContent = replaceAllBaseUrl(content);

        if (contentType.contains(HTML_CONTENT_TYPE)) {
            modifiedContent = htmlParser.modifyHtmlDoc(modifiedContent);
        }

        return modifiedContent;
    }

    private String replaceAllBaseUrl(String content) {
        String httpProtocol = httpConfig.sslPort() == -1 ? "http" : "https";
        String replacement = httpProtocol + "://" + httpConfig.host() + ":" + httpConfig.port();
        return content.replaceAll(HTTP_PROTOCOL_REGEX + remoteConfig.baseUri(), replacement);
    }

    private Response buildResponse(Object entity, String contentType) {
        return Response.ok(entity)
                .header(CONTENT_TYPE, contentType)
                .build();
    }

}
