package cool;

import java.util.*;
import java.io.PrintWriter;

public class GlobalValue extends OpClass {
    public OpClass value;
    GlobalValue(TypeMapping t, String n, OpClass v) {
        type = t;
        opName = "@" + n;
        value = v;
    }
    GlobalValue(TypeMapping t, String n) {
        type = t;
        opName = "@" + n;
    }
}