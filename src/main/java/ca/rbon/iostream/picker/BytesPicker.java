package ca.rbon.iostream.picker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import ca.rbon.iostream.Chain;
import ca.rbon.iostream.CodeFlowError;
import ca.rbon.iostream.fluent.InOutPick;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BytesPicker extends BasePicker<byte[]> implements InOutPick<byte[]> {
    
    public static final int DEFAULT_CAPACITY = -1;
    
    private static final String NO_BYTE_ARRAY_SET = "No byte array was provided for this operation.";
    
    final byte[] bytes;
    
    final int initialCapacity;
    
    ByteArrayOutputStream outStream;
    
    public byte[] getResource() {
        if (outStream != null) {
            return outStream.toByteArray();
        }
        if (bytes == null) {
            throw new CodeFlowError(NO_BYTE_ARRAY_SET);
        }
        return bytes;
    }
    
    @Override
    protected ByteArrayInputStream getInputStream() throws IOException {
        if (bytes == null) {
            throw new CodeFlowError(NO_BYTE_ARRAY_SET);
        }
        return new ByteArrayInputStream(bytes);
    }
    
    @Override
    protected Reader getReader(Chain chain) throws IOException {
        return null;
    }
    
    @Override
    protected ByteArrayOutputStream getOutputStream() throws IOException {
        outStream = initialCapacity > DEFAULT_CAPACITY
                ? new ByteArrayOutputStream(initialCapacity)
                : new ByteArrayOutputStream();
        return outStream;
    }
    
    @Override
    protected Writer getWriter(Chain chain) throws IOException {
        return null;
    }
    
}