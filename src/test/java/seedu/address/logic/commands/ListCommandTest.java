package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.ui.DisplayPreferences;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        // Default behavior: all fields are displayed
        ListCommand listCommand = new ListCommand(true, true, true, true, true, true, true);
        assertCommandSuccess(listCommand, model, ListCommand.MESSAGE_SUCCESS, expectedModel);

        // Verify DisplayPreferences are updated correctly
        assert DisplayPreferences.isShowName();
        assert DisplayPreferences.isShowPhone();
        assert DisplayPreferences.isShowEmail();
        assert DisplayPreferences.isShowAddress();
        assert DisplayPreferences.isShowStudentId();
        assert DisplayPreferences.isShowTags();
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        // Filter the list to show only the first person
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // Execute the command to reset the filter and show all persons
        ListCommand listCommand = new ListCommand(true, true, true, true, true, true, true);
        assertCommandSuccess(listCommand, model, ListCommand.MESSAGE_SUCCESS, expectedModel);

        // Verify DisplayPreferences are updated correctly
        assert DisplayPreferences.isShowName();
        assert DisplayPreferences.isShowPhone();
        assert DisplayPreferences.isShowEmail();
        assert DisplayPreferences.isShowAddress();
        assert DisplayPreferences.isShowStudentId();
        assert DisplayPreferences.isShowTags();
    }

    @Test
    public void execute_listWithSpecificFields_showsFilteredFields() {
        // Test with specific fields (e.g., name and phone only)
        ListCommand listCommand = new ListCommand(true, true, false, false, false, false, false);
        assertCommandSuccess(listCommand, model, ListCommand.MESSAGE_SUCCESS, expectedModel);

        // Verify DisplayPreferences are updated correctly
        assert DisplayPreferences.isShowName();
        assert DisplayPreferences.isShowPhone();
        assert !DisplayPreferences.isShowEmail();
        assert !DisplayPreferences.isShowAddress();
        assert !DisplayPreferences.isShowStudentId();
        assert !DisplayPreferences.isShowTags();
    }

    @Test
    public void execute_emptyAddressBook_showsEmptyList() {
        // Set up an empty model
        model = new ModelManager();
        expectedModel = new ModelManager();

        // Execute the command
        ListCommand listCommand = new ListCommand(true, true, true, true, true, true, true);
        assertCommandSuccess(listCommand, model, ListCommand.MESSAGE_SUCCESS, expectedModel);

        // Verify DisplayPreferences are updated correctly
        assert DisplayPreferences.isShowName();
        assert DisplayPreferences.isShowPhone();
        assert DisplayPreferences.isShowEmail();
        assert DisplayPreferences.isShowAddress();
        assert DisplayPreferences.isShowStudentId();
        assert DisplayPreferences.isShowTags();
    }

    @Test
    public void execute_updatesFilteredPersonList() {
        // Test that the filtered person list is updated
        ListCommand listCommand = new ListCommand(true, true, true, true, true, true, true);
        assertCommandSuccess(listCommand, model, ListCommand.MESSAGE_SUCCESS, expectedModel);

        // Verify that the filtered list is updated
        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());
    }

    @Test
    public void equals() {
        // Test equality of ListCommand objects
        ListCommand listCommand1 = new ListCommand(true, true, true, true, true, true, true);
        ListCommand listCommand2 = new ListCommand(true, true, true, true, true, true, true);
        ListCommand listCommand3 = new ListCommand(false, true, true, true, true, true, true);

        // Same values -> returns true
        assertEquals(listCommand1, listCommand2);

        // Different values -> returns false
        assertNotEquals(listCommand1, listCommand3);

        // Same object -> returns true
        assertEquals(listCommand1, listCommand1);

        // Null -> returns false
        assertNotEquals(listCommand1, null);

        // Different types -> returns false
        assertNotEquals(listCommand1, 5);
    }

    @Test
    public void hashCode_test() {
        // Test hashCode implementation
        ListCommand listCommand1 = new ListCommand(true, true, true, true, true, true, true);
        ListCommand listCommand2 = new ListCommand(true, true, true, true, true, true, true);
        ListCommand listCommand3 = new ListCommand(false, true, true, true, true, true, true);

        // Same values -> same hashCode
        assertEquals(listCommand1.hashCode(), listCommand2.hashCode());

        // Different values -> different hashCode
        assertNotEquals(listCommand1.hashCode(), listCommand3.hashCode());
    }
}
