package seedu.tarence.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.tarence.commons.core.LogsCenter;
import seedu.tarence.commons.exceptions.DataConversionException;
import seedu.tarence.model.ReadOnlyApplication;
import seedu.tarence.model.ReadOnlyUserPrefs;
import seedu.tarence.model.UserPrefs;
import seedu.tarence.model.util.SampleDataUtil;

/**
 * Manages storage of Application data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private ApplicationStorage applicationStorage;
    private UserPrefsStorage userPrefsStorage;
    private StateStorage stateStorage;


    public StorageManager(ApplicationStorage applicationStorage, UserPrefsStorage userPrefsStorage, StateStorage stateStorage) {
        super();
        this.applicationStorage = applicationStorage;
        this.userPrefsStorage = userPrefsStorage;
        this.stateStorage = stateStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ Application methods ==============================

    @Override
    public Path getApplicationFilePath() {
        return applicationStorage.getApplicationFilePath();
    }

    @Override
    public Optional<ReadOnlyApplication> readApplication() throws DataConversionException, IOException {
        return readApplication(applicationStorage.getApplicationFilePath());
    }

    @Override
    public Optional<ReadOnlyApplication> readApplication(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        Optional<ReadOnlyApplication> applicationOptional = applicationStorage.readApplication(filePath);

        // Saves the first state (loaded from previous session)
        ReadOnlyApplication initialData = applicationOptional.orElseGet(SampleDataUtil::getSampleApplication);
        stateStorage.saveApplicationState(initialData);

        return applicationOptional;
    }

    @Override
    public void saveApplication(ReadOnlyApplication application) throws IOException {
        saveApplication(application, applicationStorage.getApplicationFilePath());
    }

    @Override
    public void saveApplication(ReadOnlyApplication application, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        applicationStorage.saveApplication(application, filePath);

        // Save the states whenever a command changes the model.
        stateStorage.saveApplicationState(application);
    }

}
