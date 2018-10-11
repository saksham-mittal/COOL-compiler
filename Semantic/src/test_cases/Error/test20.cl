-- Errors in case expression

(* The following error is generated 

    test20.cl:20: Case branch has undefined type 'B'.
    test20.cl:21: Another branch has same type 'A'.

*)
class Main {
    main() : Int {
        0
    };
};
class A {
    a : Int;
    c : String;
    d : String;
    foo() : Object { {
        case c of 
                a : A => a <- 1; 
                b : B => b <- 1; 
                d : A => d <- "abc";
            esac;
    } };
};