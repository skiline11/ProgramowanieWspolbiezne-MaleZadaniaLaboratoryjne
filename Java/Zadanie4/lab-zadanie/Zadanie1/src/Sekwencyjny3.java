import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Sekwencyjny3 {

    private static final int N_WIERSZY = 10;
    private static final int N_KOLUMN = 100;

    private static final CyclicBarrier bariera1 = new CyclicBarrier(N_KOLUMN);
    private static final CountDownLatch zatrzask[] = new CountDownLatch[N_WIERSZY];
    private static final int wyniki[] = new int[1000];

    public static void main( String[] args) throws InterruptedException {
        for(int w = 0; w < N_WIERSZY; w++)
        {
            zatrzask[w] = new CountDownLatch(N_KOLUMN);
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
        private int wartosc_kolumny = 0;
        public Zliczanie_kolumny(int num)
        {
            this.num_k = num;
        }

        @Override
        public void run() {
            System.out.println("Watek dla kolumny " + num_k + " uruchomiony");
            int suma = 0;
            for(int num_w = 0; num_w < N_WIERSZY; num_w++) {
                try {
//                    System.out.println(num_k + "  " + num_w);
                    wyniki[num_w * N_KOLUMN + this.num_k] = Macierz.wartość(num_w, this.num_k);
                    zatrzask[num_w].countDown();
                    if(num_k == 0)
                    {
                        zatrzask[num_w].await();
                        for(int i = 0; i < N_KOLUMN; i++)
                        {
//                            System.out.println("wyniki[" + (num_w * N_KOLUMN + i) + "] = " + wyniki[num_w * N_KOLUMN + i]);
                            suma += wyniki[num_w * N_KOLUMN + i];
                        }
                        System.out.println("Suma dla wiersza " + num_w + " = " + suma);
                    }
                } catch (InterruptedException  e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Wątek " + num_k + " przerwany");
                }
            }
            if(num_k == 0)
            {
                System.out.println("Wynik koncowy to : " + suma);
            }
        }
    }
}
