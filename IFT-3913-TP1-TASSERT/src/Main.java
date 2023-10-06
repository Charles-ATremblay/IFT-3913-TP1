public class Main {
    public static void main(String[] args) {
        // On verifie si le nombre d'arguments passe est ok
        if (args.length != 1) {
            System.err.println("Usage: java TASSERTCalculator <path-to-test-class>");
            System.exit(1);
        }
        // premier argument est le fichier test
        String filePath = args[0];


        int tassert = TASSERTCalculator.calculateTASSERT(filePath);

        System.out.println("TASSERT: " + tassert);
    }
}