# Location History

## Usage

  1. Retrieve your data from Google: https://takeout.google.com/settings/takeout
  2. Load the data from file

`GoogleLocations locations = GoogleLocations.Loader.fromFile(String fileName);`
  3. Strip out any inaccurate points to give a better view

`GoogleLocations filteredLocations = locations.filterInnacurate(1000);`
  4. Convert the data to a more usable coordinate system (I use the [Equirectangular projection](https://en.wikipedia.org/wiki/Equirectangular_projection) for simplicity)

`List<EcpPoints> points =  GoogleLocationToEcpConverter.convertToECPPoints()`
  5. 

### Retrieving location data

### Examples
