package cool;

import java.util.*;
// Importing for Entry
import java.util.Map.Entry;

import cool.AST;
import cool.BasicClassBlock;

public class ClassInfoCG {
    // For reporting error
    // public static ReportError reportError;

    // This maps class names to all the class related attributes and methods
    public HashMap<String, BasicClassBlockCG> cls;
    
    // This maps class names to their depths in the inheritance graph
    public HashMap<String, Integer> classDepth;

    public ClassInfoCG() {
        // Instansiating cls 
        cls = new HashMap<String, BasicClassBlockCG>();
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
        List<AST.formal> objectFormalList = new ArrayList<AST.formal>();
        objectFormalList.add(new AST.formal("this", "Object", 0));

        objectMethods.put("abort", new AST.method("abort", objectFormalList, "Object", new AST.no_expr(0), 0));
        objectMethods.put("type_name", new AST.method("type_name", objectFormalList, "String", new AST.no_expr(0), 0));
        objectMethods.put("copy", new AST.method("copy",objectFormalList, "Object", new AST.no_expr(0), 0));

        ArrayList<AST.method> objectMethodList = new ArrayList<AST.method>();
        objectMethodList.add(new AST.method("abort", objectFormalList, "Object", new AST.no_expr(0), 0));
        objectMethodList.add(new AST.method("type_name", objectFormalList, "String", new AST.no_expr(0), 0));
        objectMethodList.add(new AST.method("copy", objectFormalList, "Object", new AST.no_expr(0), 0));

        // Maps the method name to the offset of the method
        // This is later used when we inherit from other classes
        // and override the methods of the parents class
        HashMap<String, Integer> objectMethodOffset = new HashMap<String, Integer>();
        objectMethodOffset.put("abort", 0);
        objectMethodOffset.put("type_name", 1);
        objectMethodOffset.put("copy", 2);

        // Mapping of the Object class method's to the llmv ir's naming format
        HashMap<String, String> objectLlvmIrNameMap = new HashMap<String, String>();
        llvmIrNameMap.put("abort", "@_ZN6Object5abort"); 
        llvmIrNameMap.put("type_name", "@_ZN6Object9type_name"); 
        llvmIrNameMap.put("copy", "@_ZN6Object4copy"); 

        // Adding the Object class to the cls(Class HashMap)
        cls.put("Object", new BasicClassBlockCG("Object", null, new HashMap<String, AST.attr>(), objectMethods,
        new HashMap<String, Integer>(), objectMethodOffset, new ArrayList<AST.attr>(), objectMethodList, objectLlvmIrNameMap));
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

        // Array list for storing formals of string, int, and io class's method 
        List<AST.formal> ioFormals = new ArrayList<AST.formal>();
        ioFormals.add(new AST.formal("this", "IO", 0));
        
        List<AST.formal> stringFormals = new ArrayList<AST.formal>();
        stringFormals.add(new AST.formal("this", "IO", 0));
        stringFormals.add(new AST.formal("out_string", "String", 0));
        
        List<AST.formal> intFormals = new ArrayList<AST.formal>();
        intFormals.add(new AST.formal("this", "IO", 0));
        intFormals.add(new AST.formal("out_int", "Int", 0));

        IOMethods.put("out_string", new AST.method("out_string", stringFormals, "IO", new AST.no_expr(0), 0));
        IOMethods.put("out_int", new AST.method("out_int", intFormals, "IO", new AST.no_expr(0), 0));
        IOMethods.put("in_string", new AST.method("in_string", ioFormals, "String", new AST.no_expr(0), 0));
        IOMethods.put("in_int", new AST.method("in_int", ioFormals, "Int", new AST.no_expr(0), 0));

        ArrayList<AST.method> ioMethodList = new ArrayList<AST.method>();
        ioMethodList.putAll(objectMethodList);
        ioMethodList.add(new AST.method("out_string", stringFormals, "IO", new AST.no_expr(0), 0));
        ioMethodList.add(new AST.method("out_int", intFormals, "IO", new AST.no_expr(0), 0));
        ioMethodList.add(new AST.method("in_string", ioFormals, "String", new AST.no_expr(0), 0));
        ioMethodList.add(new AST.method("in_int", ioFormals, "Int", new AST.no_expr(0), 0));
        ioMethodList.set(2, new AST.method("copy", ioFormals, "IO", new AST.no_expr(0), 0));

        // Maps the method name to the offset of the method
        // This is later used when we inherit from other classes
        // and override the methods of the parents class
        HashMap<String, Integer> ioMethodOffset = new HashMap<String, Integer>();
        ioMethodOffset.putAll(objectMethodOffset);
        ioMethodOffset.put("out_string", 3);
        ioMethodOffset.put("out_int", 4);
        ioMethodOffset.put("in_string", 5);
        ioMethodOffset.put("in_int", 6);

        // Mapping of the Object class method's to the llmv ir's naming format
        HashMap<String, String> ioLlvmIrNameMap = new HashMap<String, String>();
        ioLlvmIrNameMap.putAll(objectLlvmIrNameMap);
        ioLlvmIrNameMap.put("out_string", "@_ZN2IO10out_string");
        ioLlvmIrNameMap.put("out_int", "@_ZN2IO7out_int");
        ioLlvmIrNameMap.put("in_string", "@_ZN2IO9in_string"); 
        ioLlvmIrNameMap.put("in_int", "@_ZN2IO9in_int");
        ioLlvmIrNameMap.put("copy", "@_ZN2IO4copy");

        // Adding the IO class to the cls(Class HashMap)
        cls.put("IO", new BasicClassBlockCG("IO", "Object", new HashMap<String, AST.attr>(), IOMethods, new HashMap<String, Integer>(), 
                ioMethodOffset, new ArrayList<AST.attr>(), ioMethodList, ioLlvmIrNameMap));
        // Since IO class inherits from Object class
        cls.get("IO").mthdList.putAll(objectMethods);
        // IO class is at depth 1
        classDepth.put("IO", 1);
        cls.get("IO").paList.add(new AST.attr("__Base", "Object.Base", new AST.no_expr(0), 0));
        cls.get("IO").attrOffset.put("__Base", 0);

        /*
        Int class
        */

        // Adding the Int class to the cls(Class HashMap)
        cls.put("Int", new BasicClassBlockCG("Int", "Object", new HashMap<String, AST.attr>(), new HashMap<String, AST.method>(),
                new HashMap<String, Integer>(), objectMethodOffset, new ArrayList<AST.attr>(), objectMethodList, objectLlvmIrNameMap));
        // Since Int class inherits from Object class
        cls.get("Int").mthdList.putAll(objectMethods);
        // Int class is at depth 1
        classDepth.put("Int", 1);
        cls.get("Int").pmList.get(2).typeid = "Int";    // Redefining the 'copy' method\
        cls.get("Int").llvmIrName.put("copy", "@_ZN3Int4copy");

        /*
        Bool class
        */

        // Adding the Bool class to the cls(Class HashMap)
        cls.put("Bool", new BasicClassBlock("Bool", "Object", new HashMap<String, AST.attr>(), new HashMap<String, AST.method>()),
                new HashMap<String, Integer>(), objectMethodOffset, new ArrayList<AST.attr>(), objectMethodList, objectLlvmIrNameMap);
        // Since Bool class inherits from Object class
        cls.get("Bool").mthdList.putAll(objectMethods);
        // Bool class is at depth 1
        classDepth.put("Bool", 1);
        cls.get("Bool").pmList.get(2).typeid = "Bool";    // Redefining the 'copy' method\
        cls.get("Bool").llvmIrName.put("copy", "@_ZN4Bool4copy");

        /*
        String has methods :
            * length() : Int
            * concat(s : String) : String
            * substr(i : Int, l : Int) : String
        */
        // This maps the method names of String class to the AST.method nodes
        HashMap<String, AST.method> stringMethods = new HashMap<String, AST.method>();

        List<AST.formal> strFormal = new ArrayList<AST.formal>();
        strFormal.add(new AST.formal("this", "String", 0));
        
        // Array list for storing formals of concat method 
        List<AST.formal> concatFormals = new ArrayList<AST.formal>();
        concatFormals.add(new AST.formal("this", "String", 0));
        concatFormals.add(new AST.formal("s", "String", 0));
        
        // Array list for storing formals of substr method 
        List<AST.formal> substrFormals = new ArrayList<AST.formal>();
        substrFormals.add(new AST.formal("this", "String", 0));
        substrFormals.add(new AST.formal("i", "Int", 0));
        substrFormals.add(new AST.formal("l", "Int", 0));

        stringMethods.put("length", new AST.method("length", strFormal, "Int", new AST.no_expr(0), 0));
        stringMethods.put("concat", new AST.method("concat", concatFormals, "String", new AST.no_expr(0), 0));
        stringMethods.put("substr", new AST.method("substr", stringFormals, "String", new AST.no_expr(0), 0));

        ArrayList<AST.method> stringMethodList = new ArrayList<AST.method>();
        stringMethodList.putAll(objectMethodList);
        stringMethodList.add(new AST.method("length", strFormal, "Int", new AST.no_expr(0), 0));
        stringMethodList.add(new AST.method("concat", concatFormals, "String", new AST.no_expr(0), 0));
        stringMethodList.add(new AST.method("substr", stringFormals, "String", new AST.no_expr(0), 0));
        stringMethodList.set(2, new AST.method("copy", strFormal, "String", new AST.no_expr(0), 0));
        stringMethodList.get(2).typeid = "String";

        // Maps the method name to the offset of the method
        // This is later used when we inherit from other classes
        // and override the methods of the parents class
        HashMap<String, Integer> stringMethodOffset = new HashMap<String, Integer>();
        stringMethodOffset.putAll(objectMethodOffset);
        stringMethodOffset.put("length", 3);
        stringMethodOffset.put("concat", 4);
        stringMethodOffset.put("substr", 5);

        // Mapping of the Object class method's to the llmv ir's naming format
        HashMap<String, String> stringLlvmIrNameMap = new HashMap<String, String>();
        stringLlvmIrNameMap.putAll(objectLlvmIrNameMap);
        stringLlvmIrNameMap.put("length", "@_ZN6String6length");
        stringLlvmIrNameMap.put("concat", "@_ZN6String6concat");
        stringLlvmIrNameMap.put("substr", "@_ZN6String6substr"); 
        stringLlvmIrNameMap.put("copy", "@_ZN6String4copy");


        // Adding the String class to the cls(Class HashMap)
        cls.put("String", new BasicClassBlock("String", "Object", new HashMap<String, AST.attr>(), stringMethods, new HashMap<String, Integer>(),
                stringMethodOffset, new ArrayList<AST.attr>(), stringMethodList, stringLlvmIrNameMap));
        // Since String class inherits from Object class
        cls.get("String").mthdList.putAll(objectMethods);
        classDepth.put("String", 1);

        cls.get("String").llvmIrName.put("copy", "@_ZN6String4copy");
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