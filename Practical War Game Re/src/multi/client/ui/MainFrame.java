package multi.client.ui;



import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import board.Coord;
import board.Formation;
import board.Shogi;
import board.Team;
import board.Xiangqi;
import multi.client.IServerListener;
import multi.client.ServerHandler;
import multi.common.UnknownCommandException;
import ui.Renderer.TraditionalBoard;
import ui.Renderer.VisualBoard;
import ui.util.Logger;





public class MainFrame {
	public static MainFrame INSTANCE;
	private DefaultListModel<String> playerlist = new DefaultListModel<>();
	
	private JPanel chatPanel = new JPanel();
	private JScrollPane chatPane = new JScrollPane();
	private JTextArea chatArea = new JTextArea();
	private JFrame frame;
	private TraditionalBoard board;
	public ServerHandler handler;

//	JButton restart = new JButton("在塞");
	JButton reset = new JButton("在塞");
	public int team = 0; 
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					INSTANCE = window;
					window.init();
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
//		initialize();
	}
	private JSplitPane split = new JSplitPane();
	JPanel choice = new JPanel();
	public int gameType = 1;
	/**
	 * Initialize the contents of the frame.
	 */
	public void init() {
		frame = new JFrame("Practical War Game");
		frame.setBounds(100, 100, 450, 300);
		frame.setMinimumSize(new Dimension(400, 400));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setForeground(Color.WHITE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
	
		split.setResizeWeight(1);
		split.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		
		
		chatPane.setViewportView(chatArea);
		frame.getContentPane().add(split, BorderLayout.CENTER);
		
		reset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				split.setLeftComponent(choice);
			}
		});
		
		JButton Bshogi = new JButton("象棋(Chinese)");
		Bshogi.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				gameType = 2;
				board = new VisualBoard(new Xiangqi(Formation.D, TraditionalBoard.teams));
				split.setLeftComponent(board);
//					chatPanel.remove(restart);
				
			}
		});
		JButton Bxiangqi = new JButton("将棋(Korean)");
		Bxiangqi.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				gameType = 0;
				board = new VisualBoard(new Shogi(Formation.HEEH, TraditionalBoard.teams));
				split.setLeftComponent(board);
//				chatPanel.remove(restart);
			}
		});
		
		choice.add(Bshogi);
		choice.add(Bxiangqi);
		
//		frame.getContentPane().add(chatPane, BorderLayout.SOUTH);
		chatPanel.add(reset);
		chatPanel.add(chatPane);
//		chatPanel.add();
		
		split.setLeftComponent(choice);
		split.setRightComponent(chatPanel);
		
		frame.setVisible(true);
	}

	public void setHandler(ServerHandler client) {
		this.handler = client;
		handler.addListener(new DataRenderer());
	}
	
	public static class DataRenderer implements IServerListener {
		@SuppressWarnings("unchecked")
		@Override
		public void onDataReceived(String cmd, Object data) {
			Logger.log(" handler : " + cmd + " and " + data);
			switch (cmd) {

			case "MOVE":
//				String [] coords = ((String) data).split("-");
//				Coord from = Coord.parse(coords[0]);
//				Coordd to = Coord.parse(coords[1]);
//				
//				INSTANCE.board.game.trymove(from.x, from.y, to.x, to.y);
//				
				break;
			case "PRV_MSG":
				Map<String, Object> map = (Map<String, Object>)data;
				String msg = (String) map.get("msg");
				String sender = (String) map.get("sender");
				INSTANCE.printMessage("[PRV_MSG] "+sender+": "+msg);
				
				break;
			case "MSG":
				map = (Map<String, Object>)data;
				msg = (String) map.get("msg");
				sender = (String) map.get("sender");
				
				if(msg.startsWith("(") && msg.endsWith(")") && msg.contains(")-(")){
					String txt = ((String)msg);
					
					String [] coords = txt.split("-");
					Coord from = Coord.parse(coords[0]);
					Coord to = Coord.parse(coords[1]);
					
					INSTANCE.board.game.trymove(from.x, from.y, to.x, to.y);
					INSTANCE.board.repaint();
				} else INSTANCE.printMessage(sender +": "+ msg);

				break;
				
			case "CHATTER_LIST":
				String [] names = (String[]) data;
				System.out.println("대화 참여자들 : " + names.toString());
				INSTANCE.updateChatterList(names);
				break;
				
			case "LOGIN":
				String nickNam = (String)data;
				INSTANCE.addNickName(nickNam);
				INSTANCE.printMessage(nickNam+" login");
				break;
				
			case "LOGOUT":
				nickNam = (String)data;
				INSTANCE.removeNickName(nickNam);
				INSTANCE.printMessage(nickNam+" logout");
				break;
			default : 
				new UnknownCommandException("unknown command: "+cmd);
			}
		}
	}

	public void addNickName(String nicknam){
		playerlist.addElement(nicknam);
	}
	
	public void removeNickName(String nicknam){
		playerlist.removeElement(nicknam);
	}	
	
	public void updateChatterList( String[] nickNames ){
		playerlist.clear();
		Arrays.stream(nickNames).forEach(playerlist::addElement);
	}
	
	public void printMessage(String msg){
		chatArea.append(msg+"\n");
	}

	public void over(Team team) {
		printMessage(team + " 赢了！");	
	}
}
