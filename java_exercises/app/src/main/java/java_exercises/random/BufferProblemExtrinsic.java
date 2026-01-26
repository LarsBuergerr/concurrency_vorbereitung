package java_exercises.random;

import java.rmi.ConnectIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BufferProblemExtrinsic {
  


  public static void main(String args[]) throws InterruptedException {
    int n = 3;

    BoundedBufferExplicit buffer = new BoundedBufferExplicit(3);

    Thread[] consumer_threads = new Thread[n];
    Thread[] producer_threads = new Thread[n];


    List<Producer> producers = new ArrayList<Producer>();
    List<Consumer> consumers = new ArrayList<Consumer>();

    for(int i = 1; i <= n; i++) {
      producers.add(new Producer(i, buffer));
      consumers.add(new Consumer(i, buffer));
    }

    for(int i = 0; i < n; i++) {
      producer_threads[i] = new Thread(producers.get(i));
      consumer_threads[i] = new Thread(consumers.get(i));

      producer_threads[i].start();
      consumer_threads[i].start();
    }

    Thread.sleep(10000);
    for (int i = 0; i < n; i++) {
      producer_threads[i].interrupt();
      consumer_threads[i].interrupt();
    }
  }
}


class BoundedBufferExplicit {
    int size;
    List<Integer> buffer = new ArrayList<>();
    ReentrantLock lock = new ReentrantLock();
    Condition notFull = lock.newCondition();
    Condition notEmpty = lock.newCondition();

    public BoundedBufferExplicit(int size) {
      this.size = size;
    }

    public void put(int value) throws InterruptedException {
      buffer.add(value);
    }

    public void remove() throws InterruptedException {
      buffer.remove(0);
    }

    public boolean isEmpty() {
      return buffer.size() == 0;
    }

    public boolean isFull() {
      return buffer.size() == this.size;
    }

    @Override
    public String toString() {
      
      return this.buffer.toString();
    }
}



class Producer implements Runnable {

  int id;
  BoundedBufferExplicit buffer;


  public Producer(int id, BoundedBufferExplicit buffer) {
    this.id = id;
    this.buffer = buffer;
  }
  @Override
  public void run() {
      while(true) {
        this.buffer.lock.lock();

        try {
          while(this.buffer.isFull()) {
            this.buffer.notFull.await();
          }
          System.out.println("Producer " + this.id + " is adding " + this.id + " to buffer");
          Thread.sleep(1000);
          this.buffer.put(this.id);
          this.buffer.notEmpty.signal();
        } catch(InterruptedException e) {
          System.out.println("Producer " + this.id + " is interrupted...");
          break;
        } finally {
          System.out.println(this.buffer.toString());
          this.buffer.lock.unlock();
        }
      }
  }
}

class Consumer implements Runnable {

  int id;
  BoundedBufferExplicit buffer;

  public Consumer(int id, BoundedBufferExplicit buffer) {
    this.id = id;
    this.buffer = buffer;
  }
  @Override
  public void run() {
    while(true) {
      this.buffer.lock.lock();
      try {
        while(this.buffer.isEmpty()) {
          this.buffer.notEmpty.await();
        }
        System.out.println("Consumer " + this.id + " is taking first item from buffer");
        Thread.sleep(1000);
        this.buffer.remove();
        this.buffer.notFull.signal();
      } catch(InterruptedException e) {
        System.out.println("Producer " + this.id + " is interrupted...");
        break;
      } finally {
        System.out.println(this.buffer.toString());
        this.buffer.lock.unlock();
      }
    }
  }
}