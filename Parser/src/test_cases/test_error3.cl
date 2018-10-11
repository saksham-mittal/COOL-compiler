(* This code violates the following rules :

    1. feature : OBJECTID (formal formal_list) COLON TYPEID LBRACE expression RBRACE

*)

class Main inherits IO {
  main(): Object {{
    out_string("Enter an integer greater-than or equal-to 0: ");

    let input: Int <- in_int() in
      if input < 0 then
        out_string("ERROR: Number must be greater-than or equal-to 0\n")
      else {
        out_string("The factorial of ").out_int(input);
        out_string(" is ").out_int(factorial(input));
        out_string("\n");
      }
      fi;
  }};


  -- No return type has been assigned to this method (error no. 1)
  factorial(num: Int) {
    if num = 0 then 1 else num * factorial(num - 1) fi
  };
  
};
