package cool;

import java.util.*;
import java.io.PrintWriter;

// This class matches the data types stored in enum to their corresponding IR versions
public class TypeMapping {
    enum TypeID {
        EMPTY, VOID, INT1, INT1PTR, INT1DOUBLEPTR, INT8, INT8PTR, INT8DOUBLEPTR, INT32, INT32PTR, INT32DOUBLEPTR, VARARG, OBJ, OBJPTR, OBJDOUBLEPTR
    }

    public TypeID id = TypeID.EMPTY;
    public String name = "";

    public TypeMapping(TypeID i) {
        id = i;
        if(id == TypeID.EMPTY) {
            name = "";
        } else if(id == TypeID.VOID) {
            name = "void";
        } else if(id == TypeID.INT1) {
            name = "i1";
        } else if(id == TypeID.INT8) {
            name = "i8";
        } else if(id == TypeID.INT32) {
            name = "i32";
        } else if(id == TypeID.INT1PTR) {
            name = "i1*";
        } else if(id == TypeID.INT8PTR) {
            name = "i8*";
        } else if(id == TypeID.INT32PTR) {
            name = "i32*";
        } else if(id == TypeID.INT1DOUBLEPTR) {
            name = "i1**";
        } else if(id == TypeID.INT8DOUBLEPTR) {
            name = "i8**";
        } else if(id == TypeID.INT32DOUBLEPTR) {
            name = "i32**";
        } else if(id == TypeID.VARARG) {
            name = "...";
        }
    }

    // The user defined types are mapped to Object type
    public TypeMapping(String n) {
        id = TypeID.OBJ;
        name = "%" + n;
    }

    // Adds the number of pointers to the user defined type according to the value of NumPointers
    public TypeMapping(String n, int numPointers) {
        id = TypeID.OBJ;
        name = "%" + n;
        // Pointer to an object
        if (numPointers == 1) {
            id = TypeID.OBJPTR;
            name += "*";
        } else if(numPointers == 2) {
            id = TypeID.OBJDOUBLEPTR;
            name += "**";
        }
    }
    
    // Checks whether a type id is a single or double pointer
    public boolean isPtr() {
        return ((id == TypeID.INT1PTR) || (id == TypeID.INT8PTR) ||
                (id == TypeID.INT32PTR) || (id == TypeID.OBJPTR));
    }

    // This function returns a TypeMapping object corresponding to the pointer type of the current type
    public TypeMapping correspondingPtrType() {
        TypeID pointerType = TypeID.EMPTY;
        if(id == TypeID.INT1) {
            pointerType = TypeID.INT1PTR;
        } else if(id == TypeID.INT8) {
            pointerType = TypeID.INT8PTR;
        } else if(id == TypeID.INT32) {
            pointerType = TypeID.INT32PTR;
        } else if(id == TypeID.INT1PTR) {
            pointerType = TypeID.INT1DOUBLEPTR;
        } else if(id == TypeID.INT8PTR) {
            pointerType = TypeID.INT8DOUBLEPTR;
        } else if(id == TypeID.INT32PTR) {
            pointerType = TypeID.INT32DOUBLEPTR;
        } else if(id == TypeID.OBJ) {
            pointerType = TypeID.OBJPTR;
        } else if(id == TypeID.OBJPTR) {
            pointerType = TypeID.OBJDOUBLEPTR;
        }
        if (pointerType == TypeID.OBJPTR || pointerType == TypeID.OBJDOUBLEPTR) {
            // We need to remove '%' sign in case of Object pointer type
            TypeMapping tempType = new TypeMapping(name.substring(1), 1);
            tempType.id = pointerType;
            return tempType;
        }
        return (new TypeMapping(pointerType));
    }

    public TypeMapping dereferencedPtrType() {
        TypeID derefPointerType = TypeID.EMPTY;
        if(id == TypeID.INT1PTR) {
            derefPointerType = TypeID.INT1;
        } else if(id == TypeID.INT8PTR) {
            derefPointerType = TypeID.INT8;
        } else if(id == TypeID.INT32PTR) {
            derefPointerType = TypeID.INT32;
        } else if(id == TypeID.INT1DOUBLEPTR) {
            derefPointerType = TypeID.INT1PTR;
        } else if(id == TypeID.INT8DOUBLEPTR) {
            derefPointerType = TypeID.INT8PTR;
        } else if(id == TypeID.INT32DOUBLEPTR) {
            derefPointerType = TypeID.INT32PTR;
        } else if(id == TypeID.OBJPTR) {
            derefPointerType = TypeID.OBJ;
        } else if(id == TypeID.OBJDOUBLEPTR) {
            derefPointerType = TypeID.OBJPTR;
        }
        if (derefPointerType == TypeID.OBJ || derefPointerType == TypeID.OBJPTR) {
            // If derefernced type is OBJ or OBJPTR, we need to remove % from start and * from last
            TypeMapping tempType = new TypeMapping(name.substring(1, name.length() - 1));
            tempType.id = derefPointerType;
            return tempType;
        }
        return (new TypeMapping(derefPointerType));
    }

    public boolean isDoublePtr() {
        return ((id == TypeID.INT1DOUBLEPTR) || (id == TypeID.INT8DOUBLEPTR) ||
                (id == TypeID.INT32DOUBLEPTR) || (id == TypeID.OBJDOUBLEPTR));
    }
}
