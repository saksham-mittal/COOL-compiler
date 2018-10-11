-- Redefinition of formal

(*  The following error is generated

    test13.cl:14: Redefinition of formal 'x' is not allowed.

*)
class Main {
    main() : Int {
        0
    };
};
class A {
    foo( x : Int, x : String ) : Int { 
        0
    };
};