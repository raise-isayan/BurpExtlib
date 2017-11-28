/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extend.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.*;

/**
 *
 * @author isayan
 */
public final class HttpUtil {
    public static String LINE_TERMINATE = "\r\n";

    private HttpUtil() {
    }
    private final static Pattern HEADER_VALUE = Pattern.compile("^([^:]+)\\s*:\\s*(.*)");

    public static String getHeader(String key, String[] headers) {
        String value = null;
        for (String header : headers) {
            Matcher m = HEADER_VALUE.matcher(header);
            if (m.matches()) {
                String k = m.group(1);
                String v = m.group(2);
                if (key.equalsIgnoreCase(k)) {
                    value = v;
                    break;
                }
            }
        }
        return value;
    }

    public static String getEnctype(String[] headers) {
        String contentType = HttpUtil.getHeader("Content-Type", headers);
        if (contentType != null && contentType.indexOf(';') > 0) {
            contentType = contentType.substring(0, contentType.indexOf(';'));
        }
        return contentType;
    }

    public static boolean isUrlEencoded(String header) {
        return (header == null || header.equals("application/x-www-form-urlencoded"));
    }

    public static boolean isMaltiPart(String header) {
        return (header.contains("multipart"));
    }

    public static boolean isPlain(String header) {
        return (header.contains("xml") || header.contains("json"));
    }

    public static boolean startsWithHttp(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    public static boolean isSSL(String protocol) {
        return "https".equals(protocol);
    }

    /**
     * Boundaryの作成
     *
     * @return ランダムなBoundary
     */
    public static String generateBoundary() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(Util.randomIdent(40));
        return buffer.toString();
    }

    public static void outMultipartFinish(String boundary, OutputStream out) throws IOException {
        out.write(Util.getRawByte("--" + boundary + "--" + LINE_TERMINATE));
    }

    public static void outMultipartText(String boundary, OutputStream out, String name, String text) throws IOException {
        // Text出力
        out.write(Util.getRawByte("--" + boundary + LINE_TERMINATE));
        out.write(Util.getRawByte("Content-Disposition: form-data; name=\"" + name + "\"" + LINE_TERMINATE));
        out.write(Util.getRawByte("Content-Type: text/plain" + LINE_TERMINATE + LINE_TERMINATE));
        out.write(Util.getRawByte(text));
        out.write(Util.getRawByte(LINE_TERMINATE));
    }

    public static void outMultipartBinary(String boundary, OutputStream out, String name, byte[] message) throws IOException {
        // バイナリー出力
        out.write(Util.getRawByte("--" + boundary + LINE_TERMINATE));
        out.write(Util.getRawByte("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + name + "\"" + LINE_TERMINATE));
        out.write(Util.getRawByte("Content-Type: application/octet-stream" + LINE_TERMINATE + LINE_TERMINATE));
        out.write(message, 0, message.length);
        out.write(Util.getRawByte(LINE_TERMINATE));
    }

    public static HostnameVerifier ignoreHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String host, SSLSession ses) {
                return true;
            }
        };
    }

    protected static TrustManager[] trustAllCerts() {
        TrustManager[] tm = {
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }};
        return tm;
    }

    public static SSLSocketFactory ignoreSocketFactory()
            throws NoSuchAlgorithmException, KeyManagementException {
        KeyManager[] km = null;
        SSLContext sslcontext = SSLContext.getInstance("SSL");
        sslcontext.init(km, trustAllCerts(), new SecureRandom());
        return sslcontext.getSocketFactory();
    }

    public static void ignoreValidateCertification() {
        try {
            HttpsURLConnection.setDefaultSSLSocketFactory(ignoreSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HttpUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(HttpUtil.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public static void ignoreValidateCertification(
            SSLContext sslcontext) {
        try {
            ignoreValidateCertification(sslcontext, null, null);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HttpUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HttpUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            Logger.getLogger(HttpUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnrecoverableKeyException ex) {
            Logger.getLogger(HttpUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void ignoreValidateCertification(
            SSLContext sslcontext, File clientCertFile, String passwd) throws IOException, CertificateException, UnrecoverableKeyException {
        KeyManager[] km = null;
        if (clientCertFile != null) {
            try {
                //クライアント証明書対応
                char[] passphrase = passwd.toCharArray();
                KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                KeyStore ks = KeyStore.getInstance("PKCS12");
                ks.load(new FileInputStream(clientCertFile), passphrase);
                kmf.init(ks, passphrase);
                sslcontext.init(kmf.getKeyManagers(), trustAllCerts(), new SecureRandom());
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(HttpUtil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (KeyManagementException ex) {
                Logger.getLogger(HttpUtil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (KeyStoreException ex) {
                Logger.getLogger(HttpUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                sslcontext.init(km, trustAllCerts(), new SecureRandom());
            } catch (KeyManagementException ex) {
                Logger.getLogger(HttpUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static String[] extractHTMLComments(String message) {
        return extractHTMLComments(message, false);
    }

    private static final Pattern HTML_COMMENT = Pattern.compile("<!--\n{0,}.+?\n{0,}-->", Pattern.DOTALL);

    public static boolean existsHTMLComments(String message) {
        Matcher matcher = HTML_COMMENT.matcher(message);
        return matcher.find();
    }

    public static String[] extractHTMLComments(String message, boolean uniqe) {
        ArrayList<String> list = new ArrayList<String>();
        // Create matcher 
        Matcher matcher = HTML_COMMENT.matcher(message);
        while (matcher.find()) {
            String comment = matcher.group();
            list.add(comment);
        }
        if (uniqe) {
            List uniqList = Util.toUniqList(list);
            return (String[]) uniqList.toArray(new String[0]);
        } else {
            return list.toArray(new String[0]);
        }
    }

    public static byte[] buildGetRequestByte(String url) {
        StringBuilder buff = new StringBuilder();
        Pattern pattern = Pattern.compile("^(https?)://(.*?)((:\\d+)?)(/\\??.*)");
        Matcher m = pattern.matcher(url);
        if (m.find()) {
            String protocol = m.group(1);
            String host = m.group(2);
            String port = m.group(3);
            String query = m.group(5);
            buff.append("GET ");
            buff.append(query);
            buff.append(" HTTP/1.1");
            buff.append("\r\n");
            buff.append("Host: ");
            buff.append(host);
            buff.append(port);
            buff.append("\r\n");
        }
        return Util.getRawByte(buff.toString());
    }

    public static String getBaseName(URL url) {
        String path[] = url.getPath().split("/");
        String name = (path.length > 0) ? path[path.length - 1] : "";
        if (name.equals("")) {
            name = url.getHost();
        }
        return name;
    }

    public static String normalizeURL(String value) {
        Pattern pattern = Pattern.compile("^(https?)://(.*?)(:(?:80|443))/");
        StringBuffer buff = new StringBuffer();
        Matcher m = pattern.matcher(value);
        if (m.find()) {
            String protocol = m.group(1);
            String port = m.group(3);
            if ("http:".startsWith(protocol) && ":80".equals(port)) {
                m.appendReplacement(buff, "$1://$2/");
            } else if ("https:".startsWith(protocol) && ":443".equals(port)) {
                m.appendReplacement(buff, "$1://$2/");
            }
        }
        m.appendTail(buff);
        return buff.toString();
    }

    public static Map.Entry getParameter(String plain) {
        String s[] = plain.split("=", 2);
        if (s.length == 1) {
            return new AbstractMap.SimpleEntry(s[0], "");
        } else {
            return new AbstractMap.SimpleEntry(s[0], s[1]);
        }
    }

    public static Map.Entry getHeader(String plain) {
        String s[] = plain.split(":", 2);
        if (s.length == 1) {
            return new AbstractMap.SimpleEntry(s[0], "");
        } else {
            return new AbstractMap.SimpleEntry(s[0], s[1].trim());
        }
    }
    
    private static final Pattern BASE_URI_CHANGE = Pattern.compile("(<head>)|(<body>|(<html>))", Pattern.CASE_INSENSITIVE);

    public static String changeBaseURI(String content, String topURL) {
        // かなり安易なBaseURIの設定
        // 差込位置
        int pos = 0;
        Matcher m = BASE_URI_CHANGE.matcher(content);
        if (m.find()) {
            pos = m.end();
        }
        StringBuilder buff = new StringBuilder(content);
        buff.insert(pos, String.format("<base href=\"%s\">", topURL));
        return buff.toString();
    }

    public static String toURL(String schema, String host, int port) {
        String url = String.format("%s://%s", schema, host);
        if (port != 80 && port != 443) {
            url += ":" + port;
        }
        return url;
    }

    public static String toURL(String schema, String host, int port, String path) {
        return toURL(schema, host, port) + Util.appendFirstSeparator(path, "/");
    }

    public static String getDefaultProtocol(boolean useHttps) {
        if (useHttps) {
            return "https";
        } else {
            return "http";
        }
    }

    public static int getDefaultPort(boolean useHttps) {
        if (useHttps) {
            return 443;
        } else {
            return 80;
        }
    }

    public static int getDefaultPort(String protocol) {
        if ("https".equals(protocol)) {
            return 443;
        } else if ("http".equals(protocol)) {
            return 80;
        } else {
            return -1;
        }
    }

    public static String getURLBasePath(String path) {
        int lastSep = path.lastIndexOf('/');
        if (lastSep > 0) {
            return path.substring(0, lastSep);
        } else if (lastSep == 0) {
            return "/";
        } else {
            return path;
        }
    }

    /**
     * 文字コードを判別する 以下の移植 http://dobon.net/vb/dotnet/string/detectcode.html
     *
     * @param bytes 文字コードを調べるデータ
     * @return 適当と思われるEncoding、判断できなかった時はnull
     */
    public static String getGuessCode(byte[] bytes) {
        final int bEscape = 27;
        final int bAt = 64;
        final int bDollar = 36;
        final int bAnd = 38;
        final int bOpen = 40; //'('
        final int bB = 66;
        final int bD = 68;
        final int bJ = 74;
        final int bI = 73;
        int len = bytes.length;
        int b1;
        int b2;
        int b3;
        int b4;
        // Encode::is_utf8 は無視
        int ulelen = 0;
        int ubelen = 0;
        boolean isBinary = false;
        for (int i = 0; i < len; i++) {
            b1 = (bytes[i] & 255);
            if (b1 <= 6 || b1 == 127 || b1 == 255) {
                //'binary'
                isBinary = true;
                if (b1 == 0 && i < len - 1 && (bytes[i + 1] & 255) <= 127) {
                    ubelen++;
                }
                if (b1 == 0 && i > 1 && (bytes[i - 1] & 255) <= 127) {
                    ulelen++;
                }
                // バイト長の一定以上の場合UTF16と判定
                if (len * 0.3 < ulelen || len * 0.3 < ubelen) {
                    //smells like raw unicode
                    return "UTF-16"; //Unicode
                }
            }
        }
        if (isBinary) {
            return null;
        }
        //not Japanese
        boolean notJapanese = true;
        for (int i = 0; i < len; i++) {
            b1 = (bytes[i] & 255);
            if (b1 == bEscape || 128 <= b1) {
                notJapanese = false;
                break;
            }
        }
        if (notJapanese) {
            return "US-ASCII"; // ASCII
        }
        for (int i = 0; i < len - 2; i++) {
            b1 = (bytes[i] & 255);
            b2 = (bytes[i + 1] & 255);
            b3 = (bytes[i + 2] & 255);
            if (b1 == bEscape) {
                if (b2 == bDollar && b3 == bAt) {
                    //JIS_0208 1978
                    //JIS
                    return "ISO-2022-JP";
                } else if (b2 == bDollar && b3 == bB) {
                    //JIS_0208 1983
                    //JIS
                    return "ISO-2022-JP";
                } else if (b2 == bOpen && (b3 == bB || b3 == bJ)) {
                    //JIS_ASC
                    //JIS
                    return "ISO-2022-JP";
                } else if (b2 == bOpen && b3 == bI) {
                    //JIS_KANA
                    //JIS
                    return "ISO-2022-JP";
                }
                if (i < len - 3) {
                    b4 = (bytes[i + 3] & 255);
                    if (b2 == bDollar && b3 == bOpen && b4 == bD) {
                        //JIS_0212
                        //JIS
                        return "ISO-2022-JP";
                    }
                    if (i < len - 5 && b2 == bAnd && b3 == bAt && b4 == bEscape && (bytes[i + 4] & 255) == bDollar && (bytes[i + 5] & 255) == bB) {
                        //JIS_0208 1990
                        //JIS
                        return "ISO-2022-JP";
                    }
                }
            }
        }
        //should be euc|sjis|utf8
        //use of (?:) by Hiroki Ohzaki <ohzaki@iod.ricoh.co.jp>
        int sjis = 0;
        int euc = 0;
        int utf8 = 0;
        for (int i = 0; i < len - 1; i++) {
            b1 = (bytes[i] & 255);
            b2 = (bytes[i + 1] & 255);
            if (((129 <= b1 && b1 <= 159) || (224 <= b1 && b1 <= 252)) && ((64 <= b2 && b2 <= 126) || (128 <= b2 && b2 <= 252))) {
                //SJIS_C
                sjis += 2;
                i++;
            }
        }
        for (int i = 0; i < len - 1; i++) {
            b1 = (bytes[i] & 255);
            b2 = (bytes[i + 1] & 255);
            if (((161 <= b1 && b1 <= 254) && (161 <= b2 && b2 <= 254)) || (b1 == 142 && (161 <= b2 && b2 <= 223))) {
                //EUC_C
                //EUC_KANA
                euc += 2;
                i++;
            } else if (i < len - 2) {
                b3 = (bytes[i + 2] & 255);
                if (b1 == 143 && (161 <= b2 && b2 <= 254) && (161 <= b3 && b3 <= 254)) {
                    //EUC_0212
                    euc += 3;
                    i += 2;
                }
            }
        }
        for (int i = 0; i < len - 1; i++) {
            b1 = (bytes[i] & 255);
            b2 = (bytes[i + 1] & 255);
            if ((192 <= b1 && b1 <= 223) && (128 <= b2 && b2 <= 191)) {
                //UTF8
                utf8 += 2;
                i++;
            } else if (i < len - 2) {
                b3 = (bytes[i + 2] & 255);
                if ((224 <= b1 && b1 <= 239) && (128 <= b2 && b2 <= 191) && (128 <= b3 && b3 <= 191)) {
                    //UTF8
                    utf8 += 3;
                    i += 2;
                }
            }
        }
        //M. Takahashi's suggestion
        //utf8 += utf8 / 2;
        if (euc > sjis && euc > utf8) {
            //EUC
            return "EUC-JP";
        } else if (sjis > euc && sjis > utf8) {
            //SJIS
            return "Shift_JIS"; // windows-31j
        } else if (utf8 > euc && utf8 > sjis) {
            //UTF8
            return "UTF-8"; // UTF8
        }
        return null;
    }

    public static int indexOfStartsWith(List<String> headers, String prefix) {
        for (int idx = 0; idx < headers.size(); idx++) {
            if (headers.get(idx).startsWith(prefix)) {
                return idx;
            }
        }
        return -1;
    }
    
}
