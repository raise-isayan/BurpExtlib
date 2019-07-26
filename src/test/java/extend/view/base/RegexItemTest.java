package extend.view.base;

import java.util.regex.Pattern;
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
public class RegexItemTest {
    
    public RegexItemTest() {
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
     * Test of isValidRegex method, of class RegexItem.
     */
    @Test
    public void testIsValidRegex() {
        System.out.println("isValidRegex");
        RegexItem instance = new RegexItem();
        {
            boolean expResult = false;
            instance.setMatch("(");
            boolean result = instance.isValidRegex();
            assertEquals(expResult, result);        
        }
        {
            boolean expResult = true;
            instance.setMatch("\\(");
            boolean result = instance.isValidRegex();
            assertEquals(expResult, result);        
        }
        {
            boolean expResult = true;
            instance.setRegexp(false);
            instance.setMatch("(");
            boolean result = instance.isValidRegex();
            assertEquals(expResult, result);        
        }
        {
            boolean expResult = false;
            instance.setRegexp(true);
            instance.setIgnoreCase(true);
            instance.setMatch("(");
            boolean result = instance.isValidRegex();
            assertEquals(expResult, result);        
        }
    }

    /**
     * Test of compileRegex method, of class RegexItem.
     */
    @Test
    public void testCompileRegex_boolean() {
        System.out.println("compileRegex");
        RegexItem instance = new RegexItem();
        {
            Pattern expResult = null;
            instance.setMatch("(");
            Pattern result = instance.compileRegex(false);
            assertEquals(expResult, result);
        
        }
        {
            Pattern expResult = Pattern.compile(Pattern.quote("("));
            instance.setMatch("(");
            Pattern result = instance.compileRegex(true);
            assertEquals(expResult.pattern(), result.pattern());
        
        }
        {
            Pattern expResult = Pattern.compile("\\(");
            instance.setMatch("\\(");
            Pattern result = instance.compileRegex(false);
            assertEquals(expResult.pattern(), result.pattern());        
        }
    }
    
}
