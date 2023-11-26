package papplevaa.notepad.util;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import static org.junit.Assert.*;

import java.io.File;

/**
 * Unit tests for the FileUtil class.
 */
public class FileUtilTest {
    /**
     * Set up method that creates a directory for testing before class execution.
     */
    @BeforeClass
    public static void setup() {
        File dump = new File("src/test/resources/dump");
        createDirectory(dump);
    }

    /**
     * Clean up method that deletes the testing directory after class execution.
     */
    @AfterClass
    public static void cleanup() {
        File dump = new File("src/test/resources/dump");
        deleteDirectory(dump);
    }

    /**
     * Test: Load content from a file.
     * Expected: Content is successfully loaded from the specified file.
     */
    @Test
    public void testLoadContent() {
        // Arrange
        File file = new File("src/test/resources/input/load.txt");
        // Act
        String content = FileUtil.loadContent(file);
        System.out.println(content);
        // Assert
        assertEquals("Hello, World!!!\r\n", content);
    }

    /**
     * Test: Save content to a file.
     * Expected: Content is successfully saved to the specified file.
     */
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

    /**
     * Test: Serialization and Deserialization of an object.
     * Expected: The object is successfully serialized and deserialized.
     */
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

    /**
     * Utility method: Create a directory if it does not exist.
     */
    private static void createDirectory(File directory) {
        if(!directory.exists()) {
            directory.mkdir();
        }
    }

    /**
     * Utility method: Delete a directory and its contents.
     */
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
