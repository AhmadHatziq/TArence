package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Remark;

import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

public class RemarkCommandParser implements Parser<RemarkCommand>  {

    public RemarkCommand parse(String args) throws ParseException {
        requireNonNull(args);

        // argMultiMap is a mapping of prefixes to a list of strings.
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_REMARK);

        // Obtains the index
        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    RemarkCommand.MESSAGE_USAGE), ive);
        }

        // Obtains the remark string
        String remarkString = argMultimap.getValue(PREFIX_REMARK).orElse("");
        Remark remark = new Remark(remarkString);
        return new RemarkCommand(index, remark);
    }

}
