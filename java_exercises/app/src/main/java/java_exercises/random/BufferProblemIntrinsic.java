package java_exercises.random;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class BufferProblemIntrinsic {

  public static void main(String args[]) throws InterruptedException {
    int n = 3;

    BoundedBufferIntrinsic buffer = new BoundedBufferIntrinsic(3);

    Thread[] consumerIntrinsic_threads = new Thread[n];
    Thread[] producerIntrinsic_threads = new Thread[n];


    List<ProducerIntrinsic> producerIntrinsics = new ArrayList<ProducerIntrinsic>();
    List<ConsumerIntrinsic> consumerIntrinsics = new ArrayList<ConsumerIntrinsic>();

    for(int i = 1; i <= n; i++) {
      producerIntrinsics.add(new ProducerIntrinsic(i, buffer));
      consumerIntrinsics.add(new ConsumerIntrinsic(i, buffer));
    }

    for(int i = 0; i < n; i++) {
      producerIntrinsic_threads[i] = new Thread(producerIntrinsics.get(i));
      consumerIntrinsic_threads[i] = new Thread(consumerIntrinsics.get(i));

      producerIntrinsic_threads[i].start();
      consumerIntrinsic_threads[i].start();
    }

    Thread.sleep(10000);
    for (int i = 0; i < n; i++) {
      producerIntrinsic_threads[i].interrupt();
      consumerIntrinsic_threads[i].interrupt();
    }
  }
}


class BoundedBufferIntrinsic {
    int size;
    List<Integer> buffer = new ArrayList<>();
    ReentrantLock lock = new ReentrantLock();

    public BoundedBufferIntrinsic(int size) {
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



class ProducerIntrinsic implements Runnable {

  int id;
  BoundedBufferIntrinsic buffer;


  public ProducerIntrinsic(int id, BoundedBufferIntrinsic buffer) {
    this.id = id;
    this.buffer = buffer;
  }
  @Override
  public void run() {
      while(true) {
        synchronized(this.buffer.lock) {
          try {
            while(this.buffer.isFull()) {
              this.buffer.lock.wait();
            }
            System.out.println("ProducerIntrinsic " + this.id + " is adding " + this.id + " to buffer");
            Thread.sleep(1000);
            this.buffer.put(this.id);
            this.buffer.lock.notifyAll();
          } catch(InterruptedException e) {
            System.out.println("ProducerIntrinsic " + this.id + " is interrupted...");
            break;
          }
        }
      }
  }
}

class ConsumerIntrinsic implements Runnable {

  int id;
  BoundedBufferIntrinsic buffer;

  public ConsumerIntrinsic(int id, BoundedBufferIntrinsic buffer) {
    this.id = id;
    this.buffer = buffer;
  }
  @Override
  public void run() {
    while(true) {
      synchronized(this.buffer.lock) {
        try {
          while(this.buffer.isEmpty()) {
            this.buffer.lock.wait();
          }
          System.out.println("ConsumerIntrinsic " + this.id + " is taking first item from buffer");
          Thread.sleep(1000);
          this.buffer.remove();
          this.buffer.lock.notifyAll();
        } catch(InterruptedException e) {
          System.out.println("ProducerIntrinsic " + this.id + " is interrupted...");
          break;
        }
      }
    }
  }
}