package extend.util;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author isayan
 */
public class ConvertUtilTest {
    
    public ConvertUtilTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of toBase64Encode method, of class ConvertUtil.
     */
    @Test
    public void testToBase64Encode() throws Exception {
   }

    /**
     * Test of toBase64Decode method, of class ConvertUtil.
     */
    @Test
    public void testToBase64Decode() throws Exception {
    }

    /**
     * Test of toHexBinaryEncode method, of class ConvertUtil.
     */
    @Test
    public void testToHexBinaryEncode() {
    }

    /**
     * Test of toHexBinaryDecode method, of class ConvertUtil.
     */
    @Test
    public void testToHexBinaryDecode() {
    }

    /**
     * Test of escapeXml method, of class ConvertUtil.
     */
    @Test
    public void testEscapeXml() throws Exception {
        System.out.println("escapeXml");
        String target = "<s a=\"x\">&";
        String expResult = "&lt;s a=\"x\"&gt;&amp;";
        String result = ConvertUtil.escapeXml(target);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRegexQuote() {
        System.out.println("regexQuote");
        {
            String target = "aa.com";
            String expResult = "aa\\.com";
            String result = ConvertUtil.regexQuote(target);
            assertEquals(expResult, result);
        }
        {
            String target = "\\/{}<>[]()";
            String expResult = "\\\\/\\{\\}<>\\[\\]\\(\\)";
            String result = ConvertUtil.regexQuote(target);
            assertEquals(expResult, result);
        
        }
    
    }

    /**
     * Test of toInteger method, of class TransUtil.
     */
    @Test
    public void testToInteger() {
        System.out.println("toInteger");
        assertEquals(0x7fff, ConvertUtil.toInteger(new byte[]{(byte) 0x7f, (byte) 0xff}));
        assertEquals(0xff7f, ConvertUtil.toInteger(new byte[]{(byte) 0xff, (byte) 0x7f}));
        assertEquals(0x8080, ConvertUtil.toInteger(new byte[]{(byte) 0x80, (byte) 0x80}));
    }

    /**
     * Test of toBASE64Encoder method, of class TransUtil.
     */
    @Test
    public void testToBASE64Encoder() {
        try {
            System.out.println("toBASE64Encoder");
            assertEquals("PA==", ConvertUtil.toBase64Encode("<", "8859_1", true));
            assertEquals("dGVzdA==", ConvertUtil.toBase64Encode("test", "8859_1", true));
            assertEquals("ZnVnYWY=", ConvertUtil.toBase64Encode("fugaf", "8859_1", true));
            assertEquals("aG9nZWhv", ConvertUtil.toBase64Encode("hogeho", "8859_1", true));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ConvertUtilTest.class.getName()).log(Level.SEVERE, null, ex);
            assertTrue(false);
        }
    }

    /**
     * Test of toBASE64Decode method, of class TransUtil.
     */
    @Test
    public void testToBASE64Decoder() {
        try {
            System.out.println("toBASE64Decoder");
            assertEquals("<", ConvertUtil.toBase64Decode("PA==", "8859_1"));
            assertEquals("hogeho", ConvertUtil.toBase64Decode("aG9nZWhv", "8859_1"));
            assertEquals("fugaf", ConvertUtil.toBase64Decode("ZnVnYWY=", "8859_1"));
            assertEquals("test", ConvertUtil.toBase64Decode("dGVzdA==", "8859_1"));
            System.out.println(ConvertUtil.toBase64Decode("absdadbd", "8859_1"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ConvertUtilTest.class.getName()).log(Level.SEVERE, null, ex);
            assertTrue(false);
        }
    }

    
}
