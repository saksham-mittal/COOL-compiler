-- testing rules for arithmetic operations 

class Main {
    main () : Int {
        0
    };
};
class A {
    a : Int;
    b : Int;
    c : String;
    foo() : Int {
        let x : Int <- 5 in {
            a <- a + x;
            b <- b - a;
            x <- a * b;
            b <- b / x;
            0;
        }
    };
};
