-- testing rules for unary oparations 

class Main {
    main () : Int {
        0
    };
};
class A {
    a : Int;
    b : Bool;
    foo() : Int { {
        b = ~ b;
        a = not a;
        a;
    } };
};