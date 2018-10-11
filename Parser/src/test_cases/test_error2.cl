(* This code violates the following rules : 

    1. expression : OBJECTID ASSIGN expression
    2. program : class_list EOF SEMICOLON

*)
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
                -- Assignment of value is done to an expression instead of an OBJECTID (error number 1)
         		sum + loopVar <- sum ;
          		loopVar <- loopVar + 1;
      		}
      		pool
    		);
        out_string("The required sum is ").out_int(sum).out_string("\n");
        }
        fi;
    }};
-- No SEMICOLON has been used after program end (EOF) (error number 2)
}                           