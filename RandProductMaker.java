import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

public class RandProductMaker extends JFrame {
    private JTextField idField, nameField, descriptionField, costField, recordCountField;
    private JButton addButton, viewBtn;
    private int recordCount = 0;

    public RandProductMaker() {
        setTitle("Product Data Entry");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 2, 10, 10));

        JLabel idLabel = new JLabel("Product ID:");
        idField = new JTextField(15);

        JLabel nameLabel = new JLabel("Product Name:");
        nameField = new JTextField(15);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionField = new JTextField(15);

        JLabel costLabel = new JLabel("Cost:");
        costField = new JTextField(15);

        addButton = new JButton("Add Record");
        addButton.addActionListener(new AddButtonListener());

        viewBtn = new JButton("View Records");
        viewBtn.addActionListener(new ViewBtnListener());

        JLabel recordCountLabel = new JLabel("Record Count:");
        recordCountField = new JTextField(15);
        recordCountField.setEditable(false);


        add(idLabel);
        add(idField);
        add(nameLabel);
        add(nameField);
        add(descriptionLabel);
        add(descriptionField);
        add(costLabel);
        add(costField);
        add(viewBtn);
        add(addButton);
        add(recordCountLabel);
        add(recordCountField);
    }

    private class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (validateFields()) {
                String ID = idField.getText();
                String name = nameField.getText();
                String description = descriptionField.getText();
                double cost = Double.parseDouble(costField.getText());

                String productInfo = String.format("| %-15s | %-15s | %-10s | %-15s |", ID, name, description, cost);
                try (RandomAccessFile file = new RandomAccessFile("ProductData.txt", "rw")) {
                    file.seek(file.length());
                    file.writeUTF(productInfo);
                    recordCount++;
                    recordCountField.setText(Integer.toString(recordCount));
                    clearFields();
                    JOptionPane.showMessageDialog(null, "Record Added Successfully!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please fill all fields correctly!");
            }
        }
    }

    private boolean validateFields() {
        return !idField.getText().isEmpty() &&
                !nameField.getText().isEmpty() &&
                !descriptionField.getText().isEmpty() &&
                !costField.getText().isEmpty();
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        descriptionField.setText("");
        costField.setText("");
    }

    private class ViewBtnListener implements ActionListener {
        private void readAndDisplayFile(File file) {
            try (Scanner fileScanner = new Scanner(file)) {
                System.out.println("Product data from file: ");
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                readAndDisplayFile(selectedFile);
            } else {
                System.out.println("File selection incomplete..");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RandProductMaker gui = new RandProductMaker();
                gui.setVisible(true);
            }
        });
    }
}


