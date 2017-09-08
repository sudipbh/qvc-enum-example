package com.gigaspaces.enumtest;

import com.gigaspaces.enumtest.common.Cart;
import com.gigaspaces.enumtest.space.CartService;
import com.google.gson.Gson;
import com.j_spaces.core.client.SQLQuery;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.cluster.ClusterInfo;
import org.openspaces.core.space.UrlSpaceConfigurer;
import org.openspaces.pu.container.ProcessingUnitContainer;
import org.openspaces.pu.container.integrated.IntegratedProcessingUnitContainerProvider;

public class EnumTest {

  public static final String SHOPPINGCART_SPACE_URL = "/./shoppingcart-space";
  public static final String LOOKUP_GROUPS = "testgroup";

  private static ProcessingUnitContainer container;

  @BeforeClass
  public static void initializeContainer() {
    // create container
    IntegratedProcessingUnitContainerProvider provider = new IntegratedProcessingUnitContainerProvider();
    ClusterInfo clusterInfo = new ClusterInfo();
    clusterInfo.setSchema("partitioned");
    clusterInfo.setNumberOfInstances(1);
    clusterInfo.setNumberOfBackups(0);
    clusterInfo.setInstanceId(1);
    provider.setClusterInfo(clusterInfo);
    container = provider.createContainer();
  }

  @Test
  public void testEnum() {
    UrlSpaceConfigurer urlConfigurer = new UrlSpaceConfigurer(SHOPPINGCART_SPACE_URL);
    urlConfigurer.lookupGroups(LOOKUP_GROUPS);
    GigaSpace gigaSpace = new GigaSpaceConfigurer(urlConfigurer).clustered(true).gigaSpace();
    CartService cartService = new CartService(gigaSpace);
    String cart101Json = "{'id':101,'user':'sudip','status':1,'shippingAddress':{'street':'beekman road','city':'kendall park','country':'usa','type':'residence'},'billingAddress':{'street':'5th avenue','city':'new york','country':'usa','billedParty':'gigaspaces'},'prize':{'name':'christmas surprise','amount':100},'items':[{'name':'book','count':1,'discounts':[{'name':'d1','amount':5},{'name':'d2','amount':2}]},{'name':'toy','count':4,'discounts':[{'name':'d1','amount':7},{'name':'d2','amount':4}]}]}";
    cartService.createCart(101, cart101Json, Cart.CartStatus.COMPLETED);
    String cart102Json = "{'id':102,'user':'sudip','shippingAddress':{'street':'beekman road','city':'kendall park','country':'usa','type':'residence'},'billingAddress':{'street':'5th avenue','city':'new york','country':'usa','billedParty':'gigaspaces'},'prize':{'name':'christmas surprise','amount':100},'items':[{'name':'book','count':1,'discounts':[{'name':'d1','amount':5},{'name':'d2','amount':2}]},{'name':'toy','count':4,'discounts':[{'name':'d1','amount':7},{'name':'d2','amount':4}]}]}";
    cartService.createCart(102, cart102Json, Cart.CartStatus.IN_PROGRESS);
    String cart103Json = "{'id':103,'user':'sudip','shippingAddress':{'street':'beekman road','city':'kendall park','country':'usa','type':'residence'},'billingAddress':{'street':'5th avenue','city':'new york','country':'usa','billedParty':'gigaspaces'},'prize':{'name':'christmas surprise','amount':100},'items':[{'name':'book','count':1,'discounts':[{'name':'d1','amount':5},{'name':'d2','amount':2}]},{'name':'toy','count':4,'discounts':[{'name':'d1','amount':7},{'name':'d2','amount':4}]}]}";
    cartService.createCart(103, cart102Json, Cart.CartStatus.DOWNTIME_IN_PROGRESS);
    String cart104Json = "{'id':104,'user':'sudip','shippingAddress':{'street':'beekman road','city':'kendall park','country':'usa','type':'residence'},'billingAddress':{'street':'5th avenue','city':'new york','country':'usa','billedParty':'gigaspaces'},'prize':{'name':'christmas surprise','amount':100},'items':[{'name':'book','count':1,'discounts':[{'name':'d1','amount':5},{'name':'d2','amount':2}]},{'name':'toy','count':4,'discounts':[{'name':'d1','amount':7},{'name':'d2','amount':4}]}]}";
    cartService.createCart(104, cart102Json, Cart.CartStatus.COMPLETED);
    System.out.println("List of carts with status COMPLETED:");
    SQLQuery<Cart> query = new SQLQuery<>(Cart.class, "status = ? ORDER by id").setParameter(1, Cart.CartStatus.COMPLETED);
    Cart[] results = gigaSpace.readMultiple(query);
    for (Cart cart : results) {
      Cart.CartStatus status = cart.getStatus();
      Gson gson = new Gson();
      String cartJson = gson.toJson(cart);
      System.out.printf("cartJson = %s\n", cartJson);
      System.out.printf("Cart Status : %s\n", cart.getStatus().name());
      System.out.flush();
    }
  }

  @AfterClass
  public static void cleanupContainer() {
    // close container
    container.close();
  }

}
