package assignment_1.multithreading;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int timeoutMs = 5000;

        T1 t1 = new T1();
        t1.start();
        Thread.sleep(timeoutMs);

        T2 t2 = new T2();
        Thread.sleep(timeoutMs);

        t1.interrupt();
        Thread.sleep(timeoutMs);


        t2.interrupt();
    }
}
