package assignment_1.multithreading;

/**
 * Main class
 */
public class Main {
    /**
     * Main method
     * @param args NEVER USED!
     * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity. Will most likely never be thrown
     */
    public static void main(String[] args) throws InterruptedException {
        int timeoutSeconds = 5;

        T1 t1 = new T1();

        sleep(timeoutSeconds);

        T2 t2 = new T2();

        sleep(timeoutSeconds);

        t1.interrupt();

        sleep(timeoutSeconds);

        t2.interrupt();
    }

    private static void sleep(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000L);
    }

}
