use std::thread;
use std::sync::Arc;
use std::sync::Mutex;
use rand::Rng;

fn main() {
    let service = Arc::new(Mutex::new(FactorizerService::new()));

    let mut handles = Vec::new();

    for i in 0..10 {
        let service_clone = Arc::clone(&service);
        let handle = thread::spawn(move || {
            let mut acc = service_clone.lock().unwrap();


            for _ in 0..100 {
                let mut rng = rand::rng();
                let r_num = rng.random_range(1..5);

                let res = acc.service(r_num);
                acc.print_result(i, r_num, res);
            }
        });

        handles.push(handle);
    }

    for handle in handles {
        handle.join().unwrap();
    }
}



struct FactorizerService {
    LAST_NUMBER: u64,
    LAST_FACTORS: [u64;100],
}


impl FactorizerService {

    fn new() -> Self { 
        Self {
            LAST_NUMBER: 0,
            LAST_FACTORS: [0;100],
        }
    }

    fn service(&mut self, number: u64) -> [u64;100] {

        if number == self.LAST_NUMBER {
            println!("cache hit");
            return self.LAST_FACTORS;
        }
        println!("cache miss");
        let mut result: [u64; 100] = [0; 100];
        FactorizerService::factorizer(number, &mut result);
        
        self.LAST_NUMBER = number;
        self.LAST_FACTORS = result;
        return result;
    }

    fn factorizer(mut number: u64, result: &mut[u64;100]) {
        let mut i = 0;
        let mut n = 2;
        if number == 1 {result[i] = 1; return}
        while number != 1 {
            if number % n == 0 {
                number /= n;
                result[i] = n;
                i+=1;
            } else {
                n+=1;
            }
        }
    }

    fn print_result(&mut self, thread_id: u64, number: u64, result: [u64;100]) {

        let mut i = 0;

        print!("{thread_id} requested {number} = ");
        while result[i] != 0 {
            if i != 0 {
                print!(" * ")
            }
            print!("{}", result[i]);
            i+=1;
        }
        println!();
        println!();
    }
}