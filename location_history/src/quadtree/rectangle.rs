use quadtree::Point;

#[derive(Debug)]
pub struct Rectangle {
    pub x: f32,
    pub y: f32,
    pub width: f32,
    pub height: f32,
    max_x: f32,
    max_y: f32
}

impl Rectangle {

    pub fn new(x: f32, y: f32, width: f32, height: f32) -> Rectangle {
        Rectangle { x: x, y: y, width: width, height: height, max_x: x + width, max_y: y + height }
    }

    pub fn contains(&self, point : &Point) -> bool {
        self.x <= point.x && point.x <= self.max_x && self.y <= point.y && point.y <= self.max_y
    }

    pub fn intersects(&self, other : &Rectangle) -> bool {
        self.x <= other.max_x
            && self.max_x >= other.x
            && self.y <= other.max_y
            && self.max_y >= other.y
    }
}

