/*
 * Kyle Bradford, Spencer Yue
 * kmb3534, sty223
 */

import java.util.concurrent.Semaphore; // for implementation using Semaphores

public class ThreadSynch {
  private Semaphore barrier, mutex;
  int active, N;

  public ThreadSynch(int parties) {
    barrier = new Semaphore(0);
    mutex = new Semaphore (1);
    this.active = this.N = parties;
  }

  public int await() throws InterruptedException {
    mutex.acquire ();
    active--;
    int index = active;
    mutex.release ();

    if (index > 0)
      barrier.acquire ();
    else
    {
      barrier.release (N-1);
      mutex.acquire ();
      active += N;
      mutex.release ();
    }

    return index;
  }
}
