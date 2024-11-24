
 Irfazi - 2210010277 - UTS
 
# README - **Aplikasi Inventaris Barang**
Irfazi - 2210010277 - UTS

## Deskripsi
Aplikasi Inventaris Barang adalah aplikasi berbasis Java yang menggunakan **Swing** untuk mengelola data inventaris barang. Aplikasi ini memungkinkan pengguna untuk menambahkan, mengedit, menghapus, dan mengekspor data ke berbagai format seperti **TXT (CSV)**, **PDF**, **Excel**, dan **JSON**. Data barang dikelola menggunakan **JTable** dengan dukungan interaksi langsung.

---

## Fitur Utama
1. **Tambah Barang**
   - Fungsi: Menambahkan data baru ke tabel.
   - Cara Kerja:
     - Data diambil dari **JTextField** yang disediakan.
     - Field `Nama`, `Kategori`, `Jumlah`, dan `Harga` harus diisi.
     - Validasi memastikan bahwa **Jumlah** dan **Harga** adalah angka yang valid.
   - Potongan kode:
     ```java
     private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {                                          
         if (txtNama.getText().isEmpty() || txtKategori.getText().isEmpty() || 
             txtJumlah.getText().isEmpty() || txtHarga.getText().isEmpty()) {
             JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
             return;
         }

         try {
             String nama = txtNama.getText();
             String kategori = txtKategori.getText();
             int jumlah = Integer.parseInt(txtJumlah.getText());
             double harga = Double.parseDouble(txtHarga.getText());

             DefaultTableModel model = (DefaultTableModel) tblBarang.getModel();
             model.addRow(new Object[]{nama, kategori, jumlah, harga});

             txtNama.setText("");
             txtKategori.setText("");
             txtJumlah.setText("");
             txtHarga.setText("");
             txtNama.requestFocus();

             JOptionPane.showMessageDialog(this, "Barang berhasil ditambahkan!");
         } catch (NumberFormatException e) {
             JOptionPane.showMessageDialog(this, "Jumlah dan harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
         }
     }
     ```

2. **Edit Barang**
   - Fungsi: Memperbarui data pada baris yang dipilih.
   - Cara Kerja:
     - Data dari baris yang dipilih akan dimuat ke dalam **JTextField**.
     - Setelah data diubah, baris di tabel diperbarui menggunakan data baru.
     - Validasi memastikan **Jumlah** dan **Harga** adalah angka yang valid.
   - Potongan kode:
     ```java
     private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {                                        
         int selectedRow = tblBarang.getSelectedRow();
         if (selectedRow == -1) {
             JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diedit!", "Error", JOptionPane.ERROR_MESSAGE);
             return;
         }

         try {
             String nama = txtNama.getText();
             String kategori = txtKategori.getText();
             int jumlah = Integer.parseInt(txtJumlah.getText());
             double harga = Double.parseDouble(txtHarga.getText());

             DefaultTableModel model = (DefaultTableModel) tblBarang.getModel();
             model.setValueAt(nama, selectedRow, 0);
             model.setValueAt(kategori, selectedRow, 1);
             model.setValueAt(jumlah, selectedRow, 2);
             model.setValueAt(harga, selectedRow, 3);

             txtNama.setText("");
             txtKategori.setText("");
             txtJumlah.setText("");
             txtHarga.setText("");

             JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");
         } catch (NumberFormatException e) {
             JOptionPane.showMessageDialog(this, "Jumlah dan harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
         }
     }
     ```

3. **Hapus Barang**
   - Fungsi: Menghapus baris data yang dipilih.
   - Cara Kerja:
     - Data yang dipilih dihapus dari tabel setelah konfirmasi.
   - Potongan kode:
     ```java
     private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {                                         
         int selectedRow = tblBarang.getSelectedRow();
         if (selectedRow == -1) {
             JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
             return;
         }

         int confirmation = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus data ini?", 
                                                           "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
         if (confirmation == JOptionPane.YES_OPTION) {
             DefaultTableModel model = (DefaultTableModel) tblBarang.getModel();
             model.removeRow(selectedRow);

             txtNama.setText("");
             txtKategori.setText("");
             txtJumlah.setText("");
             txtHarga.setText("");

             JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
         }
     }
     ```

4. **Tabel Interaktif**
   - **JTable** mendukung pemilihan baris. Saat sebuah baris dipilih, data akan otomatis dimasukkan ke dalam **JTextField** untuk diedit atau dihapus.
   - Potongan kode:
     ```java
     private void tblBarangMouseClicked(java.awt.event.MouseEvent evt) {                                       
         int row = tblBarang.getSelectedRow();
         if (row != -1) {
             DefaultTableModel model = (DefaultTableModel) tblBarang.getModel();
             txtNama.setText(model.getValueAt(row, 0).toString());
             txtKategori.setText(model.getValueAt(row, 1).toString());
             txtJumlah.setText(model.getValueAt(row, 2).toString());
             txtHarga.setText(model.getValueAt(row, 3).toString());
         }
     }
     ```

---

## Teknologi yang Digunakan
- **Java Swing:** Antarmuka pengguna.
- **Apache POI:** Ekspor ke format Excel.
- **iText:** Ekspor ke format PDF.
- **JSON:** Ekspor ke format JSON.
- **Java File I/O:** Ekspor ke format TXT.

---


   ```

---


