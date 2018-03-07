#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include "err.h"
#include <stdbool.h>

#define NR_PROC 5

int main ()
{
	pid_t pid;
	int num_procesu = 0;
	bool koniec = false;
	while(koniec == false) {
		printf("Jestem procesem nr %d i tworze syna\n", num_procesu);
		switch(pid = fork()) {
			case -1:
				syserr("Error in fork\n");
				break;
			case 0: /* proces potomny */
				printf("Jestem procesem potomnym\n");
				num_procesu++;
				if(num_procesu == 5) {
					koniec = true;
					exit(0);
				}
				break;
			default:
				printf("Jestem rodzicem o pid = %d i moim dzieckiem jest %d\n", getpid(), pid);
				if (wait(0) == -1) syserr("Error in wait\n");
				printf("jestem rodzicem i koncze dzialanie\n");
				koniec = true;
		}
	}
	return 0;



//  /* tworzenie procesów potomnych */
//  for (i = 1; i <= NR_PROC; i++)
//    switch (pid = fork()) {
//      case -1:
//        syserr("Error in fork\n");
//
//      case 0: /* proces potomny */
//
//        printf("I am a child and my pid is %d\n", getpid());
//        return 0;
//
//    default: /* proces macierzysty */
//
//      printf("I am a parent and my pid is %d\n", getpid());
//    }
//
//  /* czekanie na zakończenie procesow potomnych */
//  for (i = 1; i <= NR_PROC; i++)
//    if (wait(0) == -1)
//      syserr("Error in wait\n");
//
//  return 0;
  
}
