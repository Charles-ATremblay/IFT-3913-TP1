import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TLOCCalculatorClass {

    public static int calculateTLOC(String filePath) {
        int tloc = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Verifie si la ligne lue n'est vide ou ne contient pas un commentaire
                if (!line.trim().isEmpty() && !line.trim().startsWith("//") && !line.trim().startsWith("/*")
                        && !line.trim().startsWith("*") && !line.trim().startsWith("*/")) {
                    tloc++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            System.exit(1);
        }

        return tloc;
    }
}
