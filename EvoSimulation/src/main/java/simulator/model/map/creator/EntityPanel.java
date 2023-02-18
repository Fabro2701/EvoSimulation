package simulator.model.map.creator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.model.map.creator.MapCreator.PanelManager;

/**
 * AttributesPanel creates the entities of the map 
 * it wasnt created in the initial {@link MapCreator} iteration so u may see some casts
 * 
 * @author fabrizioortega
 */
public class EntityPanel extends AbstractCreatorPanel {

	
	public EntityPanel(PanelManager panelManager) {
		super(panelManager);
		ctrl = new EntityPanel.Controller(1000, 1000, new Color(0, 0, 0));
		modificationPanel.setViewportView(ctrl.img);
		
	}
	
	protected class Controller extends AbstractCreatorPanel.Controller {
		// selection is our current PasiveEntity
		protected Attribute selection;
		// models has all the Attributes
		protected HashMap<String, Attribute> models;
		private Map<String, List<EntityInfo>>entities;

		public Controller(int width, int height, Color initColor) {
			super(width, height, initColor);

			models = new HashMap<String, Attribute>();
			models.put("house", new Attribute("resources/entities/house.png","house", width, height));
			models.put("supermarket", new Attribute("resources/entities/supermarket.png","supermarket", width, height));
			models.put("bar", new Attribute("resources/entities/bar.png","bar", width, height));
			models.put("restaurant", new Attribute("resources/entities/restaurant.png","restaurant", width, height));
			selection = models.get("house");
			img = new ModificationImage(new ImageIcon(bufferImage));
			
			entities = new HashMap<>();
		}
		/**
		 * Get a map with all the models entities
		 * @return
		 */
		public Map<String, List<EntityInfo>>getEntities(){
			this.entities.clear();
			for(String k:this.models.keySet()) {
				this.entities.put(k, this.models.get(k).entities);
			}
			return this.entities;
		}
		/**
		 * Loads and updates the entities
		 * @param arr
		 */
		public void loadEntities(JSONArray arr) {
			this.resetImg();
			this.entities.clear();
			for(int i=0;i<arr.length();i++) {
				JSONObject o = arr.getJSONObject(i);
				((EntityPanel.Controller.Attribute)this.models.get(o.getString("type"))).loadEntity(o);
			}
		}
		/**
		 * Attribute represents one {@link PasiveEntity}
		 * 
		 * @author fabrizioortega
		 *
		 */
		protected class Attribute {
			private Image icon;
			private List<EntityInfo>entities;
			private String name;

			public Attribute(String filename, String name, int width, int height) {
				icon = new ImageIcon(filename).getImage();
				this.name = name;
				entities = new ArrayList<>();
			}

			/**
			 * Paints on bufferGraphics and add a new {@link EntityInfo}
			 * 
			 * @param p1
			 * @param bufferGraphics
			 */
			private void paint(Point p1, Graphics2D bufferGraphics) {
				entities.add(new EntityInfo(name,p1.x,p1.y));
				bufferGraphics.drawImage(icon, p1.x-icon.getWidth(null)/2, 
											   p1.y-icon.getHeight(null)/2, null);

				EntityPanel.this.repaint();
			}
			protected void loadEntity(JSONObject o) {
				this.entities.add(new EntityInfo(o));
				for(EntityInfo info:this.entities) {
					bufferGraphics.drawImage(icon, info.x, info.y, null);
				}
				EntityPanel.this.repaint();
			}
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			selection.paint(e.getPoint(), bufferGraphics);
		}
		@Override
		public void mouseDragged(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		public void setSelection(String stringKey) {
			selection = models.get(stringKey);
		}

	}
	public void loadEntities(InputStream in) {
		JSONArray arr = new JSONArray(new JSONTokener(in));
		((EntityPanel.Controller)this.ctrl).loadEntities(arr);
	}
	public static class EntityInfo{
		String name;
		int x, y;
		public EntityInfo(String name, int x, int y){
			this.name = name;
			this.x = x;
			this.y = y;
		}
		public EntityInfo(JSONObject o) {
			this.name = o.getString("type");
			this.x = o.getInt("x");
			this.y = o.getInt("y");
		}
		public int getX() {
			return x;
		}
		public int getY() {
			return y;
		}
		public String getName() {
			return name;
		}
	}
	@Override
	public void initComponents() {

		modificationPanel = new javax.swing.JScrollPane();
		jPanel1 = new javax.swing.JPanel();
		jComboBox1 = new javax.swing.JComboBox<>();
		jLabel1 = new javax.swing.JLabel();

		jScrollPane2 = new javax.swing.JScrollPane();
		jPanel2 = new javax.swing.JPanel();

		setMaximumSize(new java.awt.Dimension(1373, 682));
		setPreferredSize(new java.awt.Dimension(1373, 682));
		setSize(new java.awt.Dimension(1373, 682));

		modificationPanel.setBorder(new javax.swing.border.MatteBorder(null));
		modificationPanel.setMaximumSize(new java.awt.Dimension(600, 600));
		modificationPanel.setMinimumSize(new java.awt.Dimension(600, 600));
		modificationPanel.setPreferredSize(new java.awt.Dimension(600, 600));

		jPanel1.setBorder(new javax.swing.border.MatteBorder(null));

		jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "house", "supermarket","bar","restaurant" }));
		jComboBox1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jComboBox1ActionPerformed(evt);
			}
		});

		jLabel1.setText("Type");




		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel1)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
						.addContainerGap(19, Short.MAX_VALUE)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
								.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34,
												javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(15, 15, 15))));

		jScrollPane2.setBorder(new javax.swing.border.MatteBorder(null));

		jPanel2.setBorder(new javax.swing.border.MatteBorder(null));

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 0, Short.MAX_VALUE));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 0, Short.MAX_VALUE));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
						.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(modificationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE).addComponent(
								jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 766,
								javax.swing.GroupLayout.PREFERRED_SIZE))
						.addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
						.addComponent(modificationPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(jScrollPane2))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))));
	}// </editor-fold>

	private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {
		((Controller) ctrl).setSelection((String) jComboBox1.getSelectedItem());
	}

	

	// Variables declaration - do not modify
	private javax.swing.JComboBox<String> jComboBox1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	// private javax.swing.JScrollPane modificationPanel;
	private javax.swing.JScrollPane jScrollPane2;
	// End of variables declaration
	
}
