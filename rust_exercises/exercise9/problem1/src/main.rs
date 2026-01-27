use std::sync::mpsc::{self, Receiver, Sender};
use std::thread;
use std::time::Duration;

fn main() {
    let (account, tx) = Account::new();

    thread::spawn(move || {
        account.run();
    });

    let deposit_tx = tx.clone();
    let deposit_thread = thread::spawn(move || {
        for _ in 0..5 {
            deposit_tx
                .send(AccountMessage {
                    command: 0,
                    amount: 10,
                })
                .unwrap();
            thread::sleep(Duration::from_millis(500));
        }
    });

    let withdraw_tx = tx.clone();
    let withdraw_thread = thread::spawn(move || {
        for _ in 0..5 {
            withdraw_tx
                .send(AccountMessage {
                    command: 1,
                    amount: 5,
                })
                .unwrap();
            thread::sleep(Duration::from_millis(700));
        }
    });

    deposit_thread.join().unwrap();
    withdraw_thread.join().unwrap();

    thread::sleep(Duration::from_secs(2));

}


struct AccountMessage {
    command: u64,
    amount: u64,
}

struct Account {
    balance: u64,
    receiver: Receiver<AccountMessage>
}

impl Account {
   
    fn new() -> (Self, Sender<AccountMessage>) {
        let (tx, rx) = mpsc::channel();
        let account = Self {
            balance: 0,
            receiver: rx,
        };
        return (account, tx);
    }

    fn run(mut self) {
        loop {
            let received = self.receiver.recv().unwrap();

            if received.command == 0 {
                self.balance+=received.amount;
            } else if received.command == 1 {
                self.balance-=received.amount;
            }

            println!("Current balance = {}", self.balance);
        }
    }
}