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
    public static void main(String[] args) {
        // Define Courses
        Course math101 = new Course("MATH101", new ArrayList<>());                // No prerequisites
        Course math201 = new Course("MATH201", Arrays.asList("MATH101"));         // Requires MATH101
        Course chem201 = new Course("CHEM201", Arrays.asList("CHEM101"));         // Requires CHEM101
        Course advancedChem = new Course("CHEM301", Arrays.asList("PHYS101"));     // Requires PHYS101

        // Initialize Student
        Student student = new Student("S1001");

        // Populate Student Academic Records
        student.addRecord("MATH101", true, 2.7);   // Completed with passing grade (Satisfied)
        student.addRecord("CHEM101", false, 0.0);  // In-progress semester (Not completed)
        // Note: PHYS101 is not added at all (Missing)

        PrerequisiteChecker checker = new PrerequisiteChecker();

        System.out.println("=== Prerequisite Checker Test Suite ===");
        
        // 1. Course with no prerequisites
        boolean test1 = checker.canRegister(student, math101);
        System.out.println("Test 1 (No Prerequisites - MATH101): " + test1 + " (Expected: true)");

        // 2. Satisfied prerequisite
        boolean test2 = checker.canRegister(student, math201);
        System.out.println("Test 2 (Satisfied Prerequisite - MATH201): " + test2 + " (Expected: true)");

        // 3. Prerequisite taken only in an in-progress semester
        boolean test3 = checker.canRegister(student, chem201);
        System.out.println("Test 3 (In-Progress Prerequisite - CHEM201): " + test3 + " (Expected: false)");

        // 4. Missing prerequisite
        boolean test4 = checker.canRegister(student, advancedChem);
        System.out.println("Test 4 (Missing Prerequisite - CHEM301): " + test4 + " (Expected: false)");
    }
}