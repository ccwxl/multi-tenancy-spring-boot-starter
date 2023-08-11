package cc.sofast.infrastructure.customization.file;


import cc.sofast.infrastructure.customization.PersistentCustomizationLoader;
import cc.sofast.infrastructure.customization.TKey;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


/**
 * @author wxl
 * 命名规则为 tenant_key.json
 * {
 * "key":value
 * }
 */
public class FileCustomizationLoader extends PersistentCustomizationLoader {

    private static final int BUFFER_SIZE = 8192;

    @Override
    public String val(TKey key) {
        ClassPathResource sbc = new ClassPathResource(key.getTenant() + "_" + key.getKey() + ".json");
        try {
            byte[] read = read(sbc.getInputStream(), 100);
            return new String(read);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean remove(TKey key) {
        //什么也不做
        return true;
    }

    @Override
    public boolean saveOrUpdate(TKey key, String valJson) {
        //什么也不做
        return true;
    }

    /**
     * Reads all the bytes from an input stream. Uses {@code initialSize} as a hint
     * about how many bytes the stream will have.
     *
     * @param source      the input stream to read from
     * @param initialSize the initial size of the byte array to allocate
     * @return a byte array containing the bytes read from the file
     * @throws IOException      if an I/O error occurs reading from the stream
     * @throws OutOfMemoryError if an array of the required size cannot be allocated
     */
    private static byte[] read(InputStream source, int initialSize) throws IOException {
        int capacity = initialSize;
        byte[] buf = new byte[capacity];
        int nread = 0;
        int n;
        for (; ; ) {
            // read to EOF which may read more or less than initialSize (eg: file
            // is truncated while we are reading)
            while ((n = source.read(buf, nread, capacity - nread)) > 0)
                nread += n;

            // if last call to source.read() returned -1, we are done
            // otherwise, try to read one more byte; if that failed we're done too
            if (n < 0 || (n = source.read()) < 0)
                break;

            // one more byte was read; need to allocate a larger buffer
            capacity = Math.max(1, BUFFER_SIZE);
            buf = Arrays.copyOf(buf, capacity);
            buf[nread++] = (byte) n;
        }
        return (capacity == nread) ? buf : Arrays.copyOf(buf, nread);
    }

}
