package extend.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

public class UTF7Decoder extends CharsetDecoder {

    private boolean shifted = false, first = false;
    private int decoder = 0, bits = 0;

    protected UTF7Decoder(Charset cs) {
        super(cs, (float) 0.4, 1);
    }

    @Override
    protected void implReset() {
        shifted = first = false;
        decoder = bits = 0;
    }

    @Override
    protected CoderResult implFlush(CharBuffer out) {
        if (shifted && decoder != 0) {
            return CoderResult.malformedForLength(0);
        }
        return CoderResult.UNDERFLOW;
    }

    @Override
    protected CoderResult decodeLoop(ByteBuffer in, CharBuffer out) {
        while (in.hasRemaining()) {
            if (!out.hasRemaining()) {
                return CoderResult.OVERFLOW;
            }
            byte c = in.get();
            if (c > UTF7Charset.MAX_UTF7_CHAR_VALUE) {
                return CoderResult.malformedForLength(0);
            }
            if (shifted) {
                byte decodedChar = UTF7Charset.INVERSE_BASE_64[c];
                if (decodedChar == UTF7Charset.NON_BASE_64) {
                    shifted = false;
                    if (first && c == UTF7Charset.END_SHIFT) {
                        out.put(UTF7Charset.BEGIN_SHIFT);
                    }
                    if (decoder != 0) {
                        return CoderResult.malformedForLength(0);
                    }
                    bits = 0;
                    if (c == UTF7Charset.END_SHIFT) {
                        continue;
                    }
                } else {
                    decoder = (decoder << 6) | decodedChar;
                    first = false;
                    bits += 6;
                    if (bits >= 16) {
                        out.put((char) (decoder >> (bits - 16)));
                        decoder &= ~(0xFFFF << (bits - 16));
                        bits -= 16;
                    }
                }
            }

            if (!shifted) {
                if (c == UTF7Charset.BEGIN_SHIFT) {
                    shifted = first = true;
                } else {
                    out.put((char) c);
                }
            }
        }
        return CoderResult.UNDERFLOW;
    }
}
