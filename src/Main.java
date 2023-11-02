import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

interface CalculatorService extends Remote {
    double add(double num1, double num2) throws RemoteException;
    double subtract(double num1, double num2) throws RemoteException;
    double multiply(double num1, double num2) throws RemoteException;
    double divide(double num1, double num2) throws RemoteException;
}

class CalculatorServiceImpl extends UnicastRemoteObject implements CalculatorService {
    public CalculatorServiceImpl() throws RemoteException {
        super();
    }

    public double add(double num1, double num2) throws RemoteException {
        return num1 + num2;
    }

    public double subtract(double num1, double num2) throws RemoteException {
        return num1 - num2;
    }

    public double multiply(double num1, double num2) throws RemoteException {
        return num1 * num2;
    }

    public double divide(double num1, double num2) throws RemoteException {
        return num1 / num2;
    }
}

class CalculatorClient extends JFrame {
    private CalculatorService calculator;

    private JTextField num1Field, num2Field, resultField;
    private JButton addButton, subtractButton, multiplyButton, divideButton;

    CalculatorClient() {
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLayout(new GridLayout(5, 2));

        num1Field = new JTextField();
        num2Field = new JTextField();
        resultField = new JTextField();
        resultField.setEditable(false);

        addButton = new JButton("+");
        subtractButton = new JButton("-");
        multiplyButton = new JButton("*");
        divideButton = new JButton("/");

        addButton.addActionListener(new OperationButtonListener("+"));
        subtractButton.addActionListener(new OperationButtonListener("-"));
        multiplyButton.addActionListener(new OperationButtonListener("*"));
        divideButton.addActionListener(new OperationButtonListener("/"));

        add(new JLabel("Number 1:"));
        add(num1Field);
        add(new JLabel("Number 2:"));
        add(num2Field);
        add(new JLabel("Result:"));
        add(resultField);
        add(addButton);
        add(subtractButton);
        add(multiplyButton);
        add(divideButton);

        try {
            calculator = (CalculatorService) Naming.lookup("rmi://localhost/CalculatorService");
        } catch (Exception e) {
            e.printStackTrace();
        }

        setVisible(true);
    }

    private class OperationButtonListener implements ActionListener {
        private String operator;

        OperationButtonListener(String operator) {
            this.operator = operator;
        }

        public void actionPerformed(ActionEvent e) {
            try {
                double num1 = Double.parseDouble(num1Field.getText());
                double num2 = Double.parseDouble(num2Field.getText());
                double result = 0.0;

                switch (operator) {
                    case "+":
                        result = calculator.add(num1, num2);
                        break;
                    case "-":
                        result = calculator.subtract(num1, num2);
                        break;
                    case "*":
                        result = calculator.multiply(num1, num2);
                        break;
                    case "/":
                        result = calculator.divide(num1, num2);
                        break;
                }

                resultField.setText(String.valueOf(result));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CalculatorClient();
            }
        });
    }
}
