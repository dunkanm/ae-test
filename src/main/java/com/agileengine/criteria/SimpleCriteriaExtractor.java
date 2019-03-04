package com.agileengine.criteria;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SimpleCriteriaExtractor implements CriteriaExtractor {

    private static Logger LOGGER = LoggerFactory.getLogger(SimpleCriteriaExtractor.class);

    private static final String SPACE = " ";
    private static final String CSS_CLASS_AN = "class";

    public List<Criteria> buildCriterias(Element referenceElement) {
        final List<Criteria> criteriaList = new ArrayList<>();
        List<Attribute> attributes = referenceElement.attributes().asList();
        if (!attributes.isEmpty()) {
            fillAttributeCriterias(attributes, criteriaList);
            fillClassCriterias(referenceElement, criteriaList);
        } else {
            LOGGER.warn("Reference element has no attributes. No attribute criterias can be used.");
        }
        fillContentCriterias(referenceElement, criteriaList);
        fillTagCriterias(referenceElement, criteriaList);
        return criteriaList;
    }

    private void fillTagCriterias(Element referenceElement, List<Criteria> criteriaList) {
        String referenceElementTag = referenceElement.tagName();
        criteriaList.add(element -> element.tagName().equals(referenceElementTag));
        LOGGER.debug("Criteria added for tag name: [{}]", referenceElementTag);
    }

    private void fillContentCriterias(Element referenceElement, List<Criteria> criteriaList) {
        String referenceElementOwnText = referenceElement.ownText();
        criteriaList.add(element -> element.hasText() && element.ownText().equals(referenceElementOwnText));
        LOGGER.debug("Criteria added for text content: [{}]", referenceElementOwnText);

    }

    /**
     * Class is the most important attribute so it should have more influence on the result.
     *
     * @param originalElement
     * @param criteriaList
     */
    private void fillClassCriterias(Element originalElement, List<Criteria> criteriaList) {
        String classAttribute = originalElement.attr(CSS_CLASS_AN);
        String[] originalCssClasses = classAttribute.split(SPACE);
        if (originalCssClasses.length > 0) {
            Stream.of(originalCssClasses).forEach(originalCssClass -> {
                        criteriaList.add(element -> {
                            String candidateClassAttr = element.attr(CSS_CLASS_AN);
                            String[] candidateCssClasses = candidateClassAttr.split(SPACE);
                            if (candidateCssClasses.length > 0) {
                                for (String candidateCssClass : candidateCssClasses) {
                                    if (candidateCssClass.equals(originalCssClass)) {
                                        return true;
                                    }
                                }
                            }
                            return false;
                        });
                        LOGGER.debug("Criteria added for css class: [{}]", originalCssClass);
                    }
            );
        } else {
            LOGGER.warn("Reference element has no css classes. No css class criterias can be extracted.");
        }
    }

    private void fillAttributeCriterias(List<Attribute> attributes, List<Criteria> criteriaList) {
        attributes.forEach(
                attribute -> {
                    criteriaList.add(element ->
                            element.hasAttr(attribute.getKey())
                                    && element.attr(attribute.getKey()).equals(attribute.getValue())
                    );
                    LOGGER.debug("Criteria added for attribute: [{}]", attribute);
                }
        );
    }
}
