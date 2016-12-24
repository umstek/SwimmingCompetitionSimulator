/*
 * FrameCompetition.java
 * GUI for managing the swimming competition.
 * 
 */
package swimmingcompetition.ux;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import swimmingcompetition.simulator.Color;
import swimmingcompetition.simulator.Stroke;
import swimmingcompetition.ux.viewmodels.ModelCompetition;
import swimmingcompetition.ux.viewmodels.ModelPrepareRound;
import swimmingcompetition.ux.viewmodels.UIUpdater;

/**
 *
 * @author Wickramaranga
 */
public class FrameCompetition extends javax.swing.JFrame {

    private final ModelCompetition competitionModel;

    /**
     * Creates new form FrameCompetition
     *
     * @param compModel
     */
    public FrameCompetition(ModelCompetition compModel) {
        this.competitionModel = compModel;
        initComponents();
        final JLabel[] swimmerLabels = {jLabelS1, jLabelS2, jLabelS3, jLabelS4, jLabelS5, jLabelS6,
                                        jLabelS7, jLabelS8, jLabelS9, jLabelS10, jLabelS11,
                                        jLabelS12};
        final JLabel[] spectatorLabels = {
            jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7, jLabel8, jLabel9, jLabel10,
            jLabel11, jLabel12, jLabel13, jLabel14, jLabel15, jLabel16, jLabel17, jLabel18,
            jLabel19, jLabel20, jLabel21, jLabel22, jLabel23, jLabel24, jLabel25, jLabel26,
            jLabel27, jLabel28, jLabel29, jLabel30, jLabel31, jLabel32, jLabel33, jLabel34,
            jLabel35, jLabel36, jLabel37, jLabel38, jLabel39, jLabel40, jLabel41, jLabel42,
            jLabel43, jLabel44, jLabel45, jLabel46, jLabel47, jLabel48, jLabel49, jLabel50,
            jLabel51, jLabel52, jLabel53, jLabel54, jLabel55, jLabel56
        };
        final JLabel[] judgeLabels = {
            jLabel57, jLabel58, jLabel59, jLabel60, jLabel61, jLabel62, jLabel63, jLabel64,
            jLabel65, jLabel66, jLabel67, jLabel68, jLabel69, jLabel70, jLabel71, jLabel72,
            jLabel73, jLabel74, jLabel75, jLabel76, jLabel77, jLabel78, jLabel79, jLabel80,
            jLabel81, jLabel82, jLabel1};
        final JLabel[] staffLabels = {
            jLabel83, jLabel84, jLabel85, jLabel86, jLabel87, jLabel88, jLabel89, jLabel90,
            jLabel91, jLabel92, jLabel93, jLabel94, jLabel95};

        competitionModel.setGeneralUpdater(new UIUpdater() {

            @Override
            public void updateUI(String partToUpdate) {
                switch (partToUpdate) {
                    case "buttons":
                        jButtonPrepare.setEnabled(competitionModel.canPrepare());
                        jButtonStart.setEnabled(competitionModel.canStart());
                        jButtonSave.setEnabled(competitionModel.canSave());
                        break;
                    case "poolbegin":
                        // Information needed to fetch and set the correct image
                        Color uniformColor = competitionModel.getUniformColor();
                        final String COLOR_CONST = uniformColor.toString().toLowerCase();
                        Stroke stroke = competitionModel.getStroke();
                        final String STROKE_CONST = stroke.toString().toLowerCase();
                        final String IMG_SRC = "/swimmingcompetition/ux/images/";
                        final String EXT = ".gif";
                        ImageIcon icon = new javax.swing.ImageIcon(
                                getClass().getResource(
                                        String.format("%s%s_%s%s",
                                                      IMG_SRC, COLOR_CONST, STROKE_CONST, EXT)));

                        // Information needed to set swimmer names
                        List<String> swimmerNames = competitionModel.getActiveSwimmerNames();

                        int laneCount = competitionModel.getActiveLaneCount();
                        for (int i = 0; i < laneCount; i++) {
                            swimmerLabels[i].setVisible(true);
                            swimmerLabels[i].setIcon(icon);
                            swimmerLabels[i].setToolTipText(swimmerNames.get(i));
                            swimmerLabels[i].setLocation(competitionModel.getInitialX(),
                                                         swimmerLabels[i].getY());
                        }
                        for (int i = laneCount; i < 12; i++) {
                            swimmerLabels[i].setVisible(false);
                        }
                        break;
                    case "poolend":
                        for (JLabel swimmerLabel : swimmerLabels) {
                            swimmerLabel.setVisible(false);
                        }
                        break;
                    case "message":
                        jLabelMsg.setText(competitionModel.getMessage());
                        break;
                    default:
                        assert false;
                }
            }
        });

        competitionModel.setPoolUpdater(new UIUpdater() {

            @Override
            public void updateUI(String partToUpdate) {
                int laneCount = competitionModel.getActiveLaneCount();
                for (int i = 0; i < laneCount; i++) {
                    swimmerLabels[i].setLocation(competitionModel.getSwimmerPositions().get(i),
                                                 swimmerLabels[i].getY());
                }
            }
        });

        competitionModel.setScoreboardUpdater(new UIUpdater() {

            @Override
            public void updateUI(String partToUpdate) {
                DefaultTableModel model = (DefaultTableModel) jTableScorecards.getModel();
                switch (partToUpdate) {
                    case "refresh": // Load saved data if any. 
                        if (competitionModel.getOldTimecardCount() > 0) {
                            SpinnerNumberModel snm = (SpinnerNumberModel) jSpinnerOld.getModel();
                            snm.setMinimum(1);
                            // Last round is not pushed into oldTimecards. That's why the ... + 1.
                            snm.setMaximum(competitionModel.getOldTimecardCount() + 1);
                            snm.setValue(competitionModel.getOldTimecardCount() + 1);
                        }
                        break;
                    case "roundbegin":
                        SpinnerNumberModel snm = (SpinnerNumberModel) jSpinnerOld.getModel();
                        snm.setMinimum(1);
                        // Well, this is the new round. That's why the ... + 1.
                        snm.setMaximum(competitionModel.getOldTimecardCount() + 1);
                        snm.setValue(competitionModel.getOldTimecardCount() + 1);
                        jSpinnerOld.setEnabled(false);
                        jButtonShowRound.setEnabled(false);
                        break;
                    case "roundend":
                        jSpinnerOld.setEnabled(true);
                        jButtonShowRound.setEnabled(true);
                        break;
                    case "clear":
                        model.setRowCount(0);
                        break;
                    case "swimmers":
                        model.setRowCount(0);

                        List<String> ranking = competitionModel.getRanking();
                        List<String> timesTaken = competitionModel.getTimesTaken();

                        for (int i = 0; i < ranking.size(); i++) {
                            model.addRow(new String[]{ranking.get(i), timesTaken.get(i)});
                        }
                        break;
                    case "show":
                        model.setRowCount(0);

                        SpinnerNumberModel snm1 = (SpinnerNumberModel) jSpinnerOld.getModel();
                        int selectedIndex = (int) snm1.getNumber() - 1;

                        // Get data of current round instead of old one.
                        if (selectedIndex == (int) snm1.getMaximum() - 1) {
                            List<String> ranking0 = competitionModel.getRanking();
                            List<String> timesTaken0 = competitionModel.getTimesTaken();

                            for (int i = 0; i < ranking0.size(); i++) {
                                model.addRow(new String[]{ranking0.get(i), timesTaken0.get(i)});
                            }
                            break; // Exit from switch immediately
                        }

                        List<String> rankingOld
                                = competitionModel.getOldRanking((int) snm1.getNumber() - 1);
                        List<String> timesTakenOld
                                = competitionModel.getOldTimesTaken((int) snm1.getNumber() - 1);

                        for (int i = 0; i < rankingOld.size(); i++) {
                            model.addRow(new String[]{rankingOld.get(i), timesTakenOld.get(i)});
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        competitionModel.refreshScoreboard();
        jLabelMsg.setText(competitionModel.getMessage());

        // Hide unnecessary lables. Show labels only if a person exists.
        for (JLabel judgeLabel : judgeLabels) {
            judgeLabel.setVisible(false);
        }
        for (JLabel spectatorLabel : spectatorLabels) {
            spectatorLabel.setVisible(false);
        }
        for (JLabel staffLabel : staffLabels) {
            staffLabel.setVisible(false);
        }

        final String IMG_SRC = "/swimmingcompetition/ux/images/";
        final String SPECT = "spectator";
        final String JUDGE = "judge";
        final String STAFF = "support";
        for (int i = 0; i < competitionModel.getSpectatorNames().size(); i++) {
            int index = (int) (Math.random() * 1000) % spectatorLabels.length;
            int picture = (int) (Math.random() * 1000) % 4 + 1;
            spectatorLabels[index].setIcon(new javax.swing.ImageIcon(
                    getClass().getResource(IMG_SRC + SPECT + (picture) + ".png")));
            spectatorLabels[index].setVisible(true);
            spectatorLabels[index].setToolTipText(competitionModel.getSpectatorNames().get(i));
        }
        for (int i = 0; i < competitionModel.getJudgeNames().size(); i++) {
            int index = (int) (Math.random() * 1000) % judgeLabels.length;
            int picture = (int) (Math.random() * 1000) % 4 + 1;
            judgeLabels[index].setIcon(new javax.swing.ImageIcon(
                    getClass().getResource(IMG_SRC + JUDGE + (picture) + ".png")));
            judgeLabels[index].setVisible(true);
            judgeLabels[index].setToolTipText(competitionModel.getJudgeNames().get(i));
        }
        for (int i = 0; i < competitionModel.getStaffMemberNames().size(); i++) {
            int index = (int) (Math.random() * 1000) % staffLabels.length;
            int picture = (int) (Math.random() * 1000) % 4 + 1;
            staffLabels[index].setIcon(new javax.swing.ImageIcon(
                    getClass().getResource(IMG_SRC + STAFF + (picture) + ".png")));
            staffLabels[index].setVisible(true);
            staffLabels[index].setToolTipText(competitionModel.getStaffMemberNames().get(i));
        }

        for (int i = competitionModel.getLaneCount(); i < 12; i++) {
            swimmerLabels[i].setVisible(false);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelSwimmingPool = new javax.swing.JPanel();
        jLabelS1 = new javax.swing.JLabel();
        jLabelS2 = new javax.swing.JLabel();
        jLabelS3 = new javax.swing.JLabel();
        jLabelS4 = new javax.swing.JLabel();
        jLabelS5 = new javax.swing.JLabel();
        jLabelS6 = new javax.swing.JLabel();
        jLabelS7 = new javax.swing.JLabel();
        jLabelS8 = new javax.swing.JLabel();
        jLabelS9 = new javax.swing.JLabel();
        jLabelS10 = new javax.swing.JLabel();
        jLabelS11 = new javax.swing.JLabel();
        jLabelS12 = new javax.swing.JLabel();
        jLabelPool = new javax.swing.JLabel();
        jPanelPavilion = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        jLabelSteps = new javax.swing.JLabel();
        jPanelScoreboard = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableScorecards = new javax.swing.JTable();
        jSpinnerOld = new javax.swing.JSpinner();
        jLabelOld = new javax.swing.JLabel();
        jButtonShowRound = new javax.swing.JButton();
        jButtonPrepare = new javax.swing.JButton();
        jButtonStart = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jLabelMsg = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Swimming Competition Simulator");
        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(1280, 720));
        setPreferredSize(new java.awt.Dimension(1280, 720));
        setResizable(false);

        jPanelSwimmingPool.setBackground(new java.awt.Color(229, 242, 255));
        jPanelSwimmingPool.setPreferredSize(new java.awt.Dimension(854, 480));
        jPanelSwimmingPool.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelS1.setText("1");
        jPanelSwimmingPool.add(jLabelS1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 216, 70, 30));

        jLabelS2.setText("2");
        jPanelSwimmingPool.add(jLabelS2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 257, 70, 30));

        jLabelS3.setText("3");
        jPanelSwimmingPool.add(jLabelS3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 175, 70, 30));

        jLabelS4.setText("4");
        jPanelSwimmingPool.add(jLabelS4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 298, 70, 30));

        jLabelS5.setText("5");
        jPanelSwimmingPool.add(jLabelS5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 134, 70, 30));

        jLabelS6.setText("6");
        jPanelSwimmingPool.add(jLabelS6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 339, 70, 30));

        jLabelS7.setText("7");
        jPanelSwimmingPool.add(jLabelS7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 93, 70, 30));

        jLabelS8.setText("8");
        jPanelSwimmingPool.add(jLabelS8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 70, 30));

        jLabelS9.setText("9");
        jPanelSwimmingPool.add(jLabelS9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 52, 70, 30));

        jLabelS10.setText("10");
        jPanelSwimmingPool.add(jLabelS10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 421, 70, 30));

        jLabelS11.setText("11");
        jPanelSwimmingPool.add(jLabelS11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 70, 30));

        jLabelS12.setText("12");
        jPanelSwimmingPool.add(jLabelS12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 462, 70, 30));

        jLabelPool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/pool.png"))); // NOI18N
        jPanelSwimmingPool.add(jLabelPool, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 503));

        jPanelPavilion.setBackground(new java.awt.Color(153, 102, 204));
        jPanelPavilion.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 80, -1, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, -1, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, -1, -1));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, -1, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, -1, -1));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 10, -1, -1));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 10, -1, -1));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, -1, -1));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, -1, -1));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, -1, -1));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, -1, -1));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 10, -1, -1));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, -1, -1));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 10, -1, -1));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 10, -1, -1));

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 10, -1, -1));

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 10, -1, -1));

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 10, -1, -1));

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 10, -1, -1));

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 10, -1, -1));

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 10, -1, -1));

        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 10, -1, -1));

        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 10, -1, -1));

        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 10, -1, -1));

        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 10, -1, -1));

        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 10, -1, -1));

        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator2.png"))); // NOI18N
        jPanelPavilion.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 10, -1, -1));

        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, -1, -1));

        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, -1, -1));

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, -1, -1));

        jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, -1, -1));

        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, -1, -1));

        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 40, -1, -1));

        jLabel37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 40, -1, -1));

        jLabel38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 40, -1, -1));

        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 40, -1, -1));

        jLabel40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 40, -1, -1));

        jLabel41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 40, -1, -1));

        jLabel42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 40, -1, -1));

        jLabel43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 40, -1, -1));

        jLabel44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 40, -1, -1));

        jLabel45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 40, -1, -1));

        jLabel46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 40, -1, -1));

        jLabel47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 40, -1, -1));

        jLabel48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 40, -1, -1));

        jLabel49.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 40, -1, -1));

        jLabel50.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 40, -1, -1));

        jLabel51.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 40, -1, -1));

        jLabel52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 40, -1, -1));

        jLabel53.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 40, -1, -1));

        jLabel54.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 40, -1, -1));

        jLabel55.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 40, -1, -1));

        jLabel56.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/spectator1.png"))); // NOI18N
        jPanelPavilion.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 40, -1, -1));

        jLabel57.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        jLabel58.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, -1, -1));

        jLabel59.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 80, -1, -1));

        jLabel60.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, -1, -1));

        jLabel61.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, -1, -1));

        jLabel62.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, -1, -1));

        jLabel63.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, -1, -1));

        jLabel64.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 80, -1, -1));

        jLabel65.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 80, -1, -1));

        jLabel66.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 80, -1, -1));

        jLabel67.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 80, -1, -1));

        jLabel68.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 80, -1, -1));

        jLabel69.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 80, -1, -1));

        jLabel70.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 80, -1, -1));

        jLabel71.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 80, -1, -1));

        jLabel72.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 80, -1, -1));

        jLabel73.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 80, -1, -1));

        jLabel74.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 80, -1, -1));

        jLabel75.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 80, -1, -1));

        jLabel76.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel76, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 80, -1, -1));

        jLabel77.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 80, -1, -1));

        jLabel78.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 80, -1, -1));

        jLabel79.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 80, -1, -1));

        jLabel80.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel80, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 80, -1, -1));

        jLabel81.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel81, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 80, -1, -1));

        jLabel82.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/judge1.png"))); // NOI18N
        jPanelPavilion.add(jLabel82, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 80, -1, -1));

        jLabelSteps.setBackground(new java.awt.Color(204, 153, 255));
        jLabelSteps.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/steps.png"))); // NOI18N
        jPanelPavilion.add(jLabelSteps, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 850, 160));

        jPanelScoreboard.setBackground(new java.awt.Color(0, 204, 153));

        jTableScorecards.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Time"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableScorecards);

        jSpinnerOld.setModel(new javax.swing.SpinnerNumberModel(0, 0, 0, 1));

        jLabelOld.setText("Previous rounds:");

        jButtonShowRound.setText("Show");
        jButtonShowRound.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonShowRoundActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelScoreboardLayout = new javax.swing.GroupLayout(jPanelScoreboard);
        jPanelScoreboard.setLayout(jPanelScoreboardLayout);
        jPanelScoreboardLayout.setHorizontalGroup(
            jPanelScoreboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelScoreboardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelScoreboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanelScoreboardLayout.createSequentialGroup()
                        .addComponent(jLabelOld, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jSpinnerOld, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonShowRound, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanelScoreboardLayout.setVerticalGroup(
            jPanelScoreboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelScoreboardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelScoreboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerOld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelOld)
                    .addComponent(jButtonShowRound))
                .addGap(7, 7, 7)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButtonPrepare.setText("New Round");
        jButtonPrepare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrepareActionPerformed(evt);
            }
        });

        jButtonStart.setText("Start");
        jButtonStart.setEnabled(false);
        jButtonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStartActionPerformed(evt);
            }
        });

        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jLabelMsg.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabelMsg.setForeground(new java.awt.Color(0, 153, 153));
        jLabelMsg.setText("Message");

        jPanel1.setBackground(new java.awt.Color(0, 204, 204));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel83.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/support1.png"))); // NOI18N
        jPanel1.add(jLabel83, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, -1, -1));

        jLabel84.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/support1.png"))); // NOI18N
        jPanel1.add(jLabel84, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 606, -1, -1));

        jLabel85.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/support1.png"))); // NOI18N
        jPanel1.add(jLabel85, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 600, -1, -1));

        jLabel86.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/support1.png"))); // NOI18N
        jPanel1.add(jLabel86, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 580, -1, -1));

        jLabel87.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/support1.png"))); // NOI18N
        jPanel1.add(jLabel87, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 570, -1, -1));

        jLabel88.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/support1.png"))); // NOI18N
        jPanel1.add(jLabel88, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, -1, -1));

        jLabel89.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/support1.png"))); // NOI18N
        jPanel1.add(jLabel89, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 540, -1, -1));

        jLabel90.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/support1.png"))); // NOI18N
        jPanel1.add(jLabel90, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 500, -1, -1));

        jLabel91.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/support1.png"))); // NOI18N
        jPanel1.add(jLabel91, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 480, -1, -1));

        jLabel92.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/support1.png"))); // NOI18N
        jPanel1.add(jLabel92, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 460, -1, -1));

        jLabel93.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/support1.png"))); // NOI18N
        jPanel1.add(jLabel93, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, -1, -1));

        jLabel94.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/support1.png"))); // NOI18N
        jPanel1.add(jLabel94, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 420, -1, -1));

        jLabel95.setIcon(new javax.swing.ImageIcon(getClass().getResource("/swimmingcompetition/ux/images/support1.png"))); // NOI18N
        jPanel1.add(jLabel95, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelSwimmingPool, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelPavilion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelMsg)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanelScoreboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonPrepare)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonStart, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonPrepare)
                                    .addComponent(jButtonStart)
                                    .addComponent(jButtonSave))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelMsg))
                            .addComponent(jPanelPavilion, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanelSwimmingPool, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanelScoreboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(0, 33, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonPrepareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrepareActionPerformed
        final ModelPrepareRound roundModel
                = new ModelPrepareRound(
                        competitionModel.getSwimmers(),
                        competitionModel.getLaneCount());
        final DialogPrepareRound prepareDialog
                = new DialogPrepareRound(this, true, roundModel);
        prepareDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (prepareDialog.isOkay()) {
                    competitionModel.prepare(roundModel.getGender(),
                                             roundModel.getStroke(),
                                             roundModel.getSelectedSwimmers());
                }
            }
        });
        prepareDialog.setVisible(true);
    }//GEN-LAST:event_jButtonPrepareActionPerformed

    private void jButtonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStartActionPerformed
        if (competitionModel.canStart()) {
            competitionModel.start();
        }
    }//GEN-LAST:event_jButtonStartActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        if (competitionModel.canSave()) {
            String fileName = JOptionPane.showInputDialog("Enter file name: ");
            if (fileName != null && !fileName.trim().isEmpty()) {
                competitionModel.save(fileName);
            }
        }

    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonShowRoundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonShowRoundActionPerformed
        if (competitionModel.getOldTimecardCount() > 0) {
            competitionModel.getOldData();
        }
    }//GEN-LAST:event_jButtonShowRoundActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonPrepare;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonShowRound;
    private javax.swing.JButton jButtonStart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabelMsg;
    private javax.swing.JLabel jLabelOld;
    private javax.swing.JLabel jLabelPool;
    private javax.swing.JLabel jLabelS1;
    private javax.swing.JLabel jLabelS10;
    private javax.swing.JLabel jLabelS11;
    private javax.swing.JLabel jLabelS12;
    private javax.swing.JLabel jLabelS2;
    private javax.swing.JLabel jLabelS3;
    private javax.swing.JLabel jLabelS4;
    private javax.swing.JLabel jLabelS5;
    private javax.swing.JLabel jLabelS6;
    private javax.swing.JLabel jLabelS7;
    private javax.swing.JLabel jLabelS8;
    private javax.swing.JLabel jLabelS9;
    private javax.swing.JLabel jLabelSteps;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelPavilion;
    private javax.swing.JPanel jPanelScoreboard;
    private javax.swing.JPanel jPanelSwimmingPool;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinnerOld;
    private javax.swing.JTable jTableScorecards;
    // End of variables declaration//GEN-END:variables
}
