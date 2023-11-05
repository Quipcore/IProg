package assignment_1.multithreading;


public class Main {
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
