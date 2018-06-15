const fs = require('fs');

const data = fs.readFileSync('./history.json', 'utf-8');
const history = JSON.parse(data);
const output = history.locations
        .map((location) => `${location.longitudeE7}, ${location.latitudeE7}, ${location.timestampMs}`)
        .join('\n');

fs.writeFileSync('./history.csv', output);


