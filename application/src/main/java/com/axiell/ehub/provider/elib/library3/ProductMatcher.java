package com.axiell.ehub.provider.elib.library3;

import java.util.List;
import com.axiell.ehub.provider.elib.library3.BookAvailability.Product;

abstract class ProductMatcher {
    private final List<Product> products;

    ProductMatcher(List<Product> products) {
        this.products = products;
    }

    boolean matches(String productId) {
        if (products == null)
            return false;
        for (Product product : products) {
            if (productId.equals(product.getProductId()))
                return condition(product);
        }
        return false;
    }

    abstract boolean condition(Product product);
}
