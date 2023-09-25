/*
  Dilarang keras menggandakan/mengcopy/menyebarkan/membajak/mendecompile 
  Software ini dalam bentuk apapun tanpa seijin pembuat software
  (Khanza.Soft Media). Bagi yang sengaja membajak softaware ini ta
  npa ijin, kami sumpahi sial 1000 turunan, miskin sampai 500 turu
  nan. Selalu mendapat kecelakaan sampai 400 turunan. Anak pertama
  nya cacat tidak punya kaki sampai 300 turunan. Susah cari jodoh
  sampai umur 50 tahun sampai 200 turunan. Ya Alloh maafkan kami 
  karena telah berdoa buruk, semua ini kami lakukan karena kami ti
  dak pernah rela karya kami dibajak tanpa ijin.
 */

package rekammedis;
import kepegawaian.*;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import restore.DlgRestoreDokter;
import simrskhanza.DlgCariSpesialis;


/**
 *
 * @author dosen
 */
public class MasterStigAtopik extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private DlgCariPegawai pegawai=new DlgCariPegawai(null,false);
    private DlgCariSpesialis spesial=new DlgCariSpesialis(null,false);
    private PreparedStatement stat;
    private ResultSet rs;
    JCheckBox checkBoxList;
    JLabel editButton;
    JLabel deleteButton;
    

    /** Creates new form DlgDokter
     * @param parent
     * @param modal */
    public MasterStigAtopik(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.setLocation(8,1);
        setSize(885,674);

       
    
    
        Object[] row={"Stigmata Atopik", "id"};
        tabMode=new DefaultTableModel(null,row){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbStigAtopik.setModel(tabMode);

//        tampil();

        //tbPetugas.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbPetugas.getBackground()));
        tbStigAtopik.setPreferredScrollableViewportSize(new Dimension(800,800));
        tbStigAtopik.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < 2; i++) {
            TableColumn column = tbStigAtopik.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(100);
            } else if(i==1){
                column.setPreferredWidth(0);
            }
        }
        tbStigAtopik.setDefaultRenderer(Object.class, new WarnaTable());

        TNm.setDocument(new batasInput((byte)50).getKata(TNm));
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
        
        ChkInput.setSelected(false);
        isForm(); 
        
              
        spesial.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(spesial.getTable().getSelectedRow()!= -1){                   
//                    KdSps.setText(spesial.getTable().getValueAt(spesial.getTable().getSelectedRow(),0).toString());
//                    TSpesialis.setText(spesial.getTable().getValueAt(spesial.getTable().getSelectedRow(),1).toString());
                }   
//                KdSps.requestFocus();
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
        
        pegawai.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(pegawai.getTable().getSelectedRow()!= -1){                   
//                    TKd.setText(pegawai.tbKamar.getValueAt(pegawai.tbKamar.getSelectedRow(),0).toString());
                    TNm.setText(pegawai.tbKamar.getValueAt(pegawai.tbKamar.getSelectedRow(),1).toString());
//                    CmbJk.setSelectedItem(pegawai.tbKamar.getValueAt(pegawai.tbKamar.getSelectedRow(),2).toString().replaceAll("Wanita","PEREMPUAN").replaceAll("Pria","LAKI-LAKI"));
//                    TTmp.setText(pegawai.tbKamar.getValueAt(pegawai.tbKamar.getSelectedRow(),11).toString());
//                    TAlmt.setText(pegawai.tbKamar.getValueAt(pegawai.tbKamar.getSelectedRow(),13).toString());
//                    Valid.SetTgl(DTPLahir,pegawai.tbKamar.getValueAt(pegawai.tbKamar.getSelectedRow(),12).toString());
                }   
//                TKd.requestFocus();
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
    }
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Popup = new javax.swing.JPopupMenu();
        MnRestore = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        jPanel1 = new javax.swing.JPanel();
        Scroll = new widget.ScrollPane();
        tbStigAtopik = new widget.Table();
        jPanel2 = new javax.swing.JPanel();
        panelGlass6 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnEdit = new widget.Button();
        BtnAll = new widget.Button();
        jLabel10 = new widget.Label();
        LCount = new widget.Label();
        BtnKeluar = new widget.Button();
        panelGlass8 = new widget.panelisi();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        PanelInput = new javax.swing.JPanel();
        FormInput = new widget.PanelBiasa();
        jLabel4 = new widget.Label();
        TNm = new widget.TextBox();
        ChkInput = new widget.CekBox();

        Popup.setName("Popup"); // NOI18N

        MnRestore.setBackground(new java.awt.Color(255, 255, 254));
        MnRestore.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnRestore.setForeground(new java.awt.Color(50, 50, 50));
        MnRestore.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnRestore.setText("Data Sampah");
        MnRestore.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnRestore.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnRestore.setIconTextGap(5);
        MnRestore.setName("MnRestore"); // NOI18N
        MnRestore.setPreferredSize(new java.awt.Dimension(200, 28));
        MnRestore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnRestoreActionPerformed(evt);
            }
        });
        Popup.add(MnRestore);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Stigmata Atopik ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(452, 402));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        internalFrame1.add(jPanel1, java.awt.BorderLayout.LINE_START);

        Scroll.setComponentPopupMenu(Popup);
        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbStigAtopik.setAutoCreateRowSorter(true);
        tbStigAtopik.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbStigAtopik.setComponentPopupMenu(Popup);
        tbStigAtopik.setName("tbStigAtopik"); // NOI18N
        tbStigAtopik.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbStigAtopikMouseClicked(evt);
            }
        });
        tbStigAtopik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbStigAtopikKeyReleased(evt);
            }
        });
        Scroll.setViewportView(tbStigAtopik);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel2.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass6.setName("panelGlass6"); // NOI18N
        panelGlass6.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass6.setLayout(null);

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setText("Simpan");
        BtnSimpan.setToolTipText("Alt+S");
        BtnSimpan.setName("BtnSimpan"); // NOI18N
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
        panelGlass6.add(BtnSimpan);
        BtnSimpan.setBounds(6, 10, 100, 30);

        BtnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Cancel-2-16x16.png"))); // NOI18N
        BtnBatal.setMnemonic('B');
        BtnBatal.setText("Baru");
        BtnBatal.setToolTipText("Alt+B");
        BtnBatal.setName("BtnBatal"); // NOI18N
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
        panelGlass6.add(BtnBatal);
        BtnBatal.setBounds(108, 10, 100, 30);

        BtnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus.setMnemonic('H');
        BtnHapus.setText("Hapus");
        BtnHapus.setToolTipText("Alt+H");
        BtnHapus.setName("BtnHapus"); // NOI18N
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
        panelGlass6.add(BtnHapus);
        BtnHapus.setBounds(210, 10, 100, 30);

        BtnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/inventaris.png"))); // NOI18N
        BtnEdit.setMnemonic('G');
        BtnEdit.setText("Ganti");
        BtnEdit.setToolTipText("Alt+G");
        BtnEdit.setName("BtnEdit"); // NOI18N
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
        panelGlass6.add(BtnEdit);
        BtnEdit.setBounds(312, 10, 100, 30);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setText("Semua");
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
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
        panelGlass6.add(BtnAll);
        BtnAll.setBounds(420, 10, 100, 30);

        jLabel10.setText("Record :");
        jLabel10.setName("jLabel10"); // NOI18N
        panelGlass6.add(jLabel10);
        jLabel10.setBounds(647, 14, 50, 23);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        panelGlass6.add(LCount);
        LCount.setBounds(700, 14, 60, 23);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
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
        panelGlass6.add(BtnKeluar);
        BtnKeluar.setBounds(774, 10, 100, 30);

        jPanel2.add(panelGlass6, java.awt.BorderLayout.CENTER);

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 10));

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass8.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(230, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TCariKeyTyped(evt);
            }
        });
        panelGlass8.add(TCari);

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
        panelGlass8.add(BtnCari);

        jPanel2.add(panelGlass8, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel2, java.awt.BorderLayout.PAGE_END);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(1331, 20));
        FormInput.setLayout(null);

        jLabel4.setText("Stigmata Atopik :");
        jLabel4.setToolTipText("");
        jLabel4.setName("jLabel4"); // NOI18N
        FormInput.add(jLabel4);
        jLabel4.setBounds(0, 10, 105, 23);

        TNm.setHighlighter(null);
        TNm.setName("TNm"); // NOI18N
        TNm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNmKeyPressed(evt);
            }
        });
        FormInput.add(TNm);
        TNm.setBounds(110, 10, 254, 23);

        PanelInput.add(FormInput, java.awt.BorderLayout.CENTER);

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
        internalFrame1.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TNmKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNmKeyPressed
//        Valid.pindah(evt,TKd,CmbJk);
}//GEN-LAST:event_TNmKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
//        if(TKd.getText().trim().equals("")){
//            Valid.textKosong(TKd,"kode dokter");
//        }else 
if(TNm.getText().trim().equals("")){
            Valid.textKosong(TNm,"stigmata atopik");
        
//else if(TSpesialis.getText().trim().equals("")||KdSps.getText().trim().equals("")){
//            Valid.textKosong(KdSps,"spesialis");
        }else{
            try { 
                LocalDateTime now = LocalDateTime.now();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = now.format(formatter);

                Sequel.AutoComitFalse();
//                Sequel.menyimpanignore("jnj_jabatan","'-','-','0','0'");
//                Sequel.menyimpanignore("departemen","'-','-'");
//                Sequel.menyimpanignore("bidang","'-'");
//                Sequel.menyimpanignore("bank","'T'");
//                Sequel.menyimpanignore("stts_wp","'-','-'");
//                Sequel.menyimpanignore("stts_kerja","'-','-','0'");
//                Sequel.menyimpanignore("kelompok_jabatan","'-','-','0'");
//                Sequel.menyimpanignore("resiko_kerja","'-','-','0'");
//                Sequel.menyimpanignore("emergency_index","'-','-','0'");
//                Sequel.menyimpanignore("pendidikan","'-','0','0','0','0'");
//                Sequel.menyimpanignore("pegawai","'0','"+TKd.getText()+"','"+TNm.getText()+"','"+CmbJk.getSelectedItem().toString().replaceAll("PEREMPUAN","Wanita").replaceAll("LAKI-LAKI","Pria")+"',"+
//                        "'-','-','-','-','-','-','-','-','-','-','-','0','"+TTmp.getText()+"','"+Valid.SetTgl(DTPLahir.getSelectedItem()+"")+"','"+TAlmt.getText()+"','-','1900-01-01','<1','-','T','-','AKTIF','0','0','0','1900-01-01','0','0','pages/pegawai/photo/','-'");        
                Sequel.simpan("tb_stigmata_atopik(stigmata_atopik, created_at)","'"+TNm.getText()+"'","'"+formattedDateTime+"'","id");
                
                        
//                        
//                
//                );
                Sequel.Commit();
                Sequel.AutoComitTrue();
                
                
//                checkBoxList.removeAll();
//                checkBoxList.revalidate();
//                checkBoxList.repaint();
                tampil();
                emptTeks();
            } catch (Exception ex) {
                return;
            }            
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
//            Valid.pindah(evt,TAlumni,BtnBatal);
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        ChkInput.setSelected(true);
        isForm(); 
        emptTeks();        
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            emptTeks();
        }else{Valid.pindah(evt, BtnSimpan, BtnHapus);}
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        try {
//            Sequel.mengedit("dokter","kd_dokter='"+TKd.getText()+"'","status='0'");
//            Sequel.mengedit("pegawai","nik='"+TKd.getText()+"'","stts_aktif='KELUAR'");
            tampil();
            emptTeks();
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
            e.printStackTrace();
        } 
}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnBatal, BtnEdit);
        }
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }else{Valid.pindah(evt,BtnKeluar,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
//        if(TKd.getText().trim().equals("")){
//            Valid.textKosong(TKd,"kode dokter");
//        }
//        else 
        if(TNm.getText().trim().equals("")){
            Valid.textKosong(TNm,"nama dokter");
        }
//        else if(TSpesialis.getText().trim().equals("")||KdSps.getText().trim().equals("")){
//            Valid.textKosong(KdSps,"spesialis");
//        }
        else{
            try { 
                koneksi.setAutoCommit(false);
//                Sequel.mengedit("pegawai","nik='"+tbStigAtopik.getValueAt(tbStigAtopik.getSelectedRow(),0).toString()+"'",
//                        "nik='"+TKd.getText()+"',nama='"+TNm.getText()+"',jk='"+CmbJk.getSelectedItem().toString().replaceAll("PEREMPUAN","Wanita").replaceAll("LAKI-LAKI","Pria")+"',"+
//                        "tmp_lahir='"+TTmp.getText()+"',tgl_lahir='"+Valid.SetTgl(DTPLahir.getSelectedItem()+"")+"',alamat='"+TAlmt.getText()+"'");
//                Sequel.mengedit("dokter","kd_dokter='"+tbStigAtopik.getValueAt(tbStigAtopik.getSelectedRow(),0).toString()+"'","kd_dokter='"+TKd.getText()+"',nm_dokter='"+TNm.getText()+
//                        "',jk='"+CmbJk.getSelectedItem().toString().replaceAll("LAKI-LAKI","L").replaceAll("PEREMPUAN","P").trim()+
//                        "',tmp_lahir='"+TTmp.getText()+
//                        "',tgl_lahir='"+Valid.SetTgl(DTPLahir.getSelectedItem()+"")+
//                        "',gol_drh='"+CMbGd.getSelectedItem()+
//                        "',agama='"+cmbAgama.getSelectedItem()+
//                        "',almt_tgl='"+TAlmt.getText()+
//                        "',no_telp='"+TTlp.getText()+
//                        "',stts_nikah='"+CmbStts.getSelectedItem()+
//                        "',kd_sps='"+KdSps.getText()+
//                        "',alumni='"+TAlumni.getText()+
//                        "',no_ijn_praktek='"+TNoi.getText()+"'");
                koneksi.setAutoCommit(true);
                if(tabMode.getRowCount()!=0){tampil();}
                emptTeks();
            } catch (SQLException ex) {
                return;
            }            
        }
}//GEN-LAST:event_BtnEditActionPerformed

    private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnEditActionPerformed(null);
        }else{
//            Valid.pindah(evt, BtnHapus);
        }
}//GEN-LAST:event_BtnEditKeyPressed

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

    private void TCariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyTyped
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }
    }//GEN-LAST:event_TCariKeyTyped

    private void tbStigAtopikMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbStigAtopikMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
}//GEN-LAST:event_tbStigAtopikMouseClicked

private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
  isForm();                
}//GEN-LAST:event_ChkInputActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        tampil();
    }//GEN-LAST:event_formWindowOpened

    private void MnRestoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnRestoreActionPerformed
        DlgRestoreDokter restore=new DlgRestoreDokter(null,true);
        restore.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        restore.setLocationRelativeTo(internalFrame1);
        restore.setVisible(true);
    }//GEN-LAST:event_MnRestoreActionPerformed

    private void tbStigAtopikKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbStigAtopikKeyReleased
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbStigAtopikKeyReleased

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnAllActionPerformed(null);
        }else{
//            Valid.pindah(evt, BtnPrint, BtnKeluar);
        }
    }//GEN-LAST:event_BtnAllKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        //        CmbCrStts.setSelectedIndex(0);
        //        cmbCrJk.setSelectedIndex(0);
        //        CmbCrGd.setSelectedIndex(0);
        TCari.setText("");
        tampil();
    }//GEN-LAST:event_BtnAllActionPerformed

    private void editButtonActionPerformed(MouseEvent evt) {
        if(TNm.getText().trim().equals("")){
            Valid.textKosong(TNm,"stigmata atopik");
        }else{
            try { 
                LocalDateTime now = LocalDateTime.now();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = now.format(formatter);
                
                koneksi.setAutoCommit(false);
                Sequel.edit("tb_stigmata_atopik",
                        "stigmata_atopik='"+TNm.getText()+"',updated_at='"+formattedDateTime+"'",
                        "stigmata_atopik='"+tbStigAtopik.getValueAt(tbStigAtopik.getSelectedRow(),0).toString()+"'");
//                Sequel.mengedit("dokter","kd_dokter='"+tbStigAtopik.getValueAt(tbStigAtopik.getSelectedRow(),0).toString()+"'","kd_dokter='"+TKd.getText()+"',nm_dokter='"+TNm.getText()+
//                        "',jk='"+CmbJk.getSelectedItem().toString().replaceAll("LAKI-LAKI","L").replaceAll("PEREMPUAN","P").trim()+
//                        "',tmp_lahir='"+TTmp.getText()+
//                        "',tgl_lahir='"+Valid.SetTgl(DTPLahir.getSelectedItem()+"")+
//                        "',gol_drh='"+CMbGd.getSelectedItem()+
//                        "',agama='"+cmbAgama.getSelectedItem()+
//                        "',almt_tgl='"+TAlmt.getText()+
//                        "',no_telp='"+TTlp.getText()+
//                        "',stts_nikah='"+CmbStts.getSelectedItem()+
//                        "',kd_sps='"+KdSps.getText()+
//                        "',alumni='"+TAlumni.getText()+
//                        "',no_ijn_praktek='"+TNoi.getText()+"'");
                koneksi.setAutoCommit(true);
                if(tabMode.getRowCount()!=0){tampil();}
                emptTeks();
            } catch (SQLException ex) {
                return;
            }            
        }
    }
    
    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {
        
    }
    
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(() -> {
            MasterStigAtopik dialog = new MasterStigAtopik(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
            
        });
//        
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.Button BtnAll;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnSimpan;
    private widget.CekBox ChkInput;
    private widget.PanelBiasa FormInput;
    private widget.Label LCount;
    private javax.swing.JMenuItem MnRestore;
    private javax.swing.JPanel PanelInput;
    private javax.swing.JPopupMenu Popup;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.TextBox TNm;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel10;
    private widget.Label jLabel4;
    private widget.Label jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private widget.panelisi panelGlass6;
    private widget.panelisi panelGlass8;
    private widget.Table tbStigAtopik;
    // End of variables declaration//GEN-END:variables

    private void tampil() {
        Valid.tabelKosong(tabMode);
        try {
            stat=koneksi.prepareStatement(
                   "select stigmata_atopik, id from tb_stigmata_atopik where status=1");
            try{
                
                rs=stat.executeQuery();
        
                while(rs.next()){
                    tabMode.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2)
                                   }); 
                }
                
                // Get all of the components in the panel.
                Component[] components = jPanel1.getComponents();

                // Iterate over the components and remove all of the checkboxes.
                for (Component component : components) {
                    if (component instanceof JPanel) {
                        jPanel1.remove(component);
                    }
                }
                
                int num = tabMode.getRowCount();

                for(int i = 0; i < num; i++) {

                    ImageIcon editIcon = new ImageIcon(new ImageIcon("src/picture/EDIT2.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
                    ImageIcon deleteIcon = new ImageIcon(new ImageIcon("src/picture/cross.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));

                    editButton = new JLabel(editIcon);
                    JButton deleteButton = new JButton(deleteIcon);
                    deleteButton.setBorderPainted( false );
                    
                    CheckboxGroup cbg = new CheckboxGroup();
                    Checkbox checkbox1 = new Checkbox(String.valueOf(tabMode.getValueAt(i, 0)), cbg, false);
                    checkbox1.setName(String.valueOf(tabMode.getValueAt(i, 1)));
                    
                    
                    checkBoxList = new JCheckBox(String.valueOf(tabMode.getValueAt(i, 0)));
                    checkBoxList.setName(String.valueOf(tabMode.getValueAt(i, 1)));
                    editButton.setName(String.valueOf(tabMode.getValueAt(i, 1)));
                    deleteButton.setName(String.valueOf(tabMode.getValueAt(i, 1)));

                    editButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent evt) {
                            System.out.println("Yay you clicked me");
                            System.out.println(editButton.getName());
                            System.out.println(checkbox1.getName());
                            System.out.println(checkBoxList.getName());
                            editButtonActionPerformed(evt);
                        }

                    });
//                    editButton.addMouseListener(new MouseListener() {
//                        public void mouseListener(ActionEvent e)
//                        {
//                            System.out.println("You clicked button "+e.getSource().toString());
//                        }
//                    });
                    deleteButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e)
                        {
                            System.out.println("You clicked button "+e.getSource().toString());
                        }
                    });
                    
                    checkBoxList.setBackground(Color.white);
                    checkBoxList.setFont(
                        checkBoxList.getFont().deriveFont(
                          Font.PLAIN,
                          checkBoxList.getFont().getSize()
                        ));
                    JPanel panel = new JPanel();
                    panel.setBackground(Color.white);
                    panel.setName(String.valueOf(tabMode.getValueAt(i, 1)));
                    panel.add(checkbox1,BorderLayout.PAGE_START);
                    panel.add(editButton);
                    panel.add(deleteButton);
//                    jPanel1.add(checkBoxList, BorderLayout.PAGE_START);
                    jPanel1.add(panel);
                    
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
        LCount.setText(""+tabMode.getRowCount());
    }

    public void emptTeks() {
        TNm.setText("");
//        Valid.autoNomer(" dokter ","D",7,TNm);
    }

    private void getData() {
//        int row=tbStigAtopik.getSelectedRow();
//        if(row!= -1){
//            
//            TNm.setText(tbStigAtopik.getValueAt(row,1).toString());
//            Sequel.cariIsi("select kd_sps from spesialis where nm_sps='"+tbStigAtopik.getValueAt(row,10).toString()+"'", TNm);
//            
//            switch (tbStigAtopik.getValueAt(row,2).toString()) {
//                case "L":
////                    CmbJk.setSelectedItem("LAKI-LAKI");
//                    break;
//                case "P":
////                    CmbJk.setSelectedItem("PEREMPUAN");
//                    break;
//            }
//            
////            Valid.SetTgl(DTPLahir,tbStigAtopik.getValueAt(row,4).toString());
//        }
    }

    
    public JTextField getTextField(){
        
        return TNm;
    }

    public JButton getButton(){
        return BtnKeluar;
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,189));
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
        BtnSimpan.setEnabled(akses.getdokter());
        BtnHapus.setEnabled(akses.getdokter());
        BtnEdit.setEnabled(akses.getdokter());
        if(akses.getkode().equals("Admin Utama")){
            MnRestore.setEnabled(true);
        }else{
            MnRestore.setEnabled(false);
        } 
    }
}
