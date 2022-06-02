#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <unistd.h>
#include "utils.h"

/**
 * Get the number of most duplicated elements 
 */
void mostDup(unsigned int *l, int idx, int *count){
    int i, j, counting, maxCount = INITIAL_ZERO;
    for (i = INITIAL_ZERO; i < idx; i++)   {
        counting = INITIAL_ZERO;
        for (j = i + INITIAL_ONE; j < idx; j++){
            if ((l[j] == l[i]) && (i != j)){
                counting++;
                if(counting > maxCount) maxCount = counting;
            }
        }
    }

    *count = maxCount;
}

/** 
 * Put value into list
 */
void putIn(unsigned int *l, unsigned int v, int *idx){
    l[*idx] = v;
    (*idx)++;
    l[*idx] = INITIAL_NEG_ONE;
}

/**
 * This function get the idx where the target is   
*/
int getIndex(unsigned int *locks, unsigned int target){ 
    for (int idx = INITIAL_ZERO; locks[idx] != INITIAL_NEG_ONE; idx++)
        if (locks[idx] == target)  return idx;
    return INITIAL_NEG_ONE;   
}

/**
 * Get the size of array 
 */
int getSize(unsigned int *l){
    int count = INITIAL_ZERO;
    for (int i = INITIAL_ZERO; l[i] != INITIAL_NEG_ONE; i++) count++;
    return count;
}

/**
 * Get the smallest value 
 */
unsigned int min(unsigned int *l){
    unsigned int small = l[INITIAL_ZERO], cur = INITIAL_ONE;
    while(l[cur] != INITIAL_NEG_ONE){
        if (l[cur] < small) {
            small = l[cur];
        }
        cur++;
    }
    return small;
}