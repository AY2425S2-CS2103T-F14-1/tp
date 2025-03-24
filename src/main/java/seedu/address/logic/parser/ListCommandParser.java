package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIAL;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ListCommand object.
 */
public class ListCommandParser implements Parser<ListCommand> {

    // Valid fields for the list command
    private static final Set<String> VALID_FIELDS = new HashSet<>(Arrays.asList(
            PREFIX_NAME.getPrefix(), PREFIX_ADDRESS.getPrefix(),
            PREFIX_EMAIL.getPrefix(), PREFIX_PHONE.getPrefix(),
            PREFIX_STUDENT_ID.getPrefix(), PREFIX_TAG.getPrefix(),
            PREFIX_TUTORIAL.getPrefix()
    ));

    /**
     * Parses the given {@code String} of arguments in the context of the ListCommand
     * and returns a ListCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public ListCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        // Default to showing everything if no arguments are provided
        boolean showName = true;
        boolean showPhone = true;
        boolean showEmail = true;
        boolean showAddress = true;
        boolean showStudentId = true;
        boolean showTags = true;
        boolean showTutorial = true;

        if (!trimmedArgs.isEmpty()) {
            // Split arguments into tokens
            String[] tokens = trimmedArgs.split("\\s+");

            // Validate each token
            for (String token : tokens) {
                if (!VALID_FIELDS.contains(token)) {
                    throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
                }
            }

            // Parse arguments to determine which fields to show
            showName = Arrays.asList(tokens).contains(PREFIX_NAME.getPrefix());
            showPhone = Arrays.asList(tokens).contains(PREFIX_PHONE.getPrefix());
            showEmail = Arrays.asList(tokens).contains(PREFIX_EMAIL.getPrefix());
            showAddress = Arrays.asList(tokens).contains(PREFIX_ADDRESS.getPrefix());
            showStudentId = Arrays.asList(tokens).contains(PREFIX_STUDENT_ID.getPrefix());
            showTags = Arrays.asList(tokens).contains(PREFIX_TAG.getPrefix());
            showTutorial = Arrays.asList(tokens).contains(PREFIX_TUTORIAL.getPrefix());
        }


        return new ListCommand(showName, showPhone, showEmail, showAddress, showStudentId, showTags, showTutorial);
    }
}
