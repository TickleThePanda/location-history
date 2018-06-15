use chrono::prelude::*;

use std::f32;

use quadtree::{ Rectangle, Point };

#[derive(Copy, Clone, Debug)]
pub struct Data {
    pub time: DateTime<Utc>
}

#[derive(Debug)]
pub struct Location {
    pub point: Point,
    pub data: Data
}

impl Location {
    pub fn get_bounds(locations: &Vec<Location>) -> Rectangle {
        let mut min_x = f32::MAX;
        let mut min_y = f32::MAX;
        let mut max_x = f32::MIN;
        let mut max_y = f32::MIN;

        for location in locations {
            min_x = min_x.min(location.point.x);
            min_y = min_y.min(location.point.y);
            max_x = max_x.max(location.point.x);
            max_y = max_y.max(location.point.y);
        }

        return Rectangle::new(min_x, min_y, max_x - min_x, max_y - min_y);
    }
}

