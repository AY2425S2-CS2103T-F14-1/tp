package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SaveCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Unit tests for the SaveCommandParser class.
 */
public class SaveCommandParserTest {

    /**
     * Tests that the parser correctly parses valid arguments and returns a SaveCommand
     * with the expected file name.
     *
     * @throws Exception if parsing fails unexpectedly.
     */
    @Test
    public void parse_validArgs_returnsSaveCommand() throws Exception {
        String fileName = "test";
        SaveCommand expectedCommand = new SaveCommand(fileName);
        SaveCommandParser parser = new SaveCommandParser();
        SaveCommand actualCommand = parser.parse(fileName);
        assertEquals(expectedCommand, actualCommand);
    }

    /**
     * Tests that the parser throws a ParseException when given empty arguments
     * (e.g., a string with only whitespace).
     */
    @Test
    public void parse_emptyArgs_throwsParseException() {
        SaveCommandParser parser = new SaveCommandParser();
        assertThrows(ParseException.class, () -> parser.parse("   "));
    }

    /**
     * Tests that the parser throws a ParseException when given null as input.
     */
    @Test
    public void parse_nullArgs_throwsParseException() {
        SaveCommandParser parser = new SaveCommandParser();
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    /**
     * Tests that the parser throws a ParseException when given invalid arguments
     * (e.g., an empty string).
     */
    @Test
    public void parse_invalidArgs_throwsParseException() {
        SaveCommandParser parser = new SaveCommandParser();
        assertThrows(ParseException.class, () -> parser.parse(""));
    }
}
