/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import game.TTTWebService;
import game.TTTWebService_Service;
import java.awt.Color;
import java.awt.List;
import java.util.ArrayList;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Vineeth
 */
public class Leaderboard extends javax.swing.JFrame {

    /**
     * Creates new form Leaderboard
     */
    private String username;
    private int win;
    private int loss;
    private int draws;

    public Leaderboard() {
        initComponents();
    }
    
    /*
    Creating a construction which is taking the username as the parameter
    This constructor will display the username in the screen.
    */
    // Constructor passing username as constructor
    public Leaderboard(String name) {
        username = name;
        initComponents();
        displayStats();
    }
    
    /*The displayStats fuctions will check the daabase using the webservice.
    This displays the number of wins,loss and draws of the registered user in the screen.
    
    */
    // Function which display the stats in the jtable
    public void displayStats() {
        TTTWebService_Service webservice = new TTTWebService_Service();
        TTTWebService proxy = webservice.getTTTWebServicePort();
        String Stats = proxy.leagueTable();
        
        //  Switch cases to check the different donditions
        switch (Stats) {
            case "ERROR-NOGAMES":
                ErrorMsg.setText("No Games");
                ErrorMsg.setForeground(Color.red);

                break;

            case "ERROR-DB ":
                ErrorMsg.setText("Failed to connect Database");
                ErrorMsg.setForeground(Color.red);
                break;
                /*In Default condition the using the leadrboard function of the webservice
                the values of each registered users are retrieved and displayed in the jtable
                in the leaderboard table.
                The username,Wins, loss and draws are displayed.
                */
            default:
                DefaultTableModel showgame = (DefaultTableModel) leadrbrd.getModel();
                Object[] row = new Object[3];
                String[] parts = Stats.split("\\n");
                String[][] Statics = new String[parts.length][5];
                ArrayList<String> values = new ArrayList<>();
                for (int j = 0; j < parts.length; j++) {
                    String gameID = Statics[j][0] = parts[j].split(",")[0];
                    String p1 = Statics[j][1] = parts[j].split(",")[1];
                    // Checking and adding the values of p1 in the list
                    if (!values.contains(p1)) {
                        values.add(p1);
                    }
                    // Checking and adding the values of p2 in the list.
                    String p2 = Statics[j][2] = parts[j].split(",")[2];
                    if (!values.contains(p2)) {
                        values.add(p2);
                    }
                    String gameState = Statics[j][3] = parts[j].split(",")[3];
                    String timeStamp = Statics[j][4] = parts[j].split(",")[4];
                }
                // Checking the condtions of wins, loss and draw.
                DefaultTableModel valuesofLeaderboard = (DefaultTableModel) leadrbrd.getModel();
                for (String u : values) {
                    for (int k = 0; k < Statics.length; k++) {
                        if (u.equals(Statics[k][1]) || u.equals(Statics[k][2])) {
                            if (u.equals(Statics[k][1]) && Statics[k][3].equals("1")) {
                                win++;
                            } else if (u.equals(Statics[k][2]) && Statics[k][3].equals("2")) {
                                win++;
                            } else if (Statics[k][3].equals("3")) {
                                draws++;
                            } else if (!Statics[k][3].equals("0") && !Statics[k][3].equals("-1") ) {
                                loss++;
                            }
                        }
                    }
                    Object[] rowLeader = new Object[4];
                    rowLeader[0] = u;
                    rowLeader[1]= win;
                    rowLeader[2] = loss;
                    rowLeader[3]= draws;
                    valuesofLeaderboard.addRow(rowLeader);
                    win = loss = draws = 0;
                }

//                    if (username.equals(p1) || username.equals(p2)) {
//                        if (username.equals(p1) && gameState.equals("1")) {
//                            //win++;
//                        } else if (username.equals(p2) && gameState.equals("2")) {
//                           // win++;
//                        } else if (gameState.equals("3")) {
//                           // draws++;
//                        } else {
//                           // loss++;
//                        }
//
//                    }
        }
        // Sorting the table in descending order.
        TableRowSorter<TableModel> leadrbrdsorting = new TableRowSorter<>(leadrbrd.getModel());
        leadrbrd.setRowSorter(leadrbrdsorting);
        ArrayList<RowSorter.SortKey> leadrbrdsortVal = new ArrayList<>();

        int columnIndexToSort = 1;
        leadrbrdsortVal.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.DESCENDING));

        leadrbrdsorting.setSortKeys(leadrbrdsortVal);
        leadrbrdsorting.sort();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        leadrbrd = new javax.swing.JTable();
        ErrorMsg = new javax.swing.JLabel();
        OK = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Leader Board");

        leadrbrd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "User Name", "Wins", "Losses", "Draw"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(leadrbrd);

        OK.setText("OK");
        OK.setToolTipText("");
        OK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(159, 159, 159)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(165, 165, 165)
                                .addComponent(OK)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ErrorMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(130, 130, 130))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ErrorMsg)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(OK)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_OKActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Leaderboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Leaderboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Leaderboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Leaderboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Leaderboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ErrorMsg;
    private javax.swing.JButton OK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable leadrbrd;
    // End of variables declaration//GEN-END:variables
}
