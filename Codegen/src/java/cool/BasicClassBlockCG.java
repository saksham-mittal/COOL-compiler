package cool;

import java.util.*;
import java.io.PrintWriter;

/* 
    This class contains the basic info of a class like :
    1. Class name
    2. Parent class name
    3. Attribute list of class
    4. Method list of class
*/
public class BasicClassBlockCG {
    public List<AST.method> mthdList = new ArrayList<AST.method>();
    public List<AST.attr> attrList = new ArrayList<AST.attr>();
    public String parent;
    public String name;

    // The constructor intialises the members of the class
    public BasicClassBlockCG(String clName, String parentName, List<AST.attr> aList, List<AST.method> mList) {
        mthdList = mList;
        attrList = aList;
        parent = parentName;
        name = clName;
    }
}