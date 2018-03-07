public class Watek implements Runnable {

    private Integer x, y;
    private int num;
    public Watek(int num, Integer x, Integer y)
    {
        this.num = num;
        this.x = x;
        this.y = y;
    }

    @Override
    public void run() {
        System.out.println("Watek " + this.num + " : Wkładam " + this.x + " i będę próbował wyjąć " + this.y);
        Main.moj_worek.włóż(this.x);
        try {
            Main.moj_worek.wyjmij(this.y);
            System.out.println("Watek " + this.num + " : wyjął " + this.y);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
