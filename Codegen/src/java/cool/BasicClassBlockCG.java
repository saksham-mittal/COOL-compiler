package cool;

import java.util.*;
import java.io.PrintWriter;

public class BasicClassBlockCG {
    public String name;
    public String parent;
    public List<AST.attr> attrList = new ArrayList<AST.attr>();
    public List<AST.method> mthdList = new ArrayList<AST.method>();

    public BasicClassBlockCG(String clName, String parentName, List<AST.attr> aList, List<AST.method> mList) {
        name = clName;
        parent = parentName;
        attrList = aList;
        mthdList = mList;
    }
}