/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.dataservice.insertdataapi.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import org.bson.BSON;
import org.bson.BSONObject;
import org.bson.BasicBSONCallback;
import org.bson.types.BSONTimestamp;
import org.bson.types.Binary;
import org.bson.types.Code;
import org.bson.types.CodeWScope;
import org.bson.types.MaxKey;
import org.bson.types.MinKey;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.util.Base64Codec;

public class JSONCallbackTimeZone extends BasicBSONCallback {
    @Override
    public BSONObject create() {
        return new BasicDBObject();
    }
    @Override
    protected BSONObject createList() {
        return new BasicDBList();
    }
    /**
     * @deprecated instead, use {@link #arrayStart(String)} if {@code array} is true, and {@link #objectStart(String)} if {@code array} 
     * is false 
     */
    @Deprecated
    @Override
    public void objectStart(boolean array, String name) {
        _lastArray = array;
        super.objectStart(array, name);
    }


	 @Override
	    public Object objectDone() {
	        String name = curName();
	        Object o = super.objectDone();
	        if (_lastArray) {
	            return o;
	        }
	        BSONObject b = (BSONObject) o;
	        // override the object if it's a special type
	        if (b.containsField("$oid")) {
	            o = new ObjectId((String) b.get("$oid"));
	        } else if (b.containsField("$date")) {
	            if (b.get("$date") instanceof Number) {
	                o = new Date(((Number) b.get("$date")).longValue());
	            } else {
	                SimpleDateFormat format = new SimpleDateFormat(_msDateFormat);
	                format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
	                o = format.parse(b.get("$date").toString(), new ParsePosition(0));
	                if (o == null) {
	                    // try older format with no ms
	                    format = new SimpleDateFormat(_secDateFormat);
	                    format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
	                    o = format.parse(b.get("$date").toString(), new ParsePosition(0));
		                if (o == null) {
		                    // try timezone
		                    format = new SimpleDateFormat(_msDateFormat_TZ);
		                    format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
		                    o = format.parse(b.get("$date").toString(), new ParsePosition(0));
			                if (o == null) {
			                    // try older format timezone
			                    format = new SimpleDateFormat(_secDateFormat_TZ);
			                    format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
			                    o = format.parse(b.get("$date").toString(), new ParsePosition(0));
			                }
			                if (o == null) {
			                	// try isoDate with JAXB
			                	Calendar cal = DatatypeConverter.parseDateTime(b.get("$date").toString());
			                	o = cal.getTime();
			                }
		                    
		                }
	                    
	                    
	                }
	            }
	        } else if (b.containsField("$regex")) {
	            o = Pattern.compile((String) b.get("$regex"),
	                    BSON.regexFlags((String) b.get("$options")));
	        } else if (b.containsField("$ts")) { //Legacy timestamp format
	            Integer ts = ((Number) b.get("$ts")).intValue();
	            Integer inc = ((Number) b.get("$inc")).intValue();
	            o = new BSONTimestamp(ts, inc);
	        } else if (b.containsField("$timestamp")) {
	            BSONObject tsObject = (BSONObject) b.get("$timestamp");
	            Integer ts = ((Number) tsObject.get("t")).intValue();
	            Integer inc = ((Number) tsObject.get("i")).intValue();
	            o = new BSONTimestamp(ts, inc);
	        } else if (b.containsField("$code")) {
	            if (b.containsField("$scope")) {
	                o = new CodeWScope((String) b.get("$code"), (DBObject) b.get("$scope"));
	            } else {
	                o = new Code((String) b.get("$code"));
	            }
	        } else if (b.containsField("$ref")) {
	            o = new DBRef(null, (String) b.get("$ref"), b.get("$id"));
	        } else if (b.containsField("$minKey")) {
	            o = new MinKey();
	        } else if (b.containsField("$maxKey")) {
	            o = new MaxKey();
	        } else if (b.containsField("$uuid")) {
	            o = UUID.fromString((String) b.get("$uuid"));
	        } else if (b.containsField("$binary")) {
	            int type = (Integer) b.get("$type");
	            byte[] bytes = (new Base64Codec()).decode((String) b.get("$binary"));
	            o = new Binary((byte) type, bytes);
	        } else if (b.containsField("$numberLong")) {
	            o = Long.valueOf((String) b.get("$numberLong"));
	        }
	        if (!isStackEmpty()) {
	            _put(name, o);
	        } else {
	            o = !BSON.hasDecodeHooks() ? o : BSON.applyDecodingHooks( o );
	            setRoot(o);
	        }
	        return o;
	    }

	    private boolean _lastArray = false;
	    public static final String _msDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	    public static final String _secDateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	    public static final String _msDateFormat_TZ = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	    public static final String _secDateFormat_TZ = "yyyy-MM-dd'T'HH:mm:ssZ";
	    
	    
	    public static void main(String[] args) {
	    	String dataStr = "2014-05-13T17:08:58+0200";
//	    	String dataStr1 = "2014-05-13T15:08:58Z";
//	    	String dataStr2 = "2014-05-13T15:08:58.000Z";
//	    	String dataStr3 = "2014-05-13T17:08:58.000+0200";
//	    	String dataStr4 = "2014-05-13T15:08:58+00:00";
//	    	String dataStr5 = "2014-05-13T17:08:58+02:00";
//	    	String dataStr6 = "2014-05-13T15:08:58.000+00:00";
//	    	String dataStr7 = "2014-05-13T17:08:58.000+02:00";
	    	SimpleDateFormat format = new SimpleDateFormat(_secDateFormat_TZ);
            format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));

	    	System.out.println(format.format(parseDate(dataStr)));
//	    	System.out.println(format.format(parseDate(dataStr1)));
//	    	System.out.println(format.format(parseDate(dataStr2)));
//	    	System.out.println(format.format(parseDate(dataStr3)));
//	    	System.out.println(format.format(parseDate(dataStr4)));
//	    	System.out.println(format.format(parseDate(dataStr5)));
//	    	System.out.println(format.format(parseDate(dataStr6)));
//	    	System.out.println(format.format(parseDate(dataStr7)));
	    	 
		}
	    
	    private static Date parseDate(String dataStr)
	    {
	    	SimpleDateFormat format = new SimpleDateFormat(_msDateFormat);
            format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
            Date o = format.parse(dataStr.toString(), new ParsePosition(0));
            if (o == null) {
                // try older format with no ms
                format = new SimpleDateFormat(_secDateFormat);
                format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
                o = format.parse(dataStr.toString(), new ParsePosition(0));
	                if (o == null) {
	                    // try timezone
	                    format = new SimpleDateFormat(_msDateFormat_TZ);
	                    format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
	                    o = format.parse(dataStr.toString(), new ParsePosition(0));
		                if (o == null) {
		                    // try older format timezone
		                    format = new SimpleDateFormat(_secDateFormat_TZ);
		                    format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
		                    o = format.parse(dataStr.toString(), new ParsePosition(0));
		                }
		                if (o == null) {
		                	// try isoDate with JAXB
		                	Calendar cal = DatatypeConverter.parseDateTime(dataStr.toString());
		                	o = cal.getTime();
		                }
	                    
	                }
            }
            return o;
	    }

}
