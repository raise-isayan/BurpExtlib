/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package burp;

import burp.IBurpExtender;
import burp.IBurpExtenderCallbacks;
import extend.util.BurpWrap;
import extend.util.Util;
import java.awt.TrayIcon;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author t.isayama
 */
public class BurpExtenderImpl implements IBurpExtender {
    private static BurpExtenderImpl extenderImpl;
    private static IBurpExtenderCallbacks callbacks;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks cb) {
        extenderImpl = this;
        callbacks = cb;
        BurpWrap.assained(cb);
        return;
    }
    
    public static <T extends BurpExtenderImpl> T getInstance() {
        return (T)extenderImpl;
    }

    public static IBurpExtenderCallbacks getCallbacks() {
        return callbacks;
    }
    
    public static IExtensionHelpers getHelpers() {
        if (callbacks != null) {
            try {
                return callbacks.getHelpers();
            } catch (AbstractMethodError ex) {
            } catch (IllegalArgumentException ex) {
            } catch (LinkageError ex) {
            } catch (Exception ex) {
            }
            return null;
        } else {
            return null;
        }
    }
    
    public static void outPrintln(String message) throws IOException {
        outPrint(message + "\n");
    }

    public static void outPrint(String message) throws IOException {
        OutputStream ostm = callbacks.getStdout();
        byte b[] = Util.getRawByte(message);
        ostm.write(b, 0, b.length);
    }

    public static void errPrint(String message) throws IOException {
        OutputStream ostm = callbacks.getStderr();
        byte b[] = Util.getRawByte(message);
        ostm.write(b, 0, b.length);
    }

    public static void errPrintln(String message) throws IOException {
        errPrint(message + "\n");
    }

    /**
     * burp alert 通知
     *
     * @param caption キャプション
     * @param text テキスト
     * @param messageType メッセージタイプ
     */
    public static void issueAlert(String caption, String text, TrayIcon.MessageType messageType) {
        if (callbacks != null) {
            callbacks.issueAlert(String.format("[%s] %s:%s", messageType, caption, text));
        }
    }
    
}