#include "stdio.h"

int main(void){
char a[1000];

scanf("%[^\n]", a);
printf("Read from other file:%s\n", a);

return 0;
}   
