package java_exercises.exercise4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

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
        Response res;

        Cache curr = this.cache.getCache();

        if(curr.lastNum == this.req.num) {
            System.out.println("Using cache");
            res = new Response(req.num, curr.lastFactors);
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
  public ReentrantLock lock = new ReentrantLock();

  public Cache(int lastNum, List<Integer> lastFactors) {
    this.lastNum = lastNum;
    this.lastFactors = lastFactors;
  }

  public Cache getCache() {
    lock.lock();
    try {
      return new Cache(this.lastNum, this.lastFactors);
    } finally {
      lock.unlock();
    }
  }

  public void updateCache(int newNum, List<Integer> newFactors) {
    lock.lock();
    this.lastNum = newNum;
    this.lastFactors = newFactors;
    lock.unlock();

    // System.out.printf("Current Cache: %d  -  %s%n", this.lastNum, this.lastFactors);
  }
}