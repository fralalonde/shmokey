package ca.rbon.quack;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;

class Murmur3ArrayHasher implements Hasher {
    int h1;
    int tailLen; // 0..3
    long bytes;
    final byte[] tail = new byte[4];

    Murmur3ArrayHasher(int seed) {
        h1 = seed;
    }

    void chug(byte a, byte b, byte c, byte d) {
        // little endian load order
        int k1 = (a & 0xff) | ((b & 0xff) << 8) | ((c & 0xff) << 16) | (d << 24);
        h1 = Murmur3.churn(k1, h1);
    }

    @Override
    public Hasher update(ByteBuffer data) {
        var array = new byte[data.limit()];
        data.get(array);
        return update(array);
    }

    @Override
    @SuppressWarnings("fallthrough")
    public Hasher update(byte[] data, int offset, int len) {
        if (len == 0) return this;
        if (tailLen > 0) {
            int required = 4 - tailLen;
            int avail = Math.min(len, required);
            System.arraycopy(data, offset, tail, tailLen, avail);
            chug(tail[0], tail[1], tail[2], tail[3]);
            bytes += 4;
            offset += avail;
            len -= avail;
        }

        int roundedLen = len & 0xfffffffc; // round down to 4 byte block
        int roundedEnd = offset + roundedLen;
        for (int i = offset; i < roundedEnd; i += 4) {
            chug(data[i], data[i + 1], data[i + 2], data[i + 3]);
        }
        bytes += roundedLen;

        tailLen = len & 0x03;
        switch (tailLen) {
            case 3: tail[2] = data[roundedEnd + 2];
            case 2: tail[1] = data[roundedEnd + 1];
            case 1: tail[0] = data[roundedEnd];
        }

        return this;
    }

    @SuppressWarnings("fallthrough")
    public byte[] digest() {
        int k1 = 0;

        switch(tailLen) {
            case 3:
                k1 = (tail[2] & 0xff) << 16;
            case 2:
                k1 |= (tail[1] & 0xff) << 8;
            case 1:
                k1 |= (tail[0] & 0xff);
                k1 *= Murmur3.c1;
                k1 = (k1 << 15) | (k1 >>> 17);  // ROTL32(k1,15);
                k1 *= Murmur3.c2;
                h1 ^= k1;
        }
        bytes += tailLen;
        return Murmur3.finish(h1, bytes);
    }
}
