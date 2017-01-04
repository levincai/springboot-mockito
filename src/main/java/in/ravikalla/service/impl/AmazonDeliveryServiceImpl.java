package in.ravikalla.service.impl;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import in.ravikalla.exception.OrderAlreadyExistsException;
import in.ravikalla.exception.OrderException;
import in.ravikalla.external.service.DeliveryScoreService;
import in.ravikalla.external.service.EmailService;
import in.ravikalla.external.service.OrderStorageService;
import in.ravikalla.factory.EmailServiceFactory;
import in.ravikalla.model.Discount;
import in.ravikalla.model.Order;
import in.ravikalla.service.AmazonDeliveryService;

/**
 * @author - Ravi Kalla
 */
@Service
@Qualifier("AmazonDeliveryService")
public class AmazonDeliveryServiceImpl implements AmazonDeliveryService {

	@Inject
	private DeliveryScoreService deliveryScoreService;

	@Inject
	private OrderStorageService orderStorageService;

	@Inject
	private EmailServiceFactory emailServiceFactory;

	/**
	 * {@inheritDoc}
	 */
	public Order initOrder(String description, double basePrice, boolean premiumCustomer)
			throws OrderAlreadyExistsException {

		if (orderStorageService.exists(description)) {
			throw new OrderAlreadyExistsException();
		}

		Order order = new Order();
		order.setDescription(description);
		order.setBasePrice(basePrice);
		order.setDelivered(false);
		order.setSent(false);
		order.setPremium(premiumCustomer);
		calcFinalPrice(order);

		orderStorageService.store(order);
		return order;
	}


	/**
	 * {@inheritDoc}
	 */
	public void addDiscount(Order order, Discount discount) {
		if (discount != null) {
			order.getDiscounts().add(discount);
		}
		calcFinalPrice(order);

		orderStorageService.store(order);
	}

	/**
	 * {@inheritDoc}
	 */
	public void markSent(Order order, Date sendDate) throws OrderException {
		if (order.isSent()) {
			throw new OrderException("Order is sent exception");
		}

		order.setSendDate(sendDate);
		Date estimatedDelivery = calcEstimatedDeliveryDate(order.isPremium());

		order.setEstimatedDelivery(estimatedDelivery);
		order.setSent(true);

		orderStorageService.store(order);
	}

	/**
	 * {@inheritDoc}
	 */
	public void markDelivered(Order order, Date deliverDate) throws OrderException {
		if (!order.isSent()) {
			throw new OrderException("Order is not sent exception");
		}

		if (order.isDelivered()) {
			throw new OrderException("Order is not delevered exception");
		}

		order.setDelivered(true);
		order.setRealDelivery(deliverDate);

		orderStorageService.store(order);

		long deliveryScore = calcDeliveryDateScore(order);

		//Submit score
		deliveryScoreService.submitDeliveryPoints(deliveryScore);

		//Send email notification
		EmailService emailService = emailServiceFactory.buildEmailService(order);

		emailService.sendDeliveryNotification();
	}

	/**
	 * @param order
	 * @return
	 */
	private long calcDeliveryDateScore(Order order) {
		final long diff = order.getEstimatedDelivery().getTime() - order.getRealDelivery().getTime();
		return diff / (60 * 60 * 1000);
	}

	/**
	 * @param order
	 */
	private void calcFinalPrice(Order order) {
		double totalDiscount = 0;

		for (Discount discount : order.getDiscounts()) {
			totalDiscount += discount.getPercent();
		}

		//calc discount
		double finalPrice = order.getBasePrice() - (order.getBasePrice() * totalDiscount / 100.0);

		//round 2 decimals
		finalPrice *= 100;
		finalPrice = Math.round(finalPrice);
		finalPrice /= 100;

		//set final price
		order.setFinalPrice(finalPrice);
	}

	/**
	 * @param premiumCustomer
	 * @return
	 */
	private Date calcEstimatedDeliveryDate(boolean premiumCustomer) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		if (premiumCustomer) {
			calendar.add(Calendar.DAY_OF_MONTH, ESTIMATED_DAYS_TO_DELIVER_PREMIUM);
		} else {
			calendar.add(Calendar.DAY_OF_MONTH, ESTIMATED_DAYS_TO_DELIVER_REGULAR);
		}
		return calendar.getTime();
	}
}
