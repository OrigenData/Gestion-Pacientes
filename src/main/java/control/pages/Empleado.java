package control.pages;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.gnome.gtk.Builder;
import org.gnome.gtk.Button;
import org.gnome.gtk.Button.Clicked;
import org.gnome.gtk.CellRendererText;
import org.gnome.gtk.DataColumnString;
import org.gnome.gtk.Entry;
import org.gnome.gtk.Entry.Changed;
import org.gnome.gtk.ListStore;
import org.gnome.gtk.TreeIter;
import org.gnome.gtk.TreeModel;
import org.gnome.gtk.TreeModelFilter;
import org.gnome.gtk.TreeView;
import org.gnome.gtk.TreeViewColumn;

import credential.database.Password;
import credential.database.ServerPG;

public class Empleado extends Password implements ServerPG{
	
	Password dbpasswd;
	Builder builder;
    TreeView view;
    TreeViewColumn vertical;
    TreeIter row;
    ListStore listStore;
    DataColumnString empIDColumn, empNombreColumn, empEdadColumn, empRFCColumn, empEmailColumn, empTelefonoColumn;
    CellRendererText empIDText, empNombreText, empEdadText, empRFCText, empEmailText, empTelefonoText;
    TreeModelFilter filter;
    Entry empleadoBuscarEntry;
    Button buttonCitaBuscar;
    Connection DB =null;
	Statement st = null;

	public Empleado(Builder b) {
		
		this.builder = b;
		
		
		buttonCitaBuscar = (Button) builder.getObject("buttonCitaBuscar");
		buttonCitaBuscar.connect(on_buttonCitaBuscar_clicked());
		
		///Entry
		empleadoBuscarEntry = (Entry) builder.getObject("entryEmpleadoBuscar");
		empleadoBuscarEntry.connect(on_entryEmpleadoBuscar_changed());
		
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
	    
	    
        filter = new TreeModelFilter(listStore, null);
        filter.setVisibleCallback(new TreeModelFilter.Visible() {
            public boolean onVisible(TreeModelFilter source, TreeModel base, TreeIter row) {
                final String empleado;
                final String search;
                
                //Se ingresa una palabra aunque aunque sea minuscula
                empleado = base.getValue(row, empNombreColumn).toLowerCase();
                search = empleadoBuscarEntry.getText().toLowerCase();

                if (empleado.contains(search)) {
                    return true;
                } else {
                    return false;
                }
            }
        });
       
        
	    
	    
	    view.setModel(filter);
     

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
 
	}
	
	
	private Changed on_entryEmpleadoBuscar_changed() {
		return new Entry.Changed() {
			
			@Override
			public void onChanged(Entry arg0) {
				// TODO Auto-generated method stub
				filter.refilter();
				
			}
		};
	}


	private Clicked on_buttonCitaBuscar_clicked() {
		return new Button.Clicked() {
			
			@Override
			public void onClicked(Button arg0) {
				// TODO Auto-generated method stub
				
			}
		};
	}


	public void treeviewEmpleado() {

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
	    
	    
        filter = new TreeModelFilter(listStore, null);
        filter.setVisibleCallback(new TreeModelFilter.Visible() {
            public boolean onVisible(TreeModelFilter source, TreeModel base, TreeIter row) {
                final String contact;
                final String search;
                
                //Se ingresa una palabra aunque aunque sea minuscula
                contact = base.getValue(row, empNombreColumn).toLowerCase();
                search = empleadoBuscarEntry.getText().toLowerCase();

                if (contact.contains(search)) {
                    return true;
                } else {
                    return false;
                }
            }
        });
       
        
	    
	    
	    view.setModel(filter);
	    
	    
	    
	    
	    
	    

        
        
        
        
	    
	
	}

}
