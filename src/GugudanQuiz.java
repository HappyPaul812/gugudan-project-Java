import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.EventQueue;

import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import java.net.URL;
import java.net.URLEncoder;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.awt.Color;
//import java.io.OutputStream;


public class GugudanQuiz {
	private JFrame frame;

	/**
	 * Launch the application.
	 */
	
	private boolean timeOver = false, playStop = false;
	private int answer, answerCounter = 0;
	
	
	private Thread time, confirm_answer;
	
	private JLabel timer;
	private JButton btnNewButton;
	private JLabel lblQuestion;
	private JLabel lblAnswerCounter;
	private JTextField userAnswer;
	private JTextField name;
	
	private JLabel lblRankView;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GugudanQuiz window = new GugudanQuiz();
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
	public GugudanQuiz() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("\uAD6C\uAD6C\uB2E8 \uD034\uC988");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 434, 261);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JPanel rankPanel = new JPanel();
		rankPanel.setBounds(0, 0, 434, 261);
		frame.getContentPane().add(rankPanel);
		rankPanel.setLayout(null);
		rankPanel.setVisible(false);
		
		JLabel title = new JLabel("\uAD6C\uAD6C\uB2E8 \uD034\uC988");
		title.setBounds(165, 10, 110, 24);
		title.setFont(new Font("??????", Font.BOLD, 20));
		panel.add(title);
		
		timer = new JLabel("\uD0C0\uC774\uBA38");
		timer.setBounds(152, 39, 142, 15);
		timer.setHorizontalAlignment(SwingConstants.CENTER);
		timer.setFont(new Font("??????", Font.PLAIN, 16));
		panel.add(timer);
		
		//?????? ??????
		btnNewButton = new JButton("\uC2DC   \uC791");
		btnNewButton.setBounds(165, 197, 97, 23);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control();
			}
		});
		panel.add(btnNewButton);
		
		lblQuestion = new JLabel("? X ? = ");
		lblQuestion.setBounds(152, 137, 57, 15);
		lblQuestion.setFont(new Font("??????", Font.PLAIN, 16));
		lblQuestion.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblQuestion);
		
		Action action = new AbstractAction() {
		    @Override
		    public void actionPerformed(ActionEvent e)
		    {
		    	confirm_answer();
		    }
		};
		
		userAnswer = new JTextField();
		userAnswer.setBounds(218, 135, 65, 21);
		userAnswer.addActionListener( action );
		panel.add(userAnswer);
		userAnswer.setColumns(10);
		
		lblAnswerCounter = new JLabel("\uB9DE\uD78C \uAC1C\uC218 : ");
		lblAnswerCounter.setBounds(26, 39, 128, 24);
		lblAnswerCounter.setFont(new Font("??????", Font.PLAIN, 16));
		lblAnswerCounter.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblAnswerCounter);
		
		JLabel lblName = new JLabel("?????? :");
		lblName.setBounds(152, 85, 57, 15);
		lblName.setFont(new Font("??????", Font.PLAIN, 16));
		panel.add(lblName);
		
		name = new JTextField();
		name.setBounds(203, 83, 81, 21);
		panel.add(name);
		name.setColumns(10);
		
		JButton btnRank = new JButton("?????? ??????");
		btnRank.setBounds(165, 228, 97, 23);
		btnRank.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setVisible(false);
				rankViewer();
				rankPanel.setVisible(true);
			}
		});
		
		panel.add(btnRank);
		
		
		
//-----------------------------------------------------------------------------------------------------------------------
		JButton btnRankClose = new JButton("?????? ??????");
		btnRankClose.setBounds(165, 228, 97, 23);
		btnRankClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setVisible(true);
				rankPanel.setVisible(false);
			}
		});
		
		lblRankView = new JLabel("??????");
		lblRankView.setHorizontalAlignment(SwingConstants.CENTER);
		lblRankView.setBounds(12, 10, 410, 208);
		rankPanel.add(lblRankView);
		lblRankView.setBackground(Color.WHITE);
		lblRankView.setFont(new Font("??????", Font.PLAIN, 16));
		
		
		
		rankPanel.add(btnRankClose);
	}
	
	public void timer() {

		time = new Thread(new Runnable() {
			
			public void run() {
				int i = 0, j = 0;
				timeOver = false;
				for(i = 2; i >= 0; i--) {
					for(j = 59; j >= 0; j--) {
						timer.setText( Integer.toString(i) + "??? "  + Integer.toString(j) + "???");
						try {
							Thread.sleep(1000); //1??? ??????
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				saveDB();
				timeOver = true;
				playStop = true;
				timer.setText("?????? ??????!!!");
			}
			
		});
		//time.stop();
	}
	
	
	public void saveDB() {
		final String sURL = "https://paul81web.000webhostapp.com/php/gugudan_insert.php";
		
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("name", name.getText());
		param.put("score", Integer.toString(answerCounter));
		//StringBuilder postData = new StringBuilder();
		//Map<String, String> resultMap = new HashMap<String, String>();
		BufferedReader in = null;
		
		try {
			URL url = new URL(sURL);
			
			StringBuilder postData = new StringBuilder();
			for(Map.Entry<String, String> params: param.entrySet()) {
				if(postData.length() != 0) postData.append("&");
				postData.append(URLEncoder.encode(params.getKey(), "UTF-8"));
				postData.append("=");
				postData.append(URLEncoder.encode(String.valueOf(params.getValue()), "UTF-8"));
					
			}
			byte[] postDataBytes = postData.toString().getBytes("UTF-8");
			
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	
			conn.setRequestMethod("POST");
	        
	        //?????? API??? ?????? ?????? content-Type??? ??????????????????.
		    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        
	        // content-length??? ????????? ???????????? ??????
		    conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
			
	        // ???????????? ??? ???????????? ????????? ??? ?????? ????????????
		    conn.setDoOutput(true);
	        
	        // POST ??????
		    conn.getOutputStream().write(postDataBytes);
		 
		    in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		 
		    String inputLine;
		    StringBuffer response = new StringBuffer();
		    while((inputLine = in.readLine()) != null) { // response ??????
		    	response.append(inputLine);
		    }

			/*String jsonStr = response.toString();
			JSONParser parser = new JSONParser();			
			resultMap = (Map<String, Object>)parser.parse(jsonStr);*/
			
			
		} catch(MalformedURLException e) {
			JOptionPane.showMessageDialog(null, "????????? ????????? ?????? ???????????? ", "URL ??????", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		} catch(IOException e) {
			JOptionPane.showMessageDialog(null, "????????? ????????? ????????? ??? ????????????.", "?????? ??????.", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		} finally { 
		//http ?????? ??? ?????? ?????? ??? BufferedReader??? ???????????????
		try {
			if (in != null) {
				in.close();	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	}
	
	public void rankViewer() {
		URL url = null;
		BufferedReader input = null;
		//?????? ????????? html????????? ????????????
		String address = "https://paul81web.000webhostapp.com/php/gugudan_score_viewer.php";
		String line = "";
		String rankHtml ="";
		
		try {
			url = new URL(address);
			input = new BufferedReader(new InputStreamReader(url.openStream()));
		    
			while((line=input.readLine()) != null) {
				//System.out.println(line);
				rankHtml += line;
			}
			
			lblRankView.setText("<html>" + rankHtml + "</html>");
			//input.close();
		} catch(MalformedURLException e) {
			JOptionPane.showMessageDialog(null, "????????? ????????? ?????? ???????????? ", "URL ??????", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		} catch(IOException e) {
			JOptionPane.showMessageDialog(null, "????????? ????????? ????????? ??? ????????????.", "?????? ??????.", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		} finally { 
			try {
				if (input != null) {
					input.close();	
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int question() {
		Random rd = new Random();
		//int i = rd.nextInt(2, 9); // ?????? ????????????
		int i = rd.nextInt(9);
		if(i <= 1)
			i = 2;
		
		return i;
	}
	
	public void questions() {
		int i = question();
		int j = question();
		answer = i*j;
		lblQuestion.setText( Integer.toString(i) + " X "  + Integer.toString(j) + " = ");
	}
	
	public void confirm_answer() {
		confirm_answer = new Thread(new Runnable() {
			
			public void run() {
				if(userAnswer.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "?????? ???????????????.", "??? ?????? ?????????.", JOptionPane.WARNING_MESSAGE);
					confirm_answer.stop();
				} else {
					int nuserAnswer = Integer.parseInt(userAnswer.getText());
					userAnswer.setText("");
					while(!timeOver) {
						if(answer == nuserAnswer) {
							answerCounter += 1;
							lblAnswerCounter.setText("?????? ?????? : " + Integer.toString(answerCounter));
							questions();
						}
						userAnswer.requestFocus();
					}
				}
			}
		});
		confirm_answer.start();
		if(timeOver) {
			confirm_answer.stop();
		}
		
	}
	
	public void control() {
		
		if(playStop) {
			time.stop();
			confirm_answer.stop();
			
			btnNewButton.setText("\uC2DC   \uC791");
			lblQuestion.setText("? X ? = ");
			timer.setText("\uD0C0\uC774\uBA38");
			lblAnswerCounter.setText("?????? ?????? : ");
			
			answerCounter = 0;
			playStop = false;
		} else {
			answerCounter = 0;
			userAnswer.requestFocus();
			btnNewButton.setText("??????  ??????");
			timer();
			time.start();
			questions();
			playStop = true;
			
		}	
	}
}
