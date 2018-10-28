package cool;

import java.util.*;

import cool.AST;

public class BuildInheritanceGraphCG {
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

    // Constructor for the class
    public BuildInheritanceGraphCG() {
        classNameToIndexMap = new HashMap<>();
        classToClassNameMap = new HashMap<>();
        indexToClassNameMap = new HashMap<>();

        graph = new ArrayList <ArrayList <Integer>>();
    }
    // Traverse the AST classes and adding to the graph
    public void addingClasses() {
        for(AST.class_ cl : AST.program.classes) {
            //cl is a new class and needs to be added to the graph
            indexToClassNameMap.put(classSize , cl.name);
            classNameToIndexMap.put(cl.name , classSize);
            classToClassNameMap.put(cl.name , cl);
            classSize++;
            graph.add(new ArrayList <Integer> ());
        }
    }
    // This method adds edges to our graph
    public void addingEdges() {
        for(AST.class_ cl : AST.program.classes) {
            // This adds an edge between parent and the child class
            graph.get(classNameToIndexMap.get(cl.parent)).add(classNameToIndexMap.get(cl.name));
        }
    }
    
    /*
		This function traverses our Inheritance graph in a BFS manner,
		and adds the classes in the clsInfoCG object
    */
    // public void fillClassInfoCG(ClassInfoCG clsInfoCG, Queue<Integer> q) {
    //     while(q.isEmpty() == false) {
    //         int firstClass = q.poll();

    //         if(firstClass != 1 && firstClass != 0) {
    //             // Means firstClass is not 'Object', 'IO', 'Integer', 'String', 'Bool'

    //             // This inserts the class in the clsInfo object
    //             // For more info, see the ClassInfoCG::insertClass() method
    //             clsInfoCG.insertClass(classToClassNameMap.get(indexToClassNameMap.get(firstClass)));
    //         }

    //         for(Integer child : graph.get(firstClass)) {
    //             // Traversing over the child classes of the current class
                
    //             // Then, add the child class in the queue for BFS traversal 
    //             q.offer(child);
    //         }
    //     }
    // }
}