import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws InterruptedException{
        PhoneStationMultithreads ps = new PhoneStationMultithreads();
        ArrayList<Thread> threadsList = new ArrayList<>();

        for (int i = 0; i < 20; i++){
            threadsList.add(new Thread(ps));
        }
        for (Thread thread : threadsList){
            thread.start();
        }
        for (Thread thread : threadsList){
            thread.join();
        }
    }
}
