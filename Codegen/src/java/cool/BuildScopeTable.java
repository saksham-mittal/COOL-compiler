package cool;

import java.util.*;

import cool.AST;
import cool.VisitorMechanism;

public class BuildScopeTable {

    // Declaring an object for 'ScopeTable' class
    public ScopeTable<AST.attr> scpTbl;
    
    // Declaring object for Visitor mechanism Implementation
    public VisitorMechanism visitorMech;
    
    public String filename;
    
    // Constructor for the class
    public BuildScopeTable() {
        scpTbl = new ScopeTable<AST.attr>();
        visitorMech = new VisitorMechanism();
    }

    // This function traverses over all nodes of AST and adds the corresponding attributes
    public void fillTable(ClassInfo clsInfo) {
        for(AST.class_ cl : AST.program.classes) {
            // Adding the filename for each class
            filename = cl.filename;
            
            // Calling enterScope while entering the scope
            // 'enterScope' increments the current level number and adds a 
            // new empty hashtable to the front of the list
            scpTbl.enterScope();
            
            // 'self' is inserted in the scope table when it is present as an attribute in a class 
            scpTbl.insert("self", new AST.attr("self", cl.name, new AST.no_expr(cl.lineNo), cl.lineNo));
            
            // 'insertAllAttrs' inserts all attributes of its parent class and ancestors,
            // and its other attributes (current class)
            // It directly adds a Hashmap to the current scope. 
            scpTbl.insertAll(clsInfo.cls.get(cl.name).attrList);

            // Function for visiting the current class
            visitorMech.VisitNode(cl, clsInfo, scpTbl);

            // Exiting the scope for the current class
            scpTbl.exitScope();
        }
    }

}