import java.util.Random;

public class Main {
    public static Worek moj_worek;
    public static void main(String[] args)
    {
        moj_worek = new Worek_klasa();
        Random rand = new Random();
        int rozmiar = 10;
        Thread t[] = new Thread[3 * rozmiar];
        int ile1[] = new int[3]; // ile zostało i-tego zasobu do włożenia
        int ile2[] = new int[3]; // ile zostało i-tego zasobu do wyjęcia
        for(int i = 0; i < 3; i++)
        {
            ile1[i] = rozmiar;
            ile2[i] = rozmiar;
        }

        Integer zasoby[] = new Integer[3];
        zasoby[0] = new Integer(0);
        zasoby[1] = new Integer(1);
        zasoby[2] = new Integer(2);
        int v1 = 0, v2 = 0;
        for(int i = 0; i < 3 * rozmiar; i++)
        {
            boolean koniec = false;
            while(koniec == false)
            {
                v1 = rand.nextInt(3);
                v2 = rand.nextInt(3);
                if(ile1[v1] != 0 && ile2[v2] != 0)
                {
                    ile1[v1]--;
                    ile2[v2]--;
                    koniec = true;
                }
            }
            t[i] = new Thread(new Watek(i, zasoby[v1], zasoby[v2])); // tworzę wątek który będzie wkładał zasoby[v1] i wyjmował zasoby[v2]
        }
        System.out.println("Watki startuja");
        for(int i = 0; i < 3 * rozmiar; i++)
        {
            t[i].start();
        }
        for(int i = 0; i < 3 * rozmiar; i++)
        {
            try {
                t[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("*** KONIEC PROGRAMU ***");
    }
}
