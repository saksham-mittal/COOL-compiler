package cool;

import java.util.*;
import java.io.PrintWriter;

/* 
    This is a helper class containing methods to print the various possible llvm-IR instructions.
    Every method prints an IR line for corresponding operations happening in the IR.
    This prevents us from writing code for various operations again and again and helps implement code reusability. 
*/

public class PrintUtility {

    /* 
        Generates IR instruction for return operation in a method.
    */
    void returnInstUtil(PrintWriter out, OpClass op, String nameVar) {
        out.print("\tret ");
        if (op.type.id == TypeMapping.TypeID.VOID) {
            out.print("void\n");
        } else {
            out.print(op.type.name + " " + op.opName + "\n");
        }  
        out.print("}\n");
    }
    
    /* 
        Generates IR instruction for alloca operation for a variable.
    */
    void allocaInstUtil(PrintWriter out, TypeMapping type, OpClass result, String nameVar) {
        out.print("\t" + result.opName + " = alloca " + type.name + "\n");
    }

    /* 
        Generates IR instruction for load operation for a variable.
    */
    void loadInstUtil(PrintWriter out, TypeMapping type, OpClass op, OpClass result, String nameVar) {
        out.print("\t" + result.opName + " = load " + type.name + ", " + op.type.name + " " + op.opName + "\n");
    }
    
    void storeInstUtil(PrintWriter out, OpClass op, OpClass result, String nameVar) {
        out.print("\tstore " + op.type.name + " " + op.opName + ", " + result.type.name + " " + result.opName + "\n");
    }
    
    void stringEscUtil(PrintWriter out, String str, String nameVar) {
        int i = 0;
        while(i < str.length()) {
            char chr = str.charAt(i);
            switch(chr) {
                case '\\':
                    out.print("\\5C");
                    break;
                case '\"':
                    out.print("\\22");
                    break;
                case '\n':
                    out.print("\\0A");
                    break;
                case '\t':
                    out.print("\\09");
                    break;
                default: 
                    out.print(chr);
            }
            i++;
        }   
    }
    
    void generateDefUtil(PrintWriter out, TypeMapping retType, String name, List<OpClass> args, String nameVar) {
        out.print("\ndefine " + retType.name + " @" + name + "( ");
        int i = 0; 
        while(i < args.size()) {
            if (i == args.size() - 1) {
                out.print(args.get(i).type.name + " " + args.get(i).opName);
            } else {
                out.print(args.get(i).type.name + " " + args.get(i).opName + ", ");
            }
            i++;
        }
        out.println(" ) {\nentry:");
    }
    
    void generateDeclUtil(PrintWriter out, TypeMapping retType, String name, List<TypeMapping> args, String nameVar) {
        out.print("declare " + retType.name + " @" + name + "( ");
        int i = 0;
        while( i < args.size()) {
            if (i == args.size() - 1) {
                out.print(args.get(i).name);
            } else {
                out.print(args.get(i).name + ", ");
            }
            i++;
        }
        out.print(" )\n");
    }
    
    void arithemeticUtil(PrintWriter out, String operation, OpClass op1, OpClass op2, OpClass result, String nameVar) {
        out.print("\t");
        if (result.type.id == TypeMapping.TypeID.VOID) {
            out.print(operation + " " + op1.type.name + " " + op1.opName + ", "  + op2.opName + "\n");
        } else {
            out.print(result.opName + " = ");
            out.print(operation + " " + op1.type.name + " " + op1.opName + ", "  + op2.opName + "\n");
        }
    }
    
    void cmpInstUtil(PrintWriter out, String cond, OpClass op1, OpClass op2, OpClass result, String nameVar) {
        out.print("\t" + result.opName + " = icmp ");
        switch(cond) {
            case "EQ":
                out.print("eq ");
                break;
            case "LT":
                out.print("slt ");
                break;
            case "LE":
                out.print("sle ");
                break;
        }
        out.print(op1.type.name + " " + op1.opName + ", " + op2.opName + "\n");
    }
    
    void getElemPtrInstUtil(PrintWriter out, TypeMapping type, List<OpClass> operandList, OpClass result, boolean inbounds, String nameVar) {
        out.print("\t");
        if (result.type.id == TypeMapping.TypeID.VOID) {
            out.print("getelementptr ");
        } else {
            out.print(result.opName + " = ");
            out.print("getelementptr ");
        }
        if (inbounds != true) {}
        else
            out.print("inbounds ");
        out.print(type.name + ", ");
        for(int i = 0; i < operandList.size(); i++) {    
            if (i == operandList.size() - 1) {
                out.print(operandList.get(i).type.name + " " + operandList.get(i).opName + "\n");
            } else {
                out.print(operandList.get(i).type.name + " " + operandList.get(i).opName + ", ");
            }
        }
    }
    
    void brConditionUtil(PrintWriter out, OpClass op, String labelTrue, String labelFalse, String nameVar) {
        out.print("\tbr " + op.type.name + " " + op.opName + ", label %" + labelTrue + ", label %" + labelFalse + "\n");
    }
    
    void brUncoditionUtil(PrintWriter out, String label, String nameVar) {
        out.print("\tbr label %" + label + "\n\n");
    }
    
    void callInstUtil(PrintWriter out, List<TypeMapping> argTypes, String funcName, boolean isGlobal, List<OpClass> args, OpClass resultOp, String nameVar) {
        out.print("\t");
        if (resultOp.type.id == TypeMapping.TypeID.VOID) {
            out.print("call " + resultOp.type.name);
        } else 
        out.print(resultOp.opName + " = call " + resultOp.type.name);
        int sz = argTypes.size();
        if ( sz > 0) {
            out.print(" (");
            int i = 0;
            while(i < sz) {
                if (i == sz - 1) {
                    out.print(argTypes.get(i).name + ") ");
                } else {
                    out.print(argTypes.get(i).name + ", ");
                }
                i++;
            }
        }
        if (isGlobal != true) {
            out.print(" %");
        } else {
            out.print(" @");
        } 
        out.print(funcName + "( ");
        int i = 0;
        while(i < args.size()) {
            if (i == args.size() - 1) {      
                out.print(args.get(i).type.name + " " + args.get(i).opName);
            } else {
                out.print(args.get(i).type.name + " " + args.get(i).opName + ", ");
            }
            i++;
        }
        out.print(" )\n");
    }
    
    void classTypeUtil(PrintWriter out, String className, List<TypeMapping> attributes, String nameVar) {
        out.print("%class." + className + " = type { ");
        int i = 0;
        while(i < attributes.size()) {
            if (i == attributes.size() - 1) { 
                out.print(attributes.get(i).name);
            } else {
                out.print(attributes.get(i).name + ", ");
            }
            i++;
        }
        out.print(" }\n");
    }
}