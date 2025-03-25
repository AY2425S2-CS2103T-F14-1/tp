package seedu.address.logic.parser;

import seedu.address.logic.commands.SaveCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SaveCommand object.
 */
public class SaveCommandParser implements Parser<SaveCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SaveCommand
     * and returns a SaveCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public SaveCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException("Save command requires a file path as an argument.");
        }
        return new SaveCommand(trimmedArgs);
    }
}
