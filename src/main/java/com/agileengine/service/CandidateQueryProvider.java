package com.agileengine.service;

import org.jsoup.nodes.Element;

public interface CandidateQueryProvider {

    String provideCandidateQuery(Element element);
}
