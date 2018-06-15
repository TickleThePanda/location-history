#![allow(dead_code)]
extern crate chrono;
extern crate image;
extern crate hyper;
extern crate futures;
extern crate url;
#[macro_use]
extern crate lazy_static;

mod quadtree;
mod heatmapper;

use std::thread;

use quadtree::*;
use quadtree::projectors::mercator::*;

use heatmapper::*;

use futures::Future;
use futures::sync::oneshot;

use hyper::{Method, Request, Response, StatusCode};
use hyper::error::Error;
use hyper::header::{ ContentLength, ContentType };
use hyper::server::{Http, Service};

use std::io::{self};

use url::Url;

use std::collections::HashMap;

lazy_static! {
    static ref QUADTREE: Quadtree = {
        create_quadtree()
    };
}

fn main() {

    // force lazy static to load the data in
    println!("Loading the quadtree.");
    let _ = &QUADTREE.bound;

    println!("Starting the webserver.");
	let addr = "127.0.0.1:3000".parse().unwrap();
    let server = Http::new().bind(&addr, move || Ok(HeatmapService)).unwrap();
    server.run().unwrap();
}

#[derive(Clone)]
struct HeatmapService;

const NOT_FOUND: &'static str = "Not Found";
const PHRASE: &'static str = "Hello world!";

impl Service for HeatmapService {
    type Request = Request;
    type Response = Response;
    type Error = hyper::Error;
    type Future = Box<Future<Item=Self::Response, Error=Self::Error>>;

    fn call(&self, req: Request) -> Self::Future {
        match (req.method(), req.uri().path()) {
            (&Method::Get, "/") => {

                let uri_string = req.uri().to_string();

                println!("{}", uri_string);

                let uri = uri_string.as_str();

                println!("{}", uri);

                let url = Url::parse(uri).unwrap();
                
                let query : HashMap<_, _> = url.query_pairs().into_owned().collect();

                println!("{:?}", query.get("t"));

				let (tx, rx) = oneshot::channel();
				thread::spawn(move || {
					let config = HeatmapConfig{
						width: 200,
						height: 190,
						projection_center: Point{ x: -2.8, y: 52.3 },
						projection_scale: 0.046,
						pixel_size: 4
					};

					let image_buffer = create_heatmap(&QUADTREE, config);

					let mut buf: Vec<u8> = Vec::new();

					image::ImageRgba8(image_buffer).write_to(&mut buf, image::PNG).unwrap();

					let res = Response::new()
                        .with_header(ContentType::png())
						.with_header(ContentLength(buf.len() as u64))
						.with_body(buf);

					tx.send(res).expect("Send error on successful file read");

				});

				Box::new(rx.map_err(|e| Error::from(io::Error::new(io::ErrorKind::Other, e))))

            },
            (_, _) => {
                Box::new(futures::future::ok(
                    Response::new()
                        .with_header(ContentLength(NOT_FOUND.len() as u64))
                        .with_status(StatusCode::NotFound)
                        .with_body(NOT_FOUND)
                ))
            
            }
        }
    }
}

fn create_quadtree() -> Quadtree {

    let mut locations = load_locations_from_file(String::from("input/history.csv"));

    project(&mut locations);

    return Quadtree::build(locations);
}
