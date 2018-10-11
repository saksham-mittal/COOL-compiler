-- Errors while adding methods and attributes
-- Redefinition of an attribute in some class with the attribute already defined in its parent class

(*  The following error is generated
    test8.cl:16: Attribute 'a' is also an attribute of its parent class.
*)
class Main {
    main() : Int {
        0
    };
};
class B {
    a : Int;
};
class A inherits B {
    a : Int;
};