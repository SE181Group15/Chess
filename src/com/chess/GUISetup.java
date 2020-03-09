package com.chess;

import com.chess.Game.ChessGame;
import com.chess.GameObservers.GUIGamePrinter;
import com.chess.GameObservers.GUITurnPrinter;
import com.chess.Players.ChessPlayer;
import com.chess.Players.OnlineChessPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings({"SameReturnValue", "Duplicates"})
public class GUISetup {
    private static List<Image> icons;
    private static JFrame frame;
    public static Component currentComponent;
    private static final float titleFontSize = 40f;
    private static ImageIcon createOptionsImage;
    private static ImageIcon onePlayerOptionsImage;
    private static ImageIcon twoPlayerOptionsImage;
    private static ImageIcon clickPieceImage;

    private static final ActionListener easyAction = e -> {
        Settings.yourColor = Settings.p1Color;
        Settings.reverseOrder = false;
        currentComponent = switchToGame("PE");
    };

    private static final ActionListener mediumAction = e -> {
        Settings.yourColor = Settings.p1Color;
        Settings.reverseOrder = false;
        currentComponent = switchToGame("PM");
    };

    private static final ActionListener hardAction = e -> {
        Settings.yourColor = Settings.p1Color;
        Settings.reverseOrder = false;
        currentComponent = switchToGame("PH");
    };

    private static final ActionListener localAction = e -> {
        Settings.yourColor = null;
        Settings.reverseOrder = false;
        currentComponent = switchToGame("PP");
    };

    private static final ActionListener createAction = e -> {
        Settings.yourColor = Settings.p1Color;
        Settings.reverseOrder = false;
        Settings.gameId = UUID.randomUUID().toString();
        OnlineChessPlayer.createGame(Settings.gameId);
        switchToWaiting();
    };

    private static final ActionListener joinAction = e -> {
        Settings.yourColor = Settings.p2Color;
        Settings.reverseOrder = true;
        switchToJoinGame();
    };

    public GUISetup() {
        frame = new JFrame();
        frame.setTitle("Classic Chess");
        icons = new ArrayList<>();
        int imageWidth = 200;
        int imageHeight = 200;
        icons.add(new ImageIcon(getClass().getResource("/com/chess/Assets/pawn.png")).getImage());
        createOptionsImage = scaleImage(new ImageIcon(getClass().getResource("/com/chess/Assets/createOptions.JPG")), imageWidth, imageHeight);
        onePlayerOptionsImage = scaleImage(new ImageIcon(getClass().getResource("/com/chess/Assets/onePlayerOptions.JPG")), imageWidth, imageHeight);
        twoPlayerOptionsImage = scaleImage(new ImageIcon(getClass().getResource("/com/chess/Assets/twoPlayerOptions.JPG")), imageWidth, imageHeight);
        clickPieceImage = scaleImage(new ImageIcon(getClass().getResource("/com/chess/Assets/pieceSelection.png")), imageWidth, imageHeight);
        frame.setIconImages(icons);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setMinimumSize(new Dimension(850, 820));
        currentComponent = buildMainMenuComponents();
        frame.add(currentComponent);

        //Create MenuBar
        JMenuBar menuBar = new JMenuBar();

        //Create File Menu
        JMenu fileMenu = new JMenu("File");

        //Main Menu
        JMenuItem mainMenuMenuItem = new JMenuItem("Main Menu");
        mainMenuMenuItem.addActionListener(e -> switchToMainMenu());
        //One Player Menu
        JMenu onePlayerMenu = new JMenu("1 Player");
        JMenuItem easyMenuItem = new JMenuItem("Easy");
        easyMenuItem.addActionListener(easyAction);
        JMenuItem mediumMenuItem = new JMenuItem("Medium");
        mediumMenuItem.addActionListener(mediumAction);
        JMenuItem hardMenuItem = new JMenuItem("Hard");
        hardMenuItem.addActionListener(hardAction);
        onePlayerMenu.add(easyMenuItem);
        onePlayerMenu.add(mediumMenuItem);
        onePlayerMenu.add(hardMenuItem);

        //Two Player Menu
        JMenu twoPlayerMenu = new JMenu("2 Player");
        JMenuItem localMenuItem = new JMenuItem("Local");
        localMenuItem.addActionListener(localAction);
        JMenuItem createMenuItem = new JMenuItem("Create Online");
        createMenuItem.addActionListener(createAction);
        JMenuItem joinMenuItem = new JMenuItem("Join Online");
        joinMenuItem.addActionListener(joinAction);
        twoPlayerMenu.add(localMenuItem);
        twoPlayerMenu.add(createMenuItem);
        twoPlayerMenu.add(joinMenuItem);

        //Exit Button
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));

        //Build final file menu and add to bar
        fileMenu.add(mainMenuMenuItem);
        fileMenu.add(onePlayerMenu);
        fileMenu.add(twoPlayerMenu);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        //Create Help Menu
        JMenu helpMenu = new JMenu("Help");

        JMenuItem helpMenuItem = new JMenuItem("Help");
        helpMenuItem.addActionListener(e -> switchToHelp());

        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(e -> switchToAbout());

        JMenuItem rulesMenuItem = new JMenuItem("Rules");
        rulesMenuItem.addActionListener(e -> {
            try {
                Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                    try {
                        desktop.browse(new URI("https://en.wikipedia.org/wiki/Rules_of_chess"));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (Exception ignored) {

            }
        });
        //Build final help menu and add to bar
        helpMenu.add(helpMenuItem);
        helpMenu.add(rulesMenuItem);
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);

        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);
    }

    private ImageIcon scaleImage(ImageIcon icon, int w, int h)
    {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();

        if(icon.getIconWidth() > w)
        {
            nw = w;
            nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }

        if(nh > h)
        {
            nh = h;
            nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }

        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }

    private static Component switchToGame(String key) {
        ChessPlayer[] players = ChessPlayerFactory.buildPlayers(key);
        ChessGame game = new ChessGame(players[0], players[1]);
        for (ChessPlayer p : players) {
            game.attachObserver(p);
        }
        Component chessBoardComponent = buildGameComponents(game);
        switchTo(chessBoardComponent);
        game.play();
        return chessBoardComponent;
    }

    public static Component switchToMainMenu() {
        Component mainMenuComponent = buildMainMenuComponents();
        switchTo(mainMenuComponent);
        return mainMenuComponent;
    }

    private static void switchToWaiting() {
        Component waitingScreenComponent = buildWaitingScreenComponents();
        JDialog waiting = new JDialog();
        waiting.setMinimumSize(new Dimension(600, 300));
        waiting.setIconImages(icons);
        waiting.setTitle("Creating Online Game");
        waiting.setResizable(false);
        waiting.add(waitingScreenComponent);
        waiting.setModal(true);

        waiting.pack();
        waiting.setVisible(true);
    }

    private static void switchTo(Component newComponent) {
        frame.remove(currentComponent);
        frame.add(newComponent);
        frame.pack();
        frame.repaint();
        frame.setVisible(true);
        currentComponent = newComponent;
    }
    private static Timer timer;

    private static Component buildWaitingScreenComponents() {
        JPanel container = new JPanel(new GridBagLayout());
        JPanel waitingScreen = new JPanel(new GridLayout(3,1));
        JLabel label = new JLabel("Your Game Code is:", SwingConstants.LEFT);
        label.setFont(label.getFont().deriveFont(20.0f));

        //ID
        JPanel idPanel = new JPanel();
        JTextField gameIDField = new JTextField(25);
        gameIDField.setText(Settings.gameId);
        gameIDField.setEditable(false);
        gameIDField.setBackground(Color.WHITE);
        gameIDField.setFont(gameIDField.getFont().deriveFont(20f));
        JButton copyButton = new JButton("Copy");
        copyButton.addActionListener(e -> {
            StringSelection stringSelection = new StringSelection(Settings.gameId);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });
        idPanel.add(gameIDField);
        idPanel.add(copyButton);

        String startLabel = "Waiting for second player";
        JLabel waitingLabel = new JLabel(startLabel, SwingConstants.LEFT);
        waitingLabel.setFont(waitingLabel.getFont().deriveFont(20.0f));
        waitingScreen.add(label);
        waitingScreen.add(idPanel);
        waitingScreen.add(waitingLabel);
        Timer waitingLabelTimer = new Timer(500, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                waitingLabel.setText(waitingLabel.getText() + ".");
                if (waitingLabel.getText().equals(startLabel + "....")) {
                    waitingLabel.setText(startLabel);
                }
            }
        });
        waitingLabelTimer.start();

        timer = new Timer(100, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Objects.requireNonNull(OnlineChessPlayer.getLatestLine(Settings.gameId)).contains("Joined the Game")) {
                    JDialog dialog = (JDialog) SwingUtilities.getRoot(container);
                    dialog.dispose();
                    currentComponent = switchToGame("LR");
                    timer.stop();
                    waitingLabelTimer.stop();
                }
            }
        });
        timer.start();
        container.add(waitingScreen);
        return container;
    }

    private static Component buildGameComponents(ChessGame game) {
        JPanel gameFrame = new JPanel(new BorderLayout());

        GUIGamePrinter boardPrinter = new GUIGamePrinter();
        boardPrinter.addMouseListener(new ChessClickListener(boardPrinter));
        game.attachObserver(boardPrinter);
        gameFrame.add(boardPrinter, BorderLayout.CENTER);
        GUITurnPrinter turnPrinter = new GUITurnPrinter(boardPrinter);
        game.attachObserver(turnPrinter);

        gameFrame.add(turnPrinter, BorderLayout.SOUTH);

        return gameFrame;
    }

    private static Component buildMainMenuComponents() {
        JPanel mainMenu = new JPanel(new BorderLayout(100, 20));
        JPanel playTypes = new JPanel(new GridLayout(2, 1, 0, 20));
        JButton onePlayer = new JButton("1 Player");
        JButton twoPlayer = new JButton("2 Player");
        Font buttonFont = new Font("Arial", Font.PLAIN, (int)(frame.getSize().width * .2));
        onePlayer.setFont(buttonFont);
        twoPlayer.setFont(buttonFont);
        onePlayer.addActionListener(e -> switchToOnePlayer());
        twoPlayer.addActionListener(e -> switchToTwoPlayer());
        playTypes.add(onePlayer);
        playTypes.add(twoPlayer);
        playTypes.setMaximumSize(new Dimension((int) (frame.getSize().width * .5), (int)(frame.getSize().height * .8)));
        JLabel title = new JLabel("CLASSIC CHESS", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(titleFontSize));
        mainMenu.add(title, BorderLayout.NORTH);
        mainMenu.add(new JPanel(), BorderLayout.EAST);
        mainMenu.add(new JPanel(), BorderLayout.WEST);
        mainMenu.add(playTypes, BorderLayout.CENTER);
        mainMenu.setBorder(BorderFactory.createEtchedBorder());
        return mainMenu;
    }

    private static Component buildOnePlayerComponents() {
        JPanel menu = new JPanel(new BorderLayout(100, 20));
        JPanel playTypes = new JPanel(new GridLayout(3, 1, 0, 20));
        JButton easy = new JButton("Easy");
        JButton medium = new JButton("Medium");
        JButton hard = new JButton("Hard");
        JButton back = new JButton("< Back");
        Font buttonFont = new Font("Arial", Font.PLAIN, (int)(frame.getSize().width * .2));
        easy.setFont(buttonFont);
        medium.setFont(buttonFont);
        hard.setFont(buttonFont);
        easy.addActionListener(easyAction);
        medium.addActionListener(mediumAction);
        hard.addActionListener(hardAction);
        back.addActionListener(e -> currentComponent = switchToMainMenu());

        playTypes.add(easy);
        playTypes.add(medium);
        playTypes.add(hard);

        JLabel title = new JLabel("One Player", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(titleFontSize));
        menu.add(title, BorderLayout.NORTH);
        menu.add(new JPanel(), BorderLayout.EAST);
        menu.add(new JPanel(), BorderLayout.WEST);
        menu.add(playTypes, BorderLayout.CENTER);
        back.setHorizontalAlignment(SwingConstants.LEFT);
        menu.add(back, BorderLayout.SOUTH);
        menu.setBorder(BorderFactory.createEtchedBorder());
        return menu;
    }

    private static Component buildTwoPlayerComponents() {
        JPanel menu = new JPanel(new BorderLayout(100, 20));
        JPanel playTypes = new JPanel(new GridLayout(3, 1, 0, 20));
        JButton local = new JButton("Local");
        JButton create = new JButton("Create");
        JButton join = new JButton("Join");
        JButton back = new JButton("< Back");
        local.setFont(new Font("Arial", Font.PLAIN, (int)(frame.getSize().width * .2)));
        create.setFont(new Font("Arial", Font.PLAIN, (int)(frame.getSize().width * .2)));
        join.setFont(new Font("Arial", Font.PLAIN, (int)(frame.getSize().width * .2)));
        local.addActionListener(localAction);
        create.addActionListener(createAction);
        join.addActionListener(joinAction);
        back.addActionListener(e -> currentComponent = switchToMainMenu());

        playTypes.add(local);
        playTypes.add(create);
        playTypes.add(join);

        JLabel title = new JLabel("Two Player", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(titleFontSize));

        menu.add(title, BorderLayout.NORTH);
        menu.add(new JPanel(), BorderLayout.EAST);
        menu.add(new JPanel(), BorderLayout.WEST);
        menu.add(playTypes, BorderLayout.CENTER);
        back.setHorizontalAlignment(SwingConstants.LEFT);
        menu.add(back, BorderLayout.SOUTH);
        menu.setBorder(BorderFactory.createEtchedBorder());
        return menu;
    }

    private static Component buildJoinGameComponents() {
        JPanel container = new JPanel(new GridBagLayout());
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JLabel label = new JLabel("Enter your Game Code Below:", SwingConstants.LEFT);
        label.setFont(label.getFont().deriveFont(20.0f));

        JPanel idPanel = new JPanel(new FlowLayout());
        JTextField gameIDField = new JTextField(25);
        gameIDField.setBackground(Color.WHITE);
        gameIDField.setFont(gameIDField.getFont().deriveFont(20f));
        gameIDField.setSize(400,100);
        JButton joinButton = new JButton("Join");
        joinButton.setEnabled(false);
        gameIDField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if(gameIDField.getText().length() > 0)
                    joinButton.setEnabled(true);
                else
                    joinButton.setEnabled(false);
            }
        });
        joinButton.addActionListener(e -> {
            Settings.gameId = gameIDField.getText();
            System.out.println(Settings.gameId);
            try {
                UUID code = UUID.fromString(Settings.gameId);
                OnlineChessPlayer.joinGame(Settings.gameId);
                Component component = (Component) e.getSource();
                JDialog dialog = (JDialog) SwingUtilities.getRoot(component);
                dialog.dispose();
                switchToGame("RL");
            } catch (Exception err) {
                JOptionPane.showMessageDialog(frame, "Invalid Game Id");
            }
        });
        idPanel.add(gameIDField);
        idPanel.add(joinButton);
        panel.add(label);
        panel.add(idPanel);
        container.add(panel);
        return container;
    }

    private static Component buildHelpComponents() {
        int rows = 4;
        JPanel container = new JPanel(new GridLayout(rows, 2));
        JTextArea[] helpText = new JTextArea[rows];
        helpText[0] = new JTextArea("When playing by yourself you can play against the computer with difficulties of Easy, Medium, and Hard.");
        helpText[1] = new JTextArea("When playing with two people you can play either Local with 1 computer or online with 2 computers.");
        helpText[2] = new JTextArea("If playing online 1 player should Create a game and share the code with the other who Joins the game.");
        helpText[3] = new JTextArea("On your turn you can move by clicking on one of your pieces and then clicking on a blue square that it can move to.");

        for (JTextArea jTextArea : helpText) {
            jTextArea.setLineWrap(true);
            jTextArea.setWrapStyleWord(true);
            jTextArea.setFont(jTextArea.getFont().deriveFont(20f));
            jTextArea.setOpaque(false);
            jTextArea.setBackground(new Color(0, 0, 0, 0));
            jTextArea.setEditable(false);
            jTextArea.setAlignmentY(50);
        }

        container.add(helpText[0]);
        try {
            JLabel onePlayerImage = new JLabel(onePlayerOptionsImage);
            container.add(onePlayerImage);
        } catch (Exception ignored) {

        }
        container.add(helpText[1]);
        try {
            JLabel twoPlayerImage = new JLabel(twoPlayerOptionsImage);
            container.add(twoPlayerImage);
        } catch (Exception ignored) {

        }
        container.add(helpText[2]);
        try {
            JLabel createGameImage = new JLabel(createOptionsImage);
            container.add(createGameImage);
        } catch (Exception ignored) {

        }
        container.add(helpText[3]);
        try {
            JLabel img = new JLabel(clickPieceImage);
            container.add(img);
        } catch (Exception ignored) {

        }
        return container;
    }

    private static Component buildSettingsComponents() {
        //TODO
        return null;
    }

    private static Component buildAboutComponents() {
        JPanel container = new JPanel(new GridLayout(1, 2));
        JTextArea text = new JTextArea();
        text.setText("Classic Chess was created by Mohammad Asim Iqbal, Binh Tran, Carter Walsh, Christopher Xi, and Shivansh Suhane for SE181 \n\n" +
                "Version: " + Settings.version);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setFont(text.getFont().deriveFont(20f));
        text.setOpaque(false);
        text.setBackground(new Color(0, 0, 0, 0));
        text.setEditable(false);
        text.setAlignmentY(50);
        container.add(text);
        container.setPreferredSize(new Dimension(300, 300));
        return container;
    }

    private static void switchToOnePlayer() {
        Component components = buildOnePlayerComponents();
        switchTo(components);
    }

    private static void switchToTwoPlayer() {
        Component components = buildTwoPlayerComponents();
        switchTo(components);
    }

    private static void switchToJoinGame() {
        Component components = buildJoinGameComponents();
        JDialog join = new JDialog();
        join.setMinimumSize(new Dimension(600, 300));
        join.setIconImages(icons);
        join.setTitle("Joining Online Game");
        join.setResizable(false);
        join.setModal(true);
        join.add(components);


        join.pack();
        join.setVisible(true);
    }

    private static void switchToHelp() {
        Component helpComponent = buildHelpComponents();
        JDialog help = new JDialog();
        help.setMinimumSize(new Dimension(400, 800));
        help.setIconImages(icons);
        help.setTitle("Help");
        help.setModal(true);

        help.add(helpComponent);

        help.pack();
        help.setVisible(true);
    }

    @SuppressWarnings("unused")
    public static Component switchToSettings() {
        Component settingsComponents = buildSettingsComponents();
        switchTo(settingsComponents);
        return settingsComponents;
    }

    public static void switchToAbout() {
        Component aboutComponents = buildAboutComponents();
        JDialog about = new JDialog();
        about.setMinimumSize(new Dimension(800, 200));
        about.setIconImages(icons);
        about.setTitle("About");
        about.setModal(true);
        about.add(aboutComponents);

        about.pack();
        about.setVisible(true);
    }
}
