package at.yehor.havryliuk.rest.proxy.demo.unit.service;

import static at.yehor.havryliuk.rest.proxy.demo.config.Constants.CONTENT_TYPE;
import static at.yehor.havryliuk.rest.proxy.demo.config.Constants.HTML_CONTENT_TYPE;
import static at.yehor.havryliuk.rest.proxy.demo.config.Constants.JSON_CONTENT_TYPE;
import static at.yehor.havryliuk.rest.proxy.demo.util.TestData.REMOTE_LINK;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.yehor.havryliuk.rest.proxy.demo.config.HttpConfig;
import at.yehor.havryliuk.rest.proxy.demo.config.RemoteConfig;
import at.yehor.havryliuk.rest.proxy.demo.model.RemoteResponseWrapper;
import at.yehor.havryliuk.rest.proxy.demo.service.ProxyService;
import at.yehor.havryliuk.rest.proxy.demo.service.RemoteConnectorService;
import at.yehor.havryliuk.rest.proxy.demo.service.parser.HtmlParser;
import jakarta.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProxyServiceTest {

    @Mock
    private HttpConfig httpConfig;
    @Mock
    private RemoteConfig remoteConfig;
    @Mock
    private HtmlParser htmlParser;
    @Mock
    private RemoteConnectorService remoteConnectorService;

    @InjectMocks
    private ProxyService proxyService;

    @Test
    @SneakyThrows
    void modifyOriginalContent_shouldReturnModifiedContent_whenInvoked() {
        // given
        String html = "<div>String</div>";
        String modifiedHtml = "<div>String™</div>";
        var remoteResponse = new RemoteResponseWrapper(html.getBytes(StandardCharsets.UTF_8), HTML_CONTENT_TYPE);

        when(remoteConnectorService.fetchRemoteByteArrayResponse(any())).thenReturn(remoteResponse);
        when(htmlParser.modifyHtmlDoc(any())).thenReturn(modifiedHtml);
        initHttpConfigs();

        // when
        Response actual = assertDoesNotThrow(() -> proxyService.modifyOriginalContent("test"));

        // then
        assertEquals(HTML_CONTENT_TYPE, actual.getHeaderString(CONTENT_TYPE));
        assertEquals(200, actual.getStatus());
        assertEquals(modifiedHtml, actual.getEntity());
    }

    @Test
    @SneakyThrows
    void modifyOriginalContent_shouldReturnUnmodifiedResource_whenNotHtmlOrJson() {
        // given
        String plainText = "testString";
        var remoteResponse = new RemoteResponseWrapper(plainText.getBytes(StandardCharsets.UTF_8), "text/plain");
        when(remoteConnectorService.fetchRemoteByteArrayResponse(any())).thenReturn(remoteResponse);

        // when
        Response actual = assertDoesNotThrow(() -> proxyService.modifyOriginalContent("test"));

        // then
        assertEquals("text/plain", actual.getHeaderString(CONTENT_TYPE));
        assertEquals(200, actual.getStatus());
        assertEquals(remoteResponse.getContent(), actual.getEntity());
        verify(htmlParser, never()).modifyHtmlDoc(any());
        verify(httpConfig, never()).host();
    }

    @Test
    @SneakyThrows
    void modifyOriginalContent_shouldReturnUpdatedResourceWithLinks_whenJson() {
        // given
        String json = "{link: " + REMOTE_LINK + "}";
        var remoteResponse = new RemoteResponseWrapper(json.getBytes(StandardCharsets.UTF_8), JSON_CONTENT_TYPE);

        when(remoteConnectorService.fetchRemoteByteArrayResponse(any())).thenReturn(remoteResponse);
        initHttpConfigs();

        // when
        Response actual = assertDoesNotThrow(() -> proxyService.modifyOriginalContent("test"));

        // then
        assertEquals(JSON_CONTENT_TYPE, actual.getHeaderString(CONTENT_TYPE));
        assertEquals(200, actual.getStatus());
        assertEquals("{link: http://localhost:8080}", actual.getEntity());
        verify(htmlParser, never()).modifyHtmlDoc(any());
    }

    private void initHttpConfigs() {
        when(remoteConfig.baseUri()).thenReturn(REMOTE_LINK);

        when(httpConfig.sslPort()).thenReturn(-1);
        when(httpConfig.host()).thenReturn("localhost");
        when(httpConfig.port()).thenReturn(8080);
    }
}
