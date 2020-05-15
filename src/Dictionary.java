import java.io.BufferedReader;
import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.FileReader; // Read file
import java.io.PrintWriter; // Import this class to write file
import java.util.ArrayList; // Array list
import java.util.Arrays;
import java.util.zip.ZipFile; // Zip File

public class Dictionary {

    public static void main(String[] args) throws Exception {

        BufferedReader reader;
        ArrayList<Long> Sizefolder = new ArrayList<>();
        ArrayList<Long> SizeZip = new ArrayList<>();
        ArrayList<String> NameFile = new ArrayList<>();
        try {
            //ex.1 - 4
            reader = new BufferedReader(new FileReader("/Job-Java/Project/src/words.txt"));

            int count = 1;
            String line;

            while((line = reader.readLine()) != null){
                SetDirectory(line, count++);
            }//while read line in file
            reader.close();
            System.out.println("The operation is finished.");
            // finish code ex.1 -4

            //ex.5
            File file = new File("/Job-Java/Project/src/DirWord/");
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
                        size = ReportStep.CalulateSize(dirs); // sum size file in folder
                        NameFile.add(temp.getName());
                        System.out.println("|\t"+ count++ + "\t|" + temp.getName() + "\t\t\t|" + ReportStep.FileSize(size) + "\t\t\t\t|");
                        Sizefolder.add(ReportStep.FileSize(size));
                    }
                }
                System.out.println("-------------------------------------------------------------------------");
            }
            // finish code ex.5

            //ex.6
            Zip zipDirectory = new Zip();
            for (File temp : filesNames) {// loop list directory in path
                if (temp.isDirectory()) { // check type direvtory or not
                    System.out.println("Entry "+ temp.getPath() + " Zipping to /Job-Java/Project/src/" + temp.getName() + ".zip");
                    zipDirectory.zipDirectory("/Job-Java/Project/src/DirWord/" + temp.getName(),"/Job-Java/Project/src/FileZip/" + temp.getName());// call funtion zip from class zip
                    System.out.println("Finish to zip file: " + temp.getName() + ".zip");
                    ZipFile zip_file = new ZipFile("/Job-Java/Project/src/DirWord/" + temp.getName() + ".zip");
                    long Szip = zip_file.size();
                    SizeZip.add(Szip);
                }// end if
            }// end loop for
            //ZipFile.close();

            ReportStep.GetReportSize(6);
            for(int i = 0; i < SizeZip.size(); i++){
                System.out.println("|\t"+ i + "\t|" + NameFile.get(i) + "\t\t\t|" + Sizefolder.get(i) + "\t\t\t\t\t|" + SizeZip.get(i) + "\t\t\t\t\t|\t\t" + (100 * SizeZip.get(i) / Sizefolder.get(i)) + "\t\t\t|");
            }
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------");
            // finish ex.6

            //type 0 = The letters not same in word.
            //type 1 = The letters are the same and next to each other or The letters are the same and separated.
            //fl_same = 0 The first and last are not same, fl_same = 1 The first and last are the same
            //Numbercha = count charecter in word
            // ex.7.1
            reader = new BufferedReader(new FileReader("/Job-Java/Project/src/words.txt"));
            ConnecDB con = new ConnecDB();
            String sql;
            int type;
            //con.testConnect();

            while((line = reader.readLine()) != null){
                int fl_same = 0;

                type = FindSame(line);

                if(line.charAt(0) == line.charAt(line.length() - 1)){
                    fl_same = 1;
                }
                sql = "NULL,'" + line + "'," + line.length() + "," + fl_same + "," + type;
                System.out.println(sql);
                con.insert(sql);
            }//while read line in file
            reader.close();
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

        } catch (FileNotFoundException e) {
            System.out.println("Operation failed");
            e.printStackTrace();
        }
    }// main

    private static void SetDirectory(String data, int count) throws FileNotFoundException {
        String word = data.toLowerCase();
        String firstF = word.substring(0,1); // substr for set directory
        File firstL = new File("/Job-Java/Project/src/DirWord/"+firstF);
        //String executionPath = System.getProperty("user.dir");
        //BufferedWriter write;

        System.out.println("No." + count + " Word: " + word) ;

        if (!firstL.exists()) {
            firstL.mkdir();
        }// if check file name

        if(word.length() > 1){
            String secondF = word.substring(1,2); // substr for set subdirectory
            String SubPath = "/Job-Java/Project/src/DirWord/" + firstF + "/" + secondF;
            File SubDiectory = new File(SubPath);

            SubDiectory.mkdirs();
            PrintWriter WriteFile = new PrintWriter("/Job-Java/Project/src/DirWord/" + firstF + "/" + secondF + "/" + word + ".txt");

            for(int i = 0; i < 100; i++){
                WriteFile.write(word + ", ");
            }// for write word to file

            WriteFile.close();
            System.out.println("Save file successfully");
        }else{
            PrintWriter WriteFile = new PrintWriter("/Job-Java/Project/src/DirWord/" + firstF + "/" + word + ".txt");

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
