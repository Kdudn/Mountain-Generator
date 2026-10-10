import javax.swing.*;
import javax.swing.event.*;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;

public class mountainGenerator implements ActionListener {

    private JFrame frame;
    private JButton generate;
    private JPanel panel;
	private JTextField input;
    private JPanel seedpanel;
    private JPanel steppanel;
	private JPanel inputpanel;
    private JPanel generatepanel;
    public JLabel seed;
	public int currentSeed;
    private JLabel steps;
    private Renderer renderer;
    private Random rand;

    private int range = 10000;
    public List<Integer> points = new ArrayList<Integer>();

    public mountainGenerator() {
        frame = new JFrame();

        renderer = new Renderer();
        rand = new Random();

        generate = new JButton("Generate");

        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generate();
            }
        });

        generate.setBackground(new Color(250, 249, 246));
        generate.setForeground(new Color(10, 10, 10));
        generate.setFocusPainted(false);
        generate.setFocusable(false);
        generate.setContentAreaFilled(false);
        generate.setOpaque(true);
        generate.setBorderPainted(false);
        generate.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                generate.setBackground(new Color(200,200,200));
                generate.revalidate();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                generate.setBackground(new Color(250,249,246));
                generate.revalidate();
            }
        });

        generatepanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        generatepanel.add(generate);
        generatepanel.setBackground(new Color(10,10,10));
        generatepanel.setAlignmentX(0.5f);
        generatepanel.setAlignmentY(0.5f);

        seed = new JLabel();
        seed.setForeground(new Color(250, 249, 246));
		
        input = new JTextField();

        seedpanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        seedpanel.add(seed);
		seedpanel.add(input);
        seedpanel.setBackground(new Color(10,10,10));
        seedpanel.setPreferredSize(new Dimension(70, 0));

        steps = new JLabel();
        steps.setForeground(new Color(250,249,246));

        steppanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        steppanel.add(steps);
        steppanel.setBackground(new Color(10,10,10));
        steppanel.setForeground(new Color(250,249,246));

        panel = new JPanel();
        panel.setLayout(new OverlayLayout(panel));
        panel.setBackground(new Color(10, 10, 10));

        panel.add(generatepanel);
        panel.add(steppanel);
        panel.add(seedpanel);

        frame.setSize(800, 500);
        frame.setResizable(false);

        frame.add(renderer, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);

        ImageIcon icon = new ImageIcon("Icon.JPG");
        frame.setIconImage(icon.getImage());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Mountain Generator (Collatz Conjecture)");
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new mountainGenerator();
    }

    public void generate() {
        points.clear();
		int startingNumber;
		if(input.getText().isEmpty() || Integer.parseInt(input.getText()) == currentSeed){
			startingNumber = rand.nextInt(range);
		} else {
			startingNumber = Integer.parseInt(input.getText());
		}
        Collatz(startingNumber);
        seed.setText("Seed: ");
		input.setText(Integer.toString(startingNumber));
        steps.setText("Steps: " + Integer.toString(points.size() + 1));
        renderer.repaint();
    }

    public void Collatz(int x) {
		currentSeed = x;
        while (x != 1) {
            points.add(x);

            if (x % 2 == 0) {
                x /= 2;
            } else {
                x = x * 3 + 1;
            }
        }
        points.add(1);
    }

    public int getScaledPoint(int point) {
        int max = 0;

        for (int p : points) {
            if (p > max) {
                max = p;
            }
        }

        if (max > 400) {
            return (point * 400) / max;
        }

        return point;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public class Renderer extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            g.setColor(new Color(10, 10, 10));
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(new Color(250, 249, 246));

            if (points.size() < 2) {
                return;
            }

            int width = getWidth();
            int height = getHeight();

            for (int i = 0; i < points.size() - 1; i++) {

                int x1 = (width * i) / (points.size() - 1);
                int x2 = (width * (i + 1)) / (points.size() - 1);

                int y1 = height - getScaledPoint(points.get(i)) - 10;
                int y2 = height - getScaledPoint(points.get(i + 1)) - 10;

                g.drawLine(x1, y1, x2, y2);
            }
        }
    }
}