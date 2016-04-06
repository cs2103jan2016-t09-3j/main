//@@author A0124697
package tnote.object;

import java.util.Comparator;

public class NameComparator implements Comparator<TaskFile> {
	
	@Override
	public int compare(TaskFile task1, TaskFile task2) {
		return task1.getName().compareTo(task2.getName());
	}
}
