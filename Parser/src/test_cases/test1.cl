(* The rules tested by this file are :

    1. program : class_list EOF
    2. class_list : (class_ SEMICOLON)*
    3. class_ :  CLASS TYPEID LBRACE feature_list RBRACE
	4. class_ :  CLASS TYPEID INHERITS TYPEID LBRACE feature_list RBRACE;
    5. feature : OBJECTID (formal formal_list) COLON TYPEID LBRACE expression RBRACE
    6. feature : OBJECTID COLON TYPEID
    7. feature : OBJECTID COLON TYPEID ASSIGN expression
    8. expression : OBJECTID ASSIGN expression
    9. expression : expression PLUS expression
	10. expression : expression MINUS expression
	11. expression : expression STAR expression
	12. expression : expression LT expression
    13. expression : expression EQUALS expression
    14. expression : IF expression THEN expression ELSE expression FI
    15. expression : NEW TYPEID
    16. expression : OBJECTID (expression, expression_list)
    17. expression : LET OBJECTID COLON TYPEID ASSIGN expression expression_list IN expression
    18. expression : expression DOT OBJECTID
*)
class ComplexNos {
  	a: Int;
  	b: Int;
    x: Int;
    y: Int;

    init(i: Int, j: Int, m: Int, n: Int) : ComplexNos {
        {
            a <- i;
            b <- j;
            x <- m;
            y <- n;
            self;
        }
	};

    realSum(): Int {
		a + x
    };

    imaginarySum(): Int {
		b + y
    };

    realDiff(): Int {
      	a - x
    };

    imaginaryDiff(): Int {
      	b - y
    };

    realMultiply(): Int {
        if 0 < b*y then
        	a * x - b * y
        else
        	a * x + b * y
        fi
    };

    imaginaryMultiply(): Int {
		a * y + b * x
    };
};

class Main inherits IO {
  myComplex: ComplexNos;
  main(): Object {{
      out_string("Enter two complex nos.\n");
      let a: Int <- in_int() in
      let b: Int <- in_int() in

      let x: Int <- in_int() in
      let y: Int <- in_int() in

      myComplex <- new ComplexNos.init(a, b, x, y);

      out_string("Enter 1 for addition of the complex nos. \n");
      out_string("Enter 2 for subtraction of the complex nos. \n");
      out_string("Enter 3 for multiplication of the complex nos. \n");

      let choice: Int <- in_int() in
      if 3 < choice then
      		out_string("Wrong choice\n")
      else
      {
          if choice = 1 then {
          	  out_int(myComplex.realSum()).out_string(" + ");
          	  out_int(myComplex.imaginarySum()).out_string("i\n");
          } else if choice = 2 then {
              out_int(myComplex.realDiff()).out_string(" + ");
          	  out_int(myComplex.imaginaryDiff()).out_string("i\n");
          } else if choice = 3 then {
              out_int(myComplex.realMultiply()).out_string(" + ");
          	  out_int(myComplex.imaginaryMultiply()).out_string("i\n");
          } else {
              out_string("Wring choice!\n");
          } fi fi fi;
      } fi;
  }};

};
