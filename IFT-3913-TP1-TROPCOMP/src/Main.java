import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String inputDirectory;
        double threshold;
        String outputFile = null;

        if (args.length < 2) {
            System.err.println("Usage: tropcomp [-o <output-file>] <input-directory> <threshold>");
            System.exit(1);
        }

        // Check if the first argument is "-o"
        if ("-o".equals(args[0])) {
            if (args.length < 4) {
                System.err.println("Usage: tropcomp -o <output-file> <input-directory> <threshold>");
                System.exit(1);
            }

            outputFile = args[1];
            inputDirectory = args[2];
            threshold = Double.parseDouble(args[3]);

        } else {
            inputDirectory = args[0];
            threshold = Double.parseDouble(args[1]);
        }


        List<String> absoluteClassPaths = listAbsoluteClassPath(inputDirectory);

        // Effectue les calculs TLOC, TASSERT et TLOC
        List<String> suspiciousClasses = findSuspiciousClasses(absoluteClassPaths, threshold);

        List<String> suspiciousClassesPaths = findSuspiciousClassesPaths(absoluteClassPaths, threshold);


        // Dependemment des arguments, on affiches les donnees(outputFile et/ou ligne de command)
        if (outputFile != null) {
            writeSuggestionsToFile(outputFile, suspiciousClasses, suspiciousClassesPaths, absoluteClassPaths);
        } else {
            printSuggestionsToConsole(suspiciousClasses);
        }
    }

    private static void writeSuggestionsToFile(String outputFile, List<String> suspiciousClasses, List<String> suspiciousClassesPaths, List<String> absoluteFilePaths) {
        try (FileWriter fileWriter = new FileWriter(outputFile, false)) {

            fileWriter.write("chemin du fichier, nom du paquet, nom de la classe, tloc de la classe, tassert de la classe, tcmp de la classe\n");

            for (int i = 0; i < suspiciousClasses.size(); i++) {
                String suspiciousClass = suspiciousClasses.get(i);
                String absoluteFilePath = suspiciousClassesPaths.get(i);
                String fileName = new File(absoluteFilePath).getName();
                fileName = "./" + fileName;

                // Calcul des mnetreiques importantes
                int tloc = TLOCCalculator.calculateTLOC(absoluteFilePath);
                int tassert = TASSERTCalculator.calculateTASSERT(absoluteFilePath);
                double tcomp = TCOMPCalculator.calculateTComp(tloc, tassert);

                // Extraire le nom du package
                String packageName = extractPackageName(new File(absoluteFilePath));

                // Ecrire le tout dans le outputfile
                fileWriter.write(String.format(Locale.US, "%s, %s, %s, %d, %d, %.2f\n",
                        fileName, packageName, suspiciousClass, tloc, tassert, tcomp));
            }

        } catch (IOException e) {
            System.err.println("Error writing to the output file: " + e.getMessage());
            System.exit(1);
        }
    }

    private static String extractPackageName(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // on cherche la premiere ligne qui contient le mot package
                Matcher matcher = Pattern.compile(".*\\bpackage\\s+(\\w+(\\.\\w+)*)\\s*;").matcher(line);
                if (matcher.find()) {
                    return matcher.group(1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Si on en trouve pas on retourne le package default
        return "default";
    }

    private static List<String> listAbsoluteClassPath(String projectDirectory) {
        List<String> testClasses = new ArrayList<>();

        File sourceDirectory = new File(projectDirectory);

        // On fait une recherche recusive des directory dans le input directory afin davoir une list de classes
        listJavaFiles(sourceDirectory, testClasses);

        return testClasses;
    }

    private static void listJavaFiles(File directory, List<String> javaFilePaths) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // recherche recursive pour les fichiers dans le dir
                        listJavaFiles(file, javaFilePaths);
                    } else if (file.getName().endsWith(".java")) {
                        // ajoute le Path jusquau fichier qui est de type java
                        javaFilePaths.add(file.getAbsolutePath());
                    }
                }
            }
        }
    }

    private static String extractClassName(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Cherche la ligne qui contient le mot classe
                Matcher matcher = Pattern.compile(".*\\bclass\\s+(\\w+)\\b.*").matcher(line);
                if (matcher.find()) {
                    return matcher.group(1);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        // si on a pas de classe, on va simplement prendre le nom du fichier
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex > 0) {
            return fileName.substring(0, lastDotIndex);
        } else {
            return fileName;
        }
    }

    private static List<String> findSuspiciousClasses(List<String> javaFilePaths, double threshold) {
        List<String> suspiciousClasses = new ArrayList<>();


        for (String javaFilePath : javaFilePaths) {
            // calcul des metriques
            int tloc = TLOCCalculator.calculateTLOC(javaFilePath);
            int tassert = TASSERTCalculator.calculateTASSERT(javaFilePath);
            double tcomp = TCOMPCalculator.calculateTComp(tloc, tassert);


            if (tcomp > threshold) {
                String className = extractClassName(new File(javaFilePath));
                suspiciousClasses.add(className);
            }
        }

        suspiciousClasses.sort(Comparator.reverseOrder());

        return suspiciousClasses;
    }

    private static List<String> findSuspiciousClassesPaths(List<String> javaFilePaths, double threshold) {
        List<String> suspiciousClassesPaths = new ArrayList<>();


        for (String javaFilePath : javaFilePaths) {
            // calcul des metriques
            int tloc = TLOCCalculator.calculateTLOC(javaFilePath);
            int tassert = TASSERTCalculator.calculateTASSERT(javaFilePath);
            double tcomp = TCOMPCalculator.calculateTComp(tloc, tassert);


            if (tcomp > threshold) {
                suspiciousClassesPaths.add(javaFilePath);
            }
        }

        suspiciousClassesPaths.sort(Comparator.reverseOrder());

        return suspiciousClassesPaths;
    }

    private static void printSuggestionsToConsole(List<String> suspiciousClasses) {
        System.out.println("Classes Suspicieuses");

        if (suspiciousClasses.isEmpty()) {
            System.out.println("Aucune classe suspicieuse trouvée pour ce seuil.");
        } else {
            for (String className : suspiciousClasses) {
                System.out.println(className);
            }
        }
    }
}