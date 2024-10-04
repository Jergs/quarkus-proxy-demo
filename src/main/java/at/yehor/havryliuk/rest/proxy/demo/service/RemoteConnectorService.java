package at.yehor.havryliuk.rest.proxy.demo.service;

import static at.yehor.havryliuk.rest.proxy.demo.config.Constants.CONTENT_TYPE;

import at.yehor.havryliuk.rest.proxy.demo.config.RemoteConfig;
import at.yehor.havryliuk.rest.proxy.demo.exception.InternalViolationException;
import at.yehor.havryliuk.rest.proxy.demo.model.RemoteResponseWrapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class RemoteConnectorService {

    @Inject
    RemoteConfig remoteConfig;

    public RemoteResponseWrapper fetchRemoteByteArrayResponse(String resourcePath)
            throws IOException, URISyntaxException {
        HttpURLConnection connection = getRemoteConnection(resourcePath);

        try (InputStream inputStream = connection.getInputStream()) {
            String contentType = connection.getHeaderField(CONTENT_TYPE);
            byte[] contentBytes = inputStream.readAllBytes();

            return new RemoteResponseWrapper(contentBytes, contentType);
        } catch (IOException e) {
            log.error("Could not fetch content from the path: {}", resourcePath, e);
            throw new InternalViolationException("Could not read the connection input stream from the path: %s/"
                    .formatted(resourcePath));
        } finally {
            connection.disconnect();
        }
    }

    private HttpURLConnection getRemoteConnection(String resourcePath) throws IOException, URISyntaxException {
        URI targetUrl = new URI("https://" + remoteConfig.baseUri() + "/" + resourcePath);
        HttpURLConnection connection = (HttpURLConnection) targetUrl.toURL().openConnection();

        connection.setRequestMethod("GET");
        connection.setConnectTimeout(remoteConfig.connectionTimeout());
        connection.setReadTimeout(remoteConfig.readTimeout());

        return connection;
    }

}
