package setup.gui.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import setup.gui.model.SetupEditorModel.EntitySeparator;

public class MainGUI extends MainGUIAbstract{
	public MainGUI() {
		super();

	}
	@Override
	protected void save() {
		JSONArray fsms = this.ctrl.pullFSMs();
		
		Map<Class<?>, EntitySeparator> separators = this.ctrl.pullSeparators();
		JSONArray seps = new JSONArray();
		for(Entry<Class<?>, EntitySeparator>entry:separators.entrySet()) {
			seps.put(new JSONObject().put("class", entry.getKey().getName()).put("att",entry.getValue().getAtt())
																					 .put("values", new JSONArray(entry.getValue().getValues())
										 )
					);
		}
		
		JSONObject setup = new JSONObject().put("fsms", fsms)
										   .put("separators", seps);

	    try {
			PrintWriter out = new PrintWriter(new FileWriter("resources/setup/save.json"));
			out.write(setup.toString(4));
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	@Override
	protected void open() {
		JSONObject o = null;
		try {
			o = new JSONObject(new JSONTokener(new FileInputStream("resources/setup/save.json")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		JSONArray arr = o.getJSONArray("separators");
		/*try {
			for(int i=0;i<arr.length();i++) {
				JSONObject es = arr.getJSONObject(i);
				ctrl.pushSeparator(Class.forName(es.getString("class")), es.getString("att"), es.getJSONArray("values").toList());
			}
		} catch (ClassNotFoundException | JSONException e) {
			e.printStackTrace();
		}*/
		
		arr = o.getJSONArray("fsms");
		for(int i=0;i<arr.length();i++) {
			JSONObject fsm = arr.getJSONObject(i);
			ctrl.pushFSM(fsm);
		}
		this.viewPanel.recalculate();
		this.repaint();
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
            java.util.logging.Logger.getLogger(MainGUIAbstract.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainGUIAbstract.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainGUIAbstract.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainGUIAbstract.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainGUI().setVisible(true);
            }
        });
    }
}
