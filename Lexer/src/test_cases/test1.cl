class Main inherits IO {
  main(): Object {{
    out_string("Enter the three sides of cuboid \n");

    let num3: Int <- in_int() in
    let num1: Int <- in_int() in
	  let num2: Int <- in_int() in

      if num1 < 0 then
        out_string("ERROR: Side must be greater-than or equal-to 0\n")
      else if num2 < 0 then
        out_string("ERROR: Side must be greater-than or equal-to 0\n")
	    else if num3 < 0 then
        out_string("ERROR: Side must be greater-than or equal-to 0\n")
      else {
        out_string("The volume of cuboid of sides ").out_int(num3);
        out_string(" ").out_int(num2).out_string(" ");
        out_int(num1).out_string(" is ").out_int(volume(num1, num2, num3));
        out_string("\n");
      }
      fi fi fi;
  }};

  volume(num1: Int, num2: Int, num3:Int): Int {
    num1 * num2 * num3
  };
};
