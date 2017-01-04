package in.ravikalla.external.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import in.ravikalla.model.Order;

/**
 * @author - Ravi Kalla
 */
public class EmailService {

	private Order order;

	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	public EmailService(Order order) {
		this.order = order;
	}

	public void sendDeliveryNotification() {
		logger.info("EmailService - sendDeliveryNotification - " + order.getDescription());
	}

}
