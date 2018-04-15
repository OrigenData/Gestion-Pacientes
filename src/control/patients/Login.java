package control.patients;

import java.io.FileNotFoundException;
import java.text.ParseException;

import org.gnome.gdk.Event;
import org.gnome.gtk.Builder;
import org.gnome.gtk.Button;
import org.gnome.gtk.ButtonsType;
import org.gnome.gtk.Button.Clicked;
import org.gnome.gtk.Entry;
import org.gnome.gtk.Gtk;
import org.gnome.gtk.MessageDialog;
import org.gnome.gtk.MessageType;
import org.gnome.gtk.ResponseType;
import org.gnome.gtk.Widget;
import org.gnome.gtk.Window;
import org.gnome.gtk.Window.DeleteEvent;
import org.gnome.gtk.WindowPosition;

import credential.database.Password;
import credential.database.ServerPG;

import java.sql.*;

public class Login extends Password implements ServerPG{
	
	Password dbpasswd;
	Builder builder;
	Window window;
	Entry entryPassword;
	Button buttonConnect, buttonQuit;
	MessageDialog dialog;
	Connection DB =null;

	public Login() {

		try {
			builder = new Builder();
			builder.addFromFile("src/gui/app/Login.glade");

		} catch (FileNotFoundException | ParseException e) {

			System.err.println(e.getMessage());

		}

		getObjectGlade();
		window.showAll();

	}

	private void getObjectGlade() {
		
		//Window
		window = (Window) builder.getObject("window_ID");
		window.setPosition(WindowPosition.CENTER);
		window.connect(on_windowID_destroy());

		//Cajas de texto
		entryPassword = (Entry) builder.getObject("entry_passwd_ID");

		//Botones
		buttonConnect = (Button) builder.getObject("button_connect_ID");
		buttonConnect.connect(on_button_connect_ID_clicked());

		buttonQuit = (Button) builder.getObject("button_quit_ID");
		buttonQuit.connect(on_button_quit_ID_activate());

	}
	

	private Clicked on_button_connect_ID_clicked() {
		return new Button.Clicked() {

			@Override
			public void onClicked(Button arg0) {


				if(entryPassword.getText().isEmpty()) {

					dialog = new MessageDialog(window, true, MessageType.WARNING,
					ButtonsType.CLOSE,"No se ha ingresado la contraseña");
					dialog.setSecondaryText("Favor de ingresar la contraseña");

					// Oculta el dialogo
					ResponseType choice = dialog.run();

					if (choice == ResponseType.CLOSE)
						dialog.hide();
					} else{

	                try {

				        //Conexion con la base de datos
				        DB = DriverManager.getConnection(URL, DBUSER, entryPassword.getText());
				        
				        //Mandar al constructor la contraseña
				        setPasswd(entryPassword.getText().toString());
				        
				        dialog = new MessageDialog(window, true, MessageType.WARNING,
				        		ButtonsType.CLOSE,"Conectado");
						dialog.setSecondaryText("Se realizo la conexion exitosamente");

						// Oculta el dialogo
						 ResponseType choice = dialog.run();
						 if (choice == ResponseType.CLOSE)
							 dialog.hide();
						 
						 DB.close();
						 
						 //Show Windows Main
						 @SuppressWarnings("unused")
						 Main main = new Main();
						 //Destuir la ventana login
						 window.destroy();
						 
	                } catch (SQLException e) {
	                	
	                	dialog = new MessageDialog(window, true, MessageType.WARNING,
	                			ButtonsType.CLOSE,"Error en la conexion");
				    	dialog.setSecondaryText(e.getMessage());
				    	
				    	// Oculta el dialogo
				    	ResponseType choice = dialog.run();
				    	if (choice == ResponseType.CLOSE)
				    		dialog.hide();
				    	}
	                }
				}//Fina onClicked
			};
			
	}

	private Clicked on_button_quit_ID_activate() {
		return new Button.Clicked() {

			@Override
			public void onClicked(Button arg0) {
				Gtk.mainQuit();
			}
		};
	}
	
	
	private DeleteEvent on_windowID_destroy() {
		return new Window.DeleteEvent() {

			@Override
			public boolean onDeleteEvent(Widget arg0, Event arg1) {
				Gtk.mainQuit();
				return false;
			}
		};

	}


	public static void main(String[] args) {

		Gtk.init(args);
		new Login();
		Gtk.main();

	}

}
