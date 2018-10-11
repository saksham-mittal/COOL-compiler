-- Errors while adding methods and attributes
-- Multiple definitions of an attribute in the same class

(*  The following error is generated
    test6.cl:14: Attribute 'a' is defined more than once.
*)
class Main {
    main() : Int {
        0
    };
};
class A {
    a : Int;
    a : String;
};