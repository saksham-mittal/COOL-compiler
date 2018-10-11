-- Testing rules for let expression

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
        let x : Int <- 5 in
        let b : String in
        let c : Int <- 10 in {
            x <- x + a;
            b <- "ABC";
            c <- a + x;
            0;
        }  
    };
};