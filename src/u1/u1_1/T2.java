package u1.u1_1;

public class T2 implements Runnable{
    private Thread t;


    /**
     * Automatically starts the thread on when calling the constructor.
     */
    public T2() {
        this.t = new Thread(this);
        this.t.start();
    }

    @Override
    public void run() {
        while(!t.isInterrupted()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                interrupt();
            }
            System.out.println("Tråd 2");
        }
    }

    public void interrupt(){
        t.interrupt();
    }
}
