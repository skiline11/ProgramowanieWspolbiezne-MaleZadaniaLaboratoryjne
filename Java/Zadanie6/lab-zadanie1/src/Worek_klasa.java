import java.util.*;

public class Worek_klasa<T> implements Worek<T>
{
    private Map<T, List<T>> mapa_elementow;
    public Worek_klasa() {
        this.mapa_elementow = Collections.synchronizedMap(new HashMap());
    }

    @Override
    public void włóż(T wartość)
    {
        synchronized(wartość) // synchronizacja na obiekcie wartość
        {
            List<T> lista;
            if(this.mapa_elementow.containsKey(wartość))
            {
                lista = this.mapa_elementow.get(wartość);
            }
            else
			{
				lista = new ArrayList<T>();
				this.mapa_elementow.put(wartość, lista);
			}
            lista.add(wartość);
            wartość.notifyAll();
            wartość.notify();
        }
    }

    @Override
    public void wyjmij(T wartość) throws InterruptedException
    {
        synchronized(wartość)
        {
//            while(this.mapa_elementow.containsKey(wartość) == false) // to działa, użyte zamiast poniższego ifa -- spontaniczne pobudki (prawdopodobnie)
            if(this.mapa_elementow.containsKey(wartość) == false)
            {
                wartość.wait();
//                System.out.println("Wchodze po wait chce wyjac "+wartość);
            }
//            else System.out.println("Wchodze bez niczego");

            List<T> lista = this.mapa_elementow.get(wartość);
            lista.remove(wartość);
            if(lista.isEmpty())
            {
				this.mapa_elementow.remove(wartość);
            }
        }
    }
}
