-- Errors while adding methods and attributes
-- Multiple definitions of a method in the same class

(*  The following error is generated
    test7.cl:16: Method 'foo' is defined more than once.
*)
class Main {
    main() : Int {
        0
    };
};
class A {
    foo() : Int {
        0
    };
    foo() : String {
        "a"
    };
};