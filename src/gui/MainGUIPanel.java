package gui;

import function.MusicPlayerFunction;
import function.validator.AudioFormatValidator;
import function.validator.DefaultAudioFormatValidator;
import custom_component.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Objects;

public class MainGUIPanel extends JPanel {
  // Arch of the Component var (final)
  private final int PANEL_ARCH = 20;
  private final int SLIDER_ARCH = 10;

  // UpperCompPanel Assets Directory
  private final String ADD_FOLDER_BUTTON = "src/resources/assets/Image/Simple Add Folder Button.png";
  private final String MIC_BUTTON = "src/resources/assets/Image/Mic Button.png";

  // ButtonComponentPanel Assets Directory
  private final String PREVIOUS_BUTTON_PATH = "src/resources/assets/Image/Prev Button.png";
  private final String NEXT_BUTTON_PATH = "src/resources/assets/Image/Next Button.png";
  private final String PLAY_BUTTON_PATH = "src/resources/assets/Image/Play Button.png";
  private final String PAUSE_BUTTON_PATH = "src/resources/assets/Image/Pause Button.png";

  // Draggable Component var
  private Point initialClick;
  private JFrame parent;
  private JPanel draggablePanel;

  // Song Slider Var
  private MusicPlayerFunction audioPlayer;
  private final AudioFormatValidator audioFormatValidator;
  private HalfRoundedPanel bgPanel;
  private CustomJSlider songSlider;
  private JLabel songTimeStamp;
  private JLabel songEnd;
  private String getAbsoluteSongPath = "";

  // Song Metadata Var
  private JLabel albumImage;
  private JLabel songTitle;
  private JLabel songArtist;

  // Main JPanel GroupLayout
  GroupLayout groupLay = new GroupLayout(this);
  JPanel bottomCompPanel;

  public JPanel upperCompPanel() {
    final Dimension TABPANEL_BUTTONSIZE = new Dimension(35, 35);

    // Add Folder Button
    OvalButton addFolderButton = new OvalButton(
        TABPANEL_BUTTONSIZE,
        ADD_FOLDER_BUTTON);
    addFolderButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    addFolderButton.setPreferredSize(TABPANEL_BUTTONSIZE);
    addFolderButton.setMaximumSize(TABPANEL_BUTTONSIZE);
    addFolderButton.addActionListener(e -> {
      JFileChooser directoryChooser = new JFileChooser();
      directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

      if (directoryChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        // Reset Song file and song directory
        if (!audioPlayer.getSongFileName().isEmpty()) {
          audioPlayer.stopAudio();
          enablePlayButtonDisablePauseButton();
          updatePlayBackSlider(0);

          // Check if the mp3file is updated or not
          if (audioPlayer.getMp3file() != null) {
            updateAudioTotalLength();
          }
        }

        File getDirectory = directoryChooser.getSelectedFile();
        getAbsoluteSongPath = getDirectory.getAbsolutePath();
        audioPlayer.setSongFileName(getAbsoluteSongPath);

        // System.out.println("Successfull to get the song directory ...");
        // System.out.println("Song Path: " + audioPlayer.getSongFileName());
      } else {
        System.out.println("Failed to get the song directory ...");
      }
    });

    // Speech to text button
    OvalToggleButton speechToTextToggle = new OvalToggleButton(
        TABPANEL_BUTTONSIZE,
        MIC_BUTTON);
    speechToTextToggle.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    speechToTextToggle.setMaximumSize(TABPANEL_BUTTONSIZE);
    speechToTextToggle.setPreferredSize(TABPANEL_BUTTONSIZE);
    speechToTextToggle.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Turn on the speechToTextToggle
        if (speechToTextToggle.isSelected()) {
          speechToTextToggle.setOpaque(true);
          speechToTextToggle.setBackground(new Color(61, 61, 61));
          speechToTextToggle.repaint();

          System.out.println("toggle on\n\n");
        } else {
          speechToTextToggle.setOpaque(false);
          speechToTextToggle.setBackground(new Color(0, 0, 0, 0));
          speechToTextToggle.repaint();

          System.out.println("toggle off\n\n");
        }
      }
    });
    // TabPanel (Main Panel > Sub Panel > Sub-Sub Panel)
    JPanel tabPanel = new JPanel();
    tabPanel.setLayout(new BoxLayout(tabPanel, BoxLayout.Y_AXIS));
    tabPanel.setBorder(BorderFactory.createEmptyBorder(50, 5, 0, 5));
    tabPanel.setMaximumSize(new Dimension(46, 400)); // 120 width
    tabPanel.setPreferredSize(new Dimension(46, 400)); // 170 heigh
    tabPanel.setBackground(new Color(31, 31, 31));
    // Tab Panel Alignment
    tabPanel.add(addFolderButton);
    tabPanel.add(Box.createVerticalStrut(10));
    tabPanel.add(speechToTextToggle);

    // MusicPlayerLogo
    JLabel musicPlayerLogo = new JLabel("") {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Image icon = new ImageIcon(PLAY_BUTTON_PATH).getImage();
        g2.drawImage(icon, 4, 4, 22, 22, null);
      }
    };
    musicPlayerLogo.setMaximumSize(new Dimension(500, 30));

    // Music Player Label
    JLabel musicPlayerLabel = new JLabel("Music Player");
    musicPlayerLabel.setForeground(Color.WHITE);
    musicPlayerLabel.setFont(setCustomFont(12));

    // Hide Button
    NormalButton hideButton = new NormalButton(
        "—",
        new Color(0, 0, 0, 0),
        new Color(0, 55, 97),
        new Color(0, 43, 76));
    hideButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    hideButton.setMaximumSize(new Dimension(50, 40));
    hideButton.setPreferredSize(new Dimension(50, 40));
    hideButton.setFont(setCustomFont(10));
    hideButton.addActionListener(e -> {
      hideAllComponent(this);
      parent.setExtendedState(JFrame.ICONIFIED);
    });

    // Exit Button
    NormalButton exitButton = new NormalButton(
        "X",
        new Color(0, 0, 0, 0),
        new Color(179, 23, 23),
        new Color(113, 11, 11));
    exitButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    exitButton.setMaximumSize(new Dimension(50, 40));
    exitButton.setPreferredSize(new Dimension(50, 40));
    exitButton.setFont(setCustomFont(15));
    exitButton.addActionListener(e -> {
      System.exit(0);
    });

    // Sub Panel 1 of "UpperPanelSub2" (Layer 5)
    draggablePanel = new JPanel();
    draggablePanel.setLayout(new BoxLayout(draggablePanel, BoxLayout.X_AXIS));
    draggablePanel.setMaximumSize(new Dimension(450, 40)); // 900 width, 30 height
    draggablePanel.setPreferredSize(new Dimension(450, 40)); // 700 width, 30 height
    draggablePanel.setBackground(new Color(0, 28, 48));
    // add Component to Draggable Panel (Upper Sub Panel of Main-Panel)
    draggablePanel.add(musicPlayerLogo);
    draggablePanel.add(musicPlayerLabel);
    draggablePanel.add(Box.createHorizontalStrut(220));
    draggablePanel.add(hideButton);
    draggablePanel.add(exitButton);
    draggablePanel.add(Box.createHorizontalStrut(8));

    // Sub Panel 2 of "UpperPanelSub2" (Layer 5)
    bgPanel = new HalfRoundedPanel(PANEL_ARCH);
    bgPanel.setMaximumSize(new Dimension(300, 400)); // 300 width
    bgPanel.setPreferredSize(new Dimension(300, 100));
    bgPanel.setBackground(new Color(0, 28, 48));
    // bgPanel.setBackground(new Color(70, 28, 48));
    bgPanel.setLayout(new FlowLayout());

    // Song album image (Metadata)
    Dimension albumSize = new Dimension(265, 265);
    albumImage = new JLabel() {
      @Override
      protected void paintComponent(Graphics g) {
        // super.paintComponent(g); // Ensure the component gets painted properly
        ImageIcon image = audioPlayer.getSongLogoImage();

        if (image != null) {
          int newWidth = albumSize.width;
          int newHeight = albumSize.height;
          // int newHeight = (int) (newWidth * ((double) image.getIconHeight() /
          // image.getIconWidth()));

          if (newHeight > 265) {
            newHeight = this.getHeight();
            newWidth = (int) (newHeight * ((double) image.getIconWidth() / image.getIconHeight()));
          }

          Graphics2D g2d = (Graphics2D) g;
          g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          g2d.drawImage(image.getImage(), 0, 0, newWidth, newHeight, null);
        }
      }
    };
    albumImage.setMaximumSize(albumSize);
    albumImage.setPreferredSize(albumSize);
    // albumImage.setBounds(((bgPanel.getWidth() - albumSize.width) / 2) +
    // albumSize.width - 5, 20, 225, 225);

    // Song title (Metadata)
    Dimension songTitleDim = new Dimension(170, 20);
    songTitle = new JLabel(audioPlayer.getSongTitle());
    songTitle.setMaximumSize(songTitleDim);
    songTitle.setFont(setCustomFont(15));

    // Alignment bgPanel
    bgPanel.add(albumImage);
    bgPanel.add(songTitle);

    // UpperPanelSub2 (Main Panel > Sub Panel > Sub-Sub Panel)
    JPanel upperPanelSub2 = new JPanel();
    upperPanelSub2.setLayout(new BoxLayout(upperPanelSub2, BoxLayout.Y_AXIS));
    upperPanelSub2.setMaximumSize(new Dimension(453, 400)); // 0, v2 450
    upperPanelSub2.setPreferredSize(new Dimension(453, 400)); // gone
    upperPanelSub2.setBackground(new Color(0, 28, 48));
    // add component to upperPanelSub2
    upperPanelSub2.add(draggablePanel);
    upperPanelSub2.add(bgPanel);

    // "UpperPanel" of "CompPanel" (Layer 3)
    JPanel upperPanel = new JPanel();
    upperPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
    upperPanel.setMaximumSize(new Dimension(500, 379)); // height 620, v2 400
    upperPanel.setPreferredSize(new Dimension(500, 379));
    // upperPanel.setBackground(new Color(0, 28, 48));
    upperPanel.add(tabPanel);
    upperPanel.add(upperPanelSub2);

    return upperPanel;
  }

  private boolean isValidSongFolder() {
    File songFolder = new File(getAbsoluteSongPath);

    // System.out.println("songFolder.exists(): " + songFolder.exists());
    // System.out.println("songFolder.isDirectory(): " + songFolder.isDirectory());
    if (songFolder.exists() && songFolder.isDirectory()) {
      File[] songList = songFolder.listFiles();

      if (songList != null) {
        for (File file : songList) {
          if (audioFormatValidator.isSupported(file)) {
            return true;
          }
        }
      }
    }

    return false;
  }

  public MainGUIPanel(JFrame parent) {
    this(parent, new DefaultAudioFormatValidator());
  }

  public MainGUIPanel(JFrame parent, AudioFormatValidator audioFormatValidator) {
    this.parent = parent;
    this.audioFormatValidator = Objects.requireNonNull(audioFormatValidator);
    setLayout(groupLay);
    setOpaque(false);

    audioPlayer = new MusicPlayerFunction(this);

    // Song TimeStamp JLabel
    songTimeStamp = new JLabel("0:00:00");
    songTimeStamp.setFont(setCustomFont(12));
    songTimeStamp.setForeground(Color.WHITE);
    songTimeStamp.setBounds(14, 15, 90, 20);

    // Song PlayTime JSlider
    songSlider = new CustomJSlider(SLIDER_ARCH);
    songSlider.setValue(0);
    songSlider.setBounds(70, 10, 350, 35); // width and height will get overwrite
    songSlider.setBackground(new Color(112, 112, 112));
    songSlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        if (!songSlider.getValueIsAdjusting() && audioPlayer.getMp3file() != null) {
          songSlider.setForeground(new Color(89, 89, 89));

          // Enable PlayButton but disable PauseButton
          enablePauseButtonDisablePlayButton();

          audioPlayer.resumeAudio();
        } else {
          songSlider.setForeground(new Color(0, 125, 202));

          // Enable PlayButton but disable PauseButton
          enablePlayButtonDisablePauseButton();

          audioPlayer.setTempAudioPaused(audioPlayer.convertFrameToMilis(songSlider.getValue()));
          audioPlayer.pauseAudio();
          updatePlayBackSlider(songSlider.getValue());

          System.out.println("ChangeAble Slider Value (Milisecond): " + songSlider.getValue());
          System.out.println("Max Audio in frame                  : " + audioPlayer.getMp3file().getFrameCount());
        }
      }
    });

    // Song End TimeStamp JLabel
    songEnd = new JLabel("0:00:00");
    songEnd.setFont(setCustomFont(12));
    songEnd.setForeground(Color.WHITE);
    songEnd.setBounds(434, 15, 90, 20);

    // Previous button
    OvalButton prevButton = new OvalButton(new Dimension(35, 35), PREVIOUS_BUTTON_PATH);
    prevButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    prevButton.setMaximumSize(new Dimension(35, 70));
    prevButton.setBounds(180, 50, 35, 70);
    prevButton.addActionListener(e -> {
      if (!getAbsoluteSongPath.isEmpty() && isValidSongFolder()) {
        // Enable PauseButton but disable PlayButton
        enablePauseButtonDisablePlayButton();

        // Stop current song and play next song
        audioPlayer.stopAudio();
        audioPlayer.playPreviousSong();
        System.out.println("Play Prev Song ...");

        // Set SongSlider Max value and update End of the song Label
        updateAudioTotalLength();

        // Update song metadata
        audioPlayer.updateSongMetadata();
        updateMetadataLabel();

      } else {
        JOptionPane.showMessageDialog(
            this,
            "Please choose a valid song folder directory first",
            "Empty Song Folder",
            JOptionPane.INFORMATION_MESSAGE);
      }
    });

    // Play button
    OvalButton playButton = new OvalButton(new Dimension(60, 60), PLAY_BUTTON_PATH);
    playButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    playButton.setMaximumSize(new Dimension(60, 120));
    playButton.setBounds(215, 38, 60, 120);
    playButton.addActionListener(e -> {
      if (!getAbsoluteSongPath.isEmpty() && isValidSongFolder()) {
        // Enable PauseButton but disable PlayButton
        enablePauseButtonDisablePlayButton();

        // If Button Icon is play button then audio is played
        if (!audioPlayer.getIsAudioPaused()) {
          // Play Music
          audioPlayer.playNextSong();
          System.out.println("PLAYING ...");

          // Set SongSlider Max value and update End of the song Label
          updateAudioTotalLength();

          // Update song metadata
          audioPlayer.updateSongMetadata();
          updateMetadataLabel();
        }
        // If Button Icon is resume button then audio is resumed
        else {
          // Resume Music
          audioPlayer.resumeAudio();
          System.out.println("RESUMING ...");
        }
      } else {
        JOptionPane.showMessageDialog(
            this,
            "Please choose a valid song folder directory first",
            "Empty Song Folder",
            JOptionPane.INFORMATION_MESSAGE);
      }

    });

    // Pause button
    OvalButton pauseButton = new OvalButton(
        new Dimension(60, 60),
        PAUSE_BUTTON_PATH);
    pauseButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    pauseButton.setMaximumSize(new Dimension(60, 120));
    pauseButton.setBounds(215, 38, 60, 120);
    pauseButton.setVisible(false);
    pauseButton.setEnabled(false);
    pauseButton.addActionListener(e -> {
      System.out.println("audioPlayer.getSongFileName().isEmpty(): " + audioPlayer.getSongFileName().isEmpty());
      System.out.println("isValidSongFolder(): " + isValidSongFolder());
      if (!getAbsoluteSongPath.isEmpty() && isValidSongFolder()) {
        // Enable PlayButton but disable PauseButton
        enablePlayButtonDisablePauseButton();

        // Pause the audio
        audioPlayer.pauseAudio();
        System.out.println("PAUSING ...");
      } else {
        JOptionPane.showMessageDialog(
            this,
            "Please choose a valid song folder directory first",
            "Empty Song Folder",
            JOptionPane.INFORMATION_MESSAGE);
      }
    });

    // Next Button
    OvalButton nextButton = new OvalButton(new Dimension(35, 35), NEXT_BUTTON_PATH);
    nextButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    nextButton.setMaximumSize(new Dimension(35, 70));
    nextButton.setBounds(275, 50, 35, 70);
    nextButton.addActionListener(e -> {
      if (!getAbsoluteSongPath.isEmpty() && isValidSongFolder()) {
        // Enable PauseButton but disable PlayButton
        enablePauseButtonDisablePlayButton();

        // Stop current song and play next song
        audioPlayer.stopAudio();
        audioPlayer.playNextSong();
        System.out.println("Play Next Song ...");

        // Set SongSlider Max value and update End of the song Label
        updateAudioTotalLength();

        // Update song metadata
        audioPlayer.updateSongMetadata();
        updateMetadataLabel();
      } else {
        JOptionPane.showMessageDialog(
            this,
            "Please choose a valid song folder directory first",
            "Empty Song Folder",
            JOptionPane.INFORMATION_MESSAGE);
      }
    });

    // buttonComp Panel (Main Panel > Sub Panel)
    bottomCompPanel = new JPanel();
    bottomCompPanel.setLayout(null);
    bottomCompPanel.setMaximumSize(new Dimension(500, 111)); // 230, v2 100
    bottomCompPanel.setPreferredSize(new Dimension(500, 111)); // gone
    bottomCompPanel.setBackground(new Color(38, 38, 38));
    // add ButtonCompPanel Component
    bottomCompPanel.add(songTimeStamp);
    bottomCompPanel.add(songSlider);
    bottomCompPanel.add(songEnd);
    bottomCompPanel.add(prevButton);
    bottomCompPanel.add(playButton);
    bottomCompPanel.add(pauseButton);
    bottomCompPanel.add(nextButton);

    // CompPanel (Main Panel)
    RoundedPanel compPanel = new RoundedPanel(PANEL_ARCH);
    compPanel.setLayout(new BoxLayout(compPanel, BoxLayout.Y_AXIS));
    // Add CompPanel Component
    compPanel.add(upperCompPanel());
    compPanel.add(bottomCompPanel);

    // This Main Panel Alignment
    groupLay.setHorizontalGroup(
        groupLay.createSequentialGroup()
            .addComponent(compPanel, GroupLayout.PREFERRED_SIZE, 500, GroupLayout.PREFERRED_SIZE));
    groupLay.setVerticalGroup(
        groupLay.createSequentialGroup()
            .addComponent(compPanel, GroupLayout.PREFERRED_SIZE, 500, GroupLayout.PREFERRED_SIZE));
    this.add(compPanel);

    // Draggable JPanel Property
    addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        // DEBUG
        System.out.println(findComponentAt(MainGUIPanel.this, e.getPoint(), draggablePanel));

        Component clickedComp = findComponentAt(MainGUIPanel.this, e.getPoint(), draggablePanel);
        if (clickedComp != null && clickedComp.equals(draggablePanel)) {
          initialClick = e.getPoint();
        }
      }
    });
    addMouseMotionListener(new MouseMotionAdapter() {
      @Override
      public void mouseDragged(MouseEvent e) {
        Component clickedComp = findComponentAt(MainGUIPanel.this, e.getPoint(), draggablePanel);
        if (clickedComp != null && clickedComp.equals(draggablePanel)) {
          // get location of Window (Frame)
          int thisX = parent.getLocation().x;
          int thisY = parent.getLocation().y;

          // Determine how much the mouse moved since the initial click (Mouse)
          int xMoved = e.getX() - initialClick.x;
          int yMoved = e.getY() - initialClick.y;

          // Move window to this position
          int targetX = thisX + xMoved;
          int targetY = thisY + yMoved;
          parent.setLocation(targetX, targetY);
        }
      }
    });

    setVisible(true);
  }

  // Update the metadata label component
  public void updateMetadataLabel() {
    albumImage.setIcon(audioPlayer.getSongLogoImage());
    songTitle.setText(limitLabelText(songTitle, 215));
  }

  private String limitLabelText(JLabel label, int maxWidth) {
    FontMetrics labelMetrics = label.getFontMetrics(label.getFont());
    String truncatedText = audioPlayer.getSongTitle();

    if (labelMetrics.stringWidth(audioPlayer.getSongTitle()) > maxWidth) {
      // for(int i = 0; i < truncatedText.length() - 1; i++) {
      for (int i = truncatedText.length() - 1; i >= 0; i--) {
        truncatedText = audioPlayer.getSongTitle().substring(0, i) + "...";
        System.out.println("Truncated Index: " + i);
        if (labelMetrics.stringWidth(truncatedText) <= maxWidth) {
          break;
        }
      }
    }

    return truncatedText;
  }

  private Component findComponentAt(Component parent, Point point, Component targetComponent) {
    if (parent instanceof Container) {
      Container container = (Container) parent;
      for (Component component : container.getComponents()) {
        Point componentPoint = SwingUtilities.convertPoint(parent, point, component);
        if (component.contains(componentPoint)) {
          Component foundComponent = findComponentAt(component, componentPoint, targetComponent);
          if (foundComponent != null) {
            return foundComponent.equals(targetComponent) ? foundComponent : targetComponent;
          }

          return component.equals(targetComponent) ? component : null;
        }
      }
    }
    return null;
  }

  public void hideAllComponent(Container parent) {
    for (Component comp : parent.getComponents()) {
      setVisible(false);
      if (comp instanceof Container) {
        hideAllComponent((Container) comp);
      }
    }
  }

  // Enable Pause Button adn Disable Play Button
  private void enablePauseButtonDisablePlayButton() {
    JButton getPlayButton = (JButton) bottomCompPanel.getComponent(4);
    JButton getPauseButton = (JButton) bottomCompPanel.getComponent(5);

    getPlayButton.setVisible(false);
    getPlayButton.setEnabled(false);

    getPauseButton.setVisible(true);
    getPauseButton.setEnabled(true);
  }

  // Enable Play Button and Disable Pause Button
  private void enablePlayButtonDisablePauseButton() {
    JButton getPlayButton = (JButton) bottomCompPanel.getComponent(4);
    JButton getPauseButton = (JButton) bottomCompPanel.getComponent(5);

    getPlayButton.setVisible(true);
    getPlayButton.setEnabled(true);

    getPauseButton.setVisible(false);
    getPauseButton.setEnabled(false);
  }

  // Update Audio TimeStamp JSlider both visually and functionally
  public void updatePlayBackSlider(int songSliderCurValue) {
    songSlider.setValue(songSliderCurValue);
    songTimeStamp.setText(audioPlayer.getCurTimeStampAsString());
  }

  // Set SongSlider Max value and update End of the song Label
  public void updateAudioTotalLength() {
    songSlider.setMaximum(audioPlayer.getMp3file().getFrameCount());
    songEnd.setText(audioPlayer.getAudioLengthAsString());
  }

  // Setting a Custom Font
  public Font setCustomFont(int size) {
    return new Font(Font.SANS_SERIF, Font.BOLD, size);
  }
}
