use quadtree::*;
use quadtree::projectors::mercator::*;
use quadtree::extra_math::next_after;

use image::*;

use std::f32;

pub struct HeatmapConfig {
    pub width: u32,
    pub height: u32,
    pub projection_center: Point,
    pub projection_scale: f32,
    pub pixel_size: u32
}

const COLOR_HUE : f32 = 80.0;
const COLOR_SATURATION : f32 = 0.44;

pub fn create_heatmap(quadtree: &Quadtree, config: HeatmapConfig) -> ImageBuffer<Rgba<u8>, Vec<u8>> {
    let image_width = config.width * config.pixel_size;
    let image_height = config.height * config.pixel_size;

    let mut buffer = ImageBuffer::new(image_width, image_height);
    let inside_box_size = next_after(config.projection_scale, f32::MIN);

    let projection_center = project_point(config.projection_center);

    let projection_width : f32 = config.projection_scale * config.width as f32;
    let projection_height : f32 = config.projection_scale * config.height as f32;

    let projection_start_x = projection_center.x - projection_width / 2.0;
    let projection_start_y = projection_center.y - projection_height / 2.0;

    let mut heatmap_values = vec![vec![0f32; image_height as usize]; image_width as usize];

    let mut max_value : f32 = 0.0;

    for pixel_y in 0..image_height {
        for pixel_x in 0..image_width {

            let box_start_x = projection_start_x + pixel_x as f32 * config.projection_scale;
            let box_start_y = projection_start_y + pixel_y as f32 * config.projection_scale;

            let value = (quadtree.count_in(Some(Rectangle::new(box_start_x, box_start_y, inside_box_size, inside_box_size))) as f32).ln();
            
            heatmap_values[pixel_x as usize][pixel_y as usize] = value;

            max_value = max_value.max(value);
        }
    }

    for (pixel_x, pixel_y, pixel_value) in buffer.enumerate_pixels_mut() {
        let h_x = pixel_x / config.pixel_size;
        let h_y = (config.height - 1) - (pixel_y / config.pixel_size);

        let n_points = heatmap_values[h_x as usize][h_y as usize];

        if n_points != f32::NEG_INFINITY {

            let value = (0.8 * (1.0 - n_points / max_value)) + 0.1;

            let rgb = hsv_to_rgb([COLOR_HUE, COLOR_SATURATION, value]);

            let r = (rgb[0] * 255.0) as u8;
            let g = (rgb[1] * 255.0) as u8;
            let b = (rgb[2] * 255.0) as u8;

            *pixel_value = Rgba([r, g, b, 255]);
        } else {
            *pixel_value = Rgba([255, 255, 255, 0]);
        }
    }

    return buffer;

}

fn hsv_to_rgb(hsv : [f32; 3]) -> [f32; 3] {
    let h = hsv[0] % 360.0;
    let s = hsv[1];
    let v = hsv[2];

    let h_p = h / 60.0;

    let c = v * s; 

    let x = c * (1.0 - (h_p % 2.0 - 1.0).abs());

    let mut rgb = match h_p as u32 {
        0 => [c, x, 0.0],
        1 => [x, c, 0.0],
        2 => [0.0, c, x],
        3 => [0.0, x, c],
        4 => [x, 0.0, c],
        5 => [c, 0.0, x],
        _ => panic!()
    };

    let m = v - c;

    rgb[0] += m;
    rgb[1] += m;
    rgb[2] += m;

    return rgb;
}

