#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <assert.h>
#include "executing.h"
#include "utils.h"

/*
 * This function reads the input file and outputs an array of process units
 * The return result is the head of the linked list consists all processes.
 * And updates the number of units.
 * Parameters: filename, number of processes, number of files
*/
Unit *unitProcessing(char *theFile, int *numUnit, int *numFiles){
    Unit *head = NULL, *cur;
    char buff[MAXSIZE];
    unsigned int ele[MAXSIZE];
    int idx = INITIAL_ZERO;
    ele[idx] = INITIAL_NEG_ONE;

    // No file exsiting
    if(access(theFile, R_OK ) == INITIAL_NEG_ONE ) {
        fprintf(stderr, "File could not be found.\n");
        return NULL;
    }

    FILE *f;
    f = fopen(theFile, "r");
    
    while (fgets(buff, MAXSIZE, f) != NULL) {
        // Unit_Number, lockfileId, waitfileId
        unsigned int ip1, ip2, ip3;
        sscanf(buff, "%u %u %u", &ip1, &ip2, &ip3);

        if (ip1 < NO_INPUT || ip2 < NO_INPUT || ip3 < NO_INPUT) {
            fprintf(stderr, "Invalid data.\n");
            return NULL;
        }
        
        // Collect distinct file ids
        if(!checkIP(ele, ip2)) putIn(ele, ip2, &idx);
        if(!checkIP(ele, ip3)) putIn(ele, ip3, &idx);
        
        if(head == NULL) {
            head = createUnit(ip1, ip2, ip3);
            cur = head;
        } else {
            cur->next = createUnit(ip1, ip2, ip3);
            cur = cur->next;           
        }

        (*numUnit)++;
    }

    *numFiles = idx;
    fclose(f);
    return head;
}

/**
 * Make simulations and get simulation times
 */
void simulating(Unit **theUnit, int numUnit){
    Unit **copy = theUnit;
    int timer = INITIAL_ZERO, idx, visit = INITIAL_ZERO;
    unsigned int curID, curLock, curWait, visited[numUnit], allID[numUnit];
    for (int i = INITIAL_ZERO; i < numUnit; i++) allID[i] = copy[i]->unitNumbers;

    for (int i = INITIAL_ZERO; i < numUnit; i++){
        curID = copy[i]->unitNumbers;
        curLock = copy[i]->fileId1;
        curWait = copy[i]->fileId2;

        if (checkIP(allID, curID) && !checkIP(visited, curID)){
            unsigned int lks[numUnit], wts[numUnit];
            int lkc = INITIAL_ZERO, wtc = INITIAL_ZERO;
            putIn(lks, curLock, &lkc);
            putIn(wts, curWait, &wtc);
            for (int j = INITIAL_ZERO; j < numUnit; j++){
                unsigned int iid, lk, wt;
                iid = copy[j]->unitNumbers;
                lk = copy[j]->fileId1;
                wt = copy[j]->fileId2;
                if (checkIP(lks, lk) && checkIP(wts, wt) && curID != iid) continue;
                if (!checkIP(visited, iid) && (!checkIP(lks, wt) && !checkIP(wts, lk))){
                    printf("%d %u %u,%u\n", timer, iid, lk, wt);
                    putIn(visited, iid, &visit);
                    putIn(lks, lk, &lkc);
                    putIn(wts, wt, &wtc);
                    idx = getIndex(allID, iid);
                    allID[idx] = POPOUT;
                }
            }
            timer++;
        }     
    }

    printf("Simulation time %d\n", timer);
}

/* 
 * Creates a new process unit. Only set up for initial input data.
 * Parameters: unitNumbers, lockID, waitID
*/
Unit *createUnit(unsigned int ip1, unsigned int ip2, unsigned int ip3){
    Unit *theUnit = malloc(sizeof(Unit));
    assert(theUnit);

    theUnit->unitNumbers = ip1;
    theUnit->fileId1 = ip2;
    theUnit->fileId2 = ip3;
    theUnit->next = NULL;

    return theUnit;
}

/*
 * Frees a unit. This is called once a process finishes executing entirely.
*/
void freeUnit(Unit *theUnit){
    if (theUnit == NULL) {
        free(theUnit);
    } else {
        theUnit->next = NULL;
        free(theUnit->next);
    }
}

/* 
 * Check if an int value existing in the target array
 * Parameters: target array, array size, int value
*/
bool checkIP(unsigned int *ip_list, unsigned int ip){
    for (int idx = INITIAL_ZERO; ip_list[idx] != INITIAL_NEG_ONE; idx++)
        if (ip_list[idx] == ip)  return true;
    return false;
}

/**
 * Get Execution Time
 */
int getExe(Unit * theUnit, int allID){
    int numExec, lockCount = INITIAL_ZERO, 
        waitCount = INITIAL_ZERO, count = INITIAL_ZERO;
    int locks = INITIAL_ZERO, waits = INITIAL_ZERO;

    unsigned int lock[MAXSIZE], wait[MAXSIZE];

    Unit *copyUnit = theUnit, *checkCopy = NULL;
    while (copyUnit != NULL){
        putIn(lock, copyUnit->fileId1, &lockCount);
        putIn(wait, copyUnit->fileId2, &waitCount);

        checkCopy = copyUnit;
        copyUnit = checkCopy->next;
        count++;
    }

    mostDup(lock, count, &locks);
    mostDup(wait, count, &waits);

    numExec = locks + waits + BASETIME;
    return numExec;
}
