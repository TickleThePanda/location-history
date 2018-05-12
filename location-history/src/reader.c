#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>

#include "locations.h"

#define MAX_INPUT_LINE_SIZE 100

int precountFileLength(FILE *file) {
  int len = 0;

  char buf[MAX_INPUT_LINE_SIZE];

  while(fgets(buf, MAX_INPUT_LINE_SIZE, file) != NULL) {
    len++;
  }

  rewind(file);

  return len;
}

Locations loadLocations(char *filePath) {

  if (access(filePath, R_OK) == -1) {
    printf("Could not find file you were looking for \"%s\"\n.", filePath);
    exit(1);
  }

  FILE *f = fopen(filePath, "r");

  int len = precountFileLength(f);

  Location *locations = malloc(len * sizeof(Location));

  long lonE7;
  long latE7;
  long time;

  int i = 0;

  while (fscanf(f, "%ld, %ld, %ld", &lonE7, &latE7, &time) != EOF) {
    locations[i].x = ((float) lonE7) / 10000000.0f;
    locations[i].y = ((float) latE7) / 10000000.0f;
    locations[i].time = (time_t) time / 1000L;

    if (lonE7 == 0L && latE7 == 0L) {
      len--;

      locations = realloc(locations, len * sizeof(Location)); 

      printf("Removing point %ld, %ld, %ld\n", lonE7, latE7, time);

      continue;
    }

    i++;
  }

  fclose(f);

  Locations allLocations = {locations, len};
  
  return allLocations;
}
