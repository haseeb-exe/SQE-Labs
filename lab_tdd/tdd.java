import java.util.*;

// Represents a course and its prerequisites
class Course {
    private String code;
    private List<String> prerequisites;

    public Course(String code, List<String> prerequisites) {
        this.code = code;
        this.prerequisites = prerequisites != null ? prerequisites : new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public List<String> getPrerequisites() {
        return prerequisites;
    }
}

// Represents a student's record for a specific course
class CourseRecord {
    private String courseCode;
    private boolean isCompleted;
    private double gradePoints;

    public CourseRecord(String courseCode, boolean isCompleted, double gradePoints) {
        this.courseCode = courseCode;
        this.isCompleted = isCompleted;
        this.gradePoints = gradePoints;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public double getGradePoints() {
        return gradePoints;
    }
}

// Student profile holding academic records
class Student {
    private String studentId;
    private Map<String, CourseRecord> records;

    public Student(String studentId) {
        this.studentId = studentId;
        this.records = new HashMap<>();
    }

    public void addRecord(String courseCode, boolean isCompleted, double gradePoints) {
        records.put(courseCode, new CourseRecord(courseCode, isCompleted, gradePoints));
    }

    public CourseRecord getRecord(String courseCode) {
        return records.get(courseCode);
    }
}

// Validates course prerequisites for registration
class PrerequisiteChecker {
    private static final double MIN_PASSING_GRADE = 1.0;

    public boolean canRegister(Student student, Course course) {
        if (student == null || course == null) {
            return false;
        }

        List<String> prereqs = course.getPrerequisites();

        // Course with no prerequisites can always be registered.
        if (prereqs == null || prereqs.isEmpty()) {
            return true;
        }

        for (String prereqCode : prereqs) {
            CourseRecord record = student.getRecord(prereqCode);

            // Missing prerequisite means the student has not completed it yet.
            if (record == null) {
                return false;
            }

            // A course taken only in an in-progress semester cannot be used as a prerequisite.
            if (!record.isCompleted()) {
                return false;
            }

            // Check if grade meets the passing threshold (at least 1.0).
            if (record.getGradePoints() < MIN_PASSING_GRADE) {
                return false;
            }
        }

        return true;
    }
}

// Main execution class to run test cases
public class tdd {
    private static void runTest(String testName, String courseCode, boolean actual, boolean expected) {
        if (actual != expected) {
            throw new AssertionError(testName + " failed for " + courseCode + ": expected " + expected + " but got " + actual);
        }
        System.out.println(testName + " (" + courseCode + "): " + actual + " (Expected: " + expected + ")");
    }

    public static void main(String[] args) {
        System.out.println("=== Prerequisite Checker Test Suite ===");

        Course math101 = new Course("MATH101", new ArrayList<>());
        Course math201 = new Course("MATH201", Arrays.asList("MATH101"));
        Course chem201 = new Course("CHEM201", Arrays.asList("CHEM101"));
        Course phy201 = new Course("PHYS201", Arrays.asList("PHYS101"));
        Course advancedChem = new Course("CHEM301", Arrays.asList("PHYS101"));

        Student student = new Student("S1001");
        student.addRecord("MATH101", true, 2.7);
        student.addRecord("CHEM101", false, 0.0);
        student.addRecord("PHYS101", true, 0.5);

        PrerequisiteChecker checker = new PrerequisiteChecker();

        // Test 1: Course with no prerequisites
        runTest("Test 1 (No Prerequisites)", math101.getCode(), checker.canRegister(student, math101), true);

        // Test 2: Satisfied prerequisite
        runTest("Test 2 (Satisfied Prerequisite)", math201.getCode(), checker.canRegister(student, math201), true);

        // Test 3: Prerequisite taken only in an in-progress semester
        runTest("Test 3 (In-Progress Prerequisite)", chem201.getCode(), checker.canRegister(student, chem201), false);

        // Test 4: Missing prerequisite
        runTest("Test 4 (Missing Prerequisite)", advancedChem.getCode(), checker.canRegister(student, advancedChem), false);

        // Test 5: Prerequisite with failing grade
        runTest("Test 5 (Failing Prerequisite Grade)", phy201.getCode(), checker.canRegister(student, phy201), false);
    }
}