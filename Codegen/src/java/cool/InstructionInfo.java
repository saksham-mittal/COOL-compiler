package cool;

import java.util.*;
import java.io.PrintWriter;

public class InstructionInfo {
    public int registerVal;
    TypeMapping lastInstructionType;
    String lastBasicBlockName;

    public InstructionInfo() {
        registerVal = 0;
        lastInstructionType = new TypeMapping(TypeMapping.TypeID.EMPTY);
        lastBasicBlockName = "";
    }

    public InstructionInfo(int regVal, TypeMapping operand, String lbType) {
        registerVal = regVal;
        lastInstructionType = operand;
        lastBasicBlockName = lbType;
    }
}