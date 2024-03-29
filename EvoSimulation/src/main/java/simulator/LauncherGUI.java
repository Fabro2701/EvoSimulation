package simulator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import simulator.control.Controller;

import simulator.view.TimeLabel;
import simulator.view.viewer.AbstractViewer;
import simulator.view.viewer.EmptyViewer;
import simulator.view.viewer.Viewer;
import simulator.view.viewer.Viewer3D;

public class LauncherGUI extends javax.swing.JFrame {
	private Controller controller;
	private HashMap<String,AbstractViewer>viewersMap;
	private AbstractViewer viewer;
	private boolean simStop;
	private ViewersController viewsController;

	
	public LauncherGUI(Controller controller) {
		simStop = false;
		this.controller = controller;
		initComponents();
        viewsController = new ViewersController();
        configureComponents();
        this.setLocationRelativeTo(null);
	}
	
	class ViewersController{
		public ViewersController() {
			viewersMap = new HashMap<String,AbstractViewer>();
			viewersMap.put("2D", new Viewer(controller));
			viewersMap.put("3D", new Viewer3D(controller));
			viewersMap.put("None", new EmptyViewer(controller));
			
			changeView("2D");
		}
		
		public void changeView(String key) {
			for(String k:viewersMap.keySet()) {
				if(k.equals(key)) {
					viewer = viewersMap.get(key);
					viewer.activate();
					jScrollPane1.setViewportView(viewer);
				}
				else {
					viewersMap.get(k).deactivate();
				}
			}jScrollPane1.repaint();
		}
	}
	
    /** Creates new form LauncherGUI */
    public LauncherGUI() {
    	/*simStop = false;
    	simulator = new EvoSimulator();
    	controller = new Controller(simulator,null);
        initComponents();
        configureComponents();*/
    }

    private void configureComponents() {
    	//jTabbedPane1.addTab("Code", entityViewer.getCodeComponent());
    	//jTabbedPane1.addTab("FitnessScores", entityViewer.geFitnessScoresComponent());
    	
    	//viewersMap.get(jrbTemperatureView.getActionCommand()).addEntityObserver(entityViewer);
    	
    	
	}

	/** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPlay = new javax.swing.JToggleButton();
        jVisuSelection = new javax.swing.JComboBox<>();
        jSimulationTime = new TimeLabel(controller);
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPlay.setText(".");
        try {
        	jPlay.setIcon(new ImageIcon(ImageIO.read(new File("resources/gui/play.png"))));
			jPlay.setSelectedIcon(new ImageIcon(ImageIO.read(new File("resources/gui/pause.png"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        jPlay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jPlayItemStateChanged(evt);
            }
        });

        jVisuSelection.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2D", "3D", "None" }));
        jVisuSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jVisuSelectionActionPerformed(evt);
            }
        });

        jSimulationTime.setText("0");

        jLabel1.setText("Time:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPlay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSimulationTime, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 302, Short.MAX_VALUE)
                .addComponent(jVisuSelection, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPlay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jVisuSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSimulationTime)
                    .addComponent(jLabel1))
                .addContainerGap())
        );


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>                          


    /*private void jcbExperimentsActionPerformed(java.awt.event.ActionEvent evt) {     
    	this.entityViewer.runExperiment(this.jcbExperiments.getSelectedItem().toString());
    }*/
	public void setSimStop(boolean simStop) {
		this.simStop = simStop;
	}
                             
    private void jVisuSelectionActionPerformed(java.awt.event.ActionEvent evt) {                                               
        this.viewsController.changeView((String) this.jVisuSelection.getSelectedItem());
    }                                              

    private void jPlayItemStateChanged(java.awt.event.ItemEvent evt) {                                       
        if(this.jPlay.isSelected()) {
        	simStop = false;
        	runEvent(100000);
        }
        else {
        	simStop = true;
        }
    }      
    public void runEvent(int n) {
    	if ( n>0 && !simStop) {
	         try {
	        	 controller.run(1);
	         } catch (Exception e) {
	        	 e.printStackTrace();
	        	 JOptionPane.showMessageDialog(this, e);
	             return;
	         }
	         SwingUtilities.invokeLater( new Runnable() {
	        	@Override
	     		public void run() {
	        		runEvent(n-1);
	     		}
	         });
	   } 

	}
	/**
     * @param args the command line arguments
     */
    /*public static void main(String args[]) {
  
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LauncherGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LauncherGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LauncherGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LauncherGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LauncherGUI().setVisible(true);
            }
        });
    }*/

    // Variables declaration - do not modify        
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToggleButton jPlay;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jSimulationTime;
    private javax.swing.JComboBox<String> jVisuSelection;
    // End of variables declaration                   

}

