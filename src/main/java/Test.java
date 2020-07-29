import com.sendgrid.*;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import static java.nio.charset.Charset.forName;

public class Test {
    public static void main(String[] args) throws Exception {

        String HTML = "<h1>Hello World</h1>"
                + "<p>This was created using iText</p>"
                + "<a href='hmkcode.com'>hmkcode.com</a>";

        Email from = new Email("puneetkumargoyal@gmail.com");
        String subject = "Sending with SendGrid is Funnys";
        Email to = new Email("puneetgoyal1985@gmail.com");
        Content content = new Content("text/html", HTML);
        Mail mail = new Mail(from, subject, to, content);

        Attachments attachments3 = new Attachments();
        Base64 x = new Base64();
        String imageDataString = x.encodeAsString(generatePDFFromHTML(HTML));
        attachments3.setContent(imageDataString);
        attachments3.setType("application/pdf");//image/png
        attachments3.setFilename("x.pdf");
        attachments3.setDisposition("attachment");
        //attachments3.setContentId("Banner");
        mail.addAttachments(attachments3);

        SendGrid sg = new SendGrid("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");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }

    public static byte[] generatePDFFromHTML(String html) throws IOException, DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
        InputStream stream = new ByteArrayInputStream(html.getBytes());
        worker.parseXHtml(writer, document, stream, forName("UTF-8"));
        document.close();
        return outputStream.toByteArray();
    }
}
