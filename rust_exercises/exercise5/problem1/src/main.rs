use std::sync::Mutex;
use std::sync::Arc;
use std::thread;

fn main() {
    let account = Arc::new(Mutex::new(Account::new()));
    let mut handles = Vec::new();

    for _ in 0..10 {
        let account_clone = Arc::clone(&account);

        let handle = thread::spawn(move || {
            let mut acc = account_clone.lock().unwrap();
            for i in 1..9 {
                if i % 2  == 0 {
                    execute("w", 10, &mut *acc);
                } else {
                    execute("d", 10, &mut *acc);
                }
            }
        });
        handles.push(handle);
    }

    for handle in handles {
        handle.join().unwrap();
    }
}

fn execute(action: &str, amount: u64, account: &mut Account) {
    if action == "w" {
        account.withdraw(amount);
    } else if action == "d" {
        account.desposit(amount);
    }
    println!("Current Account Value: {}", account.balance);
}

struct Account {
    balance: u64,
}


impl Account {
    fn new() -> Self {
        Self {
            balance: 0,
        }
    }

    fn desposit(&mut self, amount: u64) {
        self.balance += amount;
    }

    fn withdraw(&mut self, amount: u64) {
        self.balance -= amount;
    }
}
