typedef struct Location {
  float x;
  float y;
  time_t time;
} Location;

typedef struct Locations {
  Location *locations;
  int size;
} Locations;

