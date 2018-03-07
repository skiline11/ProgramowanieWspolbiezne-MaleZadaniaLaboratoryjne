import java.util.concurrent.*;

public class Zadanie4 {

    private static final int N_WIERSZY = 10;
    private static final int N_KOLUMN = 100;

//    private static final CyclicBarrier bariera1 = new CyclicBarrier(N_KOLUMN);
//    private static final CountDownLatch zatrzask[] = new CountDownLatch[N_WIERSZY];
    private static final BlockingQueue<Integer> kolejka_blokująca[] = new LinkedBlockingQueue[N_WIERSZY];

    public static void main( String[] args) throws InterruptedException {
        for(int w = 0; w < N_WIERSZY; w++)
        {
//            zatrzask[w] = new CountDownLatch(N_KOLUMN);
            kolejka_blokująca[w] = new LinkedBlockingQueue<Integer>();
        }

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
            for(int num_w = 0; num_w < N_WIERSZY; num_w++) {
                int suma = 0;
                try {
//                    System.out.println(num_k + "  " + num_w);
//                    wyniki[num_w * N_KOLUMN + this.num_k] = Macierz.wartość(num_w, this.num_k);
//                    zatrzask[num_w].countDown();
                    kolejka_blokująca[num_w].put(Macierz.wartość(num_w, this.num_k));
                    if(num_k == 0)
                    {
//                        zatrzask[num_w].await();
                        for(int i = 0; i < N_KOLUMN; i++)
                        {
//                            System.out.println("wyniki[" + (num_w * N_KOLUMN + i) + "] = " + wyniki[num_w * N_KOLUMN + i]);
//                            suma += wyniki[num_w * N_KOLUMN + i];
                            suma += kolejka_blokująca[num_w].take();
                        }
                        System.out.println("Suma dla wiersza " + num_w + " = " + suma);
                    }
                } catch (InterruptedException  e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Wątek " + num_k + " przerwany");
                }
            }
        }
    }
}
