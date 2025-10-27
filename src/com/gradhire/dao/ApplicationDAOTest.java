package com.gradhire.dao;

import com.gradhire.exception.DataAccessException;
import com.gradhire.model.Application;

/**
 * Test class for ApplicationDAO
 * Contains manual test methods to verify functionality
 * Run these tests individually to verify each feature
 * 
 * @author GradHire Team
 * @version 1.0
 */
public class ApplicationDAOTest {
    
    private ApplicationDAO dao = new ApplicationDAO();
    
    /**
     * Test 1: Validate createApplication with null application
     * Expected: Should throw IllegalArgumentException
     */
    public void testCreateApplicationWithNullApplication() {
        System.out.println("Test 1: createApplication with null application");
        try {
            dao.createApplication(null);
            System.out.println("❌ FAILED: Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ PASSED: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ FAILED: Wrong exception type: " + e.getClass().getName());
        }
    }
    
    /**
     * Test 2: Validate createApplication with null jobId
     * Expected: Should throw IllegalArgumentException
     */
    public void testCreateApplicationWithNullJobId() {
        System.out.println("\nTest 2: createApplication with null jobId");
        try {
            Application app = new Application();
            app.setStudentId(1);
            // jobId is null
            dao.createApplication(app);
            System.out.println("❌ FAILED: Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ PASSED: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ FAILED: Wrong exception type: " + e.getClass().getName());
        }
    }
    
    /**
     * Test 3: Validate createApplication with null studentId
     * Expected: Should throw IllegalArgumentException
     */
    public void testCreateApplicationWithNullStudentId() {
        System.out.println("\nTest 3: createApplication with null studentId");
        try {
            Application app = new Application();
            app.setJobId(1);
            // studentId is null
            dao.createApplication(app);
            System.out.println("❌ FAILED: Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ PASSED: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ FAILED: Wrong exception type: " + e.getClass().getName());
        }
    }
    
    /**
     * Test 4: Validate softDeleteApplication with null applicationId
     * Expected: Should throw IllegalArgumentException
     */
    public void testSoftDeleteWithNullId() {
        System.out.println("\nTest 4: softDeleteApplication with null ID");
        try {
            dao.softDeleteApplication(null);
            System.out.println("❌ FAILED: Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ PASSED: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ FAILED: Wrong exception type: " + e.getClass().getName());
        }
    }
    
    /**
     * Test 5: Validate deleteApplication with null applicationId
     * Expected: Should throw IllegalArgumentException
     */
    public void testDeleteWithNullId() {
        System.out.println("\nTest 5: deleteApplication with null ID");
        try {
            dao.deleteApplication(null);
            System.out.println("❌ FAILED: Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ PASSED: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ FAILED: Wrong exception type: " + e.getClass().getName());
        }
    }
    
    /**
     * Test 6: Validate restoreApplication with null applicationId
     * Expected: Should throw IllegalArgumentException
     */
    public void testRestoreWithNullId() {
        System.out.println("\nTest 6: restoreApplication with null ID");
        try {
            dao.restoreApplication(null);
            System.out.println("❌ FAILED: Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ PASSED: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ FAILED: Wrong exception type: " + e.getClass().getName());
        }
    }
    
    /**
     * Test 7: Validate hasApplied with null studentId
     * Expected: Should throw IllegalArgumentException
     */
    public void testHasAppliedWithNullStudentId() {
        System.out.println("\nTest 7: hasApplied with null studentId");
        try {
            dao.hasApplied(null, 1);
            System.out.println("❌ FAILED: Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ PASSED: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ FAILED: Wrong exception type: " + e.getClass().getName());
        }
    }
    
    /**
     * Test 8: Validate hasApplied with null jobId
     * Expected: Should throw IllegalArgumentException
     */
    public void testHasAppliedWithNullJobId() {
        System.out.println("\nTest 8: hasApplied with null jobId");
        try {
            dao.hasApplied(1, null);
            System.out.println("❌ FAILED: Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ PASSED: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ FAILED: Wrong exception type: " + e.getClass().getName());
        }
    }
    
    /**
     * Test 9: Validate hasApplied with both null parameters
     * Expected: Should throw IllegalArgumentException
     */
    public void testHasAppliedWithBothNull() {
        System.out.println("\nTest 9: hasApplied with both null parameters");
        try {
            dao.hasApplied(null, null);
            System.out.println("❌ FAILED: Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ PASSED: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ FAILED: Wrong exception type: " + e.getClass().getName());
        }
    }
    
    /**
     * Test 10: Validate findById with null applicationId
     * Expected: Should throw IllegalArgumentException
     */
    public void testFindByIdWithNull() {
        System.out.println("\nTest 10: findById with null ID");
        try {
            dao.findById(null);
            System.out.println("❌ FAILED: Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ PASSED: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ FAILED: Wrong exception type: " + e.getClass().getName());
        }
    }
    
    /**
     * Test 11: Test Application model deletedAt field
     */
    public void testApplicationModelDeletedAt() {
        System.out.println("\nTest 11: Application model deletedAt field");
        try {
            Application app = new Application();
            
            // Test isDeleted when deletedAt is null
            if (app.isDeleted()) {
                System.out.println("❌ FAILED: isDeleted() should return false when deletedAt is null");
                return;
            }
            
            // Test isDeleted when deletedAt is set
            app.setDeletedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            if (!app.isDeleted()) {
                System.out.println("❌ FAILED: isDeleted() should return true when deletedAt is set");
                return;
            }
            
            // Test canWithdraw when deleted
            if (app.canWithdraw()) {
                System.out.println("❌ FAILED: canWithdraw() should return false when deleted");
                return;
            }
            
            // Test canWithdraw when not deleted and pending
            app.setDeletedAt(null);
            if (!app.canWithdraw()) {
                System.out.println("❌ FAILED: canWithdraw() should return true when not deleted and pending");
                return;
            }
            
            System.out.println("✅ PASSED: All Application model tests passed");
        } catch (Exception e) {
            System.out.println("❌ FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test 12: Test DataAccessException creation
     */
    public void testDataAccessException() {
        System.out.println("\nTest 12: DataAccessException creation");
        try {
            // Test with message only
            DataAccessException ex1 = new DataAccessException("Test message");
            if (!"Test message".equals(ex1.getMessage())) {
                System.out.println("❌ FAILED: Message not set correctly");
                return;
            }
            
            // Test with message and cause
            Exception cause = new Exception("Root cause");
            DataAccessException ex2 = new DataAccessException("Test message", cause);
            if (ex2.getCause() != cause) {
                System.out.println("❌ FAILED: Cause not set correctly");
                return;
            }
            
            // Test with cause only
            DataAccessException ex3 = new DataAccessException(cause);
            if (ex3.getCause() != cause) {
                System.out.println("❌ FAILED: Cause not set correctly in constructor");
                return;
            }
            
            System.out.println("✅ PASSED: DataAccessException works correctly");
        } catch (Exception e) {
            System.out.println("❌ FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Main method to run all tests
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("ApplicationDAO Test Suite");
        System.out.println("========================================");
        
        ApplicationDAOTest tester = new ApplicationDAOTest();
        
        // Run validation tests (these don't require database connection)
        tester.testCreateApplicationWithNullApplication();
        tester.testCreateApplicationWithNullJobId();
        tester.testCreateApplicationWithNullStudentId();
        tester.testSoftDeleteWithNullId();
        tester.testDeleteWithNullId();
        tester.testRestoreWithNullId();
        tester.testHasAppliedWithNullStudentId();
        tester.testHasAppliedWithNullJobId();
        tester.testHasAppliedWithBothNull();
        tester.testFindByIdWithNull();
        tester.testApplicationModelDeletedAt();
        tester.testDataAccessException();
        
        System.out.println("\n========================================");
        System.out.println("Test Suite Complete!");
        System.out.println("========================================");
        System.out.println("\nNote: Database integration tests require:");
        System.out.println("1. Database connection configured");
        System.out.println("2. Migration script executed");
        System.out.println("3. Test data in database");
    }
}


