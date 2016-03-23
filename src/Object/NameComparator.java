package Object;

import static org.junit.Assert.assertTrue;
import java.util.Comparator;

public class NameComparator implements Comparator<TaskFile> {

	@Override
	public int compare(TaskFile task1, TaskFile task2) {
		if (task1.getIsTask()) {
			if (task2.getIsTask()) {
				return task1.getName().compareTo(task2.getName());
			} else {
				return -1;
			}
		} else if (task1.getIsDeadline()) {
			if (task2.getIsTask()) {
				return 1;
			} else {
				return task1.getStartCal().compareTo(task2.getStartCal());
			}
		} else {
			assertTrue(task1.getIsMeeting());
			if (task2.getIsTask()) {
				return 1;
			} else {
				return task1.getStartCal().compareTo(task2.getStartCal());
				
			}
		}
	}
}
