package iostream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Files;

import org.junit.Test;

import iostream.proxy.stream.DataOutputProxy;
import iostream.proxy.writer.PrintWriterProxy;

public class IoStreamsTest {

    @Test
    public void test() throws IOException {
	Writer w = IoStreams.file("doum.txt").printWriter();
	w.append("aa");
	
	IoStreams.bytes().output();
	byte[] bytes = IoStreams.bytes().dataOutput().getSubject(); 	
	
	IoStreams.file("doum.zip").zipInput();
	IoStreams.file("dam.txt", true).bufferedWriter();

	DataOutputProxy<File> tmpout = IoStreams.tempFile().dataOutput();
	tmpout.write(42);
	String tmpFilename =tmpout.getSubject().getAbsolutePath();

	IoStreams.string("agaga gogo").bufferedReader();
	IoStreams.string("agaga gogo").reader();

	String str = IoStreams.string().bufferedWriter().getSubject();
	
	IoStreams.bytes(new byte[] { 0, 1, 2 }).objectInput();
    }

    public byte[] fluent() throws IOException {
	// create and combine both objects
	PrintWriterProxy<byte[]> writer = IoStreams.bytes().printWriter();

	// write the string
	writer.write("doodoo");

	// close the writer and the inner stream at once
	writer.close();

	// extract result
	return writer.getSubject();
    }

    public byte[] classic() throws IOException {
	// explicitly create inner stream to extract result
	ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();

	// create outer writer
	PrintWriter writer = new PrintWriter(byteOutputStream);

	// write a string
	writer.write("doodoo");

	// close the writer
	writer.close();

	// dont forget to close inner stream too
	byteOutputStream.close();

	// extract result from inner stream
	return byteOutputStream.toByteArray();
    }

    public byte[] fluent2() throws IOException {
	PrintWriterProxy<byte[]> writer = IoStreams.bytes().printWriter();
	writer.write("doodoo");
	writer.close();
	return writer.getSubject();
    }

    public byte[] classic2() throws IOException {
	ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
	PrintWriter writer = new PrintWriter(byteOutputStream);
	writer.write("doodoo");
	writer.close();
	byteOutputStream.close();
	return byteOutputStream.toByteArray();
    }

    public byte[] fluent3() throws IOException {
	try (PrintWriterProxy<byte[]> writer = IoStreams.bytes().printWriter()) {
	    writer.write("doodoo");
	    return writer.getSubject();
	}
    }

    public byte[] classic3() throws IOException {
	try (ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(byteOutputStream)) {
	    writer.write("doodoo");
	    return byteOutputStream.toByteArray();
	}
    }

}
