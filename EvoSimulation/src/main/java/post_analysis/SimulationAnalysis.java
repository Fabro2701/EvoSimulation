package post_analysis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import post_analysis.fitness_tests.SimpleMazeFitnessTest;
import simulator.view.EntityViewer;

public class SimulationAnalysis extends javax.swing.JFrame {
	private DefaultTableModel tableModel;
	private Vector<Vector<Object>> data;
	private JSONArray entities;
	private EntityViewer entityViewer;
    /**
     * Creates new form SimulationAnalysis
     */
    public SimulationAnalysis() {
        initComponents();
        _config();
        _configComponents();
    }

    private void _configComponents() {
    	entityViewer = new EntityViewer(this.jScrollPane2);
    	this.jTabbedPane1.addTab("Code", entityViewer.getCodeComponent());
	}

	private void _config() {
    	setData("resources/loads/simulations/filename.json");

    	Vector<String> header = new Vector<String>();
    	header.add("id");
    	header.add("age");
    	header.add("generation");
    	header.add("fitness");
    	tableModel.setDataVector(data, header);
    	jTable1.setCellSelectionEnabled(true);
    	
		ListSelectionModel cellSelectionModel = jTable1.getSelectionModel();
		cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
		        public void valueChanged(ListSelectionEvent e) {
		        	_selectionChanged(jTable1.getSelectedRow());
	
		
		        }
		});
		
		class Task implements Callable<Integer>{
			JSONObject e;
			public Task(JSONObject e) {
				this.e=e;
			}
			@Override
			public Integer call() throws Exception {
				return new SimpleMazeFitnessTest(10000).evaluate(e);
			}
			
		}
		long startTime=System.currentTimeMillis();
		int cont=0;
		for(cont=0;cont<this.entities.length();cont+=100) {
			List<Task>tasks = new ArrayList<Task>();
	    	for(int i=cont;i<cont+100&&i<this.entities.length();i++) {
	    		tasks.add(new Task(this.entities.getJSONObject(i)));
	    	}
	    	
	    	ExecutorService service = Executors.newCachedThreadPool();
	    	List<Future<Integer>> results;
			try {
				results = service.invokeAll(tasks);
				for(int i=cont;i<cont+100&&i<this.entities.length();i++) {
		    		try {
						this.data.get(i).set(3,results.get(i-cont).get().intValue());
						System.out.println(results.get(i-cont).get().intValue());
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    	}
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			finally {
				service.shutdown();
			}
		}
    	
    	System.out.println(System.currentTimeMillis()-startTime);
	}
    
    private void setData(String fileName) {
    	try {
			JSONObject o = new JSONObject(new JSONTokener(new FileInputStream(fileName)));
			JSONArray arr = o.getJSONArray("entities");
			entities = new JSONArray();
			data = new Vector<Vector<Object>>(arr.length());
			for(int i = 0; i<arr.length(); i++) {
				Vector<Object>v = new Vector<Object>();
				if(arr.getJSONObject(i).getString("type").equals("f"))continue;
				entities.put(arr.getJSONObject(i));
				v.add(arr.getJSONObject(i).getJSONObject("data").getString("id"));
				v.add(arr.getJSONObject(i).getJSONObject("data").getInt("age"));
				v.add(arr.getJSONObject(i).getJSONObject("data").getInt("generation"));

				data.add(v);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    private void _selectionChanged(int selectedRow) {
		// TODO Auto-generated method stub
    	JLabel label = new JLabel();
    	label.setText("<html>" + this.entities.getJSONObject(selectedRow).getJSONObject("data").getJSONObject("phenotype").getString("code").replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>").replaceAll("    ", "&emsp ") + "</html>");
    	//label.setAlignmentX(Component.RIGHT_ALIGNMENT);
    	//label.setAlignmentY(Component.TOP_ALIGNMENT);
    	entityViewer.setEntity(this.entities.getJSONObject(selectedRow));
		//this.visualizationPane.setViewportView(label);
		
    	/*SimpleMazeFitnessTest t = new SimpleMazeFitnessTest(this.visualizationPane);
    	t.evaluate(this.entities.getJSONObject(selectedRow));*/
	}
	/**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
    	tableModel = new DefaultTableModel(0,0);
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable(tableModel);
        visualizationPane = new javax.swing.JScrollPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setBorder(new javax.swing.border.MatteBorder(null));

        jScrollPane1.setViewportView(jTable1);

        visualizationPane.setBorder(new javax.swing.border.MatteBorder(null));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SimpleMazeTest" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 876, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 29, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        jTabbedPane1.addTab("Experiments", jPanel1);

        visualizationPane.setViewportView(jTabbedPane1);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton1.setText("jButton1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jButton1)))
                .addGap(18, 18, 18)
                .addComponent(visualizationPane)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(visualizationPane)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15))))
        );

        pack();
    }// </editor-fold>                           
    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {      
    	this.entityViewer.runExperiment(this.jComboBox2.getSelectedItem().toString());
    	
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
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SimulationAnalysis().setVisible(true);
            }
        });
    }

 // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JScrollPane visualizationPane;
    // End of variables declaration                      
}

