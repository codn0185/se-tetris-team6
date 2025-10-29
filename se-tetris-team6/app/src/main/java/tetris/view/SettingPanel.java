package tetris.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tetris.domain.setting.Setting;

public class SettingPanel extends JPanel {
    // Exposed UI components for controller wiring
    public JComboBox<Setting.ScreenSize> sizeCombo;
    public JCheckBox colorBlindCheckbox;
    public JTextField keyMoveLeftField;
    public JTextField keyMoveRightField;
    public JTextField keyRotateField;
    public JTextField keySoftDropField;
    public JButton captureMoveLeftButton;
    public JButton captureMoveRightButton;
    public JButton captureRotateButton;
    public JButton captureSoftDropButton;
    public JButton resetScoresButton;
    public JButton resetDefaultsButton;
    public JButton saveButton;
    public JButton backToMainButton;

    public SettingPanel() {
        super(new GridBagLayout());
        setSize(TetrisFrame.FRAME_SIZE);
        setBackground(Color.LIGHT_GRAY);
        setOpaque(true);
        setVisible(false);

        // Screen size selector
        JPanel sizeRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sizeRow.setOpaque(false);
        sizeRow.add(new JLabel("Screen size:"));
        sizeCombo = new JComboBox<>(Setting.ScreenSize.values());
        sizeCombo.setPreferredSize(new Dimension(200, 24));
        sizeRow.add(sizeCombo);

        addToLayout(sizeRow, 0, 0, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.WEST);

        // Colorblind mode
        JPanel colorRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        colorRow.setOpaque(false);
        colorRow.add(new JLabel("Colorblind mode:"));
        colorBlindCheckbox = new JCheckBox();
        colorRow.add(colorBlindCheckbox);

        addToLayout(colorRow, 0, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.WEST);

        // Key bindings
        JPanel keysRow = new JPanel();
        keysRow.setLayout(new BoxLayout(keysRow, BoxLayout.Y_AXIS));
        keysRow.setOpaque(false);
        keysRow.add(new JLabel("Key bindings (use key names, e.g. LEFT, RIGHT, UP, DOWN, SPACE):"));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.setOpaque(false);
        row1.add(new JLabel("Move Left:"));
        keyMoveLeftField = new JTextField(8);
        row1.add(keyMoveLeftField);
        captureMoveLeftButton = new JButton("Capture");
        row1.add(captureMoveLeftButton);
        row1.add(new JLabel("Move Right:"));
        keyMoveRightField = new JTextField(8);
        row1.add(keyMoveRightField);
        captureMoveRightButton = new JButton("Capture");
        row1.add(captureMoveRightButton);
        keysRow.add(row1);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.setOpaque(false);
        row2.add(new JLabel("Rotate:"));
        keyRotateField = new JTextField(8);
        row2.add(keyRotateField);
        captureRotateButton = new JButton("Capture");
        row2.add(captureRotateButton);
        row2.add(new JLabel("Soft Drop:"));
        keySoftDropField = new JTextField(8);
        row2.add(keySoftDropField);
        captureSoftDropButton = new JButton("Capture");
        row2.add(captureSoftDropButton);
        keysRow.add(row2);

        addToLayout(keysRow, 0, 2, 1, 2, GridBagConstraints.BOTH, GridBagConstraints.WEST);

        // Action buttons
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.setOpaque(false);
        resetScoresButton = new JButton("Reset Scoreboard");
        resetDefaultsButton = new JButton("Reset to Defaults");
        saveButton = new JButton("Save Settings");
        backToMainButton = new JButton("Back to Menu");
        actions.add(resetScoresButton);
        actions.add(resetDefaultsButton);
        actions.add(saveButton);
        actions.add(backToMainButton);

        addToLayout(keysRow, 0, 2, 1, 2, GridBagConstraints.BOTH, GridBagConstraints.WEST);

        // install capture behavior: controller will attach listeners, but provide a
        // default capturing helper
        installCaptureHandlers();
    }

    private void addToLayout(Component comp, int x, int y, int w, int h, int fill, int anchor) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;

        gbc.weightx = gbc.gridwidth;
        gbc.weighty = gbc.gridheight;

        gbc.fill = fill;
        gbc.anchor = anchor;
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.ipadx = 5;
        gbc.ipady = 5;

        this.add(comp, gbc);
    }

    private void installCaptureHandlers() {
        // Generic capture installer
        installCapture(captureMoveLeftButton, keyMoveLeftField);
        installCapture(captureMoveRightButton, keyMoveRightField);
        installCapture(captureRotateButton, keyRotateField);
        installCapture(captureSoftDropButton, keySoftDropField);
    }

    private void installCapture(JButton button, JTextField targetField) {
        button.addActionListener(e -> {
            button.setText("Press a key...");
            button.setEnabled(false);
            // prevent the text field from accepting KEY_TYPED events while capturing
            boolean prevEditable = targetField.isEditable();
            targetField.setEditable(false);
            java.awt.KeyEventDispatcher dispatcher = new java.awt.KeyEventDispatcher() {
                @Override
                public boolean dispatchKeyEvent(java.awt.event.KeyEvent evt) {
                    // If a key was pressed, capture it and stop dispatching further key events.
                    if (evt.getID() == java.awt.event.KeyEvent.KEY_PRESSED) {
                        int code = evt.getKeyCode();
                        targetField.setText(tetris.util.KeyMapper.keyCodeToName(code));
                        // consume and prevent subsequent KEY_TYPED being delivered
                        evt.consume();
                        // restore button and field state
                        button.setText("Capture");
                        button.setEnabled(true);
                        targetField.setEditable(prevEditable);
                        java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
                        return true; // consumed
                    }
                    // Also consume any KEY_TYPED events while capturing to avoid stray input
                    if (evt.getID() == java.awt.event.KeyEvent.KEY_TYPED) {
                        evt.consume();
                        return true;
                    }
                    return false;
                }
            };

            java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(dispatcher);
        });
    }
}
