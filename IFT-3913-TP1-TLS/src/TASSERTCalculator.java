import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TASSERTCalculator {

    public static int calculateTASSERT(String filePath) {
        int tassert = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Verifie si la ligne contient une assertion
                if (line.contains("assert")) {
                    tassert++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            System.exit(1);
        }

        return tassert;
    }
}
