use std::f32;
use quadtree::{Point, Location};

pub fn project(locations: &mut Vec<Location>) {
    for mut location in locations {
        location.point = project_point(location.point);
    }
}

pub fn project_point(mut point: Point) -> Point {
    point.y = ((point.y / 90.0 + 1.0) * f32::consts::PI / 4.0).tan().ln() * 180.0 / f32::consts::PI;

    return point;
}


