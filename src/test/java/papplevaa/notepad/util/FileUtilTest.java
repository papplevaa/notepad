package papplevaa.notepad.util;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import static org.junit.Assert.*;

import java.io.File;

public class FileUtilTest {
    @BeforeClass
    public static void setup() {
        File dump = new File("src/test/resources/dump");
        createDirectory(dump);
    }

    @AfterClass
    public static void cleanup() {
        File dump = new File("src/test/resources/dump");
        deleteDirectory(dump);
    }

    @Test
    public void testLoadContent() {
        // Arrange
        File file = new File("src/test/resources/input/load.txt");
        // Act
        String content = FileUtil.loadContent(file);
        // Assert
        assertEquals("Hello, World!!!", content);
    }

    @Test
    public void testSaveContent() {
        // Arrange
        File file = new File("src/test/resources/dump/save.txt");
        // Act
        FileUtil.saveContent("Passw1234", file);
        // Assert
        String contentReadFromDisk = FileUtil.loadContent(file);
        assertEquals("Passw1234", contentReadFromDisk);
    }

    @Test
    public void testSerialization() {
        // Arrange
        File file = new File("src/test/resources/dump/ser.data");
        Double serialized = 5.775;
        // Act
        FileUtil.serialize(file, serialized);
        Double deserialized = FileUtil.deserialize(file, Double.class);
        // Assert
        assertEquals(serialized, deserialized);
    }

    private static void createDirectory(File directory) {
        if(!directory.exists()) {
            directory.mkdir();
        }
    }

    private static void deleteDirectory(File directory) {
        if(directory.exists()) {
            File[] files = directory.listFiles();
            if(files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    }
                    file.delete();
                }
            }
            directory.delete();
        }
    }
}
