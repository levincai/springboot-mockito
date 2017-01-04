package in.ravikalla.controller;

import java.util.Date;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import in.ravikalla.exception.OrderAlreadyExistsException;
import in.ravikalla.exception.OrderException;
import in.ravikalla.external.service.DeliveryScoreService;
import in.ravikalla.model.Discount;
import in.ravikalla.model.Order;
import in.ravikalla.service.AmazonDeliveryService;

/**
 * @author - Ravi Kalla
 */
@Controller
public class SampleController {

	@Inject
	private AmazonDeliveryService amazonDeliveryService;

	@Inject
	private DeliveryScoreService deliveryScoreService;

	@GetMapping("/")
	@ResponseBody
	public String home() throws OrderException, OrderAlreadyExistsException {

		final Date now = new Date();

		Order order = amazonDeliveryService.initOrder("Soaps", 90.25, true);
		amazonDeliveryService.addDiscount(order, new Discount("Promo of new year", 5.0));
		amazonDeliveryService.markSent(order, now);
		amazonDeliveryService.markDelivered(order, now);

		return "Current score of delivery service " + deliveryScoreService.getCurrentScore() + " points";
	}

}
