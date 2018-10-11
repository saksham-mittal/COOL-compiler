-- Undeclared identifier
 
(* The following error is generated 
    
    test24.cl:11: The identifier 'b' is undeclared.
    test24.cl:11: The argument in addition is of type non-Int 'Object'

*)
class Main {
    main() : Object {
        let a : Int <- 5 in {
            a <- a + b;
        }
    };
};