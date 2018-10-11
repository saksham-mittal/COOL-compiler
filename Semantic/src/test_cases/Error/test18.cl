-- Condition of a loop is not of type Bool

(* The following error is generated 

    test18.cl:11: Loop condition's return type is not 'Bool'

*)
class Main {
    main() : Object {
        let a : Int <- 0 in {
            while ("ABC") loop
                a <- 5
            pool;
        }
    };
};