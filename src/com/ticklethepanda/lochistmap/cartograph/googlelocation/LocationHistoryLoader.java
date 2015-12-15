package com.ticklethepanda.lochistmap.cartograph.googlelocation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class LocationHistoryLoader {
  private Location locations[];

  public Location[] getLocations() {
    return locations;
  }

  public void setLocations(Location locations[]) {
    this.locations = locations;
  }

  public static LocationHistoryLoader loadFromFile(String fileName)
      throws JsonSyntaxException, JsonIOException, FileNotFoundException {
    Gson gson = new Gson();

    return gson.fromJson(new BufferedReader(new FileReader("input/"
        + fileName + ".json")), LocationHistoryLoader.class);

  }
}
