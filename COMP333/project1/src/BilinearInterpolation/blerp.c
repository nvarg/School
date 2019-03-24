#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <getopt.h>

#ifdef OMP
#include <omp.h>
#endif

#include "blerp.h"

struct Image // image_t
{
    color_t *pixels;
    unsigned w;
    unsigned h;
};

int main(int argc, char** argv)
{
    char* input;
    char* output;
    int scale_factor;

    char opt;
    while((opt = getopt(argc, argv, "i:o:s:h")) != -1)
    switch(opt)
    {
	    case 'i':
		    input = optarg;
		    break;
	    case 'o':
		    output = optarg;
		    break;
	    case 's':
		    scale_factor = (int) strtol(optarg, (char**) NULL, 10);
		    break;
	    case 'h':
	    default:
		printf("Usage: %s [-h] [-isrcimg] [-odstimg] [-sscale]\n", argv[0]);
		return 127;
	    break;
    }

    image_t* image = load_bmp32(input);
    image_t scaled;
    scale_percent(image, &scaled, scale_factor);
    save_bmp32(&scaled, output);
    return 0;
}

color_t getpixel(const image_t *image, unsigned int x, unsigned int y)
{
    return image->pixels[(y*image->w)+x];
}

void putpixel(image_t *image, unsigned int x, unsigned int y, color_t color)
{
    image->pixels[(y*image->w) + x] = color;
}

float lerp(float s, float e, float t)
{
    return s+(e-s)*t;
}

float blerp(float c00, float c10, float c01, float c11, float tx, float ty)
{
    return lerp(lerp(c00, c10, tx), lerp(c01, c11, tx), ty);
}

color_t newpixel(const image_t* src, unsigned width, unsigned height,
		unsigned x, unsigned y)
{
            float gx = x / (float)(width) * (src->w-1);
            float gy = y / (float)(height) * (src->h-1);
            int gxi = (int)gx;
            int gyi = (int)gy;
            color_t c00 = getpixel(src, gxi, gyi);
            color_t c10 = getpixel(src, gxi+1, gyi);
            color_t c01 = getpixel(src, gxi, gyi+1);
            color_t c11 = getpixel(src, gxi+1, gyi+1);
            uint8_t i;
    	    color_t result=0;
            for(i = 0; i < 3; i++){
                 #define getByte(value, n) (value >> (n*8) & 0xFF)
                     result |= (uint8_t)blerp(getByte(c00, i), getByte(c10, i),
				              getByte(c01, i), getByte(c11, i),
				              gx - gxi, gy -gyi) << (8*i);
                 #undef getByte
            }
	return result;
}

void scale_percent(const image_t *src, image_t *dst, int scalexy)
{
	scale(src, dst, (float) scalexy/100, (float) scalexy/100);
}

void scale(const image_t *src, image_t *dst, float scalex, float scaley)
{
    int newWidth = (int)src->w*scalex;
    int newHeight= (int)src->h*scaley;
    int x, y;

    dst->w = newWidth;
    dst->h = newHeight;
    dst->pixels = (color_t*)malloc(sizeof(color_t) * newWidth * newHeight);

    #ifdef OMP
    	#pragma omp parallel for schedule(runtime) private(x, y)
    #endif
    for(x= 0; x < newWidth; x++){
        for(y = 0; y < newHeight; y++) {
	    color_t result = newpixel(src, newWidth, newHeight, x, y);
            putpixel(dst,x, y, result);
        }
    }
}

image_t* load_bmp32(const char *filename)
    // loses color table
{
    image_t *bmp;
    uint8_t header[54];
    FILE* file = fopen(filename, "r");
  fread(header, sizeof(uint8_t), 54, file);

    // assumes little endian
    if (*(uint16_t*)&header[28] != 32)
        // bmp is not 32 bit color
        return 0;

    bmp = (image_t*) malloc(sizeof(image_t));

    // *(int*)& assumes little endian
    bmp->w = *(uint16_t*)&header[0x12];
    bmp->h = *(uint16_t*)&header[0x16];
    bmp->pixels = (color_t*) malloc(sizeof(color_t) * bmp->w * bmp->h);

    fseek(file, *(uint32_t*)&header[0x0a], SEEK_SET);

    fread(bmp->pixels, sizeof(color_t), bmp->w * bmp->h, file);
    fclose(file);
    return bmp;
}

void save_bmp32(const image_t* bmp, const char *filename)
{
    uint8_t bmpfileheader[14] = {'B','M', 0,0,0,0, 0,0, 0,0, 54,0,0,0};
    uint8_t bmpinfoheader[40] = {40,0,0,0, 0,0,0,0, 0,0,0,0, 1,0, 32,0};
    int filesize = 54 + sizeof(color_t) * bmp->w * bmp->h;
    FILE* file;

    bmpfileheader[2] = (uint8_t)(filesize);
    bmpfileheader[3] = (uint8_t)(filesize>> 8);
    bmpfileheader[4] = (uint8_t)(filesize>>16);
    bmpfileheader[5] = (uint8_t)(filesize>>24);

    bmpinfoheader[4] = (uint8_t)(bmp->w);
    bmpinfoheader[5] = (uint8_t)(bmp->w >> 8);
    bmpinfoheader[6] = (uint8_t)(bmp->w >> 16);
    bmpinfoheader[7] = (uint8_t)(bmp->w >> 24);
    bmpinfoheader[8] = (uint8_t)(bmp->h);
    bmpinfoheader[9] = (uint8_t)(bmp->h >> 8);
    bmpinfoheader[10] = (uint8_t)(bmp->h >>16);
    bmpinfoheader[11] = (uint8_t)(bmp->h >>24);

    file = fopen(filename, "w");
    fwrite(bmpfileheader, 1, 14, file);
    fwrite(bmpinfoheader, 1, 40, file);
    fwrite(bmp->pixels, 1, filesize - 54, file);
    fclose(file);
}

void image_t_delete(image_t* image)
{
    free(image->pixels);
    free(image);
}
