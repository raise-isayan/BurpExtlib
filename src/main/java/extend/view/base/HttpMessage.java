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
public class HttpMessage {

    public final static String LINE_TERMINATE = "\r\n";
    private final Pattern CONTENT_LENGTH = Pattern.compile("^(Content-Length:\\s*)(\\d+)$", Pattern.MULTILINE);
    private final Pattern CONTENT_TYPE = Pattern.compile("^Content-Type:\\s*([^;]+)(?:;\\s*charset=[\"\']?([\\w_-]+)[\"\']?)?\\s*$", Pattern.MULTILINE);

    private String header = "";
    private String body = "";

    public HttpMessage() {
    }

    public HttpMessage(HttpMessage message) {
        this.header = message.header;
        this.body = message.body;
    }

    /**
     * @return the header
     */
    public String getHeader() {
        return this.header;
    }

    /**
     * @return the headers
     */
    public String[] getHeaders() {
        return this.header.split(LINE_TERMINATE);
    }

    /**
     * @return the header
     */
    public byte[] getHeaderBytes() {
        return Util.encodeMessage(this.header);
    }

    /**
     * @param header the header to set
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * @param name
     * @return the header
     */
    public String getHeader(String name) {
        return HttpUtil.getHeader(name, this.getHeaders());
    }

    /**
     * @return the body
     */
    public String getBody() {
        return this.body;
    }

    /**
     * @return the body
     */
    public byte[] getBodyBytes() {
        return Util.encodeMessage(this.body);
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    public byte[] getMessageBytes() {
        return Util.encodeMessage(getMessage());
    }

    public byte[] getMessageBytes(String enc) {
        return Util.encodeMessage(getMessage(), enc);
    }

    public String getMessage() {
        StringBuilder messageBuff = new StringBuilder();
        messageBuff.append(this.header);
        messageBuff.append(HttpMessage.LINE_TERMINATE);
        messageBuff.append(HttpMessage.LINE_TERMINATE);
        if (this.body.length() > 0) {
            messageBuff.append(this.body);
        }
        return messageBuff.toString();
    }

    public void setMessage(String message) throws ParseException {
        HttpMessage httpMsg = parseHttpMessage(message);
        this.setHeader(httpMsg.getHeader());
        this.setBody(httpMsg.getBody());
    }

    public boolean isBody() {
        return (this.body.length() > 0);
    }

    public static HttpMessage parseHttpMessage(byte[] message) {
        return parseHttpMessage(Util.decodeMessage(message));
    }

    public static HttpMessage parseHttpMessage(String message) {
        String[] splitMessage = message.split("(\r\n){2}", 2);
        HttpMessage httpMsg = new HttpMessage();
        httpMsg.setHeader(splitMessage[0]);
        if (splitMessage.length > 1) {
            httpMsg.setBody(splitMessage[1]);
        }
        return httpMsg;
    }

    public int getContentLength() {
        int contentlen = -1;
        Matcher m = CONTENT_LENGTH.matcher(this.getHeader());
        if (m.find()) {
            contentlen = Util.parseIntDefault(m.group(2), 0);
        }
        return contentlen;
    }

    public void updateContentLength(boolean updateLength) {
        if (!updateLength) {
            return;
        }
        Matcher m = CONTENT_LENGTH.matcher(this.getHeader());
        StringBuffer buff = new StringBuffer();
        if (m.find()) {
            StringBuilder replace = new StringBuilder();
            if (this.getBody().length() > 0) {
                replace.append(m.group(1));
                replace.append(this.getBody().length());
            }
            m.appendReplacement(buff, replace.toString());
            m.appendTail(buff);
        } else {
            buff.append(this.getHeader());
            if (this.getBody().length() > 0) {
                buff.append(LINE_TERMINATE);
                buff.append("Content-Length: ");
                buff.append(this.getBody().length());
            }
        }
        this.setHeader(buff.toString());
    }

    public boolean isContentMimeType(String mime) {
        String mimeType = this.getContentMimeType();
        if (mimeType != null) {
            return mimeType.contains(mime);
        } else {
            return false;
        }
    }

    public String getContentMimeType() {
        String mimeType = null;
        Matcher m = CONTENT_TYPE.matcher(this.getHeader());
        if (m.find()) {
            mimeType = m.group(1);
        }
        return mimeType;
    }

    public String getGuessCharset() {
        String charset = null;
        Matcher m = CONTENT_TYPE.matcher(this.getHeader());
        if (m.find()) {
            charset = m.group(2);
        }
        return normalizeCharset(charset);
    }

    protected static String normalizeCharset(String charsetName) {
        return HttpUtil.normalizeCharset(charsetName);
    }

    public static byte[] buildHttpMessage(byte[] headers, byte[] body) {
        StringBuilder messageBuff = new StringBuilder();
        messageBuff.append(Util.getRawStr(headers));
        messageBuff.append(HttpMessage.LINE_TERMINATE);
        messageBuff.append(HttpMessage.LINE_TERMINATE);
        if (body.length > 0) {
            messageBuff.append(Util.getRawStr(body));
        }
        return Util.getRawByte(messageBuff.toString());
    }

    public static byte[][] splitHttpMessage(byte[] messageByte) {
        String message = Util.getRawStr(messageByte);
        String[] splitMessage = message.split("(\r\n){2}", 2);
        if (splitMessage.length == 1) {
            return new byte[][]{Util.getRawByte(splitMessage[0]), new byte[]{}};
        } else {
            return new byte[][]{Util.getRawByte(splitMessage[0]), Util.getRawByte(splitMessage[1])};
        }
    }

}
