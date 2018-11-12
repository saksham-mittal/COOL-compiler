-- Testing for unary operations, string and int constants along with if-else statements, while statement and static dispatch

class Main inherits IO {
    i : Int;
    j : Int <- 5;
    k : Bool <- true;
    io : IO;
    a : A;
    b : B; 
    main() : Int {
        {
            io <- new IO;
            a <- new A;
            b <- new B;
            io@IO.out_string("Enter an integer value : ");
            i <- io@IO.in_int();
            i <- a@A.neg(i);
            io@IO.out_string("Its negation is : ");
            io@IO.out_int(i);
            io@IO.out_string("\nEnter another integer value : ");
            i <- io@IO.in_int();
            j <- b@B.oddEven(i);
            k <- not k;
            0;
        }
    };
};
class A inherits IO {
    a : Int;
    b : Bool;
    neg(a : Int) : Int {
        {
            a <- ~a;
            a;
        }
    };
};
class B inherits IO {
    a : Int;
    io : IO <- new IO;
    oddEven(a : Int) : Int {
        {
            while 1 < a loop a <- a - 2 pool;
            if a = 0 then io@IO.out_string("Even :)\n") else io@IO.out_string("Odd :(\n") fi;
            0;
        }
    };
};