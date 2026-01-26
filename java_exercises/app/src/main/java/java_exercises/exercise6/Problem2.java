package java_exercises.exercise6;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Problem2 {
  
  

  public static void main(String[] args) {
    int n = 5;

    ReentrantLock table = new ReentrantLock();

    ConditionedPhilosopher[] philosophers = new ConditionedPhilosopher[n];
    Thread[] threads = new Thread[n];

    for (int i = 0; i < n; i++) {
      philosophers[i] = new ConditionedPhilosopher((char) ('A' + i), table);
    }

    for (int i = 0; i < n; i++) {
      philosophers[i].setLeft(philosophers[(i + n - 1) % n]);
      philosophers[i].setRight(philosophers[(i + 1) % n]);
    }

    for (int i = 0; i < n; i++) {
      threads[i] = new Thread(philosophers[i]);
      threads[i].start();
    }
}
}


class ConditionedPhilosopher implements Runnable {
  private final char id;
  private boolean eating;
  private ConditionedPhilosopher left, right;
  private final ReentrantLock table;

  public ConditionedPhilosopher(char id, ReentrantLock table) {
    this.id = id;
    eating = false;
    this.table = table;
  }

  public void setLeft(ConditionedPhilosopher left) {
    this.left = left;
  }

  public void setRight(ConditionedPhilosopher right) {
    this.right = right;
  }

  @Override
  public void run() {
    try {
      while (true) { 
      think();
      eat();
      }
    } catch (InterruptedException ex) {
    // nothing to clean up, terminate
    }
  }
  
  private void think() throws InterruptedException {
    synchronized(table) {
      eating = false;
      table.notifyAll(); 
    }
    System.out.println("Philosopher " + id + " thinks for a while");
    TimeUnit.SECONDS.sleep(1);
  }

  private void eat() throws InterruptedException {
    synchronized(table) {
      while (left.eating || right.eating) {
        table.wait();
      }
      eating = true;
    }
    System.out.println("Philosopher " + id + " eats for a while");
    TimeUnit.SECONDS.sleep(1);
  }
}



class Table {

}