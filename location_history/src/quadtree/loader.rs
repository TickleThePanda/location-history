use std::io::prelude::*;
use std::io::BufReader;

use std::fs::File;

use chrono::prelude::*;

use quadtree::{ Point, Data, Location };

pub fn load_locations_from_file(file_name: String) -> Vec<Location> {

    let f = File::open(file_name).unwrap();

    let reader = BufReader::new(f);

    let mut vect: Vec<Location> = Vec::new();

    for line in reader.lines() {
        let l = line.unwrap();

        let s: Vec<&str> = l.split(", ").collect();
        let lon_e7: f32 = s[0].parse().unwrap();
        let lat_e7: f32 = s[1].parse().unwrap();
        let ts: i64 = s[2].parse().unwrap();

        let lon = lon_e7 / 10000000.0;
        let lat = lat_e7 / 10000000.0;

        if lon == 0.0 && lat == 0.0 {
            continue;
        }

        let naive_time = NaiveDateTime::from_timestamp(ts / 1000, 0);

        let timestamp: DateTime<Utc> = DateTime::from_utc(naive_time, Utc);

        vect.push(Location {
            point: Point {
                x: lon,
                y: lat
            },
            data: Data {
                time: timestamp
            }
        });
    }

    return vect;
}
