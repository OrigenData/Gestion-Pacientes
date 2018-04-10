package control.pages;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.gnome.gtk.Builder;
import org.gnome.gtk.CellRendererText;
import org.gnome.gtk.DataColumnString;
import org.gnome.gtk.ListStore;
import org.gnome.gtk.TreeIter;
import org.gnome.gtk.TreeView;
import org.gnome.gtk.TreeViewColumn;

import credential.database.Password;
import credential.database.ServerPG;

public class Empleado extends Password implements ServerPG{
	
	Password dbpasswd;
	public Builder builder;
    TreeView view;
    TreeViewColumn vertical;
    TreeIter row;
    ListStore listStore;
    DataColumnString empIDColumn, empNombreColumn, empEdadColumn, empRFCColumn, empEmailColumn, empTelefonoColumn;
    CellRendererText empIDText, empNombreText, empEdadText, empRFCText, empEmailText, empTelefonoText;
    Connection DB =null;
	Statement st = null;

	public Empleado(Builder b) {
		
		this.builder = b;
	}
	
	public void treeviewEmpleado() {
		
		//TreeView
        view = (TreeView) builder.getObject("treeview_Empleados_ID");
 
        /* Construccion del modelo */
        listStore = new ListStore(new DataColumnString[]{
        		
                empIDColumn = new DataColumnString(),
                empNombreColumn = new DataColumnString(),
                empEdadColumn = new DataColumnString(),
                empRFCColumn = new DataColumnString(),
                empEmailColumn = new DataColumnString(),
                empTelefonoColumn = new DataColumnString(),
        });
 
        /*Establezca TreeModel que se usa para obtener datos de origen para este TreeView*/
        view.setModel(listStore);
        
        
        /*Crear instancias de TreeViewColumn*/
        vertical = view.appendColumn();
        vertical.setTitle("ID");
        empIDText  = new CellRendererText(vertical);
        empIDText.setText(empIDColumn);
        
        vertical = view.appendColumn();
        vertical.setTitle("Nombre");
        vertical.setExpand(true);
        empNombreText = new CellRendererText(vertical);
        empNombreText.setText(empNombreColumn);
 
        vertical = view.appendColumn();
        vertical.setTitle("Edad");
        empEdadText = new CellRendererText(vertical);
        empEdadText.setText(empEdadColumn);
        
        
        vertical = view.appendColumn();
        vertical.setTitle("RFC");
        vertical.setExpand(true);
        empRFCText = new CellRendererText(vertical);
        empRFCText.setText(empRFCColumn);
        
        
        vertical = view.appendColumn();
        vertical.setTitle("Email");
        vertical.setExpand(true);
        empEmailText = new CellRendererText(vertical);
        empEmailText.setText(empEmailColumn);
        
        
        vertical = view.appendColumn();
        vertical.setTitle("Telefono");
        vertical.setExpand(true);
        empTelefonoText = new CellRendererText(vertical);
        empTelefonoText.setText(empTelefonoColumn);
        
        
        listStore.clear(); //Limpiar TreeView
		
	    try {
	        //Conexion con la base de datos
	        DB = DriverManager.getConnection(URL, DBUSER, getPasswd());
	 
	        // Se hara una consulta  de la tabla.
	        st = DB.createStatement();
	        
	        
	        ResultSet rs = st.executeQuery( "SELECT * FROM \"Empleado\"; " );
	        
	        
	        while    ( rs.next() ) {
	        	
	        	row = listStore.appendRow();
	        	listStore.setValue(row, empIDColumn, rs.getString("empID"));
                listStore.setValue(row, empNombreColumn, rs.getString("empNombre"));
                listStore.setValue(row, empEdadColumn, rs.getString("empEdad"));
                listStore.setValue(row, empRFCColumn, rs.getString("empRFC"));
                listStore.setValue(row, empEmailColumn, rs.getString("empEmail"));
                listStore.setValue(row, empTelefonoColumn, rs.getString("empTelefono"));
	        }
	 
	        rs.close();
	        st.close();
	        DB.close();
	        
	    } catch (SQLException e) {
	        System.err.println( e.getMessage() );
	
	}
	    
	}

}
