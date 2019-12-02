package ca.rbon.quack;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicLong;

public class Shmokey {

    public static void main(String[] args) throws Exception {
        final long start = System.currentTimeMillis();
//        final MessageDigest md = MessageDigest.getInstance("SHA-1");
        final var md = Murmur3.bufferHasher();
        final AtomicLong byteCount = new AtomicLong(0);
        Files.walkFileTree(new File(args[0]).toPath(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                byteCount.addAndGet(hashFile(file, md));
                var perms = Files.getPosixFilePermissions(file, LinkOption.NOFOLLOW_LINKS);
                return FileVisitResult.CONTINUE;
            }
        });

        System.out.println(bytesToHex(md.digest()));
        System.out.println(byteCount);
        long seconds = (System.currentTimeMillis() - start) / 1000;
        System.out.println(seconds);
        System.out.println(byteCount.get() / seconds);
    }

    static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String bytesToHex(int data) {
        return bytesToHex(new byte[] {
                (byte)((data >> 24) & 0xff),
                (byte)((data >> 16) & 0xff),
                (byte)((data >> 8) & 0xff),
                (byte)((data >> 0) & 0xff),
        });
    }

    static long hashFile(Path path, Hasher md) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(path.toFile(), "r")) {
            FileChannel fileChannel = file.getChannel();
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
            md.update(buffer);
            return file.length();
        }
    }
}
