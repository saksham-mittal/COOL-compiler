-- Trivial testcase 2

class Main {
    main () : Int {
        0
    };
};
class A {
    a : Bool;
    b : Int;
    foo() : Bool { {
        b = ~ b;
        a = not a;
        a;
    } };
};