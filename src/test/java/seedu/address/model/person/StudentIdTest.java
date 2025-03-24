package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class StudentIdTest {

    @Test
    public void constructor_invalidStudentId_throwsIllegalArgumentException() {
        String invalidStudentId = "B1234567Z";
        assertThrows(IllegalArgumentException.class, () -> new StudentId(invalidStudentId));
    }

    @Test
    public void constructor_validStudentId_createsStudentId() {
        String validStudentId = "A1234567Z";
        StudentId studentId = new StudentId(validStudentId);
        assertTrue(studentId.value.equals(validStudentId));
    }

    @Test
    public void isValidStudentId() {
        // invalid student IDs
        assertFalse(StudentId.isValidStudentId("")); // empty string
        assertFalse(StudentId.isValidStudentId(" ")); // spaces only
        assertFalse(StudentId.isValidStudentId("A123456")); // less than 7 digits
        assertFalse(StudentId.isValidStudentId("A12345678")); // more than 7 digits
        assertFalse(StudentId.isValidStudentId("B1234567Z")); // does not start with 'A'
        assertFalse(StudentId.isValidStudentId("A1234567")); // missing last letter
        assertFalse(StudentId.isValidStudentId("A1234567ZZ")); // more than one letter at the end
        assertFalse(StudentId.isValidStudentId("A1234567Z ")); //trailing white space

        // valid student IDs
        assertTrue(StudentId.isValidStudentId("A1234567Z"));
        assertTrue(StudentId.isValidStudentId("A7654321X"));
    }

    @Test
    public void testToString() {
        String validStudentId = "A1234567Z";
        StudentId studentId = new StudentId(validStudentId);
        assertTrue(studentId.toString().equals(validStudentId));
    }

    @Test
    public void testEquals() {
        String studentId1 = "A1234567Z";
        String studentId2 = "A7654321X";
        StudentId id1 = new StudentId(studentId1);
        StudentId id2 = new StudentId(studentId2);
        StudentId id3 = new StudentId(studentId1);

        assertTrue(id1.equals(id1)); // same object
        assertTrue(id1.equals(id3)); // different objects, same value
        assertFalse(id1.equals(id2)); // different values
        assertFalse(id1.equals(null)); // null
        assertFalse(id1.equals("A1234567Z")); // different type
    }

    @Test
    public void testHashCode() {
        String studentId1 = "A1234567Z";
        String studentId2 = "A7654321X";
        StudentId id1 = new StudentId(studentId1);
        StudentId id2 = new StudentId(studentId2);
        StudentId id3 = new StudentId(studentId1);

        assertTrue(id1.hashCode() == id3.hashCode()); // same value
        assertFalse(id1.hashCode() == id2.hashCode()); // different values
    }
}
