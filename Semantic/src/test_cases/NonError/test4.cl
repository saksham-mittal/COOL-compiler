-- Testing rules of method definitions ( Same function in inherited classes )

class Main {
    main () : Int {
        0
    };
};
class A {
    foo( x : Int , y : String ) : Int {
        0
    };
};
class B inherits A {
    foo( a : Int , b : String ) : Int {
        1
    };
};
class C inherits B {
    foo( d : Int , e : String ) : Int {
        2
    };
};