package cool;

import java.util.*;

import cool.AST;
import cool.AST.attr;

/*

This class contains the following information of a class
* Class Name
* Class Parent Name
* Class Attribute List
* Class Method List

*/
public class BasicClassBlock {

    public String name;
    public String parentName = null;
    public HashMap<String, AST.attr> attrList;
    public HashMap<String, AST.method> mthdList;

    // Constructor for the class
    public BasicClassBlock(String className, String prName, HashMap<String, AST.attr> alist, HashMap<String, AST.method> mlist) {
        name = className;
        parentName = prName;
        attrList = new HashMap<String, AST.attr>();
        attrList.putAll(alist);
        mthdList = new HashMap<String, AST.method>();
        mthdList.putAll(mlist);
    }
    
}