; I am a comment in LLVM-IR. Feel free to remove me.
target datalayout = "e-m:e-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-unknown-linux-gnu"
@Abortdivby0 = private unnamed_addr constant [22 x i8] c"Error: Division by 0\0A\00", align 1
@Abortdispvoid = private unnamed_addr constant [25 x i8] c"Error: Dispatch on void\0A\00", align 1

declare i32 @printf(i8*, ...)
declare i32 @scanf(i8*, ...)
declare i32 @strcmp(i8*, i8*)
declare i8* @strcat(i8*, i8*)
declare i8* @strcpy(i8*, i8*)
declare i8* @strncpy(i8*, i8*, i32)
declare i64 @strlen(i8*)
declare i8* @malloc(i64)
declare void @exit(i32)
@strformatstr = private unnamed_addr constant [3 x i8] c"%s\00", align 1
@intformatstr = private unnamed_addr constant [3 x i8] c"%d\00", align 1

