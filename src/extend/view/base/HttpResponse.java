/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extend.view.base;

import extend.util.HttpUtil;
import extend.util.Util;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author isayan
 */
public class HttpResponse extends HttpMessage implements HttpStatusLine {
    private final static Pattern RES_STATUS = Pattern.compile("^(.*?)\\s+(\\d+)\\s+(.*?)$", Pattern.MULTILINE);
    private HttpStatusLine statusLine;

    protected HttpResponse() {
        super();
    }
    
    public HttpResponse(HttpMessage message) throws ParseException {
        super(message);
        this.parseHeader();
    }

    protected void parseHeader() throws ParseException {
        this.statusLine = parseHttpStatusLine(this.getHeader());
    }

    @Override
    public String getStatusLine() {
        return this.statusLine.getStatusLine();
    }
        
    @Override
    public String getProtocolVersion() {
        return this.statusLine.getProtocolVersion();
    }

    @Override
    public short getStatusCode() {
        return this.statusLine.getStatusCode();
    }

    @Override
    public String getReasonPhrase() {
        return this.statusLine.getReasonPhrase();
    }
    private final Pattern RESMETA_SET = Pattern.compile("<meta (?:.*?)charset=[\"\']?([\\w_-]+)[\"\']?\\W+", Pattern.CASE_INSENSITIVE);
    //<META http-equiv="Content-Type" content="text/html; charset=Shift_JIS">

    @Override
    public String getGuessCharset() {
        String charset = super.getGuessCharset();
        if (charset == null) {
            Matcher m2 = RESMETA_SET.matcher(this.getBody());
            if (m2.find()) {
                charset = m2.group(1);
            }
        }
        if (charset == null) {
            charset = HttpUtil.getGuessCode(this.getBodyBytes());
        }
        return normalizeCharset(charset);
    }

    public static HttpStatusLine parseHttpStatusLine(byte[] message) throws ParseException {
        String request = Util.decodeMessage(message);
        return parseHttpStatusLine(request);
    }
    
    public static HttpStatusLine parseHttpStatusLine(String message) throws ParseException {
        final Matcher statusLine = RES_STATUS.matcher(message);
        if (!statusLine.find()) {
            throw new ParseException("Illegal Response Format:" + message, 1);
        }
        return new HttpStatusLine() {

            @Override
            public String getStatusLine() {
                return statusLine.group(0);
            }

            @Override
            public String getProtocolVersion() {
                return statusLine.group(1);
            }

            @Override
            public short getStatusCode() {
                return (short)Util.parseIntDefault(statusLine.group(2), 0);
            }

            @Override
            public String getReasonPhrase() {
                return statusLine.group(3);
            }

        };
    }
    
    public static HttpResponse parseHttpResponse(byte[] message) throws ParseException {
        return new HttpResponse(parseHttpMessage(Util.decodeMessage(message)));
    }

    public static HttpResponse parseHttpResponse(String message) throws ParseException {
        return new HttpResponse(parseHttpMessage(message));
    }


}
