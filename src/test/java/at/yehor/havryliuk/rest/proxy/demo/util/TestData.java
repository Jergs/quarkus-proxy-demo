package at.yehor.havryliuk.rest.proxy.demo.util;

public class TestData {

    public static final String REMOTE_LINK = "https://www.obiwan.kenobi.com";

    public static String getModifiedTestHtml() {
        return """
                <html>
                 <head></head>
                 <body>
                  <div>
                   String™
                  </div>
                 </body>
                </html>""";
    }

}
