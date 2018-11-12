package cool;

import java.util.*;
import java.io.PrintWriter;

public class VisitNodeClass {

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

    // This method returns an object of TypeMapping class for the return type of method
    public TypeMapping returnTypeOfMethod(String methodClassName, String methodName, ClassInfoCG clsInfoCG, AST.class_ cl) {
        // Iterating over the methods of all class list
        for(AST.method mthdTemp : clsInfoCG.cls.get(methodClassName).mthdList) {
            if(mthdTemp.name.equals(methodName)) {
                // We found the required method
                if(mthdTemp.typeid.equals("Object")) {
                    return (new TypeMapping(TypeMapping.TypeID.VOID));
                }
                return operandType(mthdTemp.typeid, true, 1);
            }
        }
        return (new TypeMapping(TypeMapping.TypeID.VOID));
    }

    public InstructionInfo VisitorPattern(PrintWriter out, PrintUtility printUtil, AST.expression expr, InstructionInfo registerCounter, ClassInfoCG clsInfoCG, AST.class_ cl, List<String> functionFormalNameList) {
        if(expr.getClass() == AST.assign.class) {
            return VisitNode(out, printUtil, (AST.assign)expr, registerCounter, clsInfoCG, cl, functionFormalNameList);
        } else if(expr.getClass() == AST.static_dispatch.class) {
            return VisitNode(out, printUtil, (AST.static_dispatch)expr, registerCounter, clsInfoCG, cl, functionFormalNameList);
        } else if(expr.getClass() == AST.cond.class) {
            return VisitNode(out, printUtil, (AST.cond)expr, registerCounter, clsInfoCG, cl, functionFormalNameList);
        } else if(expr.getClass() == AST.loop.class) {
            return VisitNode(out, printUtil, (AST.loop)expr, registerCounter, clsInfoCG, cl, functionFormalNameList);
        } else if(expr.getClass() == AST.block.class) {
            return VisitNode(out, printUtil, (AST.block)expr, registerCounter, clsInfoCG, cl, functionFormalNameList);
        } else if(expr.getClass() == AST.new_.class) {
            return VisitNode(out, printUtil, (AST.new_)expr, registerCounter, clsInfoCG, cl, functionFormalNameList);
        } else if(expr.getClass() == AST.plus.class) {
            return VisitNode(out, printUtil, (AST.plus)expr, registerCounter, clsInfoCG, cl, functionFormalNameList);
        } else if(expr.getClass() == AST.sub.class) {
            return VisitNode(out, printUtil, (AST.sub)expr, registerCounter, clsInfoCG, cl, functionFormalNameList);
        } else if(expr.getClass() == AST.mul.class) {
            return VisitNode(out, printUtil, (AST.mul)expr, registerCounter, clsInfoCG, cl, functionFormalNameList);
        } else if(expr.getClass() == AST.divide.class) {
            return VisitNode(out, printUtil, (AST.divide)expr, registerCounter, clsInfoCG, cl, functionFormalNameList);
        } else if(expr.getClass() == AST.comp.class) {
            return VisitNode(out, printUtil, (AST.comp)expr, registerCounter, clsInfoCG, cl, functionFormalNameList);
        } else if(expr.getClass() == AST.lt.class) {
            return VisitNode(out, printUtil, (AST.lt)expr, registerCounter, clsInfoCG, cl, functionFormalNameList);
        } else if(expr.getClass() == AST.leq.class) {
            return VisitNode(out, printUtil, (AST.leq)expr, registerCounter, clsInfoCG, cl, functionFormalNameList);
        } else if(expr.getClass() == AST.eq.class) {
            return VisitNode(out, printUtil, (AST.eq)expr, registerCounter, clsInfoCG, cl, functionFormalNameList);
        } else if(expr.getClass() == AST.neg.class) {
            return VisitNode(out, printUtil, (AST.neg)expr, registerCounter, clsInfoCG, cl, functionFormalNameList);
        } else if(expr.getClass() == AST.object.class) {
            return VisitNode(out, printUtil, (AST.object)expr, registerCounter, clsInfoCG, cl, functionFormalNameList);
        } else if(expr.getClass() == AST.int_const.class) {
            return VisitNode(out, printUtil, (AST.int_const)expr, registerCounter, clsInfoCG, cl, functionFormalNameList);
        } else if(expr.getClass() == AST.string_const.class) {
            return VisitNode(out, printUtil, (AST.string_const)expr, registerCounter, clsInfoCG, cl, functionFormalNameList);
        } else if(expr.getClass() == AST.bool_const.class) {
            return VisitNode(out, printUtil, (AST.bool_const)expr, registerCounter, clsInfoCG, cl, functionFormalNameList);
        }
        return registerCounter;
    }

    public InstructionInfo VisitNode(PrintWriter out, PrintUtility printUtil, AST.assign exprAss, InstructionInfo registerCounter, ClassInfoCG clsInfoCG, AST.class_ cl, List<String> functionFormalNameList) {
        // Generating for Assign expression
        registerCounter = VisitorPattern(out, printUtil, exprAss.e1, registerCounter, clsInfoCG, cl, functionFormalNameList);
        String temp = attributeAddressOfObj(exprAss.name, clsInfoCG, cl.name, functionFormalNameList);
        printUtil.storeInstUtil(out, new OpClass(registerCounter.lastInstructionType, String.valueOf(registerCounter.registerVal - 1)), new OpClass(registerCounter.lastInstructionType.correspondingPtrType(), temp), null);
        return registerCounter;
    }

    public InstructionInfo VisitNode(PrintWriter out, PrintUtility printUtil, AST.static_dispatch exprStd, InstructionInfo registerCounter, ClassInfoCG clsInfoCG, AST.class_ cl, List<String> functionFormalNameList) {
        // Generating for Static dispatch
        // which is of type <expr>@<type>.id(<expr>,...,<expr>)
        String exprStdType = exprStd.typeid;
        String exprStdCallee;
        List<OpClass> argumentList = new ArrayList<OpClass>();
        String mangledFunctionName = exprStd.typeid + "_" + exprStd.name;
        
        if((exprStdType.equals("Int") || exprStdType.equals("Bool") || exprStdType.equals("String")) == false) {
            registerCounter = VisitorPattern(out, printUtil, exprStd.caller, registerCounter, clsInfoCG, cl, functionFormalNameList);
            exprStdCallee = registerCounter.lastInstructionType.name;

            out.print("\t%" + registerCounter.registerVal + " = icmp eq " + exprStdCallee + " null, %" + (registerCounter.registerVal - 1) + "\n");
            printUtil.brConditionUtil(out, new OpClass(new TypeMapping(TypeMapping.TypeID.INT1), String.valueOf(registerCounter.registerVal)), "dispatch_on_void_basic_block", "proceed_" + registerCounter.registerVal, null);
        
            out.print("\nproceed_" + registerCounter.registerVal + ":\n");
            registerCounter.registerVal = registerCounter.registerVal + 1;
            registerCounter.lastBasicBlockName = "proceed_" + registerCounter.registerVal + ":";
        }

        // Adding method arguments to 'argumentList'
        for(AST.expression exp : exprStd.actuals) {
            registerCounter = VisitorPattern(out, printUtil, exp, registerCounter, clsInfoCG, cl, functionFormalNameList);
            OpClass opTemp = new OpClass(registerCounter.lastInstructionType, String.valueOf(registerCounter.registerVal - 1));
            argumentList.add(opTemp);
        }

        // Calling VisitorPattern on caller expression
        registerCounter = VisitorPattern(out, printUtil, exprStd.caller, registerCounter, clsInfoCG, cl, functionFormalNameList);

        // Adding this pointer to classes other than IO
        if(exprStd.typeid.equals("IO") == false)
            // Adding the 'this' pointer argument to the beggining of the list
            argumentList.add(0, new OpClass(registerCounter.lastInstructionType, String.valueOf(registerCounter.registerVal - 1)));
        
        printUtil.callInstUtil(out, new ArrayList<TypeMapping>(), mangledFunctionName, true, argumentList, new OpClass(returnTypeOfMethod(exprStd.typeid, exprStd.name, clsInfoCG, cl), String.valueOf(registerCounter.registerVal)), null);

        if((new OpClass(returnTypeOfMethod(exprStd.typeid, exprStd.name, clsInfoCG, cl), String.valueOf(registerCounter.registerVal))).type.id.equals(TypeMapping.TypeID.VOID))
            return new InstructionInfo(registerCounter.registerVal, returnTypeOfMethod(exprStd.typeid, exprStd.name, clsInfoCG, cl), registerCounter.lastBasicBlockName);
        else
            return new InstructionInfo(registerCounter.registerVal + 1, returnTypeOfMethod(exprStd.typeid, exprStd.name, clsInfoCG, cl), registerCounter.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, PrintUtility printUtil, AST.cond exprCond, InstructionInfo registerCounter, ClassInfoCG clsInfoCG, AST.class_ cl, List<String> functionFormalNameList) {
        // Generating for IF-ELSE statements
        // taking care of nested IF-ELSE
        int nestedValue = GenerateLlvm.nestedIfCount;
        GenerateLlvm.nestedIfCount = GenerateLlvm.nestedIfCount + 1;

        InstructionInfo preBlock, thenBlock, elseBlock;
        // Predicate Block of IF statement
        preBlock = VisitorPattern(out, printUtil, exprCond.predicate, new InstructionInfo(registerCounter.registerVal, registerCounter.lastInstructionType, registerCounter.lastBasicBlockName), clsInfoCG, cl, functionFormalNameList);
        printUtil.brConditionUtil(out, new OpClass(new TypeMapping(TypeMapping.TypeID.INT1), String.valueOf(preBlock.registerVal - 1)), "if.then" + String.valueOf(nestedValue), "if.else" + String.valueOf(nestedValue), null);
        
        // IF Body of IF statement
        out.println("\nif.then" + String.valueOf(nestedValue) + ":");
        thenBlock = VisitorPattern(out, printUtil, exprCond.ifbody, new InstructionInfo(preBlock.registerVal, preBlock.lastInstructionType, "%if.then" + String.valueOf(nestedValue)), clsInfoCG, cl, functionFormalNameList);
        printUtil.brUncoditionUtil(out, "if.end" + String.valueOf(nestedValue), null);

        // ELSE Body of IF statement
        out.println("\nif.else" + String.valueOf(nestedValue) + ":");
        elseBlock = VisitorPattern(out, printUtil, exprCond.elsebody, new InstructionInfo(thenBlock.registerVal, thenBlock.lastInstructionType, "%if.else" + String.valueOf(nestedValue)), clsInfoCG, cl, functionFormalNameList);
        
        printUtil.brUncoditionUtil(out, "if.end" + String.valueOf(nestedValue), null);

        // if-else exit code for IR
        out.println("\nif.end" + String.valueOf(nestedValue) + ":");

        TypeMapping conditionType = elseBlock.lastInstructionType;
        if(conditionType.name.equals("void") != false) {
            return new InstructionInfo(elseBlock.registerVal , conditionType, "%if.end" + String.valueOf(nestedValue));
        }

        out.println("\t%" + elseBlock.registerVal + " = phi " + conditionType.name + " [ %" + (thenBlock.registerVal - 1) + ", " + (thenBlock.lastBasicBlockName) + " ]" + ", " + " [ %" + (elseBlock.registerVal - 1) + ", " + (elseBlock.lastBasicBlockName) + " ]");

        if(elseBlock.registerVal >= 0 && GenerateLlvm.mthdType.name.equals(elseBlock.lastInstructionType.name) && ((GenerateLlvm.mthdType.name.equals("void")) == false)) {
            printUtil.storeInstUtil(out, new OpClass(GenerateLlvm.mthdType, String.valueOf(elseBlock.registerVal)), new cool.OpClass(GenerateLlvm.mthdType.correspondingPtrType(), "retval"), null);
        }
        return new InstructionInfo(elseBlock.registerVal + 1, conditionType, "%if.end" + String.valueOf(nestedValue));
    }

    public InstructionInfo VisitNode(PrintWriter out, PrintUtility printUtil, AST.loop exprLoop, InstructionInfo registerCounter, ClassInfoCG clsInfoCG, AST.class_ cl, List<String> functionFormalNameList) {
        int nestedValue = GenerateLlvm.nestedLoopCount;
        GenerateLlvm.nestedLoopCount = GenerateLlvm.nestedLoopCount + 1;
        printUtil.brUncoditionUtil(out, "for.cond" + String.valueOf(nestedValue), null);
        
        // Loop Condition start
        out.println("\nfor.cond" + String.valueOf(nestedValue) + ":");
        // Predicate of Loop
        InstructionInfo preLoop;
        preLoop = VisitorPattern(out, printUtil, exprLoop.predicate, new InstructionInfo(registerCounter.registerVal, registerCounter.lastInstructionType, "%for.cond" + String.valueOf(nestedValue)), clsInfoCG, cl, functionFormalNameList);
        printUtil.brConditionUtil(out, new OpClass(new TypeMapping(TypeMapping.TypeID.INT1), String.valueOf(preLoop.registerVal - 1)), "for.body" + String.valueOf(nestedValue), "for.end" + String.valueOf(nestedValue), null);
        
        // Loop Body Start
        out.println("\nfor.body" + String.valueOf(nestedValue) + ":");
        InstructionInfo bodyLoop;
        bodyLoop = VisitorPattern(out, printUtil, exprLoop.body, new InstructionInfo(preLoop.registerVal, new TypeMapping(TypeMapping.TypeID.INT1), "%for.body" + String.valueOf(nestedValue)), clsInfoCG, cl, functionFormalNameList);
        TypeMapping loopTypeVar;
        loopTypeVar = bodyLoop.lastInstructionType;
        printUtil.brUncoditionUtil(out, "for.cond" + String.valueOf(nestedValue), null);
        
        out.println("\nfor.end" + String.valueOf(nestedValue) + ":");

        InstructionInfo tempInstInfo = new InstructionInfo(bodyLoop.registerVal, loopTypeVar, "%for.end" + String.valueOf(nestedValue));
        return tempInstInfo;
    }

    public InstructionInfo VisitNode(PrintWriter out, PrintUtility printUtil, AST.block exprBlock, InstructionInfo registerCounter, ClassInfoCG clsInfoCG, AST.class_ cl, List<String> functionFormalNameList) {
        // Generating for Block expressions
        for(AST.expression exprTemp : exprBlock.l1) {
            registerCounter = VisitorPattern(out, printUtil, exprTemp, registerCounter, clsInfoCG, cl, functionFormalNameList);
        }
        if(registerCounter.registerVal - 1 >= 0 && GenerateLlvm.mthdType.name.equals(registerCounter.lastInstructionType.name) && ((GenerateLlvm.mthdType.name.equals("void")) == false)) {
            printUtil.storeInstUtil(out, new OpClass(GenerateLlvm.mthdType, String.valueOf(registerCounter.registerVal - 1)), new cool.OpClass(GenerateLlvm.mthdType.correspondingPtrType(), "retval"), null);
        }
        return registerCounter;
    }

    public InstructionInfo VisitNode(PrintWriter out, PrintUtility printUtil, AST.new_ exprNew, InstructionInfo registerCounter, ClassInfoCG clsInfoCG, AST.class_ cl, List<String> functionFormalNameList) {
        // Generating code for new statements
        // which is of the form, expr ::= new ID

        /* 
            Gnerating for predefined classes
        */
        if(exprNew.typeid.equals("String")) {
            // The length of a new string is 1
            String stringLengthIr = "[" + 1 + " x i8]";
            printUtil.allocaInstUtil(out, new TypeMapping(TypeMapping.TypeID.INT8PTR), new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), String.valueOf(registerCounter.registerVal)), null);
            out.print("\tstore i8* getelementptr inbounds (" + stringLengthIr + ", " + stringLengthIr + "* @.str.empty, i32 0, i32 0), i8** %" + String.valueOf(registerCounter.registerVal) + "\n");
            printUtil.loadInstUtil(out, new TypeMapping(TypeMapping.TypeID.INT8PTR), new OpClass((new TypeMapping(TypeMapping.TypeID.INT8PTR)).correspondingPtrType(), String.valueOf(registerCounter.registerVal)), new OpClass((new TypeMapping(TypeMapping.TypeID.INT8PTR)), String.valueOf(registerCounter.registerVal + 1)), null);
            return new InstructionInfo(registerCounter.registerVal + 2, new TypeMapping(TypeMapping.TypeID.INT8PTR), registerCounter.lastBasicBlockName);
        } else if(exprNew.typeid.equals("Bool")) {
            printUtil.allocaInstUtil(out, new TypeMapping(TypeMapping.TypeID.INT1), new OpClass(new TypeMapping(TypeMapping.TypeID.INT1), String.valueOf(registerCounter.registerVal)), null);
            printUtil.storeInstUtil(out, (OpClass)new BoolValClass(false), new OpClass((new TypeMapping(TypeMapping.TypeID.INT1)).correspondingPtrType(), String.valueOf(registerCounter.registerVal)), null);
            printUtil.loadInstUtil(out, new TypeMapping(TypeMapping.TypeID.INT1), new OpClass((new TypeMapping(TypeMapping.TypeID.INT1)).correspondingPtrType(), String.valueOf(registerCounter.registerVal)), new OpClass((new TypeMapping(TypeMapping.TypeID.INT1)), String.valueOf(registerCounter.registerVal + 1)), null);
            return new InstructionInfo(registerCounter.registerVal + 2, new TypeMapping(TypeMapping.TypeID.INT1), registerCounter.lastBasicBlockName);
        } else if(exprNew.typeid.equals("Int")) {
            printUtil.allocaInstUtil(out, new TypeMapping(TypeMapping.TypeID.INT32), new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(registerCounter.registerVal)), null);
            printUtil.storeInstUtil(out, (OpClass)new IntValClass(0), new OpClass((new TypeMapping(TypeMapping.TypeID.INT32)).correspondingPtrType(), String.valueOf(registerCounter.registerVal)), null);
            printUtil.loadInstUtil(out, new TypeMapping(TypeMapping.TypeID.INT32), new OpClass((new TypeMapping(TypeMapping.TypeID.INT32)).correspondingPtrType(), String.valueOf(registerCounter.registerVal)), new OpClass((new TypeMapping(TypeMapping.TypeID.INT32)), String.valueOf(registerCounter.registerVal + 1)), null);
            return new InstructionInfo(registerCounter.registerVal + 2, new TypeMapping(TypeMapping.TypeID.INT32), registerCounter.lastBasicBlockName);
        }

        // Generating for other classes
        printUtil.allocaInstUtil(out, operandType(exprNew.typeid, true, 0), new OpClass(operandType(exprNew.typeid, true, 0), String.valueOf(registerCounter.registerVal)), null);
        List<OpClass> operandList;
        operandList = new ArrayList<OpClass>();
        
        operandList.add(new OpClass(operandType(exprNew.typeid, true, 1), String.valueOf(registerCounter.registerVal)));
        printUtil.callInstUtil(out, new ArrayList<TypeMapping>(), exprNew.typeid + "_Cons_" + exprNew.typeid, true, operandList, new OpClass(operandType(exprNew.typeid, true, 1), String.valueOf(registerCounter.registerVal + 1)), null);
        return new InstructionInfo(registerCounter.registerVal + 2, operandType(exprNew.typeid, true, 1), registerCounter.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, PrintUtility printUtil, AST.plus expr, InstructionInfo registerCounter, ClassInfoCG clsInfoCG, AST.class_ cl, List<String> functionFormalNameList) {
        // Generating code for addition Operation
        InstructionInfo regCount1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsInfoCG, cl, functionFormalNameList);
        InstructionInfo regCount2 = VisitorPattern(out, printUtil, (expr).e2, regCount1, clsInfoCG, cl, functionFormalNameList);
        printUtil.arithemeticUtil(out, "add", new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(regCount1.registerVal - 1 )), new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(regCount2.registerVal - 1 )), new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(regCount2.registerVal)), null);
        
        return new InstructionInfo(regCount2.registerVal + 1, new TypeMapping(TypeMapping.TypeID.INT32), regCount2.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, PrintUtility printUtil, AST.sub expr, InstructionInfo registerCounter, ClassInfoCG clsInfoCG, AST.class_ cl, List<String> functionFormalNameList) {
        // Generating Code for subtraction Operation
        InstructionInfo regCount1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsInfoCG, cl, functionFormalNameList);
        InstructionInfo regCount2 = VisitorPattern(out, printUtil, (expr).e2, regCount1, clsInfoCG, cl, functionFormalNameList);
        printUtil.arithemeticUtil(out, "sub", new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(regCount1.registerVal - 1 )), new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(regCount2.registerVal - 1 )), new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(regCount2.registerVal)), null);
        
        return new InstructionInfo(regCount2.registerVal + 1, new TypeMapping(TypeMapping.TypeID.INT32), regCount2.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, PrintUtility printUtil, AST.mul expr, InstructionInfo registerCounter, ClassInfoCG clsInfoCG, AST.class_ cl, List<String> functionFormalNameList) {
        // Generating code for multiplication operation
        InstructionInfo regCount1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsInfoCG, cl, functionFormalNameList);
        InstructionInfo regCount2 = VisitorPattern(out, printUtil, (expr).e2, regCount1, clsInfoCG, cl, functionFormalNameList);
        printUtil.arithemeticUtil(out, "mul", new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(regCount1.registerVal - 1 )), new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(regCount2.registerVal - 1 )), new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(regCount2.registerVal)), null);
        
        return new InstructionInfo(regCount2.registerVal + 1, new TypeMapping(TypeMapping.TypeID.INT32), regCount2.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, PrintUtility printUtil, AST.divide expr, InstructionInfo registerCounter, ClassInfoCG clsInfoCG, AST.class_ cl, List<String> functionFormalNameList) {
        // Generating Code for Division Operation
        InstructionInfo regCount1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsInfoCG, cl, functionFormalNameList);
        InstructionInfo regCount2 = VisitorPattern(out, printUtil, (expr).e2, regCount1, clsInfoCG, cl, functionFormalNameList);
        // In Division we have to check whether the Dividend is a number different than  Zero or not
        String regCount2Str = String.valueOf(regCount2.registerVal - 1);
        printUtil.cmpInstUtil(out, "EQ", new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), regCount2Str), (OpClass)new IntValClass(0), new OpClass(new TypeMapping(TypeMapping.TypeID.INT1), "comp_" + String.valueOf(regCount2.registerVal - 1) + "_0"), null);
        printUtil.brConditionUtil(out, new OpClass(new TypeMapping(TypeMapping.TypeID.INT1), "comp_" + regCount2Str + "_0"), "func_div_by_zero_abort", "proceed_" + regCount2Str + "_0", null);
        // New branch to proceed to
        out.println("\nproceed_" + regCount2Str + "_0:");
        printUtil.arithemeticUtil(out, "udiv", new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(regCount1.registerVal - 1 )), new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(regCount2.registerVal - 1 )), new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(regCount2.registerVal)), null);
        
        // returning with proceed as the last Basic Block
        return new InstructionInfo(regCount2.registerVal + 1, new TypeMapping(TypeMapping.TypeID.INT32), "proceed_" + regCount2Str + "_0:");
    }

    public InstructionInfo VisitNode(PrintWriter out, PrintUtility printUtil, AST.comp expr, InstructionInfo registerCounter, ClassInfoCG clsInfoCG, AST.class_ cl, List<String> functionFormalNameList) {
        InstructionInfo regCount1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsInfoCG, cl, functionFormalNameList);
        // Taking XOR to perform compliment
        printUtil.arithemeticUtil(out, "xor", new OpClass(new TypeMapping(TypeMapping.TypeID.INT1), String.valueOf(regCount1.registerVal - 1)), (OpClass)new BoolValClass(true), new OpClass(new TypeMapping(TypeMapping.TypeID.INT1), String.valueOf(regCount1.registerVal)), null);

        return new InstructionInfo(regCount1.registerVal + 1, new TypeMapping(TypeMapping.TypeID.INT1), regCount1.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, PrintUtility printUtil, AST.lt expr, InstructionInfo registerCounter, ClassInfoCG clsInfoCG, AST.class_ cl, List<String> functionFormalNameList) {
        // Generating for Less-Than Operation
        InstructionInfo regCount1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsInfoCG, cl, functionFormalNameList);
        InstructionInfo regCount2 = VisitorPattern(out, printUtil, (expr).e2, regCount1, clsInfoCG, cl, functionFormalNameList);
        printUtil.cmpInstUtil(out, "LT", new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(regCount1.registerVal - 1)), new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(regCount2.registerVal - 1)), new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(regCount2.registerVal)), null);

        return new InstructionInfo(regCount2.registerVal + 1, new TypeMapping(TypeMapping.TypeID.INT1), regCount2.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, PrintUtility printUtil, AST.leq expr, InstructionInfo registerCounter, ClassInfoCG clsInfoCG, AST.class_ cl, List<String> functionFormalNameList) {
        // Generating for Less-Than-EqualTo Operation
        InstructionInfo regCount1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsInfoCG, cl, functionFormalNameList);
        InstructionInfo regCount2 = VisitorPattern(out, printUtil, (expr).e2, regCount1, clsInfoCG, cl, functionFormalNameList);
        printUtil.cmpInstUtil(out, "LE", new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(regCount1.registerVal - 1)), new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(regCount2.registerVal - 1)), new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(regCount2.registerVal)), null);

        return new InstructionInfo(regCount2.registerVal + 1, new TypeMapping(TypeMapping.TypeID.INT1), regCount2.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, PrintUtility printUtil, AST.eq expr, InstructionInfo registerCounter, ClassInfoCG clsInfoCG, AST.class_ cl, List<String> functionFormalNameList) {
        // Generating for EqualTo expression
        InstructionInfo regCount1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsInfoCG, cl, functionFormalNameList);
        InstructionInfo regCount2 = VisitorPattern(out, printUtil, (expr).e2, regCount1, clsInfoCG, cl, functionFormalNameList);
        if(regCount1.lastInstructionType.name.equals((new TypeMapping(TypeMapping.TypeID.INT32)).name)) {
            // If Operands are of type Int
            printUtil.cmpInstUtil(out, "EQ", new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(regCount1.registerVal - 1)), new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(regCount2.registerVal - 1)), new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(regCount2.registerVal)), null);
        } else if(regCount1.lastInstructionType.name.equals((new TypeMapping(TypeMapping.TypeID.INT1)).name)) {
            // If Operands are of type Bool
            printUtil.cmpInstUtil(out, "EQ", new OpClass(new TypeMapping(TypeMapping.TypeID.INT1), String.valueOf(regCount1.registerVal - 1)), new OpClass(new TypeMapping(TypeMapping.TypeID.INT1), String.valueOf(regCount2.registerVal - 1)), new OpClass(new TypeMapping(TypeMapping.TypeID.INT1), String.valueOf(regCount2.registerVal)), null);
        } else if(regCount1.lastInstructionType.name.equals((new TypeMapping(TypeMapping.TypeID.INT8PTR)).name)) {
            // If operands are fo type String
            OpClass retVal = new OpClass(new TypeMapping(TypeMapping.TypeID.INT1), String.valueOf(regCount2.registerVal));
            List<OpClass> args = new ArrayList<OpClass>();

            String regName1 = String.valueOf(regCount1.registerVal - 1);
            String regName2 = String.valueOf(regCount2.registerVal - 1);
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), regName1));
            args.add(new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), regName2));
            // Invoking strcmp method of C to compare strings
            printUtil.callInstUtil(out, new ArrayList<TypeMapping>(), "String_strcmp", true, args, retVal, null);
        }

        return new InstructionInfo(regCount2.registerVal + 1, new TypeMapping(TypeMapping.TypeID.INT1), regCount2.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, PrintUtility printUtil, AST.neg expr, InstructionInfo registerCounter, ClassInfoCG clsInfoCG, AST.class_ cl, List<String> functionFormalNameList) {
        InstructionInfo regCount1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsInfoCG, cl, functionFormalNameList);
        // Multiplying by -1 for negation
        printUtil.arithemeticUtil(out, "mul", new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(regCount1.registerVal - 1)), (OpClass)new IntValClass(-1), new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(regCount1.registerVal)), null);

        return new InstructionInfo(regCount1.registerVal + 1, new TypeMapping(TypeMapping.TypeID.INT32), regCount1.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, PrintUtility printUtil, AST.object exprObj, InstructionInfo registerCounter, ClassInfoCG clsInfoCG, AST.class_ cl, List<String> functionFormalNameList) {
        // Generating IR for object ID
        TypeMapping operandTemp = operandType(exprObj.type, true, 1);

        if(exprObj.name.equals("self")) {
            printUtil.loadInstUtil(out, operandTemp, new OpClass(operandTemp, "this1"), new OpClass(operandTemp, String.valueOf(registerCounter.registerVal)), null);
        } else {
            printUtil.loadInstUtil(out, operandTemp, new OpClass(operandTemp.correspondingPtrType(), attributeAddressOfObj(exprObj.name, clsInfoCG, cl.name, functionFormalNameList)), new OpClass(operandTemp, String.valueOf(registerCounter.registerVal)), null);
        }

        return new InstructionInfo(registerCounter.registerVal + 1, operandTemp, registerCounter.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, PrintUtility printUtil, AST.int_const expr, InstructionInfo registerCounter, ClassInfoCG clsInfoCG, AST.class_ cl, List<String> functionFormalNameList) {
        // Generating the IR for Int Constants
        printUtil.allocaInstUtil(out, new TypeMapping(TypeMapping.TypeID.INT32), new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(registerCounter.registerVal)), null);
        printUtil.storeInstUtil(out, (OpClass)new IntValClass((expr).value), new OpClass(new TypeMapping(TypeMapping.TypeID.INT32PTR), String.valueOf(registerCounter.registerVal)), null);
        printUtil.loadInstUtil(out, new TypeMapping(TypeMapping.TypeID.INT32),  new OpClass(new TypeMapping(TypeMapping.TypeID.INT32PTR), String.valueOf(registerCounter.registerVal)), new OpClass(new TypeMapping(TypeMapping.TypeID.INT32), String.valueOf(registerCounter.registerVal + 1)), null);
        
        return new InstructionInfo(registerCounter.registerVal + 2, new TypeMapping(TypeMapping.TypeID.INT32), registerCounter.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, PrintUtility printUtil, AST.string_const exprString, InstructionInfo registerCounter, ClassInfoCG clsInfoCG, AST.class_ cl, List<String> functionFormalNameList) {
        String stringTemp = exprString.value;
        // Generating the IR for string constants
        printUtil.allocaInstUtil(out, new TypeMapping(TypeMapping.TypeID.INT8PTR), new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), String.valueOf(registerCounter.registerVal)), null);
        out.print("\tstore i8* getelementptr inbounds ([" + String.valueOf(stringTemp.length() + 1) + " x i8], [" + String.valueOf(stringTemp.length() + 1) + " x i8]* @.str." + Codegen.stringToLineNoMapping.get(stringTemp) + ", i32 0, i32 0), i8** %" + String.valueOf(registerCounter.registerVal));
        printUtil.loadInstUtil(out, new TypeMapping(TypeMapping.TypeID.INT8PTR),  new OpClass(new TypeMapping(TypeMapping.TypeID.INT8DOUBLEPTR), String.valueOf(registerCounter.registerVal)), new OpClass(new TypeMapping(TypeMapping.TypeID.INT8PTR), String.valueOf(registerCounter.registerVal + 1)), null);
        
        return new InstructionInfo(registerCounter.registerVal + 2, new TypeMapping(TypeMapping.TypeID.INT8PTR), registerCounter.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, PrintUtility printUtil, AST.bool_const expr, InstructionInfo registerCounter, ClassInfoCG clsInfoCG, AST.class_ cl, List<String> functionFormalNameList) {
        // Generating the IR for Bool Constants
        printUtil.allocaInstUtil(out, new TypeMapping(TypeMapping.TypeID.INT1), new OpClass(new TypeMapping(TypeMapping.TypeID.INT1), String.valueOf(registerCounter.registerVal)), null);
        printUtil.storeInstUtil(out, (OpClass)new BoolValClass((expr).value), new OpClass(new TypeMapping(TypeMapping.TypeID.INT1PTR), String.valueOf(registerCounter.registerVal)), null);
        printUtil.loadInstUtil(out, new TypeMapping(TypeMapping.TypeID.INT1),  new OpClass(new TypeMapping(TypeMapping.TypeID.INT1PTR), String.valueOf(registerCounter.registerVal)), new OpClass(new TypeMapping(TypeMapping.TypeID.INT1), String.valueOf(registerCounter.registerVal + 1)), null);
        
        return new InstructionInfo(registerCounter.registerVal + 2, new TypeMapping(TypeMapping.TypeID.INT1), registerCounter.lastBasicBlockName);
    }
}