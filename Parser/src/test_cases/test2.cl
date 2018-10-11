(* The rules tested by this file are :

    1. expression : WHILE expression LOOP expression POOL
    2. expression : LBRACE expression_list RBRACE

*)

class Main inherits IO {
  main(): Object {{
    out_string("Enter number upto which you want to find fibonacci numbers \n");

    let a: Int <- in_int() in

      if a < 1 then
        out_string("ERROR: Number must be greater-than or equal-to 1\n")
      else {
        out_string("The fibonacci series upto ").out_int(a);
        out_string(" is \n");
        let loopVar : Int <- 1 in
        let num1 : Int <- 0 in
        let num2 : Int <- 1 in
        let dummy : Int in
        
        (while loopVar <= a loop
          {
              dummy <- num1;
              num1 <- num2;
              num2 <- num2 + dummy;
              out_int(num2);
              out_string(" ");
              loopVar <- loopVar + 1;
          }
          pool
        );

        out_string("\n");
      }
      fi;
  }};

};