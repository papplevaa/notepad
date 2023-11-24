package papplevaa.notepad.util;

import java.io.*;

public final class FileUtil {
    private FileUtil() {}

    public static String loadContent(File filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while (reader.ready()) {
                String line = reader.readLine();
                content.append(line);
            }
        } catch (IOException e) {
            System.out.println("Failed to load content");
            return null;
        }
        return content.toString();
    }

    public static void saveContent(String content, File filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        } catch (IOException exception) {
            System.out.println("Failed to save content");
        }
    }

    public static <T extends Serializable> void serialize(File filePath, T object) {
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            stream.writeObject(object);
        } catch (IOException exception) {
            System.out.println("Failed to save data for next session");
        }
    }

    public static <T extends Serializable> T deserialize(File filePath, Class<T> tclass) {
        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(filePath))) {
            Object object = stream.readObject();
            if(tclass.isInstance(object)) {
                return tclass.cast(object);
            }
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println("Failed to load data from previous session");
        }
        return null;
    }
}
