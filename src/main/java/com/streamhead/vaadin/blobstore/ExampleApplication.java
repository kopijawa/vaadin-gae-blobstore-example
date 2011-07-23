package com.streamhead.vaadin.blobstore;

import java.io.FileNotFoundException;
import java.util.Map;

import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.streamhead.vaadin.blobstore.model.User;
import com.vaadin.Application;
import com.vaadin.terminal.ParameterHandler;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ExampleApplication extends Application implements ClickListener, ParameterHandler {

	private static final long serialVersionUID = 1L;

	private VerticalLayout layout = new VerticalLayout();
	private TextField username;
	
	@Override
	public void init() {
		ObjectifyService.register(User.class);
		
		Window window = new Window("Vaadin / GAE Blobstore integration example");
		window.addParameterHandler(this);
		
		window.addComponent(new Label("This is an example how you can integrate the Google AppEngine blobstore with your Vaadin application."));
		
		layout.addComponent(username = new TextField("To get started, enter a user name"));
		layout.addComponent(new Button("go", this));
		window.addComponent(layout);
		
		setMainWindow(window);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		final Objectify ofy = ObjectifyService.begin();
		final User user = new User((String)username.getValue());
		final String uploadKey = user.generateUploadKey();
		ofy.put(user);
		setUser(user);
		
		layout.removeAllComponents();
		layout.addComponent(new Label("Username: " + user.getName()));
        final String uploadUrl = BlobstoreServiceFactory.getBlobstoreService().createUploadUrl("/uploadImage");
        final Label label = new Label("<form action=\"" + uploadUrl + "\" method=\"post\" enctype=\"multipart/form-data\">" +
                "<input type=\"file\" name=\"myImage\" />" +
                "<input type=\"hidden\" name=\"uploadKey\" value=\"" + uploadKey +"\" />" +
                "<input type=\"submit\" value=\"Opladen\" />" +
                "</form>", Label.CONTENT_XHTML);
        layout.addComponent(label);

	}

	@Override
	public void handleParameters(Map<String, String[]> parameters) {
		if(parameters.containsKey("refresh")) {
			//refresh the user
			final Objectify ofy = ObjectifyService.begin();
			User user = (User)getUser();
			user = ofy.get(User.class, user.getId());
			setUser(user);
			
			layout.removeAllComponents();
			try {
				final BlobstoreResource resource = new BlobstoreResource(user.getBlobKey(), this);
				layout.addComponent(new Embedded("uploaded image", resource));
			} catch (FileNotFoundException e) {
				layout.addComponent(new Label("could not load your image with key: " + user.getBlobKey()));
			}
		}
	}

}
