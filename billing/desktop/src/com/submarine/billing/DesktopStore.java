package com.submarine.billing;


import com.badlogic.gdx.utils.Array;
import com.submarine.billing.product.Product;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by sargis on 10/30/14.
 */
public class DesktopStore implements Store {
    private CopyOnWriteArrayList<StoreListener> storeListeners;
    private Map<String, Product> products;

    public DesktopStore() {
        products = new HashMap<String, Product>();
        storeListeners = new CopyOnWriteArrayList<>();
    }

    @Override
    public void requestProducts(String[] productIds) {
        float price = 0.99f;
        for (String productId : productIds) {
            Product product = new Product();
            product.id = productId;
            product.currency = "USD";
            product.price = price;
            products.put(productId, product);
            price += 1f;
        }
    }

    @Override
    public void requestProducts(Array<Product> productList) {
        for (Product product : productList) {
            products.put(product.id, product);
        }
    }

    @Override
    public boolean isInitialized() {
        return false;
    }

    @Override
    public boolean isPurchased(String id) {
        return false;
    }

    @Override
    public void restoreTransactions() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep((long) (Math.random() * 2500));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Math.random() > .3f) {
                    for (StoreListener storeListener : storeListeners) {
                        for (Product product : products.values()) {
                            storeListener.transactionRestored(product.id);
                        }
                    }
                } else {
                    for (StoreListener storeListener : storeListeners) {
                        for (Product product : products.values()) {
                            storeListener.transactionFailed(new Error(product.id));
                        }
                    }
                }

            }
        }).start();

    }

    @Override
    public void purchaseProduct(final String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep((long) (Math.random() * 2500));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Math.random() > .3f) {
                    for (StoreListener storeListener : storeListeners) {
                        storeListener.transactionCompleted(id);
                    }
                } else {
                    for (StoreListener storeListener : storeListeners) {
                        storeListener.transactionFailed(new Error(id));
                    }
                }

            }
        }).start();
    }

    @Override
    public void consumePurchase(String id) {

    }

    @Override
    public void initialize() {

    }

    @Override
    public Product getProductById(String id) {
        return products.get(id);
    }

    @Override
    public void addListener(StoreListener storeListener) {
        storeListeners.add(storeListener);
    }

    @Override
    public void removeListener(StoreListener storeListener) {
        storeListeners.remove(storeListener);
    }


}
