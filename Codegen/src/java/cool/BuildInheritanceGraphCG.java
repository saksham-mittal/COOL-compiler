package cool;

import java.io.PrintWriter;
import java.util.*;

import cool.AST;

public class BuildInheritanceGraphCG {
    // Storing the number of classes
    public Integer classSize = 0;

    // This stores the map between classe names and their index
    public static Map<String, Integer> classNameToIndexMap;

    // This stores the map between classes and their class Name
    public static Map<String, AST.class_> classToClassNameMap;

    // This stores the map between Indicies and their Class Name
    public static Map<Integer, String> indexToClassNameMap;

    // Adjacency list to store the graph of classes
    public static ArrayList <ArrayList <Integer>> graph;

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
    
    // Prints the declaration of class and its parent class attributes
    public void printDeclarationOfClass(BasicClassBlockCG cb , PrintWriter out) {
        // Printing the class definition to the IR
        out.print("%class." + cb.name + ".Base = type {" );
        // Printing attributes of the class
        for(int i = 0; i < cb.paList.size() ; i++) {
            AST.attr att = cb.paList.get(i);
            if( i != 0 ) {
                out.print(", ");
            }
            String typeid; 
            if(att.typeid == "Int" )            typeid = "i32";
            else if(att.typeid == "String" )    typeid = "[1024 x i8]*";
            else if(att.typeid == "Bool" )      typeid = "i8";
            else  typeid =  "%class." + att.typeid + "*";
            out.print(typeid);
        }
        out.print(" }\n");
        out.print("%class." + cb.name + " = type { i32, i8*, %class." + cb.name + ".Base }\n");
    }

    /*
		This function traverses our Inheritance graph in a BFS manner,
		and adds the classes in the clsInfoCG object
    */
    public void fillClassInfoCG(ClassInfoCG clsInfoCG, Queue<Integer> q, PrintWriter out) {
        while(q.isEmpty() == false) {
            int firstClass = q.poll();

            if(firstClass > 4) {
                // Means firstClass is not 'Object', 'IO', 'Integer', 'String', 'Bool'

                // This inserts the class in the clsInfo object
                // For more info, see the ClassInfoCG::insertClass() method
                clsInfoCG.insertClass(classToClassNameMap.get(indexToClassNameMap.get(firstClass)));
            }
            printDeclarationOfClass(clsInfoCG.cls.get(indexToClassNameMap.get(firstClass)) , out);
            for(Integer child : graph.get(firstClass)) {
                // Traversing over the child classes of the current class
                
                // Then, add the child class in the queue for BFS traversal 
                q.offer(child);
            }
        }
    }
}