package assignment_1.multithreading;

public class T2 implements Runnable{
    private Thread thread = new Thread(this);
    private boolean alive;

    private final int timeoutMiliseconds = 1000;


    /**
     * Automatically starts the thread when calling the constructor.
     */
    public T2() {
        this.alive = true;
        this.thread.start();
    }

    @Override
    public void run() {
        while(alive){
            try {
                Thread.sleep(timeoutMiliseconds);
            } catch (InterruptedException e) {
                interrupt();
            }
            System.out.println("Tråd 2");
        }
   }

    public void interrupt() {
        alive = false;
    }
}
