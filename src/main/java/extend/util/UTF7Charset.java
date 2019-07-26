package extend.util;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class UTF7Charset extends Charset {

    static final int MAX_UTF7_CHAR_VALUE = 0x7f;
    static final char BEGIN_SHIFT = '+';
    static final char END_SHIFT = '-';
    static final byte[] BASE_64 = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', // 0
        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', // 1
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', // 2
        'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', // 3
        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', // 4
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v', // 5
        'w', 'x', 'y', 'z', '0', '1', '2', '3', // 6
        '4', '5', '6', '7', '8', '9', '+', '/' // 7
    };
    protected static final byte INVERSE_BASE_64[] = new byte[128];
    protected static final byte NON_BASE_64 = -1;
    protected static final boolean NO_SHIFT_REQUIRED[] = new boolean[128];

    static {
        for (int i = 0; i < INVERSE_BASE_64.length; i++) {
            INVERSE_BASE_64[i] = NON_BASE_64;
        }
        for (byte i = 0; i < BASE_64.length; i++) {
            INVERSE_BASE_64[BASE_64[i]] = i;
        }

        final String unshifted = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789'(),-./:? \t\r\n";
        for (int i = 0; i < unshifted.length(); i++) {
            NO_SHIFT_REQUIRED[unshifted.charAt(i)] = true;
        }
    }

    public UTF7Charset(String canonicalName, String[] aliases) {
        super(canonicalName, aliases);
    }

    @Override
    public boolean contains(Charset cs) {
        return true;
    }

    @Override
    public CharsetDecoder newDecoder() {
        return new UTF7Decoder(this);
    }

    @Override
    public CharsetEncoder newEncoder() {
        return new UTF7Encoder(this);
    }
}
