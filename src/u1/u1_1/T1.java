package u1.u1_1;

public class T1 extends Thread{
    public T1() {
        super();
    }

    @Override
    public void run() {
        while(!isInterrupted()){
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                interrupt();
            }
            System.out.println("Tråd 1");
        }
    }
}
