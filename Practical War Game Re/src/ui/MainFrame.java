package ui;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import ui.Renderer.TraditionalBoard;
import ui.Renderer.VisualBoard;




public class MainFrame {

	private JFrame frame;
	private TraditionalBoard board;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Practical War Game");
		frame.setBounds(100, 100, 450, 300);
		frame.setMinimumSize(new Dimension(400, 400));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setForeground(Color.WHITE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		
		board = new VisualBoard();

		frame.getContentPane().add(board, BorderLayout.CENTER);
		
		
	}
}
