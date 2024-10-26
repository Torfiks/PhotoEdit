import java.io.File;
import java.util.ArrayList;

public class PhotoShare {
    public static ArrayList<String> findPhotos(File directory) {
        File[] files = directory.listFiles();
        ArrayList<String> names = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findPhotos(file);
                } else {
                    if (isPhotoFile(file.getName())) {
                        names.add(file.getAbsolutePath());
                    }
                }
            }
        }
        return names;
    }
    private static boolean isPhotoFile(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("gif");
    }
}
