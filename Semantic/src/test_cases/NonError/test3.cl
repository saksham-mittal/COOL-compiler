-- Dispatching of methods

class Main {
    main () : Int {
        0
    };
};
class A {
    foo() : Int {
        0
    };
};
class B {
    b : Int;
    a : A;
    foo1( x : Int ) : String { {
        b <- a.foo();
        "ABC";
    } };
};
class C {
    a : A;
    b : B;
    c : Int;
    d : String;
    foo2() : Object { {
        c <- foo3();
        c <- a@A.foo();
        d <- b@B.foo1(c);
    } };
    foo3() : Int {
        0
    };
};