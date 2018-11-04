; I am a comment in LLVM-IR. Feel free to remove me.
source_filename = "test5.cl"

target datalayout = "e-m:e-i64:64-f80:128-n8:16:32:64-S128"

target triple = "x86_64-pc-linux-gnu"

@divby0err = private unnamed_addr constant [31 x i8] c"Runtime Error: Divide by Zero\0A\00"
@staticdispatchonvoiderr = private unnamed_addr constant [47 x i8] c"Runtime Error: Static Dispatch on void object\0A\00"

@strfmt = private unnamed_addr constant [3 x i8] c"%s\00"
@intfmt = private unnamed_addr constant [3 x i8] c"%d\00"
@.str.empty = private unnamed_addr constant [1 x i8] c"\00"

declare i8* @strcat( i8*, i8* )
declare i8* @strcpy( i8*, i8* )
declare i32 @strcmp( i8*, i8* )
declare i8* @strncpy( i8*, i8*, i32 )
declare i32 @strlen( i8* )
declare i32 @printf( i8*, ... )
declare i32 @scanf( i8*, ... )
declare i8* @malloc( i32 )
declare void @exit( i32 )


define i32 @main(  ) {
entry:
	%obj = alloca %class.Main
	%obj1 = call %class.Main* @Main_Cons_Main( %class.Main* %obj )
	%0 = call i32 @Main_main( %class.Main* %obj1 )
	ret i32 0
}
%class.Main = type {  }

define %class.Main* @Main_Cons_Main( %class.Main* %this ) {
entry:
	%this.addr = alloca %class.Main*
	store %class.Main* %this, %class.Main** %this.addr
	%this1 = load %class.Main*, %class.Main** %this.addr
	ret %class.Main* %this1
}

define i32 @Main_main( %class.Main* %this ) {
entry:
	%retval = alloca i32
	%this.addr = alloca %class.Main*
	store %class.Main* %this, %class.Main** %this.addr
	%this1 = load %class.Main*, %class.Main** %this.addr
	%0 = alloca i32
	store i32 0, i32* %0
	%1 = load i32, i32* %0
	store i32 %1, i32* %retval
	br label %fun_returning_basic_block
dispatch_on_void_basic_block:
	%err_msg_void_dispatch = alloca i8*
	store i8* getelementptr inbounds ([47 x i8], [47 x i8]* @staticdispatchonvoiderr, i32 0, i32 0), i8** %err_msg_void_dispatch
	%print_err_msg_void_dispatch = load i8*, i8** %err_msg_void_dispatch
	call void @IO_out_string( i8* %print_err_msg_void_dispatch )
	call void @Object_abort(  )
	br label %fun_returning_basic_block
func_div_by_zero_abort:
	%err_msg = alloca i8*
	store i8* getelementptr inbounds ([31 x i8], [31 x i8]* @divby0err, i32 0, i32 0), i8** %err_msg
	%print_err_msg = load i8*, i8** %err_msg
	call void @IO_out_string( i8* %print_err_msg )
	call void @Object_abort(  )
	br label %fun_returning_basic_block
fun_returning_basic_block:
	%2 = load i32, i32* %retval
	ret i32 %2
}
%class.A = type { i32 }

define %class.A* @A_Cons_A( %class.A* %this ) {
entry:
	%this.addr = alloca %class.A*
	store %class.A* %this, %class.A** %this.addr
	%this1 = load %class.A*, %class.A** %this.addr
	%a = getelementptr inbounds %class.A, %class.A* %this1, i32 0, i32 0
	%0 = alloca i32
	store i32 5, i32* %0
	%1 = load i32, i32* %0
	store i32 %1, i32* %a
	ret %class.A* %this1
}
@.str.0 = private unnamed_addr constant [4 x i8] c"ABC\00"
%class.B = type { i8*, i1 }

define %class.B* @B_Cons_B( %class.B* %this ) {
entry:
	%this.addr = alloca %class.B*
	store %class.B* %this, %class.B** %this.addr
	%this1 = load %class.B*, %class.B** %this.addr
	%a = getelementptr inbounds %class.B, %class.B* %this1, i32 0, i32 0
	%0 = alloca i8*
	store i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.0, i32 0, i32 0), i8** %0	%1 = load i8*, i8** %0
	store i8* %1, i8** %a
	%c = getelementptr inbounds %class.B, %class.B* %this1, i32 0, i32 1
	%2 = alloca i1
	store i1 true, i1* %2
	%3 = load i1, i1* %2
	store i1 %3, i1* %c
	ret %class.B* %this1
}
%class.C = type { i32 }

define %class.C* @C_Cons_C( %class.C* %this ) {
entry:
	%this.addr = alloca %class.C*
	store %class.C* %this, %class.C** %this.addr
	%this1 = load %class.C*, %class.C** %this.addr
	%a = getelementptr inbounds %class.C, %class.C* %this1, i32 0, i32 0
	store i32 0, i32* %a
	ret %class.C* %this1
}

define i32 @C_foo( %class.C* %this ) {
entry:
	%retval = alloca i32
	%this.addr = alloca %class.C*
	store %class.C* %this, %class.C** %this.addr
	%this1 = load %class.C*, %class.C** %this.addr
	%a = getelementptr inbounds %class.C, %class.C* %this1, i32 0, i32 0
	%0 = alloca i32
	store i32 5, i32* %0
	%1 = load i32, i32* %0
	store i32 %1, i32* %a
	%2 = alloca i32
	store i32 0, i32* %2
	%3 = load i32, i32* %2
	store i32 %3, i32* %retval
	br label %fun_returning_basic_block
dispatch_on_void_basic_block:
	%err_msg_void_dispatch = alloca i8*
	store i8* getelementptr inbounds ([47 x i8], [47 x i8]* @staticdispatchonvoiderr, i32 0, i32 0), i8** %err_msg_void_dispatch
	%print_err_msg_void_dispatch = load i8*, i8** %err_msg_void_dispatch
	call void @IO_out_string( i8* %print_err_msg_void_dispatch )
	call void @Object_abort(  )
	br label %fun_returning_basic_block
func_div_by_zero_abort:
	%err_msg = alloca i8*
	store i8* getelementptr inbounds ([31 x i8], [31 x i8]* @divby0err, i32 0, i32 0), i8** %err_msg
	%print_err_msg = load i8*, i8** %err_msg
	call void @IO_out_string( i8* %print_err_msg )
	call void @Object_abort(  )
	br label %fun_returning_basic_block
fun_returning_basic_block:
	%4 = load i32, i32* %retval
	ret i32 %4
}

define void @Object_abort(  ) {
entry:
	call void (i32) @exit(i32 0)
	ret void
}

%class.Object = type {  }

define %class.Object* @Object_Cons_Object( %class.Object* %this ) {
entry:
	%this.addr = alloca %class.Object*
	store %class.Object* %this, %class.Object** %this.addr
	%this1 = load %class.Object*, %class.Object** %this.addr
	ret %class.Object* %this1
}

define i32 @IO_in_int(  ) {
entry:
	%0 = bitcast [3 x i8]* @intfmt to i8*
	%1 = call i8* @malloc( i32 4 )
	%2 = bitcast i8* %1 to i32*
	%3 = call i32 (i8*, ...)  @scanf( i8* %0, i32* %2 )
	%retval = load i32, i32* %2
	ret i32 %retval
}

define i8* @IO_in_string(  ) {
entry:
	%0 = bitcast [3 x i8]* @strfmt to i8*
	%retval = call i8* @malloc( i32 1024 )
	%1 = call i32 (i8*, ...)  @scanf( i8* %0, i8* %retval )
	ret i8* %retval
}

define void @IO_out_int( i32 %given ) {
entry:
	%0 = getelementptr inbounds [3 x i8], [3 x i8]* @intfmt, i32 0, i32 0
	%call = call i32 ( i8*, ... ) @printf(i8* %0, i32 %given)
	ret void
}


define void @IO_out_string( i8* %given ) {
entry:
	%0 = getelementptr inbounds [3 x i8], [3 x i8]* @strfmt, i32 0, i32 0
	%call = call i32 ( i8*, ... ) @printf(i8* %0, i8* %given)
	ret void
}

%class.IO = type {  }

define %class.IO* @IO_Cons_IO( %class.IO* %this ) {
entry:
	%this.addr = alloca %class.IO*
	store %class.IO* %this, %class.IO** %this.addr
	%this1 = load %class.IO*, %class.IO** %this.addr
	ret %class.IO* %this1
}

define i32 @String_length( i8* %this ) {
entry:
	%retval = call i32 @strlen( i8* %this )
	ret i32 %retval
}

define i8* @String_concat( i8* %this, i8* %that ) {
entry:
	%memnew = call i8* @malloc( i32 1024 )
	%copystring = call i8* @strcpy( i8* %memnew, i8* %this )
	%retval = call i8* @strcat( i8* %copystring, i8* %that )
	ret i8* %retval
}

define i8* @String_substr( i8* %this, i32 %start, i32 %len ) {
entry:
	%0 = call i8* @malloc( i32 1024 )
	%1 = getelementptr inbounds i8, i8* %this, i32 %start
	%2 = call i8* @strncpy( i8* %0, i8* %1, i32 %len )
	%3 = getelementptr inbounds [1 x i8], [1 x i8]* @.str.empty, i32 0, i32 0
	%retval = call i8* @strcat( i8* %2, i8* %3 )
	ret i8* %retval
}

define i1 @String_strcmp( i8* %this, i8* %start ) {
entry:
	%0 = call i32 @strcmp( i8* %this, i8* %start )
	%1 = icmp eq i32 %0, 0
	ret i1 %1
}
