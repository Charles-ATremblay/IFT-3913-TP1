import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    private static List<String> metricsLines = new ArrayList<>();

    public static void main(String[] args) {
        // verifier quil y a au moins 2 arguments, le input dir et le output file
        if (args.length < 2) {
            System.err.println("Usage: tls <input-directory> [-o <output-file>]");
            System.exit(1);
        }

        String inputDirectory = args[0]; // Argument 1 est le input dir

        String absoluteInputDirectory = Paths.get(System.getProperty("user.dir"), inputDirectory).toString();

        String outputFile = null;

        // verifier pour le flag -o qui permet de creer un outfile
        for (int i = 1; i < args.length; i++) {
            if ("-o".equals(args[i]) && i + 1 < args.length) {
                outputFile = args[i + 1];
                break;
            }
        }

        // S'il n'y a pas de flag -o, on utilise simplement le 2e argument comme le outputfile
        if (outputFile == null && args.length >= 2) {
            outputFile = args[1];
        }

        // System.out.println("Input Directory: " + absoluteInputDirectory);
        // System.out.println("Output File: " + outputFile);

        List<Path> files = listFilesInDirectory(absoluteInputDirectory);

        // Calcul des metriques
        for (Path file : files) {
            String filePath = file.toString();

            // Verifie si le fichier est bien un fichier java
            if (filePath.endsWith(".java")) {
                calculateMetricsForJavaFile(filePath, outputFile);
            }
        }

    }

    // Permet d'ecrire dans le fichier csv
    private static void writeMetricsToCSV(String outputFile) {
        if (outputFile != null) {
            try (FileWriter fileWriter = new FileWriter(outputFile, false)) {
                for (String metricsLine : metricsLines) {
                    // Write each line to the CSV file
                    fileWriter.write(metricsLine);
                }
            } catch (IOException e) {
                System.err.println("Error writing to the output file: " + e.getMessage());
                System.exit(1);
            }
        }
    }

    // ici on effectue les calculs de TLOC, TASSERT et TCMP et on extrait les autres metriques
    private static void calculateMetricsForJavaFile(String filePath, String outputFile) {
        int tloc = TLOCCalculator.calculateTLOC(filePath);
        int tassert = TASSERTCalculator.calculateTASSERT(filePath);

        // Extraction du nom du fichier
        String fileName = new File(filePath).getName();
        fileName = "./" + fileName;

        // Extraction du nom de package et du nom de classe du fichier
        String packageName = extractPackageName(filePath);
        String className = extractClassName(filePath);

        // Calcul du TCMP
        double tcmp = (tassert != 0) ? (double) tloc / tassert : 0.0;
        tcmp = Math.round(tcmp * 100.0) / 100.0;

        // Formatting de la ligne. Locale.US nous permet de montrer le TCMP avec un point et non une virgule
        String csvLine = String.format(Locale.US, "%s, %s, %s, %d, %d, %.2f%n",
                fileName, packageName, className, tloc, tassert, tcmp);

        // Verifie si la ligne existe deja dans notre ArrayList
        if (!metricsLines.contains(csvLine)) {

            // Ajoute la ligne a l'ArrayList
            metricsLines.add(csvLine);

            System.out.println(csvLine);

            // Ecriture de la ligne dans le outputFile
            if (outputFile != null) {
                writeMetricsToCSV(outputFile);
            }
        }
    }

    // Extraction du nom de la classe
    private static String extractClassName(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // On utilise une Regex pour trouver le nom de la classe
                Matcher matcher = Pattern.compile(".*\\bclass\\s+(\\w+)\\b.*").matcher(line);
                if (matcher.find()) {
                    return matcher.group(1); // Retourne le nom de la classe (premier match du pattern Regex)
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            System.exit(1);
        }

        // Si il n'y a pas de nom de classe on utilise le nome du fichier sans l'extension
        String fileName = new File(filePath).getName();
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex > 0) {
            return fileName.substring(0, lastDotIndex);
        } else {
            return fileName;
        }
    }

    // Extraction du nom du package
    private static String extractPackageName(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // On utilise une Regex pour trouver le nom du package
                Matcher matcher = Pattern.compile("^\\s*package\\s+([\\w.]+)\\s*;").matcher(line);
                if (matcher.find()) {
                    return matcher.group(1);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            System.exit(1);
        }

        // Retourne Default si on ne trouve pas de package
        return "default";
    }

    // Creer une liste de tous les fichiers dans un directory
    private static List<Path> listFilesInDirectory(String directoryPath) {
        try {
            return Files.walk(Paths.get(directoryPath))
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error listing files in the input directory: " + e.getMessage());
            System.exit(1);
            return null;
        }
    }
}