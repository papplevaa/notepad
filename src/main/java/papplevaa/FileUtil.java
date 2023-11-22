package papplevaa;

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

    public static void saveContent(String currentContent, File filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(currentContent);
        } catch (IOException exception) {
            System.out.println("Failed to save content");
        }
    }
}
