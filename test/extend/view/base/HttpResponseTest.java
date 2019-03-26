package extend.view.base;

import java.text.ParseException;
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
public class HttpResponseTest {
    
    public HttpResponseTest() {
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

    private final static String RES_TEST10_FMT = "HTTP/1.1 200 OK\r\n"
            + "Date: Sat, 03 Feb 2018 02:22:53 GMT\r\n"
            + "Server: Apache/2.4.10 (Debian)\r\n"
            + "Set-Cookie: TestCookie=test\r\n"
            + "Set-Cookie: BIGipServer2092=1677787402.36895.0000\r\n"
            + "Set-Cookie: m9q9XULEMwKPeMim=1526851379225347956\r\n"
            + "Vary: Accept-Encoding\r\n"
            + "Connection: close\r\n"
            + "\r\n\r\n";

        private final static String RES_TEST11_FMT = "HTTP/1.1 200 OK\r\n"
            + "Date: Sat, 19 Jan 2019 02:49:15 GMT\r\n"
            + "Server: Apache/2.4.10 (Debian)\r\n"
            + "Set-Cookie: TestCookie=test\r\n"
            + "Set-Cookie: BIGipServer15722=rd5o00000000000000000000ffffc0000201o80\r\n"
            + "Set-Cookie: m9q9XULEMwKPeMim=43029390064855036802\r\n"
            + "Vary: Accept-Encoding\r\n"
            + "Content-Length: 116\r\n"
            + "Connection: close\r\n"
            + "Content-Type: text/html; charset=UTF-8\r\n\r\n";
        

    /**
     * Test of getStatusLine method, of class HttpResponse.
     */
    @Test
    public void testGetStatusLine() {
        System.out.println("getStatusLine");
        try {
            HttpResponse instance = HttpResponse.parseHttpResponse(RES_TEST10_FMT);
            String expResult = "HTTP/1.1 200 OK";
            String result = instance.getStatusLine();
            assertEquals(expResult, result);
        } catch (ParseException ex) {
            fail("getStatusLine");
        }
    }

    /**
     * Test of getProtocolVersion method, of class HttpResponse.
     */
    @Test
    public void testGetProtocolVersion() {
        System.out.println("getProtocolVersion");
        try {
            HttpResponse instance = HttpResponse.parseHttpResponse(RES_TEST10_FMT);
            String expResult = "HTTP/1.1";
            String result = instance.getProtocolVersion();
            assertEquals(expResult, result);
        } catch (ParseException ex) {
            fail("getProtocolVersion");
        }
    }

    /**
     * Test of getStatusCode method, of class HttpResponse.
     */
    @Test
    public void testGetStatusCode() {
        System.out.println("getStatusCode");
        try {
            HttpResponse instance = HttpResponse.parseHttpResponse(RES_TEST10_FMT);
            short expResult = 200;
            short result = instance.getStatusCode();
            assertEquals(expResult, result);
        } catch (ParseException ex) {
            fail("getStatusCode");
        }
    }

    /**
     * Test of getReasonPhrase method, of class HttpResponse.
     */
    @Test
    public void testGetReasonPhrase() {
        System.out.println("getReasonPhrase");
        try {
            HttpResponse instance = HttpResponse.parseHttpResponse(RES_TEST10_FMT);
            String expResult = "OK";
            String result = instance.getReasonPhrase();
            assertEquals(expResult, result);
        } catch (ParseException ex) {
            fail("getReasonPhrase");
        }
    }

    /**
     * Test of getGuessCharset method, of class HttpResponse.
     */
    @Test
    public void testGetGuessCharset() {
        try {
            System.out.println("getGuessCharset");
            HttpResponse instance = HttpResponse.parseHttpResponse(RES_TEST10_FMT);
            String expResult = "US-ASCII";
            String result = instance.getGuessCharset();
            assertEquals(expResult, result);
        } catch (ParseException ex) {
            fail("getGuessCharset");
        }
    }


    /**
     * Test of getContentMimeType method, of class HttpResponse.
     */
    @Test
    public void testGetContentMimeType() {
        System.out.println("getContentMimeType");
        try {
            {
                HttpResponse instance = HttpResponse.parseHttpResponse(RES_TEST10_FMT);
                String expResult = null;
                String result = instance.getContentMimeType();
                assertEquals(expResult, result);            
            }
            {
                HttpResponse instance = HttpResponse.parseHttpResponse(RES_TEST11_FMT);
                String expResult = "text/html";
                String result = instance.getContentMimeType();
                assertEquals(expResult, result);            
            }            
        } catch (ParseException ex) {
            fail("getGuessCharset");
        }
    }

    /**
     * Test of getContentLength method, of class HttpResponse.
     */
    @Test
    public void testGetContentLength() {
        System.out.println("getContentLength");
        try {
            {
                HttpResponse instance = HttpResponse.parseHttpResponse(RES_TEST10_FMT);
                int expResult = -1;
                int result = instance.getContentLength();
                assertEquals(expResult, result);            
            }
            {
                HttpResponse instance = HttpResponse.parseHttpResponse(RES_TEST11_FMT);
                int expResult = 116;
                int result = instance.getContentLength();
                assertEquals(expResult, result);            
            }            
        } catch (ParseException ex) {
            fail("getGuessCharset");
        }
    }
    
}
