-- The type of let expression does not conform to declared type

(* The following error is generated 

    test19.cl:16: The identifier x declared type 'A' does not match with let value type 'B.

*)
class Main {
    main() : Int {
        0
    };
};
class A {
    a : B <- new B; 
    foo() : Int {
        let x : A <- a in 
        {
            0;
        }
    };
};
class B {

};