package cool;

import java.util.*;
import java.io.PrintWriter;

/* 
    This is a helper class for storing operand information. 
    Its constructor takes an object of class TypeMapping and a string and assigns it to Operand's type and name 
*/
public class OpClass {
    public TypeMapping type;
    public String opName;
    OpClass() {
        type = new TypeMapping(TypeMapping.TypeID.EMPTY);
        opName = "";
    }
    OpClass(TypeMapping t, String n) {
        type = t;
        opName = "%" + n;
    }
}
