package com.gigaspaces.enumtest.common;

import com.gigaspaces.annotation.pojo.*;
import com.gigaspaces.metadata.StorageType;
import com.gigaspaces.metadata.index.SpaceIndexType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SpaceClass
public class Cart implements Serializable {

  private String user;
  private Long id;
  private List<LineItem> lineItems;
  private ShippingAddress shippingAddress;
  private BillingAddress billingAddress;
  private Cost prize;
  private CartStatus status;

  @SpaceIndex(type = SpaceIndexType.BASIC, unique = false)
  public CartStatus getStatus() {
    return status;
  }

  public void setStatus(CartStatus status) {
    this.status = status;
  }

  public enum CartStatus {
    IN_PROGRESS, COMPLETED, DOWNTIME_IN_PROGRESS, DOWNTIME_PENDING, DOWNTIME_COMPLETED, DOWNTIME_EXCEPTION
  }

  public Cart() {
    lineItems = new ArrayList<>();
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  @SpaceId
  @SpaceRouting
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @SpaceStorageType(storageType = StorageType.BINARY)
  public ShippingAddress getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(ShippingAddress shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  @SpaceStorageType(storageType = StorageType.BINARY)
  public BillingAddress getBillingAddress() {
    return billingAddress;
  }

  public void setBillingAddress(BillingAddress billingAddress) {
    this.billingAddress = billingAddress;
  }

  @SpaceStorageType(storageType = StorageType.BINARY)
  public Cost getPrize() {
    return prize;
  }

  public void setPrize(Cost prize) {
    this.prize = prize;
  }

  public List<LineItem> getLineItems() {
    return lineItems;
  }

  public void setLineItems(List<LineItem> lineItems) {
    this.lineItems = lineItems;
  }

  public boolean dropLineItem(String lineItemId) {
    boolean rv = false;
    LineItem lineItemToBeRemoved = null;
    for (LineItem lineItem : lineItems) {
      if (lineItemId.equals(lineItem.getId())) {
        lineItemToBeRemoved = lineItem;
        break;
      }
    }
    if (lineItemToBeRemoved != null) {
      lineItems.remove(lineItemToBeRemoved);
      rv = true;
    }
    return rv;
  }

  public void addLineItem(LineItem lineItem) {
    lineItems.add(lineItem);
  }

}
