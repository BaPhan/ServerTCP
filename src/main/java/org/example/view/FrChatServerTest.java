package org.example.view;

import org.example.dao.CountryDao;
import org.example.dao.WineDao;
import org.example.domain.Country;
import org.example.domain.Wine;
import org.example.dto.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class FrChatServerTest extends JFrame {
    private JTextField portTextField;
    private JButton startButton;
    private JTextArea serverInfoTextArea;
    private JTextField chatTextField;
    private JButton sendButton;
    private ServerSocket myServer;
    private Socket clientSocket;

    public FrChatServerTest() {
        // Thiết lập các thuộc tính của JFrame
        setTitle("Server Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Đưa frame vào giữa màn hình

        // Tạo và thiết lập layout cho JFrame
        setLayout(new BorderLayout());

        // Tạo các thành phần
        portTextField = new JTextField(20);
        startButton = new JButton("Start");
        serverInfoTextArea = new JTextArea();
        chatTextField = new JTextField();
        sendButton = new JButton("Send");

        // Đặt layout cho các thành phần
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Port: "));
        topPanel.add(portTextField);
        topPanel.add(startButton);

        // Đặt các thành phần vào các vùng của layout
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(serverInfoTextArea), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(chatTextField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // Thêm sự kiện cho nút "Start"
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Thực hiện các hành động khi nút "Start" được nhấn
                String portText = portTextField.getText();
                // Xử lý logic để bắt đầu server với cổng được nhập
                serverInfoTextArea.append("Server started on port " + portText + "\n");
                try {
                    myServer = new ServerSocket(Integer.parseInt(portText));
                } catch (Exception e1) {
                    serverInfoTextArea.append(e1.toString());

                }
                // listenning
                while (true) {
                    try {
                        clientSocket = myServer.accept();
                        serverInfoTextArea.append("Client connected:" + clientSocket.getInetAddress());
                        try (ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream())) {
                            // Đọc dữ liệu từ client
                            Object receivedData = objectInputStream.readObject();
                            if (receivedData instanceof Method) {
                                Method method = (Method) receivedData;
                                String med = method.getMethod();
                                if ("GetAll".equals(med)){
                                    //xu ly lay all data
                                    handleGetAllData();
                                } else if ("Add".equals(med)) {
                                    // xu ly them data
                                    handleAddData(method);
                                } else if ("Update".equals(med)) {
                                    handleUpdate(method);
                                } else if ("Delete".equals(med)) {
                                    handleDelete(method);
                                } else if ("Search".equals(med)) {
                                    handleSearch(method);
                                } else if ("AddWine".equals(med)) {
                                    // xu ly them data
                                    handleAddWine(method);
                                } else if ("UpdateWine".equals(med)) {
                                    handleUpdateWine(method);
                                } else if ("DeleteWine".equals(med)) {
                                    handleDeleteWine(method);
                                } else if ("SearchWine".equals(med)) {
                                    handleSearchWine(method);
                                } else if ("GetAllWine".equals(med)) {
                                    handleGetAllDataWine();
                                }
                            }
                        } catch (Exception e2) {
                            serverInfoTextArea.append(e2.toString());
                        }
                    } catch (Exception exception) {
                        serverInfoTextArea.append(exception.toString());
                    }
                }
            }
        });

        // Thêm sự kiện cho nút "Send"
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Thực hiện các hành động khi nút "Send" được nhấn
                String message = chatTextField.getText();
                // Xử lý logic để gửi tin nhắn
                serverInfoTextArea.append("Server: " + message + "\n");
                chatTextField.setText(""); // Xóa nội dung trong ô nhập tin nhắn
            }
        });
    }

    private void handleAddData(Method method) {
        handleReceivedData(method);
    }

    private void handleReceivedData(Method method) {
            CountryAddDTO countryAddDTO = (CountryAddDTO) method.getData();
            CountryDao countryDao = new CountryDao();
            Country country = new Country();
            country.setId(countryAddDTO.getId());
            country.setName(countryAddDTO.getName());
            country.setDescription(countryAddDTO.getDescription());
            countryDao.addCountry(country);
            boolean success = true;

            //Tao doi tuong tra ve cho client
            ServerResponse response = new ServerResponse();
            response.setSuccess(success);
            if (success) {
                response.setData("User added successfully");
            } else {
                response.setErrorMessage("Error adding country to the database");
            }

            // Trả về thông điệp cho client
            sendResponseToClient(response);
    }

    private void sendResponseToClient(ServerResponse response) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream())) {
            objectOutputStream.writeObject(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleGetAllData() {
        try {
            // Đọc dữ liệu từ client
                CountryDao countryDao = new CountryDao();
                List<Country> countries = countryDao.getAllCountry();
                List<CountryDTO> countryDTOS = new ArrayList<>();
                for (Country country: countries){
                    CountryDTO countryDTO = new CountryDTO(country.getId(),country.getName(),country.getDescription());
                    countryDTOS.add(countryDTO);
                }
                boolean success = true;
                //Tao doi tuong tra ve cho client
                ServerResponse response = new ServerResponse();
                response.setSuccess(success);
                if (success) {
                    response.setData(countryDTOS);
                } else {
                    response.setErrorMessage("Error adding country to the database");
                }
                // Trả về thông điệp cho client
                sendResponseToClient(response);
        } catch (Exception e2) {
            serverInfoTextArea.append(e2.toString());
        }
    }

    private void handleUpdate(Method method) {
        CountryDTO countryDTO = (CountryDTO) method.getData();
        CountryDao countryDao = new CountryDao();
        Country country = new Country();
        country.setId(countryDTO.getId());
        country.setName(countryDTO.getName());
        country.setDescription(countryDTO.getDescription());
        countryDao.updateCountry(country);
        boolean success = true;

        //Tao doi tuong tra ve cho client
        ServerResponse response = new ServerResponse();
        response.setSuccess(success);
        if (success) {
            response.setData("User update successfully");
        } else {
            response.setErrorMessage("Error updating country to the database");
        }

        // Trả về thông điệp cho client
        sendResponseToClient(response);
    }
    private void handleDelete(Method method) {
        Integer id = (Integer) method.getData();
        CountryDao countryDao = new CountryDao();
        WineDao wineDao = new WineDao();
        Wine wine = wineDao.getWineByCountryId(id);
        if (wine != null){
            wineDao.deleteWine(wine.getId());
        }
        countryDao.deleteCountry(id);
        boolean success = true;

        //Tao doi tuong tra ve cho client
        ServerResponse response = new ServerResponse();
        response.setSuccess(success);
        if (success) {
            response.setData("wine delete successfully");
        } else {
            response.setErrorMessage("Error delete country to the database");
        }

        // Trả về thông điệp cho client
        sendResponseToClient(response);
    }
    private void handleSearch(Method method) {
        String name = (String) method.getData();
        CountryDao countryDao = new CountryDao();
        Country country = countryDao.getCountryByName(name);
        CountryDTO countryDTO = new CountryDTO(country.getId(), country.getName(), country.getDescription());
        boolean success = true;

        //Tao doi tuong tra ve cho client
        ServerResponse response = new ServerResponse();
        response.setSuccess(success);
        if (success) {
            response.setData(countryDTO);
        } else {
            response.setErrorMessage("Error delete country to the database");
        }

        // Trả về thông điệp cho client
        sendResponseToClient(response);
    }
    /////////////////// Xu ly Wine
    private void handleGetAllDataWine() {
        try {
            // Đọc dữ liệu từ client
            WineDao wineDao = new WineDao();
            CountryDao countryDao = new CountryDao();
            List<Wine> wines = wineDao.getAllWine();
            List<WineDTO> wineDTOS = new ArrayList<>();
            for (Wine wine: wines){
                Country country = countryDao.getCountry(wine.getCountryId());
                WineDTO wineDTO = new WineDTO(wine.getId(),wine.getName(),wine.getConcentration(),wine.getYear(),country.getName());
                wineDTOS.add(wineDTO);
            }
            boolean success = true;
            //Tao doi tuong tra ve cho client
            ServerResponse response = new ServerResponse();
            response.setSuccess(success);
            if (success) {
                response.setData(wineDTOS);
            } else {
                response.setErrorMessage("Error adding country to the database");
            }
            // Trả về thông điệp cho client
            sendResponseToClient(response);
        } catch (Exception e2) {
            serverInfoTextArea.append(e2.toString());
        }
    }
    private void handleAddWine(Method method) {
        WineDTO wineDTO = (WineDTO) method.getData();
        WineDao wineDao = new WineDao();
        CountryDao countryDao = new CountryDao();
        // tim nuoc
        Country country = countryDao.getCountryByName(wineDTO.getCountryName());
        if (country == null){
            boolean success = false;
            ServerResponse response = new ServerResponse();
            response.setSuccess(success);
            if (success) {
                response.setData("User added successfully");
            } else {
                response.setErrorMessage("Error adding country to the database");
            }
            sendResponseToClient(response);
            return;
        }
        Wine wine = new Wine();
        wine.setId(wineDTO.getId());
        wine.setName(wineDTO.getName());
        wine.setConcentration(wineDTO.getConcentration());
        wine.setYear(wineDTO.getYear());
        wine.setCountryId(country.getId());
        wineDao.addWine(wine);
        boolean success = true;

        //Tao doi tuong tra ve cho client
        ServerResponse response = new ServerResponse();
        response.setSuccess(success);
        if (success) {
            response.setData("User added successfully");
        } else {
            response.setErrorMessage("Error adding country to the database");
        }

        // Trả về thông điệp cho client
        sendResponseToClient(response);
    }

    private void handleUpdateWine(Method method) {
        WineDTO wineDTO = (WineDTO) method.getData();
        WineDao wineDao = new WineDao();
        CountryDao countryDao = new CountryDao();
        // tim nuoc
        Country country = countryDao.getCountryByName(wineDTO.getCountryName());
        if (country == null){
            boolean success = false;
            ServerResponse response = new ServerResponse();
            response.setSuccess(success);
            if (success) {
                response.setData("User added successfully");
            } else {
                response.setErrorMessage("Error adding country to the database");
            }
            sendResponseToClient(response);
            return;
        }
        Wine wine = new Wine();
        wine.setId(wineDTO.getId());
        wine.setName(wineDTO.getName());
        wine.setConcentration(wineDTO.getConcentration());
        wine.setYear(wineDTO.getYear());
        wine.setCountryId(country.getId());
        wineDao.updateWine(wine);
        boolean success = true;

        //Tao doi tuong tra ve cho client
        ServerResponse response = new ServerResponse();
        response.setSuccess(success);
        if (success) {
            response.setData("User added successfully");
        } else {
            response.setErrorMessage("Error adding country to the database");
        }

        // Trả về thông điệp cho client
        sendResponseToClient(response);
    }
    private void handleDeleteWine(Method method) {
        Integer id = (Integer) method.getData();
        WineDao wineDao = new WineDao();
        wineDao.deleteWine(id);
        boolean success = true;

        //Tao doi tuong tra ve cho client
        ServerResponse response = new ServerResponse();
        response.setSuccess(success);
        if (success) {
            response.setData("wine delete successfully");
        } else {
            response.setErrorMessage("Error delete country to the database");
        }

        // Trả về thông điệp cho client
        sendResponseToClient(response);
    }

    private void handleSearchWine(Method method) {
        String name = (String) method.getData();
        WineDao wineDao = new WineDao();
        Wine wine = wineDao.getWineByName(name);
        CountryDao countryDao = new CountryDao();
        Country country = countryDao.getCountry(wine.getCountryId());
        WineDTO wineDTO = new WineDTO(wine.getId(),wine.getName(),wine.getConcentration(),wine.getYear(),country.getName());
        boolean success = true;

        //Tao doi tuong tra ve cho client
        ServerResponse response = new ServerResponse();
        response.setSuccess(success);
        if (success) {
            response.setData(wineDTO);
        } else {
            response.setErrorMessage("Error delete country to the database");
        }

        // Trả về thông điệp cho client
        sendResponseToClient(response);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FrChatServerTest().setVisible(true);
            }
        });
    }
}
