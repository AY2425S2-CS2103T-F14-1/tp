package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddTagCommand;
import seedu.address.model.tag.Tag;

public class AddTagCommandParserTest {
    private AddTagCommandParser parser = new AddTagCommandParser();

    @Test
    public void parse_validArgs_returnsAddTagCommand() throws Exception {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag(VALID_TAG_FRIEND));

        assertParseSuccess(parser, "1" + TAG_DESC_FRIEND,
                new AddTagCommand(INDEX_FIRST_PERSON, tags));
    }

    @Test
    public void parse_multipleTags_returnsAddTagCommand() throws Exception {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag(VALID_TAG_FRIEND));
        tags.add(new Tag(VALID_TAG_HUSBAND));

        assertParseSuccess(parser, "1" + TAG_DESC_FRIEND + TAG_DESC_HUSBAND,
                new AddTagCommand(INDEX_FIRST_PERSON, tags));
    }

    @Test
    public void parse_missingIndex_failure() {
        assertParseFailure(parser, TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingTags_failure() {
        assertParseFailure(parser, "1", AddTagCommand.MESSAGE_NO_TAGS_PROVIDED);
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertParseFailure(parser, "a" + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidTag_failure() {
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS);
    }
}
