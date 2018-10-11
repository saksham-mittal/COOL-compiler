package cool;

import java.util.*;

import cool.AST;
import cool.AST.bool_const;
import cool.AST.int_const;
import cool.AST.mul;

public class VisitorMechanism {

    // For repoting error
    public static ReportError reportError;

    // Filename for error reporting
    public String filename;

    public VisitorMechanism() {
        reportError = new ReportError();

        filename = AST.program.classes.get(0).filename;
    }

    // visiting a class in the AST 
    public void VisitNode(AST.class_ cl, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        // This method runs a loop over all the features of
        // a class. The features can be methods or attributes
        for(AST.feature ft : cl.features) {
            if(ft.getClass() == AST.method.class) {
                // Comparing feature object and AST.method object
                VisitNode((AST.method)ft, clsInfo, scpTbl);
            } else if(ft.getClass() == AST.attr.class) {
                VisitNode((AST.attr)ft, clsInfo, scpTbl);
            }
        }
    }

    // visiting all the attributes a class in the AST
    public void VisitNode(AST.attr attr, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        /*
        // Finding class name using 'self' class
        AST.attr attrSelf = (AST.attr)scpTbl.lookUpLocal("self");
        */
        if(clsInfo.cls.get(attr.typeid) == null) {
            reportError.report(filename, attr.lineNo, "Undeclared type '" + attr.typeid +"' of attribute '" + attr.name + "'.");
        }
        // If attr's value is an expression class
        if(attr.value.getClass() != AST.no_expr.class) {
            // Visiting the expression value of the attribute
            VisitNode(attr.value, clsInfo, scpTbl);

            if(clsInfo.isConforming(attr.value.type, attr.typeid) == false) {
                // Attr expression type does not conforms to the return type of the Attr expression type
                reportError.report(filename, attr.value.lineNo, "The defined type '" + attr.typeid + "' of Attribute '" + attr.name + "' does not conform to the Attribute value type '" + attr.value.type + "'.");
            }
        }
    }

    // visiting all the methods of a class in the AST
    public void VisitNode(AST.method method, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        /*
        // Finding class name using 'self' class
        AST.attr attrSelf = (AST.attr)scpTbl.lookUpLocal("self");
        */
        scpTbl.enterScope();

        // Iterating the formal list of the method
        for(AST.formal fml : method.formals) {
            // If fml is present in the scope table, it will return some attribute object
            AST.feature ftre = (AST.feature)scpTbl.lookUpLocal(fml.name); 

            if(ftre != null && ftre.getClass() == AST.attr.class) {
                // Means this formal is already in the scope
                // Redefinition of a formal in the same scope is not allowed
                AST.attr attrFtre = (AST.attr) ftre;
                reportError.report(filename, attrFtre.lineNo, "Redefinition of formal '" + attrFtre.name + "' is not allowed.");
            }
            // insert the formal in the scope table 
            scpTbl.insert(fml.name, new AST.attr(fml.name, fml.typeid, new AST.no_expr(fml.lineNo), fml.lineNo));

        }

        // Calling the 'VisitNode' function for method body
        VisitNode(method.body, clsInfo, scpTbl);

        if(clsInfo.isConforming(method.body.type, method.typeid) == false) {
            // Method type does not conforms to the return type of the method
            reportError.report(filename, method.body.lineNo, "The defined return type '" + method.typeid + "' of method '" + method.name + "' does not match to the method body return type '" + method.body.type + "'.");
        }

        scpTbl.exitScope();
    }

    // visiting an expression and calling the function corresponding to that expression
    public void VisitNode(AST.expression expr, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        if(expr.getClass() == AST.assign.class) {
            VisitNode((AST.assign)expr, clsInfo, scpTbl);
        } else if(expr.getClass() == AST.static_dispatch.class) {
            VisitNode((AST.static_dispatch)expr, clsInfo, scpTbl);
        } else if(expr.getClass() == AST.dispatch.class) {
            VisitNode((AST.dispatch)expr, clsInfo, scpTbl);
        } else if(expr.getClass() == AST.cond.class) {
            VisitNode((AST.cond)expr, clsInfo, scpTbl);
        } else if(expr.getClass() == AST.loop.class) {
            VisitNode((AST.loop)expr, clsInfo, scpTbl);
        } else if(expr.getClass() == AST.block.class) {
            VisitNode((AST.block)expr, clsInfo, scpTbl);
        } else if(expr.getClass() == AST.let.class) {
            VisitNode((AST.let)expr, clsInfo, scpTbl);
        } else if(expr.getClass() == AST.typcase.class) {
            VisitNode((AST.typcase)expr, clsInfo, scpTbl);
        } else if(expr.getClass() == AST.new_.class) {
            VisitNode((AST.new_)expr, clsInfo, scpTbl);
        } else if(expr.getClass() == AST.isvoid.class) {
            VisitNode((AST.isvoid)expr, clsInfo, scpTbl);
        } else if(expr.getClass() == AST.plus.class) {
            VisitNode((AST.plus)expr, clsInfo, scpTbl);
        } else if(expr.getClass() == AST.sub.class) {
            VisitNode((AST.sub)expr, clsInfo, scpTbl);
        } else if(expr.getClass() == AST.mul.class) {
            VisitNode((AST.mul)expr, clsInfo, scpTbl);
        } else if(expr.getClass() == AST.divide.class) {
            VisitNode((AST.divide)expr, clsInfo, scpTbl);
        } else if(expr.getClass() == AST.comp.class) {
            VisitNode((AST.comp)expr, clsInfo, scpTbl);
        } else if(expr.getClass() == AST.lt.class) {
            VisitNode((AST.lt)expr, clsInfo, scpTbl);
        } else if(expr.getClass() == AST.leq.class) {
            VisitNode((AST.leq)expr, clsInfo, scpTbl);
        } else if(expr.getClass() == AST.eq.class) {
            VisitNode((AST.eq)expr, clsInfo, scpTbl);
        } else if(expr.getClass() == AST.neg.class) {
            VisitNode((AST.neg)expr, clsInfo, scpTbl);
        } else if(expr.getClass() == AST.object.class) {
            VisitNode((AST.object)expr, clsInfo, scpTbl);
        } else if(expr.getClass() == AST.int_const.class) {
            VisitNode((AST.int_const)expr, clsInfo, scpTbl);
        } else if(expr.getClass() == AST.string_const.class) {
            VisitNode((AST.string_const)expr, clsInfo, scpTbl);
        } else if(expr.getClass() == AST.bool_const.class) {
            VisitNode((AST.bool_const)expr, clsInfo, scpTbl);
        }
    }

    // visiting the expression that assigns value to a variable ( ID <- expression )
    public void VisitNode(AST.assign asgn, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        VisitNode(asgn.e1, clsInfo, scpTbl);

        AST.attr attrAsgn = scpTbl.lookUpGlobal(asgn.name);

        if(attrAsgn == null) {
            // Means no object was returned by the scope table,
            // So, the variable is undeclared when assigned
            reportError.report(filename, asgn.lineNo, "Variable '" + asgn.name + "' is undeclared during assignment.");
        } else if(clsInfo.isConforming(asgn.e1.type, attrAsgn.typeid) == false) {
            // Means the types of expression don't match during assignment
            reportError.report(filename, asgn.lineNo, "The type '" + attrAsgn.typeid + "' of identifier '" + attrAsgn.name + "' does not match with the type '" + asgn.e1.type + "' of the expression.");
        }

        // The expression's type is updated
        asgn.type = asgn.e1.type;
    }

    // visiting the expression that dispatches a method ( expression.ID([expression [[, expression]]*]) )
    public void VisitNode(AST.dispatch disp, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        AST.method mthd = null;
        boolean found = false;
        
        // Calling the visit node on caller expression
        VisitNode(disp.caller, clsInfo, scpTbl);

        for(AST.expression exp : disp.actuals) {
            // actuals is the list of expressions
            VisitNode(exp, clsInfo, scpTbl);
        }

        // Returns the type class basic block for caller expression of dispatch
        BasicClassBlock bcb = clsInfo.cls.get(disp.caller.type);
        if(bcb == null) {
            // Means no type is returned
            reportError.report(filename, disp.lineNo, "Undefined class '" + disp.caller.type + "' of dispatch caller type.");
        } else {
            if(bcb.mthdList.containsKey(disp.name)) {
                // Means the dispatch method is present in the method list of basic class block
                found = true;
                mthd = bcb.mthdList.get(disp.name);
                
                // Now we will compare the method formal list and number of parameters to the dispatch method
                if(disp.actuals.size() != mthd.formals.size()) {
                    // Different number of parameters in the called dispatch
                    reportError.report(filename, disp.lineNo, "Different number of arguments present in the method '" + mthd.name + "' as compared to the defined method.");
                } else {
                    // The number of parameters match but the type of individual parameter may not be same as defined
                    for(int i=0; i<disp.actuals.size(); i++) {
                        if(clsInfo.isConforming(disp.actuals.get(i).type, mthd.formals.get(i).typeid) == false) {
                            // Means the type does not match
                            reportError.report(filename, disp.lineNo, "The method type '" + disp.actuals.get(i).type + "' does not match with the declared type '" + mthd.formals.get(i).typeid + "' in the method '" + mthd.name + "'.");
                        }
                    } 
                }
            } else {
                // Means the method is not defined in any class
                reportError.report(filename, disp.lineNo, "Method '" + disp.name + "' is undefined.");
            }
        }

        if(found) {
            disp.type = mthd.typeid;
        } else {
            disp.type = "Object";
        }
    }

    // visiting the expression for a static dispatch of a method ( expression@TYPEID.ID([expression [[, expression]]*]) )
    public void VisitNode(AST.static_dispatch stdis, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        AST.method mthd = null;
        boolean found = false;
        
        // Calling the visit node on caller expression
        VisitNode(stdis.caller, clsInfo, scpTbl);

        for(AST.expression exp : stdis.actuals) {
            // actuals is the list of expressions
            VisitNode(exp, clsInfo, scpTbl);
        }

        // Returns the type class basic block for caller expression of dispatch
        BasicClassBlock bcb = clsInfo.cls.get(stdis.typeid);
        if(bcb == null) {
            // Means no type is returned
            reportError.report(filename, stdis.lineNo, "Undefined class '" + stdis.typeid + "' of Static dispatch type.");
        } else if(clsInfo.isConforming(stdis.caller.type, bcb.name) == false) {
            // Means the static dispatch type doesn't match to the expression type
            reportError.report(filename, stdis.lineNo, "The declared static dispatch type '" + bcb.name + "' is different from the expression type '" + stdis.caller.type + "'.");
        } else {
            if(bcb.mthdList.containsKey(stdis.name)) {
                // Means the static dispatch method is present in the method list of basic class block
                found = true;
                mthd = bcb.mthdList.get(stdis.name);
                
                // Now we will compare the method formal list and number of parameters to the static dispatch method
                if(stdis.actuals.size() != mthd.formals.size()) {
                    // Different number of parameters in the called static dispatch
                    reportError.report(filename, stdis.lineNo, "Different number of arguments present in the method '" + mthd.name + "' as compared to the defined method.");
                } else {
                    // The number of parameters match but the type of individual parameter may not be same as defined
                    for(int i=0; i<stdis.actuals.size(); i++) {
                        if(clsInfo.isConforming(stdis.actuals.get(i).type, mthd.formals.get(i).typeid) == false) {
                            // Means the type does not match
                            reportError.report(filename, stdis.lineNo, "The method type '" + stdis.actuals.get(i).type + "' does not match with the declared type '" + mthd.formals.get(i).typeid + "' in the method '" + mthd.name + "'.");
                        }
                    } 
                }
            } else {
                // Means the method is not defined in any class
                reportError.report(filename, stdis.lineNo, "Method '" + stdis.name + "' is undefined.");
            }
        }

        if(found) {
            stdis.type = mthd.typeid;
        } else {
            stdis.type = "Object";
        }
    }

    // visiting if-else expression in the AST( if expression then expression else expression fi ) 
    public void VisitNode(AST.cond cond, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        // Visiting the predicate expression
        VisitNode(cond.predicate, clsInfo, scpTbl);

        if(cond.predicate.type.equals("Bool") == false) {
            // Means the return type of conditional expression is not 'Bool'
            reportError.report(filename, cond.predicate.lineNo, "Predicate return type does not match type 'Bool'");
        }

        // Visiting the if and else body expressions
        VisitNode(cond.ifbody, clsInfo, scpTbl);
        VisitNode(cond.elsebody, clsInfo, scpTbl);

        // The common ancestor class of ifbody expression and elsebody expression is assigned to 'cond' type
        cond.type = clsInfo.commonAncestor(cond.ifbody.type, cond.elsebody.type);
    }

    // visiting the while expression in the AST( while expression loop expression pool )
    public void VisitNode(AST.loop lp, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        // Visiting the predicate expression 
        VisitNode(lp.predicate, clsInfo, scpTbl);

        // The predicate expression type should be Bool, else error!
        if(lp.predicate.type.equals("Bool") == false) {
            reportError.report(filename, lp.predicate.lineNo, "Loop condition's return type is not 'Bool'");
        }

        // Visiting the loop body
        VisitNode(lp.body, clsInfo, scpTbl);
        lp.type = "Object";
    }

    // visiting a block in the AST( { [expression;]+ } )
    public void VisitNode(AST.block blck, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        // Block is an expression list, so we just iterate over all expressions
        // and visit them indidually
        for(AST.expression exp : blck.l1) {
            VisitNode(exp, clsInfo, scpTbl);
        }

        // The type of the last expression is the type of the block
        blck.type = blck.l1.get(blck.l1.size() - 1).type;
    }

    // visiting a let expression in the AST( let ID : TYPEID [ <- expression ] )
    public void VisitNode(AST.let let, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        if(let.value.getClass() != AST.no_expr.class) {
            // Means the let's value member variable is a expression
            VisitNode(let.value, clsInfo, scpTbl);
            if(clsInfo.isConforming(let.value.type, let.typeid) == false) {
                reportError.report(filename, let.lineNo, "The identifier " + let.name + " declared type '" + let.typeid + "' does not match with let value type '" + let.value.type + ".");
            }
        }
        // Entering new scope
        scpTbl.enterScope();
        // Inserting the current let attributes in a new scope 
        scpTbl.insert(let.name, new AST.attr(let.name, let.typeid, let.value, let.lineNo));
        
        // Calling the VisitNode on let body
        VisitNode(let.body, clsInfo, scpTbl);

        let.type = let.body.type;
        // Exiting the scope
        scpTbl.exitScope();
    }

    // visiting case expression in the AST( case expression of [ID : TYPEID => expression;]+ esac )
    public void VisitNode(AST.typcase typcs, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        VisitNode(typcs.predicate, clsInfo, scpTbl);
        for(AST.branch br : typcs.branches) {
            // We are iterating over the branches of the typcase expression
            scpTbl.enterScope();
            BasicClassBlock cl = clsInfo.cls.get(br.type);

            if(cl == null) {
                reportError.report(filename, br.lineNo, "Case branch has undefined type '" + br.type + "'.");
                // To recover from the error, we add this unidentified class
                scpTbl.insert(br.name, new AST.attr(br.name, "Object", br.value, br.lineNo));
            } else {
                scpTbl.insert(br.name, new AST.attr(br.name, br.type, br.value, br.lineNo));
            }

            // Visiting the branch value expression 
            VisitNode(br.value, clsInfo, scpTbl);
            scpTbl.exitScope();
        }

        HashMap <String, Boolean> brnchMap = new HashMap<String, Boolean>();
        AST.branch brn = typcs.branches.get(0);
        String brType = brn.value.type;

        for(AST.branch br : typcs.branches) {
            if(brnchMap.containsKey(br.type) == false) {
                brnchMap.put(br.type, true);
            } else {
                reportError.report(filename, br.lineNo, "Another branch has same type '" + br.type + "'.");
            }
            brType = clsInfo.commonAncestor(brType, br.value.type);
        }

        // Updating the type of typcase with last branch type
        typcs.type = brType;
    }

    // visiting new expression in the AST ( new TYPEID )
    public void VisitNode(AST.new_ nw_, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        // retrieving the class info for class associated with new
        BasicClassBlock bcb = clsInfo.cls.get(nw_.typeid);
        if(bcb == null) {
            // checking if that class exists
            reportError.report(filename, nw_.lineNo, "The class '" + nw_.typeid +"' with 'new' is undefined.");
            nw_.type = "Object";
        } else {
            nw_.type = nw_.typeid;
        }
    }

    // visiting isVoid expression in AST ( isVoid expression )
    public void VisitNode(AST.isvoid isvoid, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        isvoid.type = "Bool";
    }

    // visiting addition expression in AST ( expression + expression )
    public void VisitNode(AST.plus pls, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        // visiting the expression to the left of addition operator
        VisitNode(pls.e1, clsInfo, scpTbl);
        // visiting the expression to the right of addition operator
        VisitNode(pls.e2, clsInfo, scpTbl);

        if(pls.e1.type.equals("Int") == false ) {
            // checking if e1 is not an integer
            reportError.report(filename, pls.lineNo, "The argument in addition is of type non-Int '" + pls.e1.type + "'");
        } 
        if(pls.e2.type.equals("Int") == false) {
            // checking if e2 is not an integer
            reportError.report(filename, pls.lineNo, "The argument in addition is of type non-Int '" + pls.e2.type + "'");
        }
        pls.type = "Int";

    }

    // visiting subtraction expression in AST ( expression - expression )
    public void VisitNode(AST.sub sub, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        // visiting the expression to the left of subtraction operator
        VisitNode(sub.e1, clsInfo, scpTbl);
        // visiting the expression to the right of subtraction operator
        VisitNode(sub.e2, clsInfo, scpTbl);

        if(sub.e1.type.equals("Int") == false) {
            // checking if e1 is not an integer
            reportError.report(filename, sub.lineNo, "The argument in subtraction is of type non-Int '" + sub.e1.type + "'");
        } 
        if(sub.e2.type.equals("Int") == false) {
            // checking if e2 is not an integer
            reportError.report(filename, sub.lineNo, "The argument in subtraction is of type non-Int '" + sub.e2.type + "'");
        }
        sub.type = "Int";

    }

    // visiting multiplication expression in AST ( expression * expression )
    public void VisitNode(AST.mul mul, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        // visiting the expression to the left of multiplication operator
        VisitNode(mul.e1, clsInfo, scpTbl);
        // visiting the expression to the right of multiplication operator
        VisitNode(mul.e2, clsInfo, scpTbl);

        if(mul.e1.type.equals("Int") == false ) {
            // checking if e1 is not an integer
            reportError.report(filename, mul.lineNo, "The argument in multiplication is of type non-Int '" + mul.e1.type + "'");
        } 
        if(mul.e2.type.equals("Int") == false) {
            // checking if e2 is not an integer
            reportError.report(filename, mul.lineNo, "The argument in multiplication is of type non-Int '" + mul.e2.type + "'");
        }
        mul.type = "Int";

    }

    // visiting division  expression in AST ( expression / expression )
    public void VisitNode(AST.divide divide, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        // visiting the expression to the left of division operator
        VisitNode(divide.e1, clsInfo, scpTbl);
        // visiting the expression to the right of division operator
        VisitNode(divide.e2, clsInfo, scpTbl);

        if(divide.e1.type.equals("Int") == false ) {
            // checking if e1 is not an integer
            reportError.report(filename, divide.lineNo, "The argument in division is of type non-Int '" + divide.e1.type + "'");
        } 
        if(divide.e2.type.equals("Int") == false) {
            // checking if e2 is not an integer
            reportError.report(filename, divide.lineNo, "The argument in division is of type non-Int '" + divide.e2.type + "'");
        }
        divide.type = "Int";
        
    }
    
    
    // visiting less than expression in AST ( expression < expression )
    public void VisitNode(AST.lt lt, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        // visiting the expression to the left of less-than operator
        VisitNode(lt.e1, clsInfo, scpTbl);
        // visiting the expression to the right of less-than operator
        VisitNode(lt.e2, clsInfo, scpTbl);
        
        if(lt.e1.type.equals("Int") == false ) {
            // checking if e1 is not an integer
            reportError.report(filename, lt.lineNo, "The argument in less-than is of type non-Int '" + lt.e1.type + "'");
        } else if(lt.e2.type.equals("Int") == false) {
            // checking if e2 is not an integer
            reportError.report(filename, lt.lineNo, "The argument in less-than is of type non-Int '" + lt.e2.type + "'");
        }
        lt.type = "Bool";
    }
    
    // visiting less than equal to expression in AST ( expression <= expression )
    public void VisitNode(AST.leq leq, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        // visiting the expression to the left of less-than-equalto operator
        VisitNode(leq.e1, clsInfo, scpTbl);
        // visiting the expression to the right of less-than-equalto operator
        VisitNode(leq.e2, clsInfo, scpTbl);
        
        if(leq.e1.type.equals("Int") == false ) {
            // checking if e1 is not an integer
            reportError.report(filename, leq.lineNo, "The argument in less-than equal to is of type non-Int '" + leq.e1.type + "'");
        } else if(leq.e2.type.equals("Int") == false) {
            // checking if e2 is not an integer
            reportError.report(filename, leq.lineNo, "The argument in less-than equal to is of type non-Int '" + leq.e2.type + "'");
        }
        leq.type = "Bool";
    }
    
    // visiting equal to expression in AST ( expression = expression )
    public void VisitNode(AST.eq eq, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        // visiting the expression to the left of equalto operator
        VisitNode(eq.e1, clsInfo, scpTbl);
        // visiting the expression to the right of equalto operator
        VisitNode(eq.e2, clsInfo, scpTbl);
        
		if((eq.e1.type.equals("String") || eq.e1.type.equals("Int") || eq.e1.type.equals("Bool")) || (eq.e2.type.equals("String") || eq.e2.type.equals("Int") || eq.e2.type.equals("Bool"))) {
            if(eq.e1.type.equals(eq.e2.type) == false) {
                reportError.report(filename, eq.lineNo, "The argument in equals have different types '" + eq.e1.type + "'"+ " and '" + eq.e2.type + "'.");
			}
		}
        eq.type = "Bool";
    }
    
    // visiting compliment expression in AST ( not expression )
    public void VisitNode(AST.comp comp, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        // visiting the expression to be complimented 
        VisitNode(comp.e1, clsInfo, scpTbl);
        if(comp.e1.type.equals("Int") == false) {
            // Checking if the expression to be complimented is of type Int
            reportError.report(filename, comp.lineNo, "In the compliment, argument must have type Int instead of type '" + comp.e1.type + "'.");
        }
        comp.type = "Int";
    }

    // visiting negation expression in AST ( ~ expression )
    public void VisitNode(AST.neg neg, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        // visiting the expression to be negated 
        VisitNode(neg.e1, clsInfo, scpTbl);
        if(neg.e1.type.equals("Bool") == false) {
            // Checking if the expression to be negated is of type Bool
            reportError.report(filename, neg.lineNo, "In the negation, argument must have type 'Bool' instead of type '" + neg.e1.type + "'.");
        }
        neg.type = "Bool";
    }

    // visiting Objectid expression ( ID )
    public void VisitNode(AST.object obj, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        // Searching for the attributes of obj in the scope table globally
        AST.attr att = scpTbl.lookUpGlobal(obj.name);
        if(att == null) {
            // If obj doesn't exist in the scope table
            reportError.report(filename, obj.lineNo, "The identifier '" + obj.name + "' is undeclared.");
            // To recover from the error 
            obj.type = "Object";
        } else {
            obj.type = att.typeid;
        }
    }

    // visiting integer constant expression in AST 
    public void VisitNode(AST.int_const int_const, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        int_const.type = "Int";
    }

    // visiting string constant expression in AST 
    public void VisitNode(AST.string_const string_const, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        string_const.type = "String";
    }

    // visiting boolean constant expression in AST 
    public void VisitNode(AST.bool_const bool_const, ClassInfo clsInfo, ScopeTable<AST.attr> scpTbl) {
        bool_const.type = "Bool";        
    }
}