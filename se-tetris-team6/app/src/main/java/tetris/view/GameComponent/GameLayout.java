package tetris.view.GameComponent;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import tetris.domain.GameModel;
import tetris.view.EmptySpace;
import tetris.view.GamePanel;

public class GameLayout extends JPanel {
    private GamePanel gamePanel;
    private NextBlockPanel nextBlockPanel;
    private ScorePanel scoreboard;

    public GameLayout() {
        super(new GridBagLayout());
        setOpaque(true);
        setVisible(false);

        gamePanel = new GamePanel();
        addToRegion(gamePanel, 0, 0, 4, 4, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

        nextBlockPanel = new NextBlockPanel();
        addToRegion(nextBlockPanel, 4, 0, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

        scoreboard = new ScorePanel();
        addToRegion(scoreboard, 4, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH);

        addToRegion(new EmptySpace(), 4, 2, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

        gamePanel.setVisible(true);
        nextBlockPanel.setVisible(true);
        scoreboard.setVisible(true);

        revalidate();
        repaint();
    }

    public void addToRegion(Component comp, int x, int y, int w, int h, int fill, int anchor) {
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

    public void bindGameModel(GameModel model) {
        gamePanel.bindGameModel(model);
        nextBlockPanel.bindGameModel(model);
        scoreboard.bindGameModel(model);
        repaint();
    }
}
