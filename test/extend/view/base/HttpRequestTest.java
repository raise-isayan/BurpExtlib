package extend.view.base;

import burp.IHttpService;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
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
 * @author raise.isayan
 */
public class HttpRequestTest {
    
    public HttpRequestTest() {
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

    private final static String REQ_MESSAGE_URLENCODE =
        "POST /cgi-bin/multienc.cgi?charset=Shift_JIS&mode=disp HTTP/1.1\r\n" +
        "Host: 192.168.0.1\r\n" +
        "Content-Length: 60\r\n" +
        "Cache-Control: max-age=0\r\n" +
        "Origin: http://192.168.0.1\r\n" +
        "Content-Type: application/x-www-form-urlencoded\r\n" +
        "User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36\r\n" +
        "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\n" +
        "Referer: http://192.168.50.140/cgi-bin/multienc.cgi?charset=Shift_JIS&mode=input\r\n" +
        "Accept-Encoding: gzip, deflate\r\n" +
        "Accept-Language: ja,en-US;q=0.9,en;q=0.8\r\n" +
        "Connection: close\r\n" +
        "\r\n" +
        "text=%82%A0%82%A2%82%A4%82%A6%82%A8&OS=win&submit=%91%97%90M\r\n";    

    private final static String REQ_MESSAGE_MULTIPART =
        "POST /cgi-bin/sendto.cgi?mode=sendto HTTP/1.1\r\n" +
        "Host: 192.168.0.1\r\n" +
        "Content-Length: 922\r\n" +
        "Cache-Control: max-age=0\r\n" +
        "Origin: http://192.168.0.1\r\n" +
        "Content-Type: multipart/form-data; boundary=----WebKitFormBoundaryOw7BuAmQWEc7jVWH\r\n" +
        "User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36\r\n" +
        "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\n" +
        "Referer: http://192.168.0.1/sendto.html\r\n" +
        "Accept-Encoding: gzip, deflate\r\n" +
        "Accept-Language: ja,en-US;q=0.9,en;q=0.8\r\n" +
        "Connection: close\r\n" +
        "\r\n" +
        "------WebKitFormBoundaryOw7BuAmQWEc7jVWH\r\n" +
        "Content-Disposition: form-data; name=\"host\"\r\n" +
        "\r\n" +
        "てすと\r\n" +
        "------WebKitFormBoundaryOw7BuAmQWEc7jVWH\r\n" +
        "Content-Disposition: form-data; name=\"port\"\r\n" +
        "\r\n" +
        "\r\n" +
        "------WebKitFormBoundaryOw7BuAmQWEc7jVWH\r\n" +
        "Content-Disposition: form-data; name=\"protocol\"\r\n" +
        "\r\n" +
        "http\r\n" +
        "------WebKitFormBoundaryOw7BuAmQWEc7jVWH\r\n" +
        "Content-Disposition: form-data; name=\"url\"\r\n" +
        "\r\n" +
        "\r\n" +
        "------WebKitFormBoundaryOw7BuAmQWEc7jVWH\r\n" +
        "Content-Disposition: form-data; name=\"comment\"\r\n" +
        "\r\n" +
        "てすと\r\n" +
        "------WebKitFormBoundaryOw7BuAmQWEc7jVWH\r\n" +
        "Content-Disposition: form-data; name=\"request\"; filename=\"\"\r\n" +
        "Content-Type: application/octet-stream\r\n" +
        "\r\n" +
        "\r\n" +
        "------WebKitFormBoundaryOw7BuAmQWEc7jVWH\r\n" +
        "Content-Disposition: form-data; name=\"response\"; filename=\"\"\r\n" +
        "Content-Type: application/octet-stream\r\n" +
        "\r\n" +
        "\r\n" +
        "------WebKitFormBoundaryOw7BuAmQWEc7jVWH\r\n" +
        "Content-Disposition: form-data; name=\"encoding\"\r\n" +
        "\r\n" +
        "UTF-8\r\n" +
        "------WebKitFormBoundaryOw7BuAmQWEc7jVWH--\r\n" +
        "\r\n";

    /**
     * Test of parseHttpRequest method, of class HttpRequest.
     */
    @Test
    public void testParseHttpRequest_urlencoded() {
        try {
            System.out.println("makeGetRequest");
            String expResult = "POST";
            HttpRequest result = HttpRequest.parseHttpRequest(REQ_MESSAGE_URLENCODE);
            assertEquals(expResult, result.getMethod());
        } catch (ParseException ex) {
            Logger.getLogger(HttpRequestTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of parseHttpRequest method, of class HttpRequest.
     */
    @Test
    public void testParseHttpRequest_multipart() {
        try {
            System.out.println("makeGetRequest");
            String expResult = "POST";
            HttpRequest result = HttpRequest.parseHttpRequest(REQ_MESSAGE_MULTIPART.getBytes(StandardCharsets.UTF_8));
            assertEquals(expResult, result.getMethod());
            System.out.println(result.getHeader());
            System.out.println(result.getBody());
        } catch (ParseException ex) {
            Logger.getLogger(HttpRequestTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
