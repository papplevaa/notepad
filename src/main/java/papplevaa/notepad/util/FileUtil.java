package papplevaa.notepad.util;

import java.io.*;

/**
 * Utility class providing file-related operations for the Notepad application.
 */
public final class FileUtil {
    private FileUtil() {
        // Private constructor to prevent instantiation; utility class with static methods.
    }

    /**
     * Reads the content of a file and returns it as a string.
     *
     * @param filePath The path to the file to be read.
     * @return The content of the file as a string, or {@code null} if an error occurs during reading.
     */
    public static String loadContent(File filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while (reader.ready()) {
                String line = reader.readLine();
                content.append(line);
                content.append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Failed to load content");
            return null;
        }
        return content.toString();
    }

    /**
     * Writes the provided content to a file.
     *
     * @param content  The content to be written to the file.
     * @param filePath The path to the file where the content will be saved.
     */
    public static void saveContent(String content, File filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        } catch (IOException exception) {
            System.out.println("Failed to save content");
        }
    }

    /**
     * Serializes an object and saves it to a file.
     *
     * @param filePath The path to the file where the serialized object will be saved.
     * @param object   The object to be serialized and saved.
     * @param <T>      The type of the object.
     */
    public static <T extends Serializable> void serialize(File filePath, T object) {
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            stream.writeObject(object);
        } catch (IOException exception) {
            System.out.println("Failed to save data for next session");
        }
    }

    /**
     * Deserializes an object from a file.
     *
     * @param filePath The path to the file containing the serialized object.
     * @param tClass   The class of the object to be deserialized.
     * @param <T>      The type of the object.
     * @return The deserialized object, or {@code null} if an error occurs during deserialization.
     */
    public static <T extends Serializable> T deserialize(File filePath, Class<T> tClass) {
        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(filePath))) {
            Object object = stream.readObject();
            if(tClass.isInstance(object)) {
                return tClass.cast(object);
            }
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println("Failed to load data from previous session");
        }
        return null;
    }
}
