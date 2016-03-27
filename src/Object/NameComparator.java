package Object;

import java.util.Comparator;

public class NameComparator implements Comparator<TaskFile> {
	
	@Override
	public int compare(TaskFile task1, TaskFile task2) {
		return task1.getName().compareTo(task2.getName());
	}
}
