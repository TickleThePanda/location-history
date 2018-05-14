#ifndef LOCATIONS_H
#define LOCATIONS_H

typedef struct Location {
  float x;
  float y;
  time_t time;
} Location;

typedef struct Locations {
  Location *locations;
  int size;
} Locations;

void project(Location* location);

#endif
