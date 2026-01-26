package java_exercises.exercise6;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class Problem3 {
    

    public static void main(String args[]) {

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Cache cache = new Cache(0, new ArrayList<>());

        for(int i = 0; i < 10000; i++) {
            Request req = new Request((int)(Math.random() * 20) + 1);

            try {
                executor.execute(new Service(req, cache));
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
    }
}


class Service implements Runnable {

    Request req;
    Cache cache;

    public Service(Request req, Cache cache) {
        this.req = req;
        this.cache = cache;
    }

    @Override
    public void run() {
        Response res = this.cache.getCache();

        if(res.num == this.req.num) {
            System.out.println("Using cache");
        } else {
            res = new Response(req.num, factor(req.num));
            cache.updateCache(res.num, res.factors);
        }
        checkFactors(res);
    }


    List<Integer> factor(int num) {
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

    boolean checkFactors(Response res) {
        int n = 1;
        for(int num : res.factors) {
            n *= num;
        }
        if(res.num == n) {
            // System.out.printf("Factors %s are correct for num %d%n%n", res.factors.toString(), res.num);
            return true;
        }
        System.out.printf("Factors %s are wrong for num %d%n%n", res.factors.toString(), res.num);
        return false;
    }
}


class Request {

  int num;

  public Request(int num) {
    this.num = num;
  }
}

class Response {

  int num;
  List<Integer> factors;

  public Response(int num, List<Integer> factors) {
    this.num = num;
    this.factors = factors;
  }
}

class Cache {
  public int lastNum;
  public List<Integer> lastFactors;
  public ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  public ReadLock readLock = lock.readLock();
  public WriteLock writeLock = lock.writeLock();

  public Cache(int lastNum, List<Integer> lastFactors) {
    this.lastNum = lastNum;
    this.lastFactors = lastFactors;
  }

  public Response getCache() {
    readLock.lock();
    try {
      return new Response(this.lastNum, new ArrayList<>(this.lastFactors));
    } finally {
      readLock.unlock();
    }
  }

  public void updateCache(int newNum, List<Integer> newFactors) {
    try {
      writeLock.lock();
      this.lastNum = newNum;
      this.lastFactors = newFactors;
    } finally {
      writeLock.unlock();
      System.out.printf("Current Cache: %d  -  %s%n", this.lastNum, this.lastFactors);
    }
  }
}