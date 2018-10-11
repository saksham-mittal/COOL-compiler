(* The rules tested by this file are :

    1. formal_list : (COMMA formal)*
    2. formal : OBJECTID COLON TYPEID
*)
class Temp {

    fn1( i : Int ) : IO{
        new IO.out_int(i)
    };

    fn2( j : Int , k: String ) : IO{
       new IO.out_int(j)
    };

    fn3( k : Int , l : String , m : Bool) : IO{
        new IO.out_int(k)
    };
};
