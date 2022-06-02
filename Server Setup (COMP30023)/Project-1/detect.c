#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <assert.h>
#include "detect.h"

int main(int argc, char **argv){
    char input, *theFile;
    bool showExecutionTime = false, simulation = false;

    // Exit if no arguments
    if (argc < NO_ARGUMENT) {
		printSpec(argv[INITIAL_ZERO]);
		return INITIAL_NEG_ONE;
	}
    
    // Getting the commands for execution.
    while((input = getopt(argc, argv, ALL_OPT_ARGS)) != EOF){
        switch( input ){
            case READ_FILE:
                theFile = optarg;
                break;

            case COMPUTE_EXECUTION_TIME:
                showExecutionTime = true;
                break;

            case SIMULATION:
                simulation = true;
                break;

            default:
                // Print the instruction to execute the program
                printSpec(argv[INITIAL_ZERO]);
                return INITIAL_NEG_ONE;
        }
    }
    
    int unitNumbers = INITIAL_ZERO, fileNumbers = INITIAL_ZERO;
    Unit *listHead  = unitProcessing(theFile, &unitNumbers, &fileNumbers);

    if (listHead == NULL) {
        fprintf(stderr, "%s reading error\n", theFile);
		return INITIAL_NEG_ONE;
    }

    // Start to detect deadlocks
    detect(listHead, unitNumbers, fileNumbers, showExecutionTime, simulation);

    freeUnit(listHead);
    return INITIAL_ZERO;
}

/*
 * Print out the instruction for user's convenience 
*/
void printSpec(char *theName){
    fprintf(stderr, "Usage: %s -f [file name] -e -c \n", theName);
	fprintf(stderr, "-f Input text file name\n");
	fprintf(stderr, "-e Compute & display execution times instead of terminate\n");
	fprintf(stderr, "-c Invokes the process allcation simulation\n");
}

/*
 * Primary function to detect deadlock in all processes
 * Parameters: head of process Unit, number of unit, number of files
 *             showExe, showSimu, 
*/
void detect(Unit *processUnit, int numUnit, int numFiles, bool showExe, 
            bool showSimu){

    unsigned int curLock, curWait, curID, tempLock[MAXSIZE];
    int tempCount = INITIAL_ZERO, simuCount = INITIAL_ZERO, unitCount = INITIAL_ZERO; 

    // Set up collecting arrays   
    tempLock[tempCount] = INITIAL_NEG_ONE; 

    Unit *curUnit = processUnit, *checkCur = NULL, *lockUnit[MAXSIZE], *simuUnit[MAXSIZE];

    while (curUnit != NULL){
        curLock = curUnit->fileId1;
        curWait = curUnit->fileId2;
        curID = curUnit->unitNumbers;
        simuUnit[simuCount] = createUnit(curID, curLock, curWait);
        simuCount++;

        Unit *copyUnit = processUnit, *checkCopy = NULL;
        while (copyUnit != NULL){
            if (curWait == copyUnit->fileId1 && !checkIP(tempLock, curID)){
                putIn(tempLock, curID, &tempCount);
                lockUnit[unitCount] = createUnit(curID, curLock, curWait);
                unitCount++;
            }   
            checkCopy = copyUnit;
            copyUnit = checkCopy->next;
        }        
        checkCur = curUnit->next;
        curUnit = checkCur;

        freeUnit(checkCopy);
        freeUnit(copyUnit);   
    }

    // Will set to have deadlock if deadlock detected
    bool deadlocked = (tempCount != NO_DEADLOCK) ? true : false ;
    
    // deadlocks
    unsigned int term[unitCount];
    term[INITIAL_ZERO] = INITIAL_NEG_ONE;
    if (deadlocked){
        breaking(lockUnit, unitCount, term);
        int termCount = getSize(term);
        quickSort(term, INITIAL_ZERO, termCount); 
    }

    // Print out the results
    getResults(simuUnit, numUnit, numFiles, getExe(processUnit, numUnit), 
                showExe, deadlocked, term, showSimu);
    
    // Free up pointers as we already finish the tasks to avoid memory leaks
    freeUnit(checkCur);
    freeUnit(curUnit);
    for (int j = INITIAL_ZERO; j < unitCount; j++) {
        if (lockUnit[j]) freeUnit(lockUnit[j]);
    }
    for (int j = INITIAL_ZERO; j < simuCount; j++) {
        if (simuUnit[j]) freeUnit(simuUnit[j]);
    }
}

/**
 * Print out results according to command argument
*/
void getResults(Unit** theUnit, int numUnit, int numFiles, int numExe, bool showExe, bool deadlocked, 
                unsigned int terminateList[], bool showSimu){

    // Start to simulating
    if (showSimu){
        simulating(theUnit, numUnit);
    } else {
        printf("Processes %d\n", numUnit);
        printf("Files %d\n", numFiles);

        // Display deadlock if no need for execution times
        if (!showExe){
            if (deadlocked) {
                printf("Deadlock detected\n");
                printf("Terminate ");
                for (int i = INITIAL_ZERO; terminateList[i] != INITIAL_NEG_ONE; i++) 
                    printf("%u ",terminateList[i]);
            } else {
                printf("No deadlocks");
            }
            printf("\n");
        } else {
            printf("Execution time %d\n", numExe);
        }    
    }
}

/**
 * Use strong connected components algorithm to make groups and pick the smallest from each of them
 */
void breaking(Unit **theUnit, int numUnit, unsigned int *term){
    unsigned int graph[numUnit][numUnit];
    for (int i = INITIAL_ZERO; i < numUnit; i++){
        for (int j = INITIAL_ZERO; j < numUnit; j++){
            graph[i][j] = INITIAL_NEG_ONE;
        }
    }
    
    int idx = INITIAL_ZERO, count = INITIAL_ZERO, visit = INITIAL_ZERO;
    unsigned int visited[numUnit], oid, owt, olk, iid, ilk, iwt;
    visited[visit] = INITIAL_NEG_ONE;

    while (idx < numUnit){
        int check = INITIAL_ZERO;
        oid = theUnit[idx]->unitNumbers;
        owt = theUnit[idx]->fileId2;
        olk = theUnit[idx]->fileId1;        
        while (check < numUnit){
            iid = theUnit[check]->unitNumbers;
            ilk = theUnit[check]->fileId1;
            iwt = theUnit[check]->fileId2;
            if (owt == ilk || olk == iwt){
                if (count == INITIAL_ZERO){
                    int num = INITIAL_ZERO;
                    putIn(graph[count], oid, &num);
                    putIn(graph[count], iid, &num);
                    count++;
                    putIn(visited, oid, &visit);
                } else {
                    int here = INITIAL_ZERO;
                    
                    while (here < count){
                        if (checkIP(graph[here], oid) && !checkIP(graph[here], iid)){
                            putIn(visited, iid, &visit);
                            insert(graph[here], iid);
                        } 

                        if (checkIP(graph[here], iid) && !checkIP(graph[here], oid)){
                            putIn(visited, oid, &visit);
                            insert(graph[here], oid);
                        } 
                        
                        here++;

                        if (!checkIP(graph[here], oid) && !checkIP(graph[here], iid)
                            && !checkIP(visited, iid) && !checkIP(visited, oid)){
                            int num = INITIAL_ZERO;
                            putIn(graph[count], oid, &num);
                            putIn(graph[count], iid, &num);
                            count++;
                            putIn(visited, iid, &visit);
                            putIn(visited, oid, &visit);
                        }
                    }
                }
            }
            check++;
        }
        idx++;
    }
    
    int cur = INITIAL_ZERO, termCount = INITIAL_ZERO;
    while (cur < count){
        unsigned int smallest = min(graph[cur]);
        putIn(term, smallest, &termCount);
        cur++;
    }
}

/**
 * Insert oid to array
 */
void insert(unsigned int *l, unsigned int oid){
    int idx = INITIAL_ZERO;
    if (l[idx] == INITIAL_NEG_ONE) l[idx] = oid;
    while (l[idx] != INITIAL_NEG_ONE){
        idx++;
        if (l[idx] == INITIAL_NEG_ONE) {
            l[idx] = oid;
            return;
        }
    }    
}
