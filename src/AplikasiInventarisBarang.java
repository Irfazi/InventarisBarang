import javax.swing.*;               // Komponen GUI
import javax.swing.table.DefaultTableModel;  // Model tabel
import java.awt.*;                  // Layout dan desain GUI
import java.awt.event.*;            // Event handling
import java.io.*;                   // Operasi file
import java.util.*;    
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfPTable;
import org.apache.poi.ss.usermodel.*;
import java.io.FileOutputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook; 
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;// Untuk file .xls


// org.json untuk ekspor dan impor JSON
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.table.DefaultTableModel;


public class AplikasiInventarisBarang extends javax.swing.JFrame {

    /**
     * Creates new form AplikasiInventarisBarang
     */
    public AplikasiInventarisBarang() {
        initComponents();
         loadTableData();  // Memuat data ke dalam JTable
        addTableSelectionListener();
        
   DefaultTableModel model = new DefaultTableModel();
model.addColumn("Nama");
model.addColumn("Kategori");
model.addColumn("Jumlah");
model.addColumn("Harga");




// Menghubungkan model ke JTable
tblBarang.setModel(model);
    }
    private void loadTableData() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Nama");
        model.addColumn("Kategori");
        model.addColumn("Jumlah");
        model.addColumn("Harga");

        // Misalkan ada data barang
        model.addRow(new Object[]{"Barang 1", "Kategori A", 10, 10000.0});
        model.addRow(new Object[]{"Barang 2", "Kategori B", 20, 20000.0});

        tblBarang.setModel(model);  // Set model tabel dengan data
    }
private void addTableSelectionListener() {
        tblBarang.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = tblBarang.getSelectedRow();
                if (selectedRow != -1) {
                    // Ambil data dari baris yang dipilih
                    String nama = (String) tblBarang.getValueAt(selectedRow, 0);
                    String kategori = (String) tblBarang.getValueAt(selectedRow, 1);
                    int jumlah = (int) tblBarang.getValueAt(selectedRow, 2);
                    double harga = (double) tblBarang.getValueAt(selectedRow, 3);
                    
                    // Isi JTextField dengan data yang dipilih
                    txtNama.setText(nama);
                    txtKategori.setText(kategori);
                    txtJumlah.setText(String.valueOf(jumlah));
                    txtHarga.setText(String.valueOf(harga));
                }
            }
        });
    }

    
    private void exportData(String selectedFormat) {
    switch (selectedFormat) {
        case "TXT (CSV)":
            exportToTxt();
            break;
        case "PDF":
            exportToPdf();
            break;
        case "Excel (.xls)":
            exportToXls();
            break;
        case "JSON":
            exportToJson();
            break;
        default:
            JOptionPane.showMessageDialog(this, "Please select a format to export.");
    }
}
    private void exportToTxt() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("data_barang.txt"))) {
        DefaultTableModel model = (DefaultTableModel) tblBarang.getModel();

        // Menulis header
        for (int i = 0; i < model.getColumnCount(); i++) {
            writer.write(model.getColumnName(i) + ",");
        }
        writer.newLine();

        // Menulis data setiap baris
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                writer.write(model.getValueAt(i, j) + ",");
            }
            writer.newLine();
        }

        JOptionPane.showMessageDialog(this, "Data berhasil diekspor ke data_barang.txt!");
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mengekspor data: " + e.getMessage());
    }
}
    

private void exportToPdf() {
    Document document = new Document();
    try {
        PdfWriter.getInstance(document, new FileOutputStream("data_barang.pdf"));
        document.open();
        // Membuat model untuk JTable




if (tblBarang == null || tblBarang.getModel().getRowCount() == 0) {
    JOptionPane.showMessageDialog(this, "Tabel kosong, tidak ada data untuk diekspor.");
    return; }
    
        // Menambahkan judul dengan font yang lebih baik
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK);
        Paragraph title = new Paragraph("Laporan Data Barang", font);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph("\n")); // Baris kosong untuk pemisah

        // Membuat tabel dengan 3 kolom
        PdfPTable table = new PdfPTable(4); // 3 kolom sesuai jumlah data
        table.setWidthPercentage(100); // Lebar tabel 100%

        // Menambahkan header ke tabel
        table.addCell("Nama");
        table.addCell("Kategori");
        table.addCell("Jumlah");
        table.addCell("Harga");

        // Menambahkan data ke tabel
        DefaultTableModel model = (DefaultTableModel) tblBarang.getModel();
            
        for (int i = 0; i < model.getRowCount(); i++) {
            table.addCell(model.getValueAt(i, 0).toString());
            table.addCell(model.getValueAt(i, 1).toString());
            table.addCell(model.getValueAt(i, 2).toString());
            table.addCell(model.getValueAt(i, 3).toString());
        }

        // Menambahkan tabel ke dokumen
        document.add(table);
        document.close();

        JOptionPane.showMessageDialog(this, "Data berhasil diekspor ke data_barang.pdf!");
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mengekspor data ke PDF.");
    }
}
    private void exportToXls() {
    try (Workbook workbook = new HSSFWorkbook()) { // Menggunakan XSSFWorkbook untuk .xlsx
        Sheet sheet = workbook.createSheet("Data Barang");

        // Menulis header
        DefaultTableModel model = (DefaultTableModel) tblBarang.getModel();
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < model.getColumnCount(); i++) {
            headerRow.createCell(i).setCellValue(model.getColumnName(i));
        }

        // Menulis data setiap baris
        for (int i = 0; i < model.getRowCount(); i++) {
            Row row = sheet.createRow(i + 1); // Baris data mulai dari baris 1
            for (int j = 0; j < model.getColumnCount(); j++) {
                row.createCell(j).setCellValue(model.getValueAt(i, j).toString());
            }
        }

        // Menyimpan file Excel
        try (FileOutputStream fileOut = new FileOutputStream("data_barang.xlsx")) {
            workbook.write(fileOut);
            JOptionPane.showMessageDialog(this, "Data berhasil diekspor ke data_barang.xlsx!");
        }

    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mengekspor data ke Excel.");
    }
}
    private void exportToJson() {
    JSONArray jsonArray = new JSONArray();
    try {
        // Mengambil data dari tabel
        DefaultTableModel model = (DefaultTableModel) tblBarang.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            JSONObject obj = new JSONObject();
            obj.put("Nama", model.getValueAt(i, 0));
            obj.put("Kategori", model.getValueAt(i, 1));
            obj.put("Jumlah", model.getValueAt(i, 2));
            obj.put("Harga", model.getValueAt(i, 3));
            jsonArray.put(obj);
        }

        // Menyimpan data ke file JSON
        try (FileWriter file = new FileWriter("data_barang.json")) {
            file.write(jsonArray.toString(4)); // Pretty print dengan indentasi 4
            JOptionPane.showMessageDialog(this, "Data berhasil diekspor ke data_barang.json!");
        }

    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mengekspor data ke JSON.");
    }
}
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBarang = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        txtKategori = new javax.swing.JTextField();
        txtJumlah = new javax.swing.JTextField();
        txtHarga = new javax.swing.JTextField();
        btnTambah = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        CBX = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        btnEksport = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Aplikasi Inventaris Barang");

        jPanel1.setBackground(new java.awt.Color(204, 255, 102));

        tblBarang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nama Barang", "Kategori", "Jumlah", "Harga"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBarangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblBarang);

        jLabel1.setText("Nama Barang");

        jLabel2.setText("Kategori");

        jLabel3.setText("Jumlah Barang");

        jLabel4.setText("Harga");

        txtNama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaActionPerformed(evt);
            }
        });

        btnTambah.setBackground(new java.awt.Color(102, 255, 204));
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnEdit.setBackground(new java.awt.Color(153, 255, 204));
        btnEdit.setText("Edit");
        btnEdit.setToolTipText("");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnHapus.setBackground(new java.awt.Color(102, 255, 204));
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        CBX.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih", "TXT (CSV)", "PDF", "Excel (.xls)", "JSON" }));
        CBX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBXActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(153, 255, 204));
        jButton1.setText("Impor");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnEksport.setBackground(new java.awt.Color(153, 255, 204));
        btnEksport.setText("Ekspor");
        btnEksport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEksportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(42, 42, 42)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNama, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                            .addComponent(txtKategori)
                            .addComponent(txtJumlah)
                            .addComponent(txtHarga))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(btnEdit)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnEksport))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(btnTambah)
                                    .addGap(49, 49, 49)
                                    .addComponent(CBX, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton1)))
                            .addComponent(btnHapus))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTambah)
                    .addComponent(CBX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(btnEdit)
                    .addComponent(btnEksport))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(btnHapus))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setBackground(new java.awt.Color(255, 204, 204));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel5.setText("Aplikasi Inventaris Barang");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(263, 263, 263)
                .addComponent(jLabel5)
                .addContainerGap(278, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel5)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
      btnTambah.addActionListener(e -> btnTambahActionPerformed(e));       
    // Validasi: Pastikan semua field diisi
    if (txtNama.getText().isEmpty() || txtKategori.getText().isEmpty() || 
        txtJumlah.getText().isEmpty() || txtHarga.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        // Ambil data dari JTextField
        String nama = txtNama.getText();
        String kategori = txtKategori.getText();
        int jumlah = Integer.parseInt(txtJumlah.getText());  // Pastikan input angka

        // Format harga dengan DecimalFormat
        String hargaString = txtHarga.getText().replace(",", "."); // Ganti koma menjadi titik
        double harga = Double.parseDouble(hargaString);  // Mengonversi string ke angka

        // Format harga menjadi Rupiah
        

        // Tambahkan data ke tabel
        DefaultTableModel model = (DefaultTableModel) tblBarang.getModel();
        model.addRow(new Object[]{nama, kategori, jumlah, harga});

        // Kosongkan kembali JTextField
        txtNama.setText("");
        txtKategori.setText("");
        txtJumlah.setText("");
        txtHarga.setText("");

        // Fokuskan kembali ke JTextField pertama (txtNama)
        txtNama.requestFocus();

        JOptionPane.showMessageDialog(this, "Barang berhasil ditambahkan!");
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Jumlah dan harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
    }


    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
                                        
    // Periksa apakah ada baris yang dipilih
   
        // Periksa apakah ada baris yang dipilih
        int selectedRow = tblBarang.getSelectedRow();
        
        if (selectedRow == -1) {  // Jika tidak ada baris yang dipilih
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diedit!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Ambil data dari JTextField
        String nama = txtNama.getText();
        String kategori = txtKategori.getText();
        
        // Pastikan Jumlah dan Harga valid
        int jumlah;
        double harga;
        try {
            jumlah = Integer.parseInt(txtJumlah.getText());
            harga = Double.parseDouble(txtHarga.getText());
        } catch (NumberFormatException e1) {
            JOptionPane.showMessageDialog(this, "Jumlah dan harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

    // Update data pada JTable (perbarui baris yang dipilih)
    DefaultTableModel model = (DefaultTableModel) tblBarang.getModel();
    model.setValueAt(nama, selectedRow, 0);  // Update kolom Nama
    model.setValueAt(kategori, selectedRow, 1);  // Update kolom Kategori
    model.setValueAt(jumlah, selectedRow, 2);  // Update kolom Jumlah
    model.setValueAt(harga, selectedRow, 3);  // Update kolom Harga

    // Kosongkan JTextField setelah edit
    txtNama.setText("");
    txtKategori.setText("");
    txtJumlah.setText("");
    txtHarga.setText("");

    JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");


    }//GEN-LAST:event_btnEditActionPerformed

    private void tblBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBarangMouseClicked
   if (tblBarang == null) {
        JOptionPane.showMessageDialog(this, "Tabel belum diinisialisasi.");
        return;  // Keluar jika tabel belum diinisialisasi
    }

    DefaultTableModel model = (DefaultTableModel) tblBarang.getModel();
    if (model.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "Tidak ada data di tabel.");
        return;
    }  // Ambil model tabel

    // Pastikan ada baris yang dipilih dan data di tabel
    int row = tblBarang.getSelectedRow();  // Mendapatkan baris yang dipilih
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Pilih baris data terlebih dahulu.");
        return;  // Keluar jika tidak ada baris yang dipilih
    }


    // Ambil data dari baris yang dipilih
    String nama = model.getValueAt(row, 0).toString();
    String kategori = model.getValueAt(row, 1).toString();
    String jumlah = model.getValueAt(row, 2).toString();
    String harga = model.getValueAt(row, 3).toString();

    // Menampilkan data yang dipilih
    System.out.println("Nama: " + nama + ", Kategori: " + kategori + "Jumlah: "+ jumlah +" Harga: " + harga);

    
        
    }//GEN-LAST:event_tblBarangMouseClicked

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
                                          // Periksa apakah ada baris yang dipilih
    int selectedRow = tblBarang.getSelectedRow();
    
    if (selectedRow == -1) {  // Jika tidak ada baris yang dipilih
        JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Tampilkan konfirmasi sebelum menghapus
    int confirmation = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus data ini?", 
                                                      "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
    if (confirmation == JOptionPane.YES_OPTION) {
        // Hapus baris yang dipilih dari model tabel
        DefaultTableModel model = (DefaultTableModel) tblBarang.getModel();
        model.removeRow(selectedRow);

        // Kosongkan semua JTextField
        txtNama.setText("");
        txtKategori.setText("");
        txtJumlah.setText("");
        txtHarga.setText("");

        // Fokuskan kembali ke JTextField pertama (txtNama)
        txtNama.requestFocus();

        JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
    }


    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnEksportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEksportActionPerformed
        String selectedFormat = (String) CBX.getSelectedItem();  // Mendapatkan pilihan dari ComboBox

    if (selectedFormat == null || selectedFormat.equals("Select Format")) {
        // Jika tidak ada pilihan yang dipilih atau "Select Format" masih dipilih
        JOptionPane.showMessageDialog(this, "Please select a format to export.");
    } else {
        // Menjalankan fungsi exportData jika ada pilihan yang valid
        exportData(selectedFormat);
    }
    }//GEN-LAST:event_btnEksportActionPerformed

    private void CBXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBXActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CBXActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AplikasiInventarisBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AplikasiInventarisBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AplikasiInventarisBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AplikasiInventarisBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AplikasiInventarisBarang().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> CBX;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnEksport;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblBarang;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtJumlah;
    private javax.swing.JTextField txtKategori;
    private javax.swing.JTextField txtNama;
    // End of variables declaration//GEN-END:variables
}
