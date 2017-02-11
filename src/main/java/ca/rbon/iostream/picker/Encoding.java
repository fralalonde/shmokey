package ca.rbon.iostream.picker;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

public class Encoding {
    
    public static final Charset DEFAULT_CHARSET = null;
    
    static InputStreamReader streamReader(InputStream stream, Charset charset) {
        return charset == DEFAULT_CHARSET
                ? new InputStreamReader(stream)
                : new InputStreamReader(stream, charset);
    }
    
    static OutputStreamWriter streamWriter(OutputStream stream, Charset charset) {
        return charset == DEFAULT_CHARSET
                ? new OutputStreamWriter(stream)
                : new OutputStreamWriter(stream, charset);
    }
    
}