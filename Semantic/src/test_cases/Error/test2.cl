-- Inheritance graph formation errors
-- Redefinition of class not allowed

(* The following error is generated

test2.cl:20: Class 'A' has already been defined earlier.
test2.cl:17: Parent class 'B' not found.

*)

class Main {
    main() : Int {
        0
    };
};
class A inherits B {

};
class A {

};