use std::collections::HashMap;
use std::collections::hash_map::Entry::{Occupied, Vacant};
use std::f32;

use quadtree::*;

const MAX_POINTS : usize = 75;

#[derive(Debug)]
pub struct Quadtree {
    trees: Option<Box<[Quadtree ; 4]>>,
    pub bound: Rectangle,
    points: HashMap<Point, Vec<Data>>
}

impl Quadtree {
    pub fn build(locations : Vec<Location>) -> Quadtree {
        let mut root = Quadtree{
            trees: None,
            bound: Location::get_bounds(&locations),
            points: HashMap::new()
        };

        root.add_all(&locations);
        return root;
    }

    fn new(bound : Rectangle) -> Quadtree {
        return Quadtree {
            trees: None,
            bound: bound,
            points: HashMap::new()
        };
    }

    fn add_all(&mut self, locations : &Vec<Location>) {
        for location in locations {
            self.add(&location);
        }
    }

    fn add(&mut self, location : &Location) -> bool {

        if !self.bound.contains(&location.point) {
            return false;
        }

        match self.trees {
            Some(_) => { self.add_to_subtrees(location); }
            None => {
                if self.points.len() > MAX_POINTS {
                    self.split();
                    self.add_to_subtrees(location);
                } else {
                    let data_to_add = location.data;
                    let point = location.point;

                    match self.points.entry(point) {
                        Vacant(entry) => {
                            entry.insert(vec![data_to_add]);
                        },
                        Occupied(mut entry) => {
                            entry.get_mut().push(data_to_add);
                        }
                    }
                }
            }
        }

        return true;

    }

    fn split(&mut self) {

        let min_x = self.bound.x;
        let min_y = self.bound.y;

        let new_width = self.bound.width / 2.0;
        let new_height = self.bound.height / 2.0;
         
        let center_x = quadtree::extra_math::next_after(self.bound.x + new_width, f32::MAX);
        let center_y = quadtree::extra_math::next_after(self.bound.y + new_height, f32::MAX);

        self.trees = Some(Box::new([
            Quadtree::new(Rectangle::new(min_x, min_y, new_width, new_height )),
            Quadtree::new(Rectangle::new(min_x, center_y, new_width, new_height )),
            Quadtree::new(Rectangle::new(center_x, min_y, new_width, new_height )),
            Quadtree::new(Rectangle::new(center_x, center_y, new_width, new_height ))
        ]));

        for location in self.get_locations_to_add().iter() {
            self.add_to_subtrees(location);
        }

        self.points = HashMap::new();
    }

    fn get_locations_to_add(&self) -> Vec<Location> {
        let mut locations_to_add = Vec::new();

        for (point, data_vec) in self.points.iter() {
            for data in data_vec.iter() {
                locations_to_add.push(Location{ point: *point, data: *data});
            }
        }

        return locations_to_add;
    }

    fn add_to_subtrees(&mut self, location : &Location) {

        match self.trees {
            Some(ref mut subtrees) => {
                for mut subtree in subtrees.iter_mut() {
                    if subtree.add(location) {
                        break;
                    }
                }
            },
            None => { unreachable!(); }
        }

    }

    pub fn count_all(&self) -> usize {
        self.count_in(None)
    }

    pub fn count_in(&self, area_option: Option<Rectangle>) -> usize {
        self.count_in_where(area_option, None)
    }

    pub fn count_in_where(&self, area_option: Option<Rectangle>, matcher_option: Option<&Fn(&Data)  -> bool>) -> usize {
        let mut to_visit = vec![self];

        let mut count = 0;

        while to_visit.len() > 0 {
            let current = to_visit.pop().unwrap();

            match current.trees {
                Some(ref subtrees) => {
                    match &area_option {
                        Some(area) => {
                            for subtree in subtrees.iter() {
                                if subtree.bound.intersects(area) {
                                    to_visit.push(subtree);
                                }
                            }
                        },
                        None => {
                            to_visit.extend(subtrees.iter());
                        }
                    }
                },
                None => ()
            }

            for (point, data) in current.points.iter() {
                match (&area_option, matcher_option) {
                    (Some(area), Some(matcher)) => {
                        if area.contains(point) {
                            for datum in data {
                                if matcher(datum) {
                                    count += 1;
                                }
                            }
                        }
                    },
                    (Some(area), None) => {
                        if area.contains(point) {
                            count += data.len();
                        }
                    },
                    (None, Some(matcher)) => {
                        for datum in data {
                            if matcher(datum) {
                                count += 1;
                            }
                        }
                        
                    },
                    (None, None) => {
                        count += data.len();
                    }
                }
            }
        }

        return count;
    }

}

