package com.submarine.billing;

import android.app.Activity;
import android.content.Intent;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.submarine.billing.product.Product;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by sargis on 10/31/14.
 */
public class AndroidStore implements Store {

    private static final String TAG = "com.submarine.billing.AndroidStore";
    private final Activity activity;
    private final String licenseKey;
    private BillingProcessor billingProcessor;
    private CopyOnWriteArrayList<StoreListener> storeListeners;
    private String[] productIds;
    private Map<String, Product> products;

    public AndroidStore(Activity activity, String licenseKey) {
        this.activity = activity;
        this.licenseKey = licenseKey;
        products = new HashMap<String, Product>();
        storeListeners = new CopyOnWriteArrayList<StoreListener>();
    }

    @Override
    public void requestProducts(String[] productIds) {
        this.productIds = productIds;
        if (billingProcessor != null) {
            return;
        }
        billingProcessor = new BillingProcessor(activity, licenseKey, new BillingHandler());
        billingProcessor.loadOwnedPurchasesFromGoogle();
    }

    @Override
    public void requestProducts(Array<Product> productList) {
        for (Product product : productList) {
            products.put(product.id, product);
        }
        requestProducts(products.keySet().toArray(new String[products.size()]));
    }

    @Override
    public boolean isInitialized() {
        if (billingProcessor!=null) {
            return billingProcessor.isInitialized();
        } else {
            return false;
        }
    }

    @Override
    public boolean isPurchased(String id) {
        return billingProcessor.isPurchased(id);
    }

    @Override
    public void restoreTransactions() {

    }

    @Override
    public void purchaseProduct(String id) {
        billingProcessor.purchase(activity, id);
    }

    @Override
    public void consumePurchase(String id) {
        billingProcessor.consumePurchase(id);
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


    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        return billingProcessor.handleActivityResult(requestCode, resultCode, data);
    }

    public void release() {
        billingProcessor.release();
    }


    private class BillingHandler implements BillingProcessor.IBillingHandler {

        @Override
        public void onProductPurchased(String s, TransactionDetails transactionDetails) {
            //Gdx.app.log(TAG, "onProductPurchased : " + transactionDetails.orderId);
            for (StoreListener storeListener : storeListeners) {
                storeListener.transactionCompleted(transactionDetails.productId);
            }
        }

        @Override
        public void onPurchaseHistoryRestored() {
            //Gdx.app.log(TAG, "onPurchaseHistoryRestored");
            for (String productId : productIds) {
                TransactionDetails transactionDetails = billingProcessor.getPurchaseTransactionDetails(productId);
                if (transactionDetails != null) {
                    for (StoreListener storeListener : storeListeners) {
                        storeListener.transactionRestored(productId);
                    }
                }
            }
            for (StoreListener storeListener : storeListeners) {
                storeListener.transactionRestoreCompleted();
            }
        }

        @Override
        public void onBillingError(int i, Throwable throwable) {
            //Gdx.app.log(TAG, "onBillingError : " + i);
            for (StoreListener storeListener : storeListeners) {
                storeListener.transactionFailed(new Error(String.valueOf(i)));
            }
        }

        @Override
        public void onBillingInitialized() {
            //Gdx.app.log(TAG, "onBillingInitialized");
            if (billingProcessor == null) {
                return;
            }
            for (String productId : productIds) {
                SkuDetails skuDetails = billingProcessor.getPurchaseListingDetails(productId);
                if (skuDetails != null) {
                    Product product = new Product();
                    product.currency = skuDetails.currency;
                    product.price = skuDetails.priceValue.floatValue();
                    product.id = productId;
                    products.put(product.id, product);
                }
            }
            for (StoreListener storeListener : storeListeners) {
                storeListener.productsReceived();
            }
        }

    }
}
