package in.ravikalla.external.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import in.ravikalla.external.service.OrderStorageService;
import in.ravikalla.model.Order;

/**
 * @author - Ravi Kalla
 */
@Service
@Qualifier("OrderStorageService")
public class OrderStorageServiceImpl implements OrderStorageService {

	private static final Logger logger = LoggerFactory.getLogger(OrderStorageServiceImpl.class);

	@Override
	public void store(Order order) {
		logger.info("OrderStorageService - order description " + order.getDescription());
	}

	@Override
	public boolean exists(String description) {
		logger.info("OrderStorageService - order exists " + description);
		return false;
	}
}
