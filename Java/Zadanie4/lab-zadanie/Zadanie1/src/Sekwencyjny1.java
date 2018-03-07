import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Sekwencyjny1 {

    private static final int N_WIERSZY = 10;
    private static final int N_KOLUMN = 100;

    private static final CyclicBarrier bariera1 = new CyclicBarrier(N_KOLUMN);

    private static List<Integer> list_dodawanie = Collections.synchronizedList(new ArrayList<Integer>());

    public static void main( String[] args) throws InterruptedException {
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
//            System.out.println("Watek dla kolumny " + num_k + " uruchomiony");
            for(int num_w = 0; num_w < N_WIERSZY; num_w++) {
                try {
//                    System.out.print(num_k + "  " + num_w);
                    list_dodawanie.add(Macierz.wartość(num_w, num_k));
//                    System.out.println(", wartosc = " + Macierz.wartość(num_w, num_k));
//                    System.out.println("Watek dla kolumny " + num_k + " czeka na barierze");
                    bariera1.await();
                    if(num_k == 0)
                    {
                        int suma = 0;
                        while(list_dodawanie.isEmpty() == false)
                        {
                            suma += list_dodawanie.remove(0);
                        }
                        System.out.println("Suma dla wiersza " + num_w + " = " + suma);
                    }
                    bariera1.await();
//                    System.out.println("Watek dla kolumny " + num_k + " za barierą");
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Wątek " + num_k + " przerwany");
                }

            }
        }
    }
}
