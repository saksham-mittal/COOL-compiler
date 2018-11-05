package cool;

import java.util.*;
import java.io.PrintWriter;

public class ConstValClass extends OpClass {
    public String value;
    ConstValClass(TypeMapping t, String val) {
        value = val;
        type = t;
        opName = val;
    }
}