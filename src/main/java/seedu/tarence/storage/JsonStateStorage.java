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

import org.apache.commons.io.FileUtils;

/**
 * A class to access the application states stored as json files on the hard disk.
 */
public class JsonStateStorage implements ApplicationStateStorage {
    private static final Logger logger = LogsCenter.getLogger(JsonApplicationStorage.class);
    private static final String STATE_FILE_PREFIX = "state";
    private static final String STATE_FILE_SUFFIX = ".json";
    private String stateFolderDirectory;
    private Stack<Integer> stateStack;

    /**
     * Constructor will be initialised with the String directory and the first state will be the data the application is
     * loaded from. Ex: "data\states\"
     * @param stateFolderDirectory String of the directory location for storing states.
     */
    public JsonStateStorage(String stateFolderDirectory) {
        //System.out.println("State storage created with directory: " + stateFolderDirectory.toString());

        this.stateFolderDirectory = stateFolderDirectory;
        stateStack = new Stack<Integer>();
        stateStack.add(0);

        try {
            clearStateFolder();
            logger.fine(stateFolderDirectory + " successfully cleared.");
        } catch (IOException e) {
            logger.info("Error in clearing state folder. Possible error with specified directory: " + stateFolderDirectory);
        }
    }

    //TODO: do a final round of cleaning during Tarence shutting down.
    public void clearStateFolder() throws IOException {
        Path filePath = Paths.get(stateFolderDirectory);
        FileUtils.deleteDirectory(filePath.toFile());
    }

    public void saveApplicationState(ReadOnlyApplication application) throws IOException {
        requireNonNull(application);

        // Get the next filePath eg "data\states\state5.json".
        Path filePath = getNextFilePath();
        //System.out.println("Mext Fiepath is: " + filePath.toString());

        // Save the application state
        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableApplication(application), filePath);

        // Increment the stack counter
        stateStack.push(stateStack.peek() + 1);

    }

    public Path getNextFilePath() {
        String filePathString = stateFolderDirectory + STATE_FILE_PREFIX + getNextStateIndex().toString() + STATE_FILE_SUFFIX;
        return Paths.get(filePathString);
    }

    public Integer getNextStateIndex() {
        return stateStack.peek() + 1;
    }

    @Override
    public void helloFromState() {
        System.out.println("Hello from State");
    }

}
