#include "stdio.h"

int main(void){
char a[1000];

scanf("%[^\n]", a);
printf("Read from other file:\n%s", a);
fprintf(stderr, "THIS IS AN ERROR" );
return 0;
}   
