#include <stdlib.h>
#include <math.h>
#include "locations.h"

#define DEG2RAD(a)   ((a) / (180 / M_PI))
#define RAD2DEG(a)   ((a) * (180 / M_PI))
#define EARTH_RADIUS 6378137

void project(Location *loc) {
  loc->x = loc->x;
  loc->y = RAD2DEG(log(tan(DEG2RAD(loc->y) / 2 + M_PI / 4 )));
}

