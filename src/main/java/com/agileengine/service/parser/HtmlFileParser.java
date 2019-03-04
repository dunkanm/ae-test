package com.agileengine.service.parser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.Optional;

public interface HtmlFileParser {

    Optional<Element> findElementById(String id, File htmlFile);

    Optional<Elements> findElementsByQuery(String query, File htmlFile);
}
