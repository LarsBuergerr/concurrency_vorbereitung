package java_exercises.exercise2;   

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Problem1 {


    public static void main(String[] args) {
        
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Account a = new Account(0);
        
        for(int i = 0; i < 100; i++) {
            try {
                executor.execute(new Action(a));
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        System.out.printf("Account balance: %d", a.balance);
    }
}


class Account {

    int balance;

    public Account(int balance) {
        this.balance = balance;
    }

    void deposit(int amount) {
        this.balance += amount;
    }

    void withdraw(int amount) {
        this.balance -= amount;
    }
}

class Action implements Runnable {

    Account acc;

    public Action(Account acc) {
        this.acc = acc;
    }

    @Override
    public void run() {
        this.acc.deposit(100);
        this.acc.withdraw(100);
    }
}