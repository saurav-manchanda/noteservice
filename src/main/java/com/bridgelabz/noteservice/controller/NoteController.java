/********************************************************************************* *
 * Purpose: To create an implementation to GoogleKeep(ToDoApplication).
 * Creating a Note Controller.  
 * @author Saurav Manchanda
 * @version 1.0
 * @since 17/07/2018
 *********************************************************************************/
package com.bridgelabz.noteservice.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.noteservice.model.Label;
import com.bridgelabz.noteservice.model.Note;
import com.bridgelabz.noteservice.model.NoteDTO;
import com.bridgelabz.noteservice.service.IFeignClient;
import com.bridgelabz.noteservice.service.INoteService;
import com.bridgelabz.noteservice.utilservice.ResponseDTO;
import com.bridgelabz.noteservice.utilservice.ToDoException;
import com.bridgelabz.noteservice.utilservice.ObjectMapper.ObjectMapping;

/**
 * @author Saurav
 *         <p>
 *         This class is a Note Controller having functionalities of creating a
 *         note, reading, updating as well as deleting a note
 *         </p>
 */
@RefreshScope
@RestController
@RequestMapping(value = "/notes")
public class NoteController {
	@Autowired
	INoteService noteService;
	@Autowired
	ObjectMapping objectMapping;
	@Autowired
	IFeignClient feign;
	public static final Logger logger = LoggerFactory.getLogger(NoteController.class);
	static String REQ_ID = "IN_Note_CONTROLLER";
	static String RESP_ID = "OUT_Note_CONTROLLER";

	/**
	 * @param note
	 * @param token
	 * @return ResponseEntity for showing the response on the swagger or Postman
	 *         <p>
	 *         This method is for creating a new Note. This method is interacting
	 *         with the service layer and calling createNote() of service layer
	 *         </p>
	 * @throws ToDoException
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/createnote", method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createNote(@RequestBody NoteDTO noteDto, HttpServletRequest request,
			@RequestHeader("token") String token) throws ToDoException, IOException {
		// logger.debug("hello");
		// logger.warn("hello");
		logger.info(REQ_ID + "Creating a new note");
		String userId = request.getHeader("userId");
		String noteId = noteService.createNote(noteDto, userId);
		logger.info(RESP_ID + " Note successfully created");
		return new ResponseEntity("Note successfully created with id: " + noteId, HttpStatus.OK);
	}

	/**
	 * @param token
	 * @param noteId
	 * @return ResponseEntity ResponseEntity for showing the response on the swagger
	 *         or Postman
	 *         <p>
	 *         This method is for deleting a Note. This method is interacting with
	 *         the service layer and calling deleteNote() of service layer
	 *         </p>
	 * @throws ToDoException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/deletenote/{noteId}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> deleteNote(HttpServletRequest request, @PathVariable String noteId,
			@RequestHeader("token") String token) throws ToDoException {
		logger.info(REQ_ID + " Deleting a Note");
		String userId = request.getHeader("userId");
		noteService.deleteNote(noteId, userId);
		logger.info(RESP_ID + " Note successfully Deleted");
		return new ResponseEntity("Note Successfully deleted " + noteId, HttpStatus.OK);
	}

	/**
	 * @param noteId
	 * @param title
	 * @param description
	 * @param token
	 * @return ResponseEntity ResponseEntity for showing the response on the swagger
	 *         or Postman
	 *         <p>
	 *         This method is for updating a Note. This method is interacting with
	 *         the service layer and calling updateNote() of service layer
	 *         </p>
	 * @throws ToDoException
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/updatenote/{noteId}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateNote(@RequestBody NoteDTO noteDto, HttpServletRequest request,
			@PathVariable String noteId, @RequestHeader("token") String token) throws ToDoException, IOException {
		logger.info(REQ_ID + " Updating a note");
		String userId = request.getHeader("userId");
		noteService.updateNote(noteId, noteDto.getTitle(), noteDto.getDescription(), userId);
		logger.info(RESP_ID + " Note sucessfully updated");
		return new ResponseEntity("Note sucessfully updated " + noteId, HttpStatus.OK);
	}

	/**
	 * @param noteId
	 * @param token
	 * @return ResponseEntity ResponseEntity for showing the response on the swagger
	 *         or Postman
	 *         <p>
	 *         This method is for displaying the Notes. This method is interacting
	 *         with the service layer and calling displayAllNotes() of service layer
	 *         </p>
	 * @throws ToDoException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/displayallnotes", method = RequestMethod.GET)
	public ResponseEntity displayAllNotes(HttpServletRequest request, @RequestHeader("token") String token)
			throws ToDoException {
		logger.info(REQ_ID + " Displaying all notes");
		String userId = request.getHeader("userId");
		List<Note> list = new ArrayList<>();
		list = noteService.displayAllNotes(userId);
		logger.info(RESP_ID + " All notes are got to display by the controller");
		return new ResponseEntity(list, HttpStatus.OK);
	}

	/**
	 * @param noteId
	 * @param colour
	 * @param token
	 * @return responseEntity ResponseEntity for showing the response on the swagger
	 *         or Postman
	 *         <p>
	 *         This method is for changing the colour of the Note.This method is
	 *         interacting with the service layer and calling changeColourOfNote()
	 *         of Note Service.
	 *         </p>
	 * @throws ToDoException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/changecolour/{noteId}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> changeColour(@PathVariable String noteId, @RequestParam("colour") String colour,
			HttpServletRequest request, @RequestHeader("token") String token) throws ToDoException {
		logger.info(REQ_ID + " Changing the colour of Note");
		String userId = request.getHeader("userId");
		noteService.changeColourOfNote(noteId, colour, userId);
		logger.info(RESP_ID + " Colour Successfully Changed ");
		return new ResponseEntity("Colour successfully changed " + noteId, HttpStatus.OK);
	}

	/**
	 * @param noteId
	 * @param token
	 * @return responseEntity ResponseEntity for showing the response on the swagger
	 *         or Postman
	 *         <p>
	 *         This method is for deleting the Note permanently. It will not be
	 *         availavble in the trash as well.
	 *         </p>
	 * @throws ToDoException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/deletepermanently/{noteId}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDTO> deleteFromTrash(@PathVariable String noteId, HttpServletRequest request,
			@RequestHeader("token") String token) throws ToDoException {
		logger.info(REQ_ID + " Deleting a Note permanently");
		String userId = request.getHeader("userId");
		noteService.deleteFromTrash(noteId, userId);
		logger.info(RESP_ID + " Note Deleted Permanently ");
		return new ResponseEntity("Note deleted Permanently " + noteId, HttpStatus.OK);
	}

	/**
	 * @param noteId
	 * @param token
	 * @return responseEntity ResponseEntity for showing the response on the swagger
	 *         or Postman
	 *         <p>
	 *         This method is for restoring a note from the trash. We are passing
	 *         the noteId and it will match the note Id and corresponding note will
	 *         be moved out of the trash. Therefore we r changing its status from
	 *         true to false.
	 *         </p>
	 * @throws ToDoException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/restorefromtrash/{noteId}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> restoreFromTrash(@PathVariable String noteId, HttpServletRequest request,
			@RequestHeader("token") String token) throws ToDoException {
		logger.info(REQ_ID + " Restoring from trash");
		String userId = request.getHeader("userId");
		noteService.restoreFromTrash(noteId, userId);
		logger.info(RESP_ID + " Restored from Trashed");
		return new ResponseEntity("Restrored from trash " + noteId, HttpStatus.OK);
	}

	/**
	 * @param noteId
	 * @param token
	 * @return responseEntity ResponseEntity for showing the response on the swagger
	 *         or Postman
	 *         <p>
	 *         this method is for pinning the note. The notes pinned will move on
	 *         the top of the list.
	 *         </p>
	 * @throws ToDoException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/pinnote/{noteId}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> pinNote(@PathVariable String noteId, HttpServletRequest request,
			@RequestHeader("token") String token) throws ToDoException {
		logger.info(REQ_ID + " Pinning a note");
		String userId = request.getHeader("userId");
		noteService.pinNote(noteId, userId);
		logger.info(RESP_ID + " Pinned the Note");
		return new ResponseEntity("Pinned the Note " + noteId, HttpStatus.OK);
	}

	/**
	 * @param noteId
	 * @param request
	 * @return ResponseEntity for showing the response on swagger or PostMan
	 * @throws ToDoException
	 *             <p>
	 *             This method is for archieving the note. The notes which are
	 *             archieved will go down in the list of all notes. A particular
	 *             note if archieved cannot be pinned or vice versa.
	 *             </p>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/archive/{noteId}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> archieveNote(@PathVariable String noteId, HttpServletRequest request,
			@RequestHeader("token") String token) throws ToDoException {
		logger.info(REQ_ID + " Archiving a note");
		String userId = request.getHeader("userId");
		noteService.archieveNote(noteId, userId);
		logger.info(RESP_ID + " Archived a note");
		return new ResponseEntity("Archived the the Note " + noteId, HttpStatus.OK);
	}

	/**
	 * @param label
	 * @param request
	 * @return
	 * @throws ToDoException
	 *             <p>
	 *             This method is for creating the Label.
	 *             </p>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/createlabel", method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createLable(@RequestBody Label label, HttpServletRequest request,
			@RequestHeader("token") String token) throws ToDoException {
		logger.info(REQ_ID + " Creating a Label");
		String userId = request.getHeader("userId");
		String labelId = noteService.createLabel(label, userId);
		logger.info(RESP_ID + "Label Created successfully ");
		return new ResponseEntity(new ResponseDTO("Label Created successfully " + labelId, 200), HttpStatus.OK);
	}

	/**
	 * @param noteId
	 * @param label
	 * @param request
	 * @param token
	 * @return
	 * @throws ToDoException
	 *             <p>
	 *             This method is for adding the label to a particular note
	 *             </p>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/addlabeltoanote/{noteId}", method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> addLabelToNote(@PathVariable String noteId, @RequestBody Label label,
			HttpServletRequest request, @RequestHeader("token") String token) throws ToDoException {
		logger.info(REQ_ID + " Creating a Label");
		String userId = request.getHeader("userId");
		noteService.addLabeltoNote(noteId, label, userId);
		logger.info(RESP_ID + "Label Created successfully ");
		return new ResponseEntity(new ResponseDTO("Label added successfully " + noteId, 200), HttpStatus.OK);
	}

	/**
	 * @param labelId
	 * @param labelName
	 * @param request
	 * @return
	 * @throws ToDoException
	 *             <p>
	 *             This method is for updating the label.
	 *             </p>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/updatelabel/{labelId}", method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> updateLabel(@PathVariable String labelId, @RequestParam String labelName,
			HttpServletRequest request, @RequestHeader("token") String token) throws ToDoException {
		logger.info(REQ_ID + " Updating a label");
		String userId = request.getHeader("userId");
		noteService.updateLabel(labelId, labelName, userId);
		logger.info(RESP_ID + "Label Updated successfully ");
		return new ResponseEntity(new ResponseDTO("Label Created successfully " + labelId, 200), HttpStatus.OK);
	}

	/**
	 * @param noteId
	 * @param labelName
	 * @param request
	 * @return
	 * @throws ToDoException
	 *             <p>
	 *             This is method is for deleting a label from a note.
	 *             </p>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/deletelabelfromanote/{noteId}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDTO> deleteLabelFromNote(@PathVariable String noteId, @RequestParam String labelName,
			HttpServletRequest request, @RequestHeader("token") String token) throws ToDoException {
		logger.info(REQ_ID + " deleting a label from the note");
		String userId = request.getHeader("userId");
		noteService.deleteLabelFromNote(noteId, labelName, userId);
		logger.info(RESP_ID + "Label Deleted successfully from the note");
		return new ResponseEntity(new ResponseDTO("Label deleted successfully " + noteId, 200), HttpStatus.OK);
	}

	/**
	 * @param labelId
	 * @param request
	 * @return
	 * @throws ToDoException
	 *             <p>
	 *             This method is for deleting the label
	 *             </p>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/deletelabel", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDTO> deleteLabel(@RequestBody String labelName, HttpServletRequest request,
			@RequestHeader("token") String token) throws ToDoException {
		logger.info(REQ_ID + " deleting a label");
		String userId = request.getHeader("userId");
		noteService.deleteLabel(labelName, userId);
		logger.info(RESP_ID + "Label Deleted successfully ");
		return new ResponseEntity(new ResponseDTO("Label deleted successfully " + labelName, 200), HttpStatus.OK);
	}

	/**
	 * @param request
	 * @return
	 * @throws ToDoException
	 *             <p>
	 *             This method is for reading all the labels present
	 *             </p>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/readlabels", method = RequestMethod.GET)
	public ResponseEntity readLabels(HttpServletRequest request, @RequestHeader("token") String token)
			throws ToDoException {
		logger.info(REQ_ID + " Displaying all labels");
		String userId = request.getHeader("userId");
		List<Label> list = null;
		list = noteService.displayLabels(userId);
		logger.info(RESP_ID + " All labes are got to display by the controller");
		return new ResponseEntity(list, HttpStatus.OK);
	}

	/**
	 * @param labelName
	 * @param request
	 * @return
	 * @throws ToDoException
	 *             <p>
	 *             This method is for searching the notes by label name
	 *             </p>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/searchnotesbylabelname", method = RequestMethod.GET)
	public ResponseEntity searchNotesByLabel(@RequestParam String labelName, HttpServletRequest request,
			@RequestHeader("token") String token) throws ToDoException {
		logger.info(REQ_ID + " Displaying all notes corresponding to that label");
		String userId = request.getHeader("userId");
		List<Note> list = new ArrayList<>();
		list = noteService.searchNotesByLabel(labelName, userId);
		logger.info(RESP_ID + " All notes are got to display by the controller");
		return new ResponseEntity(list, HttpStatus.OK);
	}

	/**
	 * @param noteId
	 * @param remindTime
	 * @param request
	 * @return
	 * @throws ToDoException
	 * @throws ParseException
	 *             <p>
	 *             This method is for setting the reminder for all the notes.
	 *             </p>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/setreminder/{noteId}", method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> setReminder(@PathVariable String noteId, @RequestParam String remindTime,
			HttpServletRequest request, @RequestHeader("token") String token) throws ToDoException, ParseException {
		logger.info(REQ_ID + " Setting the Reminder");
		String userId = request.getHeader("userId");
		String email = (String) request.getAttribute("email");
		noteService.setReminder(noteId, remindTime, userId, email);
		logger.info(RESP_ID + "Label Deleted successfully ");
		return new ResponseEntity(new ResponseDTO("Reminder set successfully " + noteId, 200), HttpStatus.OK);
	}

	/**
	 * @param request
	 * @param token
	 * @returnhn
	 * @throws ToDoException
	 *             <p>
	 *             This API is for displaying all the Notes which are in trash
	 *             </p>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/displaynotesfromtrash", method = RequestMethod.GET)
	public ResponseEntity displayNotesFromTrash(HttpServletRequest request, @RequestHeader("token") String token)
			throws ToDoException {
		logger.info(REQ_ID + " Displaying all notes");
		String userId = request.getHeader("userId");
		List<Note> list = new ArrayList<>();
		list = noteService.displayFromTrash(userId);
		logger.info(RESP_ID + " All notes are got to display by the controller");
		return new ResponseEntity(list, HttpStatus.OK);
	}

	/**
	 * @return ResponseEntity
	 *         <p>
	 *         This method is to get all the es=xisting users.
	 *         </p>
	 */
	@GetMapping(value = "/allusers", headers = "Accept=application/json")
	public ResponseEntity<List<?>> getAllUsers() {
		logger.info(REQ_ID + " inside getall user service");
		List<?> user = feign.getAllUsers();

		if (user.size() == 0) {
			logger.info("user not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			logger.info("all users retrieved");
			return new ResponseEntity<>(user, HttpStatus.OK);
		}
	}

	/**
	 * @param request
	 * @param token
	 * @return
	 * @throws ToDoException
	 *             <p>
	 *             This method is for sorting of labels
	 *             </p>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/sortlabelbyname", method = RequestMethod.GET)
	public ResponseEntity sortingOfLabelByLabelName(HttpServletRequest request, @RequestHeader("token") String token, @RequestParam("ascendingOrDescending") boolean ascendingOrDescending)
			throws ToDoException {
		logger.info(REQ_ID + " Displaying all labels");
		logger.info(REQ_ID + " Displaying all labels");
		String userId = request.getHeader("userId");
		List<Label> list = null;
		list = noteService.sortingByLabelName(userId,ascendingOrDescending);
		logger.info(RESP_ID + " All labels are got to display by the controller");
		return new ResponseEntity(list, HttpStatus.OK);
	}
	/**
	 * @param request
	 * @param token
	 * @return
	 * @throws ToDoException
	 *             <p>
	 *             This method is for sorting of labels
	 *             </p>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/sortnotebytitle", method = RequestMethod.GET)
	public ResponseEntity sortingOfNoteByTitle(HttpServletRequest request, @RequestHeader("token") String token, @RequestParam("ascendingOrDescending") boolean ascendingOrDescending)
			throws ToDoException {
		logger.info(REQ_ID + " Displaying all labels");
		logger.info(REQ_ID + " Displaying all labels");
		String userId = request.getHeader("userId");
		List<Note> list = null;
		list = noteService.sortingNoteByTitle(userId,ascendingOrDescending);
		logger.info(RESP_ID + " All labels are got to display by the controller");
		return new ResponseEntity(list, HttpStatus.OK);
	}
	/**
	 * @param request
	 * @param token
	 * @return
	 * @throws ToDoException
	 *             <p>
	 *             This method is for sorting of labels
	 *             </p>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/sortnotebydate", method = RequestMethod.GET)
	public ResponseEntity sortingOfNoteByCreatedDate(HttpServletRequest request, @RequestHeader("token") String token, @RequestParam("ascendingOrDescending") boolean ascendingOrDescending)
			throws ToDoException {
		logger.info(REQ_ID + " Displaying all labels");
		logger.info(REQ_ID + " Displaying all labels");
		String userId = request.getHeader("userId");
		List<Note> list = null;
		list = noteService.sortingNoteByDate(userId,ascendingOrDescending);
		logger.info(RESP_ID + " All labels are got to display by the controller");
		return new ResponseEntity(list, HttpStatus.OK);
	}
}
