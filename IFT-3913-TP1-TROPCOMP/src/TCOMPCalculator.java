public class TCOMPCalculator {
    public static double calculateTComp(int tloc, int tassert) {
        double tcmp = (tassert != 0) ? (double) tloc / tassert : 0.0;
        tcmp = Math.round(tcmp * 100.0) / 100.0;

        return tcmp;
    }
}
