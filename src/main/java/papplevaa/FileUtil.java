package papplevaa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileUtil {
    public static String loadContent(File filePath) {
        StringBuilder content = new StringBuilder();
        try (Scanner scanner = new Scanner(filePath)) {
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine());
                content.append(System.lineSeparator());
            }
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
        return content.toString();
    }

    public static void saveContent(String currentContent, File filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(currentContent);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
