#ifndef PROJECT4_H
#define PROJECT4_H

#include <sys/types.h>

typedef struct User user_t;
typedef struct File file_t;

user_t get_user();
file_t get_file(const char*, const user_t*);
const char* get_home(uid_t);
const char* get_path(const char*, const user_t*);
void fileaccess(const user_t*, const file_t*);

int main(int, char**);

#endif

