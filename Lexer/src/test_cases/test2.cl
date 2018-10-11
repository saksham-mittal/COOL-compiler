class Main inherits IO {
    main(): Object {{
        out_string("Enter number upto which you want to find sum \n");
    	let a: Int <- in_int() in
        let loopVar: Int <- 1 in
        let sum: Int <- 0 in

        if a < 0 then
        	out_string("Number should be greater than 0\n")
        else {
      		(while loopVar <= a loop
      		{
         		sum <- sum + loopVar;
          		loopVar <- loopVar + 1;
      		}
      		pool
    		);
        out_string("The required sum is ").out_int(sum).out_string("\n");
        }
        fi;
    }};
};
