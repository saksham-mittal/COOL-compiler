package cool;

import java.util.*;
import java.io.PrintWriter;

public class IntValClass extends ConstValClass {
    public int intValParam;
    IntValClass(int i) {
        super(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(i));
        intValParam = i;
    }
}