package in.ravikalla.external.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import in.ravikalla.external.service.DeliveryScoreService;

/**
 * @author - Ravi Kalla
 */
@Service
@Qualifier("DeliveryScoreService")
public class DeliveryScoreServiceImpl implements DeliveryScoreService {

	private long totalPoints;

	private static final Logger logger = LoggerFactory.getLogger(DeliveryScoreServiceImpl.class);

	public void submitDeliveryPoints(long points) {
		logger.info("DeliveryScoreService - Sum : " + points + " points!");
		totalPoints += points;
	}

	public long getCurrentScore() {
		return totalPoints;
	}
}
