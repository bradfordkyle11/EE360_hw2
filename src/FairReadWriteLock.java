import java.util.concurrent.locks.*;
import java.util.*;


public class FairReadWriteLock
{
  private final boolean verbose = false; // turn on for logging
  private final ArrayDeque<Long> q = new ArrayDeque<Long> ();
  private int readers = 0, writer = 0;

  public synchronized void beginRead ()
  {
    long tid = Thread.currentThread ().getId ();
    try
    {
      q.add (tid);
      while (writer != 0 || q.getFirst () != tid)
        wait ();
      q.pollFirst ();
      readers++;
    }
    catch (Exception e)
    {}

    if (verbose) System.out.println ("beginRead: " + tid);
  }

  public synchronized void endRead ()
  {
    if (verbose) System.out.println ("endRead: hello...");
    try
    {
      readers--;
      notifyAll ();
    }
    catch (Exception e)
    {
      if (verbose) System.out.println ("pooop");
      if (verbose) System.out.println ("endRead: " + Thread.currentThread().getId());
    }
  }

  public synchronized void beginWrite ()
  {
    long tid = Thread.currentThread ().getId ();
    try
    {
      q.add (tid);
      while (writer != 0 || readers != 0 || q.getFirst () != tid)
        wait ();
      q.pollFirst ();
      writer++;
    }
    catch (Exception e)
    {}

    if (verbose) System.out.println ("beginWrite: " + tid);
  }

  public synchronized void endWrite ()
  {
    if (verbose) System.out.println ("endWrite: hello...");
    try
    {
      writer--;
      notifyAll ();
    }
    catch (Exception e)
    {
      if (verbose) System.out.println ("pooop");
      if (verbose) System.out.println ("endWrite: " + Thread.currentThread().getId());
    }
  }
}
