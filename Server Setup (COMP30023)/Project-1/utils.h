#define MAXSIZE 1000 // To reach maximum
#define POPOUT -2
#define INITIAL_NEG_ONE -1
#define NO_INPUT 0
#define NO_DEADLOCK 0
#define INITIAL_ZERO 0
#define ONE 1
#define INITIAL_ONE 1
#define BASETIME 2
#define NO_ARGUMENT 3

void mostDup(unsigned int *l, int idx, int *count);
void putIn(unsigned int *l, unsigned int v, int *idx);
int getIndex(unsigned int *locks, unsigned int target);
int getSize(unsigned int *l);
unsigned int min(unsigned int *l);