#ifndef MAPGEN_H_
#define MAPGEN_H_

#define OK 0
#define ENOMEM 1

typedef char height_map_point;

/** map topology */
enum map_type { 
	PLANE,		/** plane -- map edges are not connected */
	CYLINDER, 	/** cylinder -- left edge is connected to right edge, top and bottom are not connected */
	TORUS 		/** torus -- left edge is connected to right and top is connected to bottom */
};

/**
 * Structure representing a height map.
 */
struct height_map {
    unsigned int seed;
	unsigned int width;
	unsigned int height;
	enum map_type type;
	height_map_point * map; 
};

#define HEIGHT_MAX 127
#define HEIGHT_MIN -128
#define HEIGHT_DEFAULT 0
#define HEIGHT_UNSET HEIGHT_MIN
#define MAP_POINT(m, x, y) (m)->map[(y) * (m)->width + (x)]
#define IN_MAP(m, x, y) ((x) >= 0 && (y) >= 0 && (x) < (m)->width && (y) < (m)->height)

/**
 * Generate height map with supplied parameters.
 * 
 * \param seed map id -- used as a seed for random number generator
 * \param width map width
 * \param height map height
 * \param type type of map: planar, cylindrical or toroidal
 * \param diversity non-negative number indicating land diversity level
 * \param map (out) address of pointer to the resulting map
 * \return error code
 */
extern int gen_height_map(
		const unsigned int seed, 
		const unsigned int width, 
		const unsigned int height, 
		const enum map_type type,
		const unsigned int diversity,
		struct height_map **map);

#endif /*MAPGEN_H_*/
