/*
* EID's of group members
*
*/

public class MonitorThreadSynch {
  int active, N, round;

  public MonitorThreadSynch(int parties) {
    this.active = this.N = parties;
    round = 0;
  }

  public int await() throws InterruptedException {
    int index;
    synchronized (this) {
      index = --active;
      int my_round = round;

      if (index != 0)
        while (active < N && round == my_round)
          wait ();
      else
      {
        active = N;
        round++;
        notifyAll ();
      }
    }

    return index;
  }
}
