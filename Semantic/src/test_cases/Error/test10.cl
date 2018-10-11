-- Errors while adding methods and attributes
-- Redefinition of a method in a child class does not have same return type as parent

(*  The following error is generated
    test10.cl:19: In the redefined method 'foo' the return type is 'Bool' instead of return type 'Int'.
*)
class Main {
    main() : Int {
        0
    };
};
class B {
    a : Int;
    foo( x : Int ) : Int {
        0
    };
};
class A inherits B {
    foo( x : Int ) : Bool {
        true
    };
};