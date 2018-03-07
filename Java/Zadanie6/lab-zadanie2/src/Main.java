import java.util.Random;

/**
 * Created by skiline11 on 07.11.17.
 */
public class Main {
    public static void main(String[] args)
    {
        Serwer serwer = new Serwer();
        System.out.println("W systemie mamy " + Serwer.N_grup + " grup procesów, przydzielanych losowo, tzn grupy mogą mieć różne liczności");
        int ile_watkow = Serwer.N_grup * 5;
        System.out.println("Łącznie wszystkich procesów będzie " + ile_watkow);
        System.out.println("Wszystkich zasobów jest " + Serwer.K_zasobów);
        Thread zadania[] = new Thread[ile_watkow];
        Random rand = new Random();
        for(int i = 0; i < ile_watkow; i++)
        {
            zadania[i] = new Thread(new Zadanie(rand.nextInt(Serwer.N_grup), serwer));
        }
        for(int i = 0; i < ile_watkow; i++) zadania[i].start();
        for(int i = 0; i < ile_watkow; i++)
        {
            try {
                zadania[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Koniec");
    }
}
