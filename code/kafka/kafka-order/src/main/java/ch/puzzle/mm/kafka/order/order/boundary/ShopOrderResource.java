package ch.puzzle.mm.kafka.order.order.boundary;

import ch.puzzle.mm.kafka.order.order.control.ShopOrderService;
import ch.puzzle.mm.kafka.order.order.entity.ShopOrder;
import ch.puzzle.mm.kafka.order.order.entity.ShopOrderDTO;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("/shop-orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShopOrderResource {

    @Inject
    ShopOrderService shopOrderService;

    @GET
    public List<ShopOrder> listAll() {
        return shopOrderService.listAll();
    }

    @POST
    @Transactional
    @Counted(name = "kafka_order_create_request", absolute = true, description = "number of orders requested", tags = {"application=kafka-order", "resource=ShopOrderResource"})
    @Timed(name = "kafka_order_create_timer", description = "timer for processing a order creation", tags = {"application=kafka-order", "resource=ShopOrderResource"})
    public Response createShopOrder(ShopOrderDTO shopOrderDTO) {
        ShopOrder shopOrder = shopOrderService.createOrder(shopOrderDTO);
        shopOrder.persist();
        return Response.ok(shopOrder).build();
    }
}
