-- testing rules for if-else

class Main {
    main () : Int {
        0
    };
};
class A {
    a : Int;
    b : Bool;
    foo() : Int {
        {
            if a = 4 then a = 1 else a = 0 fi ;
            if b then a = 1 else a = 0 fi ;
            0;
        }
    };
};