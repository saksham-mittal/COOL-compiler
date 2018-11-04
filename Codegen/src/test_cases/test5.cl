-- Testing rules of assign

class Main {
    main () : Int { 
        {
            while 
            0;
        }
    };
};
class A {
    a : Int <- 5;
    -- b : B <- new B;
};
class B {
    a : String <- "ABC";
    -- b : C <- new C;
    c : Bool <- true;
};
class C {
    a : Int;
    foo() : Int {
        {
            a <- 5;
            0;
        }
    };
};