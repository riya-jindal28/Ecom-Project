package com.ecomm.Project.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecomm.Project.Exception.APIException;
import com.ecomm.Project.Exception.ResourceNotFoundException;
import com.ecomm.Project.Model.Cart;
import com.ecomm.Project.Model.CartItems;
import com.ecomm.Project.Model.Product;
import com.ecomm.Project.Payload.CartDTO;
import com.ecomm.Project.Payload.ProductDTORequest;
import com.ecomm.Project.Repository.CartItemsRepository;
import com.ecomm.Project.Repository.CartRepository;
import com.ecomm.Project.Repository.ProductRepository;
import com.ecomm.Project.Util.AuthUtil;

import jakarta.transaction.Transactional;


@Service
public class CartServiceImpl implements CartService{
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemsRepository CartItemsRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        Cart cart  = createCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItems CartItems = CartItemsRepository.findCartItemsByProductIdAndCartId(cart.getCartId(), productId);

        if (CartItems != null) {
            throw new APIException("Product " + product.getProductName() + " already exists in the cart");
        }

        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        CartItems newCartItems = new CartItems();

        newCartItems.setProduct(product);
        newCartItems.setCart(cart);
        newCartItems.setQuantity(quantity);
        newCartItems.setDiscount(product.getDiscount());
        newCartItems.setProductprice(product.getSpecialPrice());

        CartItemsRepository.save(newCartItems);

        product.setQuantity(product.getQuantity());

        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));

        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItems> cartItems = cart.getCartItems();

        Stream<ProductDTORequest> productStream = cartItems.stream().map(item -> {
            ProductDTORequest map = modelMapper.map(item.getProduct(), ProductDTORequest.class);
            map.setQuantity(item.getQuantity());
            return map;
        });

        cartDTO.setProducts(productStream.toList());

        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        if (carts.size() == 0) {
            throw new APIException("No cart exists");
        }

        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<ProductDTORequest> products = cart.getCartItems().stream().map(CartItems -> {
                ProductDTORequest productDTO = modelMapper.map(CartItems.getProduct(), ProductDTORequest.class);
                productDTO.setQuantity(CartItems.getQuantity()); // Set the quantity from CartItems
                return productDTO;
            }).collect(Collectors.toList());


            cartDTO.setProducts(products);

            return cartDTO;

        }).collect(Collectors.toList());

        return cartDTOs;
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);
        if (cart == null){
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cart.getCartItems().forEach(c ->
                c.getProduct().setQuantity(c.getQuantity()));
        List<ProductDTORequest> products = cart.getCartItems().stream()
                .map(p -> modelMapper.map(p.getProduct(), ProductDTORequest.class))
                .toList();
        cartDTO.setProducts(products);
        return cartDTO;
    }

    @Transactional
    @Override
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {

        String emailId = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(emailId);
        Long cartId  = userCart.getCartId();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        CartItems CartItems = CartItemsRepository.findCartItemsByProductIdAndCartId(cartId, productId);

        if (CartItems == null) {
            throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
        }

        // Calculate new quantity
        int newQuantity = CartItems.getQuantity() + quantity;

        // Validation to prevent negative quantities
        if (newQuantity < 0) {
            throw new APIException("The resulting quantity cannot be negative.");
        }

        if (newQuantity == 0){
            deleteProductFromCart(cartId, productId);
        } else {
            CartItems.setProductprice(product.getSpecialPrice());
            CartItems.setQuantity(CartItems.getQuantity() + quantity);
            CartItems.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (CartItems.getProductprice() * quantity));
            cartRepository.save(cart);
        }

        CartItems updatedItem = CartItemsRepository.save(CartItems);
        if(updatedItem.getQuantity() == 0){
            CartItemsRepository.deleteById(updatedItem.getCartItemsID());
        }


        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItems> cartItems = cart.getCartItems();

        Stream<ProductDTORequest> productStream = cartItems.stream().map(item -> {
            ProductDTORequest prd = modelMapper.map(item.getProduct(), ProductDTORequest.class);
            prd.setQuantity(item.getQuantity());
            return prd;
        });


        cartDTO.setProducts(productStream.toList());

        return cartDTO;
    }


    private Cart createCart() {
        Cart userCart  = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if(userCart != null){
            return userCart;
        }

        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart =  cartRepository.save(cart);

        return newCart;
    }


    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        CartItems CartItems = CartItemsRepository.findCartItemsByProductIdAndCartId(cartId, productId);

        if (CartItems == null) {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }

        cart.setTotalPrice(cart.getTotalPrice() -
                (CartItems.getProductprice() * CartItems.getQuantity()));

        CartItemsRepository.deleteCartItemsByProductIdAndCartId(cartId, productId);

        return "Product " + CartItems.getProduct().getProductName() + " removed from the cart !!!";
    }


    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItems cartItems = CartItemsRepository.findCartItemsByProductIdAndCartId(cartId, productId);

        if (cartItems == null) {
            throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
        }

        double cartPrice = cart.getTotalPrice()
                - (cartItems.getProductprice() * cartItems.getQuantity());

        cartItems.setProductprice(product.getSpecialPrice());

        cart.setTotalPrice(cartPrice
                + (cartItems.getProductprice() * cartItems.getQuantity()));

        cartItems = CartItemsRepository.save(cartItems);
    }

}
