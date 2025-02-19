/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgRujuk.java
 *
 * Created on 31 Mei 10, 20:19:56
 */

package rekammedis;

import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import kepegawaian.DlgCariDokter;
import laporan.DlgBerkasRawat;



/**
 *
 * @author perpustakaan
 */
public final class RMDataMedicalReport extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps, ps2;
    private ResultSet rs, rs2;
    private int i=0;    
    private DlgCariDokter dokter=new DlgCariDokter(null,false);
    
    private String tanggal="",finger="", tglkeluar="", kebutuhanString="";
    
    List<Integer> kebutuhan = new ArrayList<>();
    
    /** Creates new form DlgRujuk
     * @param parent
     * @param modal */
    public RMDataMedicalReport(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        tabMode=new DefaultTableModel(null,new Object[]{
            "No.Rawat","No.RM","Nama Pasien","Kode Dokter","Dokter Penanggung Jawab",
            "Kode Bayar","Cara Bayar",
            "Tgl.Masuk","Tgl.Keluar","No Telp",
            "Keluhan Utama","Pemeriksaan Fisik","Pemeriksaan Penunjang",
            "Diagnosa Awal","Diagnosa Banding","Terapi","Saran Dokter",
            "Evakuasi","SaranEvakuasi","Perjalanan","Pendamping","Kebutuhan"
        }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbObat.setModel(tabMode);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 22; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(105);
            }else if(i==1){
                column.setPreferredWidth(65);
            }else if(i==2){
                column.setPreferredWidth(150);
            }else if(i==3){
                column.setPreferredWidth(65);
            }else if(i==4){
                column.setPreferredWidth(150);
            }else if(i==5){
                column.setPreferredWidth(65);
            }else if(i==6){
                column.setPreferredWidth(80);
            }else if(i==7){
                column.setPreferredWidth(65);
            }else if(i==8){
                column.setPreferredWidth(65);
            }else if(i==9){
                column.setPreferredWidth(75);
            }else if(i==10){
                column.setPreferredWidth(250);
            }else if(i==11){
                column.setPreferredWidth(250);
            }else if(i==12){
                column.setPreferredWidth(250);
            }else if(i==13){
                column.setPreferredWidth(250);
            }else if(i==14){
                column.setPreferredWidth(250);
            }else if(i==15){
                column.setPreferredWidth(250);
            }else if(i==16){
                column.setPreferredWidth(250);
            }else if(i==17){
                column.setPreferredWidth(60);
            }else if(i==18){
                column.setPreferredWidth(65);
            }else if(i==19){
                column.setPreferredWidth(60);
            }else if(i==20){
                column.setPreferredWidth(200);
            }else if(i==21){
                column.setPreferredWidth(200);
            }else{
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());

        TNoRw.setDocument(new batasInput((byte)17).getKata(TNoRw));
        
        TCari.setDocument(new batasInput((int)100).getKata(TCari));
        
        if(koneksiDB.CARICEPAT().equals("aktif")){
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
            });
        }
        
        dokter.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(dokter.getTable().getSelectedRow()!= -1){
                    KodeDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                    NamaDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                    KodeDokter.requestFocus();
                }
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        ChkInput.setSelected(false);
        isForm();
      
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        MnLaporanResume = new javax.swing.JMenuItem();
        ppBerkasDigital = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbObat = new widget.Table();
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnEdit = new widget.Button();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        BtnKeluar = new widget.Button();
        panelGlass9 = new widget.panelisi();
        jLabel19 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel21 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        PanelInput = new javax.swing.JPanel();
        ChkInput = new widget.CekBox();
        scrollInput = new widget.ScrollPane();
        FormInput = new widget.PanelBiasa();
        TNoRw = new widget.TextBox();
        TPasien = new widget.TextBox();
        TNoRM = new widget.TextBox();
        jLabel5 = new widget.Label();
        label14 = new widget.Label();
        KodeDokter = new widget.TextBox();
        NamaDokter = new widget.TextBox();
        BtnDokter = new widget.Button();
        jLabel22 = new widget.Label();
        Telepon = new widget.TextBox();
        jLabel34 = new widget.Label();
        Lahir = new widget.TextBox();
        jLabel25 = new widget.Label();
        KdPj = new widget.TextBox();
        CaraBayar = new widget.TextBox();
        jLabel35 = new widget.Label();
        Masuk = new widget.TextBox();
        jLabel36 = new widget.Label();
        Keluar = new widget.TextBox();
        jLabel38 = new widget.Label();
        scrollPane12 = new widget.ScrollPane();
        KeluhanUtama = new widget.TextArea();
        jLabel39 = new widget.Label();
        scrollPane16 = new widget.ScrollPane();
        PemeriksaanFisik = new widget.TextArea();
        jLabel40 = new widget.Label();
        scrollPane17 = new widget.ScrollPane();
        PemeriksaanPenunjang = new widget.TextArea();
        jLabel41 = new widget.Label();
        scrollPane18 = new widget.ScrollPane();
        Diagnosa = new widget.TextArea();
        jLabel42 = new widget.Label();
        scrollPane19 = new widget.ScrollPane();
        DiagnosaBanding = new widget.TextArea();
        jLabel43 = new widget.Label();
        scrollPane20 = new widget.ScrollPane();
        Terapi = new widget.TextArea();
        jLabel44 = new widget.Label();
        scrollPane21 = new widget.ScrollPane();
        SaranDokter = new widget.TextArea();
        jLabel45 = new widget.Label();
        Evakuasi = new widget.ComboBox();
        jLabel50 = new widget.Label();
        SaranEvakuasi = new widget.ComboBox();
        jLabel51 = new widget.Label();
        Perjalanan = new widget.ComboBox();
        jLabel52 = new widget.Label();
        Pendamping = new widget.ComboBox();
        jLabel53 = new widget.Label();
        OrdinarySeat = new javax.swing.JCheckBox();
        WheelChair = new javax.swing.JCheckBox();
        Stretcher = new javax.swing.JCheckBox();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnLaporanResume.setBackground(new java.awt.Color(255, 255, 254));
        MnLaporanResume.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnLaporanResume.setForeground(new java.awt.Color(50, 50, 50));
        MnLaporanResume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnLaporanResume.setText("Laporan Resume Pasien");
        MnLaporanResume.setName("MnLaporanResume"); // NOI18N
        MnLaporanResume.setPreferredSize(new java.awt.Dimension(250, 26));
        MnLaporanResume.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnLaporanResumeActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnLaporanResume);

        ppBerkasDigital.setBackground(new java.awt.Color(255, 255, 254));
        ppBerkasDigital.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppBerkasDigital.setForeground(new java.awt.Color(50, 50, 50));
        ppBerkasDigital.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppBerkasDigital.setText("Berkas Digital Perawatan");
        ppBerkasDigital.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppBerkasDigital.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppBerkasDigital.setName("ppBerkasDigital"); // NOI18N
        ppBerkasDigital.setPreferredSize(new java.awt.Dimension(250, 26));
        ppBerkasDigital.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppBerkasDigitalBtnPrintActionPerformed(evt);
            }
        });
        jPopupMenu1.add(ppBerkasDigital);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Data Medical Report ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);
        Scroll.setPreferredSize(new java.awt.Dimension(452, 200));

        tbObat.setAutoCreateRowSorter(true);
        tbObat.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbObat.setComponentPopupMenu(jPopupMenu1);
        tbObat.setName("tbObat"); // NOI18N
        tbObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbObatMouseClicked(evt);
            }
        });
        tbObat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbObatKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tbObat);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setText("Simpan");
        BtnSimpan.setToolTipText("Alt+S");
        BtnSimpan.setName("BtnSimpan"); // NOI18N
        BtnSimpan.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanActionPerformed(evt);
            }
        });
        BtnSimpan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSimpanKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnSimpan);

        BtnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Cancel-2-16x16.png"))); // NOI18N
        BtnBatal.setMnemonic('B');
        BtnBatal.setText("Baru");
        BtnBatal.setToolTipText("Alt+B");
        BtnBatal.setName("BtnBatal"); // NOI18N
        BtnBatal.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBatalActionPerformed(evt);
            }
        });
        BtnBatal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnBatalKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnBatal);

        BtnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus.setMnemonic('H');
        BtnHapus.setText("Hapus");
        BtnHapus.setToolTipText("Alt+H");
        BtnHapus.setName("BtnHapus"); // NOI18N
        BtnHapus.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusActionPerformed(evt);
            }
        });
        BtnHapus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnHapusKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnHapus);

        BtnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/inventaris.png"))); // NOI18N
        BtnEdit.setMnemonic('G');
        BtnEdit.setText("Ganti");
        BtnEdit.setToolTipText("Alt+G");
        BtnEdit.setName("BtnEdit"); // NOI18N
        BtnEdit.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEditActionPerformed(evt);
            }
        });
        BtnEdit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnEditKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnEdit);

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass8.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass8.add(LCount);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        BtnKeluar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluarKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnKeluar);

        jPanel3.add(panelGlass8, java.awt.BorderLayout.CENTER);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel19.setText("Tgl.Rawat :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(67, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "10-09-2024" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari1);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("s.d.");
        jLabel21.setName("jLabel21"); // NOI18N
        jLabel21.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass9.add(jLabel21);

        DTPCari2.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "10-09-2024" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari2);

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(310, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass9.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('3');
        BtnCari.setToolTipText("Alt+3");
        BtnCari.setName("BtnCari"); // NOI18N
        BtnCari.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariActionPerformed(evt);
            }
        });
        BtnCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariKeyPressed(evt);
            }
        });
        panelGlass9.add(BtnCari);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllActionPerformed(evt);
            }
        });
        BtnAll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllKeyPressed(evt);
            }
        });
        panelGlass9.add(BtnAll);

        jPanel3.add(panelGlass9, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setPreferredSize(new java.awt.Dimension(192, 448));
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        ChkInput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setMnemonic('I');
        ChkInput.setText(".: Input Data");
        ChkInput.setToolTipText("Alt+I");
        ChkInput.setBorderPainted(true);
        ChkInput.setBorderPaintedFlat(true);
        ChkInput.setFocusable(false);
        ChkInput.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput.setName("ChkInput"); // NOI18N
        ChkInput.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInputActionPerformed(evt);
            }
        });
        PanelInput.add(ChkInput, java.awt.BorderLayout.PAGE_END);

        scrollInput.setName("scrollInput"); // NOI18N

        FormInput.setBackground(new java.awt.Color(250, 255, 245));
        FormInput.setBorder(null);
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 702));
        FormInput.setLayout(null);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        TNoRw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwKeyPressed(evt);
            }
        });
        FormInput.add(TNoRw);
        TNoRw.setBounds(104, 10, 141, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        TPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TPasienKeyPressed(evt);
            }
        });
        FormInput.add(TPasien);
        TPasien.setBounds(361, 10, 424, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        TNoRM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRMKeyPressed(evt);
            }
        });
        FormInput.add(TNoRM);
        TNoRM.setBounds(247, 10, 112, 23);

        jLabel5.setText("No.Rawat :");
        jLabel5.setName("jLabel5"); // NOI18N
        FormInput.add(jLabel5);
        jLabel5.setBounds(0, 10, 100, 23);

        label14.setText("Dokter P.J. :");
        label14.setName("label14"); // NOI18N
        label14.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label14);
        label14.setBounds(0, 40, 100, 23);

        KodeDokter.setEditable(false);
        KodeDokter.setName("KodeDokter"); // NOI18N
        KodeDokter.setPreferredSize(new java.awt.Dimension(80, 23));
        KodeDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KodeDokterKeyPressed(evt);
            }
        });
        FormInput.add(KodeDokter);
        KodeDokter.setBounds(104, 40, 141, 23);

        NamaDokter.setEditable(false);
        NamaDokter.setName("NamaDokter"); // NOI18N
        NamaDokter.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(NamaDokter);
        NamaDokter.setBounds(247, 40, 270, 23);

        BtnDokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnDokter.setMnemonic('2');
        BtnDokter.setToolTipText("Alt+2");
        BtnDokter.setName("BtnDokter"); // NOI18N
        BtnDokter.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDokterActionPerformed(evt);
            }
        });
        BtnDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnDokterKeyPressed(evt);
            }
        });
        FormInput.add(BtnDokter);
        BtnDokter.setBounds(519, 40, 28, 23);

        jLabel22.setText("Telepon :");
        jLabel22.setName("jLabel22"); // NOI18N
        FormInput.add(jLabel22);
        jLabel22.setBounds(550, 40, 70, 23);

        Telepon.setEditable(false);
        Telepon.setHighlighter(null);
        Telepon.setName("Telepon"); // NOI18N
        FormInput.add(Telepon);
        Telepon.setBounds(625, 40, 160, 23);

        jLabel34.setText("Tanggal Lahir :");
        jLabel34.setName("jLabel34"); // NOI18N
        FormInput.add(jLabel34);
        jLabel34.setBounds(0, 70, 100, 23);

        Lahir.setEditable(false);
        Lahir.setHighlighter(null);
        Lahir.setName("Lahir"); // NOI18N
        FormInput.add(Lahir);
        Lahir.setBounds(110, 70, 90, 23);

        jLabel25.setText("Cara Bayar :");
        jLabel25.setName("jLabel25"); // NOI18N
        FormInput.add(jLabel25);
        jLabel25.setBounds(445, 70, 90, 23);

        KdPj.setEditable(false);
        KdPj.setHighlighter(null);
        KdPj.setName("KdPj"); // NOI18N
        KdPj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KdPjActionPerformed(evt);
            }
        });
        FormInput.add(KdPj);
        KdPj.setBounds(539, 70, 80, 23);

        CaraBayar.setEditable(false);
        CaraBayar.setHighlighter(null);
        CaraBayar.setName("CaraBayar"); // NOI18N
        CaraBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CaraBayarActionPerformed(evt);
            }
        });
        FormInput.add(CaraBayar);
        CaraBayar.setBounds(625, 70, 160, 23);

        jLabel35.setText("Tanggal Masuk :");
        jLabel35.setName("jLabel35"); // NOI18N
        FormInput.add(jLabel35);
        jLabel35.setBounds(0, 100, 100, 23);

        Masuk.setEditable(false);
        Masuk.setHighlighter(null);
        Masuk.setName("Masuk"); // NOI18N
        FormInput.add(Masuk);
        Masuk.setBounds(110, 100, 90, 23);

        jLabel36.setText("Tanggal Keluar :");
        jLabel36.setName("jLabel36"); // NOI18N
        FormInput.add(jLabel36);
        jLabel36.setBounds(450, 100, 90, 23);

        Keluar.setEditable(false);
        Keluar.setHighlighter(null);
        Keluar.setName("Keluar"); // NOI18N
        FormInput.add(Keluar);
        Keluar.setBounds(540, 100, 80, 23);

        jLabel38.setText("Keluhan Utama :");
        jLabel38.setName("jLabel38"); // NOI18N
        FormInput.add(jLabel38);
        jLabel38.setBounds(0, 130, 220, 23);

        scrollPane12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane12.setName("scrollPane12"); // NOI18N

        KeluhanUtama.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        KeluhanUtama.setColumns(20);
        KeluhanUtama.setRows(5);
        KeluhanUtama.setName("KeluhanUtama"); // NOI18N
        KeluhanUtama.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeluhanUtamaKeyPressed(evt);
            }
        });
        scrollPane12.setViewportView(KeluhanUtama);

        FormInput.add(scrollPane12);
        scrollPane12.setBounds(230, 130, 561, 50);

        jLabel39.setText("Pemeriksaan Fisik :");
        jLabel39.setName("jLabel39"); // NOI18N
        FormInput.add(jLabel39);
        jLabel39.setBounds(0, 190, 220, 23);

        scrollPane16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane16.setName("scrollPane16"); // NOI18N

        PemeriksaanFisik.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        PemeriksaanFisik.setColumns(20);
        PemeriksaanFisik.setRows(5);
        PemeriksaanFisik.setName("PemeriksaanFisik"); // NOI18N
        PemeriksaanFisik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PemeriksaanFisikKeyPressed(evt);
            }
        });
        scrollPane16.setViewportView(PemeriksaanFisik);

        FormInput.add(scrollPane16);
        scrollPane16.setBounds(230, 190, 561, 50);

        jLabel40.setText("Pemeriksaan Penunjang :");
        jLabel40.setName("jLabel40"); // NOI18N
        FormInput.add(jLabel40);
        jLabel40.setBounds(0, 250, 220, 23);

        scrollPane17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane17.setName("scrollPane17"); // NOI18N

        PemeriksaanPenunjang.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        PemeriksaanPenunjang.setColumns(20);
        PemeriksaanPenunjang.setRows(5);
        PemeriksaanPenunjang.setName("PemeriksaanPenunjang"); // NOI18N
        PemeriksaanPenunjang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PemeriksaanPenunjangKeyPressed(evt);
            }
        });
        scrollPane17.setViewportView(PemeriksaanPenunjang);

        FormInput.add(scrollPane17);
        scrollPane17.setBounds(230, 250, 561, 50);

        jLabel41.setText("Diagnosa :");
        jLabel41.setName("jLabel41"); // NOI18N
        FormInput.add(jLabel41);
        jLabel41.setBounds(0, 310, 220, 23);

        scrollPane18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane18.setName("scrollPane18"); // NOI18N

        Diagnosa.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Diagnosa.setColumns(20);
        Diagnosa.setRows(5);
        Diagnosa.setName("Diagnosa"); // NOI18N
        Diagnosa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DiagnosaKeyPressed(evt);
            }
        });
        scrollPane18.setViewportView(Diagnosa);

        FormInput.add(scrollPane18);
        scrollPane18.setBounds(230, 310, 561, 50);

        jLabel42.setText("Diagnosa Banding :");
        jLabel42.setName("jLabel42"); // NOI18N
        FormInput.add(jLabel42);
        jLabel42.setBounds(0, 370, 220, 23);

        scrollPane19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane19.setName("scrollPane19"); // NOI18N

        DiagnosaBanding.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        DiagnosaBanding.setColumns(20);
        DiagnosaBanding.setRows(5);
        DiagnosaBanding.setName("DiagnosaBanding"); // NOI18N
        DiagnosaBanding.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DiagnosaBandingKeyPressed(evt);
            }
        });
        scrollPane19.setViewportView(DiagnosaBanding);

        FormInput.add(scrollPane19);
        scrollPane19.setBounds(230, 370, 561, 50);

        jLabel43.setText("Terapi :");
        jLabel43.setName("jLabel43"); // NOI18N
        FormInput.add(jLabel43);
        jLabel43.setBounds(0, 430, 220, 23);

        scrollPane20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane20.setName("scrollPane20"); // NOI18N

        Terapi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Terapi.setColumns(20);
        Terapi.setRows(5);
        Terapi.setName("Terapi"); // NOI18N
        Terapi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TerapiKeyPressed(evt);
            }
        });
        scrollPane20.setViewportView(Terapi);

        FormInput.add(scrollPane20);
        scrollPane20.setBounds(230, 430, 561, 50);

        jLabel44.setText("Saran Dokter :");
        jLabel44.setName("jLabel44"); // NOI18N
        FormInput.add(jLabel44);
        jLabel44.setBounds(0, 490, 220, 23);

        scrollPane21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane21.setName("scrollPane21"); // NOI18N

        SaranDokter.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        SaranDokter.setColumns(20);
        SaranDokter.setRows(5);
        SaranDokter.setName("SaranDokter"); // NOI18N
        SaranDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SaranDokterKeyPressed(evt);
            }
        });
        scrollPane21.setViewportView(SaranDokter);

        FormInput.add(scrollPane21);
        scrollPane21.setBounds(230, 490, 561, 50);

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel45.setText("Meminta dievakuasi                  :");
        jLabel45.setName("jLabel45"); // NOI18N
        FormInput.add(jLabel45);
        jLabel45.setBounds(70, 550, 230, 23);

        Evakuasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "Yes", "No" }));
        Evakuasi.setName("Evakuasi"); // NOI18N
        Evakuasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EvakuasiActionPerformed(evt);
            }
        });
        Evakuasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EvakuasiKeyPressed(evt);
            }
        });
        FormInput.add(Evakuasi);
        Evakuasi.setBounds(540, 550, 250, 23);

        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel50.setText("Menyarankan dievakuasi dengan indikasi medis :");
        jLabel50.setName("jLabel50"); // NOI18N
        FormInput.add(jLabel50);
        jLabel50.setBounds(70, 580, 240, 23);

        SaranEvakuasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "Yes", "No" }));
        SaranEvakuasi.setName("SaranEvakuasi"); // NOI18N
        SaranEvakuasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaranEvakuasiActionPerformed(evt);
            }
        });
        SaranEvakuasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SaranEvakuasiKeyPressed(evt);
            }
        });
        FormInput.add(SaranEvakuasi);
        SaranEvakuasi.setBounds(540, 580, 250, 23);

        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel51.setText("Dapat melakukan perjalanan     :");
        jLabel51.setName("jLabel51"); // NOI18N
        FormInput.add(jLabel51);
        jLabel51.setBounds(70, 610, 240, 23);

        Perjalanan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "Yes", "No" }));
        Perjalanan.setName("Perjalanan"); // NOI18N
        Perjalanan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PerjalananKeyPressed(evt);
            }
        });
        FormInput.add(Perjalanan);
        Perjalanan.setBounds(540, 610, 250, 23);

        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel52.setText("Jika Ya, apakah?");
        jLabel52.setName("jLabel52"); // NOI18N
        FormInput.add(jLabel52);
        jLabel52.setBounds(70, 640, 240, 23);

        Pendamping.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "Tanpa Pendamping (Unescorted)", "Tanpa Pendamping Medis (With not medical escorted)", "Dengan Pendamping Medis (Medical escorted)" }));
        Pendamping.setName("Pendamping"); // NOI18N
        Pendamping.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PendampingKeyPressed(evt);
            }
        });
        FormInput.add(Pendamping);
        Pendamping.setBounds(540, 640, 250, 23);

        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel53.setText("Pasien membutuhkan                 :");
        jLabel53.setName("jLabel53"); // NOI18N
        FormInput.add(jLabel53);
        jLabel53.setBounds(70, 670, 160, 23);

        OrdinarySeat.setBackground(new java.awt.Color(255, 255, 255));
        OrdinarySeat.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        OrdinarySeat.setText("Ordinary Seat");
        OrdinarySeat.setName("OrdinarySeat"); // NOI18N
        OrdinarySeat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OrdinarySeatActionPerformed(evt);
            }
        });
        FormInput.add(OrdinarySeat);
        OrdinarySeat.setBounds(230, 670, 150, 20);

        WheelChair.setBackground(new java.awt.Color(255, 255, 255));
        WheelChair.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        WheelChair.setText("Wheel Chair Assistance");
        WheelChair.setName("WheelChair"); // NOI18N
        WheelChair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WheelChairActionPerformed(evt);
            }
        });
        FormInput.add(WheelChair);
        WheelChair.setBounds(410, 670, 150, 20);

        Stretcher.setBackground(new java.awt.Color(255, 255, 255));
        Stretcher.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        Stretcher.setText("Stretcher Case");
        Stretcher.setName("Stretcher"); // NOI18N
        Stretcher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StretcherActionPerformed(evt);
            }
        });
        FormInput.add(Stretcher);
        Stretcher.setBounds(650, 670, 150, 20);

        scrollInput.setViewportView(FormInput);

        PanelInput.add(scrollInput, java.awt.BorderLayout.CENTER);

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);
        internalFrame1.getAccessibleContext().setAccessibleName("::[ Data Medical Report ]::");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TNoRw.getText().equals("")||TNoRM.getText().equals("")||TPasien.getText().equals("")){
            Valid.textKosong(TNoRw,"Pasien");
        }else if(KodeDokter.getText().equals("")||NamaDokter.getText().equals("")){
            Valid.textKosong(BtnDokter,"Dokter Penanggung Jawab");
        }else if(KeluhanUtama.getText().equals("")){
            Valid.textKosong(KeluhanUtama,"Keluhan utama");
        }else if(PemeriksaanFisik.getText().equals("")){
            Valid.textKosong(PemeriksaanFisik,"Pemeriksaan fisik");
        }else if(PemeriksaanPenunjang.getText().equals("")){
            Valid.textKosong(PemeriksaanPenunjang,"Pemeriksaan penunjang");
        }else if(Diagnosa.getText().equals("")){
            Valid.textKosong(Diagnosa,"Diagnosa");
        }else if(DiagnosaBanding.getText().equals("")){
            Valid.textKosong(DiagnosaBanding,"Diagnosa banding");
        }else if(Terapi.getText().equals("")){
            Valid.textKosong(Terapi,"Terapi");
        }else if(SaranDokter.getText().equals("")){
            Valid.textKosong(SaranDokter,"Saran dokter");
        }else if(Evakuasi.getSelectedItem().equals("Select")){
            Valid.textKosong(Evakuasi,"Evakuasi");
        }else if(SaranEvakuasi.getSelectedItem().equals("Select")){
            Valid.textKosong(SaranEvakuasi,"Saran evakuasi");
        }else if(Perjalanan.getSelectedItem().equals("Select")){
            Valid.textKosong(Perjalanan,"Perjalanan");
        }else if(Pendamping.getSelectedItem().equals("Select")){
            Valid.textKosong(Pendamping,"Pendamping");
        } else{
//            System.out.println(Evakuasi.getSelectedItem());
            String listString = kebutuhan.stream().map(Object::toString)
                        .collect(Collectors.joining(", "));
            kebutuhanString="";
            
            if(Sequel.menyimpantf("tb_medical_report","?,?,?,?,?,?,?,?,?,?,?,?,?,?","No.Rawat",14,new String[]{
                    TNoRw.getText(),KodeDokter.getText(),KeluhanUtama.getText(),PemeriksaanFisik.getText(),PemeriksaanPenunjang.getText(),
                    Diagnosa.getText(),DiagnosaBanding.getText(),Terapi.getText(),SaranDokter.getText(),
                    Evakuasi.getSelectedItem().toString(),SaranEvakuasi.getSelectedItem().toString(),
                    Perjalanan.getSelectedItem().toString(),Pendamping.getSelectedItem().toString(),listString
                })==true){
                if(kebutuhan.contains(1)){
                    kebutuhanString += "Ordinary Seat, ";
                }
                if(kebutuhan.contains(2)){
                    kebutuhanString += "Wheel Chair Assistance, ";
                }
                if(kebutuhan.contains(3)){
                    kebutuhanString += "Stretcher Case ";
                }
                
                    tabMode.addRow(new String[]{
                        TNoRw.getText(),TNoRM.getText(),TPasien.getText(),KodeDokter.getText(),NamaDokter.getText(),
                        KdPj.getText(),CaraBayar.getText(),Masuk.getText(), Keluar.getText(),Telepon.getText(),
                        KeluhanUtama.getText(),PemeriksaanFisik.getText(),PemeriksaanPenunjang.getText(),
                        Diagnosa.getText(),DiagnosaBanding.getText(),Terapi.getText(),SaranDokter.getText(),
                        Evakuasi.getSelectedItem().toString(),SaranEvakuasi.getSelectedItem().toString(),
                        Perjalanan.getSelectedItem().toString(),
                        Pendamping.getSelectedItem().toString(),
                        kebutuhanString
                        
                    });
                    
                    emptTeks();
                    LCount.setText(""+tabMode.getRowCount());
            }
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
//            Valid.pindah(evt,Obat2an,BtnBatal);
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        emptTeks();
        ChkInput.setSelected(true);
        isForm(); 
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            emptTeks();
        }else{Valid.pindah(evt, BtnSimpan, BtnHapus);}
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if(tbObat.getSelectedRow()>-1){
            if(akses.getkode().equals("Admin Utama")){
                hapus();
            }else{
                if(KodeDokter.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(),4).toString())){
                    hapus();
                }else{
                    JOptionPane.showMessageDialog(null,"Hanya bisa dihapus oleh dokter yang bersangkutan..!!");
                }
            }
        }else{
            JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
        }            
            
}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnBatal, BtnEdit);
        }
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
        if(TNoRw.getText().equals("")||TNoRM.getText().equals("")||TPasien.getText().equals("")){
            Valid.textKosong(TNoRw,"Pasien");
        }else if(KodeDokter.getText().equals("")||NamaDokter.getText().equals("")){
            Valid.textKosong(BtnDokter,"Dokter Penanggung Jawab");
        }else if(KeluhanUtama.getText().equals("")){
            Valid.textKosong(KeluhanUtama,"Keluhan utama");
        }else if(PemeriksaanFisik.getText().equals("")){
            Valid.textKosong(PemeriksaanFisik,"Pemeriksaan fisik");
        }else if(PemeriksaanPenunjang.getText().equals("")){
            Valid.textKosong(PemeriksaanPenunjang,"Pemeriksaan penunjang");
        }else if(Diagnosa.getText().equals("")){
            Valid.textKosong(Diagnosa,"Diagnosa");
        }else if(DiagnosaBanding.getText().equals("")){
            Valid.textKosong(DiagnosaBanding,"Diagnosa banding");
        }else if(Terapi.getText().equals("")){
            Valid.textKosong(Terapi,"Terapi");
        }else if(SaranDokter.getText().equals("")){
            Valid.textKosong(SaranDokter,"Saran dokter");
        }else if(Evakuasi.getSelectedItem().equals("Select")){
            Valid.textKosong(Evakuasi,"Evakuasi");
        }else if(SaranEvakuasi.getSelectedItem().equals("Select")){
            Valid.textKosong(SaranEvakuasi,"Saran evakuasi");
        }else if(Perjalanan.getSelectedItem().equals("Select")){
            Valid.textKosong(Perjalanan,"Perjalanan");
        }else if(Pendamping.getSelectedItem().equals("Select")){
            Valid.textKosong(Pendamping,"Pendamping");
        } else{
            if(tbObat.getSelectedRow()>-1){
                if(akses.getkode().equals("Admin Utama")){
                    ganti();
                }else{
                    if(KodeDokter.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(),4).toString())){
                        ganti();
                    }else{
                        JOptionPane.showMessageDialog(null,"Hanya bisa diganti oleh dokter yang bersangkutan..!!");
                    }
                }
            }else{
                JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
            }
        }
}//GEN-LAST:event_BtnEditActionPerformed

    private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnEditActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnEdit);
        }
}//GEN-LAST:event_BtnEditKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dokter.dispose();
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnKeluarActionPerformed(null);
        }else{Valid.pindah(evt,BtnEdit,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }
}//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        tampil();
}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnAll);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        tampil();
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            tampil();
            TCari.setText("");
        }else{
            Valid.pindah(evt, BtnCari, TPasien);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void tbObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
}//GEN-LAST:event_tbObatMouseClicked

    private void tbObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatKeyPressed
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }else if(evt.getKeyCode()==KeyEvent.VK_SPACE){
                try {
                    ChkInput.setSelected(true);
                    isForm(); 
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
}//GEN-LAST:event_tbObatKeyPressed

    private void MnLaporanResumeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnLaporanResumeActionPerformed
        if(tbObat.getSelectedRow()>-1){
            Map<String, Object> param = new HashMap<>();    
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());   
            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
            param.put("norawat",tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
            param.put("kebutuhan",tbObat.getValueAt(tbObat.getSelectedRow(),21).toString());
            
            String dateInString =new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            
            tanggal=dateInString;

            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbObat.getValueAt(tbObat.getSelectedRow(),3).toString());
            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+tbObat.getValueAt(tbObat.getSelectedRow(),4).toString()+"\nID "+(finger.equals("")?tbObat.getValueAt(tbObat.getSelectedRow(),3).toString():finger)+"\n"+tanggal); 
            
            Valid.MyReport("rptMedicalReport.jasper","report","::[ Medical Report ]::",param);
        }
    }//GEN-LAST:event_MnLaporanResumeActionPerformed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void ppBerkasDigitalBtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppBerkasDigitalBtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
            TCari.requestFocus();
        }else{
            if(tbObat.getSelectedRow()>-1){
                if(!tbObat.getValueAt(tbObat.getSelectedRow(),1).toString().equals("")){
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    DlgBerkasRawat berkas=new DlgBerkasRawat(null,true);
                    berkas.setJudul("::[ Berkas Digital Perawatan ]::","berkasrawat/pages");
                    try {
                        if(akses.gethapus_berkas_digital_perawatan()==true){
                            berkas.loadURL("http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/"+"berkasrawat/login2.php?act=login&usere="+koneksiDB.USERHYBRIDWEB()+"&passwordte="+koneksiDB.PASHYBRIDWEB()+"&no_rawat="+tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
                        }else{
                            berkas.loadURL("http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/"+"berkasrawat/login2nonhapus.php?act=login&usere="+koneksiDB.USERHYBRIDWEB()+"&passwordte="+koneksiDB.PASHYBRIDWEB()+"&no_rawat="+tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
                        }   
                    } catch (Exception ex) {
                        System.out.println("Notifikasi : "+ex);
                    }

                    berkas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
                    berkas.setLocationRelativeTo(internalFrame1);
                    berkas.setVisible(true);
                    this.setCursor(Cursor.getDefaultCursor());
                }
            }
        }
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_ppBerkasDigitalBtnPrintActionPerformed

    private void BtnDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDokterKeyPressed
//        Valid.pindah(evt,TCari,Kondisi);
    }//GEN-LAST:event_BtnDokterKeyPressed

    private void BtnDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokterActionPerformed
        dokter.emptTeks();
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_BtnDokterActionPerformed

    private void KodeDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KodeDokterKeyPressed
//        Valid.pindah(evt,TCari,Kondisi);
    }//GEN-LAST:event_KodeDokterKeyPressed

    private void TNoRMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRMKeyPressed
        // Valid.pindah(evt, TNm, BtnSimpan);
    }//GEN-LAST:event_TNoRMKeyPressed

    private void TPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPasienKeyPressed
        Valid.pindah(evt,TCari,BtnSimpan);
    }//GEN-LAST:event_TPasienKeyPressed

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isRawat();
        }else{
            Valid.pindah(evt,TCari,BtnDokter);
        }
    }//GEN-LAST:event_TNoRwKeyPressed

    private void KdPjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KdPjActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KdPjActionPerformed

    private void CaraBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CaraBayarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CaraBayarActionPerformed

    private void KeluhanUtamaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeluhanUtamaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KeluhanUtamaKeyPressed

    private void PemeriksaanFisikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PemeriksaanFisikKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PemeriksaanFisikKeyPressed

    private void PemeriksaanPenunjangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PemeriksaanPenunjangKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PemeriksaanPenunjangKeyPressed

    private void DiagnosaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DiagnosaKeyPressed

    private void DiagnosaBandingKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosaBandingKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DiagnosaBandingKeyPressed

    private void TerapiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TerapiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TerapiKeyPressed

    private void SaranDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SaranDokterKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SaranDokterKeyPressed

    private void EvakuasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EvakuasiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EvakuasiActionPerformed

    private void EvakuasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EvakuasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_EvakuasiKeyPressed

    private void SaranEvakuasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaranEvakuasiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SaranEvakuasiActionPerformed

    private void SaranEvakuasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SaranEvakuasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SaranEvakuasiKeyPressed

    private void PerjalananKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PerjalananKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PerjalananKeyPressed

    private void PendampingKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PendampingKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PendampingKeyPressed

    private void OrdinarySeatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OrdinarySeatActionPerformed
        // TODO add your handling code here:
        if(OrdinarySeat.isSelected()) {
            kebutuhan.add(1);
        } else {
            kebutuhan.remove(Integer.valueOf(1));
        }
//        String listString = kebutuhan.stream().map(Object::toString)
//                        .collect(Collectors.joining(", "));
        
//        JOptionPane.showMessageDialog(null, listString);
    }//GEN-LAST:event_OrdinarySeatActionPerformed

    private void WheelChairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WheelChairActionPerformed
        // TODO add your handling code here:
        if(WheelChair.isSelected()) {
            kebutuhan.add(2);
        } else {
            kebutuhan.remove(Integer.valueOf(2));
        }
//        String listString = kebutuhan.stream().map(Object::toString)
//                        .collect(Collectors.joining(", "));
        
//        JOptionPane.showMessageDialog(null, listString);
    }//GEN-LAST:event_WheelChairActionPerformed

    private void StretcherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StretcherActionPerformed
        // TODO add your handling code here:
        if(Stretcher.isSelected()) {
            kebutuhan.add(3);
        } else {
            kebutuhan.remove(Integer.valueOf(3));
        }
//        String listString = kebutuhan.stream().map(Object::toString)
//                        .collect(Collectors.joining(", "));
        
//        JOptionPane.showMessageDialog(null, listString);
    }//GEN-LAST:event_StretcherActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMDataMedicalReport dialog = new RMDataMedicalReport(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.Button BtnAll;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnDokter;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnSimpan;
    private widget.TextBox CaraBayar;
    private widget.CekBox ChkInput;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.TextArea Diagnosa;
    private widget.TextArea DiagnosaBanding;
    private widget.ComboBox Evakuasi;
    private widget.PanelBiasa FormInput;
    private widget.TextBox KdPj;
    private widget.TextBox Keluar;
    private widget.TextArea KeluhanUtama;
    private widget.TextBox KodeDokter;
    private widget.Label LCount;
    private widget.TextBox Lahir;
    private widget.TextBox Masuk;
    private javax.swing.JMenuItem MnLaporanResume;
    private widget.TextBox NamaDokter;
    private javax.swing.JCheckBox OrdinarySeat;
    private javax.swing.JPanel PanelInput;
    private widget.TextArea PemeriksaanFisik;
    private widget.TextArea PemeriksaanPenunjang;
    private widget.ComboBox Pendamping;
    private widget.ComboBox Perjalanan;
    private widget.TextArea SaranDokter;
    private widget.ComboBox SaranEvakuasi;
    private widget.ScrollPane Scroll;
    private javax.swing.JCheckBox Stretcher;
    private widget.TextBox TCari;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.TextBox Telepon;
    private widget.TextArea Terapi;
    private javax.swing.JCheckBox WheelChair;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel19;
    private widget.Label jLabel21;
    private widget.Label jLabel22;
    private widget.Label jLabel25;
    private widget.Label jLabel34;
    private widget.Label jLabel35;
    private widget.Label jLabel36;
    private widget.Label jLabel38;
    private widget.Label jLabel39;
    private widget.Label jLabel40;
    private widget.Label jLabel41;
    private widget.Label jLabel42;
    private widget.Label jLabel43;
    private widget.Label jLabel44;
    private widget.Label jLabel45;
    private widget.Label jLabel5;
    private widget.Label jLabel50;
    private widget.Label jLabel51;
    private widget.Label jLabel52;
    private widget.Label jLabel53;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.Label label14;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private javax.swing.JMenuItem ppBerkasDigital;
    private widget.ScrollPane scrollInput;
    private widget.ScrollPane scrollPane12;
    private widget.ScrollPane scrollPane16;
    private widget.ScrollPane scrollPane17;
    private widget.ScrollPane scrollPane18;
    private widget.ScrollPane scrollPane19;
    private widget.ScrollPane scrollPane20;
    private widget.ScrollPane scrollPane21;
    private widget.Table tbObat;
    // End of variables declaration//GEN-END:variables

    public void tampil() {
        Valid.tabelKosong(tabMode);
        kebutuhanString= "";
        try{
            ps=koneksi.prepareStatement(
                    "SELECT reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.no_tlp, " +
                    "tb_medical_report.kd_dokter,dokter.nm_dokter, " +
                    "reg_periksa.tgl_registrasi, " +
                    "IF(kamar_inap.tgl_keluar IS NULL OR kamar_inap.tgl_keluar='0000-00-00',CURRENT_DATE(),kamar_inap.tgl_keluar) AS tgl_keluar, " +
                    "tb_medical_report.keluhan_utama,tb_medical_report.pemeriksaan_fisik, " +
                    "tb_medical_report.pemeriksaan_penunjang,tb_medical_report.diagnosa, " +
                    "tb_medical_report.diagnosa_banding,tb_medical_report.terapi, " +
                    "tb_medical_report.saran_dokter,tb_medical_report.evakuasi,tb_medical_report.saran_evakuasi," +
                    "tb_medical_report.perjalanan, " +
                    "tb_medical_report.pendamping,tb_medical_report.kebutuhan, " +
                    "reg_periksa.kd_pj,penjab.png_jawab " +
                    "FROM tb_medical_report " +
                    "INNER JOIN reg_periksa ON tb_medical_report.no_rawat=reg_periksa.no_rawat " +
                    "LEFT JOIN kamar_inap ON kamar_inap.no_rawat=reg_periksa.no_rawat " +
                    "INNER JOIN pasien ON reg_periksa.no_rkm_medis=pasien.no_rkm_medis " +
                    "INNER JOIN dokter ON tb_medical_report.kd_dokter=dokter.kd_dokter " +
                    "INNER JOIN penjab ON penjab.kd_pj=reg_periksa.kd_pj " +
                    "WHERE reg_periksa.tgl_registrasi BETWEEN ? AND ? "+
                    (TCari.getText().trim().equals("")?"":"and (reg_periksa.no_rkm_medis like ? or pasien.nm_pasien like ? or tb_medical_report.kd_dokter like ? or "+
                    "dokter.nm_dokter like ? or reg_periksa.no_rawat like ?)")+
                    "order by reg_periksa.tgl_registrasi,reg_periksa.status_lanjut");
            try {
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!TCari.getText().trim().equals("")){
                    ps.setString(3,"%"+TCari.getText()+"%");
                    ps.setString(4,"%"+TCari.getText()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                }

                rs=ps.executeQuery();
                while(rs.next()){
                    
                    if(rs.getString("kebutuhan").contains("1")){
                        kebutuhanString += "Ordinary Seat, ";
                    }
                    if(rs.getString("kebutuhan").contains("2")){
                        kebutuhanString += "Wheel Chair Assistance, ";
                    }
                    if(rs.getString("kebutuhan").contains("3")){
                        kebutuhanString += "Stretcher Case ";
                    }
                    
                    tabMode.addRow(new String[]{
                        rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),
                        rs.getString("kd_dokter"),rs.getString("nm_dokter"),
                        rs.getString("kd_pj"),rs.getString("png_jawab"),
                        rs.getString("tgl_registrasi"),rs.getString("tgl_keluar"),rs.getString("no_tlp"),
                        rs.getString("keluhan_utama"),rs.getString("pemeriksaan_fisik"),
                        rs.getString("pemeriksaan_penunjang"),
                        rs.getString("diagnosa"),rs.getString("diagnosa_banding"),rs.getString("terapi"),
                        rs.getString("saran_dokter"),rs.getString("evakuasi"),rs.getString("saran_evakuasi"),
                        rs.getString("perjalanan"),rs.getString("pendamping"),
                        kebutuhanString
                    });
                }
                
                                
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabMode.getRowCount());
    }

    public void emptTeks() {
        KeluhanUtama.setText("");
        PemeriksaanFisik.setText("");
        PemeriksaanPenunjang.setText("");
        Diagnosa.setText("");
        DiagnosaBanding.setText("");
        Terapi.setText("");
        SaranDokter.setText("");
        Evakuasi.setSelectedIndex(0);
        SaranEvakuasi.setSelectedIndex(0);
        Perjalanan.setSelectedIndex(0);
        Pendamping.setSelectedIndex(0);
        OrdinarySeat.setSelected(false);
        WheelChair.setSelected(false);
        Stretcher.setSelected(false);
        
        kebutuhan.clear();
    } 

    private void getData() {
        if(tbObat.getSelectedRow()!= -1){
            TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());  
            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());  
            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());  
            KodeDokter.setText(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString());
            NamaDokter.setText(tbObat.getValueAt(tbObat.getSelectedRow(),4).toString());
            Telepon.setText(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString());
            KdPj.setText(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
            CaraBayar.setText(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString());
            Masuk.setText(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString());
            Keluar.setText(tbObat.getValueAt(tbObat.getSelectedRow(),8).toString());
            
            KeluhanUtama.setText(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString());
            PemeriksaanFisik.setText(tbObat.getValueAt(tbObat.getSelectedRow(),11).toString());
            PemeriksaanPenunjang.setText(tbObat.getValueAt(tbObat.getSelectedRow(),12).toString());
            Diagnosa.setText(tbObat.getValueAt(tbObat.getSelectedRow(),13).toString());
            DiagnosaBanding.setText(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
            Terapi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),15).toString());
            SaranDokter.setText(tbObat.getValueAt(tbObat.getSelectedRow(),16).toString());
            
            if(tbObat.getValueAt(tbObat.getSelectedRow(),17).toString().equals("Yes")) {
                Evakuasi.setSelectedIndex(1);
            } else if(tbObat.getValueAt(tbObat.getSelectedRow(),17).toString().equals("No")) {
                Evakuasi.setSelectedIndex(2);
            } else {
                Evakuasi.setSelectedIndex(0);
            }
            
            if(tbObat.getValueAt(tbObat.getSelectedRow(),18).toString().equals("Yes")) {
                SaranEvakuasi.setSelectedIndex(1);
            } else if(tbObat.getValueAt(tbObat.getSelectedRow(),18).toString().equals("No")) {
                SaranEvakuasi.setSelectedIndex(2);
            } else {
                SaranEvakuasi.setSelectedIndex(0);
            }
            
            if(tbObat.getValueAt(tbObat.getSelectedRow(),19).toString().equals("Yes")) {
                Perjalanan.setSelectedIndex(1);
            } else if(tbObat.getValueAt(tbObat.getSelectedRow(),19).toString().equals("No")) {
                Perjalanan.setSelectedIndex(2);
            } else {
                Perjalanan.setSelectedIndex(0);
            }
            
            if(tbObat.getValueAt(tbObat.getSelectedRow(),20).toString().equals("Tanpa Pendamping (Unescorted)")) {
                Pendamping.setSelectedIndex(1);
            } else if(tbObat.getValueAt(tbObat.getSelectedRow(),20).toString().equals("Tanpa Pendamping Medis (With not medical escorted)")) {
                Pendamping.setSelectedIndex(2);
            } else if(tbObat.getValueAt(tbObat.getSelectedRow(),20).toString().equals("Dengan Pendamping Medis (Medical escorted)")) {
                Pendamping.setSelectedIndex(3);
            } else {
                Pendamping.setSelectedIndex(0);
            }
            
            kebutuhan.clear();
            
            if(tbObat.getValueAt(tbObat.getSelectedRow(),21).toString().contains("Ordinary Seat")){
                OrdinarySeat.setSelected(true);
                kebutuhan.add(1);
            }
            if(tbObat.getValueAt(tbObat.getSelectedRow(),21).toString().contains("Wheel Chair Assistance")){
                WheelChair.setSelected(true);
                kebutuhan.add(2);
            }
            if(tbObat.getValueAt(tbObat.getSelectedRow(),21).toString().contains("Stretcher Case")){
                Stretcher.setSelected(true);
                kebutuhan.add(3);
            }
            
        }
    }

    private void isRawat() {
         try {
            ps=koneksi.prepareStatement(
                    "SELECT reg_periksa.no_rkm_medis,pasien.nm_pasien,reg_periksa.tgl_registrasi, " +
                    "reg_periksa.kd_dokter,dokter.nm_dokter,reg_periksa.kd_pj,penjab.png_jawab, " +
                    "IF(kamar_inap.tgl_keluar IS NULL OR kamar_inap.tgl_keluar='0000-00-00',CURRENT_DATE(),kamar_inap.tgl_keluar) AS tgl_keluar, " +
                    "pasien.`tgl_lahir`, pasien.umur, pasien.`no_tlp` " +
                    "FROM reg_periksa " +
                    "INNER JOIN pasien ON pasien.no_rkm_medis=reg_periksa.no_rkm_medis " +
                    "INNER JOIN dokter ON dokter.kd_dokter=reg_periksa.kd_dokter " +
                    "INNER JOIN penjab ON penjab.kd_pj=reg_periksa.kd_pj " +
                    "LEFT JOIN kamar_inap ON kamar_inap.no_rawat=reg_periksa.no_rawat " +
                    "WHERE reg_periksa.no_rawat=?");
            try {
                ps.setString(1,TNoRw.getText());
                rs=ps.executeQuery();
                if(rs.next()){
                    DTPCari1.setDate(rs.getDate("tgl_registrasi"));
                    TNoRM.setText(rs.getString("no_rkm_medis"));
                    TPasien.setText(rs.getString("nm_pasien"));
                    Masuk.setText(rs.getString("tgl_registrasi"));
                    
                    Keluar.setText(rs.getString("tgl_keluar"));
                    Lahir.setText(rs.getString("tgl_lahir"));
                    Telepon.setText(rs.getString("no_tlp"));
                    
                    KdPj.setText(rs.getString("kd_pj"));
                    CaraBayar.setText(rs.getString("png_jawab"));
                    
                    
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        }
    }

    
    public void setNoRm(String norwt, Date tgl2) {
        TNoRw.setText(norwt);
        TCari.setText(norwt);
        
        DTPCari2.setDate(tgl2);    
        isRawat();           
        ChkInput.setSelected(true);
        isForm();
        
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,this.getHeight()-122));
            scrollInput.setVisible(true);      
            ChkInput.setVisible(true);
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            scrollInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }
    
    public void isCek(){
        BtnSimpan.setEnabled(akses.getdata_resume_pasien());
        BtnHapus.setEnabled(akses.getdata_resume_pasien());
        BtnEdit.setEnabled(akses.getdata_resume_pasien());
        
        
        ppBerkasDigital.setEnabled(akses.getberkas_digital_perawatan());    
        if(akses.getjml2()>=1){
            KodeDokter.setEditable(false);
            BtnDokter.setEnabled(false);
            KodeDokter.setText(akses.getkode());
            NamaDokter.setText(dokter.tampil3(KodeDokter.getText()));
            if(NamaDokter.getText().equals("")){
                KodeDokter.setText("");
                JOptionPane.showMessageDialog(null,"User login bukan dokter...!!");
            }
        }            
    }

    private void ganti() {
        
        String listString = kebutuhan.stream().map(Object::toString)
                        .collect(Collectors.joining(", "));
        
        if(Sequel.mengedittf("tb_medical_report","no_rawat=?","kd_dokter=?,"
                + "keluhan_utama=?,pemeriksaan_fisik=?,pemeriksaan_penunjang=?,diagnosa=?,diagnosa_banding=?,"
                + "terapi=?,saran_dokter=?,evakuasi=?,saran_evakuasi=?,"
                + "perjalanan=?,pendamping=?,kebutuhan=?",14,new String[]{
                KodeDokter.getText(),KeluhanUtama.getText(),PemeriksaanFisik.getText(),PemeriksaanPenunjang.getText(),
                    Diagnosa.getText(),DiagnosaBanding.getText(),Terapi.getText(),SaranDokter.getText(),
                    Evakuasi.getSelectedItem().toString(),SaranEvakuasi.getSelectedItem().toString(),
                    Perjalanan.getSelectedItem().toString(),Pendamping.getSelectedItem().toString(),listString,
                    tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
            })==true){
               tampil();
               emptTeks();
        }
    }

    private void hapus() {
        if(Sequel.queryu2tf("delete from tb_medical_report where no_rawat=?",1,new String[]{
            tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
        })==true){
            tabMode.removeRow(tbObat.getSelectedRow());
            LCount.setText(""+tabMode.getRowCount());
            emptTeks();
        }else{
            JOptionPane.showMessageDialog(null,"Gagal menghapus..!!");
        }
    }
    
//    public String getSelected() {
////        kebutuhan = "";
//        
//        if (((JCheckBox)).isSelected())
//            kebutuhan += ((JCheckBox) c).getText() + ",";
//            
//        
//        System.out.println(kebutuhan);
//        return kebutuhan;
//    }

    
}
