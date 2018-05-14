#include <stdlib.h>
#include <stdio.h>
#include <math.h>

#include "locations.h"
#include "reader.h"
#include "quadtree.h"

void main(int argc, char *argv[]) {

  if (argc != 2) {
    printf("Please provide a path to the input file.\n");
    exit(1);
  }

  char *filePath = argv[1];

  Locations locations = loadLocations(filePath);

  Quadtree* quadtree = quadtree_create();

  for (int i = 0; i < locations.size; i++) {
    Location loc = locations.locations[i];
    project(&loc);
  }

  quadtree_addAll(quadtree, *locations);

  free(locations.locations);


}

