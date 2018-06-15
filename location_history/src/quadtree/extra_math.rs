extern crate libc;

use self::libc::{c_float};

#[link_name = "m"]
extern {
    fn nextafterf(x: c_float, y: c_float) -> c_float;
}

pub fn next_after(value: f32, towards: f32) -> f32 {
    unsafe { nextafterf(value, towards) }
}

