package cool;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

import cool.AST;
import cool.AST.attr;

/*

This class contains the following information of a class
* Class Name
* Class Parent Name
* Class Attribute List
* Class Method List

*/
public class BasicClassBlockCG {

    public String name;
    public String parentName = null;
    public HashMap<String, AST.attr> attrList;
    public HashMap<String, AST.method> mthdList;

    public HashMap<String, Integer> attrOffset;
    public HashMap<String, Integer> mthdOffset;
    public HashMap<String, String> llvmIrName;

    public ArrayList<AST.method> pmList;
    public ArrayList<AST.attr> paList;

    // Constructor for the class
    public BasicClassBlockCG(String className, String prName, HashMap<String, AST.attr> alist, HashMap<String, AST.method> mlist,
                           HashMap<String, Integer> aOffset, HashMap<String, Integer> mOffset, ArrayList<AST.attr> parentAttrList,
                           ArrayList<AST.method> parentMthdList, HashMap<String, String> llIrNameList) {
        name = className;
        parentName = prName;
        attrList = new HashMap<String, AST.attr>();
        attrList.putAll(alist);
        mthdList = new HashMap<String, AST.method>();
        mthdList.putAll(mlist);

        attrOffset = new HashMap<String, Integer>();
        attrOffset.putAll(aOffset);

        mthdOffset = new HashMap<String, Integer>();
        mthdOffset.putAll(mOffset);

        paList = new ArrayList<AST.attr>();
        paList.addAll(parentAttrList);

        pmList = new ArrayList<AST.method>();
        pmList.addAll(parentMthdList);

        llvmIrName = new HashMap<String, String>();
        llvmIrName.putAll(llIrNameList);
    }
    
}