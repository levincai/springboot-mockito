package in.ravikalla.external.service;

import in.ravikalla.model.Order;

/**
 * @author - Ravi Kalla
 */
public interface OrderStorageService {

	/**
	 * @param order
	 */
	void store(Order order);

	/**
	 * @param description
	 * @return
	 */
	boolean exists(String description);
}
