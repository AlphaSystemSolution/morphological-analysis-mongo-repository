package com.alphasystem.morphologicalanalysis.morphology.repository;

import com.alphasystem.arabic.model.ArabicLetterType;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.persistence.mongo.repository.BaseRepository;

/**
 * @author sali
 */
public interface RootLettersRepository extends BaseRepository<RootLetters> {

    /**
     * @param firstRadical
     * @param secondRadical
     * @param thirdRadical
     * @return
     */
    RootLetters findByFirstRadicalAndSecondRadicalAndThirdRadical(ArabicLetterType firstRadical,
                                                                  ArabicLetterType secondRadical,
                                                                  ArabicLetterType thirdRadical);

    /**
     * @param firstRadical
     * @param secondRadical
     * @param thirdRadical
     * @param fourthRadical
     * @return
     */
    RootLetters findByFirstRadicalAndSecondRadicalAndThirdRadicalAndFourthRadical(ArabicLetterType firstRadical,
                                                                                  ArabicLetterType secondRadical,
                                                                                  ArabicLetterType thirdRadical,
                                                                                  ArabicLetterType fourthRadical);
}
