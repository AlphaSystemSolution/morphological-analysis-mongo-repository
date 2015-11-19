package com.alphasystem.morphologicalanalysis.repository.test;

import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.graph.repository.DependencyGraphRepository;
import com.alphasystem.morphologicalanalysis.spring.support.MongoConfig;
import com.alphasystem.morphologicalanalysis.spring.support.MorphologicalAnalysisSpringConfiguration;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

import static java.lang.String.format;

/**
 * @author sali
 */
@ContextConfiguration(classes = {MongoConfig.class, MorphologicalAnalysisSpringConfiguration.class})
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
            System.out.println(format("No DependencyGraph found with Display Name {%s}", displayName));
            return;
        }

        List<GraphNode> nodes = dependencyGraph.getNodes();
        nodes.forEach(graphNode -> repositoryUtil.delete(graphNode));

        dependencyGraphRepository.delete(dependencyGraph);
    }

    @Test
    public void updateData() {
    }
}
