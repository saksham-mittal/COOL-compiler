package cool;

import java.util.*;
import java.io.PrintWriter;

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
