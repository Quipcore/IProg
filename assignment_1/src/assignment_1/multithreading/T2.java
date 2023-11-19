package assignment_1.multithreading;

/**
 * Threading class that implements the {@link Runnable} interface
 */
public class T2 implements Runnable{
    private Thread thread = new Thread(this);
    private boolean alive;

    private final int timeoutMiliseconds = 1000;


    /**
     * Create and starts a new {@link Thread}. This will also set interrupt flag low
     */
    public T2() {
        this.alive = true;
        this.thread.start();
    }

    /**
     * Prints "Tråd 2" until interrupted
     */
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

    /**
     * Sets interrupt flag for {@link T2#run()}
     */
    public void interrupt() {
        alive = false;
    }
}
