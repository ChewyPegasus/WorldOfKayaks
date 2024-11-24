package by.shplau.services;

import by.shplau.repositories.ProductRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    @Autowired
    private ProductRepository productRepository;

//    @Autowired
//    private EmailService emailService;

    public void addToCart(HttpSession session, Long productId) {
        Map<Long, Integer> cart = getCartFromSession(session);
        cart.merge(productId, 1, Integer::sum);
        session.setAttribute("cart", cart);
    }

//    public void checkout(String email, Map<Long, Integer> cart) {
//        Order order = createOrder(cart);
//        String emailContent = generateOrderEmail(order);
//        emailService.sendOrderConfirmation(email, emailContent);
//    }

    private Map<Long, Integer> getCartFromSession(HttpSession session) {
        return Optional.ofNullable((Map<Long, Integer>)session.getAttribute("cart"))
                .orElse(new HashMap<>());
    }
}
