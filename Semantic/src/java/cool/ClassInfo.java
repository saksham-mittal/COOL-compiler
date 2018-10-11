package cool;

import java.util.*;
// Importing for Entry
import java.util.Map.Entry;

import cool.AST;
import cool.BasicClassBlock;

public class ClassInfo {
    // For reporting error
    public static ReportError reportError;

    // This maps class names to all the class related attributes and methods
    public HashMap<String, BasicClassBlock> cls;
    
    // This maps class names to their depths in the inheritance graph
    public HashMap<String, Integer> classDepth;

    public ClassInfo() {
        // Instansiating cls 
        cls = new HashMap<String, BasicClassBlock>();
        classDepth = new HashMap<String, Integer>();
        reportError = new ReportError();

        /*
        Object has methods:
            * abort() : Object
            * type_name(): String
            * copy() : Object
        */

        // This maps the method names of Object class to the AST.method nodes
        HashMap<String, AST.method> objectMethods = new HashMap<String, AST.method>();

        objectMethods.put("abort", new AST.method("abort", new ArrayList<AST.formal>(), "Object", new AST.no_expr(0), 0));
        objectMethods.put("type_name", new AST.method("type_name", new ArrayList<AST.formal>(), "String", new AST.no_expr(0), 0));
        objectMethods.put("copy", new AST.method("copy", new ArrayList<AST.formal>(), "Object", new AST.no_expr(0), 0));

        // Adding the Object class to the cls(Class HashMap)
        cls.put("Object", new BasicClassBlock("Object", null, new HashMap<String, AST.attr>(), objectMethods));
        // Object class is at depth 0
        classDepth.put("Object", 0);

        /*
        IO has methods:
            * out_string(x : String) : IO
            * out_int(x : Int) : IO
            * in_string() : String
            * in_int() : Int
        */

        // This maps the method names of IO class to the AST.method nodes
        HashMap<String, AST.method> IOMethods = new HashMap<String, AST.method>();

        // Array list for storing formals of out_string method 
        List<AST.formal> stringFormals = new ArrayList<AST.formal>();
        stringFormals.add(new AST.formal("out_string", "String", 0));
        // Array list for storing formals of out_int method 
        List<AST.formal> intFormals = new ArrayList<AST.formal>();
        intFormals.add(new AST.formal("out_int", "Int", 0));

        IOMethods.put("out_string", new AST.method("out_string", stringFormals, "IO", new AST.no_expr(0), 0));
        IOMethods.put("out_int", new AST.method("out_int", intFormals, "IO", new AST.no_expr(0), 0));
        IOMethods.put("in_string", new AST.method("in_string", new ArrayList<AST.formal>(), "String", new AST.no_expr(0), 0));
        IOMethods.put("in_int", new AST.method("in_int", new ArrayList<AST.formal>(), "Int", new AST.no_expr(0), 0));

        // Adding the IO class to the cls(Class HashMap)
        cls.put("IO", new BasicClassBlock("IO", "Object", new HashMap<String, AST.attr>(), IOMethods));
        // Since IO class inherits from Object class
        cls.get("IO").mthdList.putAll(objectMethods);
        // IO class is at depth 1
        classDepth.put("IO", 1);

        /*
        Int class
        */

        // Adding the Int class to the cls(Class HashMap)
        cls.put("Int", new BasicClassBlock("Int", "Object", new HashMap<String, AST.attr>(), new HashMap<String, AST.method>()));
        // Since Int class inherits from Object class
        cls.get("Int").mthdList.putAll(objectMethods);
        // Int class is at depth 1
        classDepth.put("Int", 1);

        /*
        Bool class
        */

        // Adding the Bool class to the cls(Class HashMap)
        cls.put("Bool", new BasicClassBlock("Bool", "Object", new HashMap<String, AST.attr>(), new HashMap<String, AST.method>()));
        // Since Bool class inherits from Object class
        cls.get("Bool").mthdList.putAll(objectMethods);
        // Bool class is at depth 1
        classDepth.put("Bool", 1);

        /*
        String has methods :
            * length() : Int
            * concat(s : String) : String
            * substr(i : Int, l : Int) : String
        */
        // This maps the method names of String class to the AST.method nodes
        HashMap<String, AST.method> stringMethods = new HashMap<String, AST.method>();

        // Array list for storing formals of concat method 
        List<AST.formal> concatFormals = new ArrayList<AST.formal>();
        concatFormals.add(new AST.formal("s", "String", 0));
        // Array list for storing formals of substr method 
        List<AST.formal> substrFormals = new ArrayList<AST.formal>();
        substrFormals.add(new AST.formal("i", "Int", 0));
        substrFormals.add(new AST.formal("l", "Int", 0));

        stringMethods.put("length", new AST.method("length", new ArrayList<AST.formal>(), "Int", new AST.no_expr(0), 0));
        stringMethods.put("concat", new AST.method("concat", concatFormals, "String", new AST.no_expr(0), 0));
        stringMethods.put("substr", new AST.method("substr", stringFormals, "String", new AST.no_expr(0), 0));

        // Adding the String class to the cls(Class HashMap)
        cls.put("String", new BasicClassBlock("String", "Object", new HashMap<String, AST.attr>(), stringMethods));
        // Since String class inherits from Object class
        cls.get("String").mthdList.putAll(stringMethods);
        classDepth.put("String", 1);
    }

    // This function checks for multiple attribute and method definitions in the class
    public void checkAttrMthd(AST.class_ cl, HashMap<String, AST.attr> al, HashMap<String, AST.method> ml) {
        for(AST.feature ft : cl.features) {
            if(ft.getClass() == AST.attr.class) {
                AST.attr attrNew = (AST.attr) ft;

                if(al.containsKey(attrNew.name)) {
                    // Means the attribute is already error
                    reportError.report(cl.filename, attrNew.lineNo, "Attribute '" + attrNew.name + "' is defined more than once.");
                } else {
                    // else adding the attribute to the list
                    al.put(attrNew.name, attrNew);
                }
            } else if(ft.getClass() == AST.method.class) {
                AST.method mthdNew = (AST.method) ft;
                if(ml.containsKey(mthdNew.name)) {
                    // Means method is already present
                    reportError.report(cl.filename, mthdNew.lineNo, "Method '" + mthdNew.name + "' is defined more than once.");
                } else {
                    // else adding tht method to the list
                    ml.put(mthdNew.name, mthdNew);
                }
            }
        }
    }

    // This method checks for redefintions of inherited attributes and methods in the class
    public void checkAttrMthdInherited(AST.class_ cl, BasicClassBlock cb, HashMap<String, AST.attr> al, HashMap<String, AST.method> ml) {

        // This loop is for attribute list
        for(Entry<String, AST.attr> es : al.entrySet()) {
            if(cb.attrList.containsKey(es.getKey())) {
                // Means if parent class attributes contains the attribute of the present class
                reportError.report(cl.filename, es.getValue().lineNo , "Attribute '" + es.getValue().name + "' is also an attribute of its parent class.");
            } else {
                // else adding the attribute the basic block
                cb.attrList.put(es.getKey(), es.getValue());
            }
        }

        boolean err;
        // This loop is for method list
        for(Entry<String, AST.method> es : ml.entrySet()) {
            // Initialising err as False for each method
            err = false;
            if(cb.mthdList.containsKey(es.getKey())) {
                // Means parent class method contains the method of present class

                // Now, methods can be different in 3 ways:
                // * Different number of formals
                // * Different return types
                // * DIfferent parameter types

                AST.method parentMthd = cb.mthdList.get(es.getKey());
                AST.method currentMthd = es.getValue();

                if(currentMthd.formals.size() != parentMthd.formals.size()) {
                    // Means Parent method parameters size is not equal to the current method parameters size
                    reportError.report(cl.filename, currentMthd.lineNo, "Different number of formal paramters in redefined method '" + currentMthd.name + "'.");
                    err = true;
                } else {
                    if(currentMthd.typeid.equals(parentMthd.typeid) == false) {
                        // Means the return type of inherited function is different
                        reportError.report(cl.filename, currentMthd.lineNo, "In the redefined method '" + currentMthd.name + "' the return type is '" + currentMthd.typeid + "' instead of return type '" + parentMthd.typeid + "'.");
                        err = true;
                    }

                    // Now we check for the typeid of parameters
                    for(int i=0; i<currentMthd.formals.size(); i++) {
                        if(currentMthd.formals.get(i).typeid.equals(parentMthd.formals.get(i).typeid) == false) {
                            // Means the parameter typeid does not match with the corresponding parameter of parents type id
                            reportError.report(cl.filename, currentMthd.lineNo, "In redefined method '" + currentMthd.name + "' the paramter typeid '" + currentMthd.formals.get(i).typeid + "' does not match with the parent typeid '" + parentMthd.formals.get(i).typeid + "'.");
                            err = true;
                        }
                    }
                }
            }
            
            if(err == false) {
                // Means no error is found 
                cb.mthdList.put(es.getKey(), es.getValue());
            }
        }
    }

    /*
    We call this function while inserting a class,
    * We insert all the attributes and methods of the parent class
    * It also checks for multiple methods attribute definitions
    * Also checks for method overrides and attributes override   
    */
    public void insertClass(AST.class_ cl) {
        String parentClass = cl.parent;
        
        // Inserting the attribute and method list of parent class to the 'cl' class 
        BasicClassBlock classBlock = new BasicClassBlock(cl.name, parentClass, cls.get(parentClass).attrList, cls.get(parentClass).mthdList);

        // Hashmap for attribute list of ClassBlock
        HashMap<String, AST.attr> classBlockAttrList = new HashMap<String, AST.attr>();
        // Hashmap for method list of ClassBlock
        HashMap<String, AST.method> classBlockMethodList = new HashMap<String, AST.method>();

        // This function checks for multiple attribute and method definitions in the class
        checkAttrMthd(cl, classBlockAttrList, classBlockMethodList);

        // This method checks for redefintions of inherited attributes and methods in the class
        checkAttrMthdInherited(cl, classBlock, classBlockAttrList, classBlockMethodList);

        // Adding the class depth for this inserted class, which is the depth of (parent + 1)
        classDepth.put(cl.name, classDepth.get(parentClass) + 1);

        // Adding the class info to the cls mapping
        cls.put(cl.name, classBlock);
    }

    // This function checks if two classes are same
    public boolean isConforming(String name1, String name2) {
        if(name1.equals(name2)) {
            // If both class names are same
            return true;
        } else {
            name1 = cls.get(name1).parentName;
            if(name1 == null) {
                return false;
            } else {
                return isConforming(name1, name2);
            }
        }
    }

    public String commonAncestor(String name1, String name2) {
        if(name1.equals(name2)) {
            return name1;
        } else if(classDepth.get(name1) < classDepth.get(name2)) {
            // name1 class is at a lower depth than class name2
            return commonAncestor(name2, name1);
        } else {
            // name2 class is at a lower depth than class name1
            return commonAncestor(cls.get(name1).parentName, name2);
        }
    }
}