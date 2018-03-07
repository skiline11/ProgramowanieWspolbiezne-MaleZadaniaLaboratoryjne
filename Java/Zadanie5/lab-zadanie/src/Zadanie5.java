import java.util.concurrent.*;

public class Zadanie5 {

    private static final int N_WIERSZY = 10;
    private static final int N_KOLUMN = 100;

//    private static final CyclicBarrier bariera1 = new CyclicBarrier(N_KOLUMN);
//    private static final BlockingQueue<Integer> kolejka_blokująca[] = new LinkedBlockingQueue[N_WIERSZY];
    private static final ConcurrentMap<Integer, BlockingQueue<Integer>> mapa_wspolbiezna = new ConcurrentHashMap<>();

    public static void main( String[] args) throws InterruptedException {
//        for(int w = 0; w < N_WIERSZY; w++)
//        {
//            kolejka_blokująca[w] = new LinkedBlockingQueue<Integer>();
//        }

        Thread[] tab_thread = new Thread[N_KOLUMN];

        for(int k = 0; k < N_KOLUMN; k++)
        {
            tab_thread[k] = new Thread(new Zliczanie_kolumny(k));
            tab_thread[k].start();
        }
        for(int k = 0; k < N_KOLUMN; k++)
        {
            tab_thread[k].join();
        }
    }

    private static class Zliczanie_kolumny implements Runnable
    {
        private int num_k;
        public Zliczanie_kolumny(int num)
        {
            this.num_k = num;
        }

        @Override
        public void run() {
//            System.out.println("Watek dla kolumny " + num_k + " uruchomiony");
            for(int num_w = 0; num_w < N_WIERSZY; num_w++)
            {
                int suma = 0;
                try {
//                    System.out.println(num_k + "  " + num_w);
//                    kolejka_blokująca[num_w].put(Macierz.wartość(num_w, this.num_k));
                    mapa_wspolbiezna.putIfAbsent(num_w, new LinkedBlockingQueue<Integer>());
                    BlockingQueue<Integer> kolejka = mapa_wspolbiezna.get(num_w);
                    kolejka.put(Macierz.wartość(num_w, this.num_k));

                    if (num_k == 0) {
                        for (int i = 0; i < N_KOLUMN; i++) {
                            suma += kolejka.take();
                        }
                        System.out.println("Suma dla wiersza " + num_w + " = " + suma);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Wątek " + num_k + " przerwany");
                }
            }
        }
    }
}
