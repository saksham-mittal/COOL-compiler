-- Inheritance graph formation errors
-- Redefinition of default classes not allowed

(* The following error is generated

test3.cl:15: Class 'IO' has already been defined earlier.

*)

class Main {
    main() : Int {
        0
    };
};
class IO inherits B {
    a : Int;
};
class B {

};