package com.agileengine;

import com.agileengine.service.ElementFinder;
import com.agileengine.service.dto.RatingEntry;
import com.agileengine.service.exception.XmlAnalyzerException;
import com.agileengine.service.impl.SimpleElementFinder;
import com.agileengine.service.parser.impl.HtmlFileParserImpl;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * Application is hardly bound on jsoup, should avoid this
 */
public class XmlAnalyzerApplication {

    private static final String ORIGIN_BUTTON_ID = "make-everything-ok-button";

    private static Logger LOGGER = LoggerFactory.getLogger(XmlAnalyzerApplication.class);

    public static void main(String[] args) {

        if (args.length < 2
                || args[0] == null
                || args[0].isEmpty()
                || args[1] == null
                || args[1].isEmpty()) {
            LOGGER.error("Please provide file paths.");
            System.err.println("Please provide file paths.");
        }

        File originFile = new File(args[0]);
        File diffFile = new File(args[1]);

        if (!originFile.exists()) {
            LOGGER.error("Origin file doest not exist.");
            System.err.println("Origin file doest not exist.");
        }
        if (!diffFile.exists()) {
            LOGGER.error("Diff file doest not exist.");
            System.err.println("Diff file doest not exist.");
        }
        ElementFinder elementFinder = new SimpleElementFinder(new HtmlFileParserImpl());
        List<RatingEntry> elementsRating;
        try {
            elementsRating = elementFinder.findSimilar(ORIGIN_BUTTON_ID, originFile, diffFile);
            printResults(elementsRating);
        } catch (XmlAnalyzerException e) {
            LOGGER.error("Fatal error during application execution. ", e);
            System.err.println("Fatal error during application execution. ");
        }
    }

    private static void printResults(List<RatingEntry> ratingEntries) {
        ratingEntries.forEach(ratingEntry -> {
            String absPath = getXPath(ratingEntry);
            System.out.println("Rating: " + ratingEntry.getRating() + "  Path: " + absPath);
        });
    }

    private static String getXPath(RatingEntry ratingEntry) {
        StringBuilder absPath = new StringBuilder();
        Elements parents = ratingEntry.getElement().parents();
        for (int j = parents.size() - 1; j >= 0; j--) {
            Element element = parents.get(j);
            addXpathNode(absPath, element);
        }
        addXpathNode(absPath, ratingEntry.getElement());
        return absPath.toString();
    }

    private static void addXpathNode(StringBuilder absPath, Element element) {
        absPath.append("/");
        absPath.append(element.tagName());
        absPath.append("[");
        absPath.append(element.siblingIndex());
        absPath.append("]");
    }
}
