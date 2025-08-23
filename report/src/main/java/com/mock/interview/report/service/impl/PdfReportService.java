package com.mock.interview.report.service.impl;

import com.mock.interview.lib.dto.CreateReportRequest;
import com.mock.interview.lib.model.ReportFormat;
import com.mock.interview.lib.model.ReportModel;
import com.mock.interview.lib.model.ReportStatus;
import com.mock.interview.report.service.ReportService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PdfReportService implements ReportService {

    private static final float MARGIN = 50;
    private static final float LINE_HEIGHT = 20;
    private static final float TITLE_FONT_SIZE = 24;
    private static final float HEADING_FONT_SIZE = 16;
    private static final float BODY_FONT_SIZE = 12;
    private static final float SMALL_FONT_SIZE = 10;

    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color ACCENT_COLOR = new Color(231, 76, 60);
    private static final Color LIGHT_GRAY = new Color(245, 245, 245);
    private static final Color DARK_GRAY = new Color(51, 51, 51);

    @Override
    public ReportFormat getStatus() {
        return ReportFormat.PDF;
    }

    @Override
    public ReportModel generate(CreateReportRequest createReportRequest) throws IOException {
        var fileName = "interview-report-" + createReportRequest.interviewId() + "-" +
                System.currentTimeMillis() + ".pdf";
        var filePath = "./reports/" + fileName;

        try (PDDocument document = new PDDocument()) {
            var page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            var contentStream = new PDPageContentStream(document, page);

            var titleFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            var headingFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            var bodyFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            var italicFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE);

            var mediaBox = page.getMediaBox();
            var width = mediaBox.getWidth();
            var yPosition = mediaBox.getHeight() - MARGIN + 20;

            drawHeader(contentStream, width, mediaBox.getHeight());

            yPosition = addTitle(contentStream, titleFont, "INTERVIEW REPORT", width, yPosition);
            yPosition -= LINE_HEIGHT * 1.5f;

            yPosition = addMetadataSection(contentStream, headingFont, bodyFont, italicFont,
                    createReportRequest, width, yPosition);
            yPosition -= LINE_HEIGHT;

            if (createReportRequest.description() != null && !createReportRequest.description().isEmpty()) {
                yPosition = addDescriptionSection(contentStream, headingFont, bodyFont,
                        createReportRequest.description(), width, yPosition);
                yPosition -= LINE_HEIGHT;
            }

            addFooter(contentStream, bodyFont, italicFont, width, mediaBox.getHeight());

            contentStream.close();
            document.save(filePath);
        }

        return ReportModel.builder()
                .generatedAt(LocalDateTime.now())
                .format(ReportFormat.PDF)
                .title(createReportRequest.title())
                .description(createReportRequest.description())
                .interviewId(createReportRequest.interviewId())
                .filePath(filePath)
                .status(ReportStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private void drawHeader(PDPageContentStream contentStream, float width, float height) throws IOException {
        contentStream.setNonStrokingColor(PRIMARY_COLOR);
        contentStream.addRect(0, height - 80, width, 80);
        contentStream.fill();

        contentStream.setNonStrokingColor(Color.WHITE);
    }

    private float addTitle(PDPageContentStream contentStream, PDFont font, String title,
                           float width, float yPosition) throws IOException {
        contentStream.setFont(font, TITLE_FONT_SIZE);
        contentStream.setNonStrokingColor(Color.WHITE);

        var titleWidth = font.getStringWidth(title) / 1000 * TITLE_FONT_SIZE;
        var titleX = (width - titleWidth) / 2;

        contentStream.beginText();
        contentStream.newLineAtOffset(titleX, yPosition - 40);
        contentStream.showText(title);
        contentStream.endText();

        return yPosition - 60;
    }

    private float addMetadataSection(PDPageContentStream contentStream, PDFont headingFont,
                                     PDFont bodyFont, PDFont italicFont,
                                     CreateReportRequest request, float width, float yPosition) throws IOException {
        contentStream.setFont(headingFont, HEADING_FONT_SIZE);
        contentStream.setNonStrokingColor(SECONDARY_COLOR);
        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN, yPosition);
        contentStream.showText("Interview Details");
        contentStream.endText();

        yPosition -= LINE_HEIGHT * 1.5f;

        var tableWidth = width - 2 * MARGIN;
        var cellPadding = 10;

        contentStream.setNonStrokingColor(LIGHT_GRAY);
        contentStream.addRect(MARGIN, yPosition - 140, tableWidth, 140);
        contentStream.fill();

        contentStream.setNonStrokingColor(DARK_GRAY);

        addTableRow(contentStream, bodyFont, "Interview ID:",
                request.interviewId().toString(), MARGIN, yPosition, tableWidth);
        yPosition -= LINE_HEIGHT;

        addTableRow(contentStream, bodyFont, "Title:",
                request.title(), MARGIN, yPosition, tableWidth);
        yPosition -= LINE_HEIGHT;

        var generatedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        addTableRow(contentStream, bodyFont, "Generated:", generatedDate, MARGIN, yPosition, tableWidth);
        yPosition -= LINE_HEIGHT;

        addTableRow(contentStream, bodyFont, "Format:", "PDF", MARGIN, yPosition, tableWidth);
        yPosition -= LINE_HEIGHT;

        contentStream.setNonStrokingColor(ACCENT_COLOR);
        addTableRow(contentStream, bodyFont, "Status:", "COMPLETED", MARGIN, yPosition, tableWidth);

        return yPosition - LINE_HEIGHT;
    }

    private float addDescriptionSection(PDPageContentStream contentStream, PDFont headingFont,
                                        PDFont bodyFont, String description,
                                        float width, float yPosition) throws IOException {
        contentStream.setFont(headingFont, HEADING_FONT_SIZE);
        contentStream.setNonStrokingColor(SECONDARY_COLOR);
        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN, yPosition);
        contentStream.showText("Description");
        contentStream.endText();

        yPosition -= LINE_HEIGHT * 1.5f;

        contentStream.setNonStrokingColor(LIGHT_GRAY);
        contentStream.addRect(MARGIN, yPosition - 120, width - 2 * MARGIN, 120);
        contentStream.fill();

        contentStream.setNonStrokingColor(DARK_GRAY);
        contentStream.setFont(bodyFont, BODY_FONT_SIZE);

        var words = description.split(" ");
        var line = new StringBuilder();
        var textY = yPosition - 20;

        for (String word : words) {
            var testLine = line + (!line.isEmpty() ? " " : "") + word;
            var testWidth = bodyFont.getStringWidth(testLine) / 1000 * BODY_FONT_SIZE;

            if (testWidth > (width - 2 * MARGIN - 20)) {
                drawTextLine(contentStream, line.toString(), MARGIN + 10, textY);
                textY -= LINE_HEIGHT;
                line = new StringBuilder(word);
            } else {
                line.append(!line.isEmpty() ? " " : "").append(word);
            }
        }

        if (!line.isEmpty()) {
            drawTextLine(contentStream, line.toString(), MARGIN + 10, textY);
        }

        return textY - LINE_HEIGHT;
    }

    private void addFooter(PDPageContentStream contentStream, PDFont bodyFont,
                           PDFont italicFont, float width, float height) throws IOException {
        contentStream.setFont(italicFont, SMALL_FONT_SIZE);
        contentStream.setNonStrokingColor(Color.GRAY);

        var footerText = "Generated by Mock Interview System • " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        var textWidth = italicFont.getStringWidth(footerText) / 1000 * SMALL_FONT_SIZE;
        var footerX = (width - textWidth) / 2;

        contentStream.beginText();
        contentStream.newLineAtOffset(footerX, MARGIN);
        contentStream.showText(footerText);
        contentStream.endText();
    }

    private void addTableRow(PDPageContentStream contentStream, PDFont font,
                             String label, String value, float x, float y, float tableWidth) throws IOException {
        contentStream.setFont(font, BODY_FONT_SIZE);

        contentStream.beginText();
        contentStream.newLineAtOffset(x + 10, y - 15);
        contentStream.showText(label);
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(x + tableWidth / 2, y - 15);
        contentStream.showText(value);
        contentStream.endText();

        contentStream.setStrokingColor(new Color(200, 200, 200));
        contentStream.setLineWidth(0.5f);
        contentStream.moveTo(x + 5, y - 25);
        contentStream.lineTo(x + tableWidth - 5, y - 25);
        contentStream.stroke();
    }

    private void drawTextLine(PDPageContentStream contentStream, String text,
                              float x, float y) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }
}