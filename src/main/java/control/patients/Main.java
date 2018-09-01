package control.patients;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import org.gnome.gdk.Event;
import org.gnome.gtk.Builder;
import org.gnome.gtk.Button;
import org.gnome.gtk.Button.Clicked;
import org.gnome.gtk.Gtk;
import org.gnome.gtk.Widget;
import org.gnome.gtk.Window;
import org.gnome.gtk.Window.DeleteEvent;

import control.pages.Cita;
import control.pages.Empleado;
import control.pages.Registro;
import control.pages.Servicio;


public class Main {

	Builder builder;
	Window window;
	Button buttonOhShitID;

	//Paginas
	Empleado pagEmpleado;
	Servicio pagServicio;
	Registro pagRegistro;
	Cita pagCita;


	public Main() {

		try {
			builder = new Builder();
			builder.addFromFile("src/main/java/gui/app/Main.glade");
		} catch (FileNotFoundException | ParseException e) {
			e.printStackTrace();
		}

		window = (Window) builder.getObject("window_main_ID");
		//window.setMaximize(true);
		window.connect(on_window_main_ID_destroy());
		
		//Button
		buttonOhShitID = (Button) builder.getObject("buttonOhShitID");
		buttonOhShitID.connect(on_buttonOhShitID_clicked());

		//Paginas
		pagEmpleado = new Empleado(builder);
		//pagEmpleado.treeviewEmpleado();

		pagServicio = new Servicio(builder);
		//pagServicio.treeviewServicio();

		pagRegistro = new Registro(builder);
		//pagRegistro.treeviewPaciente();
		//pagRegistro.treeviewFamiliar();

		pagCita = new Cita(builder);
		//pagCita.treeviewCita();

		window.showAll();
	}


	private Clicked on_buttonOhShitID_clicked() {
		return new Button.Clicked() {
			
			@Override
			public void onClicked(Button arg0) {
				
				Runtime obrun = Runtime.getRuntime();
				
				try {
					obrun.exec("tilix -- htop");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		};
	}


	private DeleteEvent on_window_main_ID_destroy() {
		return new Window.DeleteEvent() {

			@Override
			public boolean onDeleteEvent(Widget arg0, Event arg1) {
				Gtk.mainQuit();
				return false;
			}
		};
	}

	public static void main(String[] args) {
		//Cerrar login
		Gtk.init(args);
		new Main();
		Gtk.main();

	}

}
