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
            let (tx_back, rx_back): (Sender<u64>, Receiver<u64>) = mpsc::channel();
            deposit_tx
                .send(AccountMessage {
                    command: 0,
                    amount: 10,
                    sender: tx_back,
                })
                .unwrap();

            let received = rx_back.recv().unwrap();
            println!("Current balance after deposit = {}", received);
            thread::sleep(Duration::from_millis(500));
        }
    });

    let withdraw_tx = tx.clone();
    let withdraw_thread = thread::spawn(move || {
        
        for _ in 0..5 {
            let (tx_back, rx_back): (Sender<u64>, Receiver<u64>) = mpsc::channel();
            withdraw_tx
                .send(AccountMessage {
                    command: 1,
                    amount: 5,
                    sender: tx_back,
                })
                .unwrap();
            let received = rx_back.recv().unwrap();
            println!("Current balance after withdraw = {}", received);
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
    sender: Sender<u64>,
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
        while let Ok(received) = self.receiver.recv() {

            if received.command == 0 {
                self.balance+=received.amount;
            } else if received.command == 1 && self.balance >= received.amount {
                self.balance-=received.amount;
            }
            received.sender.send(self.balance).unwrap();
        }
    }
}