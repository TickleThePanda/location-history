# Location History [![Build Status](https://travis-ci.org/TickleThePanda/location-history.svg?branch=master)](https://travis-ci.org/TickleThePanda/location-history)

Using location history provided by Google and the code in this repo, you can generate heatmaps of you location history.

## Usage

  1. Retrieve your data from Google: https://takeout.google.com/settings/takeout
  2. Load the data from file: `GoogleLocations locations = GoogleLocations.Loader.fromFile(String fileName);`
  3. Strip out any inaccurate points to give a better view: `GoogleLocations filteredLocations = locations.filterInnacurate(1000);`
  4. Convert the data to a more useable coordinate system (I use the [Equirectangular projection](https://en.wikipedia.org/wiki/Equirectangular_projection) for simplicity): 
`List<EcpPoints> points =  GoogleLocationToEcpConverter.convertToECPPoints()`
  5. Convert the list of points to a [quadtree](https://en.wikipedia.org/wiki/Quadtree) (a more efficient way to access the data): `Quadtree quadtree = new Quadtree(points);`
  6. Create the heatmap array: `Heatmap heatmap = quadtree.convertToHeatmap(int numberOfHorizontalBlocks, int numberOfVerticalBlocks);`
  7. Define a heatmap painter: `HeatmapPainter painter = new HeatmapPainter(new HeatmapPainter.HeatmapColourPicker.Monotone());`
  8. Generate an image from the heatmap: `BufferedImage image = painter.paintHeatmap(heatmap, int numberOfPixelsPerBlock);`
  9. Save the image to file: `ImageWriter.writeImageOut(image, String filePath);`

When you get to step 4, you can modify what data you want to show (i.e. splitting it by month). To see some examples of how I have done it, see the classes in https://github.com/TickleThePanda/location-history/tree/master/src/main/java/co/uk/ticklethepanda/lochistmap
