package cool;

import java.util.*;
import java.io.PrintWriter;

/* 
    Utility class for representing operands of type 'Bool' inheriting from ConstValClass
    It contains a boolean value as member to store the boolean value of the operand
*/
public class BoolValClass extends ConstValClass {
    public boolean bValParam;
    BoolValClass(boolean bTemp) {
        super(new TypeMapping(TypeMapping.TypeID.INT1), "");
        bValParam = bTemp;
        if(bTemp)
            constValue = "true";
        else
            constValue = "false";
        opName = constValue;
    }
}