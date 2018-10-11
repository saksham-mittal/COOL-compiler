-- Inheritance graph formation errors
-- Parent class should be defined

(* The following error is generated

test5.cl:17: Parent class 'B' not found.

*)

class Main {
    main() : Int {
        0
    };
};
class A inherits B {
    a : Int;
};