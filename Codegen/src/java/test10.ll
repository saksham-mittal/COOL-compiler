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
%class.Main = type { %class.XYZ*, %class.IO*, i32 }

define %class.Main* @Main_Cons_Main( %class.Main* %this ) {
entry:
	%this.addr = alloca %class.Main*
	store %class.Main* %this, %class.Main** %this.addr
	%this1 = load %class.Main*, %class.Main** %this.addr
	%a = getelementptr inbounds %class.Main, %class.Main* %this1, i32 0, i32 0
	%0 = alloca %class.XYZ
	%1 = call %class.XYZ* @XYZ_Cons_XYZ( %class.XYZ* %0 )
	store %class.XYZ* %1, %class.XYZ** %a
	%b = getelementptr inbounds %class.Main, %class.Main* %this1, i32 0, i32 1
	%2 = alloca %class.IO
	%3 = call %class.IO* @IO_Cons_IO( %class.IO* %2 )
	store %class.IO* %3, %class.IO** %b
	%c = getelementptr inbounds %class.Main, %class.Main* %this1, i32 0, i32 2
	store i32 0, i32* %c
	ret %class.Main* %this1
}
@.str.0 = private unnamed_addr constant [18 x i8] c"Static Dispatch 1\00"
@.str.1 = private unnamed_addr constant [2 x i8] c"a\00"

define void @Main_main( %class.Main* %this ) {
entry:
	%this.addr = alloca %class.Main*
	store %class.Main* %this, %class.Main** %this.addr
	%this1 = load %class.Main*, %class.Main** %this.addr
	%a = getelementptr inbounds %class.Main, %class.Main* %this1, i32 0, i32 0
	%b = getelementptr inbounds %class.Main, %class.Main* %this1, i32 0, i32 1
	%c = getelementptr inbounds %class.Main, %class.Main* %this1, i32 0, i32 2
	%0 = load %class.IO*, %class.IO** %b
	%1 = icmp eq %class.IO* null, %0
	br i1 %1, label %dispatch_on_void_basic_block, label %proceed_1

proceed_1:
	%2 = alloca i8*
	store i8* getelementptr inbounds ([18 x i8], [18 x i8]* @.str.0, i32 0, i32 0), i8** %2	%3 = load i8*, i8** %2
	%4 = load %class.IO*, %class.IO** %b
	call void @IO_out_string( i8* %3 )
	%5 = load %class.IO*, %class.IO** %b
	%6 = icmp eq %class.IO* null, %5
	br i1 %6, label %dispatch_on_void_basic_block, label %proceed_6

proceed_6:
	%7 = load %class.XYZ*, %class.XYZ** %a
	%8 = icmp eq %class.XYZ* null, %7
	br i1 %8, label %dispatch_on_void_basic_block, label %proceed_8

proceed_8:
	%9 = alloca i8*
	store i8* getelementptr inbounds ([2 x i8], [2 x i8]* @.str.1, i32 0, i32 0), i8** %9	%10 = load i8*, i8** %9
	%11 = load %class.XYZ*, %class.XYZ** %a
	%12 = load %class.XYZ*, %class.XYZ** %a
	%13 = call i8* @XYZ_do_that( %class.XYZ* %12, i8* %10, %class.XYZ* %11 )
	%14 = load %class.IO*, %class.IO** %b
	call void @IO_out_string( i8* %13 )
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
@.str.2 = private unnamed_addr constant [7 x i8] c"CS3423\00"
%class.XYZ = type { i32, i1, i8*, %class.IO* }

define %class.XYZ* @XYZ_Cons_XYZ( %class.XYZ* %this ) {
entry:
	%this.addr = alloca %class.XYZ*
	store %class.XYZ* %this, %class.XYZ** %this.addr
	%this1 = load %class.XYZ*, %class.XYZ** %this.addr
	%a = getelementptr inbounds %class.XYZ, %class.XYZ* %this1, i32 0, i32 0
	%0 = alloca i32
	store i32 4, i32* %0
	%1 = load i32, i32* %0
	%2 = alloca i32
	store i32 5, i32* %2
	%3 = load i32, i32* %2
	%4 = add i32 %1, %3
	store i32 %4, i32* %a
	%b = getelementptr inbounds %class.XYZ, %class.XYZ* %this1, i32 0, i32 1
	store i1 false, i1* %b
	%c = getelementptr inbounds %class.XYZ, %class.XYZ* %this1, i32 0, i32 2
	%5 = alloca i8*
	store i8* getelementptr inbounds ([7 x i8], [7 x i8]* @.str.2, i32 0, i32 0), i8** %5	%6 = load i8*, i8** %5
	store i8* %6, i8** %c
	%d = getelementptr inbounds %class.XYZ, %class.XYZ* %this1, i32 0, i32 3
	%7 = alloca %class.IO
	%8 = call %class.IO* @IO_Cons_IO( %class.IO* %7 )
	store %class.IO* %8, %class.IO** %d
	ret %class.XYZ* %this1
}
@.str.3 = private unnamed_addr constant [18 x i8] c"Enter something:\0A\00"

define i8* @XYZ_do_this( %class.XYZ* %this, i32 %i, i8* %j, i1 %k ) {
entry:
	%retval = alloca i8*
	%this.addr = alloca %class.XYZ*
	%i.addr = alloca i32
	%j.addr = alloca i8*
	%k.addr = alloca i1
	store i32 %i, i32* %i.addr
	store i8* %j, i8** %j.addr
	store i1 %k, i1* %k.addr
	store %class.XYZ* %this, %class.XYZ** %this.addr
	%this1 = load %class.XYZ*, %class.XYZ** %this.addr
	%a = getelementptr inbounds %class.XYZ, %class.XYZ* %this1, i32 0, i32 0
	%b = getelementptr inbounds %class.XYZ, %class.XYZ* %this1, i32 0, i32 1
	%c = getelementptr inbounds %class.XYZ, %class.XYZ* %this1, i32 0, i32 2
	%d = getelementptr inbounds %class.XYZ, %class.XYZ* %this1, i32 0, i32 3
	%0 = load i32, i32* %i.addr
	%1 = alloca i32
	store i32 8, i32* %1
	%2 = load i32, i32* %1
	%comp_2_0 = icmp eq i32 %2, 0
	br i1 %comp_2_0, label %func_div_by_zero_abort, label %proceed_2_0

proceed_2_0:
	%3 = udiv i32 %0, %2
	store i32 %3, i32* %i.addr
	%4 = load %class.IO*, %class.IO** %d
	%5 = icmp eq %class.IO* null, %4
	br i1 %5, label %dispatch_on_void_basic_block, label %proceed_5

proceed_5:
	%6 = alloca i8*
	store i8* getelementptr inbounds ([18 x i8], [18 x i8]* @.str.3, i32 0, i32 0), i8** %6	%7 = load i8*, i8** %6
	%8 = load %class.IO*, %class.IO** %d
	call void @IO_out_string( i8* %7 )
	%9 = load %class.IO*, %class.IO** %d
	%10 = icmp eq %class.IO* null, %9
	br i1 %10, label %dispatch_on_void_basic_block, label %proceed_10

proceed_10:
	%11 = load %class.IO*, %class.IO** %d
	%12 = call i8* @IO_in_string(  )
	store i8* %12, i8** %c
	%13 = load %class.IO*, %class.IO** %d
	%14 = icmp eq %class.IO* null, %13
	br i1 %14, label %dispatch_on_void_basic_block, label %proceed_14

proceed_14:
	%15 = load i8*, i8** %c
	%16 = load %class.IO*, %class.IO** %d
	call void @IO_out_string( i8* %15 )
	%17 = load i8*, i8** %j.addr
	store i8* %17, i8** %retval
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
	%18 = load i8*, i8** %retval
	ret i8* %18
}

define i8* @XYZ_do_that( %class.XYZ* %this, i8* %a1, %class.XYZ* %b1 ) {
entry:
	%retval = alloca i8*
	%this.addr = alloca %class.XYZ*
	%a1.addr = alloca i8*
	%b1.addr = alloca %class.XYZ*
	store i8* %a1, i8** %a1.addr
	store %class.XYZ* %b1, %class.XYZ** %b1.addr
	store %class.XYZ* %this, %class.XYZ** %this.addr
	%this1 = load %class.XYZ*, %class.XYZ** %this.addr
	%a = getelementptr inbounds %class.XYZ, %class.XYZ* %this1, i32 0, i32 0
	%b = getelementptr inbounds %class.XYZ, %class.XYZ* %this1, i32 0, i32 1
	%c = getelementptr inbounds %class.XYZ, %class.XYZ* %this1, i32 0, i32 2
	%d = getelementptr inbounds %class.XYZ, %class.XYZ* %this1, i32 0, i32 3
	%0 = load %class.XYZ*, %class.XYZ** %b1.addr
	%1 = icmp eq %class.XYZ* null, %0
	br i1 %1, label %dispatch_on_void_basic_block, label %proceed_1

proceed_1:
	%2 = alloca i32
	store i32 1, i32* %2
	%3 = load i32, i32* %2
	%4 = load i8*, i8** %a1.addr
	%5 = alloca i1
	store i1 false, i1* %5
	%6 = load i1, i1* %5
	%7 = load %class.XYZ*, %class.XYZ** %b1.addr
	%8 = call i8* @XYZ_do_this( %class.XYZ* %7, i32 %3, i8* %4, i1 %6 )
	store i8* %8, i8** %a1.addr
	%9 = load %class.XYZ*, %class.XYZ** %b1.addr
	%10 = icmp eq %class.XYZ* null, %9
	br i1 %10, label %dispatch_on_void_basic_block, label %proceed_10

proceed_10:
	%11 = alloca i32
	store i32 2, i32* %11
	%12 = load i32, i32* %11
	%13 = load i8*, i8** %a1.addr
	%14 = alloca i1
	store i1 true, i1* %14
	%15 = load i1, i1* %14
	%16 = load %class.XYZ*, %class.XYZ** %b1.addr
	%17 = call i8* @XYZ_do_this( %class.XYZ* %16, i32 %12, i8* %13, i1 %15 )
	store i8* %17, i8** %a1.addr
	store i8* %17, i8** %retval
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
	%18 = load i8*, i8** %retval
	ret i8* %18
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
