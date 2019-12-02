package ca.rbon.quack;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class Murmur3Test {

    static byte[] ODD = {44, 55, 66, 77, 88, 99};
    static byte[] ODD_1 = {44, 55, 66};
    static byte[] ODD_2 = {77, 88, 99};

    static byte[] EVEN = {33, 44, 55, 66, 77, 88, 99, 11};
    static byte[] EVEN_1 = {33, 44, 55, 66};
    static byte[] EVEN_2 = {77, 88, 99, 11};

    @Test
    public void arrayOdd() {
        var bytes = Murmur3.arrayHasher()
                .update(ODD_1, 0, ODD_1.length)
                .update(ODD_2, 0, ODD_2.length)
                .digest();
        assertArrayEquals(Murmur3.murmurhash3_x86_32(ODD, 0, ODD.length, 0), bytes);
    }

    @Test
    public void arrayEven() {
        var bytes = Murmur3.arrayHasher()
                .update(EVEN_1, 0, EVEN_1.length)
                .update(EVEN_2, 0, EVEN_2.length)
                .digest();
        assertArrayEquals(Murmur3.murmurhash3_x86_32(EVEN, 0, EVEN.length, 0), bytes);
    }

    @Test
    public void arrayOne() {
        var bytes = Murmur3.arrayHasher()
                .update(new byte[]{1})
                .digest();
        assertArrayEquals(Murmur3.murmurhash3_x86_32(new byte[]{1}, 0, 1, 0), bytes);
    }

    @Test
    public void arrayFour() {
        var bytes = Murmur3.arrayHasher()
                .update(new byte[]{1,1,1,1})
                .digest();
        assertArrayEquals(Murmur3.murmurhash3_x86_32(new byte[]{1,1,1,1}, 0, 4, 0), bytes);
    }

    @Test
    public void bufferOdd() {
        var bytes = Murmur3.bufferHasher()
                .update(ODD_1, 0, ODD_1.length)
                .update(ODD_2, 0, ODD_2.length)
                .digest();
        assertArrayEquals(Murmur3.murmurhash3_x86_32(ODD, 0, ODD.length, 0), bytes);
    }

    @Test
    public void bufferEven() {
        var bytes = Murmur3.bufferHasher()
                .update(EVEN_1, 0, EVEN_1.length)
                .update(EVEN_2, 0, EVEN_2.length)
                .digest();
        assertArrayEquals(Murmur3.murmurhash3_x86_32(EVEN, 0, EVEN.length, 0), bytes);
    }

    @Test
    public void bufferOne() {
        var bytes = Murmur3.bufferHasher()
                .update(new byte[]{1})
                .digest();
        assertArrayEquals(Murmur3.murmurhash3_x86_32(new byte[]{1}, 0, 1, 0), bytes);
    }

    @Test
    public void bufferFour() {
        var bytes = Murmur3.bufferHasher()
                .update(new byte[]{1,1,1,1})
                .digest();
        assertArrayEquals(Murmur3.murmurhash3_x86_32(new byte[]{1,1,1,1}, 0, 4, 0), bytes);
    }

}