package com.submarine.billing;

/**
 * Created by sargis on 11/3/14.
 */
public class StoreListenerAdapter implements StoreListener {
    @Override
    public void productsReceived() {

    }

    @Override
    public void productsRequestFailed(Error error) {

    }

    @Override
    public void transactionCompleted(String productId, String receiptAsString) {

    }

    @Override
    public void transactionFailed(Error error) {

    }

    @Override
    public void transactionRestored(String productId) {

    }

    @Override
    public void transactionRestoreCompleted() {

    }

    @Override
    public void transactionRestoreFailed(Error error) {

    }
}
