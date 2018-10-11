-- Errors while adding methods and attributes
-- Redefinition of a method in a child class does not have same Number of formals as parent

(*  The following error is generated
    test9.cl:19: Different number of formal paramters in redefined method 'foo'.
*)
class Main {
    main() : Int {
        0
    };
};
class B {
    a : Int;
    foo( x : Int , y : String ) : Int {
        0
    };
};
class A inherits B {
    foo( x : Int ) : Int {
        0
    };
};