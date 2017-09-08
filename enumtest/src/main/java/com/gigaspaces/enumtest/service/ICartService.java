package com.gigaspaces.enumtest.service;

import com.gigaspaces.enumtest.common.Cart;

public interface ICartService {

  Cart getCart(long cartId);

  boolean createCart(long cartId, String cartJson, Cart.CartStatus cartStatus);

}
