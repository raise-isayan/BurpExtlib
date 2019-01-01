package extend.util;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import javax.xml.bind.DatatypeConverter;
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

    public static String toBase64Encode(String str, String enc)
            throws UnsupportedEncodingException {
        return DatatypeConverter.printBase64Binary(str.getBytes(enc));
    }
    
    public static String toBase64Decode(String str, String enc)
            throws UnsupportedEncodingException {
        byte bytes [] = DatatypeConverter.parseBase64Binary(str);
        return new String(bytes, enc);
    }
    
    public static String toHexBinaryEncode(byte [] bytes)
    {
        return DatatypeConverter.printBase64Binary(bytes);
    }

    public static byte [] toHexBinaryDecode(String str)
    {
        return DatatypeConverter.parseHexBinary(str);
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
        return DatatypeConverter.printBase64Binary(compressZlib(Util.getRawByte(content)));
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
        return Util.getRawStr(decompressZlib(DatatypeConverter.parseBase64Binary(content)));
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
