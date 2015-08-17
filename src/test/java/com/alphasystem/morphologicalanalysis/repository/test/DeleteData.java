package com.alphasystem.morphologicalanalysis.repository.test;

import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.graph.model.Fragment;
import com.alphasystem.morphologicalanalysis.graph.model.Relationship;
import com.alphasystem.morphologicalanalysis.graph.model.Terminal;
import com.alphasystem.morphologicalanalysis.graph.model.support.TerminalType;
import com.alphasystem.morphologicalanalysis.graph.repository.DependencyGraphRepository;
import com.alphasystem.morphologicalanalysis.graph.repository.FragmentRepository;
import com.alphasystem.morphologicalanalysis.graph.repository.RelationshipRepository;
import com.alphasystem.morphologicalanalysis.graph.repository.TerminalRepository;
import com.alphasystem.morphologicalanalysis.spring.support.GraphConfig;
import com.alphasystem.morphologicalanalysis.spring.support.MongoConfig;
import com.alphasystem.morphologicalanalysis.spring.support.MorphologicalAnalysisSpringConfiguration;
import com.alphasystem.morphologicalanalysis.spring.support.WordByWordConfig;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

import static com.alphasystem.morphologicalanalysis.graph.model.support.TerminalType.EMPTY;
import static com.alphasystem.morphologicalanalysis.graph.model.support.TerminalType.HIDDEN;
import static java.lang.String.format;

/**
 * @author sali
 */
@ContextConfiguration(classes = {MongoConfig.class, WordByWordConfig.class, GraphConfig.class,
        MorphologicalAnalysisSpringConfiguration.class})
public class DeleteData extends AbstractTestNGSpringContextTests {

    private MorphologicalAnalysisRepositoryUtil repositoryUtil;

    @Autowired
    public void setRepositoryUtil(MorphologicalAnalysisRepositoryUtil repositoryUtil) {
        this.repositoryUtil = repositoryUtil;
    }

    @Test
    @Parameters({"displayName"})
    public void deleteDependencyGraph(String displayName) {
        DependencyGraphRepository dependencyGraphRepository = repositoryUtil.getDependencyGraphRepository();

        DependencyGraph dependencyGraph = dependencyGraphRepository.findByDisplayName(displayName);
        if (dependencyGraph == null) {
            System.out.println(format("No DependencyGraph found with ID {%s}", displayName));
            return;
        }

        TerminalRepository terminalRepository = repositoryUtil.getTerminalRepository();
        List<Terminal> terminals = dependencyGraph.getTerminals();
        for (Terminal terminal : terminals) {
            if (terminal == null) {
                continue;
            }
            TerminalType terminalType = terminal.getTerminalType();
            if (terminalType.equals(EMPTY) || terminalType.equals(HIDDEN)) {
                continue;
            }
            terminalRepository.delete(terminal);
        }

        RelationshipRepository relationshipRepository = repositoryUtil.getRelationshipRepository();
        List<Relationship> relationships = dependencyGraph.getRelationships();
        for (Relationship relationship : relationships) {
            if(relationship == null){
                continue;
            }
            relationshipRepository.delete(relationship);
        }

        FragmentRepository fragmentRepository = repositoryUtil.getFragmentRepository();
        List<Fragment> fragments = dependencyGraph.getFragments();
        for (Fragment fragment : fragments) {
            if(fragment == null){
                continue;
            }
            fragmentRepository.delete(fragment);
        }

        dependencyGraphRepository.delete(dependencyGraph);
    }
}
