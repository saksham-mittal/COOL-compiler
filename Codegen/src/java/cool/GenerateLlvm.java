package cool;

import java.util.*;
import java.io.PrintWriter;

public class GenerateLlvm {
    public List<TypeMapping> paramsList;

    public InstructionInfo registerCounter;

    public static TypeMapping mthdType;

    public static Integer nestedIfCount;

    public static Integer nestedLoopCount;

    public VisitNodeClass visitNodeObject;

    public GenerateLlvm() {
        paramsList = new ArrayList<TypeMapping>();

        // This is the object for VisitNodeClass
        visitNodeObject = new VisitNodeClass();
    }

    public void declareStandardCFunctions(PrintUtility printUtil, PrintWriter out) {
        /*
            Error handling
        */
        out.println("@divby0err = private unnamed_addr constant [31 x i8] c\"Runtime Error: Divide by Zero\\0A\\00\"");
        out.println("@staticdispatchonvoiderr = private unnamed_addr constant [47 x i8] c\"Runtime Error: Static Dispatch on void object\\0A\\00\"\n");
        
        /*
            C format specifiers
        */
        out.println("@strfmt = private unnamed_addr constant [3 x i8] c\"%s\\00\"");
        out.println("@intfmt = private unnamed_addr constant [3 x i8] c\"%d\\00\"");
        out.println("@.str.empty = private unnamed_addr constant [1 x i8] c\"\\00\"\n");
        
        /*
            C String Methods
        */
        // String Concatenation method
        paramsList.add(new TypeMapping(TypeMapping.TypeID.INT8PTR));
        paramsList.add(new TypeMapping(TypeMapping.TypeID.INT8PTR));
        printUtil.generateDeclUtil(out, new TypeMapping(TypeMapping.TypeID.INT8PTR), "strcat", paramsList, null);
        
        // String Copy method
        printUtil.generateDeclUtil(out, new TypeMapping(TypeMapping.TypeID.INT8PTR), "strcpy", paramsList, null);
        
        // String Compare method
        printUtil.generateDeclUtil(out, new TypeMapping(TypeMapping.TypeID.INT32), "strcmp", paramsList, null);
        
        // String n Copy method
        paramsList.add(new TypeMapping(TypeMapping.TypeID.INT32));
        printUtil.generateDeclUtil(out, new TypeMapping(TypeMapping.TypeID.INT8PTR), "strncpy", paramsList, null);
        
        // String Length method
        paramsList = new ArrayList<TypeMapping>();
        paramsList.add(new TypeMapping(TypeMapping.TypeID.INT8PTR));
        printUtil.generateDeclUtil(out, new TypeMapping(TypeMapping.TypeID.INT32), "strlen", paramsList, null);


        /*
            C IO Methods
        */
        paramsList = new ArrayList<TypeMapping>();
        paramsList.add(new TypeMapping(TypeMapping.TypeID.INT8PTR));
        paramsList.add(new TypeMapping(TypeMapping.TypeID.VARARG));

        // printf method
        printUtil.generateDeclUtil(out, new TypeMapping(TypeMapping.TypeID.INT32), "printf", paramsList, null);

        // scanf method
        printUtil.generateDeclUtil(out, new TypeMapping(TypeMapping.TypeID.INT32), "scanf", paramsList, null);

        /*
            C malloc and exit methods
        */
        paramsList = new ArrayList<TypeMapping>();
        paramsList.add(new TypeMapping(TypeMapping.TypeID.INT32));
        printUtil.generateDeclUtil(out, new TypeMapping(TypeMapping.TypeID.INT8PTR), "malloc", paramsList, null);
        printUtil.generateDeclUtil(out, new TypeMapping(TypeMapping.TypeID.VOID), "exit", paramsList, null);
        out.print("\n");
    }
    
    // Method to generate ir for string methods
    public void generateStringMethods(PrintWriter out, PrintUtility printUtil, String functionName) {
        String mangledFunctionName = "String_" + functionName;
        OpClass retValue = null;
        List<OpClass> args = null;

        // Generating code for length method
        if (functionName.equals("length")) {
            retValue = new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), "retval");
            args = new ArrayList<OpClass>();
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "this"));
            printUtil.generateDefUtil(out, retValue.type, mangledFunctionName, args, null);
            printUtil.callInstUtil(out, new ArrayList<TypeMapping>(), "strlen", true, args, retValue, null);
            printUtil.returnInstUtil(out, retValue, null);
        } else if (functionName.equals("concat")) {
            // Generating code for concat method
            retValue = new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "retval");
            args = new ArrayList<OpClass>();
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "this"));
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "that"));
            printUtil.generateDefUtil(out, retValue.type, mangledFunctionName, args, null);

            retValue = new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "memnew");
            args = new ArrayList<OpClass>();
            args.add((OpClass)new IntValClass(1024));
            printUtil.callInstUtil(out, new ArrayList<TypeMapping>(), "malloc", true, args, retValue, null);

            retValue = new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "copystring");
            args = new ArrayList<OpClass>();
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "memnew"));
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "this"));
            printUtil.callInstUtil(out, new ArrayList<TypeMapping>(), "strcpy", true, args, retValue, null);

            retValue = new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "retval");
            args = new ArrayList<OpClass>();
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "copystring"));
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "that"));
            printUtil.callInstUtil(out, new ArrayList<TypeMapping>(), "strcat", true, args, retValue, null);

            printUtil.returnInstUtil(out, retValue, null);
        } else if (functionName.equals("substr")) {
            // Generating code for substr method
            retValue = new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "retval");
            args = new ArrayList<OpClass>();
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "this"));
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), "start"));
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), "len"));
            printUtil.generateDefUtil(out, retValue.type, mangledFunctionName, args, null);

            retValue = new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "0");
            args = new ArrayList<OpClass>();
            args.add((OpClass)new IntValClass(1024));
            printUtil.callInstUtil(out, new ArrayList<TypeMapping>(), "malloc", true, args, retValue, null);

            retValue = new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "1");
            args = new ArrayList<OpClass>();
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "this"));
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), "start"));
            printUtil.getElemPtrInstUtil(out, new TypeMapping(TypeMapping.TypeID.INT8), args, retValue, true, null);

            retValue = new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "2");
            args = new ArrayList<OpClass>();
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "0"));
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "1"));
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), "len"));
            printUtil.callInstUtil(out, new ArrayList<TypeMapping>(), "strncpy", true, args, retValue, null);
            out.println("\t%3 = getelementptr inbounds [1 x i8], [1 x i8]* @.str.empty, i32 0, i32 0");
            out.println("\t%retval = call i8* @strcat( i8* %2, i8* %3 )");
            out.println("\tret i8* %retval\n}");
        } else if (functionName.equals("strcmp")) {
            // Generating code for strcmp method
            retValue = new OpClass(new TypeMapping(TypeMapping.TypeID.INT1), "retval");
            args = new ArrayList<OpClass>();
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "this"));
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "start"));
            printUtil.generateDefUtil(out, retValue.type, mangledFunctionName, args, null);

            retValue = new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), "0");
            args = new ArrayList<OpClass>();
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "this"));
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "start"));
            printUtil.callInstUtil(out, new ArrayList<TypeMapping>(), "strcmp", true, args, retValue, null);

            printUtil.cmpInstUtil(out, "EQ", retValue, (OpClass)new IntValClass(0), new OpClass(new TypeMapping(TypeMapping.TypeID.INT1), "1"), null);

            printUtil.returnInstUtil(out, new OpClass(new TypeMapping(TypeMapping.TypeID.INT1), "1"), null);
        }
    }


    public void generateObjectMethods(PrintWriter out, PrintUtility printUtil, String functionName) {
        String mangledFunctionName = "Object_" + functionName;
        OpClass retValue = null;
        List<OpClass> args = null;

        // Method for generating the abort method
        if (functionName.equals("abort")) {
            retValue = new OpClass(new TypeMapping(TypeMapping.TypeID.VOID), "null");
            args = new ArrayList<OpClass>();
            printUtil.generateDefUtil(out, retValue.type, mangledFunctionName, args, null);

            out.println("\tcall void (i32) @exit(i32 0)");
            out.println("\tret void\n}\n");
        }
        // Add other object methods later (If time permits)
    }

    public void generateIOMethods(PrintWriter out, PrintUtility printUtil, String functionName) {
        String mangledFunctionName = "IO_" + functionName;
        OpClass retValue = null;
        List<OpClass> args = null;

        if (functionName.equals("out_string")) {
            // Method for generating the out_string method
            args = new ArrayList<OpClass>();
            retValue = new OpClass(new TypeMapping(TypeMapping.TypeID.VOID), "null");
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "given"));
            printUtil.generateDefUtil(out, retValue.type, mangledFunctionName, args, null);

            out.println("\t%0 = getelementptr inbounds [3 x i8], [3 x i8]* @strfmt, i32 0, i32 0");
            out.println("\t%call = call i32 ( i8*, ... ) @printf(i8* %0, i8* %given)");
            out.println("\tret void\n}\n");
        } else if (functionName.equals("out_int")) {
            // Method for generating the out_int method
            args = new ArrayList<OpClass>();
            retValue = new OpClass(new TypeMapping(TypeMapping.TypeID.VOID), "null");
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), "given"));
            printUtil.generateDefUtil(out, retValue.type, mangledFunctionName, args, null);

            out.println("\t%0 = getelementptr inbounds [3 x i8], [3 x i8]* @intfmt, i32 0, i32 0");
            out.println("\t%call = call i32 ( i8*, ... ) @printf(i8* %0, i32 %given)");
            out.println("\tret void\n}\n");
        } else if (functionName.equals("in_string")) {
            // Method for generating the in_string method
            args = new ArrayList<OpClass>();
            printUtil.generateDefUtil(out, new TypeMapping(TypeMapping.TypeID.INT8PTR), mangledFunctionName, args, null);

            out.println("\t%0 = bitcast [3 x i8]* @strfmt to i8*");

            args = new ArrayList<OpClass>();
            retValue = new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "retval");
            args.add((OpClass)new IntValClass(1024));
            printUtil.callInstUtil(out, new ArrayList<TypeMapping>(), "malloc", true, args, retValue, null);

            args = new ArrayList<OpClass>();
            retValue = new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), "1");
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "0"));
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "retval"));
            List<TypeMapping> argTypes = new ArrayList<TypeMapping>();
            argTypes.add(new TypeMapping(TypeMapping.TypeID.INT8PTR));
            argTypes.add(new TypeMapping(TypeMapping.TypeID.VARARG));
            printUtil.callInstUtil(out, argTypes, "scanf", true, args, retValue, null);
            printUtil.returnInstUtil(out, args.get(1), null);
        } else if (functionName.equals("in_int")) {
            // Method for generating the in_int method
            args = new ArrayList<OpClass>();
            printUtil.generateDefUtil(out, new TypeMapping(TypeMapping.TypeID.INT32), mangledFunctionName, args, null);

            out.println("\t%0 = bitcast [3 x i8]* @intfmt to i8*");

            args = new ArrayList<OpClass>();
            retValue = new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "1");
            args.add((OpClass)new IntValClass(4));
            printUtil.callInstUtil(out, new ArrayList<TypeMapping>(), "malloc", true, args, retValue, null);

            out.println("\t%2 = bitcast i8* %1 to i32*");

            args = new ArrayList<OpClass>();
            retValue = new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), "3");
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "0"));
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT32PTR), "2"));
            List<TypeMapping> argTypes = new ArrayList<TypeMapping>();
            argTypes.add(new TypeMapping(TypeMapping.TypeID.INT8PTR));
            argTypes.add(new TypeMapping(TypeMapping.TypeID.VARARG));
            printUtil.callInstUtil(out, argTypes, "scanf", true, args, retValue, null);

            retValue = new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), "retval");
            printUtil.loadInstUtil(out, new TypeMapping(TypeMapping.TypeID.INT32), args.get(1), retValue, null);
            printUtil.returnInstUtil(out, retValue, null);
        }
    }

    // This method returns the corresponding Typematching object corresponding to the objectTypeID
    public TypeMapping operandType(String objectTypeID, boolean operandIsClass, int numPointers) {
        String tempObjTypId = objectTypeID;

        switch(tempObjTypId) {
            case "void":
                return new TypeMapping(TypeMapping.TypeID.VOID);
        }

        if(operandIsClass) {
            switch(tempObjTypId) {
                case "String":
                    return new TypeMapping(TypeMapping.TypeID.INT8PTR);
                case "Int":
                    return new TypeMapping(TypeMapping.TypeID.INT32);
                case "Bool":
                    return new TypeMapping(TypeMapping.TypeID.INT1);
                default:
                    return new TypeMapping("class." + objectTypeID, numPointers);
            }
        }

        return new TypeMapping(objectTypeID, numPointers);
    }

    public void generateConstructorOfClass(PrintWriter out, PrintUtility printUtil, String clsName, InstructionInfo track, AST.class_ cl, ClassInfoCG clsInfoCG, List<String> functionFormalNameList) {

        // Name of constructor (mangled)
        String mthdName = clsName + "_Cons_" + clsName;
    
        // List of OpClass for attributes
        List<OpClass> attrOperandList = new ArrayList<OpClass>();
        attrOperandList.add(new OpClass(operandType(clsName, true, 1), "this"));
    
        // Define the constructor and establish pointer information
        printUtil.generateDefUtil(out, operandType(clsName, true, 1), mthdName, attrOperandList, null);
        printUtil.allocaInstUtil(out, operandType(clsName, true, 1), new OpClass(operandType(clsName, true, 1), "this.addr"), null);
        
        // Performing load and store operations for constructors
        printUtil.storeInstUtil(out, new OpClass(operandType(clsName, true, 1), "this"), new OpClass(operandType(clsName, true, 2), "this" + ".addr"), null);
        printUtil.loadInstUtil(out, operandType(clsName, true, 1), new OpClass(operandType(clsName, true, 2), "this" + ".addr"), new OpClass(operandType(clsName, true, 1), "this1"), null);
    
        List<AST.attr> attrListTemp = clsInfoCG.cls.get(clsName).attrList;
        int i = 0;
        while(i < attrListTemp.size()) {
            AST.attr attrTemp = attrListTemp.get(i);
            OpClass res = new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), attrTemp.name);
            List<OpClass> operandList = new ArrayList<OpClass>();
            operandList.add(new OpClass(operandType(clsName, true, 1), "this1"));
        
            if (attrTemp.typeid.equals("Bool") == true) {
                // Bool attribute codegen
                operandList.add((OpClass)new IntValClass(0));
                operandList.add((OpClass)new IntValClass(i));
                printUtil.getElemPtrInstUtil(out, operandType(clsName, true, 0), operandList, res, true, null);
                TypeMapping ptr = new TypeMapping(TypeMapping.TypeID.INT1PTR);
                if (attrTemp.value.getClass() != AST.no_expr.class && attrTemp.value.getClass() != AST.new_.class) {
                    track = visitNodeObject.VisitorPattern(out, printUtil, attrTemp.value, track, clsInfoCG, cl, functionFormalNameList);
                    printUtil.storeInstUtil(out, new OpClass(track.lastInstructionType, String.valueOf(track.registerVal - 1)), new OpClass(track.lastInstructionType.correspondingPtrType(), attrTemp.name), null);
                } else {
                    printUtil.storeInstUtil(out, (OpClass)new BoolValClass(false), new OpClass(ptr, attrTemp.name), null);
                }
            } else if (attrTemp.typeid.equals("String") == true) {
                // String attribute codegen
                operandList.add((OpClass)new IntValClass(0));
                operandList.add((OpClass)new IntValClass(i));
                printUtil.getElemPtrInstUtil(out, operandType(clsName, true, 0), operandList, res, true, null);
                String lenString = null;
                if (attrTemp.value.getClass() != AST.no_expr.class && attrTemp.value.getClass() != AST.new_.class) {
                    track = visitNodeObject.VisitorPattern(out, printUtil, attrTemp.value, track, clsInfoCG, cl, functionFormalNameList);
                    printUtil.storeInstUtil(out, new OpClass(track.lastInstructionType, String.valueOf(track.registerVal - 1)), new OpClass(track.lastInstructionType.correspondingPtrType(), attrTemp.name), null);
                } else {
                    lenString = "[" + 1 + " x i8]";
                    out.println("\tstore i8* getelementptr inbounds (" + lenString + ", " + lenString + "* @.str.empty , i32 0, i32 0), i8** %" + attrTemp.name);
                }
            } else if (attrTemp.typeid.equals("Int") == true) {
                // Int attribute codegen
                operandList.add((OpClass)new IntValClass(0));
                operandList.add((OpClass)new IntValClass(i));
                printUtil.getElemPtrInstUtil(out, operandType(clsName, true, 0), operandList, res, true, null);
                TypeMapping ptr = new TypeMapping(TypeMapping.TypeID.INT32PTR);
                if (attrTemp.value.getClass() != AST.no_expr.class && attrTemp.value.getClass() != AST.new_.class) {
                    track = visitNodeObject.VisitorPattern(out, printUtil, attrTemp.value, track, clsInfoCG, cl, functionFormalNameList);
                    printUtil.storeInstUtil(out, new OpClass(track.lastInstructionType, String.valueOf(track.registerVal - 1)), new OpClass(track.lastInstructionType.correspondingPtrType(), attrTemp.name), null);
                } else {
                    printUtil.storeInstUtil(out, (OpClass)new IntValClass(0), new OpClass(ptr, attrTemp.name), null);
                }
            } else {
                // other cases
                operandList.add((OpClass)new IntValClass(0));
                operandList.add((OpClass)new IntValClass(i));
                printUtil.getElemPtrInstUtil(out, operandType(clsName, true, 0), operandList, res, true, null);
                TypeMapping ptr = operandType(clsName, true, 1);
                if ((attrTemp.value.getClass() != AST.no_expr.class)) {
                    track = visitNodeObject.VisitorPattern(out, printUtil, attrTemp.value, track, clsInfoCG, cl, functionFormalNameList);
                    printUtil.storeInstUtil(out, new OpClass(operandType(attrTemp.typeid, true, 1), String.valueOf(track.registerVal - 1)), new OpClass(operandType(attrTemp.typeid, true, 1).correspondingPtrType(), attrTemp.name), null);
                } else {
                    out.println("\tstore " + operandType(attrTemp.typeid, true, 1).name + " null , " + operandType(attrTemp.typeid, true, 1).name + "* %" + attrTemp.name);
                }
            }
            i++;
        }
        printUtil.returnInstUtil(out, new OpClass(operandType(clsName, true, 1), "this1"), null);
    }

    // This method finds the string constants and generates llvm ir for them
    public void stringProcess(PrintWriter out, PrintUtility printUtil, AST.expression expr, Integer strLine) {
        if (expr.getClass() == AST.string_const.class) {
            // If expr is of string_const class, then generate
            AST.string_const exprStr = (AST.string_const)expr;
            String tempString = exprStr.value;
            Codegen.stringToLineNoMapping.put(tempString, Codegen.stringLineNo);
            Codegen.stringLineNo++;
            out.print("@.str." + Codegen.stringToLineNoMapping.get(tempString) + " = private unnamed_addr constant [" + String.valueOf(tempString.length() + 1) + " x i8] c\"");
            printUtil.stringEscUtil(out, tempString, null);
            out.println("\\00\"");
        } else if (expr.getClass() == AST.eq.class) {
            // Traverse on both sides of equality
            stringProcess(out, printUtil, ((AST.eq)expr).e1, 0);
            stringProcess(out, printUtil, ((AST.eq)expr).e2, 0);
        } else if (expr.getClass() == AST.assign.class) {
            // Traverse on assign expression
            stringProcess(out, printUtil, ((AST.assign)expr).e1, 0);
        } else if (expr.getClass() == AST.block.class) {
            for (AST.expression e : ((AST.block)expr).l1) {
                stringProcess(out, printUtil, e, 0);
            }
        } else if (expr.getClass() == AST.loop.class) {
            // Traverse on loop.body and loop.predicate expression
            stringProcess(out, printUtil, ((AST.loop)expr).predicate, 0);
            stringProcess(out, printUtil, ((AST.loop)expr).body, 0);
        } else if (expr.getClass() == AST.cond.class) {
            // Traverse on cond.ifbody, cond.elsebody and cond.predicate expression
            stringProcess(out, printUtil, ((AST.cond)expr).predicate, 0);
            stringProcess(out, printUtil, ((AST.cond)expr).ifbody, 0);
            stringProcess(out, printUtil, ((AST.cond)expr).elsebody, 0);
        } else if (expr.getClass() == AST.static_dispatch.class) {
            // Traverse on static_dispatch.expr
            stringProcess(out, printUtil, ((AST.static_dispatch)expr).caller, 0);
            for (AST.expression e : ((AST.static_dispatch)expr).actuals) {
                stringProcess(out, printUtil, e, 0);
            }
        }
    }

    public void allocateMethodAttributes(PrintWriter out, PrintUtility printUtil, List<OpClass> aList) {
        // alloca for 'this' operand of method
        printUtil.allocaInstUtil(out, aList.get(0).type, new OpClass(aList.get(0).type, "this.addr"), null);

        // Calling alloca instruction on all method attributes
        int i = 1;
        while(i < aList.size()) {
            printUtil.allocaInstUtil(out, aList.get(i).type, new OpClass(aList.get(i).type, aList.get(i).opName.substring(1) + ".addr"), null);
            i++;
        }

        // Calling store instruction on all method attributes
        i = 1;
        while(i < aList.size()) {
            printUtil.storeInstUtil(out, new OpClass(aList.get(i).type, aList.get(i).opName.substring(1)), new OpClass(aList.get(i).type.correspondingPtrType(), aList.get(i).opName.substring(1) + ".addr"), null);
            i++ ;
        }
    }

    // This is for generating code c functions (c main method, c string and io methods, etc)
    public void generateIrForClasses(AST.program program, ClassInfoCG clsInfoCG, PrintUtility printUtil, PrintWriter out, List<String> functionFormalNameList) {
        for(AST.class_ cl : AST.program.classes) {
            if(cl.name.equals("Main")) {
                // The current class is Main
                printUtil.generateDefUtil(out, new TypeMapping(TypeMapping.TypeID.INT32), "main", new ArrayList<OpClass>(), null);
                printUtil.allocaInstUtil(out, operandType("Main", true, 0), new OpClass(operandType("Main", true, 1), "obj"), null);
                List<OpClass> operandList = new ArrayList<OpClass>();
                operandList.add(new OpClass(operandType("Main", true, 1), "obj"));
                printUtil.callInstUtil(out, new ArrayList<TypeMapping>(), "Main_Cons_Main", true, operandList, new OpClass(operandType("Main", true, 1), "obj1"), null);
                operandList.set(0, new OpClass(operandType("Main", true, 1), "obj1"));
                
                // // We are finding main method in the Main class
                TypeMapping tempTypeMappingObject = new TypeMapping(TypeMapping.TypeID.EMPTY);
                for(AST.feature ftre : cl.features) {
                    if(ftre.getClass() == AST.method.class) {
                        AST.method mthdTemp = (AST.method)ftre;
                        if(mthdTemp.name.equals("main")) {
                            if(mthdTemp.typeid.equals("Object"))
                                tempTypeMappingObject = new TypeMapping(TypeMapping.TypeID.VOID);
                            else
                                tempTypeMappingObject = operandType(mthdTemp.typeid, true, 0);
                            break;
                        }
                    }
                }
                
                printUtil.callInstUtil(out, new ArrayList<TypeMapping>(), "Main_main", true, operandList, new OpClass(tempTypeMappingObject, "0"), null);
                printUtil.returnInstUtil(out, (OpClass)new IntValClass(0), null);
            }

            int flag = 0;
            // If class is a predefined COOL class, we only need to generate code for their methods only
            switch(cl.name) {
                case "String":
                    generateStringMethods(out, printUtil, "length");
                    generateStringMethods(out, printUtil, "concat");
                    generateStringMethods(out, printUtil, "substr");
                    generateStringMethods(out, printUtil, "copy");
                    generateStringMethods(out, printUtil, "strcmp");
                    flag = 1;
                    break;
                case "Object":
                    generateObjectMethods(out, printUtil, "abort");
                    printUtil.classTypeUtil(out, cl.name, new ArrayList<TypeMapping>(), null);
                    generateConstructorOfClass(out, printUtil, cl.name, new InstructionInfo(), cl, clsInfoCG, functionFormalNameList);
                    flag = 1;
                    break;
                case "IO":
                    generateIOMethods(out, printUtil, "in_int");
                    generateIOMethods(out, printUtil, "in_string");
                    generateIOMethods(out, printUtil, "out_int");
                    generateIOMethods(out, printUtil, "out_string");
                    printUtil.classTypeUtil(out, cl.name, new ArrayList<TypeMapping>(), null);
                    generateConstructorOfClass(out, printUtil, cl.name, new InstructionInfo(), cl, clsInfoCG, functionFormalNameList);
                    flag = 1;
                    break;
                case "Int":
                    flag = 1;
                    break;
                case "Bool":
                    flag = 1;
                    break;
            }
            
            if(flag == 1)
                continue;

            // Traverse over the attributes of the class and generate code for them
            List<TypeMapping> attrTypesList = new ArrayList<TypeMapping>();
            for(AST.attr attrTemp : clsInfoCG.cls.get(cl.name).attrList) {
                attrTypesList.add(operandType(attrTemp.typeid, true, 1));
                if(attrTemp.typeid.equals("String") && attrTemp.value.getClass() == AST.string_const.class) {
                    // Means the current attribute is a string constant
                    stringProcess(out, printUtil, attrTemp.value, 0);
                }
            }
            // Generates the define code for attributes of class
            printUtil.classTypeUtil(out, cl.name, attrTypesList, null);

            // Generating code for assignment of type names
            generateConstructorOfClass(out, printUtil, cl.name, new InstructionInfo(), cl, clsInfoCG, functionFormalNameList);

            registerCounter = new InstructionInfo();

            // Now, we iterate over the methods of the class and generate llvm ir for that
            for(AST.method mthdTemp : clsInfoCG.cls.get(cl.name).mthdList) {
                // For each method,
                // * We make a list of operand for the aList of the method, 
                // where the first argument is a pointer to the class with name 'this'
                // * Generate code for return type
                // * Mangle name of class with name of function
                // * Call the defined function
                stringProcess(out, printUtil, mthdTemp.body, 0);

                List<OpClass> argsList = new ArrayList<OpClass>();
                // Adding 'this' operand of function
                argsList.add(new OpClass(operandType(cl.name, true, 1), "this"));

                functionFormalNameList = new ArrayList<String>();

                // Adding other operands
                for(AST.formal frmlList : mthdTemp.formals) {
                    OpClass argTemp = new OpClass(operandType(frmlList.typeid, true, 1), frmlList.name);
                    argsList.add(argTemp);
                    functionFormalNameList.add(frmlList.name);
                }

                String mthdMangledName = cl.name + "_" + mthdTemp.name;
                if(mthdTemp.typeid.equals("Object")) {
                    mthdType = new TypeMapping(TypeMapping.TypeID.VOID);
                } else {
                    mthdType = operandType(mthdTemp.typeid, true, 0);
                }
                printUtil.generateDefUtil(out, mthdType, mthdMangledName, argsList, null);

                // Generating code for retval
                TypeMapping mthdRetType = operandType(mthdTemp.typeid, true, 0);
                if(mthdTemp.typeid.equals("Object") == false) {
                    OpClass retMthdVal = new OpClass(operandType(mthdTemp.typeid, true, 0), "retval");
                    printUtil.allocaInstUtil(out, operandType(mthdTemp.typeid, true, 0), retMthdVal, null);
                }

                // Generating the alloca instructions for argsList
                allocateMethodAttributes(out, printUtil, argsList);

                BasicClassBlockCG currentClass = clsInfoCG.cls.get(cl.name);
                TypeMapping singlePtr = operandType(cl.name, true, 2);
                printUtil.storeInstUtil(out, new OpClass(operandType(cl.name, true, 1), "this"), new OpClass(operandType(cl.name, true, 2), "this" + ".addr"), null);
                printUtil.loadInstUtil(out, operandType(cl.name, true, 1), new OpClass(operandType(cl.name, true, 2), "this" + ".addr"), new OpClass(operandType(cl.name, true, 1), "this1"), null);

                for(int i=0; i<currentClass.attrList.size(); i++) {
                    int flagTemp = 0;
                    for(String elem : functionFormalNameList) {
                        if(elem.equals(currentClass.attrList.get(i).name)) {
                            flagTemp = 1;
                            break;
                        }
                    }

                    if(flagTemp == 1)
                        continue;

                    List<OpClass> opList = new ArrayList<OpClass>();
                    OpClass result = new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), currentClass.attrList.get(i).name);
                    opList.add(new OpClass(operandType(cl.name, true, 1), "this1"));
                    opList.add((OpClass)new IntValClass(0));
                    opList.add((OpClass)new IntValClass(i));
                    printUtil.getElemPtrInstUtil(out, operandType(cl.name, true, 0), opList, result, true, null);
                }

                // Class Name of current class
                String currentClassName = cl.name;
                // For every method resetting The value of nested if and loop
                nestedIfCount = 0;
                nestedLoopCount = 0;
                
                /* 
                    Reinitializing the register count to zero for each method
                    Reinitializing the last instruction's type to method return type for each method
                    Entry is the first label of every method
                */
                registerCounter.reintialiseToDefault(0, mthdRetType, "%entry");
                
                registerCounter = visitNodeObject.VisitorPattern(out, printUtil, mthdTemp.body, registerCounter, clsInfoCG, cl, functionFormalNameList);
                
                if(((mthdTemp.body.getClass() != AST.block.class) && (mthdTemp.body.getClass() != AST.loop.class) && (mthdTemp.body.getClass() != AST.cond.class))) {
                    if(registerCounter.registerVal - 1 >= 0 && mthdType.name.equals(registerCounter.lastInstructionType.name) && ((mthdType.name.equals("void")) == false)) {
                        printUtil.storeInstUtil(out, new OpClass(mthdType, String.valueOf(registerCounter.registerVal - 1)), new cool.OpClass(mthdType.correspondingPtrType(), "retval"), null);
                    }
                }

                printUtil.brUncoditionUtil(out, "fun_returning_basic_block", null);
                // Label for dispatch on void fucntion
                out.println("dispatch_on_void_basic_block:");
                printUtil.allocaInstUtil(out, new TypeMapping(TypeMapping.TypeID.INT8PTR), new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "err_msg_void_dispatch"), null);
                out.println("\tstore i8* getelementptr inbounds ([47 x i8], [47 x i8]* @staticdispatchonvoiderr, i32 0, i32 0), i8** %err_msg_void_dispatch");
                printUtil.loadInstUtil(out, new TypeMapping(TypeMapping.TypeID.INT8PTR), new OpClass(new TypeMapping(TypeMapping.TypeID.INT8DOUBLEPTR), "err_msg_void_dispatch"), new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "print_err_msg_void_dispatch"), null);
                List<OpClass> printArguments;
                printArguments = new ArrayList<OpClass>();
                printArguments.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "print_err_msg_void_dispatch"));
                // Invoking out_string of IO to display the error message
                printUtil.callInstUtil(out, new ArrayList<TypeMapping>(), "IO_out_string", true, printArguments, new OpClass(new TypeMapping(TypeMapping.TypeID.VOID), "null"), null);
                // Aborting after printing the error message
                printUtil.callInstUtil(out, new ArrayList<TypeMapping>(), "Object_abort", true, new ArrayList<OpClass>(), new OpClass(new TypeMapping(TypeMapping.TypeID.VOID), "null"), null);
                printUtil.brUncoditionUtil(out, "fun_returning_basic_block", null);

                // Creating Print and abort labels
                out.print("func_div_by_zero_abort:\n");
                printUtil.allocaInstUtil(out, new TypeMapping(TypeMapping.TypeID.INT8PTR), new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "err_msg"), null);
                out.print("\tstore i8* getelementptr inbounds ([31 x i8], [31 x i8]* @divby0err, i32 0, i32 0), i8** %err_msg\n");
                printUtil.loadInstUtil(out, new TypeMapping(TypeMapping.TypeID.INT8PTR), new OpClass(new TypeMapping(TypeMapping.TypeID.INT8DOUBLEPTR), "err_msg"), new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "print_err_msg"), null);
                printArguments = new ArrayList<>();
                printArguments.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), "print_err_msg"));
                // Invoking out_string of IO to display the error message
                printUtil.callInstUtil(out, new ArrayList<TypeMapping>(), "IO_out_string", true, printArguments, new OpClass(new TypeMapping(TypeMapping.TypeID.VOID), "null"), null);
                // Aborting after printing the error message
                printUtil.callInstUtil(out, new ArrayList<TypeMapping>(), "Object_abort", true, new ArrayList<OpClass>(), new OpClass(new TypeMapping(TypeMapping.TypeID.VOID), "null"), null);
                printUtil.brUncoditionUtil(out, "fun_returning_basic_block", null);

                out.print("fun_returning_basic_block:\n");
                
                // Printing the return type of the method
                if(!mthdTemp.typeid.equals("Object")) {
                    printUtil.loadInstUtil(out, mthdRetType, new OpClass(mthdRetType.correspondingPtrType(), "retval"), new OpClass(mthdRetType, String.valueOf(registerCounter.registerVal)), null);
                    printUtil.returnInstUtil(out, new OpClass(mthdRetType, String.valueOf(registerCounter.registerVal)), null);
                    registerCounter.registerVal = registerCounter.registerVal + 1;
                    
                } else {
                    printUtil.returnInstUtil(out, new OpClass(new TypeMapping(TypeMapping.TypeID.VOID), "null"), null);
                }
            }
        }
    }

    public String attributeAddressOfObj(String objName, ClassInfoCG clsInfoCG, String className, List<String> functionFormalNameList) {
        boolean flag = false;
        for(String strTemp : functionFormalNameList) {
            if (strTemp.equals(objName))
              flag = true;
        }
        if(flag == false) {
            for (AST.attr attrTemp : clsInfoCG.cls.get(className).attrList) {
                if (objName.equals(attrTemp.name)) {
                    return objName;
                }
            }
        }
        return (objName + ".addr");
    }

}