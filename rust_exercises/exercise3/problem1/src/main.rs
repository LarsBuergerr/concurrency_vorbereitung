use std::char::MAX;

use rand::Rng;

static mut LAST_NUMBER: u64 = 0;
static mut LAST_FACTORS: [u64; 100] = [0;100];

fn main() {

    let mut rng = rand::rng();
    
    for _ in 1..100 {
        let random_num = rng.random_range(1..10);
        let res = service(random_num);
        print_result(random_num, res);
    }

}

fn service(number: u64) -> [u64;100] {
    unsafe {
        if number == LAST_NUMBER {
            println!("cache hit");
            return LAST_FACTORS;
        }
    }
    println!("cache miss");
    let mut result: [u64; 100] = [0; 100];
    factorizer(number, &mut result);
    unsafe {
        LAST_NUMBER = number;
        LAST_FACTORS = result;
    }
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

fn print_result(number: u64, result: [u64;100]) {

    let mut i = 0;

    print!("{number} = ");
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