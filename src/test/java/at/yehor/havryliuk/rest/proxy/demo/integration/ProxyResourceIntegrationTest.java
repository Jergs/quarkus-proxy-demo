package at.yehor.havryliuk.rest.proxy.demo.integration;

import static at.yehor.havryliuk.rest.proxy.demo.config.Constants.HTML_CONTENT_TYPE;
import static at.yehor.havryliuk.rest.proxy.demo.util.TestData.getModifiedTestHtml;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import at.yehor.havryliuk.rest.proxy.demo.model.RemoteResponseWrapper;
import at.yehor.havryliuk.rest.proxy.demo.service.RemoteConnectorService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@QuarkusTest
class ProxyResourceIntegrationTest {

    @InjectMock
    RemoteConnectorService remoteConnectorService;

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("getUrlArguments")
    void proxyMain_shouldReturn200_whenParamsAreValid(String ulr) {
        String html = "<div>String</div>";
        var remoteResponse = new RemoteResponseWrapper(html.getBytes(StandardCharsets.UTF_8), HTML_CONTENT_TYPE);
        when(remoteConnectorService.fetchRemoteByteArrayResponse(any())).thenReturn(remoteResponse);

        var actualResponse = RestAssured.given()
                .when().get(ulr)
                .then()
                .statusCode(200)
                .extract().asString();

        assertEquals(getModifiedTestHtml(), actualResponse);
    }

    private static Stream<Arguments> getUrlArguments() {
        return Stream.of(
                Arguments.of("/"),
                Arguments.of("/test"),
                Arguments.of("/test/application.js")
        );
    }

}
