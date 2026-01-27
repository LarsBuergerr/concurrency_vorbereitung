package java_exercises.exercise7;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;


public class Problem1 {
  

  public static void main(String args[]) throws InterruptedException, ExecutionException {

    CustomExecutorCompletionService executor = new CustomExecutorCompletionService(3);

    executor.submit(() -> { Thread.sleep(300); return 1; });
    executor.submit(() -> { Thread.sleep(100); return "TEST"; });
    executor.submit(() -> { Thread.sleep(200); return new Object(); });

    for (int k = 0; k < 3; k++) {
      Future<?> f = executor.take();
      System.out.println(f.get());
    }

    executor.shutdown();
  }
}


  
class CustomExecutorCompletionService {

  Worker[] workers;
  BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
  BlockingQueue<Future<?>> completionQueue = new LinkedBlockingQueue<>();

  public CustomExecutorCompletionService(int size) throws InterruptedException {
    workers = new Worker[size];

    for(int i = 0; i < size; i++) {
      workers[i] = new Worker();
      workers[i].start();
    }
  }

  private class Worker extends Thread {

    public void run() {
      while(true) {
        try {
          Runnable task = taskQueue.take();
          task.run();
        } catch (InterruptedException e) {
          break;
        }
      }
    }
  }

  void submit(Runnable task) {
    this.taskQueue.offer(task);
  }

  <T> Future<T> submit(Callable<T> task) {
    FutureTask<T> futureTask = new CompletedFutureTask<>(task);
    taskQueue.offer(futureTask);
    return futureTask;
  }

  public Future<?> take() throws InterruptedException {
    return completionQueue.take();
  }

  void shutdown() {
    for(Worker w : workers) {
      w.interrupt();
    }
  }  


  class CompletedFutureTask<T> extends FutureTask<T> {

    public CompletedFutureTask(Callable<T> callable) {
      super(callable);
    }

    @Override
    protected void done() {
      completionQueue.offer(this);
    }
  }
}