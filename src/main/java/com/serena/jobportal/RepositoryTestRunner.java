//running file for rep tests
/*package com.serena.jobportal;

import com.serena.jobportal.util.RepositoryTester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2) // Make sure this runs after your data loader
public class RepositoryTestRunner implements CommandLineRunner {

    @Autowired
    private RepositoryTester repositoryTester;

    @Override
    public void run(String... args) {
        System.out.println("\n====== STARTING REPOSITORY TESTS ======\n");

        // Run all repository tests
        repositoryTester.testAllRepositories();

        System.out.println("\n====== REPOSITORY TESTS COMPLETED ======\n");
    }
}*/