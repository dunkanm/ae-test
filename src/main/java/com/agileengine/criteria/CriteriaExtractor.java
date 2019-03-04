package com.agileengine.criteria;

import org.jsoup.nodes.Element;

import java.util.List;

public interface CriteriaExtractor {

    List<Criteria> buildCriterias(Element referenceElement);
}
