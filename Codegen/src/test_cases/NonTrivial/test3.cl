-- Testing for nested if-else and nested while and string functions

class Main inherits IO {
    a : Int;
    b : Int;
    c : Int;
    d : String;
    io : IO <- new IO;
    e : A;
    main() : Int {
        {
            io@IO.out_string("Enter an integer value : ");
            a <- io@IO.in_int();
            b <- 0;
            c <- 0;
            d <- "JeBaited\n";
            io@IO.out_string("Long we have waited, now we are\n");
            while c < a
            loop {
                b <- 0;
                while b < 3
                loop {
                    io@IO.out_string(d);
                    b <- b + 1;
                }
                pool;
                c <- c + 1;
            }
            pool;
            e <- new A;
            a <- e@A.testString();
            0;
        }
    };
};
class A inherits IO {
    a : String;
    b : String;
    c : String <- new String;
    io : IO <- new IO;
    testString() : Int {
        {
            io@IO.out_string("\nEnter a string : ");
            a <- io@IO.in_string();
            io@IO.out_string("\nEnter another string : ");
            b <- io@IO.in_string();
            if a@String.length() < b@String.length()
            then {
                if(5 < a@String.length()) then io@IO.out_string("LULW\n") else io@IO.out_string("LUL\n") fi;
            } else {
                if(5 < a@String.length()) then io@IO.out_string("haHAA\n") else io@IO.out_string("HAhaa\n") fi;
            }
            fi;
            0;
        }
    };
};