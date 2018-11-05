-- Program to demonstrate the capabilities of complement, negation, addition
-- subtraction, multiplication, division, equality and a simple if-then-else
-- with attribute initialization and IO methods

class Main {
	i : Int;
    k : Bool <- true;
    j : String <- "cs3423";
	l : IO <- new IO;

	main() : Object {
        {
            l@IO.out_string("Enter int : ");
            i <- l@IO.in_int();
            i <- i * 8 + 9 * 7;
            i <- ~i;
			i <- i/3 - 4;
			k <- not k;
			k <- not k;
			k <- "cs3423" = j;
			if k 
				then l@IO.out_string("CS3423")
				else l@IO.out_string("The computer has some serious problems")
			fi;
			
			k <- 3 < 2;
			if k
				then l@IO.out_string("The computer has some really serious problem")
				else l@IO.out_string("Good boy")
			fi;

			k <- 5 <= 5;
			if k
				then l@IO.out_string("That was a trick question")
				else l@IO.out_string("Oh oh")
			fi;
		}
	};
};
