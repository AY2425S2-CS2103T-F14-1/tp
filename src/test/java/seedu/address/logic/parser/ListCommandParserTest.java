package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIAL;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListCommand;

public class ListCommandParserTest {

    private final ListCommandParser parser = new ListCommandParser();

    @Test
    public void parse_noArguments_success() {
        // No arguments provided, default to showing all fields
        assertParseSuccess(parser, "",
                new ListCommand(true, true, true, true, true, true, true));
    }

    @Test
    public void parse_allValidFields_success() {
        // All valid fields provided
        assertParseSuccess(parser, PREFIX_NAME + " " + PREFIX_PHONE + " " + PREFIX_EMAIL + " "
                + PREFIX_ADDRESS + " " + PREFIX_STUDENT_ID + " " + PREFIX_TAG + " " + PREFIX_TUTORIAL,
                new ListCommand(true, true, true, true, true, true, true));
    }

    @Test
    public void parse_someValidFields_success() {
        // Only some valid fields provided
        assertParseSuccess(parser, PREFIX_NAME + " " + PREFIX_EMAIL + " " + PREFIX_TAG,
                new ListCommand(true, false, true, false, false, true, false));
    }

    @Test
    public void parse_invalidField_failure() {
        // Invalid field provided
        assertParseFailure(parser, "invalidField",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_mixedValidAndInvalidFields_failure() {
        // Mixed valid and invalid fields
        assertParseFailure(parser, PREFIX_NAME + " invalidField",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicateFields_success() {
        // Duplicate valid fields should still succeed
        assertParseSuccess(parser, PREFIX_NAME + " " + PREFIX_NAME + " " + PREFIX_EMAIL + " " + PREFIX_EMAIL,
                new ListCommand(true, false, true, false, false, false, false));
    }

    @Test
    public void parse_whitespaceOnly_success() {
        // Input with only whitespace should default to showing all fields
        assertParseSuccess(parser, "   ",
                new ListCommand(true, true, true, true, true, true, true));
    }
}
