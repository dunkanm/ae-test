package com.agileengine.service.dto;

import org.jsoup.nodes.Element;

public class RatingEntry {

    private final int rating;

    private final Element element;

    public RatingEntry(int rating, Element element) {
        this.rating = rating;
        this.element = element;
    }

    public int getRating() {
        return rating;
    }

    public Element getElement() {
        return element;
    }
}
