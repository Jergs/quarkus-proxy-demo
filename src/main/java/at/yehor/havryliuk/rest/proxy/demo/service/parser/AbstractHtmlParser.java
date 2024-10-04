package at.yehor.havryliuk.rest.proxy.demo.service.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Element;

public abstract class AbstractHtmlParser implements HtmlParser {

    @Override
    public void appendTextToWordsMatchingRegex(Element element, String textToAppend, String regex) {
        Matcher matcher = getElementRegexMatcher(element, regex);
        StringBuilder newSentence = new StringBuilder();

        while (matcher.find()) {
            String word = matcher.group();
            matcher.appendReplacement(newSentence, word + textToAppend);
        }

        matcher.appendTail(newSentence);
        element.text(newSentence.toString());
    }

    private Matcher getElementRegexMatcher(Element element, String regex) {
        String text = element.ownText();
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(text);
    }

}
