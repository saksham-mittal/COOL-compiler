-- Defined type of an attribute is different than that of the expression assigned to it

(*  The following error is generated

    test12.cl:11: The type 'Bool' of identifier 'a' does not match with the type 'Int' of the expression.

*)
class Main {
    a : Bool;
    main() : Int { {
        a <- 5+3;
    } };
};