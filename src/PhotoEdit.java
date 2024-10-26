import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;

public class PhotoEdit extends JFrame{
    
    String urlPhoto;
    String namePhoto;
    int height, width;
    BufferedImage myPicture;
    
    /*Barra menu*/
    JMenuBar barra = new JMenuBar();
    JMenu edit = new JMenu("Изменить");
    JMenuItem open = new JMenuItem("Открыть");
    JMenuItem save = new JMenuItem("Сохранить");
    JMenuItem exit = new JMenuItem("Выход");


    JPanel panel = new JPanel();
    JLabel label = new JLabel();

    JTextField textArea, fontField, sizeField, colorField, xField, yField;

    public PhotoEdit(){
        setTitle("Редактор фото by Мешков");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setJMenuBar(barra);
        barra.add(edit);
        edit.add(open);
        edit.add(save);
        edit.add(exit);

        menuListener menuListener = new menuListener();
        open.addActionListener(menuListener);
        save.addActionListener(menuListener);
        exit.addActionListener(menuListener);


        // панель настроект
        sidePanel();
        add(panel);

        // Добавление JLabel на JFrame
        getContentPane().add(label);
        setLayout(new FlowLayout());



        setSize(640, 640);
        setVisible(true);

    }

    private class menuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(open)) {
                JFileChooser abridgeArchive = new JFileChooser();
                abridgeArchive.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int selection = abridgeArchive.showOpenDialog(null);

                if (selection == JFileChooser.APPROVE_OPTION) {
                    File f = abridgeArchive.getSelectedFile();

                    namePhoto = f.getName();
                    urlPhoto = f.getAbsolutePath();


                    setTitle(namePhoto + " [" + urlPhoto + "]");

                    ImageIcon imageIcon = new ImageIcon(urlPhoto);
                    // Создание объекта JLabel с картинкой внутри
                    height = imageIcon.getIconHeight();
                    width = imageIcon.getIconWidth();

                    label.setIcon(imageIcon);
                }
            }
            if (e.getSource().equals(save)) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int showSaveDialog = fileChooser.showSaveDialog(null);

                if (showSaveDialog == JFileChooser.APPROVE_OPTION) {
                    File f = fileChooser.getSelectedFile();

                    String path = f.getAbsolutePath();

                    if (f.exists()) {
                        if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(panel,
                                "Файл есть, меняем?",
                                "Перезаписать Файл", JOptionPane.YES_NO_OPTION)) {
                            setSave(path);
                        }
                    } else {
                        try {
                            f.createNewFile();
                            setSave(path);
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                }
            }
            if (e.getSource().equals(exit)) {
                System.exit(0);
            }
        }

    }
    private void setSave(String path){
        try {
            if(myPicture != null){
                ImageIO.write(myPicture, namePhoto.split("\\.")[1], new File(path));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void sidePanel() {
        panel.setLayout(new GridLayout(4, 6));


        JLabel textLabel = new JLabel("Text:");
        textArea = new JTextField();
        panel.add(textLabel);
        panel.add(textArea);

        JLabel fontLabel = new JLabel("Font:");
        fontField = new JTextField();
        panel.add(fontLabel);
        panel.add(fontField);

        JLabel sizeLabel = new JLabel("Size:");
        sizeField = new JTextField();
        panel.add(sizeLabel);
        panel.add(sizeField);

        JLabel colorLabel = new JLabel("Color:");
        colorField = new JTextField();
        panel.add(colorLabel);
        panel.add(colorField);

        JLabel xLabel = new JLabel("X coordinate:");
        xField = new JTextField();
        panel.add(xLabel);
        panel.add(xField);

        JLabel yLabel = new JLabel("Y coordinate:");
        yField = new JTextField();
        panel.add(yLabel);
        panel.add(yField);

        JButton applyButton = new JButton("Изменить");
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(urlPhoto != null) {
                    try {
                        myPicture = ImageIO.read(new File(urlPhoto));
                    } catch (IOException s) {
                        throw new RuntimeException(s);
                    }
                    Font font = new Font(fontField.getText(), Font.PLAIN, Integer.parseInt(sizeField.getText())); // Шрифт надписи

                    int y = width;
                    if(Integer.parseInt(yField.getText())<=width){
                        y = Integer.parseInt(yField.getText());
                    }

                    Graphics2D g = (Graphics2D) myPicture.getGraphics();
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setFont(font);
                    g.setColor(Color.decode(colorField.getText()));
                    g.drawString(textArea.getText(), Integer.parseInt(xField.getText()), y);

                    label.setIcon(new ImageIcon(myPicture));
                }
            }
        });

        JButton overButton = new JButton("Изменить все Фотки");
        overButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                File file = new File(urlPhoto);
                File directory = new File(file.getParent());

                for(String photo:PhotoShare.findPhotos(directory)){
                    try {
                        myPicture = ImageIO.read(new File(photo));

                        Font font = new Font(fontField.getText(), Font.PLAIN, Integer.parseInt(sizeField.getText())); // Шрифт надписи

                        Graphics2D g = (Graphics2D) myPicture.getGraphics();
                        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g.setFont(font);
                        g.setColor(Color.decode(colorField.getText()));
                        g.drawString(textArea.getText(), Integer.parseInt(xField.getText()), Integer.parseInt(yField.getText()));
                        g.dispose();

                        ImageIO.write(myPicture, photo.split("\\.")[1], new File(photo));

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            }
        });

        panel.add(applyButton, BorderLayout.AFTER_LAST_LINE);
        panel.add(overButton, BorderLayout.AFTER_LAST_LINE);
    }

    public static void main(String[] args) {
        new PhotoEdit();
    }
}
