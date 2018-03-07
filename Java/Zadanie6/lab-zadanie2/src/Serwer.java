import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;

/**
 * Created by skiline11 on 07.11.17.
 */
public class Serwer {
    public static final int N_grup = 4;
    public static final int K_zasobów = 2;
    private Integer watki_z_grupy_tych_ktorzy_juz_sa_czekajacy_na_zasob[];
    private Integer pierwszy_z_grupy_czekam_na_zasob;
    private Integer pozostali_z_grupy_czekajacy_na_zasob[];
    private int ile_z_grupy_czeka_na_zasob[] = new int[N_grup];
    private int ile_osob_czeka;
    private boolean czy_grupa_korzysta[] = new boolean[N_grup];
    private int ile_z_grupy_korzysta[] = new int[N_grup];
    private int z_ktorego_zasobu_grupa_korzysta[] = new int[N_grup];
    private List<Integer> lista_wolnych_zasobow = new ArrayList<Integer>();
    public Serwer()
    {
        this.pierwszy_z_grupy_czekam_na_zasob = new Integer(0);
        this.pozostali_z_grupy_czekajacy_na_zasob = new Integer[N_grup];
        this.watki_z_grupy_tych_ktorzy_juz_sa_czekajacy_na_zasob = new Integer[N_grup];
        ile_osob_czeka = 0;
        for(int i = 0; i < N_grup; i++)
        {
            czy_grupa_korzysta[i] = false;
            ile_z_grupy_czeka_na_zasob[i] = 0;
            ile_z_grupy_korzysta[i] = 0;
            this.pozostali_z_grupy_czekajacy_na_zasob[i] = new Integer(0);
            this.watki_z_grupy_tych_ktorzy_juz_sa_czekajacy_na_zasob[i] = new Integer(0);
        }
        for(int i = 0; i < K_zasobów; i++)
        {
            lista_wolnych_zasobow.add(i);
        }
    }

    public int chcęKorzystać(int grupa) throws InterruptedException
    {
        System.out.println("Watek z grupy " + grupa + " chce skorzystać");
        synchronized (this)
        {
            boolean dodalem_ze_czekam = false;
            if(czy_grupa_korzysta[grupa] == true)
            {
                if(ile_osob_czeka == 0)
                {
                    ile_z_grupy_korzysta[grupa]++;
                    return z_ktorego_zasobu_grupa_korzysta[grupa];
                }
                else
                {
                    ile_osob_czeka++;
                    dodalem_ze_czekam = true;
                    synchronized (this.watki_z_grupy_tych_ktorzy_juz_sa_czekajacy_na_zasob[grupa])
                    {
                        this.watki_z_grupy_tych_ktorzy_juz_sa_czekajacy_na_zasob[grupa].wait();
                    }
                }
            }

            // tutaj już nikt z naszej grupy nie korzysta
            if(lista_wolnych_zasobow.isEmpty()) // jeśli nie ma wolnych zasobów
            {
                if(ile_z_grupy_czeka_na_zasob[grupa] == 0) // nikt z mojej grupy nie czeka na zasób - jestem pierwszy
                {
                    ile_osob_czeka++;
                    ile_z_grupy_czeka_na_zasob[grupa]++;
                    synchronized (this.pierwszy_z_grupy_czekam_na_zasob)
                    {
                        System.out.println("Watek z grupy " + grupa + " czeka na zasób");
                        this.pierwszy_z_grupy_czekam_na_zasob.wait();
                    }
                    // jak zostane obudzony to
                    ile_osob_czeka--;
                    czy_grupa_korzysta[grupa] = true;
                    ile_z_grupy_czeka_na_zasob[grupa]--;
                    ile_z_grupy_korzysta[grupa]++;
                    z_ktorego_zasobu_grupa_korzysta[grupa] = lista_wolnych_zasobow.remove(0);
                    synchronized (this.pozostali_z_grupy_czekajacy_na_zasob[grupa])
                    {
                        this.pozostali_z_grupy_czekajacy_na_zasob[grupa].notifyAll();
                    }
                }
                else // ktos z mojej grupy czeka wiec ja czekam aż mnie obudzi
                {
                    ile_osob_czeka++;
                    ile_z_grupy_czeka_na_zasob[grupa]++;
                    synchronized (pozostali_z_grupy_czekajacy_na_zasob[grupa])
                    {
                        System.out.println("Watek z grupy " + grupa + " czeka na zasób");
                        pozostali_z_grupy_czekajacy_na_zasob[grupa].wait();
                    }
                    ile_osob_czeka--;
                    ile_z_grupy_czeka_na_zasob[grupa]--;
                    ile_z_grupy_korzysta[grupa]++;
                }
            }
            else // grupa nie korzysta i jest wolny zasób więc nikt z mojej grupy nie czeka
            {
                czy_grupa_korzysta[grupa] = true;
                ile_z_grupy_korzysta[grupa]++;
                z_ktorego_zasobu_grupa_korzysta[grupa] = lista_wolnych_zasobow.remove(0);
            }
            return z_ktorego_zasobu_grupa_korzysta[grupa];
        }
    }

    public void skończyłem(int grupa, int zasób)
    {
        ile_z_grupy_korzysta[grupa]--;
        if(ile_z_grupy_korzysta[grupa] == 0)
        {
            czy_grupa_korzysta[grupa] = false;
            lista_wolnych_zasobow.add(zasób);
            synchronized (pierwszy_z_grupy_czekam_na_zasob)
            {
                pierwszy_z_grupy_czekam_na_zasob.notify();
            }
            System.out.println("Grupa " + grupa + " skończyła korzystanie z zasobu " + zasób);
        }

    }
}
