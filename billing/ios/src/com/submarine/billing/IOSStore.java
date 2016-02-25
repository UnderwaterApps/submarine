package com.submarine.billing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.submarine.billing.product.Product;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.foundation.NSErrorUserInfo;
import org.robovm.apple.storekit.SKPaymentTransaction;
import org.robovm.apple.storekit.SKProduct;
import org.robovm.apple.storekit.SKRequest;
import org.robovm.apple.uikit.UIAlertView;
import org.robovm.pods.billing.AppStoreListener;
import org.robovm.pods.billing.AppStoreManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by sargis on 10/27/14.
 */
public class IOSStore implements Store {
    private static final String TAG = "com.underwater.submarine.store.IOSStore";
    private final AppStoreManager appStoreManager;
    private final IOSAppStoreListener iOSAppStoreListener;
    private CopyOnWriteArrayList<StoreListener> storeListeners;
    private Map<String, SKProduct> appStoreProducts;
    private Map<String, Product> products;
    private UIAlertView uiAlertView;

    public IOSStore() {
        iOSAppStoreListener = new IOSAppStoreListener();
        appStoreManager = new AppStoreManager(iOSAppStoreListener);
        products = new HashMap<>();
        storeListeners = new CopyOnWriteArrayList<>();
    }

    @Override
    public void requestProducts(String[] productIds) {
        appStoreManager.requestProducts(productIds);
        Gdx.app.log(TAG, "requestProducts");
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
        return false;
    }

    @Override
    public boolean isPurchased(String id) {
        return false;
    }

    @Override
    public void restoreTransactions() {
        appStoreManager.restoreTransactions();
        showAlert("Processing...", "Cancel");
    }

    @Override
    public void purchaseProduct(String id) {
        if (appStoreProducts == null || !appStoreProducts.containsKey(id)) {
            iOSAppStoreListener.transactionFailed(new SKPaymentTransaction(), new NSError("item not available", 0, new NSErrorUserInfo()));
            return;
        }
        appStoreManager.purchaseProduct(appStoreProducts.get(id));
        showAlert("Processing...", "Cancel");
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

    private void showAlert(String title, String cancelButtonTitle) {
        uiAlertView = new UIAlertView(title, null, null, cancelButtonTitle);
        uiAlertView.show();
    }

    private void showAlert(String title) {
        showAlert(title, "OK");
    }

    private void hideAlert() {
        if (uiAlertView == null) {
            return;
        }
        uiAlertView.dismiss(0, true);
    }

    private class IOSAppStoreListener implements AppStoreListener {
        private static final String TAG = "com.underwater.clickers.store.IOSStore.IOSAppStoreListener";


        @Override
        public void productsReceived(List<SKProduct> skProducts) {
            Gdx.app.log(TAG, "productsReceived : " + skProducts);
            appStoreProducts = new HashMap<String, SKProduct>();
            for (SKProduct skProduct : skProducts) {
                appStoreProducts.put(skProduct.getProductIdentifier(), skProduct);
                Product product = new Product();
                product.price = skProduct.getPrice().floatValue();
                product.id = skProduct.getProductIdentifier();
                product.currency = skProduct.getPriceLocale().getLocaleIdentifier().split("=")[1];
                Gdx.app.log(TAG, "[" + product.id + "] : " + product);
                products.put(product.id, product);
            }
            for (StoreListener storeListener : storeListeners) {
                storeListener.productsReceived();
            }
        }

        @Override
        public void productsRequestFailed(SKRequest skRequest, NSError nsError) {
            //Gdx.app.log(TAG, "productsRequestFailed : " + nsError);
            for (StoreListener storeListener : storeListeners) {
                storeListener.productsRequestFailed(new Error(nsError.getLocalizedFailureReason()));
            }
        }

        @Override
        public void transactionCompleted(SKPaymentTransaction skPaymentTransaction) {
            Gdx.app.log(TAG, "transactionCompleted : " + skPaymentTransaction);
            hideAlert();
            // Purchase successfully completed.
            // Get the product identifier and award the product to the user.
            String productId = skPaymentTransaction.getPayment().getProductIdentifier();
            for (StoreListener storeListener : storeListeners) {
                storeListener.transactionCompleted(productId);
            }

        }

        @Override
        public void transactionFailed(SKPaymentTransaction skPaymentTransaction, NSError nsError) {
            Gdx.app.log(TAG, "transactionFailed : " + nsError);
            hideAlert();
            for (StoreListener storeListener : storeListeners) {
                storeListener.transactionFailed(new Error(nsError.getLocalizedFailureReason()));
            }
            showAlert("Can not connect to App Store.");
        }

        @Override
        public void transactionRestored(SKPaymentTransaction skPaymentTransaction) {
            Gdx.app.log(TAG, "transactionRestored : " + skPaymentTransaction);
            // Purchase successfully restored.
            // Get the product identifier and award the product to the user. This is only useful for non-consumable products.
            String productId = skPaymentTransaction.getPayment().getProductIdentifier();
            for (StoreListener storeListener : storeListeners) {
                storeListener.transactionRestored(productId);
            }
        }

        @Override
        public void transactionDeferred(SKPaymentTransaction skPaymentTransaction) {
            Gdx.app.log(TAG, "transactionDeferred : " + skPaymentTransaction);
        }

        @Override
        public void transactionRestoreCompleted(List<SKPaymentTransaction> list) {
            Gdx.app.log(TAG, "transactionRestoreCompleted : " + list.size());
            hideAlert();
            String title = "There are no items available to restore at this time.";
            if (list.size() > 0) {
                title = "All purchases successfully restored.";
            }
            for (StoreListener storeListener : storeListeners) {
                storeListener.transactionRestoreCompleted();
            }
            showAlert(title);
        }

        @Override
        public void transactionRestoreFailed(List<SKPaymentTransaction> list, NSError nsError) {
            Gdx.app.log(TAG, "transactionRestoreFailed : " + nsError);
            hideAlert();
            for (StoreListener storeListener : storeListeners) {
                storeListener.transactionRestoreFailed(new Error(nsError.getLocalizedFailureReason()));
            }
            showAlert("Can not connect to App Store.");
        }
    }
}


