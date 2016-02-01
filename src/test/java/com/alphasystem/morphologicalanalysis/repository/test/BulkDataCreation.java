/**
 *
 */
package com.alphasystem.morphologicalanalysis.repository.test;

import com.alphasystem.morphologicalanalysis.spring.support.MongoConfig;
import com.alphasystem.morphologicalanalysis.spring.support.MorphologicalAnalysisSpringConfiguration;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static com.alphasystem.util.Utils.getTimeConsumed;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;

/**
 * @author sali
 */
@ContextConfiguration(classes = {MongoConfig.class, MorphologicalAnalysisSpringConfiguration.class})
public class BulkDataCreation extends AbstractTestNGSpringContextTests {

    @Autowired
    private MorphologicalAnalysisRepositoryUtil repositoryUtil;

    /**
     * Creates initial data.
     */
    @Test
    public void createChapters() {
        long startTime = currentTimeMillis();
        for (int chapterNumber = 1; chapterNumber <= 114; chapterNumber++) {
            long chapterStartTime = currentTimeMillis();
            repositoryUtil.createChapter(chapterNumber);
            long chapterEndTime = currentTimeMillis();
            out.printf("Time consume to save chapter {%s} is {%s}",
                    chapterNumber, getTimeConsumed(chapterEndTime
                            - chapterStartTime));
            out.println();
        }
        long endTime = currentTimeMillis();
        out.printf("Total time consume is {%s}", getTimeConsumed(endTime
                - startTime));
        out.println();
    }

}
