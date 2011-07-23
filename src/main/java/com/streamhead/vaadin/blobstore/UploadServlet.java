package com.streamhead.vaadin.blobstore;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.streamhead.vaadin.blobstore.model.User;

public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

    private final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
        final Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
        final BlobKey blobKey = blobs.get("myImage");
        final String uploadKey = req.getParameter("uploadKey");

        ObjectifyService.register(User.class);
        final Objectify ofy = ObjectifyService.begin();
        final User user = ofy.query(User.class).filter("uploadKey", uploadKey).get();
        user.setBlobKey(blobKey.getKeyString());
        ofy.put(user);
        
		resp.sendRedirect("/?refresh");
	}

	
}
