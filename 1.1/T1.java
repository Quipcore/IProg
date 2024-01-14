package assignment_1.multithreading;

/**
 * Thread class that extends {@link Thread}
 */
public class T1 extends Thread{

    private long timeoutMiliseconds = 1000L;

    /**
     * Creates thread and automatically starts execution
     */
    public T1() {
        start();
    }

    /**
     * Prints "Tråd 1" until interrupted
     */
    @Override
    public void run() {
        while(!isInterrupted()){
            try {
                sleep(timeoutMiliseconds);
            } catch (InterruptedException e) {
                interrupt();
            }
            System.out.println("Tråd 1");
        }
    }
}
