package extend.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
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
public class HttpUtilTest {
    
    public HttpUtilTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of startsWithHttp method, of class HttpUtil.
     */
    @Test
    public void testStartsWithHttp() {
        System.out.println("startsWithHttp");
        assertEquals(true, HttpUtil.startsWithHttp("http://"));
        assertEquals(true, HttpUtil.startsWithHttp("https://"));
        assertEquals(false, HttpUtil.startsWithHttp("shttp://"));
        assertEquals(false, HttpUtil.startsWithHttp("httpx://"));
    }

    /**
     * Test of normalizeURL method, of class HttpUtil.
     */
    @Test
    public void testNormalizeURL() {
        System.out.println("normalizeURL");
        assertEquals("http://example.com/", HttpUtil.normalizeURL("http://example.com:80/"));
        assertEquals("http://example.com:8080/", HttpUtil.normalizeURL("http://example.com:8080/"));
        assertEquals("https://example.com:4438/", HttpUtil.normalizeURL("https://example.com:4438/"));
        assertEquals("https://example.com/", HttpUtil.normalizeURL("https://example.com:443/"));
        assertEquals("http://example.com/xxx", HttpUtil.normalizeURL("http://example.com:80/xxx"));
        assertEquals("https://example.com/xxx", HttpUtil.normalizeURL("https://example.com:443/xxx"));
        assertEquals("https://example.com:8443/xxx", HttpUtil.normalizeURL("https://example.com:8443/xxx"));
        assertEquals("https://example.com/xxx", HttpUtil.normalizeURL("https://example.com:443/xxx"));
    }

    /**
     * Test of testBaseName method, of class HttpUtil.
     */
    @Test
    public void testBaseName() {
        System.out.println("testBaseName");
        try {
            assertEquals("path.exe", HttpUtil.getBaseName(new URL("http://example.com/dir/file/path.exe?1328319481")));
            assertEquals("file", HttpUtil.getBaseName(new URL("http://example.com/dir/file/?1328319481")));
            assertEquals("dir", HttpUtil.getBaseName(new URL("http://example.com/dir?1328319481")));
            assertEquals("example.com", HttpUtil.getBaseName(new URL("http://example.com/?1328319481")));
        } catch (MalformedURLException ex) {
            Logger.getLogger(HttpUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
    
    /**
     * Test of buildGetRequestByte method, of class HttpUtil.
     */
    @Test
   public void testBuildGetRequestByte() {
       System.out.println("testBuildGetRequestByte");
       assertEquals("GET / HTTP/1.1\r\nHost: example.com\r\n", (Util.decodeMessage(HttpUtil.buildGetRequestByte("http://example.com/"))));   
       assertEquals("GET / HTTP/1.1\r\nHost: example.com:443\r\n", (Util.decodeMessage(HttpUtil.buildGetRequestByte("http://example.com:443/"))));   
       assertEquals("GET /?xxx=yyy&ccc=ddd HTTP/1.1\r\nHost: example.com:8443\r\n", (Util.decodeMessage(HttpUtil.buildGetRequestByte("http://example.com:8443/?xxx=yyy&ccc=ddd"))));   
       assertEquals("GET /?abc=123 HTTP/1.1\r\nHost: example.com\r\n", (Util.decodeMessage(HttpUtil.buildGetRequestByte("http://example.com/?abc=123"))));   
       assertEquals("GET /dir/file/?abc=123 HTTP/1.1\r\nHost: example.com\r\n", (Util.decodeMessage(HttpUtil.buildGetRequestByte("http://example.com/dir/file/?abc=123"))));   
       assertEquals("GET /dir/file?abc=123 HTTP/1.1\r\nHost: example.com\r\n", (Util.decodeMessage(HttpUtil.buildGetRequestByte("http://example.com/dir/file?abc=123"))));   
   }

    /**
     * Test of testGetParameter method, of class HttpUtil.
     */
    @Test
    public void testGetParameter() {
       System.out.println("testGetParameter");
       {
            Map.Entry keyval = HttpUtil.getParameter("abc");
            assertEquals("abc", keyval.getKey());               
            assertEquals("", keyval.getValue());               
       }
       {
            Map.Entry keyval = HttpUtil.getParameter("abc=edf");
            assertEquals("abc", keyval.getKey());               
            assertEquals("edf", keyval.getValue());               
       }
       {
            Map.Entry keyval = HttpUtil.getParameter("abc=edf=fgh");
            assertEquals("abc", keyval.getKey());               
            assertEquals("edf=fgh", keyval.getValue());               
       }
       {
            Map.Entry keyval = HttpUtil.getParameter("=");
            assertEquals("", keyval.getKey());               
            assertEquals("", keyval.getValue());               
       }
       {
            Map.Entry keyval = HttpUtil.getParameter("==");
            assertEquals("", keyval.getKey());               
            assertEquals("=", keyval.getValue());               
       }
    }

    /**
     * Test of testGetDefaultProtocol method, of class HttpUtil.
     */
    @Test
    public void testGetDefaultProtocol() {
        System.out.println("testGetDefaultProtocol");
        assertEquals("https", HttpUtil.getDefaultProtocol(true));               
        assertEquals("http", HttpUtil.getDefaultProtocol(false));               
    }

    /**
     * Test of testGetDefaultProtocol method, of class HttpUtil.
     */
    @Test
    public void testGetDefaultPort() {
        System.out.println("testGetDefaultPort");
        assertEquals(443, HttpUtil.getDefaultPort(true));               
        assertEquals(80, HttpUtil.getDefaultPort(false));               
    }

    /**
     * Test of testGetDefaultProtocol method, of class HttpUtil.
     */
    @Test
    public void testGetDefaultPort_String() {
        System.out.println("testGetDefaultPort_String");
        assertEquals(443, HttpUtil.getDefaultPort("https"));               
        assertEquals(80, HttpUtil.getDefaultPort("http"));               
        assertEquals(-1, HttpUtil.getDefaultPort("httpxxx"));               
    }

    /**
     * Test of testMultipart method, of class HttpUtil.
     */
    @Test
    public void testMultipart() {
        try {
            String boundary = HttpUtil.generateBoundary();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            HttpUtil.outMultipartText(boundary, out, "host", "www.exmple.com");
            HttpUtil.outMultipartText(boundary, out, "port", Util.toString(8080));
            HttpUtil.outMultipartText(boundary, out, "protocol", "https");
            HttpUtil.outMultipartText(boundary, out, "url", "https://www.example.com/");
            HttpUtil.outMultipartText(boundary, out, "comment", "コメント", StandardCharsets.UTF_8);
            System.out.println(new String(out.toByteArray(), StandardCharsets.ISO_8859_1));
        } catch (IOException ex) {
            Logger.getLogger(HttpUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    /**
     * Test of testNormalizeCharset method, of class HttpUtil.
     */
    @Test
    public void testNormalizeCharset() {
        System.out.println("testNormalizeCharset");
        System.out.println(HttpUtil.normalizeCharset("Shift_JIS")); 
        System.out.println(HttpUtil.normalizeCharset("Shift-JIS")); 
        System.out.println(HttpUtil.normalizeCharset("shift_jis")); 
        System.out.println(HttpUtil.normalizeCharset("Windows-31J")); 
        System.out.println(HttpUtil.normalizeCharset("MS932")); 
        System.out.println(HttpUtil.normalizeCharset(Util.DEFAULT_ENCODING)); 
        System.out.println(HttpUtil.normalizeCharset(null)); 

        System.out.println(Util.DEFAULT_ENCODING); 
                
    }

    
}
