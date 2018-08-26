package control.pages;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.gnome.gtk.Builder;
import org.gnome.gtk.Button;
import org.gnome.gtk.Button.Clicked;
import org.gnome.gtk.CellRendererText;
import org.gnome.gtk.DataColumnString;
import org.gnome.gtk.Entry;
import org.gnome.gtk.ListStore;
import org.gnome.gtk.TreeIter;
import org.gnome.gtk.TreePath;
import org.gnome.gtk.TreeView;
import org.gnome.gtk.TreeView.RowActivated;
import org.gnome.gtk.TreeViewColumn;

import credential.database.Password;
import credential.database.ServerPG;

public class Registro extends Password implements ServerPG{
	
	Builder builder;
    TreeView view;
    TreeViewColumn vertical;
    TreeIter row;
    ListStore listStorePaciente,listStoreFamiliar ;
    //Paciente
    Button buttonPacienteActualizar;
    Entry paciNombreEntry, paciEdadEntry, paciEmailEntry, paciTelefonoEntry;
    DataColumnString paciIDColumn, paciNombreColumn, paciEdadColumn, paciEmailColumn, paciTelefonoColumn;  
    CellRendererText paciIDText, paciNombreText, paciEdadText, paciEmailText, paciTelefonoText;
    //Familiar
    Button buttonFamiliarActualizar;
    Entry famNombreEntry, famEdadEntry, famEmailEntry, famTelefonoEntry;
    DataColumnString famIDColumn, famNombreColumn, famEdadColumn, famEmailColumn, famTelefonoColumn, famPacienteColumn;  
    CellRendererText famIDText, famNombreText, famEdadText, famEmailText, famTelefonoText, famPacienteText;
    //Botones Generales Registro
    Button buttonRegGuardar, buttonRegLimpiar,buttonRegEliminar, buttonRegEditar;
    //Se lamacenara el ID de la tabla Paciente y Familiar
    String idPaciente, idFamiliar;
    
    Connection DB =null;
	Statement st = null;
	ResultSet rs = null;
	PreparedStatement pst = null;
	
	public Registro(Builder b) {
		
		this.builder = b;
		
		
		///////////////////////////		Registro	///////////////////////////

		
		//******************** Buttons	********************//
		
		buttonRegGuardar = (Button) builder.getObject("buttonRegGuardar");
		buttonRegGuardar.connect(on_buttonRegGuardar_clicked());
		
		buttonRegLimpiar = (Button) builder.getObject("buttonRegLimpiar");
		buttonRegLimpiar.connect(on_buttonRegLimpiar_clicked());
		
		buttonRegEliminar = (Button) builder.getObject("buttonRegEliminar");
		buttonRegEliminar.connect(on_buttonRegEliminar_clicked());
		
		buttonRegEditar = (Button) builder.getObject("buttonRegEditar");
		buttonRegEditar.connect(on_buttonRegEditar_clicked());
		
		
		///////////////////////////		Paciente	///////////////////////////
		
		//******************** Entry	********************//
				
		paciNombreEntry = (Entry) builder.getObject("entryPaciNombre");
		paciEdadEntry = (Entry) builder.getObject("entryPaciEdad");
		paciEmailEntry = (Entry) builder.getObject("entryPaciEmail");
		paciTelefonoEntry = (Entry) builder.getObject("entryPaciTelefono");
		
		//******************** Buttons	********************//
		
		buttonPacienteActualizar = (Button) builder.getObject("buttonPacienteActualizar");
		buttonPacienteActualizar.connect(on_buttonPacienteActualizar_activate());
		
		
		//******************** TreeView	********************//
		
		view = (TreeView) builder.getObject("treeview_Pacientes_ID");
 
        /* Construccion del modelo */
        listStorePaciente = new ListStore(new DataColumnString[]{
        		
        		paciIDColumn		= new DataColumnString(),
        		paciNombreColumn	= new DataColumnString(),
        		paciEdadColumn		= new DataColumnString(),
        		paciEmailColumn		= new DataColumnString(),
        		paciTelefonoColumn	= new DataColumnString(),
        		
        });
        
        //on_treeview_Citas_ID_row_activated
        view.connect(on_treeview_Pacientes_ID_row_activated());
        
        /*Establezca TreeModel que se usa para obtener datos de origen para este TreeView*/
        view.setModel(listStorePaciente);
        
        
        /*Crear instancias de TreeViewColumn*/
        vertical = view.appendColumn();
        vertical.setTitle("ID");
        paciIDText  = new CellRendererText(vertical);
        paciIDText.setText(paciIDColumn);
        
        
        vertical = view.appendColumn();
        vertical.setTitle("Nombre");
        vertical.setExpand(true);
        paciNombreText = new CellRendererText(vertical);
        paciNombreText.setText(paciNombreColumn);
        
        
        vertical = view.appendColumn();
        vertical.setTitle("Edad");
        paciEdadText = new CellRendererText(vertical);
        paciEdadText.setText(paciEdadColumn);
        
        
        vertical = view.appendColumn();
        vertical.setTitle("Email");
        vertical.setExpand(true);
        paciEmailText = new CellRendererText(vertical);
        paciEmailText.setText(paciEmailColumn);
        
        vertical = view.appendColumn();
        vertical.setTitle("Telefono");
        vertical.setExpand(true);
        paciTelefonoText = new CellRendererText(vertical);
        paciTelefonoText.setText(paciTelefonoColumn);
        
        
        ///////////////////////////		Familiar	///////////////////////////
        
        
        //******************** Entry	********************//
        
        //famNombreEntry, famEdadEntry, famEmaiEntry, famTelefono;
        
        famNombreEntry = (Entry) builder.getObject("entryFamNombre");
        famEdadEntry = (Entry) builder.getObject("entryFamEdad");
        famEmailEntry = (Entry) builder.getObject("entryFamEmail");
        famTelefonoEntry = (Entry) builder.getObject("entryFamTelefono");
        
        //******************** Buttons	********************//
        
        buttonFamiliarActualizar = (Button) builder.getObject("buttonFamiliarActualizar");
        buttonFamiliarActualizar.connect(on_buttonFamiliarActualizar_clicked());
        
		
        //******************** TreeView	********************//
        
        view = (TreeView) builder.getObject("treeview_Familiares_ID");
 
        /* Construccion del modelo */
        listStoreFamiliar = new ListStore(new DataColumnString[]{
        		
        		famIDColumn = new DataColumnString(),
        		famNombreColumn = new DataColumnString(),
        		famEdadColumn = new DataColumnString(),
        		famEmailColumn = new DataColumnString(),
        		famTelefonoColumn = new DataColumnString(),
        		famPacienteColumn = new DataColumnString(),
        });
 
        /*Establezca TreeModel que se usa para obtener datos de origen para este TreeView*/
        view.setModel(listStoreFamiliar);
        
        
        /*Crear instancias de TreeViewColumn*/
        vertical = view.appendColumn();
        vertical.setTitle("ID");
        famIDText  = new CellRendererText(vertical);
        famIDText.setText(famIDColumn);
        
        
        vertical = view.appendColumn();
        vertical.setTitle("Nombre");
        vertical.setExpand(true);
        famNombreText = new CellRendererText(vertical);
        famNombreText.setText(famNombreColumn);
        
        
        vertical = view.appendColumn();
        vertical.setTitle("Edad");
        famEdadText = new CellRendererText(vertical);
        famEdadText.setText(famEdadColumn);
        
        
        vertical = view.appendColumn();
        vertical.setTitle("Email");
        vertical.setExpand(true);
        famEmailText = new CellRendererText(vertical);
        famEmailText.setText(famEmailColumn);
        
        vertical = view.appendColumn();
        vertical.setTitle("Telefono");
        vertical.setExpand(true);
        famTelefonoText = new CellRendererText(vertical);
        famTelefonoText.setText(famTelefonoColumn);
        
        vertical = view.appendColumn();
        vertical.setTitle("Paciente");
        vertical.setExpand(true);
        famPacienteText = new CellRendererText(vertical);
        famPacienteText.setText(famPacienteColumn);
        
        
        //on_treeview_Citas_ID_row_activated
        view.connect(on_treeview_Pacientes_ID_row_activated());
        
        
       
	}
	



	///////////////////////// Acciones del botones /////////////////////////
	
	
	
	private Clicked on_buttonRegEditar_clicked() {
		return new Button.Clicked() {
			
			@Override
			public void onClicked(Button arg0) {
				
				 try {
				        //Conexion con la base de datos
				        DB = DriverManager.getConnection(URL, DBUSER, getPasswd());
				        String query;
				        
				        query = "UPDATE \"Familiar\""
				        		+ "SET \"famNombre\" = ? , \"famEdad\" = ? , \"famEmail\"= ? , \"famTelefono\"= ?"
				        		+ "WHERE \"famID\"= ?;"
				        		+ "UPDATE \"Paciente\""
				        		+ "SET \"paciNombre\" = ? , \"paciEdad\" = ? , \"paciEmail\" = ? , \"paciTelefono\" = ?"
				        		+ "WHERE \"paciID\"= ?";

				        pst= DB.prepareStatement(query);
				        
				        pst.setString(1, famNombreEntry.getText());
				        pst.setString(2, famEdadEntry.getText());
				        pst.setString(3, famEmailEntry.getText());
				        pst.setString(4, famTelefonoEntry.getText());
				        pst.setInt(5, Integer.valueOf(idFamiliar));
				        
				        pst.setString(6, paciNombreEntry.getText());
				        pst.setString(7, paciEdadEntry.getText());
				        pst.setString(8, paciEmailEntry.getText());
				        pst.setString(9, paciTelefonoEntry.getText());
				        pst.setInt(10, Integer.valueOf(idPaciente));
				        
				        pst.executeUpdate();
				        
				        pst.close();
				        DB.close();
				        
				    } catch (SQLException e) {
				        System.err.println("Error: " +e.getMessage() );
				 
				    }
				
			}
		};
	}




	private Clicked on_buttonRegEliminar_clicked() {
		return new Button.Clicked() {
			
			@Override
			public void onClicked(Button arg0) {
				
				
				 try {
				        //Conexion con la base de datos
				        DB = DriverManager.getConnection(URL, DBUSER, getPasswd());

				        String query=null;
				        
				        query =	 "DELETE FROM \"Familiar\" WHERE  \"famID\"   = ? ;"
				        		+"DELETE FROM \"Cita\" WHERE \"citaPaciente\" = ? ;"
				        		+"DELETE FROM \"Paciente\" WHERE  \"paciID\"  = ? ";

				        pst= DB.prepareStatement(query);
				        
				        pst.setInt(1, Integer.valueOf(idFamiliar));
				        pst.setInt(2, Integer.valueOf(idPaciente));
				        pst.setInt(3, Integer.valueOf(idPaciente));
				        
				        pst.executeUpdate();
				        
				        pst.close();
				        DB.close();
				 

				    } catch (SQLException e) {
				        System.err.println("Error: " +e.getMessage() );
				 
				    }
				
				
			}
		};
	}


	private RowActivated on_treeview_Pacientes_ID_row_activated() {
		return new TreeView.RowActivated() {
			
			@Override
			public void onRowActivated(TreeView arg0, TreePath arg1, TreeViewColumn arg2) {
				// TODO Auto-generated method stub
				
				
				final TreeIter rowPaciente, rowFamiliar;
				
				rowPaciente = listStorePaciente.getIter(arg1);

		          
				 String paciID = listStorePaciente.getValue(rowPaciente, paciIDColumn);
		         String paciNombre = listStorePaciente.getValue(rowPaciente, paciNombreColumn);
		         String paciEdad = listStorePaciente.getValue(rowPaciente, paciEdadColumn);
		         String paciEmail = listStorePaciente.getValue(rowPaciente, paciEmailColumn);
		         String paciTelefono = listStorePaciente.getValue(rowPaciente, paciTelefonoColumn);
		         
		         idPaciente = paciID;
		         paciNombreEntry.setText(paciNombre);
		         paciEdadEntry.setText(paciEdad);
		         paciEmailEntry.setText(paciEmail);
		         paciTelefonoEntry.setText(paciTelefono);
		         
		         
		         
		         
		         rowFamiliar = listStoreFamiliar.getIter(arg1);
		         
		         
		         String famID = listStoreFamiliar.getValue(rowFamiliar, famIDColumn);
		         String famNombre = listStoreFamiliar.getValue(rowFamiliar, famNombreColumn);
		         String famEdad = listStoreFamiliar.getValue(rowFamiliar, famEdadColumn);
		         String famEmail = listStoreFamiliar.getValue(rowFamiliar, famEmailColumn);
		         String famTelefono = listStoreFamiliar.getValue(rowFamiliar, famTelefonoColumn);
		         
		         idFamiliar = famID;
		         famNombreEntry.setText(famNombre);
		         famEdadEntry.setText(famEdad);
		         famEmailEntry.setText(famEmail);
		         famTelefonoEntry.setText(famTelefono);
	
				
			}
		};
	}
	
	
	
	private Clicked on_buttonRegLimpiar_clicked() {
		return new Button.Clicked() {
			
			@Override
			public void onClicked(Button arg0) {
				
				famNombreEntry.setText("");
				famEdadEntry.setText("");
				famEmailEntry.setText("");
				famTelefonoEntry.setText("");
				paciNombreEntry.setText("");
				paciEdadEntry.setText("");
				paciEmailEntry.setText("");
				paciTelefonoEntry.setText("");
				
			}
		};
	}



	private Clicked on_buttonFamiliarActualizar_clicked() {
		return new Button.Clicked() {
			
			@Override
			public void onClicked(Button arg0) {
				
				treeviewFamiliar();
				
			}
		};
	}


	
	private Clicked on_buttonPacienteActualizar_activate() {
		return new Button.Clicked() {
			
			@Override
			public void onClicked(Button arg0) {
				
				treeviewPaciente();
				
			}
		};
	}
	
	
	
	private Clicked on_buttonRegGuardar_clicked() {
		return new Button.Clicked() {
			
			@Override
			public void onClicked(Button arg0) {
				
				//##################	Paciente INTO		##################//
				
                try {
                	
			        //Conexion con la base de datos
			        DB = DriverManager.getConnection(URL, DBUSER, getPasswd());

			        String query=null;
			        
			        query =	 "INSERT INTO \"Paciente\" ( \"paciNombre\" , \"paciEdad\" , \"paciEmail\" , \"paciTelefono\" ) "
			        		+ "VALUES ( ? , ? , ? , ? )";

			        pst= DB.prepareStatement(query);
			        
			        pst.setString(1, paciNombreEntry.getText());
			        pst.setString(2, paciEdadEntry.getText());
			        pst.setString(3, paciEmailEntry.getText());
			        pst.setString(4, paciTelefonoEntry.getText());
			        
			        pst.executeUpdate();
			        
			        pst.close();
			        DB.close();
			        
                } catch (SQLException e) {
			        System.err.println("Error: " +e.getMessage() );
			        
			    }
                
                
                
                //##################	Familiar INTO		##################//
                
                try {
                	
			        //Conexion con la base de datos
			        DB = DriverManager.getConnection(URL, DBUSER, getPasswd());
			        
			        st = DB.createStatement();
			        
			        //Consigo el ultimo ID del paciente ingresado
			        rs = st.executeQuery("SELECT \"paciID\" FROM \"Paciente\" ORDER BY \"paciID\" DESC LIMIT 1;");
			        int pacID = 0;
			        while(rs.next()) {
			        	pacID = rs.getInt("paciID");
			        }
			        
			        
			        String query=null;
			        
			        query =	 "INSERT INTO \"Familiar\" ( \"famNombre\" , \"famEdad\" , \"famEmail\" , \"famTelefono\" , \"famPaciente\" )"
			        		+ "VALUES ( ? , ? , ? , ? , ? );";;

			        pst= DB.prepareStatement(query);
			        
			        pst.setString(1, famNombreEntry.getText());
			        pst.setString(2, famEdadEntry.getText());
			        pst.setString(3, famEmailEntry.getText());
			        pst.setString(4, famTelefonoEntry.getText());
			        pst.setInt(5, pacID);
			        
			        pst.executeUpdate();
			        
			        rs.close();
			        st.close();
			        pst.close();
			        DB.close();
			        
			    } catch (SQLException e) {
			        System.err.println("Error: " +e.getMessage() );
			 
			    }
                
                
                
				
			}
		};
	}
	
	
	

	
	
	///////////////////////// Acciones /////////////////////////
	
	public void treeviewPaciente() {
		
		listStorePaciente.clear(); //Limpiar TreeView
		
	    try {
	        //Conexion con la base de datos
	        DB = DriverManager.getConnection(URL, DBUSER, getPasswd());
	        // Se hara una consulta  de la tabla.
	        st = DB.createStatement();
	        
	        String query = "SELECT * FROM \"Paciente\" ;";
	        
	        
	        ResultSet rs = st.executeQuery(query);
	        
	        
	        while    ( rs.next() ) {
	        	
	        	row = listStorePaciente.appendRow();
	        	listStorePaciente.setValue(row, paciIDColumn, rs.getString("paciID"));
	        	listStorePaciente.setValue(row, paciNombreColumn, rs.getString("paciNombre"));
	        	listStorePaciente.setValue(row, paciEdadColumn, rs.getString("paciEdad"));
	        	listStorePaciente.setValue(row, paciEmailColumn, rs.getString("paciEmail"));
	        	listStorePaciente.setValue(row, paciTelefonoColumn, rs.getString("paciTelefono"));
	        }
	 
	        rs.close();
	        st.close();
	        DB.close();
	        
	    } catch (SQLException e) {
	        System.err.println( e.getMessage() );
	        
	    }
		
	}
	
	
	
	public void treeviewFamiliar() {
		
		listStoreFamiliar.clear(); //Limpiar TreeView
		
	    try {
	        //Conexion con la base de datos
	        DB = DriverManager.getConnection(URL, DBUSER, getPasswd());
	 
	        // Se hara una consulta  de la tabla.
	        st = DB.createStatement();

	        String query = String.format(""
	        		+ "SELECT \"%s\" , \"%s\" , \"%s\" , \"%s\" , \"%s\" , \"%s\" "
	        		+ "FROM \"%s\" , \"%s\" "
	        		+ "WHERE \"%s\"=\"%s\";"
	        		,"famID" , "famNombre" , "famEdad" , "famEmail" , "famTelefono" , "paciNombre"
	        		,"Familiar" , "Paciente"
	        		,"famPaciente","paciID");
	        
	        
	        ResultSet rs = st.executeQuery(query);
	        
	        
	        while    ( rs.next() ) {
	        	
	        	row = listStoreFamiliar.appendRow();
	        	listStoreFamiliar.setValue(row, famIDColumn, rs.getString("famID"));
	        	listStoreFamiliar.setValue(row, famNombreColumn, rs.getString("famNombre"));
                listStoreFamiliar.setValue(row, famEdadColumn, rs.getString("famEdad"));
                listStoreFamiliar.setValue(row, famEmailColumn, rs.getString("famEmail"));
                listStoreFamiliar.setValue(row, famTelefonoColumn, rs.getString("famTelefono"));
                listStoreFamiliar.setValue(row, famPacienteColumn, rs.getString("paciNombre"));
	        }
	 
	        rs.close();
	        st.close();
	        DB.close();
	        
	    } catch (SQLException e) {
	        System.err.println( e.getMessage() );
	        
	    }
		
	}

	

}
