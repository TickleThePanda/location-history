const fs = require('fs');

const data = fs.readFileSync('./input/history.json', 'utf-8');
const history = JSON.parse(data);
const output = history.locations
        .map((location) => `${location.longitudeE7}, ${location.latitudeE7}, ${location.timestampMs}`)
        .join('\n');

fs.writeFileSync('./output/history.csv', output);


