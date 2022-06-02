#include <stdio.h>
#include "quickSort.h"

/**
 * Executing quicksort and make array in ascending order 
 */
void quickSort(unsigned int *l, int lo, int hi) {
    if (lo < hi) {
        int pi = partition(l, lo, hi);
        
        quickSort(l, lo, pi - INITIAL_ONE);
        quickSort(l, pi + INITIAL_ONE, hi);
    }
}

/**
 * This function is to find the partition position
 */
int partition(unsigned int  *l, int lo, int hi) {
    int pi = l[hi], i = (lo - INITIAL_ONE), pos;

    for (int j = lo; j < hi; j++) {
        if (l[j] <= pi) {
            i++;
            swap(&l[i], &l[j]);
        }
    }

    pos = i + INITIAL_ONE;
    swap(&l[pos], &l[hi]);

    return pos;
}

/**
 * Swaping element 
 */
void swap(unsigned int *a, unsigned int *b) {
    int t = *a;
    *a = *b;
    *b = t;
}
