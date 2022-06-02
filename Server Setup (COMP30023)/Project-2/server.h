#define IMPLEMENTS_IPV6 // Can be run in IPv6
// Collect Magic Numbers
#define CONTENT_TYPE_LEN 100
#define TEMP_BUFFER 8192
#define THREADS_NUM 10
#define TEMP_SIZE 100
#define TEN_KB 10240
#define BIG_NUM 4096
#define ONE_KB 1024
// Simple Sign Define
#define END_SPACE 1
#define ESCAPE ".."
#define START '\0'
#define SLASH "/"
#define SPACE " "
#define DOT_CHAR "."
#define DOT_SIGN '.'
#define END_SPACE 1
// Status Define
#define VALID_RESPONSE 200
#define SOCKETERROR 0
#define NOT_FOUND 404
// Type Define
#define JS_FILE_TYPE "js"
#define CSS_FILE_TYPE "css"
#define JPG_FILE_TYPE "jpg"
#define HTML_FILE_TYPE "html"
#define CSS_CONTENT_TYPE "text/css"
#define HTML_CONTENT_TYPE "text/html"
#define JPG_CONTENT_TYPE "image/jpeg"
#define JS_CONTENT_TYPE "text/javascript"
#define OTHER_CONTENT_TYPE "application/octet-stream"

typedef enum { false, true } bool; // Make my own bool type

char *concat(char *first, char *second);
char *printType(char *typeString);
char *getSubPath(char *the_path);
char *getType(char *path);
int checkEXP(int exp, const char *msg);
bool checkSpace(char *the_path);




