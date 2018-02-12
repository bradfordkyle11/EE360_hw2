/*
 * EID's of group members
 * 
 */
import java.util.ArrayDeque;
import java.util.concurrent.Semaphore; // for implementation using Semaphores

public class ThreadSynch {
    int curIndex;
    private Semaphore s;

    public ThreadSynch(int parties) {
        curIndex = parties - 1;
        s = new Semaphore(1);
    }

    public int await() throws InterruptedException {
        int index = 0;

        // you need to write this code
        s.acquire();
        index = curIndex;
        curIndex--;
        s.release();

        while(curIndex > 0){
            wait();
        }
        return index;    
    }
}
