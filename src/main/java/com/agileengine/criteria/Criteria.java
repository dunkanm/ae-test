package com.agileengine.criteria;

import org.jsoup.nodes.Element;

public interface Criteria {

    boolean apply(Element element);
}
