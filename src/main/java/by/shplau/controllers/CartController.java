package by.shplau.controllers;

import by.shplau.services.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add/{productId}")
    public ResponseEntity<String> addToCart(@PathVariable Long id, HttpSession session) {
        cartService.addToCart(session, id);
        return ResponseEntity.ok("added to cart");
    }

//    @PostMapping("/checkout")
//    public ResponseEntity<String> checkout(
//            @AuthenticationPrincipal UserDetails userDetails,
//            HttpSession session) {
//        @SuppressWarnings("unchecked")
//        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
//        if (cart == null || cart.isEmpty()) {
//            return ResponseEntity.badRequest().body("Cart is empty");
//
//            cartService.checkout(userDetails.getUsername(), cart);
//            session.removeAttribute("cart");
//
//            return ResponseEntity.ok("Checkout is succesful");
//        }
//    }
}
