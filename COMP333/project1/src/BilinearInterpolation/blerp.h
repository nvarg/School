#ifndef BLERP_H
#define BLERP_H

#include <stdint.h>

typedef struct Image image_t;
typedef uint32_t color_t;

color_t getpixel(const image_t*, unsigned, unsigned);
color_t newpixel(const image_t*, unsigned, unsigned, unsigned, unsigned);
void putpixel(image_t*, unsigned, unsigned, color_t);
void scale_percent(const image_t*, image_t*, int);
void scale(const image_t*, image_t*, float, float);

float lerp(float, float, float);
float blerp(float, float, float, float, float, float);

image_t* load_bmp32(const char*);
void save_bmp32(const image_t*, const char*);
void image_t_delete(image_t*);

int main(int, char**);

#endif

