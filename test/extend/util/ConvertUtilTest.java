/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extend.util;

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
    
}
