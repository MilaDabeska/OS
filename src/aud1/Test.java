package aud1;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.FileAlreadyExistsException;

public class Test {
    public static void main(String[] args) { //args - argumenti od command line
        File f = new File(".");
//        String[] list;
//        if (args.length == 0) { //ako nema argumenti
//            list = f.list();
//        } else {
//            list = f.list(new DirFilter(args[0]));
//        }
//        for (int i = 0; i < list.length; i++) {
//            System.out.println(list[i]);
//        }
        DirFilter.listFile(f.getAbsolutePath(), ".txt\t");
    }

    private static class DirFilter implements FilenameFilter {
        String s;

        public DirFilter(String s) {
            this.s = s;
        }

        @Override
        public boolean accept(File dir, String name) {
            String f = new File(name).getName();
            return f.indexOf(s) != -1;
        }

        public static String getPermissions(File f) {
            return String.format("%s%s%s", f.canRead() ? "r" : "-", f.canWrite() ? "w" : "-",
                    f.canExecute() ? "x" : "-");
        }

        public static void listFile(String absolutePath, String prefix) {
            File file = new File(absolutePath);

            if (file.exists()) {
                File[] subfiles = file.listFiles();
                for (File f : subfiles) {
                    System.out.println(prefix + getPermissions(f) + "\t" + f.getName());

                    if (f.isDirectory()) {
                        listFile(f.getAbsolutePath(), prefix + "\t"); //rekurzivno
                    }
                }
            }
        }

        private final static String usage = "Usage:MakeDirectories path1 ... \n" + "Creates each path\n" + "Usage: MakeDirectories -d path1 .. \n" +
                "Usage: MakeDirectories -r path1 path2\n";
    }

    private static void fileData(File f) {
        System.out.println("Absolute path: " + f.getAbsolutePath() + "\n Can read: " + f.canRead() +
                "\n Can write: " + f.canWrite() + "\n getName: " + f.getName() + "\n getParent: " + f.getParent() +
                "\n getPath: " + f.getPath() +
                "\n lastModified: " + f.lastModified());

        if (f.isFile())
            System.out.println("it's a file");
        else if (f.isDirectory())
            System.out.println("it's a directory");
    }
}
