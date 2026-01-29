package java_exercises.random;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Generator {
  


  public static void main(String args[]) throws InterruptedException {

    PrimeGenerator p = new PrimeGenerator();
    p.start();

  
    Thread.sleep(1000);

    System.out.println("this");
    p.interrupt();
    System.out.println(p.get().toString());
  }
}


class PrimeGenerator extends Thread {

  private final List<BigInteger> primes = new ArrayList<>();

  @Override
  public void run() {
    BigInteger p = BigInteger.ONE;
    
    while (!this.isInterrupted()) {
      p = p.nextProbablePrime();
      System.out.println(p);
      synchronized(this) {
        primes.add(p);
      }
    }
  }

  public synchronized List<BigInteger> get() {
    return new ArrayList<BigInteger>(primes);
  }
}
