public class Macierz {

    public static int wartość(final int w, final int k) {
        final int a = 2 * k + 1;
        return (w + 1) * (a % 4 - 2) * a;

        // 0 0 a = 1, ret 1* (-1)*1 = -1
        // 0 1
    }

}
