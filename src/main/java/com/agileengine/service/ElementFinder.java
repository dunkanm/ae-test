package com.agileengine.service;

import com.agileengine.service.dto.RatingEntry;

import java.io.File;
import java.util.List;

public interface ElementFinder {

    List<RatingEntry> findSimilar(String referenceElementId, File originFile, File diffFile);
}
