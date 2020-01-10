require('dotenv').config();

const zlib = require('zlib');
const fs = require('fs');

const AWS = require('aws-sdk');

const s3 = new AWS.S3();

run().catch(() => console.log("An error occured"));

async function readFile(path) {
  return new Promise((resolve, reject) => {
    fs.readFile(path, function(err, data) {
      if (err) {
        reject(err);
      } else {
        resolve(data);
      }
    });
  });
}

async function gzip(data) {
  return new Promise((resolve, reject) => {
    zlib.gzip(data, function(err, data) {
      if (err) {
        reject(err);
      } else {
        resolve(data);
      }
    });
  });
}

async function run() {

  const filePath = await process.argv[2];

  if (!filePath) {
    throw new Error("path to file must be specified as first argument");
  }

  console.log('Reading file from ' + filePath);

  const data = await readFile(filePath);

  console.log('Zipping file');

  const zipped = await gzip(data);

  console.log('Uploading file');

  const params = {
    Body: zipped,
    Bucket: 'location-history',
    Key: 'history.json',
    ContentEncoding: 'gzip'
  }
  
  const result = await s3.putObject(params).promise();

  

  console.log(result);

}

