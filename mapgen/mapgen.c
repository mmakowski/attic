#include <stdlib.h>
#include <string.h>

#include "mapgen.h"

/** variation reduction after square step */
#define RED_VAR_SQUARE 0.75
/** variation reduction after diamond step */
#define RED_VAR_DIAMOND 0.66
/** variation reduction after 1D displacement step */
#define RED_VAR_1D 0.5

#define min(x, y) (x) > (y) ? (y) : (x)

static unsigned int tile_size = 512;

/**
 * Perform diamond-square algorithm on supplied map
 */	
void diamond_square(
		struct height_map * map, 
		const unsigned int div, 
		unsigned int var)
{
	// TODO: support for torus and cylinder

	int side, hside;
	int x, y;
	int p1, p2, p3, p4, ph;
	double varf;

	srand(map->seed);
	varf = (double) var / (double) RAND_MAX;
	for (side = (min(map->width, map->height) - 1) >> div; side > 1; side /= 2) {
		hside = side / 2;
		// perform square step
		for (y = hside; y < map->height; y += side) {
			for (x = side / 2; x < map->width; x+= side) {
				if (MAP_POINT(map, x, y) != HEIGHT_UNSET)
					continue;

				p1 = (int) (IN_MAP(map, x - hside, y - hside) ? MAP_POINT(map, x - hside, y - hside) : HEIGHT_DEFAULT);
				p2 = (int) (IN_MAP(map, x + hside, y - hside) ? MAP_POINT(map, x + hside, y - hside) : HEIGHT_DEFAULT);
				p3 = (int) (IN_MAP(map, x + hside, y + hside) ? MAP_POINT(map, x + hside, y + hside) : HEIGHT_DEFAULT);
				p4 = (int) (IN_MAP(map, x - hside, y + hside) ? MAP_POINT(map, x - hside, y + hside) : HEIGHT_DEFAULT);
				
				ph = (p1 + p2 + p3 + p4) / 4 + (int) (rand() * varf - var / 2);
				ph = (ph < HEIGHT_MIN) ? HEIGHT_MIN : ph;
				ph = (ph > HEIGHT_MAX) ? HEIGHT_MAX : ph;
			
				MAP_POINT(map, x, y) = (height_map_point) ph;
			}
		}
		var *= RED_VAR_SQUARE; // TODO: parameterize
		varf = (double) var / (double) RAND_MAX;
		// perform diamond step
		for (y = 0; y < map->height; y += side / 2) {
			for (x = (y % side == 0) ? side / 2 : 0; x < map->width; x+= side) {
				if (MAP_POINT(map, x, y) != HEIGHT_UNSET)
					continue;

				p1 = (int) (IN_MAP(map, x, y - hside) ? MAP_POINT(map, x, y - hside) : HEIGHT_DEFAULT);
				p2 = (int) (IN_MAP(map, x + hside, y) ? MAP_POINT(map, x + hside, y) : HEIGHT_DEFAULT);
				p3 = (int) (IN_MAP(map, x, y + hside) ? MAP_POINT(map, x, y + hside) : HEIGHT_DEFAULT);
				p4 = (int) (IN_MAP(map, x - hside, y) ? MAP_POINT(map, x - hside, y) : HEIGHT_DEFAULT);
				
				ph = (p1 + p2 + p3 + p4) / 4 + (int) (rand() * varf - var / 2);
				ph = (ph < HEIGHT_MIN) ? HEIGHT_MIN : ph;
				ph = (ph > HEIGHT_MAX) ? HEIGHT_MAX : ph;
			
				MAP_POINT(map, x, y) = (height_map_point) ph;
			}
		}
		var *= RED_VAR_DIAMOND; // TODO: parameterize
		varf = (double) var / (double) RAND_MAX;
	}
}


/**
 * Create height map structure under given address and allocate memory for the map
 */
int init_height_map(
		const unsigned int width, 
		const unsigned int height, 
		const enum map_type type,
		struct height_map **map)
{
	// allocate memory for the map
	if ((*map = malloc(sizeof(struct height_map))) == 0)
		return ENOMEM;
	(*map)->seed = seed;
	(*map)->width = width;
	(*map)->height = height;
	if (((*map)->map = calloc(width * height, sizeof(height_map_point))) == 0)
		return ENOMEM;
	
	// unset all heights
	memset((*map)->map, HEIGHT_UNSET, width * height);
}


/**
 * generate map using diamond-square algorithm
 * \param seed map id -- used as a seed for random number generator
 * \param width map width
 * \param height map height
 * \param type type of map: planar, cylindrical or toroidal
 * \param div non-negative number indicating land diversity level
 * \param map (out) address of pointer to the resulting map
 * \return error code
 */
int gen_height_map(
		const unsigned int seed, 
		const unsigned int width, 
		const unsigned int height, 
		const enum map_type type,
		const unsigned int div,
		struct height_map **map)
{
	int x, y, measure, r;
	
	if ((r = init_height_map(width, height, map)) != OK)
		return r;

	// set reference points
	measure = min(width, height);
	for (y = 0; y < height; y += ((measure - 1) >> div)) {
		for (x = 0; x < width; x += ((measure - 1) >> div)) {
			MAP_POINT(*map, x, y) = HEIGHT_DEFAULT;
		}
	}
	
	// generate map
	diamond_square(*map, div, 255);
	
	return OK;
}


int gen_tile(
		struct height_map *map,
		const unsigned int x,
		const unsigned int y,
		struct height_map **tile)
{
	// TODO
	
	return OK;
}