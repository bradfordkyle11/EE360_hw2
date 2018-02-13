import java.util.*;
import java.util.concurrent.locks.*;

@SuppressWarnings("unchecked")
public class PriorityQueue {
  private LinkedList<String> q[] = new LinkedList[10];
  private ReentrantLock q_lock[] = new ReentrantLock[10];
  private ReentrantLock length_mutex = new ReentrantLock ();
  private Condition full = length_mutex.newCondition ();
  private Condition empty = length_mutex.newCondition ();
  private ArrayDeque<String> name_q = new ArrayDeque<String> ();
  private ReentrantLock name_q_mutex = new ReentrantLock ();
  private int length = 0;
  private int maxSize;

  public PriorityQueue (int maxSize) {
    // Creates a Priority queue with maximum allowed size as capacity
    for (int i=0; i<10; i++)
    {
      q[i] = new LinkedList<String> ();
      q_lock[i] = new ReentrantLock ();
    }
    this.maxSize = maxSize;
  }

  public int add (String name, int priority) {
    // Adds the name with its priority to this queue.
    // Returns the current position in the list where the name was inserted;
    // otherwise, returns -1 if the name is already present in the list.
    // This method blocks when the list is full.
    length_mutex.lock ();
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
      length_mutex.unlock ();
    }

    int result = -1;
    name_q_mutex.lock ();
    if (!name_q.contains (name))
    {
      name_q.add (name);
      name_q_mutex.unlock ();

      if (search (name) == -1)
      {
        q_lock[priority].lock ();
        q[priority].add (name);
        q_lock[priority].unlock ();

        name_q_mutex.lock ();
        name_q.remove (name);
        name_q_mutex.unlock ();

        // System.out.println ("(" + name + ", " + priority + ") before...");
        result = search (name);
        // System.out.println ("(" + name + ", " + priority + ") after...");

        length_mutex.lock ();
        empty.signal ();
        length_mutex.unlock ();
      }
    }
    else
      name_q_mutex.unlock ();

    if (result == -1)
    {
      length_mutex.lock ();
      length--;
      length_mutex.unlock ();
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
      q_lock[i].lock ();
      result = q[i].indexOf (name);
      if (result == -1)
        offset += q[i].size ();
      q_lock[i].unlock ();
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
    length_mutex.lock ();
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
      length_mutex.unlock ();
    }

    String result = null;
    for (int i=9; i>-1; i--)
    {
      q_lock[i].lock ();
      result = q[i].pollFirst ();
      q_lock[i].unlock ();
      if (result != null)
        break;
    }
    assert (result != null);

    length_mutex.lock ();
    full.signal ();
    length_mutex.unlock ();

    return result;
  }
}

