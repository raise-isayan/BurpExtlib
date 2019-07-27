package burp;

import extend.util.BurpWrap;
import extend.util.Util;
import java.awt.TrayIcon;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author isayan
 */
public class BurpExtenderImpl implements IBurpExtender {

    private static BurpExtenderImpl extenderImpl;
    private static IBurpExtenderCallbacks callbacks;
    private BurpVersion burp_version = null;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks cb) {
        extenderImpl = this;
        callbacks = cb;
        BurpWrap.assained(cb);
        burp_version = new BurpWrap.Version(cb);
        return;
    }

    public static <T extends BurpExtenderImpl> T getInstance() {
        return (T) extenderImpl;
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

    public BurpVersion getBurpVersion() {
        return burp_version;
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