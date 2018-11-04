package cool;

import java.util.*;
import java.io.PrintWriter;

public class IntValue extends ConstValue {
    public int iValue;
    IntValue(int i) {
        super(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(i));
        iValue = i;
    }
}