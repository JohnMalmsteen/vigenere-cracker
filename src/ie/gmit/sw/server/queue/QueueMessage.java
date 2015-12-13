package ie.gmit.sw.server.queue;

public class QueueMessage {
	private String jobNumber;
	private int maxKeyLength;
	private String text;
	private long timeCompleted;
	
	public QueueMessage(String num, int keyLen, String cypher){
		this.jobNumber = num;
		this.maxKeyLength = keyLen;
		this.text = cypher;
	}
	
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public int getMaxKeyLength() {
		return maxKeyLength;
	}
	public void setMaxKeyLength(int maxKeyLength) {
		this.maxKeyLength = maxKeyLength;
	}
	public String getCypherText() {
		return text;
	}
	public void setCypherText(String cypherText) {
		this.text = cypherText;
	}

	public long getTimeCompleted() {
		return timeCompleted;
	}

	public void setTimeCompleted(long timeCompleted) {
		this.timeCompleted = timeCompleted;
	}
}
