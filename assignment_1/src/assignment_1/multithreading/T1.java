package assignment_1.multithreading;

public class T1 extends Thread{

    private long timeoutMiliseconds = 1000L;

    public T1() {
        start();
    }

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
