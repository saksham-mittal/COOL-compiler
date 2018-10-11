(* This code violates the following rules :
    1. expression : LET OBJECTID COLON TYPEID ASSIGN expression expression_list IN expression
       TYPEID COLON OBJECTID is written instead of OBJECTID COLON TYPEID
    2. expression : IF expression THEN expression ELSE expression FI
*)
class Main inherits IO {
  main(): Object {{
      out_string("Enter the string to verify whether it is a palindrome or not \n");

      -- Error in the syntax of let statement (error number 1)
      let String : a <- in_string() in              
      if(palindrome(a)) then
      	out_string("The given string is a palindrome\n")
      else
      	out_string("The given string is not a palindrome\n")
      fi;

   }};

   palindrome(s : String) : Bool {
    	 if s.length() = 0 then
       		true
       else if s.length() = 1 then
       		true
       else if s.substr(0, 1) = s.substr(s.length() - 1, 1) then
       		palindrome(s.substr(1, s.length() - 2))
        -- No 'else' keyword is used to finish off 'if (error number 2)
       fi fi fi                                       
    };

};
