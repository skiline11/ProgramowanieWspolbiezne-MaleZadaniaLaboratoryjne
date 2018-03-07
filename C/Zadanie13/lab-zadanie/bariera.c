#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <stdarg.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <string.h>

#define N 4; // liczba dzieci
#define K 5; // liczba pruśb do rozpoczęcia działania

sigset_t block_mask;

void syserr(const char *fmt, ...)
{
	va_list fmt_args;

	fprintf(stderr, "ERROR: ");

	va_start(fmt_args, fmt);
	vfprintf(stderr, fmt, fmt_args);
	va_end (fmt_args);
	fprintf(stderr," (%d; %s)\n", errno, strerror(errno));
	exit(1);
}

void uruchom_dzieci(int ile_dzieci, pid_t pid_glownego_rodzica) {
	pid_t pid;
	if(ile_dzieci > 1) {
		switch(pid = fork()) {
			case -1:
				printf("Error in fork\n");
				break;
			case 0:
				printf("I am a child and my pid is %d\n", getpid());
				printf("I am a child and fork() return value is %d\n\n", pid);
				sleep(1);
				uruchom_dzieci(ile_dzieci - 1, pid_glownego_rodzica);
				break;
			default:
				// muszę wysłać sygnał do głównego rodzica za pomocą funkcji :
				// int kill(pid_t pid, int sig);
				kill(pid_glownego_rodzica, )
				break;

		}
	}
	printf("pid potomnego = %d, moj_pid = %d\n", pid, getpid());
}

void rodzic() {
	int ile_dzieci = N;
	pid_t pid_glownego_rodzica = getpid();
	pid_t pid_dziecka;
	if(ile_dzieci > 0) {
		switch(pid_dziecka = fork()) {
			case -1:
				printf("Error in fork\n");
				break;
			case 0:
				uruchom_dzieci(ile_dzieci, pid_glownego_rodzica);
				break;
			default:
				printf("Jestem rodzicem i czekam na sygnały od dzieci");

				if (wait(0) == -1) syserr("Error in wait\n");
				printf("Jestem rodzicem i skończyłem czekać\n", getpid());
				break;
		}
	}
	uruchom_dzieci(ile_dzieci, pid_glownego_rodzica);
	int zycie_bariery = K;
	while(zycie_bariery > 0) {
		sigwaitinfo(block_mask, )
		zycie_bariery--;
	}

}

int main() {
	sigemptyset (&block_mask);
	sigaddset(&block_mask, SIGALRM);
	rodzic();
	return 0;
}