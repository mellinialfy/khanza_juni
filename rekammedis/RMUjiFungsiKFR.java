/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgDataSkriningGiziLanjut.java
 * Kontribusi Haris Rochmatullah RS Bhayangkara Nganjuk
 * Created on 11 November 2020, 20:19:56
 */

package rekammedis;

import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import kepegawaian.DlgCariDokter;


/**
 *
 * @author perpustakaan
 */
public final class RMUjiFungsiKFR extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private int i=0;    
    private DlgCariDokter dokter=new DlgCariDokter(null,false);
    private String finger="";
    /** Creates new form DlgRujuk
     * @param parent
     * @param modal */
    public RMUjiFungsiKFR(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8,1);
        setSize(628,674);

        
                                
        tabMode=new DefaultTableModel(null,new Object[]{
            "No.Rawat","No.R.M.","Nama Pasien","Umur","JK","Tgl.Lahir","Tanggal Mulai",
            "Tanggal Kontrol","Hubungan","Anamnesa","Pemeriksaan Fisik",
            "Diagnosis Fungsional","Diagnosis Medis",
            "Pemeriksaan Penunjang","Tata Laksana KFR","GOAL","Anjuran",
            "Evaluasi","Suspek Akibat Kerja","Keterangan Suspek","Diagnosa","Permintaan Terapi","Kode Dokter","Nama Dokter"
        }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbObat.setModel(tabMode);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 24; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(105);
            }else if(i==1){
                column.setPreferredWidth(65);
            }else if(i==2){
                column.setPreferredWidth(160);
            }else if(i==3){
                column.setPreferredWidth(35);
            }else if(i==4){
                column.setPreferredWidth(20);
            }else if(i==5){
                column.setPreferredWidth(65);
            }else if(i==6){
                column.setPreferredWidth(65);
            }else if(i==7){
                column.setPreferredWidth(65);
            }else if(i==8){
                column.setPreferredWidth(70);
            }else if(i==9){
                column.setPreferredWidth(150);
            }else if(i==10){
                column.setPreferredWidth(150);
            }else if(i==11){
                column.setPreferredWidth(120);
            }else if(i==12){
                column.setPreferredWidth(120);
            }else if(i==13){
                column.setPreferredWidth(150);
            }else if(i==14){
                column.setPreferredWidth(150);
            }else if(i==15){
                column.setPreferredWidth(70);
            }else if(i==16){
                column.setPreferredWidth(150);
            }else if(i==17){
                column.setPreferredWidth(150);
            }else if(i==18){
                column.setPreferredWidth(50);
            }else if(i==19){
                column.setPreferredWidth(100);
            }else if(i==20){
                column.setPreferredWidth(150);
            }else if(i==21){
                column.setPreferredWidth(150);
            }else if(i==22){
                column.setMaxWidth(0);
                column.setMinWidth(0);
            }else if(i==23){
                column.setPreferredWidth(70);
            }
            
        }
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());

        TNoRw.setDocument(new batasInput((byte)17).getKata(TNoRw));
        KdDokter.setDocument(new batasInput((byte)20).getKata(KdDokter));
        DiagnosisFungsional.setDocument(new batasInput((int)50).getKata(DiagnosisFungsional));
        DiagnosisMedis.setDocument(new batasInput((int)50).getKata(DiagnosisMedis));
        
        
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
                    KdDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                    NmDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                }  
                KdDokter.requestFocus();
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
        
        jam();
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
        MnUjiFungsi = new javax.swing.JMenuItem();
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
        Scroll1 = new widget.ScrollPane();
        FormInput = new widget.PanelBiasa();
        jLabel4 = new widget.Label();
        TNoRw = new widget.TextBox();
        TPasien = new widget.TextBox();
        Tanggal = new widget.Tanggal();
        TNoRM = new widget.TextBox();
        jLabel16 = new widget.Label();
        Jam = new widget.ComboBox();
        Menit = new widget.ComboBox();
        Detik = new widget.ComboBox();
        ChkKejadian = new widget.CekBox();
        jLabel18 = new widget.Label();
        KdDokter = new widget.TextBox();
        NmDokter = new widget.TextBox();
        btnPetugas = new widget.Button();
        jLabel8 = new widget.Label();
        TglLahir = new widget.TextBox();
        jLabel12 = new widget.Label();
        DiagnosisFungsional = new widget.TextBox();
        JK = new widget.TextBox();
        jLabel13 = new widget.Label();
        DiagnosisMedis = new widget.TextBox();
        jLabel14 = new widget.Label();
        jLabel22 = new widget.Label();
        Telepon = new widget.TextBox();
        jLabel36 = new widget.Label();
        Alamat = new widget.TextBox();
        jLabel25 = new widget.Label();
        KdPj = new widget.TextBox();
        CaraBayar = new widget.TextBox();
        jLabel37 = new widget.Label();
        Tanggal1 = new widget.Tanggal();
        jLabel23 = new widget.Label();
        Hubungan = new widget.ComboBox();
        HubunganLainnya = new widget.TextBox();
        scrollPane12 = new widget.ScrollPane();
        Anamnesa = new widget.TextArea();
        jLabel38 = new widget.Label();
        jLabel50 = new widget.Label();
        scrollPane13 = new widget.ScrollPane();
        PemeriksaanFisik = new widget.TextArea();
        jLabel41 = new widget.Label();
        scrollPane18 = new widget.ScrollPane();
        PemeriksaanPenunjang = new widget.TextArea();
        jLabel42 = new widget.Label();
        scrollPane19 = new widget.ScrollPane();
        TataLaksana = new widget.TextArea();
        jLabel46 = new widget.Label();
        scrollPane22 = new widget.ScrollPane();
        Goal = new widget.TextArea();
        jLabel47 = new widget.Label();
        scrollPane23 = new widget.ScrollPane();
        Diagnosa = new widget.TextArea();
        jLabel48 = new widget.Label();
        scrollPane24 = new widget.ScrollPane();
        PermintaanTerapi = new widget.TextArea();
        jLabel49 = new widget.Label();
        jLabel44 = new widget.Label();
        jLabel45 = new widget.Label();
        AkibatKerja = new widget.ComboBox();
        KetAkibatKerja = new widget.TextBox();
        Scroll8 = new widget.ScrollPane();
        tbProgramFisio = new widget.Table();
        label12 = new widget.Label();
        TCariProgram = new widget.TextBox();
        BtnCariProgram = new widget.Button();
        BtnAllProgram = new widget.Button();
        BtnTambahProgram = new widget.Button();
        btnTemplate = new widget.Button();
        Anjuran = new widget.ComboBox();
        Evaluasi = new widget.ComboBox();
        ChkInput = new widget.CekBox();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnUjiFungsi.setBackground(new java.awt.Color(255, 255, 254));
        MnUjiFungsi.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnUjiFungsi.setForeground(new java.awt.Color(50, 50, 50));
        MnUjiFungsi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnUjiFungsi.setText("Formulir/Lembar Uji Fungsi/Prosedur KFR");
        MnUjiFungsi.setName("MnUjiFungsi"); // NOI18N
        MnUjiFungsi.setPreferredSize(new java.awt.Dimension(270, 26));
        MnUjiFungsi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnUjiFungsiActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnUjiFungsi);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Uji Fungsi/Prosedur KFR ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);
        Scroll.setPreferredSize(new java.awt.Dimension(452, 200));

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

        jLabel19.setText("Tanggal :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(55, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "18-02-2025" }));
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
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "18-02-2025" }));
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
        TCari.setPreferredSize(new java.awt.Dimension(320, 23));
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
        PanelInput.setPreferredSize(new java.awt.Dimension(192, 274));
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll1.setName("Scroll1"); // NOI18N
        Scroll1.setOpaque(true);
        Scroll1.setPreferredSize(new java.awt.Dimension(452, 200));

        FormInput.setBackground(new java.awt.Color(250, 255, 245));
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 930));
        FormInput.setLayout(null);

        jLabel4.setText("No.Rawat :");
        jLabel4.setName("jLabel4"); // NOI18N
        FormInput.add(jLabel4);
        jLabel4.setBounds(0, 10, 75, 23);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        TNoRw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwKeyPressed(evt);
            }
        });
        FormInput.add(TNoRw);
        TNoRw.setBounds(79, 10, 141, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        TPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TPasienKeyPressed(evt);
            }
        });
        FormInput.add(TPasien);
        TPasien.setBounds(336, 10, 240, 23);

        Tanggal.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "18-02-2025" }));
        Tanggal.setDisplayFormat("dd-MM-yyyy");
        Tanggal.setName("Tanggal"); // NOI18N
        Tanggal.setOpaque(false);
        Tanggal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TanggalKeyPressed(evt);
            }
        });
        FormInput.add(Tanggal);
        Tanggal.setBounds(80, 40, 90, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        TNoRM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRMKeyPressed(evt);
            }
        });
        FormInput.add(TNoRM);
        TNoRM.setBounds(222, 10, 112, 23);

        jLabel16.setText("Tanggal :");
        jLabel16.setName("jLabel16"); // NOI18N
        jLabel16.setVerifyInputWhenFocusTarget(false);
        FormInput.add(jLabel16);
        jLabel16.setBounds(0, 40, 75, 23);

        Jam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        Jam.setName("Jam"); // NOI18N
        Jam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JamKeyPressed(evt);
            }
        });
        FormInput.add(Jam);
        Jam.setBounds(173, 40, 62, 23);

        Menit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Menit.setName("Menit"); // NOI18N
        Menit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MenitKeyPressed(evt);
            }
        });
        FormInput.add(Menit);
        Menit.setBounds(238, 40, 62, 23);

        Detik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Detik.setName("Detik"); // NOI18N
        Detik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DetikKeyPressed(evt);
            }
        });
        FormInput.add(Detik);
        Detik.setBounds(303, 40, 62, 23);

        ChkKejadian.setBorder(null);
        ChkKejadian.setSelected(true);
        ChkKejadian.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ChkKejadian.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkKejadian.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkKejadian.setName("ChkKejadian"); // NOI18N
        FormInput.add(ChkKejadian);
        ChkKejadian.setBounds(368, 40, 23, 23);

        jLabel18.setText("Dokter Sp.RM :");
        jLabel18.setName("jLabel18"); // NOI18N
        FormInput.add(jLabel18);
        jLabel18.setBounds(400, 40, 80, 23);

        KdDokter.setEditable(false);
        KdDokter.setHighlighter(null);
        KdDokter.setName("KdDokter"); // NOI18N
        KdDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdDokterKeyPressed(evt);
            }
        });
        FormInput.add(KdDokter);
        KdDokter.setBounds(484, 40, 94, 23);

        NmDokter.setEditable(false);
        NmDokter.setName("NmDokter"); // NOI18N
        FormInput.add(NmDokter);
        NmDokter.setBounds(580, 40, 177, 23);

        btnPetugas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPetugas.setMnemonic('2');
        btnPetugas.setToolTipText("ALt+2");
        btnPetugas.setName("btnPetugas"); // NOI18N
        btnPetugas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPetugasActionPerformed(evt);
            }
        });
        btnPetugas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPetugasKeyPressed(evt);
            }
        });
        FormInput.add(btnPetugas);
        btnPetugas.setBounds(760, 40, 28, 23);

        jLabel8.setText("Tgl.Lahir :");
        jLabel8.setName("jLabel8"); // NOI18N
        FormInput.add(jLabel8);
        jLabel8.setBounds(625, 10, 60, 23);

        TglLahir.setHighlighter(null);
        TglLahir.setName("TglLahir"); // NOI18N
        FormInput.add(TglLahir);
        TglLahir.setBounds(689, 10, 100, 23);

        jLabel12.setText("Diagnosis Fungsional (ICD-10):");
        jLabel12.setName("jLabel12"); // NOI18N
        FormInput.add(jLabel12);
        jLabel12.setBounds(0, 240, 220, 23);

        DiagnosisFungsional.setFocusTraversalPolicyProvider(true);
        DiagnosisFungsional.setName("DiagnosisFungsional"); // NOI18N
        DiagnosisFungsional.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DiagnosisFungsionalKeyPressed(evt);
            }
        });
        FormInput.add(DiagnosisFungsional);
        DiagnosisFungsional.setBounds(230, 240, 560, 23);

        JK.setEditable(false);
        JK.setHighlighter(null);
        JK.setName("JK"); // NOI18N
        JK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JKKeyPressed(evt);
            }
        });
        FormInput.add(JK);
        JK.setBounds(578, 10, 35, 23);

        jLabel13.setText("Diagnosis Medis (ICD-10):");
        jLabel13.setName("jLabel13"); // NOI18N
        FormInput.add(jLabel13);
        jLabel13.setBounds(0, 270, 220, 23);

        DiagnosisMedis.setFocusTraversalPolicyProvider(true);
        DiagnosisMedis.setName("DiagnosisMedis"); // NOI18N
        DiagnosisMedis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DiagnosisMedisKeyPressed(evt);
            }
        });
        FormInput.add(DiagnosisMedis);
        DiagnosisMedis.setBounds(230, 270, 560, 23);

        jLabel14.setText("PROGRAM: ");
        jLabel14.setName("jLabel14"); // NOI18N
        FormInput.add(jLabel14);
        jLabel14.setBounds(0, 680, 130, 23);

        jLabel22.setText("Telepon :");
        jLabel22.setName("jLabel22"); // NOI18N
        FormInput.add(jLabel22);
        jLabel22.setBounds(170, 70, 70, 23);

        Telepon.setEditable(false);
        Telepon.setHighlighter(null);
        Telepon.setName("Telepon"); // NOI18N
        FormInput.add(Telepon);
        Telepon.setBounds(240, 70, 150, 23);

        jLabel36.setText("Alamat :");
        jLabel36.setName("jLabel36"); // NOI18N
        FormInput.add(jLabel36);
        jLabel36.setBounds(30, 100, 50, 23);

        Alamat.setEditable(false);
        Alamat.setName("Alamat"); // NOI18N
        Alamat.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(Alamat);
        Alamat.setBounds(80, 100, 330, 23);

        jLabel25.setText("Cara Bayar :");
        jLabel25.setName("jLabel25"); // NOI18N
        FormInput.add(jLabel25);
        jLabel25.setBounds(410, 70, 70, 23);

        KdPj.setEditable(false);
        KdPj.setHighlighter(null);
        KdPj.setName("KdPj"); // NOI18N
        KdPj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KdPjActionPerformed(evt);
            }
        });
        FormInput.add(KdPj);
        KdPj.setBounds(490, 70, 80, 23);

        CaraBayar.setEditable(false);
        CaraBayar.setHighlighter(null);
        CaraBayar.setName("CaraBayar"); // NOI18N
        CaraBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CaraBayarActionPerformed(evt);
            }
        });
        FormInput.add(CaraBayar);
        CaraBayar.setBounds(570, 70, 160, 23);

        jLabel37.setText("Tgl Kontrol :");
        jLabel37.setName("jLabel37"); // NOI18N
        FormInput.add(jLabel37);
        jLabel37.setBounds(0, 70, 80, 23);

        Tanggal1.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "18-02-2025" }));
        Tanggal1.setDisplayFormat("dd-MM-yyyy");
        Tanggal1.setName("Tanggal1"); // NOI18N
        Tanggal1.setOpaque(false);
        Tanggal1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tanggal1KeyPressed(evt);
            }
        });
        FormInput.add(Tanggal1);
        Tanggal1.setBounds(80, 70, 90, 23);

        jLabel23.setText("Hubungan dengan tertanggung :");
        jLabel23.setName("jLabel23"); // NOI18N
        FormInput.add(jLabel23);
        jLabel23.setBounds(420, 100, 160, 23);

        Hubungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "Pasien", "Suami", "Istri", "Anak", "Orang Tua", "Lainnya" }));
        Hubungan.setName("Hubungan"); // NOI18N
        Hubungan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HubunganActionPerformed(evt);
            }
        });
        Hubungan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HubunganKeyPressed(evt);
            }
        });
        FormInput.add(Hubungan);
        Hubungan.setBounds(580, 100, 110, 23);

        HubunganLainnya.setEditable(false);
        HubunganLainnya.setHighlighter(null);
        HubunganLainnya.setName("HubunganLainnya"); // NOI18N
        FormInput.add(HubunganLainnya);
        HubunganLainnya.setBounds(700, 100, 90, 23);

        scrollPane12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane12.setName("scrollPane12"); // NOI18N

        Anamnesa.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Anamnesa.setColumns(20);
        Anamnesa.setRows(5);
        Anamnesa.setName("Anamnesa"); // NOI18N
        Anamnesa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AnamnesaKeyPressed(evt);
            }
        });
        scrollPane12.setViewportView(Anamnesa);

        FormInput.add(scrollPane12);
        scrollPane12.setBounds(130, 130, 660, 40);

        jLabel38.setText("Anamnesa :");
        jLabel38.setName("jLabel38"); // NOI18N
        FormInput.add(jLabel38);
        jLabel38.setBounds(40, 130, 80, 23);

        jLabel50.setText("<html>Pemeriksaan Fisik dan Uji Fungsi :</html>");
        jLabel50.setName("jLabel50"); // NOI18N
        FormInput.add(jLabel50);
        jLabel50.setBounds(10, 180, 110, 50);

        scrollPane13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane13.setName("scrollPane13"); // NOI18N

        PemeriksaanFisik.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        PemeriksaanFisik.setColumns(20);
        PemeriksaanFisik.setRows(5);
        PemeriksaanFisik.setName("PemeriksaanFisik"); // NOI18N
        PemeriksaanFisik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PemeriksaanFisikKeyPressed(evt);
            }
        });
        scrollPane13.setViewportView(PemeriksaanFisik);

        FormInput.add(scrollPane13);
        scrollPane13.setBounds(131, 180, 660, 50);

        jLabel41.setText("Pemeriksaan Penunjang :");
        jLabel41.setName("jLabel41"); // NOI18N
        FormInput.add(jLabel41);
        jLabel41.setBounds(0, 310, 220, 23);

        scrollPane18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane18.setName("scrollPane18"); // NOI18N

        PemeriksaanPenunjang.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        PemeriksaanPenunjang.setColumns(20);
        PemeriksaanPenunjang.setRows(5);
        PemeriksaanPenunjang.setName("PemeriksaanPenunjang"); // NOI18N
        PemeriksaanPenunjang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PemeriksaanPenunjangKeyPressed(evt);
            }
        });
        scrollPane18.setViewportView(PemeriksaanPenunjang);

        FormInput.add(scrollPane18);
        scrollPane18.setBounds(230, 310, 561, 50);

        jLabel42.setText("Tata Laksana KFR :");
        jLabel42.setName("jLabel42"); // NOI18N
        FormInput.add(jLabel42);
        jLabel42.setBounds(0, 370, 220, 23);

        scrollPane19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane19.setName("scrollPane19"); // NOI18N

        TataLaksana.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TataLaksana.setColumns(20);
        TataLaksana.setRows(5);
        TataLaksana.setName("TataLaksana"); // NOI18N
        TataLaksana.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TataLaksanaKeyPressed(evt);
            }
        });
        scrollPane19.setViewportView(TataLaksana);

        FormInput.add(scrollPane19);
        scrollPane19.setBounds(230, 370, 561, 50);

        jLabel46.setText("GOAL :");
        jLabel46.setName("jLabel46"); // NOI18N
        FormInput.add(jLabel46);
        jLabel46.setBounds(0, 430, 220, 23);

        scrollPane22.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane22.setName("scrollPane22"); // NOI18N

        Goal.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Goal.setColumns(20);
        Goal.setRows(5);
        Goal.setName("Goal"); // NOI18N
        Goal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                GoalKeyPressed(evt);
            }
        });
        scrollPane22.setViewportView(Goal);

        FormInput.add(scrollPane22);
        scrollPane22.setBounds(230, 430, 561, 30);

        jLabel47.setText("Diagnosa :");
        jLabel47.setName("jLabel47"); // NOI18N
        FormInput.add(jLabel47);
        jLabel47.setBounds(0, 470, 220, 23);

        scrollPane23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane23.setName("scrollPane23"); // NOI18N

        Diagnosa.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Diagnosa.setColumns(20);
        Diagnosa.setRows(5);
        Diagnosa.setName("Diagnosa"); // NOI18N
        Diagnosa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DiagnosaKeyPressed(evt);
            }
        });
        scrollPane23.setViewportView(Diagnosa);

        FormInput.add(scrollPane23);
        scrollPane23.setBounds(230, 470, 561, 50);

        jLabel48.setText("Permintaan Terapi :");
        jLabel48.setName("jLabel48"); // NOI18N
        FormInput.add(jLabel48);
        jLabel48.setBounds(0, 530, 220, 23);

        scrollPane24.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane24.setName("scrollPane24"); // NOI18N

        PermintaanTerapi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        PermintaanTerapi.setColumns(20);
        PermintaanTerapi.setRows(5);
        PermintaanTerapi.setName("PermintaanTerapi"); // NOI18N
        PermintaanTerapi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PermintaanTerapiKeyPressed(evt);
            }
        });
        scrollPane24.setViewportView(PermintaanTerapi);

        FormInput.add(scrollPane24);
        scrollPane24.setBounds(230, 530, 561, 50);

        jLabel49.setText("Anjuran :");
        jLabel49.setName("jLabel49"); // NOI18N
        FormInput.add(jLabel49);
        jLabel49.setBounds(0, 590, 220, 20);

        jLabel44.setText("Evaluasi :");
        jLabel44.setName("jLabel44"); // NOI18N
        FormInput.add(jLabel44);
        jLabel44.setBounds(0, 620, 220, 23);

        jLabel45.setText("Suspek Penyakit Akibat Kerja :");
        jLabel45.setName("jLabel45"); // NOI18N
        FormInput.add(jLabel45);
        jLabel45.setBounds(0, 650, 220, 23);

        AkibatKerja.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "Ya", "Tidak" }));
        AkibatKerja.setName("AkibatKerja"); // NOI18N
        AkibatKerja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AkibatKerjaActionPerformed(evt);
            }
        });
        AkibatKerja.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AkibatKerjaKeyPressed(evt);
            }
        });
        FormInput.add(AkibatKerja);
        AkibatKerja.setBounds(230, 650, 180, 23);

        KetAkibatKerja.setEditable(false);
        KetAkibatKerja.setHighlighter(null);
        KetAkibatKerja.setName("KetAkibatKerja"); // NOI18N
        FormInput.add(KetAkibatKerja);
        KetAkibatKerja.setBounds(420, 650, 370, 23);

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
        Scroll8.setBounds(70, 710, 720, 143);

        label12.setText("Key Word :");
        label12.setName("label12"); // NOI18N
        label12.setPreferredSize(new java.awt.Dimension(60, 23));
        FormInput.add(label12);
        label12.setBounds(70, 860, 60, 23);

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
        TCariProgram.setBounds(140, 860, 550, 23);

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
        BtnCariProgram.setBounds(690, 860, 30, 23);

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
        BtnAllProgram.setBounds(720, 860, 30, 23);

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
        BtnTambahProgram.setBounds(750, 860, 30, 23);

        btnTemplate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnTemplate.setMnemonic('2');
        btnTemplate.setToolTipText("ALt+2");
        btnTemplate.setName("btnTemplate"); // NOI18N
        btnTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTemplateActionPerformed(evt);
            }
        });
        btnTemplate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnTemplateKeyPressed(evt);
            }
        });
        FormInput.add(btnTemplate);
        btnTemplate.setBounds(90, 150, 28, 23);

        Anjuran.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "Terapi 2x/minggu", "Home Exercise Program" }));
        Anjuran.setName("Anjuran"); // NOI18N
        Anjuran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnjuranActionPerformed(evt);
            }
        });
        Anjuran.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AnjuranKeyPressed(evt);
            }
        });
        FormInput.add(Anjuran);
        Anjuran.setBounds(230, 590, 180, 23);

        Evaluasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "2 minggu", "Stop Terapi" }));
        Evaluasi.setName("Evaluasi"); // NOI18N
        Evaluasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EvaluasiActionPerformed(evt);
            }
        });
        Evaluasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EvaluasiKeyPressed(evt);
            }
        });
        FormInput.add(Evaluasi);
        Evaluasi.setBounds(230, 620, 180, 23);

        Scroll1.setViewportView(FormInput);

        PanelInput.add(Scroll1, java.awt.BorderLayout.CENTER);

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

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isRawat();
            isPsien();
        }else{            
            Valid.pindah(evt,TCari,Tanggal);
        }
}//GEN-LAST:event_TNoRwKeyPressed

    private void TPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPasienKeyPressed
        Valid.pindah(evt,TCari,BtnSimpan);
}//GEN-LAST:event_TPasienKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TNoRw.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"Pasien");
        }else if(KdDokter.getText().trim().equals("")||NmDokter.getText().trim().equals("")){
            Valid.textKosong(KdDokter,"Dokter");
        }
        else if(Hubungan.getSelectedItem().equals("Select")){
            Valid.textKosong(Hubungan,"hubungan dengan tertanggung");
        }else if(Hubungan.getSelectedItem().equals("Lainnya")) {
            Valid.textKosong(HubunganLainnya,"hubungan lainnya");
        }else if(Anamnesa.getText().trim().equals("")){
            Valid.textKosong(Anamnesa,"anamnesa");
        }else if(PemeriksaanFisik.getText().trim().equals("")){
            Valid.textKosong(PemeriksaanFisik,"pemeriksaan fisik dan uji fungsi");
        }else if(PemeriksaanFisik.getText().trim().equals("")){
            Valid.textKosong(PemeriksaanFisik,"pemeriksaan fisik dan uji fungsi");
        }else if(DiagnosisFungsional.getText().trim().equals("")){
            Valid.textKosong(DiagnosisFungsional,"diagnosis fungsional");
        }else if(DiagnosisMedis.getText().trim().equals("")){
            Valid.textKosong(DiagnosisMedis,"diagnosis medis");
        }else if(PemeriksaanPenunjang.getText().trim().equals("")){
            Valid.textKosong(PemeriksaanPenunjang,"pemeriksaan penunjang");
        }else if(TataLaksana.getText().trim().equals("")){
            Valid.textKosong(TataLaksana,"tata laksana KFR");
        }else if(Goal.getText().trim().equals("")){
            Valid.textKosong(Goal,"GOAL");
        }else if(Diagnosa.getText().trim().equals("")){
            Valid.textKosong(Diagnosa,"diagnosa");
        }else if(PermintaanTerapi.getText().trim().equals("")){
            Valid.textKosong(PermintaanTerapi,"permintaan terapi");
        }else if(Anjuran.getSelectedItem().equals("Select")){
            Valid.textKosong(Anjuran,"anjuran");
        }else if(Evaluasi.getSelectedItem().equals("Select")){
            Valid.textKosong(Evaluasi,"evaluasi");
        }else if(AkibatKerja.getSelectedItem().equals("Select")){
            Valid.textKosong(AkibatKerja,"suspek penyakit akibat kerja");
        }else if(AkibatKerja.getSelectedItem().equals("Ya")){
            Valid.textKosong(KetAkibatKerja,"keterangan suspek");
        }
        
        else{
            String hub = Hubungan.getSelectedItem().toString();
            if(HubunganLainnya.getText().equals("Lainnya")){
                hub = HubunganLainnya.getText();
            } 
            
            if(Sequel.menyimpantf("tb_uji_fungsi_kfr","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?","Data",18,new String[]{
                TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),
                Valid.SetTgl(Tanggal1.getSelectedItem()+""),
                hub, 
                Anamnesa.getText(),PemeriksaanFisik.getText(),
                DiagnosisFungsional.getText(),DiagnosisMedis.getText(),
                PemeriksaanPenunjang.getText(),TataLaksana.getText(),Goal.getText(),
                Anjuran.getSelectedItem().toString(), Evaluasi.getSelectedItem().toString(),
                AkibatKerja.getSelectedItem().toString(), KetAkibatKerja.getText(),
                Diagnosa.getText(),PermintaanTerapi.getText(),KdDokter.getText()
                
            })==true){
                tampil();
                emptTeks();
            }   
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
            //Valid.pindah(evt,cmbSkor3,BtnBatal);
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
        if(tbObat.getSelectedRow()!= -1){
            if(akses.getkode().equals("Admin Utama")){
                hapus();
            }else{
                if(KdDokter.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(),12).toString())){
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
        if(TNoRw.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"Pasien");
        }else if(KdDokter.getText().trim().equals("")||NmDokter.getText().trim().equals("")){
            Valid.textKosong(KdDokter,"Dokter");
        }else if(DiagnosisFungsional.getText().trim().equals("")){
            Valid.textKosong(DiagnosisFungsional,"Diagnosis Fungsional");
        }else if(DiagnosisMedis.getText().trim().equals("")){
            Valid.textKosong(DiagnosisMedis,"Diagnosis Medis");
        }else if(HasilYangDidapat.getText().trim().equals("")){
            Valid.textKosong(HasilYangDidapat,"Hasil Yang Didapat");
        }else if(Kesimpulan.getText().trim().equals("")){
            Valid.textKosong(Kesimpulan,"Kesimpulan");
        }else if(Rekomendasi.getText().trim().equals("")){
            Valid.textKosong(Rekomendasi,"Rekomendasi");
        }else{     
            if(tbObat.getSelectedRow()>-1){
                if(akses.getkode().equals("Admin Utama")){
                    ganti();
                }else{
                    if(KdDokter.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(),12).toString())){
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
            Valid.pindah(evt, BtnHapus, BtnPrint);
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

    private void TanggalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalKeyPressed
        Valid.pindah(evt,TCari,Jam);
}//GEN-LAST:event_TanggalKeyPressed

    private void TNoRMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRMKeyPressed
        // Valid.pindah(evt, TNm, BtnSimpan);
}//GEN-LAST:event_TNoRMKeyPressed

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
            }
        }
}//GEN-LAST:event_tbObatKeyPressed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void JamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JamKeyPressed
        Valid.pindah(evt,Tanggal,Menit);
    }//GEN-LAST:event_JamKeyPressed

    private void MenitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MenitKeyPressed
        Valid.pindah(evt,Jam,Detik);
    }//GEN-LAST:event_MenitKeyPressed

    private void DetikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DetikKeyPressed
        Valid.pindah(evt,Menit,btnPetugas);
    }//GEN-LAST:event_DetikKeyPressed

    private void KdDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdDokterKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            NmDokter.setText(dokter.tampil3(KdDokter.getText()));
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            Detik.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            //Alergi.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            btnPetugasActionPerformed(null);
        }
    }//GEN-LAST:event_KdDokterKeyPressed

    private void btnPetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPetugasActionPerformed
        dokter.emptTeks();
        dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_btnPetugasActionPerformed

    private void btnPetugasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPetugasKeyPressed
        Valid.pindah(evt,Detik,DiagnosisFungsional);
    }//GEN-LAST:event_btnPetugasKeyPressed

    private void MnUjiFungsiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnUjiFungsiActionPerformed
        if(tbObat.getSelectedRow()>-1){
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());   
            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbObat.getValueAt(tbObat.getSelectedRow(),12).toString());
            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+tbObat.getValueAt(tbObat.getSelectedRow(),13).toString()+"\nID "+(finger.equals("")?tbObat.getValueAt(tbObat.getSelectedRow(),12).toString():finger)+"\n"+Tanggal.getSelectedItem()); 
            Valid.MyReportqry("rptCetakUjiFungsiKFR.jasper","report","::[ Formulir/Lembar Uji Fungsi/Prosedur KFR ]::",
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur,"+
                    "pasien.jk,uji_fungsi_kfr.tanggal,uji_fungsi_kfr.diagnosis_fungsional,uji_fungsi_kfr.diagnosis_medis,uji_fungsi_kfr.hasil_didapat,"+
                    "uji_fungsi_kfr.kesimpulan,uji_fungsi_kfr.rekomedasi,uji_fungsi_kfr.kd_dokter,dokter.nm_dokter,date_format(pasien.tgl_lahir,'%d-%m-%Y') as lahir, "+
                    "date_format(uji_fungsi_kfr.tanggal,'%d-%m-%Y') as periksa from uji_fungsi_kfr inner join reg_periksa on uji_fungsi_kfr.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join dokter on uji_fungsi_kfr.kd_dokter=dokter.kd_dokter where reg_periksa.no_rawat='"+tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()+"'",param);
        }
    }//GEN-LAST:event_MnUjiFungsiActionPerformed

    private void DiagnosisFungsionalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosisFungsionalKeyPressed
        Valid.pindah(evt,btnPetugas,DiagnosisMedis);
    }//GEN-LAST:event_DiagnosisFungsionalKeyPressed

    private void JKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JKKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_JKKeyPressed

    private void DiagnosisMedisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosisMedisKeyPressed
        Valid.pindah(evt,DiagnosisFungsional,HasilYangDidapat);
    }//GEN-LAST:event_DiagnosisMedisKeyPressed

    private void KdPjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KdPjActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KdPjActionPerformed

    private void CaraBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CaraBayarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CaraBayarActionPerformed

    private void Tanggal1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tanggal1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Tanggal1KeyPressed

    private void HubunganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HubunganActionPerformed
        // TODO add your handling code here:
        if (Hubungan.getSelectedItem().toString().equalsIgnoreCase("Lainnya")) {
            HubunganLainnya.setEnabled(true);
            HubunganLainnya.requestFocus();
        } else {
            HubunganLainnya.setEnabled(false);
            HubunganLainnya.setText(""); // Clear input if "Other" is not selected
        }
    }//GEN-LAST:event_HubunganActionPerformed

    private void HubunganKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HubunganKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_HubunganKeyPressed

    private void AnamnesaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AnamnesaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_AnamnesaKeyPressed

    private void PemeriksaanFisikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PemeriksaanFisikKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PemeriksaanFisikKeyPressed

    private void PemeriksaanPenunjangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PemeriksaanPenunjangKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PemeriksaanPenunjangKeyPressed

    private void TataLaksanaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TataLaksanaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TataLaksanaKeyPressed

    private void GoalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GoalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_GoalKeyPressed

    private void DiagnosaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DiagnosaKeyPressed

    private void PermintaanTerapiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PermintaanTerapiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PermintaanTerapiKeyPressed

    private void AkibatKerjaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AkibatKerjaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AkibatKerjaActionPerformed

    private void AkibatKerjaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AkibatKerjaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_AkibatKerjaKeyPressed

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

    private void TCariProgramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TCariProgramActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TCariProgramActionPerformed

    private void TCariProgramKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariProgramKeyPressed
        //        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            //            tampilMasalah2();
            //        }else if((evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN)||(evt.getKeyCode()==KeyEvent.VK_TAB)){
            //            Rencana.requestFocus();
            //        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            //            KetDokter.requestFocus();
            //        }
    }//GEN-LAST:event_TCariProgramKeyPressed

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

    private void BtnTambahProgramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahProgramActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        MasterMasalahKeperawatanIGD form=new MasterMasalahKeperawatanIGD(null,false);
        form.isCek();
        form.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        form.setLocationRelativeTo(internalFrame1);
        form.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnTambahProgramActionPerformed

    private void btnTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTemplateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTemplateActionPerformed

    private void btnTemplateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnTemplateKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTemplateKeyPressed

    private void AnjuranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnjuranActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AnjuranActionPerformed

    private void AnjuranKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AnjuranKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_AnjuranKeyPressed

    private void EvaluasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EvaluasiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EvaluasiActionPerformed

    private void EvaluasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EvaluasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_EvaluasiKeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMUjiFungsiKFR dialog = new RMUjiFungsiKFR(new javax.swing.JFrame(), true);
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
    private widget.ComboBox AkibatKerja;
    private widget.TextBox Alamat;
    private widget.TextArea Anamnesa;
    private widget.ComboBox Anjuran;
    private widget.Button BtnAll;
    private widget.Button BtnAllProgram;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnCariProgram;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnSimpan;
    private widget.Button BtnTambahProgram;
    private widget.TextBox CaraBayar;
    private widget.CekBox ChkInput;
    private widget.CekBox ChkKejadian;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.ComboBox Detik;
    private widget.TextArea Diagnosa;
    private widget.TextBox DiagnosisFungsional;
    private widget.TextBox DiagnosisMedis;
    private widget.ComboBox Evaluasi;
    private widget.PanelBiasa FormInput;
    private widget.TextArea Goal;
    private widget.ComboBox Hubungan;
    private widget.TextBox HubunganLainnya;
    private widget.TextBox JK;
    private widget.ComboBox Jam;
    private widget.TextBox KdDokter;
    private widget.TextBox KdPj;
    private widget.TextBox KetAkibatKerja;
    private widget.Label LCount;
    private widget.ComboBox Menit;
    private javax.swing.JMenuItem MnUjiFungsi;
    private widget.TextBox NmDokter;
    private javax.swing.JPanel PanelInput;
    private widget.TextArea PemeriksaanFisik;
    private widget.TextArea PemeriksaanPenunjang;
    private widget.TextArea PermintaanTerapi;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane Scroll1;
    private widget.ScrollPane Scroll8;
    private widget.TextBox TCari;
    private widget.TextBox TCariProgram;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.Tanggal Tanggal;
    private widget.Tanggal Tanggal1;
    private widget.TextArea TataLaksana;
    private widget.TextBox Telepon;
    private widget.TextBox TglLahir;
    private widget.Button btnPetugas;
    private widget.Button btnTemplate;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel14;
    private widget.Label jLabel16;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private widget.Label jLabel21;
    private widget.Label jLabel22;
    private widget.Label jLabel23;
    private widget.Label jLabel25;
    private widget.Label jLabel36;
    private widget.Label jLabel37;
    private widget.Label jLabel38;
    private widget.Label jLabel4;
    private widget.Label jLabel41;
    private widget.Label jLabel42;
    private widget.Label jLabel44;
    private widget.Label jLabel45;
    private widget.Label jLabel46;
    private widget.Label jLabel47;
    private widget.Label jLabel48;
    private widget.Label jLabel49;
    private widget.Label jLabel50;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.Label label12;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.ScrollPane scrollPane12;
    private widget.ScrollPane scrollPane13;
    private widget.ScrollPane scrollPane18;
    private widget.ScrollPane scrollPane19;
    private widget.ScrollPane scrollPane22;
    private widget.ScrollPane scrollPane23;
    private widget.ScrollPane scrollPane24;
    private widget.Table tbObat;
    private widget.Table tbProgramFisio;
    // End of variables declaration//GEN-END:variables
    
    public void tampil() {
        Valid.tabelKosong(tabMode);
        try{
            if(TCari.getText().toString().trim().equals("")){
                ps=koneksi.prepareStatement(
                    "SELECT reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur, " +
                    "pasien.jk,tb_uji_fungsi_kfr.tanggal_mulai,tb_uji_fungsi_kfr.tanggal_selesai, " +
                    "tb_uji_fungsi_kfr.hubungan, tb_uji_fungsi_kfr.anamnesa, tb_uji_fungsi_kfr.pemeriksaan_fisik, tb_uji_fungsi_kfr.diagnosis_medis, " +
                    "tb_uji_fungsi_kfr.diagnosis_fungsional, tb_uji_fungsi_kfr.pemeriksaan_penunjang,  tb_uji_fungsi_kfr.tata_laksana_kfr, " +
                    "tb_uji_fungsi_kfr.goal, tb_uji_fungsi_kfr.anjuran, tb_uji_fungsi_kfr.evaluasi, " +
                    "tb_uji_fungsi_kfr.suspek_akibat_kerja, tb_uji_fungsi_kfr.ket_suspek, tb_uji_fungsi_kfr.diagnosa, " +
                    "tb_uji_fungsi_kfr.permintaan_terapi, " +
                    "tb_uji_fungsi_kfr.kd_dokter,dokter.nm_dokter,DATE_FORMAT(pasien.tgl_lahir,'%d-%m-%Y') AS lahir " +
                    "FROM tb_uji_fungsi_kfr INNER JOIN reg_periksa ON tb_uji_fungsi_kfr.no_rawat=reg_periksa.no_rawat " +
                    "INNER JOIN pasien ON reg_periksa.no_rkm_medis=pasien.no_rkm_medis " +
                    "INNER JOIN dokter ON tb_uji_fungsi_kfr.kd_dokter=dokter.kd_dokter WHERE " +
                    "tb_uji_fungsi_kfr.tanggal_mulai BETWEEN ? AND ? ORDER BY tb_uji_fungsi_kfr.tanggal_mulai ");
            }else{
                ps=koneksi.prepareStatement(
                    "SELECT reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur, " +
                    "pasien.jk,tb_uji_fungsi_kfr.tanggal_mulai,tb_uji_fungsi_kfr.tanggal_selesai, " +
                    "tb_uji_fungsi_kfr.hubungan, tb_uji_fungsi_kfr.anamnesa, tb_uji_fungsi_kfr.pemeriksaan_fisik, tb_uji_fungsi_kfr.diagnosis_medis, " +
                    "tb_uji_fungsi_kfr.diagnosis_fungsional, tb_uji_fungsi_kfr.pemeriksaan_penunjang,  tb_uji_fungsi_kfr.tata_laksana_kfr, " +
                    "tb_uji_fungsi_kfr.goal, tb_uji_fungsi_kfr.anjuran, tb_uji_fungsi_kfr.evaluasi, " +
                    "tb_uji_fungsi_kfr.suspek_akibat_kerja, tb_uji_fungsi_kfr.ket_suspek, tb_uji_fungsi_kfr.diagnosa, " +
                    "tb_uji_fungsi_kfr.permintaan_terapi, " +
                    "tb_uji_fungsi_kfr.kd_dokter,dokter.nm_dokter,DATE_FORMAT(pasien.tgl_lahir,'%d-%m-%Y') AS lahir " +
                    "FROM tb_uji_fungsi_kfr INNER JOIN reg_periksa ON tb_uji_fungsi_kfr.no_rawat=reg_periksa.no_rawat " +
                    "INNER JOIN pasien ON reg_periksa.no_rkm_medis=pasien.no_rkm_medis " +
                    "INNER JOIN dokter ON tb_uji_fungsi_kfr.kd_dokter=dokter.kd_dokter WHERE " +
                    "tb_uji_fungsi_kfr.tanggal_mulai BETWEEN ? AND ? AND " +
                    "(reg_periksa.no_rawat LIKE ? OR pasien.no_rkm_medis LIKE ? OR pasien.nm_pasien LIKE ? OR tb_uji_fungsi_kfr.kd_dokter LIKE ? OR dokter.nm_dokter LIKE ?) " +
                    "ORDER BY tb_uji_fungsi_kfr.tanggal_mulai ");
            }
                
            try {
                if(TCari.getText().toString().trim().equals("")){
                    ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                }else{
                    ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                    ps.setString(3,"%"+TCari.getText()+"%");
                    ps.setString(4,"%"+TCari.getText()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                }
                    
                rs=ps.executeQuery();
                while(rs.next()){
                    tabMode.addRow(new String[]{
                        rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),
                        rs.getString("umurdaftar")+" "+rs.getString("sttsumur"),rs.getString("jk"),
                        rs.getString("lahir"),rs.getString("tanggal_mulai"),rs.getString("tanggal_selesai"),
                        rs.getString("hubungan"),rs.getString("anamnesa"),rs.getString("pemeriksaan_fisik"),
                        rs.getString("diagnosis_fungsional"),rs.getString("diagnosis_medis"),
                        rs.getString("pemeriksaan_penunjang"),rs.getString("tata_laksana_kfr"),
                        rs.getString("goal"),rs.getString("anjuran"),rs.getString("evaluasi"),
                        rs.getString("suspek_akibat_kerja"),rs.getString("ket_suspek"),
                        rs.getString("diagnosa"),
                        rs.getString("permintaan_terapi"),rs.getString("kd_dokter"),rs.getString("nm_dokter")
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
        }catch(SQLException e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabMode.getRowCount());
    }
    
    public void emptTeks() {
        DiagnosisFungsional.setText("");
        DiagnosisMedis.setText("");
        DiagnosisFungsional.requestFocus();
        
        Hubungan.setSelectedItem("Select");
        HubunganLainnya.setText("");
        Anamnesa.setText("");
        PemeriksaanFisik.setText("");
        PemeriksaanPenunjang.setText("");
        TataLaksana.setText("");
        Goal.setText("");
        Diagnosa.setText("");
        PermintaanTerapi.setText("");
        KetAkibatKerja.setText("");
        AkibatKerja.setSelectedItem("Select");
        
        
    } 

    private void getData() {
        if(tbObat.getSelectedRow()!= -1){
            // "No.Rawat","No.R.M.","Nama Pasien","Umur","JK","Tgl.Lahir","Tanggal Mulai",
            "Tanggal Kontrol","Hubungan","Anamnesa","Pemeriksaan Fisik",
            "Diagnosis Fungsional","Diagnosis Medis",
            "Pemeriksaan Penunjang","Tata Laksana KFR","GOAL","Anjuran",
            "Evaluasi","Suspek Akibat Kerja","Keterangan Suspek","Diagnosa","Permintaan Terapi","Kode Dokter","Nama Dokter"
            TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());
            JK.setText(tbObat.getValueAt(tbObat.getSelectedRow(),4).toString());
            TglLahir.setText(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
            
            Valid.SetTgl(Tanggal,tbObat.getValueAt(tbObat.getSelectedRow(),6).toString());
            Valid.SetTgl(Tanggal1,tbObat.getValueAt(tbObat.getSelectedRow(),7).toString());
            
            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
            DiagnosisFungsional.setText(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString());
            DiagnosisMedis.setText(tbObat.getValueAt(tbObat.getSelectedRow(),8).toString());
            HasilYangDidapat.setText(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString());
            Kesimpulan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString());
            Rekomendasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),11).toString());
            
            Valid.SetTgl(Tanggal,tbObat.getValueAt(tbObat.getSelectedRow(),6).toString());
            Jam.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString().substring(11,13));
            Menit.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString().substring(14,15));
            Detik.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString().substring(17,19));
        }
    }
    private void isRawat() {
         Sequel.cariIsi("select reg_periksa.no_rkm_medis from reg_periksa where reg_periksa.no_rawat='"+TNoRw.getText()+"' ",TNoRM);
    }

    private void isPsien() {
        Sequel.cariIsi("select pasien.nm_pasien from pasien where pasien.no_rkm_medis='"+TNoRM.getText()+"' ",TPasien);
        Sequel.cariIsi("select pasien.jk from pasien where pasien.no_rkm_medis='"+TNoRM.getText()+"' ",JK);
        Sequel.cariIsi("select date_format(pasien.tgl_lahir,'%d-%m-%Y') from pasien where pasien.no_rkm_medis=? ",TglLahir,TNoRM.getText());
    }
    
    public void setNoRm(String norwt, Date tgl2) {
        TNoRw.setText(norwt);
        
        Sequel.cariIsi("SELECT reg_periksa.kd_pj FROM reg_periksa WHERE reg_periksa.no_rawat='"+norwt+"'", KdPj);
        Sequel.cariIsi("SELECT penjab.png_jawab FROM penjab WHERE penjab.kd_pj='"+KdPj.getText()+"'", CaraBayar);
        Telepon.setText(norwt);
        Alamat.setText(norwt);
        
        TCari.setText(norwt);
        Sequel.cariIsi("select reg_periksa.tgl_registrasi from reg_periksa where reg_periksa.no_rawat='"+norwt+"'", DTPCari1);
        DTPCari2.setDate(tgl2);
        isRawat();
        isPsien();
        ChkInput.setSelected(true);
        isForm();
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,274));
            FormInput.setVisible(true);      
            ChkInput.setVisible(true);
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            FormInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }
    
    public void isCek(){
        BtnSimpan.setEnabled(akses.getuji_fungsi_kfr());
        BtnHapus.setEnabled(akses.getuji_fungsi_kfr());
        BtnEdit.setEnabled(akses.getuji_fungsi_kfr());
        BtnPrint.setEnabled(akses.getuji_fungsi_kfr()); 
        if(akses.getjml2()>=1){
            KdDokter.setEditable(false);
            btnPetugas.setEnabled(false);
            KdDokter.setText(akses.getkode());
            NmDokter.setText(dokter.tampil3(KdDokter.getText()));
            if(NmDokter.getText().equals("")){
                KdDokter.setText("");
                JOptionPane.showMessageDialog(null,"User login bukan dokter...!!");
            }
        }            
    }

    private void jam(){
        ActionListener taskPerformer = new ActionListener(){
            private int nilai_jam;
            private int nilai_menit;
            private int nilai_detik;
            public void actionPerformed(ActionEvent e) {
                String nol_jam = "";
                String nol_menit = "";
                String nol_detik = "";
                
                Date now = Calendar.getInstance().getTime();

                // Mengambil nilaj JAM, MENIT, dan DETIK Sekarang
                if(ChkKejadian.isSelected()==true){
                    nilai_jam = now.getHours();
                    nilai_menit = now.getMinutes();
                    nilai_detik = now.getSeconds();
                }else if(ChkKejadian.isSelected()==false){
                    nilai_jam =Jam.getSelectedIndex();
                    nilai_menit =Menit.getSelectedIndex();
                    nilai_detik =Detik.getSelectedIndex();
                }

                // Jika nilai JAM lebih kecil dari 10 (hanya 1 digit)
                if (nilai_jam <= 9) {
                    // Tambahkan "0" didepannya
                    nol_jam = "0";
                }
                // Jika nilai MENIT lebih kecil dari 10 (hanya 1 digit)
                if (nilai_menit <= 9) {
                    // Tambahkan "0" didepannya
                    nol_menit = "0";
                }
                // Jika nilai DETIK lebih kecil dari 10 (hanya 1 digit)
                if (nilai_detik <= 9) {
                    // Tambahkan "0" didepannya
                    nol_detik = "0";
                }
                // Membuat String JAM, MENIT, DETIK
                String jam = nol_jam + Integer.toString(nilai_jam);
                String menit = nol_menit + Integer.toString(nilai_menit);
                String detik = nol_detik + Integer.toString(nilai_detik);
                // Menampilkan pada Layar
                //tampil_jam.setText("  " + jam + " : " + menit + " : " + detik + "  ");
                Jam.setSelectedItem(jam);
                Menit.setSelectedItem(menit);
                Detik.setSelectedItem(detik);
            }
        };
        // Timer
        new Timer(1000, taskPerformer).start();
    }

    private void ganti() {
        Sequel.mengedit("uji_fungsi_kfr","no_rawat=?","no_rawat=?,tanggal=?,diagnosis_fungsional=?,diagnosis_medis=?,hasil_didapat=?,kesimpulan=?,rekomedasi=?,kd_dokter=?",9,new String[]{
            TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),
            DiagnosisFungsional.getText(),DiagnosisMedis.getText(),HasilYangDidapat.getText(),Kesimpulan.getText(),Rekomendasi.getText(),KdDokter.getText(),
            tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
        });
        if(tabMode.getRowCount()!=0){tampil();}
        emptTeks();
    }

    private void hapus() {
        if(Sequel.queryu2tf("delete from uji_fungsi_kfr where no_rawat=?",1,new String[]{
            tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
        })==true){
            tabMode.removeRow(tbObat.getSelectedRow());
            LCount.setText(""+tabMode.getRowCount());
            emptTeks();
        }else{
            JOptionPane.showMessageDialog(null,"Gagal menghapus..!!");
        }
    }
}
