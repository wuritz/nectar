package hu.bluestoplight.systems;

import hu.bluestoplight.SednaClient;
import hu.bluestoplight.utils.files.StreamUtils;
import hu.bluestoplight.utils.misc.interfaces.ISerializable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.crash.CrashException;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class System<T> implements ISerializable<T> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss", Locale.ROOT);

    private final String name;
    private File file;

    public System(String name) {
        this.name = name;

        if (name != null) {
            this.file = new File(SednaClient.FOLDER, name + ".nbt");
        }
    }

    public void init() {
        // Every system has its own init things
    }

    public void save() {
        save(null);
    }

    public void load() {
        load(null);
    }

    public void save(File folder) {
        File file = getFile();
        if (file == null) return;

        NbtCompound tag = toTag();
        if (tag == null) return;

        try {
            File temp = File.createTempFile(SednaClient.MOD_ID, file.getName());
            NbtIo.write(tag, temp.toPath());

            if (folder != null) file = new File(folder, file.getName());

            file.getParentFile().mkdirs();

            try {
                Files.move(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                StreamUtils.copy(temp, file);
            }

            temp.delete();
        } catch (IOException e) {
            SednaClient.LOGGER.error("Error saving {}. Possibly corrupted.", this.name, e);
        }
    }

    public void load(File folder) {
        File file = getFile();
        if (file == null) return;

        try {
            if (folder != null) file = new File(folder, file.getName());

            if (file.exists()) {
                try {
                    fromTag(NbtIo.read(file.toPath()));
                } catch (CrashException e) {
                    String backupName = FilenameUtils.removeExtension(file.getName()) + "-" + ZonedDateTime.now().format(DATE_TIME_FORMATTER) + ".backup.nbt";
                    File backup = new File(file.getParentFile(), backupName);

                    try {
                        Files.move(file.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
                    } catch (AtomicMoveNotSupportedException ex) {
                        StreamUtils.copy(file, backup);
                    }

                    SednaClient.LOGGER.error("Error loading {}. Possibly corrupted?", this.name, e);
                    SednaClient.LOGGER.info("Saved settings backup to '{}'.", backup);
                }
            }
        } catch (IOException e) {
            SednaClient.LOGGER.error("Error loading {}. Possibly corrupted?", this.name, e);
        }
    }

    private File getFile() {
        return file;
    }

    @Override
    public NbtCompound toTag() {
        return null;
    }

    @Override
    public T fromTag(NbtCompound tag) {
        return null;
    }
}
