/*
Zadanie

Zaimplementuj w javie, wykorzystując wątki i semafory problem śpiącego fryzjera.

Zakład fryzjerski zatrudnia jednego fryzjera, który dysponuje jednym fotelem, oraz poczekalnią z pewną liczbą krzeseł. W chwili zakończenia strzyżenia klient opuszcza zakład natomiast fryzjer udaje się do poczekalni i sprawdza, czy czeka tam jakiś klient. Jeśli tak, zaprasza jednego na swój fotel i strzyże go. Jeśli nie ma czekających klientów usypia na fotelu.

Każdy klient, po wejściu do zakładu sprawdza, co robi fryzjer. Jeśli ten  śpi, budzi go i siada na fotelu. Jeśli fryzjer strzyże kogoś, klient idzie do poczekalni i sprawdza, czy jest wolne krzesło. Jeśli tak, siada i czeka, jeśli nie, wychodzi.

Uwagi:

Zarówno fryzjer, jak też każdy z klientów powinien zostać zaimplementowany jako oddzielny wątek.
Do synchronizacji należy użyć semaforów.
Każdy z wątków powinien wypisywać w odpowiednich momentach stosowne komunikaty, pozwalające na prześledzenie przebiegu programu.
Implementacja powinna zawierać metodę main ilustrującą działanie tej implementacji.
 */

import java.util.concurrent.Semaphore;

public class Zadanie_v2 throw InterruptedException
{
    public static void main(String[] args)
    {
        static Semaphore sem_klienci_w_kolejce = new Semaphore(0);
        Fryzjer fryzjer = new Fryzjer(sem_klienci_w_kolejce);
        SalonFryzjerski salon = new SalonFryzjerski(fryzjer, 3);

        int ile = 15;
        Thread watki_klienci[] = new KlientThread[ile];
        Fryzjer.start();
        for(int i = 0; i < ile; i++)
        {
            Thread.sleep(200);
            watki_klienci[i] = new KlientThread(i, salon);
            watki_klienci[i].start();
        }
    }
}

class Fryzjer throw InterruptedException
{
    static Semaphore sem_klienci_w_kolejce;
    public Fryzjer(Semaphore sem_klienci_w_kolejce)
    {
        this.sem_klienci_w_kolejce = sem_klienci_w_kolejce;
    }

    public void run()
    {
        System.out.println("Fryzjer siada na stanowisku pracy i zasypia");
        while(true)
        {
            sem_klienci_w_kolejce.acquire();
            System.out.println("Fryzjer obsługuje klienta");
            sem_klienci_w_kolejce.release();
        }
    }
}

class SalonFryzjerski throw InterruptedException
{
    static private int liczba_krzesel;
    static private Fryzjer fryzjer;
    private int ile_w_poczekalni;
    private int ile_u_fryzjera;

    static Semaphore sem_ochrona = new Semaphore(1);
    static Semaphore poczekalnia = new Semaphore(0);


    public SalonFryzjerski(Fryzjer fryzjer, int liczba_krzesel)
    {
        this.fryzjer = fryzjer;
        this.liczba_krzesel = liczba_krzesel;
        System.out.println("Salon fryzjerski posiada poczekalnie z liczbą krzeseł równą : " + liczba_krzesel);
    }

    public void chce_wejsc_klient(KlientThread klient)
    {
        int num_klienta = klient.getNum();

        sem_ochrona.acquire();
        System.out.println("Klient nr " + num_klienta + " wchodzi do salonu");

        // zmienna ile_u_fryzjera pozwala sprawdzić czy jest ktoś u fryzjera bez siadania w kolejce
        // jeśli nikogo nie ma u fryzjera i kolejka jest pusta to wchodzimy bezpośrednio i dopiero wtedy

        if(this.ile_u_fryzjera == 0 && this.ile_w_poczekalni == 0)
        {
            try
            {
                sem_fryzjera.acquire();
            }
            catch (InterruptedException ie) { System.err.println("InterruptedExceprion");}
            ile_u_fryzjera = 1;
            sem_ochrona.release();

            System.out.println("Klient nr " + num_klienta + " jest obslugiwany ...");
            try
            {
                Thread.sleep(400);
                sem_ochrona.acquire();
            } catch (InterruptedException ie) { System.err.println("InterruptedException"); }
            ile_u_fryzjera = 0;
            sem_ochrona.release();
            System.out.println(" ... a następnie wychodzi " + "Klient nr " + num_klienta);
            sem_fryzjera.release();
        }
        else if(this.ile_w_poczekalni < this.liczba_krzesel)
        {
            this.ile_w_poczekalni++;
            System.out.println("Klient nr " + num_klienta + " siada w poczekalni (miejsc " + this.ile_w_poczekalni + "/" + this.liczba_krzesel + ")");
            sem_ochrona.release();
            try
            {
                sem_fryzjera.acquire();
                sem_ochrona.acquire();
            }
            catch (InterruptedException ie) { System.err.println("InterruptedExceprion");}

            ile_w_poczekalni--;
            ile_u_fryzjera = 1;
            System.out.println("Klient nr " + num_klienta + " jest obslugiwany ... (miejsc " + this.ile_w_poczekalni + "/" + this.liczba_krzesel + ")");
            sem_ochrona.release();

            try
            {
                Thread.sleep(400);
                sem_ochrona.acquire();
            } catch (InterruptedException ie) { System.err.println("InterruptedException"); }
            ile_u_fryzjera = 0;
            sem_ochrona.release();
            System.out.println(" ... a następnie wychodzi " + "Klient nr " + num_klienta);
            sem_fryzjera.release();
        }
        else
        {
            System.out.println("Klient nr " + num_klienta + " wychodzi z poczekalnie bo nie ma wolnych miejsc");
            sem_ochrona.release();
        }
    }
}

class KlientThread extends Thread
{
    private int num;
    private SalonFryzjerski salon;
    public KlientThread(int num, SalonFryzjerski salon)
    {
        this.num = num;
        this.salon = salon;
    }

    public int getNum()
    {
        return this.num;
    }

    public void run() {
        salon.chce_wejsc_klient(this);
    }
}