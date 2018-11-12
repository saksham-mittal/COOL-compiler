-- Testing for arithmetic operations along with static dispatch and new expressions

class Main inherits IO {
    i : Int;
    j : Int;
    k : Int;
    a : A <- new A;
    b : IO <- new IO;
    main() : Int {
        {
            b@IO.out_string("Enter a number : ");
            i <- b@IO.in_int();
            b@IO.out_string("\nEnter second number : ");
            j <- b@IO.in_int();
            k <- a@A.add(i, j);
            k <- a@A.sub(i, j);
            k <- a@A.mul(i, j);
            k <- a@A.div(i, j);
            0;
        }
    } ;
};
class A inherits IO{
    k : Int;
    a : IO <- new IO;
    add(i : Int, j : Int) : Int {
        {
            k <- i + j;
            a@IO.out_string("\nAddition is : ");
            a@IO.out_int(k);
            0;
        }
    };
    sub(i : Int, j : Int) : Int {
        {
            k <- i - j;
            a@IO.out_string("\nSubtraction is : ");
            a@IO.out_int(k);
            0;
        }
    };
    mul(i : Int, j : Int) : Int {
        {
            k <- i * j;
            a@IO.out_string("\nMultiplication is : ");
            a@IO.out_int(k);
            0;
        }
    };
    div(i : Int, j : Int) : Int {
        {
            k <- i / j;
            a@IO.out_string("\nDivision is : ");
            a@IO.out_int(k);
            0;
        }
    };
};