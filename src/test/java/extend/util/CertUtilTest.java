package extend.util;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
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
public class CertUtilTest {

    public CertUtilTest() {
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
     * Test of exportToPem method, of class CertUtil.
     */
    @Test
    public void testExportToPem_Key_X509Certificate_JKS() throws Exception {
        System.out.println("exportToPem JKS");
        String storeFileName = CertUtilTest.class.getResource("/resources/server.keystore").getPath();
        HashMap<String, Map.Entry<Key, X509Certificate>> certMap = CertUtil.loadFromJKS(new File(storeFileName), "testca");
        for (String ailias : certMap.keySet()) {
            Map.Entry<Key, X509Certificate> cert = certMap.get(ailias);
            String result = CertUtil.exportToPem(cert.getKey(), cert.getValue());
            System.out.println(result);
        }
    }

    /**
     * Test of exportToPem method, of class CertUtil.
     */
    @Test
    public void testExportToPem_Key_X509Certificate_PKCS12() throws Exception {
        System.out.println("exportToPem PKCS12");
        String storeFileName = CertUtilTest.class.getResource("/resources/burpca.p12").getPath();
        HashMap<String, Map.Entry<Key, X509Certificate>> certMap = CertUtil.loadFromPKCS12(new File(storeFileName), "testca");
        for (String ailias : certMap.keySet()) {
            System.out.println("ailias:" + ailias);
            Map.Entry<Key, X509Certificate> cert = certMap.get(ailias);
            String result = CertUtil.exportToPem(cert.getKey(), cert.getValue());
            System.out.println(result);
        }
    }

    /**
     * Test of exportToPem method, of class CertUtil.
     */
    @Test
    public void testExportToPem_Key() throws Exception {
        System.out.println("exportToPem");
        String storeFileName = CertUtilTest.class.getResource("/resources/server.keystore").getPath();
        HashMap<String, Map.Entry<Key, X509Certificate>> certMap = CertUtil.loadFromJKS(new File(storeFileName), "testca");
        for (String ailias : certMap.keySet()) {
            Map.Entry<Key, X509Certificate> cert = certMap.get(ailias);
            PKCS8EncodedKeySpec pkcs8Key = new PKCS8EncodedKeySpec(cert.getKey().getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8Key);
        }
    }

    @Test
    public void testPemToPrivateKey() throws Exception {
        System.out.println("pemToPrivateKey");
        String storeFileName = CertUtilTest.class.getResource("/resources/burpca.pem").getPath();
        PrivateKey priKey = CertUtil.pemToPrivateKey(Util.getRawStr(Util.bytesFromFile(new File(storeFileName))));
    }

    @Test
    public void testPemToCertificate() throws Exception {
        System.out.println("pemToCerficate");
        String storeFileName = CertUtilTest.class.getResource("/resources/burpca.pem").getPath();
        X509Certificate x509Cert = CertUtil.loadCertificate(Util.getRawStr(Util.bytesFromFile(new File(storeFileName))));
    }


    /**
     * Test of exportToPem method, of class CertUtil.
     */
    @Test
    public void testExportToPem_X509Certificate() throws Exception {
        System.out.println("exportToPem");
        String pemFileName = CertUtilTest.class.getResource("/resources/burpca_certificate.pem").getPath();
        String expResult = Util.getRawStr(Util.bytesFromFile(new File(pemFileName)));

        String storeFileName = CertUtilTest.class.getResource("/resources/burpca.p12").getPath();
        HashMap<String, Map.Entry<Key, X509Certificate>> certMap = CertUtil.loadFromPKCS12(new File(storeFileName), "testca");
        for (String ailias : certMap.keySet()) {
            Map.Entry<Key, X509Certificate> x509cert = certMap.get(ailias);
            String result = CertUtil.exportToPem(x509cert.getValue());
            assertEquals(expResult, result);
            break;
        }
    }


    /**
     * Test of exportToDer method, of class CertUtil.
     */
    @Test
    public void testExportToDer_Key() throws Exception {
        System.out.println("exportToDer");
        String storeFileName = CertUtilTest.class.getResource("/resources/burpca.p12").getPath();
        HashMap<String, Map.Entry<Key, X509Certificate>> certMap = CertUtil.loadFromPKCS12(new File(storeFileName), "testca");
        for (String ailias : certMap.keySet()) {
            Map.Entry<Key, X509Certificate> x509cert = certMap.get(ailias);
            byte [] result = CertUtil.exportToDer(x509cert.getKey());            
            break;
        }
    }

    /**
     * Test of exportToDer method, of class CertUtil.
     */
    @Test
    public void testExportToDer_X509Certificate() throws Exception {
        System.out.println("exportToDer");
        String storeFileName = CertUtilTest.class.getResource("/resources/burpca.p12").getPath();
        HashMap<String, Map.Entry<Key, X509Certificate>> certMap = CertUtil.loadFromPKCS12(new File(storeFileName), "testca");
        for (String ailias : certMap.keySet()) {
            Map.Entry<Key, X509Certificate> x509cert = certMap.get(ailias);
            byte [] result = CertUtil.exportToDer(x509cert.getValue());
            break;
        }
    }

    @Test
    public void testRSAPem() throws Exception {
        String privateFile = CertUtilTest.class.getResource("/resources/private-key.pem").getPath();
        byte[] privateBytes = Util.bytesFromFile(new File(privateFile));
        String privaeData = new String(privateBytes, StandardCharsets.ISO_8859_1);
        PrivateKey privateKey = CertUtil.loadPrivateKey(privaeData);
        String publicFile = CertUtilTest.class.getResource("/resources/public-key.pem").getPath();
        byte[] publicBytes = Util.bytesFromFile(new File(publicFile));
        String publicData = new String(publicBytes, StandardCharsets.ISO_8859_1);
        PublicKey publicKey = CertUtil.loadPublicKey(publicData);
    }

    /**
     * Test of getFirstAlias method, of class OCSPUtil.
     */
    @Test
    public void testGetFirstAlias() throws Exception {
        System.out.println("getFirstAlias");
        KeyStore ks = KeyStore.getInstance("PKCS12");
        String password = "testca";
        String caFileName = CertUtilTest.class.getResource("../../resources/burpca.p12").getPath();
        ks.load(new FileInputStream(caFileName), password.toCharArray());
        String alias = CertUtil.getFirstAlias(ks);
        assertEquals("cacert", alias);
    }
    
}
