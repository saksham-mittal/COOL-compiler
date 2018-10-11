-- Errors While creating a new object of a class

(* The following error is generated 

    test21.cl:23: The class 'D' with 'new' is undefined.
    test21.cl:23: The defined type 'A' of Attribute 'c' does not conform to the Attribute value type 'Object'.

*)
class Main {
    main() : Int {
        0
    };
};
class A {

};
class B {

};
class C {
    a : A <- new A;
    b : B <- new B;
    c : A <- new D;
};