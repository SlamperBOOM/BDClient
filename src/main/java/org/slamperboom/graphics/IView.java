package org.slamperboom.graphics;

public interface IView {
    void startingConnection();
    void connected();
    void failedConnection();
    String getPassword();
}
