#include <stdlib.h>
#include <stdio.h>
#include "mapgen.h"

int main(int argc, char *argv[])
{
	struct height_map *m;
	int x, y;
	int errno;
	height_map_point p;
	int seed, width, height, div;
	if (argc < 5)
		return 2;
	seed = atoi(argv[1]);
	width = atoi(argv[2]);
	height = atoi(argv[3]);
	div = atoi(argv[4]);
	
	if ((errno = gen_height_map(seed, width, height, PLANE, div, &m)) != OK) {
		fprintf(stderr, "error in  generate_height_map: %d", errno);
		return 1;
	}
	for (y = 0; y < m->height; y++) {
		for (x = 0; x < m->width; x++) {
			p = MAP_POINT(m, x, y);
			if (p < 0) {
				printf("  ");
			} else if (p < 20) {
				printf("..");
			} else if (p < 40) {
				printf("''");
			} else if (p < 60) {
				printf("::");
			} else if (p < 80) {
				printf("!!");
			} else if (p < 100) {
				printf("||");
			} else {
				printf("##");
			}
			// printf("%+4d ", p);
		}
		printf("\n");
	}
		
	return 0;
}
