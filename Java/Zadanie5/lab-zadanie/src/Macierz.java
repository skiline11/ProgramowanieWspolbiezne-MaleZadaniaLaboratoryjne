public class Macierz {

    public static int wartość(final int w, final int k) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        final int a = 2 * k + 1;
        return (w + 1) * (a % 4 - 2) * a;
    }

}
