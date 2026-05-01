import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

public class RestaurantOrderBillingSystem extends JFrame {

    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);

    DefaultTableModel model;
    JLabel totalLabel;
    JComboBox<String> tableBox;

    JTextField nameField, phoneField;

    double total = 0;

    String[] items = {"Burger", "Pizza", "Fried Rice", "Coke", "Pasta", "Chicken Fry"};
    double[] prices = {250, 500, 100, 20, 200, 150};

    String[] images = {
            "burger.jpeg",
            "pizza.jpeg",
            "friedrice.jpeg",
            "coke.jpeg",
            "pasta.jpeg",
            "chickenfry.jpeg"
    };

    public RestaurantOrderBillingSystem() {
        setTitle("SmartDine Restaurant System");
        setSize(1300, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.add(createFrontPage(), "front");
        mainPanel.add(createOrderPage(), "order");

        add(mainPanel);

        cardLayout.show(mainPanel, "front");
    }

    // ---------------- FRONT PAGE ----------------
    JPanel createFrontPage() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(30, 30, 30));

        JLabel title = new JLabel("Welcome to SmartDine");
        title.setFont(new Font("Arial", Font.BOLD, 50));
        title.setForeground(Color.WHITE);

        JButton enterBtn = new JButton("Click here");
        enterBtn.setFont(new Font("Arial", Font.BOLD, 18));
        enterBtn.addActionListener(e -> cardLayout.show(mainPanel, "order"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        panel.add(title, gbc);

        gbc.gridy = 1;
        panel.add(Box.createVerticalStrut(20), gbc);

        gbc.gridy = 2;
        panel.add(enterBtn, gbc);

        return panel;
    }

    // ---------------- ORDER PAGE ----------------
    JPanel createOrderPage() {
        JPanel panel = new JPanel(new BorderLayout());

        // CUSTOMER INFO PANEL
        JPanel customerPanel = new JPanel();
        customerPanel.setBorder(BorderFactory.createTitledBorder("Customer Information"));

        nameField = new JTextField(10);
        phoneField = new JTextField(10);

        tableBox = new JComboBox<>(new String[]{"Table 1", "Table 2", "Table 3", "Table 4", "Table 5"});

        customerPanel.add(new JLabel("Name:"));
        customerPanel.add(nameField);
        customerPanel.add(new JLabel("Phone:"));
        customerPanel.add(phoneField);
        customerPanel.add(new JLabel("Table:"));
        customerPanel.add(tableBox);

        // MENU PANEL
        JPanel menuPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        menuPanel.setBorder(BorderFactory.createTitledBorder("Menu Items"));

        for (int i = 0; i < items.length; i++) {
            menuPanel.add(createItemCard(i));
        }

        // TABLE
        model = new DefaultTableModel(new String[]{"Item", "Qty", "Price"}, 0);
        JTable table = new JTable(model);

        // BOTTOM
        JPanel bottom = new JPanel();

        totalLabel = new JLabel("Total: 0 Tk");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton billBtn = new JButton("Generate Bill");

        billBtn.addActionListener(e -> generateBill());

        bottom.add(totalLabel);
        bottom.add(billBtn);

        panel.add(customerPanel, BorderLayout.NORTH);
        panel.add(menuPanel, BorderLayout.WEST);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    // ---------------- ITEM CARD ----------------
    JPanel createItemCard(int index) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        ImageIcon icon = loadImage(images[index]);
        if (icon != null) {
            Image img = icon.getImage().getScaledInstance(140, 110, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        } else {
            imgLabel.setText("No Image");
        }

        JLabel name = new JLabel(items[index], SwingConstants.CENTER);
        name.setFont(new Font("Arial", Font.BOLD, 35));

        JLabel price = new JLabel("Price: " + prices[index] + " Tk", SwingConstants.CENTER);

        JSpinner qty = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));

        JButton addBtn = new JButton("Add");

        addBtn.addActionListener(e -> addOrder(index, (int) qty.getValue()));

        card.add(imgLabel);
        card.add(name);
        card.add(price);
        card.add(new JLabel("Qty:"));
        card.add(qty);
        card.add(addBtn);

        return card;
    }

    // ---------------- LOAD IMAGE ----------------
    ImageIcon loadImage(String path) {
        File file = new File(path);
        if (!file.exists()) return null;
        return new ImageIcon(path);
    }

    // ---------------- ADD ORDER ----------------
    void addOrder(int index, int qty) {
        double price = prices[index] * qty;
        total += price;

        model.addRow(new Object[]{items[index], qty, price});
        totalLabel.setText("Total: " + total + " Tk");
    }

    // ---------------- BILL ----------------
    void generateBill() {
        double vat = total * 0.1;
        double finalTotal = total + vat;

        JOptionPane.showMessageDialog(this,
                "Customer Name: " + nameField.getText() +
                        "\nPhone: " + phoneField.getText() +
                        "\nTable: " + tableBox.getSelectedItem() +
                        "\nSubtotal: " + total + " Tk" +
                        "\nVAT (10%): " + vat + " Tk" +
                        "\nTotal: " + finalTotal + " Tk"
        );
    }

    // ---------------- MAIN ----------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RestaurantOrderBillingSystem().setVisible(true));
    }
}
