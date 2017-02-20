package ca.rbon.iostream.resource;

import java.io.IOException;
import java.nio.CharBuffer;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import ca.rbon.iostream.CodeFlowError;
import ca.rbon.iostream.IoStream;
import ca.rbon.iostream.wrap.ReaderOf;
import ca.rbon.iostream.wrap.WriterOf;

public class StringTest {
    
    @Test(expected = CodeFlowError.class)
    public void nullString() throws IOException {
        new StringResource(null, 0).getInputStream();
    }
    
    @Test(expected = CodeFlowError.class)
    public void nullBuffer() throws IOException {
        new StringResource(null, 0).getResource();
    }
    
    @Test
    public void create() throws IOException {
        WriterOf<String> w = IoStream.string().writer();
        try {
            w.append("eee");
            Assertions.assertThat(w.get()).isEqualTo("eee");
        } finally {
            w.close();
        }
    }
    
    @Test
    public void append() throws IOException {
        WriterOf<String> w = IoStream.string("AA").writer();
        try {
            w.append("eee");
            Assertions.assertThat(w.get()).isEqualTo("AAeee");
        } finally {
            w.close();
        }
    }
    
    @Test
    public void appendCapacity() throws IOException {
        WriterOf<String> w = IoStream.string("AA", 3).writer();
        try {
            w.append("eee");
            Assertions.assertThat(w.get()).isEqualTo("AAeee");
        } finally {
            w.close();
        }
    }
    
    @Test
    public void read() throws IOException {
        ReaderOf<String> r = IoStream.string("AA").reader();
        try {
            CharBuffer cb = CharBuffer.allocate(4);
            Assertions.assertThat(r.read(cb)).isEqualTo(2);
            cb.flip();
            Assertions.assertThat(cb.toString()).isEqualTo("AA");
        } finally {
            r.close();
        }
    }
    
}
