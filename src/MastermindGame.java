import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class MastermindGame {
    private final JFrame frame;
    private final JPanel colorSelectionPanel;
    private final JPanel guessesPanel;
    private final JButton submitButton;
    private final String[] colors = {"Red", "Blue", "Green", "Yellow", "Orange", "Purple"};
    private final ArrayList<String> secretCode;
    private final ArrayList<JComboBox<String>> colorSelectors;
    private final JTextArea feedbackArea;

    public MastermindGame() {
        frame = new JFrame("Mastermind Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Generate the secret code
        secretCode = generateSecretCode();

        // Top panel for selecting colors
        colorSelectionPanel = new JPanel();
        colorSelectionPanel.setLayout(new FlowLayout());
        colorSelectors = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            JComboBox<String> comboBox = new JComboBox<>(colors);
            colorSelectors.add(comboBox);
            colorSelectionPanel.add(comboBox);
        }

        // Submit button
        submitButton = new JButton("Submit Guess");
        submitButton.addActionListener(new SubmitGuessListener());

        // Panel for displaying guesses and feedback
        guessesPanel = new JPanel();
        guessesPanel.setLayout(new BoxLayout(guessesPanel, BoxLayout.Y_AXIS));
        feedbackArea = new JTextArea();
        feedbackArea.setEditable(false);

        // Add components to frame
        frame.add(colorSelectionPanel, BorderLayout.NORTH);
        frame.add(submitButton, BorderLayout.CENTER);
        frame.add(new JScrollPane(feedbackArea), BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private ArrayList<String> generateSecretCode() {
        Random random = new Random();
        ArrayList<String> code = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            code.add(colors[random.nextInt(colors.length)]);
        }
        return code;
    }

    private class SubmitGuessListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> guess = new ArrayList<>();
            for (JComboBox<String> selector : colorSelectors) {
                guess.add((String) selector.getSelectedItem());
            }

            Feedback feedback = checkGuess(guess);
            feedbackArea.append("Guess: " + guess + "\n");
            feedbackArea.append("Correct Positions: " + feedback.correctPosition + ", Correct Colors: " + feedback.correctColor + "\n");

            if (feedback.correctPosition == 4) {
                JOptionPane.showMessageDialog(frame, "Congratulations! You guessed the code!");
                frame.dispose();
            }
        }
    }

    private Feedback checkGuess(ArrayList<String> guess) {
        int correctPosition = 0;
        int correctColor = 0;

        ArrayList<String> secretCopy = new ArrayList<>(secretCode);
        ArrayList<String> guessCopy = new ArrayList<>(guess);

        // Check for correct positions
        for (int i = 0; i < guess.size(); i++) {
            if (guess.get(i).equals(secretCopy.get(i))) {
                correctPosition++;
                secretCopy.set(i, null);
                guessCopy.set(i, null);
            }
        }

        // Check for correct colors
        for (String color : guessCopy) {
            if (color != null && secretCopy.contains(color)) {
                correctColor++;
                secretCopy.remove(color);
            }
        }

        return new Feedback(correctPosition, correctColor);
    }

    private record Feedback(int correctPosition, int correctColor) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MastermindGame::new);
    }
}
