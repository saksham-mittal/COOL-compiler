package cool;

import java.util.*;
import java.io.PrintWriter;

/* 
    Utility class for representing operands of type 'Int' 
    It contains a Integer value as member to store the integer value of the operand
*/
public class IntValClass extends ConstValClass {
    public int intValParam;
    IntValClass(int i) {
        super(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(i));
        intValParam = i;
    }
}