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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import kepegawaian.DlgCariDokter;
import keuangan.DlgKamar;


/**
 *
 * @author perpustakaan
 */
public final class RMAsesmenAwalMedisKulitKelamin extends javax.swing.JDialog {
    private final DefaultTableModel tabMode,tabModeMasalah,tabModeDetailMasalah, tabModeStig, tabModeSyaraf;
    private final DefaultTableModel tabModeKepala, tabModeUub, tabModeMata, tabModeTht, tabModeMulut, tabModeThorax;
    private final DefaultTableModel tabModeAbdomen, tabModeTaliPusat, tabModePunggung, tabModeEkstremnitas, tabModeKulit;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps,ps2,stat, stig,syaraf,kepala,uub,mata,tht,mulut,thorax,abdomen,tpusat,punggung,ekstrem,kulit,genet;
    private ResultSet rs,rs2;
    private int i=0,jml=0,index=0, stigclick=0, syarafclick=0, kepalaclick=0, uubclick=0, mataclick=0, thtclick=0, mulutclick=0;
    private int thoraxclick=0, abdomenclick=0, tpusatclick=0, punggungclick=0, ekstremclick=0, kulitclick=0;
    private DlgCariDokter petugas=new DlgCariDokter(null,false);
    private MasterStigAtopik stigatopik=new MasterStigAtopik(null,false);
    private DlgKamar dlgkamar=new DlgKamar(null,false);
    private DlgKamar dlgkamar1=new DlgKamar(null,false);
    String no_rawat,no_rm,pasien,jk,tgllahir,ginjal_kanan,ginjal_kiri,
            sbuli,kd_petugas,nm_petugas,tgl_asuhan,finger="",pilihan="",kamar,namakamar,alamat,umur;
    JPanel jPanel1;
    List<Integer> idstiglist = new ArrayList<>();
    List<String> stiglist = new ArrayList<>();
    List<Integer> idsyaraflist = new ArrayList<>();
    List<String> syaraflist = new ArrayList<>();
    List<Integer> idkepalalist = new ArrayList<>();
    List<String> kepalalist = new ArrayList<>();
    List<Integer> iduublist = new ArrayList<>();
    List<String> uublist = new ArrayList<>();
    List<Integer> idmatalist = new ArrayList<>();
    List<String> matalist = new ArrayList<>();
    List<Integer> idthtlist = new ArrayList<>();
    List<String> thtlist = new ArrayList<>();
    List<Integer> idmulutlist = new ArrayList<>();
    List<String> mulutlist = new ArrayList<>();
    List<Integer> idthoraxlist = new ArrayList<>();
    List<String> thoraxlist = new ArrayList<>();
    List<Integer> idabdomenlist = new ArrayList<>();
    List<String> abdomenlist = new ArrayList<>();
    List<Integer> idtalipusatlist = new ArrayList<>();
    List<String> talipusatlist = new ArrayList<>();
    List<Integer> idpunggunglist = new ArrayList<>();
    List<String> punggunglist = new ArrayList<>();
    List<Integer> idekstremnitaslist = new ArrayList<>();
    List<String> ekstremnitaslist = new ArrayList<>();
    List<Integer> idkulitlist = new ArrayList<>();
    List<String> kulitlist = new ArrayList<>();
    String fkelenjarkeringat;
    String disposisiderma;
    String anus;
    String reflek;
    String disposisineona;
    
    /** Creates new form DlgRujuk
     * @param parent
     * @param modal */
    public RMAsesmenAwalMedisKulitKelamin(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
//                        rs.getString("rencana_kerja_derma"),
//                        rs.getString("terapi_derma"),rs.getString("disposisi_derma"),
//                        rs.getString("jam_disposisi_derma"),rs.getString("tgl_disposisi_derma"),
//                        rs.getString("tempat_disposisi_derma"),rs.getString("ruang_disposisi_derma"),
//                        rs.getString("warna_mukosa"),rs.getString("anus"),
//                        rs.getString("turgor"),rs.getString("hasil_penunjang_neonatologi"),
//                        rs.getString("diag_kerja_neonatologi"),rs.getString("rencana_kerja_neonatologi"),
//                        rs.getString("terapi_neonatologi"),rs.getString("disposisi_neonatologi"),
//                        rs.getString("jam_disposisi_neona"),rs.getString("tgl_disposisi_neonatologi"),
//                        rs.getString("tempat_disposisi_neonatologi"),rs.getString("ruang_disposisi_neonatologi"),
//                        rs.getString("kd_dokter"),rs.getString("nm_dokter")
        
        tabMode=new DefaultTableModel(null,new Object[]{
            "No.Rawat","No.RM","Nama Pasien","J.K.",
            "Tgl.Lahir","Tgl Periksa", 
            "Keluhan Utama","Riwayat Penyakit Sekarang",
            "Lokalisasi", "Eflorisensi",
            "Mukosa", "Rambut","Kuku",
            "Fungsi Kelenjar Keringat","Kelenjar Limfe","Hasil Penunjang Derma",
            "Diagnosa Kerja Derma", "", "", "", "", "","","","","","","","","","","","","","","",
            "Kode Dokter","Nama Dokter"
        }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbObat.setModel(tabMode);


        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());
        
        
        Object[] row={"Stigmata Atopik", "id"};
        tabModeStig=new DefaultTableModel(null,row){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        
        Object[] syarafrow={"Syaraf", "id"};
        tabModeSyaraf=new DefaultTableModel(null,syarafrow){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        
        Object[] kepalarow={"Kepala", "id"};
        tabModeKepala=new DefaultTableModel(null,kepalarow){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        
        Object[] uubrow={"UUB", "id"};
        tabModeUub=new DefaultTableModel(null,uubrow){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        
        Object[] matarow={"Mata", "id"};
        tabModeMata=new DefaultTableModel(null,matarow){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        
        Object[] thtrow={"THT", "id"};
        tabModeTht=new DefaultTableModel(null,thtrow){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        
        Object[] mulutrow={"Mulut", "id"};
        tabModeMulut=new DefaultTableModel(null,mulutrow){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        
        Object[] thoraxrow={"Thorax", "id"};
        tabModeThorax=new DefaultTableModel(null,thoraxrow){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        
        Object[] abdomenrow={"Abdomen", "id"};
        tabModeAbdomen=new DefaultTableModel(null,abdomenrow){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        
        Object[] talipusatrow={"Tali Pusat", "id"};
        tabModeTaliPusat=new DefaultTableModel(null,talipusatrow){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        
        Object[] punggungrow={"Punggung", "id"};
        tabModePunggung=new DefaultTableModel(null,punggungrow){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        
        Object[] ekstremnitasrow={"Ekstremnitas", "id"};
        tabModeEkstremnitas=new DefaultTableModel(null,ekstremnitasrow){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        
        Object[] kulitrow={"Kulit", "id"};
        tabModeKulit=new DefaultTableModel(null,kulitrow){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };

        
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
        
        dlgkamar.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(dlgkamar.getTable().getSelectedRow()!= -1){ 
                    NoBed.setText(dlgkamar.getTable().getValueAt(dlgkamar.getTable().getSelectedRow(),1).toString());
                    KdKamar.setText(dlgkamar.getTable().getValueAt(dlgkamar.getTable().getSelectedRow(),2).toString());   
                    NmKamar.setText(dlgkamar.getTable().getValueAt(dlgkamar.getTable().getSelectedRow(),3).toString());   
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
        
        dlgkamar1.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(dlgkamar1.getTable().getSelectedRow()!= -1){ 
                    NoBed1.setText(dlgkamar1.getTable().getValueAt(dlgkamar1.getTable().getSelectedRow(),1).toString());
                    KdKamar1.setText(dlgkamar1.getTable().getValueAt(dlgkamar1.getTable().getSelectedRow(),2).toString());   
                    NmKamar1.setText(dlgkamar1.getTable().getValueAt(dlgkamar1.getTable().getSelectedRow(),3).toString());   
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
        timePicker1 = new com.raven.swing.TimePicker();
        timePicker2 = new com.raven.swing.TimePicker();
        BtnGrpFungsiKelenjarKeringat = new javax.swing.ButtonGroup();
        BtnGrpDisposisiDerma = new javax.swing.ButtonGroup();
        BtnGrpAnus = new javax.swing.ButtonGroup();
        BtnGrpReflek = new javax.swing.ButtonGroup();
        BtnGrpDisposisiNeona = new javax.swing.ButtonGroup();
        BtnGrpGenetalia = new javax.swing.ButtonGroup();
        pStig = new javax.swing.JPanel();
        saveBtn = new javax.swing.JButton();
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
        jLabel30 = new widget.Label();
        scrollPane1 = new widget.ScrollPane();
        Eflorisensi = new widget.TextArea();
        scrollPane2 = new widget.ScrollPane();
        HasilPenunjangNeonatologi = new widget.TextArea();
        jLabel31 = new widget.Label();
        TglDerma = new widget.Tanggal();
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
        Jk = new widget.TextBox();
        jLabel36 = new widget.Label();
        StigAtopik = new widget.TextBox();
        jLabel37 = new widget.Label();
        Kuku = new widget.TextBox();
        jLabel38 = new widget.Label();
        Rambut = new widget.TextBox();
        jLabel39 = new widget.Label();
        jLabel40 = new widget.Label();
        Mukosa = new widget.TextBox();
        jLabel41 = new widget.Label();
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
        jLabel47 = new widget.Label();
        jLabel48 = new widget.Label();
        jLabel49 = new widget.Label();
        TempatNeonatologi = new widget.TextBox();
        jLabel50 = new widget.Label();
        jLabel51 = new widget.Label();
        jSeparator13 = new javax.swing.JSeparator();
        jLabel100 = new widget.Label();
        jSeparator14 = new javax.swing.JSeparator();
        jLabel52 = new widget.Label();
        jLabel53 = new widget.Label();
        KelenjarLimfe = new widget.TextBox();
        jLabel54 = new widget.Label();
        jLabel55 = new widget.Label();
        jLabel56 = new widget.Label();
        WarnaMukosa = new widget.TextBox();
        jLabel57 = new widget.Label();
        jLabel58 = new widget.Label();
        jLabel59 = new widget.Label();
        jLabel60 = new widget.Label();
        jLabel61 = new widget.Label();
        jLabel63 = new widget.Label();
        jLabel64 = new widget.Label();
        jLabel65 = new widget.Label();
        Root = new widget.TextBox();
        jLabel66 = new widget.Label();
        cbMoro = new javax.swing.JCheckBox();
        cbGrasping = new javax.swing.JCheckBox();
        cbSuck = new javax.swing.JCheckBox();
        cbRoot = new javax.swing.JCheckBox();
        Turgor = new widget.TextBox();
        Moro = new widget.TextBox();
        Grasping = new widget.TextBox();
        Suck = new widget.TextBox();
        cbTonick = new javax.swing.JCheckBox();
        Tonick = new widget.TextBox();
        cbSwallow = new javax.swing.JCheckBox();
        Swallow = new widget.TextBox();
        cbStep = new javax.swing.JCheckBox();
        Step = new widget.TextBox();
        cbBabinski = new javax.swing.JCheckBox();
        Babinski = new widget.TextBox();
        cbLainnya = new javax.swing.JCheckBox();
        Lainnya = new widget.TextBox();
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
        jLabel72 = new widget.Label();
        jLabel73 = new widget.Label();
        jLabel74 = new widget.Label();
        JamKeluarDerma = new widget.TextBox();
        NmKamar = new widget.TextBox();
        jLabel75 = new widget.Label();
        jLabel76 = new widget.Label();
        TempatDerma = new widget.TextBox();
        BtnStigAtopik = new widget.Button();
        BtnDropStig = new widget.Button();
        Syaraf = new widget.TextBox();
        BtnDropSyaraf = new widget.Button();
        BtnSyaraf = new widget.Button();
        Uub = new widget.TextBox();
        BtnDropUub = new widget.Button();
        BtnUub = new widget.Button();
        Kepala = new widget.TextBox();
        BtnDropKepala = new widget.Button();
        BtnKepala = new widget.Button();
        Mata = new widget.TextBox();
        BtnDropMata = new widget.Button();
        BtnMata = new widget.Button();
        Tht = new widget.TextBox();
        BtnDropTht = new widget.Button();
        BtnTht = new widget.Button();
        Mulut = new widget.TextBox();
        BtnDropMulut = new widget.Button();
        BtnMulut = new widget.Button();
        Thorax = new widget.TextBox();
        BtnDropThorax = new widget.Button();
        BtnThorax = new widget.Button();
        Abdomen = new widget.TextBox();
        BtnDropAbdomen = new widget.Button();
        BtnAbdomen = new widget.Button();
        TaliPusat = new widget.TextBox();
        BtnDropTaliPusat = new widget.Button();
        BtnTaliPusat = new widget.Button();
        Punggung = new widget.TextBox();
        BtnDropPunggung = new widget.Button();
        BtnPunggung = new widget.Button();
        Genetalia = new widget.TextBox();
        Ekstremnitas = new widget.TextBox();
        BtnDropEkstremnitas = new widget.Button();
        BtnEkstremnitas = new widget.Button();
        Kulit = new widget.TextBox();
        BtnDropKulit = new widget.Button();
        BtnKulit = new widget.Button();
        jLabel77 = new widget.Label();
        BtnDropJamDisposisi = new widget.Button();
        TglAsuhan = new widget.Tanggal();
        BtnKamar = new widget.Button();
        BtnShowJamNeo = new widget.Button();
        TglDerma1 = new widget.Tanggal();
        NoBed = new widget.TextBox();
        KdKamar = new widget.TextBox();
        NoBed1 = new widget.TextBox();
        KdKamar1 = new widget.TextBox();
        NmKamar1 = new widget.TextBox();
        BtnKamar1 = new widget.Button();
        rbHiperhidrosis = new javax.swing.JRadioButton();
        rbAnshidrosis = new javax.swing.JRadioButton();
        rbBolehPulangDisposisi = new javax.swing.JRadioButton();
        rbKontrolDisposisi = new javax.swing.JRadioButton();
        rbDirawatDisposisi = new javax.swing.JRadioButton();
        rbAda = new javax.swing.JRadioButton();
        rbTidakAda = new javax.swing.JRadioButton();
        rbBolehPulangNeona = new javax.swing.JRadioButton();
        rbKontrolNeona = new javax.swing.JRadioButton();
        rbDirawatNeona = new javax.swing.JRadioButton();
        jLabel78 = new widget.Label();
        rbKelainan = new javax.swing.JRadioButton();
        rbNormal = new javax.swing.JRadioButton();
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

        timePicker1.set24hourMode(true);
        timePicker1.setDisplayText(JamKeluarDerma);
        timePicker1.setName("timePicker1"); // NOI18N

        timePicker2.set24hourMode(true);
        timePicker2.setDisplayText(JamNeonatologi);
        timePicker2.setName("timePicker2"); // NOI18N

        pStig.setName("pStig"); // NOI18N

        saveBtn.setText("SIMPAN");
        saveBtn.setName("saveBtn"); // NOI18N
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });
        pStig.add(saveBtn);

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
        KdPetugas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KdPetugasActionPerformed(evt);
            }
        });
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
        NmPetugas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NmPetugasActionPerformed(evt);
            }
        });
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
        BtnDokter.setBounds(360, 40, 28, 23);

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

        TglDerma.setForeground(new java.awt.Color(50, 70, 50));
        TglDerma.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "23-11-2023" }));
        TglDerma.setDisplayFormat("dd-MM-yyyy");
        TglDerma.setName("TglDerma"); // NOI18N
        TglDerma.setOpaque(false);
        TglDerma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TglDermaActionPerformed(evt);
            }
        });
        TglDerma.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TglDermaKeyPressed(evt);
            }
        });
        FormInput.add(TglDerma);
        TglDerma.setBounds(370, 550, 80, 23);

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

        Jk.setEditable(false);
        Jk.setHighlighter(null);
        Jk.setName("Jk"); // NOI18N
        FormInput.add(Jk);
        Jk.setBounds(774, 10, 80, 23);

        jLabel36.setText("Stigma Atopik:");
        jLabel36.setToolTipText("");
        jLabel36.setName("jLabel36"); // NOI18N
        FormInput.add(jLabel36);
        jLabel36.setBounds(0, 250, 150, 23);

        StigAtopik.setHighlighter(null);
        StigAtopik.setName("StigAtopik"); // NOI18N
        StigAtopik.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StigAtopikActionPerformed(evt);
            }
        });
        FormInput.add(StigAtopik);
        StigAtopik.setBounds(170, 250, 640, 23);

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

        JamNeonatologi.setHighlighter(null);
        JamNeonatologi.setName("JamNeonatologi"); // NOI18N
        FormInput.add(JamNeonatologi);
        JamNeonatologi.setBounds(370, 1300, 80, 23);

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

        jLabel47.setText("Kepala:");
        jLabel47.setToolTipText("");
        jLabel47.setName("jLabel47"); // NOI18N
        FormInput.add(jLabel47);
        jLabel47.setBounds(0, 640, 150, 23);

        jLabel48.setText("WITA");
        jLabel48.setToolTipText("");
        jLabel48.setName("jLabel48"); // NOI18N
        FormInput.add(jLabel48);
        jLabel48.setBounds(470, 1300, 40, 23);

        jLabel49.setText("Tempat:");
        jLabel49.setToolTipText("");
        jLabel49.setName("jLabel49"); // NOI18N
        FormInput.add(jLabel49);
        jLabel49.setBounds(470, 1330, 40, 23);

        TempatNeonatologi.setHighlighter(null);
        TempatNeonatologi.setName("TempatNeonatologi"); // NOI18N
        FormInput.add(TempatNeonatologi);
        TempatNeonatologi.setBounds(520, 1330, 64, 23);

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

        jLabel53.setText("UUB:");
        jLabel53.setToolTipText("");
        jLabel53.setName("jLabel53"); // NOI18N
        FormInput.add(jLabel53);
        jLabel53.setBounds(0, 670, 150, 23);

        KelenjarLimfe.setHighlighter(null);
        KelenjarLimfe.setName("KelenjarLimfe"); // NOI18N
        FormInput.add(KelenjarLimfe);
        KelenjarLimfe.setBounds(170, 340, 150, 23);

        jLabel54.setText("Mata:");
        jLabel54.setToolTipText("");
        jLabel54.setName("jLabel54"); // NOI18N
        FormInput.add(jLabel54);
        jLabel54.setBounds(0, 700, 150, 23);

        jLabel55.setText("THT:");
        jLabel55.setToolTipText("");
        jLabel55.setName("jLabel55"); // NOI18N
        FormInput.add(jLabel55);
        jLabel55.setBounds(0, 730, 150, 23);

        jLabel56.setText("Turgor: ");
        jLabel56.setToolTipText("");
        jLabel56.setName("jLabel56"); // NOI18N
        FormInput.add(jLabel56);
        jLabel56.setBounds(170, 1060, 40, 23);

        WarnaMukosa.setHighlighter(null);
        WarnaMukosa.setName("WarnaMukosa"); // NOI18N
        FormInput.add(WarnaMukosa);
        WarnaMukosa.setBounds(260, 790, 150, 23);

        jLabel57.setText("Mulut:");
        jLabel57.setToolTipText("");
        jLabel57.setName("jLabel57"); // NOI18N
        FormInput.add(jLabel57);
        jLabel57.setBounds(0, 760, 150, 23);

        jLabel58.setText("Thorax:");
        jLabel58.setToolTipText("");
        jLabel58.setName("jLabel58"); // NOI18N
        FormInput.add(jLabel58);
        jLabel58.setBounds(0, 820, 150, 23);

        jLabel59.setText("Abdomen:");
        jLabel59.setToolTipText("");
        jLabel59.setName("jLabel59"); // NOI18N
        FormInput.add(jLabel59);
        jLabel59.setBounds(0, 850, 150, 23);

        jLabel60.setText("Tali pusat:");
        jLabel60.setToolTipText("");
        jLabel60.setName("jLabel60"); // NOI18N
        FormInput.add(jLabel60);
        jLabel60.setBounds(0, 880, 150, 23);

        jLabel61.setText("Punggung:");
        jLabel61.setToolTipText("");
        jLabel61.setName("jLabel61"); // NOI18N
        FormInput.add(jLabel61);
        jLabel61.setBounds(0, 910, 150, 23);

        jLabel63.setText("Anus:");
        jLabel63.setToolTipText("");
        jLabel63.setName("jLabel63"); // NOI18N
        FormInput.add(jLabel63);
        jLabel63.setBounds(0, 970, 150, 23);

        jLabel64.setText("Ekstremnitas:");
        jLabel64.setToolTipText("");
        jLabel64.setName("jLabel64"); // NOI18N
        FormInput.add(jLabel64);
        jLabel64.setBounds(0, 1000, 150, 23);

        jLabel65.setText("Kulit:");
        jLabel65.setToolTipText("");
        jLabel65.setName("jLabel65"); // NOI18N
        FormInput.add(jLabel65);
        jLabel65.setBounds(0, 1030, 150, 23);

        Root.setHighlighter(null);
        Root.setName("Root"); // NOI18N
        FormInput.add(Root);
        Root.setBounds(810, 1090, 64, 23);

        jLabel66.setText("Reflek:");
        jLabel66.setToolTipText("");
        jLabel66.setName("jLabel66"); // NOI18N
        FormInput.add(jLabel66);
        jLabel66.setBounds(0, 1090, 150, 23);

        cbMoro.setBackground(new java.awt.Color(255, 255, 255));
        BtnGrpReflek.add(cbMoro);
        cbMoro.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        cbMoro.setForeground(new java.awt.Color(50, 50, 50));
        cbMoro.setText("Moro");
        cbMoro.setToolTipText("");
        cbMoro.setName("cbMoro"); // NOI18N
        cbMoro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbMoroActionPerformed(evt);
            }
        });
        FormInput.add(cbMoro);
        cbMoro.setBounds(170, 1090, 80, 19);

        cbGrasping.setBackground(new java.awt.Color(255, 255, 255));
        BtnGrpReflek.add(cbGrasping);
        cbGrasping.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        cbGrasping.setForeground(new java.awt.Color(50, 50, 50));
        cbGrasping.setText("Grasping/genggam");
        cbGrasping.setName("cbGrasping"); // NOI18N
        cbGrasping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbGraspingActionPerformed(evt);
            }
        });
        FormInput.add(cbGrasping);
        cbGrasping.setBounds(330, 1090, 120, 19);

        cbSuck.setBackground(new java.awt.Color(255, 255, 255));
        BtnGrpReflek.add(cbSuck);
        cbSuck.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        cbSuck.setForeground(new java.awt.Color(50, 50, 50));
        cbSuck.setText("Sucking/isap");
        cbSuck.setName("cbSuck"); // NOI18N
        cbSuck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSuckActionPerformed(evt);
            }
        });
        FormInput.add(cbSuck);
        cbSuck.setBounds(540, 1090, 90, 19);

        cbRoot.setBackground(new java.awt.Color(255, 255, 255));
        BtnGrpReflek.add(cbRoot);
        cbRoot.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        cbRoot.setForeground(new java.awt.Color(50, 50, 50));
        cbRoot.setText("Rooting");
        cbRoot.setName("cbRoot"); // NOI18N
        cbRoot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbRootActionPerformed(evt);
            }
        });
        FormInput.add(cbRoot);
        cbRoot.setBounds(740, 1090, 70, 19);

        Turgor.setHighlighter(null);
        Turgor.setName("Turgor"); // NOI18N
        FormInput.add(Turgor);
        Turgor.setBounds(260, 1060, 64, 23);

        Moro.setHighlighter(null);
        Moro.setName("Moro"); // NOI18N
        FormInput.add(Moro);
        Moro.setBounds(260, 1090, 64, 23);

        Grasping.setHighlighter(null);
        Grasping.setName("Grasping"); // NOI18N
        FormInput.add(Grasping);
        Grasping.setBounds(460, 1090, 64, 23);

        Suck.setHighlighter(null);
        Suck.setName("Suck"); // NOI18N
        FormInput.add(Suck);
        Suck.setBounds(630, 1090, 64, 23);

        cbTonick.setBackground(new java.awt.Color(255, 255, 255));
        BtnGrpReflek.add(cbTonick);
        cbTonick.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        cbTonick.setForeground(new java.awt.Color(50, 50, 50));
        cbTonick.setText("Tonick neck");
        cbTonick.setToolTipText("");
        cbTonick.setName("cbTonick"); // NOI18N
        cbTonick.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTonickActionPerformed(evt);
            }
        });
        FormInput.add(cbTonick);
        cbTonick.setBounds(170, 1120, 90, 19);

        Tonick.setHighlighter(null);
        Tonick.setName("Tonick"); // NOI18N
        FormInput.add(Tonick);
        Tonick.setBounds(260, 1120, 64, 23);

        cbSwallow.setBackground(new java.awt.Color(255, 255, 255));
        BtnGrpReflek.add(cbSwallow);
        cbSwallow.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        cbSwallow.setForeground(new java.awt.Color(50, 50, 50));
        cbSwallow.setText("Swallowing/menelan");
        cbSwallow.setName("cbSwallow"); // NOI18N
        cbSwallow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSwallowActionPerformed(evt);
            }
        });
        FormInput.add(cbSwallow);
        cbSwallow.setBounds(330, 1120, 130, 19);

        Swallow.setHighlighter(null);
        Swallow.setName("Swallow"); // NOI18N
        FormInput.add(Swallow);
        Swallow.setBounds(460, 1120, 64, 23);

        cbStep.setBackground(new java.awt.Color(255, 255, 255));
        BtnGrpReflek.add(cbStep);
        cbStep.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        cbStep.setForeground(new java.awt.Color(50, 50, 50));
        cbStep.setText("Stepping");
        cbStep.setName("cbStep"); // NOI18N
        cbStep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbStepActionPerformed(evt);
            }
        });
        FormInput.add(cbStep);
        cbStep.setBounds(540, 1120, 90, 19);

        Step.setHighlighter(null);
        Step.setName("Step"); // NOI18N
        FormInput.add(Step);
        Step.setBounds(630, 1120, 64, 23);

        cbBabinski.setBackground(new java.awt.Color(255, 255, 255));
        BtnGrpReflek.add(cbBabinski);
        cbBabinski.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        cbBabinski.setForeground(new java.awt.Color(50, 50, 50));
        cbBabinski.setText("Babinski");
        cbBabinski.setName("cbBabinski"); // NOI18N
        cbBabinski.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbBabinskiActionPerformed(evt);
            }
        });
        FormInput.add(cbBabinski);
        cbBabinski.setBounds(740, 1120, 70, 19);

        Babinski.setHighlighter(null);
        Babinski.setName("Babinski"); // NOI18N
        FormInput.add(Babinski);
        Babinski.setBounds(810, 1120, 64, 23);

        cbLainnya.setBackground(new java.awt.Color(255, 255, 255));
        BtnGrpReflek.add(cbLainnya);
        cbLainnya.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        cbLainnya.setForeground(new java.awt.Color(50, 50, 50));
        cbLainnya.setText("Lainnya");
        cbLainnya.setToolTipText("");
        cbLainnya.setName("cbLainnya"); // NOI18N
        cbLainnya.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbLainnyaActionPerformed(evt);
            }
        });
        FormInput.add(cbLainnya);
        cbLainnya.setBounds(170, 1150, 80, 19);

        Lainnya.setHighlighter(null);
        Lainnya.setName("Lainnya"); // NOI18N
        FormInput.add(Lainnya);
        Lainnya.setBounds(260, 1150, 64, 23);

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
        JamKeluarDerma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JamKeluarDermaActionPerformed(evt);
            }
        });
        FormInput.add(JamKeluarDerma);
        JamKeluarDerma.setBounds(370, 520, 80, 23);

        NmKamar.setHighlighter(null);
        NmKamar.setName("NmKamar"); // NOI18N
        FormInput.add(NmKamar);
        NmKamar.setBounds(520, 580, 220, 23);

        jLabel75.setText("WITA");
        jLabel75.setToolTipText("");
        jLabel75.setName("jLabel75"); // NOI18N
        FormInput.add(jLabel75);
        jLabel75.setBounds(470, 520, 40, 23);

        jLabel76.setText("Tempat:");
        jLabel76.setToolTipText("");
        jLabel76.setName("jLabel76"); // NOI18N
        FormInput.add(jLabel76);
        jLabel76.setBounds(470, 550, 40, 23);

        TempatDerma.setHighlighter(null);
        TempatDerma.setName("TempatDerma"); // NOI18N
        FormInput.add(TempatDerma);
        TempatDerma.setBounds(520, 550, 64, 23);

        BtnStigAtopik.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnStigAtopik.setMnemonic('2');
        BtnStigAtopik.setToolTipText("Alt+2");
        BtnStigAtopik.setName("BtnStigAtopik"); // NOI18N
        BtnStigAtopik.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnStigAtopik.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnStigAtopikActionPerformed(evt);
            }
        });
        BtnStigAtopik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnStigAtopikKeyPressed(evt);
            }
        });
        FormInput.add(BtnStigAtopik);
        BtnStigAtopik.setBounds(840, 250, 28, 23);

        BtnDropStig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); // NOI18N
        BtnDropStig.setMnemonic('2');
        BtnDropStig.setToolTipText("Alt+2");
        BtnDropStig.setName("BtnDropStig"); // NOI18N
        BtnDropStig.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnDropStig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDropStigActionPerformed(evt);
            }
        });
        BtnDropStig.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnDropStigKeyPressed(evt);
            }
        });
        FormInput.add(BtnDropStig);
        BtnDropStig.setBounds(810, 250, 28, 23);

        Syaraf.setHighlighter(null);
        Syaraf.setName("Syaraf"); // NOI18N
        Syaraf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SyarafActionPerformed(evt);
            }
        });
        FormInput.add(Syaraf);
        Syaraf.setBounds(170, 370, 640, 23);

        BtnDropSyaraf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); // NOI18N
        BtnDropSyaraf.setMnemonic('2');
        BtnDropSyaraf.setToolTipText("Alt+2");
        BtnDropSyaraf.setName("BtnDropSyaraf"); // NOI18N
        BtnDropSyaraf.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnDropSyaraf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDropSyarafActionPerformed(evt);
            }
        });
        BtnDropSyaraf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnDropSyarafKeyPressed(evt);
            }
        });
        FormInput.add(BtnDropSyaraf);
        BtnDropSyaraf.setBounds(810, 370, 28, 23);

        BtnSyaraf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnSyaraf.setMnemonic('2');
        BtnSyaraf.setToolTipText("Alt+2");
        BtnSyaraf.setName("BtnSyaraf"); // NOI18N
        BtnSyaraf.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnSyaraf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSyarafActionPerformed(evt);
            }
        });
        BtnSyaraf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSyarafKeyPressed(evt);
            }
        });
        FormInput.add(BtnSyaraf);
        BtnSyaraf.setBounds(840, 370, 28, 23);

        Uub.setHighlighter(null);
        Uub.setName("Uub"); // NOI18N
        Uub.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UubActionPerformed(evt);
            }
        });
        FormInput.add(Uub);
        Uub.setBounds(170, 670, 640, 23);

        BtnDropUub.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); // NOI18N
        BtnDropUub.setMnemonic('2');
        BtnDropUub.setToolTipText("Alt+2");
        BtnDropUub.setName("BtnDropUub"); // NOI18N
        BtnDropUub.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnDropUub.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDropUubActionPerformed(evt);
            }
        });
        BtnDropUub.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnDropUubKeyPressed(evt);
            }
        });
        FormInput.add(BtnDropUub);
        BtnDropUub.setBounds(810, 670, 28, 23);

        BtnUub.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnUub.setMnemonic('2');
        BtnUub.setToolTipText("Alt+2");
        BtnUub.setName("BtnUub"); // NOI18N
        BtnUub.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnUub.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnUubActionPerformed(evt);
            }
        });
        BtnUub.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnUubKeyPressed(evt);
            }
        });
        FormInput.add(BtnUub);
        BtnUub.setBounds(840, 670, 28, 23);

        Kepala.setHighlighter(null);
        Kepala.setName("Kepala"); // NOI18N
        Kepala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KepalaActionPerformed(evt);
            }
        });
        FormInput.add(Kepala);
        Kepala.setBounds(170, 640, 640, 23);

        BtnDropKepala.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); // NOI18N
        BtnDropKepala.setMnemonic('2');
        BtnDropKepala.setToolTipText("Alt+2");
        BtnDropKepala.setName("BtnDropKepala"); // NOI18N
        BtnDropKepala.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnDropKepala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDropKepalaActionPerformed(evt);
            }
        });
        BtnDropKepala.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnDropKepalaKeyPressed(evt);
            }
        });
        FormInput.add(BtnDropKepala);
        BtnDropKepala.setBounds(810, 640, 28, 23);

        BtnKepala.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnKepala.setMnemonic('2');
        BtnKepala.setToolTipText("Alt+2");
        BtnKepala.setName("BtnKepala"); // NOI18N
        BtnKepala.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnKepala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKepalaActionPerformed(evt);
            }
        });
        BtnKepala.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKepalaKeyPressed(evt);
            }
        });
        FormInput.add(BtnKepala);
        BtnKepala.setBounds(840, 640, 28, 23);

        Mata.setHighlighter(null);
        Mata.setName("Mata"); // NOI18N
        Mata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MataActionPerformed(evt);
            }
        });
        FormInput.add(Mata);
        Mata.setBounds(170, 700, 640, 23);

        BtnDropMata.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); // NOI18N
        BtnDropMata.setMnemonic('2');
        BtnDropMata.setToolTipText("Alt+2");
        BtnDropMata.setName("BtnDropMata"); // NOI18N
        BtnDropMata.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnDropMata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDropMataActionPerformed(evt);
            }
        });
        BtnDropMata.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnDropMataKeyPressed(evt);
            }
        });
        FormInput.add(BtnDropMata);
        BtnDropMata.setBounds(810, 700, 28, 23);

        BtnMata.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnMata.setMnemonic('2');
        BtnMata.setToolTipText("Alt+2");
        BtnMata.setName("BtnMata"); // NOI18N
        BtnMata.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnMata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnMataActionPerformed(evt);
            }
        });
        BtnMata.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnMataKeyPressed(evt);
            }
        });
        FormInput.add(BtnMata);
        BtnMata.setBounds(840, 700, 28, 23);

        Tht.setHighlighter(null);
        Tht.setName("Tht"); // NOI18N
        Tht.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ThtActionPerformed(evt);
            }
        });
        FormInput.add(Tht);
        Tht.setBounds(170, 730, 640, 23);

        BtnDropTht.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); // NOI18N
        BtnDropTht.setMnemonic('2');
        BtnDropTht.setToolTipText("Alt+2");
        BtnDropTht.setName("BtnDropTht"); // NOI18N
        BtnDropTht.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnDropTht.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDropThtActionPerformed(evt);
            }
        });
        BtnDropTht.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnDropThtKeyPressed(evt);
            }
        });
        FormInput.add(BtnDropTht);
        BtnDropTht.setBounds(810, 730, 28, 23);

        BtnTht.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnTht.setMnemonic('2');
        BtnTht.setToolTipText("Alt+2");
        BtnTht.setName("BtnTht"); // NOI18N
        BtnTht.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnTht.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnThtActionPerformed(evt);
            }
        });
        BtnTht.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnThtKeyPressed(evt);
            }
        });
        FormInput.add(BtnTht);
        BtnTht.setBounds(840, 730, 28, 23);

        Mulut.setHighlighter(null);
        Mulut.setName("Mulut"); // NOI18N
        Mulut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MulutActionPerformed(evt);
            }
        });
        FormInput.add(Mulut);
        Mulut.setBounds(170, 760, 640, 23);

        BtnDropMulut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); // NOI18N
        BtnDropMulut.setMnemonic('2');
        BtnDropMulut.setToolTipText("Alt+2");
        BtnDropMulut.setName("BtnDropMulut"); // NOI18N
        BtnDropMulut.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnDropMulut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDropMulutActionPerformed(evt);
            }
        });
        BtnDropMulut.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnDropMulutKeyPressed(evt);
            }
        });
        FormInput.add(BtnDropMulut);
        BtnDropMulut.setBounds(810, 760, 28, 23);

        BtnMulut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnMulut.setMnemonic('2');
        BtnMulut.setToolTipText("Alt+2");
        BtnMulut.setName("BtnMulut"); // NOI18N
        BtnMulut.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnMulut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnMulutActionPerformed(evt);
            }
        });
        BtnMulut.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnMulutKeyPressed(evt);
            }
        });
        FormInput.add(BtnMulut);
        BtnMulut.setBounds(840, 760, 28, 23);

        Thorax.setHighlighter(null);
        Thorax.setName("Thorax"); // NOI18N
        Thorax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ThoraxActionPerformed(evt);
            }
        });
        FormInput.add(Thorax);
        Thorax.setBounds(170, 820, 640, 23);

        BtnDropThorax.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); // NOI18N
        BtnDropThorax.setMnemonic('2');
        BtnDropThorax.setToolTipText("Alt+2");
        BtnDropThorax.setName("BtnDropThorax"); // NOI18N
        BtnDropThorax.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnDropThorax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDropThoraxActionPerformed(evt);
            }
        });
        BtnDropThorax.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnDropThoraxKeyPressed(evt);
            }
        });
        FormInput.add(BtnDropThorax);
        BtnDropThorax.setBounds(810, 820, 28, 23);

        BtnThorax.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnThorax.setMnemonic('2');
        BtnThorax.setToolTipText("Alt+2");
        BtnThorax.setName("BtnThorax"); // NOI18N
        BtnThorax.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnThorax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnThoraxActionPerformed(evt);
            }
        });
        BtnThorax.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnThoraxKeyPressed(evt);
            }
        });
        FormInput.add(BtnThorax);
        BtnThorax.setBounds(840, 820, 28, 23);

        Abdomen.setHighlighter(null);
        Abdomen.setName("Abdomen"); // NOI18N
        Abdomen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AbdomenActionPerformed(evt);
            }
        });
        FormInput.add(Abdomen);
        Abdomen.setBounds(170, 850, 640, 23);

        BtnDropAbdomen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); // NOI18N
        BtnDropAbdomen.setMnemonic('2');
        BtnDropAbdomen.setToolTipText("Alt+2");
        BtnDropAbdomen.setName("BtnDropAbdomen"); // NOI18N
        BtnDropAbdomen.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnDropAbdomen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDropAbdomenActionPerformed(evt);
            }
        });
        BtnDropAbdomen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnDropAbdomenKeyPressed(evt);
            }
        });
        FormInput.add(BtnDropAbdomen);
        BtnDropAbdomen.setBounds(810, 850, 28, 23);

        BtnAbdomen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnAbdomen.setMnemonic('2');
        BtnAbdomen.setToolTipText("Alt+2");
        BtnAbdomen.setName("BtnAbdomen"); // NOI18N
        BtnAbdomen.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAbdomen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAbdomenActionPerformed(evt);
            }
        });
        BtnAbdomen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAbdomenKeyPressed(evt);
            }
        });
        FormInput.add(BtnAbdomen);
        BtnAbdomen.setBounds(840, 850, 28, 23);

        TaliPusat.setHighlighter(null);
        TaliPusat.setName("TaliPusat"); // NOI18N
        TaliPusat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TaliPusatActionPerformed(evt);
            }
        });
        FormInput.add(TaliPusat);
        TaliPusat.setBounds(170, 880, 640, 23);

        BtnDropTaliPusat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); // NOI18N
        BtnDropTaliPusat.setMnemonic('2');
        BtnDropTaliPusat.setToolTipText("Alt+2");
        BtnDropTaliPusat.setName("BtnDropTaliPusat"); // NOI18N
        BtnDropTaliPusat.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnDropTaliPusat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDropTaliPusatActionPerformed(evt);
            }
        });
        BtnDropTaliPusat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnDropTaliPusatKeyPressed(evt);
            }
        });
        FormInput.add(BtnDropTaliPusat);
        BtnDropTaliPusat.setBounds(810, 880, 28, 23);

        BtnTaliPusat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnTaliPusat.setMnemonic('2');
        BtnTaliPusat.setToolTipText("Alt+2");
        BtnTaliPusat.setName("BtnTaliPusat"); // NOI18N
        BtnTaliPusat.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnTaliPusat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTaliPusatActionPerformed(evt);
            }
        });
        BtnTaliPusat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnTaliPusatKeyPressed(evt);
            }
        });
        FormInput.add(BtnTaliPusat);
        BtnTaliPusat.setBounds(840, 880, 28, 23);

        Punggung.setHighlighter(null);
        Punggung.setName("Punggung"); // NOI18N
        Punggung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PunggungActionPerformed(evt);
            }
        });
        FormInput.add(Punggung);
        Punggung.setBounds(170, 910, 640, 23);

        BtnDropPunggung.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); // NOI18N
        BtnDropPunggung.setMnemonic('2');
        BtnDropPunggung.setToolTipText("Alt+2");
        BtnDropPunggung.setName("BtnDropPunggung"); // NOI18N
        BtnDropPunggung.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnDropPunggung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDropPunggungActionPerformed(evt);
            }
        });
        BtnDropPunggung.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnDropPunggungKeyPressed(evt);
            }
        });
        FormInput.add(BtnDropPunggung);
        BtnDropPunggung.setBounds(810, 910, 28, 23);

        BtnPunggung.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnPunggung.setMnemonic('2');
        BtnPunggung.setToolTipText("Alt+2");
        BtnPunggung.setName("BtnPunggung"); // NOI18N
        BtnPunggung.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPunggung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPunggungActionPerformed(evt);
            }
        });
        BtnPunggung.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPunggungKeyPressed(evt);
            }
        });
        FormInput.add(BtnPunggung);
        BtnPunggung.setBounds(840, 910, 28, 23);

        Genetalia.setHighlighter(null);
        Genetalia.setName("Genetalia"); // NOI18N
        Genetalia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GenetaliaActionPerformed(evt);
            }
        });
        FormInput.add(Genetalia);
        Genetalia.setBounds(440, 940, 370, 23);

        Ekstremnitas.setHighlighter(null);
        Ekstremnitas.setName("Ekstremnitas"); // NOI18N
        Ekstremnitas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EkstremnitasActionPerformed(evt);
            }
        });
        FormInput.add(Ekstremnitas);
        Ekstremnitas.setBounds(170, 1000, 640, 23);

        BtnDropEkstremnitas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); // NOI18N
        BtnDropEkstremnitas.setMnemonic('2');
        BtnDropEkstremnitas.setToolTipText("Alt+2");
        BtnDropEkstremnitas.setName("BtnDropEkstremnitas"); // NOI18N
        BtnDropEkstremnitas.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnDropEkstremnitas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDropEkstremnitasActionPerformed(evt);
            }
        });
        BtnDropEkstremnitas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnDropEkstremnitasKeyPressed(evt);
            }
        });
        FormInput.add(BtnDropEkstremnitas);
        BtnDropEkstremnitas.setBounds(810, 1000, 28, 23);

        BtnEkstremnitas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnEkstremnitas.setMnemonic('2');
        BtnEkstremnitas.setToolTipText("Alt+2");
        BtnEkstremnitas.setName("BtnEkstremnitas"); // NOI18N
        BtnEkstremnitas.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnEkstremnitas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEkstremnitasActionPerformed(evt);
            }
        });
        BtnEkstremnitas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnEkstremnitasKeyPressed(evt);
            }
        });
        FormInput.add(BtnEkstremnitas);
        BtnEkstremnitas.setBounds(840, 1000, 28, 23);

        Kulit.setHighlighter(null);
        Kulit.setName("Kulit"); // NOI18N
        Kulit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KulitActionPerformed(evt);
            }
        });
        FormInput.add(Kulit);
        Kulit.setBounds(170, 1030, 640, 23);

        BtnDropKulit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); // NOI18N
        BtnDropKulit.setMnemonic('2');
        BtnDropKulit.setToolTipText("Alt+2");
        BtnDropKulit.setName("BtnDropKulit"); // NOI18N
        BtnDropKulit.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnDropKulit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDropKulitActionPerformed(evt);
            }
        });
        BtnDropKulit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnDropKulitKeyPressed(evt);
            }
        });
        FormInput.add(BtnDropKulit);
        BtnDropKulit.setBounds(810, 1030, 28, 23);

        BtnKulit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnKulit.setMnemonic('2');
        BtnKulit.setToolTipText("Alt+2");
        BtnKulit.setName("BtnKulit"); // NOI18N
        BtnKulit.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnKulit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKulitActionPerformed(evt);
            }
        });
        BtnKulit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKulitKeyPressed(evt);
            }
        });
        FormInput.add(BtnKulit);
        BtnKulit.setBounds(840, 1030, 28, 23);

        jLabel77.setText("Mukosa: warna");
        jLabel77.setToolTipText("");
        jLabel77.setName("jLabel77"); // NOI18N
        FormInput.add(jLabel77);
        jLabel77.setBounds(170, 790, 80, 23);

        BtnDropJamDisposisi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); // NOI18N
        BtnDropJamDisposisi.setMnemonic('2');
        BtnDropJamDisposisi.setToolTipText("Alt+2");
        BtnDropJamDisposisi.setName("BtnDropJamDisposisi"); // NOI18N
        BtnDropJamDisposisi.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnDropJamDisposisi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDropJamDisposisiActionPerformed(evt);
            }
        });
        BtnDropJamDisposisi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnDropJamDisposisiKeyPressed(evt);
            }
        });
        FormInput.add(BtnDropJamDisposisi);
        BtnDropJamDisposisi.setBounds(450, 520, 28, 23);

        TglAsuhan.setForeground(new java.awt.Color(50, 70, 50));
        TglAsuhan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "23-11-2023 13:26:58" }));
        TglAsuhan.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        TglAsuhan.setName("TglAsuhan"); // NOI18N
        TglAsuhan.setOpaque(false);
        TglAsuhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TglAsuhanActionPerformed(evt);
            }
        });
        TglAsuhan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TglAsuhanKeyPressed(evt);
            }
        });
        FormInput.add(TglAsuhan);
        TglAsuhan.setBounds(456, 40, 130, 23);

        BtnKamar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnKamar.setMnemonic('2');
        BtnKamar.setToolTipText("Alt+2");
        BtnKamar.setName("BtnKamar"); // NOI18N
        BtnKamar.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnKamar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKamarActionPerformed(evt);
            }
        });
        BtnKamar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKamarKeyPressed(evt);
            }
        });
        FormInput.add(BtnKamar);
        BtnKamar.setBounds(740, 580, 28, 23);

        BtnShowJamNeo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); // NOI18N
        BtnShowJamNeo.setMnemonic('2');
        BtnShowJamNeo.setToolTipText("Alt+2");
        BtnShowJamNeo.setName("BtnShowJamNeo"); // NOI18N
        BtnShowJamNeo.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnShowJamNeo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnShowJamNeoActionPerformed(evt);
            }
        });
        BtnShowJamNeo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnShowJamNeoKeyPressed(evt);
            }
        });
        FormInput.add(BtnShowJamNeo);
        BtnShowJamNeo.setBounds(450, 1300, 28, 23);

        TglDerma1.setForeground(new java.awt.Color(50, 70, 50));
        TglDerma1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "23-11-2023" }));
        TglDerma1.setDisplayFormat("dd-MM-yyyy");
        TglDerma1.setName("TglDerma1"); // NOI18N
        TglDerma1.setOpaque(false);
        TglDerma1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TglDerma1ActionPerformed(evt);
            }
        });
        TglDerma1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TglDerma1KeyPressed(evt);
            }
        });
        FormInput.add(TglDerma1);
        TglDerma1.setBounds(370, 1330, 80, 23);

        NoBed.setHighlighter(null);
        NoBed.setName("NoBed"); // NOI18N
        FormInput.add(NoBed);
        NoBed.setBounds(370, 580, 80, 23);

        KdKamar.setHighlighter(null);
        KdKamar.setName("KdKamar"); // NOI18N
        FormInput.add(KdKamar);
        KdKamar.setBounds(450, 580, 70, 23);

        NoBed1.setHighlighter(null);
        NoBed1.setName("NoBed1"); // NOI18N
        FormInput.add(NoBed1);
        NoBed1.setBounds(370, 1360, 80, 23);

        KdKamar1.setHighlighter(null);
        KdKamar1.setName("KdKamar1"); // NOI18N
        FormInput.add(KdKamar1);
        KdKamar1.setBounds(450, 1360, 70, 23);

        NmKamar1.setHighlighter(null);
        NmKamar1.setName("NmKamar1"); // NOI18N
        FormInput.add(NmKamar1);
        NmKamar1.setBounds(520, 1360, 220, 23);

        BtnKamar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnKamar1.setMnemonic('2');
        BtnKamar1.setToolTipText("Alt+2");
        BtnKamar1.setName("BtnKamar1"); // NOI18N
        BtnKamar1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnKamar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKamar1ActionPerformed(evt);
            }
        });
        BtnKamar1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKamar1KeyPressed(evt);
            }
        });
        FormInput.add(BtnKamar1);
        BtnKamar1.setBounds(740, 1360, 28, 23);

        rbHiperhidrosis.setBackground(new java.awt.Color(255, 255, 255));
        BtnGrpFungsiKelenjarKeringat.add(rbHiperhidrosis);
        rbHiperhidrosis.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        rbHiperhidrosis.setForeground(new java.awt.Color(50, 50, 50));
        rbHiperhidrosis.setText("Hiperhidrosis");
        rbHiperhidrosis.setActionCommand("Hiperhidrosis");
        rbHiperhidrosis.setName("rbHiperhidrosis"); // NOI18N
        rbHiperhidrosis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbHiperhidrosisActionPerformed(evt);
            }
        });
        FormInput.add(rbHiperhidrosis);
        rbHiperhidrosis.setBounds(170, 310, 98, 19);

        rbAnshidrosis.setBackground(new java.awt.Color(255, 255, 255));
        BtnGrpFungsiKelenjarKeringat.add(rbAnshidrosis);
        rbAnshidrosis.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        rbAnshidrosis.setForeground(new java.awt.Color(50, 50, 50));
        rbAnshidrosis.setText("Anshidrosis");
        rbAnshidrosis.setName("rbAnshidrosis"); // NOI18N
        rbAnshidrosis.setActionCommand("Anshidrosis");
        rbAnshidrosis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbAnshidrosisActionPerformed(evt);
            }
        });
        FormInput.add(rbAnshidrosis);
        rbAnshidrosis.setBounds(320, 310, 110, 19);

        rbBolehPulangDisposisi.setBackground(new java.awt.Color(255, 255, 255));
        BtnGrpDisposisiDerma.add(rbBolehPulangDisposisi);
        rbBolehPulangDisposisi.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        rbBolehPulangDisposisi.setForeground(new java.awt.Color(50, 50, 50));
        rbBolehPulangDisposisi.setText("Boleh pulang");
        rbBolehPulangDisposisi.setActionCommand("Boleh Pulang");
        rbBolehPulangDisposisi.setName("rbBolehPulangDisposisi"); // NOI18N
        rbBolehPulangDisposisi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbBolehPulangDisposisiActionPerformed(evt);
            }
        });
        FormInput.add(rbBolehPulangDisposisi);
        rbBolehPulangDisposisi.setBounds(170, 520, 98, 21);

        rbKontrolDisposisi.setBackground(new java.awt.Color(255, 255, 255));
        BtnGrpDisposisiDerma.add(rbKontrolDisposisi);
        rbKontrolDisposisi.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        rbKontrolDisposisi.setForeground(new java.awt.Color(50, 50, 50));
        rbKontrolDisposisi.setText("Kontrol");
        rbKontrolDisposisi.setActionCommand("Kontrol");
        rbKontrolDisposisi.setName("rbKontrolDisposisi"); // NOI18N
        rbKontrolDisposisi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbKontrolDisposisiActionPerformed(evt);
            }
        });
        FormInput.add(rbKontrolDisposisi);
        rbKontrolDisposisi.setBounds(170, 550, 90, 19);

        rbDirawatDisposisi.setBackground(new java.awt.Color(255, 255, 255));
        BtnGrpDisposisiDerma.add(rbDirawatDisposisi);
        rbDirawatDisposisi.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        rbDirawatDisposisi.setForeground(new java.awt.Color(50, 50, 50));
        rbDirawatDisposisi.setText("Dirawat");
        rbDirawatDisposisi.setActionCommand("Dirawat");
        rbDirawatDisposisi.setName("rbDirawatDisposisi"); // NOI18N
        rbDirawatDisposisi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbDirawatDisposisiActionPerformed(evt);
            }
        });
        FormInput.add(rbDirawatDisposisi);
        rbDirawatDisposisi.setBounds(170, 580, 98, 21);

        rbAda.setActionCommand("Ada");
        rbAda.setBackground(new java.awt.Color(255, 255, 255));
        BtnGrpAnus.add(rbAda);
        rbAda.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        rbAda.setForeground(new java.awt.Color(50, 50, 50));
        rbAda.setText("Ada");
        rbAda.setName("rbAda"); // NOI18N
        rbAda.setActionCommand("Ada");
        rbAda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbAdaActionPerformed(evt);
            }
        });
        FormInput.add(rbAda);
        rbAda.setBounds(170, 970, 98, 21);

        rbTidakAda.setActionCommand("Tidak ada");
        rbTidakAda.setBackground(new java.awt.Color(255, 255, 255));
        BtnGrpAnus.add(rbTidakAda);
        rbTidakAda.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        rbTidakAda.setForeground(new java.awt.Color(50, 50, 50));
        rbTidakAda.setText("Tidak ada");
        rbTidakAda.setName("rbTidakAda"); // NOI18N
        rbTidakAda.setActionCommand("Tidak ada");
        rbTidakAda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbTidakAdaActionPerformed(evt);
            }
        });
        FormInput.add(rbTidakAda);
        rbTidakAda.setBounds(330, 970, 80, 19);

        rbBolehPulangNeona.setBackground(new java.awt.Color(255, 255, 255));
        BtnGrpDisposisiNeona.add(rbBolehPulangNeona);
        rbBolehPulangNeona.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        rbBolehPulangNeona.setForeground(new java.awt.Color(50, 50, 50));
        rbBolehPulangNeona.setText("Boleh pulang");
        rbBolehPulangNeona.setName("rbBolehPulangNeona"); // NOI18N
        rbBolehPulangNeona.setActionCommand("Boleh pulang");
        rbBolehPulangNeona.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbBolehPulangNeonaActionPerformed(evt);
            }
        });
        FormInput.add(rbBolehPulangNeona);
        rbBolehPulangNeona.setBounds(170, 1300, 98, 21);

        rbKontrolNeona.setBackground(new java.awt.Color(255, 255, 255));
        BtnGrpDisposisiNeona.add(rbKontrolNeona);
        rbKontrolNeona.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        rbKontrolNeona.setForeground(new java.awt.Color(50, 50, 50));
        rbKontrolNeona.setText("Kontrol");
        rbKontrolNeona.setName("rbKontrolNeona"); // NOI18N
        rbKontrolNeona.setActionCommand("Kontrol");
        rbKontrolNeona.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbKontrolNeonaActionPerformed(evt);
            }
        });
        FormInput.add(rbKontrolNeona);
        rbKontrolNeona.setBounds(170, 1330, 80, 19);

        rbDirawatNeona.setBackground(new java.awt.Color(255, 255, 255));
        BtnGrpDisposisiNeona.add(rbDirawatNeona);
        rbDirawatNeona.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        rbDirawatNeona.setForeground(new java.awt.Color(50, 50, 50));
        rbDirawatNeona.setText("Dirawat");
        rbDirawatNeona.setName("rbDirawatNeona"); // NOI18N
        rbDirawatNeona.setActionCommand("Dirawat");
        rbDirawatNeona.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbDirawatNeonaActionPerformed(evt);
            }
        });
        FormInput.add(rbDirawatNeona);
        rbDirawatNeona.setBounds(170, 1360, 98, 21);

        jLabel78.setText("Genetalia:");
        jLabel78.setToolTipText("");
        jLabel78.setName("jLabel78"); // NOI18N
        FormInput.add(jLabel78);
        jLabel78.setBounds(0, 940, 150, 23);

        rbKelainan.setBackground(new java.awt.Color(255, 255, 255));
        BtnGrpGenetalia.add(rbKelainan);
        rbKelainan.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        rbKelainan.setForeground(new java.awt.Color(50, 50, 50));
        rbKelainan.setText("Kelainan:");
        rbKelainan.setName("rbKelainan"); // NOI18N
        rbKelainan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbKelainanActionPerformed(evt);
            }
        });
        FormInput.add(rbKelainan);
        rbKelainan.setBounds(330, 940, 98, 21);

        rbNormal.setBackground(new java.awt.Color(255, 255, 255));
        BtnGrpGenetalia.add(rbNormal);
        rbNormal.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        rbNormal.setForeground(new java.awt.Color(50, 50, 50));
        rbNormal.setText("Normal");
        rbNormal.setName("rbNormal"); // NOI18N
        rbNormal.setActionCommand("Normal");
        rbNormal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbNormalActionPerformed(evt);
            }
        });
        FormInput.add(rbNormal);
        rbNormal.setBounds(170, 940, 100, 19);

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
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "23-11-2023" }));
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
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "23-11-2023" }));
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

        scrollPane6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 254)), "Asesmen Awal Medis Kulit Kelamin", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
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
        scrollPane6.getAccessibleContext().setAccessibleDescription("");

        PanelAccor.add(FormMasalahRencana, java.awt.BorderLayout.CENTER);

        internalFrame3.add(PanelAccor, java.awt.BorderLayout.EAST);

        TabRawat.addTab("Data Penilaian", internalFrame3);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        fkelenjarkeringat = BtnGrpFungsiKelenjarKeringat.getSelection().getActionCommand();
        disposisiderma = BtnGrpDisposisiDerma.getSelection().getActionCommand();
        anus = BtnGrpAnus.getSelection().getActionCommand();
        reflek = BtnGrpReflek.getSelection().getActionCommand();
        disposisineona = BtnGrpDisposisiNeona.getSelection().getActionCommand();
        
        
        System.out.println(BtnGrpDisposisiNeona.getSelection().getActionCommand());
        System.out.println(BtnGrpAnus.getSelection().getActionCommand());
        
        
        
        if(TNoRM.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"Nama Pasien");
//        }else if(RiwPenyakitSkrg.getText().trim().equals("")){
//            Valid.textKosong(RiwPenyakitSkrg,"Riwayat Penyakit Sekarang");
//        }else if(Eflorisensi.getText().trim().equals("")){
//            Valid.textKosong(Eflorisensi,"Eflorisensi");
//        }else if(Lokalisasi.getText().trim().equals("")){
//            Valid.textKosong(Lokalisasi,"Lokalisasi");
        }else{
            if(Sequel.menyimpantf("tb_asesmen_awal_medis_kulit_kelamin",
                    "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                    + "","No.Rawat",33,new String[]{
                    TNoRw.getText(), KdPetugas.getText(),
                        Valid.SetTgl(TglAsuhan.getSelectedItem()+"")+" "+TglAsuhan.getSelectedItem().toString().substring(11,19),
                        KeluhanUtama.getText(), RiwPenyakitSkrg.getText(),
                        Lokalisasi.getText(),
                        Eflorisensi.getText(),
                        Mukosa.getText(),Rambut.getText(),Kuku.getText(),
                        fkelenjarkeringat, KelenjarLimfe.getText(),
                        HasilPenunjangDerma.getText(),DiagnosaKerjaDerma.getText(),
                        RencanaKerjaDerma.getText(), TerapiDerma.getText(),
                        disposisiderma,
                        JamKeluarDerma.getText(),
                        Valid.SetTgl(TglDerma.getSelectedItem().toString()),
                        TempatDerma.getText(),
                        NoBed.getText(),
                        WarnaMukosa.getText(),anus, 
                        Turgor.getText(),
                        HasilPenunjangNeonatologi.getText(), DiagnosaKerjaNeonatologi.getText(),
                        RencanaKerjaNeonatologi.getText(), TerapiNeonatologi.getText(),
                        disposisineona, 
                        JamNeonatologi.getText(), 
                        Valid.SetTgl(TglDerma1.getSelectedItem().toString()),
                        TempatNeonatologi.getText(), 
                        NoBed1.getText()
                })==true){
                
                try {
                    if(!idstiglist.isEmpty()){
                        stig = (PreparedStatement) koneksi.prepareStatement(""
                            + "insert into tb_det_ases_kul_kel_stig_atopik(no_rawat, id_stigmata_atopik)"
                            + "values(?,?)");
                    
                        for(int a=0; a<idstiglist.size(); a++){
                            stig.setString(1,TNoRw.getText());
                            stig.setString(2,idstiglist.get(a).toString());
                            stig.executeUpdate();
                        }
                    }
                    
                    if(!idsyaraflist.isEmpty()){
                        syaraf = (PreparedStatement) koneksi.prepareStatement(""
                            + "insert into tb_det_ases_kul_kel_syaraf(no_rawat, id_syaraf)"
                            + "values(?,?)");
                    
                        for(int a=0; a<idsyaraflist.size(); a++){
                            syaraf.setString(1,TNoRw.getText());
                            syaraf.setString(2,idsyaraflist.get(a).toString());
                            syaraf.executeUpdate();
                        }
                    }
                    
                    if(!idkepalalist.isEmpty()){
                        kepala = (PreparedStatement) koneksi.prepareStatement(""
                            + "insert into tb_det_ases_kul_kel_kepala(no_rawat, id_kepala)"
                            + "values(?,?)");
                    
                        for(int a=0; a<idkepalalist.size(); a++){
                            kepala.setString(1,TNoRw.getText());
                            kepala.setString(2,idkepalalist.get(a).toString());
                            kepala.executeUpdate();
                        }
                    }
                    
                    if(!iduublist.isEmpty()){
                        uub = (PreparedStatement) koneksi.prepareStatement(""
                            + "insert into tb_det_ases_kul_kel_uub(no_rawat, id_uub)"
                            + "values(?,?)");
                    
                        for(int a=0; a<iduublist.size(); a++){
                            uub.setString(1,TNoRw.getText());
                            uub.setString(2,iduublist.get(a).toString());
                            uub.executeUpdate();
                        }
                    }
                    
                    if(!idmatalist.isEmpty()){
                        mata = (PreparedStatement) koneksi.prepareStatement(""
                            + "insert into tb_det_ases_kul_kel_mata(no_rawat, id_mata)"
                            + "values(?,?)");
                    
                        for(int a=0; a<idmatalist.size(); a++){
                            mata.setString(1,TNoRw.getText());
                            mata.setString(2,idmatalist.get(a).toString());
                            mata.executeUpdate();
                        }
                    }
                    
                    if(!idthtlist.isEmpty()){
                        tht = (PreparedStatement) koneksi.prepareStatement(""
                            + "insert into tb_det_ases_kul_kel_tht(no_rawat, id_tht)"
                            + "values(?,?)");
                    
                        for(int a=0; a<idthtlist.size(); a++){
                            tht.setString(1,TNoRw.getText());
                            tht.setString(2,idthtlist.get(a).toString());
                            tht.executeUpdate();
                        }
                    }
                    
                    if(!idmulutlist.isEmpty()){
                        mulut = (PreparedStatement) koneksi.prepareStatement(""
                            + "insert into tb_det_ases_kul_kel_mulut(no_rawat, id_mulut)"
                            + "values(?,?)");
                    
                        for(int a=0; a<idmulutlist.size(); a++){
                            mulut.setString(1,TNoRw.getText());
                            mulut.setString(2,idmulutlist.get(a).toString());
                            mulut.executeUpdate();
                        }
                    }
                    
                    if(!idthoraxlist.isEmpty()){
                        thorax = (PreparedStatement) koneksi.prepareStatement(""
                            + "insert into tb_det_ases_kul_kel_thorax(no_rawat, id_thorax)"
                            + "values(?,?)");
                    
                        for(int a=0; a<idthoraxlist.size(); a++){
                            thorax.setString(1,TNoRw.getText());
                            thorax.setString(2,idthoraxlist.get(a).toString());
                            thorax.executeUpdate();
                        }
                    }
                    
                    if(!idabdomenlist.isEmpty()){
                        abdomen = (PreparedStatement) koneksi.prepareStatement(""
                            + "insert into tb_det_ases_kul_kel_abdomen(no_rawat, id_abdomen)"
                            + "values(?,?)");
                    
                        for(int a=0; a<idabdomenlist.size(); a++){
                            abdomen.setString(1,TNoRw.getText());
                            abdomen.setString(2,idabdomenlist.get(a).toString());
                            abdomen.executeUpdate();
                        }
                    }
                    
                    if(!idtalipusatlist.isEmpty()){
                        tpusat = (PreparedStatement) koneksi.prepareStatement(""
                            + "insert into tb_det_ases_kul_kel_tpusat(no_rawat, id_tali_pusat)"
                            + "values(?,?)");
                    
                        for(int a=0; a<idtalipusatlist.size(); a++){
                            tpusat.setString(1,TNoRw.getText());
                            tpusat.setString(2,idtalipusatlist.get(a).toString());
                            tpusat.executeUpdate();
                        }
                    }
                    
                    if(!idpunggunglist.isEmpty()){
                        punggung = (PreparedStatement) koneksi.prepareStatement(""
                            + "insert into tb_det_ases_kul_kel_punggung(no_rawat, id_punggung)"
                            + "values(?,?)");
                    
                        for(int a=0; a<idpunggunglist.size(); a++){
                            punggung.setString(1,TNoRw.getText());
                            punggung.setString(2,idpunggunglist.get(a).toString());
                            punggung.executeUpdate();
                        }
                    }
                    
                    if(!idekstremnitaslist.isEmpty()){
                        ekstrem = (PreparedStatement) koneksi.prepareStatement(""
                            + "insert into tb_det_ases_kul_kel_ekstrem(no_rawat, id_ekstrem)"
                            + "values(?,?)");
                    
                        for(int a=0; a<idekstremnitaslist.size(); a++){
                            ekstrem.setString(1,TNoRw.getText());
                            ekstrem.setString(2,idekstremnitaslist.get(a).toString());
                            ekstrem.executeUpdate();
                        }
                    }
                    
                    if(!idkulitlist.isEmpty()){
                        kulit = (PreparedStatement) koneksi.prepareStatement(""
                            + "insert into tb_det_ases_kul_kel_kulit(no_rawat, id_kulit)"
                            + "values(?,?)");
                    
                        for(int a=0; a<idkulitlist.size(); a++){
                            kulit.setString(1,TNoRw.getText());
                            kulit.setString(2,idkulitlist.get(a).toString());
                            kulit.executeUpdate();
                        }
                    }
                    
                    System.out.println(BtnGrpGenetalia.getSelection().getActionCommand());
                    if(BtnGrpGenetalia.getSelection().getActionCommand()=="Kelainan:"){
                        genet = (PreparedStatement) koneksi.prepareStatement(""
                            + "insert into tb_det_kelainan_genetalia(no_rawat, kelainan_genetalia)"
                            + "values(?,?)");
                    
//                        for(int a=0; a<idkulitlist.size(); a++){
                            genet.setString(1,TNoRw.getText());
                            genet.setString(2,Genetalia.getText());
                            genet.executeUpdate();
//                        }
                    } else {
                        genet = (PreparedStatement) koneksi.prepareStatement(""
                            + "insert into tb_det_kelainan_genetalia(no_rawat, kelainan_genetalia)"
                            + "values(?,?)");
                        
                        genet.setString(1,TNoRw.getText());
                        genet.setString(2,"Normal");
                        genet.executeUpdate();
                    }
                    
                    
                } catch (SQLException ex) {
                    System.out.println("Notifikasi stig : "+ex);
                    ex.printStackTrace();
                    Logger.getLogger(RMAsesmenAwalMedisKulitKelamin.class.getName()).log(Level.SEVERE, null, ex);
                }
                
//              if(Sequel.menyimpantf("tb_det_ases_kul_kel_stig_atopik","?,?,?,?" + "",
//                        "id", 4, new String[]{
//                            "", 
//                        }

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
            
                Sequel.queryu2tf("delete from tb_det_ases_kul_kel_stig_atopik where no_rawat=?",1,
                        new String[]{tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()});
                Sequel.queryu2tf("delete from tb_det_ases_kul_kel_syaraf where no_rawat=?",1,
                        new String[]{tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()});
                Sequel.queryu2tf("delete from tb_det_ases_kul_kel_kepala where no_rawat=?",1,
                        new String[]{tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()});
                Sequel.queryu2tf("delete from tb_det_ases_kul_kel_uub where no_rawat=?",1,
                        new String[]{tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()});
                Sequel.queryu2tf("delete from tb_det_ases_kul_kel_mata where no_rawat=?",1,
                        new String[]{tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()});
                Sequel.queryu2tf("delete from tb_det_ases_kul_kel_tht where no_rawat=?",1,
                        new String[]{tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()});
                Sequel.queryu2tf("delete from tb_det_ases_kul_kel_mulut where no_rawat=?",1,
                        new String[]{tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()});
                Sequel.queryu2tf("delete from tb_det_ases_kul_kel_thorax where no_rawat=?",1,
                        new String[]{tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()});
                Sequel.queryu2tf("delete from tb_det_ases_kul_kel_abdomen where no_rawat=?",1,
                        new String[]{tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()});
                Sequel.queryu2tf("delete from tb_det_ases_kul_kel_tpusat where no_rawat=?",1,
                        new String[]{tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()});
                Sequel.queryu2tf("delete from tb_det_ases_kul_kel_punggung where no_rawat=?",1,
                        new String[]{tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()});
                Sequel.queryu2tf("delete from tb_det_ases_kul_kel_ekstrem where no_rawat=?",1,
                        new String[]{tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()});
                Sequel.queryu2tf("delete from tb_det_ases_kul_kel_kulit where no_rawat=?",1,
                        new String[]{tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()});
                Sequel.queryu2tf("delete from tb_det_kelainan_genetalia where no_rawat=?",1,
                        new String[]{tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()});
                Sequel.queryu2tf("delete from tb_asesmen_awal_medis_kulit_kelamin where no_rawat=?",1,
                        new String[]{tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()});
                
                TNoRM1.setText("");
                TPasien1.setText("");
                Valid.tabelKosong(tabModeDetailMasalah);
                ChkAccor.setSelected(false);
                isMenu();
                tampil();
                emptTeks();
            
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
        fkelenjarkeringat = BtnGrpFungsiKelenjarKeringat.getSelection().getActionCommand();
        disposisiderma = BtnGrpDisposisiDerma.getSelection().getActionCommand();
        anus = BtnGrpAnus.getSelection().getActionCommand();
        reflek = BtnGrpReflek.getSelection().getActionCommand();
        disposisineona = BtnGrpDisposisiNeona.getSelection().getActionCommand();
        rbHiperhidrosis.isSelected();
        
        
        if(TNoRM.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"Nama Pasien");
//        }else 
//            if(Eflorisensi.getText().trim().equals("")){
//            Valid.textKosong(Eflorisensi,"Ginjal Kanan");
//        }else if(HasilPenunjangNeonatologi.getText().trim().equals("")){
//            Valid.textKosong(HasilPenunjangNeonatologi,"Ginjal Kiri");
        }else{
            if(tbObat.getSelectedRow()>-1){
                if(Sequel.mengedittf("tb_asesmen_awal_medis_kulit_kelamin",
                        "no_rawat=?","no_rawat=?,kd_dokter=?,tanggal=?,keluhan_utama=?,"
                                + "riwayat_penyakit_sekarang=?,lokalisasi=?,eflorisensi=?,"
                                + "mukosa=?,rambut=?,kuku=?,fungsi_kelenjar_keringat=?,"
                                + "kelenjar_limfe=?,hasil_penunjang_derma=?,diag_kerja_derma=?,"
                                + "rencana_kerja_derma=?,terapi_derma=?,jam_disposisi_derma=?,"
                                + "tgl_disposisi_derma=?,tempat_disposisi_derma=?,ruang_disposisi_derma=?,"
                                + "warna_mukosa=?,anus=?,turgor=?,hasil_penunjang_neonatologi=?,"
                                + "diag_kerja_neonatologi=?,rencana_kerja_neonatologi=?,"
                                + "terapi_neonatologi=?,disposisi_neonatologi=?,jam_disposisi_neona=?,"
                                + "tgl_disposisi_neonatologi=?,tempat_disposisi_neonatologi=?,ruang_disposisi_neonatologi=?",
                        33,new String[]{
                    TNoRw.getText(), KdPetugas.getText(),
                        Valid.SetTgl(TglAsuhan.getSelectedItem()+"")+" "+TglAsuhan.getSelectedItem().toString().substring(11,19),
                        KeluhanUtama.getText(), RiwPenyakitSkrg.getText(),
                        Lokalisasi.getText(),
                        Eflorisensi.getText(),
                        Mukosa.getText(),Rambut.getText(),Kuku.getText(),
                        fkelenjarkeringat, KelenjarLimfe.getText(),
                        HasilPenunjangDerma.getText(),DiagnosaKerjaDerma.getText(),
                        RencanaKerjaDerma.getText(), TerapiDerma.getText(),
                        disposisiderma,
                        JamKeluarDerma.getText(),
                        Valid.SetTgl(TglDerma.getSelectedItem().toString()),
                        TempatDerma.getText(),
                        NoBed.getText(),
                        WarnaMukosa.getText(),anus, 
                        Turgor.getText(),
                        HasilPenunjangNeonatologi.getText(), DiagnosaKerjaNeonatologi.getText(),
                        RencanaKerjaNeonatologi.getText(), TerapiNeonatologi.getText(),
                        disposisineona, 
                        JamNeonatologi.getText(), 
                        Valid.SetTgl(TglDerma1.getSelectedItem().toString()),
                        TempatNeonatologi.getText(), 
                        NoBed1.getText(),TNoRw.getText()
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
//        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
//            BtnKeluarActionPerformed(null);
//        }else{Valid.pindah(evt,BtnEdit,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed

        if(tbObat.getSelectedRow()== -1){
               JOptionPane.showMessageDialog(null,"Maaf, silahkan pilih data terlebih dahulu...!!!!"); 
        }else{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); 
            getData();
            tgl_asuhan = Valid.SetTgl(TglDerma.getSelectedItem()+"")+" "+TglDerma.getSelectedItem().toString().substring(11,19);
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

    private void TglDermaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TglDermaKeyPressed
//        Valid.pindah2(evt,Rencana,RPD);
    }//GEN-LAST:event_TglDermaKeyPressed

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

    private void cbMoroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMoroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbMoroActionPerformed

    private void cbGraspingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbGraspingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbGraspingActionPerformed

    private void cbSuckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSuckActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbSuckActionPerformed

    private void cbRootActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRootActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbRootActionPerformed

    private void cbTonickActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTonickActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbTonickActionPerformed

    private void cbSwallowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSwallowActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbSwallowActionPerformed

    private void cbStepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStepActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbStepActionPerformed

    private void cbBabinskiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbBabinskiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbBabinskiActionPerformed

    private void cbLainnyaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbLainnyaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbLainnyaActionPerformed

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

    private void StigAtopikActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StigAtopikActionPerformed
        // TODO add your handling code here:
        tampilStigAtopik();
        
    }//GEN-LAST:event_StigAtopikActionPerformed

    private void BtnStigAtopikActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnStigAtopikActionPerformed
        // TODO add your handling code here:
        stigatopik.isCek();
        stigatopik.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        stigatopik.setLocationRelativeTo(internalFrame1);
        stigatopik.setAlwaysOnTop(false);
        stigatopik.setVisible(true);
    }//GEN-LAST:event_BtnStigAtopikActionPerformed

    private void BtnStigAtopikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnStigAtopikKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnStigAtopikKeyPressed

    private void BtnDropStigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDropStigActionPerformed
        // TODO add your handling code here:
        
        JButton saveBtn = new JButton("SIMPAN");
        saveBtn.setPreferredSize(new java.awt.Dimension(100, 30));

        

        if(stigclick==0) {
            stigclick = 1;
            
            jPanel1 = new JPanel(new GridLayout(0, 5));
        
            jPanel1.setBackground(Color.WHITE);
            jPanel1.setBorder(new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.LIGHT_GRAY));
            jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            jPanel1.setName("jPanel1"); // NOI18N
            jPanel1.setPreferredSize(new java.awt.Dimension(452, 402));
            jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
            FormInput.add(jPanel1);
            jPanel1.setBounds(170, 280, 640, 80);

            //Get the components in the panel
            Component[] components = jPanel1.getComponents();

            //Loop through the components
            for(Component c : components){

                //Find the components you want to remove
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            //IMPORTANT
            jPanel1.revalidate();
            jPanel1.repaint();

            jPanel1.setVisible(true);
            BtnDropStig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/drowup-24.png"))); 

            
            tampilStigAtopik();
            
            int num = tabModeStig.getRowCount();
            JCheckBox cb[]=new JCheckBox[num];
            
            
            
            for(int i = 0; i < num; i++) {
                
                cb[i]=new JCheckBox(String.valueOf(tabModeStig.getValueAt(i, 0)));
                cb[i].setName(String.valueOf(tabModeStig.getValueAt(i, 1)));
                cb[i].setBackground(Color.white);
                
                cb[i].setFont(
                    cb[i].getFont().deriveFont(
                      Font.PLAIN,
                      cb[i].getFont().getSize()
                    ));
                
                JPanel panel = new JPanel();
                panel.setBackground(Color.white);

                panel.add(cb[i],BorderLayout.PAGE_START);
                
                jPanel1.add(panel);


                
                
                

            }
            
            
            saveBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    idstiglist.clear();
                    stiglist.clear();
                    
                    for(int i=0;i<num;i++) {
                        if(cb[i].isSelected()) {
                            int idStig = Integer.parseInt(cb[i].getName());
                            String stigAtopik = cb[i].getText();
                            idstiglist.add(idStig);
                            stiglist.add(stigAtopik);
                            System.out.println(idStig);
                            
                        }
                    }
                    String listString = String.join(", ", stiglist);

                    StigAtopik.setText(listString);
                    
                    stigclick = 0;

                    //Get the components in the panel
                    Component[] componentsss = jPanel1.getComponents();

                    //Loop through the components
                    for(Component c : componentsss){

                        //Find the components you want to remove

                        if(c instanceof JPanel){
                            //Remove it
                            jPanel1.remove(c);

                        }
                    }


                    //IMPORTANT 
                    jPanel1.revalidate();
                    jPanel1.repaint();

                    jPanel1.setVisible(false);
                    FormInput.remove(jPanel1);

                    BtnDropStig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 
                }
            });
            jPanel1.add(saveBtn, BorderLayout.PAGE_END);
            
        } else {
            stigclick = 0;
            pStig.setVisible(false);
            
            //Get the components in the panel
            Component[] componentsss = jPanel1.getComponents();
            
            //Loop through the components
            for(Component c : componentsss){

                //Find the components you want to remove
                
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            
            //IMPORTANT 
            jPanel1.revalidate();
            jPanel1.repaint();
            
            jPanel1.setVisible(false);
            FormInput.remove(jPanel1);
            
            BtnDropStig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 

        }
        
        

        
    }//GEN-LAST:event_BtnDropStigActionPerformed

    private void BtnDropStigKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDropStigKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnDropStigKeyPressed

    private void SyarafActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SyarafActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SyarafActionPerformed

    private void BtnDropSyarafActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDropSyarafActionPerformed
        // TODO add your handling code here:
        JButton saveBtn = new JButton("SIMPAN");
        saveBtn.setPreferredSize(new java.awt.Dimension(100, 30));

        

        if(syarafclick==0) {
            syarafclick = 1;
            
            
            jPanel1 = new JPanel(new GridLayout(0, 5));
        
            jPanel1.setBackground(new java.awt.Color(255, 255, 255));
            jPanel1.setBorder(new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.LIGHT_GRAY));
            jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            jPanel1.setName("jPanel1"); // NOI18N
            jPanel1.setPreferredSize(new java.awt.Dimension(452, 402));
            jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
            jPanel1.setOpaque(true);
            FormInput.add(jPanel1);
            jPanel1.setBounds(170, 400, 640, 80);

            //Get the components in the panel
            Component[] components = jPanel1.getComponents();

            //Loop through the components
            for(Component c : components){

                //Find the components you want to remove
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            //IMPORTANT
            jPanel1.revalidate();
            jPanel1.repaint();

            jPanel1.setVisible(true);
            BtnDropSyaraf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/drowup-24.png"))); 

            
            tampilSyaraf();
            
            int num = tabModeSyaraf.getRowCount();
            JCheckBox cb[]=new JCheckBox[num];
            
            
            
            for(int i = 0; i < num; i++) {
                
                cb[i]=new JCheckBox(String.valueOf(tabModeSyaraf.getValueAt(i, 0)));
                cb[i].setName(String.valueOf(tabModeSyaraf.getValueAt(i, 1)));
                cb[i].setBackground(Color.white);
                
                cb[i].setFont(
                    cb[i].getFont().deriveFont(
                      Font.PLAIN,
                      cb[i].getFont().getSize()
                    ));
                
                JPanel panel = new JPanel();
                panel.setBackground(Color.white);

                panel.add(cb[i],BorderLayout.PAGE_START);
                jPanel1.add(panel);
                
            }
            
            
            saveBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    idsyaraflist.clear();
                    syaraflist.clear();
                    
                    for(int i=0;i<num;i++) {
                        if(cb[i].isSelected()) {
                            int idSyaraf = Integer.parseInt(cb[i].getName());
                            String syaraf = cb[i].getText();
                            idsyaraflist.add(idSyaraf);
                            syaraflist.add(syaraf);
                        }
                    }
                    String listString = String.join(", ", syaraflist);

                    Syaraf.setText(listString);
                  
                    syarafclick = 0;

                    //Get the components in the panel
                    Component[] componentsss = jPanel1.getComponents();

                    //Loop through the components
                    for(Component c : componentsss){

                        //Find the components you want to remove

                        if(c instanceof JPanel){
                            //Remove it
                            jPanel1.remove(c);

                        }
                    }


                    //IMPORTANT 
                    jPanel1.revalidate();
                    jPanel1.repaint();

                    jPanel1.setVisible(false);
                    FormInput.remove(jPanel1);

                    BtnDropSyaraf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 
                }
            });
            jPanel1.add(saveBtn);
            
        } else {
            syarafclick = 0;
            
            
            
            //Get the components in the panel
            Component[] componentsss = jPanel1.getComponents();
            
            //Loop through the components
            for(Component c : componentsss){

                //Find the components you want to remove
                
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            
            //IMPORTANT 
            jPanel1.revalidate();
            jPanel1.repaint();
            
            jPanel1.setVisible(false);
            FormInput.remove(jPanel1);
            
            BtnDropSyaraf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 

        }
    }//GEN-LAST:event_BtnDropSyarafActionPerformed

    private void BtnDropSyarafKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDropSyarafKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnDropSyarafKeyPressed

    private void BtnSyarafActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSyarafActionPerformed
        // TODO add your handling code here:
        MasterSyaraf syaraf=new MasterSyaraf(null,false);
        syaraf.isCek();
        syaraf.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        syaraf.setLocationRelativeTo(internalFrame1);
        syaraf.setAlwaysOnTop(false);
        syaraf.setVisible(true);
    }//GEN-LAST:event_BtnSyarafActionPerformed

    private void BtnSyarafKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSyarafKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnSyarafKeyPressed

    private void UubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UubActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_UubActionPerformed

    private void BtnDropUubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDropUubActionPerformed
        // TODO add your handling code here:
        JButton saveBtn = new JButton("SIMPAN");
        saveBtn.setPreferredSize(new java.awt.Dimension(100, 30));

        

        if(uubclick==0) {
            uubclick = 1;
            
            jPanel1 = new JPanel(new GridLayout(0, 5));
        
            jPanel1.setBackground(Color.WHITE);
            jPanel1.setBorder(new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.LIGHT_GRAY));
            jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            jPanel1.setName("jPanel1"); // NOI18N
            jPanel1.setPreferredSize(new java.awt.Dimension(452, 402));
            jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
            FormInput.add(jPanel1);
            jPanel1.setBounds(170, 700, 640, 80);

            //Get the components in the panel
            Component[] components = jPanel1.getComponents();

            //Loop through the components
            for(Component c : components){

                //Find the components you want to remove
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            //IMPORTANT
            jPanel1.revalidate();
            jPanel1.repaint();

            jPanel1.setVisible(true);
            BtnDropUub.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/drowup-24.png"))); 

            
            tampilUub();
            
            int num = tabModeUub.getRowCount();
            JCheckBox cb[]=new JCheckBox[num];
            
            
            
            for(int i = 0; i < num; i++) {
                
                cb[i]=new JCheckBox(String.valueOf(tabModeUub.getValueAt(i, 0)));
                cb[i].setName(String.valueOf(tabModeUub.getValueAt(i, 1)));
                cb[i].setBackground(Color.white);
                
                cb[i].setFont(
                    cb[i].getFont().deriveFont(
                      Font.PLAIN,
                      cb[i].getFont().getSize()
                    ));
                
                JPanel panel = new JPanel();
                panel.setBackground(Color.white);

                panel.add(cb[i],BorderLayout.PAGE_START);
                jPanel1.add(panel);
                
                
                

            }
            
            
            saveBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    iduublist.clear();
                    uublist.clear();
                    
                    for(int i=0;i<num;i++) {
                        if(cb[i].isSelected()) {
                            int idUub = Integer.parseInt(cb[i].getName());
                            String uub = cb[i].getText();
                            iduublist.add(idUub);
                            uublist.add(uub);
                            
                        }
                    }
                    String listString = String.join(", ", uublist);

                    Uub.setText(listString);
                    
                    uubclick = 0;

                    //Get the components in the panel
                    Component[] componentsss = jPanel1.getComponents();

                    //Loop through the components
                    for(Component c : componentsss){

                        //Find the components you want to remove

                        if(c instanceof JPanel){
                            //Remove it
                            jPanel1.remove(c);

                        }
                    }


                    //IMPORTANT 
                    jPanel1.revalidate();
                    jPanel1.repaint();

                    jPanel1.setVisible(false);
                    FormInput.remove(jPanel1);

                    BtnDropUub.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 
                }
            });
            jPanel1.add(saveBtn, BorderLayout.PAGE_END);
            
        } else {
            uubclick = 0;
            
            //Get the components in the panel
            Component[] componentsss = jPanel1.getComponents();
            
            //Loop through the components
            for(Component c : componentsss){

                //Find the components you want to remove
                
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            
            //IMPORTANT 
            jPanel1.revalidate();
            jPanel1.repaint();
            
            jPanel1.setVisible(false);
            FormInput.remove(jPanel1);
            
            BtnDropUub.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 

        }
    }//GEN-LAST:event_BtnDropUubActionPerformed

    private void BtnDropUubKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDropUubKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnDropUubKeyPressed

    private void BtnUubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnUubActionPerformed
        // TODO add your handling code here:
        MasterUub uub=new MasterUub(null,false);
        uub.isCek();
        uub.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        uub.setLocationRelativeTo(internalFrame1);
        uub.setAlwaysOnTop(false);
        uub.setVisible(true);
    }//GEN-LAST:event_BtnUubActionPerformed

    private void BtnUubKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnUubKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnUubKeyPressed

    private void KepalaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KepalaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KepalaActionPerformed

    private void BtnDropKepalaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDropKepalaActionPerformed
        // TODO add your handling code here:
        JButton saveBtn = new JButton("SIMPAN");
        saveBtn.setPreferredSize(new java.awt.Dimension(100, 30));

        

        if(kepalaclick==0) {
            kepalaclick = 1;
            
            jPanel1 = new JPanel(new GridLayout(0, 5));
        
            jPanel1.setBackground(Color.WHITE);
            jPanel1.setBorder(new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.LIGHT_GRAY));
            jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            jPanel1.setName("jPanel1"); // NOI18N
            jPanel1.setPreferredSize(new java.awt.Dimension(452, 402));
            jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
            FormInput.add(jPanel1);
            jPanel1.setBounds(170, 670, 640, 80);

            //Get the components in the panel
            Component[] components = jPanel1.getComponents();

            //Loop through the components
            for(Component c : components){

                //Find the components you want to remove
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            //IMPORTANT
            jPanel1.revalidate();
            jPanel1.repaint();

            jPanel1.setVisible(true);
            BtnDropKepala.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/drowup-24.png"))); 

            
            tampilKepala();
            
            int num = tabModeKepala.getRowCount();
            JCheckBox cb[]=new JCheckBox[num];
            
            
            
            for(int i = 0; i < num; i++) {
                
                cb[i]=new JCheckBox(String.valueOf(tabModeKepala.getValueAt(i, 0)));
                cb[i].setName(String.valueOf(tabModeKepala.getValueAt(i, 1)));
                cb[i].setBackground(Color.white);
                
                cb[i].setFont(
                    cb[i].getFont().deriveFont(
                      Font.PLAIN,
                      cb[i].getFont().getSize()
                    ));
                
                JPanel panel = new JPanel();
                panel.setBackground(Color.white);

                panel.add(cb[i],BorderLayout.PAGE_START);
                jPanel1.add(panel);
                
                
                

            }
            
            
            saveBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    idkepalalist.clear();
                    kepalalist.clear();
                    
                    for(int i=0;i<num;i++) {
                        if(cb[i].isSelected()) {
                            int idKepala = Integer.parseInt(cb[i].getName());
                            String kepala = cb[i].getText();
                            idkepalalist.add(idKepala);
                            kepalalist.add(kepala);
                            
                        }
                    }
                    String listString = String.join(", ", kepalalist);

                    Kepala.setText(listString);
                    
                    kepalaclick = 0;

                    //Get the components in the panel
                    Component[] componentsss = jPanel1.getComponents();

                    //Loop through the components
                    for(Component c : componentsss){

                        //Find the components you want to remove

                        if(c instanceof JPanel){
                            //Remove it
                            jPanel1.remove(c);

                        }
                    }


                    //IMPORTANT 
                    jPanel1.revalidate();
                    jPanel1.repaint();

                    jPanel1.setVisible(false);
                    FormInput.remove(jPanel1);

                    BtnDropKepala.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 
                }
            });
            jPanel1.add(saveBtn, BorderLayout.PAGE_END);
            
        } else {
            kepalaclick = 0;
            
            //Get the components in the panel
            Component[] componentsss = jPanel1.getComponents();
            
            //Loop through the components
            for(Component c : componentsss){

                //Find the components you want to remove
                
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            
            //IMPORTANT 
            jPanel1.revalidate();
            jPanel1.repaint();
            
            jPanel1.setVisible(false);
            FormInput.remove(jPanel1);
            
            BtnDropKepala.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 

        }
    }//GEN-LAST:event_BtnDropKepalaActionPerformed

    private void BtnDropKepalaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDropKepalaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnDropKepalaKeyPressed

    private void BtnKepalaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKepalaActionPerformed
        // TODO add your handling code here:
        MasterKepala kepala=new MasterKepala(null,false);
        kepala.isCek();
        kepala.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        kepala.setLocationRelativeTo(internalFrame1);
        kepala.setAlwaysOnTop(false);
        kepala.setVisible(true);
    }//GEN-LAST:event_BtnKepalaActionPerformed

    private void BtnKepalaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKepalaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnKepalaKeyPressed

    private void MataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MataActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MataActionPerformed

    private void BtnDropMataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDropMataActionPerformed
        // TODO add your handling code here:
        JButton saveBtn = new JButton("SIMPAN");
        saveBtn.setPreferredSize(new java.awt.Dimension(100, 30));

        

        if(mataclick==0) {
            mataclick = 1;
            
            jPanel1 = new JPanel(new GridLayout(0, 5));
        
            jPanel1.setBackground(Color.WHITE);
            jPanel1.setBorder(new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.LIGHT_GRAY));
            jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            jPanel1.setName("jPanel1"); // NOI18N
            jPanel1.setPreferredSize(new java.awt.Dimension(452, 402));
            jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
            FormInput.add(jPanel1);
            jPanel1.setBounds(170, 730, 640, 80);

            //Get the components in the panel
            Component[] components = jPanel1.getComponents();

            //Loop through the components
            for(Component c : components){

                //Find the components you want to remove
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            //IMPORTANT
            jPanel1.revalidate();
            jPanel1.repaint();

            jPanel1.setVisible(true);
            BtnDropMata.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/drowup-24.png"))); 

            
            tampilMata();
            
            int num = tabModeMata.getRowCount();
            JCheckBox cb[]=new JCheckBox[num];
            
            
            
            for(int i = 0; i < num; i++) {
                
                cb[i]=new JCheckBox(String.valueOf(tabModeMata.getValueAt(i, 0)));
                cb[i].setName(String.valueOf(tabModeMata.getValueAt(i, 1)));
                cb[i].setBackground(Color.white);
                
                cb[i].setFont(
                    cb[i].getFont().deriveFont(
                      Font.PLAIN,
                      cb[i].getFont().getSize()
                    ));
                
                JPanel panel = new JPanel();
                panel.setBackground(Color.white);

                panel.add(cb[i],BorderLayout.PAGE_START);
                jPanel1.add(panel);
                
                
                

            }
            
            
            saveBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    idmatalist.clear();
                    matalist.clear();
                    
                    for(int i=0;i<num;i++) {
                        if(cb[i].isSelected()) {
                            int idStig = Integer.parseInt(cb[i].getName());
                            String stigAtopik = cb[i].getText();
                            idmatalist.add(idStig);
                            matalist.add(stigAtopik);
                            
                        }
                    }
                    String listString = String.join(", ", matalist);

                    Mata.setText(listString);
                    
                    mataclick = 0;

                    //Get the components in the panel
                    Component[] componentsss = jPanel1.getComponents();

                    //Loop through the components
                    for(Component c : componentsss){

                        //Find the components you want to remove

                        if(c instanceof JPanel){
                            //Remove it
                            jPanel1.remove(c);

                        }
                    }


                    //IMPORTANT 
                    jPanel1.revalidate();
                    jPanel1.repaint();

                    jPanel1.setVisible(false);
                    FormInput.remove(jPanel1);

                    BtnDropMata.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 
                }
            });
            jPanel1.add(saveBtn, BorderLayout.PAGE_END);
            
        } else {
            mataclick = 0;
            
            //Get the components in the panel
            Component[] componentsss = jPanel1.getComponents();
            
            //Loop through the components
            for(Component c : componentsss){

                //Find the components you want to remove
                
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            
            //IMPORTANT 
            jPanel1.revalidate();
            jPanel1.repaint();
            
            jPanel1.setVisible(false);
            FormInput.remove(jPanel1);
            
            BtnDropMata.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 

        }
        
    }//GEN-LAST:event_BtnDropMataActionPerformed

    private void BtnDropMataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDropMataKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnDropMataKeyPressed

    private void BtnMataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnMataActionPerformed
        // TODO add your handling code here:
        MasterMata mata=new MasterMata(null,false);
        mata.isCek();
        mata.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        mata.setLocationRelativeTo(internalFrame1);
        mata.setAlwaysOnTop(false);
        mata.setVisible(true);
    }//GEN-LAST:event_BtnMataActionPerformed

    private void BtnMataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnMataKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnMataKeyPressed

    private void ThtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ThtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ThtActionPerformed

    private void BtnDropThtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDropThtActionPerformed
        // TODO add your handling code here:
        
        JButton saveBtn = new JButton("SIMPAN");
        saveBtn.setPreferredSize(new java.awt.Dimension(100, 30));

        

        if(thtclick==0) {
            thtclick = 1;
            
            jPanel1 = new JPanel(new GridLayout(0, 5));
        
            jPanel1.setBackground(Color.WHITE);
            jPanel1.setBorder(new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.LIGHT_GRAY));
            jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            jPanel1.setName("jPanel1"); // NOI18N
            jPanel1.setPreferredSize(new java.awt.Dimension(452, 402));
            jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
            FormInput.add(jPanel1);
            jPanel1.setBounds(170, 760, 640, 80);

            //Get the components in the panel
            Component[] components = jPanel1.getComponents();

            //Loop through the components
            for(Component c : components){

                //Find the components you want to remove
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            //IMPORTANT
            jPanel1.revalidate();
            jPanel1.repaint();

            jPanel1.setVisible(true);
            BtnDropTht.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/drowup-24.png"))); 

            
            tampilTht();
            
            int num = tabModeTht.getRowCount();
            JCheckBox cb[]=new JCheckBox[num];
            
            
            
            for(int i = 0; i < num; i++) {
                
                cb[i]=new JCheckBox(String.valueOf(tabModeTht.getValueAt(i, 0)));
                cb[i].setName(String.valueOf(tabModeTht.getValueAt(i, 1)));
                cb[i].setBackground(Color.white);
                
                cb[i].setFont(
                    cb[i].getFont().deriveFont(
                      Font.PLAIN,
                      cb[i].getFont().getSize()
                    ));
                
                JPanel panel = new JPanel();
                panel.setBackground(Color.white);

                panel.add(cb[i],BorderLayout.PAGE_START);
                jPanel1.add(panel);
                
                
                

            }
            
            
            saveBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    idthtlist.clear();
                    thtlist.clear();
                    
                    for(int i=0;i<num;i++) {
                        if(cb[i].isSelected()) {
                            int idStig = Integer.parseInt(cb[i].getName());
                            String stigAtopik = cb[i].getText();
                            idthtlist.add(idStig);
                            thtlist.add(stigAtopik);
                            
                        }
                    }
                    String listString = String.join(", ", thtlist);

                    Tht.setText(listString);
                    
                    thtclick = 0;

                    //Get the components in the panel
                    Component[] componentsss = jPanel1.getComponents();

                    //Loop through the components
                    for(Component c : componentsss){

                        //Find the components you want to remove

                        if(c instanceof JPanel){
                            //Remove it
                            jPanel1.remove(c);

                        }
                    }


                    //IMPORTANT 
                    jPanel1.revalidate();
                    jPanel1.repaint();

                    jPanel1.setVisible(false);
                    FormInput.remove(jPanel1);

                    BtnDropTht.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 
                }
            });
            jPanel1.add(saveBtn, BorderLayout.PAGE_END);
            
        } else {
            thtclick = 0;
            
            //Get the components in the panel
            Component[] componentsss = jPanel1.getComponents();
            
            //Loop through the components
            for(Component c : componentsss){

                //Find the components you want to remove
                
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            
            //IMPORTANT 
            jPanel1.revalidate();
            jPanel1.repaint();
            
            jPanel1.setVisible(false);
            FormInput.remove(jPanel1);
            
            BtnDropTht.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 

        }
        
    }//GEN-LAST:event_BtnDropThtActionPerformed

    private void BtnDropThtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDropThtKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnDropThtKeyPressed

    private void BtnThtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnThtActionPerformed
        // TODO add your handling code here:
        MasterTht tht=new MasterTht(null,false);
        tht.isCek();
        tht.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        tht.setLocationRelativeTo(internalFrame1);
        tht.setAlwaysOnTop(false);
        tht.setVisible(true);
    }//GEN-LAST:event_BtnThtActionPerformed

    private void BtnThtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnThtKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnThtKeyPressed

    private void MulutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MulutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MulutActionPerformed

    private void BtnDropMulutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDropMulutActionPerformed
        // TODO add your handling code here:
        JButton saveBtn = new JButton("SIMPAN");
        saveBtn.setPreferredSize(new java.awt.Dimension(100, 30));


        if(mulutclick==0) {
            mulutclick = 1;
            
            jPanel1 = new JPanel(new GridLayout(0, 5));
        
            jPanel1.setBackground(Color.WHITE);
            jPanel1.setBorder(new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.LIGHT_GRAY));
            jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            jPanel1.setName("jPanel1"); // NOI18N
            jPanel1.setPreferredSize(new java.awt.Dimension(452, 402));
            jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
            FormInput.add(jPanel1);
            jPanel1.setBounds(170, 790, 640, 80);

            //Get the components in the panel
            Component[] components = jPanel1.getComponents();

            //Loop through the components
            for(Component c : components){

                //Find the components you want to remove
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            //IMPORTANT
            jPanel1.revalidate();
            jPanel1.repaint();

            jPanel1.setVisible(true);
            BtnDropMulut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/drowup-24.png"))); 

            
            tampilMulut();
            
            int num = tabModeMulut.getRowCount();
            JCheckBox cb[]=new JCheckBox[num];
            
            
            
            for(int i = 0; i < num; i++) {
                
                cb[i]=new JCheckBox(String.valueOf(tabModeMulut.getValueAt(i, 0)));
                cb[i].setName(String.valueOf(tabModeMulut.getValueAt(i, 1)));
                cb[i].setBackground(Color.white);
                
                cb[i].setFont(
                    cb[i].getFont().deriveFont(
                      Font.PLAIN,
                      cb[i].getFont().getSize()
                    ));
                
                JPanel panel = new JPanel();
                panel.setBackground(Color.white);

                panel.add(cb[i],BorderLayout.PAGE_START);
                jPanel1.add(panel);
                
                
                

            }
            
            
            saveBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    idmulutlist.clear();
                    mulutlist.clear();
                    
                    for(int i=0;i<num;i++) {
                        if(cb[i].isSelected()) {
                            int idMulut = Integer.parseInt(cb[i].getName());
                            String mulut = cb[i].getText();
                            idmulutlist.add(idMulut);
                            mulutlist.add(mulut);
                            
                        }
                    }
                    String listString = String.join(", ", mulutlist);

                    Mulut.setText(listString);
                    
                    mulutclick = 0;

                    //Get the components in the panel
                    Component[] componentsss = jPanel1.getComponents();

                    //Loop through the components
                    for(Component c : componentsss){

                        //Find the components you want to remove

                        if(c instanceof JPanel){
                            //Remove it
                            jPanel1.remove(c);

                        }
                    }


                    //IMPORTANT 
                    jPanel1.revalidate();
                    jPanel1.repaint();

                    jPanel1.setVisible(false);
                    FormInput.remove(jPanel1);

                    BtnDropMulut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 
                }
            });
            jPanel1.add(saveBtn, BorderLayout.PAGE_END);
            
        } else {
            mulutclick = 0;
            
            //Get the components in the panel
            Component[] componentsss = jPanel1.getComponents();
            
            //Loop through the components
            for(Component c : componentsss){

                //Find the components you want to remove
                
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            
            //IMPORTANT 
            jPanel1.revalidate();
            jPanel1.repaint();
            
            jPanel1.setVisible(false);
            FormInput.remove(jPanel1);
            
            BtnDropMulut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 

        }
    }//GEN-LAST:event_BtnDropMulutActionPerformed

    private void BtnDropMulutKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDropMulutKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnDropMulutKeyPressed

    private void BtnMulutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnMulutActionPerformed
        // TODO add your handling code here:
        MasterMulut mulut=new MasterMulut(null,false);
        mulut.isCek();
        mulut.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        mulut.setLocationRelativeTo(internalFrame1);
        mulut.setAlwaysOnTop(false);
        mulut.setVisible(true);
    }//GEN-LAST:event_BtnMulutActionPerformed

    private void BtnMulutKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnMulutKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnMulutKeyPressed

    private void ThoraxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ThoraxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ThoraxActionPerformed

    private void BtnDropThoraxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDropThoraxActionPerformed
        // TODO add your handling code here:
        JButton saveBtn = new JButton("SIMPAN");
        saveBtn.setPreferredSize(new java.awt.Dimension(100, 30));


        if(thoraxclick==0) {
            thoraxclick = 1;
            
            jPanel1 = new JPanel(new GridLayout(0, 5));
        
            jPanel1.setBackground(Color.WHITE);
            jPanel1.setBorder(new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.LIGHT_GRAY));
            jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            jPanel1.setName("jPanel1"); // NOI18N
            jPanel1.setPreferredSize(new java.awt.Dimension(452, 402));
            jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
            FormInput.add(jPanel1);
            jPanel1.setBounds(170, 850, 640, 80);

            //Get the components in the panel
            Component[] components = jPanel1.getComponents();

            //Loop through the components
            for(Component c : components){

                //Find the components you want to remove
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            //IMPORTANT
            jPanel1.revalidate();
            jPanel1.repaint();

            jPanel1.setVisible(true);
            BtnDropThorax.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/drowup-24.png"))); 

            
            tampilThorax();
            
            int num = tabModeThorax.getRowCount();
            JCheckBox cb[]=new JCheckBox[num];
            
            
            
            for(int i = 0; i < num; i++) {
                
                cb[i]=new JCheckBox(String.valueOf(tabModeThorax.getValueAt(i, 0)));
                cb[i].setName(String.valueOf(tabModeThorax.getValueAt(i, 1)));
                cb[i].setBackground(Color.white);
                
                cb[i].setFont(
                    cb[i].getFont().deriveFont(
                      Font.PLAIN,
                      cb[i].getFont().getSize()
                    ));
                
                JPanel panel = new JPanel();
                panel.setBackground(Color.white);

                panel.add(cb[i],BorderLayout.PAGE_START);
                jPanel1.add(panel);
                
                
                

            }
            
            
            saveBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    idthoraxlist.clear();
                    thoraxlist.clear();
                    
                    for(int i=0;i<num;i++) {
                        if(cb[i].isSelected()) {
                            int idThorax = Integer.parseInt(cb[i].getName());
                            String thorax = cb[i].getText();
                            idthoraxlist.add(idThorax);
                            thoraxlist.add(thorax);
                            
                        }
                    }
                    String listString = String.join(", ", thoraxlist);

                    Thorax.setText(listString);
                    
                    thoraxclick = 0;

                    //Get the components in the panel
                    Component[] componentsss = jPanel1.getComponents();

                    //Loop through the components
                    for(Component c : componentsss){

                        //Find the components you want to remove

                        if(c instanceof JPanel){
                            //Remove it
                            jPanel1.remove(c);

                        }
                    }


                    //IMPORTANT 
                    jPanel1.revalidate();
                    jPanel1.repaint();

                    jPanel1.setVisible(false);
                    FormInput.remove(jPanel1);

                    BtnDropThorax.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 
                }
            });
            jPanel1.add(saveBtn, BorderLayout.PAGE_END);
            
        } else {
            thoraxclick = 0;
            
            //Get the components in the panel
            Component[] componentsss = jPanel1.getComponents();
            
            //Loop through the components
            for(Component c : componentsss){

                //Find the components you want to remove
                
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            
            //IMPORTANT 
            jPanel1.revalidate();
            jPanel1.repaint();
            
            jPanel1.setVisible(false);
            FormInput.remove(jPanel1);
            
            BtnDropThorax.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 

        }
    }//GEN-LAST:event_BtnDropThoraxActionPerformed

    private void BtnDropThoraxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDropThoraxKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnDropThoraxKeyPressed

    private void BtnThoraxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnThoraxActionPerformed
        // TODO add your handling code here:
        MasterThorax thorax=new MasterThorax(null,false);
        thorax.isCek();
        thorax.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        thorax.setLocationRelativeTo(internalFrame1);
        thorax.setAlwaysOnTop(false);
        thorax.setVisible(true);
    }//GEN-LAST:event_BtnThoraxActionPerformed

    private void BtnThoraxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnThoraxKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnThoraxKeyPressed

    private void AbdomenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AbdomenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AbdomenActionPerformed

    private void BtnDropAbdomenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDropAbdomenActionPerformed
        // TODO add your handling code here:
        JButton saveBtn = new JButton("SIMPAN");
        saveBtn.setPreferredSize(new java.awt.Dimension(100, 30));


        if(abdomenclick==0) {
            abdomenclick = 1;
            
            jPanel1 = new JPanel(new GridLayout(0, 5));
        
            jPanel1.setBackground(Color.WHITE);
            jPanel1.setBorder(new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.LIGHT_GRAY));
            jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            jPanel1.setName("jPanel1"); // NOI18N
            jPanel1.setPreferredSize(new java.awt.Dimension(452, 402));
            jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
            FormInput.add(jPanel1);
            jPanel1.setBounds(170, 880, 640, 80);

            //Get the components in the panel
            Component[] components = jPanel1.getComponents();

            //Loop through the components
            for(Component c : components){

                //Find the components you want to remove
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            //IMPORTANT
            jPanel1.revalidate();
            jPanel1.repaint();

            jPanel1.setVisible(true);
            BtnDropAbdomen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/drowup-24.png"))); 

            
            tampilAbdomen();
            
            int num = tabModeAbdomen.getRowCount();
            JCheckBox cb[]=new JCheckBox[num];
            
            
            
            for(int i = 0; i < num; i++) {
                
                cb[i]=new JCheckBox(String.valueOf(tabModeAbdomen.getValueAt(i, 0)));
                cb[i].setName(String.valueOf(tabModeAbdomen.getValueAt(i, 1)));
                cb[i].setBackground(Color.white);
                
                cb[i].setFont(
                    cb[i].getFont().deriveFont(
                      Font.PLAIN,
                      cb[i].getFont().getSize()
                    ));
                
                JPanel panel = new JPanel();
                panel.setBackground(Color.white);

                panel.add(cb[i],BorderLayout.PAGE_START);
                jPanel1.add(panel);
                
                
                

            }
            
            
            saveBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    idabdomenlist.clear();
                    abdomenlist.clear();
                    
                    for(int i=0;i<num;i++) {
                        if(cb[i].isSelected()) {
                            int idAbdomen = Integer.parseInt(cb[i].getName());
                            String abdomen = cb[i].getText();
                            idabdomenlist.add(idAbdomen);
                            abdomenlist.add(abdomen);
                            
                        }
                    }
                    String listString = String.join(", ", abdomenlist);

                    Abdomen.setText(listString);
                    
                    abdomenclick = 0;

                    //Get the components in the panel
                    Component[] componentsss = jPanel1.getComponents();

                    //Loop through the components
                    for(Component c : componentsss){

                        //Find the components you want to remove

                        if(c instanceof JPanel){
                            //Remove it
                            jPanel1.remove(c);

                        }
                    }


                    //IMPORTANT 
                    jPanel1.revalidate();
                    jPanel1.repaint();

                    jPanel1.setVisible(false);
                    FormInput.remove(jPanel1);

                    BtnDropAbdomen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 
                }
            });
            jPanel1.add(saveBtn, BorderLayout.PAGE_END);
            
        } else {
            abdomenclick = 0;
            
            //Get the components in the panel
            Component[] componentsss = jPanel1.getComponents();
            
            //Loop through the components
            for(Component c : componentsss){

                //Find the components you want to remove
                
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            
            //IMPORTANT 
            jPanel1.revalidate();
            jPanel1.repaint();
            
            jPanel1.setVisible(false);
            FormInput.remove(jPanel1);
            
            BtnDropAbdomen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 

        }
    }//GEN-LAST:event_BtnDropAbdomenActionPerformed

    private void BtnDropAbdomenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDropAbdomenKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnDropAbdomenKeyPressed

    private void BtnAbdomenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAbdomenActionPerformed
        // TODO add your handling code here:
        MasterAbdomen abdomen=new MasterAbdomen(null,false);
        abdomen.isCek();
        abdomen.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        abdomen.setLocationRelativeTo(internalFrame1);
        abdomen.setAlwaysOnTop(false);
        abdomen.setVisible(true);
    }//GEN-LAST:event_BtnAbdomenActionPerformed

    private void BtnAbdomenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAbdomenKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnAbdomenKeyPressed

    private void TaliPusatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TaliPusatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TaliPusatActionPerformed

    private void BtnDropTaliPusatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDropTaliPusatActionPerformed
        // TODO add your handling code here:
        JButton saveBtn = new JButton("SIMPAN");
        saveBtn.setPreferredSize(new java.awt.Dimension(100, 30));


        if(tpusatclick==0) {
           tpusatclick = 1;
            
            jPanel1 = new JPanel(new GridLayout(0, 5));
        
            jPanel1.setBackground(Color.WHITE);
            jPanel1.setBorder(new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.LIGHT_GRAY));
            jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            jPanel1.setName("jPanel1"); // NOI18N
            jPanel1.setPreferredSize(new java.awt.Dimension(452, 402));
            jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
            FormInput.add(jPanel1);
            jPanel1.setBounds(170, 910, 640, 80);

            //Get the components in the panel
            Component[] components = jPanel1.getComponents();

            //Loop through the components
            for(Component c : components){

                //Find the components you want to remove
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            //IMPORTANT
            jPanel1.revalidate();
            jPanel1.repaint();

            jPanel1.setVisible(true);
            BtnDropTaliPusat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/drowup-24.png"))); 

            
            tampilTaliPusat();
            
            int num = tabModeTaliPusat.getRowCount();
            JCheckBox cb[]=new JCheckBox[num];
            
            
            
            for(int i = 0; i < num; i++) {
                
                cb[i]=new JCheckBox(String.valueOf(tabModeTaliPusat.getValueAt(i, 0)));
                cb[i].setName(String.valueOf(tabModeTaliPusat.getValueAt(i, 1)));
                cb[i].setBackground(Color.white);
                
                cb[i].setFont(
                    cb[i].getFont().deriveFont(
                      Font.PLAIN,
                      cb[i].getFont().getSize()
                    ));
                
                JPanel panel = new JPanel();
                panel.setBackground(Color.white);

                panel.add(cb[i],BorderLayout.PAGE_START);
                jPanel1.add(panel);
                
                
                

            }
            
            
            saveBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    idtalipusatlist.clear();
                    talipusatlist.clear();
                    
                    for(int i=0;i<num;i++) {
                        if(cb[i].isSelected()) {
                            int idTaliPusat = Integer.parseInt(cb[i].getName());
                            String talipusat = cb[i].getText();
                            idtalipusatlist.add(idTaliPusat);
                            talipusatlist.add(talipusat);
                            
                        }
                    }
                    String listString = String.join(", ", talipusatlist);

                    TaliPusat.setText(listString);
                    
                    tpusatclick = 0;

                    //Get the components in the panel
                    Component[] componentsss = jPanel1.getComponents();

                    //Loop through the components
                    for(Component c : componentsss){

                        //Find the components you want to remove

                        if(c instanceof JPanel){
                            //Remove it
                            jPanel1.remove(c);

                        }
                    }


                    //IMPORTANT 
                    jPanel1.revalidate();
                    jPanel1.repaint();

                    jPanel1.setVisible(false);
                    FormInput.remove(jPanel1);

                    BtnDropTaliPusat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 
                }
            });
            jPanel1.add(saveBtn, BorderLayout.PAGE_END);
            
        } else {
            tpusatclick = 0;
            
            //Get the components in the panel
            Component[] componentsss = jPanel1.getComponents();
            
            //Loop through the components
            for(Component c : componentsss){

                //Find the components you want to remove
                
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            
            //IMPORTANT 
            jPanel1.revalidate();
            jPanel1.repaint();
            
            jPanel1.setVisible(false);
            FormInput.remove(jPanel1);
            
            BtnDropTaliPusat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 

        }
    }//GEN-LAST:event_BtnDropTaliPusatActionPerformed

    private void BtnDropTaliPusatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDropTaliPusatKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnDropTaliPusatKeyPressed

    private void BtnTaliPusatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTaliPusatActionPerformed
        // TODO add your handling code here:
        MasterTaliPusat talipusat=new MasterTaliPusat(null,false);
        talipusat.isCek();
        talipusat.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        talipusat.setLocationRelativeTo(internalFrame1);
        talipusat.setAlwaysOnTop(false);
        talipusat.setVisible(true);
    }//GEN-LAST:event_BtnTaliPusatActionPerformed

    private void BtnTaliPusatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnTaliPusatKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnTaliPusatKeyPressed

    private void PunggungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PunggungActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PunggungActionPerformed

    private void BtnDropPunggungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDropPunggungActionPerformed
        // TODO add your handling code here:
        JButton saveBtn = new JButton("SIMPAN");
        saveBtn.setPreferredSize(new java.awt.Dimension(100, 30));


        if(punggungclick==0) {
           punggungclick = 1;
            
            jPanel1 = new JPanel(new GridLayout(0, 5));
        
            jPanel1.setBackground(Color.WHITE);
            jPanel1.setBorder(new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.LIGHT_GRAY));
            jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            jPanel1.setName("jPanel1"); // NOI18N
            jPanel1.setPreferredSize(new java.awt.Dimension(452, 402));
            jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
            FormInput.add(jPanel1);
            jPanel1.setBounds(170, 940, 640, 80);

            //Get the components in the panel
            Component[] components = jPanel1.getComponents();

            //Loop through the components
            for(Component c : components){

                //Find the components you want to remove
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            //IMPORTANT
            jPanel1.revalidate();
            jPanel1.repaint();

            jPanel1.setVisible(true);
            BtnDropPunggung.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/drowup-24.png"))); 

            
            tampilPunggung();
            
            int num = tabModePunggung.getRowCount();
            JCheckBox cb[]=new JCheckBox[num];
            
            
            
            for(int i = 0; i < num; i++) {
                
                cb[i]=new JCheckBox(String.valueOf(tabModePunggung.getValueAt(i, 0)));
                cb[i].setName(String.valueOf(tabModePunggung.getValueAt(i, 1)));
                cb[i].setBackground(Color.white);
                
                cb[i].setFont(
                    cb[i].getFont().deriveFont(
                      Font.PLAIN,
                      cb[i].getFont().getSize()
                    ));
                
                JPanel panel = new JPanel();
                panel.setBackground(Color.white);

                panel.add(cb[i],BorderLayout.PAGE_START);
                jPanel1.add(panel);
                
                
                

            }
            
            
            saveBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    idpunggunglist.clear();
                    punggunglist.clear();
                    
                    for(int i=0;i<num;i++) {
                        if(cb[i].isSelected()) {
                            int idPunggung = Integer.parseInt(cb[i].getName());
                            String punggung = cb[i].getText();
                            idpunggunglist.add(idPunggung);
                            punggunglist.add(punggung);
                            
                        }
                    }
                    String listString = String.join(", ", punggunglist);

                    Punggung.setText(listString);
                    
                    punggungclick = 0;

                    //Get the components in the panel
                    Component[] componentsss = jPanel1.getComponents();

                    //Loop through the components
                    for(Component c : componentsss){

                        //Find the components you want to remove

                        if(c instanceof JPanel){
                            //Remove it
                            jPanel1.remove(c);

                        }
                    }


                    //IMPORTANT 
                    jPanel1.revalidate();
                    jPanel1.repaint();

                    jPanel1.setVisible(false);
                    FormInput.remove(jPanel1);

                    BtnDropPunggung.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 
                }
            });
            jPanel1.add(saveBtn, BorderLayout.PAGE_END);
            
        } else {
            punggungclick = 0;
            
            //Get the components in the panel
            Component[] componentsss = jPanel1.getComponents();
            
            //Loop through the components
            for(Component c : componentsss){

                //Find the components you want to remove
                
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            
            //IMPORTANT 
            jPanel1.revalidate();
            jPanel1.repaint();
            
            jPanel1.setVisible(false);
            FormInput.remove(jPanel1);
            
            BtnDropPunggung.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 

        }
    }//GEN-LAST:event_BtnDropPunggungActionPerformed

    private void BtnDropPunggungKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDropPunggungKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnDropPunggungKeyPressed

    private void BtnPunggungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPunggungActionPerformed
        // TODO add your handling code here:
        MasterPunggung punggung=new MasterPunggung(null,false);
        punggung.isCek();
        punggung.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        punggung.setLocationRelativeTo(internalFrame1);
        punggung.setAlwaysOnTop(false);
        punggung.setVisible(true);
    }//GEN-LAST:event_BtnPunggungActionPerformed

    private void BtnPunggungKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPunggungKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnPunggungKeyPressed

    private void GenetaliaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GenetaliaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_GenetaliaActionPerformed

    private void EkstremnitasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EkstremnitasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EkstremnitasActionPerformed

    private void BtnDropEkstremnitasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDropEkstremnitasActionPerformed
        // TODO add your handling code here:
        JButton saveBtn = new JButton("SIMPAN");
        saveBtn.setPreferredSize(new java.awt.Dimension(100, 30));


        if(ekstremclick==0) {
           ekstremclick = 1;
            
            jPanel1 = new JPanel(new GridLayout(0, 5));
        
            jPanel1.setBackground(Color.WHITE);
            jPanel1.setBorder(new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.LIGHT_GRAY));
            jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            jPanel1.setName("jPanel1"); // NOI18N
            jPanel1.setPreferredSize(new java.awt.Dimension(452, 402));
            jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
            FormInput.add(jPanel1);
            jPanel1.setBounds(170, 1030, 640, 80);

            //Get the components in the panel
            Component[] components = jPanel1.getComponents();

            //Loop through the components
            for(Component c : components){

                //Find the components you want to remove
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            //IMPORTANT
            jPanel1.revalidate();
            jPanel1.repaint();

            jPanel1.setVisible(true);
            BtnDropEkstremnitas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/drowup-24.png"))); 

            
            tampilEkstremnitas();
            
            int num = tabModeEkstremnitas.getRowCount();
            JCheckBox cb[]=new JCheckBox[num];
            
            
            
            for(int i = 0; i < num; i++) {
                
                cb[i]=new JCheckBox(String.valueOf(tabModeEkstremnitas.getValueAt(i, 0)));
                cb[i].setName(String.valueOf(tabModeEkstremnitas.getValueAt(i, 1)));
                cb[i].setBackground(Color.white);
                
                cb[i].setFont(
                    cb[i].getFont().deriveFont(
                      Font.PLAIN,
                      cb[i].getFont().getSize()
                    ));
                
                JPanel panel = new JPanel();
                panel.setBackground(Color.white);

                panel.add(cb[i],BorderLayout.PAGE_START);
                jPanel1.add(panel);
                
                
                

            }
            
            
            saveBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    idekstremnitaslist.clear();
                    ekstremnitaslist.clear();
                    
                    for(int i=0;i<num;i++) {
                        if(cb[i].isSelected()) {
                            int idEkstremnitas = Integer.parseInt(cb[i].getName());
                            String ekstremnitas = cb[i].getText();
                            idekstremnitaslist.add(idEkstremnitas);
                            ekstremnitaslist.add(ekstremnitas);
                            
                        }
                    }
                    String listString = String.join(", ", ekstremnitaslist);

                    
                    Ekstremnitas.setText(listString);
                    
                    ekstremclick = 0;

                    //Get the components in the panel
                    Component[] componentsss = jPanel1.getComponents();

                    //Loop through the components
                    for(Component c : componentsss){

                        //Find the components you want to remove

                        if(c instanceof JPanel){
                            //Remove it
                            jPanel1.remove(c);

                        }
                    }


                    //IMPORTANT 
                    jPanel1.revalidate();
                    jPanel1.repaint();

                    jPanel1.setVisible(false);
                    FormInput.remove(jPanel1);

                    BtnDropEkstremnitas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 
                    
                }
            });
            jPanel1.add(saveBtn, BorderLayout.PAGE_END);
            
        } else {
            ekstremclick = 0;
            
            //Get the components in the panel
            Component[] componentsss = jPanel1.getComponents();
            
            //Loop through the components
            for(Component c : componentsss){

                //Find the components you want to remove
                
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            
            //IMPORTANT 
            jPanel1.revalidate();
            jPanel1.repaint();
            
            jPanel1.setVisible(false);
            FormInput.remove(jPanel1);
            
            BtnDropEkstremnitas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 

        }
    }//GEN-LAST:event_BtnDropEkstremnitasActionPerformed

    private void BtnDropEkstremnitasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDropEkstremnitasKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnDropEkstremnitasKeyPressed

    private void BtnEkstremnitasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEkstremnitasActionPerformed
        // TODO add your handling code here:
        MasterEkstremnitas ekstremnitas=new MasterEkstremnitas(null,false);
        ekstremnitas.isCek();
        ekstremnitas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        ekstremnitas.setLocationRelativeTo(internalFrame1);
        ekstremnitas.setAlwaysOnTop(false);
        ekstremnitas.setVisible(true);
    }//GEN-LAST:event_BtnEkstremnitasActionPerformed

    private void BtnEkstremnitasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEkstremnitasKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnEkstremnitasKeyPressed

    private void KulitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KulitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KulitActionPerformed

    private void BtnDropKulitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDropKulitActionPerformed
        // TODO add your handling code here:
        JButton saveBtn = new JButton("SIMPAN");
        saveBtn.setPreferredSize(new java.awt.Dimension(100, 30));


        if(kulitclick==0) {
           kulitclick = 1;
            
            jPanel1 = new JPanel(new GridLayout(0, 5));
        
            jPanel1.setBackground(Color.WHITE);
            jPanel1.setBorder(new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.LIGHT_GRAY));
            jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            jPanel1.setName("jPanel1"); // NOI18N
            jPanel1.setPreferredSize(new java.awt.Dimension(452, 402));
            jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
            FormInput.add(jPanel1);
            jPanel1.setBounds(170, 1060, 640, 80);

            //Get the components in the panel
            Component[] components = jPanel1.getComponents();

            //Loop through the components
            for(Component c : components){

                //Find the components you want to remove
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            //IMPORTANT
            jPanel1.revalidate();
            jPanel1.repaint();

            jPanel1.setVisible(true);
            BtnDropKulit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/drowup-24.png"))); 

            
            tampilKulit();
            
            int num = tabModeKulit.getRowCount();
            JCheckBox cb[]=new JCheckBox[num];
            
            
            
            for(int i = 0; i < num; i++) {
                
                cb[i]=new JCheckBox(String.valueOf(tabModeKulit.getValueAt(i, 0)));
                cb[i].setName(String.valueOf(tabModeKulit.getValueAt(i, 1)));
                cb[i].setBackground(Color.white);
                
                cb[i].setFont(
                    cb[i].getFont().deriveFont(
                      Font.PLAIN,
                      cb[i].getFont().getSize()
                    ));
                
                JPanel panel = new JPanel();
                panel.setBackground(Color.white);

                panel.add(cb[i],BorderLayout.PAGE_START);
                jPanel1.add(panel);
                
                
                

            }
            
            
            saveBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    idkulitlist.clear();
                    kulitlist.clear();
                    
                    for(int i=0;i<num;i++) {
                        if(cb[i].isSelected()) {
                            int idKulit = Integer.parseInt(cb[i].getName());
                            String kulit = cb[i].getText();
                            idkulitlist.add(idKulit);
                            kulitlist.add(kulit);
                            
                        }
                    }
                    String listString = String.join(", ", kulitlist);

                    
                    Kulit.setText(listString);
                    
                    kulitclick = 0;

                    //Get the components in the panel
                    Component[] componentsss = jPanel1.getComponents();

                    //Loop through the components
                    for(Component c : componentsss){

                        //Find the components you want to remove

                        if(c instanceof JPanel){
                            //Remove it
                            jPanel1.remove(c);

                        }
                    }


                    //IMPORTANT 
                    jPanel1.revalidate();
                    jPanel1.repaint();

                    jPanel1.setVisible(false);
                    FormInput.remove(jPanel1);

                    BtnDropKulit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 
                    
                }
            });
            jPanel1.add(saveBtn, BorderLayout.PAGE_END);
            
        } else {
            kulitclick = 0;
            
            //Get the components in the panel
            Component[] componentsss = jPanel1.getComponents();
            
            //Loop through the components
            for(Component c : componentsss){

                //Find the components you want to remove
                
                if(c instanceof JPanel){
                    //Remove it
                    jPanel1.remove(c);
                    
                }
            }

            
            //IMPORTANT 
            jPanel1.revalidate();
            jPanel1.repaint();
            
            jPanel1.setVisible(false);
            FormInput.remove(jPanel1);
            
            BtnDropKulit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 

        }
        
    }//GEN-LAST:event_BtnDropKulitActionPerformed

    private void BtnDropKulitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDropKulitKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnDropKulitKeyPressed

    private void BtnKulitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKulitActionPerformed
        // TODO add your handling code here:
        MasterKulit kulit=new MasterKulit(null,false);
        kulit.isCek();
        kulit.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        kulit.setLocationRelativeTo(internalFrame1);
        kulit.setAlwaysOnTop(false);
        kulit.setVisible(true);
    }//GEN-LAST:event_BtnKulitActionPerformed

    private void BtnKulitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKulitKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnKulitKeyPressed

    private void TglDermaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TglDermaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TglDermaActionPerformed

    private void BtnDropJamDisposisiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDropJamDisposisiActionPerformed
        // TODO add your handling code here:
        timePicker1.showPopup(this, 100, 100);
    }//GEN-LAST:event_BtnDropJamDisposisiActionPerformed

    private void BtnDropJamDisposisiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDropJamDisposisiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnDropJamDisposisiKeyPressed

    private void TglAsuhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TglAsuhanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TglAsuhanActionPerformed

    private void TglAsuhanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TglAsuhanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TglAsuhanKeyPressed

    private void BtnKamarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKamarActionPerformed
        // TODO add your handling code here:
        
        dlgkamar.isCek();
        dlgkamar.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dlgkamar.setLocationRelativeTo(internalFrame1);
        dlgkamar.setAlwaysOnTop(false);
        dlgkamar.setVisible(true);
    }//GEN-LAST:event_BtnKamarActionPerformed

    private void BtnKamarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKamarKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnKamarKeyPressed

    private void BtnShowJamNeoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnShowJamNeoActionPerformed
        // TODO add your handling code here:
        if(timePicker2.isShowing()) {
            BtnShowJamNeo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/drowup-24.png"))); 
        } else{
            BtnShowJamNeo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/dropdown-24.png"))); 
        }
//        BtnShowJamNeo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/drowup-24.png"))); 
        timePicker2.showPopup(this, 100, 100);
        
        
    }//GEN-LAST:event_BtnShowJamNeoActionPerformed

    private void BtnShowJamNeoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnShowJamNeoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnShowJamNeoKeyPressed

    private void TglDerma1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TglDerma1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TglDerma1ActionPerformed

    private void TglDerma1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TglDerma1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TglDerma1KeyPressed

    private void NmPetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NmPetugasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NmPetugasActionPerformed

    private void KdPetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KdPetugasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KdPetugasActionPerformed

    private void BtnKamar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKamar1ActionPerformed
        // TODO add your handling code here:
        dlgkamar1.isCek();
        dlgkamar1.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dlgkamar1.setLocationRelativeTo(internalFrame1);
        dlgkamar1.setAlwaysOnTop(false);
        dlgkamar1.setVisible(true);
    }//GEN-LAST:event_BtnKamar1ActionPerformed

    private void BtnKamar1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKamar1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnKamar1KeyPressed

    private void rbHiperhidrosisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbHiperhidrosisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbHiperhidrosisActionPerformed

    private void rbAnshidrosisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbAnshidrosisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbAnshidrosisActionPerformed

    private void rbBolehPulangDisposisiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbBolehPulangDisposisiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbBolehPulangDisposisiActionPerformed

    private void rbKontrolNeonaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbKontrolNeonaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbKontrolNeonaActionPerformed

    private void JamKeluarDermaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JamKeluarDermaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JamKeluarDermaActionPerformed

    private void rbAdaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbAdaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbAdaActionPerformed

    private void rbDirawatDisposisiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbDirawatDisposisiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbDirawatDisposisiActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_saveBtnActionPerformed

    private void rbTidakAdaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbTidakAdaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbTidakAdaActionPerformed

    private void rbNormalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbNormalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbNormalActionPerformed

    private void rbKontrolDisposisiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbKontrolDisposisiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbKontrolDisposisiActionPerformed

    private void rbKelainanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbKelainanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbKelainanActionPerformed

    private void rbBolehPulangNeonaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbBolehPulangNeonaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbBolehPulangNeonaActionPerformed

    private void rbDirawatNeonaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbDirawatNeonaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbDirawatNeonaActionPerformed

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
    private widget.TextBox Abdomen;
    private widget.TextBox Babinski;
    private widget.Button BtnAbdomen;
    private widget.Button BtnAll;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnDokter;
    private widget.Button BtnDropAbdomen;
    private widget.Button BtnDropEkstremnitas;
    private widget.Button BtnDropJamDisposisi;
    private widget.Button BtnDropKepala;
    private widget.Button BtnDropKulit;
    private widget.Button BtnDropMata;
    private widget.Button BtnDropMulut;
    private widget.Button BtnDropPunggung;
    private widget.Button BtnDropStig;
    private widget.Button BtnDropSyaraf;
    private widget.Button BtnDropTaliPusat;
    private widget.Button BtnDropThorax;
    private widget.Button BtnDropTht;
    private widget.Button BtnDropUub;
    private widget.Button BtnEdit;
    private widget.Button BtnEkstremnitas;
    private javax.swing.ButtonGroup BtnGrpAnus;
    private javax.swing.ButtonGroup BtnGrpDisposisiDerma;
    private javax.swing.ButtonGroup BtnGrpDisposisiNeona;
    private javax.swing.ButtonGroup BtnGrpFungsiKelenjarKeringat;
    private javax.swing.ButtonGroup BtnGrpGenetalia;
    private javax.swing.ButtonGroup BtnGrpReflek;
    private widget.Button BtnHapus;
    private widget.Button BtnKamar;
    private widget.Button BtnKamar1;
    private widget.Button BtnKeluar;
    private widget.Button BtnKepala;
    private widget.Button BtnKulit;
    private widget.Button BtnMata;
    private widget.Button BtnMulut;
    private widget.Button BtnPrint;
    private widget.Button BtnPunggung;
    private widget.Button BtnShowJamNeo;
    private widget.Button BtnSimpan;
    private widget.Button BtnStigAtopik;
    private widget.Button BtnSyaraf;
    private widget.Button BtnTaliPusat;
    private widget.Button BtnThorax;
    private widget.Button BtnTht;
    private widget.Button BtnUub;
    private widget.CekBox ChkAccor;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.TextArea DetailRencana;
    private widget.TextArea DiagnosaKerjaDerma;
    private widget.TextArea DiagnosaKerjaNeonatologi;
    private widget.TextArea Eflorisensi;
    private widget.TextBox Ekstremnitas;
    private widget.PanelBiasa FormInput;
    private widget.PanelBiasa FormMasalahRencana;
    private widget.PanelBiasa FormMenu;
    private widget.TextBox Genetalia;
    private widget.TextBox Grasping;
    private widget.TextArea HasilPenunjangDerma;
    private widget.TextArea HasilPenunjangNeonatologi;
    private widget.TextBox JamKeluarDerma;
    private widget.TextBox JamNeonatologi;
    private widget.TextBox Jk;
    private widget.TextBox KdKamar;
    private widget.TextBox KdKamar1;
    private widget.TextBox KdPetugas;
    private widget.TextBox KelenjarLimfe;
    private widget.TextArea KeluhanUtama;
    private widget.TextBox Kepala;
    private widget.TextBox Kuku;
    private widget.TextBox Kulit;
    private widget.Label LCount;
    private widget.TextBox Lainnya;
    private widget.editorpane LoadHTML;
    private widget.TextArea Lokalisasi;
    private widget.TextBox Mata;
    private widget.TextBox Moro;
    private widget.TextBox Mukosa;
    private widget.TextBox Mulut;
    private widget.TextBox NmKamar;
    private widget.TextBox NmKamar1;
    private widget.TextBox NmPetugas;
    private widget.TextBox NoBed;
    private widget.TextBox NoBed1;
    private widget.PanelBiasa PanelAccor;
    private widget.TextBox Punggung;
    private widget.TextBox Rambut;
    private widget.TextArea RencanaKerjaDerma;
    private widget.TextArea RencanaKerjaNeonatologi;
    private widget.TextArea RiwPenyakitSkrg;
    private widget.TextBox Root;
    private widget.ScrollPane Scroll;
    private widget.TextBox Step;
    private widget.TextBox StigAtopik;
    private widget.TextBox Suck;
    private widget.TextBox Swallow;
    private widget.TextBox Syaraf;
    private widget.TextBox TCari;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRM1;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.TextBox TPasien1;
    private javax.swing.JTabbedPane TabRawat;
    private widget.TextBox TaliPusat;
    private widget.TextBox TempatDerma;
    private widget.TextBox TempatNeonatologi;
    private widget.TextArea TerapiDerma;
    private widget.TextArea TerapiNeonatologi;
    private widget.Tanggal TglAsuhan;
    private widget.Tanggal TglDerma;
    private widget.Tanggal TglDerma1;
    private widget.TextBox TglLahir;
    private widget.TextBox Thorax;
    private widget.TextBox Tht;
    private widget.TextBox Tonick;
    private widget.TextBox Turgor;
    private widget.TextBox Uub;
    private widget.TextBox WarnaMukosa;
    private javax.swing.JCheckBox cbBabinski;
    private javax.swing.JCheckBox cbGrasping;
    private javax.swing.JCheckBox cbLainnya;
    private javax.swing.JCheckBox cbMoro;
    private javax.swing.JCheckBox cbRoot;
    private javax.swing.JCheckBox cbStep;
    private javax.swing.JCheckBox cbSuck;
    private javax.swing.JCheckBox cbSwallow;
    private javax.swing.JCheckBox cbTonick;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.InternalFrame internalFrame3;
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
    private widget.Label jLabel77;
    private widget.Label jLabel78;
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
    private javax.swing.JPanel pStig;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private javax.swing.JRadioButton rbAda;
    private javax.swing.JRadioButton rbAnshidrosis;
    private javax.swing.JRadioButton rbBolehPulangDisposisi;
    private javax.swing.JRadioButton rbBolehPulangNeona;
    private javax.swing.JRadioButton rbDirawatDisposisi;
    private javax.swing.JRadioButton rbDirawatNeona;
    private javax.swing.JRadioButton rbHiperhidrosis;
    private javax.swing.JRadioButton rbKelainan;
    private javax.swing.JRadioButton rbKontrolDisposisi;
    private javax.swing.JRadioButton rbKontrolNeona;
    private javax.swing.JRadioButton rbNormal;
    private javax.swing.JRadioButton rbTidakAda;
    private javax.swing.JButton saveBtn;
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
    private com.raven.swing.TimePicker timePicker1;
    private com.raven.swing.TimePicker timePicker2;
    // End of variables declaration//GEN-END:variables

    private void tampil() {
        Valid.tabelKosong(tabMode);
        
        try{
            if(TCari.getText().equals("")){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,if(pasien.jk='L','Laki-Laki','Perempuan') as jk,pasien.tgl_lahir, " +
                    "tb_asesmen_awal_medis_kulit_kelamin.tanggal, " +
                            "tb_asesmen_awal_medis_kulit_kelamin.kd_dokter,dokter.nm_dokter, " +
                            "tb_asesmen_awal_medis_kulit_kelamin.`keluhan_utama`, " +
                    "tb_asesmen_awal_medis_kulit_kelamin.`riwayat_penyakit_sekarang`,tb_asesmen_awal_medis_kulit_kelamin.`lokalisasi`, " +
                    "tb_asesmen_awal_medis_kulit_kelamin.`eflorisensi`,tb_asesmen_awal_medis_kulit_kelamin.`mukosa`, " +
                    "tb_asesmen_awal_medis_kulit_kelamin.`rambut`,tb_asesmen_awal_medis_kulit_kelamin.`kuku`," +
                    "tb_asesmen_awal_medis_kulit_kelamin.`fungsi_kelenjar_keringat`,tb_asesmen_awal_medis_kulit_kelamin.`kelenjar_limfe`, " +
                    "tb_asesmen_awal_medis_kulit_kelamin.`hasil_penunjang_derma`,tb_asesmen_awal_medis_kulit_kelamin.`diag_kerja_derma`, " +
                    "tb_asesmen_awal_medis_kulit_kelamin.`rencana_kerja_derma`,tb_asesmen_awal_medis_kulit_kelamin.`terapi_derma`, " +
                    "tb_asesmen_awal_medis_kulit_kelamin.`disposisi_derma`,tb_asesmen_awal_medis_kulit_kelamin.`jam_disposisi_derma`, " +
                    "tb_asesmen_awal_medis_kulit_kelamin.`tgl_disposisi_derma`,tb_asesmen_awal_medis_kulit_kelamin.`tempat_disposisi_derma`, " +
                    "tb_asesmen_awal_medis_kulit_kelamin.`ruang_disposisi_derma`,tb_asesmen_awal_medis_kulit_kelamin.`warna_mukosa`, " +
                    "tb_asesmen_awal_medis_kulit_kelamin.`anus`,tb_asesmen_awal_medis_kulit_kelamin.`turgor`, " +
                    "tb_asesmen_awal_medis_kulit_kelamin.`hasil_penunjang_neonatologi`,tb_asesmen_awal_medis_kulit_kelamin.`diag_kerja_neonatologi`, " +
                    "tb_asesmen_awal_medis_kulit_kelamin.`rencana_kerja_neonatologi`,tb_asesmen_awal_medis_kulit_kelamin.`terapi_neonatologi`, " +
                    "tb_asesmen_awal_medis_kulit_kelamin.`disposisi_neonatologi`,tb_asesmen_awal_medis_kulit_kelamin.`jam_disposisi_neona`, " +
                    "tb_asesmen_awal_medis_kulit_kelamin.`tgl_disposisi_neonatologi`,tb_asesmen_awal_medis_kulit_kelamin.`tempat_disposisi_neonatologi`, " +
                    "tb_asesmen_awal_medis_kulit_kelamin.`ruang_disposisi_neonatologi` " +
                    "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis " +
                    "inner join tb_asesmen_awal_medis_kulit_kelamin on reg_periksa.no_rawat=tb_asesmen_awal_medis_kulit_kelamin.no_rawat " +
                    "inner join dokter on tb_asesmen_awal_medis_kulit_kelamin.kd_dokter=dokter.kd_dokter " +
                    "inner join penjab on penjab.kd_pj=reg_periksa.kd_pj " +
                    "where " +
                    "tb_asesmen_awal_medis_kulit_kelamin.tanggal between ? and ? " +
                    "and pasien.no_rkm_medis=? " +
                    "order by tb_asesmen_awal_medis_kulit_kelamin.tanggal ");
            }else{
                ps=koneksi.prepareStatement(
                        "SELECT reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,IF(pasien.jk='L','Laki-Laki','Perempuan') AS jk,pasien.tgl_lahir, " +
                        "tb_asesmen_awal_medis_kulit_kelamin.tanggal, " +
                                "tb_asesmen_awal_medis_kulit_kelamin.kd_dokter,dokter.nm_dokter, " +
                                "tb_asesmen_awal_medis_kulit_kelamin.`keluhan_utama`, " +
                        "tb_asesmen_awal_medis_kulit_kelamin.`riwayat_penyakit_sekarang`,tb_asesmen_awal_medis_kulit_kelamin.`lokalisasi`, " +
                        "tb_asesmen_awal_medis_kulit_kelamin.`eflorisensi`,tb_asesmen_awal_medis_kulit_kelamin.`mukosa`, " +
                        "tb_asesmen_awal_medis_kulit_kelamin.`rambut`,tb_asesmen_awal_medis_kulit_kelamin.`kuku`, " +
                        "tb_asesmen_awal_medis_kulit_kelamin.`fungsi_kelenjar_keringat`,tb_asesmen_awal_medis_kulit_kelamin.`kelenjar_limfe`, " +
                        "tb_asesmen_awal_medis_kulit_kelamin.`hasil_penunjang_derma`,tb_asesmen_awal_medis_kulit_kelamin.`diag_kerja_derma`, " +
                        "tb_asesmen_awal_medis_kulit_kelamin.`rencana_kerja_derma`,tb_asesmen_awal_medis_kulit_kelamin.`terapi_derma`, " +
                        "tb_asesmen_awal_medis_kulit_kelamin.`disposisi_derma`,tb_asesmen_awal_medis_kulit_kelamin.`jam_disposisi_derma`, " +
                        "tb_asesmen_awal_medis_kulit_kelamin.`tgl_disposisi_derma`,tb_asesmen_awal_medis_kulit_kelamin.`tempat_disposisi_derma`, " +
                        "tb_asesmen_awal_medis_kulit_kelamin.`ruang_disposisi_derma`,tb_asesmen_awal_medis_kulit_kelamin.`warna_mukosa`, " +
                        "tb_asesmen_awal_medis_kulit_kelamin.`anus`,tb_asesmen_awal_medis_kulit_kelamin.`turgor`, " +
                        "tb_asesmen_awal_medis_kulit_kelamin.`hasil_penunjang_neonatologi`,tb_asesmen_awal_medis_kulit_kelamin.`diag_kerja_neonatologi`, " +
                        "tb_asesmen_awal_medis_kulit_kelamin.`rencana_kerja_neonatologi`,tb_asesmen_awal_medis_kulit_kelamin.`terapi_neonatologi`, " +
                        "tb_asesmen_awal_medis_kulit_kelamin.`disposisi_neonatologi`,tb_asesmen_awal_medis_kulit_kelamin.`jam_disposisi_neona`, " +
                        "tb_asesmen_awal_medis_kulit_kelamin.`tgl_disposisi_neonatologi`,tb_asesmen_awal_medis_kulit_kelamin.`tempat_disposisi_neonatologi`, " +
                        "tb_asesmen_awal_medis_kulit_kelamin.`ruang_disposisi_neonatologi` " +
                        "FROM reg_periksa INNER JOIN pasien ON reg_periksa.no_rkm_medis=pasien.no_rkm_medis " +
                        "INNER JOIN tb_asesmen_awal_medis_kulit_kelamin ON reg_periksa.no_rawat=tb_asesmen_awal_medis_kulit_kelamin.no_rawat " +
                        "INNER JOIN dokter ON tb_asesmen_awal_medis_kulit_kelamin.kd_dokter=dokter.kd_dokter " +
                        "INNER JOIN bahasa_pasien ON bahasa_pasien.id=pasien.bahasa_pasien " +
                        "INNER JOIN penjab ON penjab.kd_pj=reg_periksa.kd_pj " +
                        "INNER JOIN cacat_fisik ON cacat_fisik.id=pasien.cacat_fisik WHERE " +
                        "tb_asesmen_awal_medis_kulit_kelamin.tanggal BETWEEN ? AND ? AND " +
                        "pasien.no_rkm_medis=? AND " +
                        "(reg_periksa.no_rawat LIKE ? OR pasien.no_rkm_medis LIKE ? OR pasien.nm_pasien LIKE ? OR " +
                        "tb_asesmen_awal_medis_kulit_kelamin.kd_dokter LIKE ? OR dokter.nm_dokter LIKE ?) " +
                        "ORDER BY tb_asesmen_awal_medis_kulit_kelamin.tanggal");
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
                System.out.println(ps);
                rs=ps.executeQuery();
                while(rs.next()){
                    tabMode.addRow(new String[]{
                        rs.getString("no_rawat"),rs.getString("no_rkm_medis"),
                        rs.getString("nm_pasien"),rs.getString("jk"),
                        rs.getString("tgl_lahir"),rs.getString("tanggal"),
                        rs.getString("keluhan_utama"),rs.getString("riwayat_penyakit_sekarang"),
                        rs.getString("lokalisasi"),rs.getString("eflorisensi"),
                        rs.getString("mukosa"),rs.getString("rambut"),
                        rs.getString("kuku"),rs.getString("fungsi_kelenjar_keringat"),
                        rs.getString("kelenjar_limfe"),rs.getString("hasil_penunjang_derma"),
                        rs.getString("diag_kerja_derma"),rs.getString("rencana_kerja_derma"),
                        rs.getString("terapi_derma"),rs.getString("disposisi_derma"),
                        rs.getString("jam_disposisi_derma"),rs.getString("tgl_disposisi_derma"),
                        rs.getString("tempat_disposisi_derma"),rs.getString("ruang_disposisi_derma"),
                        rs.getString("warna_mukosa"),rs.getString("anus"),
                        rs.getString("turgor"),rs.getString("hasil_penunjang_neonatologi"),
                        rs.getString("diag_kerja_neonatologi"),rs.getString("rencana_kerja_neonatologi"),
                        rs.getString("terapi_neonatologi"),rs.getString("disposisi_neonatologi"),
                        rs.getString("jam_disposisi_neona"),rs.getString("tgl_disposisi_neonatologi"),
                        rs.getString("tempat_disposisi_neonatologi"),rs.getString("ruang_disposisi_neonatologi"),
                        rs.getString("kd_dokter"),rs.getString("nm_dokter")
                        
                    });
                }
            
            if(tabMode.getColumnCount() != 0) {
                TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()); 
                TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
                TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString()); 
                Jk.setText(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString()); 
                TglLahir.setText(tbObat.getValueAt(tbObat.getSelectedRow(),4).toString()); 
                Valid.SetTgl2(TglAsuhan,tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
                KeluhanUtama.setText(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString()); 
                RiwPenyakitSkrg.setText(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString()); 
                Lokalisasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),8).toString());
                Eflorisensi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString());
                Mukosa.setText(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString());
                Rambut.setText(tbObat.getValueAt(tbObat.getSelectedRow(),11).toString());
                Kuku.setText(tbObat.getValueAt(tbObat.getSelectedRow(),12).toString());

                if(tbObat.getValueAt(tbObat.getSelectedRow(),13).toString()== "Hiperhidrosis") {
                    rbHiperhidrosis.isSelected();
                } else {
                    rbAnshidrosis.isSelected();
                }

                KelenjarLimfe.setText(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
                HasilPenunjangDerma.setText(tbObat.getValueAt(tbObat.getSelectedRow(),15).toString());
                DiagnosaKerjaDerma.setText(tbObat.getValueAt(tbObat.getSelectedRow(),16).toString());
                RencanaKerjaDerma.setText(tbObat.getValueAt(tbObat.getSelectedRow(),17).toString());
                TerapiDerma.setText(tbObat.getValueAt(tbObat.getSelectedRow(),18).toString());
                if(tbObat.getValueAt(tbObat.getSelectedRow(),19).toString()== "Boleh Pulang") {
                    rbBolehPulangDisposisi.isSelected();
                } else if(tbObat.getValueAt(tbObat.getSelectedRow(),19).toString()== "Kontrol"){
                    rbKontrolDisposisi.isSelected();
                } else {
                    rbDirawatDisposisi.isSelected();
                }
                JamKeluarDerma.setText(tbObat.getValueAt(tbObat.getSelectedRow(),20).toString());
                Valid.SetTgl2(TglDerma,tbObat.getValueAt(tbObat.getSelectedRow(),21).toString());

                TempatDerma.setText(tbObat.getValueAt(tbObat.getSelectedRow(),22).toString());
                NoBed.setText(tbObat.getValueAt(tbObat.getSelectedRow(),23).toString());
                WarnaMukosa.setText(tbObat.getValueAt(tbObat.getSelectedRow(),24).toString());
                if(tbObat.getValueAt(tbObat.getSelectedRow(),25).toString()== "Ada") {
                    rbAda.isSelected();
                } else {
                    rbTidakAda.isSelected();
                }

                Turgor.setText(tbObat.getValueAt(tbObat.getSelectedRow(),26).toString());
                HasilPenunjangNeonatologi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),27).toString());
                DiagnosaKerjaNeonatologi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),28).toString());
                RencanaKerjaNeonatologi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),29).toString());
                TerapiNeonatologi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),30).toString());
                if(tbObat.getValueAt(tbObat.getSelectedRow(),31).toString()== "Boleh Pulang") {
                    rbBolehPulangNeona.isSelected();
                } else if(tbObat.getValueAt(tbObat.getSelectedRow(),31).toString()== "Kontrol"){
                    rbKontrolNeona.isSelected();
                } else {
                    rbDirawatNeona.isSelected();
                }
                JamKeluarDerma.setText(tbObat.getValueAt(tbObat.getSelectedRow(),32).toString());
                Valid.SetTgl2(TglDerma1,tbObat.getValueAt(tbObat.getSelectedRow(),33).toString());

                TempatNeonatologi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),34).toString());
                NoBed1.setText(tbObat.getValueAt(tbObat.getSelectedRow(),35).toString());
                KdPetugas.setText(tbObat.getValueAt(tbObat.getSelectedRow(),36).toString());
                NmPetugas.setText(tbObat.getValueAt(tbObat.getSelectedRow(),37).toString());
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
    
    

    private void tampilStigAtopik() {
        Valid.tabelKosong(tabModeStig);
        try {
            stat=koneksi.prepareStatement(
                   "select stigmata_atopik, id from tb_stigmata_atopik where status=1");
            try{
                
                rs=stat.executeQuery();
        
                while(rs.next()){
                    tabModeStig.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2)
                                   }); 
                }
                
                
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                e.printStackTrace();
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(stat != null){
                    stat.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
            e.printStackTrace();
        }
        LCount.setText(""+tabModeStig.getRowCount());
    }

    private void tampilSyaraf() {
        Valid.tabelKosong(tabModeSyaraf);
        try {
            stat = koneksi.prepareStatement(
                    "select syaraf, id from tb_syaraf where status=1");
            try {

                rs = stat.executeQuery();

                while (rs.next()) {
                    tabModeSyaraf.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2)
                    });
                }

            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
                e.printStackTrace();
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (stat != null) {
                    stat.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
            e.printStackTrace();
        }
        LCount.setText("" + tabModeSyaraf.getRowCount());
    }
    
    private void tampilKepala() {
        Valid.tabelKosong(tabModeKepala);
        try {
            stat=koneksi.prepareStatement(
                   "select kepala, id from tb_kepala where status=1");
            try{
                
                rs=stat.executeQuery();
        
                while(rs.next()){
                    tabModeKepala.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2)
                                   }); 
                }
                
                
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                e.printStackTrace();
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(stat != null){
                    stat.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
            e.printStackTrace();
        }
        LCount.setText(""+tabModeKepala.getRowCount());
    }
    
    private void tampilUub() {
        Valid.tabelKosong(tabModeUub);
        try {
            stat=koneksi.prepareStatement(
                   "select uub, id from tb_uub where status=1");
            try{
                
                rs=stat.executeQuery();
        
                while(rs.next()){
                    tabModeUub.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2)
                                   }); 
                }
                
                
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                e.printStackTrace();
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(stat != null){
                    stat.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
            e.printStackTrace();
        }
        LCount.setText(""+tabModeUub.getRowCount());
    }
    
    private void tampilMata() {
        Valid.tabelKosong(tabModeMata);
        try {
            stat=koneksi.prepareStatement(
                   "select mata, id from tb_mata where status=1");
            try{
                
                rs=stat.executeQuery();
        
                while(rs.next()){
                    tabModeMata.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2)
                                   }); 
                }
                
                
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                e.printStackTrace();
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(stat != null){
                    stat.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
            e.printStackTrace();
        }
        LCount.setText(""+tabModeMata.getRowCount());
    }
    
    private void tampilTht() {
        Valid.tabelKosong(tabModeTht);
        try {
            stat=koneksi.prepareStatement(
                   "select tht, id from tb_tht where status=1");
            try{
                
                rs=stat.executeQuery();
        
                while(rs.next()){
                    tabModeTht.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2)
                                   }); 
                }
                
                
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                e.printStackTrace();
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(stat != null){
                    stat.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
            e.printStackTrace();
        }
        LCount.setText(""+tabModeTht.getRowCount());
    }
    
    private void tampilMulut() {
        Valid.tabelKosong(tabModeMulut);
        try {
            stat = koneksi.prepareStatement(
                    "select mulut, id from tb_mulut where status=1");
            try {

                rs = stat.executeQuery();

                while (rs.next()) {
                    tabModeMulut.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2)
                    });
                }

            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
                e.printStackTrace();
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (stat != null) {
                    stat.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
            e.printStackTrace();
        }
        LCount.setText("" + tabModeMulut.getRowCount());
    }
    
    private void tampilThorax() {
        Valid.tabelKosong(tabModeThorax);
        try {
            stat=koneksi.prepareStatement(
                   "select thorax, id from tb_thorax where status=1");
            try{
                
                rs=stat.executeQuery();
        
                while(rs.next()){
                    tabModeThorax.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2)
                                   }); 
                }
                
                
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                e.printStackTrace();
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(stat != null){
                    stat.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
            e.printStackTrace();
        }
        LCount.setText(""+tabModeThorax.getRowCount());
    }
    
    private void tampilAbdomen() {
        Valid.tabelKosong(tabModeAbdomen);
        try {
            stat=koneksi.prepareStatement(
                   "select abdomen, id from tb_abdomen where status=1");
            try{
                
                rs=stat.executeQuery();
        
                while(rs.next()){
                    tabModeAbdomen .addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2)
                                   }); 
                }
                
                
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                e.printStackTrace();
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(stat != null){
                    stat.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
            e.printStackTrace();
        }
        LCount.setText(""+tabModeAbdomen.getRowCount());
    }
    
    private void tampilTaliPusat() {
        Valid.tabelKosong(tabModeTaliPusat);
        try {
            stat=koneksi.prepareStatement(
                   "select tali_pusat, id from tb_tali_pusat where status=1");
            try{
                
                rs=stat.executeQuery();
        
                while(rs.next()){
                    tabModeTaliPusat.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2)
                                   }); 
                }
                
                
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                e.printStackTrace();
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(stat != null){
                    stat.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
            e.printStackTrace();
        }
        LCount.setText(""+tabModeTaliPusat.getRowCount());
    }
    
    private void tampilPunggung() {
        Valid.tabelKosong(tabModePunggung);
        try {
            stat=koneksi.prepareStatement(
                   "select punggung, id from tb_punggung where status=1");
            try{
                
                rs=stat.executeQuery();
        
                while(rs.next()){
                    tabModePunggung.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2)
                                   }); 
                }
                
                
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                e.printStackTrace();
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(stat != null){
                    stat.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
            e.printStackTrace();
        }
        LCount.setText(""+tabModePunggung.getRowCount());
    }
    
    private void tampilEkstremnitas() {
        Valid.tabelKosong(tabModeEkstremnitas);
        try {
            stat=koneksi.prepareStatement(
                   "select ekstremnitas, id from tb_ekstremnitas where status=1");
            try{
                
                rs=stat.executeQuery();
        
                while(rs.next()){
                    tabModeEkstremnitas.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2)
                                   }); 
                }
                
                
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                e.printStackTrace();
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(stat != null){
                    stat.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
            e.printStackTrace();
        }
        LCount.setText(""+tabModeEkstremnitas.getRowCount());
    }
    
    private void tampilKulit() {
        Valid.tabelKosong(tabModeKulit);
        try {
            stat=koneksi.prepareStatement(
                   "select kulit, id from tb_kulit where status=1");
            try{
                
                rs=stat.executeQuery();
        
                while(rs.next()){
                    tabModeKulit.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2)
                                   }); 
                }
                
                
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                e.printStackTrace();
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(stat != null){
                    stat.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
            e.printStackTrace();
        }
        LCount.setText(""+tabModeKulit.getRowCount());
    }
   
    public void emptTeks() {
        TglAsuhan.setDate(new Date());
        TglDerma.setDate(new Date());
        TglDerma1.setDate(new Date());
        KeluhanUtama.setText("");
        RiwPenyakitSkrg.setText("");
        Lokalisasi.setText("");
        Mukosa.setText("");
        Rambut.setText("");
        Kuku.setText("");
        KelenjarLimfe.setText("");
        HasilPenunjangDerma.setText("");
        DiagnosaKerjaDerma.setText("");
        RencanaKerjaDerma.setText("");
        TerapiDerma.setText("");
        TempatDerma.setText("");
        NoBed.setText("");
        WarnaMukosa.setText("");
        Turgor.setText("");
        HasilPenunjangNeonatologi.setText("");
        DiagnosaKerjaNeonatologi.setText("");
        RencanaKerjaNeonatologi.setText("");
        TerapiNeonatologi.setText("");
        TempatNeonatologi.setText("");
        NoBed1.setText("");
        Eflorisensi.setText("");
        StigAtopik.setText("");
        Syaraf.setText("");
        Kepala.setText("");
        Uub.setText("");
        Mata.setText("");
        Tht.setText("");
        Mulut.setText("");
        Thorax.setText("");
        Abdomen.setText("");
        TaliPusat.setText("");
        Punggung.setText("");
        Genetalia.setText("");
        Ekstremnitas.setText("");
        Kulit.setText("");
        
        
        
        fkelenjarkeringat = "";
        disposisiderma = "";
        anus = "";
        reflek = "";
        disposisineona = "";
        
        BtnGrpFungsiKelenjarKeringat.clearSelection();
        BtnGrpDisposisiDerma.clearSelection();
        BtnGrpAnus.clearSelection();
        BtnGrpReflek.clearSelection();
        BtnGrpDisposisiNeona.clearSelection();
        
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
            kd_petugas = tbObat.getValueAt(tbObat.getSelectedRow(),5).toString();
            nm_petugas = tbObat.getValueAt(tbObat.getSelectedRow(),6).toString();
            
            
            TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()); 
            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString()); 
            Jk.setText(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString()); 
            TglLahir.setText(tbObat.getValueAt(tbObat.getSelectedRow(),4).toString()); 
            Valid.SetTgl2(TglAsuhan,tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
            KeluhanUtama.setText(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString()); 
            RiwPenyakitSkrg.setText(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString()); 
            Lokalisasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),8).toString());
            Eflorisensi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString());
            Mukosa.setText(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString());
            Rambut.setText(tbObat.getValueAt(tbObat.getSelectedRow(),11).toString());
            Kuku.setText(tbObat.getValueAt(tbObat.getSelectedRow(),12).toString());
            
            System.out.println(tbObat.getValueAt(tbObat.getSelectedRow(),13).toString());
            
            if(tbObat.getValueAt(tbObat.getSelectedRow(),13).toString()== "Hiperhidrosis") {
                
                rbHiperhidrosis.isSelected();
            } else {
                rbAnshidrosis.isSelected();
            }
            
            KelenjarLimfe.setText(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
            HasilPenunjangDerma.setText(tbObat.getValueAt(tbObat.getSelectedRow(),15).toString());
            DiagnosaKerjaDerma.setText(tbObat.getValueAt(tbObat.getSelectedRow(),16).toString());
            RencanaKerjaDerma.setText(tbObat.getValueAt(tbObat.getSelectedRow(),17).toString());
            TerapiDerma.setText(tbObat.getValueAt(tbObat.getSelectedRow(),18).toString());
            
            
            if(tbObat.getValueAt(tbObat.getSelectedRow(),19).toString()== "Boleh pulang") {
                rbBolehPulangDisposisi.isSelected();
            } else if(tbObat.getValueAt(tbObat.getSelectedRow(),19).toString()== "Kontrol"){
                rbKontrolDisposisi.isSelected();
            } else {
                rbDirawatDisposisi.isSelected();
            }
            JamKeluarDerma.setText(tbObat.getValueAt(tbObat.getSelectedRow(),20).toString());
            Valid.SetTgl2(TglDerma,tbObat.getValueAt(tbObat.getSelectedRow(),21).toString());
            
            TempatDerma.setText(tbObat.getValueAt(tbObat.getSelectedRow(),22).toString());
            NoBed.setText(tbObat.getValueAt(tbObat.getSelectedRow(),23).toString());
            WarnaMukosa.setText(tbObat.getValueAt(tbObat.getSelectedRow(),24).toString());
            
            System.out.println(tbObat.getValueAt(tbObat.getSelectedRow(),25).toString());
            if(tbObat.getValueAt(tbObat.getSelectedRow(),25).toString()== "Ada") {
                rbAda.isSelected();
            } else {
                rbTidakAda.isSelected();
            }
            
            Turgor.setText(tbObat.getValueAt(tbObat.getSelectedRow(),26).toString());
            HasilPenunjangNeonatologi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),27).toString());
            DiagnosaKerjaNeonatologi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),28).toString());
            RencanaKerjaNeonatologi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),29).toString());
            TerapiNeonatologi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),30).toString());
            if(tbObat.getValueAt(tbObat.getSelectedRow(),31).toString()== "Boleh pulang") {
                rbBolehPulangNeona.isSelected();
            } else if(tbObat.getValueAt(tbObat.getSelectedRow(),31).toString()== "Kontrol"){
                rbKontrolNeona.isSelected();
            } else {
                rbDirawatNeona.isSelected();
            }
            JamKeluarDerma.setText(tbObat.getValueAt(tbObat.getSelectedRow(),32).toString());
            Valid.SetTgl2(TglDerma1,tbObat.getValueAt(tbObat.getSelectedRow(),33).toString());
            
            TempatNeonatologi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),34).toString());
            NoBed1.setText(tbObat.getValueAt(tbObat.getSelectedRow(),35).toString());
            KdPetugas.setText(tbObat.getValueAt(tbObat.getSelectedRow(),36).toString());
            NmPetugas.setText(tbObat.getValueAt(tbObat.getSelectedRow(),37).toString());
            
            
            StigAtopik.setText("");
            Syaraf.setText("");
            Kepala.setText("");
            Uub.setText("");
            Mata.setText("");
            Tht.setText("");
            Mulut.setText("");
            Thorax.setText("");
            Abdomen.setText("");
            TaliPusat.setText("");
            Punggung.setText("");
            Genetalia.setText("");
            Ekstremnitas.setText("");
            Kulit.setText("");
            
            
            
//            DetailRencana.setText("Ginjal Kanan: \n"+ginjal_kanan + "\n\nGinjal Kiri: \n"+ginjal_kiri+"\n\nBuli: \n" +sbuli);
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
                    Jk.setText(rs.getString("jk"));
                    TglLahir.setText(rs.getString("tgl_lahir"));
                }
            } catch (Exception e) {
                System.out.println("Notif israwat: "+e);
                e.printStackTrace();
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif israwatt: "+e);
            e.printStackTrace();
        }
    }
    
    public void setNoRm(String norwt, Date tgl2) {
        TNoRw.setText(norwt);
        TCari.setText(norwt);
        DTPCari2.setDate(tgl2);    
        isRawat();
    }
    
    private void isReady(){
        JamKeluarDerma.setEnabled(false);
        BtnDropJamDisposisi.setEnabled(false);
        TglDerma.setEnabled(false);
        TempatDerma.setEnabled(false);
        NoBed.setEnabled(false);
        KdKamar.setEnabled(false);
        NmKamar.setEnabled(false);
        BtnKamar.setEnabled(false);
        Genetalia.setEnabled(false);
        Moro.setEnabled(false);
        Tonick.setEnabled(false);
        Lainnya.setEnabled(false);
        Grasping.setEnabled(false);
        Swallow.setEnabled(false);
        Suck.setEnabled(false);
        Step.setEnabled(false);
        Root.setEnabled(false);
        Babinski.setEnabled(false);
        JamNeonatologi.setEnabled(false);
        BtnShowJamNeo.setEnabled(false);
        TglDerma1.setEnabled(false);
        TempatNeonatologi.setEnabled(false);
        NoBed1.setEnabled(false);
        KdKamar1.setEnabled(false);
        NmKamar1.setEnabled(false);
        BtnKamar1.setEnabled(false);
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
       isReady();
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
