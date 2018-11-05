package cool;

import java.util.*;
import java.io.PrintWriter;

public class Codegen{

	// Making object of printUtility class
	public PrintUtility printUtil;

	public GenerateLlvm gl;
	
	// The class for storing class information (its attributes and methods)
	public static ClassInfoCG clsInfoCG;

	// This Hashmap maps each string constant to the line number it comes on
	// This helps for giving @.str numbers to the string constants 
	public static HashMap<String, Integer> stringToLineNoMapping;

	public static Integer stringLineNo;

	public Codegen(AST.program program, PrintWriter out){
		//Write Code generator code here
		out.println("; I am a comment in LLVM-IR. Feel free to remove me.");
		
		String filename = AST.program.classes.get(0).filename;
		out.println("source_filename = \"" + filename + "\"\n");
	
		/* 
		Layout as specified by clang++ :
		layout ::= bigendian | littleendian | ptr sz align0 align1 | int sz align0 align1
		| float sz align0 align1 | aggr sz align0 align1 | stack sz align0 align1
		*/
		out.println("target datalayout = \"e-m:e-i64:64-f80:128-n8:16:32:64-S128\"\n");
		// This specifies target host
		out.println("target triple = \"x86_64-pc-linux-gnu\"\n");
	
		printUtil = new PrintUtility();

		// Instantiating stringToLineMapping hashmap 
		stringToLineNoMapping = new HashMap<String, Integer>();

		stringLineNo = 0;

		// Instanstating GenerateLlvm object
		gl = new GenerateLlvm();

		// This prints the declaration of standard methods of String, Int, Bool, IO and Object classes 
		gl.declareStandardCFunctions(printUtil, out);

		// The constructor adds the default classes and their methods to the AST.program
		clsInfoCG = new ClassInfoCG(program);

		// This method iterates over all the classes of the AST.program classes list and stores their attributes and methods
		clsInfoCG.insertClasses(program);

		// This method generates llvm ir by iterating over all classes
		// For more info, see the GenerateLlvm.java class
		List<String> functionFormalNameList = new ArrayList<String>();
		gl.generateIrForClasses(program, clsInfoCG, printUtil, out, functionFormalNameList);
	}
}
