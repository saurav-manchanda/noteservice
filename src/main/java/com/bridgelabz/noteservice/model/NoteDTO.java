/********************************************************************************* *
 * Purpose: To create an implementation to GoogleKeep(ToDoApplication).
 * Creating a DTO class of Note having fields related to a Note and corresponding setters and getters  
 * @author Saurav Manchanda
 * @version 1.0
 * @since 17/07/2018
 *********************************************************************************/
package com.bridgelabz.noteservice.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Saurav
 *<p>
 *DTO class of Note having fields related to a Note and corresponding setters and getters.
 *</p>
 */
public class NoteDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String title;
	private String description;
	private List<Label> labels;
	public List<Label> getLabels() {
		return labels;
	}
	public void setLabels(List<Label> labels) {
		this.labels = labels;
	}
	/**
	 * Method to get the Title of the Note
	 * @return
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * Method to set the title 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * Method to get the Description of the Note
	 * @return
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * Method to set the Description 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
