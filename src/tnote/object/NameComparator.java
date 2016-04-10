//@@author A0124131B
package tnote.object;

import java.util.Comparator;

/**
 * This class is a comparator class for the TaskFile object. It allows TaskFile
 * objects to be sorted in alphabetical order
 * 
 * @author A0124131B
 *
 */
public class NameComparator implements Comparator<TaskFile> {

	@Override
	public int compare(TaskFile task1, TaskFile task2) {
		return task1.getName().compareTo(task2.getName());
	}
}
