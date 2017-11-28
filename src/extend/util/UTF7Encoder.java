package extend.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

public class UTF7Encoder extends CharsetEncoder {

    private boolean shifted = false;
    private int encoder = 0, bits = 0;

    protected UTF7Encoder(Charset cs) {
        super(cs, (float) 2.5, 5);
    }

    @Override
    protected void implReset() {
        shifted = false;
        encoder = bits = 0;
    }

    @Override
    protected CoderResult implFlush(ByteBuffer out) {
        if (shifted) {
            if (out.remaining() < 2) {
                return CoderResult.OVERFLOW;
            }
            if (bits > 0) {
                encoder <<= (6 - bits);
                out.put(UTF7Charset.BASE_64[encoder]);
                encoder = bits = 0;
            }
            out.put((byte) UTF7Charset.END_SHIFT);
            shifted = false;
        }
        return CoderResult.UNDERFLOW;
    }

    @Override
    protected CoderResult encodeLoop(CharBuffer in, ByteBuffer out) {
        while (in.hasRemaining()) {
            if (out.remaining() < 4) {
                return CoderResult.OVERFLOW;
            }
            char c = in.get();
            boolean needsShift = c > UTF7Charset.MAX_UTF7_CHAR_VALUE || !UTF7Charset.NO_SHIFT_REQUIRED[c];

            if (needsShift && !shifted) {
                out.put((byte) UTF7Charset.BEGIN_SHIFT);
                if (c == UTF7Charset.BEGIN_SHIFT) {
                    out.put((byte) UTF7Charset.END_SHIFT);
                } else {
                    shifted = true;
                }
            }

            if (shifted) {
                if (needsShift) {
                    encoder = (encoder << 16) | c;
                    bits += 16;
                    do {
                        out.put(UTF7Charset.BASE_64[0x3F & (encoder >> (bits - 6))]);
                        bits -= 6;
                    } while (bits >= 6);
                    encoder &= (0x3F >> (6 - bits));
                } else {
                    implFlush(out);
                }
            }

            if (!needsShift) {
                out.put((byte) c);
            }
        }
        return implFlush(out);
    }
}
