/*
 * Persistence.java
 * Holds methods which manage serialization, deserialization and other 
 * file-related tasks. 
 */
package swimmingcompetition.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wickramaranga
 */
public class Persistence {

    /**
     * (User's home directory)/.SwimmingCompetitionSimulator/ is the software's
     * persistent storage directory.
     */
    public static final String SAVE_PATH = System.getProperty("user.home")
                                           + "/.SwimmingCompetitionSimulator/";

    /**
     * Custom file extension for SwimmingCompetition objects.
     */
    public static final String EXTENSION = ".w-scs";

    /**
     * Gets a list of SwimmingCompetition objects in the SAVE_PATH directory.
     *
     * @return a list of names of serialized SwimmingCompetition objects.
     */
    public static List<String> listFiles() {
        List<String> list = new ArrayList<>();
        File scsHome = new File(SAVE_PATH);
        if (scsHome.isDirectory()) {
            for (File f : scsHome.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(EXTENSION);
                }
            })) {
                String fileName = f.getName();
                list.add(fileName.substring(
                        0, fileName.length() - EXTENSION.length()));
            }
        }
        return list;
    }

    /**
     * Deletes given file from SwimmingCompetitionSimulator's persistent
     * storage.
     *
     * @param fileName name of the file to be removed. (Without path or ext.)
     * @return whether the file was removed.
     */
    public static boolean removeFile(String fileName) {
        File file = new File(SAVE_PATH + fileName + EXTENSION);
        return file.delete();
    }

    /**
     * Serializes a SwimmingCompetition object as binary to a file.
     *
     * @param object the SwimmingCompetition object which needs to be saved.
     * @param fileName name of the SwimmingCompetition.
     * @return whether the file was saved.
     */
    public static boolean saveFile(Object object, String fileName) {
        try (FileOutputStream fos
                = new FileOutputStream(SAVE_PATH + fileName + EXTENSION);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(object);
        } catch (Exception ex) {
            //ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Loads a SwimmingCompetition object from a file.
     *
     * @param fileName name of the competition to be loaded.
     * @return an object of the required competition if found or null otherwise.
     */
    public static Object loadFile(String fileName) {
        try (FileInputStream fis
                = new FileInputStream(SAVE_PATH + fileName + EXTENSION);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return ois.readObject();
        } catch (Exception ex) {
            //ex.printStackTrace();
            return null;
        }
    }

}
