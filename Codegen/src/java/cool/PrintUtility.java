package cool;

import java.util.*;
import java.io.PrintWriter;

public class PrintUtility {
    void escapedString(PrintWriter out, String str) {
        for(int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\\') {
                out.print("\\5C");
            } else if (str.charAt(i) == '\"') {
                out.print("\\22");
            } else if (str.charAt(i) == '\n') {
                out.print("\\0A");
            } else if (str.charAt(i) == '\t') {
                out.print("\\09");
            } else {
                out.print(str.charAt(i));
            }
        }   
    }
    
    void initConstant(PrintWriter out, String name, ConstValue op) {
        out.print("@" + name + " = ");
        out.print("constant " + op.type.name + " ");
        if (op.type.id == TypeMapping.TypeID.INT8) {
            out.print("c\"");
            escapedString(out, op.value);
            out.print("\\00\"\n");
        } else {
            out.print(op.value + "\n");
        }
    }
    
    void define(PrintWriter out, TypeMapping retType, String name, List<OpClass> args) {
        out.print("\ndefine " + retType.name + " @" + name + "( ");
        for(int i = 0; i < args.size(); i++) {
            if (i != args.size() - 1) {
                out.print(args.get(i).type.name + " " + args.get(i).opName + ", ");
            } else {
                out.print(args.get(i).type.name + " " + args.get(i).opName + "");
            }
        }
        out.println(" ) {\nentry:");
    }
    
    void declare(PrintWriter out, TypeMapping retType, String name, List<TypeMapping> args) {
        out.print("declare " + retType.name + " @" + name + "( ");
        for(int i = 0; i < args.size(); i++) {
            if (i != args.size() - 1) {
                out.print(args.get(i).name + ", ");
            } else {
                out.print(args.get(i).name + "");
            }
        }
        out.print(" )\n");
    }
    
    void typeDefine(PrintWriter out, String className, List<TypeMapping> attributes) {
        out.print("%class." + className + " = type { ");
        for(int i = 0; i < attributes.size(); i++) {
            if (i != attributes.size() - 1) { 
                out.print(attributes.get(i).name + ", ");
            } else {
                out.print(attributes.get(i).name + "");
            }
        }
        out.print(" }\n");
    }
    
    void arithOp(PrintWriter out, String operation, OpClass op1, OpClass op2, OpClass result) {
        out.print("\t");
        if (result.type.id != TypeMapping.TypeID.VOID) {
            out.print(result.opName + " = ");
        }
        out.print(operation + " " + op1.type.name + " " + op1.opName + ", "  + op2.opName + "\n");
    }
    
    void allocaOp(PrintWriter out, TypeMapping type, OpClass result) {
        out.print("\t" + result.opName + " = alloca " + type.name + "\n");
    }
    
    void loadOp(PrintWriter out, TypeMapping type, OpClass op, OpClass result) {
        out.print("\t" + result.opName + " = load " + type.name + ", " + op.type.name + " "
            + op.opName + "\n");
    }
    
    void storeOp(PrintWriter out, OpClass op, OpClass result) {
        out.print("\tstore " + op.type.name + " " + op.opName + ", " + result.type.name + " "
            + result.opName + "\n");
    }
    
    void getElementPtr(PrintWriter out, TypeMapping type, List<OpClass> operandList, OpClass result, boolean inbounds) {
        out.print("\t");
        if (result.type.id != TypeMapping.TypeID.VOID) {
            out.print(result.opName + " = ");
        }
        out.print("getelementptr ");
        if (inbounds == true) {
            out.print("inbounds ");
        } 
        out.print(type.name + ", ");
        for(int i = 0; i < operandList.size(); i++) {    
            if (i != operandList.size() - 1) {
                out.print(operandList.get(i).type.name + " " + operandList.get(i).opName + ", ");
            } else {
                out.print(operandList.get(i).type.name + " " + operandList.get(i).opName + "\n");
            }
        }
    }
    
    void getElementPtrEmbed(PrintWriter out, TypeMapping type, OpClass op1, OpClass op2, OpClass op3, boolean inbounds) {
        out.print("\tgetelementptr ");
        if (inbounds == true) {    
            out.print("inbounds ");
        }
        out.print("( " + type.name + ", " + op1.type.name + "* " + op1.opName + ", "
            + op2.type.name + " " + op2.opName + ", " + op3.type.name + " " + op3.opName + ")\n");
    }
    
    void branchCondOp(PrintWriter out, OpClass op, String labelTrue, String labelFalse) {
        out.print("\tbr " + op.type.name + " " + op.opName + ", label %" + labelTrue
            + ", label %" + labelFalse + "\n");
    }
    
    void branchUncondOp(PrintWriter out, String label) {
        out.print("\tbr label %" + label + "\n");
    }
    
    void compareOp(PrintWriter out, String cond, OpClass op1, OpClass op2, OpClass result) {
        out.print("\t" + result.opName + " = icmp ");
        if (cond.equals("EQ")) {
            out.print("eq ");
        } else if (cond.equals("LT")) {
            out.print("slt ");
        } else if (cond.equals("LE")) {
            out.print("sle ");
        }
        out.print(op1.type.name + " " + op1.opName + ", " + op2.opName + "\n");
    }
    
    void callOp(PrintWriter out, List<TypeMapping> argTypes, String funcName, boolean isGlobal, List<OpClass> args, OpClass resultOp) {
        out.print("\t");
        if (resultOp.type.id != TypeMapping.TypeID.VOID) {
            out.print(resultOp.opName + " = ");
        }
        out.print("call " + resultOp.type.name);
        if (argTypes.size() > 0) {
            out.print(" (");
        for (int i = 0; i < argTypes.size(); i++) {
            if (i != argTypes.size() - 1) {
                out.print(argTypes.get(i).name + ", ");
            } else {
                out.print(argTypes.get(i).name + ") ");
            }
        }
        }
        if (isGlobal == true) {
            out.print(" @");
        } else {
            out.print(" %");
        } 
        out.print(funcName + "( ");
        for (int i = 0; i < args.size(); i++) {
            if (i != args.size() - 1) {      
                out.print(args.get(i).type.name + " " + args.get(i).opName + ", ");
            } else {
                out.print(args.get(i).type.name + " " + args.get(i).opName + "");
            }
        }
        out.print(" )\n");
    }
    
    void retOp(PrintWriter out, OpClass op) {
        out.print("\tret ");
        if (op.type.id != TypeMapping.TypeID.VOID) {
            out.print(op.type.name + " " + op.opName + "\n");
        } else {
            out.print("void\n");
        }  
        out.print("}\n");
    }
}