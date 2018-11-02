package cool;

import java.io.PrintWriter;
import java.util.*;

import cool.AST;
import cool.BasicClassBlockCG;
import cool.BuildInheritanceGraph;

public class GenerateLlvm {

    public void printMethodsOfClass(BasicClassBlockCG bcb , PrintWriter out) {
        out.print("@_ZTV" + bcb.name.length() + bcb.name + " = constant " + "[" + 
                  bcb.pmList.size() + " x i8*] [");
        for(int i = 0; i < bcb.pmList.size(); i++) {
            if(i > 0)   out.print(", ");
            AST.method mthd = bcb.pmList.get(i);
            out.print("i8* bitcast (");
            String typeid; 
            if(mthd.typeid == "Int" )            typeid = "i32";
            else if(mthd.typeid == "String" )    typeid = "[1024 x i8]*";
            else if(mthd.typeid == "Bool" )      typeid = "i8";
            else  typeid =  "%class." + mthd.typeid + "*";
            out.print(typeid);
            out.print(" ( ");
            for(int j = 0; j < mthd.formals.size(); j++) {
                AST.formal form = mthd.formals.get(j);
                if(j > 0)   out.print(", ");
                String typeid1; 
                if(form.typeid == "Int")            typeid1 = "i32";
                else if(form.typeid == "String")    typeid1 = "[1024 x i8]*";
                else if(form.typeid == "Bool")      typeid1 = "i8";
                else  typeid1 =  "%class." + form.typeid + "*";
                out.print(typeid1);
            }
            out.print(" )* ");
            out.print(bcb.llvmIrName.get(mthd.name));
            out.print(" to i8*)");
        }
        out.print("] \n");
    }

    public void utilPrintMethodsOfClass(ClassInfoCG clsInfoCG, Queue<Integer> q, PrintWriter out) {
        while(q.isEmpty() == false) {
            int firstClass = q.poll();
            // if(indexToClassNameMap.size() > 0)
            //     System.out.println("here\n");
            printMethodsOfClass(clsInfoCG.cls.get(BuildInheritanceGraphCG.indexToClassNameMap.get(firstClass)), out);
            for(Integer child : BuildInheritanceGraphCG.graph.get(firstClass)) {
                q.offer(child);
            }
        }
    }

    public void printBaseConstructor(BasicClassBlockCG bcb, PrintWriter out) {
        out.println("define void @_Z" + bcb.name.length() + bcb.name + "BaseC ( %" + bcb.name + ".Base*%this) { \nentry: ");
        
        for(int i=0; i<bcb.paList.size(); i++) {
            AST.attr attrTemp = bcb.paList.get(i);
            out.println("\t%" + i + " = getelementptr inbounds %class." + attrTemp.typeid + ".Base, class." + attrTemp.typeid + ".Base* %this, i32 0, i32 " + i);
            out.println("\tcall void @_Z" + attrTemp.typeid.length() + attrTemp.typeid + "BaseC( %class." + attrTemp.typeid + ".Base*%" + i + ")");
        }
        out.println("\tret void\n}\n");
    }

    public void generateMethodsOfClass(BasicClassBlockCG bcb, PrintWriter out) {
        if(bcb.name.equals("Int") || bcb.name.equals("Bool")) {
            return;
        } else if(bcb.name.equals("String")) {
            /* 
                Printing default String Methods
            */
            // LLVM IR translation for concat method
            out.println("define [1024 x i8]* @_ZN6String6concat( [1024 x i8]* %this, [1024 x i8]* %that ) {\nentry:\n\t%retval = call [1024 x i8]* @_ZN6String4copy( [1024 x i8]* %this )\n\t%0 = bitcast [1024 x i8]* %retval to i8*\n\t%1 = bitcast [1024 x i8]* %that to i8*\n\t%2 = call i8* @strcat( i8* %0, i8* %1 )\n\tret [1024 x i8]* %retval\n}\n");
            // LLVM IR translation for copy method
            out.println("define [1024 x i8]* @_ZN6String4copy( [1024 x i8]* %this ) {\nentry:\n\t%0 = call i8* @malloc( i64 1024 )\n\t%retval = bitcast i8* %0 to [1024 x i8]*\n\t%1 = bitcast [1024 x i8]* %this to i8*\n\t%2 = bitcast [1024 x i8]* %retval to i8*\n\t%3 = call i8* @strcpy( i8* %2, i8* %1)\n\tret [1024 x i8]* %retval\n}\n");
            // LLVM IR translation for length method
            out.println("define i32 @_ZN6String6length( [1024 x i8]* %this ) {\nentry:\n%0 = bitcast [1024 x i8]* %this to i8*\n\t%1 = call i64 @strlen( i8* %0 )\n\t%retval = trunc i64 %1 to i32\n\tret i32 %retval\n}\n");
            // LLVM IR translation for substr method
            out.println("define [1024 x i8]* @_ZN6String6substr( [1024 x i8]* %this, i32 %start, i32 %len ) {\nentry:\n\t%0 = getelementptr inbounds [1024 x i8], [1024 x i8]* %this, i32 0, i32 %start\n\t%1 = call i8* @malloc( i64 1024 )\n\t%retval = bitcast i8* %1 to [1024 x i8]*\n\t%2 = bitcast [1024 x i8]* %retval to i8*\n\t%3 = call i8* @strncpy( i8* %2, i8* %0, i32 %len )\n\t%4 = getelementptr inbounds [1024 x i8], [1024 x i8]* %retval, i32 0, i32 %len\n\tstore i8 0, i8* %4\n\tret [1024 x i8]* %retval\n}\n");
        } else if(bcb.name.equals("Object")) {
            /*
                Printing methods for Object class
            */
            // LLVM IR translation for object abort method
            out.println("define %classObject* @_ZN6Object5abort( %class.Object* %this ) noreturn {\nentry:\n\tcall void @exit( i32 1 )\n\tret %classObject* null\n}\n");
            // LLVM IR translation for object base method
            out.println("define void @_Z6ObjectBaseC ( %class.Object.Base ) {\nentry:\n\tret void\n}\n");
            // LLVM IR translation for object typename method
            out.println("define [1024 x i8]* @_ZN6Object9type_name( %class.Object* %this ) {\nentry:\n\t%0 = getelementptr inbounds %classObject, %classObject* %this, i32 0, i32 0\n\t%1 = load i32, i32* %0\n\t%2 = getelementptr inbounds [8 x [1024 x i8]], [8 x [1024 x i8]]* @classnames, i32 0, i32 %1\n\t%retval = call [1024 x i8]* @_ZN6String4copy( [1024 x i8]* %2 )\n\tret [1024 x i8]* %retval\n}\n");
        } else if(bcb.name.equals("IO")) {
            /*
                Printing methods for IO class
            */
            // LLVM IR translation for io out_string method
            out.println("define %classIO* @_ZN2IO10out_string( %class.IO* %this, [1024 x i8]* %str ) {\nentry:\n\t%0 = call i32 (i8*, ...) @printf( i8* bitcast ( [3 x i8]* @strformatstr to i8* ), [1024 x i8]* %str )\n\tret %classIO* %this\n}\n");
            // LLVM IR translation for io out_int method
            out.println("define %classIO* @_ZN2IO7out_int( %class.IO* %this, i32 %int ) {\nentry:\n\t%0 = call i32 (i8*, ...) @printf( i8* bitcast ( [3 x i8]* @intformatstr to i8* ), i32 %int )\n\tret %classIO* %this\n}\n");
            // LLVM IR translation for io in_string method
            out.println("define [1024 x i8]* @_ZN2IO9in_string( %class.IO* %this ) {\nentry:\n\t%0 = call i8* @malloc( i64 1024 )\n\t%retval = bitcast i8* %0 to [1024 x i8]*\n\t%1 = call i32 (i8*, ...) @scanf( i8* bitcast ( [3 x i8]* @strformatstr to i8* ), [1024 x i8]* %retval )\n\tret [1024 x i8]* %retval\n}\n");
            // LLVM IR translation for io in_int method
            out.println("define i32 @_ZN2IO6in_int( %class.IO* %this ) {\nentry:\n\t%0 = call i8* @malloc( i64 4 )\n\t%1 = bitcast i8* %0 to i32*\n\t%2 = call i32 (i8*, ...) @scanf( i8* bitcast ( [3 x i8]* @intformatstr to i8* ), i32* %1 )\n\t%retval = load i32, i32* %1\n\tret i32 %retval\n}\n");
            printBaseConstructor(bcb, out);
        } else {
            printBaseConstructor(bcb, out);
        }
    }

    public void utilGenerateMethodsOfClass(ClassInfoCG clsInfoCG, Queue<Integer> q, PrintWriter out) {
        while(q.isEmpty() == false) {
            int firstClass = q.poll();
            generateMethodsOfClass(clsInfoCG.cls.get(BuildInheritanceGraphCG.indexToClassNameMap.get(firstClass)), out);
            for(Integer child : BuildInheritanceGraphCG.graph.get(firstClass)) {
                q.offer(child);
            }
        }
    }
}