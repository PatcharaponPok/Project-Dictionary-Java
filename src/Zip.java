import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip {
    public void zipDirectory(String dirPath, String zipPath) {
        Path sourceDir = Paths.get(dirPath); // Path source directory
        String zipFileName = zipPath.concat(".zip"); // Path file for build zip
        try {
            final ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(zipFileName));
            Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() { // walk in to folder in the form of a file tree
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {// call function invoked file in directory
                    try {
                        Path targetDir = sourceDir.relativize(file);// set taget source
                        zipFile.putNextEntry(new ZipEntry(targetDir.toString()));// build zip file
                        byte[] bytes = Files.readAllBytes(file);// read all bytes in file
                        zipFile.write(bytes, 0, bytes.length);
                        zipFile.closeEntry();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
            zipFile.close();
        } catch (IOException e) {
            System.out.println("File compression failed");
            //e.printStackTrace();
        }
    }
}
