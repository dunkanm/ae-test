package com.agileengine.service.impl;

import com.agileengine.service.CandidateQueryProvider;
import org.jsoup.nodes.Element;

public class SimpleCandidateQueryProvider implements CandidateQueryProvider {

    /**
     * Some complicated logic could be here :)
     * @param element reference element
     * @return
     */
    public String provideCandidateQuery(Element element) {
        return "body *";//element.tagName();
    }
}
