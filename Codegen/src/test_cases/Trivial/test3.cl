-- Trivial testcase 3

class Main {
    main () : Int {
        0
    };
};
class A {
    a : Int;
    b : String;
    foo() : Int {
        {
            {
                a <- a + 5;
                b <- "String";
            };
            {
                b <- "ABC";
                a <- a + 10;
            };
            0;
        }  
    };
};