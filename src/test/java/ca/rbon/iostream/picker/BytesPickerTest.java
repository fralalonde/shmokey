package ca.rbon.iostream.picker;

import static ca.rbon.iostream.IoStream.bytes;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import ca.rbon.iostream.IoStream;
import ca.rbon.iostream.writer.BufferedWriterOf;

public class BytesPickerTest {
    
    @Test
    public void recreate() throws IOException {
        BufferedWriterOf<byte[]> w = IoStream.bytes().bufferedWriter(4);
        try {
            w.append("eee");
            assertThat(bytes(w.getResource()).reader().read()).isEqualTo(-1);
            w.append("eee");
            assertThat(bytes(w.getResource()).reader().read()).isEqualTo(-1);
            w.flush();
            assertThat(bytes(w.getResource()).reader().read()).isEqualTo(101);
        } finally {
            w.close();
        }
    }
    
}
