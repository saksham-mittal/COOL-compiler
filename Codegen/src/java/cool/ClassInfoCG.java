package cool;

import java.util.*;
import java.io.PrintWriter;

/* 
    This class stores info of all classses in form of Hashmap,
    which maps the class names to their BasicClassBlockCG objects
*/
public class ClassInfoCG {
    // This maps class names to all the class related attributes and methods
    public HashMap<String, BasicClassBlockCG> cls;

    /*
        The constructor adds the default classes and their methods to the AST.program 
        The default classes include :
        1. Object
        2. IO
        3. Int
        4. Bool
        5. String
    */  
    public ClassInfoCG(AST.program program) {
        // Instansiating cls 
        cls = new HashMap<String, BasicClassBlockCG>();

        String filename = AST.program.classes.get(0).filename;

        List<AST.feature> objectFeatureList;
        List<AST.feature> ioFeatureList;  // IO inherits from Object
        List<AST.formal> osFormalList;
        List<AST.formal> oiFormalList;
        List<AST.feature> stringFeatureList;
        List<AST.formal> concatFormalList;
        List<AST.formal> substrFormalList;
        /* Object class */
        /*
            Object has methods:
                1. abort() : Object
                2. type_name(): String
                3. copy() : Object
        */
        objectFeatureList = new ArrayList <AST.feature>();
        objectFeatureList.add(new AST.method("type_name", new ArrayList<AST.formal>(), "String", new AST.no_expr(0), 0));
        objectFeatureList.add(new AST.method("abort", new ArrayList<AST.formal>(), "Object", new AST.no_expr(0), 0));
        AST.program.classes.add(new AST.class_("Object", filename, null, objectFeatureList, 0));

        /* IO class */
        /*
            IO has methods:
                1. out_string(x : String) : IO
                2. out_int(x : Int) : IO
                3. in_string() : String
                4. in_int() : Int
        */
        ioFeatureList = new ArrayList<AST.feature>(objectFeatureList);
        osFormalList = new ArrayList<AST.formal>();
        oiFormalList = new ArrayList<AST.formal>();
        oiFormalList.add(new AST.formal("out_int", "Int", 0));
        osFormalList.add(new AST.formal("out_string", "String", 0));

        ioFeatureList.add(new AST.method("out_int", oiFormalList, "Object", new AST.no_expr(0), 0));
        ioFeatureList.add(new AST.method("in_string", new ArrayList<AST.formal>(), "String", new AST.no_expr(0), 0));
        ioFeatureList.add(new AST.method("in_int", new ArrayList<AST.formal>(), "Int", new AST.no_expr(0), 0));
        ioFeatureList.add(new AST.method("out_string", osFormalList, "Object", new AST.no_expr(0), 0));
        AST.program.classes.add(new AST.class_("IO", filename, "Object", ioFeatureList, 0));

        /* Int class */
        AST.program.classes.add(new AST.class_("Int", filename, "Object", objectFeatureList, 0));

        /* Bool class */
        AST.program.classes.add(new AST.class_("Bool", filename, "Object", objectFeatureList, 0));

        /* String class */
        /*
            String has methods :
                1. length() : Int
                2. concat(s : String) : String
                3. substr(i : Int, l : Int) : String
        */
        stringFeatureList = new ArrayList<AST.feature>(objectFeatureList);
        substrFormalList = new ArrayList<AST.formal>();
        substrFormalList.add(new AST.formal("i", "Int", 0));
        substrFormalList.add(new AST.formal("l", "Int", 0));
        concatFormalList = new ArrayList<AST.formal>();
        concatFormalList.add(new AST.formal("s", "String", 0));

        stringFeatureList.add(new AST.method("concat", concatFormalList, "String", new AST.no_expr(0), 0));
        stringFeatureList.add(new AST.method("substr", substrFormalList, "String", new AST.no_expr(0), 0));
        stringFeatureList.add(new AST.method("length", new ArrayList<AST.formal>(), "Int", new AST.no_expr(0), 0));

        AST.program.classes.add(new AST.class_("String", filename, "Object", stringFeatureList, 0));
    }

    // This method insert the classes of the AST.program to the Hashmap cls
    // Also takes care of adding attributes and methods to 'cls'
    public void insertClasses(AST.program program) {
        for(AST.class_ cl : AST.program.classes) {
            List<AST.attr> attrTempList = new ArrayList<AST.attr>();
            List<AST.method> mthdTempList = new ArrayList<AST.method>();

            for (AST.feature ftre : cl.features) {
                if (ftre.getClass() != AST.attr.class) {
                    AST.method mthdTemp = (AST.method)ftre;
                    mthdTempList.add(mthdTemp);
                } else if (ftre.getClass() != AST.method.class) {
                    AST.attr attrTemp = (AST.attr)ftre;
                    attrTempList.add(attrTemp);
                }
            }

            if (cl.parent != null) {
                cls.put(cl.name, new BasicClassBlockCG(cl.name, cl.parent, attrTempList, mthdTempList));
            } else {
                cls.put(cl.name, new BasicClassBlockCG(cl.name, cl.name, attrTempList, mthdTempList));
            }
        }
    }
}