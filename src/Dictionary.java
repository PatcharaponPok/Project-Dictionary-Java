import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


public class Dictionary {

    public static void main(String[] args) throws Exception {

        BufferedReader reader;
        ArrayList<Long> Sizefolder = new ArrayList<>();
        ArrayList<Long> SizeZip = new ArrayList<>();
        ArrayList<String> NameFile = new ArrayList<>();
        ArrayList<String> WordFromFile = new ArrayList<>();
        ArrayList<String> sql = new ArrayList<>();

        try {
            //ex.1 - 4
            reader = new BufferedReader(new FileReader("src/words.txt"));

            int count = 1;
            String line;

            while((line = reader.readLine()) != null){
                SetDirectory(line, count++);
                WordFromFile.add(line);
            }//while read line in file
            reader.close();
            System.out.println("The operation is finished.");
            // finish code ex.1 -4

            //ex.5
            File file = new File("src/DirWord/");
            Report ReportStep = new Report();
            File[] filesNames = file.listFiles();

            ReportStep.GetReportSize(5); // call head table
            if (file.isDirectory()) {
                // File[] filesNames = file.listFiles();
                count = 1;
                for (File temp : filesNames) {
                    long size;
                    if (temp.isDirectory()) {
                        File dirs = new File(temp.getPath());
                        //size = ReportStep.CalulateSize(dirs); // sum size file in folder
                        size = dirs.getTotalSpace();
                        NameFile.add(temp.getName());
                        System.out.println("|\t"+ count++ + "\t|"
                                + temp.getName() + "\t\t\t\t|"
                                + ReportStep.FileSize(size) + "\t\t\t\t|");
                        Sizefolder.add(ReportStep.FileSize(size));
                    }
                }
                System.out.println("-------------------------------------------------");
            }
            // finish code ex.5

            //ex.6
            Zip zipDirectory = new Zip();
            for (File temp : filesNames) {// loop list directory in path
                if (temp.isDirectory()) { // check type direvtory or not
                    System.out.println("Entry "+ temp.getPath()
                            + " Zipping to src/"
                            + temp.getName() + ".zip");
                    zipDirectory.zipDirectory("src/DirWord/"
                            + temp.getName(),"src/FileZip/"
                            + temp.getName());// call funtion zip from class zip
                    System.out.println("Finish to zip file: " + temp.getName() + ".zip");
                    File zip_file = new File("src/FileZip/"
                            + temp.getName() + ".zip");//สร้างตัวแปรเก็บสตริง
                    long Szip = zip_file.getTotalSpace();
                    SizeZip.add(ReportStep.FileSize(Szip));
                }// end if
            }// end loop for
            //ZipFile.close();

            ReportStep.GetReportSize(6);
            for(int i = 0; i < SizeZip.size(); i++){
                System.out.println("|\t"+ i + "\t|"
                        + NameFile.get(i) + "\t\t\t\t|"
                        + Sizefolder.get(i) + "\t\t\t\t\t\t|"
                        + SizeZip.get(i) + "\t\t\t\t\t|\t\t\t"
                        + (100 * SizeZip.get(i) / Sizefolder.get(i)) + "\t\t\t\t\t|");
            }
            System.out.println("---------------------------------------------------------------------------------------------------------------------");
            // finish ex.6

            //type 0 = The letters not same in word.
            //type 1 = The letters are the same and next to each other or The letters are the same and separated.
            //fl_same = 0 The first and last are not same, fl_same = 1 The first and last are the same
            //Numbercha = count charecter in word
            // ex.7.1
            //reader = new BufferedReader(new FileReader("src/test.txt"));
            ConnecDB con = new ConnecDB();
            int type;
            //con.testConnect();

            for(int i=0;i < WordFromFile.size();i++){
                int fl_same = 0;

                type = FindSame(WordFromFile.get(i));

                if(WordFromFile.get(i).charAt(0) == WordFromFile.get(i).charAt(WordFromFile.get(i).length() - 1)){
                    fl_same = 1;
                }
                sql.add("NULL,'" + WordFromFile.get(i) + "'," + WordFromFile.get(i).length() + "," + fl_same + "," + type);
            }
            con.insert(sql);
            con.GetCountChar();
            //finish ex.7.1

            //ex.7.2
            con.CountSameName();
            //ex.7.2

            //ex.7.3
            con.GetFirstLastSame();
            //finish ex.7.3

            //ex.7.4
            con.Convert();
            //finish ex.7.4

            //ex.8
            Pdf Export = new Pdf();
            Export.ExportPDF();
            //ex.8

        } catch (FileNotFoundException e) {
            System.out.println("Operation failed");
            e.printStackTrace();
        }
    }// main

    private static void SetDirectory(String data, int count) throws FileNotFoundException {
        String word = data.toLowerCase();
        String firstF = word.substring(0,1); // substr for set directory
        File firstL = new File("src/DirWord/"+firstF);
        //String executionPath = System.getProperty("user.dir");
        //BufferedWriter write;

        System.out.println("No." + count + " Word: " + word) ;

        if (!firstL.exists()) {
            firstL.mkdir();
        }// if check file name

        if(word.length() > 1){
            String secondF = word.substring(1,2); // substr for set subdirectory
            String SubPath = "src/DirWord/" + firstF + "/" + secondF;
            File SubDiectory = new File(SubPath);

            SubDiectory.mkdirs();
            PrintWriter WriteFile = new PrintWriter("src/DirWord/" + firstF
                    + "/" + secondF
                    + "/" + word + ".txt");

            for(int i = 0; i < 100; i++){
                WriteFile.write(word + ", ");
            }// for write word to file

            WriteFile.close();
            System.out.println("Save file successfully");
        }else{
            PrintWriter WriteFile = new PrintWriter("src/DirWord/"
                    + firstF + "/"
                    + word + ".txt");

            for(int i = 0; i < 100; i++){
                WriteFile.write(word + ", ");
            }// for write word to file

            WriteFile.close();
            System.out.println("Save file successfully");
        }// if check length word for set directory
   }; // function SetDirectory

    private static int FindSame(String word){
        String[] charecter = word.split("");
        Arrays.sort(charecter);

        boolean check = true;
        for (int i=1; i < charecter.length && check; i++){
            if(charecter[i].equals(charecter[i-1])){
                check = false;
            }
        }
        if(check){
            return 0;
        }else {
            return 1;
        }
    }

}
