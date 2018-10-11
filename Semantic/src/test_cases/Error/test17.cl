-- Errors while dispatching a method

(* The following errors are generated 

    test17.cl:13: Different number of arguments present in the method 'foo' as compared to the defined method.
    test17.cl:14: Method 'func' is undefined.
    test17.cl:14: The type 'String' of identifier 'b' does not match with the type 'Object' of the expression.

*)
class Main {

    main() : Object {
        let a : Int in 
        let b : String in {
            a <- foo(a,b);
            b <- func();
        } 
    };
    foo( x : Int ) : Int {
        0
    };
};
