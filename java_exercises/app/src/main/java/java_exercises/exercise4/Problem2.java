package java_exercises.exercise4;

import java.util.ArrayList;
import java.util.List;

public class Problem2 {
  


  public static void main(String args[]) throws InterruptedException {
    Thread t1 = new Thread(new Synchronized());
    Thread t2 = new Thread(new Unsynchronized());


    System.out.println("Warming up...");
    Thread.sleep(10000);

    System.out.println("Starting Thread 1");
    long startTimet1 = System.currentTimeMillis();

    t1.start();
    t1.join();

    long endTimet1 = System.currentTimeMillis();
    System.out.println("T1 execution time = " + (endTimet1 - startTimet1));

     System.out.println("Starting Thread 2");
    long startTimet2 = System.currentTimeMillis();

    t2.start();
    t2.join();

    long endTimet2 = System.currentTimeMillis();
    System.out.println("T2 execution time = " + (endTimet2 - startTimet2));


    System.out.println("Difference = " + ((endTimet1 - startTimet1) - (endTimet2 - startTimet2)));
  }
}




class Synchronized implements Runnable {

  private int counter;

  @Override
  public void run() {
    for(int i = 0; i < 1000000;i++) {
      factor(600_851_475_143L);
    }
  }

  synchronized List<Integer> factor(long num) {
    List<Integer> factors = new ArrayList<>();
    int n = 2;
    while(num > 1) {
      if (num % n == 0) {
        factors.add(n);
        num /= n;
      } else {
        n+=1;
      }
    }
    return factors;
  }

  void increment() {
    counter+=1;
  }
}


class Unsynchronized implements Runnable {

  private int counter;

  @Override
  public void run() {
    for(int i = 0; i < 1000000;i++) {
      factor(600_851_475_143L);
    }
  }

  List<Integer> factor(long num) {
    List<Integer> factors = new ArrayList<>();
    int n = 2;
    while(num > 1) {
      if (num % n == 0) {
        factors.add(n);
        num /= n;
      } else {
        n+=1;
      }
    }
    return factors;
  }

  void increment() {
    counter+=1;
  }
}
