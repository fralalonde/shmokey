package ca.rbon.quack;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class Murmur3BufferHasher implements Hasher {
    int h1;
    long bytes;
    final ByteBuffer tail = ByteBuffer.allocate(4);

    Murmur3BufferHasher(int seed) {
        h1 = seed;
    }

    void chug(ByteBuffer buf) {
        h1 = Murmur3.churn(buf.getInt(), h1);
    }

    public Hasher update(ByteBuffer data) {
        if (data.order() == ByteOrder.BIG_ENDIAN)
            data = data.order(ByteOrder.LITTLE_ENDIAN);
        if (data.remaining() == 0) return this;
        var start = data.position();
        if (tail.position() > 0) {
            while (tail.remaining() > 0 && data.remaining() > 0) {
                tail.put(data.get());
            }
            tail.flip();
            // FIXME tail buffer is not filled in correct byte order?
            chug(tail);
            tail.clear();
        }

        while (data.remaining() >= 4) {
            chug(data);
        }
        bytes += data.position() - start;

        while (data.remaining() > 0) {
            tail.put(data.get());
        }

        return this;
    }

    public Hasher update(byte[] data, int offset, int len) {
        return update(ByteBuffer.wrap(data, offset, len));
    }

    @SuppressWarnings("fallthrough")
    public byte[] digest() {
        int k1 = 0;

        tail.flip();
        switch(tail.limit()) {
            case 3:
                k1 = (tail.get(2) & 0xff) << 16;
            case 2:
                k1 |= (tail.get(1) & 0xff) << 8;
            case 1:
                k1 |= (tail.get(0) & 0xff);
                k1 *= Murmur3.c1;
                k1 = (k1 << 15) | (k1 >>> 17);  // ROTL32(k1,15);
                k1 *= Murmur3.c2;
                h1 ^= k1;
        }
        bytes += tail.limit();

        // this might just be an intermediate hash, go back to writing
        tail.flip();

        return Murmur3.finish(h1, bytes);
    }
}
