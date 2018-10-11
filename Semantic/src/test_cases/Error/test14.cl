-- Return type of method does not conform to body return type

(*  The following error is generated

   test14.cl:15: The defined return type 'Int' of method 'foo' does not match to the method body return type 'Bool'.
   
*)
class Main {
    main() : Int {
        0
    };
};
class A {
    foo( x : Int) : Int { 
        true
    };
};