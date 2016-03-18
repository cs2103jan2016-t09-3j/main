package Object;

import java.util.Comparator;

public class NameComparator implements Comparator<TaskFile> {

		@Override
		public int compare(TaskFile task1, TaskFile task2) {
			if(task1.getStartCal().equals(task2.getStartCal())) {
				return task1.getName().compareTo(task2.getName());
			} else {
				return task1.getStartCal().compareTo(task2.getStartCal());
			}
		}
	}

