fn main() {
    

    let mut user: User = User::new(String::from("Michael"), 28, true);

    println!("Username = {}", user.name);

    user = user.toggleStatus();

    println!("User active status is {}", user.active);

    let user2 = User {
        active: true,
        ..user
    };

    println!("{}", user.active);
    println!("{}", user2.active);
    println!("{}", user.age);
}



struct User {
    name: String,
    age: u64,
    active: bool,
}


impl User {


    fn new(name: String, age: u64, active: bool) -> Self {
        Self {
            name,
            age,
            active,
        }
    }


    fn toggleStatus(mut self) -> Self {
        self.active = !self.active;
        return self;
    }
}
