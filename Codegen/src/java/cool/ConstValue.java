package cool;

import java.util.*;
import java.io.PrintWriter;

public class ConstValue extends OpClass {
    public String value;
    ConstValue(TypeMapping t, String val) {
        value = val;
        type = t;
        opName = val;
    }
}