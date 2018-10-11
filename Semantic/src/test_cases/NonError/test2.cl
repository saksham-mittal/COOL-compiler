-- Initialization of attributes and creating new attributes

class Main {
    main () : Int {
        0
    };
};
class A {
    a : Int <- 5 ;
    b : String ;
    c : C <- new C ;
};
class B {
    a : Bool <- true ;
    b : A <- new A;
    c : String <- "ABC" ;

};
class C {
    a : A <- new A;
    b : B;
    c : Int;
};