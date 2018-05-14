#include <stdlib.h>
#include <math.h>
#include "quadtree.h"
#include "locations.h"

#define BUCKET_SIZE 75

#define NW 0
#define NE 1
#define SW 2
#define SE 3

Quadtree* quadtree_create() {
  Quadtree *quadtree = malloc(sizeof(Quadtree));
  quadtree.quadtrees = NULL;
  quadtree.size = 0;
}

void quadtree_destroy(Quadtree* quadtree) {
  
}

void quadteee_isLeaf(Quadtree* quadtree) {

}

void quadtree_add(Quadtree* quadtree, Location location) {
  
}

void quadtree_addAll(Quadtree* quadtree, Locations location) {
  for (int i = 0; i < location.size; i++) {
    quadtree_add(quadtree, location.locations[i]);
  }
}

void quadtree_split(Quadtree* quadtree) {
  
}

void quadtree_count(Quadtree* quadtree, Rectangle viewport) {
  
}

void quadtree_countMatching(Quadtree* quadtree, Rectangle viewport, int (*f)(Location)) {
  
}

