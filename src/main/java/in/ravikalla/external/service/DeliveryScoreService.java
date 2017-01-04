package in.ravikalla.external.service;

/**
 * @author - Ravi Kalla
 */
public interface DeliveryScoreService {
	void submitDeliveryPoints(long points);
	long getCurrentScore();
}
