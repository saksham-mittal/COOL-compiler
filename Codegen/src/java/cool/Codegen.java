package cool;

import java.util.*;
import java.io.PrintWriter;

public class Codegen{

	public static BuildInheritanceGraphCG inheritanceGraph;

	public static GenerateLlvm generateLlvmObject;

	public BasicClassBlockCG bcb;

	// The class for storing class information (its attributes and methods)
	public static ClassInfoCG clsInfoCG;

	public Codegen(AST.program program, PrintWriter out){
		//Write Code generator code here
		out.println("; I am a comment in LLVM-IR. Feel free to remove me.");
		
		/* 
		Layout as specified by clang++ :
		layout ::= bigendian | littleendian | ptr sz align0 align1 | int sz align0 align1
		| float sz align0 align1 | aggr sz align0 align1 | stack sz align0 align1
		*/
		out.println("target datalayout = \"e-m:e-i64:64-f80:128-n8:16:32:64-S128\"");
		// This specifies target host
		out.println("target triple = \"x86_64-unknown-linux-gnu\"\n");
		out.println("@Abortdivby0 = private unnamed_addr constant [22 x i8] c\"Error: Division by 0\\0A\\00\", align 1\n@Abortdispvoid = private unnamed_addr constant [25 x i8] c\"Error: Dispatch on void\\0A\\00\", align 1\n");
		out.println("declare i32 @printf(i8*, ...)\ndeclare i32 @scanf(i8*, ...)\ndeclare i32 @strcmp(i8*, i8*)\ndeclare i8* @strcat(i8*, i8*)\ndeclare i8* @strcpy(i8*, i8*)\ndeclare i8* @strncpy(i8*, i8*, i32)\ndeclare i64 @strlen(i8*)\ndeclare i8* @malloc(i64)\ndeclare void @exit(i32)");
		out.println("@strformatstr = private unnamed_addr constant [3 x i8] c\"%s\\00\", align 1\n@intformatstr = private unnamed_addr constant [3 x i8] c\"%d\\00\", align 1\n");
	
		// The inheritance graph built after adding all classes and their parents
		inheritanceGraph = new BuildInheritanceGraphCG();

		// Initialising the maps for 'Object' and 'IO' classes
        BuildInheritanceGraphCG.classNameToIndexMap.put("Object", 0);
        BuildInheritanceGraphCG.indexToClassNameMap.put(0, "Object");
        BuildInheritanceGraphCG.classNameToIndexMap.put("IO", 1);
		BuildInheritanceGraphCG.indexToClassNameMap.put(1, "IO");
		BuildInheritanceGraphCG.classNameToIndexMap.put("Int", 2);
		BuildInheritanceGraphCG.indexToClassNameMap.put(2, "Int");
		BuildInheritanceGraphCG.classNameToIndexMap.put("String", 3);
		BuildInheritanceGraphCG.indexToClassNameMap.put(3, "String");
		BuildInheritanceGraphCG.classNameToIndexMap.put("Bool", 4);
		BuildInheritanceGraphCG.indexToClassNameMap.put(4, "Bool");

		// Adding 'Object', 'IO', 'Int', 'String' and 'Bool' classes to the graph
        BuildInheritanceGraphCG.graph.add(new ArrayList <Integer> (Arrays.asList(1, 2, 3, 4)));
		BuildInheritanceGraphCG.graph.add(new ArrayList <Integer>());
		BuildInheritanceGraphCG.graph.add(new ArrayList <Integer>());
		BuildInheritanceGraphCG.graph.add(new ArrayList <Integer>());
		BuildInheritanceGraphCG.graph.add(new ArrayList <Integer>());

		// Incrementing two times because two classes are added
		inheritanceGraph.classSize += 5;

		// Traverse the AST classes and adding to the graph
		inheritanceGraph.addingClasses();

		// Adding edges to our graph
		inheritanceGraph.addingEdges();

		// 'bfsQueue' maintains the queue while traversing the nodes
		Queue<Integer> bfsQueue = new LinkedList<Integer>();
		bfsQueue.offer(0);

		/*
		This function traverses our Inheritance graph in a BFS manner,
		and adds the classes in the clsInfoCG object
		*/
		clsInfoCG = new ClassInfoCG();

		inheritanceGraph.fillClassInfoCG(clsInfoCG, bfsQueue, out);
		out.println("\n");

		bfsQueue.clear();
		bfsQueue.offer(0);

		generateLlvmObject = new GenerateLlvm();
		
		// Generating the declarations of standard class's methods 
		generateLlvmObject.utilPrintMethodsOfClass(clsInfoCG, bfsQueue, out);
		out.println("\n");
		
		bfsQueue.clear();
		bfsQueue.offer(0);
		
		// Generating the definitions of standard class's methods 
		generateLlvmObject.utilGenerateMethodsOfClass(clsInfoCG, bfsQueue, out);

		// First, generating the code for the 'Main' class
		generateLlvmObject.generateLlvmMainClass();

		// Generating code for classes other than 'Main'
		generateLlvmObject.generateLlvmOtherClasses();
	}
}
