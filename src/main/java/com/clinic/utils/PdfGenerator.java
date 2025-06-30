package com.clinic.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.clinic.models.Payment;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PdfGenerator {

    public static void generateInvoicePDF(Payment payment, Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Simpan Invoice PDF");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        fileChooser.setInitialFileName("invoice-" + payment.getNoInvoice() + ".pdf");

        File file = fileChooser.showSaveDialog(stage);

        String medicalRecordNumber = getMedicalRecordNumberByVisitId(payment.getIdKunjungan());
        String patientName = getPatientNameByVisitId(payment.getIdKunjungan());

        if (file != null) {
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                // Font styling
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
                Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
                Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

                // Header klinik
                Paragraph clinicHeader = new Paragraph("Klinik Sehat Bahagia", headerFont);
                clinicHeader.setAlignment(Element.ALIGN_CENTER);
                document.add(clinicHeader);

                Paragraph clinicAddress = new Paragraph("Jl. Kesehatan No. 123, Padang\nTelp: (021) 555-1234 | Email: info@kliniksehat.com", normalFont);
                clinicAddress.setAlignment(Element.ALIGN_CENTER);
                clinicAddress.setSpacingAfter(20);
                document.add(clinicAddress);

                // Garis pemisah
                document.add(new Paragraph("________________________________________________________________________"));
                document.add(new Paragraph("\n"));

                // Informasi invoice
                Paragraph invoiceTitle = new Paragraph("INVOICE PEMBAYARAN", titleFont);
                invoiceTitle.setSpacingAfter(15);
                document.add(invoiceTitle);

                // Tabel informasi
                PdfPTable infoTable = new PdfPTable(2);
                infoTable.setWidthPercentage(100);
                infoTable.setWidths(new float[]{30, 70});

                addCell(infoTable, "No. Invoice:", boldFont);
                addCell(infoTable, payment.getNoInvoice(), normalFont);
                addCell(infoTable, "No. Rekam Medis:", boldFont);
                addCell(infoTable, medicalRecordNumber, normalFont);
                addCell(infoTable, "Nama Pasien:", boldFont);
                addCell(infoTable, patientName, normalFont);
                addCell(infoTable, "Tanggal Pembayaran:", boldFont);
                addCell(infoTable, payment.getTanggalPembayaran().toString(), normalFont);
                addCell(infoTable, "Status Pembayaran:", boldFont);
                addCell(infoTable, payment.getStatusPembayaran().toString(), normalFont);

                document.add(infoTable);
                document.add(new Paragraph("\n"));

                // Detail biaya
                Paragraph costTitle = new Paragraph("RINCIAN BIAYA", titleFont);
                costTitle.setSpacingAfter(15);
                document.add(costTitle);

                PdfPTable costTable = new PdfPTable(2);
                costTable.setWidthPercentage(100);
                costTable.setWidths(new float[]{70, 30});

                addCell(costTable, "Biaya Konsultasi", boldFont);
                addCell(costTable, formatCurrency(payment.getBiayaKonsultasi()), normalFont);
                addCell(costTable, "Biaya Tindakan", boldFont);
                addCell(costTable, formatCurrency(payment.getBiayaTindakan()), normalFont);
                addCell(costTable, "Biaya Obat", boldFont);
                addCell(costTable, formatCurrency(payment.getBiayaObat()), normalFont);

                // Baris kosong untuk separator
                PdfPCell emptyCell = new PdfPCell(new Phrase(""));
                emptyCell.setBorder(PdfPCell.NO_BORDER);
                costTable.addCell(emptyCell);
                costTable.addCell(emptyCell);

                // Total biaya
                addCell(costTable, "TOTAL BIAYA", boldFont);
                addCell(costTable, formatCurrency(payment.getTotalBiaya()), boldFont);

                document.add(costTable);
                document.add(new Paragraph("\n\n"));

                // Tanda tangan
                PdfPTable signatureTable = new PdfPTable(2);
                signatureTable.setWidthPercentage(100);

                addCenteredCell(signatureTable, "Petugas Kasir", boldFont);
                addCenteredCell(signatureTable, "Pasien", boldFont);
                addCenteredCell(signatureTable, "\n\n(____________________)", normalFont);
                addCenteredCell(signatureTable, "\n\n(____________________)", normalFont);
                addCenteredCell(signatureTable, "Admin", normalFont);
                addCenteredCell(signatureTable, patientName, normalFont);

                document.add(signatureTable);
                document.add(new Paragraph("\n"));

                // Catatan kaki
                Paragraph footer = new Paragraph(
                        """
                    Terima kasih telah menggunakan layanan Klinik Sehat Bahagia
                    * Tagihan ini merupakan bukti pembayaran yang sah
                    ** Simpan tagihan ini untuk keperluan klaim asuransi""",
                        normalFont
                );
                footer.setAlignment(Element.ALIGN_CENTER);
                document.add(footer);

                document.close();

                // Konfirmasi sukses
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sukses");
                alert.setHeaderText(null);
                alert.setContentText("Invoice berhasil disimpan di:\n" + file.getAbsolutePath());
                alert.showAndWait();

            } catch (DocumentException | IOException e) {
                e.printStackTrace();
                showErrorAlert("Gagal menyimpan PDF: " + e.getMessage());
            }
        }
    }

    private static void addCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        table.addCell(cell);
    }

    private static void addCenteredCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private static String formatCurrency(double amount) {
        return String.format("Rp%,.2f", amount);
    }

    private static void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static String getMedicalRecordNumberByVisitId(int visitId) {
        String noRm = "N/A";
        String sql = "SELECT p.no_rm "
                + "FROM pasien p "
                + "JOIN kunjungan k ON k.id_pasien = p.id_pasien "
                + "WHERE k.id_kunjungan = ?";

        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, visitId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                noRm = rs.getString("no_rm");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return noRm;
    }

    public static String getPatientNameByVisitId(int visitId) {
        String patientName = "N/A";
        String sql = "SELECT p.nama_lengkap "
                + "FROM pasien p "
                + "JOIN kunjungan k ON k.id_pasien = p.id_pasien "
                + "WHERE k.id_kunjungan = ?";

        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, visitId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                patientName = rs.getString("nama_lengkap");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patientName;
    }
}
