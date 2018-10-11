-- Inheritance graph formation errors
-- Classes cannot inherit from String, Int, and Bool class

(* The following error is generated

test4.cl:17: Class 'A' cannot inherit class 'Int'.

*)

class Main {
    main() : Int {
        0
    };
};
class A inherits Int {
    a : Int;
};