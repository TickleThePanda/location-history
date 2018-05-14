#ifndef QUADTREE_H
#define QUADTREE_H

#include "locations.h"

typedef struct Rectangle {
  float x;
  float y;
  float width;
  float height;
} Rectangle;

typedef struct Quadtree {
  struct Quadtree *quadtrees[4];
  Location locations[BUCKET_SIZE];
  int stored;
} Quadtree;

Quadtree* quadtree_create();

void quadtree_destroy(Quadtree* quadtree);

void quadtree_add(Quadtree* quadtree, Location location);

void quadtree_addAll(Quadtree* quadtree, Locations location);

void quadtree_split(Quadtree* quadtree);

void quadtree_count(Quadtree* quadtree, Rectangle viewport);

void quadtree_countMatching(Quadtree* quadtree, Rectangle viewport, int (*f)(Location));

#endif

