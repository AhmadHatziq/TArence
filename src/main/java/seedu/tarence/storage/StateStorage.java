package seedu.tarence.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Stack;
import java.util.logging.Logger;

import seedu.tarence.commons.core.LogsCenter;
import seedu.tarence.commons.util.FileUtil;
import seedu.tarence.commons.util.JsonUtil;
import seedu.tarence.model.ReadOnlyApplication;

/**
 * A class to access the application states stored as json files on the hard disk.
 */
public class StateStorage {
    private static final Logger logger = LogsCenter.getLogger(JsonApplicationStorage.class);
    private static final String STATE_FILE_PREFIX = "state";
    private static final String STATE_FILE_SUFFIX = ".json";
    private String stateFolderDirectory;
    private Stack<Integer> stateStack;

    /**
     * Constructor will be initalised with the String directory and the first state will be the data the application is
     * loaded from. Ex: "data\states\"
     * @param stateFolderDirectory
     */
    public StateStorage(String stateFolderDirectory) {
        //System.out.println("State storage created with directory: " + stateFolderDirectory.toString());

        this.stateFolderDirectory = stateFolderDirectory;
        stateStack = new Stack<Integer>();
        stateStack.add(0);
    }

    public void saveApplicationState(ReadOnlyApplication application) throws IOException {
        requireNonNull(application);

        Path filePath = getNextFilePath();
        //System.out.println("Mext Fiepath is: " + filePath.toString());

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableApplication(application), filePath);

    }

    public Path getNextFilePath() {
        String filePathString = stateFolderDirectory + STATE_FILE_PREFIX + getNextStateIndex().toString() + STATE_FILE_SUFFIX;
        return Paths.get(filePathString);
    }

    public Integer getNextStateIndex() {
        return stateStack.peek() + 1;
    }

}
