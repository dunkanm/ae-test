package com.agileengine.service.impl;

import com.agileengine.criteria.Criteria;
import com.agileengine.criteria.CriteriaExtractor;
import com.agileengine.criteria.SimpleCriteriaExtractor;
import com.agileengine.service.CandidateQueryProvider;
import com.agileengine.service.ElementFinder;
import com.agileengine.service.dto.RatingEntry;
import com.agileengine.service.exception.XmlAnalyzerException;
import com.agileengine.service.parser.HtmlFileParser;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleElementFinder implements ElementFinder {

    private static Logger LOGGER = LoggerFactory.getLogger(SimpleElementFinder.class);

    private static final int RATING_LIMIT = 10;

    private final HtmlFileParser fileParser;
    private final CriteriaExtractor criteriaExtractor;
    private final CandidateQueryProvider candidateQueryProvider;

    public SimpleElementFinder(HtmlFileParser fileParser) {
        this.fileParser = fileParser;
        this.criteriaExtractor = new SimpleCriteriaExtractor();
        this.candidateQueryProvider = new SimpleCandidateQueryProvider();
    }

    @Override
    public List<RatingEntry> findSimilar(String referenceElementId, File originFile, File diffFile) {
        Element originalButton = fileParser.findElementById(referenceElementId, originFile)
                .orElseThrow(() -> new XmlAnalyzerException("Could not find original element"));

        List<Criteria> criteriaList = buildCriterias(originalButton);
        List<Element> candidates = findCandidates(originalButton, diffFile);
        if (candidates.isEmpty()) {
            LOGGER.warn("No candidates for reference element found!");
            return Collections.EMPTY_LIST;
        }

        return candidates.stream()
                .map(candidate -> {
                    int rating = 0;
                    for (Criteria criteria : criteriaList) {
                        if (criteria.apply(candidate)) {
                            rating++;
                        }
                    }
                    return new RatingEntry(rating, candidate);
                })
                .sorted(Comparator.comparingInt(RatingEntry::getRating).reversed())
                .limit(RATING_LIMIT)
                .filter(ratingEntry -> ratingEntry.getRating() > 0)
                .collect(Collectors.toList());

    }

    private List<Element> findCandidates(Element referenceElement, File diffFile) {
        String css = candidateQueryProvider.provideCandidateQuery(referenceElement);
        return fileParser.findElementsByQuery(css, diffFile)
                .orElseThrow(() -> new XmlAnalyzerException("Error reading file. No candidates can be found."));
    }

    private List<Criteria> buildCriterias(Element originalButton) {
        return criteriaExtractor.buildCriterias(originalButton);
    }

}
