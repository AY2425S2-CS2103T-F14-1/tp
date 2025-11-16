package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        // Clear tutorials explicitly so expected Person matches parsed Person.
        Person person = new PersonBuilder().withStudentId("A1234567X").withTutorials().withTelegram("example").build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_addtag() throws Exception {
        assertTrue(parser.parseCommand("addtag 1 t/friend") instanceof seedu.address.logic.commands.AddTagCommand);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        String studentId = "A1234567X";
        DeleteCommand expectedDeleteCommandById = new DeleteCommand("s/", studentId);
        DeleteCommand parsedDeleteCommandById = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " s/ " + studentId);
        assertEquals(expectedDeleteCommandById, parsedDeleteCommandById);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().withStudentId("A1234567X").withTelegram("example").build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        // Force expected descriptor's tags to be null (since no tag prefix is provided in the command)
        descriptor.setTags(null);
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " "
                + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        System.out.println(new EditCommand(INDEX_FIRST_PERSON, descriptor));
        System.out.println(command);
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> nameKeywords = Arrays.asList("bar", "baz");
        FindCommand expectedFindCommand =
                new FindCommand(new PersonContainsKeywordsPredicate(PREFIX_NAME.getPrefix(), nameKeywords, false));

        String input = FindCommand.COMMAND_WORD + " " + nameKeywords.stream()
                .map(keyword -> PREFIX_NAME.getPrefix() + keyword)
                .collect(Collectors.joining(" "));

        assertEquals(expectedFindCommand, parser.parseCommand(input));
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        ListCommand command = (ListCommand) parser.parseCommand(ListCommand.COMMAND_WORD);
        assertEquals(new ListCommand(true, true, true, true, true, true, true, true), command);

        command = (ListCommand) parser.parseCommand(ListCommand.COMMAND_WORD + " n/ p/");
        assertEquals(new ListCommand(true, true, false, false, false, false, false, false), command);

        command = (ListCommand) parser.parseCommand(ListCommand.COMMAND_WORD + " e/ a/ t/");
        assertEquals(new ListCommand(false, false, true, true, false, true, false, false), command);

        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(ListCommand.COMMAND_WORD + " invalidField"));
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
