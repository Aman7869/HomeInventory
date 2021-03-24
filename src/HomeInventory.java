
import javax.swing.*;
import java.awt.*;
//import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.event.*;
import com.toedter.calendar.*;
import java.beans.*;
import javax.swing.JButton;
import java.io.*;
import java.awt.Image;
import java.awt.geom.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;
import java.text.*;
import javax.swing.filechooser.*;
import java.awt.print.*;

public class HomeInventory extends javax.swing.JFrame {

   

    InventoryItem myItem = new InventoryItem();
    int currentEntry;
    static final int maximumEntries = 300;
    static int numberEntries;
    static InventoryItem[] myInventory = new InventoryItem[maximumEntries];
    static final int entriesPerPage = 2;
    static int lastPage;

    private void exitForm(WindowEvent evt) {
        if (JOptionPane.showConfirmDialog(null, "An unsaved changes will be lost.\nAre you sure you want to exit?", "Exit Program",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
            return;
        }

// write entries back to file
        try {
            PrintWriter outputFile = new PrintWriter(new BufferedWriter(new FileWriter("inventory.txt")));
            outputFile.println(numberEntries);
            if (numberEntries != 0) {
                for (int i = 0; i < numberEntries; i++) {
                    outputFile.println(myInventory[i].description);
                    outputFile.println(myInventory[i].location);
                    outputFile.println(myInventory[i].serialNumber);
                    outputFile.println(myInventory[i].marked);
                    outputFile.println(myInventory[i].purchasePrice);
                    outputFile.println(myInventory[i].purchaseDate);
                    outputFile.println(myInventory[i].purchaseLocation);
                    outputFile.println(myInventory[i].note);
                    outputFile.println(myInventory[i].photoFile);
                }
            }
// write combo box entries
            outputFile.println(locationComboBox.getItemCount());
            if (locationComboBox.getItemCount() != 0) {
                for (int i = 0; i < locationComboBox.getItemCount(); i++) {
                    outputFile.println(locationComboBox.getItemAt(i));
                }
            }
            outputFile.close();
        } catch (Exception ex) {
        }
        System.exit(0);
    }

    private void deleteEntry(int j) {
// delete entr j
        if (j != numberEntries) {
// move all entries under j up one level
            for (int i = j; i < numberEntries; i++) {
                myInventory[i - 1] = new InventoryItem();
                myInventory[i - 1] = myInventory[i];
            }
        }
        numberEntries--;
    }

    private void checkSave() {
        boolean edited = false;
        if (!myInventory[currentEntry - 1].description.equals(itemTextField.getText())) {
            edited = true;
        } else if (!myInventory[currentEntry
                - 1].location.equals(locationComboBox.getSelectedItem().toString())) {
            edited = true;
        } else if (myInventory[currentEntry - 1].marked != markedCheckBox.isSelected()) {
            edited = true;
        } else if (!myInventory[currentEntry - 1].serialNumber.equals(serialTextField.getText())) {
            edited = true;
        } else if (!myInventory[currentEntry - 1].purchasePrice.equals(priceTextField.getText())) {
            edited = true;
        } else if (!myInventory[currentEntry
                - 1].purchaseDate.equals(dateToString(dateDateChooser.getDate()))) {
            edited = true;
        } else if (!myInventory[currentEntry
                - 1].purchaseLocation.equals(storeTextField.getText())) {
            edited = true;
        } else if (!myInventory[currentEntry - 1].note.equals(noteTextField.getText())) {
            edited = true;
        } else if (!myInventory[currentEntry - 1].photoFile.equals(photoTextArea.getText())) {
            edited = true;
        }
        if (edited) {
            if (JOptionPane.showConfirmDialog(null, "You have edited this item. Do you want to save the changes?", "Save Item", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                saveButton.doClick();
            }
        }
    }


//    }
    private static class numberEntries {

        public numberEntries() {
        }

    }

    private void searchButtonActionPerformed(ActionEvent e) {


        int i;
        if (numberEntries == 0) {
            return;
        }
// search for item letter
        String letterClicked = e.getActionCommand();
        i = 0;
        do {
            if (myInventory[i].description.substring(0, 1).equals(letterClicked)) {
                currentEntry = i + 1;
                showEntry(currentEntry);
                return;
            }
            i++;
        } while (i < numberEntries);
        JOptionPane.showConfirmDialog(null, "No " + letterClicked + " inventor items.", "None Found", JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE);

    }

    class InventoryDocument implements Printable {

        public int print(Graphics g, PageFormat pf, int pageIndex) {
            Graphics2D g2D = (Graphics2D) g;
            if ((pageIndex + 1) > HomeInventory.lastPage) {
                return NO_SUCH_PAGE;
            }
            int i, iEnd;
// here you decide what goes on each page and draw it
// header
            g2D.setFont(new Font("Arial", Font.BOLD, 14));
            g2D.drawString("Home Inventor Items - Page " + String.valueOf(pageIndex + 1),
                    (int) pf.getImageableX(), (int) (pf.getImageableY() + 25));
// get starting y
            int d = (int) g2D.getFont().getStringBounds("S",
                    g2D.getFontRenderContext()).getHeight();
            int y = (int) (pf.getImageableY() + 4 * d);
            iEnd = HomeInventory.entriesPerPage * (pageIndex + 1);
            if (iEnd > HomeInventory.numberEntries) {
                iEnd = HomeInventory.numberEntries;
            }
            for (i = 0 + HomeInventory.entriesPerPage * pageIndex; i < iEnd; i++) {
// dividing line
                Line2D.Double dividingLine = new Line2D.Double(pf.getImageableX(), y,
                        pf.getImageableX() + pf.getImageableWidth(), y);
                g2D.draw(dividingLine);
                int dy = 0;
                y += dy;
                g2D.setFont(new Font("Arial", Font.BOLD, 12));
                g2D.drawString(HomeInventory.myInventory[i].description, (int) pf.getImageableX(), y);
                y += dy;
                g2D.setFont(new Font("Arial", Font.PLAIN, 12));
                g2D.drawString("Location: " + HomeInventory.myInventory[i].location, (int) (pf.getImageableX() + 25), y);
                y += dy;
                if (HomeInventory.myInventory[i].marked) {
                    g2D.drawString("Item is marked with identifying information.", (int) (pf.getImageableX() + 25), y);
                } else {
                    g2D.drawString("Item is NOT marked with identifying information.", (int) (pf.getImageableX() + 25), y);
                }
                y += dy;
                g2D.drawString("Serial Number: " + HomeInventory.myInventory[i].serialNumber, (int) (pf.getImageableX() + 25), y);
                y += dy;
                g2D.drawString("Price: $" + HomeInventory.myInventory[i].purchasePrice + ",Purchased on: " + HomeInventory.myInventory[i].purchaseDate, (int) (pf.getImageableX() + 25), y);
                y += dy;
                g2D.drawString("Purchased at: "
                        + HomeInventory.myInventory[i].purchaseLocation, (int) (pf.getImageableX() + 25), y);
                y += dy;
                g2D.drawString("Note: " + HomeInventory.myInventory[i].note, (int) (pf.getImageableX() + 25), y);
                y += dy;
                try {
// maintain original width/height ratio
                    Image inventoryImage = new ImageIcon(HomeInventory.myInventory[i].photoFile).getImage();
                    double ratio = (double) (inventoryImage.getWidth(null)) / (double) inventoryImage.getHeight(null);
                    g2D.drawImage(inventoryImage, (int) (pf.getImageableX() + 25), y, (int) (100 * ratio), 100, null);
                } catch (Exception ex) {
// have place to go in case image file doesn't open
                }
                y += 2 * dy + 100;
            }
            return PAGE_EXISTS;
        }
    }

    public class InventoryItem {

        public String description;
        public String location;
        public boolean marked;
        public String serialNumber;
        public String purchasePrice;
        public String purchaseDate;
        public String purchaseLocation;
        public String note;
        public String photoFile;
    }

        

    



    private Date stringToDate(String s) {
        int m = Integer.valueOf(s.substring(0, 2)).intValue() - 1;
        int d = Integer.valueOf(s.substring(3, 5)).intValue();
        int y = Integer.valueOf(s.substring(6)).intValue() - 1900;
        return (new Date(y, m, d));
    }

    private String dateToString(Date dd) {
        String yString = String.valueOf(dd.getYear() + 1900);
        int m = dd.getMonth() + 1;
        String mString = new DecimalFormat("00").format(m);
        int d = dd.getDate();
        String dString = new DecimalFormat("00").format(d);
        return (mString + "/" + dString + "/" + yString);
    }

    private void showPhoto(String photoFile) {
        if (!photoFile.equals("")) {
            try {
                photoTextArea.setText(photoFile);
            } catch (Exception ex) {
                photoTextArea.setText("");
            }
        } else {
            photoTextArea.setText("");
        }
        photoPanel.repaint();
    }


    class PhotoPanel extends JPanel {

        public void paintComponent(Graphics g) {
            Graphics2D g2D = (Graphics2D) g;
            super.paintComponent(g2D);
// draw border
            g2D.setPaint(Color.BLACK);
            g2D.draw(new Rectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1));

// show photo
            Image photoImage;
            photoImage = new ImageIcon(HomeInventory.photoTextArea.getText()).getImage();
            int w = getWidth();
            int h = getHeight();
            double rWidth = (double) getWidth() / (double) photoImage.getWidth(null);
            double rHeight = (double) getHeight() / (double) photoImage.getHeight(null);
            if (rWidth > rHeight) {
// leave height at displa height, change width b amount height is changed
                w = (int) (photoImage.getWidth(null) * rHeight);
            } else {
// leave width at displa width, change height b amount width is changed
                h = (int) (photoImage.getHeight(null) * rWidth);
            }
// center in panel
            g2D.drawImage(photoImage, (int) (0.5 * (getWidth() - w)), (int) (0.5 * (getHeight() - h)),
                    w, h, null);

            g2D.dispose();
        }
    }

    private void blankValues() {
// blank input screen
        newButton.setEnabled(false);
        deleteButton.setEnabled(false);
        saveButton.setEnabled(true);
        previousButton.setEnabled(false);
        nextButton.setEnabled(false);
        printButton.setEnabled(false);
        itemTextField.setText("");
        locationComboBox.setSelectedItem("");
        markedCheckBox.setSelected(false);
        serialTextField.setText("");
        priceTextField.setText("");
        dateDateChooser.setDate(new Date());
        storeTextField.setText("");
        noteTextField.setText("");
        photoTextArea.setText("");
        photoPanel.repaint();
        itemTextField.requestFocus();
    }

 
    
    public HomeInventory() {
        setTitle("Home Inventory Manager");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                exitForm(evt);
            }
        });

        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gridConstraints;
        
        inventoryToolBar.setFloatable(false);
        inventoryToolBar.setBackground(Color.BLUE);
        inventoryToolBar.setOrientation(SwingConstants.VERTICAL);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 0;
        gridConstraints.gridy= 0;
        gridConstraints.gridheight = 8;
        gridConstraints.fill = GridBagConstraints.VERTICAL;
        getContentPane().add(inventoryToolBar, gridConstraints);
        inventoryToolBar.addSeparator();
        Dimension bSize = new Dimension(70, 50);
        newButton.setText("New");
        sizeButton(newButton, bSize);
        newButton.setToolTipText("Add New Item");
        newButton.setHorizontalTextPosition(SwingConstants.CENTER);
        newButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        newButton.setFocusable(false);
        inventoryToolBar.add(newButton);
        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newButtonActionPerformed(e);
            }
        });
        deleteButton.setText("Delete");
        sizeButton(deleteButton, bSize);
        deleteButton.setToolTipText("Delete Current Item");
        deleteButton.setHorizontalTextPosition(SwingConstants.CENTER);
        deleteButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        deleteButton.setFocusable(false);
        inventoryToolBar.add(deleteButton);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteButtonActionPerformed(e);
            }
        });
        saveButton.setText("Save");
        sizeButton(saveButton, bSize);
        saveButton.setToolTipText("Save Current Item");
        saveButton.setHorizontalTextPosition(SwingConstants.CENTER);
        saveButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        saveButton.setFocusable(false);
        inventoryToolBar.add(saveButton);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveButtonActionPerformed(e);
            }
        });
        inventoryToolBar.addSeparator();
        previousButton.setText("Previous");
        sizeButton(previousButton, bSize);
        previousButton.setToolTipText("Displa Previous Item");
        previousButton.setHorizontalTextPosition(SwingConstants.CENTER);
        previousButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        previousButton.setFocusable(false);
        inventoryToolBar.add(previousButton);
        previousButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                previousButtonActionPerformed(e);
            }
        });
        nextButton.setText("Next");
        sizeButton(nextButton, bSize);
        nextButton.setToolTipText("Displa Next Item");
        nextButton.setHorizontalTextPosition(SwingConstants.CENTER);
        nextButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        nextButton.setFocusable(false);
        inventoryToolBar.add(nextButton);
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextButtonActionPerformed(e);
            }
        });
        inventoryToolBar.addSeparator();
        printButton.setText("Print");
        sizeButton(printButton, bSize);
        printButton.setToolTipText("Print Inventor List");
        printButton.setHorizontalTextPosition(SwingConstants.CENTER);
        printButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        printButton.setFocusable(false);
        inventoryToolBar.add(printButton);
        printButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                printButtonActionPerformed(e);
            }
        });
        exitButton.setText("Exit");
        sizeButton(exitButton, bSize);
        exitButton.setToolTipText("Exit Program");
        exitButton.setFocusable(false);
        inventoryToolBar.add(exitButton);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitButtonActionPerformed(e);
            }
        });
        itemLabel.setText("Inventor Item");
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy= 0;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        gridConstraints.anchor = GridBagConstraints.EAST;
        getContentPane().add(itemLabel, gridConstraints);
        itemTextField.setPreferredSize(new Dimension(400, 25));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy= 0;
        gridConstraints.gridwidth = 5;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        getContentPane().add(itemTextField, gridConstraints);
        itemTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                itemTextFieldActionPerformed(e);
            }
        });
        locationLabel.setText("Location");
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy= 1;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        gridConstraints.anchor = GridBagConstraints.EAST;
        getContentPane().add(locationLabel, gridConstraints);
        locationComboBox.setPreferredSize(new Dimension(270, 25));
        locationComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        locationComboBox.setEditable(true);
        locationComboBox.setBackground(Color.WHITE);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy= 1;
        gridConstraints.gridwidth = 3;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        getContentPane().add(locationComboBox, gridConstraints);
        locationComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                locationComboBoxActionPerformed(e);
            }
        });
        markedCheckBox.setText("Marked?");
        markedCheckBox.setFocusable(false);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 5;
        gridConstraints.gridy= 1;
        gridConstraints.insets = new Insets(10, 10, 0, 0);
        gridConstraints.anchor = GridBagConstraints.WEST;
        getContentPane().add(markedCheckBox, gridConstraints);
        serialLabel.setText("Serial Number");
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy= 2;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        gridConstraints.anchor = GridBagConstraints.EAST;
        getContentPane().add(serialLabel, gridConstraints);
        serialTextField.setPreferredSize(new Dimension(270, 25));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy= 2;
        gridConstraints.gridwidth = 3;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        getContentPane().add(serialTextField, gridConstraints);
        serialTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                serialTextFieldActionPerformed(e);
            }
        });
        priceLabel.setText("Purchase Price");
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy= 3;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        gridConstraints.anchor = GridBagConstraints.EAST;
        getContentPane().add(priceLabel, gridConstraints);
        priceTextField.setPreferredSize(new Dimension(160, 25));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy= 3;
        gridConstraints.gridwidth = 2;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        getContentPane().add(priceTextField, gridConstraints);
        priceTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                priceTextFieldActionPerformed(e);
            }
        });
        dateLabel.setText("Date Purchased");
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 4;
        gridConstraints.gridy= 3;
        gridConstraints.insets = new Insets(10, 10, 0, 0);
        gridConstraints.anchor = GridBagConstraints.WEST;
        getContentPane().add(dateLabel, gridConstraints);
        dateDateChooser.setPreferredSize(new Dimension(120, 25));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 5;
        gridConstraints.gridy= 3;
        gridConstraints.gridwidth = 2;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        getContentPane().add(dateDateChooser, gridConstraints);
        dateDateChooser.addPropertyChangeListener(new PropertyChangeListener() 
        {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                dateDateChooserPropertyChange(e);
            }

        });
        storeLabel.setText("Store/Website");
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy= 4;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        gridConstraints.anchor = GridBagConstraints.EAST;
        getContentPane().add(storeLabel, gridConstraints);
        storeTextField.setPreferredSize(new Dimension(400, 25));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy= 4;
        gridConstraints.gridwidth = 5;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        getContentPane().add(storeTextField, gridConstraints);
        storeTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                storeTextFieldActionPerformed(e);
            }
        });
        noteLabel.setText("Note");
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy= 5;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        gridConstraints.anchor = GridBagConstraints.EAST;
        getContentPane().add(noteLabel, gridConstraints);
        noteTextField.setPreferredSize(new Dimension(400, 25));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy= 5;
        gridConstraints.gridwidth = 5;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        getContentPane().add(noteTextField, gridConstraints);
        noteTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                noteTextFieldActionPerformed(e);
            }
        });
        photoLabel.setText("Photo");
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy= 6;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        gridConstraints.anchor = GridBagConstraints.EAST;
        getContentPane().add(photoLabel, gridConstraints);
        photoTextArea.setPreferredSize(new Dimension(350, 35));
        photoTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
        photoTextArea.setEditable(false);
        photoTextArea.setLineWrap(true);
        photoTextArea.setWrapStyleWord(true);
        photoTextArea.setBackground(new Color(255, 255, 192));
        photoTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        photoTextArea.setFocusable(false);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy= 6;
        gridConstraints.gridwidth = 4;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        getContentPane().add(photoTextArea, gridConstraints);
        photoButton.setText("...");
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 6;
        gridConstraints.gridy= 6;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        getContentPane().add(photoButton, gridConstraints);
        photoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                photoButtonActionPerformed(e);
            }
        });

        JPanel searchPanel = new JPanel();
        JButton[] searchButton = new JButton[26];
//Add them to the panel using:
        searchPanel.setPreferredSize(new Dimension(240, 160));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Item Search"));
        searchPanel.setLayout(new GridBagLayout());
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 7;
        gridConstraints.gridwidth = 3;
        gridConstraints.insets = new Insets(10, 0, 10, 0);
        gridConstraints.anchor = GridBagConstraints.CENTER;
        getContentPane().add(searchPanel, gridConstraints);
        int x = 0, y = 0;
// create and position 26 buttons
        for (int i = 0; i < 26; i++) {
// create new button
            searchButton[i] = new JButton();
// set text property
            searchButton[i].setText(String.valueOf((char) (65 + i)));
            searchButton[i].setFont(new Font("Arial", Font.BOLD, 12));
            searchButton[i].setMargin(new Insets(-10, -10, -10, -10));
            sizeButton(searchButton[i], new Dimension(37, 27));
            searchButton[i].setBackground(Color.YELLOW);
            gridConstraints = new GridBagConstraints();
            gridConstraints.gridx = x;
            gridConstraints.gridy = y;
            searchPanel.add(searchButton[i], gridConstraints);
// add method
            searchButton[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    searchButtonActionPerformed(e);
                }

                private void searchButtonActionPerformed(ActionEvent e) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            x++;
// six buttons per row
            if (x % 6 == 0) {
                x = 0;
                y++;
            }
        }

        PhotoPanel photoPanel = new PhotoPanel();
//and added to the frame using:
        photoPanel.setPreferredSize(new Dimension(240, 160));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 4;
        gridConstraints.gridy = 7;
        gridConstraints.gridwidth = 3;
        gridConstraints.insets = new Insets(10, 0, 10, 10);
        gridConstraints.anchor = GridBagConstraints.CENTER;
        getContentPane().add(photoPanel, gridConstraints);
        
        pack();
Dimension screenSize =
Toolkit.getDefaultToolkit().getScreenSize();
setBounds((int) (0.5 * (screenSize.width - getWidth())), (int) (0.5 * (screenSize.height -
getHeight())), getWidth(), getHeight());


int n;
// open file for entries
try
{
BufferedReader inputFile = new BufferedReader(new FileReader("inventory.txt"));
numberEntries =
Integer.valueOf(inputFile.readLine()).intValue();
if (numberEntries != 0)
{
for (int i = 0; i < numberEntries; i++)
{
myInventory[i] = new InventoryItem();
myInventory[i].description = inputFile.readLine();
myInventory[i].location = inputFile.readLine();
myInventory[i].serialNumber = inputFile.readLine();
myInventory[i].marked =
Boolean.valueOf(inputFile.readLine()).booleanValue();
myInventory[i].purchasePrice =
inputFile.readLine();
myInventory[i].purchaseDate = inputFile.readLine();
myInventory[i].purchaseLocation =
inputFile.readLine();
myInventory[i].note = inputFile.readLine();
myInventory[i].photoFile = inputFile.readLine();
}
}
// read in combo box elements
n = Integer.valueOf(inputFile.readLine()).intValue();
if (n != 0)
{
for (int i = 0; i < n; i++)
{
locationComboBox.addItem(inputFile.readLine());
}
}
inputFile.close();
currentEntry = 1;
showEntry(currentEntry);
}
catch (Exception ex)
{
numberEntries = 0;
currentEntry = 0;
}
if (numberEntries == 0)
{
newButton.setEnabled(false);
deleteButton.setEnabled(false);
nextButton.setEnabled(false);
previousButton.setEnabled(false);
printButton.setEnabled(false);
}
          


        

        itemTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                itemTextFieldActionPerformed(e);
            }
        });

        locationComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                locationComboBoxActionPerformed(e);
            }
        });

        serialTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                serialTextFieldActionPerformed(e);
            }
        });

        priceTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                priceTextFieldActionPerformed(e);
            }
        });

        storeTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                storeTextFieldActionPerformed(e);
            }
        });

        noteTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                noteTextFieldActionPerformed(e);
            }
        });


        initComponents();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        itemLabel = new javax.swing.JLabel();
        itemTextField = new javax.swing.JTextField();
        locationLabel = new javax.swing.JLabel();
        locationComboBox = new javax.swing.JComboBox<>();
        serialLabel = new javax.swing.JLabel();
        serialTextField = new javax.swing.JTextField();
        priceLabel = new javax.swing.JLabel();
        priceTextField = new javax.swing.JTextField();
        dateLabel = new javax.swing.JLabel();
        markedCheckBox = new javax.swing.JCheckBox();
        storeLabel = new javax.swing.JLabel();
        storeTextField = new javax.swing.JTextField();
        noteLabel = new javax.swing.JLabel();
        noteTextField = new javax.swing.JTextField();
        photoLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        photoTextArea = new javax.swing.JTextArea();
        photoButton = new javax.swing.JButton();
        searchPanel = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        searchButton = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        itemlabel = new javax.swing.JLabel();
        inventoryToolBar = new javax.swing.JToolBar();
        newButton = new javax.swing.JButton(new ImageIcon("C:\\Users\\lenovo\\Downloads\\new.png"));
        deleteButton = new javax.swing.JButton(new ImageIcon("C:\\Users\\lenovo\\Downloads\\delete.png"));
        saveButton = new javax.swing.JButton(new ImageIcon("C:\\Users\\lenovo\\Downloads\\save.png"));
        previousButton = new javax.swing.JButton(new ImageIcon("C:\\Users\\lenovo\\Downloads\\backword.png"));
        nextButton = new javax.swing.JButton(new ImageIcon("C:\\Users\\lenovo\\Downloads\\forword.png"));
        printButton = new javax.swing.JButton(new ImageIcon("C:\\Users\\lenovo\\Downloads\\print.png"));
        exitButton = new javax.swing.JButton();
        photoPanel = new javax.swing.JPanel();
        dateDateChooser = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Home Inventory Manager");
        setResizable(false);

        itemLabel.setText("Inventory Item");

        itemTextField.setPreferredSize(new java.awt.Dimension(400, 25));
        itemTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemTextFieldActionPerformed(evt);
            }
        });

        locationLabel.setText("Location");

        locationComboBox.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        locationComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        locationComboBox.setPreferredSize(new java.awt.Dimension(270, 25));
        locationComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locationComboBoxActionPerformed(evt);
            }
        });

        serialLabel.setText("Serial Number");

        serialTextField.setPreferredSize(new java.awt.Dimension(270, 25));
        serialTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serialTextFieldActionPerformed(evt);
            }
        });

        priceLabel.setText("Purchase Price");

        priceTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priceTextFieldActionPerformed(evt);
            }
        });

        dateLabel.setText("Date Purchased");

        markedCheckBox.setText("Marked?");

        storeLabel.setText("Store/Website");

        storeTextField.setPreferredSize(new java.awt.Dimension(400, 25));
        storeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                storeTextFieldActionPerformed(evt);
            }
        });

        noteLabel.setText("Note");

        noteTextField.setPreferredSize(new java.awt.Dimension(400, 25));
        noteTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noteTextFieldActionPerformed(evt);
            }
        });

        photoLabel.setText("Photo");

        photoTextArea.setColumns(20);
        photoTextArea.setRows(5);
        jScrollPane1.setViewportView(photoTextArea);

        photoButton.setText("...");
        photoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                photoButtonActionPerformed(evt);
            }
        });

        searchPanel.setAlignmentX(0.0F);
        searchPanel.setAlignmentY(0.0F);
        searchPanel.setPreferredSize(new java.awt.Dimension(286, 160));

        jButton3.setBackground(new java.awt.Color(255, 255, 0));
        jButton3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton3.setText("C");
        jButton3.setAlignmentY(0.0F);
        jButton3.setBorderPainted(false);
        jButton3.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton3.setPreferredSize(new java.awt.Dimension(37, 27));

        jButton4.setBackground(new java.awt.Color(255, 255, 0));
        jButton4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton4.setText("B");
        jButton4.setAlignmentY(0.0F);
        jButton4.setBorderPainted(false);
        jButton4.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton4.setPreferredSize(new java.awt.Dimension(37, 27));

        searchButton.setBackground(new java.awt.Color(255, 255, 0));
        searchButton.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        searchButton.setText("A");
        searchButton.setAlignmentY(0.0F);
        searchButton.setBorderPainted(false);
        searchButton.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        searchButton.setPreferredSize(new java.awt.Dimension(37, 27));

        jButton6.setBackground(new java.awt.Color(255, 255, 0));
        jButton6.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton6.setText("D");
        jButton6.setAlignmentY(0.0F);
        jButton6.setBorderPainted(false);
        jButton6.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton6.setPreferredSize(new java.awt.Dimension(37, 27));

        jButton7.setBackground(new java.awt.Color(255, 255, 0));
        jButton7.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton7.setText("E");
        jButton7.setAlignmentY(0.0F);
        jButton7.setBorderPainted(false);
        jButton7.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton7.setPreferredSize(new java.awt.Dimension(37, 27));

        jButton8.setBackground(new java.awt.Color(255, 255, 0));
        jButton8.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton8.setText("F");
        jButton8.setAlignmentY(0.0F);
        jButton8.setBorderPainted(false);
        jButton8.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton8.setPreferredSize(new java.awt.Dimension(37, 27));

        jButton9.setBackground(new java.awt.Color(255, 255, 0));
        jButton9.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton9.setText("G");
        jButton9.setAlignmentY(0.0F);
        jButton9.setBorderPainted(false);
        jButton9.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton9.setPreferredSize(new java.awt.Dimension(37, 27));

        jButton10.setBackground(new java.awt.Color(255, 255, 0));
        jButton10.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton10.setText("H");
        jButton10.setAlignmentY(0.0F);
        jButton10.setBorderPainted(false);
        jButton10.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton10.setPreferredSize(new java.awt.Dimension(37, 27));

        jButton11.setBackground(new java.awt.Color(255, 255, 0));
        jButton11.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton11.setText("I");
        jButton11.setAlignmentY(0.0F);
        jButton11.setBorderPainted(false);
        jButton11.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton11.setPreferredSize(new java.awt.Dimension(37, 27));

        jButton12.setBackground(new java.awt.Color(255, 255, 0));
        jButton12.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton12.setText("J");
        jButton12.setAlignmentY(0.0F);
        jButton12.setBorderPainted(false);
        jButton12.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton12.setPreferredSize(new java.awt.Dimension(37, 27));

        jButton13.setBackground(new java.awt.Color(255, 255, 0));
        jButton13.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton13.setText("K");
        jButton13.setAlignmentY(0.0F);
        jButton13.setBorderPainted(false);
        jButton13.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton13.setPreferredSize(new java.awt.Dimension(37, 27));

        jButton14.setBackground(new java.awt.Color(255, 255, 0));
        jButton14.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton14.setText("L");
        jButton14.setAlignmentY(0.0F);
        jButton14.setBorderPainted(false);
        jButton14.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton14.setPreferredSize(new java.awt.Dimension(37, 27));

        jButton15.setBackground(new java.awt.Color(255, 255, 0));
        jButton15.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton15.setText("N");
        jButton15.setAlignmentY(0.0F);
        jButton15.setBorderPainted(false);
        jButton15.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton15.setPreferredSize(new java.awt.Dimension(37, 27));

        jButton16.setBackground(new java.awt.Color(255, 255, 0));
        jButton16.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton16.setText("M");
        jButton16.setAlignmentY(0.0F);
        jButton16.setBorderPainted(false);
        jButton16.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton16.setPreferredSize(new java.awt.Dimension(37, 27));

        jButton17.setBackground(new java.awt.Color(255, 255, 0));
        jButton17.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton17.setText("O");
        jButton17.setAlignmentY(0.0F);
        jButton17.setBorderPainted(false);
        jButton17.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton17.setPreferredSize(new java.awt.Dimension(37, 27));

        jButton18.setBackground(new java.awt.Color(255, 255, 0));
        jButton18.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton18.setText("P");
        jButton18.setAlignmentY(0.0F);
        jButton18.setBorderPainted(false);
        jButton18.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton18.setPreferredSize(new java.awt.Dimension(37, 27));

        jButton19.setBackground(new java.awt.Color(255, 255, 0));
        jButton19.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton19.setText("Q");
        jButton19.setAlignmentY(0.0F);
        jButton19.setBorderPainted(false);
        jButton19.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton19.setPreferredSize(new java.awt.Dimension(37, 27));

        jButton20.setBackground(new java.awt.Color(255, 255, 0));
        jButton20.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton20.setText("R");
        jButton20.setAlignmentY(0.0F);
        jButton20.setBorderPainted(false);
        jButton20.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton20.setPreferredSize(new java.awt.Dimension(37, 27));

        jButton21.setBackground(new java.awt.Color(255, 255, 0));
        jButton21.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton21.setText("S");
        jButton21.setAlignmentY(0.0F);
        jButton21.setBorderPainted(false);
        jButton21.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton21.setPreferredSize(new java.awt.Dimension(37, 27));

        jButton22.setBackground(new java.awt.Color(255, 255, 0));
        jButton22.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton22.setText("T");
        jButton22.setAlignmentY(0.0F);
        jButton22.setBorderPainted(false);
        jButton22.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton22.setPreferredSize(new java.awt.Dimension(37, 27));

        jButton23.setBackground(new java.awt.Color(255, 255, 0));
        jButton23.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton23.setText("U");
        jButton23.setAlignmentY(0.0F);
        jButton23.setBorderPainted(false);
        jButton23.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton23.setPreferredSize(new java.awt.Dimension(37, 27));

        jButton24.setBackground(new java.awt.Color(255, 255, 0));
        jButton24.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton24.setText("V");
        jButton24.setAlignmentY(0.0F);
        jButton24.setBorderPainted(false);
        jButton24.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton24.setPreferredSize(new java.awt.Dimension(37, 27));

        jButton25.setBackground(new java.awt.Color(255, 255, 0));
        jButton25.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton25.setText("X");
        jButton25.setAlignmentY(0.0F);
        jButton25.setBorderPainted(false);
        jButton25.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton25.setPreferredSize(new java.awt.Dimension(37, 27));

        jButton26.setBackground(new java.awt.Color(255, 255, 0));
        jButton26.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton26.setText("Y");
        jButton26.setAlignmentY(0.0F);
        jButton26.setBorderPainted(false);
        jButton26.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton26.setPreferredSize(new java.awt.Dimension(37, 27));

        jButton27.setBackground(new java.awt.Color(255, 255, 0));
        jButton27.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton27.setText("Z");
        jButton27.setAlignmentY(0.0F);
        jButton27.setBorderPainted(false);
        jButton27.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        jButton27.setPreferredSize(new java.awt.Dimension(37, 27));

        javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(searchPanelLayout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(searchPanelLayout.createSequentialGroup()
                        .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(searchPanelLayout.createSequentialGroup()
                                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(searchPanelLayout.createSequentialGroup()
                                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(searchPanelLayout.createSequentialGroup()
                                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(searchPanelLayout.createSequentialGroup()
                                .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(searchPanelLayout.createSequentialGroup()
                        .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(4, 4, 4))
        );
        searchPanelLayout.setVerticalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        itemlabel.setText("Item");

        inventoryToolBar.setBackground(new java.awt.Color(0, 153, 255));
        inventoryToolBar.setFloatable(false);
        inventoryToolBar.setOrientation(javax.swing.SwingConstants.VERTICAL);
        inventoryToolBar.setRollover(true);
        inventoryToolBar.setDoubleBuffered(true);

        newButton.setText("New");
        newButton.setToolTipText("Add New Item");
        newButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newButton.setPreferredSize(new java.awt.Dimension(70, 100));
        newButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });
        inventoryToolBar.add(newButton);

        deleteButton.setText("Delete");
        deleteButton.setToolTipText("Delete Current Item");
        deleteButton.setFocusable(false);
        deleteButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteButton.setPreferredSize(new java.awt.Dimension(70, 50));
        deleteButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        inventoryToolBar.add(deleteButton);

        saveButton.setText("Save");
        saveButton.setToolTipText("Save Current Item");
        saveButton.setFocusable(false);
        saveButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveButton.setPreferredSize(new java.awt.Dimension(70, 50));
        saveButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        inventoryToolBar.add(saveButton);

        previousButton.setText("Previous");
        previousButton.setToolTipText("Display Previous Item");
        previousButton.setFocusable(false);
        previousButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        previousButton.setPreferredSize(new java.awt.Dimension(70, 50));
        previousButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        previousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousButtonActionPerformed(evt);
            }
        });
        inventoryToolBar.add(previousButton);

        nextButton.setText("Next");
        nextButton.setToolTipText("Display Next Item");
        nextButton.setFocusable(false);
        nextButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        nextButton.setPreferredSize(new java.awt.Dimension(70, 50));
        nextButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });
        inventoryToolBar.add(nextButton);

        printButton.setText("Print");
        printButton.setToolTipText("Print Inventory List");
        printButton.setFocusable(false);
        printButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        printButton.setPreferredSize(new java.awt.Dimension(70, 50));
        printButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });
        inventoryToolBar.add(printButton);

        exitButton.setText("Exit");
        exitButton.setToolTipText("Exit Program");
        exitButton.setFocusable(false);
        exitButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        exitButton.setPreferredSize(new java.awt.Dimension(70, 50));
        exitButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });
        inventoryToolBar.add(exitButton);

        photoPanel.setPreferredSize(new java.awt.Dimension(240, 160));

        javax.swing.GroupLayout photoPanelLayout = new javax.swing.GroupLayout(photoPanel);
        photoPanel.setLayout(photoPanelLayout);
        photoPanelLayout.setHorizontalGroup(
            photoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 240, Short.MAX_VALUE)
        );
        photoPanelLayout.setVerticalGroup(
            photoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 160, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(inventoryToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(itemLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(locationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(itemTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(serialLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(priceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(storeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(noteLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(photoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addComponent(locationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(markedCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(itemlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(serialTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(priceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(63, 63, 63)
                                .addComponent(dateLabel)
                                .addGap(33, 33, 33)
                                .addComponent(dateDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(storeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(48, 48, 48)
                                .addComponent(photoButton))
                            .addComponent(noteTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(photoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 29, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inventoryToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(itemLabel)
                            .addComponent(itemTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(locationLabel)
                            .addComponent(locationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(markedCheckBox))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(31, 31, 31)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(dateDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(storeLabel)
                                            .addComponent(storeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(noteLabel)
                                            .addComponent(noteTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(serialLabel)
                                            .addComponent(serialTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(priceLabel)
                                            .addComponent(priceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(photoLabel)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(14, 14, 14)
                                .addComponent(itemlabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(photoButton)
                                .addGap(28, 28, 28)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(photoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        blankValues();
        checkSave();

    }//GEN-LAST:event_newButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        // TODO add your handling code here:
        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this item?", "Delete Inventory Item", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
            return;
        }
        deleteEntry(currentEntry);
        if (numberEntries == 0) {
            currentEntry = 0;
            blankValues();
        } else {
            currentEntry--;
            if (currentEntry == 0) {
                currentEntry = 1;
            }
            showEntry(currentEntry);
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
 
        itemTextField.setText(itemTextField.getText().trim());
        if (itemTextField.getText().equals("")) {
            JOptionPane.showConfirmDialog(null, "Must have item description.", "Error",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            itemTextField.requestFocus();
            return;
        }
        if (newButton.isEnabled()) {
// delete edit entr then resave
            deleteEntry(currentEntry);
        }

// capitalize first letter
        String s = itemTextField.getText();
        itemTextField.setText(s.substring(0, 1).toUpperCase() + s.substring(1));
        numberEntries++;
// determine new current entr location based on description
        currentEntry = 1;
        if (numberEntries != 1) {
            do {
                if (itemTextField.getText().compareTo(myInventory[currentEntry - 1].description) < 0) {
                    break;
                }
                currentEntry++;
            } while (currentEntry < numberEntries);
        }
// move all entries below new value down one position unless at end
        if (currentEntry != numberEntries) {
            for (int i = numberEntries; i >= currentEntry + 1; i--) {
                myInventory[i - 1] = myInventory[i - 2];
                myInventory[i - 2] = new InventoryItem();
            }
        }
        myInventory[currentEntry - 1] = new InventoryItem();
        myInventory[currentEntry - 1].description = itemTextField.getText();
        myInventory[currentEntry - 1].location
                = locationComboBox.getSelectedItem().toString();
        myInventory[currentEntry - 1].marked = markedCheckBox.isSelected();
        myInventory[currentEntry - 1].serialNumber = serialTextField.getText();
        myInventory[currentEntry - 1].purchasePrice = priceTextField.getText();
        myInventory[currentEntry - 1].purchaseDate
                = dateToString(dateDateChooser.getDate());
        myInventory[currentEntry - 1].purchaseLocation = storeTextField.getText();
        myInventory[currentEntry - 1].photoFile = photoTextArea.getText();
        myInventory[currentEntry - 1].note = noteTextField.getText();
        showEntry(currentEntry);
        if (numberEntries < maximumEntries) {
            newButton.setEnabled(true);
        } else {
            newButton.setEnabled(false);
        }
        deleteButton.setEnabled(true);
        printButton.setEnabled(true);

    }//GEN-LAST:event_saveButtonActionPerformed

    private void previousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousButtonActionPerformed
        checkSave();
        currentEntry--;
        showEntry(currentEntry);
    }//GEN-LAST:event_previousButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed

        // TODO add your handling code here:
        checkSave();
        currentEntry++;
        showEntry(currentEntry);
    }//GEN-LAST:event_nextButtonActionPerformed

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
        // TODO add your handling code here:

        lastPage = (int) (1 + (numberEntries - 1) / entriesPerPage);
        PrinterJob inventoryPrinterJob = PrinterJob.getPrinterJob();
        inventoryPrinterJob.setPrintable(new InventoryDocument());
        if (inventoryPrinterJob.printDialog()) {
            try {
                inventoryPrinterJob.print();
            } catch (PrinterException ex) {
                JOptionPane.showConfirmDialog(null, ex.getMessage(), "Print Error",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        }

    }//GEN-LAST:event_printButtonActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        // TODO add your handling code here:
        exitForm(null);
    }//GEN-LAST:event_exitButtonActionPerformed

    private void photoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_photoButtonActionPerformed
        // TODO add your handling code here:

        JFileChooser openChooser = new JFileChooser();
        openChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        openChooser.setDialogTitle("Open Photo File");
        openChooser.addChoosableFileFilter(new FileNameExtensionFilter("Photo Files", "jpg"));
        if (openChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            showPhoto(openChooser.getSelectedFile().toString());
        }
    }//GEN-LAST:event_photoButtonActionPerformed

    private void itemTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemTextFieldActionPerformed
        locationComboBox.requestFocus();
    }//GEN-LAST:event_itemTextFieldActionPerformed

    private void locationComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locationComboBoxActionPerformed
        // If in list - exit method
        if (locationComboBox.getItemCount() != 0) {
            for (int i = 0; i < locationComboBox.getItemCount(); i++) {
                if (locationComboBox.getSelectedItem().toString().equals(locationComboBox.getItemAt(i).toString())) {
                    serialTextField.requestFocus();
                    return;
                }
            }
        }
// If not found, add to list box
        locationComboBox.addItem((String) locationComboBox.getSelectedItem());
        serialTextField.requestFocus();
        serialTextField.requestFocus();
    }//GEN-LAST:event_locationComboBoxActionPerformed

    private void serialTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serialTextFieldActionPerformed

        priceTextField.requestFocus();
    }//GEN-LAST:event_serialTextFieldActionPerformed

    private void priceTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priceTextFieldActionPerformed

        dateDateChooser.requestFocus();
    }//GEN-LAST:event_priceTextFieldActionPerformed

    private void dateDateChooserPropertyChange(PropertyChangeEvent e)
{
        storeTextField.requestFocus();
}
    
    private void storeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_storeTextFieldActionPerformed

        noteTextField.requestFocus();
    }//GEN-LAST:event_storeTextFieldActionPerformed

    private void noteTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noteTextFieldActionPerformed
        // TODO add your handling code here:
        photoButton.requestFocus();
    }//GEN-LAST:event_noteTextFieldActionPerformed

   private void sizeButton(JButton b,Dimension d)
{
b.setPreferredSize(d);
b.setMinimumSize(d);
b.setMaximumSize(d);
}
    
       private void showEntry(int j) {
// displa entr j (1 to numberEntries)
        itemTextField.setText(myInventory[j - 1].description);
        locationComboBox.setSelectedItem(myInventory[j - 1].location);
        markedCheckBox.setSelected(myInventory[j - 1].marked);
        serialTextField.setText(myInventory[j - 1].serialNumber);
        priceTextField.setText(myInventory[j - 1].purchasePrice);
        dateDateChooser.setDate(stringToDate(myInventory[j - 1].purchaseDate));
        storeTextField.setText(myInventory[j - 1].purchaseLocation);
        noteTextField.setText(myInventory[j - 1].note);
        showPhoto(myInventory[j - 1].photoFile);

        nextButton.setEnabled(true);
        previousButton.setEnabled(true);
        if (j == 1) {
            previousButton.setEnabled(false);
        }
        if (j == numberEntries) {
            nextButton.setEnabled(false);
        }
        itemTextField.requestFocus();
    }
   
   
    public static void main(String args[]) {

       new HomeInventory().show();
       java.awt.EventQueue.invokeLater(new Runnable() {
           public void run() {

               new HomeInventory().setVisible(true);
          }
       });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser dateDateChooser;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton exitButton;
    private javax.swing.JToolBar inventoryToolBar;
    private javax.swing.JLabel itemLabel;
    private javax.swing.JTextField itemTextField;
    private javax.swing.JLabel itemlabel;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> locationComboBox;
    private javax.swing.JLabel locationLabel;
    private javax.swing.JCheckBox markedCheckBox;
    public javax.swing.JButton newButton;
    private javax.swing.JButton nextButton;
    private javax.swing.JLabel noteLabel;
    private javax.swing.JTextField noteTextField;
    private javax.swing.JButton photoButton;
    private javax.swing.JLabel photoLabel;
    private javax.swing.JPanel photoPanel;
    private static javax.swing.JTextArea photoTextArea;
    private javax.swing.JButton previousButton;
    private javax.swing.JLabel priceLabel;
    private javax.swing.JTextField priceTextField;
    private javax.swing.JButton printButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JLabel serialLabel;
    private javax.swing.JTextField serialTextField;
    private javax.swing.JLabel storeLabel;
    private javax.swing.JTextField storeTextField;
    // End of variables declaration//GEN-END:variables


}
