package example.nosql;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Part;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cloudant.client.api.Database;
import com.cloudant.client.api.Search;
import com.cloudant.client.api.views.Key;
import com.gitlab.Issue;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

@Path("/vinic")
/**
 * CRUD service of todo list table. It uses REST style.
 */
public class ResourceServletVinic {

	public ResourceServletVinic() {
	}

	@POST
	public Response create(@QueryParam("id") String id, @FormParam("name") String name, @FormParam("role") String role, @FormParam("band") String band)
			throws Exception {

		Database db = null;
		try {
			db = getDB();
		} catch (Exception re) {
			re.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
		String idString = id == null ? null : id.toString();
		JsonObject resultObject = create(db, idString, name, role, band, null, null);

		System.out.println("Create Successful.");

		return Response.ok(resultObject.toString()).build();

	}

	protected JsonObject create(Database db, String id, String name, String role, String band, Part part, String fileName)
			throws IOException {
		
		// check if document exist
		HashMap<String, Object> obj = (id == null) ? null : db.find(HashMap.class, id);

		if (obj == null) {
			// if new document

			id = String.valueOf(System.currentTimeMillis());

			// create a new document
			System.out.println("Creating new document with id : " + id);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("name", name);
			data.put("_id", id);
			data.put("role", role);
			data.put("band", band);
			data.put("creation_date", new Date().toString());
			db.save(data);

			// attach the attachment object
			obj = db.find(HashMap.class, id);
			saveAttachment(db, id, part, fileName, obj);
		} else {
			// if existing document
			// attach the attachment object
			saveAttachment(db, id, part, fileName, obj);

			// update other fields in the document
			obj = db.find(HashMap.class, id);
			obj.put("name", name);
			obj.put("role", role);
			obj.put("band", band);
			db.update(obj);
		}

		obj = db.find(HashMap.class, id);

		JsonObject resultObject = toJsonObject(obj);

		return resultObject;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(/**@QueryParam("id") String id,**/ @QueryParam("query") String query) throws Exception {

		Database db = null;
		try {
			db = getDB();
		} catch (Exception re) {
			re.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		System.out.println("query = " + query);
		
		
		JsonObject resultObject = new JsonObject();
		JsonArray jsonArray = new JsonArray();

		//Return all records
		if (query == null || query == "") {
			try {
				// get all the document present in database
				List<HashMap> allDocs = db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse()
						.getDocsAs(HashMap.class);

				if (allDocs.size() == 0) {
					allDocs = initializeSampleData(db);
				}

				for (HashMap doc : allDocs) {
					HashMap<String, Object> obj = db.find(HashMap.class, doc.get("_id") + "");
					JsonObject jsonObject = new JsonObject();
					LinkedTreeMap<String, Object> attachments = (LinkedTreeMap<String, Object>) obj.get("_attachments");
					
					//
//					List<String> list = db.getViewRequestBuilder("name","find-by-name")
//							.newRequest(Key.Type.STRING, String.class)
//							.keys("Luiz")
//							.limit(10)
//							.includeDocs(true)
//							.build()
//							.getResponse()
//							.getKeys();
					//

					if (attachments != null && attachments.size() > 0) {
						JsonArray attachmentList = getAttachmentList(attachments, obj.get("_id") + "");
						jsonObject.addProperty("id", obj.get("_id") + "");
						jsonObject.addProperty("name", obj.get("assignee") + "");
						jsonObject.addProperty("value", obj.get("value") + "");
						jsonObject.addProperty("role", obj.get("title") + "");
						jsonObject.addProperty("band", obj.get("band") + "");
						jsonObject.add("attachements", attachmentList);

					} else {
						jsonObject.addProperty("id", obj.get("_id") + "");
						jsonObject.addProperty("name", obj.get("assignee") + "");
						jsonObject.addProperty("value", obj.get("value") + "");
						jsonObject.addProperty("role", obj.get("title") + "");
						jsonObject.addProperty("band", obj.get("band") + "");
					}

					jsonArray.add(jsonObject);
				}
			} catch (Exception dnfe) {
				System.out.println("Exception thrown : " + dnfe.getMessage());
			}

			resultObject.addProperty("id", "all");
			resultObject.add("body", jsonArray);

			return Response.ok(resultObject.toString()).build();
		}
		
		
		
		
		//Execute query
		else {
			try {

				//List<HashMap> queryResult = db.findByIndex("{'assignee.name' : 'Luiz Galeoti'}", HashMap.class);
				List<Issue> issues = db.findByIndex(query, Issue.class);
				
				//If not found, insert blank row
				if(issues.size() == 0)
				{
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("id", "");
					jsonObject.addProperty("name", "");
					jsonObject.addProperty("value", "");
					jsonObject.addProperty("role", "");
					jsonArray.add(jsonObject);
				//Insert rows found
				}else {
				
					for(Integer i = 0; i < issues.size(); i++) {
						
						//System.out.println("Issue" + i.toString() +" = " + issues.get(i).title + "-" + issues.get(i).assignee.name + "-" + issues.get(i).updated_at + "-" + issues.get(i).time_stats.human_time_estimate + "-" + issues.get(i).confidential );
						
						JsonObject jsonObject = new JsonObject();
						
						jsonObject.addProperty("id", issues.get(i).id + "");
						jsonObject.addProperty("name", issues.get(i).assignee.name + "");
						jsonObject.addProperty("value", issues.get(i).title + "");
						jsonObject.addProperty("role", issues.get(i).updated_at + "");
						
						if(issues.get(i).labels.length > 0)
						{
							jsonObject.addProperty("band", issues.get(i).labels[0] + "");
						} else {
							jsonObject.addProperty("band", "");
						}
						
						System.out.println(jsonObject);
						
						jsonArray.add(jsonObject);
	
					}
				}

			} catch (Exception dnfe) {
				System.out.println("Exception thrown : " + dnfe.getMessage());
			}

			resultObject.addProperty("id", "all");
			resultObject.add("body", jsonArray);

			return Response.ok(resultObject.toString()).build();
		}
		
		
		
		/**** Confirmar se precisa remover - ELSE original, tratamento de exceção
		// check if document exists
		HashMap<String, Object> obj = db.find(HashMap.class, id + "");
		if (obj != null) {
			JsonObject jsonObject = toJsonObject(obj);
			return Response.ok(jsonObject.toString()).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		****/
	}

	@DELETE
	public Response delete(@QueryParam("id") String id) {

		Database db = null;
		try {
			db = getDB();
		} catch (Exception re) {
			re.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		// check if document exist
		HashMap<String, Object> obj = db.find(HashMap.class, id + "");

		if (obj == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		} else {
			db.remove(obj);

			System.out.println("Delete Successful.");

			return Response.ok("OK").build();
		}
	}

	@PUT
	public Response update(@QueryParam("id") String id, @QueryParam("name") String name, @QueryParam("role") String role, @QueryParam("band") String band) {

		Database db = null;
		try {
			db = getDB();
		} catch (Exception re) {
			re.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		// check if document exist
		HashMap<String, Object> obj = db.find(HashMap.class, id + "");

		if (obj == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		} else {
			obj.put("name", name);
			obj.put("role", role);
			obj.put("band", band);


			System.out.println("Update Successful.");

			return Response.ok("OK").build();
		}
	}

	private JsonArray getAttachmentList(LinkedTreeMap<String, Object> attachmentList, String docID) {
		JsonArray attachmentArray = new JsonArray();

		for (Object key : attachmentList.keySet()) {
			LinkedTreeMap<String, Object> attach = (LinkedTreeMap<String, Object>) attachmentList.get(key);

			JsonObject attachedObject = new JsonObject();
			// set the content type of the attachment
			attachedObject.addProperty("content_type", attach.get("content_type").toString());
			// append the document id and attachment key to the URL
			attachedObject.addProperty("url", "attach?id=" + docID + "&key=" + key);
			// set the key of the attachment
			attachedObject.addProperty("key", key + "");

			// add the attachment object to the array
			attachmentArray.add(attachedObject);
		}

		return attachmentArray;
	}

	private JsonObject toJsonObject(HashMap<String, Object> obj) {
		JsonObject jsonObject = new JsonObject();
		LinkedTreeMap<String, Object> attachments = (LinkedTreeMap<String, Object>) obj.get("_attachments");
		if (attachments != null && attachments.size() > 0) {
			JsonArray attachmentList = getAttachmentList(attachments, obj.get("_id") + "");
			jsonObject.add("attachements", attachmentList);
		}
		jsonObject.addProperty("id", obj.get("_id") + "");
		jsonObject.addProperty("name", obj.get("name") + "");
		jsonObject.addProperty("value", obj.get("value") + "");
		jsonObject.addProperty("role", obj.get("role") + "");
		jsonObject.addProperty("band", obj.get("band") + "");
		return jsonObject;
	}

	private void saveAttachment(Database db, String id, Part part, String fileName, HashMap<String, Object> obj)
			throws IOException {
		if (part != null) {
			InputStream inputStream = part.getInputStream();
			try {
				db.saveAttachment(inputStream, URLEncoder.encode(fileName,"UTF-8"), part.getContentType(), id, (String) obj.get("_rev"));
			} finally {
				inputStream.close();
			}
		}
	}

	/*
	 * Create a document and Initialize with sample data/attachments
	 */
	private List<HashMap> initializeSampleData(Database db) throws Exception {

		long id = System.currentTimeMillis();
		String name = "Sample category";
		String value = "List of sample files";

		// create a new document
		System.out.println("Creating new document with id : " + id);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("name", name);
		data.put("_id", id + "");
		data.put("value", value);
		data.put("role", "");
		data.put("band", "");
		data.put("creation_date", new Date().toString());
		db.save(data);

		// attach the object
		HashMap<String, Object> obj = db.find(HashMap.class, id + "");

		// attachment#1
		File file = new File("Sample.txt");
		file.createNewFile();
		PrintWriter writer = new PrintWriter(file);
		writer.write("This is a sample file...");
		writer.flush();
		writer.close();
		FileInputStream fileInputStream = new FileInputStream(file);
		db.saveAttachment(fileInputStream, file.getName(), "text/plain", id + "", (String) obj.get("_rev"));
		fileInputStream.close();

		return db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse().getDocsAs(HashMap.class);

	}

	private Database getDB() {
		return CloudantClientMgr.getDB();
	}

}
