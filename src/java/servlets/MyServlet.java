/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entity.Customer;
import entity.Item;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import session.CustomerFacade;
import session.ItemFacade;


/**
 *
 * @author Melnikov
 */
@WebServlet(name = "MyServlet", urlPatterns = {
    "/addCustomer",
    "/createCustomer",
    "/editCustomerForm",
    "/editCustomer",
    "/listCustomer",
    "/index",
    "/addItem",
    "/createItem",
    "/editItemForm",
    "/choiceItem",
    "/editItem",
    "/listItem",
    "/addMoneyForm",
    "/customerChoice",
    "/addMoney",
    "/buyItemForm",
    "/buyItem"
})
public class MyServlet extends HttpServlet {
    @EJB
    private CustomerFacade customerFacade;
    @EJB
    private ItemFacade itemFacade;
    
    private List<Customer> listCustomer;
    private List<Item> listItem;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        String path = request.getServletPath();
        switch (path) {
            case "/index":
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                break;
            case "/addCustomer":
                request.getRequestDispatcher("/WEB-INF/addCustomerForm.jsp").forward(request, response);
                break;
            case "/createCustomer":
                String name = request.getParameter("name");
                String surname = request.getParameter("surname");
                String phone = request.getParameter("phone");
                String email = request.getParameter("email");
                Customer customer = new Customer(name, surname, phone, email, 0);
                customerFacade.create(customer);
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                break;
            case "/editCustomerForm":
                listCustomer = customerFacade.findAll();
                request.setAttribute("listCustomer", listCustomer);
                request.getRequestDispatcher("/WEB-INF/editCustomerForm.jsp").forward(request, response);
                break;
            case "/editCustomer":
                String id = request.getParameter("customer");
                name = request.getParameter("name");
                surname = request.getParameter("surname");
                phone = request.getParameter("phone");
                email = request.getParameter("email");
                
                customer = customerFacade.find(Long.parseLong(id));
                customer.setName(name);
                customer.setSurname(surname);
                customer.setPhone(phone);
                customer.setEmail(email);
                customerFacade.edit(customer);

                request.getRequestDispatcher("/index.jsp").forward(request, response);
                break;
            case "/listCustomer":
                List<Customer> listCustomers = customerFacade.findAll();
                request.setAttribute("listCustomers", listCustomers);
                request.getRequestDispatcher("/WEB-INF/listCustomer.jsp").forward(request, response);
                break;
            case "/addItem":
                request.getRequestDispatcher("/WEB-INF/addItemForm.jsp").forward(request, response);
                break;
            case "/createItem":
                name = request.getParameter("name");
                double price = Double.parseDouble(request.getParameter("price"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                Item item = new Item(name, price, quantity);
                itemFacade.create(item);
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                break;
            case "/editItemForm":
                listItem = itemFacade.findAll();
                request.setAttribute("listItem", listItem);
                request.getRequestDispatcher("/WEB-INF/editItemForm.jsp").forward(request, response);
                break;
            case "/choiceItem":
                String value = request.getParameter("itemId");;
                item = itemFacade.find(Long.parseLong(value));               
                request.setAttribute("listItem", listItem);    
                request.setAttribute("item", item);
                request.getRequestDispatcher("/WEB-INF/editItemForm.jsp").forward(request, response);
                break;
            case "/editItem":
                id = request.getParameter("itemID");
                name = request.getParameter("name");
                price = Double.parseDouble(request.getParameter("price"));
                quantity = Integer.parseInt(request.getParameter("quantity"));
                
                item = itemFacade.find(Long.parseLong(id)); 
                item.setName(name);
                item.setPrice(price);
                item.setQuantity(quantity);
                itemFacade.edit(item);

                request.getRequestDispatcher("/index.jsp").forward(request, response);
                break;
            case "/listItem":
                listItem = itemFacade.findAll();
                request.setAttribute("listItem", listItem);
                request.getRequestDispatcher("/WEB-INF/listItem.jsp").forward(request, response);
                break;
            case "/addMoneyForm":
                listCustomer = customerFacade.findAll();
                request.setAttribute("listCustomer", listCustomer);
                request.getRequestDispatcher("/WEB-INF/addMoneyForm.jsp").forward(request, response);
                break;
            case "/customerChoice":
                value = request.getParameter("customerID");
                customer = customerFacade.find(Long.parseLong(value));
                request.setAttribute("listCustomer", listCustomer);
                request.setAttribute("customer", customer);
                request.getRequestDispatcher("/WEB-INF/addMoneyForm.jsp").forward(request, response);
            case "/addMoney":
                id = request.getParameter("customerID");
                Double money = Double.parseDouble(request.getParameter("money"));
                customer = customerFacade.find(Long.parseLong(id));
                
                customer.setMoney(customer.getMoney() + money);
                customerFacade.edit(customer);
                
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            case "/buyItemForm":
                listCustomer = customerFacade.findAll();
                listItem = itemFacade.findAll();
                request.setAttribute("listCustomer", listCustomer);
                request.setAttribute("listItem", listItem);
                request.getRequestDispatcher("/WEB-INF/buyItemForm.jsp").forward(request, response);
                break;
            case "/buyItem":
                String customerID = request.getParameter("customerID");
                String itemID = request.getParameter("itemID");
                customer = customerFacade.find(Long.parseLong(customerID));
                item = itemFacade.find(Long.parseLong(itemID));
                if (customer.getMoney() - item.getPrice() > 0){
                    item.setQuantity(item.getQuantity() - 1);
                    customer.setMoney(customer.getMoney() - item.getPrice());
                    customerFacade.edit(customer);
                    itemFacade.edit(item);
                    request.setAttribute("info", "Куплент товар: " + item.toString());
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                } else {
                    request.setAttribute("info", "Недостаточно денег");
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                }
                break;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
