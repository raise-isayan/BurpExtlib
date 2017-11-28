/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extend.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author isayan
 */
public final class HashUtil {

    /**
     * ハッシュ値の取得
     *
     * @param algorithm
     * @param str 対象文字列
     * @param enc エンコーディング
     * @return ハッシュ値
     * @throws UnsupportedEncodingException
     * @throws java.security.NoSuchAlgorithmException
     */
    public static String toMessageDigest(String algorithm, String str, String enc, boolean upperCase)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return toMessageDigest(algorithm, str.getBytes(enc), upperCase);
    }

    public static String toMessageDigest(String algorithm, byte body[], boolean upperCase)
            throws NoSuchAlgorithmException {
        String digeststr = "";
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.reset();
        md.update(body);
        digeststr = javax.xml.bind.DatatypeConverter.printHexBinary(md.digest());
        if (upperCase) {
            return digeststr;
        }
        else {
            return digeststr.toLowerCase();        
        }
    }

    /**
     * MD2値の取得
     *
     * @param body 対象バイト
     * @return ハッシュ値
     */
    public static String toMd2Sum(byte[] body, boolean upperCase) {
        try {
            return toMessageDigest("MD2", body, upperCase);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

    /**
     * MD2値の取得
     *
     * @param str 対象文字列
     * @return ハッシュ値
     */
    public static String toMd2Sum(String str, boolean upperCase) {
        try {
            return toMessageDigest("MD2", str, "8859_1", upperCase);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }

    /**
     * MD2値の取得
     *
     * @param str 対象文字列
     * @param enc エンコーディング
     * @return ハッシュ値
     * @throws UnsupportedEncodingException
     */
    public static String toMd2Sum(String str, String enc, boolean upperCase)
            throws UnsupportedEncodingException {
        try {
            return toMessageDigest("MD2", str, enc, upperCase);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

    /**
     * MD5値の取得
     *
     * @param body 対象バイト
     * @return ハッシュ値
     */
    public static String toMd5Sum(byte[] body, boolean upperCase) {
        try {
            return toMessageDigest("MD5", body, upperCase);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

    /**
     * MD5値の取得
     *
     * @param str 対象文字列
     * @return ハッシュ値
     */
    public static String toMd5Sum(String str, boolean upperCase) {
        try {
            return toMessageDigest("MD5", str, "8859_1", upperCase);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }

    /**
     * MD5値の取得
     *
     * @param str 対象文字列
     * @param enc エンコーディング
     * @return ハッシュ値
     * @throws UnsupportedEncodingException
     */
    public static String toMd5Sum(String str, String enc, boolean upperCase)
            throws UnsupportedEncodingException {
        try {
            return toMessageDigest("MD5", str, enc, upperCase);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

    /**
     * SHA-1値の取得
     *
     * @param body 対象バイト
     * @return ハッシュ値
     */
    public static String toSHA1Sum(byte[] body, boolean upperCase) {
        try {
            return toMessageDigest("SHA-1", body, upperCase);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

    /**
     * SHA-1値の取得
     *
     * @param str 対象文字列
     * @return ハッシュ値
     */
    public static String toSHA1Sum(String str, boolean upperCase) {
        try {
            return toMessageDigest("SHA-1", str, "8859_1", upperCase);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }

    /**
     * SHA-1値の取得
     *
     * @param str 対象文字列
     * @param enc エンコーディング
     * @return ハッシュ値
     * @throws UnsupportedEncodingException
     */
    public static String toSHA1Sum(String str, String enc, boolean upperCase)
            throws UnsupportedEncodingException {
        try {
            return toMessageDigest("SHA-1", str, enc, upperCase);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

    /**
     * SHA-256値の取得
     *
     * @param body 対象バイト
     * @return ハッシュ値
     */
    public static String toSHA256Sum(byte[] body, boolean upperCase) {
        try {
            return toMessageDigest("SHA-256", body, upperCase);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

    /**
     * SHA-256値の取得
     *
     * @param str 対象文字列
     * @return ハッシュ値
     */
    public static String toSHA256Sum(String str, boolean upperCase) {
        try {
            return toMessageDigest("SHA-256", str, "8859_1", upperCase);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }

    /**
     * SHA-256値の取得
     *
     * @param str 対象文字列
     * @param enc エンコーディング
     * @return ハッシュ値
     * @throws UnsupportedEncodingException
     */
    public static String toSHA256Sum(String str, String enc, boolean upperCase)
            throws UnsupportedEncodingException {
        try {
            return toMessageDigest("SHA-256", str, enc, upperCase);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

    /**
     * SHA-384値の取得
     *
     * @param body 対象バイト
     * @return ハッシュ値
     */
    public static String toSHA384Sum(byte[] body, boolean upperCase) {
        try {
            return toMessageDigest("SHA-384", body, upperCase);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

    /**
     * SHA-384値の取得
     *
     * @param str 対象文字列
     * @return ハッシュ値
     */
    public static String toSHA384Sum(String str, boolean upperCase) {
        try {
            return toMessageDigest("SHA-384", str, "8859_1", upperCase);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }

    /**
     * SHA-384値の取得
     *
     * @param str 対象文字列
     * @param enc エンコーディング
     * @return ハッシュ値
     * @throws UnsupportedEncodingException
     */
    public static String toSHA384Sum(String str, String enc, boolean upperCase)
            throws UnsupportedEncodingException {
        try {
            return toMessageDigest("SHA-384", str, enc, upperCase);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

    /**
     * SHA-512値の取得
     *
     * @param body 対象バイト
     * @return ハッシュ値
     */
    public static String toSHA512Sum(byte[] body, boolean upperCase) {
        try {
            return toMessageDigest("SHA-512", body, upperCase);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

    /**
     * SHA-512値の取得
     *
     * @param str 対象文字列
     * @return ハッシュ値
     */
    public static String toSHA512Sum(String str, boolean upperCase) {
        try {
            return toMessageDigest("SHA-512", str, "8859_1", upperCase);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }

    /**
     * SHA-512値の取得
     *
     * @param str 対象文字列
     * @param enc エンコーディング
     * @return ハッシュ値
     * @throws UnsupportedEncodingException
     */
    public static String toSHA512Sum(String str, String enc, boolean upperCase)
            throws UnsupportedEncodingException {
        try {
            return toMessageDigest("SHA-512", str, enc, upperCase);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
    
}
