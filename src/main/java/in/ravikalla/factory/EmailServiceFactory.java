package in.ravikalla.factory;

import org.springframework.stereotype.Component;

import in.ravikalla.external.service.EmailService;
import in.ravikalla.model.Order;

/**
 * @author - Ravi Kalla
 */
@Component
public class EmailServiceFactory {

	/**
	 * @param order
	 * @return
	 */
	public EmailService buildEmailService(Order order) {
		return new EmailService(order);
	}

}
