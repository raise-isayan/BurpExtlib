package extend.util;

import burp.BurpVersion;
import burp.IBurpExtenderCallbacks;
import burp.IContextMenuInvocation;
import burp.IExtensionHelpers;
import burp.IHttpRequestResponse;
import burp.IHttpService;
import burp.IRequestInfo;
import burp.IResponseInfo;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @author isayan
 */
public class BurpWrap {

    private static IBurpExtenderCallbacks callbacks = null;
    private static IExtensionHelpers helpers = null;

    public static void assained(IBurpExtenderCallbacks cb) {
        BurpWrap.callbacks = cb;
        BurpWrap.helpers = cb.getHelpers();
    }

    /**
     * burp version
     *
     * @param cb
     * @return
     */
    public static String[] getBurpVersion(IBurpExtenderCallbacks cb) {
        String[] version = new String[0];
        try {
            version = cb.getBurpVersion();
        } catch (AbstractMethodError ex) {
        } catch (IllegalArgumentException ex) {
        } catch (LinkageError ex) {
        } catch (Exception ex) {
        }
        return version;
    }

    public static boolean isInScope(java.net.URL url) {
        return callbacks.isInScope(url);
    }
    
    public static IHttpService getHttpService(final IHttpRequestResponse messageInfo) {
        try {
            return messageInfo.getHttpService();
        } catch (AbstractMethodError ex) {
        } catch (IllegalArgumentException ex) {
        } catch (LinkageError ex) {
        } catch (Exception ex) {
        }
        return null;
    }

    public static IHttpService getHttpService(final String host, final int port, final boolean useHttps) {
        return new IHttpService() {

            @Override
            public String getHost() {
                return host;
            }

            @Override
            public int getPort() {
                return port;
            }

            @Override
            public String getProtocol() {
                return HttpUtil.getDefaultProtocol(useHttps);
            }

        };
    }
    public static String getURLString(IHttpService httpService) {
        return String.format("%s://%s:%d", httpService.getProtocol(), httpService.getHost(), httpService.getPort()); 
    }
    
    public static URL getURL(IHttpRequestResponse messageInfo) {
        if (helpers == null) {
            throw new NullPointerException("There is a need to call the 'assained' method");
        }
        IRequestInfo reqInfo = helpers.analyzeRequest(messageInfo.getHttpService(), messageInfo.getRequest());
        return reqInfo.getUrl();
    }

    public static URL getURL(byte[] request) {
        if (helpers == null) {
            throw new NullPointerException("There is a need to call the 'assained' method");
        }
        IRequestInfo reqInfo = helpers.analyzeRequest(request);
        return reqInfo.getUrl();
    }

    public static IHttpService getHttpService(final String host, final int port, final String protocol) {
        return new IHttpService() {

            @Override
            public String getHost() {
                return host;
            }

            @Override
            public int getPort() {
                return port;
            }

            @Override
            public String getProtocol() {
                return protocol;
            }

        };
    }

//    protected byte [] getMessage(IContextMenuInvocation contextMenu) {
//        byte context = contextMenu.getInvocationContext();
//        byte message[] = new byte [0];
//        if (context == IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_REQUEST || context == IContextMenuInvocation.CONTEXT_MESSAGE_VIEWER_REQUEST) {
//            message = messageInfo[0].getRequest();
//        }
//        else if (context == IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_RESPONSE || context == IContextMenuInvocation.CONTEXT_MESSAGE_VIEWER_RESPONSE) {
//            message = messageInfo[0].getResponse();                
//        }
//        return message;    
//    }
    public static String copySelectionData(IContextMenuInvocation contextMenu, boolean selectionTextOnly) {
        String text = null;
        byte context = contextMenu.getInvocationContext();
        int se[] = contextMenu.getSelectionBounds();
        if (se == null && selectionTextOnly) {
            return null;
        }
        IHttpRequestResponse[] messageInfo = contextMenu.getSelectedMessages();
        byte message[] = new byte[0];
        if (context == IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_REQUEST || context == IContextMenuInvocation.CONTEXT_MESSAGE_VIEWER_REQUEST) {
            message = messageInfo[0].getRequest();
        } else if (context == IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_RESPONSE || context == IContextMenuInvocation.CONTEXT_MESSAGE_VIEWER_RESPONSE) {
            message = messageInfo[0].getResponse();
        }
        if (message != null) {
            if (se == null) {
                text = Util.decodeMessage(message, StandardCharsets.ISO_8859_1);
            } else {
                text = Util.decodeMessage(message, se[0], se[1] - se[0], StandardCharsets.ISO_8859_1);
            }
        }
        return text;
    }

    public static void pasteSelectionData(IContextMenuInvocation contextMenu, String text, boolean selectionTextOnly) {
        int se[] = contextMenu.getSelectionBounds();
        byte context = contextMenu.getInvocationContext();
        if (se == null && selectionTextOnly) {
            return;
        }
        IHttpRequestResponse[] messageInfo = contextMenu.getSelectedMessages();
        byte message[] = new byte[0];
        if (context == IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_REQUEST || context == IContextMenuInvocation.CONTEXT_MESSAGE_VIEWER_REQUEST) {
            message = messageInfo[0].getRequest();
        } else if (context == IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_RESPONSE || context == IContextMenuInvocation.CONTEXT_MESSAGE_VIEWER_RESPONSE) {
            message = messageInfo[0].getResponse();
        }
        if (message != null) {
            if (se == null) {
                // nothing
            } else {
                text = Util.decodeMessage(Util.byteReplace(message, se[0], se[1], Util.getRawByte(text)), StandardCharsets.ISO_8859_1);
            }
        }
        if (context == IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_REQUEST || context == IContextMenuInvocation.CONTEXT_MESSAGE_VIEWER_REQUEST) {
            messageInfo[0].setRequest(Util.encodeMessage(text));
        } else if (context == IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_RESPONSE || context == IContextMenuInvocation.CONTEXT_MESSAGE_VIEWER_RESPONSE) {
            messageInfo[0].setResponse(Util.encodeMessage(text));
        }
    }

    /**
     * burp support version is 1.4.03 over
     *
     * @param msg
     * @return
     */
    public static String getHighlightColor(IHttpRequestResponse msg) {
        String color = null;
        try {
            color = msg.getHighlight();
        } catch (AbstractMethodError ex) {
        } catch (IllegalArgumentException ex) {
        } catch (LinkageError ex) {
        } catch (Exception ex) {
        }
        return color;
    }

    /**
     * burp support version is 1.4.03 over
     *
     * @param msg
     * @param color
     */
    public static void setHighlightColor(IHttpRequestResponse msg, String color) {
        try {
            msg.setHighlight(color);
        } catch (AbstractMethodError ex) {
        } catch (IllegalArgumentException ex) {
        } catch (LinkageError ex) {
        } catch (Exception ex) {
        }
    }

    public static byte[] getRequestBody(IHttpService httpService, byte[] content) {
        IRequestInfo reqInfo = helpers.analyzeRequest(httpService, content);
        return getRequestBody(reqInfo, content);
    }

    public static byte[] getRequestBody(IRequestInfo reqInfo, byte[] content) {
        return Arrays.copyOfRange(content, reqInfo.getBodyOffset(), content.length);
    }

    public static byte[] getResponseBody(byte[] content) {
        IResponseInfo resInfo = helpers.analyzeResponse(content);
        return getResponseBody(resInfo, content);
    }

    public static byte[] getResponseBody(IResponseInfo resInfo, byte[] content) {
        return Arrays.copyOfRange(content, resInfo.getBodyOffset(), content.length);
    }

    public final static class Version implements BurpVersion {

        private final IBurpExtenderCallbacks callbacks;
        private String productName = "";
        private String majorVersion = "";
        private String minorVersion = "";

        public Version(IBurpExtenderCallbacks cb) {
            callbacks = cb;
            parseVersion(cb);
        }

        void parseVersion(IBurpExtenderCallbacks cb) {
            String[] version = getBurpVersion(cb);
            if (version.length >= 3) {
                this.productName = version[0];
                this.majorVersion = version[1];
                this.minorVersion = version[2];
            }
        }

        public String getProductName() {
            return this.productName;
        }

        public String getMajor() {
            return this.majorVersion;
        }

        public String getMinor() {
            return this.minorVersion;
        }

        public int getMajorVersion() {
            String majorver = this.majorVersion.replaceAll("\\.", "");
            return Util.parseIntDefault(majorver, 0);
        }

        public int getMinorVersion() {
            return Util.parseIntDefault(this.minorVersion, 0);
        }

        public boolean isFreeVersion() {
            return !this.isProfessional();
        }

        public boolean isExtendSupport() {
            return ((this.getMajorVersion() == 16) && (this.getMinorVersion() > 0)) || ((this.getMajorVersion() > 0));
        }

        public boolean isProfessional() {
            return (0 <= this.productName.indexOf("Professional"));
        }


    }

    public static String parseFilterPattern(String pattern) {
        String[] extentions = pattern.split(",");
        StringBuilder buff = new StringBuilder();
        if (extentions.length == 1 && extentions[0].equals("")) {
            return buff.toString();
        }
        buff.append("\\.");
        buff.append("(");
        for (int i = 0; i < extentions.length; i++) {
            if (extentions[i].length() > 0) {
                if (i > 0) {
                    buff.append("|");
                }
                buff.append(extentions[i]);
            }
        }
        buff.append(")$");
        return buff.toString();
    }
    
    public static IHttpRequestResponse syncRequest(IHttpService httpService, byte[] request) throws ExecutionException {
        return syncRequest(new TestRequest(httpService, request));
    }

    public static IHttpRequestResponse syncRequest(TestRequest testRequest) throws ExecutionException {
        try {
            final ExecutorService executor = Executors.newSingleThreadExecutor();
            FutureTask<IHttpRequestResponse> task = new FutureTask(testRequest);
            executor.submit(task);
            IHttpRequestResponse message = task.get();        
            return message;
        } catch (InterruptedException ex) {
            throw new ExecutionException(ex);
        }
    }
    
    public static class TestRequest implements Callable<IHttpRequestResponse> {
        private IHttpService httpService = null;
        private byte[] request = new byte[0];

        public TestRequest(IHttpService httpService, byte[] request) {
            this.httpService = httpService;
            this.request = request;
        }

        public TestRequest(String host,
                int port,
                boolean useHttps,
                byte[] request) {
            if (helpers == null) {
                throw new NullPointerException("There is a need to call the 'assained' method");
            }
            this.httpService = helpers.buildHttpService(host, port, useHttps);
            this.request = request;
        }

        public IHttpService getHttpService() {
            return this.httpService;
        }
        
        public byte [] getRequest() {
            return this.request;
        }
        
        @Override
        public IHttpRequestResponse call() throws Exception {
            if (callbacks != null) {
                IHttpRequestResponse message = callbacks.makeHttpRequest(this.httpService, this.request);            
                return message;            
            }
            return null;
        }

    }

}
