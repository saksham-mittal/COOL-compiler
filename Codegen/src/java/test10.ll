; I am a comment in LLVM-IR. Feel free to remove me.
source_filename = "test10.cl"

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
	call void @Main_main( %class.Main* %obj1 )
	ret i32 0
}
@.str.0 = private unnamed_addr constant [7 x i8] c"cs3423\00"
%class.Main = type { i32, i1, i8*, %class.IO* }

define %class.Main* @Main_Cons_Main( %class.Main* %this ) {
entry:
	%this.addr = alloca %class.Main*
	store %class.Main* %this, %class.Main** %this.addr
	%this1 = load %class.Main*, %class.Main** %this.addr
	%i = getelementptr inbounds %class.Main, %class.Main* %this1, i32 0, i32 0
	store i32 0, i32* %i
	%k = getelementptr inbounds %class.Main, %class.Main* %this1, i32 0, i32 1
	%0 = alloca i1
	store i1 true, i1* %0
	%1 = load i1, i1* %0
	store i1 %1, i1* %k
	%j = getelementptr inbounds %class.Main, %class.Main* %this1, i32 0, i32 2
	%2 = alloca i8*
	store i8* getelementptr inbounds ([7 x i8], [7 x i8]* @.str.0, i32 0, i32 0), i8** %2	%3 = load i8*, i8** %2
	store i8* %3, i8** %j
	%l = getelementptr inbounds %class.Main, %class.Main* %this1, i32 0, i32 3
	%4 = alloca %class.IO
	%5 = call %class.IO* @IO_Cons_IO( %class.IO* %4 )
	store %class.IO* %5, %class.IO** %l
	ret %class.Main* %this1
}
@.str.1 = private unnamed_addr constant [13 x i8] c"Enter int : \00"
@.str.2 = private unnamed_addr constant [7 x i8] c"cs3423\00"
@.str.3 = private unnamed_addr constant [7 x i8] c"CS3423\00"
@.str.4 = private unnamed_addr constant [39 x i8] c"The computer has some serious problems\00"
@.str.5 = private unnamed_addr constant [45 x i8] c"The computer has some really serious problem\00"
@.str.6 = private unnamed_addr constant [9 x i8] c"Good boy\00"
@.str.7 = private unnamed_addr constant [26 x i8] c"That was a trick question\00"
@.str.8 = private unnamed_addr constant [6 x i8] c"Oh oh\00"

define void @Main_main( %class.Main* %this ) {
entry:
	%this.addr = alloca %class.Main*
	store %class.Main* %this, %class.Main** %this.addr
	%this1 = load %class.Main*, %class.Main** %this.addr
	%i = getelementptr inbounds %class.Main, %class.Main* %this1, i32 0, i32 0
	%k = getelementptr inbounds %class.Main, %class.Main* %this1, i32 0, i32 1
	%j = getelementptr inbounds %class.Main, %class.Main* %this1, i32 0, i32 2
	%l = getelementptr inbounds %class.Main, %class.Main* %this1, i32 0, i32 3
	%0 = load %class.IO*, %class.IO** %l
	%1 = icmp eq %class.IO* null, %0
	br i1 %1, label %dispatch_on_void_basic_block, label %proceed_1

proceed_1:
	%2 = alloca i8*
	store i8* getelementptr inbounds ([13 x i8], [13 x i8]* @.str.1, i32 0, i32 0), i8** %2	%3 = load i8*, i8** %2
	%4 = load %class.IO*, %class.IO** %l
	call void @IO_out_string( i8* %3 )
	%5 = load %class.IO*, %class.IO** %l
	%6 = icmp eq %class.IO* null, %5
	br i1 %6, label %dispatch_on_void_basic_block, label %proceed_6

proceed_6:
	%7 = load %class.IO*, %class.IO** %l
	%8 = call i32 @IO_in_int(  )
	store i32 %8, i32* %i
	%9 = load i32, i32* %i
	%10 = alloca i32
	store i32 8, i32* %10
	%11 = load i32, i32* %10
	%12 = mul i32 %9, %11
	%13 = alloca i32
	store i32 9, i32* %13
	%14 = load i32, i32* %13
	%15 = alloca i32
	store i32 7, i32* %15
	%16 = load i32, i32* %15
	%17 = mul i32 %14, %16
	%18 = add i32 %12, %17
	store i32 %18, i32* %i
	%19 = load i32, i32* %i
	%20 = mul i32 %19, -1
	store i32 %20, i32* %i
	%21 = load i32, i32* %i
	%22 = alloca i32
	store i32 3, i32* %22
	%23 = load i32, i32* %22
	%comp_23_0 = icmp eq i32 %23, 0
	br i1 %comp_23_0, label %func_div_by_zero_abort, label %proceed_23_0

proceed_23_0:
	%24 = udiv i32 %21, %23
	%25 = alloca i32
	store i32 4, i32* %25
	%26 = load i32, i32* %25
	%27 = sub i32 %24, %26
	store i32 %27, i32* %i
	%28 = load i1, i1* %k
	%29 = xor i1 %28, true
	store i1 %29, i1* %k
	%30 = load i1, i1* %k
	%31 = xor i1 %30, true
	store i1 %31, i1* %k
	%32 = alloca i8*
	store i8* getelementptr inbounds ([7 x i8], [7 x i8]* @.str.2, i32 0, i32 0), i8** %32	%33 = load i8*, i8** %32
	%34 = load i8*, i8** %j
	%35 = call i1 @String_strcmp( i8* %33, i8* %34 )
	store i1 %35, i1* %k
	%36 = load i1, i1* %k
	br i1 %36, label %if.then0, label %if.else0

if.then0:
	%37 = load %class.IO*, %class.IO** %l
	%38 = icmp eq %class.IO* null, %37
	br i1 %38, label %dispatch_on_void_basic_block, label %proceed_38

proceed_38:
	%39 = alloca i8*
	store i8* getelementptr inbounds ([7 x i8], [7 x i8]* @.str.3, i32 0, i32 0), i8** %39	%40 = load i8*, i8** %39
	%41 = load %class.IO*, %class.IO** %l
	call void @IO_out_string( i8* %40 )
	br label %if.end0


if.else0:
	%42 = load %class.IO*, %class.IO** %l
	%43 = icmp eq %class.IO* null, %42
	br i1 %43, label %dispatch_on_void_basic_block, label %proceed_43

proceed_43:
	%44 = alloca i8*
	store i8* getelementptr inbounds ([39 x i8], [39 x i8]* @.str.4, i32 0, i32 0), i8** %44	%45 = load i8*, i8** %44
	%46 = load %class.IO*, %class.IO** %l
	call void @IO_out_string( i8* %45 )
	br label %if.end0


if.end0:
	%47 = alloca i32
	store i32 3, i32* %47
	%48 = load i32, i32* %47
	%49 = alloca i32
	store i32 2, i32* %49
	%50 = load i32, i32* %49
	%51 = icmp slt i32 %48, %50
	store i1 %51, i1* %k
	%52 = load i1, i1* %k
	br i1 %52, label %if.then1, label %if.else1

if.then1:
	%53 = load %class.IO*, %class.IO** %l
	%54 = icmp eq %class.IO* null, %53
	br i1 %54, label %dispatch_on_void_basic_block, label %proceed_54

proceed_54:
	%55 = alloca i8*
	store i8* getelementptr inbounds ([45 x i8], [45 x i8]* @.str.5, i32 0, i32 0), i8** %55	%56 = load i8*, i8** %55
	%57 = load %class.IO*, %class.IO** %l
	call void @IO_out_string( i8* %56 )
	br label %if.end1


if.else1:
	%58 = load %class.IO*, %class.IO** %l
	%59 = icmp eq %class.IO* null, %58
	br i1 %59, label %dispatch_on_void_basic_block, label %proceed_59

proceed_59:
	%60 = alloca i8*
	store i8* getelementptr inbounds ([9 x i8], [9 x i8]* @.str.6, i32 0, i32 0), i8** %60	%61 = load i8*, i8** %60
	%62 = load %class.IO*, %class.IO** %l
	call void @IO_out_string( i8* %61 )
	br label %if.end1


if.end1:
	%63 = alloca i32
	store i32 5, i32* %63
	%64 = load i32, i32* %63
	%65 = alloca i32
	store i32 5, i32* %65
	%66 = load i32, i32* %65
	%67 = icmp sle i32 %64, %66
	store i1 %67, i1* %k
	%68 = load i1, i1* %k
	br i1 %68, label %if.then2, label %if.else2

if.then2:
	%69 = load %class.IO*, %class.IO** %l
	%70 = icmp eq %class.IO* null, %69
	br i1 %70, label %dispatch_on_void_basic_block, label %proceed_70

proceed_70:
	%71 = alloca i8*
	store i8* getelementptr inbounds ([26 x i8], [26 x i8]* @.str.7, i32 0, i32 0), i8** %71	%72 = load i8*, i8** %71
	%73 = load %class.IO*, %class.IO** %l
	call void @IO_out_string( i8* %72 )
	br label %if.end2


if.else2:
	%74 = load %class.IO*, %class.IO** %l
	%75 = icmp eq %class.IO* null, %74
	br i1 %75, label %dispatch_on_void_basic_block, label %proceed_75

proceed_75:
	%76 = alloca i8*
	store i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.8, i32 0, i32 0), i8** %76	%77 = load i8*, i8** %76
	%78 = load %class.IO*, %class.IO** %l
	call void @IO_out_string( i8* %77 )
	br label %if.end2


if.end2:
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
	ret void
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
