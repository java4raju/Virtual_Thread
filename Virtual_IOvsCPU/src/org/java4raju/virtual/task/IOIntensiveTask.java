package org.java4raju.virtual.task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * IO Intensive task
 */
public class IOIntensiveTask {
    public void runIOIntensiveTask(String fileName){
        String filename = fileName; // Replace this with the actual file path

        try {
            // Open the file for reading
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;

            // Read each line from the file
            while ((line = bufferedReader.readLine()) != null) {
                // Process the line (count the number of words)
                countWords(line);
            }

            // Close the resources
            bufferedReader.close();
            fileReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Count the number of words in a given string
    private static int countWords(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        return text.split("\\s+").length;
    }

}
