package ca.rbon.quack;

import java.nio.ByteBuffer;

public interface Hasher {

    default Hasher update(byte[] data) {
        return update(data, 0, data.length);
    }

    Hasher update(byte[] bytes, int offset, int length);

    Hasher update(ByteBuffer bytes);

    byte[] digest();
}
