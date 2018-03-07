public class Zadanie implements Runnable {

    private final int grupa;
    private final Serwer serwer;

    private void własneSprawy() {
        System.out.println("Watek z grupy " + this.grupa + " wykonuje własne sprawy");
    }

    private void korzystam(int zasób) {
        System.out.println("Watek z grupy " + this.grupa + " korzysta z zasobu " + zasób);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Zadanie(final int grupa, final Serwer serwer) {
        this.grupa = grupa;
        this.serwer = serwer;
    }

    @Override
    public void run() {
//        while (true) {
            własneSprawy();
            int zasób = 0;
            try {
                zasób = serwer.chcęKorzystać(grupa);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            korzystam(zasób);
            serwer.skończyłem(grupa, zasób);
//        }
    }
}