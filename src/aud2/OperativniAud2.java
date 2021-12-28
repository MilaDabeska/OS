package aud2;

import java.io.*;

public class OperativniAud2 {
    public static void main(String[] args) throws IOException {
        InputStream in = new FileInputStream("proba.txt");
        OutputStream out = new FileOutputStream("file.txt"); //se kreira file
//        copyStream(in, out);
//        correctReading(in);
//        System.out.println(readTextFile("proba.txt"));
//
//        copyTextFile("proba.txt", "file.txt"); //from -> to
//        fileOutputLineNumbering();
//        dataReadWrite();
//        display("proba.txt");
//        randomAccess("proba.txt");
        redirect();

    }

    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        //Read and Write, byte by byte
        try {
            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
        } finally {
            if (in != null) in.close(); //ako inputot e nevaliden
            if (out != null) out.close(); //ako outputot e nevaliden
        }
    }

    public static void correctReading(InputStream in) throws IOException {
        //pravilno citanje od niza na bytes
        try {
            byte[] buffer = new byte[100];
            int readLength = 0; //kolku bytes imame procitano
            int leftToBeRead = 100; //kolku slobodni mesta imame vo bufferot
            int offset = 0; //do kaj sme stignale so citanje vo bufferot

            while ((readLength = in.read(buffer, offset, leftToBeRead)) != -1) {
                offset += readLength; //offsetot se pomestuva za onolku bytes kolku sto sme procitale
                leftToBeRead -= readLength; //slobodnite mesta se namaluvaat za onolku bytes kolku sto sme procitale
            }
            doSomethingWithReadData(buffer, offset);

        } finally {
            if (in != null) in.close();
        }
    }

    private static void doSomethingWithReadData(byte[] buffer, int offset) {
        System.out.println(new String(buffer));
    }

    private static String readTextFile(String path) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
//        String line = null;
        String line = br.readLine();
        StringBuilder sb = new StringBuilder();
        String tmp = "";
//        while ((line = br.readLine()) != null) {
        while (line != null) { //II nacin
            sb.append(line).append("\n"); //I nacin
            tmp += line + "\n"; //II nacin
            br.readLine();
        }
        br.close();
        return sb.toString();
    }

    private static void stdinRead() throws IOException {
        //citanje od konzola,tastatura
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter a line");
        String line = br.readLine();
        while (line != null && line.length() != 0) {
            //line.length() != 0 -> za da ne pravime force stop na programata
            System.out.println(line);
            System.out.println("Enter a line");
            line = br.readLine();
        }
    }

    private static void writeTextFromFile(String path, String text, boolean append) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(path, append));
        bw.write(text);
        bw.close();
    }

    private static void copyTextFile(String from, String to) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(from)); //citanje
        BufferedWriter bw = new BufferedWriter(new FileWriter(to)); //zapishuvanje

        String line = br.readLine();
//        String line=null;
        while (line != null) {
//        while ((line=br.readLine())!=null){
            bw.write(line + "\n"); //write nema nov red,pa nie morame da stavame
            line = br.readLine();
        }
        br.close();
        bw.close();
    }

    private static void fileOutputLineNumbering() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("proba.txt"));
//        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("file.txt")));
        BufferedWriter bw = new BufferedWriter(new FileWriter("file.txt"));
        try {
            String line = br.readLine();
            int lineNumbering = 1; //broenje na liniite
            while (line != null) {
//                pw.println(lineNumbering + ": " + line); //println -> razlika megju BufferedWriter i PrintWriter
                bw.write(lineNumbering + ": " + line + "\n");
                line = br.readLine();
                lineNumbering++;
            }
        } finally {
            if (br != null) br.close();
//            if (pw != null) pw.close();
            if (bw != null) bw.close();
        }
    }

    public static void dataReadWrite() throws IOException {
        DataInputStream in = new DataInputStream(new FileInputStream("proba.txt"));
        DataOutputStream out = new DataOutputStream(new FileOutputStream("proba.txt")); //vo 1 ist txt file
//        DataInputStream in=null;
//        DataOutputStream out=null;

        try {
//            in = new DataInputStream(new FileInputStream("proba.txt"));
//            out = new DataOutputStream(new FileOutputStream("proba.txt"));
            out.writeDouble(35.77);
            out.writeUTF("Operativni Sistemi");
            out.writeInt(10);
            System.out.println(in.readDouble());
            System.out.println(in.readUTF());
            System.out.println(in.readInt());
        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
        }
    }

    public static void display(String path) throws IOException {
        RandomAccessFile file = null;

        try {
            file = new RandomAccessFile(/*"proba.txt"*/ path, "r");
            for (int i = 0; i < 10; i++) {
                System.out.println("Value: " + i + " " + file.readDouble()/*file.readLine()*/);
            }
            System.out.println(file.readUTF());
        } finally {
            if (file != null) file.close();
        }
    }

    public static void randomAccess(String path) throws IOException {
        RandomAccessFile file = null;

        try {
            file = new RandomAccessFile(/*"proba.txt"*/ path, "rw");
            for (int i = 0; i < 10; i++) {
                file.writeDouble(i * 1.12);
            }
            file.writeUTF("End");
        } finally {
            if (file != null) file.close();
        }
        display(path);
        try {
            file = new RandomAccessFile(/*"proba.txt"*/ path, "rw");
            file.seek(40); //5(shifts)*8(bits)
            System.out.println(file.readDouble());
            file.writeDouble(0.12);
        } finally {
            if (file != null) file.close();
        }
        display(path);
    }

    private static void redirect() throws IOException {
        InputStream consoleIn = System.in;
        PrintStream consoleOut = System.out;
        BufferedInputStream in = null;
        PrintStream out = null;

        try {
            in = new BufferedInputStream(new FileInputStream("proba.txt"));
            out = new PrintStream(new BufferedOutputStream(new FileOutputStream("file.txt")));

            System.setIn(in); //citanje
            System.setOut(out); //zapishuvanje
            System.setErr(out);

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //citanje od konzola(tastatura)
            String line = br.readLine();
            while (line != null) {
//                String[] parts = br.readLine().split(" ");
//                int num1 = Integer.parseInt(parts[0]);
//                int num2 = Integer.parseInt(parts[1]);
//                System.out.println(num1 + num2);
                System.out.println(line);
                line = br.readLine();
            }
        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
            System.setIn(consoleIn);
            System.setOut(consoleOut);
        }
    }
}
