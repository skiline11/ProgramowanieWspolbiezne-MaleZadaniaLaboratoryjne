public interface Worek<T>
{
    public void włóż(T wartość);
    public void wyjmij(T wartość) throws InterruptedException;
}
