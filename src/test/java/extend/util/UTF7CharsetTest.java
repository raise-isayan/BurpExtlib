package extend.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
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
public class UTF7CharsetTest {
    
    public UTF7CharsetTest() {
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

    public static String toUTF7Encode(String str) {
        UTF7Charset utf7cs = new UTF7Charset("UTF-7", new String[]{}, false);
        ByteBuffer bb = utf7cs.encode(str);
        byte[] content = new byte[bb.limit()];
        System.arraycopy(bb.array(), 0, content, 0, content.length);
        return new String(content, StandardCharsets.US_ASCII);
    }

    public static String toUTF7Decode(String str) {
        UTF7Charset utf7cs = new UTF7Charset("UTF-7", new String[]{}, false);
        CharBuffer cb = utf7cs.decode(ByteBuffer.wrap(str.getBytes(StandardCharsets.US_ASCII)));
        return cb.toString();
    }
    
    /**
     * Test of UTF7Decode method, of class TransUtil.
     */
    @Test
    public void testToUTF7Decode() {
        System.out.println("toUTF7Decode");
        assertEquals("<", toUTF7Decode("+ADw-"));
        assertEquals("<script>", toUTF7Decode("+ADw-script+AD4-"));
        assertEquals("+", toUTF7Decode("+-"));
        assertEquals("変換前の文字列", toUTF7Decode("+WQlj21JNMG5lh1tXUhc-"));
    }

    /**
     * Test of UTF7Decode method, of class TransUtil.
     */
    @Test
    public void testTotoUTF7Encode() {
        System.out.println("toUTF7Encode");
        assertEquals("+ADw-", toUTF7Encode("<"));
        assertEquals("+ADw-script+AD4-", toUTF7Encode("<script>"));
        assertEquals("+-", toUTF7Encode("+"));
        assertEquals("+WQlj21JNMG5lh1tXUhc-", toUTF7Encode("変換前の文字列"));
    }
    
}
