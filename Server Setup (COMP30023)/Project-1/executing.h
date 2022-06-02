typedef enum {false, true} bool; // make my own bool type
typedef struct Unit_s {
    unsigned int unitNumbers;
    unsigned int fileId1;
    unsigned int fileId2;

    struct Unit_s *next;
} Unit;

Unit *unitProcessing(char *theFile, int *numUnit, int *numFiles);
Unit *createUnit(unsigned int ip1, unsigned int ip2, unsigned int ip3);
void freeUnit(Unit *theUnit);
int getExe(Unit * theUnit, int allID);
void simulating(Unit **theUnit, int numUnit);
bool checkIP(unsigned int *ip_list, unsigned int ip);