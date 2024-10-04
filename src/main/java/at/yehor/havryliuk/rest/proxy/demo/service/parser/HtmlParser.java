package at.yehor.havryliuk.rest.proxy.demo.service.parser;

import org.jsoup.nodes.Element;

public interface HtmlParser {

    String modifyHtmlDoc(String htmlDoc);

    void appendTextToWordsMatchingRegex(Element element, String textToAppend, String regex);

}
