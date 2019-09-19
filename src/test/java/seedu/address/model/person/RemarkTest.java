package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class RemarkTest {

    @Test
    public void equals() {
        Remark remark = new Remark("Hello");

        // Same object -> returns true
        assertTrue(remark.equals(remark));

        // Same values -> return true
        Remark remarkCopy = new Remark(remark.value);
        assertTrue(remark.equals(remarkCopy));

        // Different types -> return false
        assertFalse(remark.equals(1));

        // Null -> returns false
        assertFalse(remark.equals(null));

        // Different Remark -> returns false
        Remark differentRemark = new Remark("Not Hello");
        assertFalse(remark.equals(differentRemark));
    }

}
