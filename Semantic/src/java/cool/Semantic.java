package cool;

import java.util.*;

import javax.swing.plaf.synth.SynthTextAreaUI;

public class Semantic{
	private boolean errorFlag = false;
	public void reportError(String filename, int lineNo, String error){
		errorFlag = true;
		System.err.println(filename+":"+lineNo+": "+error);
	}
	public boolean getErrorFlag(){
		return errorFlag;
	}

/*
	Don't change code above this line
*/

	// The inheritance graph built after adding all classes and their parents 
	public static BuildInheritanceGraph inheritanceGraph;

	/*
	This stores the attributes of the current scope,
	which includes : 
	* Class attributes
	* let-declarations
	* method-parameters
	* assign-varables
	*/
	public static BuildScopeTable scopeTable;

	// The class for storing class information (its attributes and methods)
	public static ClassInfo clsInfo;

	// Filename for argument in reportError() function
	public String filename;

	public Semantic(AST.program program) {
		//Write Semantic analyzer code here

		// The inheritance graph built after adding all classes and their parents 
		inheritanceGraph = new BuildInheritanceGraph();

		// Instantiating the ClassInfo class
		clsInfo = new ClassInfo();

		// Initialising the maps for 'Object' and 'IO' classes
        inheritanceGraph.classNameToIndexMap.put("Object", 0);
        inheritanceGraph.indexToClassNameMap.put(0, "Object");
        inheritanceGraph.classNameToIndexMap.put("IO", 1);
		inheritanceGraph.indexToClassNameMap.put(1, "IO");

        // Adding 'Object' and 'IO' classes to the graph
        inheritanceGraph.graph.add(new ArrayList <Integer> (Arrays.asList(1)));
		inheritanceGraph.graph.add(new ArrayList <Integer>());

		// Incrementing two times because two classes are added
		inheritanceGraph.classSize += 2;

		// Traverse the AST classes and adding to the graph
		inheritanceGraph.addingAndVerifyingClasses();

		// Adding edges to our graph
		inheritanceGraph.addingEdges();

		// isCycle variable = true means cycle is there
		boolean isCycle = false;
		// 'visited' array for maintaining whoch nodes are visited
		Boolean[] visited = new Boolean[inheritanceGraph.classSize + 10];
		// Initialiseing 'visited' array with FALSE value
		Arrays.fill(visited, Boolean.FALSE);
		// 'bfsQueue' maintains the queue while traversing the nodes
		Queue<Integer> bfsQueue = new LinkedList<Integer>();
		bfsQueue.offer(0);

		/* 
		
		Detecting Loops
		 * This function traverses our graph in a BFS order
		 * If multiple cycles exists, all of them are reported
		
		*/
		if(inheritanceGraph.detectLoops(isCycle, visited, bfsQueue) == true) {
			// This means that cycles were detected, and so compilation will be haulted
			System.exit(1);
		}

		// Emptying the queues and adding the root node in the queue
		bfsQueue.clear();
		bfsQueue.offer(0);

		/*
		This function traverses our Inheritance graph in a BFS manner,
		and adds the classes in the clsInfo object
		*/
		inheritanceGraph.fillClassInfo(clsInfo, bfsQueue);

		// Instantiating the BuildScopeTable class
		scopeTable = new BuildScopeTable();
		
		// Filling the Scope Table
		scopeTable.fillTable(clsInfo);

		filename = AST.program.classes.get(0).filename;
		/*
		Now, we check if Main class is present, and if it is present it should contain 'main' method
		*/		
		BasicClassBlock mainClass = clsInfo.cls.get("Main");
		if(mainClass == null) {
			reportError(filename, 1, "'Main' class is not present in the program.");
		} else if(mainClass.mthdList.containsKey("main") == false) {
			// Means 'main' method is not present in the program
			reportError(filename, 1, "'main' method is not present in the Main class");
		}
	}
}