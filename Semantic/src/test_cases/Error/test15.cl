-- Undeclared variable while assignment

(*  The following error is generated

   test15.cl:10: Variable 'a' is undeclared during assignment.

*)
class Main {
    main() : Int { {
        a <- 5;
    }};
};