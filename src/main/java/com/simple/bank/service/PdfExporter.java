package com.simple.bank.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.simple.bank.model.UserPdfData;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class PdfExporter {

    private List<UserPdfData> listUsers;

    public PdfExporter(List<UserPdfData> listUsers) {
        this.listUsers = listUsers;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        com.lowagie.text.Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Account Number", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Customer Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Account Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Deposit Amount", font));
        table.addCell(cell);

//        cell.setPhrase(new Phrase("Enabled", font));
//        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        for (UserPdfData user : listUsers) {
            table.addCell(String.valueOf(user.getUserAccountNumber()));
            table.addCell(user.getUserName());
            table.addCell(user.getUserAccountName());
            table.addCell(String.valueOf(user.getDepositAmount()));
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("Account statement", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();

    }
}
