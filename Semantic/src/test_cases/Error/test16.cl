-- Type of expression doesn't match during assignment

(*  The following error is generated

   test16.cl:11: The type 'Bool' of identifier 'a' does not match with the type 'Int' of the expression.

*)
class Main {
    main() : Int {
        let a : Bool in {
            a <- 5 + 3;
        }
    };
};