package de.fhbingen.wbs.dbaccess.repositories;

import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.dbaccess.data.TestCase;
import de.fhbingen.wbs.dbaccess.data.TestExecution;
import de.fhbingen.wbs.dbaccess.repositories.core.ParentToElementMappedCache;

import java.util.List;

public class TestCaseExecutionRepository {
    private static class TestCaseExecutionParentToElementMappedCache extends ParentToElementMappedCache<TestExecution> {
        @Override
        protected List<TestExecution> loadAllElements() {
            return DBModelManager.getTestExecutionModel().get;
        }

        @Override
        protected Integer getParentId(TestCase element) {
            return element.getWorkpackageID();
        }

        @Override
        protected Integer getId(TestCase element) {
            return element.getId();
        }
    }

    private static TestCaseExecutionParentToElementMappedCache testCaseExecutionMap;

    private TestCaseExecutionRepository() {}

    public static List<TestCase> getAllTestCaseExecutions(TestCase testCase) {
        return getTestCaseExecutionMap().getAllByParentElement(testCase.getId());
    }

    public static TestCaseExecutionParentToElementMappedCache getTestCaseExecutionMap() {
        if (testCaseExecutionMap == null) {
            testCaseExecutionMap = new TestCaseExecutionParentToElementMappedCache();
        }

        return testCaseExecutionMap;
    }
}
