package com.digitalprimaryschool.digitalprimaryschool.model;

import java.time.Instant;

public abstract class BaseModel {
    private SyncStatus syncStatus = SyncStatus.PENDING_REMOTELY;
    private Long updatedAt;

    public BaseModel() {
        this.touch();
    }

    public void touch() {
        this.updatedAt = Instant.now().toEpochMilli();
    }

    public SyncStatus getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(SyncStatus syncStatus) {
        this.syncStatus = syncStatus;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}