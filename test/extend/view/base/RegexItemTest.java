/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

    /**
     * Test of recompileRegex method, of class RegexItem.
     */
    @Test
    public void testRecompileRegex() {
        System.out.println("recompileRegex");
        boolean quote = false;
        RegexItem instance = new RegexItem();
        instance.recompileRegex(quote);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRegexPattern method, of class RegexItem.
     */
    @Test
    public void testGetRegexPattern() {
        System.out.println("getRegexPattern");
        RegexItem instance = new RegexItem();
        Pattern expResult = null;
        Pattern result = instance.getRegexPattern();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of compileRegex method, of class RegexItem.
     */
    @Test
    public void testCompileRegex_3args() {
        System.out.println("compileRegex");
        String text = "";
        int flags = 0;
        boolean quote = false;
        Pattern expResult = null;
        Pattern result = RegexItem.compileRegex(text, flags, quote);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
