-- Errors in Conditional operations

(* The following error is generated 
    
    test23.cl:14: The argument in less-than is of type non-Int 'String'
    test23.cl:15: The argument in less-than equal to is of type non-Int 'String'
    test23.cl:16: The argument in equals have different types 'Int' and 'Bool'.

*)
class Main {
    main() : Object {
        let a : Int <- 5 in
        let b : String <- "abc" in
        let c : Bool <- true in {
            a < b;
            b <= c;
            a = c;
        }
    };
};