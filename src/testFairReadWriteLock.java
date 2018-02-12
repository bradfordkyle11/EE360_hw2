import java.util.concurrent.ThreadLocalRandom;

public class testFairReadWriteLock implements Runnable
{
  final FairReadWriteLock f;
  final int index;

  public testFairReadWriteLock (FairReadWriteLock f, int index)
  {
    this.f = f;
    this.index = index;
  }

  public void run ()
  {
    int i = ThreadLocalRandom.current().nextInt(0, 100);
    int duration = ThreadLocalRandom.current().nextInt(0, 500);
    if (i < 50)
    {
      System.out.println ("reading... " + Thread.currentThread ().getId () + " (" + index + ")");
      f.beginRead ();
      try
      {
        Thread.sleep (duration);
      }
      catch (Exception e)
      {}
      f.endRead ();
    }
    else
    {
      System.out.println ("writing... " + Thread.currentThread ().getId () + " (" + index + ")");
      f.beginWrite ();
      try
      {
        Thread.sleep (duration);
      }
      catch (Exception e)
      {}
      f.endWrite ();
    }
  }

  public static void main (String[] args)
  {
    final int SIZE = 10;
    Thread[] t = new Thread[SIZE];
    FairReadWriteLock f = new FairReadWriteLock ();

    for (int i = 0; i < SIZE; ++i) {
      t[i] = new Thread (new testFairReadWriteLock (f, i));
    }

    for (int i = 0; i < SIZE; ++i) {
      t[i].start();
    }
  }
}