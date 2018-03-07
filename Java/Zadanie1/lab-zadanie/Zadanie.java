import java.util.*;

public class Zadanie
{


    public static void main(String[] args)
    {
        Random rand = new Random();
        System.out.println("Tworze pierwszy Wektor który wyglada tak : ");
        int size1 = rand.nextInt(15) + 10;
        int[] sk1 = new int[size1];
        for(int i = 0; i < size1; i++) sk1[i] = rand.nextInt(100);
        Wektor w1 = new Wektor(size1, sk1);
        w1.wypisz_sie();

        System.out.println("Tworze drugi Wektor który wyglada tak : ");
        int size2 = rand.nextInt(15) + 10;
        int[] sk2 = new int[size2];
        for(int i = 0; i < size2; i++) sk2[i] = rand.nextInt(100);
        Wektor w2 = new Wektor(size2, sk2);
        w2.wypisz_sie();

        System.out.println("Tworze nowy Wektor poprzez dodanie do siebie dwóch poprzednich wektorów, który wygląda tak:");
        double czas_przed = System.currentTimeMillis();
        Wektor w3 = w1.dodaj(w2);
        double czas_wykonania = System.currentTimeMillis() - czas_przed;
//        w3.wypisz_sie();
        System.out.println("Czas wykonania wynosi : " + czas_wykonania/1000 + " sekund");

        System.out.print("Gdybyśmy dodawali jedno wątkowo trwało by to tyle czasu : ");
        czas_przed = System.currentTimeMillis();
        int max_size = size1;
        int min_size = size2;
        if(size1 < size2)
        {
            max_size = size2;
            min_size = size1;
        }
        int tab[] = new int[max_size];
        for(int i = 0; i < max_size; i++) tab[i] = w1.getSkladowa(i) + w2.getSkladowa(i);
        czas_wykonania = System.currentTimeMillis() - czas_przed;
        System.out.println(czas_wykonania/1000 + " sekund");
        for(int i = 0; i < max_size; i++) System.out.print(tab[i] + ", ");
    }
}

class Wektor
{
    private int skladowe[];
    int dlugosc;
    List<Pair<Integer, Integer>> list = Collections.synchronizedList(new ArrayList<Pair<Integer, Integer>>());


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
            watki[i] = new MaszynaLiczaca(this, w, i, list);
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
            Pair<Integer, Integer> p = list.remove(0);
            nowe_skladowe[p.getL()] = p.getR();
        }
        return new Wektor(maxSize, nowe_skladowe);
        // Jeszcze nie usuwam ostatnich zer na koncach
    }
}

class MaszynaLiczaca extends Thread {
    private int suma;
    private Wektor w1, w2;
    private int pos;
    private List<Pair<Integer, Integer>> list;

    public MaszynaLiczaca(Wektor w1, Wektor w2, int pos, List<Pair<Integer, Integer>> list) {
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