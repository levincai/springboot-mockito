package in.ravikalla.service;

import java.util.Date;

import in.ravikalla.exception.OrderAlreadyExistsException;
import in.ravikalla.exception.OrderException;
import in.ravikalla.model.Discount;
import in.ravikalla.model.Order;

/**
 * @author - Ravi Kalla
 */
public interface AmazonDeliveryService {

	//Days to estimated delivery
	public static final int ESTIMATED_DAYS_TO_DELIVER_PREMIUM = 2;
	public static final int ESTIMATED_DAYS_TO_DELIVER_REGULAR = 5;

	/**
	 *
	 * @param description
	 * @param basePrice
	 * @param premiumCustomer
	 * @return
	 */
	Order initOrder(String description, double basePrice, boolean premiumCustomer) throws OrderAlreadyExistsException;

	/**
	 * @param order
	 * @param discount
	 */
	void addDiscount(Order order, Discount discount);

	/**
	 * @param order
	 * @throws OrderException
	 */
	void markSent(Order order, Date sendDate) throws OrderException;

	/**
	 * @param order
	 * @throws OrderException
	 */
	void markDelivered(Order order, Date deliverDate) throws OrderException;

}
