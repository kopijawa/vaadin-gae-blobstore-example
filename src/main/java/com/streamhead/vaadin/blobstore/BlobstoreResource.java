package com.streamhead.vaadin.blobstore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.vaadin.Application;
import com.vaadin.terminal.ApplicationResource;
import com.vaadin.terminal.DownloadStream;

public class BlobstoreResource implements ApplicationResource {

	private static final long serialVersionUID = 1L;

	private long cacheTime = DEFAULT_CACHETIME;
	private final Application application;
	private final BlobKey blobKey;
	private final BlobInfo blobInfo;

	// private BlobstoreInputStream inputStream;

	public BlobstoreResource(String keyStr, Application application)
			throws FileNotFoundException {
		this.application = application;
		this.blobKey = new BlobKey(keyStr);
		this.blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
		if (blobInfo == null)
			throw new FileNotFoundException("Blob with key " + keyStr
					+ " not found."); // you might want to create your own
										// custom exception

		application.addResource(this);
	}

	@Override
	public String getMIMEType() {
		return blobInfo.getContentType();
	}

	@Override
	public DownloadStream getStream() {
		InputStream inputStream;
		try {
			inputStream = new BlobstoreInputStream(blobKey);
		} catch (IOException e) {
			// this shouldn't happen since we checked if the blobs exists in the
			// constructor
			return null;
		}

		final DownloadStream ds = new DownloadStream(inputStream,
				getMIMEType(), getFilename());
		ds.setBufferSize(getBufferSize());
		ds.setCacheTime(cacheTime);
		return ds;
	}

	@Override
	public Application getApplication() {
		return application;
	}

	@Override
	public String getFilename() {
		return blobInfo.getFilename();
	}

	@Override
	public long getCacheTime() {
		return cacheTime;
	}

	public void setCacheTime(long cacheTime) {
		this.cacheTime = cacheTime;
	}

	@Override
	public int getBufferSize() {
		return 0;
	}

}
