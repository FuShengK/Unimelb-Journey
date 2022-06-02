// This code skeleton is copied from week 9 practical server.c file
#define _POSIX_C_SOURCE 200112L
#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <assert.h>
#include <pthread.h>
#include "server.h"

int main(int argc, char **argv)
{
    int sockfd, newsockfd, re, s;
    char buffer[TEMP_BUFFER];
    struct addrinfo hints, *res;
    struct sockaddr_storage client_addr;
    socklen_t client_addr_size;
    char *root_path = argv[3];

    if (argc < 4) {
        fprintf(stderr, "ERROR, Command Error\n");
        exit(EXIT_FAILURE);
    }

    // Create address we're going to listen on (with given port number)
    memset(&hints, 0, sizeof hints);
    if (strcmp(argv[1], "4") == 0) {
        hints.ai_family = AF_INET; // IPv4
    } else if (strcmp(argv[1], "6") == 0) {
        hints.ai_family = AF_INET6; // IPv6
    } else {
        fprintf(stderr, "ERROR, IP hints Error\n");
        exit(EXIT_FAILURE);
    }
    hints.ai_socktype = SOCK_STREAM; // TCP
    hints.ai_flags = AI_PASSIVE;     // for bind, listen, accept

    // node (NULL means any interface), service (port), hints, res
    s = getaddrinfo(NULL, argv[2], &hints, &res);
    if (s != 0) {
        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(s));
        exit(EXIT_FAILURE);
    }

    // Check everything about socket before put into thread
    sockfd = checkEXP((socket(res->ai_family, res->ai_socktype, res->ai_protocol)), "Cannot create socket");
    // Check everything about socket before put into thread
    if (sockfd) {
        checkEXP((setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &re, sizeof(int))), "Cannot set sockopt"); // Reuse port if possible
        checkEXP((bind(sockfd, res->ai_addr, res->ai_addrlen)), "Cannot bind address to socket");
        freeaddrinfo(res);
        checkEXP((listen(sockfd, TEMP_SIZE)), "Cannot listen to socket");
    }

    // Set up to accept coninuous port
    while (true) {
        client_addr_size = sizeof client_addr;

        newsockfd = checkEXP((accept(sockfd, (struct sockaddr *)&client_addr, &client_addr_size)), "Cannot access to socket");
        int bytes_read = checkEXP((read(newsockfd, buffer, TEN_KB)), "Cannot read properly");

        // Null-terminate string
        buffer[bytes_read] = START;

        char *sub_path = getSubPath(buffer);
        char *content_type = getType(sub_path);
        char *full_path = concat(root_path, sub_path); // concat root dir to sub_path

        bool escaped = checkSpace(full_path), valid;
        if (strchr(root_path, '.') == NULL) valid = true;

        FILE *fp;
        if (((fp = fopen(full_path, "r")) != NULL) && content_type != NULL && !escaped && valid) {
            // Send ok status and content type
            write(newsockfd, "HTTP/1.0 200 OK\r\n", strlen("HTTP/1.0 200 OK\r\n"));

            char *ok_type = printType(content_type);
            write(newsockfd, ok_type, strlen(ok_type));
            free(ok_type);

            char *temps = malloc(BIG_NUM);
            assert(temps);
            int len;
            while ((len = fread(temps, sizeof(char), BIG_NUM, fp)) != 0) {
                write(newsockfd, temps, len);
            }
            free(temps);
            fclose(fp);
        } else {
            // Send 404 status and html type
            write(newsockfd, "HTTP/1.0 404 NOT FOUND\r\n", strlen("HTTP/1.0 404 NOT FOUND\r\n"));

            char *no_found_type = printType(HTML_CONTENT_TYPE);
            write(newsockfd, no_found_type, strlen(no_found_type));
            free(no_found_type);
        }

        close(newsockfd);
        free(full_path);
    }

    close(sockfd);
    return 0;
}
/**
 * @brief Check all socket execution status
 *
 * @param exp
 * @param msg
 * @return int
 */
int checkEXP(int exp, const char *msg) {
    if (exp < SOCKETERROR) {
        perror(msg);
        exit(EXIT_FAILURE);
    }
    return exp;
}
/**
 * @brief Check escape exist in path
 *
 * @param the_path
 * @return char*
 */
bool checkSpace(char *the_path) {
    char *check = malloc(strlen(the_path));
    assert(check);
    strcpy(check, the_path);

    char *token = strtok(check, SLASH);
    char *cur;
    while (token != NULL) {
        if (strcmp(token, ESCAPE) == 0) {
            return true;
        }
        cur = strtok(NULL, SLASH);
        token = cur;
    }

    return false;
}
/**
 * @brief Get the Sub Path String
 *
 * @param the_path
 * @return char*
 */
char *getSubPath(char *the_path) {
    // CRUD command token
    char *token = strtok(the_path, SPACE);
    // The path token needed
    char *path_needed;
    if (token != NULL) {
        path_needed = strtok(NULL, SPACE);
    }

    return path_needed;
}
/**
 * @brief Concat two string, mainly to get full path
 *
 * @param first
 * @param second
 * @return char*
 */
char *concat(char *first, char *second) {
    char *full_string = malloc(strlen(first) + strlen(second) + END_SPACE);
    assert(full_string);
    strcat(full_string, first);
    strcat(full_string, second);

    return full_string;
}
/**
 * @brief Get the Type String
 *
 * @param path
 * @return char*
 */
char *getType(char *path) {

    char *temp = malloc(strlen(path));
    assert(temp);
    strcpy(temp, path);
    char *token = strtok(temp, DOT_CHAR), *prev;
    while (token != NULL) {
        prev = token;
        token = strtok(NULL, DOT_CHAR);
    }

    // Make file type assignments
    if (strcmp(prev, JPG_FILE_TYPE) == 0) {
        return JPG_CONTENT_TYPE;
    } else if (strcmp(prev, HTML_FILE_TYPE) == 0) {
        return HTML_CONTENT_TYPE;
    } else if (strcmp(prev, CSS_FILE_TYPE) == 0) {
        return CSS_CONTENT_TYPE;
    } else if (strcmp(prev, JS_FILE_TYPE) == 0) {
        return JS_CONTENT_TYPE;
    } else {
        return OTHER_CONTENT_TYPE;
    }

    return token;
}
/**
 * @brief print out the content type with complete sentence
 *
 * @param typeString
 * @return char*
 */
char *printType(char *typeString) {
    char THE_TYPE[] = "Content-type: %s\r\n\r\n";
    char *temp = malloc(strlen(THE_TYPE) + strlen(typeString));
    sprintf(temp, THE_TYPE, typeString);

    return temp;
}