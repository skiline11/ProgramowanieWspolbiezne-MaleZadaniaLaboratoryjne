import java.util.*;

public class Zadanie
{


    public static void main(String[] args)
    {
        Random rand = new Random();
        System.out.println("Tworze pierwszy Wektor który wyglada tak : ");
        int size1 = rand.nextInt(10) + 3;
        int[] sk1 = new int[size1];
        for(int i = 0; i < size1; i++) sk1[i] = rand.nextInt(10);
        Wektor w1 = new Wektor(size1, sk1);
        w1.wypisz_sie();

        System.out.println("Tworze drugi Wektor który wyglada tak : ");
        int size2 = rand.nextInt(10) + 3;
        int[] sk2 = new int[size2];
        for(int i = 0; i < size2; i++) sk2[i] = rand.nextInt(10);
        Wektor w2 = new Wektor(size2, sk2);
        w2.wypisz_sie();

        System.out.println("Tworze nowy Wektor poprzez dodanie do siebie dwóch poprzednich wektorów, który wygląda tak:");
        double czas_przed = System.currentTimeMillis();
        int iloczyn_skalarny = w1.razy(w2);
        System.out.println("Iloczyn skalarny tych wektorów wynosi " + iloczyn_skalarny);
    }
}

class Wektor
{
    private int skladowe[];
    int dlugosc;
    private static volatile int ILOCZYN = 0;
    List<Pair<Integer, Integer>> list_dodawanie = Collections.synchronizedList(new ArrayList<Pair<Integer, Integer>>());
    List<Integer> list_mnozenie = Collections.synchronizedList(new ArrayList<Integer>());

    public Wektor(int N)
    {
        this.dlugosc = N;
        this.skladowe = new int[N];
        for(int i = 0; i < N; i++) this.skladowe[i] = 0;
    }

    public Wektor(int N, int [] skladowe)
    {
        this.dlugosc = N;
        this.skladowe = skladowe;
    }

    public int getDlugosc() {return this.dlugosc; }

    public int[] getSkladowe() {return this.skladowe; }

    public int getSkladowa(int n) {
        if(n >= this.dlugosc) return 0;
        else return this.skladowe[n];
    }

    public void wypisz_sie()
    {
        for(int i = 0; i < this.dlugosc; i++) System.out.print(this.skladowe[i] + ", ");
        System.out.println("");
    }

    public Wektor dodaj(Wektor w)
    {
        int[] skladowe_w = w.getSkladowe();
        int size1 = this.dlugosc;
        int size2 = w.getDlugosc();
        int maxSize = size1;
        int minSize = size2;
        if (size2 > size1)
        {
            maxSize = size2;
            minSize = size1;
        }

        Thread[] watki = new Thread[maxSize];
        for(int i = 0; i < maxSize; i++)
        {
            watki[i] = new MaszynaDodajaca(this, w, i, list_dodawanie);
            watki[i].start();
        }

        for (int i = 0; i < maxSize; i++) {
            while (watki[i].isAlive())
                try {
                    Thread.sleep(100); // Sleep for 10 milliseconds
                } catch (InterruptedException e) {
                }
        }

        int[] nowe_skladowe = new int[maxSize];
        for(int i = 0; i < maxSize; i++)
        {
            Pair<Integer, Integer> p = list_dodawanie.remove(0);
            nowe_skladowe[p.getL()] = p.getR();
        }
        return new Wektor(maxSize, nowe_skladowe);
        // Jeszcze nie usuwam ostatnich zer na koncach
    }

    public int razy(Wektor w)
    {
        int iloczyn = 0;
        int[] skladowe_w = w.getSkladowe();
        int size1 = this.dlugosc;
        int size2 = w.getDlugosc();
        int maxSize = size1;
        int minSize = size2;
        if (size2 > size1)
        {
            maxSize = size2;
            minSize = size1;
        }
        Thread[] watki = new Thread[maxSize];
        for(int i = 0; i < maxSize; i++)
        {
            watki[i] = new MaszynaMnozaca(this, w, i, list_mnozenie);
            watki[i].start();
        }

        for (int i = 0; i < maxSize; i++) {
            try {
                watki[i].join();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                System.err.println("Wątek główny przerwany.");
            }
        }

        for(int i = 0; i < maxSize; i++)
        {
            iloczyn += list_mnozenie.remove(0);
        }
        return iloczyn;
        // Jeszcze nie usuwam ostatnich zer na koncach
    }

    static class MaszynaMnozaca extends Thread {

        private int pos;
        private Wektor w1, w2;
        private List<Integer> list;

        public MaszynaMnozaca(Wektor w1, Wektor w2, int pos, List<Integer> list)
        {
            this.w1 = w1;
            this.w2 = w2;
            this.pos = pos;
            this.list = list;
        }

        @Override
        public void run() {
            System.out.println("Mnoze " + w1.getSkladowa(this.pos) + " * " + w2.getSkladowa(this.pos));
            list.add(w1.getSkladowa(this.pos) * w2.getSkladowa(this.pos));
        }
    }

    static class MaszynaDodajaca extends Thread {
        private int suma;
        private Wektor w1, w2;
        private int pos;
        private List<Pair<Integer, Integer>> list;

        public MaszynaDodajaca(Wektor w1, Wektor w2, int pos, List<Pair<Integer, Integer>> list) {
            this.w1 = w1;
            this.w2 = w2;
            this.suma = 0;
            this.pos = pos;
            this.list = list;
        }

        @Override
        public void run() {

            try {
                this.suma = w1.getSkladowa(this.pos) + w2.getSkladowa(this.pos);
                Thread.sleep(1000);
                list.add(new Pair<>(this.pos, this.suma));
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
