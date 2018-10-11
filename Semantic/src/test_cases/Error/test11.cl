-- Errors while adding methods and attributes
-- Redefinition of a method in a child class does not have same type of formals as parent

(*  The following error is generated
    test11.cl:19: In redefined method 'foo' the paramter typeid 'String' does not match with the parent typeid 'Int'.
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
    foo( x : String ) : Int {
        0
    };
};