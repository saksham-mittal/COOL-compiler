-- Inheritance graph formation errors
-- Cycle formation in the inheritance graph (handles detection of multiple cycles)

(* c

test1.cl:1: Class 'A' or it's ancestor is a part of inheritance cycle.
test1.cl:1: Class 'A1' or it's ancestor is a part of inheritance cycle.
test1.cl:1: Class 'X' or it's ancestor is a part of inheritance cycle.

*)

class Main {
    main() : Int {
        0
    };
};
class A inherits B {

};
class B inherits C {

};
class C inherits A {

};
class A1 inherits B1 {

};
class B1 inherits C1 {

};
class C1 inherits D1 {

};
class D1 inherits A1 {

};
class X inherits Y {

};
class Y inherits X {

};