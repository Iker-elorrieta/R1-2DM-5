package Vista;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;

public class Login extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public Login() {
		setLayout(null);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setFont(new Font("Arial Black", Font.PLAIN, 17));
		lblLogin.setBounds(155, 114, 314, 66);
		add(lblLogin);

	}

}
