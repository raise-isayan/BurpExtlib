package extend.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Text;

/**
 *
 * @author isayan
 */
public final class ConvertUtil {

    public static String repeat(String str, int n) {
      return String.join("", Collections.nCopies(n, str));
    }    
    
    public static String newLine(String separator, String value, int length) {
        Pattern p = Pattern.compile(String.format("(.{%d})", length));
        StringBuffer sb = new StringBuffer();
        Matcher m = p.matcher(value);
        while (m.find()) {
            m.appendReplacement(sb, m.group(1) + separator);
        }
        m.appendTail(sb);
        return sb.toString();
    }
    
    public static String toBase64Encode(String src, Charset charset) {
        return toBase64Encode(src, charset, true);
    }

    public static String toBase64Encode(String src, Charset charset, boolean padding) {
        if (padding) {
            byte bytes[] = Base64.getEncoder().encode(src.getBytes(charset));
            return Util.getRawStr(bytes);
        } else {
            byte bytes[] = Base64.getEncoder().withoutPadding().encode(src.getBytes(charset));
            return Util.getRawStr(bytes);
        }
    }

    public static String toBase64Encode(String src, String charset)
            throws UnsupportedEncodingException {
        return toBase64Encode(src, charset, true);
    }

    public static String toBase64Encode(String src, String charset, boolean padding)
            throws UnsupportedEncodingException {
        if (padding) {
            byte bytes[] = Base64.getEncoder().encode(src.getBytes(charset));
            return Util.getRawStr(bytes);
        } else {
            byte bytes[] = Base64.getEncoder().withoutPadding().encode(src.getBytes(charset));
            return Util.getRawStr(bytes);
        }
    }

    public static String toBase64Encode(byte[] src, String charset)
            throws UnsupportedEncodingException {
        return toBase64Encode(src, true);
    }

    public static String toBase64Encode(byte[] src, boolean padding) {
        if (padding) {
            byte bytes[] = Base64.getEncoder().encode(src);
            return Util.getRawStr(bytes);
        } else {
            byte bytes[] = Base64.getEncoder().withoutPadding().encode(src);
            return Util.getRawStr(bytes);
        }
    }

    public static String toBase64Decode(String str, Charset charset) {
        byte bytes[] = Base64.getDecoder().decode(str);
        return new String(bytes, charset);
    }

    public static String toBase64Decode(String str, String charset)
            throws UnsupportedEncodingException {
        byte bytes[] = Base64.getDecoder().decode(str);
        return new String(bytes, charset);
    }

    public static byte[] toBase64Decode(String str) {
        return Base64.getDecoder().decode(str);
    }

    public static String toBase64URLSafeEncode(String src, Charset charset) {
        byte bytes[] = Base64.getUrlEncoder().withoutPadding().encode(src.getBytes(charset));
        return Util.getRawStr(bytes);
    }

    public static String toBase64URLSafeEncode(String src, String charset)
            throws UnsupportedEncodingException {
        byte bytes[] = Base64.getUrlEncoder().withoutPadding().encode(src.getBytes(charset));
        return Util.getRawStr(bytes);
    }

    public static String toBase64URLSafeEncode(byte[] src) {
        byte bytes[] = Base64.getUrlEncoder().withoutPadding().encode(src);
        return Util.getRawStr(bytes);
    }

    public static String toBase64URLSafeDecode(String str, Charset charset) {
        byte bytes[] = Base64.getUrlDecoder().decode(str);
        return new String(bytes, charset);
    }

    public static String toBase64URLSafeDecode(String str, String charset)
            throws UnsupportedEncodingException {
        byte bytes[] = Base64.getUrlDecoder().decode(str);
        return new String(bytes, charset);
    }

    public static byte[] toBase64URLSafeDecode(String str) {
        return Base64.getUrlDecoder().decode(str);
    }

    public static String toHexString(byte[] input) {
        StringBuilder digestbuff = new StringBuilder();
        for (int i = 0; i < input.length; i++) {
            String tmp = Integer.toHexString(input[i] & 0xff);
            if (tmp.length() == 1) {
                digestbuff.append('0').append(tmp);
            } else {
                digestbuff.append(tmp);
            }
        }
        return digestbuff.toString();
    }

    public static String toOctString(byte[] input) {
        StringBuilder digestbuff = new StringBuilder();
        for (int i = 0; i < input.length; i++) {
            String tmp = Integer.toOctalString(input[i] & 0xff);
            if (tmp.length() == 1) {
                digestbuff.append('0').append(tmp);
            } else {
                digestbuff.append(tmp);
            }
        }
        return digestbuff.toString();
    }

    public static int toInteger(byte[] input) {
        int value = 0;
        for (int i = 0; i < input.length; i++) {
            value = (value << 8) | (input[i] & 0xff);
        }
        return value;
    }

    public static String toHexString(byte input) {
        StringBuilder digestbuff = new StringBuilder();
        String tmp = Integer.toHexString(input & 0xff);
        if (tmp.length() == 1) {
            digestbuff.append('0').append(tmp);
        } else {
            digestbuff.append(tmp);
        }
        return digestbuff.toString();
    }

    public static String toHexString(int input) {
        StringBuilder digestbuff = new StringBuilder();
        String tmp = Integer.toHexString(input & 0xffffffff);
        // 2で割れる数で0詰め
        if ((tmp.length() % 2) != 0) {
            digestbuff.append('0');
        }
        digestbuff.append(tmp);
        return digestbuff.toString();
    }

    public static String escapeXml(String target) throws Exception {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Text text = document.createTextNode(target);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        DOMSource source = new DOMSource(text);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(source, result);
        return writer.toString();
    }

    public static String escapeJson(String value) {
        return value.replaceAll("([\"\\\\/])", "\\\\$1");        
    }
    
    public static byte[] compressGzip(byte[] content) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(content))) {
            byte[] buf = new byte[1024];
            int size;
            try (OutputStream gos = new GZIPOutputStream(baos)) {
                while ((size = in.read(buf, 0, buf.length)) != -1) {
                    gos.write(buf, 0, size);
                }
                gos.flush();
            }
        }
        return baos.toByteArray();
    }

    public static byte[] compressZlib(byte[] content) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Deflater compresser = new Deflater(Deflater.BEST_COMPRESSION, true);
        try {
            compresser.setInput(content);
            compresser.finish();
            byte[] buf = new byte[1024];
            int count = 0;
            while (!compresser.finished()) {
                count = compresser.deflate(buf);
                bout.write(buf, 0, count);
            }
        } finally {
            compresser.end();
        }
        return bout.toByteArray();
    }

    public static String compressZlibBase64(String content, Charset charset) {
        return toBase64Encode(compressZlib(Util.encodeMessage(content, charset)), true);
    }

    public static String compressZlibBase64(String content) {
        return compressZlibBase64(content, StandardCharsets.ISO_8859_1);
    }

    public static byte[] decompressGzip(byte[] content) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(content))) {
            try (BufferedOutputStream out = new BufferedOutputStream(baos)) {
                byte[] buf = new byte[1024];
                int size;
                while ((size = gis.read(buf, 0, buf.length)) != -1) {
                    out.write(buf, 0, size);
                }
                out.flush();
            }
        }
        return baos.toByteArray();
    }

    public static byte[] decompressZlib(byte[] content) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Inflater decompresser = new Inflater(true);
        try {
            decompresser.setInput(content);
            byte[] buf = new byte[1024];
            int count = 0;
            try {
                while (!decompresser.finished()) {
                    count = decompresser.inflate(buf);
                    if (count <= 0) {
                        break;
                    }
                    bout.write(buf, 0, count);
                }
            } catch (DataFormatException ex) {
            }
        } finally {
            decompresser.end();
        }
        return bout.toByteArray();
    }

    public static String decompressZlibBase64(String content, Charset charset) {
        return Util.decodeMessage(decompressZlib(toBase64Decode(content)), charset);
    }

    public static String decompressZlibBase64(String content) {
        return decompressZlibBase64(content, StandardCharsets.ISO_8859_1);
    }

    /**
     * 正規表現のエンコード(エスケープ)
     *
     * @param value
     * @return エンコードされた値
     */
    public static String regexQuote(String value) {
        return value.replaceAll("([\\.\\\\\\+\\*\\?\\[\\^\\]\\$\\(\\)\\{\\}\\=\\!\\<\\>\\|\\:\\-])", "\\\\$1");
    }

}
