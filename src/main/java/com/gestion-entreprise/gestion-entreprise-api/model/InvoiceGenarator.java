package com.example.springbootoauthjwt.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.springbootoauthjwt.exceptions.InvoiceException;
import com.example.springbootoauthjwt.service.InvoiceService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

public class InvoiceGenarator {
//   @Autowired
//    private  InvoiceService invoiceService;

    public byte[] generateInvoice1(String companyName,String companyEmail,String invoiceNumber, String invoiceDate, String customerName, String customerAddress, 
            Set<InvoiceItem> items,String totalAmount) throws IOException, InvoiceException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Header
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 26);
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("Salford & Co.");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, 730);
                contentStream.showText("123 Anywhere St., Any City, ST 12345");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, 710);
                contentStream.showText("Tel: +123-456-7890");
                contentStream.endText();

                // Invoice title
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
                contentStream.newLineAtOffset(50, 650);
                contentStream.showText("INVOICE");
                contentStream.endText();

                // Invoice details
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, 630);
                contentStream.showText("Invoice No: " + invoiceNumber);
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(400, 630);
                contentStream.showText("Date: " + invoiceDate);
                contentStream.endText();

                // Bill to details
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, 610);
                contentStream.showText("Bill to:");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, 590);
                contentStream.showText(customerName);
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, 570);
                contentStream.showText(customerAddress);
                contentStream.endText();

                // Table headers
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(50, 500);
                contentStream.showText("Item");
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText("Description");
                contentStream.newLineAtOffset(200, 0);
                contentStream.showText("Price");
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText("Amount");
                contentStream.endText();

                // Table content
                int yOffset = 480;
                // String[][] items = [["1", "Logo Design", "200", "200"],["2", "Advertising Design", "500", "500"]];
                
                // String[][] items = { { "1", "Logo Design", "200", "200" },
                //         { "2", "Advertising Design", "500", "500" } };
        //    Long id=1;
        //          Invoice invoice =  invoiceService.findInvoiceById((long) 1);
                for (InvoiceItem item : items) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.newLineAtOffset(50, yOffset);
                    contentStream.showText(item.getItemId().toString());
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.showText(item.getItemName());
                    contentStream.newLineAtOffset(200, 0);
                    contentStream.showText("$" + item.getPrice());
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.showText("$" + item.getQuantity());
                    contentStream.endText();
                    yOffset -= 20;
                }

                // Total
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(400, 300);
                contentStream.showText("Total");
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText("$" + totalAmount);
                contentStream.endText();

                // Footer
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, 100);
                contentStream.showText("If you have any questions, please contact: hello@reallygreatsite.com");
                contentStream.endText();
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }

    public  void  generateInvoice(String customerName, String productName, String price) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Invoice");
            contentStream.endText();

            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 680);
            contentStream.showText("Customer: " + customerName);
            contentStream.newLine();
            contentStream.showText("Product: " + productName);
            contentStream.newLine();
            contentStream.showText("Price: $" + price);
            contentStream.endText();
            contentStream.close();

            document.save("invoices/invoice.pdf");
        }
    }
}
