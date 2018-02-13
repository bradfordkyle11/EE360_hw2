import java.util.*;
import java.util.concurrent.locks.*;

@SuppressWarnings("unchecked")
public class PriorityQueue {
  private LinkedList<String> q[] = new LinkedList[10];
  private ReentrantLock qLock[] = new ReentrantLock[10];
  private ReentrantLock lengthMutex = new ReentrantLock ();
  private Condition full = lengthMutex.newCondition ();
  private Condition empty = lengthMutex.newCondition ();
  private ArrayDeque<String> nameQ = new ArrayDeque<String> ();
  private ReentrantLock nameQMutex = new ReentrantLock ();
  private int length = 0;
  private int maxSize;

  public PriorityQueue (int maxSize) {
    // Creates a Priority queue with maximum allowed size as capacity
    for (int i=0; i<10; i++)
    {
      q[i] = new LinkedList<String> ();
      qLock[i] = new ReentrantLock ();
    }
    this.maxSize = maxSize;
  }

  public int add (String name, int priority) {
    // Adds the name with its priority to this queue.
    // Returns the current position in the list where the name was inserted;
    // otherwise, returns -1 if the name is already present in the list.
    // This method blocks when the list is full.
    lengthMutex.lock ();
    try
    {
      while (length >= maxSize)
        full.await ();
      length++;
    }
    catch (Exception e)
    {}
    finally
    {
      lengthMutex.unlock ();
    }

    int result = -1;
    nameQMutex.lock ();
    if (!nameQ.contains (name))
    {
      nameQ.add (name);
      nameQMutex.unlock ();

      if (search (name) == -1)
      {
        qLock[priority].lock ();
        q[priority].add (name);
        qLock[priority].unlock ();

        nameQMutex.lock ();
        nameQ.remove (name);
        nameQMutex.unlock ();

        // System.out.println ("(" + name + ", " + priority + ") before...");
        result = search (name);
        // System.out.println ("(" + name + ", " + priority + ") after...");

        lengthMutex.lock ();
        empty.signal ();
        lengthMutex.unlock ();
      }
    }
    else
      nameQMutex.unlock ();

    if (result == -1)
    {
      lengthMutex.lock ();
      length--;
      lengthMutex.unlock ();
    }

    return result;
  }

  public int search (String name) {
    // Returns the position of the name in the list;
    // otherwise, returns -1 if the name is not found.
    int result = -1;
    int offset = 0;

    for (int i=9; i>-1; i--)
    {
      qLock[i].lock ();
      result = q[i].indexOf (name);
      if (result == -1)
        offset += q[i].size ();
      qLock[i].unlock ();
      if (result != -1)
        break;
    }

    if (result == -1)
      return result;

    return result + offset;
  }

  public String getFirst () {
    // Retrieves and removes the name with the highest priority in the list,
    // or blocks the thread if the list is empty.
    lengthMutex.lock ();
    try
    {
      while (length == 0)
        empty.await ();
      length--;
    }
    catch (Exception e)
    {}
    finally
    {
      lengthMutex.unlock ();
    }

    String result = null;
    for (int i=9; i>-1; i--)
    {
      qLock[i].lock ();
      result = q[i].pollFirst ();
      qLock[i].unlock ();
      if (result != null)
        break;
    }
    assert (result != null);

    lengthMutex.lock ();
    full.signal ();
    lengthMutex.unlock ();

    return result;
  }
}

