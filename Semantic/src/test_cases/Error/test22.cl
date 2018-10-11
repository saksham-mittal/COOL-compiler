-- Errors in Arithmetic operations

(* The following error is generated 

    test22.cl:17: The argument in addition is of type non-Int 'String'
    test22.cl:18: The argument in multiplication is of type non-Int 'Bool'
    test22.cl:19: The argument in subtraction is of type non-Int 'Bool'
    test22.cl:19: The argument in subtraction is of type non-Int 'String'
    test22.cl:20: The argument in division is of type non-Int 'String'
    test22.cl:20: The argument in division is of type non-Int 'Bool'

*)class Main {
    main() : Object {
        let a : Int <- 5 in
        let b : String <- "abc" in
        let c : Bool <- true in {
            a <- a + b;
            a <- a * c;
            a <- c - b;
            a <- b / c;
        }
    };
};
