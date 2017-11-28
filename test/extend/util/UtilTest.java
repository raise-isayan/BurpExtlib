/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extend.util;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author isayan
 */
public class UtilTest {
    
    public UtilTest() {
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
     */
    @Test
    public void testCalc() {
        double pow = Math.pow(16, 20);
        System.out.println(String.format("%4.2f", Math.log(pow) / Math.log(2.0)));        
    }
    
    
    /**
     */
    @Test
    public void testURL() {
        try {
            URL u = new URL("http://192.168.50.130/cgi-bin/multienc.cgi");
            System.out.println("url:" + u.toString());
            System.out.println("url:" + u.toExternalForm());

            URL u2 = new URL("http://192.168.50.130:80/cgi-bin/multienc.cgi");
            System.out.println("url2:" + u2.toString());
            System.out.println("url2:" + u2.toExternalForm());
            System.out.println("url3:" + u2.toURI().normalize().toString());
        } catch (URISyntaxException ex) {
            Logger.getLogger(UtilTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(UtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     */
    @Test
    public void testMatch() {
        Pattern p = Pattern.compile("http://192.168.50.130/cgi-bin/multienc.cgi");
        Matcher m = p.matcher("http://192.168.50.130/cgi-bin/multienc.cgi?charset=EUC-JP");
        if (m.matches()) {
            System.out.println("match");
        }
    }
    
    /**
     */
    @Test
    public void testByteReplace_0() {
        byte [] base = new byte [] {1,2,3,4,5,6,7,8,9,10}; 
        byte [] replace = new byte [] {21,22,23}; 
        byte [] content = Util.byteReplace(base, 0, 0, replace);
        for (int i = 0; i < content.length; i++) {
            System.out.println("content_0:" + content[i]);
        }
    }
        
    @Test
    public void testByteReplace_1_3() {
        byte [] base = new byte [] {1,2,3,4,5,6,7,8,9,10}; 
        byte [] replace = new byte [] {21,22,23}; 
        byte [] content = Util.byteReplace(base, 1, 3, replace);
        for (int i = 0; i < content.length; i++) {
            System.out.println("content_1_3:" + content[i]);
        }
    }
    
    @Test
    public void testString_0() {
        String rep0 = Util.stringReplace("1234567890", 0, 0, "abc");
        System.out.println("testString_0:" + rep0);
        String rep1 = Util.stringReplace("1234567890", 1, 3, "abc");
        System.out.println("testString_1_3:" + rep1);
    }
    
}
