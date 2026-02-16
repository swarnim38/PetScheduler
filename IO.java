public class IO {
    public static void print(String message) {
        System.out.print(message);
    }

    public static void println(String message) {
        System.out.println(message);
    }

    public static void println() {
        System.out.println();
    }

    public static void printError(String message) {
        System.err.println("ERROR: " + message);
    }
}
