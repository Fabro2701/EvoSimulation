package simulator;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.events.EventManager;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.builders.Builder;
import simulator.factories.builders.entity.FoodEntityBuilder;
import simulator.factories.builders.entity.MyIndividualBuilder;
import simulator.factories.builders.entity.SimpleRandomEntityBuilder;
import simulator.factories.builders.entity.SimpleUPEntityBuilder;
import simulator.factories.builders.events.AddEntitiesEventBuilder;
import simulator.factories.builders.events.AddFoodConditionGeneratorEventBuilder;
import simulator.factories.builders.events.AddFoodDistributionEventBuilder;
import simulator.factories.builders.events.AddFoodGeneratorEventBuilder;
import simulator.factories.builders.events.AddRandomEntitiesConditionGeneratorEventBuilder;
import simulator.factories.builders.events.AddRandomEntitiesGeneratorEventBuilder;
import simulator.factories.builders.events.SaveSimulationEventBuilder;
import simulator.factories.builders.stats.BestIndividualCodeBuilder;
import simulator.factories.builders.stats.ChildDepthBuilder;
import simulator.factories.builders.stats.DeadOffSpringBuilder;
import simulator.factories.builders.stats.EnergyBuilder;
import simulator.factories.builders.stats.GenotypeHeterogeneityBuilder;
import simulator.factories.builders.stats.MutationReproductionBuilder;
import simulator.factories.builders.stats.PopulationAgeBuilder;
import simulator.factories.builders.stats.PopulationCountBuilder;
import simulator.model.EvoSimulator;
import simulator.model.entity.Entity;
import statistics.StatsData;
import statistics.StatsManager;

public class OptimizedLauncherGUI extends javax.swing.JFrame {
	private EvoSimulator simulator;
	private Controller ctrl;
    /**
     * Creates new form OptimizedLauncherGUI
     */
    public OptimizedLauncherGUI() {
        initComponents();
        _config();
    }

    private void _config() {
		BuilderBasedFactory<StatsData> statsFactory = new BuilderBasedFactory<StatsData>("statsFactory");
		StatsManager statsManager = new StatsManager(statsFactory);
    	
		BuilderBasedFactory<Event> eventFactory = new BuilderBasedFactory<Event>("eventsFactory");
		EventManager eventManager = new EventManager();

		BuilderBasedFactory<Entity> entityFactory = new BuilderBasedFactory<Entity>("entitiesFactory");

		simulator = new EvoSimulator();
		simulator.setDebug(true);
		ctrl = new Controller(simulator, entityFactory, eventFactory, eventManager,statsManager);
		
		try {
			int op=0;
			if(op==0) {
				ctrl.loadEvents(new FileInputStream("resources/loads/events/eventstest1.json"));
				//ctrl.loadEntities(new FileInputStream("resources/loads/entities/test1.json"));
			}
			else {
				ctrl.loadEntities(new FileInputStream("resources/loads/simulations/filename.json"));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jbStart = new javax.swing.JButton();
        jbSave = new javax.swing.JButton();
        jtfFileName = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jbStart.setText("Start");
        jbStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbStartActionPerformed(evt);
            }
        });

        jbSave.setText("Save ");
        jbSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSaveActionPerformed(evt);
            }
        });

        jtfFileName.setText("filename");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbStart, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jbSave, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfFileName, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbStart, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbSave, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfFileName, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        

    private void jbStartActionPerformed(java.awt.event.ActionEvent evt) {                                        
        this.jbStart.setEnabled(false);
        new Thread() {
        	public void run() {
        		 ctrl.run(Integer.MAX_VALUE>>1);
        	}
        }.start();
       
    }                                       

    private void jbSaveActionPerformed(java.awt.event.ActionEvent evt) {      
    	this.ctrl.saveSimulation();
//        String filename = this.jtfFileName.getText();
//        JSONObject o = simulator.toJSON();
//        try {
//        	//File myObj = new File("resources/loads/simulations/"+filename+".json");myObj.createNewFile();
//			PrintWriter out = new PrintWriter(new FileWriter("resources/loads/simulations/"+filename+".json"));
//			out.write(o.toString());
//			out.close();
//			out = new PrintWriter(new FileWriter("resources/loads/grammars/"+filename+".bnf"));
//			out.write(simulator.getCommonGrammar().toString());
//			out.close();
//			System.out.println("Done");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    }                                      

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
            java.util.logging.Logger.getLogger(OptimizedLauncherGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OptimizedLauncherGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OptimizedLauncherGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OptimizedLauncherGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
    	
        SwingUtilities.invokeLater(() -> {
        	new OptimizedLauncherGUI().setVisible(true);
        });

    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton jbSave;
    private javax.swing.JButton jbStart;
    private javax.swing.JTextField jtfFileName;
    // End of variables declaration                   
}
