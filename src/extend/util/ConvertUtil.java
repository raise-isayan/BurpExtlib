package extend.util;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
//import javax.xml.bind.DatatypeConverter;
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
    
    public static String toBase64Encode(String src, String enc)
            throws UnsupportedEncodingException {
        byte bytes [] = Base64.getEncoder().encode(src.getBytes(enc));
        return Util.getRawStr(bytes);
        //return DatatypeConverter.printBase64Binary(str.getBytes(enc));
    }

    public static String toBase64Encode(byte [] src) {
        byte bytes [] = Base64.getEncoder().encode(src);
        return Util.getRawStr(bytes);
    }
    
    public static String toBase64Decode(String str, String enc)
            throws UnsupportedEncodingException {
        byte bytes [] = Base64.getDecoder().decode(str);
        return new String(bytes, enc);
    }

    public static byte[] toBase64Decode(String str) {
        return Base64.getDecoder().decode(str);
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

    public static byte [] compressZlib(byte [] content) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Deflater compresser = new Deflater();
        try {
            compresser.setLevel(Deflater.BEST_COMPRESSION);
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
    
    public static String compressZlibBase64(String content) {
        return toBase64Encode(compressZlib(Util.getRawByte(content)));
        //return DatatypeConverter.printBase64Binary(compressZlib(Util.getRawByte(content)));
    }

    public static byte [] decompressZlib(byte [] content) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Inflater decompresser = new Inflater();
        try {
            decompresser.setInput(content);
            byte[] buf = new byte[1024];
            int count = 0;
            try {
                while (!decompresser.finished()) {
                    count = decompresser.inflate(buf);
                    if (count <= 0) break;
                    bout.write(buf, 0, count);
                }
            } catch (DataFormatException ex) {
            }        
        }
        finally {
            decompresser.end();
        }
        return bout.toByteArray();
    }
    
    
    public static String decompressZlibBase64(String content) {
        return Util.getRawStr(decompressZlib(toBase64Decode(content)));
//        return Util.getRawStr(decompressZlib(DatatypeConverter.parseBase64Binary(content)));
    }

    /**
     * 正規表現のエンコード(エスケープ)
     *
     * @param value
     * @return エンコードされた値
     */
    public static String regexQuote(String value) {
        return value.replaceAll("([\\\\(){}\\]\\[.])", "\\\\$1");
    }
    
}
