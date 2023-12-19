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
public final class RMImplementasiKeperawatan extends javax.swing.JDialog {
    private final DefaultTableModel tabMode,tabModeMasalah,tabModeDetailMasalah;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps,ps2;
    private ResultSet rs,rs2;
    private int i=0,jml=0,index=0;
    private DlgCariPetugas petugas=new DlgCariPetugas(null,false);
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
    String no_rawat,no_rm,pasien,jk,tgllahir,respon,tindakan,
            id,kd_petugas,nm_petugas,tgl_asuhan,finger="",pilihan="",kamar,namakamar,alamat,umur;
    
    /** Creates new form DlgRujuk
     * @param parent
     * @param modal */
    public RMImplementasiKeperawatan(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        txtId.setVisible(false);
        
        tabMode=new DefaultTableModel(null,new Object[]{
            "No.Rawat","No.RM","Nama Pasien","J.K.",
//            "Agama",
//            "Bahasa","Cacat Fisik",
            "Tgl.Lahir","Tgl Periksa",
//            "Informasi",
            "Ginjal Kanan","Ginjal Kiri","Buli",
//                "Riwayat Penggunaan obat",
//            "Status Hamil","Gravida","Para","Abortus","HPHT","Tekanan Intrakranial","Pupil","Neurosensorik/Muskuloskeletal","Integumen","Turgor Kulit","Edema","Mukosa Mulut","Perdarahan",
//            "Jml Perdarahan (cc)","Warna Perdarahan","Intoksikasi","Frekuensi BAB","x/","Konsistensi BAB","Warna BAB","Frekuensi BAK","x/","Warna BAK","Lain-lain BAK","Kondisi Psikologis",
//            "Gangguan Jiwa Di Masa Lalu","Adakah Perilaku","Dilaporkan Ke","Sebutkan","Hubungan Pasien Dengan Anggota Keluarga","Status Pernikahan","Tinggal Dengan","Ket. Tinggal Dengan",
//            "Pekerjaan","Pembayaran","Nilai-nilai Kebudayaan","Ket. Nilai-nilai Kebudayaan","Pendidikan Pasien","Pendidikan PJ","Ket. Pendidikan PJ",
//            "Edukasi Diberikan Kepada","Ket. Edukasi Diberikan Kepada","Kemampuan Aktifitas Sehari-hari","Aktifitas","Alat bantu","Ket. Alat bantu",
//            "Tingkat Nyeri","Provokes","Ket. Provokes","Kualitas","Ket. Kualitas","Lokasi","Menyebar","Skala Nyeri","Durasi","Nyeri Hilang","Ket. Hilang Nyeri","Lapor Ke Dokter",
//            "Jam Lapor","Cara Berjalan A","Cara Berjalan B","Cara Berjalan C","Hasil Penilaian Resiko Jatuh","Lapor Dokter","Ket. Lapor","Rencana",
                "Kode Petugas","Nama Petugas"
        }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbObat.setModel(tabMode);

//        tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

//        81
//        for (i = 0; i < 3; i++) {
//            TableColumn column = tbObat.getColumnModel().getColumn(i);
//            if(i==0){
//                column.setPreferredWidth(105);
//            }else if(i==1){
//                column.setPreferredWidth(65);
//            }
//            else if(i==2){
//                column.setPreferredWidth(160);
//            }else if(i==3){
//                column.setPreferredWidth(50);
//            }else if(i==4){
//                column.setPreferredWidth(60);
//            }else if(i==5){
//                column.setPreferredWidth(90);
//            }else if(i==6){
//                column.setPreferredWidth(90);
//            }else if(i==7){
//                column.setPreferredWidth(65);
//            }else if(i==8){
//                column.setPreferredWidth(120);
//            }else if(i==9){
//                column.setPreferredWidth(90);
//            }else if(i==10){
//                column.setPreferredWidth(200);
//            }else if(i==11){
//                column.setPreferredWidth(200);
//            }else if(i==12){
//                column.setPreferredWidth(200);
//            }else if(i==13){
//                column.setPreferredWidth(70);
//            }else if(i==14){
//                column.setPreferredWidth(50);
//            }else if(i==15){
//                column.setPreferredWidth(50);
//            }else if(i==16){
//                column.setPreferredWidth(50);
//            }else if(i==17){
//                column.setPreferredWidth(70);
//            }else if(i==18){
//                column.setPreferredWidth(110);
//            }else if(i==19){
//                column.setPreferredWidth(50);
//            }else if(i==20){
//                column.setPreferredWidth(160);
//            }else if(i==21){
//                column.setPreferredWidth(80);
//            }else if(i==22){
//                column.setPreferredWidth(67);
//            }else if(i==23){
//                column.setPreferredWidth(77);
//            }else if(i==24){
//                column.setPreferredWidth(77);
//            }else if(i==25){
//                column.setPreferredWidth(66);
//            }else if(i==26){
//                column.setPreferredWidth(107);
//            }else if(i==27){
//                column.setPreferredWidth(105);
//            }else if(i==28){
//                column.setPreferredWidth(90);
//            }else if(i==29){
//                column.setPreferredWidth(80);
//            }else if(i==30){
//                column.setPreferredWidth(65);
//            }else if(i==31){
//                column.setPreferredWidth(85);
//            }else if(i==32){
//                column.setPreferredWidth(80);
//            }else if(i==33){
//                column.setPreferredWidth(77);
//            }else if(i==34){
//                column.setPreferredWidth(65);
//            }else if(i==35){
//                column.setPreferredWidth(65);
//            }else if(i==36){
//                column.setPreferredWidth(80);
//            }else if(i==37){
//                column.setPreferredWidth(100);
//            }else if(i==38){
//                column.setPreferredWidth(147);
//            }else if(i==39){
//                column.setPreferredWidth(190);
//            }else if(i==40){
//                column.setPreferredWidth(90);
//            }else if(i==41){
//                column.setPreferredWidth(100);
//            }else if(i==42){
//                column.setPreferredWidth(220);
//            }else if(i==43){
//                column.setPreferredWidth(95);
//            }else if(i==44){
//                column.setPreferredWidth(85);
//            }else if(i==45){
//                column.setPreferredWidth(105);
//            }else if(i==46){
//                column.setPreferredWidth(100);
//            }else if(i==47){
//                column.setPreferredWidth(100);
//            }else if(i==48){
//                column.setPreferredWidth(120);
//            }else if(i==49){
//                column.setPreferredWidth(150);
//            }else if(i==50){
//                column.setPreferredWidth(97);
//            }else if(i==51){
//                column.setPreferredWidth(97);
//            }else if(i==52){
//                column.setPreferredWidth(110);
//            }else if(i==53){
//                column.setPreferredWidth(135);
//            }else if(i==54){
//                column.setPreferredWidth(155);
//            }else if(i==55){
//                column.setPreferredWidth(175);
//            }else if(i==56){
//                column.setPreferredWidth(65);
//            }else if(i==57){
//                column.setPreferredWidth(63);
//            }else if(i==58){
//                column.setPreferredWidth(97);
//            }else if(i==59){
//                column.setPreferredWidth(87);
//            }else if(i==60){
//                column.setPreferredWidth(85);
//            }else if(i==61){
//                column.setPreferredWidth(100);
//            }else if(i==62){
//                column.setPreferredWidth(90);
//            }else if(i==63){
//                column.setPreferredWidth(150);
//            }else if(i==64){
//                column.setPreferredWidth(80);
//            }else if(i==65){
//                column.setPreferredWidth(58);
//            }else if(i==66){
//                column.setPreferredWidth(65);
//            }else if(i==67){
//                column.setPreferredWidth(45);
//            }else if(i==68){
//                column.setPreferredWidth(85);
//            }else if(i==69){
//                column.setPreferredWidth(100);
//            }else if(i==70){
//                column.setPreferredWidth(85);
//            }else if(i==71){
//                column.setPreferredWidth(60);
//            }else if(i==72){
//                column.setPreferredWidth(85);
//            }else if(i==73){
//                column.setPreferredWidth(85);
//            }else if(i==74){
//                column.setPreferredWidth(85);
//            }else if(i==75){
//                column.setPreferredWidth(203);
//            }else if(i==76){
//                column.setPreferredWidth(70);
//            }else if(i==77){
//                column.setPreferredWidth(90);
//            }else if(i==78){
//                column.setPreferredWidth(210);
//            }else if(i==79){
//                column.setPreferredWidth(75);
//            }else if(i==80){
//                column.setPreferredWidth(150);
//            }
//        }
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
//        tbMasalahKeperawatan.setModel(tabModeMasalah);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
//        tbMasalahKeperawatan.setPreferredScrollableViewportSize(new Dimension(500,500));
//        tbMasalahKeperawatan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
//        for (i = 0; i < 3; i++) {
//            TableColumn column = tbMasalahKeperawatan.getColumnModel().getColumn(i);
//            if(i==0){
//                column.setPreferredWidth(20);
//            }else if(i==1){
//                column.setMinWidth(0);
//                column.setMaxWidth(0);
//            }else if(i==2){
//                column.setPreferredWidth(350);
//            }
//        }
//        tbMasalahKeperawatan.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModeDetailMasalah=new DefaultTableModel(null,new Object[]{
                "Kode","Masalah Keperawatan"
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
//        tbMasalahDetailMasalah.setModel(tabModeDetailMasalah);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
//        tbMasalahDetailMasalah.setPreferredScrollableViewportSize(new Dimension(500,500));
//        tbMasalahDetailMasalah.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

//        for (i = 0; i < 2; i++) {
//            TableColumn column = tbMasalahDetailMasalah.getColumnModel().getColumn(i);
//            if(i==0){
//                column.setMinWidth(0);
//                column.setMaxWidth(0);
//            }else if(i==1){
//                column.setPreferredWidth(420);
//            }
//        }
//        tbMasalahDetailMasalah.setDefaultRenderer(Object.class, new WarnaTable());

        TNoRw.setDocument(new batasInput((byte)17).getKata(TNoRw));
        Respon.setDocument(new batasInput((int)150).getKata(Respon));
        Tindakan.setDocument(new batasInput((int)150).getKata(Tindakan));
        
//        RPO.setDocument(new batasInput((int)100).getKata(RPO));
//        Gravida.setDocument(new batasInput((byte)20).getKata(Gravida));
//        Para.setDocument(new batasInput((byte)20).getKata(Para));
//        Abortus.setDocument(new batasInput((byte)20).getKata(Abortus));
//        HPHT.setDocument(new batasInput((byte)20).getKata(HPHT));
//        JumlahPerdarahan.setDocument(new batasInput((byte)5).getKata(JumlahPerdarahan));
//        WarnaPerdarahan.setDocument(new batasInput((int)40).getKata(WarnaPerdarahan));
//        BAB.setDocument(new batasInput((byte)2).getKata(BAB));
//        XBAB.setDocument(new batasInput((byte)10).getKata(XBAB));
//        KBAB.setDocument(new batasInput((int)40).getKata(KBAB));
//        WBAB.setDocument(new batasInput((int)40).getKata(WBAB));
//        BAK.setDocument(new batasInput((byte)2).getKata(BAK));
//        XBAK.setDocument(new batasInput((byte)10).getKata(XBAK));
//        WBAK.setDocument(new batasInput((int)40).getKata(WBAK));
//        LBAK.setDocument(new batasInput((int)40).getKata(LBAK));
//        Dilaporkan.setDocument(new batasInput((int)50).getKata(Dilaporkan));
//        Sebutkan.setDocument(new batasInput((int)50).getKata(Sebutkan));
//        KetTinggal.setDocument(new batasInput((int)50).getKata(KetTinggal));
//        KetBudaya.setDocument(new batasInput((int)50).getKata(KetBudaya));
//        KetPendidikanPJ.setDocument(new batasInput((int)50).getKata(KetPendidikanPJ));
//        KetEdukasi.setDocument(new batasInput((int)50).getKata(KetEdukasi));
//        KetAlatBantu.setDocument(new batasInput((int)50).getKata(KetAlatBantu));
//        KetProvokes.setDocument(new batasInput((int)40).getKata(KetProvokes));
//        KetQuality.setDocument(new batasInput((int)50).getKata(KetQuality));
//        Lokasi.setDocument(new batasInput((int)50).getKata(Lokasi));
//        Durasi.setDocument(new batasInput((int)25).getKata(Durasi));
//        KetNyeri.setDocument(new batasInput((int)40).getKata(KetNyeri));
//        KetDokter.setDocument(new batasInput((byte)15).getKata(KetDokter));
//        KetLapor.setDocument(new batasInput((int)15).getKata(KetLapor));
//        Rencana.setDocument(new batasInput((int)200).getKata(Rencana));
//        TCari.setDocument(new batasInput((int)100).getKata(TCari));
        
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
        internalFrame2 = new widget.InternalFrame();
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
        Jk = new widget.TextBox();
        jLabel10 = new widget.Label();
        label11 = new widget.Label();
        txtId = new widget.Label();
        scrollPane1 = new widget.ScrollPane();
        Respon = new widget.TextArea();
        jLabel30 = new widget.Label();
        scrollPane2 = new widget.ScrollPane();
        Tindakan = new widget.TextArea();
        jLabel31 = new widget.Label();
        TglAsuhan = new widget.Tanggal();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        jLabel98 = new widget.Label();
        scrollInput = new widget.ScrollPane();
        jLabel12 = new widget.Label();
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

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Implementasi Keperawatan ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
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

        internalFrame2.setBorder(null);
        internalFrame2.setName("internalFrame2"); // NOI18N
        internalFrame2.setLayout(new java.awt.BorderLayout(1, 1));

        FormInput.setBackground(new java.awt.Color(255, 255, 255));
        FormInput.setBorder(null);
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(870, 200));
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

        Jk.setEditable(false);
        Jk.setHighlighter(null);
        Jk.setName("Jk"); // NOI18N
        FormInput.add(Jk);
        Jk.setBounds(774, 10, 80, 23);

        jLabel10.setText("No.Rawat :");
        jLabel10.setName("jLabel10"); // NOI18N
        FormInput.add(jLabel10);
        jLabel10.setBounds(0, 10, 70, 23);

        label11.setText("Tanggal :");
        label11.setName("label11"); // NOI18N
        label11.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label11);
        label11.setBounds(395, 40, 57, 23);

        txtId.setText("id");
        txtId.setName("txtId"); // NOI18N
        FormInput.add(txtId);
        txtId.setBounds(600, 40, 30, 23);

        scrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane1.setName("scrollPane1"); // NOI18N

        Respon.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Respon.setColumns(30);
        Respon.setRows(5);
        Respon.setName("Respon"); // NOI18N
        Respon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ResponKeyPressed(evt);
            }
        });
        scrollPane1.setViewportView(Respon);

        FormInput.add(scrollPane1);
        scrollPane1.setBounds(580, 90, 260, 70);

        jLabel30.setText("Respon :");
        jLabel30.setName("jLabel30"); // NOI18N
        FormInput.add(jLabel30);
        jLabel30.setBounds(400, 90, 175, 20);

        scrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane2.setName("scrollPane2"); // NOI18N

        Tindakan.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Tindakan.setColumns(30);
        Tindakan.setRows(5);
        Tindakan.setName("Tindakan"); // NOI18N
        Tindakan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TindakanKeyPressed(evt);
            }
        });
        scrollPane2.setViewportView(Tindakan);
        Tindakan.getAccessibleContext().setAccessibleName("");

        FormInput.add(scrollPane2);
        scrollPane2.setBounds(179, 90, 260, 70);

        jLabel31.setText("Tindakan :");
        jLabel31.setToolTipText("");
        jLabel31.setName("jLabel31"); // NOI18N
        FormInput.add(jLabel31);
        jLabel31.setBounds(0, 90, 175, 23);

        TglAsuhan.setForeground(new java.awt.Color(50, 70, 50));
        TglAsuhan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "18-12-2023 12:49:19" }));
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
        jSeparator11.setBounds(0, 210, 880, 1);

        jLabel98.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel98.setText("I. IMPLEMENTASI KEPERAWATAN");
        jLabel98.setToolTipText("");
        jLabel98.setName("jLabel98"); // NOI18N
        FormInput.add(jLabel98);
        jLabel98.setBounds(10, 70, 180, 23);

        scrollInput.setName("scrollInput"); // NOI18N
        scrollInput.setPreferredSize(new java.awt.Dimension(102, 557));
        FormInput.add(scrollInput);
        scrollInput.setBounds(0, 90, 966, 557);

        jLabel12.setText("J.K. :");
        jLabel12.setName("jLabel12"); // NOI18N
        FormInput.add(jLabel12);
        jLabel12.setBounds(740, 10, 30, 23);

        internalFrame2.add(FormInput, java.awt.BorderLayout.PAGE_START);

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

        internalFrame2.add(Scroll, java.awt.BorderLayout.CENTER);

        internalFrame1.add(internalFrame2, java.awt.BorderLayout.PAGE_START);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel19.setText("Tgl.Asuhan :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "18-12-2023" }));
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
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "18-12-2023" }));
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

        internalFrame1.add(panelGlass9, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TNoRM.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"Nama Pasien");
        }else 
            if(Respon.getText().trim().equals("")){
            Valid.textKosong(Respon,"Respon");
        }else if(Tindakan.getText().trim().equals("")){
            Valid.textKosong(Tindakan,"Tindakan");
        }else{
            if(Sequel.menyimpantf("tb_implementasi_keperawatan(no_rawat, tanggal, respon, tindakan, nip)","?,?,?,?,?"
                    + "","id",5,new String[]{
                    TNoRw.getText(),
                        Valid.SetTgl(TglAsuhan.getSelectedItem()+"")+" "+TglAsuhan.getSelectedItem().toString().substring(11,19),
                        Respon.getText(),
                Tindakan.getText(),
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
            if(Sequel.queryu2tf("delete from tb_implementasi_keperawatan where id=?",1,new String[]{
                txtId.getText()
            })==true){
//                Sequel.meghapus("penilaian_awal_keperawatan_igd_masalah","no_rawat",tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                Valid.tabelKosong(tabModeDetailMasalah);
                
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
            if(Respon.getText().trim().equals("")){
            Valid.textKosong(Respon,"Respon");
        }else if(Tindakan.getText().trim().equals("")){
            System.out.println(txtId.getText());
            Valid.textKosong(Tindakan,"Tindakan");
            
        }else{
            if(tbObat.getSelectedRow()>-1){
                if(Sequel.mengedittf("tb_implementasi_keperawatan","id=?","no_rawat=?,tanggal=?,respon=?,tindakan=?,"+
                    "nip=?",6,new String[]{
                    TNoRw.getText(),Valid.SetTgl(TglAsuhan.getSelectedItem()+"")+" "+TglAsuhan.getSelectedItem().toString().substring(11,19),
                        Respon.getText(),Tindakan.getText(),KdPetugas.getText(),txtId.getText()
                     })==true){
                        tampil();
                        emptTeks();
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
            param.put("hasil","Ginjal Kanan\t: "+respon + "\n\nGinjal Kiri\t: "+tindakan+"\n\nBuli\t\t: " +id);
            param.put("logo",Sequel.cariGambar("select setting.logo from setting"));
            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",kd_petugas);
            param.put("finger2","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+nm_petugas+"\nID "+(finger.equals("")?kd_petugas:finger)+"\n"+Valid.SetTgl3(tgl[0]));  

            pilihan = (String)JOptionPane.showInputDialog(null,"Silahkan pilih hasil pemeriksaan..!","Hasil Pemeriksaan",JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Model 1","PDF Model 1"},"Model 1");
//            pilihan = opil == null ? null : opil.toString();
            if(pilihan!=null && pilihan !="null") {
                switch (pilihan) {
                    case "Model 1":
                          Valid.MyReport("rptImplementasiKeperawatan.jasper","report","::[ Implementasi Keperawatan ]::",param);
                          break;
                    case "PDF Model 1":
                          Valid.MyReportPDF("rptImplementasiKeperawatan.jasper","report","::[ Implementasi Keperawatan ]::",param);
                          break;
                    
                }    
            }
                                
            
            this.setCursor(Cursor.getDefaultCursor());
        }


//        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//        if(tabMode.getRowCount()==0){
//            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
//            BtnBatal.requestFocus();
//        }else if(tabMode.getRowCount()!=0){
//            try{
//                if(TCari.getText().equals("")){
//                    ps=koneksi.prepareStatement(
//                            "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,if(pasien.jk='L','Laki-Laki','Perempuan') as jk,pasien.tgl_lahir,pasien.agama,bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,penilaian_awal_keperawatan_igd.tanggal,penilaian_awal_keperawatan_igd.informasi,"+
//                            "penilaian_awal_keperawatan_igd.keluhan_utama,penilaian_awal_keperawatan_igd.rpd,penilaian_awal_keperawatan_igd.rpo,penilaian_awal_keperawatan_igd.status_kehamilan,penilaian_awal_keperawatan_igd.gravida,penilaian_awal_keperawatan_igd.para,"+
//                            "penilaian_awal_keperawatan_igd.abortus,penilaian_awal_keperawatan_igd.hpht,penilaian_awal_keperawatan_igd.tekanan,penilaian_awal_keperawatan_igd.pupil,penilaian_awal_keperawatan_igd.neurosensorik,penilaian_awal_keperawatan_igd.integumen,penilaian_awal_keperawatan_igd.turgor,"+ 
//                            "penilaian_awal_keperawatan_igd.edema,penilaian_awal_keperawatan_igd.mukosa,penilaian_awal_keperawatan_igd.perdarahan,penilaian_awal_keperawatan_igd.jumlah_perdarahan,penilaian_awal_keperawatan_igd.warna_perdarahan,penilaian_awal_keperawatan_igd.intoksikasi,"+
//                            "penilaian_awal_keperawatan_igd.bab,penilaian_awal_keperawatan_igd.xbab,penilaian_awal_keperawatan_igd.kbab,penilaian_awal_keperawatan_igd.wbab,penilaian_awal_keperawatan_igd.bak,penilaian_awal_keperawatan_igd.xbak,penilaian_awal_keperawatan_igd.wbak,"+
//                            "penilaian_awal_keperawatan_igd.lbak,penilaian_awal_keperawatan_igd.psikologis,penilaian_awal_keperawatan_igd.jiwa,penilaian_awal_keperawatan_igd.perilaku,penilaian_awal_keperawatan_igd.dilaporkan,penilaian_awal_keperawatan_igd.sebutkan,penilaian_awal_keperawatan_igd.hubungan,pasien.stts_nikah,"+ 
//                            "penilaian_awal_keperawatan_igd.tinggal_dengan,penilaian_awal_keperawatan_igd.ket_tinggal,pasien.pekerjaan,penjab.png_jawab,penilaian_awal_keperawatan_igd.budaya,penilaian_awal_keperawatan_igd.ket_budaya,pasien.pnd,penilaian_awal_keperawatan_igd.pendidikan_pj,penilaian_awal_keperawatan_igd.ket_pendidikan_pj,"+  
//                            "penilaian_awal_keperawatan_igd.edukasi,penilaian_awal_keperawatan_igd.ket_edukasi,penilaian_awal_keperawatan_igd.kemampuan,penilaian_awal_keperawatan_igd.aktifitas,penilaian_awal_keperawatan_igd.alat_bantu,penilaian_awal_keperawatan_igd.ket_bantu,"+
//                            "penilaian_awal_keperawatan_igd.nyeri,penilaian_awal_keperawatan_igd.provokes,penilaian_awal_keperawatan_igd.ket_provokes,penilaian_awal_keperawatan_igd.quality,penilaian_awal_keperawatan_igd.ket_quality,penilaian_awal_keperawatan_igd.lokasi,penilaian_awal_keperawatan_igd.menyebar,"+
//                            "penilaian_awal_keperawatan_igd.skala_nyeri,penilaian_awal_keperawatan_igd.durasi,penilaian_awal_keperawatan_igd.nyeri_hilang,penilaian_awal_keperawatan_igd.ket_nyeri,penilaian_awal_keperawatan_igd.pada_dokter,penilaian_awal_keperawatan_igd.ket_dokter,"+
//                            "penilaian_awal_keperawatan_igd.berjalan_a,penilaian_awal_keperawatan_igd.berjalan_b,penilaian_awal_keperawatan_igd.berjalan_c,penilaian_awal_keperawatan_igd.hasil,penilaian_awal_keperawatan_igd.lapor,penilaian_awal_keperawatan_igd.ket_lapor,"+
//                            "penilaian_awal_keperawatan_igd.rencana,penilaian_awal_keperawatan_igd.nip,petugas.nama "+
//                            "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
//                            "inner join penilaian_awal_keperawatan_igd on reg_periksa.no_rawat=penilaian_awal_keperawatan_igd.no_rawat "+
//                            "inner join petugas on penilaian_awal_keperawatan_igd.nip=petugas.nip "+
//                            "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "+
//                            "inner join penjab on penjab.kd_pj=reg_periksa.kd_pj "+
//                            "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik where "+
//                            "penilaian_awal_keperawatan_igd.tanggal between ? and ? order by penilaian_awal_keperawatan_igd.tanggal");
//                }else{
//                    ps=koneksi.prepareStatement(
//                            "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,if(pasien.jk='L','Laki-Laki','Perempuan') as jk,pasien.tgl_lahir,pasien.agama,bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,penilaian_awal_keperawatan_igd.tanggal,penilaian_awal_keperawatan_igd.informasi,"+
//                            "penilaian_awal_keperawatan_igd.keluhan_utama,penilaian_awal_keperawatan_igd.rpd,penilaian_awal_keperawatan_igd.rpo,penilaian_awal_keperawatan_igd.status_kehamilan,penilaian_awal_keperawatan_igd.gravida,penilaian_awal_keperawatan_igd.para,"+
//                            "penilaian_awal_keperawatan_igd.abortus,penilaian_awal_keperawatan_igd.hpht,penilaian_awal_keperawatan_igd.tekanan,penilaian_awal_keperawatan_igd.pupil,penilaian_awal_keperawatan_igd.neurosensorik,penilaian_awal_keperawatan_igd.integumen,penilaian_awal_keperawatan_igd.turgor,"+ 
//                            "penilaian_awal_keperawatan_igd.edema,penilaian_awal_keperawatan_igd.mukosa,penilaian_awal_keperawatan_igd.perdarahan,penilaian_awal_keperawatan_igd.jumlah_perdarahan,penilaian_awal_keperawatan_igd.warna_perdarahan,penilaian_awal_keperawatan_igd.intoksikasi,"+
//                            "penilaian_awal_keperawatan_igd.bab,penilaian_awal_keperawatan_igd.xbab,penilaian_awal_keperawatan_igd.kbab,penilaian_awal_keperawatan_igd.wbab,penilaian_awal_keperawatan_igd.bak,penilaian_awal_keperawatan_igd.xbak,penilaian_awal_keperawatan_igd.wbak,"+
//                            "penilaian_awal_keperawatan_igd.lbak,penilaian_awal_keperawatan_igd.psikologis,penilaian_awal_keperawatan_igd.jiwa,penilaian_awal_keperawatan_igd.perilaku,penilaian_awal_keperawatan_igd.dilaporkan,penilaian_awal_keperawatan_igd.sebutkan,penilaian_awal_keperawatan_igd.hubungan,pasien.stts_nikah,"+ 
//                            "penilaian_awal_keperawatan_igd.tinggal_dengan,penilaian_awal_keperawatan_igd.ket_tinggal,pasien.pekerjaan,penjab.png_jawab,penilaian_awal_keperawatan_igd.budaya,penilaian_awal_keperawatan_igd.ket_budaya,pasien.pnd,penilaian_awal_keperawatan_igd.pendidikan_pj,penilaian_awal_keperawatan_igd.ket_pendidikan_pj,"+  
//                            "penilaian_awal_keperawatan_igd.edukasi,penilaian_awal_keperawatan_igd.ket_edukasi,penilaian_awal_keperawatan_igd.kemampuan,penilaian_awal_keperawatan_igd.aktifitas,penilaian_awal_keperawatan_igd.alat_bantu,penilaian_awal_keperawatan_igd.ket_bantu,"+
//                            "penilaian_awal_keperawatan_igd.nyeri,penilaian_awal_keperawatan_igd.provokes,penilaian_awal_keperawatan_igd.ket_provokes,penilaian_awal_keperawatan_igd.quality,penilaian_awal_keperawatan_igd.ket_quality,penilaian_awal_keperawatan_igd.lokasi,penilaian_awal_keperawatan_igd.menyebar,"+
//                            "penilaian_awal_keperawatan_igd.skala_nyeri,penilaian_awal_keperawatan_igd.durasi,penilaian_awal_keperawatan_igd.nyeri_hilang,penilaian_awal_keperawatan_igd.ket_nyeri,penilaian_awal_keperawatan_igd.pada_dokter,penilaian_awal_keperawatan_igd.ket_dokter,"+
//                            "penilaian_awal_keperawatan_igd.berjalan_a,penilaian_awal_keperawatan_igd.berjalan_b,penilaian_awal_keperawatan_igd.berjalan_c,penilaian_awal_keperawatan_igd.hasil,penilaian_awal_keperawatan_igd.lapor,penilaian_awal_keperawatan_igd.ket_lapor,"+
//                            "penilaian_awal_keperawatan_igd.rencana,penilaian_awal_keperawatan_igd.nip,petugas.nama "+
//                            "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
//                            "inner join penilaian_awal_keperawatan_igd on reg_periksa.no_rawat=penilaian_awal_keperawatan_igd.no_rawat "+
//                            "inner join petugas on penilaian_awal_keperawatan_igd.nip=petugas.nip "+
//                            "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "+
//                            "inner join penjab on penjab.kd_pj=reg_periksa.kd_pj "+
//                            "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik where "+
//                            "penilaian_awal_keperawatan_igd.tanggal between ? and ? and "+
//                            "(reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or "+
//                            "penilaian_awal_keperawatan_igd.nip like ? or petugas.nama like ?) "+
//                            "order by penilaian_awal_keperawatan_igd.tanggal");
//                }
//
//                try {
//                    if(TCari.getText().equals("")){
//                        ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
//                        ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
//                    }else{
//                        ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
//                        ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
//                        ps.setString(3,"%"+TCari.getText()+"%");
//                        ps.setString(4,"%"+TCari.getText()+"%");
//                        ps.setString(5,"%"+TCari.getText()+"%");
//                        ps.setString(6,"%"+TCari.getText()+"%");
//                        ps.setString(7,"%"+TCari.getText()+"%");
//                    }  
//                    rs=ps.executeQuery();
//                    htmlContent = new StringBuilder();
//                    htmlContent.append(                             
//                        "<tr class='isi'>"+
//                            "<td valign='middle' bgcolor='#FFFAF8' align='center' width='9%'><b>PASIEN & PETUGAS</b></td>"+
//                            "<td valign='middle' bgcolor='#FFFAF8' align='center' width='10%'><b>I. RIWAYAT KESEHATAN PASIEN</b></td>"+
//                            "<td valign='middle' bgcolor='#FFFAF8' align='center' width='15%'><b>II. PEMERIKSAAN FISIK</b></td>"+
//                            "<td valign='middle' bgcolor='#FFFAF8' align='center' width='14%'><b>III. RIWAYAT PSIKOLOGIS - SOSIAL - EKONOMI - BUDAYA - SPIRITUAL</b></td>"+
//                            "<td valign='middle' bgcolor='#FFFAF8' align='center' width='8%'><b>IV. PENGKAJIAN FUNGSI</b></td>"+
//                            "<td valign='middle' bgcolor='#FFFAF8' align='center' width='12%'><b>V. SKALA NYERI</b></td>"+
//                            "<td valign='middle' bgcolor='#FFFAF8' align='center' width='16%'><b>VI. PENILAIAN RESIKO JATUH (GET UP AND GO)</b></td>"+
//                            "<td valign='middle' bgcolor='#FFFAF8' align='center' width='11%'><b>MASALAH & RENCANA KEPERAWATAN</b></td>"+
//                        "</tr>"
//                    );
//                    while(rs.next()){
//                        masalahkeperawatanigd="";
//                        ps2=koneksi.prepareStatement(
//                            "select master_masalah_keperawatan_igd.kode_masalah,master_masalah_keperawatan_igd.nama_masalah from master_masalah_keperawatan_igd "+
//                            "inner join penilaian_awal_keperawatan_igd_masalah on penilaian_awal_keperawatan_igd_masalah.kode_masalah=master_masalah_keperawatan_igd.kode_masalah "+
//                            "where penilaian_awal_keperawatan_igd_masalah.no_rawat=? order by penilaian_awal_keperawatan_igd_masalah.kode_masalah");
//                        try {
//                            ps2.setString(1,rs.getString("no_rawat"));
//                            rs2=ps2.executeQuery();
//                            while(rs2.next()){
//                                masalahkeperawatanigd=rs2.getString("nama_masalah")+", "+masalahkeperawatanigd;
//                            }
//                        } catch (Exception e) {
//                            System.out.println("Notif : "+e);
//                        } finally{
//                            if(rs2!=null){
//                                rs2.close();
//                            }
//                            if(ps2!=null){
//                                ps2.close();
//                            }
//                        }
//                        htmlContent.append(
//                            "<tr class='isi'>"+
//                                "<td valign='top' cellpadding='0' cellspacing='0'>"+
//                                    "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='32%' valign='top'>No.Rawat</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>"+rs.getString("no_rawat")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='32%' valign='top'>No.R.M.</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>"+rs.getString("no_rkm_medis")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='32%' valign='top'>Nama Pasien</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>"+rs.getString("nm_pasien")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='32%' valign='top'>J.K.</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>"+rs.getString("jk")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='32%' valign='top'>Tgl.Lahir</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>"+rs.getString("tgl_lahir")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='32%' valign='top'>Agama</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>"+rs.getString("agama")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='32%' valign='top'>Bahasa</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>"+rs.getString("nama_bahasa")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='32%' valign='top'>Pekerjaan</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>"+rs.getString("pekerjaan")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='32%' valign='top'>Pembayaran</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>"+rs.getString("png_jawab")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='32%' valign='top'>Pendidikan</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>"+rs.getString("pnd")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='32%' valign='top'>Stts.Nikah</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>"+rs.getString("stts_nikah")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='32%' valign='top'>Cacat Fisik</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>"+rs.getString("nama_cacat")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='32%' valign='top'>Petugas</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>"+rs.getString("nip")+" "+rs.getString("nama")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='32%' valign='top'>Tgl.Asuhan</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>"+rs.getString("tanggal")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='32%' valign='top'>Informasi</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>"+rs.getString("informasi")+"</td>"+
//                                        "</tr>"+
//                                    "</table>"+
//                                "</td>"+
//                                "<td valign='top' cellpadding='0' cellspacing='0'>"+
//                                    "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='32%' valign='top'>RPS</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>"+rs.getString("keluhan_utama")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='32%' valign='top'>RPD</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>"+rs.getString("rpd")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='32%' valign='top'>RPO</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>"+rs.getString("rpo")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='32%' valign='top'>Stts.Hami</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>"+rs.getString("status_kehamilan")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='32%' valign='top'>HPHT</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>"+rs.getString("hpht")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='32%' valign='top'>Para</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>"+rs.getString("para")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='32%' valign='top'>Abortus</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>"+rs.getString("abortus")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='32%' valign='top'>Gravida</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>"+rs.getString("gravida")+"</td>"+
//                                        "</tr>"+
//                                    "</table>"+
//                                "</td>"+
//                                "<td valign='top' cellpadding='0' cellspacing='0'>"+
//                                    "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='34%' valign='top'>Tekanan Intrakranial</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>"+rs.getString("tekanan")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='34%' valign='top'>Pupil</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>"+rs.getString("pupil")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='34%' valign='top'>Neurosensorik / Muskuloskeletal</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>"+rs.getString("neurosensorik")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='34%' valign='top'>Integumen</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>"+rs.getString("integumen")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='34%' valign='top'>Turgor kulit</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>"+rs.getString("turgor")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='34%' valign='top'>Edema</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>"+rs.getString("edema")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='34%' valign='top'>Mukosa Mulut</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>"+rs.getString("mukosa")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='34%' valign='top'>Perdarahan</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>"+rs.getString("perdarahan")+", Jumlah : "+rs.getString("jumlah_perdarahan")+", Warna : "+rs.getString("warna_perdarahan")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='34%' valign='top'>Intoksikasi</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>"+rs.getString("intoksikasi")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='34%' valign='top'>Eliminasi</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>"+
//                                                "BAB : -Frekuensi : "+rs.getString("bab")+" x / "+rs.getString("xbab")+" -Konsistensi : "+rs.getString("kbab")+" -Warna : "+rs.getString("wbab")+"<br>"+
//                                                "BAK : -Frekuensi : "+rs.getString("bak")+" x / "+rs.getString("xbak")+" -Warna : "+rs.getString("wbak")+" -Lain-lain : "+rs.getString("lbak")+"<br>"+
//                                            "</td>"+
//                                        "</tr>"+
//                                    "</table>"+
//                                "</td>"+
//                                "<td valign='top' cellpadding='0' cellspacing='0'>"+
//                                    "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='44%' valign='top'>Kondisi Psikologis</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>"+rs.getString("psikologis")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='44%' valign='top'>Gangguan Jiwa Di Masa Lalu</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>"+rs.getString("jiwa")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='44%' valign='top'>Adakah perilaku</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>"+rs.getString("perilaku")+", Dilaporkan Ke : "+rs.getString("dilaporkan")+", Sebutkan : "+rs.getString("sebutkan")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='44%' valign='top'>Hubungan Pasien Dengan Anggota Keluarga</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>"+rs.getString("hubungan")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='44%' valign='top'>Tinggal Dengan</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>"+rs.getString("tinggal_dengan")+(rs.getString("ket_tinggal").equals("")?"":", "+rs.getString("ket_tinggal"))+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='44%' valign='top'>Kepercayaan / Budaya / Nilai-nilai Khusus</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>"+rs.getString("budaya")+(rs.getString("ket_budaya").equals("")?"":", "+rs.getString("ket_budaya"))+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='44%' valign='top'>Pendidikan P.J.</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>"+rs.getString("pendidikan_pj")+(rs.getString("ket_pendidikan_pj").equals("")?"":", "+rs.getString("ket_pendidikan_pj"))+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='44%' valign='top'>Edukasi Diberikan Kepada</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>"+rs.getString("edukasi")+(rs.getString("ket_edukasi").equals("")?"":", "+rs.getString("ket_edukasi"))+"</td>"+
//                                        "</tr>"+
//                                    "</table>"+
//                                "</td>"+
//                                "<td valign='top' cellpadding='0' cellspacing='0'>"+
//                                    "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='34%' valign='top'>Kemampuan Aktifitas Sehari-hari</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>"+rs.getString("kemampuan")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='34%' valign='top'>Aktifitas</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>"+rs.getString("aktifitas")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='34%' valign='top'>Alat Bantu</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>"+rs.getString("alat_bantu")+(rs.getString("ket_bantu").equals("")?"":", "+rs.getString("ket_bantu"))+"</td>"+
//                                        "</tr>"+
//                                    "</table>"+
//                                "</td>"+
//                                "<td valign='top' cellpadding='0' cellspacing='0'>"+
//                                    "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='44%' valign='top'>Tingkat Nyeri</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>"+rs.getString("nyeri")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='44%' valign='top'>Provokes</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>"+rs.getString("provokes")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='44%' valign='top'>Ket. Provokes</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>"+rs.getString("ket_provokes")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='44%' valign='top'>Kualitas</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>"+rs.getString("quality")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='44%' valign='top'>Ket. Kualitas</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>"+rs.getString("ket_quality")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='44%' valign='top'>Lokas</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>"+rs.getString("lokasi")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='44%' valign='top'>Menyebar</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>"+rs.getString("menyebar")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='44%' valign='top'>Skala Nyeri</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>"+rs.getString("skala_nyeri")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='44%' valign='top'>Durasi</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>"+rs.getString("durasi")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='44%' valign='top'>Nyeri Hilang</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>"+rs.getString("nyeri_hilang")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='44%' valign='top'>Ket. Hilang Nyeri</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>"+rs.getString("ket_nyeri")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='44%' valign='top'>Lapor Ke Dokter</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>"+rs.getString("pada_dokter")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='44%' valign='top'>Jam Lapor</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>"+rs.getString("ket_dokter")+"</td>"+
//                                        "</tr>"+
//                                    "</table>"+
//                                "</td>"+
//                                "<td valign='top' cellpadding='0' cellspacing='0'>"+
//                                    "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='64%' valign='top'>Tidak seimbang/sempoyongan/limbung</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>"+rs.getString("berjalan_a")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='64%' valign='top'>Jalan dengan menggunakan alat bantu (kruk, tripot, kursi roda, orang lain)</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>"+rs.getString("berjalan_b")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='64%' valign='top'>Menopang saat akan duduk, tampak memegang pinggiran kursi atau meja/benda lain sebagai penopang</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>"+rs.getString("berjalan_c")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='64%' valign='top'>Hasil</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>"+rs.getString("hasil")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='64%' valign='top'>Dilaporan ke dokter?</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>"+rs.getString("lapor")+"</td>"+
//                                        "</tr>"+
//                                        "<tr class='isi2'>"+
//                                            "<td width='64%' valign='top'>Jam Lapor</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>"+rs.getString("ket_lapor")+"</td>"+
//                                        "</tr>"+
//                                    "</table>"+
//                                "</td>"+
//                                "<td valign='top' cellpadding='0' cellspacing='0'>"+
//                                    "Masalah Keperawatan : "+masalahkeperawatanigd+"<br><br>"+
//                                    "Rencana Keperawatan : "+rs.getString("rencana")+
//                                "</td>"+
//                            "</tr>"
//                        );
//                    }
//                    LoadHTML.setText(
//                        "<html>"+
//                          "<table width='100%' border='0' align='center' cellpadding='1px' cellspacing='0' class='tbl_form'>"+
//                           htmlContent.toString()+
//                          "</table>"+
//                        "</html>"
//                    );
//
//                    File g = new File("file2.css");            
//                    BufferedWriter bg = new BufferedWriter(new FileWriter(g));
//                    bg.write(
//                        ".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
//                        ".isi2 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#323232;}"+
//                        ".isi3 td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
//                        ".isi4 td{font: 11px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
//                        ".isi5 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#AA0000;}"+
//                        ".isi6 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#FF0000;}"+
//                        ".isi7 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#C8C800;}"+
//                        ".isi8 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#00AA00;}"+
//                        ".isi9 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#969696;}"
//                    );
//                    bg.close();
//
//                    File f = new File("DataPenilaianAwalKeperawatanIGD.html");            
//                    BufferedWriter bw = new BufferedWriter(new FileWriter(f));            
//                    bw.write(LoadHTML.getText().replaceAll("<head>","<head>"+
//                                "<link href=\"file2.css\" rel=\"stylesheet\" type=\"text/css\" />"+
//                                "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
//                                    "<tr class='isi2'>"+
//                                        "<td valign='top' align='center'>"+
//                                            "<font size='4' face='Tahoma'>"+akses.getnamars()+"</font><br>"+
//                                            akses.getalamatrs()+", "+akses.getkabupatenrs()+", "+akses.getpropinsirs()+"<br>"+
//                                            akses.getkontakrs()+", E-mail : "+akses.getemailrs()+"<br><br>"+
//                                            "<font size='2' face='Tahoma'>DATA PENILAIAN AWAL KEPERAWATAN IGD<br><br></font>"+        
//                                        "</td>"+
//                                   "</tr>"+
//                                "</table>")
//                    );
//                    bw.close();                         
//                    Desktop.getDesktop().browse(f.toURI());
//                } catch (Exception e) {
//                    System.out.println("Notif : "+e);
//                } finally{
//                    if(rs!=null){
//                        rs.close();
//                    }
//                    if(ps!=null){
//                        ps.close();
//                    }
//                }
//
//            }catch(Exception e){
//                System.out.println("Notifikasi : "+e);
//            }
//        }
//        this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnEdit, BtnKeluar);
        }
}//GEN-LAST:event_BtnPrintKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
//        TCari.setText("");
        tampil();
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
//            TCari.setText("");
            tampil();
        }else{
//            Valid.pindah(evt, BtnCari, TPasien);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        try {
            if(Valid.daysOld("./cache/masalahkeperawatanigd.iyem")<8){
                tampilMasalah2();
            }else{
                tampilMasalah();
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_formWindowOpened

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnAll);
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

    private void tbObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatKeyPressed
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getMasalah();
                } catch (java.lang.NullPointerException e) {
                }
            }else if(evt.getKeyCode()==KeyEvent.VK_SPACE){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbObatKeyPressed

    private void tbObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getMasalah();
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tbObatMouseClicked

    private void TglAsuhanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TglAsuhanKeyPressed
        //        Valid.pindah2(evt,Rencana,RPD);
    }//GEN-LAST:event_TglAsuhanKeyPressed

    private void TindakanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TindakanKeyPressed
        //        Valid.pindah2(evt,KeluhanUtama,RPO);
    }//GEN-LAST:event_TindakanKeyPressed

    private void ResponKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ResponKeyPressed
        //        Valid.pindah2(evt,Informasi,RPD);
    }//GEN-LAST:event_ResponKeyPressed

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
            //            Valid.pindah(evt,TCari,BtnDokter);
        }
    }//GEN-LAST:event_TNoRwKeyPressed

    private void TNoRwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TNoRwActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TNoRwActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMImplementasiKeperawatan dialog = new RMImplementasiKeperawatan(new javax.swing.JFrame(), true);
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
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.PanelBiasa FormInput;
    private widget.TextBox Jk;
    private widget.TextBox KdPetugas;
    private widget.Label LCount;
    private widget.editorpane LoadHTML;
    private widget.TextBox NmPetugas;
    private widget.TextArea Respon;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.Tanggal TglAsuhan;
    private widget.TextBox TglLahir;
    private widget.TextArea Tindakan;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.Label jLabel10;
    private widget.Label jLabel12;
    private widget.Label jLabel19;
    private widget.Label jLabel21;
    private widget.Label jLabel30;
    private widget.Label jLabel31;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private widget.Label jLabel98;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator11;
    private widget.Label label11;
    private widget.Label label14;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.ScrollPane scrollInput;
    private widget.ScrollPane scrollPane1;
    private widget.ScrollPane scrollPane2;
    private widget.Table tbObat;
    private widget.Label txtId;
    // End of variables declaration//GEN-END:variables

     private void tampil() {
        Valid.tabelKosong(tabMode);
        txtId.setVisible(false);
        try{
            if(TCari.getText().equals("")){
                ps=koneksi.prepareStatement(
                        "SELECT tb_implementasi_keperawatan.id, reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,IF(pasien.jk='L','Laki-Laki','Perempuan') AS jk, " +
                        "pasien.tgl_lahir,tb_implementasi_keperawatan.tanggal, " +
                        "tb_implementasi_keperawatan.tindakan,tb_implementasi_keperawatan.respon, " +
                        "tb_implementasi_keperawatan.nip,petugas.nama " +
                        "FROM reg_periksa " +
                        "INNER JOIN pasien ON reg_periksa.no_rkm_medis=pasien.no_rkm_medis " +
                        "INNER JOIN tb_implementasi_keperawatan ON reg_periksa.no_rawat=tb_implementasi_keperawatan.no_rawat " +
                        "INNER JOIN petugas ON tb_implementasi_keperawatan.nip=petugas.nip " +
                        "WHERE tb_implementasi_keperawatan.tanggal BETWEEN ? AND ? " +
                        "AND pasien.no_rkm_medis=? " +
                        "ORDER BY tb_implementasi_keperawatan.tanggal DESC ");

            }else{
                ps=koneksi.prepareStatement(
                        "SELECT tb_implementasi_keperawatan.id, reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,IF(pasien.jk='L','Laki-Laki','Perempuan') AS jk, " +
                        "pasien.tgl_lahir,tb_implementasi_keperawatan.tanggal, " +
                        "tb_implementasi_keperawatan.tindakan,tb_implementasi_keperawatan.respon, " +
                        "tb_implementasi_keperawatan.nip,petugas.nama " +
                        "FROM reg_periksa " +
                        "INNER JOIN pasien ON reg_periksa.no_rkm_medis=pasien.no_rkm_medis " +
                        "INNER JOIN tb_implementasi_keperawatan ON reg_periksa.no_rawat=tb_implementasi_keperawatan.no_rawat " +
                        "INNER JOIN petugas ON tb_implementasi_keperawatan.nip=petugas.nip " +
                        "WHERE tb_implementasi_keperawatan.tanggal BETWEEN ? AND ? " +
                        "AND pasien.no_rkm_medis=? " +
                        "AND (reg_periksa.no_rawat LIKE ? OR pasien.no_rkm_medis LIKE ? OR pasien.nm_pasien LIKE ? OR " +
                        "tb_implementasi_keperawatan.nip LIKE ? OR petugas.nama LIKE ?) " +
                        "ORDER BY tb_implementasi_keperawatan.tanggal DESC");
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
//                        rs.getString("agama"),
//                        rs.getString("nama_bahasa"),rs.getString("nama_cacat"),
                        rs.getString("tgl_lahir"),rs.getString("tanggal"),
//                        rs.getString("informasi"),
                        rs.getString("tindakan"),rs.getString("respon"),
                        rs.getString("id"),
//                        rs.getString("rpo")
//                            ,rs.getString("status_kehamilan"),rs.getString("gravida"),
//                        rs.getString("para"),rs.getString("abortus"),rs.getString("hpht"),rs.getString("tekanan"),rs.getString("pupil"),rs.getString("neurosensorik"),rs.getString("integumen"),rs.getString("turgor"),
//                        rs.getString("edema"),rs.getString("mukosa"),rs.getString("perdarahan"),rs.getString("jumlah_perdarahan"),rs.getString("warna_perdarahan"),rs.getString("intoksikasi"),rs.getString("bab"),rs.getString("xbab"),rs.getString("kbab"),
//                        rs.getString("wbab"),rs.getString("bak"),rs.getString("xbak"),rs.getString("wbak"),rs.getString("lbak"),rs.getString("psikologis"),rs.getString("jiwa"),rs.getString("perilaku"),rs.getString("dilaporkan"),
//                        rs.getString("sebutkan"),rs.getString("hubungan"),rs.getString("stts_nikah"),rs.getString("tinggal_dengan"),rs.getString("ket_tinggal"),rs.getString("pekerjaan"),rs.getString("png_jawab"),rs.getString("budaya"),rs.getString("ket_budaya"),rs.getString("pnd"),rs.getString("pendidikan_pj"),rs.getString("ket_pendidikan_pj"),rs.getString("edukasi"),
//                        rs.getString("ket_edukasi"),rs.getString("kemampuan"),rs.getString("aktifitas"),rs.getString("alat_bantu"),rs.getString("ket_bantu"),rs.getString("nyeri"),rs.getString("provokes"),rs.getString("ket_provokes"),rs.getString("quality"),rs.getString("ket_quality"),
//                        rs.getString("lokasi"),rs.getString("menyebar"),rs.getString("skala_nyeri"),rs.getString("durasi"),rs.getString("nyeri_hilang"),rs.getString("ket_nyeri"),rs.getString("pada_dokter"),rs.getString("ket_dokter"),
//                        rs.getString("berjalan_a"),rs.getString("berjalan_b"),rs.getString("berjalan_c"),rs.getString("hasil"),rs.getString("lapor"),rs.getString("ket_lapor"),rs.getString("rencana")
                          rs.getString("nip"),rs.getString("nama")
                    });
                }
                System.out.println(tabMode);
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
//        LCount.setText(""+tabMode.getRowCount());
    }

    public void emptTeks() {
        TglAsuhan.setDate(new Date());
//        Informasi.setSelectedIndex(0);
        Respon.setText("");
        Tindakan.setText("");
        
//        RPO.setText("");
//        StatusKehamilan.setSelectedIndex(0);
//        Gravida.setText("");
//        Para.setText("");
//        Abortus.setText("");
//        HPHT.setText("");
//        Tekanan.setSelectedIndex(0);
//        Pupil.setSelectedIndex(0);
//        Neurosensorik.setSelectedIndex(0);
//        Integumen.setSelectedIndex(0);
//        Turgor.setSelectedIndex(0);
//        Edema.setSelectedIndex(0);
//        Mukosa.setSelectedIndex(0);
//        Perdarahan.setSelectedIndex(0);
//        JumlahPerdarahan.setText("");
//        WarnaPerdarahan.setText("");
//        Intoksikasi.setSelectedIndex(0);
//        BAB.setText("");
//        XBAB.setText("");
//        KBAB.setText("");
//        WBAB.setText("");
//        BAK.setText("");
//        XBAK.setText("");
//        WBAK.setText("");
//        LBAK.setText("");
//        Psikologis.setSelectedIndex(0);
//        Jiwa.setSelectedIndex(0);
//        Perilaku.setSelectedIndex(0);
//        Dilaporkan.setText("");
//        Sebutkan.setText("");
//        Hubungan.setSelectedIndex(0);
//        TinggalDengan.setSelectedIndex(0);
//        KetTinggal.setText("");
//        StatusBudaya.setSelectedIndex(0);
//        KetBudaya.setText("");
//        PendidikanPJ.setSelectedIndex(0);
//        KetPendidikanPJ.setText("");
//        Edukasi.setSelectedIndex(0);
//        KetEdukasi.setText("");
//        ADL.setSelectedIndex(0);
//        Aktifitas.setSelectedIndex(0);
//        AlatBantu.setSelectedIndex(0);
//        KetAlatBantu.setText("");
//        Nyeri.setSelectedIndex(0);
//        Provokes.setSelectedIndex(0);
//        KetProvokes.setText("");
//        Quality.setSelectedIndex(0);
//        KetQuality.setText("");
//        Lokasi.setText("");
//        Menyebar.setSelectedIndex(0);
//        SkalaNyeri.setSelectedIndex(0);
//        Durasi.setText("");
//        NyeriHilang.setSelectedIndex(0);
//        KetNyeri.setText("");
//        PadaDokter.setSelectedIndex(0);
//        KetDokter.setText("");
//        ATS.setSelectedIndex(0);
//        BJM.setSelectedIndex(0);
//        MSA.setSelectedIndex(0);
//        Hasil.setSelectedIndex(0);
//        Lapor.setSelectedIndex(0);
//        KetLapor.setText("");
//        Rencana.setText("");
        for (i = 0; i < tabModeMasalah.getRowCount(); i++) {
            tabModeMasalah.setValueAt(false,i,0);
        }
//        Informasi.requestFocus();
    } 

    private void getData() {
        if(tbObat.getSelectedRow()!= -1){
            no_rawat = tbObat.getValueAt(tbObat.getSelectedRow(),0).toString(); 
            no_rm = tbObat.getValueAt(tbObat.getSelectedRow(),1).toString();
            pasien = tbObat.getValueAt(tbObat.getSelectedRow(),2).toString(); 
            jk = tbObat.getValueAt(tbObat.getSelectedRow(),3).toString(); 
            tgllahir = tbObat.getValueAt(tbObat.getSelectedRow(),4).toString();
            tindakan = tbObat.getValueAt(tbObat.getSelectedRow(),6).toString();
            respon = tbObat.getValueAt(tbObat.getSelectedRow(),7).toString();
            id = tbObat.getValueAt(tbObat.getSelectedRow(),8).toString();
            kd_petugas = tbObat.getValueAt(tbObat.getSelectedRow(),9).toString();
            nm_petugas = tbObat.getValueAt(tbObat.getSelectedRow(),10).toString();
            
            
            TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()); 
            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString()); 
            Jk.setText(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString()); 
            TglLahir.setText(tbObat.getValueAt(tbObat.getSelectedRow(),4).toString()); 
            Tindakan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString());
            Respon.setText(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString());
            txtId.setText(tbObat.getValueAt(tbObat.getSelectedRow(),8).toString());
            
//            RPO.setText(tbObat.getValueAt(tbObat.getSelectedRow(),12).toString());
//            StatusKehamilan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),13).toString());
//            Gravida.setText(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
//            Para.setText(tbObat.getValueAt(tbObat.getSelectedRow(),15).toString());
//            Abortus.setText(tbObat.getValueAt(tbObat.getSelectedRow(),16).toString());
//            HPHT.setText(tbObat.getValueAt(tbObat.getSelectedRow(),17).toString());
//            Tekanan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),18).toString());
//            Pupil.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),19).toString());
//            Neurosensorik.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),20).toString());
//            Integumen.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),21).toString());
//            Turgor.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),22).toString());
//            Edema.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),23).toString());
//            Mukosa.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),24).toString());
//            Perdarahan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),25).toString());
//            JumlahPerdarahan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),26).toString());
//            WarnaPerdarahan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),27).toString());
//            Intoksikasi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),28).toString());
//            BAB.setText(tbObat.getValueAt(tbObat.getSelectedRow(),29).toString());
//            XBAB.setText(tbObat.getValueAt(tbObat.getSelectedRow(),30).toString());
//            KBAB.setText(tbObat.getValueAt(tbObat.getSelectedRow(),31).toString());
//            WBAB.setText(tbObat.getValueAt(tbObat.getSelectedRow(),32).toString());
//            BAK.setText(tbObat.getValueAt(tbObat.getSelectedRow(),33).toString());
//            XBAK.setText(tbObat.getValueAt(tbObat.getSelectedRow(),34).toString());
//            WBAK.setText(tbObat.getValueAt(tbObat.getSelectedRow(),35).toString());
//            LBAK.setText(tbObat.getValueAt(tbObat.getSelectedRow(),36).toString());
//            Psikologis.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),37).toString());
//            Jiwa.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),38).toString());
//            Perilaku.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),39).toString());
//            Dilaporkan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),40).toString());
//            Sebutkan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),41).toString());
//            Hubungan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),42).toString());
//            StatusPernikahan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),43).toString());
//            TinggalDengan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),44).toString());
//            KetTinggal.setText(tbObat.getValueAt(tbObat.getSelectedRow(),45).toString());
//            Pekerjaan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),46).toString());
//            Pembayaran.setText(tbObat.getValueAt(tbObat.getSelectedRow(),47).toString());
//            StatusBudaya.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),48).toString());
//            KetBudaya.setText(tbObat.getValueAt(tbObat.getSelectedRow(),49).toString());
//            PendidikanPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),50).toString());
//            PendidikanPJ.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),51).toString());
//            KetPendidikanPJ.setText(tbObat.getValueAt(tbObat.getSelectedRow(),52).toString());
//            Edukasi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),53).toString());
//            KetEdukasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),54).toString());
//            ADL.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),55).toString());
//            Aktifitas.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),56).toString());
//            AlatBantu.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),57).toString());
//            KetAlatBantu.setText(tbObat.getValueAt(tbObat.getSelectedRow(),58).toString());
//            Nyeri.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),59).toString());
//            Provokes.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),60).toString());
//            KetProvokes.setText(tbObat.getValueAt(tbObat.getSelectedRow(),61).toString());
//            Quality.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),62).toString());
//            KetQuality.setText(tbObat.getValueAt(tbObat.getSelectedRow(),63).toString());
//            Lokasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),64).toString());
//            Menyebar.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),65).toString());
//            SkalaNyeri.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),66).toString());
//            Durasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),67).toString());
//            NyeriHilang.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),68).toString());
//            KetNyeri.setText(tbObat.getValueAt(tbObat.getSelectedRow(),69).toString());
//            PadaDokter.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),70).toString());
//            KetDokter.setText(tbObat.getValueAt(tbObat.getSelectedRow(),71).toString());
//            ATS.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),72).toString());
//            BJM.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),73).toString());
//            MSA.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),74).toString());
//            Hasil.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),75).toString());
//            Lapor.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),76).toString());
//            KetLapor.setText(tbObat.getValueAt(tbObat.getSelectedRow(),77).toString());
//            Rencana.setText(tbObat.getValueAt(tbObat.getSelectedRow(),78).toString());
            KdPetugas.setText(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString());
            NmPetugas.setText(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString());
            Valid.SetTgl2(TglAsuhan,tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
            
//            try {
//                Valid.tabelKosong(tabModeMasalah);
//                
//                ps=koneksi.prepareStatement(
//                        "select master_masalah_keperawatan_igd.kode_masalah,master_masalah_keperawatan_igd.nama_masalah from master_masalah_keperawatan_igd "+
//                        "inner join penilaian_awal_keperawatan_igd_masalah on penilaian_awal_keperawatan_igd_masalah.kode_masalah=master_masalah_keperawatan_igd.kode_masalah "+
//                        "where penilaian_awal_keperawatan_igd_masalah.no_rawat=? order by penilaian_awal_keperawatan_igd_masalah.kode_masalah");
//                try {
//                    ps.setString(1,tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
//                    rs=ps.executeQuery();
//                    while(rs.next()){
//                        tabModeMasalah.addRow(new Object[]{true,rs.getString(1),rs.getString(2)});
//                    }
//                } catch (Exception e) {
//                    System.out.println("Notif : "+e);
//                } finally{
//                    if(rs!=null){
//                        rs.close();
//                    }
//                    if(ps!=null){
//                        ps.close();
//                    }
//                }
//            } catch (Exception e) {
//                System.out.println("Notif : "+e);
//            }
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
//                    DTPCari1.setDate(rs.getDate("tgl_registrasi"));
                    Jk.setText(rs.getString("jk"));
                    TglLahir.setText(rs.getString("tgl_lahir"));
//                    Agama.setText(rs.getString("agama"));
//                    Bahasa.setText(rs.getString("nama_bahasa"));
//                    CacatFisik.setText(rs.getString("nama_cacat"));
//                    StatusPernikahan.setText(rs.getString("stts_nikah"));
//                    Pekerjaan.setText(rs.getString("pekerjaan"));
//                    PendidikanPasien.setText(rs.getString("pnd"));
//                    Pembayaran.setText("png_jawab");
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
//        TCari.setText(norwt);
//        DTPCari2.setDate(tgl2);    
        isRawat();
    }
    
    
    public void isCek(){
        BtnSimpan.setEnabled(akses.getpenilaian_awal_keperawatan_igd());
        BtnHapus.setEnabled(akses.getpenilaian_awal_keperawatan_igd());
        BtnEdit.setEnabled(akses.getpenilaian_awal_keperawatan_igd());
        BtnEdit.setEnabled(akses.getpenilaian_awal_keperawatan_igd());
//        BtnTambahMasalah.setEnabled(akses.getmaster_masalah_keperawatan_igd());  
        if(akses.getjml2()>=1){
            KdPetugas.setEditable(false);
            BtnDokter.setEnabled(false);
            KdPetugas.setText(akses.getkode());
            Sequel.cariIsi("select petugas.nama from petugas where petugas.nip=?", NmPetugas,KdPetugas.getText());
            if(NmPetugas.getText().equals("")){
                KdPetugas.setText("");
                JOptionPane.showMessageDialog(null,"User login bukan petugas...!!");
            }
        }             
    }

    public void setTampil(){
       tampil();
    }
    
    private void tampilMasalah() {
        try{
            Valid.tabelKosong(tabModeMasalah);
            file=new File("./cache/masalahkeperawatanigd.iyem");
            file.createNewFile();
            fileWriter = new FileWriter(file);
            iyem="";
            ps=koneksi.prepareStatement("select * from master_masalah_keperawatan_igd order by master_masalah_keperawatan_igd.kode_masalah");
            try {
                rs=ps.executeQuery();
                while(rs.next()){
                    tabModeMasalah.addRow(new Object[]{false,rs.getString(1),rs.getString(2)});
                    iyem=iyem+"{\"KodeMasalah\":\""+rs.getString(1)+"\",\"NamaMasalah\":\""+rs.getString(2)+"\"},";
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
            fileWriter.write("{\"masalahkeperawatanigd\":["+iyem.substring(0,iyem.length()-1)+"]}");
            fileWriter.flush();
            fileWriter.close();
            iyem=null;
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
            e.printStackTrace();
        }
    }
    
    private void tampilMasalah2() {
        try{
            jml=0;
//            for(i=0;i<tbMasalahKeperawatan.getRowCount();i++){
//                if(tbMasalahKeperawatan.getValueAt(i,0).toString().equals("true")){
//                    jml++;
//                }
//            }

            pilih=null;
            pilih=new boolean[jml]; 
            kode=null;
            kode=new String[jml];
            masalah=null;
            masalah=new String[jml];

            index=0;        
//            for(i=0;i<tbMasalahKeperawatan.getRowCount();i++){
//                if(tbMasalahKeperawatan.getValueAt(i,0).toString().equals("true")){
//                    pilih[index]=true;
//                    kode[index]=tbMasalahKeperawatan.getValueAt(i,1).toString();
//                    masalah[index]=tbMasalahKeperawatan.getValueAt(i,2).toString();
//                    index++;
//                }
//            } 

            Valid.tabelKosong(tabModeMasalah);

            for(i=0;i<jml;i++){
                tabModeMasalah.addRow(new Object[] {
                    pilih[i],kode[i],masalah[i]
                });
            }
            
//            myObj = new FileReader("./cache/masalahkeperawatanigd.iyem");
//            root = mapper.readTree(myObj);
//            response = root.path("masalahkeperawatanigd");
//            if(response.isArray()){
//                for(JsonNode list:response){
//                    if(list.path("KodeMasalah").asText().toLowerCase().contains(TCariMasalah.getText().toLowerCase())||list.path("NamaMasalah").asText().toLowerCase().contains(TCariMasalah.getText().toLowerCase())){
//                        tabModeMasalah.addRow(new Object[]{
//                            false,list.path("KodeMasalah").asText(),list.path("NamaMasalah").asText()
//                        });                    
//                    }
//                }
//            }
//            myObj.close();
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
            e.printStackTrace();
        }
    }
    
    

    private void getMasalah() {
        if(tbObat.getSelectedRow()!= -1){
//            DetailRencana.setText(tbObat.getValueAt(tbObat.getSelectedRow(),77).toString());
            try {
                Valid.tabelKosong(tabModeDetailMasalah);
                ps=koneksi.prepareStatement(
                        "select master_masalah_keperawatan_igd.kode_masalah,master_masalah_keperawatan_igd.nama_masalah from master_masalah_keperawatan_igd "+
                        "inner join penilaian_awal_keperawatan_igd_masalah on penilaian_awal_keperawatan_igd_masalah.kode_masalah=master_masalah_keperawatan_igd.kode_masalah "+
                        "where penilaian_awal_keperawatan_igd_masalah.no_rawat=? order by penilaian_awal_keperawatan_igd_masalah.kode_masalah");
                try {
                    ps.setString(1,tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                    rs=ps.executeQuery();
                    while(rs.next()){
                        tabModeDetailMasalah.addRow(new Object[]{rs.getString(1),rs.getString(2)});
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
    }
    
}
