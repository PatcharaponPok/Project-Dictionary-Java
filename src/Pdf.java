import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Pdf {

    public static final String Thsarabun = "font/THSarabun.ttf";

    //public void ExportPDF(){
    static ArrayList<String> text = new ArrayList<>();
    public void ExportPDF(){
        try
        {
            ConnecDB con = new ConnecDB();
            text = con.SelectWord();
            String word ;
            var doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream("Word.pdf"));
            doc.open();

            BaseFont baseFont = BaseFont.createFont(Thsarabun, BaseFont.WINANSI, BaseFont.EMBEDDED);

            Font font = new Font(baseFont, 18);
            for(int i=0;i<text.size();i++){
                var txt = new Paragraph(text.get(i));
                doc.add(txt);
                txt.setFont(font);
            }
            doc.close();
            System.out.println("Export PDF Success");
            System.out.println("PDF file : Word.pdf");
        } catch (DocumentException e)
        {
            e.printStackTrace();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
