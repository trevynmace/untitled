package workbench;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class MainMenu
{
    private static final String OPTIONS_PANEL = "Options Panel"; //$NON-NLS-1$
    private static final String MENU_PANEL = "Menu Panel"; //$NON-NLS-1$
    private static final int FRAME_WIDTH = 500;
    private static final int FRAME_HEIGHT = 500;

    JFrame frame;
    JPanel panel;
    private JPanel menuPanel;
    private JPanel optionsPanel;
    private JButton startGame;
    private JButton options;
    private JButton quitGame;

    private int framePosX;
    private int framePosY;

    void setup()
    {
        setupFrame();
        setupButtons();
        setupPanels();

        frame.add(panel);
        show(MENU_PANEL);
        panel.setVisible(true);
        frame.setVisible(true);
    }

    private void setupFrame()
    {
        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setShape(new RoundRectangle2D.Double(1, 1, 498, 498, 30, 30));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLocation(findBestLocation());
        frame.setResizable(false);
        frame.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                setFramePosX(e.getX());
                setFramePosY(e.getY());
            }
        });
        frame.addMouseMotionListener(new MouseAdapter()
        {
            @Override
            public void mouseDragged(MouseEvent evt)
            {
                //sets frame position when mouse dragged
                frame.setLocation(evt.getXOnScreen() - getFramePosX(), evt.getYOnScreen() - getFramePosY());
            }
        });
    }

    /**
     *  Get half of the width of screen minus half of the width of the frame
     *
     *  Resolution of 1000 x 800
     *  i.e. (1000 / 2) - (500 / 2) = 250 as starting horizontal location
     *       (800 / 2) - (500 / 2) = 150 as starting vertical location
     *
     *  Resolution of 1440 x 900
     *  i.e. (1440 / 2) - (500 / 2) = 470 as starting horizontal location
     *       (900 / 2) - (500 / 2) = 200 as starting vertical location
     *
     * @return a Point value for the location of the frame
     */
    private Point findBestLocation()
    {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();

        int idealHorizontalLocation = (width / 2) - (FRAME_WIDTH / 2);
        int idealVerticalLocation = (height / 2) - (FRAME_HEIGHT / 2);
        return new Point(idealHorizontalLocation, idealVerticalLocation);
    }

    private void setupButtons()
    {
        Font blakeFont = null;
        try
        {
            blakeFont = Font.createFont(Font.TRUETYPE_FONT, new File("./fonts/blake2.ttf")); //$NON-NLS-1$
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        startGame = new JButton("Start"); //$NON-NLS-1$
        options = new JButton("Options"); //$NON-NLS-1$
        quitGame = new JButton("Quit"); //$NON-NLS-1$

        if (blakeFont != null)
        {
            startGame.setFont(blakeFont.deriveFont(Font.BOLD, 20f));
            options.setFont(blakeFont.deriveFont(Font.BOLD, 20f));
            quitGame.setFont(blakeFont.deriveFont(Font.BOLD, 20f));
        }

        Border emptyBorder = BorderFactory.createEmptyBorder();
        startGame.setBorder(emptyBorder);
        options.setBorder(emptyBorder);
        quitGame.setBorder(emptyBorder);

        addButtonActions();
    }

    private void setupPanels()
    {
        panel = new JPanel(new CardLayout());
        panel.setBackground(new Color(0, 135, 85));

        menuPanel = new JPanel();
        menuPanel.setBackground(new Color(0, 135, 85));
        menuPanel.add(startGame);
        menuPanel.add(options);
        menuPanel.add(quitGame);

        optionsPanel = new JPanel();
        optionsPanel.setBackground(new Color(0, 135, 85));
        addToOptionsPanel();

        panel.add(menuPanel, MENU_PANEL);
        panel.add(optionsPanel, OPTIONS_PANEL);
    }

    private void addToOptionsPanel()
    {
        optionsPanel.removeAll();

        JTextArea area = new JTextArea();
        area.setBackground(new Color(0, 0, 0, 0));
        area.setText("Blah"); //$NON-NLS-1$
        optionsPanel.add(area);
        area.setVisible(true);

        JButton backButton = new JButton("Back"); //$NON-NLS-1$
        backButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                show(MENU_PANEL);
            }
        });

        optionsPanel.add(backButton);

        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    private void addButtonActions()
    {
        startGame.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                frame.dispose();
                InitGame game = new InitGame();
                game.init();
            }
        });
        options.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                show(OPTIONS_PANEL);
            }
        });
        quitGame.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (JOptionPane.showConfirmDialog(frame, "Are you sure you want to quit?", "Quit?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) //$NON-NLS-1$ //$NON-NLS-2$
                {
                    frame = null;
                    System.exit(0);
                }
            }
        });
    }

    void show(String panelToShow)
    {
        CardLayout cl = (CardLayout)panel.getLayout();
        cl.show(panel, panelToShow);
    }

    public int getFramePosX()
    {
        return framePosX;
    }

    public void setFramePosX(int pos)
    {
        framePosX = pos;
    }

    public int getFramePosY()
    {
        return framePosY;
    }

    public void setFramePosY(int pos)
    {
        framePosY = pos;
    }
}