package codes;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraphBurningUI extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GraphBurningUI() {
        setTitle("Graph Burning Visualization");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String fontName = "Comic Sans MS";

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel mainGrid = new JPanel(new GridLayout(4, 1, 0, 10));
        
        // Heading 1
        JLabel headingLabel = new JLabel("Welcome to the");
        headingLabel.setFont(new Font(fontName, Font.BOLD, 40));

        // Heading 2
        JLabel headingLabel2 = new JLabel("          Graph Burning");
        headingLabel2.setFont(new Font(fontName, Font.BOLD, 40));

        // Heading 3
        JLabel headingLabel3 = new JLabel("                    Visualization");
        headingLabel3.setFont(new Font(fontName, Font.BOLD, 40));

        
        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 0));

        JButton button2Approximation = createButton("3-Approximation");
        JButton button3Approximation = createButton("Furthest First");
        JButton buttonRandomized = createButton("Randomized");
        JButton buttonCustom = createButton("Custom");

        button2Approximation.setFont(new Font(fontName, Font.PLAIN, 13));
        button3Approximation.setFont(new Font(fontName, Font.PLAIN, 13));
        buttonRandomized.setFont(new Font(fontName, Font.PLAIN, 13));
        buttonCustom.setFont(new Font(fontName, Font.PLAIN, 13));
        
        buttonPanel.add(button2Approximation);
        buttonPanel.add(button3Approximation);
        buttonPanel.add(buttonRandomized);
        buttonPanel.add(buttonCustom);

        
        mainGrid.add(headingLabel);
        mainGrid.add(headingLabel2);
        mainGrid.add(headingLabel3);
        mainGrid.add(buttonPanel);
        
        mainPanel.add(mainGrid);

        add(mainPanel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createButton(String buttonText) {
        JButton button = new JButton(buttonText);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your logic for button click here
//                JOptionPane.showMessageDialog(null, "Selected: " + buttonText);
            	setVisible(false);
            	new Main();
            }
        });
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GraphBurningUI());
    }
}
