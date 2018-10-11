package cool;

import java.util.*;

/*

This class has the following functionality :
* It intialises and builds the inheritance graph
* It traverses the class list from AST.class_, and adds them to the graph
* It checks if the class to be added is already among {Object, IO, String, Integer, Bool}
* It checks if the class is inheriting from {Integer, String, Bool}
* It checks if the parent class is already defined or not  

*/

public class BuildInheritanceGraph {

    // Storing the number of classes
    public Integer classSize = 0;

    // This stores the map between classe names and their index
    public Map<String, Integer> classNameToIndexMap;

    // This stores the map between classes and their class Name
    public Map<String, AST.class_> classToClassNameMap;

    // This stores the map between Indicies and their Class Name
    public Map<Integer, String> indexToClassNameMap;

    // Adjacency list to store the graph of classes
    public ArrayList <ArrayList <Integer>> graph;

    // For repoting error
    public static ReportError reportError;

    // Constructor for the class
    public BuildInheritanceGraph() {
        classNameToIndexMap = new HashMap<>();
        classToClassNameMap = new HashMap<>();
        indexToClassNameMap = new HashMap<>();

        graph = new ArrayList <ArrayList <Integer>>();

        reportError = new ReportError();
    }

    // Traverse the AST classes and adding to the graph
    public void addingAndVerifyingClasses() {
        for(AST.class_ cl : AST.program.classes) {
			if(cl.name == "Object" || cl.name == "String" || cl.name == "Int" || cl.name == "Bool" || cl.name == "IO") {
				reportError.report(cl.filename, cl.lineNo, "Class redefinition of class '" + cl.name + "' not possible.");
				System.exit(1);
			} else if(cl.parent == "String" || cl.parent == "Int" || cl.parent == "Bool") {
				reportError.report(cl.filename, cl.lineNo, "Class '" + cl.name + "' cannot inherit class '" + cl.parent + "'.");
				System.exit(1);
			} else if(classNameToIndexMap.containsKey(cl.name) == false) {
				// Means cl is a new class and needs to be added to the graph
				indexToClassNameMap.put(classSize, cl.name);
				classNameToIndexMap.put(cl.name, classSize);
                classToClassNameMap.put(cl.name, cl);
                
                classSize = classSize + 1;

				graph.add(new ArrayList <Integer> ());
			} else if(classNameToIndexMap.containsKey(cl.name) == true) {
                reportError.report(cl.filename, cl.lineNo, "Class '" + cl.name + "' has already been defined earlier.");
                System.exit(1);
            }
		}
    }

    // This method adds edges to our graph after checking for undefined parent behaviour 
    public void addingEdges() {
        for(AST.class_ cl : AST.program.classes) {
            // Finding a parent class, which is not defined            
            if(classNameToIndexMap.containsKey(cl.parent) == false) {
                reportError.report(cl.filename, cl.lineNo, "Parent class '" + cl.parent + "' not found.");
                System.exit(1);
            }

            // This adds an edge between parent and the child class
            graph.get(classNameToIndexMap.get(cl.parent)).add(classNameToIndexMap.get(cl.name));
		}
    }

    // Detecting Loops
	// 	 * This function traverses our graph in a BFS order
	// 	 * If multiple cycles exists, all of them are reported
    public boolean detectLoops(boolean cycl, Boolean[] visited, Queue<Integer> q) {
        while(q.isEmpty() == false) {
            // Current node 
            int node = q.poll();

            if(visited[node] == false) {
                // If a node is not visited, after visiting it is marked as 'true'
                visited[node] = true;
            } else {
                // Reporting error on finding cycle
                reportError.report(classToClassNameMap.get(indexToClassNameMap.get(node)).filename, 1, "Class '" + indexToClassNameMap.get(node) + "' or it's ancestor is a part of inheritance cycle.");
                // Means cycle is detected
                cycl = true;

                if(q.isEmpty() == true) {
                    // If queue becomes empty, we iterate over all classes to check if
                    // anyone is still not visited
                    for(int i=0; i<classSize; ++i) {
                        if(visited[i] == false) {
                            q.offer(i);
                            break;
                        }
                    }
                }

                continue;
            }

            // Adding the childs of the current node
            for(Integer child : graph.get(node)) {
                q.offer(child);
            }

            if(q.isEmpty() == true) {
                // If queue becomes empty, we iterate over all classes to check if
                // anyone is still not visited
                for(int i=0; i<classSize; ++i) {
                    if(visited[i] == false) {
                        q.offer(i);
                        break;
                    }
                }
            }
        }

        return cycl;
    }

    /*
		This function traverses our Inheritance graph in a BFS manner,
		and adds the classes in the clsInfo object
    */
    public void fillClassInfo(ClassInfo clsInfo, Queue<Integer> q) {
        while(q.isEmpty() == false) {
            int firstClass = q.poll();

            if(firstClass != 1 && firstClass != 0) {
                // Means firstClass is not 'Object', 'IO', 'Integer', 'String', 'Bool'

                // This inserts the class in the clsInfo object
                // For more info, see the ClassInfo::insertClass() method
                clsInfo.insertClass(classToClassNameMap.get(indexToClassNameMap.get(firstClass)));
            }
            for(Integer child : graph.get(firstClass)) {
                // Traversing over the child classes of the current class
                
                // Then, add the child class in the queue for BFS traversal 
                q.offer(child);
            }
        }
    }

}