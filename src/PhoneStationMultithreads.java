import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class PhoneStationMultithreads implements Runnable {
    static Semaphore Op = new Semaphore(2);  // operator access control
    static Semaphore Line = new Semaphore(1); // Line  access control
    static int counter = 0; // Counter
    static int waiting_op = 0; // Keep operator queue
    static int waiting_line = 0; // Keep operator queue

    public PhoneStationMultithreads(){
        new Thread(new Runnable() { // Update console from other threads
            @Override
            public void run() {
                do {
                    printStation();
                    try {
                        Thread.sleep(50); // Updating every 50ms
                    }catch (InterruptedException e){
                        throw new RuntimeException(e);
                    }
                }while (counter < 20); // Until 20 people talked
                printStation();
                System.out.println(counter + "People talked");
                System.out.println("Press enter for exit!");
                new Scanner(System.in).nextLine();
            }
        }).start();
    }

    @Override
    public void run() {
        waiting_op++;  // Add threads to operator waiting queue
        try {
            getOp();
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }


    private void getOp() throws InterruptedException{
        if(Op.tryAcquire()){
            Thread.sleep((long)(Math.random() * 1000)); // Communicate operator
            waiting_op--;
            waiting_line++;
            getLine();
        }else{
            Thread.sleep((long) (Math.random() * 1000)); // Communicate operator
            getOp();
        }
    }


    private synchronized void getLine() throws InterruptedException{
        if(!Line.tryAcquire()){
            wait();
        }
        Thread.sleep((long) (Math.random() * 1000)); // Random talking time
        counter++; // Update counter
        waiting_line--;
        Line.release(); // Release line semaphore
        Op.release(); // Release operator semaphore
        notify(); // Notify threads
    }


    private void printStation(){
        clear_console();
        System.out.println("Line: " + Line.availablePermits());
        System.out.println("Operators: " + Op.availablePermits());
        System.out.println("Waiting_Line: " + waiting_line);
        System.out.println("Waiting_op: " + waiting_op);
        System.out.println("==============================================");
    }


    private static void clear_console() {
        //clear console
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033\143");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
