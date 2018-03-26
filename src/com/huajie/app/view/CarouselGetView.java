package com.huajie.app.view;

import java.util.List;

public class CarouselGetView {
	private String result;
	private String carousel_version;
	private List<Carousel> carousel_list;
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getCarousel_version() {
		return carousel_version;
	}
	public void setCarousel_version(String carousel_version) {
		this.carousel_version = carousel_version;
	}
	public List<Carousel> getCarousel_list() {
		return carousel_list;
	}
	public void setCarousel_list(List<Carousel> carousel_list) {
		this.carousel_list = carousel_list;
	}
}
