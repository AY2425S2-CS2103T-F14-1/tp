package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for AddTagCommand.
 */
public class AddTagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addTagUnfilteredList_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("newTag"));

        AddTagCommand addTagCommand = new AddTagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        // Create expected person with added tags
        Set<Tag> updatedTags = new HashSet<>(firstPerson.getTags());
        updatedTags.addAll(tagsToAdd);
        Person personWithAddedTags = new PersonBuilder(firstPerson).withTags(getTagNames(updatedTags)).build();

        String expectedMessage = String.format(AddTagCommand.MESSAGE_ADD_TAG_SUCCESS,
                Messages.format(personWithAddedTags));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, personWithAddedTags);

        assertCommandSuccess(addTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addMultipleTagsUnfilteredList_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("tag1"));
        tagsToAdd.add(new Tag("tag2"));

        AddTagCommand addTagCommand = new AddTagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        // Create expected person with added tags
        Set<Tag> updatedTags = new HashSet<>(firstPerson.getTags());
        updatedTags.addAll(tagsToAdd);
        Person personWithAddedTags = new PersonBuilder(firstPerson).withTags(getTagNames(updatedTags)).build();

        String expectedMessage = String.format(AddTagCommand.MESSAGE_ADD_TAG_SUCCESS,
                Messages.format(personWithAddedTags));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, personWithAddedTags);

        assertCommandSuccess(addTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addDuplicateTag_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // Assuming firstPerson already has some tags
        Set<Tag> existingTags = firstPerson.getTags();
        Set<Tag> tagsToAdd = new HashSet<>(existingTags); // Add the same tags again

        AddTagCommand addTagCommand = new AddTagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        // Since tags are a Set, duplicates should not be added
        Person personWithAddedTags = new PersonBuilder(firstPerson).withTags(getTagNames(existingTags)).build();

        String expectedMessage = String.format(AddTagCommand.MESSAGE_ADD_TAG_SUCCESS,
                Messages.format(personWithAddedTags));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, personWithAddedTags);

        assertCommandSuccess(addTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("filteredTag"));

        AddTagCommand addTagCommand = new AddTagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        Set<Tag> updatedTags = new HashSet<>(firstPerson.getTags());
        updatedTags.addAll(tagsToAdd);
        Person personWithAddedTags = new PersonBuilder(firstPerson).withTags(getTagNames(updatedTags)).build();

        String expectedMessage = String.format(AddTagCommand.MESSAGE_ADD_TAG_SUCCESS,
                Messages.format(personWithAddedTags));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, personWithAddedTags);

        assertCommandSuccess(addTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("tag"));

        AddTagCommand addTagCommand = new AddTagCommand(outOfBoundIndex, tagsToAdd);

        assertCommandFailure(addTagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // Ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("tag"));

        AddTagCommand addTagCommand = new AddTagCommand(outOfBoundIndex, tagsToAdd);

        assertCommandFailure(addTagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() throws Exception {
        Set<Tag> tags1 = new HashSet<>();
        tags1.add(new Tag("tag1"));
        Set<Tag> tags2 = new HashSet<>();
        tags2.add(new Tag("tag2"));

        AddTagCommand addTagFirstCommand = new AddTagCommand(INDEX_FIRST_PERSON, tags1);
        AddTagCommand addTagSecondCommand = new AddTagCommand(INDEX_SECOND_PERSON, tags1);
        AddTagCommand addTagDifferentTagsCommand = new AddTagCommand(INDEX_FIRST_PERSON, tags2);

        // same object -> returns true
        assertTrue(addTagFirstCommand.equals(addTagFirstCommand));

        // same values -> returns true
        AddTagCommand addTagFirstCommandCopy = new AddTagCommand(INDEX_FIRST_PERSON, tags1);
        assertTrue(addTagFirstCommand.equals(addTagFirstCommandCopy));

        // different types -> returns false
        assertFalse(addTagFirstCommand.equals(1));

        // null -> returns false
        assertFalse(addTagFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(addTagFirstCommand.equals(addTagSecondCommand));

        // different tags -> returns false
        assertFalse(addTagFirstCommand.equals(addTagDifferentTagsCommand));
    }

    @Test
    public void toStringMethod() throws Exception {
        Index targetIndex = Index.fromOneBased(1);
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("tag"));
        AddTagCommand addTagCommand = new AddTagCommand(targetIndex, tags);
        String expected = AddTagCommand.class.getCanonicalName()
                + "{index=" + targetIndex + ", tagsToAdd=" + tags + "}";
        assertEquals(expected, addTagCommand.toString());
    }

    /**
     * Helper method to convert a Set of Tags to an array of tag name Strings.
     */
    private String[] getTagNames(Set<Tag> tags) {
        return tags.stream()
                .map(tag -> tag.tagName)
                .toArray(String[]::new);
    }
}
