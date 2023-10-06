
public class Main {
    public static void main(String[] args) {if (args.length != 1) {
        System.err.println("Usage: java TLOCCalculator <path-to-java-file>");
        System.exit(1);
    }

        // prendre le fichier .java passer en argument afin de tester
        String filePath = args[0];

        int tlocForFile = TLOCCalculatorClass.calculateTLOC(filePath);

        System.out.println("TLOC: " + tlocForFile);
    }
}