package cc.sofast.infrastructure.jdbc.schema.utils;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * @author apple
 */
public class TzxUtils {

    /**
     * 解压缩txz包
     *
     * @param fileInputStream 文件输入流
     * @param outputDirPath   输出
     * @throws IOException 异常
     */
    public static void extractTarXz(InputStream fileInputStream, String outputDirPath) throws IOException {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
             XZCompressorInputStream xzInputStream = new XZCompressorInputStream(bufferedInputStream);
             TarArchiveInputStream archiveInputStream = new TarArchiveInputStream(xzInputStream)) {
            ArchiveEntry entry;
            while ((entry = archiveInputStream.getNextEntry()) != null) {
                String entryName = entry.getName();
                File outputFile = new File(outputDirPath, entryName);
                if (entry.isDirectory()) {
                    // 如果是目录，则创建相应的目录
                    if (!outputFile.isDirectory() && !outputFile.mkdirs()) {
                        throw new IOException("failed to create directory " + outputFile);
                    }
                } else if (entry instanceof TarArchiveEntry tarEntry) {
                    Path path = outputFile.toPath();
                    // 处理 Tar 文件的软连接
                    if (tarEntry.isSymbolicLink()) {
                        Path targetPath = Paths.get(tarEntry.getLinkName());
                        Files.createSymbolicLink(path, targetPath);
                    } else {
                        // 如果是文件，则写入文件内容
                        try (OutputStream outputFileStream = new FileOutputStream(outputFile)) {
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = archiveInputStream.read(buffer)) != -1) {
                                outputFileStream.write(buffer, 0, length);
                            }
                        }
                    }
                }
            }
        }
    }

}
