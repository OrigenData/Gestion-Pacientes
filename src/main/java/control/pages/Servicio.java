package control.pages;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.gnome.gdk.Event;
import org.gnome.gtk.Builder;
import org.gnome.gtk.CellRendererText;
import org.gnome.gtk.DataColumnString;
import org.gnome.gtk.Entry;
import org.gnome.gtk.EntryIconPosition;
import org.gnome.gtk.Entry.Changed;
import org.gnome.gtk.Entry.IconPress;
import org.gnome.gtk.ListStore;
import org.gnome.gtk.TreeIter;
import org.gnome.gtk.TreeModel;
import org.gnome.gtk.TreeModelFilter;
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
    TreeModelFilter filter;
    Entry serviciosBuscarEntry;
    Connection DB =null;
	Statement st = null;

	public Servicio(Builder b) {
		
		this.builder = b;
		
		//Entry
		serviciosBuscarEntry = (Entry) builder.getObject("entryServiciosBuscar");
		serviciosBuscarEntry.connect(on_entryServiciosBuscar_icon_press());
		serviciosBuscarEntry.connect(on_entryServiciosBuscar_changed());
		
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
        
        treeviewServicio();
        
        
	    // Filtro de palabras "Nombre"
        filter = new TreeModelFilter(listStore, null);
        filter.setVisibleCallback(new TreeModelFilter.Visible() {
            public boolean onVisible(TreeModelFilter source, TreeModel base, TreeIter row) {
                final String servicio;
                final String search;
                
                //Se ingresa una palabra aunque aunque sea minuscula
                servicio = base.getValue(row, servNombreColumn).toLowerCase();
                search = serviciosBuscarEntry.getText().toLowerCase();

                if (servicio.contains(search)) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        
        
        
        /*Establezca TreeModel que se usa para obtener datos de origen para este TreeView*/
        view.setModel(filter);
        
        
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
        
	}
	
	private Changed on_entryServiciosBuscar_changed() {
		return new Changed() {
			
			@Override
			public void onChanged(Entry arg0) {
				// TODO Auto-generated method stub
				filter.refilter();
			}
		};
	}

	private IconPress on_entryServiciosBuscar_icon_press() {
		return new Entry.IconPress() {
			
			@Override
			public void onIconPress(Entry arg0, EntryIconPosition arg1, Event arg2) {
				serviciosBuscarEntry.setText("");
				
			}
		};
	}

	public void treeviewServicio() {
		
		
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
