package cool;

import java.util.*;
import java.io.PrintWriter;

public class BoolValue extends ConstValue {
    public boolean bValue;
    BoolValue(boolean b) {
        super(new TypeMapping(TypeMapping.TypeID.INT1), "");
        bValue = b;
        if(b)
            value = "true";
        else
            value = "false";
        opName = value;
    }
}