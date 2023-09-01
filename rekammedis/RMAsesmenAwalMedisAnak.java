/*
 * Kontribusi dari Abdul Wahid, RSUD Cipayung Jakarta Timur
 */


package rekammedis;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import kepegawaian.DlgCariDokter;
import kepegawaian.DlgCariPetugas;


/**
 *
 * @author perpustakaan
 */
public final class RMAsesmenAwalMedisAnak extends javax.swing.JDialog {
    private final DefaultTableModel tabMode,tabModeMasalah,tabModeDetailMasalah;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps,ps2;
    private ResultSet rs,rs2;
    private int i=0,jml=0,index=0;
    private DlgCariDokter petugas=new DlgCariDokter(null,false);
    private boolean[] pilih; 
    private String[] kode,masalah;
    private String masalahkeperawatanigd=""; 
    private StringBuilder htmlContent;
    private File file;
    private FileWriter fileWriter;
    private String iyem;
    private ObjectMapper mapper = new ObjectMapper();
    private JsonNode root;
    private JsonNode response;
    private FileReader myObj;
    String no_rawat,no_rm,pasien,jk,tgllahir,ginjal_kanan,ginjal_kiri,
            sbuli,kd_petugas,nm_petugas,tgl_asuhan,finger="",pilihan="",kamar,namakamar,alamat,umur;
    
    /** Creates new form DlgRujuk
     * @param parent
     * @param modal */
    public RMAsesmenAwalMedisAnak(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        tabMode=new DefaultTableModel(null,new Object[]{
            "No.Rawat","No.RM","Nama Pasien","J.K.",
            "Tgl.Lahir","Tgl Periksa",
            "Ginjal Kanan","Ginjal Kiri","Buli",
            "Kode Dokter","Nama Petugas"
        }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbObat.setModel(tabMode);


        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModeMasalah=new DefaultTableModel(null,new Object[]{
                "P","KODE","MASALAH KEPERAWATAN"
            }){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tabModeDetailMasalah=new DefaultTableModel(null,new Object[]{
                "Kode","Masalah Keperawatan"
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };

        TNoRw.setDocument(new batasInput((byte)17).getKata(TNoRw));
        Eflorisensi.setDocument(new batasInput((int)150).getKata(Eflorisensi));
        HasilPenunjangNeonatologi.setDocument(new batasInput((int)150).getKata(HasilPenunjangNeonatologi));
       
        
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
        
        petugas.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(petugas.getTable().getSelectedRow()!= -1){ 
                    KdPetugas.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),0).toString());
                    NmPetugas.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),1).toString());   
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
        
        
        HTMLEditorKit kit = new HTMLEditorKit();
        LoadHTML.setEditable(true);
        LoadHTML.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule(
                ".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                ".isi2 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#323232;}"+
                ".isi3 td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                ".isi4 td{font: 11px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                ".isi5 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#AA0000;}"+
                ".isi6 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#FF0000;}"+
                ".isi7 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#C8C800;}"+
                ".isi8 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#00AA00;}"+
                ".isi9 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#969696;}"
        );
        Document doc = kit.createDefaultDocument();
        LoadHTML.setDocument(doc);
        
        
        isMenu();
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LoadHTML = new widget.editorpane();
        internalFrame1 = new widget.InternalFrame();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnEdit = new widget.Button();
        BtnPrint = new widget.Button();
        BtnAll = new widget.Button();
        BtnKeluar = new widget.Button();
        TabRawat = new javax.swing.JTabbedPane();
        internalFrame2 = new widget.InternalFrame();
        scrollInput = new widget.ScrollPane();
        FormInput = new widget.PanelBiasa();
        TNoRw = new widget.TextBox();
        TPasien = new widget.TextBox();
        TNoRM = new widget.TextBox();
        label14 = new widget.Label();
        KdPetugas = new widget.TextBox();
        NmPetugas = new widget.TextBox();
        BtnDokter = new widget.Button();
        jLabel8 = new widget.Label();
        TglLahir = new widget.TextBox();
        jLabel10 = new widget.Label();
        label11 = new widget.Label();
        jLabel11 = new widget.Label();
        scrollPane2 = new widget.ScrollPane();
        HasilPenunjangNeonatologi = new widget.TextArea();
        jLabel31 = new widget.Label();
        TglAsuhan = new widget.Tanggal();
        jLabel98 = new widget.Label();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel32 = new widget.Label();
        scrollPane4 = new widget.ScrollPane();
        KeluhanUtama = new widget.TextArea();
        jLabel35 = new widget.Label();
        scrollPane5 = new widget.ScrollPane();
        RiwPenyakitSkrg = new widget.TextArea();
        jLabel33 = new widget.Label();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        Jk1 = new widget.TextBox();
        jLabel36 = new widget.Label();
        Jk2 = new widget.TextBox();
        Rambut = new widget.TextBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jCheckBox7 = new javax.swing.JCheckBox();
        jLabel41 = new widget.Label();
        jCheckBox8 = new javax.swing.JCheckBox();
        jCheckBox10 = new javax.swing.JCheckBox();
        jCheckBox11 = new javax.swing.JCheckBox();
        jCheckBox12 = new javax.swing.JCheckBox();
        JamNeonatologi = new widget.TextBox();
        jLabel42 = new widget.Label();
        scrollPane3 = new widget.ScrollPane();
        Lokalisasi = new widget.TextArea();
        jLabel43 = new widget.Label();
        scrollPane7 = new widget.ScrollPane();
        DiagnosaKerjaNeonatologi = new widget.TextArea();
        jLabel44 = new widget.Label();
        scrollPane8 = new widget.ScrollPane();
        RencanaKerjaNeonatologi = new widget.TextArea();
        jLabel45 = new widget.Label();
        scrollPane9 = new widget.ScrollPane();
        TerapiNeonatologi = new widget.TextArea();
        jLabel46 = new widget.Label();
        jCheckBox14 = new javax.swing.JCheckBox();
        jCheckBox15 = new javax.swing.JCheckBox();
        jLabel47 = new widget.Label();
        jLabel48 = new widget.Label();
        jLabel49 = new widget.Label();
        Jk7 = new widget.TextBox();
        RuangNeonatologi = new widget.TextBox();
        TempatNeonatologi = new widget.TextBox();
        jLabel50 = new widget.Label();
        jLabel51 = new widget.Label();
        TglNeonatologi = new widget.TextBox();
        jSeparator13 = new javax.swing.JSeparator();
        jLabel52 = new widget.Label();
        jCheckBox16 = new javax.swing.JCheckBox();
        jLabel53 = new widget.Label();
        jCheckBox24 = new javax.swing.JCheckBox();
        jCheckBox25 = new javax.swing.JCheckBox();
        jCheckBox26 = new javax.swing.JCheckBox();
        jCheckBox27 = new javax.swing.JCheckBox();
        jLabel58 = new widget.Label();
        jLabel12 = new widget.Label();
        Jk3 = new widget.TextBox();
        jLabel77 = new widget.Label();
        Jk8 = new widget.TextBox();
        jLabel78 = new widget.Label();
        jCheckBox78 = new javax.swing.JCheckBox();
        Jk9 = new widget.TextBox();
        jLabel79 = new widget.Label();
        jCheckBox79 = new javax.swing.JCheckBox();
        Jk10 = new widget.TextBox();
        jLabel80 = new widget.Label();
        jCheckBox80 = new javax.swing.JCheckBox();
        Jk15 = new widget.TextBox();
        jLabel81 = new widget.Label();
        jCheckBox81 = new javax.swing.JCheckBox();
        Jk34 = new widget.TextBox();
        jLabel82 = new widget.Label();
        jCheckBox82 = new javax.swing.JCheckBox();
        Jk35 = new widget.TextBox();
        jLabel83 = new widget.Label();
        jLabel84 = new widget.Label();
        jLabel85 = new widget.Label();
        jCheckBox90 = new javax.swing.JCheckBox();
        jCheckBox91 = new javax.swing.JCheckBox();
        jCheckBox92 = new javax.swing.JCheckBox();
        jCheckBox93 = new javax.swing.JCheckBox();
        jCheckBox94 = new javax.swing.JCheckBox();
        Jk6 = new widget.TextBox();
        jCheckBox95 = new javax.swing.JCheckBox();
        Jk36 = new widget.TextBox();
        Jk38 = new widget.TextBox();
        jLabel86 = new widget.Label();
        jLabel87 = new widget.Label();
        Jk39 = new widget.TextBox();
        jLabel88 = new widget.Label();
        Jk40 = new widget.TextBox();
        jLabel89 = new widget.Label();
        Jk41 = new widget.TextBox();
        jLabel90 = new widget.Label();
        Jk42 = new widget.TextBox();
        jLabel91 = new widget.Label();
        jCheckBox75 = new javax.swing.JCheckBox();
        jCheckBox76 = new javax.swing.JCheckBox();
        jCheckBox77 = new javax.swing.JCheckBox();
        jLabel67 = new widget.Label();
        Jk43 = new widget.TextBox();
        jLabel92 = new widget.Label();
        Jk44 = new widget.TextBox();
        jLabel93 = new widget.Label();
        Jk45 = new widget.TextBox();
        jLabel94 = new widget.Label();
        Jk46 = new widget.TextBox();
        jLabel95 = new widget.Label();
        jSeparator14 = new javax.swing.JSeparator();
        scrollPane10 = new widget.ScrollPane();
        Lokalisasi1 = new widget.TextArea();
        internalFrame3 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbObat = new widget.Table();
        panelGlass9 = new widget.panelisi();
        jLabel19 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel21 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        PanelAccor = new widget.PanelBiasa();
        ChkAccor = new widget.CekBox();
        FormMenu = new widget.PanelBiasa();
        jLabel34 = new widget.Label();
        TNoRM1 = new widget.TextBox();
        TPasien1 = new widget.TextBox();
        FormMasalahRencana = new widget.PanelBiasa();
        scrollPane6 = new widget.ScrollPane();
        DetailRencana = new widget.TextArea();

        LoadHTML.setBorder(null);
        LoadHTML.setName("LoadHTML"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Asesmen Awal Medis Kulit Kelamin ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 54));
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
        BtnBatal.setText("Batal");
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
        panelGlass8.add(BtnPrint);

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
        panelGlass8.add(BtnAll);

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

        internalFrame1.add(panelGlass8, java.awt.BorderLayout.PAGE_END);

        TabRawat.setBackground(new java.awt.Color(254, 255, 254));
        TabRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        TabRawat.setName("TabRawat"); // NOI18N
        TabRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatMouseClicked(evt);
            }
        });

        internalFrame2.setBorder(null);
        internalFrame2.setName("internalFrame2"); // NOI18N
        internalFrame2.setLayout(new java.awt.BorderLayout(1, 1));

        scrollInput.setName("scrollInput"); // NOI18N
        scrollInput.setPreferredSize(new java.awt.Dimension(102, 557));

        FormInput.setBackground(new java.awt.Color(255, 255, 255));
        FormInput.setBorder(null);
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(470, 1400));
        FormInput.setLayout(null);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        TNoRw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TNoRwActionPerformed(evt);
            }
        });
        TNoRw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwKeyPressed(evt);
            }
        });
        FormInput.add(TNoRw);
        TNoRw.setBounds(74, 10, 131, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        TPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TPasienActionPerformed(evt);
            }
        });
        FormInput.add(TPasien);
        TPasien.setBounds(309, 10, 260, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        TNoRM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TNoRMActionPerformed(evt);
            }
        });
        FormInput.add(TNoRM);
        TNoRM.setBounds(207, 10, 100, 23);

        label14.setText("Petugas :");
        label14.setName("label14"); // NOI18N
        label14.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label14);
        label14.setBounds(0, 40, 70, 23);

        KdPetugas.setEditable(false);
        KdPetugas.setName("KdPetugas"); // NOI18N
        KdPetugas.setPreferredSize(new java.awt.Dimension(80, 23));
        KdPetugas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdPetugasKeyPressed(evt);
            }
        });
        FormInput.add(KdPetugas);
        KdPetugas.setBounds(74, 40, 100, 23);

        NmPetugas.setEditable(false);
        NmPetugas.setName("NmPetugas"); // NOI18N
        NmPetugas.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(NmPetugas);
        NmPetugas.setBounds(176, 40, 180, 23);

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
        BtnDokter.setBounds(358, 40, 28, 23);

        jLabel8.setText("Tgl.Lahir :");
        jLabel8.setName("jLabel8"); // NOI18N
        FormInput.add(jLabel8);
        jLabel8.setBounds(580, 10, 60, 23);

        TglLahir.setEditable(false);
        TglLahir.setHighlighter(null);
        TglLahir.setName("TglLahir"); // NOI18N
        FormInput.add(TglLahir);
        TglLahir.setBounds(644, 10, 80, 23);

        jLabel10.setText("No.Rawat :");
        jLabel10.setName("jLabel10"); // NOI18N
        FormInput.add(jLabel10);
        jLabel10.setBounds(0, 10, 70, 23);

        label11.setText("Tanggal :");
        label11.setName("label11"); // NOI18N
        label11.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label11);
        label11.setBounds(395, 40, 57, 23);

        jLabel11.setText("J.K. :");
        jLabel11.setName("jLabel11"); // NOI18N
        FormInput.add(jLabel11);
        jLabel11.setBounds(740, 10, 30, 23);

        scrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane2.setToolTipText("Jabarkan lokasi sesuai dengan regio anatomis");
        scrollPane2.setName("scrollPane2"); // NOI18N

        HasilPenunjangNeonatologi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        HasilPenunjangNeonatologi.setColumns(30);
        HasilPenunjangNeonatologi.setRows(5);
        HasilPenunjangNeonatologi.setToolTipText("Jabarkan lokasi sesuai dengan regio anatomis");
        HasilPenunjangNeonatologi.setName("HasilPenunjangNeonatologi"); // NOI18N
        HasilPenunjangNeonatologi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HasilPenunjangNeonatologiKeyPressed(evt);
            }
        });
        scrollPane2.setViewportView(HasilPenunjangNeonatologi);
        HasilPenunjangNeonatologi.getAccessibleContext().setAccessibleName("");

        FormInput.add(scrollPane2);
        scrollPane2.setBounds(170, 580, 260, 53);
        scrollPane2.getAccessibleContext().setAccessibleName("");
        scrollPane2.getAccessibleContext().setAccessibleDescription("");

        jLabel31.setText("Syaraf:");
        jLabel31.setToolTipText("");
        jLabel31.setName("jLabel31"); // NOI18N
        FormInput.add(jLabel31);
        jLabel31.setBounds(0, 210, 150, 23);

        TglAsuhan.setForeground(new java.awt.Color(50, 70, 50));
        TglAsuhan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "01-09-2023 18:01:25" }));
        TglAsuhan.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        TglAsuhan.setName("TglAsuhan"); // NOI18N
        TglAsuhan.setOpaque(false);
        TglAsuhan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TglAsuhanKeyPressed(evt);
            }
        });
        FormInput.add(TglAsuhan);
        TglAsuhan.setBounds(456, 40, 130, 23);

        jLabel98.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel98.setText("I. ANAMNESA");
        jLabel98.setToolTipText("");
        jLabel98.setName("jLabel98"); // NOI18N
        FormInput.add(jLabel98);
        jLabel98.setBounds(10, 70, 180, 23);

        jSeparator1.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator1.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator1.setName("jSeparator1"); // NOI18N
        FormInput.add(jSeparator1);
        jSeparator1.setBounds(0, 70, 880, 1);

        jLabel32.setText("Keluhan Utama:");
        jLabel32.setToolTipText("");
        jLabel32.setName("jLabel32"); // NOI18N
        FormInput.add(jLabel32);
        jLabel32.setBounds(0, 90, 150, 23);

        scrollPane4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane4.setName("scrollPane4"); // NOI18N

        KeluhanUtama.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        KeluhanUtama.setColumns(30);
        KeluhanUtama.setRows(5);
        KeluhanUtama.setName("KeluhanUtama"); // NOI18N
        KeluhanUtama.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeluhanUtamaKeyPressed(evt);
            }
        });
        scrollPane4.setViewportView(KeluhanUtama);

        FormInput.add(scrollPane4);
        scrollPane4.setBounds(170, 90, 260, 53);

        jLabel35.setText("bulan");
        jLabel35.setName("jLabel35"); // NOI18N
        FormInput.add(jLabel35);
        jLabel35.setBounds(840, 210, 30, 20);

        scrollPane5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane5.setColumnHeaderView(null);
        scrollPane5.setName("scrollPane5"); // NOI18N

        RiwPenyakitSkrg.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        RiwPenyakitSkrg.setColumns(30);
        RiwPenyakitSkrg.setRows(5);
        RiwPenyakitSkrg.setName("RiwPenyakitSkrg"); // NOI18N
        RiwPenyakitSkrg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RiwPenyakitSkrgKeyPressed(evt);
            }
        });
        scrollPane5.setViewportView(RiwPenyakitSkrg);

        FormInput.add(scrollPane5);
        scrollPane5.setBounds(610, 90, 260, 53);

        jLabel33.setText("Hasil Pemeriksaan Penunjang:");
        jLabel33.setToolTipText("");
        jLabel33.setName("jLabel33"); // NOI18N
        FormInput.add(jLabel33);
        jLabel33.setBounds(0, 580, 150, 23);

        jCheckBox1.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox1.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox1.setText("Tambahan:");
        jCheckBox1.setName("Pit. Alba"); // NOI18N
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox1);
        jCheckBox1.setBounds(640, 150, 80, 19);

        jCheckBox2.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox2.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox2.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox2.setText("Tidak Lengkap");
        jCheckBox2.setName("jCheckBox2"); // NOI18N
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox2);
        jCheckBox2.setBounds(530, 150, 110, 19);

        jCheckBox3.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox3.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox3.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox3.setText("Belum Lengkap");
        jCheckBox3.setName("jCheckBox3"); // NOI18N
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox3);
        jCheckBox3.setBounds(330, 150, 100, 19);

        jCheckBox4.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox4.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox4.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox4.setText("Dasar");
        jCheckBox4.setName("jCheckBox4"); // NOI18N
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox4);
        jCheckBox4.setBounds(170, 150, 80, 19);

        jCheckBox5.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox5.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox5.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox5.setText("Lengkap");
        jCheckBox5.setName("jCheckBox5"); // NOI18N
        jCheckBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox5ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox5);
        jCheckBox5.setBounds(430, 150, 80, 19);

        Jk1.setEditable(false);
        Jk1.setHighlighter(null);
        Jk1.setName("Jk1"); // NOI18N
        FormInput.add(Jk1);
        Jk1.setBounds(774, 10, 80, 23);

        jLabel36.setText("Riwayat Imunisasi:");
        jLabel36.setToolTipText("");
        jLabel36.setName("jLabel36"); // NOI18N
        FormInput.add(jLabel36);
        jLabel36.setBounds(0, 150, 150, 23);

        Jk2.setHighlighter(null);
        Jk2.setName("Jk2"); // NOI18N
        FormInput.add(Jk2);
        Jk2.setBounds(720, 150, 150, 23);

        Rambut.setHighlighter(null);
        Rambut.setName("Rambut"); // NOI18N
        FormInput.add(Rambut);
        Rambut.setBounds(430, 180, 150, 23);

        jCheckBox6.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox6.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox6.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox6.setText("Persalinan normal");
        jCheckBox6.setName("jCheckBox6"); // NOI18N
        jCheckBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox6ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox6);
        jCheckBox6.setBounds(170, 180, 120, 19);

        jCheckBox7.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox7.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox7.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox7.setText("Patologi, yaitu:");
        jCheckBox7.setName("jCheckBox7"); // NOI18N
        jCheckBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox7ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox7);
        jCheckBox7.setBounds(330, 180, 110, 19);

        jLabel41.setText("Riwayat Kelahiran:");
        jLabel41.setToolTipText("");
        jLabel41.setName("jLabel41"); // NOI18N
        FormInput.add(jLabel41);
        jLabel41.setBounds(0, 180, 150, 23);

        jCheckBox8.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox8.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox8.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox8.setText("Sesuai usia");
        jCheckBox8.setName("jCheckBox8"); // NOI18N
        jCheckBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox8ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox8);
        jCheckBox8.setBounds(170, 210, 150, 19);

        jCheckBox10.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox10.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox10.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox10.setText("Lambat, yaitu:");
        jCheckBox10.setName("jCheckBox10"); // NOI18N
        jCheckBox10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox10ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox10);
        jCheckBox10.setBounds(330, 210, 100, 19);

        jCheckBox11.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox11.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox11.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox11.setText("Menegakkan kepala");
        jCheckBox11.setName("jCheckBox11"); // NOI18N
        jCheckBox11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox11ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox11);
        jCheckBox11.setBounds(430, 210, 130, 19);

        jCheckBox12.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox12.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox12.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox12.setText("Membalikkan badan");
        jCheckBox12.setName("jCheckBox12"); // NOI18N
        jCheckBox12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox12ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox12);
        jCheckBox12.setBounds(670, 210, 130, 19);

        JamNeonatologi.setHighlighter(null);
        JamNeonatologi.setName("JamNeonatologi"); // NOI18N
        FormInput.add(JamNeonatologi);
        JamNeonatologi.setBounds(370, 700, 64, 23);

        jLabel42.setText("(Obat yang dibawa dari rumah)");
        jLabel42.setToolTipText("");
        jLabel42.setName("jLabel42"); // NOI18N
        FormInput.add(jLabel42);
        jLabel42.setBounds(0, 320, 150, 23);

        scrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane3.setToolTipText("Jabarkan lokasi sesuai dengan regio anatomis");
        scrollPane3.setName("scrollPane3"); // NOI18N

        Lokalisasi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Lokalisasi.setColumns(30);
        Lokalisasi.setRows(5);
        Lokalisasi.setToolTipText("Jabarkan lokasi sesuai dengan regio anatomis");
        Lokalisasi.setName("Lokalisasi"); // NOI18N
        Lokalisasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LokalisasiKeyPressed(evt);
            }
        });
        scrollPane3.setViewportView(Lokalisasi);

        FormInput.add(scrollPane3);
        scrollPane3.setBounds(170, 520, 260, 53);

        jLabel43.setText("Diagnosa Kerja/Diagnosa Banding:");
        jLabel43.setToolTipText("");
        jLabel43.setName("jLabel43"); // NOI18N
        FormInput.add(jLabel43);
        jLabel43.setBounds(430, 580, 170, 23);

        scrollPane7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane7.setToolTipText("Jabarkan lokasi sesuai dengan regio anatomis");
        scrollPane7.setName("scrollPane7"); // NOI18N

        DiagnosaKerjaNeonatologi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        DiagnosaKerjaNeonatologi.setColumns(30);
        DiagnosaKerjaNeonatologi.setRows(5);
        DiagnosaKerjaNeonatologi.setToolTipText("Jabarkan lokasi sesuai dengan regio anatomis");
        DiagnosaKerjaNeonatologi.setName("DiagnosaKerjaNeonatologi"); // NOI18N
        DiagnosaKerjaNeonatologi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DiagnosaKerjaNeonatologiKeyPressed(evt);
            }
        });
        scrollPane7.setViewportView(DiagnosaKerjaNeonatologi);

        FormInput.add(scrollPane7);
        scrollPane7.setBounds(610, 580, 260, 53);

        jLabel44.setText("Rencana Kerja:");
        jLabel44.setToolTipText("");
        jLabel44.setName("jLabel44"); // NOI18N
        FormInput.add(jLabel44);
        jLabel44.setBounds(0, 640, 150, 23);

        scrollPane8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane8.setToolTipText("Jabarkan lokasi sesuai dengan regio anatomis");
        scrollPane8.setName("scrollPane8"); // NOI18N

        RencanaKerjaNeonatologi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        RencanaKerjaNeonatologi.setColumns(30);
        RencanaKerjaNeonatologi.setRows(5);
        RencanaKerjaNeonatologi.setToolTipText("Jabarkan lokasi sesuai dengan regio anatomis");
        RencanaKerjaNeonatologi.setName("RencanaKerjaNeonatologi"); // NOI18N
        RencanaKerjaNeonatologi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RencanaKerjaNeonatologiKeyPressed(evt);
            }
        });
        scrollPane8.setViewportView(RencanaKerjaNeonatologi);

        FormInput.add(scrollPane8);
        scrollPane8.setBounds(170, 640, 260, 53);

        jLabel45.setText("Terapi/Tindakan:");
        jLabel45.setToolTipText("");
        jLabel45.setName("jLabel45"); // NOI18N
        FormInput.add(jLabel45);
        jLabel45.setBounds(430, 640, 170, 23);

        scrollPane9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane9.setToolTipText("Jabarkan lokasi sesuai dengan regio anatomis");
        scrollPane9.setName("scrollPane9"); // NOI18N

        TerapiNeonatologi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TerapiNeonatologi.setColumns(30);
        TerapiNeonatologi.setRows(5);
        TerapiNeonatologi.setToolTipText("Jabarkan lokasi sesuai dengan regio anatomis");
        TerapiNeonatologi.setName("TerapiNeonatologi"); // NOI18N
        TerapiNeonatologi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TerapiNeonatologiKeyPressed(evt);
            }
        });
        scrollPane9.setViewportView(TerapiNeonatologi);

        FormInput.add(scrollPane9);
        scrollPane9.setBounds(610, 640, 260, 53);

        jLabel46.setText("Di ruang:");
        jLabel46.setToolTipText("");
        jLabel46.setName("jLabel46"); // NOI18N
        FormInput.add(jLabel46);
        jLabel46.setBounds(260, 760, 100, 23);

        jCheckBox14.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox14.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox14.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox14.setText("Boleh pulang");
        jCheckBox14.setName("jCheckBox14"); // NOI18N
        jCheckBox14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox14ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox14);
        jCheckBox14.setBounds(170, 700, 130, 19);

        jCheckBox15.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox15.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox15.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox15.setText("Kontrol");
        jCheckBox15.setName("jCheckBox15"); // NOI18N
        jCheckBox15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox15ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox15);
        jCheckBox15.setBounds(170, 730, 130, 19);

        jLabel47.setText("Tanda-tanda Vital: Tekanan darah:");
        jLabel47.setToolTipText("");
        jLabel47.setName("jLabel47"); // NOI18N
        FormInput.add(jLabel47);
        jLabel47.setBounds(0, 470, 230, 23);

        jLabel48.setText("WITA");
        jLabel48.setToolTipText("");
        jLabel48.setName("jLabel48"); // NOI18N
        FormInput.add(jLabel48);
        jLabel48.setBounds(440, 700, 40, 23);

        jLabel49.setText("Tempat:");
        jLabel49.setToolTipText("");
        jLabel49.setName("jLabel49"); // NOI18N
        FormInput.add(jLabel49);
        jLabel49.setBounds(440, 730, 40, 23);

        Jk7.setHighlighter(null);
        Jk7.setName("Jk7"); // NOI18N
        Jk7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Jk7ActionPerformed(evt);
            }
        });
        FormInput.add(Jk7);
        Jk7.setBounds(800, 210, 40, 23);

        RuangNeonatologi.setHighlighter(null);
        RuangNeonatologi.setName("RuangNeonatologi"); // NOI18N
        FormInput.add(RuangNeonatologi);
        RuangNeonatologi.setBounds(370, 760, 64, 23);

        TempatNeonatologi.setHighlighter(null);
        TempatNeonatologi.setName("TempatNeonatologi"); // NOI18N
        FormInput.add(TempatNeonatologi);
        TempatNeonatologi.setBounds(490, 730, 64, 23);

        jLabel50.setText("Jam keluar:");
        jLabel50.setToolTipText("");
        jLabel50.setName("jLabel50"); // NOI18N
        FormInput.add(jLabel50);
        jLabel50.setBounds(260, 700, 100, 23);

        jLabel51.setText("Tanggal:");
        jLabel51.setToolTipText("");
        jLabel51.setName("jLabel51"); // NOI18N
        FormInput.add(jLabel51);
        jLabel51.setBounds(260, 730, 100, 23);

        TglNeonatologi.setHighlighter(null);
        TglNeonatologi.setName("TglNeonatologi"); // NOI18N
        FormInput.add(TglNeonatologi);
        TglNeonatologi.setBounds(370, 730, 64, 23);

        jSeparator13.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator13.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator13.setName("jSeparator13"); // NOI18N
        FormInput.add(jSeparator13);
        jSeparator13.setBounds(0, 400, 880, 1);

        jLabel52.setText("Disposisi:");
        jLabel52.setToolTipText("");
        jLabel52.setName("jLabel52"); // NOI18N
        FormInput.add(jLabel52);
        jLabel52.setBounds(0, 700, 150, 23);

        jCheckBox16.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox16.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox16.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox16.setText("Dirawat");
        jCheckBox16.setName("jCheckBox16"); // NOI18N
        jCheckBox16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox16ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox16);
        jCheckBox16.setBounds(170, 760, 130, 19);

        jLabel53.setText("Kesadaran:");
        jLabel53.setToolTipText("");
        jLabel53.setName("jLabel53"); // NOI18N
        FormInput.add(jLabel53);
        jLabel53.setBounds(0, 440, 150, 23);

        jCheckBox24.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox24.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox24.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox24.setText("CM");
        jCheckBox24.setName("jCheckBox24"); // NOI18N
        jCheckBox24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox24ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox24);
        jCheckBox24.setBounds(170, 440, 70, 19);

        jCheckBox25.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox25.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox25.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox25.setText("Apatis");
        jCheckBox25.setToolTipText("");
        jCheckBox25.setName("jCheckBox25"); // NOI18N
        jCheckBox25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox25ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox25);
        jCheckBox25.setBounds(270, 440, 80, 19);

        jCheckBox26.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox26.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox26.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox26.setText("Delirium");
        jCheckBox26.setName("jCheckBox26"); // NOI18N
        jCheckBox26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox26ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox26);
        jCheckBox26.setBounds(380, 440, 100, 19);

        jCheckBox27.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox27.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox27.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox27.setText("Koma");
        jCheckBox27.setName("jCheckBox27"); // NOI18N
        jCheckBox27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox27ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox27);
        jCheckBox27.setBounds(770, 440, 80, 19);

        jLabel58.setText("STATUS PEDIATRIC:");
        jLabel58.setToolTipText("");
        jLabel58.setName("jLabel58"); // NOI18N
        FormInput.add(jLabel58);
        jLabel58.setBounds(0, 520, 150, 23);

        jLabel12.setText("Alamat:");
        jLabel12.setName("jLabel12"); // NOI18N
        FormInput.add(jLabel12);
        jLabel12.setBounds(720, 40, 50, 23);

        Jk3.setEditable(false);
        Jk3.setHighlighter(null);
        Jk3.setName("Jk3"); // NOI18N
        FormInput.add(Jk3);
        Jk3.setBounds(774, 40, 80, 23);

        jLabel77.setText("Riwayat Penyakit Sekarang:");
        jLabel77.setName("jLabel77"); // NOI18N
        FormInput.add(jLabel77);
        jLabel77.setBounds(430, 90, 175, 20);

        Jk8.setHighlighter(null);
        Jk8.setName("Jk8"); // NOI18N
        FormInput.add(Jk8);
        Jk8.setBounds(560, 210, 40, 23);

        jLabel78.setText("bulan");
        jLabel78.setName("jLabel78"); // NOI18N
        FormInput.add(jLabel78);
        jLabel78.setBounds(600, 210, 30, 20);

        jCheckBox78.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox78.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox78.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox78.setText("Duduk");
        jCheckBox78.setName("jCheckBox78"); // NOI18N
        jCheckBox78.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox78ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox78);
        jCheckBox78.setBounds(430, 240, 60, 19);

        Jk9.setHighlighter(null);
        Jk9.setName("Jk9"); // NOI18N
        FormInput.add(Jk9);
        Jk9.setBounds(500, 240, 40, 23);

        jLabel79.setText("bulan");
        jLabel79.setName("jLabel79"); // NOI18N
        FormInput.add(jLabel79);
        jLabel79.setBounds(540, 240, 30, 20);

        jCheckBox79.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox79.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox79.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox79.setText("Merangkak");
        jCheckBox79.setName("jCheckBox79"); // NOI18N
        jCheckBox79.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox79ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox79);
        jCheckBox79.setBounds(580, 240, 80, 19);

        Jk10.setHighlighter(null);
        Jk10.setName("Jk10"); // NOI18N
        FormInput.add(Jk10);
        Jk10.setBounds(660, 240, 40, 23);

        jLabel80.setText("bulan");
        jLabel80.setName("jLabel80"); // NOI18N
        FormInput.add(jLabel80);
        jLabel80.setBounds(700, 240, 30, 20);

        jCheckBox80.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox80.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox80.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox80.setText("Berdiri");
        jCheckBox80.setName("jCheckBox80"); // NOI18N
        jCheckBox80.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox80ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox80);
        jCheckBox80.setBounds(740, 240, 60, 20);

        Jk15.setHighlighter(null);
        Jk15.setName("Jk15"); // NOI18N
        FormInput.add(Jk15);
        Jk15.setBounds(800, 240, 40, 20);

        jLabel81.setText("bulan");
        jLabel81.setName("jLabel81"); // NOI18N
        FormInput.add(jLabel81);
        jLabel81.setBounds(840, 240, 30, 20);

        jCheckBox81.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox81.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox81.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox81.setText("Berjalan");
        jCheckBox81.setName("jCheckBox81"); // NOI18N
        jCheckBox81.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox81ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox81);
        jCheckBox81.setBounds(430, 270, 70, 19);

        Jk34.setHighlighter(null);
        Jk34.setName("Jk34"); // NOI18N
        FormInput.add(Jk34);
        Jk34.setBounds(500, 270, 40, 23);

        jLabel82.setText("bulan");
        jLabel82.setName("jLabel82"); // NOI18N
        FormInput.add(jLabel82);
        jLabel82.setBounds(540, 270, 30, 20);

        jCheckBox82.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox82.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox82.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox82.setText("Bicara");
        jCheckBox82.setName("jCheckBox82"); // NOI18N
        jCheckBox82.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox82ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox82);
        jCheckBox82.setBounds(580, 270, 70, 19);

        Jk35.setHighlighter(null);
        Jk35.setName("Jk35"); // NOI18N
        FormInput.add(Jk35);
        Jk35.setBounds(660, 270, 40, 23);

        jLabel83.setText("bulan");
        jLabel83.setName("jLabel83"); // NOI18N
        FormInput.add(jLabel83);
        jLabel83.setBounds(700, 270, 30, 20);

        jLabel84.setText("Riwayat Alergi:");
        jLabel84.setToolTipText("");
        jLabel84.setName("jLabel84"); // NOI18N
        FormInput.add(jLabel84);
        jLabel84.setBounds(460, 300, 150, 23);

        jLabel85.setText("Riwayat Pengobatan:");
        jLabel85.setToolTipText("");
        jLabel85.setName("jLabel85"); // NOI18N
        FormInput.add(jLabel85);
        jLabel85.setBounds(0, 300, 150, 23);

        jCheckBox90.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox90.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox90.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox90.setText("Ya");
        jCheckBox90.setName("jCheckBox90"); // NOI18N
        jCheckBox90.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox90ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox90);
        jCheckBox90.setBounds(680, 300, 50, 19);

        jCheckBox91.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox91.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox91.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox91.setText("Tidak");
        jCheckBox91.setName("jCheckBox91"); // NOI18N
        jCheckBox91.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox91ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox91);
        jCheckBox91.setBounds(620, 300, 60, 19);

        jCheckBox92.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox92.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox92.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox92.setText("Obat");
        jCheckBox92.setName("jCheckBox92"); // NOI18N
        jCheckBox92.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox92ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox92);
        jCheckBox92.setBounds(730, 300, 60, 19);

        jCheckBox93.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox93.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox93.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox93.setText("Makanan");
        jCheckBox93.setName("jCheckBox93"); // NOI18N
        jCheckBox93.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox93ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox93);
        jCheckBox93.setBounds(790, 300, 110, 19);

        jCheckBox94.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox94.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox94.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox94.setText("Lainnya:");
        jCheckBox94.setName("jCheckBox94"); // NOI18N
        jCheckBox94.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox94ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox94);
        jCheckBox94.setBounds(620, 330, 80, 19);

        Jk6.setHighlighter(null);
        Jk6.setName("Jk6"); // NOI18N
        FormInput.add(Jk6);
        Jk6.setBounds(720, 330, 150, 23);

        jCheckBox95.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox95.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox95.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox95.setText("Reaksi yang timbul");
        jCheckBox95.setName("jCheckBox95"); // NOI18N
        jCheckBox95.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox95ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox95);
        jCheckBox95.setBounds(620, 360, 120, 19);

        Jk36.setHighlighter(null);
        Jk36.setName("Jk36"); // NOI18N
        FormInput.add(Jk36);
        Jk36.setBounds(740, 360, 130, 23);

        Jk38.setHighlighter(null);
        Jk38.setName("Jk38"); // NOI18N
        FormInput.add(Jk38);
        Jk38.setBounds(220, 410, 40, 23);

        jLabel86.setText("GCS: E");
        jLabel86.setName("jLabel86"); // NOI18N
        FormInput.add(jLabel86);
        jLabel86.setBounds(170, 410, 40, 20);

        jLabel87.setText("V");
        jLabel87.setName("jLabel87"); // NOI18N
        FormInput.add(jLabel87);
        jLabel87.setBounds(260, 410, 20, 20);

        Jk39.setHighlighter(null);
        Jk39.setName("Jk39"); // NOI18N
        FormInput.add(Jk39);
        Jk39.setBounds(290, 410, 40, 23);

        jLabel88.setText("M");
        jLabel88.setName("jLabel88"); // NOI18N
        FormInput.add(jLabel88);
        jLabel88.setBounds(330, 410, 20, 20);

        Jk40.setHighlighter(null);
        Jk40.setName("Jk40"); // NOI18N
        FormInput.add(Jk40);
        Jk40.setBounds(360, 410, 40, 23);

        jLabel89.setText("BB:");
        jLabel89.setName("jLabel89"); // NOI18N
        FormInput.add(jLabel89);
        jLabel89.setBounds(410, 410, 20, 20);

        Jk41.setHighlighter(null);
        Jk41.setName("Jk41"); // NOI18N
        FormInput.add(Jk41);
        Jk41.setBounds(430, 410, 40, 23);

        jLabel90.setText("kg. TB:");
        jLabel90.setName("jLabel90"); // NOI18N
        FormInput.add(jLabel90);
        jLabel90.setBounds(470, 410, 40, 20);

        Jk42.setHighlighter(null);
        Jk42.setName("Jk42"); // NOI18N
        FormInput.add(Jk42);
        Jk42.setBounds(510, 410, 40, 23);

        jLabel91.setText("cm");
        jLabel91.setName("jLabel91"); // NOI18N
        FormInput.add(jLabel91);
        jLabel91.setBounds(550, 410, 20, 20);

        jCheckBox75.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox75.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox75.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox75.setText("Somnolen");
        jCheckBox75.setName("jCheckBox75"); // NOI18N
        jCheckBox75.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox75ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox75);
        jCheckBox75.setBounds(480, 440, 80, 19);

        jCheckBox76.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox76.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox76.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox76.setText("Sopor");
        jCheckBox76.setName("jCheckBox76"); // NOI18N
        jCheckBox76.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox76ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox76);
        jCheckBox76.setBounds(580, 440, 80, 19);

        jCheckBox77.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox77.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox77.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox77.setText("Pre Koma");
        jCheckBox77.setName("jCheckBox77"); // NOI18N
        jCheckBox77.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox77ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox77);
        jCheckBox77.setBounds(670, 440, 80, 19);

        jLabel67.setText("KEADAAN UMUM:");
        jLabel67.setToolTipText("");
        jLabel67.setName("jLabel67"); // NOI18N
        FormInput.add(jLabel67);
        jLabel67.setBounds(0, 410, 150, 23);

        Jk43.setHighlighter(null);
        Jk43.setName("Jk43"); // NOI18N
        Jk43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Jk43ActionPerformed(evt);
            }
        });
        FormInput.add(Jk43);
        Jk43.setBounds(340, 470, 40, 23);

        jLabel92.setText("x/Menit   Suhu:");
        jLabel92.setName("jLabel92"); // NOI18N
        FormInput.add(jLabel92);
        jLabel92.setBounds(380, 470, 80, 20);

        Jk44.setHighlighter(null);
        Jk44.setName("Jk44"); // NOI18N
        FormInput.add(Jk44);
        Jk44.setBounds(230, 470, 40, 23);

        jLabel93.setText("MmHg   Nadi:");
        jLabel93.setName("jLabel93"); // NOI18N
        FormInput.add(jLabel93);
        jLabel93.setBounds(270, 470, 70, 20);

        Jk45.setHighlighter(null);
        Jk45.setName("Jk45"); // NOI18N
        FormInput.add(Jk45);
        Jk45.setBounds(460, 470, 40, 23);

        jLabel94.setText("°C   Respirasi:");
        jLabel94.setName("jLabel94"); // NOI18N
        FormInput.add(jLabel94);
        jLabel94.setBounds(500, 470, 70, 20);

        Jk46.setHighlighter(null);
        Jk46.setName("Jk46"); // NOI18N
        FormInput.add(Jk46);
        Jk46.setBounds(570, 470, 40, 23);

        jLabel95.setText("x/Menit");
        jLabel95.setName("jLabel95"); // NOI18N
        FormInput.add(jLabel95);
        jLabel95.setBounds(610, 470, 40, 20);

        jSeparator14.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator14.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator14.setName("jSeparator14"); // NOI18N
        FormInput.add(jSeparator14);
        jSeparator14.setBounds(0, 510, 880, 1);

        scrollPane10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane10.setToolTipText("Jabarkan lokasi sesuai dengan regio anatomis");
        scrollPane10.setName("scrollPane10"); // NOI18N

        Lokalisasi1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Lokalisasi1.setColumns(30);
        Lokalisasi1.setRows(5);
        Lokalisasi1.setToolTipText("Jabarkan lokasi sesuai dengan regio anatomis");
        Lokalisasi1.setName("Lokalisasi1"); // NOI18N
        Lokalisasi1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Lokalisasi1KeyPressed(evt);
            }
        });
        scrollPane10.setViewportView(Lokalisasi1);

        FormInput.add(scrollPane10);
        scrollPane10.setBounds(170, 300, 260, 53);

        scrollInput.setViewportView(FormInput);

        internalFrame2.add(scrollInput, java.awt.BorderLayout.CENTER);

        TabRawat.addTab("Input Penilaian", internalFrame2);

        internalFrame3.setBorder(null);
        internalFrame3.setName("internalFrame3"); // NOI18N
        internalFrame3.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);
        Scroll.setPreferredSize(new java.awt.Dimension(452, 200));

        tbObat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
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
        tbObat.getAccessibleContext().setAccessibleName("");

        internalFrame3.add(Scroll, java.awt.BorderLayout.CENTER);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel19.setText("Tgl.Asuhan :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "01-09-2023" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(DTPCari1);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("s.d.");
        jLabel21.setName("jLabel21"); // NOI18N
        jLabel21.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass9.add(jLabel21);

        DTPCari2.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "01-09-2023" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(DTPCari2);

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass9.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(195, 23));
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

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass9.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass9.add(LCount);

        internalFrame3.add(panelGlass9, java.awt.BorderLayout.PAGE_END);

        PanelAccor.setBackground(new java.awt.Color(255, 255, 255));
        PanelAccor.setName("PanelAccor"); // NOI18N
        PanelAccor.setPreferredSize(new java.awt.Dimension(470, 43));
        PanelAccor.setLayout(new java.awt.BorderLayout(1, 1));

        ChkAccor.setBackground(new java.awt.Color(255, 250, 248));
        ChkAccor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kiri.png"))); // NOI18N
        ChkAccor.setSelected(true);
        ChkAccor.setFocusable(false);
        ChkAccor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkAccor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkAccor.setName("ChkAccor"); // NOI18N
        ChkAccor.setPreferredSize(new java.awt.Dimension(15, 20));
        ChkAccor.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kiri.png"))); // NOI18N
        ChkAccor.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kanan.png"))); // NOI18N
        ChkAccor.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kanan.png"))); // NOI18N
        ChkAccor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkAccorActionPerformed(evt);
            }
        });
        PanelAccor.add(ChkAccor, java.awt.BorderLayout.WEST);

        FormMenu.setBackground(new java.awt.Color(255, 255, 255));
        FormMenu.setBorder(null);
        FormMenu.setName("FormMenu"); // NOI18N
        FormMenu.setPreferredSize(new java.awt.Dimension(115, 43));
        FormMenu.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 9));

        jLabel34.setText("Pasien :");
        jLabel34.setName("jLabel34"); // NOI18N
        jLabel34.setPreferredSize(new java.awt.Dimension(55, 23));
        FormMenu.add(jLabel34);

        TNoRM1.setEditable(false);
        TNoRM1.setHighlighter(null);
        TNoRM1.setName("TNoRM1"); // NOI18N
        TNoRM1.setPreferredSize(new java.awt.Dimension(100, 23));
        FormMenu.add(TNoRM1);

        TPasien1.setEditable(false);
        TPasien1.setBackground(new java.awt.Color(245, 250, 240));
        TPasien1.setHighlighter(null);
        TPasien1.setName("TPasien1"); // NOI18N
        TPasien1.setPreferredSize(new java.awt.Dimension(250, 23));
        FormMenu.add(TPasien1);

        PanelAccor.add(FormMenu, java.awt.BorderLayout.NORTH);

        FormMasalahRencana.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 254)));
        FormMasalahRencana.setName("FormPenilaian"); // NOI18N
        FormMasalahRencana.setLayout(new java.awt.GridLayout(1, 0, 1, 1));

        scrollPane6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 254)), "Hasil USG Urologi", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        scrollPane6.setName("scrollPane6"); // NOI18N

        DetailRencana.setEditable(false);
        DetailRencana.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        DetailRencana.setColumns(20);
        DetailRencana.setRows(20);
        DetailRencana.setTabSize(10);
        DetailRencana.setToolTipText("");
        DetailRencana.setName("DetailRencana"); // NOI18N
        DetailRencana.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DetailRencanaKeyPressed(evt);
            }
        });
        scrollPane6.setViewportView(DetailRencana);

        FormMasalahRencana.add(scrollPane6);
        scrollPane6.getAccessibleContext().setAccessibleName("Buli :");
        scrollPane6.getAccessibleContext().setAccessibleDescription("");

        PanelAccor.add(FormMasalahRencana, java.awt.BorderLayout.CENTER);

        internalFrame3.add(PanelAccor, java.awt.BorderLayout.EAST);

        TabRawat.addTab("Data Penilaian", internalFrame3);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TNoRM.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"Nama Pasien");
        }else 
            if(Eflorisensi.getText().trim().equals("")){
            Valid.textKosong(Eflorisensi,"Ginjal Kanan");
        }else if(HasilPenunjangNeonatologi.getText().trim().equals("")){
            Valid.textKosong(HasilPenunjangNeonatologi,"Ginjal Kiri");
        }else{
            if(Sequel.menyimpantf("penilaian_usg_urologi","?,?,?,?,?,?"
                    + "","No.Rawat",6,new String[]{
                    TNoRw.getText(),
                        Valid.SetTgl(TglAsuhan.getSelectedItem()+"")+" "+TglAsuhan.getSelectedItem().toString().substring(11,19),
                        Eflorisensi.getText(),
                HasilPenunjangNeonatologi.getText(),
                    KdPetugas.getText()
                })==true){

                    emptTeks();
            }
        }
    
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
//            Valid.pindah(evt,Rencana,BtnBatal);
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        emptTeks();
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            emptTeks();
        }else{Valid.pindah(evt, BtnSimpan, BtnHapus);}
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if(tbObat.getSelectedRow()>-1){
            if(Sequel.queryu2tf("delete from penilaian_usg_urologi where no_rawat=?",1,new String[]{
                tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
            })==true){
                TNoRM1.setText("");
                TPasien1.setText("");
                Valid.tabelKosong(tabModeDetailMasalah);
                ChkAccor.setSelected(false);
                isMenu();
                tampil();
                emptTeks();
            }else{
                JOptionPane.showMessageDialog(null,"Gagal menghapus..!!");
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
        if(TNoRM.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"Nama Pasien");
        }else 
            if(Eflorisensi.getText().trim().equals("")){
            Valid.textKosong(Eflorisensi,"Ginjal Kanan");
        }else if(HasilPenunjangNeonatologi.getText().trim().equals("")){
            Valid.textKosong(HasilPenunjangNeonatologi,"Ginjal Kiri");
        }else{
            if(tbObat.getSelectedRow()>-1){
                if(Sequel.mengedittf("penilaian_usg_urologi","no_rawat=?","no_rawat=?,tanggal=?,ginjal_kanan=?,ginjal_kiri=?,buli=?,"+
                    "kd_dokter=?",7,new String[]{
                    TNoRw.getText(),Valid.SetTgl(TglAsuhan.getSelectedItem()+"")+" "+TglAsuhan.getSelectedItem().toString().substring(11,19),
                        Eflorisensi.getText(),HasilPenunjangNeonatologi.getText(),KdPetugas.getText(),TNoRw.getText()
                     })==true){
                    
                        tampil();
                        emptTeks();
                        TabRawat.setSelectedIndex(1);
                    
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
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnKeluarActionPerformed(null);
        }else{Valid.pindah(evt,BtnEdit,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed

        if(tbObat.getSelectedRow()== -1){
               JOptionPane.showMessageDialog(null,"Maaf, silahkan pilih data terlebih dahulu...!!!!"); 
        }else{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); 
            getData();
            tgl_asuhan = Valid.SetTgl(TglAsuhan.getSelectedItem()+"")+" "+TglAsuhan.getSelectedItem().toString().substring(11,19);
            String[] tgl = tgl_asuhan.split(" ");
            
            umur = Sequel.cariIsi("select umur from pasien where no_rkm_medis=?",no_rm);
            alamat = Sequel.cariIsi("select concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab) as alamat from pasien inner join kelurahan "
                    + "inner join kecamatan inner join kabupaten on pasien.kd_kel=kelurahan.kd_kel and pasien.kd_kec=kecamatan.kd_kec and pasien.kd_kab=kabupaten.kd_kab "
                    + "where no_rkm_medis=? ",no_rm);
            
            kamar=Sequel.cariIsi("select ifnull(kd_kamar,'') from kamar_inap where no_rawat='"+no_rawat+"' order by tgl_masuk desc limit 1");
            
            if(!kamar.equals("")){
                namakamar=kamar+", "+Sequel.cariIsi("select nm_bangsal from bangsal inner join kamar on bangsal.kd_bangsal=kamar.kd_bangsal "+
                            " where kamar.kd_kamar='"+kamar+"' ");            
                kamar="Kamar";
            }else if(kamar.equals("")){
                kamar="Poli";
                namakamar=Sequel.cariIsi("select nm_poli from poliklinik inner join reg_periksa on poliklinik.kd_poli=reg_periksa.kd_poli "+
                            "where reg_periksa.no_rawat='"+no_rawat+"'");
                
            }
            
            Map<String, Object> param = new HashMap<>();
            param.put("noperiksa",no_rawat);
            param.put("norm",no_rm);
            param.put("namapasien",pasien);
            param.put("jkel",jk);
            param.put("umur",umur);
            param.put("lahir",tgllahir);
            param.put("tanggal",Valid.SetTgl3(tgl[0]));
            param.put("petugas",nm_petugas);
            param.put("alamat",alamat);
            param.put("kamar",kamar);
            param.put("namakamar",namakamar);
            param.put("jam",tgl[1]);
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());
            param.put("hasil","Ginjal Kanan\t: "+ginjal_kanan + "\n\nGinjal Kiri\t: "+ginjal_kiri+"\n\nBuli\t\t: " +sbuli);
            param.put("logo",Sequel.cariGambar("select setting.logo from setting"));
            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",kd_petugas);
            param.put("finger2","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+nm_petugas+"\nID "+(finger.equals("")?kd_petugas:finger)+"\n"+Valid.SetTgl3(tgl[0]));  

            pilihan = (String)JOptionPane.showInputDialog(null,"Silahkan pilih hasil pemeriksaan..!","Hasil Pemeriksaan",JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Model 1","PDF Model 1"},"Model 1");

            if(pilihan!=null && pilihan !="null") {
                switch (pilihan) {
                    case "Model 1":
                          Valid.MyReport("rptPeriksaUrologi.jasper","report","::[ Pemeriksaan Urologi ]::",param);
                          break;
                    case "PDF Model 1":
                          Valid.MyReportPDF("rptPeriksaUrologi.jasper","report","::[ Pemeriksaan Urologi ]::",param);
                          break;
                    
                }    
            }
                                
            
            this.setCursor(Cursor.getDefaultCursor());
        }
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnEdit, BtnKeluar);
        }
}//GEN-LAST:event_BtnPrintKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        tampil();
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            TCari.setText("");
            tampil();
        }else{
            Valid.pindah(evt, BtnCari, TPasien);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
        if(TabRawat.getSelectedIndex()==1){
            tampil();
        }
    }//GEN-LAST:event_TabRawatMouseClicked

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        
    }//GEN-LAST:event_formWindowOpened

    private void TglAsuhanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TglAsuhanKeyPressed
//        Valid.pindah2(evt,Rencana,RPD);
    }//GEN-LAST:event_TglAsuhanKeyPressed

    private void HasilPenunjangNeonatologiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HasilPenunjangNeonatologiKeyPressed
//        Valid.pindah2(evt,KeluhanUtama,RPO);
    }//GEN-LAST:event_HasilPenunjangNeonatologiKeyPressed

    private void BtnDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDokterKeyPressed
        //Valid.pindah(evt,Monitoring,BtnSimpan);
    }//GEN-LAST:event_BtnDokterKeyPressed

    private void BtnDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokterActionPerformed
        petugas.isCek();
        petugas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        petugas.setLocationRelativeTo(internalFrame1);
        petugas.setAlwaysOnTop(false);
        petugas.setVisible(true);
    }//GEN-LAST:event_BtnDokterActionPerformed

    private void KdPetugasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdPetugasKeyPressed

    }//GEN-LAST:event_KdPetugasKeyPressed

    private void TNoRMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TNoRMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRMActionPerformed

    private void TPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TPasienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TPasienActionPerformed

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isRawat();
        }else{
            Valid.pindah(evt,TCari,BtnDokter);
        }
    }//GEN-LAST:event_TNoRwKeyPressed

    private void TNoRwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TNoRwActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRwActionPerformed

    private void tbObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                ChkAccor.setSelected(true);
                isMenu();
                getMasalah();
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tbObatMouseClicked

    private void tbObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatKeyPressed
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    ChkAccor.setSelected(true);
                    isMenu();
                    getMasalah();
                } catch (java.lang.NullPointerException e) {
                }
            }else if(evt.getKeyCode()==KeyEvent.VK_SPACE){
                try {
                    getData();
                    TabRawat.setSelectedIndex(0);
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbObatKeyPressed

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

    private void ChkAccorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkAccorActionPerformed
        if(tbObat.getSelectedRow()!= -1){
            isMenu();
        }else{
            ChkAccor.setSelected(false);
            JOptionPane.showMessageDialog(null,"Maaf, silahkan pilih data yang mau ditampilkan...!!!!");
        }
    }//GEN-LAST:event_ChkAccorActionPerformed

    private void DetailRencanaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DetailRencanaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DetailRencanaKeyPressed

    private void KeluhanUtamaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeluhanUtamaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KeluhanUtamaKeyPressed

    private void RiwPenyakitSkrgKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RiwPenyakitSkrgKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_RiwPenyakitSkrgKeyPressed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jCheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox5ActionPerformed

    private void jCheckBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox6ActionPerformed

    private void jCheckBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox7ActionPerformed

    private void jCheckBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox8ActionPerformed

    private void jCheckBox10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox10ActionPerformed

    private void jCheckBox11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox11ActionPerformed

    private void jCheckBox12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox12ActionPerformed

    private void LokalisasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LokalisasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_LokalisasiKeyPressed

    private void DiagnosaKerjaNeonatologiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosaKerjaNeonatologiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DiagnosaKerjaNeonatologiKeyPressed

    private void RencanaKerjaNeonatologiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RencanaKerjaNeonatologiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_RencanaKerjaNeonatologiKeyPressed

    private void TerapiNeonatologiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TerapiNeonatologiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TerapiNeonatologiKeyPressed

    private void jCheckBox14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox14ActionPerformed

    private void jCheckBox15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox15ActionPerformed

    private void jCheckBox16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox16ActionPerformed

    private void jCheckBox24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox24ActionPerformed

    private void jCheckBox25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox25ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox25ActionPerformed

    private void jCheckBox26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox26ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox26ActionPerformed

    private void jCheckBox27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox27ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox27ActionPerformed

    private void Jk7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Jk7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Jk7ActionPerformed

    private void jCheckBox78ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox78ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox78ActionPerformed

    private void jCheckBox79ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox79ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox79ActionPerformed

    private void jCheckBox80ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox80ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox80ActionPerformed

    private void jCheckBox81ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox81ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox81ActionPerformed

    private void jCheckBox82ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox82ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox82ActionPerformed

    private void jCheckBox90ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox90ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox90ActionPerformed

    private void jCheckBox91ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox91ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox91ActionPerformed

    private void jCheckBox92ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox92ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox92ActionPerformed

    private void jCheckBox93ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox93ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox93ActionPerformed

    private void jCheckBox94ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox94ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox94ActionPerformed

    private void jCheckBox95ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox95ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox95ActionPerformed

    private void jCheckBox75ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox75ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox75ActionPerformed

    private void jCheckBox76ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox76ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox76ActionPerformed

    private void jCheckBox77ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox77ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox77ActionPerformed

    private void Jk43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Jk43ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Jk43ActionPerformed

    private void Lokalisasi1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Lokalisasi1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Lokalisasi1KeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMAsesmenAwalMedisAnak dialog = new RMAsesmenAwalMedisAnak(new javax.swing.JFrame(), true);
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
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.CekBox ChkAccor;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.TextArea DetailRencana;
    private widget.TextArea DiagnosaKerjaNeonatologi;
    private widget.PanelBiasa FormInput;
    private widget.PanelBiasa FormMasalahRencana;
    private widget.PanelBiasa FormMenu;
    private widget.TextArea HasilPenunjangNeonatologi;
    private widget.TextBox JamNeonatologi;
    private widget.TextBox Jk1;
    private widget.TextBox Jk10;
    private widget.TextBox Jk15;
    private widget.TextBox Jk2;
    private widget.TextBox Jk3;
    private widget.TextBox Jk34;
    private widget.TextBox Jk35;
    private widget.TextBox Jk36;
    private widget.TextBox Jk38;
    private widget.TextBox Jk39;
    private widget.TextBox Jk40;
    private widget.TextBox Jk41;
    private widget.TextBox Jk42;
    private widget.TextBox Jk43;
    private widget.TextBox Jk44;
    private widget.TextBox Jk45;
    private widget.TextBox Jk46;
    private widget.TextBox Jk6;
    private widget.TextBox Jk7;
    private widget.TextBox Jk8;
    private widget.TextBox Jk9;
    private widget.TextBox KdPetugas;
    private widget.TextArea KeluhanUtama;
    private widget.Label LCount;
    private widget.editorpane LoadHTML;
    private widget.TextArea Lokalisasi;
    private widget.TextArea Lokalisasi1;
    private widget.TextBox NmPetugas;
    private widget.PanelBiasa PanelAccor;
    private widget.TextBox Rambut;
    private widget.TextArea RencanaKerjaNeonatologi;
    private widget.TextArea RiwPenyakitSkrg;
    private widget.TextBox RuangNeonatologi;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRM1;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.TextBox TPasien1;
    private javax.swing.JTabbedPane TabRawat;
    private widget.TextBox TempatNeonatologi;
    private widget.TextArea TerapiNeonatologi;
    private widget.Tanggal TglAsuhan;
    private widget.TextBox TglLahir;
    private widget.TextBox TglNeonatologi;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.InternalFrame internalFrame3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox10;
    private javax.swing.JCheckBox jCheckBox11;
    private javax.swing.JCheckBox jCheckBox12;
    private javax.swing.JCheckBox jCheckBox14;
    private javax.swing.JCheckBox jCheckBox15;
    private javax.swing.JCheckBox jCheckBox16;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox24;
    private javax.swing.JCheckBox jCheckBox25;
    private javax.swing.JCheckBox jCheckBox26;
    private javax.swing.JCheckBox jCheckBox27;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox75;
    private javax.swing.JCheckBox jCheckBox76;
    private javax.swing.JCheckBox jCheckBox77;
    private javax.swing.JCheckBox jCheckBox78;
    private javax.swing.JCheckBox jCheckBox79;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox80;
    private javax.swing.JCheckBox jCheckBox81;
    private javax.swing.JCheckBox jCheckBox82;
    private javax.swing.JCheckBox jCheckBox90;
    private javax.swing.JCheckBox jCheckBox91;
    private javax.swing.JCheckBox jCheckBox92;
    private javax.swing.JCheckBox jCheckBox93;
    private javax.swing.JCheckBox jCheckBox94;
    private javax.swing.JCheckBox jCheckBox95;
    private widget.Label jLabel10;
    private widget.Label jLabel11;
    private widget.Label jLabel12;
    private widget.Label jLabel19;
    private widget.Label jLabel21;
    private widget.Label jLabel31;
    private widget.Label jLabel32;
    private widget.Label jLabel33;
    private widget.Label jLabel34;
    private widget.Label jLabel35;
    private widget.Label jLabel36;
    private widget.Label jLabel41;
    private widget.Label jLabel42;
    private widget.Label jLabel43;
    private widget.Label jLabel44;
    private widget.Label jLabel45;
    private widget.Label jLabel46;
    private widget.Label jLabel47;
    private widget.Label jLabel48;
    private widget.Label jLabel49;
    private widget.Label jLabel50;
    private widget.Label jLabel51;
    private widget.Label jLabel52;
    private widget.Label jLabel53;
    private widget.Label jLabel58;
    private widget.Label jLabel6;
    private widget.Label jLabel67;
    private widget.Label jLabel7;
    private widget.Label jLabel77;
    private widget.Label jLabel78;
    private widget.Label jLabel79;
    private widget.Label jLabel8;
    private widget.Label jLabel80;
    private widget.Label jLabel81;
    private widget.Label jLabel82;
    private widget.Label jLabel83;
    private widget.Label jLabel84;
    private widget.Label jLabel85;
    private widget.Label jLabel86;
    private widget.Label jLabel87;
    private widget.Label jLabel88;
    private widget.Label jLabel89;
    private widget.Label jLabel90;
    private widget.Label jLabel91;
    private widget.Label jLabel92;
    private widget.Label jLabel93;
    private widget.Label jLabel94;
    private widget.Label jLabel95;
    private widget.Label jLabel98;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private widget.Label label11;
    private widget.Label label14;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.ScrollPane scrollInput;
    private widget.ScrollPane scrollPane10;
    private widget.ScrollPane scrollPane2;
    private widget.ScrollPane scrollPane3;
    private widget.ScrollPane scrollPane4;
    private widget.ScrollPane scrollPane5;
    private widget.ScrollPane scrollPane6;
    private widget.ScrollPane scrollPane7;
    private widget.ScrollPane scrollPane8;
    private widget.ScrollPane scrollPane9;
    private widget.Table tbObat;
    // End of variables declaration//GEN-END:variables

    private void tampil() {
        Valid.tabelKosong(tabMode);
        try{
            if(TCari.getText().equals("")){
                ps=koneksi.prepareStatement(
                        "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,if(pasien.jk='L','Laki-Laki','Perempuan') as jk,pasien.tgl_lahir,pasien.agama,bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,penilaian_usg_urologi.tanggal,"+
                        "penilaian_usg_urologi.ginjal_kanan as ginjalkanan,penilaian_usg_urologi.ginjal_kiri as ginjalkiri,penilaian_usg_urologi.buli as sbuli,"+
                        "pasien.stts_nikah,"+ 
                        "pasien.pekerjaan,penjab.png_jawab,pasien.pnd,"+  
                        "penilaian_usg_urologi.kd_dokter,dokter.nm_dokter "+
                        "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                        "inner join penilaian_usg_urologi on reg_periksa.no_rawat=penilaian_usg_urologi.no_rawat "+
                        "inner join dokter on penilaian_usg_urologi.kd_dokter=dokter.kd_dokter "+
                        "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "+
                        "inner join penjab on penjab.kd_pj=reg_periksa.kd_pj "+
                        "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik where "+
                        "penilaian_usg_urologi.tanggal between ? and ? "
                                + "and pasien.no_rkm_medis=?"
                                + "order by penilaian_usg_urologi.tanggal");
            }else{
                ps=koneksi.prepareStatement(
                        "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,if(pasien.jk='L','Laki-Laki','Perempuan') as jk,pasien.tgl_lahir,pasien.agama,bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,penilaian_usg_urologi.tanggal,"+
                        "penilaian_usg_urologi.ginjal_kanan as ginjalkanan,penilaian_usg_urologi.ginjal_kiri as ginjalkiri,penilaian_usg_urologi.buli as sbuli,"+
                        "pasien.stts_nikah,"+ 
                        "pasien.pekerjaan,penjab.png_jawab,pasien.pnd,"+  
                        "penilaian_usg_urologi.kd_dokter,dokter.nm_dokter "+
                        "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                        "inner join penilaian_usg_urologi on reg_periksa.no_rawat=penilaian_usg_urologi.no_rawat "+
                        "inner join dokter on penilaian_usg_urologi.kd_dokter=dokter.kd_dokter "+
                        "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "+
                        "inner join penjab on penjab.kd_pj=reg_periksa.kd_pj "+
                        "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik where "+
                        "penilaian_usg_urologi.tanggal between ? and ? and "
                                + "pasien.no_rkm_medis=? and " +
                        "(reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or "+
                        "penilaian_usg_urologi.kd_dokter like ? or dokter.nm_dokter like ?) "+
                        "order by penilaian_usg_urologi.tanggal");
            }
                
            try {
                if(TCari.getText().equals("")){
                    ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                    ps.setString(3,TNoRM.getText());
                }else{
                    ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                    ps.setString(3,TNoRM.getText());
                    ps.setString(4,"%"+TCari.getText()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                }   
                rs=ps.executeQuery();
                while(rs.next()){
                    
                    tabMode.addRow(new String[]{
                        rs.getString("no_rawat"),rs.getString("no_rkm_medis"),
                        rs.getString("nm_pasien"),rs.getString("jk"),
                        rs.getString("tgl_lahir"),rs.getString("tanggal"),
                        rs.getString("ginjalkanan"),
                        rs.getString("ginjalkiri"),rs.getString("sbuli") 
                            ,rs.getString("kd_dokter"),rs.getString("nm_dokter")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
                e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    public void emptTeks() {
        TglAsuhan.setDate(new Date());
        Eflorisensi.setText("");
        HasilPenunjangNeonatologi.setText("");
        
        for (i = 0; i < tabModeMasalah.getRowCount(); i++) {
            tabModeMasalah.setValueAt(false,i,0);
        }
        TabRawat.setSelectedIndex(0);
    }

    private void getData() {
        if(tbObat.getSelectedRow()!= -1){
            no_rawat = tbObat.getValueAt(tbObat.getSelectedRow(),0).toString(); 
            no_rm = tbObat.getValueAt(tbObat.getSelectedRow(),1).toString();
            pasien = tbObat.getValueAt(tbObat.getSelectedRow(),2).toString(); 
            jk = tbObat.getValueAt(tbObat.getSelectedRow(),3).toString(); 
            tgllahir = tbObat.getValueAt(tbObat.getSelectedRow(),4).toString(); 
            ginjal_kanan = tbObat.getValueAt(tbObat.getSelectedRow(),6).toString();
            ginjal_kiri = tbObat.getValueAt(tbObat.getSelectedRow(),7).toString();
            sbuli = tbObat.getValueAt(tbObat.getSelectedRow(),8).toString();
            kd_petugas = tbObat.getValueAt(tbObat.getSelectedRow(),9).toString();
            nm_petugas = tbObat.getValueAt(tbObat.getSelectedRow(),10).toString();
            
            
            TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()); 
            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString()); 
            KelenjarLimfe.setText(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString()); 
            TglLahir.setText(tbObat.getValueAt(tbObat.getSelectedRow(),4).toString()); 
            Eflorisensi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString());
            HasilPenunjangNeonatologi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString());
            
            KdPetugas.setText(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString());
            NmPetugas.setText(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString());
            Valid.SetTgl2(TglAsuhan,tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
            
            DetailRencana.setText("Ginjal Kanan: \n"+ginjal_kanan + "\n\nGinjal Kiri: \n"+ginjal_kiri+"\n\nBuli: \n" +sbuli);
        }
    }

    private void isRawat() {
        try {
            ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rkm_medis,pasien.nm_pasien, if(pasien.jk='L','Laki-Laki','Perempuan') as jk,"+
                    "pasien.tgl_lahir,pasien.agama,bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,reg_periksa.tgl_registrasi, "+
                    "pasien.stts_nikah,pasien.pekerjaan,pasien.pnd,penjab.png_jawab "+
                    "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "+
                    "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik "+
                    "inner join penjab on penjab.kd_pj=reg_periksa.kd_pj "+
                    "where reg_periksa.no_rawat=?");
            try {
                ps.setString(1,TNoRw.getText());
                rs=ps.executeQuery();
                if(rs.next()){
                    TNoRM.setText(rs.getString("no_rkm_medis"));
                    TPasien.setText(rs.getString("nm_pasien"));
                    KelenjarLimfe.setText(rs.getString("jk"));
                    TglLahir.setText(rs.getString("tgl_lahir"));
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
    }
    
    
    public void isCek(){
        BtnSimpan.setEnabled(akses.getpenilaian_awal_keperawatan_igd());
        BtnHapus.setEnabled(akses.getpenilaian_awal_keperawatan_igd());
        BtnEdit.setEnabled(akses.getpenilaian_awal_keperawatan_igd());
        BtnEdit.setEnabled(akses.getpenilaian_awal_keperawatan_igd());
        if(akses.getjml2()>=1){
            KdPetugas.setEditable(false);
            BtnDokter.setEnabled(false);
            KdPetugas.setText(akses.getkode());
            Sequel.cariIsi("select dokter.nm_dokter from dokter where dokter.kd_dokter=?", NmPetugas,KdPetugas.getText());
            if(NmPetugas.getText().equals("")){
                KdPetugas.setText("");
                JOptionPane.showMessageDialog(null,"User login bukan petugas...!!");
            }
        }             
    }

    public void setTampil(){
       TabRawat.setSelectedIndex(1);
       tampil();
    }
    
    private void isMenu(){
        if(ChkAccor.isSelected()==true){
            ChkAccor.setVisible(false);
            PanelAccor.setPreferredSize(new Dimension(470,HEIGHT));
            FormMenu.setVisible(true);  
            FormMasalahRencana.setVisible(true);  
            ChkAccor.setVisible(true);
        }else if(ChkAccor.isSelected()==false){   
            ChkAccor.setVisible(false);
            PanelAccor.setPreferredSize(new Dimension(15,HEIGHT));
            FormMenu.setVisible(false);  
            FormMasalahRencana.setVisible(false);   
            ChkAccor.setVisible(true);
        }
    }

    private void getMasalah() {
        if(tbObat.getSelectedRow()!= -1){
            TNoRM1.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
            TPasien1.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString()); 
            
        }
    }
    
}
