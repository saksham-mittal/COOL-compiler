lexer grammar CoolLexer;

tokens{
	ERROR,
	TYPEID,
	OBJECTID,
	BOOL_CONST,
	INT_CONST,
	STR_CONST,
	LPAREN,
	RPAREN,
	COLON,
	ATSYM,
	SEMICOLON,
	COMMA,
	PLUS,
	MINUS,
	STAR,
	SLASH,
	TILDE,
	LT,
	EQUALS,
	LBRACE,
	RBRACE,
	DOT,
	DARROW,
	LE,
	ASSIGN,
	CLASS,
	ELSE,
	FI,
	IF,
	IN,
	INHERITS,
	LET,
	LOOP,
	POOL,
	THEN,
	WHILE,
	CASE,
	ESAC,
	OF,
	NEW,
	ISVOID,
	NOT
}

/*
  DO NOT EDIT CODE ABOVE THIS LINE
*/

@lexer::header {
	#include <Token.h>
	#include <string>
}
@lexer::postinclude {
	using namespace std;
	using namespace antlr4;
}
@members{

	/*
		YOU CAN ADD YOUR MEMBER VARIABLES AND METHODS HERE
	*/

	/**
	* Function to report errors.
	* Use this function whenever your lexer encounters any erroneous input
	* DO NOT EDIT THIS FUNCTION
	*/
	void reportError(string errorString){
		setText(errorString);
		setType(ERROR);
	}

	/*
		processString() processes a string for escaped characters
	*/
	void processString() {
		auto t = _factory->create(make_pair(this, _input), type, _text, channel, tokenStartCharIndex, getCharIndex()-1, tokenStartLine, tokenStartCharPositionInLine);
		string in = t->getText();

		string dummyBuffer = "";

		//write your code to test strings here
		for(int i=0; i<in.length(); i++) {
			if(in[i] == '\\') {
				if(in[i + 1] == 'n') {
					dummyBuffer.push_back('\n');
				} else if(in[i + 1] == 'f') {
					dummyBuffer.push_back('\f');
				} else if(in[i + 1] == 't') {
					dummyBuffer.push_back('\t');
				} else if(in[i + 1] == 'b') {
					dummyBuffer.push_back('\b');
				} else if(in[i + 1] == '\"') {
					dummyBuffer.push_back('\"');
				} else if(in[i + 1] == '\\') {
					dummyBuffer.push_back('\\');
				} else {
					dummyBuffer.push_back(in[i + 1]);
				}
				i++;
			} else {
				dummyBuffer.push_back(in[i]);
			}
		}

		/*
			Checks if the string is too long. If it is, it prints 'String constant too long'
		*/
		if(dummyBuffer.length() > 1024) {
			reportError("String constant too long");
			return;
		}

		setText(dummyBuffer);
		return;

	}

	/*
		unknownToken() reports an error if an unknown token is found
	*/
	void unknownToken() {
		auto t = _factory->create(make_pair(this, _input), type, _text, channel, tokenStartCharIndex, getCharIndex()-1, tokenStartLine, tokenStartCharPositionInLine);
		string in = t->getText();

		reportError(in);
	}

}

/*
	WRITE ALL LEXER RULES BELOW
*/

/*
	String constant calls processString() function
*/
STR_CONST				: '"' (ESC|.)*? '"' { processString(); };
fragment ESC 			: '\\"' | '\\\\';

BOOL_CONST				: 't'('r'|'R')('u'|'U')('e'|'E')
						| 'f'('a'|'A')('l'|'L')('s'|'S')('e'|'E');

SEMICOLON    			: ';';
DARROW       			: '=>';
INT_CONST				: [0-9]+;
SELF					: 'self';
SELF_TYPE				: 'SELF_TYPE';
LPAREN					: '(';
RPAREN					: ')';
COLON					: ':';
ATSYM					: '@';
COMMA					: ',';
PLUS					: '+';
MINUS					: '-';
STAR					: '*';
SLASH					: '/';
TILDE					: '~';
EQUALS					: '=';
LT						: '<';
LE						: '<=';
LBRACE					: '{';
RBRACE					: '}';
DOT						: '.';
ASSIGN					: '<-';

/*
	Keywords Lexer Rules
*/
CLASS					: ('c'|'C')('l'|'L')('a'|'A')('s'|'S')('s'|'S');
ELSE					: ('e'|'E')('l'|'L')('s'|'S')('e'|'E');
FI						: ('f'|'F')('i'|'I');
IF						: ('i'|'I')('f'|'F');
INHERITS				: ('i'|'I')('n'|'N')('h'|'H')('e'|'E')('r'|'R')('i'|'I')('t'|'T')('s'|'S');
IN						: ('i'|'I')('n'|'N');
LOOP					: ('l'|'L')('o'|'O')('o'|'O')('p'|'P');
LET						: ('l'|'L')('e'|'E')('t'|'T');
POOL					: ('p'|'P')('o'|'O')('o'|'O')('l'|'L');
THEN					: ('t'|'T')('h'|'H')('e'|'E')('n'|'N');
WHILE					: ('w'|'W')('h'|'H')('i'|'I')('l'|'L')('e'|'E');
CASE					: ('c'|'C')('a'|'A')('s'|'S')('e'|'E');
ESAC					: ('e'|'E')('s'|'S')('a'|'A')('c'|'C');
OF						: ('o'|'O')('f'|'F');
NOT						: ('n'|'N')('o'|'O')('t'|'T');
NEW						: ('n'|'N')('e'|'E')('w'|'W');
ISVOID					: ('i'|'I')('s'|'S')('v'|'V')('o'|'O')('i'|'I')('d'|'D');

SPACES					: [ \t\r\n\f]+ -> skip;

OBJECTID				: [a-z][_a-zA-Z0-9]+;
TYPEID					: [A-Z][_a-zA-Z0-9]*;

/*
	Comment Lexer Rules
*/
ONE_LINE_COMMENT			: '--' .*? '\n' -> skip;
END_MULTI_COMMENT			: '*)' EOF? { reportError("Unmatched *)"); };
BEGIN_NESTED_COMMENT1		: '(*' -> pushMode(IN_MULTI_COMMENT), skip;

mode IN_MULTI_COMMENT;
ERR							: .(EOF) { reportError("EOF in comment"); };
BEGIN_NESTED_COMMENT2		: '(*' -> pushMode(IN_IN_MULTI_COM), skip;
END_NESTED_COMMENT1			: '*)' -> popMode, skip;
IM_MULTI_COMMENT_T			: . -> skip;

mode IN_IN_MULTI_COM;
ERR2						: .(EOF) { reportError("EOF in comment"); };
BEGIN_NESTED_COMMENT3		: '(*' -> pushMode(IN_IN_MULTI_COM), skip;
ERR3						: '*)' EOF { reportError("EOF in comment"); };
END_NESTED_COMMENT2			: '*)' -> popMode, skip;
IM_NESTED_COMMENT_T			: . -> skip;

/*
	Lexer Rules for error function
*/
ERROR				 		: '"' (~[\u0000]* ('\\u0000'))+ ~["\nEOF]* ["\nEOF] { reportError("String has NULL character"); }
							| '"' ~[\n"]* (EOF) { reportError("EOF in string literal"); }
							| '"' ~["\nEOF]* ('\n') { reportError("Unterminated string constant"); }
							;

/*
	UNKNOWN if unknown token is found
*/
UNKNOWN						: . { unknownToken(); };
