use std::hash::{Hash, Hasher};

#[derive(Copy, Clone, Debug)]
pub struct Point {
    pub x: f32,
    pub y: f32,
}

impl PartialEq for Point {
    fn eq(&self, other : &Point) -> bool {
        self.x == other.x && self.y == other.y
    }
}

impl Eq for Point {}

impl Hash for Point {
    fn hash<H: Hasher>(&self, state : &mut H) {
        state.write_u32(self.x.to_bits());
        state.write_u32(self.y.to_bits());
    }
}

