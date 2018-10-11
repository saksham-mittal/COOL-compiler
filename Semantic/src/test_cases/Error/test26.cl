-- main method not defined
 
(* The following error is generated 
    
    test26.cl:1: 'main' method is not present in the Main class

*)
class Main {
    foo() : Int {
        0
    };
};
class A {
    a : Int;
};
class B {
    b : String;
};
class C {
    c : Bool;
};