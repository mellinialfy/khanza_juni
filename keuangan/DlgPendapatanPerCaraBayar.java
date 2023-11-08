/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgLhtBiaya.java
 *
 * Created on 12 Jul 10, 16:21:34
 */

package keuangan;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 *
 * @author perpustakaan
 */
public final class DlgPendapatanPerCaraBayar extends javax.swing.JDialog {
    private final Connection koneksi=koneksiDB.condb();
    private final sekuel Sequel=new sekuel();
    private final validasi Valid=new validasi();
    private PreparedStatement ps,ps1,ps2,ps3,ps4,ps5,ps6,ps7,ps8;
    private ResultSet rs,rs1,rs2,rs3,rs4,rs5,rs6,rs7,rs8;
    private double nilairalancash=0,nilaimcucash=0,nilairanapcash=0,nilairalanpenjamin=0,nilaimcupenjamin=0,nilairanappenjamin=0,
                ttlnilairalancash=0,ttlnilaimcucash=0,ttlnilairanapcash=0,ttlnilairalanpenjamin=0,ttlnilaimcupenjamin=0,ttlnilairanappenjamin=0;
    private int i,no, jmlralan=0,jmlmcu=0,jmlranap=0,ttlralan=0,ttlmcu=0,ttlranap=0;
    private StringBuilder htmlContent;
    
    
    private double all=0,Laborat=0,Radiologi=0,Obat=0,Ralan_Dokter=0,Ralan_Dokter_paramedis=0,Ralan_Paramedis=0,Tambahan=0,Potongan=0,Registrasi=0,
                    ttlLaborat=0,ttlRadiologi=0,ttlObat=0,ttlRalan_Dokter=0,ttlRalan_Paramedis=0,ttlTambahan=0,ttlPotongan=0,ttlRegistrasi=0,
                   Operasi=0,ttlOperasi=0,ttlVk,ttlUgd;
    private double Ralan_Dokter1=0,Ralan_Dokter_paramedis1=0,Ralan_Paramedis1=0,Tambahan1=0,Potongan1=0,Registrasi1=0;
    private double PotonganLab=0, PotonganRad=0, PotonganVk=0, PotonganUgd=0,TambahanLab=0,TambahanRad=0,TambahanVk=0,TambahanUgd=0;
    private double ttlRalan_Dokter1=0,ttlRalan_Dokter_paramedis1=0,ttlRalan_Paramedis1=0,ttlTambahan1=0,ttlPotongan1=0,ttlRegistrasi1=0;
    private final DefaultTableModel tabMode1, tabMode, tabMode2, tabMode3;
    
    private double Ranap_Dokter=0,Ranap_Paramedis=0,Ranap_Dokter_Paramedis=0,Ralan_Dokter_Paramedis=0,Kamar=0,
            Harian=0,Retur_Obat=0,Resep_Pulang=0,
            Service=0,ttlRanap_Dokter=0,ttlRanap_Paramedis=0,
            ttlKamar=0,ttlHarian=0,ttlRetur_Obat=0,ttlResep_Pulang=0,ttlService=0;
    private double Ranap_Dokter1=0,Ranap_Paramedis1=0,Ranap_Dokter_Paramedis1=0,
            Kamar1=0,Harian1=0,Retur_Obat1=0,Resep_Pulang1=0,Ralan_Dokter_Paramedis1=0,
            Service1=0,ttlRanap_Dokter1=0,ttlRanap_Paramedis1=0,
            ttlKamar1=0,ttlHarian1=0,ttlRetur_Obat1=0,ttlResep_Pulang1=0,ttlService1=0;
    private double Ranap_Dokter2=0,Ranap_Paramedis2=0,Ranap_Dokter_Paramedis2=0,Ralan_Dokter_Paramedis2=0,
            Kamar2=0,Harian2=0,Retur_Obat2=0,Resep_Pulang2=0,
            Service2=0,ttlRanap_Dokter2=0,ttlRanap_Paramedis2=0,
            ttlKamar2=0,ttlHarian2=0,ttlRetur_Obat2=0,ttlResep_Pulang2=0,ttlService2=0;
    private double Ralan_Dokter2=0,Ralan_Dokter_paramedis2=0,Ralan_Paramedis2=0,Tambahan2=0,Potongan2=0,Registrasi2=0,
            ttlRalan_Dokter2=0,ttlRalan_Paramedis2=0,ttlTambahan2=0,ttlPotongan2=0,ttlRegistrasi2=0;
    private double Kamar_ServiceLab=0,Kamar_ServiceRad=0,Kamar_ServiceVk=0,Kamar_ServiceUgd=0,
            HarianLab=0,HarianRad=0,HarianVk=0,HarianUgd=0,QtyLab=0,QtyRad=0,QtyVk=0,QtyUgd=0,QtyPoli=0;
    
    private double ttlVkUmum=0,ttlUgdUmum=0,ttlRadUmum=0,ttlLabUmum=0,ttlOperasiUmum=0,ttlObatUmum=0,ttlPoliUmum=0;
    private double ttlVkIks=0,ttlUgdIks=0,ttlRadIks=0,ttlLabIks=0,ttlOperasiIks=0,ttlObatIks=0,ttlPoliIks=0;
    private double ttlVkBpjs=0,ttlUgdBpjs=0,ttlRadBpjs=0,ttlLabBpjs=0,ttlOperasiBpjs=0,ttlObatBpjs=0,ttlPoliBpjs=0;
    
    private double ttlNotaPoli=0,ttlNotaVk=0,ttlNotaUgd=0,ttlNotaLab=0,ttlNotaRad=0;
    double BayarDeposit=0;
    String sqty,stambahan,spotongan,stotal,sharian,skamarservice, nama_poli;
    double dqty,dtambahan, dpotongan, dtotal,dharian,dkamarservice,jumlah;
    

    /** Creates new form DlgLhtBiaya
     * @param parent
     * @param modal */
    public DlgPendapatanPerCaraBayar(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8,1);
        setSize(885,674);
        
        Object[] rowRwJlDr1={"Nomor","Unit","Penjamin","Qty","Tambahan","Potongan","Jumlah","Total","Total Bayar"};
        tabMode1=new DefaultTableModel(null,rowRwJlDr1){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        
        Object[] rowRwJlDr={"Nomor","Unit","Penjamin","Qty","Tambahan","Potongan",
                            "Kamar+Service","Harian","Jumlah","Total","Total Nota"};
        tabMode=new DefaultTableModel(null,rowRwJlDr){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        
        Object[] rowRwJlDr2={"","nota","Qty","Umum","IKS",
                            "BPJS","Total"};
        tabMode2=new DefaultTableModel(null,rowRwJlDr2){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        
        Object[] rowRwJlDr3={"","nota","Qty","Umum","IKS",
                            "BPJS","Total"};
        tabMode3=new DefaultTableModel(null,rowRwJlDr3){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };

        TCari.setDocument(new batasInput((byte)100).getKata(TCari));
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
        LoadHTML.setEditable(true);
        HTMLEditorKit kit = new HTMLEditorKit();
        LoadHTML.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule(
                ".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                ".isi2 td{font: 8.5px tahoma;height:12px;background: #ffffff;color:#323232;}"+
                ".isi3 td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                ".isi4 td{font: 11px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"
        );
        Document doc = kit.createDefaultDocument();
        LoadHTML.setDocument(doc);
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
        panelGlass5 = new widget.panelisi();
        label17 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        jLabel11 = new javax.swing.JLabel();
        BtnAll = new widget.Button();
        BtnPrint = new widget.Button();
        BtnPrint1 = new widget.Button();
        BtnKeluar = new widget.Button();
        panelGlass6 = new widget.panelisi();
        label11 = new widget.Label();
        Tgl1 = new widget.Tanggal();
        jLabel9 = new widget.Label();
        CmbJam = new widget.ComboBox();
        CmbMenit = new widget.ComboBox();
        CmbDetik = new widget.ComboBox();
        label12 = new widget.Label();
        Tgl2 = new widget.Tanggal();
        jLabel10 = new widget.Label();
        CmbJam2 = new widget.ComboBox();
        CmbMenit2 = new widget.ComboBox();
        CmbDetik2 = new widget.ComboBox();
        Scroll = new widget.ScrollPane();
        LoadHTML = new widget.editorpane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Pendapatan Per Cara Bayar (Berdasarkan Closing Billing) ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass5.setName("panelGlass5"); // NOI18N
        panelGlass5.setPreferredSize(new java.awt.Dimension(55, 55));
        panelGlass5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        label17.setText("Key Word :");
        label17.setName("label17"); // NOI18N
        label17.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass5.add(label17);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(320, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass5.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('2');
        BtnCari.setToolTipText("Alt+2");
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
        panelGlass5.add(BtnCari);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(50, 50, 50));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setName("jLabel11"); // NOI18N
        jLabel11.setPreferredSize(new java.awt.Dimension(30, 23));
        panelGlass5.add(jLabel11);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setText("Semua");
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(100, 30));
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
        panelGlass5.add(BtnAll);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setText("Cetak");
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setName("BtnPrint"); // NOI18N
        BtnPrint.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });
        BtnPrint.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPrintKeyPressed(evt);
            }
        });
        panelGlass5.add(BtnPrint);

        BtnPrint1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint1.setMnemonic('T');
        BtnPrint1.setText("Rekapan");
        BtnPrint1.setToolTipText("Alt+T");
        BtnPrint1.setName("BtnPrint1"); // NOI18N
        BtnPrint1.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnPrint1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrint1ActionPerformed(evt);
            }
        });
        BtnPrint1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPrint1KeyPressed(evt);
            }
        });
        panelGlass5.add(BtnPrint1);

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
        panelGlass5.add(BtnKeluar);

        internalFrame1.add(panelGlass5, java.awt.BorderLayout.PAGE_END);

        panelGlass6.setName("panelGlass6"); // NOI18N
        panelGlass6.setPreferredSize(new java.awt.Dimension(55, 45));
        panelGlass6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        label11.setText("Periode :");
        label11.setName("label11"); // NOI18N
        label11.setPreferredSize(new java.awt.Dimension(53, 23));
        panelGlass6.add(label11);

        Tgl1.setDisplayFormat("dd-MM-yyyy");
        Tgl1.setName("Tgl1"); // NOI18N
        Tgl1.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass6.add(Tgl1);

        jLabel9.setText("Jam :");
        jLabel9.setName("jLabel9"); // NOI18N
        jLabel9.setPreferredSize(new java.awt.Dimension(40, 23));
        panelGlass6.add(jLabel9);

        CmbJam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        CmbJam.setName("CmbJam"); // NOI18N
        CmbJam.setPreferredSize(new java.awt.Dimension(62, 23));
        CmbJam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CmbJamKeyPressed(evt);
            }
        });
        panelGlass6.add(CmbJam);

        CmbMenit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        CmbMenit.setName("CmbMenit"); // NOI18N
        CmbMenit.setPreferredSize(new java.awt.Dimension(62, 23));
        CmbMenit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CmbMenitKeyPressed(evt);
            }
        });
        panelGlass6.add(CmbMenit);

        CmbDetik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        CmbDetik.setName("CmbDetik"); // NOI18N
        CmbDetik.setPreferredSize(new java.awt.Dimension(62, 23));
        CmbDetik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CmbDetikKeyPressed(evt);
            }
        });
        panelGlass6.add(CmbDetik);

        label12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label12.setText("s.d.");
        label12.setName("label12"); // NOI18N
        label12.setPreferredSize(new java.awt.Dimension(25, 23));
        panelGlass6.add(label12);

        Tgl2.setDisplayFormat("dd-MM-yyyy");
        Tgl2.setName("Tgl2"); // NOI18N
        Tgl2.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass6.add(Tgl2);

        jLabel10.setText("Jam :");
        jLabel10.setName("jLabel10"); // NOI18N
        jLabel10.setPreferredSize(new java.awt.Dimension(40, 23));
        panelGlass6.add(jLabel10);

        CmbJam2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        CmbJam2.setSelectedIndex(23);
        CmbJam2.setName("CmbJam2"); // NOI18N
        CmbJam2.setPreferredSize(new java.awt.Dimension(62, 23));
        CmbJam2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CmbJam2KeyPressed(evt);
            }
        });
        panelGlass6.add(CmbJam2);

        CmbMenit2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        CmbMenit2.setSelectedIndex(59);
        CmbMenit2.setName("CmbMenit2"); // NOI18N
        CmbMenit2.setPreferredSize(new java.awt.Dimension(62, 23));
        CmbMenit2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CmbMenit2KeyPressed(evt);
            }
        });
        panelGlass6.add(CmbMenit2);

        CmbDetik2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        CmbDetik2.setSelectedIndex(59);
        CmbDetik2.setName("CmbDetik2"); // NOI18N
        CmbDetik2.setPreferredSize(new java.awt.Dimension(62, 23));
        CmbDetik2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CmbDetik2KeyPressed(evt);
            }
        });
        panelGlass6.add(CmbDetik2);

        internalFrame1.add(panelGlass6, java.awt.BorderLayout.PAGE_START);

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        LoadHTML.setBorder(null);
        LoadHTML.setName("LoadHTML"); // NOI18N
        Scroll.setViewportView(LoadHTML);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {            
            File g = new File("pendapatanpercarabayar.css");            
            BufferedWriter bg = new BufferedWriter(new FileWriter(g));
            bg.write(
                ".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                ".isi2 td{font: 8.5px tahoma;height:12px;background: #ffffff;color:#323232;}"+
                ".isi3 td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                ".isi4 td{font: 11px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"
            );
            bg.close();
            
            File f = new File("PendapatanPerCaraBayar.html");            
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));            
            bw.write(LoadHTML.getText().replaceAll("<head>","<head><link href=\"pendapatanpercarabayar.css\" rel=\"stylesheet\" type=\"text/css\" />"+
                        "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                            "<tr class='isi2'>"+
                                "<td valign='top' align='center'>"+
                                    "<font size='4' face='Tahoma'>"+akses.getnamars()+"</font><br>"+
                                    akses.getalamatrs()+", "+akses.getkabupatenrs()+", "+akses.getpropinsirs()+"<br>"+
                                    akses.getkontakrs()+", E-mail : "+akses.getemailrs()+"<br><br>"+
                                    "<font size='2' face='Tahoma'>PENDAPATAN PER CARA BAYAR<br>TANGGAL "+Tgl1.getSelectedItem()+" "+CmbJam.getSelectedItem()+":"+CmbMenit.getSelectedItem()+":"+CmbDetik.getSelectedItem()+" s.d. "+Tgl2.getSelectedItem()+" "+CmbJam2.getSelectedItem()+":"+CmbMenit2.getSelectedItem()+":"+CmbDetik2.getSelectedItem()+"<br><br></font>"+        
                                "</td>"+
                           "</tr>"+
                        "</table>")
            );
            bw.close();                         
            Desktop.getDesktop().browse(f.toURI());
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }     
        
        this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt, Tgl1,BtnKeluar);
        }
}//GEN-LAST:event_BtnPrintKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }else{Valid.pindah(evt,BtnKeluar,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        tampil();
    }//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnAllActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnPrint);
        }
    }//GEN-LAST:event_BtnAllKeyPressed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            tampil();
            this.setCursor(Cursor.getDefaultCursor());
        }else{
            Valid.pindah(evt,TCari, BtnPrint);
        }
    }//GEN-LAST:event_BtnCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        tampil();
    }//GEN-LAST:event_BtnCariActionPerformed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }
    }//GEN-LAST:event_TCariKeyPressed

    private void CmbJamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CmbJamKeyPressed
        Valid.pindah(evt,Tgl1,CmbMenit);
    }//GEN-LAST:event_CmbJamKeyPressed

    private void CmbMenitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CmbMenitKeyPressed
        Valid.pindah(evt,CmbJam,CmbDetik);
    }//GEN-LAST:event_CmbMenitKeyPressed

    private void CmbDetikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CmbDetikKeyPressed
        Valid.pindah(evt,CmbMenit,Tgl2);
    }//GEN-LAST:event_CmbDetikKeyPressed

    private void CmbJam2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CmbJam2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_CmbJam2KeyPressed

    private void CmbMenit2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CmbMenit2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_CmbMenit2KeyPressed

    private void CmbDetik2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CmbDetik2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_CmbDetik2KeyPressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        htmlContent = new StringBuilder();
        htmlContent.append(                             
            "<tr class='isi'>"+
                "<td valign='middle' bgcolor='#FFFAFA' align='center' width='30px' rowspan='3'>NO.</td>"+
                "<td valign='middle' bgcolor='#FFFAFA' align='center' width='300px' rowspan='3'>CARA BAYAR</td>"+
                "<td valign='middle' bgcolor='#FFFAFA' align='center' width='600px' colspan='6'>TRANSAKSI</td>"+
                "<td valign='middle' bgcolor='#FFFAFA' align='center' width='380px' colspan='2'>TOTAL</td>"+
            "</tr>"+
            "<tr class='isi'>"+
                "<td valign='middle' bgcolor='#FFFAFA' align='center' colspan='2'>RAWAT JALAN</td>"+
                "<td valign='middle' bgcolor='#FFFAFA' align='center' colspan='2'>MCU</td>"+
                "<td valign='middle' bgcolor='#FFFAFA' align='center' colspan='2'>RAWAT INAP</td>"+
                "<td valign='middle' bgcolor='#FFFAFA' align='center' rowspan='2'>CASH</td>"+
                "<td valign='middle' bgcolor='#FFFAFA' align='center' rowspan='2'>PENJAMIN</td>"+
            "</tr>"+
            "<tr class='isi'>"+
                "<td valign='middle' bgcolor='#FFFAFA' align='center' width='150px'>NILAI</td>"+
                "<td valign='middle' bgcolor='#FFFAFA' align='center' width='50px'>QTY</td>"+
                "<td valign='middle' bgcolor='#FFFAFA' align='center' width='150px'>NILAI</td>"+
                "<td valign='middle' bgcolor='#FFFAFA' align='center' width='50px'>QTY</td>"+
                "<td valign='middle' bgcolor='#FFFAFA' align='center' width='150px'>NILAI</td>"+
                "<td valign='middle' bgcolor='#FFFAFA' align='center' width='50px'>QTY</td>"+
            "</tr>"
        ); 
        LoadHTML.setText(
                    "<html>"+
                      "<table width='100%' border='0' align='left' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                       htmlContent.toString()+
                      "</table>"+
                    "</html>");
    }//GEN-LAST:event_formWindowOpened

    private void BtnPrint1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrint1ActionPerformed
        // TODO add your handling code here:
        tampil2();
        if(tabMode2.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print!");
            BtnPrint1.requestFocus();
        }else if(tabMode2.getRowCount()!=0){
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            try {
                File g = new File("pendapatanpercarabayar.css");
                BufferedWriter bg = new BufferedWriter(new FileWriter(g));
                bg.write(
                    ".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"
                    + ".isi2 td{font: 8.5px tahoma;height:12px;background: #ffffff;color:#323232;}"
                    + ".isi3 td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"
                    + ".isi4 td{font: 11px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"
                );
                bg.close();

                File f;
                BufferedWriter bw;
                htmlContent = new StringBuilder();
                htmlContent.append(
                    "<tr class='isi'>"
                    + "<td valign='middle' bgcolor='#FFFAF8' align='center' width='300px'> </td>"
                    + "<td valign='middle' bgcolor='#FFFAF8' align='center' width='30px'>QTY</td>"
                    + "<td valign='middle' bgcolor='#FFFAF8' align='center' width='300px'>nota</td>"
                    + "<td valign='middle' bgcolor='#FFFAF8' align='center' width='300px'>UMUM</td>"

                    + "<td valign='middle' bgcolor='#FFFAF8' align='center' width='300px'>IKS</td>"
                    + "<td valign='middle' bgcolor='#FFFAF8' align='center' width='300px'>BPJS</td>"
                    + "<td valign='middle' bgcolor='#FFFAF8' align='center' width='300px'>TOTAL</td>"

                    + "</tr>"

                );

                for(i=0;i<tabMode2.getRowCount();i++){
                    htmlContent.append(
                        "<tr class='isi'>"
                        + "<td>" + tabMode2.getValueAt(i,0) + "</td>"
                        + "<td>" + tabMode2.getValueAt(i,1) + "</td>"
                        + "<td align='right'>" + tabMode2.getValueAt(i,2) + "</td>"
                        + "<td align='right'>" + tabMode2.getValueAt(i,3) + "</td>"
                        + "<td align='right'>" + tabMode2.getValueAt(i,4) + "</td>"
                        + "<td align='right'>" + tabMode2.getValueAt(i,5) + "</td>"
                        + "<td align='right'>" + tabMode2.getValueAt(i,6) + "</td>"
                        + "</tr>"
                    );
                }

                StringBuilder htmlContent1 = new StringBuilder();
                htmlContent1.append(
                    "<tr class='isi'>"
                    + "<td valign='middle' bgcolor='#FFFAF8' align='center' width='300px'> </td>"
                    + "<td valign='middle' bgcolor='#FFFAF8' align='center' width='30px'>QTY</td>"
                    + "<td valign='middle' bgcolor='#FFFAF8' align='center' width='300px'>nota</td>"
                    + "<td valign='middle' bgcolor='#FFFAF8' align='center' width='300px'>UMUM</td>"
                    + "<td valign='middle' bgcolor='#FFFAF8' align='center' width='300px'>IKS</td>"
                    + "<td valign='middle' bgcolor='#FFFAF8' align='center' width='300px'>BPJS</td>"
                    + "<td valign='middle' bgcolor='#FFFAF8' align='center' width='300px'>TOTAL</td>"

                    + "</tr>"
                );

                for(int i=0;i<tabMode3.getRowCount();i++){
                    htmlContent1.append(
                        "<tr class='isi'>"
                        + "<td>" + tabMode3.getValueAt(i,0) + "</td>"
                        + "<td>" + tabMode3.getValueAt(i,1) + "</td>"
                        + "<td align='right'>" + tabMode3.getValueAt(i,2) + "</td>"
                        + "<td align='right'>" + tabMode3.getValueAt(i,3) + "</td>"
                        + "<td align='right'>" + tabMode3.getValueAt(i,4) + "</td>"
                        + "<td align='right'>" + tabMode3.getValueAt(i,5) + "</td>"
                        + "<td align='right'>" + tabMode3.getValueAt(i,6) + "</td>"
                        + "</tr>"
                    );
                }

                f = new File("PendapatanPerCaraBayar1.html");
                bw = new BufferedWriter(new FileWriter(f));
                bw.write("<html>"+
                    "<head><link href=\"file2.css\" rel=\"stylesheet\" type=\"text/css\" /></head>"+
                    "<body>"+

                    "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                    "<tr class='isi2'>"+
                    "<td valign='top' align='center'>"+
                    "<font size='4' face='Tahoma'>"+akses.getnamars()+"</font><br>"+
                    akses.getalamatrs()+", "+akses.getkabupatenrs()+", "+akses.getpropinsirs()+"<br>"+
                    akses.getkontakrs()+", E-mail : "+akses.getemailrs()+"<br><br>"+
                    "<font size='2' face='Tahoma'>PENDAPATAN PER UNIT<br>TANGGAL "+Tgl1.getSelectedItem()+" s.d. "+Tgl2.getSelectedItem()+"<br><br></font>"+
                    "</td>"+
                    "</tr>"+
                    "</table>"+
                    "<font align='center' size='3' face='Tahoma'>RAWAT JALAN</font><br>"+
                    "<table id='datatable' width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                    "<tbody id=\"databody\">"+
                    htmlContent.toString()+
                    "</tbody>"+
                    "</table><br><br><br>"+
                    "<font align='center' size='3' face='Tahoma'>RAWAT INAP</font><br>"+
                    "<table id='datatable' width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"
                    + "<tbody id=\"databody1\">"+
                    htmlContent1.toString()+
                    "</tbody>"+
                    "</table>"+
                    "</body>"+
                    "</html>"
                    //                        + "<script>"
                    //                        + "function mergeCells() {" +
                    //                            "  let db = document.getElementById(\"databody\");" +
                    //                            "  let dbRows = db.rows;" +
                    //                            "  let lastValue = \"\";" +
                    //                            "  let lastCounter = 1;" +
                    //                            "  let lastRow = 0;" +
                    //                            "  for (let i = 0; i < dbRows.length; i++) {" +
                    //                            "    let thisValue = dbRows[i].cells[1].innerHTML;" +
                    //                            "    if (thisValue == lastValue) {" +
                    //                            "      lastCounter++;" +
                    //                            "      dbRows[lastRow].cells[1].rowSpan = lastCounter;" +
                    //                            "      dbRows[i].cells[1].style.display = \"none\";"+
                    //                            "      dbRows[lastRow].cells[7].rowSpan = lastCounter;" +
                    //                            "      dbRows[i].cells[7].style.display = \"none\";"+
                    //                            "    } else {" +
                    //                            "      dbRows[i].cells[1].style.display = \"table-cell\";" +
                    //                            "      lastValue = thisValue;" +
                    //                            "      lastCounter = 1;" +
                    //                            "      lastRow = i;" +
                    //                            "    }" +
                    //                            "  }"
                    //                                            + ""+
                    //                            "  let db1 = document.getElementById(\"databody1\");" +
                    //                            "  let dbRows1 = db1.rows;" +
                    //                            "  let lastValue1 = \"\";" +
                    //                            "  let lastCounter1 = 1;" +
                    //                            "  let lastRow1 = 0;" +
                    //                            "  for (let i = 0; i < dbRows1.length; i++) {" +
                    //                            "    let thisValue1 = dbRows1[i].cells[1].innerHTML;" +
                    //                            "    if (thisValue1 == lastValue1) {" +
                    //                            "      lastCounter1++;" +
                    //                            "      dbRows1[lastRow1].cells[1].rowSpan = lastCounter1;" +
                    //                            "      dbRows1[i].cells[1].style.display = \"none\";" +
                    //                            "    } else {" +
                    //                            "      dbRows1[i].cells[1].style.display = \"table-cell\";" +
                    //                            "      lastValue1 = thisValue1;" +
                    //                            "      lastCounter1 = 1;" +
                    //                            "      lastRow1 = i;" +
                    //                            "    }" +
                    //                            "  }" +
                    //                            "}" +
                    //                            "" +
                    //                            "window.onload = mergeCells;"
                    //                            + "</script>"
                );

                bw.close();
                Desktop.getDesktop().browse(f.toURI());
            } catch (Exception e) {
                System.out.println("Notif print1 : "+e);
                e.printStackTrace();
            }

            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_BtnPrint1ActionPerformed

    private void BtnPrint1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrint1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnPrint1ActionPerformed(null);
        } else {
            Valid.pindah(evt, Tgl1, BtnKeluar);
        }
    }//GEN-LAST:event_BtnPrint1KeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgPendapatanPerCaraBayar dialog = new DlgPendapatanPerCaraBayar(new javax.swing.JFrame(), true);
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
    private widget.Button BtnCari;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnPrint1;
    private widget.ComboBox CmbDetik;
    private widget.ComboBox CmbDetik2;
    private widget.ComboBox CmbJam;
    private widget.ComboBox CmbJam2;
    private widget.ComboBox CmbMenit;
    private widget.ComboBox CmbMenit2;
    private widget.editorpane LoadHTML;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.Tanggal Tgl1;
    private widget.Tanggal Tgl2;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel10;
    private javax.swing.JLabel jLabel11;
    private widget.Label jLabel9;
    private widget.Label label11;
    private widget.Label label12;
    private widget.Label label17;
    private widget.panelisi panelGlass5;
    private widget.panelisi panelGlass6;
    // End of variables declaration//GEN-END:variables

    private void tampil(){
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); 
        try{        
            htmlContent = new StringBuilder();
            htmlContent.append(                             
                "<tr class='isi'>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center' width='30px' rowspan='3'>NO.</td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center' width='300px' rowspan='3'>CARA BAYAR</td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center' width='600px' colspan='6'>TRANSAKSI</td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center' width='380px' colspan='2'>TOTAL</td>"+
                "</tr>"+
                "<tr class='isi'>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center' colspan='2'>RAWAT JALAN</td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center' colspan='2'>MCU</td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center' colspan='2'>RAWAT INAP</td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center' rowspan='2'>CASH</td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center' rowspan='2'>PENJAMIN</td>"+
                "</tr>"+
                "<tr class='isi'>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center' width='150px'>NILAI</td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center' width='50px'>QTY</td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center' width='150px'>NILAI</td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center' width='50px'>QTY</td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center' width='150px'>NILAI</td>"+
                    "<td valign='middle' bgcolor='#FFFAFA' align='center' width='50px'>QTY</td>"+
                "</tr>"
            );   
            
            ttlralan=0;ttlmcu=0;ttlranap=0;
            ttlnilairalancash=0;ttlnilaimcucash=0;ttlnilairanapcash=0;
            ttlnilairalanpenjamin=0;ttlnilaimcupenjamin=0;ttlnilairanappenjamin=0;
            ps=koneksi.prepareStatement("select penjab.kd_pj,penjab.png_jawab from penjab order by penjab.png_jawab");
            try {
                rs=ps.executeQuery();
                i=1;
                while(rs.next()){
                    jmlralan=Sequel.cariInteger("select count(reg_periksa.no_rawat) from reg_periksa inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join nota_jalan on reg_periksa.no_rawat=nota_jalan.no_rawat where poliklinik.nm_poli not like '%mcu%' and reg_periksa.kd_pj='"+rs.getString("kd_pj")+"' and concat(nota_jalan.tanggal,' ',nota_jalan.jam) between '"+Valid.SetTgl(Tgl1.getSelectedItem()+"")+" "+CmbJam.getSelectedItem()+":"+CmbMenit.getSelectedItem()+":"+CmbDetik.getSelectedItem()+"' and '"+Valid.SetTgl(Tgl2.getSelectedItem()+"")+" "+CmbJam2.getSelectedItem()+":"+CmbMenit2.getSelectedItem()+":"+CmbDetik2.getSelectedItem()+"'");
                    jmlmcu=Sequel.cariInteger("select count(reg_periksa.no_rawat) from reg_periksa inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join nota_jalan on reg_periksa.no_rawat=nota_jalan.no_rawat where poliklinik.nm_poli like '%mcu%' and reg_periksa.kd_pj='"+rs.getString("kd_pj")+"' and concat(nota_jalan.tanggal,' ',nota_jalan.jam) between '"+Valid.SetTgl(Tgl1.getSelectedItem()+"")+" "+CmbJam.getSelectedItem()+":"+CmbMenit.getSelectedItem()+":"+CmbDetik.getSelectedItem()+"' and '"+Valid.SetTgl(Tgl2.getSelectedItem()+"")+" "+CmbJam2.getSelectedItem()+":"+CmbMenit2.getSelectedItem()+":"+CmbDetik2.getSelectedItem()+"'");jmlranap=0;
                    jmlranap=Sequel.cariInteger("select count(reg_periksa.no_rawat) from reg_periksa inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join nota_inap on reg_periksa.no_rawat=nota_inap.no_rawat where reg_periksa.kd_pj='"+rs.getString("kd_pj")+"' and concat(nota_inap.tanggal,' ',nota_inap.jam) between '"+Valid.SetTgl(Tgl1.getSelectedItem()+"")+" "+CmbJam.getSelectedItem()+":"+CmbMenit.getSelectedItem()+":"+CmbDetik.getSelectedItem()+"' and '"+Valid.SetTgl(Tgl2.getSelectedItem()+"")+" "+CmbJam2.getSelectedItem()+":"+CmbMenit2.getSelectedItem()+":"+CmbDetik2.getSelectedItem()+"'");
                    
                    ttlralan=ttlralan+jmlralan;
                    ttlmcu=ttlmcu+jmlmcu;
                    ttlranap=ttlranap+jmlranap;
                    
                    nilairalancash=Sequel.cariIsiAngka("select sum(detail_nota_jalan.besar_bayar) from reg_periksa inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join nota_jalan on reg_periksa.no_rawat=nota_jalan.no_rawat inner join detail_nota_jalan on detail_nota_jalan.no_rawat=nota_jalan.no_rawat where poliklinik.nm_poli not like '%mcu%' and reg_periksa.kd_pj='"+rs.getString("kd_pj")+"' and concat(nota_jalan.tanggal,' ',nota_jalan.jam) between '"+Valid.SetTgl(Tgl1.getSelectedItem()+"")+" "+CmbJam.getSelectedItem()+":"+CmbMenit.getSelectedItem()+":"+CmbDetik.getSelectedItem()+"' and '"+Valid.SetTgl(Tgl2.getSelectedItem()+"")+" "+CmbJam2.getSelectedItem()+":"+CmbMenit2.getSelectedItem()+":"+CmbDetik2.getSelectedItem()+"'");
                    nilaimcucash=Sequel.cariIsiAngka("select sum(detail_nota_jalan.besar_bayar) from reg_periksa inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join nota_jalan on reg_periksa.no_rawat=nota_jalan.no_rawat inner join detail_nota_jalan on detail_nota_jalan.no_rawat=nota_jalan.no_rawat where poliklinik.nm_poli like '%mcu%' and reg_periksa.kd_pj='"+rs.getString("kd_pj")+"' and concat(nota_jalan.tanggal,' ',nota_jalan.jam) between '"+Valid.SetTgl(Tgl1.getSelectedItem()+"")+" "+CmbJam.getSelectedItem()+":"+CmbMenit.getSelectedItem()+":"+CmbDetik.getSelectedItem()+"' and '"+Valid.SetTgl(Tgl2.getSelectedItem()+"")+" "+CmbJam2.getSelectedItem()+":"+CmbMenit2.getSelectedItem()+":"+CmbDetik2.getSelectedItem()+"'");
                    nilairanapcash=Sequel.cariIsiAngka("select sum(detail_nota_inap.besar_bayar) from reg_periksa inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join nota_inap on reg_periksa.no_rawat=nota_inap.no_rawat inner join detail_nota_inap on detail_nota_inap.no_rawat=nota_inap.no_rawat where reg_periksa.kd_pj='"+rs.getString("kd_pj")+"' and concat(nota_inap.tanggal,' ',nota_inap.jam) between '"+Valid.SetTgl(Tgl1.getSelectedItem()+"")+" "+CmbJam.getSelectedItem()+":"+CmbMenit.getSelectedItem()+":"+CmbDetik.getSelectedItem()+"' and '"+Valid.SetTgl(Tgl2.getSelectedItem()+"")+" "+CmbJam2.getSelectedItem()+":"+CmbMenit2.getSelectedItem()+":"+CmbDetik2.getSelectedItem()+"'")+
                                  Sequel.cariIsiAngka("select sum(deposit.besar_deposit) from reg_periksa inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join nota_inap on reg_periksa.no_rawat=nota_inap.no_rawat inner join deposit on deposit.no_rawat=nota_inap.no_rawat where reg_periksa.kd_pj='"+rs.getString("kd_pj")+"' and concat(nota_inap.tanggal,' ',nota_inap.jam) between '"+Valid.SetTgl(Tgl1.getSelectedItem()+"")+" "+CmbJam.getSelectedItem()+":"+CmbMenit.getSelectedItem()+":"+CmbDetik.getSelectedItem()+"' and '"+Valid.SetTgl(Tgl2.getSelectedItem()+"")+" "+CmbJam2.getSelectedItem()+":"+CmbMenit2.getSelectedItem()+":"+CmbDetik2.getSelectedItem()+"'")-
                                  Sequel.cariIsiAngka("select sum(pengembalian_deposit.besar_pengembalian) from reg_periksa inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join nota_inap on reg_periksa.no_rawat=nota_inap.no_rawat inner join pengembalian_deposit on pengembalian_deposit.no_rawat=nota_inap.no_rawat where reg_periksa.kd_pj='"+rs.getString("kd_pj")+"' and concat(nota_inap.tanggal,' ',nota_inap.jam) between '"+Valid.SetTgl(Tgl1.getSelectedItem()+"")+" "+CmbJam.getSelectedItem()+":"+CmbMenit.getSelectedItem()+":"+CmbDetik.getSelectedItem()+"' and '"+Valid.SetTgl(Tgl2.getSelectedItem()+"")+" "+CmbJam2.getSelectedItem()+":"+CmbMenit2.getSelectedItem()+":"+CmbDetik2.getSelectedItem()+"'");
                    
                    ttlnilairalancash=ttlnilairalancash+nilairalancash;
                    ttlnilaimcucash=ttlnilaimcucash+nilaimcucash;
                    ttlnilairanapcash=ttlnilairanapcash+nilairanapcash;
                    
                    nilairalanpenjamin=Sequel.cariIsiAngka("select sum(detail_piutang_pasien.totalpiutang) from reg_periksa inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join nota_jalan on reg_periksa.no_rawat=nota_jalan.no_rawat inner join detail_piutang_pasien on detail_piutang_pasien.no_rawat=nota_jalan.no_rawat where poliklinik.nm_poli not like '%mcu%' and reg_periksa.kd_pj='"+rs.getString("kd_pj")+"' and concat(nota_jalan.tanggal,' ',nota_jalan.jam) between '"+Valid.SetTgl(Tgl1.getSelectedItem()+"")+" "+CmbJam.getSelectedItem()+":"+CmbMenit.getSelectedItem()+":"+CmbDetik.getSelectedItem()+"' and '"+Valid.SetTgl(Tgl2.getSelectedItem()+"")+" "+CmbJam2.getSelectedItem()+":"+CmbMenit2.getSelectedItem()+":"+CmbDetik2.getSelectedItem()+"'");
                    nilaimcupenjamin=Sequel.cariIsiAngka("select sum(detail_piutang_pasien.totalpiutang) from reg_periksa inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join nota_jalan on reg_periksa.no_rawat=nota_jalan.no_rawat inner join detail_piutang_pasien on detail_piutang_pasien.no_rawat=nota_jalan.no_rawat where poliklinik.nm_poli like '%mcu%' and reg_periksa.kd_pj='"+rs.getString("kd_pj")+"' and concat(nota_jalan.tanggal,' ',nota_jalan.jam) between '"+Valid.SetTgl(Tgl1.getSelectedItem()+"")+" "+CmbJam.getSelectedItem()+":"+CmbMenit.getSelectedItem()+":"+CmbDetik.getSelectedItem()+"' and '"+Valid.SetTgl(Tgl2.getSelectedItem()+"")+" "+CmbJam2.getSelectedItem()+":"+CmbMenit2.getSelectedItem()+":"+CmbDetik2.getSelectedItem()+"'");
                    nilairanappenjamin=Sequel.cariIsiAngka("select sum(detail_piutang_pasien.totalpiutang) from reg_periksa inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join nota_inap on reg_periksa.no_rawat=nota_inap.no_rawat inner join detail_piutang_pasien on detail_piutang_pasien.no_rawat=nota_inap.no_rawat where reg_periksa.kd_pj='"+rs.getString("kd_pj")+"' and concat(nota_inap.tanggal,' ',nota_inap.jam) between '"+Valid.SetTgl(Tgl1.getSelectedItem()+"")+" "+CmbJam.getSelectedItem()+":"+CmbMenit.getSelectedItem()+":"+CmbDetik.getSelectedItem()+"' and '"+Valid.SetTgl(Tgl2.getSelectedItem()+"")+" "+CmbJam2.getSelectedItem()+":"+CmbMenit2.getSelectedItem()+":"+CmbDetik2.getSelectedItem()+"'");
                    
                    ttlnilairalanpenjamin=ttlnilairalanpenjamin+nilairalanpenjamin;
                    ttlnilaimcupenjamin=ttlnilaimcupenjamin+nilaimcupenjamin;
                    ttlnilairanappenjamin=ttlnilairanappenjamin+nilairanappenjamin;
                    
                    htmlContent.append(                             
                        "<tr class='isi'>"+
                            "<td>"+i+"</td>"+
                            "<td>"+rs.getString("png_jawab")+"</td>"+
                            "<td align='right'>"+Valid.SetAngka(nilairalancash+nilairalanpenjamin)+"</td>"+
                            "<td align='center'>"+jmlralan+"</td>"+
                            "<td align='right'>"+Valid.SetAngka(nilaimcucash+nilaimcupenjamin)+"</td>"+
                            "<td align='center'>"+jmlmcu+"</td>"+
                            "<td align='right'>"+Valid.SetAngka(nilairanapcash+nilairanappenjamin)+"</td>"+
                            "<td align='center'>"+jmlranap+"</td>"+
                            "<td align='right'>"+Valid.SetAngka(nilairalancash+nilaimcucash+nilairanapcash)+"</td>"+
                            "<td align='right'>"+Valid.SetAngka(nilairalanpenjamin+nilaimcupenjamin+nilairanappenjamin)+"</td>"+
                        "</tr>"
                    );   
                    i++;
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
            
            htmlContent.append(     
                "<tr class='isi'>"+
                    "<td></td>"+
                    "<td>TOTAL :</td>"+
                    "<td align='right'>"+Valid.SetAngka(ttlnilairalancash+ttlnilairalanpenjamin)+"</td>"+
                    "<td align='center'>"+ttlralan+"</td>"+
                    "<td align='right'>"+Valid.SetAngka(ttlnilaimcucash+ttlnilaimcupenjamin)+"</td>"+
                    "<td align='center'>"+ttlmcu+"</td>"+
                    "<td align='right'>"+Valid.SetAngka(ttlnilairanapcash+ttlnilairanappenjamin)+"</td>"+
                    "<td align='center'>"+ttlranap+"</td>"+
                    "<td align='right'>"+Valid.SetAngka(ttlnilairalancash+ttlnilaimcucash+ttlnilairanapcash)+"</td>"+
                    "<td align='right'>"+Valid.SetAngka(ttlnilairalanpenjamin+ttlnilaimcupenjamin+ttlnilairanappenjamin)+"</td>"+
                "</tr>"
            ); 
            LoadHTML.setText(
                    "<html>"+
                      "<table width='100%' border='0' align='left' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                       htmlContent.toString()+
                      "</table>"+
                    "</html>");
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        this.setCursor(Cursor.getDefaultCursor());
    }    
    
    private void tampil2() {

        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Valid.tabelKosong(tabMode2);
        Valid.tabelKosong(tabMode3);
        try {

            ps = koneksi.prepareStatement("SELECT GROUP_CONCAT(DISTINCT reg_periksa.`no_rawat` SEPARATOR ',') AS no_rawat,COUNT(DISTINCT reg_periksa.`no_rawat`) AS nocount, " +
                "poliklinik.nm_poli, IFNULL(SUM(detail_nota_jalan.besar_bayar),0) AS besar_bayar, " +
                "IFNULL(SUM(detail_piutang_pasien.totalpiutang),0) AS totalpiutang " +
                "FROM reg_periksa " +
                "INNER JOIN poliklinik ON reg_periksa.kd_poli=poliklinik.kd_poli " +
                "INNER JOIN nota_jalan ON reg_periksa.no_rawat=nota_jalan.no_rawat " +
                "LEFT JOIN detail_nota_jalan ON detail_nota_jalan.no_rawat=nota_jalan.no_rawat " +
                "LEFT JOIN detail_piutang_pasien ON detail_piutang_pasien.no_rawat=nota_jalan.no_rawat " +
                "WHERE poliklinik.nm_poli NOT LIKE '%mcu%' " +
                "AND CONCAT(nota_jalan.tanggal,' ',nota_jalan.jam) BETWEEN '" + Valid.SetTgl(Tgl1.getSelectedItem() + "") + " " + CmbJam.getSelectedItem() + ":" + CmbMenit.getSelectedItem() + ":" + CmbDetik.getSelectedItem() + "' and '" + Valid.SetTgl(Tgl2.getSelectedItem() + "") + " " + CmbJam2.getSelectedItem() + ":" + CmbMenit2.getSelectedItem() + ":" + CmbDetik2.getSelectedItem() + "'" +
                "GROUP BY poliklinik.kd_poli ORDER BY poliklinik.nm_poli");

            ps4= koneksi.prepareStatement(
                    "SELECT GROUP_CONCAT(DISTINCT reg_periksa.`no_rawat` SEPARATOR ',') AS no_rawat, COUNT(DISTINCT reg_periksa.`no_rawat`) AS nocount, " +
                    "poliklinik.nm_poli, SUM(detail_nota_inap.besar_bayar) AS besar_bayar, " +
                    "SUM(deposit.besar_deposit) AS besar_deposit, " +
                    "SUM(detail_piutang_pasien.totalpiutang) as totalpiutang, " +
                    "kamar_inap.stts_pulang FROM reg_periksa " +
                    "INNER JOIN kamar_inap ON kamar_inap.no_rawat=reg_periksa.no_rawat " +
                    "INNER JOIN poliklinik ON reg_periksa.kd_poli=poliklinik.kd_poli " +
                    "INNER JOIN nota_inap ON reg_periksa.no_rawat=nota_inap.no_rawat " +
                    "LEFT JOIN detail_nota_inap ON detail_nota_inap.no_rawat=nota_inap.no_rawat " +
                    "LEFT JOIN deposit ON deposit.no_rawat=nota_inap.no_rawat " +
                    "LEFT JOIN detail_piutang_pasien ON detail_piutang_pasien.no_rawat=nota_inap.no_rawat " +
                    "WHERE " +
                    "CONCAT(nota_inap.tanggal,' ',nota_inap.jam) BETWEEN '" + Valid.SetTgl(Tgl1.getSelectedItem() + "") + " " + CmbJam.getSelectedItem() + ":" + CmbMenit.getSelectedItem() + ":" + CmbDetik.getSelectedItem() + "' and '" + Valid.SetTgl(Tgl2.getSelectedItem() + "") + " " + CmbJam2.getSelectedItem() + ":" + CmbMenit2.getSelectedItem() + ":" + CmbDetik2.getSelectedItem() + "'" +
                    "GROUP BY poliklinik.kd_poli ORDER BY poliklinik.nm_poli");

            ps8= koneksi.prepareStatement(
                    "SELECT GROUP_CONCAT(DISTINCT reg_periksa.`no_rawat` SEPARATOR ',') AS no_rawat,count(DISTINCT reg_periksa.`no_rawat`) as nocount, " +
                    "poliklinik.nm_poli, SUM(pengembalian_deposit.besar_pengembalian) as besar_pengembalian, kamar_inap.stts_pulang  FROM reg_periksa " +
                    "INNER JOIN kamar_inap ON kamar_inap.no_rawat=reg_periksa.no_rawat " +
                    "INNER JOIN poliklinik ON reg_periksa.kd_poli=poliklinik.kd_poli " +
                    "INNER JOIN nota_inap ON reg_periksa.no_rawat=nota_inap.no_rawat " +
                    "INNER JOIN pengembalian_deposit ON pengembalian_deposit.no_rawat=nota_inap.no_rawat " +
                    "WHERE " +
                    "CONCAT(nota_inap.tanggal,' ',nota_inap.jam) BETWEEN '" + Valid.SetTgl(Tgl1.getSelectedItem() + "") + " " + CmbJam.getSelectedItem() + ":" + CmbMenit.getSelectedItem() + ":" + CmbDetik.getSelectedItem() + "' and '" + Valid.SetTgl(Tgl2.getSelectedItem() + "") + " " + CmbJam2.getSelectedItem() + ":" + CmbMenit2.getSelectedItem() + ":" + CmbDetik2.getSelectedItem() + "'" +
                    "GROUP BY poliklinik.kd_poli ORDER BY poliklinik.nm_poli");

            
            try {
                rs = ps.executeQuery();
                
                ttlLaborat=0;ttlRadiologi=0;ttlOperasi=0;ttlObat=0;ttlRalan_Dokter=0;ttlRalan_Paramedis=0;ttlTambahan=0;ttlPotongan=0;ttlRegistrasi=0;
                ttlVk=0;ttlUgd=0;
                
                ttlRalan_Dokter1=0;ttlRalan_Dokter_paramedis1=0;ttlRalan_Paramedis1=0;ttlTambahan1=0;ttlPotongan1=0;ttlRegistrasi1=0;
    
                PotonganLab=0; PotonganRad=0; PotonganVk=0; PotonganUgd=0;TambahanLab=0;TambahanRad=0;TambahanVk=0;TambahanUgd=0;
                QtyLab=0;QtyRad=0;QtyVk=0;QtyUgd=0;QtyPoli=0;
                
                ttlVkUmum=0;ttlUgdUmum=0;ttlRadUmum=0;ttlLabUmum=0;ttlOperasiUmum=0;ttlObatUmum=0;ttlPoliUmum=0;
                ttlVkIks=0;ttlUgdIks=0;ttlRadIks=0;ttlLabIks=0;ttlOperasiIks=0;ttlObatIks=0;ttlPoliIks=0;
                ttlVkBpjs=0;ttlUgdBpjs=0;ttlRadBpjs=0;ttlLabBpjs=0;ttlOperasiBpjs=0;ttlObatBpjs=0;ttlPoliBpjs=0;
                
                ttlNotaPoli=0;ttlNotaVk=0;ttlNotaUgd=0;ttlNotaLab=0;ttlNotaRad=0;
                ArrayList<Object[]> tabModeObject = new ArrayList<>();
                
                no=1;
                while (rs.next()) {
                    
                    String norawat =  rs.getString("no_rawat");
                    List<String> norawat_list = new ArrayList<String>(Arrays.asList(norawat.split(",")));

                    String markers = ",?".repeat(norawat_list.size()).substring(1);
                    
                    ps3= koneksi.prepareStatement("SELECT SUM(billing.totalbiaya) AS totalbiaya FROM billing WHERE billing.no_rawat in("+ markers +") "
                            + "AND billing.status NOT IN('Obat','Laborat','TtlObat','Radiologi')");
                    
                    for (int o = 0; o < norawat_list.size(); o++) {
                        ps3.setString(o + 1, norawat_list.get(o));
                    }

                    rs3 = ps3.executeQuery();

                    while (rs3.next()) {
                        jumlah = rs3.getDouble("totalbiaya");
                    }
                    

                    ps2= koneksi.prepareStatement("SELECT GROUP_CONCAT(DISTINCT reg_periksa.no_rawat) AS norawat1, penjab.kd_pj, " +
                            "penjab.png_jawab AS penjab, " +
                            "COUNT(reg_periksa.`no_rawat`) AS qty, " +
                            "IFNULL(SUM(detail_nota_jalan.besar_bayar),0) AS besar_bayar, " +
                            "IFNULL(SUM(detail_piutang_pasien.totalpiutang),0) AS totalpiutang " +
                            "FROM reg_periksa " +
                            "INNER JOIN penjab ON penjab.kd_pj=reg_periksa.kd_pj " +
                            "INNER JOIN nota_jalan ON reg_periksa.no_rawat=nota_jalan.no_rawat " +
                "LEFT JOIN detail_nota_jalan ON detail_nota_jalan.no_rawat=nota_jalan.no_rawat " +
                "LEFT JOIN detail_piutang_pasien ON detail_piutang_pasien.no_rawat=nota_jalan.no_rawat " +
                            "WHERE reg_periksa.`no_rawat` IN( "+ markers + ") " +
                            "GROUP BY penjab.kd_pj ORDER BY penjab.png_jawab");
                    
                    try {
                        for (int o = 0; o < norawat_list.size(); o++) {
                            ps2.setString(o + 1, norawat_list.get(o));
                        }
                        
                        rs2 = ps2.executeQuery();
                        
                        while (rs2.next()) {
                            String norawat1 =  rs2.getString("norawat1");
                            List<String> norawat_list1 = new ArrayList<String>(Arrays.asList(norawat1.split(",")));

                            String markers1 = ",?".repeat(norawat_list1.size()).substring(1);
                        
                            Operasi=0;Laborat=0;Radiologi=0;Obat=0;
                            Ralan_Dokter1=0;Ralan_Dokter_paramedis1=0;Ralan_Paramedis1=0;Tambahan1=0;Potongan1=0;Registrasi1=0;
                            
                            
                            ps1=koneksi.prepareStatement(
                                "select billing.status, sum(billing.totalbiaya) as totalbiaya from billing where billing.no_rawat in( "+ markers1 +" ) "
                                                + "GROUP BY billing.status ORDER BY billing.status REGEXP '^[a-z]' ASC");
                            try{

                                for (int o = 0; o < norawat_list1.size(); o++) {
                                    ps1.setString(o + 1, norawat_list1.get(o));
                                }

        //                        ps4.setString(1,rs3.getString("no_rawat"));
                                rs1=ps1.executeQuery();
                                while(rs1.next()){
                                    switch (rs1.getString("status")) {
                                        case "Laborat":
                                            ttlLaborat=ttlLaborat+rs1.getDouble("totalbiaya");
                                            Laborat=Laborat+rs1.getDouble("totalbiaya");
                                            
                                            if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                                ttlLabUmum = ttlLabUmum + Laborat;

                                            } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                                ttlLabBpjs = ttlLabBpjs + Laborat;

                                            } else {
                                                ttlLabIks = ttlLabIks + Laborat;

                                            }
                                            break;
                                        case "Radiologi":
                                            ttlRadiologi=ttlRadiologi+rs1.getDouble("totalbiaya");
                                            Radiologi=Radiologi+rs1.getDouble("totalbiaya");
                                            
                                            if(rs2.getString("kd_pj").equals("A05")|| rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                                ttlRadUmum = ttlRadUmum + Radiologi;
                                                
                                            } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                                ttlRadBpjs = ttlRadBpjs + Radiologi;

                                            } else {
                                                ttlRadIks = ttlRadIks + Radiologi;

                                            }
                                            break;
                                        case "Obat":
                                            ttlObat=ttlObat+rs1.getDouble("totalbiaya");
                                            Obat=Obat+rs1.getDouble("totalbiaya");
                                            
                                            if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                                ttlObatUmum = ttlObatUmum + Obat;

                                            } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                                ttlObatBpjs = ttlObatBpjs + Obat;

                                            } else {
                                                ttlObatIks = ttlObatIks + Obat;
                                            }
                                            break;
                                        case "Ralan Dokter":
                                            ttlRalan_Dokter1=ttlRalan_Dokter1+rs1.getDouble("totalbiaya");
                                            Ralan_Dokter1=Ralan_Dokter1+rs1.getDouble("totalbiaya");
                                            break;     
                                        case "Ralan Dokter Paramedis":
                                            ttlRalan_Dokter_paramedis1=ttlRalan_Dokter_paramedis1+rs1.getDouble("totalbiaya");
                                            Ralan_Dokter_paramedis1=Ralan_Dokter_paramedis1+rs1.getDouble("totalbiaya");
                                            break;    
                                        case "Ralan Paramedis":
                                            ttlRalan_Paramedis1=ttlRalan_Paramedis1+rs1.getDouble("totalbiaya");
                                            Ralan_Paramedis1=Ralan_Paramedis1+rs1.getDouble("totalbiaya");
                                            break;
                                        case "Tambahan":
                                            ttlTambahan1=ttlTambahan1+rs1.getDouble("totalbiaya");
                                            Tambahan1=Tambahan1+rs1.getDouble("totalbiaya");
                                            break;
                                        case "Potongan":
                                            ttlPotongan1=ttlPotongan1+rs1.getDouble("totalbiaya");
                                            Potongan1=Potongan1+rs1.getDouble("totalbiaya");
                                            break;
                                        case "Registrasi":
                                            ttlRegistrasi1=ttlRegistrasi1+rs1.getDouble("totalbiaya");
                                            Registrasi1=Registrasi1+rs1.getDouble("totalbiaya");
                                            break;
                                        case "Operasi":
                                            ttlOperasi=ttlOperasi+rs1.getDouble("totalbiaya");
                                            Operasi=Operasi+rs1.getDouble("totalbiaya");
                                            if(rs2.getString("kd_pj").equals("A05")) {
                                                ttlOperasiUmum = ttlOperasiUmum + Operasi;

                                            } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                                ttlOperasiBpjs = ttlOperasiBpjs + Operasi;

                                            } else {
                                                ttlOperasiIks = ttlOperasiIks + Operasi;
                                            }
                                            break;
                                    }      
                                }
                                        
                                if(rs.getString("nm_poli").contains("UGD")) {
                                    if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                        ttlUgdUmum = ttlUgdUmum + Ralan_Dokter1+Ralan_Paramedis1+Ralan_Dokter_paramedis1+Tambahan1+Registrasi1+Potongan1;
                                        ttlNotaUgd = ttlNotaUgd + rs2.getDouble("besar_bayar") + rs2.getDouble("totalpiutang");
                                    } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                        ttlUgdBpjs = ttlUgdBpjs + Ralan_Dokter1+Ralan_Paramedis1+Ralan_Dokter_paramedis1+Tambahan1+Registrasi1+Potongan1;

                                    } else {
                                        ttlUgdIks = ttlUgdIks + Ralan_Dokter1+Ralan_Paramedis1+Ralan_Dokter_paramedis1+Tambahan1+Registrasi1+Potongan1; 
                                    }
                                    QtyUgd = QtyUgd+rs2.getDouble("qty");
                                } else if(rs.getString("nm_poli").contains("VK")) {
                                    if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                        ttlVkUmum = ttlVkUmum + Ralan_Dokter1+Ralan_Paramedis1+Ralan_Dokter_paramedis1+Tambahan1+Potongan1+Registrasi1;
                                        ttlNotaVk = ttlNotaVk + rs2.getDouble("besar_bayar") + rs2.getDouble("totalpiutang");
                                    } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                        ttlVkBpjs = ttlVkBpjs + Ralan_Dokter1+Ralan_Paramedis1+Ralan_Dokter_paramedis1+Tambahan1+Potongan1+Registrasi1;

                                    } else {
                                        ttlVkIks = ttlVkIks + Ralan_Dokter1+Ralan_Paramedis1+Ralan_Dokter_paramedis1+Tambahan1+Potongan1+Registrasi1;
                                    }
                                    QtyVk = QtyVk+rs2.getDouble("qty");
                                } else if(rs.getString("nm_poli").contains("RADIOLOGI")) {
                                    if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                        ttlRadUmum = ttlRadUmum + Ralan_Dokter1+Ralan_Paramedis1+Ralan_Dokter_paramedis1+Tambahan1+Potongan1+Registrasi1;
                                        ttlNotaRad = ttlNotaRad + rs2.getDouble("besar_bayar") + rs2.getDouble("totalpiutang");
                                    } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                        ttlRadBpjs = ttlRadBpjs + Ralan_Dokter1+Ralan_Paramedis1+Ralan_Dokter_paramedis1+Tambahan1+Potongan1+Registrasi1;

                                    } else {
                                        ttlRadIks = ttlRadIks + Ralan_Dokter1+Ralan_Paramedis1+Ralan_Dokter_paramedis1+Tambahan1+Potongan1+Registrasi1;
                                        
                                    }
                                    QtyRad = QtyRad+rs2.getDouble("qty");
                                } else if(Pattern.compile(Pattern.quote("LAB"), Pattern.CASE_INSENSITIVE).matcher(rs.getString("nm_poli")).find()) {
                                    if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                        ttlLabUmum = ttlLabUmum + Ralan_Dokter1+Ralan_Paramedis1+Ralan_Dokter_paramedis1+Tambahan1+Potongan1+Registrasi1;
                                        ttlNotaLab = ttlNotaLab + rs2.getDouble("besar_bayar") + rs2.getDouble("totalpiutang");
                                    } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                        ttlLabBpjs = ttlLabBpjs + Ralan_Dokter1+Ralan_Paramedis1+Ralan_Dokter_paramedis1+Tambahan1+Potongan1+Registrasi1;

                                    } else {
                                        ttlLabIks = ttlLabIks + Ralan_Dokter1+Ralan_Paramedis1+Ralan_Dokter_paramedis1+Tambahan1+Potongan1+Registrasi1;
                                        
                                    }
                                    QtyLab = QtyLab+rs2.getDouble("qty");
                                } 
                                else{
                                    if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                        ttlPoliUmum = ttlPoliUmum + Ralan_Dokter1+Ralan_Paramedis1+Ralan_Dokter_paramedis1+Tambahan1+Potongan1+Registrasi1;
                                        ttlNotaPoli = ttlNotaPoli + rs2.getDouble("besar_bayar") + rs2.getDouble("totalpiutang");
                                    } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                        ttlPoliBpjs = ttlPoliBpjs + Ralan_Dokter1+Ralan_Paramedis1+Ralan_Dokter_paramedis1+Tambahan1+Potongan1+Registrasi1;

                                    } else {
                                        ttlPoliIks = ttlPoliIks + Ralan_Dokter1+Ralan_Paramedis1+Ralan_Dokter_paramedis1+Tambahan1+Potongan1+Registrasi1;
                                        
                                    }
                                    QtyPoli = QtyPoli + rs2.getDouble("qty");


                                }
                            }catch(Exception e){
                                System.out.println("Notif 4 : "+e);
                                e.printStackTrace();
                            } finally{
                                if(rs1!=null){
                                    rs1.close();
                                }
                                if(ps1!=null){
                                    ps1.close();
                                }
                            }
                            
                        }
                    }catch(Exception e){
                        System.out.println("Notif 2 : "+e);
                        e.printStackTrace();
                    } finally{
                        if(rs2!=null){
                            rs2.close();
                        }
                        if(ps2!=null){
                            ps2.close();
                        }
                    }
                
                            
                    
                }
                
                tabMode2.addRow(new Object[] {
                    "POLI",Valid.SetAngka(QtyPoli),Valid.SetAngka(ttlNotaPoli),Valid.SetAngka(ttlPoliUmum),
                    Valid.SetAngka(ttlPoliIks),
                    Valid.SetAngka(ttlPoliBpjs), 
                    Valid.SetAngka(ttlPoliUmum+ttlPoliIks+ttlPoliBpjs)
                    
                });
                
                tabMode2.addRow(new Object[] {
                    "OBAT","","",Valid.SetAngka(ttlObatUmum),
                    Valid.SetAngka(ttlObatIks),
                    Valid.SetAngka(ttlObatBpjs), 
                    Valid.SetAngka(ttlObatUmum+ttlObatIks+ttlObatBpjs)
                });
                
                tabMode2.addRow(new Object[] {
                    "OPERASI","","",Valid.SetAngka(ttlOperasiUmum),
                    Valid.SetAngka(ttlOperasiIks),
                    Valid.SetAngka(ttlOperasiBpjs), 
                    Valid.SetAngka(ttlOperasiUmum+ttlOperasiIks+ttlOperasiBpjs)
                });
                
                tabMode2.addRow(new Object[] {
                    "LABORAT",Valid.SetAngka(QtyLab),Valid.SetAngka(ttlNotaLab),Valid.SetAngka(ttlLabUmum),
                    Valid.SetAngka(ttlLabIks),
                    Valid.SetAngka(ttlLabBpjs), 
                    Valid.SetAngka(ttlLabUmum+ttlLabIks+ttlLabBpjs)
                });
                
                tabMode2.addRow(new Object[] {
                    "RADIOLOGI",Valid.SetAngka(QtyRad),Valid.SetAngka(ttlNotaRad),Valid.SetAngka(ttlRadUmum),
                    Valid.SetAngka(ttlRadIks),
                    Valid.SetAngka(ttlRadBpjs), 
                    Valid.SetAngka(ttlRadUmum+ttlRadIks+ttlRadBpjs)
                });
                
                tabMode2.addRow(new Object[] {
                    "UGD",Valid.SetAngka(QtyUgd),Valid.SetAngka(ttlNotaUgd),Valid.SetAngka(ttlUgdUmum),
                    Valid.SetAngka(ttlUgdIks),
                    Valid.SetAngka(ttlUgdBpjs), 
                    Valid.SetAngka(ttlUgdUmum+ttlUgdIks+ttlUgdBpjs)
                });
                
                tabMode2.addRow(new Object[] {
                    "VK",Valid.SetAngka(QtyVk),Valid.SetAngka(ttlNotaVk),Valid.SetAngka(ttlVkUmum),
                    Valid.SetAngka(ttlVkIks),
                    Valid.SetAngka(ttlVkBpjs), 
                    Valid.SetAngka(ttlVkUmum+ttlVkIks+ttlVkBpjs)
                });
                tabMode2.addRow(new Object[] {
                    "TOTAL",Valid.SetAngka(QtyVk+QtyUgd+QtyRad+QtyLab+QtyPoli),Valid.SetAngka(ttlNotaPoli+ttlNotaVk+ttlNotaUgd+ttlNotaLab+ttlNotaRad),
                    Valid.SetAngka(ttlVkUmum+ttlUgdUmum+ttlRadUmum+ttlLabUmum+ttlPoliUmum+ttlOperasiUmum+ttlObatUmum),
                    
                    Valid.SetAngka(ttlVkIks+ttlUgdIks+ttlRadIks+ttlLabIks+ttlOperasiIks+ttlObatIks+ttlPoliIks),
                    Valid.SetAngka(ttlVkBpjs+ttlUgdBpjs+ttlRadBpjs+ttlLabBpjs+ttlOperasiBpjs+ttlObatBpjs+ttlPoliBpjs), 
                    Valid.SetAngka(ttlVkUmum+ttlVkIks+ttlVkBpjs+ttlUgdUmum+ttlUgdIks+ttlUgdBpjs+
                            ttlRadUmum+ttlRadIks+ttlRadBpjs+ttlLabUmum+ttlLabIks+ttlLabBpjs+
                            ttlOperasiUmum+ttlOperasiIks+ttlOperasiBpjs+ttlObatUmum+ttlObatIks+ttlObatBpjs+
                            ttlPoliUmum+ttlPoliIks+ttlPoliBpjs)
                });
            } catch (Exception e) {
                System.out.println("Notif : " + e);
                e.printStackTrace();
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
            
            try {
                rs4=ps4.executeQuery();
                rs8=ps8.executeQuery();
                
                ttlLaborat=0;ttlRadiologi=0;ttlOperasi=0;ttlObat=0;
                ttlRanap_Dokter=0;ttlRanap_Paramedis=0;ttlRalan_Dokter=0;
                ttlRalan_Paramedis=0;ttlTambahan=0;ttlPotongan=0;ttlKamar=0;
                ttlRegistrasi=0;ttlHarian=0;
                ttlService=0;
                
                ttlRanap_Dokter1=0;ttlRanap_Paramedis1=0;ttlRalan_Dokter1=0;
                ttlRalan_Paramedis1=0;ttlTambahan1=0;ttlPotongan1=0;ttlKamar1=0;
                ttlRegistrasi1=0;ttlHarian1=0;
                ttlService1=0;ttlUgd=0;ttlVk=0;
                
                ttlRanap_Dokter2=0;ttlRanap_Paramedis2=0;ttlRalan_Dokter2=0;
                ttlRalan_Paramedis2=0;ttlTambahan2=0;ttlPotongan2=0;ttlKamar2=0;
                ttlRegistrasi2=0;ttlHarian2=0;
                ttlService2=0;
                
                
                PotonganVk=0;TambahanVk = 0;
                Kamar_ServiceVk = 0;HarianVk = 0;
                PotonganUgd=0;TambahanUgd = 0;
                Kamar_ServiceUgd = 0;HarianUgd = 0;
                PotonganLab=0;TambahanLab = 0;
                Kamar_ServiceLab = 0;HarianLab = 0;
                PotonganRad=0;TambahanRad = 0;
                Kamar_ServiceRad = 0;HarianRad = 0;
                QtyVk=0;QtyUgd=0;QtyLab=0;QtyRad=0;QtyPoli=0;
                
                ttlVkUmum=0;ttlUgdUmum=0;ttlRadUmum=0;ttlLabUmum=0;ttlOperasiUmum=0;ttlObatUmum=0;ttlPoliUmum=0;
                ttlVkIks=0;ttlUgdIks=0;ttlRadIks=0;ttlLabIks=0;ttlOperasiIks=0;ttlObatIks=0;ttlPoliIks=0;
                ttlVkBpjs=0;ttlUgdBpjs=0;ttlRadBpjs=0;ttlLabBpjs=0;ttlOperasiBpjs=0;ttlObatBpjs=0;ttlPoliBpjs=0;
                
                
                ArrayList<Object[]> tabModeObject1 = new ArrayList<>();
                                    
                
                while(rs4.next()){
                    
                    String norawat =  rs4.getString("no_rawat");
                    List<String> norawat_list = new ArrayList<String>(Arrays.asList(norawat.split(",")));


                    String markers = ",?".repeat(norawat_list.size()).substring(1);
                    
                    
                    
                    
                    ps2= koneksi.prepareStatement("SELECT GROUP_CONCAT(DISTINCT reg_periksa.no_rawat) AS norawat1, penjab.kd_pj, " +
                            "penjab.png_jawab AS penjab, " +
                            "COUNT(reg_periksa.`no_rawat`) AS qty " +
                            "FROM reg_periksa " +
                            "INNER JOIN penjab ON penjab.kd_pj=reg_periksa.kd_pj " +
                            "WHERE reg_periksa.`no_rawat` IN( "+ markers + ") " +
                            "GROUP BY penjab.kd_pj ORDER BY penjab.png_jawab");
                    
                    try {
                        for (int o = 0; o < norawat_list.size(); o++) {
                            ps2.setString(o + 1, norawat_list.get(o));
                        }
                        
                        rs2 = ps2.executeQuery();
                        
                        while (rs2.next()) {
                            String norawat1 =  rs2.getString("norawat1");
                            List<String> norawat_list1 = new ArrayList<String>(Arrays.asList(norawat1.split(",")));

                            String markers1 = ",?".repeat(norawat_list1.size()).substring(1);
                            
                            
                            Operasi=0;Laborat=0;Radiologi=0;Obat=0;
                            Ranap_Dokter1=0;Ranap_Paramedis1=0;Ralan_Dokter1=0;Ranap_Dokter_Paramedis1=0;Ralan_Dokter_Paramedis1=0;
                            Ralan_Paramedis1=0;Tambahan1=0;Potongan1=0;Kamar1=0;
                            Registrasi1=0;Harian1=0;
                            Service1=0;

                            if(!rs4.getString("stts_pulang").equals("-")){
                            if(!rs4.getString("stts_pulang").equals("Pindah Kamar")){
                                ps1=koneksi.prepareStatement("select billing.status, sum(billing.totalbiaya) as totbiaya from billing where billing.no_rawat in( "+ markers1 +" ) "
                                        + "GROUP BY billing.status ORDER BY billing.status REGEXP '^[a-z]' ASC");
                                try {
                                    for (int o = 0; o < norawat_list1.size(); o++) {
                                        ps1.setString(o + 1, norawat_list1.get(o));
                                    }
            //                        ps4.setString(1,rs3.getString("no_rawat1"));
                                    rs1=ps1.executeQuery();
                                    if(!rs1.next()) {
                                        System.out.println("no data");
                                    }
                                    while(rs1.next()){
                                        switch (rs1.getString("status")) {
                                            case "Laborat":
                                                ttlLaborat=ttlLaborat+rs1.getDouble("totbiaya");
                                                Laborat=Laborat+rs1.getDouble("totbiaya");
                                                
                                                if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                                    ttlLabUmum = ttlLabUmum + Laborat;

                                                } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                                    ttlLabBpjs = ttlLabBpjs + Laborat;

                                                } else {
                                                    ttlLabIks = ttlLabIks + Laborat;

                                                }
                                                break;
                                            case "Radiologi":
                                                ttlRadiologi=ttlRadiologi+rs1.getDouble("totbiaya");
                                                Radiologi=Radiologi+rs1.getDouble("totbiaya");
                                                
                                                if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                                    ttlRadUmum = ttlRadUmum + Radiologi;

                                                } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                                    ttlRadBpjs = ttlRadBpjs + Radiologi;

                                                } else {
                                                    ttlRadIks = ttlRadIks + Radiologi;

                                                }
                                                break;
                                            case "Obat":
                                                ttlObat=ttlObat+rs1.getDouble("totbiaya");
                                                Obat=Obat+rs1.getDouble("totbiaya");
                                                
                                                if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                                    ttlObatUmum = ttlObatUmum + Obat;

                                                } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                                    ttlObatBpjs = ttlObatBpjs + Obat;

                                                } else {
                                                    ttlObatIks = ttlObatIks + Obat;
                                                }
                                                break;
                                            case "Ranap Dokter":
                                                ttlRanap_Dokter1=ttlRanap_Dokter1+rs1.getDouble("totbiaya");
                                                Ranap_Dokter1=rs1.getDouble("totbiaya");
                                                break;
                                            case "Ranap Dokter Paramedis":
                                                ttlRanap_Dokter1=ttlRanap_Dokter1+rs1.getDouble("totbiaya");
                                                Ranap_Dokter_Paramedis1=rs1.getDouble("totbiaya");
                                                break;
                                            case "Ranap Paramedis":
                                                ttlRanap_Paramedis1=ttlRanap_Paramedis1+rs1.getDouble("totbiaya");
                                                Ranap_Paramedis1=rs1.getDouble("totbiaya");
                                                break;
                                            case "Ralan Dokter":
                                                ttlRalan_Dokter1=ttlRalan_Dokter1+rs1.getDouble("totbiaya");
                                                Ralan_Dokter1=Ralan_Dokter1+rs1.getDouble("totbiaya");
                                                break; 
                                            case "Ralan Dokter Paramedis":
                                                ttlRalan_Dokter1=ttlRalan_Dokter1+rs1.getDouble("totbiaya");
                                                Ralan_Dokter_Paramedis1=rs1.getDouble("totbiaya");
                                                break;
                                            case "Ralan Paramedis":
                                                ttlRalan_Paramedis1=ttlRalan_Paramedis1+rs1.getDouble("totbiaya");
                                                Ralan_Paramedis1=rs1.getDouble("totbiaya");
                                                break;
                                            case "Kamar":
                                                ttlKamar1=ttlKamar1+rs1.getDouble("totbiaya");
                                                Kamar1=rs1.getDouble("totbiaya");
                                                break;
                                            case "Harian":
                                                ttlHarian1=ttlHarian1+rs1.getDouble("totbiaya");
                                                Harian1=rs1.getDouble("totbiaya");
                                                break;
                                            case "Retur Obat":
                                                ttlRetur_Obat1=ttlRetur_Obat1+rs1.getDouble("totbiaya");
                                                Retur_Obat1=rs1.getDouble("totbiaya");
                                                break;
                                            case "Resep Pulang":
                                                ttlResep_Pulang1=ttlResep_Pulang1+rs1.getDouble("totbiaya");
                                                Resep_Pulang1=rs1.getDouble("totbiaya");
                                                break;
                                            case "Service":
                                                ttlService1=ttlService1+rs1.getDouble("totbiaya");
                                                Service1=rs1.getDouble("totbiaya");
                                                break;
                                            case "Tambahan":
                                                ttlTambahan1=ttlTambahan1+rs1.getDouble("totbiaya");
                                                Tambahan1=Tambahan1+rs1.getDouble("totbiaya");
                                                break;
                                            case "Potongan":
                                                ttlPotongan1=ttlPotongan1+rs1.getDouble("totbiaya");
                                                Potongan1=Potongan1+rs1.getDouble("totbiaya");
                                                break;
                                            case "Registrasi":
                                                ttlRegistrasi1=ttlRegistrasi1+rs1.getDouble("totbiaya");
                                                Registrasi1=Registrasi1+rs1.getDouble("totbiaya");
                                                break;
                                            case "Operasi":
                                                ttlOperasi=ttlOperasi+rs1.getDouble("totbiaya");
                                                Operasi=Operasi+rs1.getDouble("totbiaya");
                                                
                                                if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                                    ttlOperasiUmum = ttlOperasiUmum + Operasi;

                                                } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                                    ttlOperasiBpjs = ttlOperasiBpjs + Operasi;

                                                } else {
                                                    ttlOperasiIks = ttlOperasiIks + Operasi;
                                                }
                                                break;
                                        }

                                    }
                                    
                                    
                                    if(rs4.getString("nm_poli").contains("UGD")) {
                                        if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                            ttlUgdUmum = ttlUgdUmum + Ranap_Dokter1+Ranap_Dokter_Paramedis1+Ranap_Paramedis1+Ralan_Dokter1+Ralan_Dokter_Paramedis1+Ralan_Paramedis1+Tambahan1+Registrasi1+Potongan1+Kamar1+Service1+Harian1;

                                        } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                            ttlUgdBpjs = ttlUgdBpjs + Ranap_Dokter1+Ranap_Dokter_Paramedis1+Ranap_Paramedis1+Ralan_Dokter1+Ralan_Dokter_Paramedis1+Ralan_Paramedis1+Tambahan1+Registrasi1+Potongan1+Kamar1+Service1+Harian1;

                                        } else {
                                            ttlUgdIks = ttlUgdIks + Ranap_Dokter1+Ranap_Dokter_Paramedis1+Ranap_Paramedis1+Ralan_Dokter1+Ralan_Dokter_Paramedis1+Ralan_Paramedis1+Tambahan1+Registrasi1+Potongan1+Kamar1+Service1+Harian1;
                                        }
                                        QtyUgd = QtyUgd+rs2.getDouble("qty");
                                    } else if(rs4.getString("nm_poli").contains("VK")) {
                                        if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                            ttlVkUmum = ttlVkUmum + Ranap_Dokter1+Ranap_Dokter_Paramedis1+Ranap_Paramedis1+Ralan_Dokter1+Ralan_Dokter_Paramedis1+Ralan_Paramedis1+Tambahan1+Registrasi1+Potongan1+Kamar1+Service1+Harian1;

                                        } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                            ttlVkBpjs = ttlVkBpjs + Ranap_Dokter1+Ranap_Dokter_Paramedis1+Ranap_Paramedis1+Ralan_Dokter1+Ralan_Dokter_Paramedis1+Ralan_Paramedis1+Tambahan1+Registrasi1+Potongan1+Kamar1+Service1+Harian1;

                                        } else {
                                            ttlVkIks = ttlVkIks + Ranap_Dokter1+Ranap_Dokter_Paramedis1+Ranap_Paramedis1+Ralan_Dokter1+Ralan_Dokter_Paramedis1+Ralan_Paramedis1+Tambahan1+Registrasi1+Potongan1+Kamar1+Service1+Harian1;
                                        }
                                        QtyVk = QtyVk+rs2.getDouble("qty");
                                    } else if(rs4.getString("nm_poli").contains("RADIOLOGI")) {
                                        if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                            ttlRadUmum = ttlRadUmum + Ranap_Dokter1+Ranap_Dokter_Paramedis1+Ranap_Paramedis1+Ralan_Dokter1+Ralan_Dokter_Paramedis1+Ralan_Paramedis1+Tambahan1+Registrasi1+Potongan1+Kamar1+Service1+Harian1;

                                        } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                            ttlRadBpjs = ttlRadBpjs + Ranap_Dokter1+Ranap_Dokter_Paramedis1+Ranap_Paramedis1+Ralan_Dokter1+Ralan_Dokter_Paramedis1+Ralan_Paramedis1+Tambahan1+Registrasi1+Potongan1+Kamar1+Service1+Harian1;

                                        } else {
                                            ttlRadIks = ttlRadIks + Ranap_Dokter1+Ranap_Dokter_Paramedis1+Ranap_Paramedis1+Ralan_Dokter1+Ralan_Dokter_Paramedis1+Ralan_Paramedis1+Tambahan1+Registrasi1+Potongan1+Kamar1+Service1+Harian1;

                                        }
                                        QtyRad = QtyRad+rs2.getDouble("qty");
                                    } else if(Pattern.compile(Pattern.quote("LAB"), Pattern.CASE_INSENSITIVE).matcher(rs4.getString("nm_poli")).find()) {
                                        if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                            ttlLabUmum = ttlLabUmum + Ranap_Dokter1+Ranap_Dokter_Paramedis1+Ranap_Paramedis1+Ralan_Dokter1+Ralan_Dokter_Paramedis1+Ralan_Paramedis1+Tambahan1+Registrasi1+Potongan1+Kamar1+Service1+Harian1;

                                        } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                            ttlLabBpjs = ttlLabBpjs + Ranap_Dokter1+Ranap_Dokter_Paramedis1+Ranap_Paramedis1+Ralan_Dokter1+Ralan_Dokter_Paramedis1+Ralan_Paramedis1+Tambahan1+Registrasi1+Potongan1+Kamar1+Service1+Harian1;

                                        } else {
                                            ttlLabIks = ttlLabIks + Ranap_Dokter1+Ranap_Dokter_Paramedis1+Ranap_Paramedis1+Ralan_Dokter1+Ralan_Dokter_Paramedis1+Ralan_Paramedis1+Tambahan1+Registrasi1+Potongan1+Kamar1+Service1+Harian1;

                                        }
                                        QtyLab = QtyLab+rs2.getDouble("qty");
                                    } 
                                    else{
                                        if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                            ttlPoliUmum = ttlPoliUmum + Ranap_Dokter1+Ranap_Dokter_Paramedis1+Ranap_Paramedis1+Ralan_Dokter1+Ralan_Dokter_Paramedis1+Ralan_Paramedis1+Tambahan1+Registrasi1+Potongan1+Kamar1+Service1+Harian1;

                                        } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                            ttlPoliBpjs = ttlPoliBpjs + Ranap_Dokter1+Ranap_Dokter_Paramedis1+Ranap_Paramedis1+Ralan_Dokter1+Ralan_Dokter_Paramedis1+Ralan_Paramedis1+Tambahan1+Registrasi1+Potongan1+Kamar1+Service1+Harian1;

                                        } else {
                                            ttlPoliIks = ttlPoliIks + Ranap_Dokter1+Ranap_Dokter_Paramedis1+Ranap_Paramedis1+Ralan_Dokter1+Ralan_Dokter_Paramedis1+Ralan_Paramedis1+Tambahan1+Registrasi1+Potongan1+Kamar1+Service1+Harian1;

                                        }
                                        QtyPoli = QtyPoli + rs2.getDouble("qty");

                                    }
                                    
                                } catch (Exception e) {
                                    System.out.println("Notif 2: "+e);
                                    e.printStackTrace();
                                } finally{
                                    if(rs1!=null){
                                        rs1.close();
                                    }
                                    if(ps1!=null){
                                        ps1.close();
                                    }
                                }
                            }}
                            
                        }
                    }catch(Exception e){
                        System.out.println("Notif 2 : "+e);
                        e.printStackTrace();
                    } finally{
                        if(rs2!=null){
                            rs2.close();
                        }
                        if(ps2!=null){
                            ps2.close();
                        }
                    }
                        
                    
                    while(rs8.next()){

                        String norawat1 =  rs8.getString("no_rawat");
                        List<String> norawat_list1 = new ArrayList<String>(Arrays.asList(norawat1.split(",")));


                        String markers2 = ",?".repeat(norawat_list1.size()).substring(1);

                        ps2= koneksi.prepareStatement("SELECT GROUP_CONCAT(DISTINCT reg_periksa.no_rawat) AS norawat1, penjab.kd_pj, " +
                            "penjab.png_jawab AS penjab, " +
                            "COUNT(reg_periksa.`no_rawat`) AS qty " +
                            "FROM reg_periksa " +
                            "INNER JOIN penjab ON penjab.kd_pj=reg_periksa.kd_pj " +
                            "WHERE reg_periksa.`no_rawat` IN( "+ markers2 + ") " +
                            "GROUP BY penjab.kd_pj ORDER BY penjab.png_jawab");
                    
                        try {
                            for (int o = 0; o < norawat_list1.size(); o++) {
                                ps2.setString(o + 1, norawat_list1.get(o));
                            }

                            rs2 = ps2.executeQuery();

                            while (rs2.next()) {
                                String norawat2 =  rs2.getString("norawat1");
                                List<String> norawat_list2 = new ArrayList<String>(Arrays.asList(norawat2.split(",")));

                                String markers3 = ",?".repeat(norawat_list2.size()).substring(1);

                                Operasi=0;Laborat=0;Radiologi=0;Obat=0;
                                Ranap_Dokter2=0;Ranap_Paramedis2=0;Ralan_Dokter2=0;Ranap_Dokter_Paramedis2=0;Ralan_Dokter_Paramedis2=0;
                                Ralan_Paramedis2=0;Tambahan2=0;Potongan2=0;Kamar2=0;
                                Registrasi2=0;Harian2=0;
                                Service2=0;

                                if(!rs8.getString("stts_pulang").equals("-")){
                                if(!rs8.getString("stts_pulang").equals("Pindah Kamar")){
                                    ps5=koneksi.prepareStatement("select billing.status, sum(billing.totalbiaya) as totbiaya from billing where billing.no_rawat in( "+ markers3 +" ) "
                                            + "GROUP BY billing.status ORDER BY billing.status REGEXP '^[a-z]' ASC");
                                    try {
                                        for (int o = 0; o < norawat_list2.size(); o++) {
                                            ps5.setString(o + 1, norawat_list2.get(o));
                                        }
                //                        ps4.setString(1,rs3.getString("no_rawat1"));
                                        rs5=ps5.executeQuery();
                                        if(!rs5.next()) {
                                            System.out.println("no data");
                                        }
                                        while(rs5.next()){
                                            switch (rs5.getString("status")) {
                                                case "Laborat":
                                                    ttlLaborat=ttlLaborat+rs5.getDouble("totbiaya");
                                                    Laborat=Laborat+rs5.getDouble("totbiaya");
                                                    
                                                    if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                                        ttlLabUmum = ttlLabUmum - Laborat;

                                                    } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                                        ttlLabBpjs = ttlLabBpjs - Laborat;

                                                    } else {
                                                        ttlLabIks = ttlLabIks - Laborat;

                                                    }
                                                    break;
                                                case "Radiologi":
                                                    ttlRadiologi=ttlRadiologi+rs5.getDouble("totbiaya");
                                                    Radiologi=Radiologi+rs5.getDouble("totbiaya");
                                                    
                                                    if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                                        ttlRadUmum = ttlRadUmum - Radiologi;

                                                    } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                                        ttlRadBpjs = ttlRadBpjs - Radiologi;

                                                    } else {
                                                        ttlRadIks = ttlRadIks - Radiologi;

                                                    }
                                                    break;
                                                case "Obat":
                                                    ttlObat=ttlObat+rs5.getDouble("totbiaya");
                                                    Obat=Obat+rs5.getDouble("totbiaya");
                                                    if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                                        ttlObatUmum = ttlObatUmum - Obat;

                                                    } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                                        ttlObatBpjs = ttlObatBpjs - Obat;

                                                    } else {
                                                        ttlObatIks = ttlObatIks - Obat;
                                                    }
                                                    break;
                                                case "Ranap Dokter":
                                                    ttlRanap_Dokter2=ttlRanap_Dokter2+rs5.getDouble("totbiaya");
                                                    Ranap_Dokter2=rs5.getDouble("totbiaya");
                                                    break;
                                                case "Ranap Dokter Paramedis":
                                                    ttlRanap_Dokter2=ttlRanap_Dokter2+rs5.getDouble("totbiaya");
                                                    Ranap_Dokter_Paramedis2=rs5.getDouble("totbiaya");
                                                    break;
                                                case "Ranap Paramedis":
                                                    ttlRanap_Paramedis2=ttlRanap_Paramedis2+rs5.getDouble("totbiaya");
                                                    Ranap_Paramedis2=rs5.getDouble("totbiaya");
                                                    break;
                                                case "Ralan Dokter":
                                                    ttlRalan_Dokter2=ttlRalan_Dokter2+rs5.getDouble("totbiaya");
                                                    Ralan_Dokter2=Ralan_Dokter2+rs5.getDouble("totbiaya");
                                                    break; 
                                                case "Ralan Dokter Paramedis":
                                                    ttlRalan_Dokter2=ttlRalan_Dokter2+rs5.getDouble("totbiaya");
                                                    Ralan_Dokter_Paramedis2=rs5.getDouble("totbiaya");
                                                    break;
                                                case "Ralan Paramedis":
                                                    ttlRalan_Paramedis2=ttlRalan_Paramedis2+rs5.getDouble("totbiaya");
                                                    Ralan_Paramedis2=rs5.getDouble("totbiaya");
                                                    break;
                                                case "Kamar":
                                                    ttlKamar2=ttlKamar2+rs5.getDouble("totbiaya");
                                                    Kamar2=rs5.getDouble("totbiaya");
                                                    break;
                                                case "Harian":
                                                    ttlHarian2=ttlHarian2+rs5.getDouble("totbiaya");
                                                    Harian2=rs5.getDouble("totbiaya");
                                                    break;
                                                case "Retur Obat":
                                                    ttlRetur_Obat2=ttlRetur_Obat2+rs5.getDouble("totbiaya");
                                                    Retur_Obat2=rs5.getDouble("totbiaya");
                                                    break;
                                                case "Resep Pulang":
                                                    ttlResep_Pulang2=ttlResep_Pulang2+rs5.getDouble("totbiaya");
                                                    Resep_Pulang2=rs5.getDouble("totbiaya");
                                                    break;
                                                case "Service":
                                                    ttlService2=ttlService2+rs5.getDouble("totbiaya");
                                                    Service2=rs5.getDouble("totbiaya");
                                                    break;
                                                case "Tambahan":
                                                    ttlTambahan2=ttlTambahan2+rs5.getDouble("totbiaya");
                                                    Tambahan2=Tambahan2+rs5.getDouble("totbiaya");
                                                    break;
                                                case "Potongan":
                                                    ttlPotongan2=ttlPotongan2+rs5.getDouble("totbiaya");
                                                    Potongan2=Potongan2+rs5.getDouble("totbiaya");
                                                    break;
                                                case "Registrasi":
                                                    ttlRegistrasi2=ttlRegistrasi2+rs5.getDouble("totbiaya");
                                                    Registrasi2=Registrasi2+rs5.getDouble("totbiaya");
                                                    break;
                                                case "Operasi":
                                                    ttlOperasi=ttlOperasi+rs5.getDouble("totbiaya");
                                                    Operasi=Operasi+rs5.getDouble("totbiaya");
                                                    
                                                    if(rs2.getString("kd_pj").equals("A05")) {
                                                        ttlOperasiUmum = ttlOperasiUmum - Operasi;

                                                    } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                                        ttlOperasiBpjs = ttlOperasiBpjs - Operasi;

                                                    } else {
                                                        ttlOperasiIks = ttlOperasiIks - Operasi;
                                                    }
                                                    break;
                                            }

                                        }
                                        if(rs8.getString("nm_poli").contains("UGD")) {
                                            if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                                ttlUgdUmum = ttlUgdUmum - Ranap_Dokter2-Ranap_Dokter_Paramedis2-Ranap_Paramedis2-Ralan_Dokter2-Ralan_Dokter_Paramedis2-Ralan_Paramedis2-Tambahan2-Registrasi2-Potongan2-Kamar2-Service2-Harian2;

                                            } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                                ttlUgdBpjs = ttlUgdBpjs - Ranap_Dokter2-Ranap_Dokter_Paramedis2-Ranap_Paramedis2-Ralan_Dokter2-Ralan_Dokter_Paramedis2-Ralan_Paramedis2-Tambahan2-Registrasi2-Potongan2-Kamar2-Service2-Harian2;

                                            } else {
                                                ttlUgdIks = ttlUgdIks - Ranap_Dokter2-Ranap_Dokter_Paramedis2-Ranap_Paramedis2-Ralan_Dokter2-Ralan_Dokter_Paramedis2-Ralan_Paramedis2-Tambahan2-Registrasi2-Potongan2-Kamar2-Service2-Harian2;
                                            }
//                                            QtyUgd = QtyUgd+rs2.getDouble("qty");
                                        } else if(rs8.getString("nm_poli").contains("VK")) {
                                            if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                                ttlVkUmum = ttlVkUmum - Ranap_Dokter2-Ranap_Dokter_Paramedis2-Ranap_Paramedis2-Ralan_Dokter2-Ralan_Dokter_Paramedis2-Ralan_Paramedis2-Tambahan2-Registrasi2-Potongan2-Kamar2-Service2-Harian2;

                                            } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                                ttlVkBpjs = ttlVkBpjs - Ranap_Dokter2-Ranap_Dokter_Paramedis2-Ranap_Paramedis2-Ralan_Dokter2-Ralan_Dokter_Paramedis2-Ralan_Paramedis2-Tambahan2-Registrasi2-Potongan2-Kamar2-Service2-Harian2;

                                            } else {
                                                ttlVkIks = ttlVkIks - Ranap_Dokter2-Ranap_Dokter_Paramedis2-Ranap_Paramedis2-Ralan_Dokter2-Ralan_Dokter_Paramedis2-Ralan_Paramedis2-Tambahan2-Registrasi2-Potongan2-Kamar2-Service2-Harian2;
                                            }
//                                            QtyVk = QtyVk+rs2.getDouble("qty");
                                        } else if(rs8.getString("nm_poli").contains("RADIOLOGI")) {
                                            if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                                ttlRadUmum = ttlRadUmum - Ranap_Dokter2-Ranap_Dokter_Paramedis2-Ranap_Paramedis2-Ralan_Dokter2-Ralan_Dokter_Paramedis2-Ralan_Paramedis2-Tambahan2-Registrasi2-Potongan2-Kamar2-Service2-Harian2;

                                            } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                                ttlRadBpjs = ttlRadBpjs - Ranap_Dokter2-Ranap_Dokter_Paramedis2-Ranap_Paramedis2-Ralan_Dokter2-Ralan_Dokter_Paramedis2-Ralan_Paramedis2-Tambahan2-Registrasi2-Potongan2-Kamar2-Service2-Harian2;

                                            } else {
                                                ttlRadIks = ttlRadIks - Ranap_Dokter2-Ranap_Dokter_Paramedis2-Ranap_Paramedis2-Ralan_Dokter2-Ralan_Dokter_Paramedis2-Ralan_Paramedis2-Tambahan2-Registrasi2-Potongan2-Kamar2-Service2-Harian2;

                                            }
//                                            QtyRad = QtyRad+rs2.getDouble("qty");
                                        } else if(Pattern.compile(Pattern.quote("LAB"), Pattern.CASE_INSENSITIVE).matcher(rs8.getString("nm_poli")).find()) {
                                            if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                                ttlLabUmum = ttlLabUmum - Ranap_Dokter2-Ranap_Dokter_Paramedis2-Ranap_Paramedis2-Ralan_Dokter2-Ralan_Dokter_Paramedis2-Ralan_Paramedis2-Tambahan2-Registrasi2-Potongan2-Kamar2-Service2-Harian2;

                                            } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                                ttlLabBpjs = ttlLabBpjs - Ranap_Dokter2-Ranap_Dokter_Paramedis2-Ranap_Paramedis2-Ralan_Dokter2-Ralan_Dokter_Paramedis2-Ralan_Paramedis2-Tambahan2-Registrasi2-Potongan2-Kamar2-Service2-Harian2;

                                            } else {
                                                ttlLabIks = ttlLabIks - Ranap_Dokter2-Ranap_Dokter_Paramedis2-Ranap_Paramedis2-Ralan_Dokter2-Ralan_Dokter_Paramedis2-Ralan_Paramedis2-Tambahan2-Registrasi2-Potongan2-Kamar2-Service2-Harian2;

                                            }
//                                            QtyLab = QtyLab+rs2.getDouble("qty");
                                        } 
                                        else{
                                            if(rs2.getString("kd_pj").equals("A05") || rs2.getString("kd_pj").equals("A07") || rs2.getString("kd_pj").equals("A08")) {
                                                ttlPoliUmum = ttlPoliUmum - Ranap_Dokter2-Ranap_Dokter_Paramedis2-Ranap_Paramedis2-Ralan_Dokter2-Ralan_Dokter_Paramedis2-Ralan_Paramedis2-Tambahan2-Registrasi2-Potongan2-Kamar2-Service2-Harian2;

                                            } else if (rs2.getString("kd_pj").equals("BPJ")) {
                                                ttlPoliBpjs = ttlPoliBpjs - Ranap_Dokter2-Ranap_Dokter_Paramedis2-Ranap_Paramedis2-Ralan_Dokter2-Ralan_Dokter_Paramedis2-Ralan_Paramedis2-Tambahan2-Registrasi2-Potongan2-Kamar2-Service2-Harian2;

                                            } else {
                                                ttlPoliIks = ttlPoliIks - Ranap_Dokter2-Ranap_Dokter_Paramedis2-Ranap_Paramedis2-Ralan_Dokter2-Ralan_Dokter_Paramedis2-Ralan_Paramedis2-Tambahan2-Registrasi2-Potongan2-Kamar2-Service2-Harian2;

                                            }
//                                            QtyPoli = QtyPoli + rs2.getDouble("qty");

                                        }
                                        
                                    } catch (Exception e) {
                                        System.out.println("Notif 2: "+e);
                                        e.printStackTrace();
                                    } finally{
                                        if(rs5!=null){
                                            rs5.close();
                                        }
                                        if(ps5!=null){
                                            ps5.close();
                                        }
                                    }


                                }}
                            }
                        } catch(Exception e){
                            System.out.println("Notif 2 : "+e);
                            e.printStackTrace();
                        } finally{
                            if(rs2!=null){
                                rs2.close();
                            }
                            if(ps2!=null){
                                ps2.close();
                            }
                        }
                    }
                }
                
                
                tabMode3.addRow(new Object[] {
                    "POLI",Valid.SetAngka(QtyPoli),Valid.SetAngka(ttlPoliUmum),
                    Valid.SetAngka(ttlPoliIks),
                    Valid.SetAngka(ttlPoliBpjs), 
                    Valid.SetAngka(ttlPoliUmum+ttlPoliIks+ttlPoliBpjs)
                    
                });
                
                tabMode3.addRow(new Object[] {
                    "OBAT","",Valid.SetAngka(ttlObatUmum),
                    Valid.SetAngka(ttlObatIks),
                    Valid.SetAngka(ttlObatBpjs), 
                    Valid.SetAngka(ttlObatUmum+ttlObatIks+ttlObatBpjs)
                });
                
                tabMode3.addRow(new Object[] {
                    "OPERASI","",Valid.SetAngka(ttlOperasiUmum),
                    Valid.SetAngka(ttlOperasiIks),
                    Valid.SetAngka(ttlOperasiBpjs), 
                    Valid.SetAngka(ttlOperasiUmum+ttlOperasiIks+ttlOperasiBpjs)
                });
                
                tabMode3.addRow(new Object[] {
                    "LABORAT",Valid.SetAngka(QtyLab),Valid.SetAngka(ttlLabUmum),
                    Valid.SetAngka(ttlLabIks),
                    Valid.SetAngka(ttlLabBpjs), 
                    Valid.SetAngka(ttlLabUmum+ttlLabIks+ttlLabBpjs)
                });
                
                tabMode3.addRow(new Object[] {
                    "RADIOLOGI",Valid.SetAngka(QtyRad),Valid.SetAngka(ttlRadUmum),
                    Valid.SetAngka(ttlRadIks),
                    Valid.SetAngka(ttlRadBpjs), 
                    Valid.SetAngka(ttlRadUmum+ttlRadIks+ttlRadBpjs)
                });
                
                tabMode3.addRow(new Object[] {
                    "UGD",Valid.SetAngka(QtyUgd),Valid.SetAngka(ttlUgdUmum),
                    Valid.SetAngka(ttlUgdIks),
                    Valid.SetAngka(ttlUgdBpjs), 
                    Valid.SetAngka(ttlUgdUmum+ttlUgdIks+ttlUgdBpjs)
                });
                
                tabMode3.addRow(new Object[] {
                    "VK",Valid.SetAngka(QtyVk),Valid.SetAngka(ttlVkUmum),
                    Valid.SetAngka(ttlVkIks),
                    Valid.SetAngka(ttlVkBpjs), 
                    Valid.SetAngka(ttlVkUmum+ttlVkIks+ttlVkBpjs)
                });
                tabMode3.addRow(new Object[] {
                    "TOTAL",Valid.SetAngka(QtyVk+QtyUgd+QtyRad+QtyLab+QtyPoli),
                    Valid.SetAngka(ttlVkUmum+ttlUgdUmum+ttlRadUmum+ttlLabUmum+ttlOperasiUmum+ttlObatUmum+ttlPoliUmum),
                    Valid.SetAngka(ttlVkIks+ttlUgdIks+ttlRadIks+ttlLabIks+ttlOperasiIks+ttlObatIks+ttlPoliIks),
                    Valid.SetAngka(ttlVkBpjs+ttlUgdBpjs+ttlRadBpjs+ttlLabBpjs+ttlOperasiBpjs+ttlObatBpjs+ttlPoliBpjs), 
                    Valid.SetAngka(ttlVkUmum+ttlVkIks+ttlVkBpjs+ttlUgdUmum+ttlUgdIks+ttlUgdBpjs+
                            ttlRadUmum+ttlRadIks+ttlRadBpjs+ttlLabUmum+ttlLabIks+ttlLabBpjs+
                            ttlOperasiUmum+ttlOperasiIks+ttlOperasiBpjs+ttlObatUmum+ttlObatIks+ttlObatBpjs+
                            ttlPoliUmum+ttlPoliIks+ttlPoliBpjs)
                });
                
                
            } catch (Exception e) {
                System.out.println("Notif 1 : "+e);
                e.printStackTrace();
            } finally{
                if(rs4!=null){
                    rs4.close();
                }
                if(ps4!=null){
                    ps4.close();
                }
                if(rs8!=null){
                    rs8.close();
                }
                if(ps8!=null){
                    ps8.close();
                }
            }
            
            
            
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
        this.setCursor(Cursor.getDefaultCursor());
    }

}
