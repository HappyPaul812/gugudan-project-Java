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
		
		JLabel title = new JLabel("\uAD6C\uAD6C\uB2E8 \uD034\uC988");
		title.setBounds(165, 10, 110, 24);
		title.setFont(new Font("굴림", Font.BOLD, 20));
		panel.add(title);
		
		timer = new JLabel("\uD0C0\uC774\uBA38");
		timer.setHorizontalAlignment(SwingConstants.CENTER);
		timer.setFont(new Font("굴림", Font.PLAIN, 16));
		timer.setBounds(152, 39, 142, 15);
		panel.add(timer);
		
		//시작 버튼
		btnNewButton = new JButton("\uC2DC   \uC791");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control();
			}
		});
		btnNewButton.setBounds(165, 197, 97, 23);
		panel.add(btnNewButton);
		
		lblQuestion = new JLabel("? X ? = ");
		lblQuestion.setFont(new Font("굴림", Font.PLAIN, 16));
		lblQuestion.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuestion.setBounds(131, 87, 57, 15);
		panel.add(lblQuestion);
		
		Action action = new AbstractAction() {
		    @Override
		    public void actionPerformed(ActionEvent e)
		    {
		    	confirm_answer();
		    }
		};
		
		userAnswer = new JTextField();
		userAnswer.addActionListener( action );
		userAnswer.setBounds(197, 85, 65, 21);
		panel.add(userAnswer);
		userAnswer.setColumns(10);
		
		lblAnswerCounter = new JLabel("\uB9DE\uD78C \uAC1C\uC218 : ");
		lblAnswerCounter.setFont(new Font("굴림", Font.PLAIN, 16));
		lblAnswerCounter.setHorizontalAlignment(SwingConstants.LEFT);
		lblAnswerCounter.setBounds(26, 39, 128, 24);
		panel.add(lblAnswerCounter);
	}
	
	public void timer() {

		time = new Thread(new Runnable() {
			
			public void run() {
				int i = 0, j = 0;
				timeOver = false;
				for(i = 2; i >= 0; i--) {
					for(j = 59; j >= 0; j--) {
						timer.setText( Integer.toString(i) + "분 "  + Integer.toString(j) + "초");
						try {
							Thread.sleep(1000); //1초 대기
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				timeOver = true;
				playStop = true;
				timer.setText("시간 초과!!!");
			}
			
		});
		//time.stop();
	}
	
	public int question() {
		Random rd = new Random();
		//int i = rd.nextInt(2, 9); // 최신 자바문법
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
					JOptionPane.showMessageDialog(null, "답을 넣어주세요.", "딥 넣지 않았음.", JOptionPane.WARNING_MESSAGE);
					confirm_answer.stop();
				} else {
					int nuserAnswer = Integer.parseInt(userAnswer.getText());
					userAnswer.setText("");
					while(!timeOver) {
						if(answer == nuserAnswer) {
							answerCounter += 1;
							lblAnswerCounter.setText("맞힌 개수 : " + Integer.toString(answerCounter));
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
			lblAnswerCounter.setText("맞힌 개수 : ");
			
			answerCounter = 0;
			playStop = false;
		} else {
			answerCounter = 0;
			userAnswer.requestFocus();
			btnNewButton.setText("다시  시작");
			timer();
			time.start();
			questions();
			playStop = true;
			
		}	
	}
}
