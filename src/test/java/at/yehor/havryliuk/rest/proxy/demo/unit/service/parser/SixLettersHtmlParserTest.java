package at.yehor.havryliuk.rest.proxy.demo.unit.service.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import at.yehor.havryliuk.rest.proxy.demo.service.parser.SixLettersHtmlParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class SixLettersHtmlParserTest {

    private final SixLettersHtmlParser parser = new SixLettersHtmlParser();

    @ParameterizedTest
    @NullAndEmptySource
    void modifyHtmlDoc_shouldThrowException_whenHtmlContentIsNullOrEmpty(String htmlContent) {
        assertThrows(IllegalArgumentException.class, () -> parser.modifyHtmlDoc(htmlContent));
    }

    @Test
    void modifyHtmlDoc_shouldReturnModifiedContent_whenHtmlContentIsValid() {
        // given
        String content = "<div>Test case sentence to check if the six letter words are changed</div>";

        // when
        String actual = parser.modifyHtmlDoc(content);

        // then
        assertTrue(actual.contains("Test case sentence to check if the six letter™ words are changed"));
    }
}