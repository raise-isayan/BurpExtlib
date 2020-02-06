package extend.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ListModel;

/**
 *
 * @author isayan
 */
public final class Util {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static final String DEFAULT_ENCODING = System.getProperty("file.encoding");
    public static final String NEW_LINE = System.getProperty("line.separator");

    private Util() {
    }

    public static String toString(String value) {
        if (value == null) {
            return "";
        }
        else {
            return String.valueOf(value);
        }
    }

    public static String toString(Boolean value) {
        if (value == null) {
            return "";
        }
        else {
            return String.valueOf(value);
        }
    }
    
    public static String toString(Integer value) {
        if (value == null) {
            return "";
        }
        else {
            return String.valueOf(value);
        }
    }

    public static String toString(Float value) {
        if (value == null) {
            return "";
        }
        else {
            return String.valueOf(value);
        }
    }

    public static String toString(Object value) {
        if (value == null) {
            return "";
        }
        else {
            return String.valueOf(value);
        }
    }
    
    /**
     * 文字列を数字に変換
     *
     * @param value 対象文字列
     * @param defvalue 変換できなかった場合のデフォルト値
     * @return 変換後の数字
     */
    public static int parseIntDefault(String value, int defvalue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defvalue;
        }
    }

    /**
     * 文字列をFloatに変換
     *
     * @param value 対象文字列
     * @param defvalue 変換できなかった場合のデフォルト値
     * @return 変換後のFloat
     */
    public static float parseFloatDefault(String value, float defvalue) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException ex) {
            return defvalue;
        }
    }

    /**
     * 文字列をBoolean型に変換
     *
     * @param value 対象文字列
     * @param defvalue 変換できなかった場合のデフォルト値
     * @return 変換後のBoolean
     */
    public static boolean parseBooleanDefault(String value, boolean defvalue) {
        if (value == null) {
            return defvalue;
        } else if (value.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        } else if (value.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        } else {
            return defvalue;
        }
    }

    /**
     * 文字列を対応するEnum型に変換
     *
     * @param enumType
     * @param name
     * @param defvalue 変換できなかった場合のデフォルト値
     * @return 変換後のEnum
     */
    @SuppressWarnings("unchecked")
    public static Enum parseEnumDefault(Class enumType, String name, Enum defvalue) {
        try {
            return Enum.valueOf(enumType, name);
        } catch (IllegalArgumentException e) {
            return defvalue;
        } catch (NullPointerException e) {
            return defvalue;
        }
    }

    public static String enumSetToString(EnumSet<?> enumset) {
        Iterator<?> it = enumset.iterator();
        if (!it.hasNext()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            Enum<?> e = (Enum<?>) it.next();
            sb.append(e.name());
            if (!it.hasNext()) {
                return sb.append(']').toString();
            }
            sb.append(',').append(' ');
        }
    }

    @SuppressWarnings("unchecked")
    public static Enum parseEnumValue(Class enumType, String value) {
        if (value != null) {
            value = value.toUpperCase();
            value = value.replace(' ', '_');
            return Enum.valueOf(enumType, value);
        }
        return null;
    }

    /**
     * 生のバイト文字列取得
     *
     * @param message 対象文字列
     * @return バイト列
     */
    public static byte[] getRawByte(String message) {
        return message.getBytes(StandardCharsets.ISO_8859_1);
    }

    public static String getRawStr(byte[] message) {
        return new String(message, StandardCharsets.ISO_8859_1);
    }

    public static String getRawByteStr(String message, String encoding) throws UnsupportedEncodingException {
        byte[] encodeByte = message.getBytes(encoding);
        return new String(encodeByte, StandardCharsets.ISO_8859_1);
    }

    public static String getRawByteStr(String message, Charset charset) {
        byte[] encodeByte = message.getBytes(charset);
        return new String(encodeByte, StandardCharsets.ISO_8859_1);
    }
    
    public static String appendFirstSeparator(String path, String separator) {
        if (path.startsWith(separator)) {
            return path;
        } else {
            return separator + path;
        }
    }

    public static String removeFirstSeparator(String path, String separator) {
        if (path.startsWith(separator)) {
            return path.substring(path.indexOf(separator) + separator.length());
        } else {
            return path;
        }
    }

    public static String appendLastSeparator(String path, String separator) {
        if (path.endsWith(separator)) {
            return path;
        } else {
            return path + separator;
        }
    }

    public static String removeLastSeparator(String path, String separator) {
        if (path.endsWith(separator)) {
            return path.substring(0, path.lastIndexOf(separator));
        } else {
            return path;
        }
    }

    /**
     * 有効な文字列エンコーディングリストの取得
     *
     * @return エンコーディングリスト
     */
    public static String[] getAvailableEncodingList() {
        List<String> list = new ArrayList<String>();
        SortedMap<String, Charset> map = Charset.availableCharsets();
        Charset charsets[] = (Charset[]) map.values().toArray(
                new Charset[]{});
        for (int i = 0; i < charsets.length; i++) {
            String charname = charsets[i].displayName();
            list.add(charname);
        }
        return list.toArray(new String[0]);
    }

    /**
     * デコード文字列
     *
     * @param message メッセージ
     * @return バイト列
     */
    public static String decodeMessage(byte[] message) {
        return decodeMessage(message, StandardCharsets.ISO_8859_1);
    }

    /**
     * エンコード文字列
     *
     * @param message メッセ0時
     * @return
     */
    public static byte[] encodeMessage(String message) {
        return encodeMessage(message, StandardCharsets.ISO_8859_1);
    }

    public static String decodeMessage(byte[] message, String encoding) {
        String decodeStr = null;
        try {
            decodeStr = new String(message, encoding);
        } catch (UnsupportedEncodingException ex) {
        }
        return decodeStr;
    }
    
    public static String decodeMessage(byte[] message, Charset charset) {
        return new String(message, charset);
    }

    public static String decodeMessage(byte[] message, int offset, int length, Charset charset) {
        return new String(message, offset, length, charset);
    }
    
    public static String decodeMessage(byte[] message, int offset, int length, String encoding) {
        String decodeStr = null;
        try {
            decodeStr = new String(message, offset, length, encoding);
        } catch (UnsupportedEncodingException ex) {
        }
        return decodeStr;
    }

    public static byte[] encodeMessage(String message, Charset charset) {
        return message.getBytes(charset);
    }
    
    public static byte[] encodeMessage(String message, String encoding) {
        byte[] encodeByte = null;
        try {
            encodeByte = message.getBytes(encoding);
        } catch (UnsupportedEncodingException ex) {
        }
        return encodeByte;
    }

    public static byte[] byteReplace(byte[] base, int startPos, int endPos, byte[] replace) {
        ByteArrayOutputStream bstm = new ByteArrayOutputStream();
        try {
            bstm.write(Arrays.copyOfRange(base, 0, startPos));
            bstm.write(replace);
            bstm.write(Arrays.copyOfRange(base, endPos, base.length));
        } catch (IOException ex) {
        }
        return bstm.toByteArray();
    }

    /**
     * 文字列置換
     *
     * @param str 置換元文字列
     * @param startPos 開始位置
     * @param endPos 終了位置
     * @param repstr 置換文字列
     * @return 置換後文字列
     */
    public static String stringReplace(String str, int startPos, int endPos, String repstr) {
        StringBuilder buff = new StringBuilder(str);
        buff.delete(startPos, endPos);
        buff.insert(startPos, repstr);
        return buff.toString();
    }

    public static String propertyListToString(List<String> list) {
        StringBuilder buff = new StringBuilder();
        for (String s : list) {
            buff.append(s);
            buff.append("\n");
        }
        return buff.toString();
    }

    public static List<String> propertyStringToList(String s) {
        return Arrays.asList(s.split("\n"));
    }

    public static boolean contains(ListModel model, String value) {
        for (int i = 0; i < model.getSize(); i++) {
            Object o = model.getElementAt(i);
            if (equalsString(value, (String) o)) {
                return true;
            }
        }
        return false;
    }

    public static boolean equalsString(String a1, String a2) {
        if (a1 == null || a2 == null) {
            return false;
        }
        return Arrays.equals(a1.toCharArray(), a2.toCharArray());
    }

    public static int compareToString(String a1, String a2) {
        int len1 = a1.length();
        int len2 = a2.length();
        int lim = Math.min(len1, len2);
        char v1[] = a1.toCharArray();
        char v2[] = a2.toCharArray();
        int k = 0;
        while (k < lim) {
            char c1 = v1[k];
            char c2 = v2[k];
            if (c1 != c2) {
                return c1 - c2;
            }
            k++;
        }
        return len1 - len2;
    }

    public static <T> List<T> toList(Iterator<T> e) {
        List<T> l = new ArrayList<T>();
        while (e.hasNext()) {
            l.add(e.next());
        }
        return l;
    }

    public static <T> List<T> toUniqList(List<T> list) {
        Map<T, Boolean> mapUniq = new LinkedHashMap<T, Boolean>(16, (float) 0.75, true);
        for (T k : list) {
            mapUniq.put(k, true);
        }
        return toList(mapUniq.keySet().iterator());
    }

    public static List<String> toUniqList(String regex, List<String> list) {
        Pattern pattern = Pattern.compile(regex);
        Map<String, Boolean> mapUniq = new LinkedHashMap<>(16, (float) 0.75, true);
        for (String k : list) {
            Matcher m = pattern.matcher(Util.toString(k));
            if (m.matches()) {
                String g = (m.groupCount() > 0) ? (m.group(1)) : (m.group(0));
                mapUniq.put(g, true);
            }
        }
        return toList(mapUniq.keySet().iterator());
    }

    /**
     * ローテーション可能なファイル名を探す
     *
     * @param dir ディレクトリ
     * @param value
     * @return ローテーションファイル名
     */
    public static boolean existsStartsDir(File dir, final String value) {
        String[] list = dir.list(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(value);
            }
        });
        return (list.length > 0);
    }

    /**
     * ローテーション可能なファイル名を探す
     *
     * @param dir ディレクトリ
     * @param pattern パターン
     * @return ローテーションファイル名
     */
    public static File rotateFile(File dir, String pattern) {
        int count = 1;
        pattern = pattern.replace("%", "%%");
        pattern += ".%d";
        // 存在しないファイルを探す
        File file = new File(dir, String.format(pattern, count));
        while (file.exists()) {
            count++;
            file = new File(dir, String.format(pattern, count));
        }
        return file;
    }

    public static Process executeFormat(String target, String args[]) throws IOException {
        Process process = null;
        String command = "";
        MessageFormat msgfmt = new MessageFormat(target);
        if (msgfmt.getFormats().length > 0) {
            command = msgfmt.format(target, (Object[]) args);
            process = Runtime.getRuntime().exec(command);
        } else {
            ArrayList<String> list = new ArrayList<String>(Arrays.asList(args));
            list.add(0, target);
            process = Runtime.getRuntime().exec((String[]) list.toArray(new String[0]));
        }
        //Runtime.getRuntime().exec(args);
        return process;
    }

    public static File tempFile(byte[] buff, String prefix) {
        File file = null;
        FileOutputStream fostm = null;
        try {
            file = File.createTempFile(prefix, ".tmp");
            file.deleteOnExit();
            fostm = new FileOutputStream(file, true);
            fostm.write(buff);
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (fostm != null) {
                    fostm.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return file;
    }

    public static byte[] bytesFromFile(File file) throws IOException {
        ByteArrayOutputStream bostm = new ByteArrayOutputStream();
        try (FileInputStream fstm = new FileInputStream(file)) {
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = fstm.read(buff)) > 0) {
                bostm.write(buff, 0, len);
            }
        }
        return bostm.toByteArray();
    }

    public static File bytesToFile(byte[] bytes, File file) throws IOException {
        ByteArrayInputStream bostm = new ByteArrayInputStream(bytes);
        try (FileOutputStream fstm = new FileOutputStream(file)) {
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = bostm.read(buff)) > 0) {
                fstm.write(buff, 0, len);
            }
        }
        return file;
    }

    /* InputStream.readAllBytes は JDK 9 からサポート */
    public static byte[] readAllBytes(InputStream stream) throws IOException {
        ByteArrayOutputStream bostm = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len = 0;
        while ((len = stream.read(buff)) >= 0) {
            bostm.write(buff, 0, len);
        }
        return bostm.toByteArray();
    }

    private final static char[] NUM_CHARS = "1234567890".toCharArray();
    private final static char[] IDENT_CHARS
            = "_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public static String randomNumeric(int length) {
        StringBuilder buff = new StringBuilder();
        for (int i = 0; i < length; i++) {
            buff.append(NUM_CHARS[RANDOM.nextInt(NUM_CHARS.length)]);
        }
        return buff.toString();
    }

    public static String randomIdent(int length) {
        StringBuilder buff = new StringBuilder();
        for (int i = 0; i < length; i++) {
            buff.append(IDENT_CHARS[RANDOM.nextInt(IDENT_CHARS.length)]);
        }
        return buff.toString();
    }

    public static String randomCharRange(String range, int length) {
        char[] chars = range.toCharArray();
        StringBuilder buff = new StringBuilder();
        for (int i = 0; i < length; i++) {
            buff.append(chars[RANDOM.nextInt(chars.length)]);
        }
        return buff.toString();
    }

    public static String getStackTraceMessage(Exception ex) {
        return String.format("%s: %s", ex.getClass().getName(), ex.getMessage());
    }

    
    /**
     * @since 1.9.3
     */
    public static String getStackTrace(Throwable ex) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        ex.printStackTrace(printWriter);
        return result.toString();
    }
        
    public static Charset lookupCharset(String csn) {
        if (Charset.isSupported(csn)) {
            try {
                return Charset.forName(csn);
            } catch (UnsupportedCharsetException x) {
                return null;
            }
        }
        return null;
    }

}
