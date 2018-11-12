-- Trivial testcase 1

class Main {
    main () : Int {
        0
    };
};
class A {
    a : Int;
    b : Int;
    x : Int;
    foo() : Int {
        {
            a <- a + x;
            b <- b - a;
            x <- a * b;
            b <- b / x;
            0;
        }
    };
};