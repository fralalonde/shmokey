package ca.rbon.iostream.picker;

import java.io.IOException;
import java.nio.CharBuffer;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import ca.rbon.iostream.IoStream;
import ca.rbon.iostream.writer.ReaderOf;
import ca.rbon.iostream.writer.WriterOf;

public class StringPickerTest {
    
    @Test
    public void create() throws IOException {
        WriterOf<String> w = IoStream.string().writer();
        try {
            w.append("eee");
            Assertions.assertThat(w.getResource()).isEqualTo("eee");
        } finally {
            w.close();
        }
    }
    
    @Test
    public void append() throws IOException {
        WriterOf<String> w = IoStream.string("AA").writer();
        try {
            w.append("eee");
            Assertions.assertThat(w.getResource()).isEqualTo("AAeee");
        } finally {
            w.close();
        }
    }

    @Test
    public void read() throws IOException {
        ReaderOf<String> r = IoStream.string("AA").reader();
        try {
            CharBuffer cb =  CharBuffer.allocate(4);
            Assertions.assertThat(r.read(cb)).isEqualTo(2);
            cb.flip();
            Assertions.assertThat(cb.toString()).isEqualTo("AA");
        } finally {
            r.close();
        }
    }
    
    
}
