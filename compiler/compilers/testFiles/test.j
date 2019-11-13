.class public test
.super java/lang/Object

;
; standard initializer (calls java.lang.Object's initializer)
;
.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

;
; main() method
;
.method public static main([Ljava/lang/String;)V
.limit stack 99
.limit locals 99

ldc 1
ldc 2
iadd
istore 1
iconst_1
istore 2
iload 1
ldc 5
if_icmplt then0
else0:
getstatic java/lang/System/out Ljava/io/PrintStream;
iload 2
invokevirtual java/io/PrintStream/println(I)V
goto endif_0
then0:
getstatic java/lang/System/out Ljava/io/PrintStream;
iload 1
invokevirtual java/io/PrintStream/println(I)V
endif_0:


return

.end method