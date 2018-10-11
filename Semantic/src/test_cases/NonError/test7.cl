-- testing rules for while

class Main {
    main () : Int {
        0
    };
};
class A {
    a : Int;
    f : Bool;
    foo() : Int {
        {  
            while a < 3 loop a <- a + 1 pool;
            while f loop a <- a + 1 pool;
            0;
        }
    };
};