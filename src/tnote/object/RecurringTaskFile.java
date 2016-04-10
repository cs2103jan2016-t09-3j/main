//@@author A0124131B
package tnote.object;

import java.text.ParseException;
import java.util.ArrayList;

import tnote.util.exceptions.IncorrectTimeException;

/**
 * This class is used to represent recurring tasks in TNotes, as recurring tasks
 * require some additional information to be stored
 * 
 * @author A0124131B
 *
 */

public class RecurringTaskFile extends TaskFile {
	private String recurringInterval;
	private ArrayList<String> listOfRecurStartDates;
	private ArrayList<String> listOfRecurEndDates;

	/*--------------------Constructors-----------------------------*/
	/**
	 * Basic constructor for RecurringTaskFile
	 */
	public RecurringTaskFile() {
		recurringInterval = new String();
		listOfRecurStartDates = new ArrayList<String>();
		listOfRecurEndDates = new ArrayList<String>();
	}

	/**
	 * Constructor for RecurringTaskFile which takes in a TaskFile object,
	 * creating a copy of the TaskFile object
	 * 
	 * @param task
	 *            the TaskFile object to copy
	 * @throws ParseException
	 *             Error when the start date and time or end date and time
	 *             cannot be parsed into a calendar object
	 * @throws IncorrectTimeException
	 *             Error when the end date and time is before the start date and
	 *             time
	 */
	public RecurringTaskFile(TaskFile task) throws ParseException, IncorrectTimeException {
		super(task);
		recurringInterval = new String();
		listOfRecurStartDates = new ArrayList<String>();
		listOfRecurEndDates = new ArrayList<String>();
	}

	/*---------------------------------Accessors------------------------------------*/

	/**
	 * Method to retrieve the recurring interval of the RecurringTaskFile
	 * 
	 * @return String the recurring interval of the RecurringTaskFile
	 */
	public String getRecurringInterval() {
		return recurringInterval;
	}

	/**
	 * Method to retrieve the list of starting dates for the RecurringTaskFile
	 * 
	 * @return ArrayList{@code<String>} the list of starting dates for the
	 *         RecurringTaskFile
	 */
	public ArrayList<String> getListOfRecurStartDates() {
		return listOfRecurStartDates;
	}

	/**
	 * Method to retrieve the list of ending dates for the RecurringTaskFile
	 * 
	 * @return ArrayList{@code<String>} the list of ending dates for the
	 *         RecurringTaskFile
	 */
	public ArrayList<String> getListOFRecurEndDates() {
		return listOfRecurEndDates;
	}

	/*-------------------------------Mutators--------------------------------------------*/

	/**
	 * Method to set the recurring interval attribute in the RecurringTaskFile
	 * 
	 * @param interval
	 *            the String to set as the recurringInterval for the
	 *            RecurringTaskFile
	 */
	public void setRecurringInterval(String interval) {
		this.recurringInterval = interval;
	}

	/**
	 * Method to add a list of dates as the starting dates for the
	 * RecurringTaskFile
	 * 
	 * @param date
	 *            the list of Strings to set as the list of starting dates for
	 *            the RecurringTaskFile
	 */
	public void addRecurringStartDate(ArrayList<String> date) {
		this.listOfRecurStartDates = date;
	}

	/**
	 * Method to add a list of dates as the ending dates for the
	 * RecurringTaskFile
	 * 
	 * @param date
	 *            the list of Strings to set as the list of ending dates for the
	 *            RecurringTaskFile
	 */
	public void addRecurringEndDate(ArrayList<String> date) {
		this.listOfRecurEndDates = date;
	}
}
