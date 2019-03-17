#include "project4.h"

#include <linux/limits.h>
#include <sys/stat.h>
#include <pwd.h>
#include <unistd.h>
#include <stdio.h>
#include <string.h>

int main(int argc, char** argv)
{
	user_t me = get_user();
	file_t file;
	int i;
	for(i = 1; i < argc; i++)
	{
		file = get_file(argv[i], &me);
		fileaccess(&me, &file);
	}
	return 0;
}

struct User
{
	uid_t uid;
	gid_t gid;
	const char* home;
};

struct File
{
	uid_t uid;
	gid_t gid;
	mode_t mode;
	const char* path;
};

user_t get_user()
{
	user_t user;
	user.uid = getuid();
	user.gid = getgid();
	user.home = get_home(user.uid);
	return user;
}

file_t get_file(const char* filename, const user_t* user)
{
	struct stat file_stat;
	file_t file;
	const char* path = get_path(filename, user);
	stat(path, &file_stat);
	file.uid = file_stat.st_uid;
	file.gid = file_stat.st_gid;
	file.mode = file_stat.st_mode;
	file.path = path;
	return file;
}

const char* get_home(uid_t id)
{
	return getpwuid(id)->pw_dir;
}

const char* get_path(const char* filename, const user_t* user)
{
	static char path[PATH_MAX];
	if (filename[0] == '/')
	    sprintf(path, "%s", filename);
	else
	    sprintf(path, "%s/%s", user->home, filename);
	return path;
}

void fileaccess(const user_t* user, const file_t* file)
{
	mode_t r_mask = S_IRUSR;
	mode_t w_mask = S_IWUSR;
	mode_t x_mask = S_IXUSR;

	if (user->uid == file->uid) ;
	else if (user->gid == file->gid)
	{
		r_mask = r_mask >> 3;
		w_mask = w_mask >> 3;
		x_mask = x_mask >> 3;
	}
	else
	{
		r_mask = r_mask >> 6;
		w_mask = w_mask >> 6;
		x_mask = x_mask >> 6;
	}

	printf(" %c%c%c : %s\n",
		file->mode & r_mask ? 'r' : '-',
		file->mode & w_mask ? 'w' : '-',
		file->mode & x_mask ? 'x' : '-',
		file->path);
}

