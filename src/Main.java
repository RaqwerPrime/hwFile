import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {
        StringBuilder log = new StringBuilder();

        List<File> catalogs = Arrays.asList(new File("D://Games//src"),

                new File("D://Games//res"), new File("D://Games//savegames"),

                new File("D://Games//temp"), new File("D://Games//src//main"),

                new File("D://Games//src//test"), new File("D://Games//res//drawables"),

                new File("D://Games//res//vectors"), new File("D://Games//res//icons"));

        List<File> fiels = Arrays.asList(new File("D://Games//src//main//Main.java"),

                new File("D://Games//src//main//Utils.java"), new File("D://Games//temp//temp.txt"));


        for (File dir : catalogs) {
            if (dir.mkdirs()) {
                log.append("Каталог " + dir + " создан!\n");
            } else {
                log.append("Каталог " + dir + " не удалось создать!\n");
            }

        }
        try {
            for (File file : fiels) {
                if (file.createNewFile()) {
                    log.append("Файл " + file + " создан!\n");
                } else {
                    log.append("Файл " + file + " не удалось создать!\n");
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try {
            FileWriter writer = new FileWriter("D://Games//temp//temp.txt", true);//сделал true, что бы
            // после каждого запуска дополнять log
            writer.write(String.valueOf(log));
            writer.flush();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


        ArrayList<String> saveFilesPath = new ArrayList<>(Arrays.asList("D://Games//savegames//save1.dat",
                "D://Games//savegames//save2.dat",
                "D://Games//savegames//save3.dat"));


        GameProgress player1 = new GameProgress(33, 4, 44, 234.4);
        GameProgress player2 = new GameProgress(23, 5, 22, 34.5);
        GameProgress player3 = new GameProgress(33, 43, 21, 43.4);

        saveGames(saveFilesPath.get(0), player1);
        saveGames(saveFilesPath.get(1), player2);
        saveGames(saveFilesPath.get(2), player3);

        zipFiles("D://Games//savegames//save.zip", saveFilesPath);
        deleteFiles(saveFilesPath);

    }

    public static void saveGames(String saveFilesPath, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(saveFilesPath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    public static void zipFiles(String pathZip, ArrayList<String> saveFilesPath) {

        try (FileOutputStream fos = new FileOutputStream(pathZip);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            for (String file : saveFilesPath) {
                File fileToZip = new File(file);

                try (FileInputStream fis = new FileInputStream(fileToZip)) {
                    ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                    zos.putNextEntry(zipEntry);
                    byte[] bytes = new byte[8192];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zos.write(bytes, 0, length);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteFiles(ArrayList<String> saveFilesPath) {
        for (int i = 0; i < saveFilesPath.size(); i++) {
            Path path = Path.of(saveFilesPath.get(i));
            try {
                Files.delete(path);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }
    }
}
