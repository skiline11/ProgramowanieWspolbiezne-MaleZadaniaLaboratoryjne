import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class Zadanie6 {

    private static final int N_WIERSZY = 10;
    private static final int N_KOLUMN = 100;

    private static final int N_WATKOW = 4;

    private static class Zadanie implements Callable<Integer>
    {
        private int wiersz, kolumna;
        public Zadanie(int num)
        {
            this.wiersz = num/N_KOLUMN;
            this.kolumna = num - this.wiersz * N_KOLUMN;
        }

        @Override
        public Integer call()
        {
            return Macierz.wartość(this.wiersz, this.kolumna);
        }
    }

    public static void main( String[] args) throws InterruptedException {
        final ExecutorService pulaWatkow = Executors.newFixedThreadPool(N_WATKOW);
        final List<Future<Integer>> sumowanie = new ArrayList<>();
        for(int y = 0; y < N_WIERSZY; y++)
        {
            for(int x = 0; x < N_KOLUMN; x++)
            {
                sumowanie.add(pulaWatkow.submit(new Zadanie(x)));
            }
            int suma = 0;
            try {
                for(Future<Integer> kolejna : sumowanie)
                {
                    suma += kolejna.get();
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println("Suma dla wiersza " + y + " wynosi " + suma);
        }
        pulaWatkow.shutdown();
    }

}
