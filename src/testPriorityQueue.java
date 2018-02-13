import java.util.concurrent.ThreadLocalRandom;

public class testPriorityQueue implements Runnable
{
  static PriorityQueue q = new PriorityQueue (10);
  static int count = 0;

  public testPriorityQueue ()
  {
  }

  public void run ()
  {
    long tid = Thread.currentThread ().getId ();
    int i = ThreadLocalRandom.current().nextInt(0, 100);
    int duration = ThreadLocalRandom.current().nextInt(0, 300);
    int times = 10;

    if (i < 66)
    {
      for (int j=0; j<times; j++)
      {
        int which = ThreadLocalRandom.current().nextInt(0, 6);
        int priority = ThreadLocalRandom.current().nextInt(0, 10);
        int myCount = ++count;
        System.out.println (tid + " q.add ("+ names[which] + ", " + priority + ")              " + myCount);
        int result = q.add (names[which], priority);
        System.out.println (" > q.add ("+ names[which] + ", " + priority + ") => " + result + "    " + myCount);
        // q.print ();

        int yes = ThreadLocalRandom.current().nextInt(0, 2);
        if (yes > 0 && result != -1)
        {
          try
          {
            Thread.sleep (duration);
          }
          catch (Exception e)
          {}
          System.out.println (tid + " q.getFirst ()             " + myCount);
          System.out.println (" > q.getFirst () => " + q.getFirst () + "     " + myCount);
          // q.print ();
        }
      }
    }
    else
    {
      for (int j=0; j<times; j++)
      {
        int which = ThreadLocalRandom.current().nextInt(0, 6);
        int myCount = ++count;
        System.out.println (tid + " q.search ("+ names[which] + ")              " + myCount);
        System.out.println (" > q.search ("+ names[which] + ") => " + q.search (names[which]) + "    " + myCount);
        // q.print ();
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
