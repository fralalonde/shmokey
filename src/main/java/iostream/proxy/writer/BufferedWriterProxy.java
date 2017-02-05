package iostream.proxy.writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

import iostream.Closer;
import iostream.SinkTarget;
import lombok.Getter;

public class BufferedWriterProxy<T> extends BufferedWriter implements SinkTarget<T> {

    final Closer closer;

    @Getter
    final SinkTarget<T> realTarget;

    public BufferedWriterProxy(SinkTarget<T> t, Closer cl, Writer wr) throws IOException {
	super(wr);
	realTarget = t;
	cl.register(wr);
	closer = cl;
    }

    public void close() {
	closer.closeAll();
    }

    @Override
    public T getTarget() {
	return realTarget.getTarget();
    }


}