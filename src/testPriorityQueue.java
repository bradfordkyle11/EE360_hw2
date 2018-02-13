import java.util.concurrent.ThreadLocalRandom;

public class testPriorityQueue implements Runnable
{
  static PriorityQueue q = new PriorityQueue (10);

  public testPriorityQueue ()
  {
  }

  public void run ()
  {
    int i = ThreadLocalRandom.current().nextInt(0, 100);
    int times = ThreadLocalRandom.current().nextInt(0, 3);

    if (i < 33)
    {
      for (int j=0; j<times; j++)
      {
        int which = ThreadLocalRandom.current().nextInt(0, 6);
        int priority = ThreadLocalRandom.current().nextInt(0, 10);
        System.out.println ("q.add ("+ names[which] + ", " + priority + ") => " + q.add (names[which], priority));
      }
    }
    else if (i < 66)
    {
      for (int j=0; j<times; j++)
      {
        int which = ThreadLocalRandom.current().nextInt(0, 6);
        System.out.println ("q.search ("+ names[which] + ") => " + q.search (names[which]));
      }
    }
    else
    {
      for (int j=0; j<times; j++)
      {
        System.out.println ("q.getFirst () => " + q.getFirst ());
      }
    }
  }

  String names[] = new String[] {"A", "B", "C", "D", "E", "F"};

  public static void main (String[] args)
  {
    final int SIZE = 10;
    Thread[] t = new Thread[SIZE];

    for (int i = 0; i < SIZE; ++i) {
      t[i] = new Thread (new testPriorityQueue ());
    }

    for (int i = 0; i < SIZE; ++i) {
      t[i].start();
    }
  }
}
