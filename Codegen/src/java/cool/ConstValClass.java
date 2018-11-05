package cool;

import java.util.*;
import java.io.PrintWriter;

/* 
    This class is a superclass for IntValClass and BoolValClass  
*/
public class ConstValClass extends OpClass {
    public String constValue;
    ConstValClass(TypeMapping t, String valTemp) {
        constValue = valTemp;
        type = t;
        opName = valTemp;
    }
}