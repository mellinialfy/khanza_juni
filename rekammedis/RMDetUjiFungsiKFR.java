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
public final class RMDetUjiFungsiKFR extends javax.swing.JDialog {
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
    public RMDetUjiFungsiKFR(java.awt.Frame parent, boolean modal) {
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
        FormInput = new widget.PanelBiasa();
        TNoRw = new widget.TextBox();
        TPasien = new widget.TextBox();
        TNoRM = new widget.TextBox();
        jLabel5 = new widget.Label();
        label14 = new widget.Label();
        KodeDokter = new widget.TextBox();
        NamaDokter = new widget.TextBox();
        BtnDokter = new widget.Button();
        jLabel34 = new widget.Label();
        jLabel36 = new widget.Label();
        NamaDokter1 = new widget.TextBox();
        Scroll8 = new widget.ScrollPane();
        tbProgramFisio = new widget.Table();
        BtnTambahProgram = new widget.Button();
        BtnAllProgram = new widget.Button();
        BtnCariProgram = new widget.Button();
        TCariProgram = new widget.TextBox();
        label12 = new widget.Label();
        jLabel15 = new widget.Label();
        HasilYangDidapat = new widget.TextBox();
        jLabel17 = new widget.Label();
        Kesimpulan = new widget.TextBox();
        jLabel20 = new widget.Label();
        Rekomendasi = new widget.TextBox();
        Tanggal1 = new widget.Tanggal();
        jLabel38 = new widget.Label();
        NamaDokter2 = new widget.TextBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Layanan Kedokteran Fisik dan Rehabilitasi ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);
        Scroll.setPreferredSize(new java.awt.Dimension(452, 200));

        tbObat.setAutoCreateRowSorter(true);
        tbObat.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
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

        jLabel19.setText("Tanggal :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(67, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "17-02-2025" }));
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
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "17-02-2025" }));
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
        PanelInput.setPreferredSize(new java.awt.Dimension(192, 260));
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

        FormInput.setBackground(new java.awt.Color(250, 255, 245));
        FormInput.setBorder(null);
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 250));
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

        label14.setText("Petugas :");
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
        NamaDokter.setBounds(250, 40, 270, 23);

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

        jLabel34.setText("Tanggal :");
        jLabel34.setName("jLabel34"); // NOI18N
        FormInput.add(jLabel34);
        jLabel34.setBounds(580, 40, 100, 23);

        jLabel36.setText("Program :");
        jLabel36.setName("jLabel36"); // NOI18N
        FormInput.add(jLabel36);
        jLabel36.setBounds(0, 70, 110, 23);

        NamaDokter1.setEditable(false);
        NamaDokter1.setName("Alamat"); // NOI18N
        NamaDokter1.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(NamaDokter1);
        NamaDokter1.setBounds(120, 70, 670, 23);

        Scroll8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 253)));
        Scroll8.setName("Scroll8"); // NOI18N
        Scroll8.setOpaque(true);

        tbProgramFisio.setName("tbProgramFisio"); // NOI18N
        tbProgramFisio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbProgramFisioMouseClicked(evt);
            }
        });
        tbProgramFisio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbProgramFisioKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbProgramFisioKeyReleased(evt);
            }
        });
        Scroll8.setViewportView(tbProgramFisio);

        FormInput.add(Scroll8);
        Scroll8.setBounds(150, 830, 640, 143);

        BtnTambahProgram.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        BtnTambahProgram.setMnemonic('3');
        BtnTambahProgram.setToolTipText("Alt+3");
        BtnTambahProgram.setName("BtnTambahProgram"); // NOI18N
        BtnTambahProgram.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnTambahProgram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTambahProgramActionPerformed(evt);
            }
        });
        FormInput.add(BtnTambahProgram);
        BtnTambahProgram.setBounds(750, 980, 30, 23);

        BtnAllProgram.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAllProgram.setMnemonic('2');
        BtnAllProgram.setToolTipText("2Alt+2");
        BtnAllProgram.setName("BtnAllProgram"); // NOI18N
        BtnAllProgram.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAllProgram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllProgramActionPerformed(evt);
            }
        });
        BtnAllProgram.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllProgramKeyPressed(evt);
            }
        });
        FormInput.add(BtnAllProgram);
        BtnAllProgram.setBounds(720, 980, 30, 23);

        BtnCariProgram.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCariProgram.setMnemonic('1');
        BtnCariProgram.setToolTipText("Alt+1");
        BtnCariProgram.setName("BtnCariProgram"); // NOI18N
        BtnCariProgram.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCariProgram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariProgramActionPerformed(evt);
            }
        });
        BtnCariProgram.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariProgramKeyPressed(evt);
            }
        });
        FormInput.add(BtnCariProgram);
        BtnCariProgram.setBounds(690, 980, 30, 23);

        TCariProgram.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TCariProgram.setToolTipText("Alt+C");
        TCariProgram.setName("TCariProgram"); // NOI18N
        TCariProgram.setPreferredSize(new java.awt.Dimension(140, 23));
        TCariProgram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TCariProgramActionPerformed(evt);
            }
        });
        TCariProgram.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariProgramKeyPressed(evt);
            }
        });
        FormInput.add(TCariProgram);
        TCariProgram.setBounds(230, 980, 460, 23);

        label12.setText("Key Word :");
        label12.setName("label12"); // NOI18N
        label12.setPreferredSize(new java.awt.Dimension(60, 23));
        FormInput.add(label12);
        label12.setBounds(150, 980, 60, 23);

        jLabel15.setText("Hasil Yang Didapat :");
        jLabel15.setName("jLabel15"); // NOI18N
        FormInput.add(jLabel15);
        jLabel15.setBounds(0, 130, 114, 23);

        HasilYangDidapat.setFocusTraversalPolicyProvider(true);
        HasilYangDidapat.setName("HasilYangDidapat"); // NOI18N
        HasilYangDidapat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HasilYangDidapatKeyPressed(evt);
            }
        });
        FormInput.add(HasilYangDidapat);
        HasilYangDidapat.setBounds(120, 130, 670, 23);

        jLabel17.setText("Kesimpulan :");
        jLabel17.setName("jLabel17"); // NOI18N
        FormInput.add(jLabel17);
        jLabel17.setBounds(0, 160, 78, 23);

        Kesimpulan.setFocusTraversalPolicyProvider(true);
        Kesimpulan.setName("Kesimpulan"); // NOI18N
        Kesimpulan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KesimpulanKeyPressed(evt);
            }
        });
        FormInput.add(Kesimpulan);
        Kesimpulan.setBounds(80, 160, 707, 23);

        jLabel20.setText("Rekomendasi :");
        jLabel20.setName("jLabel20"); // NOI18N
        FormInput.add(jLabel20);
        jLabel20.setBounds(0, 190, 87, 23);

        Rekomendasi.setFocusTraversalPolicyProvider(true);
        Rekomendasi.setName("Rekomendasi"); // NOI18N
        Rekomendasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RekomendasiKeyPressed(evt);
            }
        });
        FormInput.add(Rekomendasi);
        Rekomendasi.setBounds(90, 190, 698, 23);

        Tanggal1.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "17-02-2025" }));
        Tanggal1.setDisplayFormat("dd-MM-yyyy");
        Tanggal1.setName("Tanggal1"); // NOI18N
        Tanggal1.setOpaque(false);
        Tanggal1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tanggal1KeyPressed(evt);
            }
        });
        FormInput.add(Tanggal1);
        Tanggal1.setBounds(690, 40, 90, 23);

        jLabel38.setText("Instrumen Uji Fungsi / Prosedur KFR :");
        jLabel38.setName("jLabel38"); // NOI18N
        FormInput.add(jLabel38);
        jLabel38.setBounds(0, 100, 200, 23);

        NamaDokter2.setEditable(false);
        NamaDokter2.setName("NamaDokter2"); // NOI18N
        NamaDokter2.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(NamaDokter2);
        NamaDokter2.setBounds(210, 100, 580, 23);

        PanelInput.add(FormInput, java.awt.BorderLayout.PAGE_START);

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
        }else if(Anamnesa.getText().equals("")){
            Valid.textKosong(Anamnesa,"Keluhan utama");
        }else if(DiagnosisMedis.getText().equals("")){
            Valid.textKosong(DiagnosisMedis,"Pemeriksaan fisik");
        }else if(DiagnosisFungsi.getText().equals("")){
            Valid.textKosong(DiagnosisFungsi,"Pemeriksaan penunjang");
        }else if(PemeriksaanPenunjang.getText().equals("")){
            Valid.textKosong(PemeriksaanPenunjang,"Diagnosa");
        }else if(TataLaksana.getText().equals("")){
            Valid.textKosong(TataLaksana,"Diagnosa banding");
        }
        else if(HasilYangDidapat.getText().trim().equals("")){
            Valid.textKosong(HasilYangDidapat,"Hasil Yang Didapat");
        }else if(Kesimpulan.getText().trim().equals("")){
            Valid.textKosong(Kesimpulan,"Kesimpulan");
        }else if(Rekomendasi.getText().trim().equals("")){
            Valid.textKosong(Rekomendasi,"Rekomendasi");
        }
//        else if(Anjuran.getText().equals("")){
//            Valid.textKosong(Anjuran,"Terapi");
//        }else if(Evaluasi.getText().equals("")){
//            Valid.textKosong(Evaluasi,"Saran dokter");
//        }else if(AkibatKerja.getSelectedItem().equals("Select")){
//            Valid.textKosong(AkibatKerja,"Evakuasi");
//        }else if(SaranEvakuasi.getSelectedItem().equals("Select")){
//            Valid.textKosong(SaranEvakuasi,"Saran evakuasi");
//        }else if(Perjalanan.getSelectedItem().equals("Select")){
//            Valid.textKosong(Perjalanan,"Perjalanan");
//        }else if(Pendamping.getSelectedItem().equals("Select")){
//            Valid.textKosong(Pendamping,"Pendamping");
//        } 
        else{
//            System.out.println(Evakuasi.getSelectedItem());
            String listString = kebutuhan.stream().map(Object::toString)
                        .collect(Collectors.joining(", "));
            kebutuhanString="";
            
//            if(Sequel.menyimpantf("tb_medical_report","?,?,?,?,?,?,?,?,?,?,?,?,?,?","No.Rawat",14,new String[]{
//                    TNoRw.getText(),KodeDokter.getText(),Anamnesa.getText(),DiagnosisMedis.getText(),DiagnosisFungsi.getText(),
//                    PemeriksaanPenunjang.getText(),TataLaksana.getText(),Anjuran.getText(),Evaluasi.getText(),
//                    AkibatKerja.getSelectedItem().toString(),SaranEvakuasi.getSelectedItem().toString(),
//                    Perjalanan.getSelectedItem().toString(),Pendamping.getSelectedItem().toString(),listString
//                })==true){
//                if(kebutuhan.contains(1)){
//                    kebutuhanString += "Ordinary Seat, ";
//                }
//                if(kebutuhan.contains(2)){
//                    kebutuhanString += "Wheel Chair Assistance, ";
//                }
//                if(kebutuhan.contains(3)){
//                    kebutuhanString += "Stretcher Case ";
//                }
//                
//                    tabMode.addRow(new String[]{
//                        TNoRw.getText(),TNoRM.getText(),TPasien.getText(),KodeDokter.getText(),NamaDokter.getText(),
//                        KdPj.getText(),CaraBayar.getText(),Masuk.getText(), Keluar.getText(),Telepon.getText(),
//                        Anamnesa.getText(),DiagnosisMedis.getText(),DiagnosisFungsi.getText(),
//                        PemeriksaanPenunjang.getText(),TataLaksana.getText(),Anjuran.getText(),Evaluasi.getText(),
//                        AkibatKerja.getSelectedItem().toString(),SaranEvakuasi.getSelectedItem().toString(),
//                        Perjalanan.getSelectedItem().toString(),
//                        Pendamping.getSelectedItem().toString(),
//                        kebutuhanString
//                        
//                    });
//                    
//                    emptTeks();
//                    LCount.setText(""+tabMode.getRowCount());
//            }
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
        }else if(Anamnesa.getText().equals("")){
            Valid.textKosong(Anamnesa,"Keluhan utama");
        }else if(DiagnosisMedis.getText().equals("")){
            Valid.textKosong(DiagnosisMedis,"Pemeriksaan fisik");
        }else if(DiagnosisFungsi.getText().equals("")){
            Valid.textKosong(DiagnosisFungsi,"Pemeriksaan penunjang");
        }else if(PemeriksaanPenunjang.getText().equals("")){
            Valid.textKosong(PemeriksaanPenunjang,"Diagnosa");
        }else if(TataLaksana.getText().equals("")){
            Valid.textKosong(TataLaksana,"Diagnosa banding");
        }
//        else if(Anjuran.getText().equals("")){
//            Valid.textKosong(Anjuran,"Terapi");
//        }else if(Evaluasi.getText().equals("")){
//            Valid.textKosong(Evaluasi,"Saran dokter");
//        }else if(AkibatKerja.getSelectedItem().equals("Select")){
//            Valid.textKosong(AkibatKerja,"Evakuasi");
//        }else if(SaranEvakuasi.getSelectedItem().equals("Select")){
//            Valid.textKosong(SaranEvakuasi,"Saran evakuasi");
//        }else if(Perjalanan.getSelectedItem().equals("Select")){
//            Valid.textKosong(Perjalanan,"Perjalanan");
//        }else if(Pendamping.getSelectedItem().equals("Select")){
//            Valid.textKosong(Pendamping,"Pendamping");
//        } 
        else{
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

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

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

    private void tbProgramFisioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbProgramFisioMouseClicked
//        if(tabModeMasalah.getRowCount()!=0){
//            try {
//                tampilRencana2();
//            } catch (java.lang.NullPointerException e) {
//            }
//        }
    }//GEN-LAST:event_tbProgramFisioMouseClicked

    private void tbProgramFisioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbProgramFisioKeyPressed
//        if(tabModeMasalah.getRowCount()!=0){
//            if(evt.getKeyCode()==KeyEvent.VK_SHIFT){
//                TCariProgram.setText("");
//                TCariProgram.requestFocus();
//            }
//        }
    }//GEN-LAST:event_tbProgramFisioKeyPressed

    private void tbProgramFisioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbProgramFisioKeyReleased
//        if(tabModeMasalah.getRowCount()!=0){
//            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
//                try {
//                    tampilRencana2();
//                } catch (java.lang.NullPointerException e) {
//                }
//            }
//        }
    }//GEN-LAST:event_tbProgramFisioKeyReleased

    private void BtnTambahProgramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahProgramActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        MasterMasalahKeperawatanIGD form=new MasterMasalahKeperawatanIGD(null,false);
        form.isCek();
        form.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        form.setLocationRelativeTo(internalFrame1);
        form.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnTambahProgramActionPerformed

    private void BtnAllProgramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllProgramActionPerformed
        TCari.setText("");
//        tampilMasalah();
    }//GEN-LAST:event_BtnAllProgramActionPerformed

    private void BtnAllProgramKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllProgramKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnAllProgramActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnCariProgram, TCariProgram);
        }
    }//GEN-LAST:event_BtnAllProgramKeyPressed

    private void BtnCariProgramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariProgramActionPerformed
//        tampilMasalah2();
    }//GEN-LAST:event_BtnCariProgramActionPerformed

    private void BtnCariProgramKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariProgramKeyPressed
//        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
//            tampilMasalah2();
//        }else if((evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN)||(evt.getKeyCode()==KeyEvent.VK_TAB)){
//            Rencana.requestFocus();
//        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
//            KetDokter.requestFocus();
//        }
    }//GEN-LAST:event_BtnCariProgramKeyPressed

    private void TCariProgramKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariProgramKeyPressed
//        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
//            tampilMasalah2();
//        }else if((evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN)||(evt.getKeyCode()==KeyEvent.VK_TAB)){
//            Rencana.requestFocus();
//        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
//            KetDokter.requestFocus();
//        }
    }//GEN-LAST:event_TCariProgramKeyPressed

    private void TCariProgramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TCariProgramActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TCariProgramActionPerformed

    private void HasilYangDidapatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HasilYangDidapatKeyPressed
        Valid.pindah(evt,DiagnosisMedis,Kesimpulan);
    }//GEN-LAST:event_HasilYangDidapatKeyPressed

    private void KesimpulanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KesimpulanKeyPressed
        Valid.pindah(evt,HasilYangDidapat,Rekomendasi);
    }//GEN-LAST:event_KesimpulanKeyPressed

    private void RekomendasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RekomendasiKeyPressed
        Valid.pindah(evt,Kesimpulan,BtnSimpan);
    }//GEN-LAST:event_RekomendasiKeyPressed

    private void Tanggal1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tanggal1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Tanggal1KeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMDetUjiFungsiKFR dialog = new RMDetUjiFungsiKFR(new javax.swing.JFrame(), true);
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
    private widget.Button BtnAllProgram;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnCariProgram;
    private widget.Button BtnDokter;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnSimpan;
    private widget.Button BtnTambahProgram;
    private widget.CekBox ChkInput;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.PanelBiasa FormInput;
    private widget.TextBox HasilYangDidapat;
    private widget.TextBox Kesimpulan;
    private widget.TextBox KodeDokter;
    private widget.Label LCount;
    private widget.TextBox NamaDokter;
    private widget.TextBox NamaDokter1;
    private widget.TextBox NamaDokter2;
    private javax.swing.JPanel PanelInput;
    private widget.TextBox Rekomendasi;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane Scroll8;
    private widget.TextBox TCari;
    private widget.TextBox TCariProgram;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.Tanggal Tanggal1;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel15;
    private widget.Label jLabel17;
    private widget.Label jLabel19;
    private widget.Label jLabel20;
    private widget.Label jLabel21;
    private widget.Label jLabel34;
    private widget.Label jLabel36;
    private widget.Label jLabel38;
    private widget.Label jLabel5;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private javax.swing.JPanel jPanel3;
    private widget.Label label12;
    private widget.Label label14;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.Table tbObat;
    private widget.Table tbProgramFisio;
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
        Anamnesa.setText("");
        DiagnosisMedis.setText("");
        DiagnosisFungsi.setText("");
        PemeriksaanPenunjang.setText("");
        TataLaksana.setText("");
//        Anjuran.setText("");
//        Evaluasi.setText("");
        AkibatKerja.setSelectedIndex(0);
        
        HasilYangDidapat.setText("");
        Kesimpulan.setText("");
        Rekomendasi.setText("");
//        SaranEvakuasi.setSelectedIndex(0);
//        Perjalanan.setSelectedIndex(0);
//        Pendamping.setSelectedIndex(0);
//        OrdinarySeat.setSelected(false);
//        WheelChair.setSelected(false);
//        Stretcher.setSelected(false);


        
        kebutuhan.clear();
    } 

    private void getData() {
//        if(tbObat.getSelectedRow()!= -1){
//            TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());  
//            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());  
//            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());  
//            KodeDokter.setText(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString());
//            NamaDokter.setText(tbObat.getValueAt(tbObat.getSelectedRow(),4).toString());
//            Telepon.setText(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString());
//            KdPj.setText(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
//            CaraBayar.setText(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString());
//            Masuk.setText(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString());
//            Keluar.setText(tbObat.getValueAt(tbObat.getSelectedRow(),8).toString());
//            
//            Anamnesa.setText(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString());
//            DiagnosisMedis.setText(tbObat.getValueAt(tbObat.getSelectedRow(),11).toString());
//            DiagnosisFungsi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),12).toString());
//            PemeriksaanPenunjang.setText(tbObat.getValueAt(tbObat.getSelectedRow(),13).toString());
//            TataLaksana.setText(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
//            Anjuran.setText(tbObat.getValueAt(tbObat.getSelectedRow(),15).toString());
//            Evaluasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),16).toString());
//            
//            if(tbObat.getValueAt(tbObat.getSelectedRow(),17).toString().equals("Yes")) {
//                AkibatKerja.setSelectedIndex(1);
//            } else if(tbObat.getValueAt(tbObat.getSelectedRow(),17).toString().equals("No")) {
//                AkibatKerja.setSelectedIndex(2);
//            } else {
//                AkibatKerja.setSelectedIndex(0);
//            }
//            
//            if(tbObat.getValueAt(tbObat.getSelectedRow(),18).toString().equals("Yes")) {
//                SaranEvakuasi.setSelectedIndex(1);
//            } else if(tbObat.getValueAt(tbObat.getSelectedRow(),18).toString().equals("No")) {
//                SaranEvakuasi.setSelectedIndex(2);
//            } else {
//                SaranEvakuasi.setSelectedIndex(0);
//            }
//            
//            if(tbObat.getValueAt(tbObat.getSelectedRow(),19).toString().equals("Yes")) {
//                Perjalanan.setSelectedIndex(1);
//            } else if(tbObat.getValueAt(tbObat.getSelectedRow(),19).toString().equals("No")) {
//                Perjalanan.setSelectedIndex(2);
//            } else {
//                Perjalanan.setSelectedIndex(0);
//            }
//            
//            if(tbObat.getValueAt(tbObat.getSelectedRow(),20).toString().equals("Tanpa Pendamping (Unescorted)")) {
//                Pendamping.setSelectedIndex(1);
//            } else if(tbObat.getValueAt(tbObat.getSelectedRow(),20).toString().equals("Tanpa Pendamping Medis (With not medical escorted)")) {
//                Pendamping.setSelectedIndex(2);
//            } else if(tbObat.getValueAt(tbObat.getSelectedRow(),20).toString().equals("Dengan Pendamping Medis (Medical escorted)")) {
//                Pendamping.setSelectedIndex(3);
//            } else {
//                Pendamping.setSelectedIndex(0);
//            }
//            
//            kebutuhan.clear();
//            
//            if(tbObat.getValueAt(tbObat.getSelectedRow(),21).toString().contains("Ordinary Seat")){
//                OrdinarySeat.setSelected(true);
//                kebutuhan.add(1);
//            }
//            if(tbObat.getValueAt(tbObat.getSelectedRow(),21).toString().contains("Wheel Chair Assistance")){
//                WheelChair.setSelected(true);
//                kebutuhan.add(2);
//            }
//            if(tbObat.getValueAt(tbObat.getSelectedRow(),21).toString().contains("Stretcher Case")){
//                Stretcher.setSelected(true);
//                kebutuhan.add(3);
//            }
//            
//        }
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
//        
//        String listString = kebutuhan.stream().map(Object::toString)
//                        .collect(Collectors.joining(", "));
//        
//        if(Sequel.mengedittf("tb_medical_report","no_rawat=?","kd_dokter=?,"
//                + "keluhan_utama=?,pemeriksaan_fisik=?,pemeriksaan_penunjang=?,diagnosa=?,diagnosa_banding=?,"
//                + "terapi=?,saran_dokter=?,evakuasi=?,saran_evakuasi=?,"
//                + "perjalanan=?,pendamping=?,kebutuhan=?",14,new String[]{
//                KodeDokter.getText(),Anamnesa.getText(),DiagnosisMedis.getText(),DiagnosisFungsi.getText(),
//                    PemeriksaanPenunjang.getText(),TataLaksana.getText(),Anjuran.getText(),Evaluasi.getText(),
//                    AkibatKerja.getSelectedItem().toString(),SaranEvakuasi.getSelectedItem().toString(),
//                    Perjalanan.getSelectedItem().toString(),Pendamping.getSelectedItem().toString(),listString,
//                    tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
//            })==true){
//               tampil();
//               emptTeks();
//        }
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
