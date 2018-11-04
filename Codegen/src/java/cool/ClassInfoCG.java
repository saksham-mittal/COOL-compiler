package cool;

import java.util.*;
import java.io.PrintWriter;

public class ClassInfoCG {
    // This maps class names to all the class related attributes and methods
    public HashMap<String, BasicClassBlockCG> cls;

    // The constructor adds the default classes and their methods to the AST.program    
    public ClassInfoCG(AST.program program) {
        // Instansiating cls 
        cls = new HashMap<String, BasicClassBlockCG>();

        String filename = AST.program.classes.get(0).filename;

        // Object class
        List<AST.feature> objectFeatureList = new ArrayList <AST.feature>();
        objectFeatureList.add(new AST.method("abort", new ArrayList<AST.formal>(), "Object", new AST.no_expr(0), 0));
        objectFeatureList.add(new AST.method("type_name", new ArrayList<AST.formal>(), "String", new AST.no_expr(0), 0));
        AST.program.classes.add(new AST.class_("Object", filename, null, objectFeatureList, 0));

        // IO class
        List<AST.feature> ioFeatureList = new ArrayList<AST.feature>(objectFeatureList);  // IO inherits from Object

        List<AST.formal> osFormalList = new ArrayList<AST.formal>();
        osFormalList.add(new AST.formal("out_string", "String", 0));
        List<AST.formal> oiFormalList = new ArrayList<AST.formal>();
        oiFormalList.add(new AST.formal("out_int", "Int", 0));

        ioFeatureList.add(new AST.method("out_string", osFormalList, "Object", new AST.no_expr(0), 0));
        ioFeatureList.add(new AST.method("out_int", oiFormalList, "Object", new AST.no_expr(0), 0));
        ioFeatureList.add(new AST.method("in_string", new ArrayList<AST.formal>(), "String", new AST.no_expr(0), 0));
        ioFeatureList.add(new AST.method("in_int", new ArrayList<AST.formal>(), "Int", new AST.no_expr(0), 0));
        AST.program.classes.add(new AST.class_("IO", filename, "Object", ioFeatureList, 0));

        // Int class
        AST.program.classes.add(new AST.class_("Int", filename, "Object", objectFeatureList, 0));

        // Bool class
        AST.program.classes.add(new AST.class_("Bool", filename, "Object", objectFeatureList, 0));

        List<AST.feature> stringFeatureList = new ArrayList<AST.feature>(objectFeatureList);
        List<AST.formal> concatFormalList = new ArrayList<AST.formal>();
        concatFormalList.add(new AST.formal("s", "String", 0));
        List<AST.formal> substrFormalList = new ArrayList<AST.formal>();
        substrFormalList.add(new AST.formal("i", "Int", 0));
        substrFormalList.add(new AST.formal("l", "Int", 0));

        stringFeatureList.add(new AST.method("length", new ArrayList<AST.formal>(), "Int", new AST.no_expr(0), 0));
        stringFeatureList.add(new AST.method("concat", concatFormalList, "String", new AST.no_expr(0), 0));
        stringFeatureList.add(new AST.method("substr", substrFormalList, "String", new AST.no_expr(0), 0));

        AST.program.classes.add(new AST.class_("String", filename, "Object", stringFeatureList, 0));
    }

    public void insertClasses(AST.program program) {
        for(AST.class_ cl : AST.program.classes) {
            List<AST.attr> attrTempList = new ArrayList<AST.attr>();
            List<AST.method> mthdTempList = new ArrayList<AST.method>();

            for (AST.feature ftre : cl.features) {
                if (ftre.getClass() == AST.attr.class) {
                    AST.attr attrTemp = (AST.attr)ftre;
                    attrTempList.add(attrTemp);
                } else if (ftre.getClass() == AST.method.class) {
                    AST.method mthdTemp = (AST.method)ftre;
                    mthdTempList.add(mthdTemp);
                }
            }

            if (cl.parent == null) {
                cls.put(cl.name, new BasicClassBlockCG(cl.name, cl.name, attrTempList, mthdTempList));
            } else {
                cls.put(cl.name, new BasicClassBlockCG(cl.name, cl.parent, attrTempList, mthdTempList));
            }
        }
    }
}