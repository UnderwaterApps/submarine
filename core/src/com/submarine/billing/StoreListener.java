package com.submarine.billing;

/**
 * Created by sargis on 10/27/14.
 */
public interface StoreListener {
    void productsReceived();

    void productsRequestFailed(Error error);

    void transactionCompleted(String productId);

    void transactionFailed(Error error);

    void transactionRestored(String productId);

    void transactionRestoreCompleted();

    void transactionRestoreFailed(Error error);

}
