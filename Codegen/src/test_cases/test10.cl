-- Program to demonstrate multi class programs with static dispatch and new.

class Main {
	a : XYZ <- new XYZ;
	b : IO <- new IO;
	c : Int <- new Int;
	main () : Object {
		{
			b@IO.out_string("Static Dispatch 1");
			b@IO.out_string(a@XYZ.do_that("a", a));
		}
	};
};

class XYZ inherits IO {
	a : Int <- 4 + 5;
	b : Bool;
	c : String <- "CS3423";
	d : IO <- new IO;
	do_this(i : Int, j: String, k: Bool) : String {
		{
			i <- i / 8;
			d@IO.out_string("Enter something:\n");
			c <- d@IO.in_string();
            d@IO.out_string(c);
			j;
		}
	};
	
	do_that(a1: String, b1: XYZ) : String {
		{
			a1 <- b1@XYZ.do_this(1, a1, false);
			a1 <- b1@XYZ.do_this(2, a1, true);
		}
	};
};
