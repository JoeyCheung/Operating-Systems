#include <sys/wait.h>
#include <stdio.h>
#include <stdlib.h>
int main()
{
	int pid = fork();
	if (pid == 0)
	{
		// Currently in child process
		printf("I am the child with PID [%d] and my parent has PPID [%d].\n", getpid(), getppid());
		sleep(1);
		exit(0); // Child process terminates and enters zombie state
	}
	else
	{
		// Currently in parent process
		printf("I am the parent and my id is [%d]\n", getpid());
		sleep(30); // This sleep is what causes the child to be in zombie state
	}
	wait(NULL); // Terminate child
}
