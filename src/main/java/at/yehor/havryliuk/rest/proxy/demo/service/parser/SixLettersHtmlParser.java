package at.yehor.havryliuk.rest.proxy.demo.service.parser;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@Slf4j
@ApplicationScoped
public class SixLettersHtmlParser extends AbstractHtmlParser {

    private static final String SIX_LETTER_WORD_REGEX = "\\b[a-zA-Z]{6}\\b";

    @Override
    public String modifyHtmlDoc(String html) {
        if (html == null || html.isBlank()) {
            throw new IllegalArgumentException("html content is null or empty");
        }

        Document doc = Jsoup.parse(html);
        Elements elementsWithText = doc.getElementsMatchingOwnText(SIX_LETTER_WORD_REGEX);
        elementsWithText.forEach(el -> appendTextToWordsMatchingRegex(el, "™", SIX_LETTER_WORD_REGEX));

        return doc.html();
    }

}
