use std::sync::mpsc::{self, Receiver, Sender};
use std::thread;
use std::time::Duration;

fn main() {
    let (tx, rx) = mpsc::channel();
    thread::spawn(move || {
        let vals = vec![
            String::from("hi"), String::from("from"),
            String::from("the"), String::from("thread")
        ];
        for val in vals {
            tx.send(val).unwrap();
            thread::sleep(Duration::from_secs(1));
        }
    });
    for received in rx {
        println!("Got: {received}");
    }
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
        }
    }
}