#pragma once
#define READ_FILE 'f'
#define COMPUTE_EXECUTION_TIME 'e'
#define SIMULATION 'c'
#define ALL_OPT_ARGS "f:ec"
#include "executing.h"
#include "quickSort.h"
#include "utils.h"

/* Extern declarations: */
extern char *optarg;

void printSpec(char *theName);
void detect(Unit *processUnit, int numUnit, int numFiles, bool showDL,  bool showSimu);
void getResults(Unit** theUnit, int numUnit, int numFiles, int numExe, bool showExe, bool deadlocked, 
                unsigned int terminateList[], bool showSimu);
void breaking(Unit **theUnit, int numUnit, unsigned int *term);
void insert(unsigned int *l, unsigned int oid);
