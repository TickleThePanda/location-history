#include <stdlib.h>
#include <stdio.h>
#include <math.h>

#include "locations.h"
#include "reader.h"

#define BUCKET_SIZE 75

#define NW 0
#define NE 1
#define SW 2
#define SE 3

typedef struct Quadtree {
  struct Quadtree *quadtrees[4];
  Location locations[BUCKET_SIZE];
  int stored;
} Quadtree;


void project(Location *loc) {
  
}

void main(int argc, char *argv[]) {

  if (argc != 2) {
    printf("Please provide a path to the input file.\n");
    exit(1);
  }

  char *filePath = argv[1];

  Locations locations = loadLocations(filePath);

  for (int i = 0; i < locations.size; i++) {
    Location loc = locations.locations[i];
    project(&loc);
  }

  free(locations.locations);
}

