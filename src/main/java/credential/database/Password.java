package credential.database;

public class Password {

	private static String passwd;

	public Password() {

	}

	public String getPasswd() {
		return passwd;
	}

	@SuppressWarnings("static-access")
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

}
