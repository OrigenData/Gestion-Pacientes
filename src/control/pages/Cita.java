package control.pages;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.gnome.gtk.Builder;
import org.gnome.gtk.Button;
import org.gnome.gtk.Button.Clicked;
import org.gnome.gtk.Calendar;
import org.gnome.gtk.Calendar.DaySelectedDoubleClick;
import org.gnome.gtk.CellRendererText;
import org.gnome.gtk.ComboBox;
import org.gnome.gtk.DataColumnString;
import org.gnome.gtk.Entry;
import org.gnome.gtk.ListStore;
import org.gnome.gtk.TreeIter;
import org.gnome.gtk.TreeView;
import org.gnome.gtk.TreeViewColumn;

import credential.database.Password;
import credential.database.ServerPG;

public class Cita extends Password implements ServerPG{

	Builder builder;
	Calendar citaCalendar;
	ComboBox servicioCombo;
	String servicioArray[] ={"Comunicación","Conductual", "Cognitiva/Académica" ,"Motriz", "Adaptativa","Social","Autoayuda"};
    ListStore servicioStore;
    DataColumnString serviceID;
    CellRendererText serviceCellR;
    //TreeView
    ListStore listStore;
    TreeView view;
    TreeViewColumn vertical;
    TreeIter row;
    DataColumnString citaIDColumn, paciNombreColumn, servNombreColumn, citaFechaColumn, servHoraInicioColumn, servHoraSalidaColumn, empNombreColumn;
    CellRendererText citaIDText, paciNombreText, servNombreText, citaFechaText, servHoraInicioText, servHoraSalidaText, empNombreText;
    Entry citaFechaEntry, citaPacienteEntry;
    Button buttonCitaActualizar, buttonCitaGuardar, buttonCitaLimpiar;
    Connection DB =null;
	Statement st = null;

	
	public Cita(Builder b) {
		
		this.builder = b;
		
		
		//#################		Calendar		#################	\\
		
		citaCalendar = (Calendar) builder.getObject("calendarID");
		citaCalendar.connect(on_calendarID_day_selected_double_click());
		
		//#################		ComboBoxText		#################	\\
		
		servicioCombo = (ComboBox) builder.getObject("entryListaServicioCitas");
				
		servicioStore = new ListStore(new DataColumnString[]{
			    		
						serviceID = new DataColumnString(),
				});
				
		servicioCombo.setModel(servicioStore);
				
		serviceCellR = new CellRendererText (servicioCombo);
				
		serviceCellR.setText(serviceID);
				
		for (int i = 0; i < servicioArray.length; i++) {
			row = servicioStore.appendRow();
			servicioStore.setValue(row, serviceID, servicioArray[i]);
			
		}
		
		
		//#################		Entry		#################	\\
		
		//citaFechaEntry, citaServicioEntry, citaPacienteEntry;
		
		citaPacienteEntry = (Entry) builder.getObject("entryIDClientCitas");
		citaFechaEntry = (Entry) builder.getObject("entryFechaCitas");
				
		
		//#################		Button		#################	\\
				
				
		buttonCitaActualizar = (Button) builder.getObject("buttonCitaActualizar");
		buttonCitaActualizar.connect(on_buttonCitaActualizar_clicked());
		
		buttonCitaLimpiar = (Button) builder.getObject("buttonCitaLimpiar");
		buttonCitaLimpiar.connect(on_buttonCitaLimpiar_clicked());
		
		buttonCitaGuardar = (Button) builder.getObject("buttonCitaGuardar");
		buttonCitaGuardar.connect(on_buttonCitaGuardar_clicked());
		
		
		//#################		TreeView		#################	\\
		
        view = (TreeView) builder.getObject("treeview_Citas_ID");
 
        /* Construccion del modelo */
        listStore = new ListStore(new DataColumnString[]{
        		
        		citaIDColumn = new DataColumnString(),
        		paciNombreColumn = new DataColumnString(),
        		servNombreColumn = new DataColumnString(),
        		citaFechaColumn = new DataColumnString(),
        		servHoraInicioColumn = new DataColumnString(),
        		servHoraSalidaColumn = new DataColumnString(),
        		empNombreColumn = new DataColumnString(),
        });
 
        /*Establezca TreeModel que se usa para obtener datos de origen para este TreeView*/
        view.setModel(listStore);
        
        
        /*Crear instancias de TreeViewColumn*/
        vertical = view.appendColumn();
        vertical.setTitle("ID");
        citaIDText  = new CellRendererText(vertical);
        citaIDText.setText(citaIDColumn);
        
        
        vertical = view.appendColumn();
        vertical.setTitle("Nombre Paciente");
        vertical.setExpand(true);
        paciNombreText = new CellRendererText(vertical);
        paciNombreText.setText(paciNombreColumn);
        
        
        
        vertical = view.appendColumn();
        vertical.setTitle("Servicio");
        vertical.setExpand(true);
        servNombreText = new CellRendererText(vertical);
        servNombreText.setText(servNombreColumn);
        
        
        vertical = view.appendColumn();
        vertical.setTitle("Fecha");
        vertical.setExpand(true);
        citaFechaText = new CellRendererText(vertical);
        citaFechaText.setText(citaFechaColumn);
        
        
        
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
        vertical.setTitle("Nombre Empleado");
        vertical.setExpand(true);
        empNombreText = new CellRendererText(vertical);
        empNombreText.setText(empNombreColumn);
        
        
        
	}
	
	
	
	private DaySelectedDoubleClick on_calendarID_day_selected_double_click() {
		return new Calendar.DaySelectedDoubleClick() {
			
			@Override
			public void onDaySelectedDoubleClick(Calendar arg0) {
				
				String Data = String.format("%d-%d-%d", citaCalendar.getDateYear(),citaCalendar.getDateMonth(),citaCalendar.getDateDay());
				citaFechaEntry.setText(Data);
				
			}
		};
	}



	//---------------------- Accion de Botones ----------------------//
	
	
	
	private Clicked on_buttonCitaLimpiar_clicked() {
		return new Button.Clicked() {
			
			@Override
			public void onClicked(Button arg0) {
				
				citaFechaEntry.setText("");
				citaPacienteEntry.setText("");
				servicioCombo.getCanDefault();
			}
		};
	}



	private Clicked on_buttonCitaGuardar_clicked() {
		return new Button.Clicked() {
			
			@Override
			public void onClicked(Button arg0) {
				
				int SelecServiceCombo = 0;
				
				try {
					
					if(servicioCombo.getActive()!=-1) {
						
						//System.out.println("Servicio : "+servicioArray[servicioCombo.getActive()]);
						SelecServiceCombo = servicioCombo.getActive()+1;
					}
					
				}catch(ArrayIndexOutOfBoundsException e) {
					
					System.out.println(e.getMessage());
				}
				
				
				//##################	Paciente INTO		##################//
				
                try {
			        //Conexion con la base de datos
			        DB = DriverManager.getConnection(URL, DBUSER, getPasswd());
			 
			        // Se hara una consulta
			        st = DB.createStatement();
			        
			        String query = String.format("INSERT INTO \"%s\" ( \"%s\" , \"%s\" , \"%s\" )"
			        		+ "VALUES ( '%s' , '%s' , '%s' );" 
			        		+ "", "Cita","citaFecha","citaServicio","citaPaciente",
			        		citaFechaEntry.getText(), SelecServiceCombo, citaPacienteEntry.getText());
			        
			        
			        st.executeUpdate(query);
			        
			        st.close();
			        DB.close();
			    } catch (SQLException e) {
			        System.err.println("Error: " +e.getMessage() );
			 
			    }
				
			
				
				
				
			}
		};
	}



	private Clicked on_buttonCitaActualizar_clicked() {
		return new Button.Clicked() {
			
			@Override
			public void onClicked(Button arg0) {
				
				
				treeviewCita();
				
			}
		};
	}
	
	
	
	
	
	//---------------------- Metodos ----------------------//
	
	
	public void treeviewCita() {
		
        listStore.clear(); //Limpiar TreeView
		
	    try {
	        //Conexion con la base de datos
	        DB = DriverManager.getConnection(URL, DBUSER, getPasswd());
	 
	        // Se hara una consulta  de la tabla.
	        st = DB.createStatement();
	        
	        String query = String.format(""
	        		+ "SELECT \"%s\" , \"%s\" , \"%s\" , \"%s\" , \"%s\" , \"%s\" , \"%s\" "
	        		+ "FROM \"%s\" , \"%s\" , \"%s\" , \"%s\" "
	        		+ "WHERE \"%s\" = \"%s\" "
	        		+ "AND \"%s\" = \"%s\" AND \"%s\" = \"%s\" "
	        		+ "ORDER BY \"%s\" ASC;"
	        		, "citaID", "paciNombre" , "servNombre" , "citaFecha" , "servHoraInicio" , "servHoraSalida" , "empNombre"
	        		, "Cita" , "Paciente" , "Servicio"  , "Empleado"
	        		, "citaServicio", "servID"
	        		, "servEmpleado" , "empID" , "citaPaciente" , "paciID"
	        		, "citaID");
	        
	        
	        ResultSet rs = st.executeQuery(query);
	        
	        
	        while    ( rs.next() ) {
	        	
	        	row = listStore.appendRow();
	        	listStore.setValue(row, citaIDColumn		, String.valueOf(rs.getString("citaID")));
	        	listStore.setValue(row, paciNombreColumn	, String.valueOf(rs.getString("paciNombre")));
	        	listStore.setValue(row, servNombreColumn	, String.valueOf(rs.getString("servNombre")));
	        	listStore.setValue(row, citaFechaColumn		, String.valueOf(rs.getString("citaFecha")));
                listStore.setValue(row, servHoraInicioColumn, String.valueOf(rs.getString("servHoraInicio")));
                listStore.setValue(row, servHoraSalidaColumn, String.valueOf(rs.getString("servHoraSalida")));
                listStore.setValue(row, empNombreColumn		, String.valueOf(rs.getString("empNombre")));
	        }
	 
	        rs.close();
	        st.close();
	        DB.close();
	        
	    } catch (SQLException e) {
	        System.err.println( e.getMessage() );
	        
	    }
		
	}

}
