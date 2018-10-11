parser grammar CoolParser;

options {
	tokenVocab = CoolLexer;
}

@header{
	import java.util.List;
}

@members{
	String filename;
	public void setFilename(String f){
		filename = f;
	}

/*
	DO NOT EDIT THE FILE ABOVE THIS LINE
	Add member functions, variables below.
*/

}

/*
	Add appropriate actions to grammar rules for building AST below.
*/

// This is the root production for the program
program returns [AST.program value]	: 
	cl=class_list EOF 
	{
		$value = new AST.program($cl.cl_value, $cl.cl_value.get(0).lineNo);
	}
;					

// class_list : [[ class ]]*
// This is the list of all classes
class_list returns [ArrayList<AST.class_> cl_value]
	@init {
		$cl_value = new ArrayList<AST.class_>();
	}
	: ( c = class_ SEMICOLON {$cl_value.add($c.c_value);})*
	;

// class TYPE { [[ feature ]]* }
// Single class 
class_  returns [AST.class_ c_value]:
	// Without class inheritence
	cl = CLASS t = TYPEID LBRACE ftre_list = feature_list RBRACE
	{
		$c_value = new AST.class_($t.getText() , filename , "Object" , $ftre_list.ftre_list_value , $cl.getLine());

	}
	// When there is class inheritence
	| cl = CLASS t = TYPEID INHERITS inhrt_type=TYPEID LBRACE ftre_list = feature_list RBRACE
	{
		$c_value = new AST.class_($t.getText() , filename , $inhrt_type.getText() , $ftre_list.ftre_list_value , $cl.getLine());
	}
	;

// feature_list : [[ feature; ]]*
// Returns list of all features
feature_list returns [ArrayList<AST.feature ftre_list_value>]
	@init {
		$ftre_list_value = new ArrayList<AST.feature>();
	}
	: ( c = feature SEMICOLON {$ftre_list_value.add($c.ftre_value);})*
	;

// Consists of function(method) or declaration
feature returns [AST.feature ftre_value]:
	fn = method 
	{
		$ftre_value = $fn.mthd_value ;
	}
	| var = attr
	{
		$ftre_value = $var.attr_value;
	} 
	;

// Variable declaration 
attr returns [AST.attr attr_value] :
	// Without assignment
	id = OBJECTID COLON t = TYPEID
	{
		$attr_value = new AST.attr($id.getText() , $t.getText() , new AST.no_expr($id.getLine()) , $id.getLine());
	}
	// With assignment
	| id = OBJECTID COLON t =TYPEID ASSIGN expr = expression
	{
		$attr_value = new AST.attr($id.getText() , $t.getText() , $expr.expr_value , $id.getLine());
	}
	;

// Function declaration
method returns [AST.method mthd_value] :
	// Without formal list
	id = OBJECTID LPAREN RPAREN COLON t = TYPEID LBRACE expr = expression RBRACE
	{
		$mthd_value = new AST.method($id.getText() , new ArrayList<AST.formal>() , $t.getText() , $expr.expr_value , $id.getLine());
	}
	// With formal list
	| id = OBJECTID LPAREN frml_list = formal_list RPAREN COLON t = TYPEID LBRACE expr = expression RBRACE
	{
		$mthd_value = new AST.method($id.getText() , $frml_list.frml_list_value , $t.getText() , $expr.expr_value , $id.getLine() );
	}
	;

// formal_list : [ formal [[ ,formal ]]* ]
// List of formals
formal_list returns [ArrayList<AST.formal> frml_list_value]
	@init {
		$frml_list_value = new ArrayList<AST.formal>();			
	}
	:
	x = formal {$frml_list_value.add($x.frml_value);}
	( COMMA y = formal {$frml_list_value.add($y.frml_value);} )*
	;

// Variable declaration
// Eg : input : Int
formal returns [AST.formal frml_value]:
	id = OBJECTID COLON t = TYPEID
	{
		$frml_value = new AST.formal($id.getText() , $t.getText() , $id.getLine());
	}
	;

// expression_list : [ expr [[, expr]]* ]
// Comma seperated list of seperations
expression_list returns [ArrayList<AST.expression> expr_list_value]
	@init{
		$expr_list_value = new ArrayList<AST.expression>();
	}
	:
		(expr = expression {$expr_list_value.add($expr.expr_value);} (COMMA expr = expression {$expr_list_value.add($expr.expr_value);})* )?
	;

// block_expression_list : { [[ expression; ]]+ }
// Expression list seperated by a semicolon inclosed in '{' '}'
block_expression_list returns [ArrayList<AST.expression> bl_expr_list_value]
	@init
	{
		$bl_expr_list_value = new ArrayList<AST.expression>();
	}
	:
		(expr = expression SEMICOLON {$bl_expr_list_value.add($expr.expr_value);})+
	;

// List of branches seperated with semicolon used in 'CASE' construct
branch_list returns [ArrayList<AST.branch> brlist_value]
	@init 
	{
		$brlist_value = new ArrayList<AST.branch>();
	}
	:	(b = branch SEMICOLON {$brlist_value.add($b.br_value);})+
	;

// Branch in type 'CASE'
branch returns [AST.branch br_value] :
	id = OBJECTID COLON t = TYPEID DARROW expr = expression SEMICOLON
	{
		$br_value = new AST.branch($id.getText() , $t.getText() , $expr.expr_value , $id.getLine());
	}
	;

// List of all 'LET' assignments
let_list returns [ArrayList<AST.attr> ll_value]
	@init{
		$ll_value = new ArrayList<AST.attr>();
	}
	:
	// variable declaration which doesnt start with COMMA
	at_un = attr { $ll_value.add($at_un.attr_value);}
	// Subsequent declarations starting with COMMA
	( COMMA at_deux = attr {$ll_value.add($at_deux.attr_value);})*
	;

// Different types of expressions
expression returns [AST.expression expr_value] :
	// expr.ID([expr [[, expr]]* ])
	// Calling a function (dispatch)
	expr = expression DOT id = OBJECTID LPAREN expr_list = expression_list RPAREN
	{
		$expr_value = new AST.dispatch($expr.expr_value , $id.getText() , $expr_list.expr_list_value , $expr.expr_value.lineNo);
	}
	//  expr[@TYPE].ID([expr [[, expr]]* ])
	//  A way of accessing methods of parent classes that have been hidden by
	//	redefinitions in child classes (static dispatch)
	| expr = expression ATSYM t = TYPEID DOT id = OBJECTID LPAREN expr_list = expression_list RPAREN
	{
		$expr_value = new AST.static_dispatch($expr.expr_value , $t.getText() , $id.getText() , $expr_list.expr_list_value , $expr.expr_value.lineNo);
	}
	// function call of return type SELF_TYPE
	| id = OBJECTID LPAREN expr_list = expression_list RPAREN
	{
		$expr_value = new AST.dispatch(new AST.object("self" , $id.getLine()) , $id.getText() , $expr_list.expr_list_value , $id.getLine());
	}
	// integer compliment of <expr>
	| stmt = TILDE exp1 = expression
	{
		$expr_value = new AST.comp($exp1.expr_value , $stmt.getLine());
	} 
	// checks whether <expr> is void or not : true if void and false otherwise
	| stmt = ISVOID exp1 = expression
	{
		$expr_value = new AST.isvoid($exp1.expr_value , $stmt.getLine());
	} 
	// multiplication : exp1 * exp2
	| exp1 = expression STAR exp2 = expression
	{
		$expr_value = new AST.mul($exp1.expr_value , $exp2.expr_value , $exp1.expr_value.lineNo);
	}
	// division : exp1 / exp2
	| exp1 = expression SLASH exp2 = expression
	{
		$expr_value = new AST.divide($exp1.expr_value , $exp2.expr_value , $exp1.expr_value.lineNo);
	}
	// addition : exp1 + exp2
	| exp1 = expression PLUS exp2 = expression
	{
		$expr_value = new AST.plus($exp1.expr_value , $exp2.expr_value , $exp1.expr_value.lineNo);
	}
	// subtraction : exp1 - exp2
	| exp1 = expression MINUS exp2 = expression
	{
		$expr_value = new AST.sub($exp1.expr_value , $exp2.expr_value , $exp1.expr_value.lineNo);
	}
	// less than : exp1 < exp2 (used for comparison)
	| exp1 = expression LT exp2 = expression
	{
		$expr_value = new AST.lt($exp1.expr_value , $exp2.expr_value , $exp1.expr_value.lineNo);
	}
	// less than equals to : exp1 <= exp2 (used for comparison)
	| exp1 = expression LE exp2 = expression
	{
		$expr_value = new AST.leq($exp1.expr_value , $exp2.expr_value , $exp1.expr_value.lineNo);
	}
	// equals to : exp1 = exp2 (used for comparison)
	| exp1 = expression EQUALS exp2 = expression
	{
		$expr_value = new AST.eq($exp1.expr_value , $exp2.expr_value , $exp1.expr_value.lineNo);
	}
	// neagtion of an expression 
	| stmt = NOT exp1 = expression
	{
		$expr_value = new AST.neg($exp1.expr_value , $stmt.getLine());
	} 
	// Assgning the value : <id> <- <expr> 
	| id = OBJECTID ASSIGN expr = expression
	{
		$expr_value = new AST.assign($id.getText() , $expr.expr_value , $id.getLine());
	}
	// if-else statements
	// if <expr> then <expr> else <expr> fi
	// if predicate is true 'then' branch is evaluated else 'else' branch is evaluated
	| stmt = IF prdct = expression THEN if_body = expression ELSE else_body = expression FI
	{
		$expr_value = new AST.cond($prdct.expr_value , $if_body.expr_value , $else_body.expr_value , $stmt.getLine());
	}
	// while loop
	// while <expr> loop <expr> pool
	// loop terminates when predicate is false
	| stmt = WHILE prdct = expression LOOP while_body = expression POOL
	{
		$expr_value = new AST.loop($prdct.expr_value , $while_body.expr_value , $stmt.getLine());
	}
	// {[[expr ;]]+}
	| stmt = LBRACE exp = block_expression_list RBRACE
	{
		$expr_value = new AST.block($exp.bl_expr_list_value, $stmt.getLine());
	}
	// let ID : TYPE [[, ID : TYPE [<- expression]]]* in expression
	// initialization expressions are optional
	| stmt = LET ll= let_list IN exp1 = expression
	{
		$expr_value = $exp1.expr_value;
		AST.attr temp_attr;
		for(int j = $ll.ll_value.size() - 1; j>=0; j--)
		{
			temp_attr = $ll.ll_value.get(j);
			$expr_value = new AST.let(temp_attr.name , temp_attr.typeid , temp_attr.value , $expr_value , $stmt.getLine());
		}
	}
	// case expression of [[ID : TYPE => expression;]]+ esac
	| stmt = CASE prdct = expression OF branch1 = branch_list ESAC
	{
		$expr_value = new AST.typcase($prdct.expr_value , $branch1.brlist_value , $stmt.getLine());
	}
	// declares the fresh object of appropriate class
	| stmt = NEW t = TYPEID
	{
		$expr_value = new AST.new_($t.getText() , $stmt.getLine());
	}
	// bounded expression
	| stmt = LPAREN exp1 = expression RPAREN
	{
		$expr_value = $exp1.expr_value;
	}
	// object of a class 
	| id = OBJECTID
	{
		$expr_value = new AST.object($id.getText() , $id.getLine());
	}
	// integer constant
	| i = INT_CONST
	{
		$expr_value = new AST.int_const( Integer.parseInt($i.getText()) , $i.getLine());
	}
	// string constant
	| str = STR_CONST
	{
		$expr_value = new AST.string_const($str.getText() , $str.getLine());
	}
	// boolean constant
	| bc = BOOL_CONST
	{
		$expr_value = new AST.bool_const($bc.getText().charAt(0) == 't' , $bc.getLine());
	}
	;

/*
Part of Phase - A

expr	: expr (ATSYM TYPEID)? DOT OBJECTID LPAREN (expr (COMMA expr)*)? RPAREN
		| OBJECTID LPAREN (expr (COMMA expr)*)? RPAREN
		| IF expr THEN expr ELSE expr FI
		| WHILE expr LOOP expr POOL
		| LBRACE (expr SEMICOLON)* RBRACE
		| LET OBJECTID COLON TYPEID ( ASSIGN expr )? ( COMMA OBJECTID COLON TYPEID ( ASSIGN expr )?)* IN expr
		| CASE expr OF (OBJECTID COLON TYPEID DARROW expr SEMICOLON)+ ESAC
		| NEW TYPEID
		| TILDE expr
		| ISVOID expr
		| expr STAR expr
		| expr SLASH expr
		| expr PLUS expr
		| expr MINUS expr
		| expr LT expr
		| expr LE expr
		| expr EQUALS expr
		| NOT expr
		| <assoc=right>OBJECTID ASSIGN expr
		| LPAREN expr RPAREN
		| OBJECTID
		| INT_CONST
		| STR_CONST
		| BOOL_CONST
		;
*/