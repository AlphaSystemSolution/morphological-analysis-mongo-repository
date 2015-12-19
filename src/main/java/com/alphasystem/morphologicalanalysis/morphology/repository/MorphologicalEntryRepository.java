package com.alphasystem.morphologicalanalysis.morphology.repository;

import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.persistence.mongo.repository.BaseRepository;

import java.util.List;

/**
 * @author sali
 */
public interface MorphologicalEntryRepository extends BaseRepository<MorphologicalEntry> {

    List<MorphologicalEntry> findByGroupTag(String groupTag);
}
