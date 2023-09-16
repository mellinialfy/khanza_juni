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
public final class RMAsesmenAwalMedisKulitKelamin extends javax.swing.JDialog {
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
    public RMAsesmenAwalMedisKulitKelamin(java.awt.Frame parent, boolean modal) {
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
        KelenjarLimfe = new widget.TextBox();
        jLabel10 = new widget.Label();
        label11 = new widget.Label();
        jLabel11 = new widget.Label();
        jLabel30 = new widget.Label();
        scrollPane1 = new widget.ScrollPane();
        Eflorisensi = new widget.TextArea();
        scrollPane2 = new widget.ScrollPane();
        HasilPenunjangNeonatologi = new widget.TextArea();
        jLabel31 = new widget.Label();
        TglAsuhan = new widget.Tanggal();
        jLabel98 = new widget.Label();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        jLabel99 = new widget.Label();
        jLabel32 = new widget.Label();
        jSeparator12 = new javax.swing.JSeparator();
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
        jLabel37 = new widget.Label();
        Kuku = new widget.TextBox();
        jLabel38 = new widget.Label();
        Rambut = new widget.TextBox();
        jLabel39 = new widget.Label();
        jCheckBox6 = new javax.swing.JCheckBox();
        jCheckBox7 = new javax.swing.JCheckBox();
        jLabel40 = new widget.Label();
        Mukosa = new widget.TextBox();
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
        jLabel100 = new widget.Label();
        jSeparator14 = new javax.swing.JSeparator();
        jLabel52 = new widget.Label();
        jCheckBox16 = new javax.swing.JCheckBox();
        jCheckBox9 = new javax.swing.JCheckBox();
        jCheckBox18 = new javax.swing.JCheckBox();
        jCheckBox19 = new javax.swing.JCheckBox();
        jCheckBox20 = new javax.swing.JCheckBox();
        Jk11 = new widget.TextBox();
        jCheckBox13 = new javax.swing.JCheckBox();
        jCheckBox21 = new javax.swing.JCheckBox();
        jCheckBox22 = new javax.swing.JCheckBox();
        jCheckBox23 = new javax.swing.JCheckBox();
        jLabel53 = new widget.Label();
        jCheckBox24 = new javax.swing.JCheckBox();
        jCheckBox25 = new javax.swing.JCheckBox();
        jCheckBox26 = new javax.swing.JCheckBox();
        jCheckBox27 = new javax.swing.JCheckBox();
        Jk12 = new widget.TextBox();
        jLabel54 = new widget.Label();
        jCheckBox28 = new javax.swing.JCheckBox();
        jCheckBox29 = new javax.swing.JCheckBox();
        jCheckBox30 = new javax.swing.JCheckBox();
        jCheckBox31 = new javax.swing.JCheckBox();
        Jk13 = new widget.TextBox();
        jCheckBox32 = new javax.swing.JCheckBox();
        jLabel55 = new widget.Label();
        jCheckBox33 = new javax.swing.JCheckBox();
        jCheckBox35 = new javax.swing.JCheckBox();
        jCheckBox36 = new javax.swing.JCheckBox();
        jCheckBox37 = new javax.swing.JCheckBox();
        Jk14 = new widget.TextBox();
        jLabel56 = new widget.Label();
        jCheckBox38 = new javax.swing.JCheckBox();
        jCheckBox39 = new javax.swing.JCheckBox();
        jCheckBox40 = new javax.swing.JCheckBox();
        jCheckBox41 = new javax.swing.JCheckBox();
        WarnaMukosa = new widget.TextBox();
        jLabel57 = new widget.Label();
        Jk16 = new widget.TextBox();
        jCheckBox42 = new javax.swing.JCheckBox();
        jLabel58 = new widget.Label();
        jCheckBox43 = new javax.swing.JCheckBox();
        jCheckBox44 = new javax.swing.JCheckBox();
        jCheckBox45 = new javax.swing.JCheckBox();
        Jk17 = new widget.TextBox();
        jLabel59 = new widget.Label();
        jCheckBox46 = new javax.swing.JCheckBox();
        jCheckBox47 = new javax.swing.JCheckBox();
        jCheckBox48 = new javax.swing.JCheckBox();
        jCheckBox49 = new javax.swing.JCheckBox();
        Jk18 = new widget.TextBox();
        jLabel60 = new widget.Label();
        jCheckBox50 = new javax.swing.JCheckBox();
        jCheckBox51 = new javax.swing.JCheckBox();
        jCheckBox52 = new javax.swing.JCheckBox();
        Jk19 = new widget.TextBox();
        jLabel61 = new widget.Label();
        jCheckBox53 = new javax.swing.JCheckBox();
        jCheckBox54 = new javax.swing.JCheckBox();
        jCheckBox55 = new javax.swing.JCheckBox();
        Jk20 = new widget.TextBox();
        jLabel62 = new widget.Label();
        jCheckBox56 = new javax.swing.JCheckBox();
        jCheckBox57 = new javax.swing.JCheckBox();
        Jk21 = new widget.TextBox();
        jLabel63 = new widget.Label();
        jCheckBox58 = new javax.swing.JCheckBox();
        jCheckBox59 = new javax.swing.JCheckBox();
        jLabel64 = new widget.Label();
        jCheckBox60 = new javax.swing.JCheckBox();
        jCheckBox61 = new javax.swing.JCheckBox();
        jCheckBox62 = new javax.swing.JCheckBox();
        Jk22 = new widget.TextBox();
        jLabel65 = new widget.Label();
        jCheckBox34 = new javax.swing.JCheckBox();
        jCheckBox63 = new javax.swing.JCheckBox();
        jCheckBox64 = new javax.swing.JCheckBox();
        jCheckBox65 = new javax.swing.JCheckBox();
        jCheckBox66 = new javax.swing.JCheckBox();
        Jk23 = new widget.TextBox();
        Jk24 = new widget.TextBox();
        jLabel66 = new widget.Label();
        jCheckBox17 = new javax.swing.JCheckBox();
        jCheckBox67 = new javax.swing.JCheckBox();
        jCheckBox68 = new javax.swing.JCheckBox();
        jCheckBox69 = new javax.swing.JCheckBox();
        Jk25 = new widget.TextBox();
        Jk26 = new widget.TextBox();
        Jk27 = new widget.TextBox();
        Jk28 = new widget.TextBox();
        jCheckBox70 = new javax.swing.JCheckBox();
        Jk29 = new widget.TextBox();
        jCheckBox71 = new javax.swing.JCheckBox();
        Jk30 = new widget.TextBox();
        jCheckBox72 = new javax.swing.JCheckBox();
        Jk31 = new widget.TextBox();
        jCheckBox73 = new javax.swing.JCheckBox();
        Jk32 = new widget.TextBox();
        jCheckBox74 = new javax.swing.JCheckBox();
        Jk33 = new widget.TextBox();
        jLabel67 = new widget.Label();
        scrollPane10 = new widget.ScrollPane();
        HasilPenunjangDerma = new widget.TextArea();
        jLabel68 = new widget.Label();
        scrollPane11 = new widget.ScrollPane();
        DiagnosaKerjaDerma = new widget.TextArea();
        scrollPane12 = new widget.ScrollPane();
        TerapiDerma = new widget.TextArea();
        jLabel69 = new widget.Label();
        scrollPane13 = new widget.ScrollPane();
        RencanaKerjaDerma = new widget.TextArea();
        jLabel70 = new widget.Label();
        jLabel71 = new widget.Label();
        jCheckBox75 = new javax.swing.JCheckBox();
        jCheckBox76 = new javax.swing.JCheckBox();
        jCheckBox77 = new javax.swing.JCheckBox();
        jLabel72 = new widget.Label();
        jLabel73 = new widget.Label();
        jLabel74 = new widget.Label();
        JamKeluarDerma = new widget.TextBox();
        TglDerma = new widget.TextBox();
        RuangDerma = new widget.TextBox();
        jLabel75 = new widget.Label();
        jLabel76 = new widget.Label();
        TempatDerma = new widget.TextBox();
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

        KelenjarLimfe.setHighlighter(null);
        KelenjarLimfe.setName("KelenjarLimfe"); // NOI18N
        FormInput.add(KelenjarLimfe);
        KelenjarLimfe.setBounds(170, 340, 150, 23);

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

        jLabel30.setText("Bentuk Kelainan Kulit (Eflorisensi):");
        jLabel30.setName("jLabel30"); // NOI18N
        FormInput.add(jLabel30);
        jLabel30.setBounds(430, 190, 175, 20);

        scrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane1.setColumnHeaderView(null);
        scrollPane1.setName("scrollPane1"); // NOI18N

        Eflorisensi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Eflorisensi.setColumns(30);
        Eflorisensi.setRows(5);
        Eflorisensi.setName("Eflorisensi"); // NOI18N
        Eflorisensi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EflorisensiKeyPressed(evt);
            }
        });
        scrollPane1.setViewportView(Eflorisensi);

        FormInput.add(scrollPane1);
        scrollPane1.setBounds(610, 190, 260, 53);

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
        scrollPane2.setBounds(170, 1180, 260, 53);
        scrollPane2.getAccessibleContext().setAccessibleName("");
        scrollPane2.getAccessibleContext().setAccessibleDescription("");

        jLabel31.setText("Syaraf:");
        jLabel31.setToolTipText("");
        jLabel31.setName("jLabel31"); // NOI18N
        FormInput.add(jLabel31);
        jLabel31.setBounds(0, 370, 150, 23);

        TglAsuhan.setForeground(new java.awt.Color(50, 70, 50));
        TglAsuhan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "15-09-2023 15:02:58" }));
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

        jSeparator11.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator11.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator11.setName("jSeparator11"); // NOI18N
        FormInput.add(jSeparator11);
        jSeparator11.setBounds(0, 160, 880, 1);

        jLabel99.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel99.setText("II. STATUS DERMATOLOGI");
        jLabel99.setToolTipText("");
        jLabel99.setName("jLabel99"); // NOI18N
        FormInput.add(jLabel99);
        jLabel99.setBounds(10, 160, 180, 23);

        jLabel32.setText("Keluhan Utama:");
        jLabel32.setToolTipText("");
        jLabel32.setName("jLabel32"); // NOI18N
        FormInput.add(jLabel32);
        jLabel32.setBounds(0, 90, 150, 23);

        jSeparator12.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator12.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator12.setName("jSeparator12"); // NOI18N
        FormInput.add(jSeparator12);
        jSeparator12.setBounds(0, 185, 880, 1);

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

        jLabel35.setText("Riwayat Penyakit Sekarang:");
        jLabel35.setName("jLabel35"); // NOI18N
        FormInput.add(jLabel35);
        jLabel35.setBounds(430, 90, 175, 20);

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
        jLabel33.setBounds(0, 1180, 150, 23);

        jCheckBox1.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox1.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox1.setText("Lainnya");
        jCheckBox1.setName("Pit. Alba"); // NOI18N
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox1);
        jCheckBox1.setBounds(640, 250, 70, 19);

        jCheckBox2.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox2.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox2.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox2.setText("Dengie-Morgagni");
        jCheckBox2.setName("jCheckBox2"); // NOI18N
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox2);
        jCheckBox2.setBounds(530, 250, 110, 19);

        jCheckBox3.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox3.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox3.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox3.setText("Ikhtiosis");
        jCheckBox3.setName("jCheckBox3"); // NOI18N
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox3);
        jCheckBox3.setBounds(330, 250, 80, 19);

        jCheckBox4.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox4.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox4.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox4.setText("Pit. Alba");
        jCheckBox4.setName("jCheckBox4"); // NOI18N
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox4);
        jCheckBox4.setBounds(170, 250, 80, 19);

        jCheckBox5.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox5.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox5.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox5.setText("Keratosis");
        jCheckBox5.setName("jCheckBox5"); // NOI18N
        jCheckBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox5ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox5);
        jCheckBox5.setBounds(430, 250, 80, 19);

        Jk1.setEditable(false);
        Jk1.setHighlighter(null);
        Jk1.setName("Jk1"); // NOI18N
        FormInput.add(Jk1);
        Jk1.setBounds(774, 10, 80, 23);

        jLabel36.setText("Stigma Atopik:");
        jLabel36.setToolTipText("");
        jLabel36.setName("jLabel36"); // NOI18N
        FormInput.add(jLabel36);
        jLabel36.setBounds(0, 250, 150, 23);

        Jk2.setHighlighter(null);
        Jk2.setName("Jk2"); // NOI18N
        FormInput.add(Jk2);
        Jk2.setBounds(720, 250, 150, 23);

        jLabel37.setText("Kuku:");
        jLabel37.setToolTipText("");
        jLabel37.setName("jLabel37"); // NOI18N
        FormInput.add(jLabel37);
        jLabel37.setBounds(660, 280, 40, 23);

        Kuku.setHighlighter(null);
        Kuku.setName("Kuku"); // NOI18N
        FormInput.add(Kuku);
        Kuku.setBounds(720, 280, 150, 23);

        jLabel38.setText("Rambut:");
        jLabel38.setToolTipText("");
        jLabel38.setName("jLabel38"); // NOI18N
        FormInput.add(jLabel38);
        jLabel38.setBounds(430, 280, 60, 23);

        Rambut.setHighlighter(null);
        Rambut.setName("Rambut"); // NOI18N
        FormInput.add(Rambut);
        Rambut.setBounds(510, 280, 150, 23);

        jLabel39.setText("Kelenjar limfe:");
        jLabel39.setToolTipText("");
        jLabel39.setName("jLabel39"); // NOI18N
        FormInput.add(jLabel39);
        jLabel39.setBounds(0, 340, 150, 23);

        jCheckBox6.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox6.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox6.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox6.setText("Hiperhidrosis");
        jCheckBox6.setName("jCheckBox6"); // NOI18N
        jCheckBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox6ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox6);
        jCheckBox6.setBounds(170, 310, 100, 19);

        jCheckBox7.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox7.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox7.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox7.setText("Anshidrosis");
        jCheckBox7.setName("jCheckBox7"); // NOI18N
        jCheckBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox7ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox7);
        jCheckBox7.setBounds(330, 310, 90, 19);

        jLabel40.setText("Mukosa:");
        jLabel40.setToolTipText("");
        jLabel40.setName("jLabel40"); // NOI18N
        FormInput.add(jLabel40);
        jLabel40.setBounds(0, 280, 150, 23);

        Mukosa.setHighlighter(null);
        Mukosa.setName("Mukosa"); // NOI18N
        FormInput.add(Mukosa);
        Mukosa.setBounds(170, 280, 150, 23);

        jLabel41.setText("Fungsi kelenjar keringat:");
        jLabel41.setToolTipText("");
        jLabel41.setName("jLabel41"); // NOI18N
        FormInput.add(jLabel41);
        jLabel41.setBounds(0, 310, 150, 23);

        jCheckBox8.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox8.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox8.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox8.setText("Penebalan saraf perifer");
        jCheckBox8.setName("jCheckBox8"); // NOI18N
        jCheckBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox8ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox8);
        jCheckBox8.setBounds(170, 370, 150, 19);

        jCheckBox10.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox10.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox10.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox10.setText("Parestesi");
        jCheckBox10.setName("jCheckBox10"); // NOI18N
        jCheckBox10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox10ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox10);
        jCheckBox10.setBounds(330, 370, 80, 19);

        jCheckBox11.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox11.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox11.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox11.setText("Makula-Anestesi");
        jCheckBox11.setName("jCheckBox11"); // NOI18N
        jCheckBox11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox11ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox11);
        jCheckBox11.setBounds(430, 370, 110, 19);

        jCheckBox12.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox12.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox12.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox12.setText("Lainnya");
        jCheckBox12.setName("jCheckBox12"); // NOI18N
        jCheckBox12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox12ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox12);
        jCheckBox12.setBounds(640, 370, 70, 19);

        JamNeonatologi.setHighlighter(null);
        JamNeonatologi.setName("JamNeonatologi"); // NOI18N
        FormInput.add(JamNeonatologi);
        JamNeonatologi.setBounds(370, 1300, 64, 23);

        jLabel42.setText("Lokalisasi:");
        jLabel42.setToolTipText("");
        jLabel42.setName("jLabel42"); // NOI18N
        FormInput.add(jLabel42);
        jLabel42.setBounds(0, 190, 150, 23);

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
        scrollPane3.setBounds(170, 190, 260, 53);

        jLabel43.setText("Diagnosa Kerja/Diagnosa Banding:");
        jLabel43.setToolTipText("");
        jLabel43.setName("jLabel43"); // NOI18N
        FormInput.add(jLabel43);
        jLabel43.setBounds(430, 1180, 170, 23);

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
        scrollPane7.setBounds(610, 1180, 260, 53);

        jLabel44.setText("Rencana Kerja:");
        jLabel44.setToolTipText("");
        jLabel44.setName("jLabel44"); // NOI18N
        FormInput.add(jLabel44);
        jLabel44.setBounds(0, 1240, 150, 23);

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
        scrollPane8.setBounds(170, 1240, 260, 53);

        jLabel45.setText("Terapi/Tindakan:");
        jLabel45.setToolTipText("");
        jLabel45.setName("jLabel45"); // NOI18N
        FormInput.add(jLabel45);
        jLabel45.setBounds(430, 1240, 170, 23);

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
        scrollPane9.setBounds(610, 1240, 260, 53);

        jLabel46.setText("Di ruang:");
        jLabel46.setToolTipText("");
        jLabel46.setName("jLabel46"); // NOI18N
        FormInput.add(jLabel46);
        jLabel46.setBounds(260, 1360, 100, 23);

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
        jCheckBox14.setBounds(170, 1300, 130, 19);

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
        jCheckBox15.setBounds(170, 1330, 130, 19);

        jLabel47.setText("Kepala:");
        jLabel47.setToolTipText("");
        jLabel47.setName("jLabel47"); // NOI18N
        FormInput.add(jLabel47);
        jLabel47.setBounds(0, 640, 150, 23);

        jLabel48.setText("WITA");
        jLabel48.setToolTipText("");
        jLabel48.setName("jLabel48"); // NOI18N
        FormInput.add(jLabel48);
        jLabel48.setBounds(440, 1300, 40, 23);

        jLabel49.setText("Tempat:");
        jLabel49.setToolTipText("");
        jLabel49.setName("jLabel49"); // NOI18N
        FormInput.add(jLabel49);
        jLabel49.setBounds(440, 1330, 40, 23);

        Jk7.setHighlighter(null);
        Jk7.setName("Jk7"); // NOI18N
        FormInput.add(Jk7);
        Jk7.setBounds(720, 370, 150, 23);

        RuangNeonatologi.setHighlighter(null);
        RuangNeonatologi.setName("RuangNeonatologi"); // NOI18N
        FormInput.add(RuangNeonatologi);
        RuangNeonatologi.setBounds(370, 1360, 64, 23);

        TempatNeonatologi.setHighlighter(null);
        TempatNeonatologi.setName("TempatNeonatologi"); // NOI18N
        FormInput.add(TempatNeonatologi);
        TempatNeonatologi.setBounds(490, 1330, 64, 23);

        jLabel50.setText("Jam keluar:");
        jLabel50.setToolTipText("");
        jLabel50.setName("jLabel50"); // NOI18N
        FormInput.add(jLabel50);
        jLabel50.setBounds(260, 1300, 100, 23);

        jLabel51.setText("Tanggal:");
        jLabel51.setToolTipText("");
        jLabel51.setName("jLabel51"); // NOI18N
        FormInput.add(jLabel51);
        jLabel51.setBounds(260, 1330, 100, 23);

        TglNeonatologi.setHighlighter(null);
        TglNeonatologi.setName("TglNeonatologi"); // NOI18N
        FormInput.add(TglNeonatologi);
        TglNeonatologi.setBounds(370, 1330, 64, 23);

        jSeparator13.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator13.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator13.setName("jSeparator13"); // NOI18N
        FormInput.add(jSeparator13);
        jSeparator13.setBounds(0, 610, 880, 1);

        jLabel100.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel100.setText("III. STATUS NEONATOLOGI");
        jLabel100.setToolTipText("");
        jLabel100.setName("jLabel100"); // NOI18N
        FormInput.add(jLabel100);
        jLabel100.setBounds(10, 610, 180, 23);

        jSeparator14.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator14.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator14.setName("jSeparator14"); // NOI18N
        FormInput.add(jSeparator14);
        jSeparator14.setBounds(0, 635, 880, 1);

        jLabel52.setText("Disposisi:");
        jLabel52.setToolTipText("");
        jLabel52.setName("jLabel52"); // NOI18N
        FormInput.add(jLabel52);
        jLabel52.setBounds(0, 1300, 150, 23);

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
        jCheckBox16.setBounds(170, 1360, 130, 19);

        jCheckBox9.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox9.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox9.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox9.setText("Simetri");
        jCheckBox9.setName("jCheckBox9"); // NOI18N
        jCheckBox9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox9ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox9);
        jCheckBox9.setBounds(170, 640, 80, 19);

        jCheckBox18.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox18.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox18.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox18.setText("Microcephal");
        jCheckBox18.setName("jCheckBox18"); // NOI18N
        jCheckBox18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox18ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox18);
        jCheckBox18.setBounds(330, 640, 100, 19);

        jCheckBox19.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox19.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox19.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox19.setText("Cephal Hematoma");
        jCheckBox19.setName("jCheckBox19"); // NOI18N
        jCheckBox19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox19ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox19);
        jCheckBox19.setBounds(430, 640, 140, 19);

        jCheckBox20.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox20.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox20.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox20.setText("Caput Succedasinium");
        jCheckBox20.setName("jCheckBox20"); // NOI18N
        jCheckBox20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox20ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox20);
        jCheckBox20.setBounds(640, 640, 130, 19);

        Jk11.setHighlighter(null);
        Jk11.setName("Jk11"); // NOI18N
        FormInput.add(Jk11);
        Jk11.setBounds(720, 700, 150, 23);

        jCheckBox13.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox13.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox13.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox13.setText("Asimetris");
        jCheckBox13.setName("jCheckBox13"); // NOI18N
        jCheckBox13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox13ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox13);
        jCheckBox13.setBounds(170, 670, 120, 19);

        jCheckBox21.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox21.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox21.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox21.setText("Hydrocephalus");
        jCheckBox21.setName("jCheckBox21"); // NOI18N
        jCheckBox21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox21ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox21);
        jCheckBox21.setBounds(330, 670, 100, 19);

        jCheckBox22.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox22.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox22.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox22.setText("Anecephali");
        jCheckBox22.setName("jCheckBox22"); // NOI18N
        jCheckBox22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox22ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox22);
        jCheckBox22.setBounds(430, 670, 120, 19);

        jCheckBox23.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox23.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox23.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox23.setText("Lainnya");
        jCheckBox23.setName("jCheckBox23"); // NOI18N
        jCheckBox23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox23ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox23);
        jCheckBox23.setBounds(640, 670, 70, 19);

        jLabel53.setText("UUB:");
        jLabel53.setToolTipText("");
        jLabel53.setName("jLabel53"); // NOI18N
        FormInput.add(jLabel53);
        jLabel53.setBounds(0, 700, 150, 23);

        jCheckBox24.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox24.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox24.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox24.setText("Datar");
        jCheckBox24.setName("jCheckBox24"); // NOI18N
        jCheckBox24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox24ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox24);
        jCheckBox24.setBounds(170, 700, 70, 19);

        jCheckBox25.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox25.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox25.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox25.setText("Cembung");
        jCheckBox25.setName("jCheckBox25"); // NOI18N
        jCheckBox25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox25ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox25);
        jCheckBox25.setBounds(330, 700, 80, 19);

        jCheckBox26.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox26.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox26.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox26.setText("Cekung");
        jCheckBox26.setName("jCheckBox26"); // NOI18N
        jCheckBox26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox26ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox26);
        jCheckBox26.setBounds(430, 700, 100, 19);

        jCheckBox27.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox27.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox27.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox27.setText("Lainnya");
        jCheckBox27.setName("jCheckBox27"); // NOI18N
        jCheckBox27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox27ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox27);
        jCheckBox27.setBounds(640, 700, 80, 19);

        Jk12.setHighlighter(null);
        Jk12.setName("Jk12"); // NOI18N
        FormInput.add(Jk12);
        Jk12.setBounds(720, 670, 150, 23);

        jLabel54.setText("Mata:");
        jLabel54.setToolTipText("");
        jLabel54.setName("jLabel54"); // NOI18N
        FormInput.add(jLabel54);
        jLabel54.setBounds(0, 730, 150, 23);

        jCheckBox28.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox28.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox28.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox28.setText("Normal");
        jCheckBox28.setName("jCheckBox28"); // NOI18N
        jCheckBox28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox28ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox28);
        jCheckBox28.setBounds(170, 730, 60, 19);

        jCheckBox29.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox29.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox29.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox29.setText("Anemia");
        jCheckBox29.setName("jCheckBox29"); // NOI18N
        jCheckBox29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox29ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox29);
        jCheckBox29.setBounds(330, 730, 80, 19);

        jCheckBox30.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox30.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox30.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox30.setText("Sekret");
        jCheckBox30.setName("jCheckBox30"); // NOI18N
        jCheckBox30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox30ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox30);
        jCheckBox30.setBounds(540, 730, 100, 19);

        jCheckBox31.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox31.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox31.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox31.setText("Lainnya");
        jCheckBox31.setName("jCheckBox31"); // NOI18N
        jCheckBox31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox31ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox31);
        jCheckBox31.setBounds(640, 730, 80, 19);

        Jk13.setHighlighter(null);
        Jk13.setName("Jk13"); // NOI18N
        FormInput.add(Jk13);
        Jk13.setBounds(720, 730, 150, 23);

        jCheckBox32.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox32.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox32.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox32.setText("Ikterus");
        jCheckBox32.setName("jCheckBox32"); // NOI18N
        jCheckBox32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox32ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox32);
        jCheckBox32.setBounds(430, 730, 80, 19);

        jLabel55.setText("THT:");
        jLabel55.setToolTipText("");
        jLabel55.setName("jLabel55"); // NOI18N
        FormInput.add(jLabel55);
        jLabel55.setBounds(0, 760, 150, 23);

        jCheckBox33.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox33.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox33.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox33.setText("Normal");
        jCheckBox33.setName("jCheckBox33"); // NOI18N
        jCheckBox33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox33ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox33);
        jCheckBox33.setBounds(170, 760, 60, 19);

        jCheckBox35.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox35.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox35.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox35.setText("Sianosis");
        jCheckBox35.setName("jCheckBox35"); // NOI18N
        jCheckBox35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox35ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox35);
        jCheckBox35.setBounds(330, 760, 80, 19);

        jCheckBox36.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox36.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox36.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox36.setText("Sekret");
        jCheckBox36.setName("jCheckBox36"); // NOI18N
        jCheckBox36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox36ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox36);
        jCheckBox36.setBounds(430, 760, 120, 19);

        jCheckBox37.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox37.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox37.setText("Lainnya");
        jCheckBox37.setName("jCheckBox37"); // NOI18N
        jCheckBox37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox37ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox37);
        jCheckBox37.setBounds(640, 820, 80, 19);

        Jk14.setHighlighter(null);
        Jk14.setName("Jk14"); // NOI18N
        FormInput.add(Jk14);
        Jk14.setBounds(720, 760, 150, 23);

        jLabel56.setText("Mukosa: warna");
        jLabel56.setToolTipText("");
        jLabel56.setName("jLabel56"); // NOI18N
        FormInput.add(jLabel56);
        jLabel56.setBounds(170, 820, 80, 23);

        jCheckBox38.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox38.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox38.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox38.setText("Normal");
        jCheckBox38.setName("jCheckBox38"); // NOI18N
        jCheckBox38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox38ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox38);
        jCheckBox38.setBounds(170, 790, 60, 19);

        jCheckBox39.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox39.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox39.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox39.setText("Labiodchizis");
        jCheckBox39.setName("jCheckBox39"); // NOI18N
        jCheckBox39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox39ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox39);
        jCheckBox39.setBounds(330, 790, 100, 19);

        jCheckBox40.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox40.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox40.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox40.setText("Labiopalatoshizis");
        jCheckBox40.setName("jCheckBox40"); // NOI18N
        jCheckBox40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox40ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox40);
        jCheckBox40.setBounds(430, 790, 120, 19);

        jCheckBox41.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox41.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox41.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox41.setText("Labiogenatopalatoschizis");
        jCheckBox41.setName("jCheckBox41"); // NOI18N
        jCheckBox41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox41ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox41);
        jCheckBox41.setBounds(640, 790, 160, 19);

        WarnaMukosa.setHighlighter(null);
        WarnaMukosa.setName("WarnaMukosa"); // NOI18N
        FormInput.add(WarnaMukosa);
        WarnaMukosa.setBounds(260, 820, 150, 23);

        jLabel57.setText("Mulut:");
        jLabel57.setToolTipText("");
        jLabel57.setName("jLabel57"); // NOI18N
        FormInput.add(jLabel57);
        jLabel57.setBounds(0, 790, 150, 23);

        Jk16.setHighlighter(null);
        Jk16.setName("Jk16"); // NOI18N
        FormInput.add(Jk16);
        Jk16.setBounds(720, 820, 150, 23);

        jCheckBox42.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox42.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox42.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox42.setText("Lainnya");
        jCheckBox42.setName("jCheckBox42"); // NOI18N
        jCheckBox42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox42ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox42);
        jCheckBox42.setBounds(640, 760, 80, 19);

        jLabel58.setText("Thorax:");
        jLabel58.setToolTipText("");
        jLabel58.setName("jLabel58"); // NOI18N
        FormInput.add(jLabel58);
        jLabel58.setBounds(0, 850, 150, 23);

        jCheckBox43.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox43.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox43.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox43.setText("Normal");
        jCheckBox43.setName("jCheckBox43"); // NOI18N
        jCheckBox43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox43ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox43);
        jCheckBox43.setBounds(170, 850, 60, 19);

        jCheckBox44.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox44.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox44.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox44.setText("Retraksi");
        jCheckBox44.setName("jCheckBox44"); // NOI18N
        jCheckBox44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox44ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox44);
        jCheckBox44.setBounds(330, 850, 80, 19);

        jCheckBox45.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox45.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox45.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox45.setText("Lainnya");
        jCheckBox45.setName("jCheckBox45"); // NOI18N
        jCheckBox45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox45ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox45);
        jCheckBox45.setBounds(640, 850, 80, 19);

        Jk17.setHighlighter(null);
        Jk17.setName("Jk17"); // NOI18N
        FormInput.add(Jk17);
        Jk17.setBounds(720, 850, 150, 23);

        jLabel59.setText("Abdomen:");
        jLabel59.setToolTipText("");
        jLabel59.setName("jLabel59"); // NOI18N
        FormInput.add(jLabel59);
        jLabel59.setBounds(0, 880, 150, 23);

        jCheckBox46.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox46.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox46.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox46.setText("Normal");
        jCheckBox46.setName("jCheckBox46"); // NOI18N
        jCheckBox46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox46ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox46);
        jCheckBox46.setBounds(170, 880, 60, 19);

        jCheckBox47.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox47.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox47.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox47.setText("Distensi");
        jCheckBox47.setName("jCheckBox47"); // NOI18N
        jCheckBox47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox47ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox47);
        jCheckBox47.setBounds(330, 880, 80, 19);

        jCheckBox48.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox48.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox48.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox48.setText("Omphalocele");
        jCheckBox48.setName("jCheckBox48"); // NOI18N
        jCheckBox48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox48ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox48);
        jCheckBox48.setBounds(430, 880, 120, 19);

        jCheckBox49.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox49.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox49.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox49.setText("Lainnya");
        jCheckBox49.setName("jCheckBox49"); // NOI18N
        jCheckBox49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox49ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox49);
        jCheckBox49.setBounds(640, 880, 80, 19);

        Jk18.setHighlighter(null);
        Jk18.setName("Jk18"); // NOI18N
        FormInput.add(Jk18);
        Jk18.setBounds(720, 880, 150, 23);

        jLabel60.setText("Tali pusat:");
        jLabel60.setToolTipText("");
        jLabel60.setName("jLabel60"); // NOI18N
        FormInput.add(jLabel60);
        jLabel60.setBounds(0, 910, 150, 23);

        jCheckBox50.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox50.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox50.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox50.setText("Segar");
        jCheckBox50.setName("jCheckBox50"); // NOI18N
        jCheckBox50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox50ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox50);
        jCheckBox50.setBounds(170, 910, 60, 19);

        jCheckBox51.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox51.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox51.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox51.setText("Tidak segar");
        jCheckBox51.setName("jCheckBox51"); // NOI18N
        jCheckBox51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox51ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox51);
        jCheckBox51.setBounds(330, 910, 80, 19);

        jCheckBox52.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox52.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox52.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox52.setText("Lainnya");
        jCheckBox52.setName("jCheckBox52"); // NOI18N
        jCheckBox52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox52ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox52);
        jCheckBox52.setBounds(640, 910, 80, 19);

        Jk19.setHighlighter(null);
        Jk19.setName("Jk19"); // NOI18N
        FormInput.add(Jk19);
        Jk19.setBounds(720, 910, 150, 23);

        jLabel61.setText("Punggung:");
        jLabel61.setToolTipText("");
        jLabel61.setName("jLabel61"); // NOI18N
        FormInput.add(jLabel61);
        jLabel61.setBounds(0, 940, 150, 23);

        jCheckBox53.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox53.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox53.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox53.setText("Normal");
        jCheckBox53.setName("jCheckBox53"); // NOI18N
        jCheckBox53.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox53ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox53);
        jCheckBox53.setBounds(170, 940, 60, 19);

        jCheckBox54.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox54.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox54.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox54.setText("Spina bifida");
        jCheckBox54.setName("jCheckBox54"); // NOI18N
        jCheckBox54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox54ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox54);
        jCheckBox54.setBounds(330, 940, 80, 19);

        jCheckBox55.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox55.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox55.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox55.setText("Lainnya");
        jCheckBox55.setName("jCheckBox55"); // NOI18N
        jCheckBox55.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox55ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox55);
        jCheckBox55.setBounds(640, 940, 80, 19);

        Jk20.setHighlighter(null);
        Jk20.setName("Jk20"); // NOI18N
        FormInput.add(Jk20);
        Jk20.setBounds(720, 940, 150, 23);

        jLabel62.setText("Genetalia");
        jLabel62.setToolTipText("");
        jLabel62.setName("jLabel62"); // NOI18N
        FormInput.add(jLabel62);
        jLabel62.setBounds(0, 970, 150, 23);

        jCheckBox56.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox56.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox56.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox56.setText("Normal");
        jCheckBox56.setName("jCheckBox56"); // NOI18N
        jCheckBox56.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox56ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox56);
        jCheckBox56.setBounds(170, 970, 60, 19);

        jCheckBox57.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox57.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox57.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox57.setText("Kelainan");
        jCheckBox57.setName("jCheckBox57"); // NOI18N
        jCheckBox57.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox57ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox57);
        jCheckBox57.setBounds(640, 970, 80, 19);

        Jk21.setHighlighter(null);
        Jk21.setName("Jk21"); // NOI18N
        FormInput.add(Jk21);
        Jk21.setBounds(720, 970, 150, 23);

        jLabel63.setText("Anus:");
        jLabel63.setToolTipText("");
        jLabel63.setName("jLabel63"); // NOI18N
        FormInput.add(jLabel63);
        jLabel63.setBounds(0, 1000, 150, 23);

        jCheckBox58.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox58.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox58.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox58.setText("Ada");
        jCheckBox58.setName("jCheckBox58"); // NOI18N
        jCheckBox58.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox58ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox58);
        jCheckBox58.setBounds(170, 1000, 60, 19);

        jCheckBox59.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox59.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox59.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox59.setText("Tidak ada");
        jCheckBox59.setName("jCheckBox59"); // NOI18N
        jCheckBox59.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox59ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox59);
        jCheckBox59.setBounds(330, 1000, 80, 19);

        jLabel64.setText("Ekstremnitas:");
        jLabel64.setToolTipText("");
        jLabel64.setName("jLabel64"); // NOI18N
        FormInput.add(jLabel64);
        jLabel64.setBounds(0, 1030, 150, 23);

        jCheckBox60.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox60.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox60.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox60.setText("Simetris");
        jCheckBox60.setName("jCheckBox60"); // NOI18N
        jCheckBox60.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox60ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox60);
        jCheckBox60.setBounds(170, 1030, 90, 19);

        jCheckBox61.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox61.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox61.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox61.setText("Asimetris");
        jCheckBox61.setName("jCheckBox61"); // NOI18N
        jCheckBox61.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox61ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox61);
        jCheckBox61.setBounds(330, 1030, 80, 19);

        jCheckBox62.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox62.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox62.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox62.setText("Lainnya");
        jCheckBox62.setName("jCheckBox62"); // NOI18N
        jCheckBox62.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox62ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox62);
        jCheckBox62.setBounds(640, 1030, 80, 19);

        Jk22.setHighlighter(null);
        Jk22.setName("Jk22"); // NOI18N
        FormInput.add(Jk22);
        Jk22.setBounds(720, 1030, 150, 23);

        jLabel65.setText("Kulit:");
        jLabel65.setToolTipText("");
        jLabel65.setName("jLabel65"); // NOI18N
        FormInput.add(jLabel65);
        jLabel65.setBounds(0, 1060, 150, 23);

        jCheckBox34.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox34.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox34.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox34.setText("Turgor");
        jCheckBox34.setName("jCheckBox34"); // NOI18N
        jCheckBox34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox34ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox34);
        jCheckBox34.setBounds(170, 1060, 60, 19);

        jCheckBox63.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox63.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox63.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox63.setText("Sianosis");
        jCheckBox63.setName("jCheckBox63"); // NOI18N
        jCheckBox63.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox63ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox63);
        jCheckBox63.setBounds(540, 1060, 90, 19);

        jCheckBox64.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox64.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox64.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox64.setText("Pendarahan");
        jCheckBox64.setName("jCheckBox64"); // NOI18N
        jCheckBox64.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox64ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox64);
        jCheckBox64.setBounds(330, 1060, 90, 19);

        jCheckBox65.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox65.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox65.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox65.setText("Hematoma");
        jCheckBox65.setName("jCheckBox65"); // NOI18N
        jCheckBox65.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox65ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox65);
        jCheckBox65.setBounds(430, 1060, 80, 19);

        jCheckBox66.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox66.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox66.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox66.setText("Lainnya");
        jCheckBox66.setName("jCheckBox66"); // NOI18N
        jCheckBox66.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox66ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox66);
        jCheckBox66.setBounds(640, 1060, 80, 19);

        Jk23.setHighlighter(null);
        Jk23.setName("Jk23"); // NOI18N
        FormInput.add(Jk23);
        Jk23.setBounds(810, 1090, 64, 23);

        Jk24.setHighlighter(null);
        Jk24.setName("Jk24"); // NOI18N
        FormInput.add(Jk24);
        Jk24.setBounds(720, 1060, 150, 23);

        jLabel66.setText("Reflek:");
        jLabel66.setToolTipText("");
        jLabel66.setName("jLabel66"); // NOI18N
        FormInput.add(jLabel66);
        jLabel66.setBounds(0, 1090, 150, 23);

        jCheckBox17.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox17.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox17.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox17.setText("Moro");
        jCheckBox17.setToolTipText("");
        jCheckBox17.setName("jCheckBox17"); // NOI18N
        jCheckBox17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox17ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox17);
        jCheckBox17.setBounds(170, 1090, 80, 19);

        jCheckBox67.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox67.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox67.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox67.setText("Grasping/genggam");
        jCheckBox67.setName("jCheckBox67"); // NOI18N
        jCheckBox67.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox67ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox67);
        jCheckBox67.setBounds(330, 1090, 120, 19);

        jCheckBox68.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox68.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox68.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox68.setText("Sucking/isap");
        jCheckBox68.setName("jCheckBox68"); // NOI18N
        jCheckBox68.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox68ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox68);
        jCheckBox68.setBounds(540, 1090, 90, 19);

        jCheckBox69.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox69.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox69.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox69.setText("Rooting");
        jCheckBox69.setName("jCheckBox69"); // NOI18N
        jCheckBox69.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox69ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox69);
        jCheckBox69.setBounds(740, 1090, 70, 19);

        Jk25.setHighlighter(null);
        Jk25.setName("Jk25"); // NOI18N
        FormInput.add(Jk25);
        Jk25.setBounds(260, 1060, 64, 23);

        Jk26.setHighlighter(null);
        Jk26.setName("Jk26"); // NOI18N
        FormInput.add(Jk26);
        Jk26.setBounds(260, 1090, 64, 23);

        Jk27.setHighlighter(null);
        Jk27.setName("Jk27"); // NOI18N
        FormInput.add(Jk27);
        Jk27.setBounds(460, 1090, 64, 23);

        Jk28.setHighlighter(null);
        Jk28.setName("Jk28"); // NOI18N
        FormInput.add(Jk28);
        Jk28.setBounds(630, 1090, 64, 23);

        jCheckBox70.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox70.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox70.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox70.setText("Tonick neck");
        jCheckBox70.setToolTipText("");
        jCheckBox70.setName("jCheckBox70"); // NOI18N
        jCheckBox70.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox70ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox70);
        jCheckBox70.setBounds(170, 1120, 90, 19);

        Jk29.setHighlighter(null);
        Jk29.setName("Jk29"); // NOI18N
        FormInput.add(Jk29);
        Jk29.setBounds(260, 1120, 64, 23);

        jCheckBox71.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox71.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox71.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox71.setText("Swallowing/menelan");
        jCheckBox71.setName("jCheckBox71"); // NOI18N
        jCheckBox71.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox71ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox71);
        jCheckBox71.setBounds(330, 1120, 130, 19);

        Jk30.setHighlighter(null);
        Jk30.setName("Jk30"); // NOI18N
        FormInput.add(Jk30);
        Jk30.setBounds(460, 1120, 64, 23);

        jCheckBox72.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox72.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox72.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox72.setText("Stepping");
        jCheckBox72.setName("jCheckBox72"); // NOI18N
        jCheckBox72.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox72ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox72);
        jCheckBox72.setBounds(540, 1120, 90, 19);

        Jk31.setHighlighter(null);
        Jk31.setName("Jk31"); // NOI18N
        FormInput.add(Jk31);
        Jk31.setBounds(630, 1120, 64, 23);

        jCheckBox73.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox73.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox73.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox73.setText("Babinski");
        jCheckBox73.setName("jCheckBox73"); // NOI18N
        jCheckBox73.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox73ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox73);
        jCheckBox73.setBounds(740, 1120, 70, 19);

        Jk32.setHighlighter(null);
        Jk32.setName("Jk32"); // NOI18N
        FormInput.add(Jk32);
        Jk32.setBounds(810, 1120, 64, 23);

        jCheckBox74.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox74.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox74.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox74.setText("Lainnya");
        jCheckBox74.setToolTipText("");
        jCheckBox74.setName("jCheckBox74"); // NOI18N
        jCheckBox74.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox74ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox74);
        jCheckBox74.setBounds(170, 1150, 80, 19);

        Jk33.setHighlighter(null);
        Jk33.setName("Jk33"); // NOI18N
        FormInput.add(Jk33);
        Jk33.setBounds(260, 1150, 64, 23);

        jLabel67.setText("Hasil Pemeriksaan Penunjang:");
        jLabel67.setToolTipText("");
        jLabel67.setName("jLabel67"); // NOI18N
        FormInput.add(jLabel67);
        jLabel67.setBounds(0, 400, 150, 23);

        scrollPane10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane10.setToolTipText("Jabarkan lokasi sesuai dengan regio anatomis");
        scrollPane10.setName("scrollPane10"); // NOI18N

        HasilPenunjangDerma.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        HasilPenunjangDerma.setColumns(30);
        HasilPenunjangDerma.setRows(5);
        HasilPenunjangDerma.setToolTipText("Jabarkan lokasi sesuai dengan regio anatomis");
        HasilPenunjangDerma.setName("HasilPenunjangDerma"); // NOI18N
        HasilPenunjangDerma.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HasilPenunjangDermaKeyPressed(evt);
            }
        });
        scrollPane10.setViewportView(HasilPenunjangDerma);

        FormInput.add(scrollPane10);
        scrollPane10.setBounds(170, 400, 260, 53);

        jLabel68.setText("Diagnosa Kerja/Diagnosa Banding:");
        jLabel68.setToolTipText("");
        jLabel68.setName("jLabel68"); // NOI18N
        FormInput.add(jLabel68);
        jLabel68.setBounds(430, 400, 170, 23);

        scrollPane11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane11.setToolTipText("Jabarkan lokasi sesuai dengan regio anatomis");
        scrollPane11.setName("scrollPane11"); // NOI18N

        DiagnosaKerjaDerma.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        DiagnosaKerjaDerma.setColumns(30);
        DiagnosaKerjaDerma.setRows(5);
        DiagnosaKerjaDerma.setToolTipText("Jabarkan lokasi sesuai dengan regio anatomis");
        DiagnosaKerjaDerma.setName("DiagnosaKerjaDerma"); // NOI18N
        DiagnosaKerjaDerma.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DiagnosaKerjaDermaKeyPressed(evt);
            }
        });
        scrollPane11.setViewportView(DiagnosaKerjaDerma);

        FormInput.add(scrollPane11);
        scrollPane11.setBounds(610, 400, 260, 53);

        scrollPane12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane12.setToolTipText("Jabarkan lokasi sesuai dengan regio anatomis");
        scrollPane12.setName("scrollPane12"); // NOI18N

        TerapiDerma.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TerapiDerma.setColumns(30);
        TerapiDerma.setRows(5);
        TerapiDerma.setToolTipText("Jabarkan lokasi sesuai dengan regio anatomis");
        TerapiDerma.setName("TerapiDerma"); // NOI18N
        TerapiDerma.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TerapiDermaKeyPressed(evt);
            }
        });
        scrollPane12.setViewportView(TerapiDerma);

        FormInput.add(scrollPane12);
        scrollPane12.setBounds(610, 460, 260, 53);

        jLabel69.setText("Terapi/Tindakan:");
        jLabel69.setToolTipText("");
        jLabel69.setName("jLabel69"); // NOI18N
        FormInput.add(jLabel69);
        jLabel69.setBounds(430, 460, 170, 23);

        scrollPane13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane13.setToolTipText("Jabarkan lokasi sesuai dengan regio anatomis");
        scrollPane13.setName("scrollPane13"); // NOI18N

        RencanaKerjaDerma.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        RencanaKerjaDerma.setColumns(30);
        RencanaKerjaDerma.setRows(5);
        RencanaKerjaDerma.setToolTipText("Jabarkan lokasi sesuai dengan regio anatomis");
        RencanaKerjaDerma.setName("RencanaKerjaDerma"); // NOI18N
        RencanaKerjaDerma.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RencanaKerjaDermaKeyPressed(evt);
            }
        });
        scrollPane13.setViewportView(RencanaKerjaDerma);

        FormInput.add(scrollPane13);
        scrollPane13.setBounds(170, 460, 260, 53);

        jLabel70.setText("Rencana Kerja:");
        jLabel70.setToolTipText("");
        jLabel70.setName("jLabel70"); // NOI18N
        FormInput.add(jLabel70);
        jLabel70.setBounds(0, 460, 150, 23);

        jLabel71.setText("Disposisi:");
        jLabel71.setToolTipText("");
        jLabel71.setName("jLabel71"); // NOI18N
        FormInput.add(jLabel71);
        jLabel71.setBounds(0, 520, 150, 23);

        jCheckBox75.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox75.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox75.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox75.setText("Boleh pulang");
        jCheckBox75.setName("jCheckBox75"); // NOI18N
        jCheckBox75.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox75ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox75);
        jCheckBox75.setBounds(170, 520, 130, 19);

        jCheckBox76.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox76.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox76.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox76.setText("Kontrol");
        jCheckBox76.setName("jCheckBox76"); // NOI18N
        jCheckBox76.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox76ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox76);
        jCheckBox76.setBounds(170, 550, 130, 19);

        jCheckBox77.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox77.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox77.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox77.setText("Dirawat");
        jCheckBox77.setName("jCheckBox77"); // NOI18N
        jCheckBox77.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox77ActionPerformed(evt);
            }
        });
        FormInput.add(jCheckBox77);
        jCheckBox77.setBounds(170, 580, 130, 19);

        jLabel72.setText("Jam keluar:");
        jLabel72.setToolTipText("");
        jLabel72.setName("jLabel72"); // NOI18N
        FormInput.add(jLabel72);
        jLabel72.setBounds(260, 520, 100, 23);

        jLabel73.setText("Tanggal:");
        jLabel73.setToolTipText("");
        jLabel73.setName("jLabel73"); // NOI18N
        FormInput.add(jLabel73);
        jLabel73.setBounds(260, 550, 100, 23);

        jLabel74.setText("Di ruang:");
        jLabel74.setToolTipText("");
        jLabel74.setName("jLabel74"); // NOI18N
        FormInput.add(jLabel74);
        jLabel74.setBounds(260, 580, 100, 23);

        JamKeluarDerma.setHighlighter(null);
        JamKeluarDerma.setName("JamKeluarDerma"); // NOI18N
        FormInput.add(JamKeluarDerma);
        JamKeluarDerma.setBounds(370, 520, 64, 23);

        TglDerma.setHighlighter(null);
        TglDerma.setName("TglDerma"); // NOI18N
        FormInput.add(TglDerma);
        TglDerma.setBounds(370, 550, 64, 23);

        RuangDerma.setHighlighter(null);
        RuangDerma.setName("RuangDerma"); // NOI18N
        FormInput.add(RuangDerma);
        RuangDerma.setBounds(370, 580, 64, 23);

        jLabel75.setText("WITA");
        jLabel75.setToolTipText("");
        jLabel75.setName("jLabel75"); // NOI18N
        FormInput.add(jLabel75);
        jLabel75.setBounds(440, 520, 40, 23);

        jLabel76.setText("Tempat:");
        jLabel76.setToolTipText("");
        jLabel76.setName("jLabel76"); // NOI18N
        FormInput.add(jLabel76);
        jLabel76.setBounds(440, 550, 40, 23);

        TempatDerma.setHighlighter(null);
        TempatDerma.setName("TempatDerma"); // NOI18N
        FormInput.add(TempatDerma);
        TempatDerma.setBounds(490, 550, 64, 23);

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
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "15-09-2023" }));
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
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "15-09-2023" }));
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

    private void EflorisensiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EflorisensiKeyPressed
//        Valid.pindah2(evt,Informasi,RPD);
    }//GEN-LAST:event_EflorisensiKeyPressed

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

    private void jCheckBox9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox9ActionPerformed

    private void jCheckBox18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox18ActionPerformed

    private void jCheckBox19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox19ActionPerformed

    private void jCheckBox20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox20ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox20ActionPerformed

    private void jCheckBox13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox13ActionPerformed

    private void jCheckBox21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox21ActionPerformed

    private void jCheckBox22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox22ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox22ActionPerformed

    private void jCheckBox23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox23ActionPerformed

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

    private void jCheckBox28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox28ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox28ActionPerformed

    private void jCheckBox29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox29ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox29ActionPerformed

    private void jCheckBox30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox30ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox30ActionPerformed

    private void jCheckBox31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox31ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox31ActionPerformed

    private void jCheckBox32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox32ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox32ActionPerformed

    private void jCheckBox33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox33ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox33ActionPerformed

    private void jCheckBox35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox35ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox35ActionPerformed

    private void jCheckBox36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox36ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox36ActionPerformed

    private void jCheckBox37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox37ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox37ActionPerformed

    private void jCheckBox38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox38ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox38ActionPerformed

    private void jCheckBox39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox39ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox39ActionPerformed

    private void jCheckBox40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox40ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox40ActionPerformed

    private void jCheckBox41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox41ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox41ActionPerformed

    private void jCheckBox42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox42ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox42ActionPerformed

    private void jCheckBox43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox43ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox43ActionPerformed

    private void jCheckBox44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox44ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox44ActionPerformed

    private void jCheckBox45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox45ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox45ActionPerformed

    private void jCheckBox46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox46ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox46ActionPerformed

    private void jCheckBox47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox47ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox47ActionPerformed

    private void jCheckBox48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox48ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox48ActionPerformed

    private void jCheckBox49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox49ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox49ActionPerformed

    private void jCheckBox50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox50ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox50ActionPerformed

    private void jCheckBox51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox51ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox51ActionPerformed

    private void jCheckBox52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox52ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox52ActionPerformed

    private void jCheckBox53ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox53ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox53ActionPerformed

    private void jCheckBox54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox54ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox54ActionPerformed

    private void jCheckBox55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox55ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox55ActionPerformed

    private void jCheckBox56ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox56ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox56ActionPerformed

    private void jCheckBox57ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox57ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox57ActionPerformed

    private void jCheckBox58ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox58ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox58ActionPerformed

    private void jCheckBox59ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox59ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox59ActionPerformed

    private void jCheckBox60ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox60ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox60ActionPerformed

    private void jCheckBox61ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox61ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox61ActionPerformed

    private void jCheckBox62ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox62ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox62ActionPerformed

    private void jCheckBox34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox34ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox34ActionPerformed

    private void jCheckBox63ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox63ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox63ActionPerformed

    private void jCheckBox64ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox64ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox64ActionPerformed

    private void jCheckBox65ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox65ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox65ActionPerformed

    private void jCheckBox66ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox66ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox66ActionPerformed

    private void jCheckBox17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox17ActionPerformed

    private void jCheckBox67ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox67ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox67ActionPerformed

    private void jCheckBox68ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox68ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox68ActionPerformed

    private void jCheckBox69ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox69ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox69ActionPerformed

    private void jCheckBox70ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox70ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox70ActionPerformed

    private void jCheckBox71ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox71ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox71ActionPerformed

    private void jCheckBox72ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox72ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox72ActionPerformed

    private void jCheckBox73ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox73ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox73ActionPerformed

    private void jCheckBox74ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox74ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox74ActionPerformed

    private void HasilPenunjangDermaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HasilPenunjangDermaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_HasilPenunjangDermaKeyPressed

    private void DiagnosaKerjaDermaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosaKerjaDermaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DiagnosaKerjaDermaKeyPressed

    private void TerapiDermaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TerapiDermaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TerapiDermaKeyPressed

    private void RencanaKerjaDermaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RencanaKerjaDermaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_RencanaKerjaDermaKeyPressed

    private void jCheckBox75ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox75ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox75ActionPerformed

    private void jCheckBox76ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox76ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox76ActionPerformed

    private void jCheckBox77ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox77ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox77ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMAsesmenAwalMedisKulitKelamin dialog = new RMAsesmenAwalMedisKulitKelamin(new javax.swing.JFrame(), true);
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
    private widget.TextArea DiagnosaKerjaDerma;
    private widget.TextArea DiagnosaKerjaNeonatologi;
    private widget.TextArea Eflorisensi;
    private widget.PanelBiasa FormInput;
    private widget.PanelBiasa FormMasalahRencana;
    private widget.PanelBiasa FormMenu;
    private widget.TextArea HasilPenunjangDerma;
    private widget.TextArea HasilPenunjangNeonatologi;
    private widget.TextBox JamKeluarDerma;
    private widget.TextBox JamNeonatologi;
    private widget.TextBox Jk1;
    private widget.TextBox Jk11;
    private widget.TextBox Jk12;
    private widget.TextBox Jk13;
    private widget.TextBox Jk14;
    private widget.TextBox Jk16;
    private widget.TextBox Jk17;
    private widget.TextBox Jk18;
    private widget.TextBox Jk19;
    private widget.TextBox Jk2;
    private widget.TextBox Jk20;
    private widget.TextBox Jk21;
    private widget.TextBox Jk22;
    private widget.TextBox Jk23;
    private widget.TextBox Jk24;
    private widget.TextBox Jk25;
    private widget.TextBox Jk26;
    private widget.TextBox Jk27;
    private widget.TextBox Jk28;
    private widget.TextBox Jk29;
    private widget.TextBox Jk30;
    private widget.TextBox Jk31;
    private widget.TextBox Jk32;
    private widget.TextBox Jk33;
    private widget.TextBox Jk7;
    private widget.TextBox KdPetugas;
    private widget.TextBox KelenjarLimfe;
    private widget.TextArea KeluhanUtama;
    private widget.TextBox Kuku;
    private widget.Label LCount;
    private widget.editorpane LoadHTML;
    private widget.TextArea Lokalisasi;
    private widget.TextBox Mukosa;
    private widget.TextBox NmPetugas;
    private widget.PanelBiasa PanelAccor;
    private widget.TextBox Rambut;
    private widget.TextArea RencanaKerjaDerma;
    private widget.TextArea RencanaKerjaNeonatologi;
    private widget.TextArea RiwPenyakitSkrg;
    private widget.TextBox RuangDerma;
    private widget.TextBox RuangNeonatologi;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRM1;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.TextBox TPasien1;
    private javax.swing.JTabbedPane TabRawat;
    private widget.TextBox TempatDerma;
    private widget.TextBox TempatNeonatologi;
    private widget.TextArea TerapiDerma;
    private widget.TextArea TerapiNeonatologi;
    private widget.Tanggal TglAsuhan;
    private widget.TextBox TglDerma;
    private widget.TextBox TglLahir;
    private widget.TextBox TglNeonatologi;
    private widget.TextBox WarnaMukosa;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.InternalFrame internalFrame3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox10;
    private javax.swing.JCheckBox jCheckBox11;
    private javax.swing.JCheckBox jCheckBox12;
    private javax.swing.JCheckBox jCheckBox13;
    private javax.swing.JCheckBox jCheckBox14;
    private javax.swing.JCheckBox jCheckBox15;
    private javax.swing.JCheckBox jCheckBox16;
    private javax.swing.JCheckBox jCheckBox17;
    private javax.swing.JCheckBox jCheckBox18;
    private javax.swing.JCheckBox jCheckBox19;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox20;
    private javax.swing.JCheckBox jCheckBox21;
    private javax.swing.JCheckBox jCheckBox22;
    private javax.swing.JCheckBox jCheckBox23;
    private javax.swing.JCheckBox jCheckBox24;
    private javax.swing.JCheckBox jCheckBox25;
    private javax.swing.JCheckBox jCheckBox26;
    private javax.swing.JCheckBox jCheckBox27;
    private javax.swing.JCheckBox jCheckBox28;
    private javax.swing.JCheckBox jCheckBox29;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox30;
    private javax.swing.JCheckBox jCheckBox31;
    private javax.swing.JCheckBox jCheckBox32;
    private javax.swing.JCheckBox jCheckBox33;
    private javax.swing.JCheckBox jCheckBox34;
    private javax.swing.JCheckBox jCheckBox35;
    private javax.swing.JCheckBox jCheckBox36;
    private javax.swing.JCheckBox jCheckBox37;
    private javax.swing.JCheckBox jCheckBox38;
    private javax.swing.JCheckBox jCheckBox39;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox40;
    private javax.swing.JCheckBox jCheckBox41;
    private javax.swing.JCheckBox jCheckBox42;
    private javax.swing.JCheckBox jCheckBox43;
    private javax.swing.JCheckBox jCheckBox44;
    private javax.swing.JCheckBox jCheckBox45;
    private javax.swing.JCheckBox jCheckBox46;
    private javax.swing.JCheckBox jCheckBox47;
    private javax.swing.JCheckBox jCheckBox48;
    private javax.swing.JCheckBox jCheckBox49;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox50;
    private javax.swing.JCheckBox jCheckBox51;
    private javax.swing.JCheckBox jCheckBox52;
    private javax.swing.JCheckBox jCheckBox53;
    private javax.swing.JCheckBox jCheckBox54;
    private javax.swing.JCheckBox jCheckBox55;
    private javax.swing.JCheckBox jCheckBox56;
    private javax.swing.JCheckBox jCheckBox57;
    private javax.swing.JCheckBox jCheckBox58;
    private javax.swing.JCheckBox jCheckBox59;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox60;
    private javax.swing.JCheckBox jCheckBox61;
    private javax.swing.JCheckBox jCheckBox62;
    private javax.swing.JCheckBox jCheckBox63;
    private javax.swing.JCheckBox jCheckBox64;
    private javax.swing.JCheckBox jCheckBox65;
    private javax.swing.JCheckBox jCheckBox66;
    private javax.swing.JCheckBox jCheckBox67;
    private javax.swing.JCheckBox jCheckBox68;
    private javax.swing.JCheckBox jCheckBox69;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox70;
    private javax.swing.JCheckBox jCheckBox71;
    private javax.swing.JCheckBox jCheckBox72;
    private javax.swing.JCheckBox jCheckBox73;
    private javax.swing.JCheckBox jCheckBox74;
    private javax.swing.JCheckBox jCheckBox75;
    private javax.swing.JCheckBox jCheckBox76;
    private javax.swing.JCheckBox jCheckBox77;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    private widget.Label jLabel10;
    private widget.Label jLabel100;
    private widget.Label jLabel11;
    private widget.Label jLabel19;
    private widget.Label jLabel21;
    private widget.Label jLabel30;
    private widget.Label jLabel31;
    private widget.Label jLabel32;
    private widget.Label jLabel33;
    private widget.Label jLabel34;
    private widget.Label jLabel35;
    private widget.Label jLabel36;
    private widget.Label jLabel37;
    private widget.Label jLabel38;
    private widget.Label jLabel39;
    private widget.Label jLabel40;
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
    private widget.Label jLabel54;
    private widget.Label jLabel55;
    private widget.Label jLabel56;
    private widget.Label jLabel57;
    private widget.Label jLabel58;
    private widget.Label jLabel59;
    private widget.Label jLabel6;
    private widget.Label jLabel60;
    private widget.Label jLabel61;
    private widget.Label jLabel62;
    private widget.Label jLabel63;
    private widget.Label jLabel64;
    private widget.Label jLabel65;
    private widget.Label jLabel66;
    private widget.Label jLabel67;
    private widget.Label jLabel68;
    private widget.Label jLabel69;
    private widget.Label jLabel7;
    private widget.Label jLabel70;
    private widget.Label jLabel71;
    private widget.Label jLabel72;
    private widget.Label jLabel73;
    private widget.Label jLabel74;
    private widget.Label jLabel75;
    private widget.Label jLabel76;
    private widget.Label jLabel8;
    private widget.Label jLabel98;
    private widget.Label jLabel99;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private widget.Label label11;
    private widget.Label label14;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.ScrollPane scrollInput;
    private widget.ScrollPane scrollPane1;
    private widget.ScrollPane scrollPane10;
    private widget.ScrollPane scrollPane11;
    private widget.ScrollPane scrollPane12;
    private widget.ScrollPane scrollPane13;
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
            Jk1.setText(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString()); 
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
                    Jk1.setText(rs.getString("jk"));
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
