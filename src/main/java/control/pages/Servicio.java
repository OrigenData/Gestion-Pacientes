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

public class Servicio extends Password implements ServerPG{
	
	Password dbpasswd;
	Builder builder;
    TreeView view;
    TreeViewColumn vertical;
    TreeIter row;
    ListStore listStore;
    DataColumnString servIDColumn, servNombreColumn, servHoraInicioColumn, servHoraSalidaColumn, empNombreColumn;  
    CellRendererText servIDText, servNombreText, servHoraInicioText, servHoraSalidaText, empNombreText;
    Connection DB =null;
	Statement st = null;

	public Servicio(Builder b) {
		
		this.builder = b;
	}
	
	public void treeviewServicio() {
		
		//TreeView
        view = (TreeView) builder.getObject("treeview_Servicios_ID");
 
        /* Construccion del modelo */
        listStore = new ListStore(new DataColumnString[]{
        		
                servIDColumn = new DataColumnString(),
               	servNombreColumn = new DataColumnString(),
               	servHoraInicioColumn = new DataColumnString(),
               	servHoraSalidaColumn = new DataColumnString(),
                empNombreColumn = new DataColumnString(),
        });
 
        /*Establezca TreeModel que se usa para obtener datos de origen para este TreeView*/
        view.setModel(listStore);
        
        
        /*Crear instancias de TreeViewColumn*/
        vertical = view.appendColumn();
        vertical.setTitle("ID");
        servIDText  = new CellRendererText(vertical);
        servIDText.setText(servIDColumn);
        
        
        vertical = view.appendColumn();
        vertical.setTitle("Nombre Del Servicio");
        vertical.setExpand(true);
        servNombreText = new CellRendererText(vertical);
        servNombreText.setText(servNombreColumn);
        
        
        vertical = view.appendColumn();
        vertical.setTitle("Hora de Inicio");
        vertical.setExpand(true);
        servHoraInicioText = new CellRendererText(vertical);
        servHoraInicioText.setText(servHoraInicioColumn);
        
        
        vertical = view.appendColumn();
        vertical.setTitle("Hora de Salida");
        vertical.setExpand(true);
        servHoraSalidaText = new CellRendererText(vertical);
        servHoraSalidaText.setText(servHoraSalidaColumn);
        
        vertical = view.appendColumn();
        vertical.setTitle("Nombre De Empleado");
        vertical.setExpand(true);
        empNombreText = new CellRendererText(vertical);
        empNombreText.setText(empNombreColumn);
 
        
        
        listStore.clear(); //Limpiar TreeView
		
	    try {
	        //Conexion con la base de datos
	        DB = DriverManager.getConnection(URL, DBUSER, getPasswd());
	 
	        // Se hara una consulta  de la tabla.
	        st = DB.createStatement();
	        
	        String query = String.format("SELECT \"%s\" , \"%s\" , \"%s\" , \"%s\" , \"%s\" FROM \"%s\" , \"%s\" WHERE \"%s\"=\"%s\" ;",
	        		"servID","servNombre","servHoraInicio","servHoraSalida","empNombre","Servicio","Empleado","servEmpleado","empID");
	        
	        
	        ResultSet rs = st.executeQuery(query);
	        
	        
	        while    ( rs.next() ) {

	        	row = listStore.appendRow();
	        	listStore.setValue(row, servIDColumn, rs.getString("servID"));
                listStore.setValue(row, servNombreColumn, rs.getString("servNombre"));
                listStore.setValue(row, servHoraInicioColumn, rs.getString("servHoraInicio"));
                listStore.setValue(row, servHoraSalidaColumn, rs.getString("servHoraSalida"));
                listStore.setValue(row, empNombreColumn, rs.getString("empNombre"));
	        }
	 
	        rs.close();
	        st.close();
	        DB.close();
	        
	    } catch (SQLException e) {
	        System.err.println( e.getMessage() );
	        
	    }
		
		
		
	}

}
