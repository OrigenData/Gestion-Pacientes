package control.pages;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.gnome.gdk.Event;
import org.gnome.gtk.Builder;
import org.gnome.gtk.Button;
import org.gnome.gtk.Button.Clicked;
import org.gnome.gtk.Calendar;
import org.gnome.gtk.Calendar.DaySelectedDoubleClick;
import org.gnome.gtk.CellRendererText;
import org.gnome.gtk.ComboBox;
import org.gnome.gtk.DataColumnString;
import org.gnome.gtk.Entry;
import org.gnome.gtk.EntryIconPosition;
import org.gnome.gtk.Entry.IconPress;
import org.gnome.gtk.ListStore;
import org.gnome.gtk.TreeIter;
import org.gnome.gtk.TreePath;
import org.gnome.gtk.TreeView;
import org.gnome.gtk.TreeView.RowActivated;
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
    Entry citaFechaEntry, citaPacienteEntry , buscarCitaEntry;
    Button buttonCitaActualizar, buttonCitaGuardar, buttonCitaLimpiar, buttonCitaEliminar, buttonCitaEditar;
    String idCita=null; //Id de la tabla Cita
    Connection DB =null;
	Statement st = null;
	PreparedStatement pst = null;

	
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
		buscarCitaEntry = (Entry) builder.getObject("entryBuscarCita");
		buscarCitaEntry.connect(on_entryBuscarCita_icon_press());		
		
		//#################		Button		#################	\\
				
				
		buttonCitaActualizar = (Button) builder.getObject("buttonCitaActualizar");
		buttonCitaActualizar.connect(on_buttonCitaActualizar_clicked());
		
		buttonCitaLimpiar = (Button) builder.getObject("buttonCitaLimpiar");
		buttonCitaLimpiar.connect(on_buttonCitaLimpiar_clicked());
		
		buttonCitaGuardar = (Button) builder.getObject("buttonCitaGuardar");
		buttonCitaGuardar.connect(on_buttonCitaGuardar_clicked());
		
		buttonCitaEliminar = (Button) builder.getObject("buttonCitaEliminar");
		buttonCitaEliminar.connect(on_buttonCitaEliminar_clicked());
		
		buttonCitaEditar = (Button) builder.getObject("buttonCitaEditar");
		buttonCitaEditar.connect(on_buttonCitaEditar_clicked());
		
		
		//#################		TreeView		#################	\\
		
        view = (TreeView) builder.getObject("treeview_Citas_ID");
 
        /* Construccion del modelo */
        listStore = new ListStore(new DataColumnString[]{
        		
        		citaIDColumn 			= new DataColumnString(),
        		paciNombreColumn		= new DataColumnString(),
        		servNombreColumn		= new DataColumnString(),
        		citaFechaColumn			= new DataColumnString(),
        		servHoraInicioColumn	= new DataColumnString(),
        		servHoraSalidaColumn	= new DataColumnString(),
        		empNombreColumn			= new DataColumnString(),
        });
 
        /*Establezca TreeModel que se usa para obtener datos de origen para este TreeView*/
        view.connect(on_treeview_Citas_ID_row_activated());
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
	
	//---------------------- Accion de Botones ----------------------//
	
	
	
	private Clicked on_buttonCitaEditar_clicked() {
		return new Button.Clicked() {
			
			@Override
			public void onClicked(Button arg0) {
				
				
				
				try {
			        //Conexion con la base de datos
			        DB = DriverManager.getConnection(URL, DBUSER, getPasswd());
			        String query;
			        
			        query = "UPDATE \"Cita\" "
			        		+ "SET \"citaFecha\" = ? , \"citaServicio\" = ? , \"citaPaciente\"= ? "
			        		+ "WHERE \"citaID\" = ?;";

			        pst= DB.prepareStatement(query);
			        
			        pst.setDate(1, Date.valueOf(citaFechaEntry.getText()));
			        pst.setInt(2, servicioCombo.getActive());
			        pst.setInt(3, Integer.valueOf(citaPacienteEntry.getText()));
			        pst.setInt(4, Integer.valueOf(idCita));

			        pst.executeUpdate();
			        
			        pst.close();
			        DB.close();
			        
			    } catch (SQLException e) {
			        System.err.println("Error: " +e.getMessage() );
			 
			    }
				
				
				
				
			}
		};
	}

	private Clicked on_buttonCitaEliminar_clicked() {
		return new Button.Clicked() {
			
			@Override
			public void onClicked(Button arg0) {
				
				
				try {
			        //Conexion con la base de datos
			        DB = DriverManager.getConnection(URL, DBUSER, getPasswd());

			        String query=null;
			        
			        query =	 "DELETE FROM \"Cita\" WHERE  \"citaID\"   = ?";

			        pst= DB.prepareStatement(query);
			        
			        pst.setInt(1, Integer.valueOf(idCita));
			        
			        pst.executeUpdate();
			        
			        pst.close();
			        DB.close();
			 

			    } catch (SQLException e) {
			        System.err.println("Error: " +e.getMessage() );
			 
			    }
				
			}
		};
	}

	private RowActivated on_treeview_Citas_ID_row_activated() {
		return new TreeView.RowActivated() {
			
			@Override
			public void onRowActivated(TreeView arg0, TreePath arg1, TreeViewColumn arg2) {
				// TODO Auto-generated method stub
				final TreeIter row;
				 
		        row = listStore.getIter(arg1);
		        //
		        //idCita
		        idCita  = listStore.getValue(row, citaIDColumn);
		        String paciNombre = listStore.getValue(row, paciNombreColumn);
		        String servNombre = listStore.getValue(row, servNombreColumn);
		        String citaFecha = listStore.getValue(row, citaFechaColumn);
		        
		        
		        String idPaciente = null;
		        //Conexion con la base de datos
		        try {
		        	
		        	DB = DriverManager.getConnection(URL, DBUSER, getPasswd());
		        	
		        	st = DB.createStatement();
						
					String query=null;
				        
			        query =String.format("SELECT \"paciID\" FROM \"Paciente\" WHERE \"paciNombre\"= '%s';",paciNombre);

			        ResultSet rs = st.executeQuery(query);
			        while    ( rs.next() )
			        	idPaciente = rs.getString("paciID");

				        
				    rs.close();
				    st.close();
				    DB.close();
				        
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			         
		         
		         
		         citaPacienteEntry.setText(idPaciente);
		         
		         for (int i = 0; i < servicioArray.length; i++) {
		        	 
		        	 if(servicioArray[i].equals(servNombre)) {
		        		 
		        		 servicioCombo.setActive(i);
		        		 break;
		        		 
		        	 }
					
				}
		         citaFechaEntry.setText(citaFecha);
		         
		         
			}
		};
	}
	

	private IconPress on_entryBuscarCita_icon_press() {
		return new Entry.IconPress() {
			
			@Override
			public void onIconPress(Entry arg0, EntryIconPosition arg1, Event arg2) {
				// TODO Auto-generated method stub
				
				buscarCitaEntry.setText("");
				
			}
		};
	}



	private DaySelectedDoubleClick on_calendarID_day_selected_double_click() {
		return new Calendar.DaySelectedDoubleClick() {
			
			@Override
			public void onDaySelectedDoubleClick(Calendar arg0) {
				
				String Data = String.format("%d-%d-%d",
						citaCalendar.getDateYear(),citaCalendar.getDateMonth(),citaCalendar.getDateDay());
				citaFechaEntry.setText(Data);
				
			}
		};
	}

	
	
	
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
			        
			        String query=null;
			        
			        
			        query = "INSERT INTO \"Cita\" ( \"citaFecha\" , \"citaServicio\" , \"citaPaciente\" ) "
			        		+ "VALUES ( ?  , ? , ? );";
			        
			        

			        pst= DB.prepareStatement(query);

			        pst.setDate(1, Date.valueOf(citaFechaEntry.getText()));
			        pst.setInt(2, SelecServiceCombo);
			        pst.setInt(3, Integer.valueOf(citaPacienteEntry.getText()));
			        
			        pst.executeUpdate();
			        
			        pst.close();
			        DB.close();
			        
			    } catch (SQLException e) {
			        System.err.println("Error: ss " +e.getMessage() );
			 
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
